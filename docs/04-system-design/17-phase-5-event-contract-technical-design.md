# Phase 5 Event Contract Technical Design

**Document ID:** AEOS-P05-D17  
**Phase:** 5 — Implementation (Technical Design Pre-requisite)  
**System:** AnverraGlobal  

## 1. Executive Summary
This document establishes the exact event contracts, payloads, and delivery semantics required by the Phase 5 Reporting module to consume operational data from the Policy and Commission modules, fulfilling the deferred design work explicitly mandated by D05 and the Phase 5 Architecture Decisions.

## 2. Source Authority
- **AEOS-P04-D05**: Event & Asynchronous Implementation Architecture (Section 16).
- **12-phase-5-technical-design.md**: Reporting Design (Section 9), Policy/Commission Domains (Sections 5 & 6), and Persistence Design (Section 8).
- **16-phase-5-architecture-decisions-approved.md**: Decision 1 (Atomic Consistency) and Decision 3 (Reporting Ownership).

## 3. Event Set Justification
To fulfill the Reporting module's requirement to aggregate statistics across all policy statuses, branches, and commissions, the following event set is baselined:
1. `PolicyCreatedEvent`
2. `PolicyActivatedEvent`
3. `PolicyDeactivatedEvent`
4. `PolicyReactivatedEvent`
5. `PolicyPremiumUpdatedEvent`
6. `CommissionConfiguredEvent`

**Justification for Minimum Set**:
Reporting must track policies in DRAFT, ACTIVE, and INACTIVE states, requiring creation and lifecycle transition events. Premium updates significantly alter financial aggregates and trigger commission resets, requiring `PolicyPremiumUpdatedEvent`. Commission configurations directly update commission statistics, requiring `CommissionConfiguredEvent`.

*Alternative Considered*: A generic `PolicySnapshotEvent` published on any state change.
*Trade-off*: A generic event reduces the number of event classes and simplifies the producer, but it loses specific domain intent (violating strict DDD semantics). The six-event explicit inventory is maintained to preserve rich domain semantics, even though the policy payloads are structurally identical snapshots.
*Classification*: **DESIGN DECISION**

## 4. Event Ownership & Publishing Use Cases
- **Policy Module**: Owns and publishes `PolicyCreatedEvent`, `PolicyActivatedEvent`, `PolicyDeactivatedEvent`, `PolicyReactivatedEvent`, and `PolicyPremiumUpdatedEvent`.
- **Commission Module**: Owns and publishes `CommissionConfiguredEvent`.
- **Consuming Module**: The `Reporting` module consumes all of the above.

*Classification*: **REQUIRED CONSEQUENCE**

## 5. Aggregate Identity & Versioning Semantics

### 5.1 Commission Aggregate Identity
The authoritative Phase 5 technical design (Section 8 - Persistence Design) defines the Commission database table with `policy_id` as its Primary Key. 
Therefore, **Commission is intentionally identified by `policyId` as its aggregate identity.** It does not possess an independent surrogate key.

For all Commission events, the `aggregateId` field in the event header WILL be the `policyId`.

### 5.2 Independent Aggregate Versions
Because Policy and Commission are distinct aggregates residing in separate modules, their optimistic locking versions are strictly independent, despite sharing the same identity value.
- **Policy Aggregate Version**: Tracked and incremented by the Policy module.
- **Commission Aggregate Version**: Tracked and incremented by the Commission module.

Reporting MUST track two distinct versions per `policyId` within its read model: `policyAggregateVersion` and `commissionAggregateVersion`.
A valid `CommissionConfiguredEvent` with a lower Commission version MUST NOT be discarded due to a higher Policy version. Stale-event detection operates strictly within the respective aggregate's version line.

*Classification*: **AUTHORITATIVE**

## 6. Premium → Commission RESET Transaction Boundary

The architectural exception for the Premium → Commission RESET is explicitly defined as an atomic cross-module transaction in `12-phase-5-technical-design.md` (Section 7).

- **Transaction Owner**: The Policy application layer (`PolicyManagementApplicationService`).
- **Transaction Boundary**: A single Spring `@Transactional` scope spanning the premium update use case.
- **Policy Update**: The Policy domain processes the premium change.
- **Commission Reset**: The Policy application service invokes the public `CommissionManagementService` contract to reset the commission to UNSET.
- **Event Registration**: Both domain events (`PolicyPremiumUpdatedEvent` and `CommissionConfiguredEvent` [UNSET]) are published to the Spring Modulith event registry within the identical database transaction.
- **Rollback Behavior**: If either the Policy update, the Commission reset, or the event registry insert fails (including optimistic locking failures on either aggregate), the entire transaction rolls back atomically. No state is changed, and no events are queued for publication.
- **Event Publication Behavior**: After the unified transaction successfully COMMITs to the database, the Spring Modulith registry asynchronously dispatches both events to the Reporting module.

*Classification*: **REQUIRED CONSEQUENCE** (The specific dual-event registration is derived from the atomic transaction exception defined in 12-phase-5-technical-design.md Section 7 combined with the D05 atomic publication rule).

## 7. Event Ordering & Consumer Invariant

Because the Spring Modulith registry dispatches events asynchronously after commit, the events may arrive in any order. The D05 architecture explicitly states:

> "Ordering Not Required (Default): Independent events or tasks execute concurrently. The architecture does not promise global ordering."

Because global ordering is not guaranteed, `PolicyPremiumUpdatedEvent` and `CommissionConfiguredEvent` may be processed by the Reporting module in either order. No additional cross-module ordering mechanism is introduced.

**REQUIRED Consumer Invariant**: Reporting MUST produce a correct eventual projection regardless of processing order.

**Guarantee Mechanism**: Independent aggregate version tracking guarantees this invariant, preventing one aggregate's event from incorrectly invalidating the other.
- If `PolicyPremiumUpdatedEvent` arrives first, Reporting updates the premium and `policyAggregateVersion`. The Commission read-model retains the old Commission until the second event arrives (eventual consistency).
- If `CommissionConfiguredEvent` (UNSET) arrives first, Reporting sets the Commission read-model to UNSET and updates the `commissionAggregateVersion`. The old Premium is retained until the Policy event arrives.
Because stale-event detection runs independently per aggregate version line, neither event erroneously discards the other. The shared `policyId` inherently correlates the two updates on the projection row. 

*Classification*: **REQUIRED CONSEQUENCE**

## 8. Payload Contracts & Sufficiency

### 8.1 Common Event Header Fields
- **Event Identity** (UUID, Required): Unique identity of the event.
- **Schema Version** (Integer, Required): Schema version (default 1).
- **Occurrence Timestamp** (Instant, Required): Timestamp of the business operation.
- **Aggregate Identity** (UUID, Required): The Policy Identity (for both Policy and Commission events).
- **Aggregate Version** (Long, Required): The optimistic locking version of the specific aggregate (Policy or Commission) at the time of publication.

### 8.2 Policy Events (`PolicyCreatedEvent`, `PolicyActivatedEvent`, `PolicyDeactivatedEvent`, `PolicyReactivatedEvent`, `PolicyPremiumUpdatedEvent`)
**Logical Payload Fields**:
- **Policy Number** (String, Required)
- **Customer Identity** (UUID, Required)
- **Agent A Identity** (UUID, Optional)
- **Agent B Identity** (UUID, Optional)
- **Branch Identity** (UUID, Optional)
- **Policy Status** (String, Required): "DRAFT", "ACTIVE", or "INACTIVE".
- **Premium Amount** (Decimal, Required)

**Sufficiency**: These events are 100% sufficient to update the Policy read models independently.

### 8.3 Commission Events (`CommissionConfiguredEvent`)
**Logical Payload Fields**:
- **Commission Status** (String, Required): "UNSET" or "CONFIGURED".
- **Commission Type** (String, Optional): "FIXED" or "PERCENTAGE". Null if Commission Status is UNSET.
- **Total Commission Value** (Decimal, Optional): Must be exactly `0` if configured as ZERO. Null if UNSET.
- **Agent A Share** (Decimal, Optional)
- **Agent B Share** (Decimal, Optional)

**Sufficiency**: `CommissionConfiguredEvent` is sufficient to update ONLY the *Commission fields* of the Reporting read model. Because the Commission module does not own organizational context, this event does NOT contain `branchId` or `status`. Reporting MUST rely on the previously ingested Policy events (stored in the Reporting read model) to provide the organizational context required for aggregating Commission statistics.

*Commission Semantics*: UNSET `commissionStatus` dictates exclusion from commission statistics. A `commissionStatus` of CONFIGURED with `totalCommissionValue = 0` dictates inclusion in statistics as a zero value.

*Classification*: **REQUIRED CONSEQUENCE**

## 9. Rebuild / Replay Semantics
- **Spring Modulith Guarantees**: Provides durable publication and at-least-once delivery for pending events. It does *not* provide an infinite persistent event history log.
- **Phase 5 Requirements**: Full historical replay from a persistent event log is explicitly out of scope for Phase 5.
- **Rebuild Mechanism**: If Reporting read models are lost or structurally changed, rebuilding the read models must be accomplished via an operational snapshot sync (initial load batch process) directly from the operational tables. Introducing Kafka, RabbitMQ, SQS, or a new event store is strictly prohibited.

*Classification*: **AUTHORITATIVE**

## 10. Remaining Ambiguities
- **Exact Java Event Class / JSON field names**: To be determined during implementation. *Classification*: **IMPLEMENTATION DETAIL**.
- **Reporting Read Model DB Schema mapping**: *Classification*: **IMPLEMENTATION DETAIL**.
- **Event tracking garbage collection**: How the consumer idempotency table is pruned over time. *Classification*: **IMPLEMENTATION DETAIL**.

There are no remaining architectural or technical-design ambiguities that block Phase 5/8 implementation of the Reporting events.

## 11. Safety Gate
Source code modified: NO
Dependencies modified: NO
Database modified: NO
Migrations modified: NO
Tests modified: NO
Implementation performed: NO

**FINAL STATUS:** D05 EVENT CONTRACT DESIGN COMPLETE — READY FOR HUMAN APPROVAL
