# Messaging Technology Blueprint

**Document ID:** AEOS-P03-D04  
**Version:** 3.0  
**Status:** Proposed  
**Phase:** 3 — Technology Selection & Architecture Enablement  
**System:** AnverraGlobal

---

# 1. Document Identity
**Title:** Messaging Technology Blueprint  
**ID:** AEOS-P03-D04

# 2. Purpose
The purpose of this document is to evaluate and select the messaging technology required to support asynchronous decoupling between business modules in the AnverraGlobal system. It ensures the chosen technology supports reliable business-fact publication while preserving the Modular Monolith architecture and strict module isolation established in Phase 2.

# 3. Scope
The scope of this document covers the evaluation of in-process asynchronous eventing versus external message brokers, delivery semantics, transactional consistency mechanisms (e.g., the dual-write problem), and Spring ecosystem integration. It explicitly does NOT decide messaging schemas, event payloads, specific topics/queues, routing keys, or domain workflows.

# 4. Architectural Context
Phase 2 established seven isolated business modules within a single deployable Modular Monolith. Phase 3 has established Java + Spring Modulith (D01), PostgreSQL (D02), and REST API Driving Adapters (D03). Asynchronous messaging is an architectural requirement to decouple producers from downstream consumers (e.g., Notification processing) without violating module boundaries or creating synchronous network bottlenecks.

# 5. Authoritative Constraints
- **Modular Monolith First:** The backend remains a single deployable Modular Monolith. Messaging must not turn modules into microservices.
- **Data Encapsulation:** Messaging must not permit cross-module persistence or bypass established module contracts.
- **Anti-Invention:** This document evaluates technology. It must not invent business events, message payloads, notification channels, or recipient domain models.
- **Infrastructure is not Business:** Messaging infrastructure is a technical mechanism, not a business module. It does not own business rules.

# 6. Messaging Requirement
Asynchronous messaging is an established architectural requirement. The system requires the ability for business modules to publish asynchronous business facts that other modules (such as Notification or future unresolved capabilities) can react to independently, decoupling the producer's transaction from the consumer's execution.

# 7. Messaging Responsibility Boundary
Messaging provides the technical transport, routing, and durability mechanisms for asynchronous events. It does not dictate business logic. Synchronous communication remains appropriate for immediate queries/commands within the monolith. Asynchronous messaging is strictly for downstream side effects, eventual-consistency workflows, and independent consumers.

# 8. Candidate Technologies
1. **Spring Modulith Durable In-Process Event Publication:** In-process JVM eventing backed by native Spring features.
2. **RabbitMQ:** An external AMQP-based message broker.
3. **Apache Kafka:** An external distributed event streaming platform.
4. **Redis Streams:** A log data structure within Redis.

# 9. Evaluation Criteria
Candidates are evaluated against:
- Modular Monolith compatibility.
- Asynchronous decoupling capability.
- Durability and Reliability.
- Delivery semantics and Ordering.
- Transactional consistency (resolving the dual-write problem).
- Scalability and Operational complexity.
- Spring Boot and Spring Modulith integration.
- AI-assisted development safety.

# 10. Spring Modulith Evaluation
- **Pros:** Native to the established backend baseline (D01). Zero additional operational infrastructure. Spring Modulith provides the selected in-process event-publication mechanism, supporting asynchronous event consumption within the Modular Monolith. Exact annotations, listener configuration, executor configuration, and implementation mechanics belong to the implementation/design phase. Resolves the dual-write problem via durable event publication backed by PostgreSQL.
- **Cons:** Keeps event consumption within a single JVM unless bridged to an external broker later.
- **Architectural Fit:** Exceptional fit for a Modular Monolith, providing decoupling without premature distributed complexity.

# 11. RabbitMQ Evaluation
- **Pros:** Mature AMQP support, excellent routing capabilities, durable queues.
- **Cons:** Introduces significant operational infrastructure. Forces serialization and network hops for inter-module communication within the same JVM.
- **Architectural Fit:** RabbitMQ provides mature broker-based asynchronous messaging and routing capabilities, but introduces external infrastructure that is not currently required by the single-deployable Modular Monolith.

# 12. Kafka Evaluation
- **Pros:** High throughput, persistent replayable event logs, partition-based scalability.
- **Cons:** Introduces significant operational infrastructure and requires specific JVM/cluster management.
- **Architectural Fit:** Kafka provides durable event-streaming and replay capabilities that exceed the current requirements of the Modular Monolith, while introducing additional infrastructure and operational complexity.

# 13. Redis Streams Evaluation
- **Pros:** Lightweight stream processing capabilities.
- **Cons:** Expands Redis beyond its standard architectural role (caching/session) into a mission-critical durable event store.
- **Architectural Fit:** Redis Streams provides asynchronous stream capabilities, but adopting it for authoritative business-event durability would expand the architectural role of Redis and therefore requires explicit justification against the existing Redis constraints. Currently, authoritative requirements do not explicitly permit expanding Redis into a primary persistent event store.

# 14. In-Process vs External Broker Comparison
- **Option A (In-Process Durable Eventing):** Keeps event publication and consumption within the same JVM process, utilizing the primary persistence store (PostgreSQL) for transactional durability. Low latency, zero infrastructure overhead.
- **Option B (External Broker):** Forces network serialization, introduces a new point of failure, and requires complex dual-write resolution. Solves problems (independent physical scaling, cross-language consumption) that do not currently exist in the single-deployable Modular Monolith.

# 15. Delivery Semantics
The architecture targets **at-least-once** delivery with idempotent consumers. At-most-once risks losing critical business facts. Exactly-once is technically complex and not established as a requirement. At-least-once, combined with idempotent consumers, provides the optimal balance of reliability and performance.

# 16. Durability
Business facts must not be lost if the application crashes between publication and consumption. The selected architecture must provide durable publication semantics sufficient to prevent loss of an accepted business fact when the originating transaction commits. Durable publication does not mean guaranteed successful consumer execution. Consumers may fail, retry, or recover independently.

# 17. Ordering
No global or per-aggregate ordering requirement has been established by authoritative business requirements at this stage. D04 therefore does not mandate a particular ordering guarantee. If a future business workflow requires ordering, its required ordering scope must be explicitly established during that workflow's design.

# 18. Retry / Failure Handling
The selected mechanism must support controlled recovery from transient and permanently unprocessable messages. Retry, backoff, dead-letter, quarantine, and replay strategies will be defined during implementation design according to the requirements of the specific asynchronous workflow.

# 19. Dead-Letter Handling
A dead-letter mechanism must be technically possible to handle unprocessable messages, but the specific topology, dead-letter queues, or recovery workflows remain deferred implementation details.

# 20. Transactional Consistency
The system must explicitly solve the dual-write problem:
`(Business transaction succeeds) + (Message publication fails) = Lost business fact.`
Mechanisms relying on "best effort 1PC" (publish after commit) are unsafe. Spring Modulith provides a durable event-publication mechanism that can persist event-publication state transactionally with the originating business transaction. This provides the foundation for reliable asynchronous publication. The exact persistence implementation and whether the project's implementation is formally designated as a Transactional Outbox belong to the implementation/design phase.

# 21. Dual-Write Guarantee
If the originating business transaction commits, the durable event-publication state must also be committed transactionally. Actual asynchronous consumer execution occurs later and may succeed, fail, retry, or recover independently.

# 22. Replay / Recovery
In-process eventing utilizing a persistent state provides the technical ability to inspect uncompleted events and recover them upon JVM restart. Long-term historical replay strategies remain deferred.

# 23. Scalability
The selected mechanism must support multiple application instances without compromising durability or creating unsafe duplicate processing. Because the architecture targets at-least-once delivery, asynchronous consumers must be designed for idempotent processing. Exact concurrency coordination and deployment behavior are implementation concerns.

# 24. Operational Complexity
An external broker introduces additional operational complexity, including infrastructure management, monitoring, security, upgrades, and network operations. Avoiding an external broker aligns with the simplicity goals of the Modular Monolith, as this additional complexity is not currently justified by the established requirements.

# 25. Spring Integration
The solution must integrate natively with Spring Boot. Spring Modulith integrates with the established Spring Boot ecosystem and provides durable event-publication capabilities that can use the approved PostgreSQL persistence foundation. The exact persistence adapter and configuration remain implementation concerns.

# 26. Security
If an external broker were selected, it would require TLS, authentication, and authorization. In-process eventing leverages the existing JVM security context, drastically reducing the security attack surface.

# 27. Observability
Integration with Micrometer is essential to trace an event from the producer thread, through the persistent publication state, into the asynchronous consumer thread.

# 28. Cost / Infrastructure Complexity
Deferring an external broker eliminates the infrastructure cost and DevOps overhead associated with managing Kafka or RabbitMQ clusters.

# 29. Structured Comparative Evaluation Matrix

| Criterion                 | Spring Modulith | RabbitMQ      | Apache Kafka  | Redis Streams |
| ------------------------- | --------------- | ------------- | ------------- | ------------- |
| Modular Monolith Fit      | Excellent       | Moderate      | Moderate      | Moderate      |
| Asynchronous Decoupling   | High            | Very High     | Very High     | High          |
| Durability                | High            | High          | Very High     | Moderate      |
| Transactional Consistency | Excellent       | Complex       | Complex       | Complex       |
| Delivery Semantics        | At-least-once   | At-least-once | At-least-once | At-least-once |
| Ordering                  | Unspecified     | Queue-based   | Partitioned   | Stream-based  |
| Replay / Recovery         | Local           | Queue-based   | Log-based     | Stream-based  |
| Scalability               | Moderate        | High          | Very High     | High          |
| Operational Complexity    | Very Low        | High          | Very High     | Moderate      |
| Spring Boot Integration   | Native          | Native        | Native        | Native        |
| Spring Modulith Alignment | Native          | Bridge Req.   | Bridge Req.   | Bridge Req.   |
| Infrastructure Cost       | None            | Moderate      | High          | Low/Moderate  |
| AI Implementation Safety  | High            | Moderate      | Moderate      | Moderate      |
| Future Evolution          | Extensible      | Extensible    | Extensible    | Extensible    |

*Rationale:* Spring Modulith naturally fits the Modular Monolith constraints with zero operational overhead and excellent dual-write resolution. RabbitMQ and Kafka provide capabilities that exceed current requirements while introducing infrastructure complexity. Redis Streams introduces complexity regarding Redis's approved architectural role.

# 30. Recommended Technology
**Recommended Messaging Stack:**
- **Architecture:** Spring Modulith durable asynchronous event publication within the Modular Monolith, using the approved PostgreSQL persistence foundation.
- **External Broker:** Not required at the current architectural stage.
- **Delivery Model:** Target at-least-once processing with idempotent consumers.
- **Future Evolution:** External broker adoption remains a future architectural decision triggered by concrete requirements.

# 31. Decision Rationale
Spring Modulith durable in-process asynchronous eventing is sufficient for the current Modular Monolith. An external broker is not required now because the system is a single deployable unit; introducing one would force unnecessary serialization, latency, and operational complexity. Durable event publication elegantly solves the dual-write problem by leveraging the approved D02 PostgreSQL baseline. This decision preserves strict module isolation by decoupling execution threads while targeting reliable at-least-once delivery with idempotent consumers.

# 32. Deferred Decisions
This document explicitly defers decisions regarding:
- Specific message brokers (deferred until an architectural requirement justifies one).
- Event names, payloads, routing keys, schemas, and topics.
- Outbox table schemas, columns, implementation classes, and repositories.
- Specific retry counts, DLQ implementation details, and backoff policies.
- Business workflows reacting to events.
- Client Web/Mobile technology (D05).
- Agent/Sub-Agent/Dealer/Partner domain models and capabilities.

# 33. Architectural Extensibility
The selected asynchronous mechanism does not prevent future adoption of an external broker if future requirements justify it. Such evolution should occur at the infrastructure/publication boundary while preserving module ownership and business/domain contracts. The future broker decision should be triggered by concrete architectural requirements such as independently deployed consumers, external consumers, cross-application integration, or service extraction.

# 34. Notification Architectural Scenario
*Conceptual Scenario:* An approved business operation produces a business fact. Notification may consume that fact asynchronously without the originating operation synchronously invoking Notification. The asynchronous architecture must remain extensible to future consumers without establishing those consumers, their contracts, or their domain models at this stage.

# 35. AI Implementation Guidance
Future coding AI MUST:
- Use only the messaging technology stack after this blueprint is formally baselined.
- AI must use the approved durable event-publication mechanism for business facts where the finalized implementation design requires reliable asynchronous publication.
- AI must not replace durable publication with best-effort post-transaction publication.
- Implement consumers to be idempotent to handle at-least-once delivery.
- Never write direct network integration (HTTP/REST) to bypass asynchronous eventing for downstream side effects.
- Never invent business events, queues, topics, schemas, or unresolved capability models not authorized by later design.

# 36. Traceability
This document traces directly to:
- AEOS-P03-D00, D01, D02, D03
- Phase 1 Engineering Constitution
- AEOS-P02-S01-D01 through D05
- AEOS-P02-S02-D00 through D07

# 37. Definition of Done
The blueprint is complete when:
- [x] Asynchronous messaging requirement is preserved.
- [x] Candidate technologies were evaluated.
- [x] In-process vs external broker was evaluated.
- [x] Transactional consistency was evaluated.
- [x] Durable event publication was addressed.
- [x] Delivery semantics were evaluated.
- [x] Ordering requirements were not invented.
- [x] Retry/DLQ implementation details remain deferred.
- [x] No event names were invented.
- [x] No event payloads were invented.
- [x] No queues/topics/routing keys were invented.
- [x] No notification channels were invented.
- [x] No unresolved capabilities were modeled.
- [x] No cross-module persistence was introduced.
- [x] No microservice architecture was introduced.
- [x] Redis architectural constraints were considered.
- [x] External broker was not prematurely introduced.
- [x] Future external-broker evolution remains possible.
- [x] AI constraints are explicit.
- [x] Traceability is complete.
