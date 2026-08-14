---
description: Convert approved requirements, architecture, technical design, API contracts, persistence design, and event design into a concrete implementation plan without modifying application code.
---

# Implementation Planning Workflow

## 1. Purpose

Create a concrete, implementation-ready plan from the approved Phase 5 baseline.

This workflow is READ-ONLY.

It MUST NOT:

- modify application source code
- modify tests
- modify dependencies
- create migrations
- modify configuration
- implement APIs
- implement events
- implement persistence
- authorize implementation

The output is a plan for human approval.

## 2. Required Baseline

Before planning, verify:

1. Repository discovery is complete.
2. Requirements are complete.
3. Architecture is approved.
4. Technical design is complete.
5. API contract is complete where applicable.
6. Persistence design is baselined.
7. Event architecture is baselined.
8. Security architecture is baselined.
9. No unresolved business decision blocks implementation.

If any prerequisite is missing:

    IMPLEMENTATION PLANNING BLOCKED — BASELINE INCOMPLETE

## 3. Required Skills

Use:

    .ai/skills/repository-discovery/SKILL.md
    .ai/skills/requirement-analysis/SKILL.md
    .ai/skills/architecture-analysis/SKILL.md
    .ai/skills/technical-design/SKILL.md
    .ai/skills/implementation-planning/SKILL.md

The implementation-planning skill is the primary planning authority for this workflow.

## 4. Repository Reality

Before producing tasks, inspect the current repository.

Determine:

- what already exists
- what is partially implemented
- what is missing
- existing package conventions
- existing dependency configuration
- existing testing conventions
- existing database configuration
- existing security configuration

Never assume documented future structures already exist.

Clearly distinguish:

    EXISTS TODAY
    MUST BE CREATED
    MUST BE MODIFIED
    MUST NOT BE CREATED

## 5. Plan Structure

The plan MUST be ordered by dependency.

For every task specify:

- task ID
- phase
- module
- layer
- exact file/path where reasonably determinable
- purpose
- requirements satisfied
- architectural authority
- technical-design authority
- dependencies
- implementation notes
- tests required
- verification criteria

## 6. Phase 5 Implementation Order

Unless repository evidence requires a different approved ordering, plan the work in this sequence:

### P0 — Dependency Preparation

Plan:

- Springdoc OpenAPI dependency
- AWS SDK S3 dependency for Cloudflare R2

Do NOT add:

- Kafka
- RabbitMQ
- AWS SQS

### P1 — Organization

Plan:

- Organization domain
- Dealer
- Branch
- Branch Admin
- Agent
- Data Entry
- OrganizationScope resolution
- persistence

Do not create an Organization HTTP API unless an authoritative requirement requires one.

### P2 — Policy

Plan:

- Policy aggregate
- lifecycle
- identity
- invariants
- application use cases
- inbound ports

### P3 — Commission

Plan:

- Commission aggregate
- Fixed/Percentage
- maximum 50%
- UNSET/ZERO/POSITIVE semantics
- Agent A/B allocation

### P4 — Policy / Commission Atomic Transaction

Plan:

    PolicyManagementApplicationService
        ↓
    Policy Premium Update
        ↓
    Commission RESET → UNSET

This is the only approved narrow cross-module transaction.

Do NOT plan:

- orchestration module
- distributed saga
- generic transaction coordinator

### P5 — Persistence

Plan:

- policy schema
- commission schema
- organization schema
- reporting schema
- Spring Data JDBC
- sequential numeric Flyway migrations
- UUID identifiers

Verify:

- no cross-schema physical FKs
- no BIGSERIAL
- no persistence annotations in domain
- no @Transactional in domain

### P6 — Authorization

Plan:

- authenticated Principal propagation
- OrganizationScope resolution
- resource authorization
- scope enforcement

Identity remains responsible for authentication/RBAC.

Organization remains responsible for hierarchy.

### P7 — Policy APIs

Plan the approved `/api/v1` endpoints:

- Policy create
- Policy retrieve
- Policy search
- Policy progressive update
- Policy resolution
- lifecycle activation
- lifecycle deactivation
- lifecycle reactivation

Controllers contain no business logic.

### P8 — Documents

Plan:

    policy.port.outbound.DocumentStoragePort

and:

    policy.adapter.outbound

for the Cloudflare R2 adapter.

Do NOT create:

    document module
    document/

Plan:

- upload
- download signed URL
- replacement
- removal
- 0..1 invariant

### P9 — Events

Plan only event contracts required by approved Reporting requirements.

For every event specify:

- event name
- producer
- consumer
- payload fields
- aggregate identifier
- aggregate version
- occurrence timestamp
- idempotency requirements
- out-of-order behavior

Do NOT invent event payloads when the approved Reporting requirements do not justify them.

### P10 — Reporting

Plan:

- Reporting projections
- Spring Modulith listeners
- consumer idempotency
- out-of-order handling
- UNSET exclusion
- ZERO inclusion
- statistics APIs

Reporting must not directly query operational module databases.

### P11 — Testing

Plan:

- domain unit tests
- application tests
- persistence integration tests
- API tests
- authorization tests
- event tests
- Reporting projection tests
- architecture tests
- transaction rollback tests

### P12 — Verification

Plan final verification against:

- requirements
- architecture
- persistence
- events
- APIs
- security
- tests
- dependency restrictions
- prohibited structures

## 7. Prohibited Structures

The plan MUST explicitly verify that implementation does NOT introduce:

    document/
    orchestration/
    agent/
    subagent/
    dealer/
    admin/

as unauthorized top-level business modules.

## 8. Critical Business Invariants

The plan MUST explicitly trace:

### Commission

    UNSET != ZERO

### Activation

    0 agents + UNSET    → allowed
    0 agents + ZERO     → allowed
    0 agents + POSITIVE → allowed

    1 agent + UNSET     → prohibited
    1 agent + ZERO      → allowed
    1 agent + POSITIVE  → allowed

    2 agents + UNSET     → prohibited
    2 agents + ZERO      → allowed
    2 agents + POSITIVE  → allowed

### Lifecycle

    DRAFT
    ACTIVE
    INACTIVE

Only.

### Premium Change

    Premium change
        →
    Commission RESET
        →
    UNSET

Atomic within the approved Policy application transaction.

## 9. Event Planning Safety

Do not silently resolve event-contract ambiguity.

If the Reporting requirements do not provide enough information to define a payload:

    STOP

Record:

- missing information
- affected task
- affected consumer
- human decision required

Do not invent a contract merely to make the plan look complete.

## 10. File-Level Traceability

Where possible, identify concrete paths such as:

    backend/src/main/java/com/anverraglobal/policy/...
    backend/src/main/java/com/anverraglobal/commission/...
    backend/src/main/java/com/anverraglobal/organization/...
    backend/src/main/java/com/anverraglobal/reporting/...

Also identify:

    backend/src/main/resources/db/migration/...

Do not create these files during planning.

## 11. Dependency Traceability

Every dependency addition must identify:

- why it is required
- which requirement/design requires it
- where it will be added
- how it will be verified

Unapproved dependencies become a planning blocker.

## 12. Human Decision Gate

The implementation plan MUST end with an explicit approval state.

Use:

    IMPLEMENTATION AUTHORIZED: NO

A completed plan is NOT authorization.

The workflow MUST NOT infer authorization from:

- "plan is complete"
- "looks good"
- "ready"
- "proceed with plan"

Implementation authorization requires an explicit human instruction authorizing implementation.

## 13. Plan Completion

The plan is complete only when an engineer or implementation agent can determine:

- what to implement
- where to implement it
- in what order
- why it is required
- what constraints apply
- what tests prove correctness
- what must not be changed

The final output MUST be exactly one of:

    IMPLEMENTATION PLAN COMPLETE — READY FOR HUMAN APPROVAL

or

    IMPLEMENTATION PLANNING BLOCKED — BASELINE INCOMPLETE

or

    IMPLEMENTATION PLANNING BLOCKED — HUMAN DECISION REQUIRED

or

    IMPLEMENTATION PLANNING BLOCKED — ARCHITECTURE CONFLICT
