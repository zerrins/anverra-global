# Module Implementation Architecture

**Document ID:** AEOS-P04-D02  
**Version:** 1.0  
**Status:** Baseline Candidate  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 3  
**Depends on:** Phase 1 Engineering Constitution · AEOS-P02-S01-D01 through D05 · AEOS-P02-S02-D00 through D07 · AEOS-P03-D00 through D05 · AEOS-P04-D00 · AEOS-P04-D01  

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D02 |
| **Title** | Module Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Authoring Position** | 3 |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |
| **Immediately Preceding Document** | AEOS-P04-D01 — Backend Implementation Architecture |

---

# 2. Purpose

This document establishes the authoritative internal implementation architecture for the seven approved business modules inside the AnverraGlobal Modular Monolith backend.

Where Phase 1 established constitutional principles, Phase 2 defined application and business capability boundaries, Phase 3 selected the technology stack, Phase 4 D00 established overall design governance, and Phase 4 D01 established the backend build runtime and package root (`com.anverraglobal`), this document (AEOS-P04-D02) defines how every business module is internally organized and isolated.

Specifically, this document:
1. Evaluates and formally resolves Open Decision **O3** (Inbound Adapter Directory & Package Naming).
2. Establishes the canonical package and layer structure applied uniformly across all seven approved business modules (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`).
3. Defines the exact responsibilities and boundary rules for `domain`, `application`, `port.inbound`, `port.outbound`, `adapter.inbound`, `adapter.outbound`, `contracts`, and `events`.
4. Reinforces unconditional domain purity, prohibiting framework or infrastructure imports inside the `domain/` package.
5. Defines the explicit separation between Ports (interfaces) and Adapters (implementations) under Hexagonal Architecture.
6. Establishes how Spring Modulith module verification and ArchUnit rules provide automated architectural verification of synchronous `contracts` and asynchronous `events` public surfaces during the build/test lifecycle while keeping internal implementation packages strictly private.
7. Guarantees strict protection of unresolved capabilities, prohibiting placeholder packages or premature infrastructure for non-approved modules.
8. Provides actionable governance rules for AI coding agents and software engineers implementing backend business modules.

> "D02 answers how each approved business module is internally organized while remaining isolated from every other module. It does not define specific business logic, entities, database tables, or API endpoints, which remain governed by authoritative business requirements and downstream design documents."

---

# 3. Scope

## 3.1 In Scope

The scope of this document is strictly limited to module-internal implementation architecture:
- Evaluation and resolution of Open Decision **O3** (Inbound Adapter Directory Naming: `interfaces/` vs. `adapter/inbound/`).
- Establishment of the canonical sub-package structure for business modules under `com.anverraglobal.<module>`.
- Internal layering model: Domain Layer, Application Layer, Inbound Ports, Outbound Ports, Inbound Adapters, Outbound Adapters, Contracts Boundary, and Events Boundary.
- Clear architectural distinction between Ports (abstractions/interfaces) and Adapters (framework/technology mechanisms).
- Enforcement of unconditional domain purity within `com.anverraglobal.<module>.domain`.
- Definition of the public synchronous surface (`contracts`) and public asynchronous surface (`events`) verified via Spring Modulith named interfaces.
- Inter-module dependency rules and strict prohibition of private package access across module boundaries.
- Prevention of circular module dependencies across all internal layers and public boundaries.
- Layer-specific representation boundaries (Domain Models vs. Application Models vs. API DTOs vs. Persistence Entities vs. Contract DTOs).
- Conceptual validation, transaction, persistence adapter, security, and platform integration boundaries within module structure.
- ArchUnit automated verification rules for module-internal layering and boundary compliance during the build/test lifecycle.
- AI implementation governance rules for backend module authoring.

## 3.2 Explicitly Out of Scope

The following concerns are explicitly outside the scope of D02 and belong to designated Phase 4 design documents or business requirement specifications:
- **O4:** OpenAPI implementation approach — assigned to **AEOS-P04-D04**.
- **O5:** OpenAPI client generation — assigned to **AEOS-P04-D07**.
- **O6:** PostgreSQL schema naming strategy — assigned to **AEOS-P04-D03**.
- **O7:** Event listener idempotency mechanism — assigned to **AEOS-P04-D05**.
- **O8:** DataSource configuration pattern — assigned to **AEOS-P04-D03**.
- **O9:** Shared vs independently generated client API types — assigned to **AEOS-P04-D07**.
- Specific business use cases, domain entities, value objects, aggregates, or domain fields.
- REST controller endpoint URLs, HTTP methods, payload schemas, or request validation annotations — assigned to **AEOS-P04-D04**.
- Spring Data JDBC repository implementations, Flyway SQL scripts, database tables, or column mappings — assigned to **AEOS-P04-D03**.
- Spring Modulith event listener retry policies, dead-letter queues, or transaction publication logs — assigned to **AEOS-P04-D05**.
- Spring Security filter chains, OAuth2/OIDC token configurations, or role hierarchy definitions — assigned to **AEOS-P04-D06**.
- Physical multi-module Maven POM layouts — assigned to **AEOS-P04-D10**.

---

# 4. Architectural Context

The AnverraGlobal system is executed as a single-process **Modular Monolith** running on Java 21 and Spring Boot 3 within a single JVM, as established by Phase 1 ([AEC-ARC-003](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-003-modular-monolith.md)), Phase 2 ([AEOS-P02-S01-D01](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md)), Phase 3 ([AEOS-P03-D01](file:///Users/shashank/Projects/anverra-global/docs/03-technology/01-backend-blueprint.md)), and Phase 4 D00 ([AEOS-P04-D00](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/00-phase-4-overview.md)).

D01 ([AEOS-P04-D01](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/01-backend-implementation-architecture.md)) established the foundational backend implementation framework:
- **Build Tool (O1):** Apache Maven (`pom.xml`)
- **Java Root Package (O2):** `com.anverraglobal`
- **Module Discovery Engine:** Spring Modulith (`org.springframework.modulith`)
- **Structural Guardrails:** ArchUnit (`com.tngtech.archunit`)
- **Shared Technical Infrastructure:** `com.anverraglobal.platform`

D02 builds directly upon D01 by standardizing the internal structure of the business modules located directly beneath `com.anverraglobal.<module>`.

```
com.anverraglobal/
├── AnverraApplication.java         (Spring Boot Main Entry Point)
├── platform/                       (Shared Technical Infrastructure — Zero Business Logic)
└── <module>/                       (One of the 7 Approved Business Modules)
    ├── domain/                     (Pure DDD Business Domain)
    ├── application/                (Use-Case Orchestration)
    ├── port/                       (Hexagonal Ports: Inbound & Outbound)
    ├── adapter/                    (Hexagonal Adapters: Inbound & Outbound)
    ├── contracts/                  (Synchronous Cross-Module Public API)
    └── events/                     (Asynchronous Cross-Module Public Integration Events)
```

---

# 5. Module Implementation Model

AnverraGlobal business modules integrate three complementary architectural paradigms:

1. **Domain-Driven Design (DDD):** Isolates pure domain logic inside bounded contexts, separating business invariants from application workflows and technical delivery mechanisms.
2. **Hexagonal Architecture (Ports and Adapters):** Enforces dependency inversion around the core domain and application layer. All external interactions pass through explicit Ports (interfaces) implemented or invoked by Adapters (technology constructs).
3. **Spring Modulith Bounded Modules:** Provides module discovery, module modeling, and automated architectural verification during the build/test lifecycle, verifying that internal implementation packages remain isolated.

```
                    ┌─────────────────────────────────────────────────────────┐
                    │               MODULE BOUNDARY (Spring Modulith)          │
                    │                                                         │
                    │   ┌─────────────────────────────────────────────────┐   │
                    │   │               INBOUND ADAPTERS                  │   │
                    │   │         (REST Controllers, Web Handlers)        │   │
                    │   └────────────────────────┬────────────────────────┘   │
                    │                            │ (uses)                     │
                    │                            ▼                            │
                    │   ┌─────────────────────────────────────────────────┐   │
                    │   │                  INBOUND PORTS                  │   │
                    │   │              (Use-Case Interfaces)              │   │
                    │   └────────────────────────▲────────────────────────┘   │
                    │                            │ (implements)               │
                    │                            │                            │
┌──────────────┐    │   ┌────────────────────────┴────────────────────────┐   │    ┌──────────────┐
│  SYNCHRONOUS │    │   │               APPLICATION LAYER                 │   │    │ ASYNCHRONOUS │
│  CONTRACTS   │◄───┼───┤            (Use-Case Orchestration)             │   │    │    EVENTS    │
│ (contracts/) │    │   └───────┬─────────────────────────────────┬───────┘   │    │  (events/)   │
└──────────────┘    │           │ (uses)                          │ (uses)    │    └──────────────┘
                    │           ▼                                 ▼           │
                    │   ┌───────────────┐                 ┌───────────────┐   │
                    │   │ DOMAIN LAYER  │                 │ OUTBOUND PORTS│   │
                    │   │(Business Core)│                 │ (SPI Interfaces)  │
                    │   └───────────────┘                 └───────▲───────┘   │
                    │                                             │           │
                    │                                             │(implements)
                    │   ┌─────────────────────────────────────────┴───────┐   │
                    │   │              OUTBOUND ADAPTERS                  │   │
                    │   │     (Spring Data JDBC, External Integrations)   │   │
                    │   └─────────────────────────────────────────────────┘   │
                    └─────────────────────────────────────────────────────────┘
```

---

# 6. Eight Approved Module Boundaries

## 6.1 Module Inventory

Phase 2 ([AEOS-P02-S01-D01](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md)) established seven approved business modules, and Phase 5 D16 formally introduced Organization Management, bringing the total to eight. D02 mandates that every business module exist strictly within its approved package path beneath `com.anverraglobal`:

| Module Name | Java Package Root | Business Category | Owning Phase 2 Document |
|---|---|---|---|
| **Identity & Access** | `com.anverraglobal.identity` | Platform Business | AEOS-P02-S02-D01 |
| **Customer Management** | `com.anverraglobal.customer` | Core Business | AEOS-P02-S02-D02 |
| **Product Catalogue** | `com.anverraglobal.product` | Core Business | AEOS-P02-S02-D03 |
| **Policy Lifecycle** | `com.anverraglobal.policy` | Core Business | AEOS-P02-S02-D04 |
| **Commission Management** | `com.anverraglobal.commission` | Core Business | AEOS-P02-S02-D05 |
| **Notification Management** | `com.anverraglobal.notification` | Supporting Business | AEOS-P02-S02-D06 |
| **Reporting & Analytics** | `com.anverraglobal.reporting` | Supporting Business | AEOS-P02-S02-D07 |
| **Organization Management** | `com.anverraglobal.organization` | Core Business | AEOS-P04-D16 |

## 6.2 Strict Module Boundary Rules

- **No Renaming:** Module package names must exactly match the canonical names above.
- **No Deletions or Mergers:** All eight modules are required and must remain distinct top-level packages under `com.anverraglobal`.
- **No Additional Business Modules:** Creating new top-level business module packages (e.g., `com.anverraglobal.billing` or `com.anverraglobal.claims`) is strictly prohibited in Phase 4.

## 6.3 Strict Protection of Unresolved Capabilities

Per AEOS-P04-D00 §24, the following business capabilities are **unresolved** and deferred to future architectural phases:

- Partner Management
- Proposal Management
- Document & KYC Management
- Administration

> [!CAUTION]
> **PROHIBITION:** Developers and AI coding agents MUST NOT create packages, sub-packages, modules, or directories representing unresolved capabilities. The creation of `agent/`, `subagent/`, `dealer/`, `partner/`, `proposal/`, `document/`, `kyc/`, or `admin/` packages anywhere in the codebase is a severe structural violation.

---

# 7. Internal Layering Model

Every approved business module must be structured using the following standardized package layout:

```text
com.anverraglobal.<module>/
├── domain/                  (Pure DDD business logic, invariants, domain entities)
├── application/             (Use-case orchestration services)
├── port/                    (Hexagonal ports)
│   ├── inbound/             (Inbound use-case interfaces)
│   └── outbound/            (Outbound infrastructure SPI interfaces)
├── adapter/                 (Hexagonal adapters)
│   ├── inbound/             (Inbound technology adapters for API boundary)
│   └── outbound/            (Outbound infrastructure adapters — Persistence & Clients)
├── contracts/               (Synchronous public collaboration surface & DTOs)
└── events/                  (Asynchronous public integration event definitions)
```

| Package Path | Layer Responsibility | Visibility / Accessibility |
|---|---|---|
| `domain/` | Domain entities, value objects, domain invariants, domain exceptions, domain events | Module-private |
| `application/` | Use-case orchestration services, application logic | Module-private |
| `port.inbound/` | Use-case interfaces defining application entry capabilities | Module-private |
| `port.outbound/` | Infrastructure SPI interfaces required by application use cases | Module-private |
| `adapter.inbound/` | Inbound technology adapters for the approved external API boundary (conventions owned by D04) | Module-private |
| `adapter.outbound/` | Outbound infrastructure adapters for persistence and client integrations (conventions owned by D03/D04) | Module-private |
| `contracts/` | Synchronous public interfaces, contract request/response DTOs | **Public Cross-Module Surface** |
| `events/` | Asynchronous public integration event classes/records | **Public Cross-Module Surface** |

---

# 8. Domain Layer

## 8.1 Package Location
`com.anverraglobal.<module>.domain`

## 8.2 Architectural Role
The Domain Layer is the innermost core of each business module. It encapsulates all business logic, domain entities, value objects, domain invariants, domain events, and domain exceptions.

## 8.3 Non-Negotiable Domain Purity Rule

> [!IMPORTANT]
> **CONSTITUTIONAL REQUIREMENT:** The `domain/` package of every module must remain unconditionally pure. It MUST NOT contain any framework or infrastructure dependencies.

Specifically, code inside `com.anverraglobal.<module>.domain` MUST NOT import or use:
- Spring Framework annotations or classes (`@Service`, `@Component`, `@Autowired`, `@Transactional`, etc.)
- Spring Boot classes
- Spring Data, JDBC, or JPA annotations (`@Table`, `@Id`, `@Column`, `@Entity`, etc.)
- HTTP, Servlet, or Web MVC imports (`HttpServletRequest`, `@RestController`, `ResponseEntity`, etc.)
- Spring Security annotations or types (`@PreAuthorize`, `Authentication`, `SecurityContext`, etc.)
- JSON processing annotations (`@JsonProperty`, `@JsonIgnore`, Jackson, Gson)
- Database-specific or driver-specific libraries
- `platform/` infrastructure packages

## 8.4 Permitted Domain Dependencies
The `domain/` package may depend ONLY on:
- Standard Java 21 runtime libraries (`java.lang`, `java.util`, `java.time`, `java.math`, etc.)
- Other classes residing strictly within its own `com.anverraglobal.<module>.domain` package

Domain code MUST NOT depend on `platform/`, Spring, persistence, HTTP, security, serialization, or other infrastructure. If a reusable technical utility is ever required by domain code, it must be separately evaluated for architectural compatibility rather than being automatically permitted by D02.

---

# 9. Application Layer

## 9.1 Package Location
`com.anverraglobal.<module>.application`

## 9.2 Architectural Role
The Application Layer orchestrates use-case execution. It sits directly between inbound adapters/ports and the internal domain layer.

The Application Layer is responsible for:
- Orchestrating use-case workflows by calling domain objects and outbound ports.
- Coordinating application-level workflow steps.
- Publishing domain events or invoking integration event mappers.
- Implementing the inbound port interfaces defined in `com.anverraglobal.<module>.port.inbound`.

Transaction orchestration must remain outside the domain layer. The concrete transaction boundary, propagation model, and persistence transaction configuration are defined by AEOS-P04-D03.

## 9.3 Application Layer Rules
- **No Infrastructure Dependencies:** The application layer must not depend on concrete persistence drivers, HTTP frameworks, or external integration SDKs.
- **Depends Inward Only:** The application layer depends on `domain` and `port` abstractions. It does not depend on `adapter`.
- **No Premature Business Use-Cases:** D02 establishes the application layer boundary structurally. Developers must not invent speculative use-case classes until required by authoritative functional specifications.

---

# 10. Inbound Ports

## 10.1 Package Location
`com.anverraglobal.<module>.port.inbound`

## 10.2 Architectural Role
Inbound Ports are Java interfaces that define the capabilities exposed by the application layer to primary (inbound) adapters.

Inbound Ports establish explicit, technology-agnostic use-case contracts within the module. They specify:
- What operations the application layer can execute.
- What input parameters (application command/query models) are required.
- What return types (application result models or domain objects) are produced.

## 10.3 Inbound Port Rules
- Inbound Ports are strictly Java `interface` definitions.
- Inbound Ports reside in `port.inbound`, separate from inbound adapters.
- Inbound Ports must not expose transport-specific types (e.g., HTTP request objects, Spring MVC `ResponseEntity`, or JSON nodes).
- Inbound Ports are implemented by application services in `com.anverraglobal.<module>.application`.

---

# 11. Inbound Adapter Decision Context

Phase 2 blueprints utilized conceptual diagrams containing `interfaces/` to represent external entry points. In Hexagonal Architecture and DDD literature, inbound entry points are variously named `interfaces/`, `adapter/inbound/`, or `entrypoints/`.

Phase 4 Open Decision **O3** requires formally resolving the inbound adapter directory and package naming convention across all seven business modules in the backend codebase.

---

# 12. O3 Evaluation

D02 evaluates the two primary candidate package structures for inbound adapters and module-internal entry points:

## 12.1 Candidate A — `interfaces/`

Under Candidate A, inbound adapters reside under a top-level package named `interfaces/`:

```text
com.anverraglobal.<module>/
├── domain/
├── application/
├── interfaces/             (Inbound Technology Adapters)
├── infrastructure/         (Outbound Persistence & External Adapters)
├── contracts/
└── events/
```

- **Advantages:**
  - Concise single-word package name.
  - Aligns with classic DDD terminology ("User Interface Layer").
  - Matches conceptual naming snippets in early Phase 2 documentation.

- **Disadvantages:**
  - **Naming Ambiguity:** The package name `interfaces/` can be semantically ambiguous because it may be interpreted as a general location for Java interfaces, including ports, contracts, repository abstractions, and other interface types. This weakens the explicit distinction between architectural Ports and concrete inbound Adapters.
  - **Divergent Hexagonal Nomenclature:** It decouples inbound adapters (`interfaces/`) from outbound adapters (`infrastructure/` or `outbound/`), breaking structural symmetry.
  - **AI Coding Agent Misinterpretation:** AI coding agents frequently misinterpret a package named `interfaces/` as a general dumping ground for all Java `interface` files across domain, application, and infrastructure layers, leading to package leakage and architectural degradation.

## 12.2 Candidate B — `adapter/inbound/` with `adapter/outbound/`

Under Candidate B, inbound adapters reside in `adapter.inbound`, outbound adapters reside in `adapter.outbound`, and ports reside in `port.inbound` and `port.outbound`:

```text
com.anverraglobal.<module>/
├── domain/
├── application/
├── port/
│   ├── inbound/             (Inbound Use-Case Interfaces)
│   └── outbound/            (Outbound Infrastructure SPI Interfaces)
├── adapter/
│   ├── inbound/             (Inbound Technology Adapters)
│   └── outbound/            (Outbound Infrastructure Adapters)
├── contracts/
└── events/
```

- **Advantages:**
  - **Explicit Hexagonal Architecture Alignment:** Perfectly reflects the Ports & Adapters architectural pattern mandated by Phase 1 ([AEC-ARC-004](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-004-hexagonal-architecture.md)).
  - **Architectural Naming Clarity:** Completely eliminates ambiguity regarding where Java interface types vs. concrete adapters reside.
  - **Structural Symmetry:** Standardizes adapter naming into clean `inbound` and `outbound` sub-packages beneath a single `adapter` root.
  - **Unambiguous Port Placement:** Explicitly isolates Java interfaces into `port/inbound` and `port/outbound`, preventing ports from mixing with adapter implementations.
  - **Automated Verification Precision:** Facilitates explicit, clean ArchUnit package predicates (e.g., `..adapter.inbound..` vs `..adapter.outbound..`).
  - **AI Coding Agent Comprehension:** Provides unambiguous, machine-verifiable package rules that prevent AI coding agents from placing concrete controllers or persistence classes into interface packages.

## 12.3 Evaluation Matrix

| Criterion | Candidate A (`interfaces/`) | Candidate B (`adapter/inbound/`) | Preferred Option |
|---|---|---|---|
| **1. Hexagonal Architecture Alignment** | Moderate (Asymmetric) | **High (Explicit Ports & Adapters)** | **Candidate B** |
| **2. Architectural Naming Clarity** | Moderate Ambiguity (`interfaces` name) | **High Clarity (`port/` vs `adapter/`)** | **Candidate B** |
| **3. Port vs. Adapter Distinction** | Confused (Ports mixed or displaced) | **Explicit (`port/` vs `adapter/`)** | **Candidate B** |
| **4. Architectural Symmetry** | Low (`interfaces` vs `infrastructure`) | **High (`adapter/inbound` vs `adapter/outbound`)** | **Candidate B** |
| **5. ArchUnit Rule Precision** | Moderate | **High (Exact package paths)** | **Candidate B** |
| **6. AI Coding Agent Safety** | Low (Risk of interface dumping) | **High (Explicit structural targets)** | **Candidate B** |
| **7. Cross-Module Consistency** | Moderate | **High (Uniform across all 7 modules)** | **Candidate B** |

---

# 13. O3 Decision

## 13.1 Formal Resolution

> [!IMPORTANT]
> **OPEN DECISION O3 IS FORMALLY RESOLVED:**  
> **Candidate B (`adapter/inbound/` and `adapter/outbound/`, paired with `port/inbound/` and `port/outbound/`)** is selected as the authoritative package and directory naming architecture for all business modules in AnverraGlobal.

Candidate A (`interfaces/`) is explicitly rejected.

## 13.2 Decision Rationale

Candidate B is selected because it provides an **unambiguous, symmetric, and machine-verifiable implementation of Hexagonal Architecture** that enforces explicit separation between Ports (abstractions) and Adapters (implementations).

1. **Elimination of Naming Ambiguity:** Candidate A (`interfaces/`) can be semantically ambiguous because it may be interpreted as a general location for Java interfaces, including ports, contracts, and repository abstractions. Candidate B provides absolute clarity: `port/` contains Java interfaces (ports); `adapter/` contains technical implementations (adapters).
2. **Hexagonal Precision:** Candidate B explicitly separates primary entry mechanisms (`adapter/inbound/`) from secondary infrastructure implementations (`adapter/outbound/`), establishing complete structural symmetry.
3. **AI Development Guardrails:** AI coding agents generate code with higher compliance when package paths explicitly communicate architectural intent. `com.anverraglobal.<module>.adapter.inbound` leaves zero doubt that HTTP entry-point classes belong inside it.

## 13.3 Decision Consequences

- All seven business modules MUST use the `adapter/inbound/` and `adapter/outbound/` package structure.
- The root package segment `interfaces/` is PROHIBITED across all backend business modules.
- `adapter.inbound` contains inbound technology adapters for the approved external API boundary. Concrete controller, DTO, routing, validation, and OpenAPI conventions are defined by AEOS-P04-D04.
- `adapter.outbound.persistence` is the designated location for persistence-related outbound adapters. The concrete repository, mapping, schema, and persistence implementation conventions are defined by AEOS-P04-D03.

---

# 14. Outbound Ports

## 14.1 Package Location
`com.anverraglobal.<module>.port.outbound`

## 14.2 Architectural Role
Outbound Ports are application-owned Java interfaces defined by the application layer to express dependencies on external infrastructure capabilities required by application use cases.

Examples of outbound port responsibilities include:
- Persistence SPIs (data retrieval and persistence abstractions required by application logic).
- External Integration SPIs (outbound notification delivery, third-party client abstractions).

## 14.3 Outbound Port Rules
- Outbound Ports are strictly Java `interface` definitions owned by the application layer.
- Outbound Ports MUST NOT contain framework-specific parameters or annotations (e.g., no Spring Data `@Query`, SQL strings, or HTTP headers).
- Outbound Ports are implemented by outbound adapters in `com.anverraglobal.<module>.adapter.outbound`.

---

# 15. Outbound Adapter Structure

## 15.1 Package Location
`com.anverraglobal.<module>.adapter.outbound`

## 15.2 Sub-Package Layout
To keep technology concerns organized, outbound adapters are structured into dedicated sub-packages:

```text
com.anverraglobal.<module>.adapter.outbound/
├── persistence/            (Persistence-Related Outbound Adapters)
└── client/                 (External System Integration Clients)
```

## 15.3 Architectural Role
Outbound Adapters implement the Outbound Ports defined in `port.outbound`. They contain technology-specific code required to interact with external databases, file systems, or third-party web services.

`adapter.outbound.persistence` is the designated location for persistence-related outbound adapters. The concrete repository, mapping, schema, and persistence implementation conventions are defined by AEOS-P04-D03.

## 15.4 Outbound Adapter Rules
- Outbound Adapters depend inward on `port.outbound` and `domain`.
- The domain and application layers NEVER depend on classes inside `adapter.outbound`.
- Database access and persistence implementations must remain completely isolated within `adapter.outbound.persistence`.

---

# 16. Contracts Boundary

## 16.1 Package Location
`com.anverraglobal.<module>.contracts`

## 16.2 Architectural Role
The `contracts/` package is a designated public module surface for synchronous cross-module collaboration.

When Module A requires synchronous data or services from Module B, Module A MUST interact exclusively with Java interfaces and Data Transfer Objects (DTOs) declared within Module B's `contracts/` package.

`contracts/` and `events/` are designated public module surfaces. Only intentionally approved types within those surfaces constitute supported cross-module contracts or integration-event representations. Internal `domain`, `application`, `port`, and `adapter` packages remain strictly private to the owning module.

```text
com.anverraglobal.<module>.contracts/
├── <Module>Contract.java            (Public Synchronous Service Interface)
└── dto/                             (Contract Request/Response DTOs & Records)
```

## 16.3 Strict Contracts Boundary Rules

> [!WARNING]
> **CROSS-MODULE ACCESS PROHIBITION:** Modules MUST NOT access another module's internal `domain/`, `application/`, `port/`, or `adapter/` packages. All synchronous cross-module access outside `contracts/` is illegal and detected via automated architectural verification using Spring Modulith and ArchUnit during the build/test lifecycle.

- **No Leaky Entities:** Contract interfaces and DTOs inside `contracts/` MUST NOT expose internal domain entities or persistence objects.
- **Immutable DTOs:** All request and response models in `contracts/dto/` MUST be implemented as immutable Java `record` types or final immutable classes.
- **Minimal Surface:** `contracts/` must contain only minimal, explicitly designed public APIs required for approved cross-module collaboration.

---

# 17. Spring Modulith Public Interfaces

## 17.1 Module Verification Engine
AnverraGlobal uses **Spring Modulith** (`org.springframework.modulith`) to provide module discovery, module modeling, and automated architectural verification during the build/test verification lifecycle, as established in D01.

By default, Spring Modulith treats top-level module sub-packages as private package boundaries. To expose a public API surface to other modules, Spring Modulith named interfaces or explicit package export rules must be applied.

## 17.2 Named Interface Configuration
Each business module explicitly designates `contracts/` and `events/` as its public surfaces using Spring Modulith `@NamedInterface` annotations or package conventions:

```java
// Conceptual package-info.java declaration for contracts
@org.springframework.modulith.NamedInterface("contracts")
package com.anverraglobal.<module>.contracts;
```

```java
// Conceptual package-info.java declaration for events
@org.springframework.modulith.NamedInterface("events")
package com.anverraglobal.<module>.events;
```

## 17.3 Why Java `public` Visibility Is Insufficient
In standard Java package rules, a `public` class inside `com.anverraglobal.policy.domain.Policy` can be imported by any class in the JVM if it resides on the classpath. 

Java `public` visibility alone does NOT enforce modular boundaries. AnverraGlobal relies on **Spring Modulith module verification** (`ApplicationModules.of(AnverraApplication.class).verify()`) executed during the build/test verification lifecycle and **ArchUnit structural tests** to verify that `public` classes inside private packages (`domain`, `application`, `adapter`) are not referenced by external modules. Spring Modulith provides architectural verification during test execution; it does not physically restrict JVM access at runtime to public Java classes.

---

# 18. Events Boundary

## 18.1 Package Location
`com.anverraglobal.<module>.events`

## 18.2 Architectural Role
The `events/` package is a designated public module surface for asynchronous cross-module collaboration.

The `events/` package contains immutable integration event definitions (Java `record` classes) that may be published when an approved business interaction requires asynchronous cross-module collaboration.

`contracts/` and `events/` are designated public module surfaces. Only intentionally approved types within those surfaces constitute supported cross-module contracts or integration-event representations.

```text
com.anverraglobal.<module>.events/
└── <Concept>IntegrationEvent.java   (Immutable Integration Event Record)
```

## 18.3 Events Boundary Rules
- **Integration Events Only:** `events/` contains cross-module *integration events*. Internal domain events used strictly within the module reside inside `domain/`.
- **Immutable Event Records:** All integration events MUST be Java `record` types containing plain Java primitive or immutable types.
- **Zero Consumer Coupling:** Event definitions in `events/` MUST NOT contain logic, handler interfaces, or assumptions about which modules consume the event.
- **Asynchronous Decoupling:** Event publication and listening mechanics are governed by AEOS-P04-D05.

---

# 19. Cross-Module Dependency Rules

## 19.1 Approved Dependency Directions

Cross-module collaboration is strictly restricted to approved public surfaces (`contracts/` and `events/`):

```text
┌──────────────────────────┐                    ┌──────────────────────────┐
│        MODULE A          │                    │        MODULE B          │
│                          │                    │                          │
│  ┌────────────────────┐  │                    │  ┌────────────────────┐  │
│  │ Application Service│──┼───────────────────►│  │ contracts/         │  │
│  └────────────────────┘  │  (Synchronous)     │  │  <Module>Contract  │  │
│                          │                    │  └────────────────────┘  │
│                          │                    │                          │
│  ┌────────────────────┐  │                    │  ┌────────────────────┐  │
│  │ Event Listener     │──┼───────────────────►│  │ events/            │  │
│  └────────────────────┘  │  (Asynchronous)    │  │  IntegrationEvent  │  │
│                          │                    │  └────────────────────┘  │
└──────────────────────────┘                    └──────────────────────────┘
```

## 19.2 Prohibited Cross-Module Dependencies

| Attempted Dependency Path | Status | Violation Reason |
|---|---|---|
| `moduleA` ➔ `moduleB.domain` | **PROHIBITED** | Direct domain coupling; breaks Bounded Context boundary |
| `moduleA` ➔ `moduleB.application` | **PROHIBITED** | Accessing private application service; bypassing `contracts/` |
| `moduleA` ➔ `moduleB.port` | **PROHIBITED** | Accessing private port interface; bypassing `contracts/` |
| `moduleA` ➔ `moduleB.adapter` | **PROHIBITED** | Direct adapter coupling; bypassing domain and contracts |
| `moduleA` ➔ `moduleB.database/table` | **PROHIBITED** | Data ownership violation ([AEC-ARC-011](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-011-data-ownership.md)) |

---

# 20. Circular Dependency Prevention

## 20.1 Constitutional Rule
Phase 1 ([AEC-ARC-007](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-007-module-boundaries.md)) mandates that **circular dependencies between modules are strictly prohibited**.

Direct circular dependencies (`Module A ➔ Module B ➔ Module A`) and transitive circular dependencies (`Module A ➔ Module B ➔ Module C ➔ Module A`) are disallowed regardless of whether they occur through synchronous contracts or asynchronous events.

## 20.2 Architectural Prevention & Verification
- **Permitted Collaboration Surfaces:** Synchronous and asynchronous cross-module collaboration are both permitted only through their respective approved public surfaces. The existence of `contracts/` or `events/` does not establish an actual business dependency between modules.
- **Acyclic Dependency Requirement:** The complete module dependency graph MUST remain acyclic. Any combination of synchronous contract dependencies and asynchronous event dependencies that produces a prohibited circular module dependency is a violation.
- **Automated Verification:** Spring Modulith `.verify()` and ArchUnit `slices().matching("com.anverraglobal.(*)..").should().beFreeOfCycles()` execute during the build/test verification lifecycle to verify cycle-free architecture.

---

# 21. Domain Purity Enforcement

## 21.1 Architectural Rationale
Domain purity guarantees that core business rules remain entirely independent of database choices, web frameworks, security implementations, or vendor software updates. This ensures long-term system maintainability and allows domain logic to be unit tested in milliseconds without Spring container startup.

## 21.2 ArchUnit Rule Specifications
Domain purity is automatically verified during the build/test lifecycle using ArchUnit:

```java
// Conceptual ArchUnit Rule for Domain Purity
ArchRule domainPurityRule = noClasses()
    .that().resideInAPackage("com.anverraglobal..domain..")
    .should().dependOnClassesThat().resideInAnyPackage(
        "org.springframework..",
        "jakarta.persistence..",
        "jakarta.servlet..",
        "com.fasterxml.jackson..",
        "org.hibernate..",
        "com.anverraglobal.platform.."
    );
```

---

# 22. DTO and Representation Boundaries

D02 establishes strict separation between representations across internal layers and module boundaries:

```text
┌──────────────────────┐      ┌──────────────────────┐      ┌──────────────────────┐
│     HTTP REQUEST     │      │   APPLICATION MODEL  │      │     DOMAIN MODEL     │
│   (adapter.inbound)  │─────►│    (port.inbound)    │─────►│       (domain)       │
│   RestRequestDTO     │      │  ExecuteCommand      │      │     DomainEntity     │
└──────────────────────┘      └──────────────────────┘      └──────────────────────┘
                                                                       │
                                                                       ▼
┌──────────────────────┐      ┌──────────────────────┐      ┌──────────────────────┐
│     CONTRACT DTO     │      │  INTEGRATION EVENT   │      │  PERSISTENCE ENTITY  │
│     (contracts)      │      │       (events)       │      │  (adapter.outbound)  │
│  ContractResponse    │      │ IntegrationRecord    │      │   RelationalEntity   │
└──────────────────────┘      └──────────────────────┘      └──────────────────────┘
```

1. **Inbound REST DTOs (`adapter.inbound`):** Transport representations for the external API boundary. Concrete DTO conventions are defined by AEOS-P04-D04.
2. **Application Command/Query Models (`port.inbound`):** Transport-agnostic inputs passed into inbound ports.
3. **Domain Models (`domain`):** Pure business entities and value objects containing business invariants. Never exposed over APIs or cross-module contracts.
4. **Persistence Entities (`adapter.outbound.persistence`):** Persistence representations. Concrete persistence mapping conventions are defined by AEOS-P04-D03.
5. **Contract DTOs (`contracts/dto`):** Immutable public DTO records exposed for synchronous cross-module calls.
6. **Integration Events (`events`):** Immutable public records published asynchronously to other modules.

---

# 23. Validation Ownership

D02 establishes three distinct validation tiers across the internal module layers:

```text
Inbound Input Payload ──► [Adapter Validation] ──► [Application Validation] ──► [Domain Validation]
```

## 23.1 Adapter Validation (Transport Level)
- **Location:** `com.anverraglobal.<module>.adapter.inbound`
- **Scope:** Adapter validation handles transport/representation constraints established by the API architecture. Concrete API validation mechanisms are defined by AEOS-P04-D04.

## 23.2 Application Validation (Workflow Preconditions)
- **Location:** `com.anverraglobal.<module>.application`
- **Scope:** Application validation enforces use-case preconditions established by authoritative functional requirements.

## 23.3 Domain Validation (Business Invariants)
- **Location:** `com.anverraglobal.<module>.domain`
- **Scope:** Domain validation enforces business invariants established by authoritative functional requirements. Guaranteed unconditionally by domain object constructors and state mutation methods.

---

# 24. Transaction Boundary

## 24.1 Transaction Placement
Transaction orchestration must remain outside the domain layer. The concrete transaction boundary, propagation model, and persistence transaction configuration are defined by AEOS-P04-D03.

## 24.2 Conceptual Boundary Rules
- Domain objects inside `domain/` do NOT manage transactions and MUST NOT use `@Transactional`.
- Detailed transaction propagation, isolation levels, DataSource configurations, and event publication transaction semantics remain governed by **AEOS-P04-D03** (Persistence) and **AEOS-P04-D05** (Events).
- **Phase 5 Exception (Cross-Module Transaction):** A narrow cross-module transactional exception is authorized exclusively for the atomic Policy Premium Update and Commission RESET → UNSET business operation, as explicitly approved in AEOS-P04-D16 (Phase 5 Architecture Decisions). No general cross-module transaction permission is granted.

---

# 25. Persistence Adapter Boundary

## 25.1 Architectural Role
`adapter.outbound.persistence` is the designated location for persistence-related outbound adapters. Persistence adapters implement the outbound persistence SPI ports declared in `com.anverraglobal.<module>.port.outbound`. The concrete repository, mapping, schema, and persistence implementation conventions are defined by AEOS-P04-D03.

## 25.2 Boundary Isolation Rules
- Domain code defines entity concepts. Domain code MUST NOT depend on database or relational annotations.
- Persistence-related adapters reside inside `adapter.outbound.persistence`.
- Database schema names, DataSource patterns, Flyway migration files, and table structures remain strictly owned by **AEOS-P04-D03**.

---

# 26. Security Boundary

## 26.1 Responsibility Isolation
Identity remains authoritative for identity and access responsibilities. D02 does not define authentication or authorization implementation placement. Detailed Spring Security integration and authorization placement are owned by AEOS-P04-D06.

## 26.2 Security Plumbing Boundary
Security infrastructure remains outside the pure domain layer and is governed by AEOS-P04-D06.

---

# 27. Platform Boundary

## 27.1 Technical Infrastructure Role
The `com.anverraglobal.platform` package (established in D01) provides technical, cross-cutting infrastructure (logging utilities, base exception structures, database drivers).

## 27.2 Non-Negotiable Platform Rules
- **Zero Business Logic:** `platform/` MUST NOT contain any business domain logic, business rules, or business entities.
- **Strict Inward Dependency Prohibition:** `platform/` MUST NOT depend on any of the seven business modules (`platform` ➔ `module` is PROHIBITED).
- Domain code inside `com.anverraglobal.<module>.domain` MUST NOT depend on `platform/`.
- Non-domain module layers (e.g., adapters or application services) may consume technical utilities from `platform/`, but must never move business code into `platform/`.

---

# 28. ArchUnit / Structural Verification

D02 mandates that module-internal package structures and boundary rules are automatically verified on every build via ArchUnit tests in `src/test/java/com/anverraglobal/architecture/`.

## 28.1 Mandatory ArchUnit Rule Categories for D02

1. **Module Package Integrity Rule:** Enforces that code resides only within approved top-level packages (`com.anverraglobal.platform` or one of the 7 approved modules).
2. **Domain Purity Rule:** Enforces zero framework/infrastructure/platform imports in `com.anverraglobal..domain..`.
3. **Hexagonal Dependency Rule:** Enforces the dependency direction:
   - `adapter.inbound` ➔ `port.inbound`
   - `application` ➔ `port.inbound`
   - `application` ➔ `domain`
   - `application` ➔ `port.outbound`
   - `adapter.outbound` ➔ `port.outbound`
   Concrete adapters must not be dependencies of the application or domain layers.
4. **Ports vs Adapters Rule:** Enforces that application services implement interfaces in `port.inbound`; inbound adapters depend on/use interfaces in `port.inbound`; and outbound adapters implement interfaces in `port.outbound`.
5. **Private Package Encapsulation Rule:** Enforces that no module can import classes from another module's `domain`, `application`, `port`, or `adapter` packages.
6. **Contracts & Events Exposure Rule:** Enforces that cross-module references are restricted strictly to approved types inside `contracts` and `events`.
7. **Unresolved Capability Rule:** Enforces that no package containing `agent`, `subagent`, `dealer`, `partner`, `proposal`, `document`, `kyc`, or `admin` exists.

---

# 29. AI Implementation Governance

When generating, modifying, or refactoring code within backend business modules, human software engineers and AI coding agents MUST strictly obey the following rules:

1. **Read Governing Docs:** Inspect D00, D01, and D02 before creating or modifying any package or class under `com.anverraglobal`.
2. **Preserve Seven Approved Modules:** Never create, rename, delete, or merge business modules. Operate strictly within the seven approved module roots.
3. **Respect Unresolved Capabilities Protection:** NEVER create packages, sub-packages, or classes for unresolved capabilities (`agent`, `subagent`, `dealer`, `partner`, `proposal`, `document`, `kyc`, `admin`).
4. **Obey Canonical Sub-Packages:** Every module file must reside in one of the approved package locations: `domain`, `application`, `port.inbound`, `port.outbound`, `adapter.inbound`, `adapter.outbound`, `contracts`, or `events`.
5. **Enforce Absolute Domain Purity:** Never add Spring, JDBC, JPA, HTTP, Jackson, Security, or `platform/` imports/annotations to any file in `domain/`.
6. **Enforce Ports vs Adapters Separation:** Place use-case interfaces in `port.inbound`, outbound SPIs in `port.outbound`, inbound adapters in `adapter.inbound`, and outbound adapters in `adapter.outbound`.
7. **Respect Synchronous Public Boundaries:** Never import another module's internal classes. Cross-module synchronous calls MUST go through approved types in the producing module's `contracts/` surface.
8. **Respect Asynchronous Public Boundaries:** Asynchronous cross-module communications MUST use approved integration event records in `events/`.
9. **Never Create Shared Business Modules:** Do not create `shared-domain/`, `common-business/`, or move business logic into `platform/`.
10. **Do Not Prematurely Resolve Open Decisions:** Do not invent database schemas (O6), event retry mechanisms (O7), DataSource setups (O8), or API client generation rules (O5/O9). Keep O4–O9 open.

---

# 30. Rejected Alternatives

## 30.1 Rejected Option 1: Candidate A (`interfaces/`) Package Naming
- **Reason for Rejection:** Can be semantically ambiguous (interpreted as a general dumping ground for Java interface types), breaks hexagonal naming symmetry, and leads AI coding agents to leak port interfaces and domain abstractions into web entry-point packages. Candidate B (`adapter/inbound/` and `adapter/outbound/`) was selected instead.

## 30.2 Rejected Option 2: Flat Module Package Layout (`com.anverraglobal.<module>.*`)
- **Reason for Rejection:** Placing all domain, application, and web classes directly in a single flat module package obscures dependency direction, violates Hexagonal Architecture, and makes automated ArchUnit rule enforcement impossible.

## 30.3 Rejected Option 3: Shared Business Common Package (`com.anverraglobal.shared`)
- **Reason for Rejection:** Violates Phase 1 ([AEC-ARC-008](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-008-business-capability-ownership.md)) Business Capability Ownership. Creates an unmaintainable dumping ground of shared business models that couples modules together.

## 30.4 Rejected Option 4: Direct Entity Sharing Across Modules
- **Reason for Rejection:** Violates Phase 1 ([AEC-ARC-011](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-011-data-ownership.md)) Data Ownership and Spring Modulith boundary rules. Modules must interact solely through approved DTOs defined in `contracts/` or event records in `events/`.

---

# 31. Architectural Consequences

## 31.1 Positive Consequences
- **Complete Structural Uniformity:** Every business module follows the exact same 8-package template, making the codebase instantly navigable for developers and AI agents.
- **Unambiguous Hexagonal Boundaries:** Clean separation between ports (interfaces) and adapters (implementations) simplifies testing and framework upgrades.
- **Automated Architectural Verification:** Spring Modulith and ArchUnit provide automated architectural verification during the build/test lifecycle to prevent architectural drift.
- **Zero-Framework Domain Testing:** Pure domain entities and rules can be unit tested without starting Spring or initializing databases.

## 31.2 Operational Consequences & Requirements
- **Mapping Overhead:** Requires mapping between transport DTOs, application commands, domain models, persistence entities, and contract DTOs across layer boundaries.
- **Strict Package Governance:** Developers and AI agents must follow disciplined package selection rules when adding new classes.

---

# 32. Risks and Mitigations

| Identified Risk | Severity | Mitigation Mechanism |
|---|---|---|
| **Accidental Framework Import in Domain** | High | Automated ArchUnit domain purity rule executed during `mvn test` and CI pipeline builds. |
| **Bypassing Contracts Surface for Cross-Module Access** | Critical | Spring Modulith `ApplicationModules.verify()` test fails verification build if private package is referenced. |
| **Creation of Unresolved Capability Packages** | High | ArchUnit package rule explicitly fails build if `agent`, `dealer`, `partner`, etc. packages are detected. |
| **DTO Proliferation Across Layers** | Moderate | Clear DTO boundary matrix (§22) specifying exact responsibilities for each DTO tier. |

---

# 33. Deferred Decisions

D02 explicitly preserves the open status of downstream design decisions, as governed by AEOS-P04-D00 §27:

| Open Decision ID | Description | Assigned Document | Status |
|---|---|---|---|
| **O4** | OpenAPI implementation approach | **AEOS-P04-D04** | **OPEN** |
| **O5** | OpenAPI client generation approach | **AEOS-P04-D07** | **OPEN** |
| **O6** | PostgreSQL schema naming strategy | **AEOS-P04-D03** | **OPEN** |
| **O7** | Event listener idempotency mechanism | **AEOS-P04-D05** | **OPEN** |
| **O8** | DataSource configuration pattern | **AEOS-P04-D03** | **OPEN** |
| **O9** | Shared vs independently generated client API types | **AEOS-P04-D07** | **OPEN** |

Additionally, D02 defers all business entity fields, database schemas, API endpoint paths, Flyway migrations, and event payload schemas to their respective downstream documents.

---

# 34. Traceability

D02 maintains complete traceability to prior authoritative documents:

## 34.1 Phase 1 — Engineering Constitution
- [AEC-ARC-002 — Domain-Driven Design](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-002-domain-driven-design.md)
- [AEC-ARC-003 — Modular Monolith](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-003-modular-monolith.md)
- [AEC-ARC-004 — Hexagonal Architecture](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-004-hexagonal-architecture.md)
- [AEC-ARC-006 — Dependency Direction](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-006-dependency-direction.md)
- [AEC-ARC-007 — Module Boundaries](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-007-module-boundaries.md)
- [AEC-ARC-008 — Business Capability Ownership](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-008-business-capability-ownership.md)
- [AEC-ARC-009 — Explicit Contracts](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-009-explicit-contracts.md)
- [AEC-ARC-010 — Event-Driven Collaboration](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-010-event-driven-collaboration.md)
- [AEC-ARC-011 — Data Ownership](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-011-data-ownership.md)

## 34.2 Phase 2 — System & Module Blueprints
- [AEOS-P02-S01-D01 — System Blueprint](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md)
- [AEOS-P02-S01-D02 — Repository Architecture](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/02-repository-architecture.md)
- [AEOS-P02-S01-D03 — Application Boundaries](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/03-application-boundaries.md)
- [AEOS-P02-S01-D04 — Architectural Boundaries](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/04-architectural-boundaries.md)
- [AEOS-P02-S01-D05 — Blueprint Traceability](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/05-blueprint-traceability.md)
- AEOS-P02-S02-D01 through D07 (Business Module Blueprints)

## 34.3 Phase 3 — Technology Blueprints
- [AEOS-P03-D00 — Phase 3 Technology Overview](file:///Users/shashank/Projects/anverra-global/docs/03-technology/00-phase-3-overview.md)
- [AEOS-P03-D01 — Backend Technology Blueprint](file:///Users/shashank/Projects/anverra-global/docs/03-technology/01-backend-blueprint.md)
- [AEOS-P03-D02 — Persistence Technology Blueprint](file:///Users/shashank/Projects/anverra-global/docs/03-technology/02-persistence-blueprint.md)
- [AEOS-P03-D03 — API & Transport Technology Blueprint](file:///Users/shashank/Projects/anverra-global/docs/03-technology/03-api-blueprint.md)
- [AEOS-P03-D04 — Messaging Technology Blueprint](file:///Users/shashank/Projects/anverra-global/docs/03-technology/04-messaging-blueprint.md)
- [AEOS-P03-D05 — Client Technology Blueprint](file:///Users/shashank/Projects/anverra-global/docs/03-technology/05-client-blueprint.md)

## 34.4 Phase 4 — System Design Documents
- [AEOS-P04-D00 — Phase 4 System Design Overview](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/00-phase-4-overview.md)
- [AEOS-P04-D01 — Backend Implementation Architecture](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/01-backend-implementation-architecture.md)

---

# 35. Definition of Done

This document (AEOS-P04-D02) is complete when:
1. Open Decision **O3** is formally evaluated and resolved (`adapter/inbound/` and `adapter/outbound/` selected; `interfaces/` rejected).
2. The canonical package and layer structure for all seven business modules is defined without ambiguity.
3. Domain purity rules for `com.anverraglobal.<module>.domain` are explicitly stated and reinforced.
4. The separation between Ports (interfaces) and Adapters (implementations) under Hexagonal Architecture is defined.
5. Synchronous `contracts/` and asynchronous `events/` cross-module surfaces are specified alongside Spring Modulith named interface configuration.
6. The strict protection of unresolved capabilities is reinforced, prohibiting placeholder packages or modules.
7. Explicit ArchUnit rule categories are specified for automated structural verification during the build/test lifecycle.
8. Complete AI implementation governance rules for module structuring are recorded.
9. All 36 required sections are present and fully articulated.
10. No source code, Java classes, persistence schemas, API endpoints, or downstream documents (D03+) were created.

---

# 36. Final Decision / Baseline Status

## 36.1 Document Status
This document is authored and recorded as **Baseline Candidate**.

## 36.2 Decision Register Summary

| Decision ID | Subject | Selected Option | Status |
|---|---|---|---|
| **O1** | Build Tool | Apache Maven | **RESOLVED (D01)** |
| **O2** | Java Root Package | `com.anverraglobal` | **RESOLVED (D01)** |
| **O3** | Inbound Adapter Package Naming | `adapter/inbound/` & `adapter/outbound/` | **RESOLVED (D02)** |
| **O4** | OpenAPI Implementation Approach | — | **OPEN (D04)** |
| **O5** | OpenAPI Client Generation | — | **OPEN (D07)** |
| **O6** | PostgreSQL Schema Naming | — | **OPEN (D03)** |
| **O7** | Event Listener Idempotency | — | **OPEN (D05)** |
| **O8** | DataSource Configuration Pattern | — | **OPEN (D03)** |
| **O9** | Shared vs Generated Client Types | — | **OPEN (D07)** |

## 36.3 Stop Rule & Next Step
- **Authoring Position 3 Complete:** AEOS-P04-D02 is fully authored.
- **Do NOT proceed to AEOS-P04-D03.**
- **Do NOT create Java classes, database schemas, or API endpoints.**
- **Awaiting formal architectural review before proceeding to D03 (Persistence Implementation Architecture).**
