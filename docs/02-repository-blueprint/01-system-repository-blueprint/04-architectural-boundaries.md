# Architectural Boundaries

**Document ID:** AEOS-P02-S01-D04  
**Version:** 1.0  
**Status:** Baseline  
**Phase:** 2 — Application & Repository Blueprint  
**Stage:** 1 — System & Repository Blueprint  
**System:** AnverraGlobal

---

# 1. Purpose

This document establishes the internal architectural boundaries of the business capability modules within the AnverraGlobal backend system.

Where Document 02 established the high-level repository structure (where modules reside) and Document 03 established application-level boundaries (Web vs. Mobile vs. Backend), this document answers:

> **How are backend business modules internally structured, how do they enforce encapsulation, and how do dependencies flow within them?**

This document serves as the bridge between high-level repository organization and low-level code implementation, codifying the principles of Hexagonal Architecture and Domain-Driven Design established in the Engineering Constitution.

---

# 2. Architectural Paradigm

The AnverraGlobal backend strictly adheres to two core architectural paradigms to ensure long-term maintainability and technology independence:

1. **Hexagonal Architecture (Ports and Adapters)**: 
   Business logic is entirely isolated from external technologies, frameworks, databases, and user interfaces. The domain is the center of the architecture.
   
2. **Domain-Driven Design (DDD) Alignment**: 
   The core of every module is modeled around business capabilities rather than data structures. The domain remains pure and focused entirely on solving business problems.

---

# 3. Canonical Module Structure

Every backend business module must adhere to the canonical internal directory structure defined by the Constitution (AEC-REP-003). 

This structure applies to every capability module (e.g., `identity/`, `customer/`, `policy/`) residing in `backend/src/modules/`:

- **`domain/`**: The pure business core. Contains Aggregates, Entities, Value Objects, Domain Services, and Domain Events. The domain and/or appropriate inward-facing architectural boundary may define the abstractions required for outbound dependencies. Their precise placement is an implementation decision governed by the selected architecture. This layer has absolutely no knowledge of frameworks, databases, or external systems.
- **`application/`**: The orchestration layer. Contains Use Cases, Commands, Queries, and Application Services. It coordinates domain objects to execute business transactions but does not contain core business rules.
- **`interfaces/`**: The inbound adapters. Translates external requests (e.g., messaging consumers, external interfaces) into application commands or queries.
- **`infrastructure/`**: The outbound adapters. Provides technical implementations for the ports and abstractions defined in the domain or application layer (e.g., database repositories, 3rd-party API clients, framework configurations).
- **`contracts/`**: The public surface of the module. Contains explicit module contracts and DTOs that other modules are permitted to consume. Note that backend module contracts are distinct from application-facing API contracts. Exact packaging and transport of contracts is deferred.
- **`events/`**: Contains integration events intended for approved consumers. Not every internal or domain event is public.
- **`configuration/`**: Module-specific configuration and wiring required by the selected implementation architecture.
- **`tests/`**: Module-specific automated tests, including applicable unit, integration, and contract tests.
- **`documentation/`**: Internal module documentation, ADRs, and sequence diagrams.

---

# 4. Dependency Direction

Dependency management is strictly enforced to protect the Domain from technological churn. Dependencies must always flow inward toward stable business abstractions. Conceptually:

- Inbound adapters depend inward toward application/domain abstractions.
- Application depends on domain.
- Outbound infrastructure/adapters implement abstractions defined toward the inside.
- Domain remains independent of frameworks and external technical infrastructure.

The Domain must never depend on external technical frameworks or outbound infrastructure implementations.

---

# 5. Encapsulation and Inter-Module Boundaries

Business modules are strictly encapsulated, cohesive business capability units that are independently understandable and evolvable. A module boundary is a hard architectural contract.

## 5.1 Public Surface
A module may expose only the following to other modules:
- Explicit APIs defined in `contracts/`.
- Published integration events defined in `events/`.
- Public application services.

## 5.2 Private Internals and Data Ownership
The `domain/`, `application/`, and `infrastructure/` layers are strictly private to the module. Each business module strictly owns its own business data and persistence.

- A module may access its own authoritative persistence.
- A module must not directly read another module's persistence.
- A module must not directly modify another module's persistence.
- Cross-module collaboration occurs exclusively through approved contracts, commands, queries, or events.

---

# 6. Module Collaboration Strategies

When modules must interact to fulfill a business requirement, they utilize one of two collaboration strategies:

1. **Synchronous Collaboration (Explicit Contracts)**: 
   Used when one module requires an immediate response from another to proceed (e.g., `Commission` asking `Policy` for a policy's premium amount). This is achieved through explicit module contracts, application services, commands, or queries, where appropriate.

2. **Asynchronous Collaboration (Event-Driven)**: 
   The preferred method when a module only needs to react to a completed business action (e.g., `Notification` reacting to a `Customer` registration). The owning module publishes an immutable, past-tense business fact. Consumer modules react independently without the publisher's knowledge.

---

# 7. Architectural Anti-Patterns

The following are strict violations of the architectural boundaries established in this document:

- **Database-Centric Architecture**: Polluting the `domain/` layer with ORM annotations or database-specific logic.
- **Fat Controllers**: Placing authoritative business rules, validations, or complex workflow logic inside `interfaces/` adapters instead of the `domain/` or `application/` layers.
- **Smart Repositories**: Implementing business rules or domain logic inside `infrastructure/` database adapters.
- **Leaky Modules**: One module bypassing explicit contracts to directly query another module's database schema.
- **Circular Dependencies**: Module A depending on Module B, while Module B simultaneously depends on Module A.

---

# 8. Deferred Decisions

This document defines structural and logical boundaries. The following implementation decisions are explicitly deferred:

- Specific ORM, database technologies, database schemas, and physical database topologies.
- Specific event broker technologies and messaging formats.
- Specific dependency injection frameworks.
- Specific API protocols, transports, and application-facing API contract formats.
- Specific package management tooling for enforcing boundaries.

These will be resolved in Phase 3 Technology decisions.

---

# 9. Definition of Done

This Architectural Boundaries document is considered complete and baseline when:

- [x] Internal module layers map exactly to the canonical structure defined in AEC-REP-003.
- [x] Hexagonal Architecture principles are conceptually defined.
- [x] Dependency direction is strictly inward, protecting the Domain.
- [x] Inter-module encapsulation rules are defined (public contracts vs. private internals).
- [x] Cross-module database access is explicitly prohibited, establishing strict data ownership.
- [x] Collaboration strategies (Synchronous Contracts vs. Asynchronous Events) are established.
- [x] Architectural anti-patterns are clearly documented.
- [x] No specific technology, framework, deployment topology, or implementation decisions have been invented.
- [x] The document aligns perfectly with Documents 01, 02, and 03.

---

*This document serves as the authoritative blueprint for the internal construction of AnverraGlobal business modules. It must be read in conjunction with the Engineering Constitution's architectural principles.*
