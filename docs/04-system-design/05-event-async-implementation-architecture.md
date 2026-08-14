# Event & Asynchronous Implementation Architecture

**Document ID:** AEOS-P04-D05  
**Version:** 1.0  
**Status:** Baseline Candidate  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 6  
**Depends on:** Phase 1 Engineering Constitution · AEOS-P02-S01-D01 through D05 · AEOS-P02-S02-D00 through D07 · AEOS-P03-D00 through D05 · AEOS-P04-D00 · AEOS-P04-D01 · AEOS-P04-D02 · AEOS-P04-D03 · AEOS-P04-D04

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D05 |
| **Title** | Event & Asynchronous Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Authoring Position** | 6 |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |
| **Immediately Preceding Document** | AEOS-P04-D04 — API & Transport Implementation Architecture |

---

# 2. Purpose

This document establishes the authoritative asynchronous event and messaging architecture for the AnverraGlobal Modular Monolith backend.

Where Phase 1 established event-driven principles, Phase 3 selected Spring Modulith and PostgreSQL as the technology baseline, and D03 established the persistence architecture, this document (AEOS-P04-D05) determines how asynchronous communication is implemented.

Specifically, this document:
1. Formally resolves Open Decision **O7** (Event Listener Idempotency Mechanism).
2. Formally evaluates and resolves Open Decision **O10** (Messaging Architecture / External Broker Strategy).
3. Defines the transactional event consistency model using Spring Modulith's durable publication registry.
4. Establishes the technical capability for asynchronous notification processing and agent/sub-agent coordination without inventing their business models.
5. Defines retry, recovery, ordering, and delivery semantics.
6. Defines the boundary and criteria for externalizing events to an external message broker (Kafka/RabbitMQ).

---

# 3. Scope

## 3.1 In Scope
- Event-driven communication architecture.
- Synchronous vs asynchronous module collaboration criteria.
- Application/domain event publication model.
- Spring Modulith event mechanisms.
- Durable event publication strategy.
- Asynchronous listener execution model.
- Event transactionality.
- Event retry/recovery strategy.
- Event idempotency architecture (O7 resolution).
- Event ordering requirements.
- Failure/dead-letter strategy.
- Event observability.
- Event retention principles.
- External broker evaluation (Kafka vs RabbitMQ/AMQP vs no external broker).
- Broker introduction criteria (O10 resolution).
- Notification processing architecture (technical capability).
- Agent/sub-agent asynchronous coordination architecture (technical capability).
- Event contract governance at the architectural level.

## 3.2 Explicitly Out of Scope
- Specific business event payloads (e.g., fields of `PolicyIssuedEvent`).
- Specific event names for unresolved capabilities.
- Notification templates, provider selections, or notification content.
- Agent/Sub-Agent business workflows or prompt structures.
- API endpoints.
- Database schemas outside the approved event-publication mechanism.
- Client WebSocket/SSE implementation.
- Security/authorization rules.
- Business-domain algorithms.
- Specific queues/topics for individual business features.

---

# 4. Architectural Context

The AnverraGlobal system executes as a single-process **Modular Monolith** running on Java 21, Spring Boot 3, and Spring Web MVC within a single JVM, as established by Phase 1 (AEC-ARC-003) and Phase 3 (AEOS-P03-D01).

D03 established PostgreSQL as the authoritative persistence store. This directly influences D05 because durable event publication can leverage the existing transactional persistence architecture via Spring Modulith's JDBC-backed event publication registry.

Messaging technology cannot redefine module boundaries. A module boundary remains strictly enforced regardless of whether an event is delivered in-process or via an external broker. 

---

# 5. Asynchronous Architecture Model

D05 establishes the following conceptual model for all asynchronous processing:

```text
Business Operation
       │
       ▼
Domain / Application Event
       │
       ▼
Transactional Event Publication (Spring Modulith)
       │
       ├───────────────┐
       │               │
       ▼               ▼
In-Process        Externalization
Listener          (if justified / required)
       │               │
       ▼               ▼
Module B         Broker (Kafka/RabbitMQ)
                 External Consumer
```

There is a strict distinction between:
- **In-process asynchronous event:** Module A → Spring Modulith → Module B listener
- **Externalized asynchronous event:** Module A → Spring Modulith event → Externalization / Outbox mechanism → Broker → External consumer

Spring Modulith provides the abstraction for both, allowing the architecture to evolve.

---

# 6. Event-Driven Communication Principles

1. **Decoupling:** Producers publish events without knowledge of consumers.
2. **Immutability:** An event is a historical record of something that happened. It cannot be altered once published.
3. **Module Ownership:** The module where the business transaction occurs owns the event definition and the publication contract.
4. **Resilience:** Consumer failure must not compromise the integrity of the producer's completed business transaction.

---

# 7. Synchronous vs Asynchronous Decision Rules

Not all cross-module communication is asynchronous. D05 establishes the following boundaries:

- **Synchronous Collaboration (via `contracts/`):** Used when the calling module requires an immediate response to complete its own business transaction (e.g., Module A must ask Module B if a customer is eligible before saving).
- **Asynchronous Collaboration (via `events/`):** Used when the calling module has completed its authoritative state change and is merely notifying the rest of the system (e.g., Module A saved the customer and emits `CustomerRegisteredEvent`).

---

# 8. Spring Modulith Event Architecture

Spring Modulith provides the native baseline for asynchronous event handling within the JVM:
- It intercepts standard Spring `ApplicationEvent` publications.
- It provides `@ApplicationModuleListener` as a dedicated abstraction over `@Async @TransactionalEventListener`.
- It executes listeners in isolated transactions, decoupling the consumer's transaction from the producer's.

This is the default mechanism for all inter-module event communication unless externalization criteria are explicitly met.

---

# 9. Durable Event Publication

In-memory event publication is insufficient for enterprise business processing because a JVM crash between publication and listener execution results in lost events.

D05 mandates **Durable Event Publication** backed by Spring Modulith's Event Publication Registry.
- Spring Modulith's event publication infrastructure persists durable publication state in the approved technical persistence storage.
- This registry tracks incomplete, completed, and failed publications.
- On JVM restart, incomplete publications can be resubmitted.

---

# 10. Transactional Event Consistency

To prevent the dual-write problem, event publication must be transactionally consistent with the business state change. 

We must avoid:
`Database transaction → COMMIT → Publish message → (X failure)`

D05 establishes the following transactional event mechanisms:
- **Internal events:** Transactional durable event publication using Spring Modulith's Event Publication Registry.
- **Future broker externalization:** Transactional Outbox externalization using Spring Modulith's outbox externalization support.

For internal durable events, the publication flow is:

```text
Business Transaction
       │
       ├── Domain State modification
       │
       └── Event Publication Record (INSERT into technical persistence)
              │
              ▼
            COMMIT (Atomic)
              │
              ▼
       Async Publication (Dispatched to listeners or broker)
```

If the business transaction rolls back, the event is never published. If the transaction commits, the event publication record is durably recorded and becomes eligible for asynchronous delivery and retry/recovery according to the configured publication and recovery mechanisms.

---

# 11. Asynchronous Listener Architecture

Listeners are placed in the consuming module's application or inbound adapter layer. 
- They must be annotated with `@ApplicationModuleListener`.
- They execute asynchronously in their own transaction scope.
- They consume events defined in the producing module's `events/` package.
- They map the integration event to their own internal application commands.

---

# 12. O7 Idempotency Evaluation & Resolution

Open Decision **O7** requires defining the event listener idempotency mechanism. Because delivery semantics are "at-least-once," listeners may receive the same event multiple times due to retry or network failure.

## 12.1 Candidate Mechanisms
1. **Natural Idempotency:** The business operation is inherently idempotent (e.g., `UPDATE status = 'ACTIVE' WHERE id = 1`).
2. **Consumer State Tracking:** The consumer maintains a dedicated table of processed Event IDs.
3. **Idempotency Key / Distributed Lock:** A caching layer (e.g., Redis) prevents concurrent processing.

## 12.2 Formal Resolution of O7
> [!IMPORTANT]
> **OPEN DECISION O7 IS FORMALLY RESOLVED:**
> **Consumer State Tracking** is established as the primary architectural idempotency mechanism.

Every asynchronous consumer MUST provide an explicit idempotency guarantee. Consumer State Tracking using processed Event IDs is the default mechanism for operations that are not naturally idempotent or otherwise protected by an equivalent authoritative uniqueness/state constraint.

```text
Idempotency
├── Natural / state-based idempotency
├── Authoritative uniqueness constraint
└── Consumer Event-ID tracking ← default
```

This guarantees that duplicate delivery does not result in duplicate business execution.

---

# 13. Retry & Failure Recovery

D05 establishes the failure handling architecture:
1. **Transient Failures:** Handled via automated local retries (e.g., Spring `@Retryable` or Modulith automatic resubmission) with exponential backoff.
2. **Permanent Failures:** If an event remains incomplete or repeatedly fails according to the configured publication/recovery policy, it remains eligible for operational recovery or, where an external broker is used, broker-specific dead-letter handling.
3. **Recovery:** Operations personnel can trigger controlled resubmission through approved operational management mechanisms.

*(Note: Exact values like "3 retries, 5 seconds" belong to operational configuration, not D05).*

---

# 14. Delivery Semantics

D05 formally mandates **At-Least-Once Delivery + Idempotent Consumers** for all asynchronous processing. 
The architecture does not attempt to engineer "exactly-once" distributed processing, which is practically unattainable and operationally brittle.

---

# 15. Event Ordering

D05 distinguishes between causal ordering and independent execution:
- **Ordering Not Required (Default):** Independent events or tasks execute concurrently. The architecture does not promise global ordering.
- **Ordering Required:** Established only where a strict business or process requirement demands it. In such cases, sequence numbers, causal state machine tracking, or ordered message queues must be explicitly modeled.

---

# 16. Event Contract Governance

Events are part of a module's public surface (`events/`). They are:
- **Owned** by their producing module.
- **Immutable** once published.
- **Versioned** when backward-incompatible structural evolution requires it.
- **Independent** of persistence entities (no `@Entity` classes in payloads).
- **Independent** of HTTP DTOs (no JSON request/response classes in payloads).

At a minimum, an event contract should carry:
- Event Identity (UUID)
- Occurrence Timestamp
- Payload containing necessary domain identifiers

*(D05 must not define actual business payloads).*

**Phase 5 Clarification (Reporting Analytics):**
As established in AEOS-P04-D16, operational modules (e.g., Policy, Commission) must provide governed events to the `Reporting` module for analytics and read-model generation. The exact event contracts, payloads, and persistence requirements remain future D03/D05 technical-design work.

---

# 17. Event vs Command Model

D05 explicitly distinguishes the semantic intent of asynchronous messages:
- **Event:** A statement of fact. "Something happened in the past." (e.g., `PolicyIssuedEvent`). The publisher does not care who listens.
- **Command:** A request for action. "Please perform this action." (e.g., `SendNotificationCommand`). The publisher expects a specific processor to handle the task.

This distinction is critical for defining Agent/Sub-Agent orchestration patterns, where tasks often behave semantically as commands dispatched to async workers.

---

# 18. Internal Module Event Communication

Internal module events (Category 1) leverage Spring Modulith in-process listeners when:
- The consumer is another backend module within the monolith.
- The event does not require independent infrastructure scaling.
- In-process delivery is sufficient.

This is the architectural default for inter-module collaboration.

---

# 19. Notification Processing Architecture

AnverraGlobal requires asynchronous processing for notifications. D05 establishes the technical capability pipeline without inventing templates or rules:

`Business Event ──► Notification Processing Component/Module Boundary ──► Notification Task ──► External Channels`

The architectural requirement is that the originating business transaction (e.g., policy creation) must NOT synchronously wait for the external SMS/Email provider. It publishes a durable event, and the designated notification-processing component/module processes the delivery asynchronously.

---

# 20. Agent/Sub-Agent Async Coordination Capability

AnverraGlobal requires coordination for Agent and Sub-Agent workflows. D05 establishes the asynchronous messaging architecture to support this:

`Parent Task Event ──► Async Dispatcher ──► Worker A / Worker B / Worker C`

*(Note: Worker A/B/C are conceptual asynchronous consumers only and do not represent approved AnverraGlobal Agent/Sub-Agent domain entities or workflows.)*

The messaging architecture is fully capable of dispatching asynchronous work and receiving completion notifications without synchronous HTTP coupling.
*(D05 does NOT establish Agent entities, Sub-Agent workflows, prompt structures, or orchestration algorithms. It only provides the messaging infrastructure).*

---

# 21. External Broker Evaluation (O10)

Open Decision **O10 (Messaging Architecture / External Broker Strategy)** requires formal evaluation of whether to introduce Kafka or RabbitMQ, or to rely exclusively on Spring Modulith in-process events.

## 21.1 Candidate A — Spring Modulith Durable In-Process Events
- **Advantages:** No external broker. Strong Modular Monolith alignment. Lower operational complexity. Leverages existing PostgreSQL infrastructure.
- **Risks:** Backend remains the processing boundary. Scaling consumers requires scaling the entire monolith. Limited replay capability compared to dedicated event logs.

## 21.2 Candidate B — RabbitMQ / AMQP
- **Advantages:** Strong queue distribution. Excellent for task-oriented processing and work queues (like notifications).
- **Risks:** Additional infrastructure. Another failure domain. Routing complexity.

## 21.3 Candidate C — Apache Kafka
- **Advantages:** High throughput. Durable event streams with infinite retention potential. Strong replay model. Consumer-group scalability.
- **Risks:** Significant operational complexity. Often excessive for a single modular-monolith deployment.

## 21.4 Candidate D — Hybrid Evolutionary Architecture
The architecture begins with **Spring Modulith + PostgreSQL-backed durable publication** for all internal communication, while explicitly utilizing Spring Modulith's **event externalization** abstractions to selectively bridge events to Kafka/RabbitMQ only when external consumers or distinct independent scaling requirements emerge.

---

# 22. Kafka Evaluation

Kafka is evaluated as overly complex for the *initial* phase of a Modular Monolith where all consumers are internal. It introduces broker cluster, partition, replication, consumer-group, offset, and operational management overhead that is not justified by current internal module-to-module requirements.

---

# 23. RabbitMQ/AMQP Evaluation

RabbitMQ is well-suited for task distribution (e.g., Agent coordination, Notifications). However, introducing it immediately would add infrastructure complexity that is not currently justified by the established requirements.

---

# 24. Broker Adoption Criteria (Resolution of O10)

> [!IMPORTANT]
> **OPEN DECISION O10 IS FORMALLY RESOLVED:**
> **Candidate D — Hybrid Evolutionary Architecture** is established as the external broker strategy.

Use durable asynchronous publication through the approved event infrastructure. **Spring Modulith/PostgreSQL** is the default internal asynchronous mechanism for in-process consumers, with an explicit externalization path for workloads that genuinely require broker characteristics.

An external broker (Kafka/RabbitMQ) is justified when one of the following criteria is explicitly met:
1. **External Consumers:** Systems outside the Modular Monolith boundary require real-time subscription to events.
2. **Independent Scaling:** A specific asynchronous processor requires physical scaling independent of the monolith JVM.
3. **Throughput Saturation:** Event publication volume exceeds the efficient capacity of the PostgreSQL event registry.
4. **Workload Isolation:** A workload requires an independently managed execution environment, queue-based back-pressure, or worker scaling characteristics that are impractical within the monolith process.

When these criteria are met, Spring Modulith's event externalization features will be used to selectively publish specific event types to the broker, without rewriting the producer's internal domain logic.

---

# 25. Externalization / Outbox Architecture

When O10 broker adoption criteria are met in the future, the architecture will use the Transactional Outbox pattern implemented via Spring Modulith's externalization support:
`Business Transaction ──► Modulith Event Registry ──► Modulith Externalizer ──► Message Broker`
This preserves transactional consistency between the originating business transaction and the durable event publication record while providing a governed mechanism for subsequent externalization to the message broker.

---

# 26. Dead-Letter & Recovery Architecture

Events that consistently fail processing must not block the system indefinitely.
- Failed events are flagged in the Spring Modulith event registry.
- Operations personnel can monitor the registry and trigger manual resubmission.
- If an external broker is eventually introduced, standard Dead Letter Queue (DLQ) topologies will be applied.

---

# 27. Observability & Traceability

D05 mandates asynchronous observability:
- **Metrics:** Publication success/failure, processing latency, and retry counts must be exposed via Micrometer.
- **Trace Context:** Correlation and tracing context must be preserved across asynchronous boundaries using the application's approved observability/tracing mechanism. The implementation must support W3C Trace Context compatibility where applicable.
- **Registry Monitoring:** The state of the durable event publication registry (Incomplete/Completed) must be continuously monitored.

---

# 28. Testing Strategy

1. **Unit Tests:** Independent verification of event producers and consumer mapping logic.
2. **Integration Tests:** Verification of the complete `Transaction ──► Event Publication ──► Async Listener` flow using `@ApplicationModuleTest`.
3. **Failure Tests:** Simulating duplicate delivery to verify O7 idempotency enforcement, and simulating exception throws to verify retry logic.

---

# 29. AI Development Governance

When generating asynchronous implementations, AI agents MUST:
1. Never invent business event payloads.
2. Never publish events directly from REST controllers.
3. Never use `@Entity` database persistence classes as event payloads.
4. Never introduce HTTP communication between internal modules as a substitute for events.
5. Never assume every event requires an external broker like Kafka.
6. Never create external queues/topics without explicit architectural authorization.
7. Never ignore O7 consumer state idempotency requirements.
8. Never invent Agent/Sub-Agent workflow rules or prompts.
9. Always use `@ApplicationModuleListener` instead of standard `@EventListener`.

---

# 30. Deferred Decisions Register

| Decision ID | Description | Assigned Document | Status |
|---|---|---|---|
| **O1** | Build Tool (Apache Maven) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O2** | Java Root Package (`com.anverraglobal`) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O3** | Inbound Adapter Package (`adapter/inbound/`) | **AEOS-P04-D02** | **RESOLVED (D02)** |
| **O4** | OpenAPI implementation approach | **AEOS-P04-D04** | **RESOLVED (D04)** |
| **O5** | OpenAPI client generation approach | **AEOS-P04-D07** | **OPEN** |
| **O6** | PostgreSQL schema naming strategy | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O7** | Event listener idempotency mechanism | **AEOS-P04-D05** | **RESOLVED (D05)** |
| **O8** | DataSource configuration pattern | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O9** | Shared vs independently generated client API types | **AEOS-P04-D07** | **OPEN** |
| **O10** | Messaging Architecture / External Broker Strategy | **AEOS-P04-D05** | **RESOLVED (D05)** |

---

# 31. Traceability

## 31.1 Phase 1 — Engineering Constitution
- `docs/01-constitution/03-architecture-principles/AEC-ARC-003-modular-monolith.md`
- `docs/01-constitution/03-architecture-principles/AEC-ARC-010-event-driven-collaboration.md`

## 31.2 Phase 2 — System & Module Blueprints
- `docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md`
- AEOS-P02-S02-D01 through D07

## 31.3 Phase 3 — Technology Blueprints
- `docs/03-technology/04-messaging-technology-blueprint.md`

## 31.4 Phase 4 — System Design Documents
- `docs/04-system-design/00-phase-4-overview.md`
- `docs/04-system-design/03-persistence-implementation-architecture.md`
- `docs/04-system-design/04-api-transport-implementation-architecture.md`

---

# 32. Definition of Done & Final Baseline Status

## 32.1 Definition of Done
This document is complete when:
1. Asynchronous architecture is formally defined.
2. Synchronous vs asynchronous boundaries are explicit.
3. Spring Modulith event architecture is evaluated and established.
4. Durable publication is established using the event registry.
5. Transaction/event consistency is defined using Spring Modulith's transactional durable event publication mechanism, with the Transactional Outbox pattern reserved for approved external broker externalization.
6. **O7** idempotency is formally resolved (Consumer State Tracking).
7. Delivery semantics (At-Least-Once) are defined.
8. Retry/recovery and ordering principles are defined.
9. Notification and Agent/Sub-Agent asynchronous coordination capability is established without inventing business models.
10. Kafka and RabbitMQ are objectively evaluated.
11. **O10** external broker adoption strategy is formally resolved (Hybrid Evolutionary).
12. Externalization and outbox architecture are defined.
13. Failure/dead-letter and observability architecture are defined.
14. AI governance is established (no invented payloads, no unwarranted brokers).
15. All 32 required sections are present and complete.

## 32.2 Final Status
This document is authored and recorded as **Baseline Candidate**.

## 32.3 Stop Rule & Next Step
- **Authoring Position 6 Complete:** AEOS-P04-D05 is fully authored.
- **Do NOT proceed to AEOS-P04-D06.**
- **Do NOT create Java classes, API endpoints, or database schemas.**
- **Awaiting formal architectural review before proceeding to D06 (Security Implementation Architecture).**
