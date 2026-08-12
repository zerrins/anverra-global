# Phase 4 System Design Overview

**Document ID:** AEOS-P04-D00
**Version:** 1.0
**Status:** Baseline Candidate
**Phase:** 4 — System Design & Implementation Planning
**System:** AnverraGlobal
**Depends on:** Phase 1 Engineering Constitution · AEOS-P02-S01-D01 through D05 · AEOS-P02-S02-D00 through D07 · AEOS-P03-D00 through D05

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D00 |
| **Title** | Phase 4 System Design Overview |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Authored after** | Phase 4 Pre-Authoring Analysis (AEOS-P04-PRE-ANALYSIS) |
| **Authorizes** | D01 — Backend Implementation Architecture |

---

# 2. Purpose

This document is the governance document for Phase 4 of the AnverraGlobal engineering program.

Phase 4 — System Design & Implementation Planning — translates three prior phases of architectural work into an implementation-ready system design:

```
Phase 1  —  Engineering Constitution
               ↓
Phase 2  —  Application & Repository Blueprint
               ↓
Phase 3  —  Technology Selection & Architecture Enablement
               ↓
Phase 4  —  System Design & Implementation Planning
               ↓
Phase 5+ —  Implementation
```

D00 does not solve the implementation-design problems that belong to D01–D11. It establishes the governance rules, responsibility boundaries, open-decision register, document inventory, authoring order, AI implementation constraints, and exit criteria that every subsequent Phase 4 document must follow.

Every Phase 4 document (D01–D11) must read and comply with this document before authoring begins.

---

# 3. Phase 4 Objectives

Phase 4 has the following objectives, to be accomplished across D00–D11:

1. **Establish implementation-ready backend architecture.** Define the Java/Spring Boot/Spring Modulith implementation structure, internal module layering, package conventions, boundary enforcement mechanisms, and ArchUnit rule categories without inventing business logic or domain models.

2. **Establish implementation-ready module structure.** Define the canonical internal structure for each of the seven approved business modules, including how each module enforces the public surface defined by Phase 2 and the technology capabilities provided by Phase 3.

3. **Establish implementation-ready persistence architecture.** Formally decide the physical isolation strategy for module persistence, the schema/DataSource configuration pattern, the Flyway migration organization, and the transaction-boundary ownership convention.

4. **Establish implementation-ready API architecture.** Define the REST adapter pattern, DTO placement convention, OpenAPI implementation approach, validation layer separation, and error response architecture.

5. **Establish implementation-ready asynchronous processing architecture.** Define the Spring Modulith event publication mechanism, listener placement, transaction relationship, idempotency convention, recovery behavior, and observability integration.

6. **Establish implementation-ready security integration architecture.** Define how Spring Security is integrated into the backend and where authorization enforcement is placed, without inventing authentication protocols, JWT claim structures, role hierarchies, or business authorization rules.

7. **Establish implementation-ready client architecture.** Define the source organization, API client generation approach, server-state boundary, authentication-credential handling, and navigation conventions for the Web (React + TypeScript SPA) and Mobile (React Native + Expo) applications.

8. **Establish the testing architecture.** Define the test pyramid for each application surface, the architectural test scope, the integration test strategy, and the testing tools and frameworks to be used.

9. **Establish the configuration and environment architecture.** Define Spring Boot profile conventions, configuration property namespacing, and the secrets management boundary.

10. **Establish the physical repository implementation structure.** Document the repository to the Java package depth for the backend and to the feature/layer depth for client applications.

11. **Validate Phase 4 completeness.** Verify that all design decisions have been made, all Phase 1–3 rules have been preserved, and the system is sufficiently specified for a developer or AI agent to begin implementation without architectural guessing.

---

# 4. Authority and Governance

## 4.1 Authoritative Hierarchy

Phase 4 operates under the following strict authority chain. Lower levels may not contradict or weaken higher levels:

| Priority | Authority | Role |
|---|---|---|
| 1 — Highest | Phase 1 — Engineering Constitution | Constitutional rules; cannot be weakened by any subsequent phase |
| 2 | Phase 2 — Application & Repository Blueprint | Business module boundaries, ownership, repository structure; cannot be redefined |
| 3 | Phase 3 — Technology Selection (AEOS-P03-D00 through D05) | Approved-for-planning technology direction |
| 4 — Current | Phase 4 — System Design | Translate the above into implementation-ready architecture |

**Phase 4 may not:**

- Contradict any Phase 1 constitutional rule.
- Change any Phase 2 business module boundary, capability ownership, or inter-module dependency rule.
- Contradict any Phase 3 technology selection that is in Proposed or higher status.
- Reinterpret an established decision to mean something different from the originating document's intent.
- Silently promote a Phase 3 "Proposed" decision to "Established" without the originating document being formally baselined.

**Phase 4 may:**

- Define implementation-level architecture within the structural, behavioral, and technological constraints established by Phases 1–3.
- Resolve decisions explicitly deferred to Phase 4 by Phase 3 documents (e.g., build tool, package naming convention, OpenAPI generation approach).
- Evaluate and formally decide physical implementation details (e.g., persistence isolation mechanism, DataSource configuration pattern) that Phase 3 identified as approved-for-planning directions.
- Produce implementation-ready design specifications across D01–D11.

## 4.2 Status Vocabulary

The following status vocabulary must be used consistently across all Phase 4 documents:

| Status | Definition |
|---|---|
| **ESTABLISHED** | Inherited constitutional or architectural requirement; not open to re-evaluation in Phase 4 |
| **APPROVED-FOR-PLANNING** | Phase 3 Proposed technology direction; sufficient to drive Phase 4 design; not yet formally baselined |
| **PROPOSED** | A Phase 4 design choice recommended by the authoring document, pending Phase 4 baseline approval |
| **OPEN** | A decision assigned to a specific later Phase 4 document; must not be decided prematurely |
| **DEFERRED** | Explicitly outside Phase 4 responsibility; will be resolved in a later phase or by explicit architectural authorization |
| **PROHIBITED** | Explicitly disallowed within Phase 4; producing this artifact or decision in Phase 4 is a violation |

Do not silently convert APPROVED-FOR-PLANNING into ESTABLISHED. Do not silently convert OPEN into PROPOSED.

---

# 5. Relationship to Phase 1 — Engineering Constitution

## 5.1 Inheritance Model

Phase 4 inherits and operationalizes every Phase 1 architectural principle. Phase 4 does not redefine these principles; it applies them to the specific technology context established by Phase 3.

## 5.2 Applicable Architectural Principles

The following Engineering Constitution architectural principles (governed by their constituent documents AEC-ARC-001 through AEC-ARC-014, with the overall index at `AEC-ARC-000`) are directly operative in Phase 4:

| Principle | Document | Phase 4 Operationalization |
|---|---|---|
| Architecture First | AEC-ARC-001 | Phase 4 design follows from established architecture; no implementation convenience may override architectural structure |
| Domain-Driven Design | AEC-ARC-002 | Module internals are organized by business domain; domain layer has no framework dependencies |
| Modular Monolith | AEC-ARC-003 | Seven modules in one deployable process; no inter-module HTTP; no microservice decomposition |
| Hexagonal Architecture | AEC-ARC-004 | Domain layer is the pure center; adapters translate external concerns; dependency direction is strictly inward |
| Separation of Concerns | AEC-ARC-005 | Validation, error handling, authorization, persistence, and presentation each occupy their correct layer |
| Dependency Direction | AEC-ARC-006 | Dependencies flow inward toward the domain; infrastructure depends on domain abstractions, not the reverse |
| Module Boundaries | AEC-ARC-007 | A module's `domain/`, `application/`, and `infrastructure/` are strictly private; only `contracts/` and `events/` are public |
| Business Capability Ownership | AEC-ARC-008 | Exactly one module owns each business capability; shared ownership of business logic is prohibited |
| Explicit Contracts | AEC-ARC-009 | Cross-module synchronous collaboration occurs only through explicitly declared public contracts |
| Event-Driven Collaboration | AEC-ARC-010 | Cross-module asynchronous collaboration occurs through published integration events; the publisher has no knowledge of consumers |
| Data Ownership | AEC-ARC-011 | Each module exclusively owns its authoritative persistence; no module may directly read or write another module's persistence |
| Evolutionary Architecture | AEC-ARC-012 | Architectural decisions are recorded and justified; the architecture evolves through deliberate decision, not accidental drift |
| Architecture Decision Records | AEC-ARC-013 | Every open decision resolved by a Phase 4 document must be recorded with context, alternatives, rationale, and consequences; ADRs are immutable after approval |
| Architecture Review | AEC-ARC-014 | No significant Phase 4 design decision proceeds without verification against the Engineering Constitution; AI agents must self-review against architectural principles before presenting Phase 4 design artifacts |

## 5.3 Constitutional Rules Operative Across All Phase 4 Documents

The following rules are ESTABLISHED and apply to every Phase 4 document without exception:

- Domain objects must not carry framework annotations or persistence annotations.
- Business rules must remain within the owning module's domain or application layer.
- Cross-module synchronous collaboration must occur only through explicitly declared public contracts in the producing module's `contracts/` package. A public application service is accessible to other modules only when it is declared through — or exposed via — the module's `contracts/` surface. No consuming module may access another module's internal `application/`, `domain/`, or `infrastructure/` packages directly.
- Cross-module asynchronous collaboration must occur only through published integration events in the producing module's `events/` package.
- No module may directly access another module's business database schema, tables, or persistence objects.
- No circular module dependencies are permitted.
- No shared business-domain packages are permitted.
- No business logic may reside in client applications.
- Technical infrastructure (logging, security plumbing, web framework configuration, shared utilities) may be shared via the `platform/` layer, provided it contains no business logic.

---

# 6. Relationship to Phase 2 — Application & Repository Blueprint

## 6.1 Phase 2 Status

The Phase 2 documents listed in Section 29 (Traceability) are **ESTABLISHED** — formally baselined and authoritative. Phase 4 may not reopen, re-evaluate, or reinterpret any Phase 2 decision.

## 6.2 Seven Approved Business Modules

Phase 4 must represent exactly these seven approved business/application modules:

| # | Module | Canonical Name | Phase 2 Category | Phase 2 Document |
|---|---|---|---|---|
| 1 | Identity & Access | `identity` | Platform | AEOS-P02-S02-D01 |
| 2 | Customer Management | `customer` | Core Business | AEOS-P02-S02-D02 |
| 3 | Insurance Product Catalogue | `product` | Core Business | AEOS-P02-S02-D03 |
| 4 | Policy Lifecycle Management | `policy` | Core Business | AEOS-P02-S02-D04 |
| 5 | Commission Management | `commission` | Core Business | AEOS-P02-S02-D05 |
| 6 | Notification Management | `notification` | Supporting | AEOS-P02-S02-D06 |
| 7 | Reporting & Analytics | `reporting` | Supporting | AEOS-P02-S02-D07 |

**No additional modules may be introduced in Phase 4.** Any apparent need for an additional module must be escalated as an architectural decision record and cannot be implemented within Phase 4.

## 6.3 Phase 2 Repository Structure

Phase 4 must preserve and elaborate the repository structure established by AEOS-P02-S01-D02 (Repository Architecture). The Phase 2 outer shell is ESTABLISHED:

```
anverra-global/
├── backend/
│   └── src/
│       ├── modules/
│       │   ├── identity/
│       │   ├── customer/
│       │   ├── product/
│       │   ├── policy/
│       │   ├── commission/
│       │   ├── notification/
│       │   └── reporting/
│       └── platform/
├── frontend/
├── mobile/
├── docs/
├── architecture/
├── infrastructure/
├── .ai/
├── scripts/
├── tools/
└── .github/
```

Phase 4 D10 elaborates this structure to include the Java source tree depth, test directory conventions, and configuration file placement.

## 6.4 Phase 2 Architectural Boundaries

The internal module structure established by AEOS-P02-S01-D04 (Architectural Boundaries) is ESTABLISHED. Every module must contain:

```
<module>/
├── domain/          — pure business core; no framework dependencies
├── application/     — orchestration; coordinates domain objects
├── interfaces/      — inbound adapters [NOTE: naming resolution is OPEN — see O3]
├── infrastructure/  — outbound adapters; technical implementations
├── contracts/       — public module surface; accessible to other modules
├── events/          — public integration events
├── configuration/   — module-specific Spring wiring
├── tests/           — module-level tests
└── documentation/   — module ADRs, diagrams
```

> [!NOTE]
> Phase 2 used the name `interfaces/` for inbound adapters. Phase 4 D02 must evaluate whether to retain `interfaces/` or harmonize with the hexagonal architecture convention of `adapter/inbound/` and `adapter/outbound/`. This is **Open Decision O3** and must be resolved by D02.

## 6.5 Unresolved Capabilities — Permanent Protection

The following business capabilities remain unresolved as of Phase 4 and must remain unresolved throughout all Phase 4 documents:

| Unresolved Capability |
|---|
| Agent Management |
| Dealer Management |
| Partner Management |
| Organization Management |
| Proposal Management |
| Document & KYC Management |
| Administration |

**No Phase 4 document may:**
- Model, design, or partially implement any of these capabilities.
- Create a module, package, directory, or configuration artifact for any of these capabilities.
- Reference these capabilities as implicit dependencies of an approved module.
- Create placeholder structures "in anticipation of" these capabilities.

Any activity relating to these capabilities requires an explicit architectural decision that resolves their capability ownership and adds them to the approved module inventory. Such a decision is outside Phase 4 scope.

---

# 7. Relationship to Phase 3 — Technology Selection & Architecture Enablement

## 7.1 Phase 3 Status

Phase 3 status must be understood along three distinct dimensions:

**Formal repository document status.** AEOS-P03-D00 (Phase 3 Technology Overview) is **Baseline**. AEOS-P03-D01 through AEOS-P03-D05 are each in **Proposed** status in the repository. This formal status has not been changed by Phase 4 authoring and is not changed by this document.

**Architectural evaluation closure.** The Phase 3 technology evaluation is substantively complete. Each Proposed document has been authored, internally consistent, and reviewed to a level of closure readiness sufficient to enable Phase 4 design work. This closure readiness does NOT retroactively change the repository document status of any Phase 3 document.

**Phase 4 planning authority.** Phase 4 treats Phase 3 technology directions as **APPROVED-FOR-PLANNING** — meaning they are sufficiently evaluated to drive Phase 4 architectural design, but they are not ESTABLISHED. APPROVED-FOR-PLANNING is a distinct status: it is stronger than a proposal under consideration, but it does not carry the authority of a formally baselined decision.

The critical distinction:

| Status | Meaning |
|---|---|
| **ESTABLISHED** | Baselined constitutional or architectural decision; not open to re-evaluation in Phase 4 |
| **APPROVED-FOR-PLANNING** | Phase 3 technology direction; substantively evaluated; sufficient to drive Phase 4 design; not formally baselined; may not be silently promoted to ESTABLISHED |

For the purposes of Phase 4, the following distinction applies:

- **Architecture and design document authoring** (producing D01–D11 as governance and design documents) may proceed once Phase 3 technology direction is APPROVED-FOR-PLANNING. Design authoring does not constitute implementation.
- **Application implementation** (writing source code, compiling, deploying, running the system) must not begin until the applicable Phase 3 documents are formally baselined and the required Phase 4 design documents are formally baselined.

This distinction preserves the Phase 3 baseline requirement for implementation while permitting Phase 4 design work to proceed from its APPROVED-FOR-PLANNING foundation.

## 7.2 Approved-for-Planning Technology Direction

The following technology direction is inherited from Phase 3 and carried forward by Phase 4:

### Backend (AEOS-P03-D01)

| Technology | Status | Notes |
|---|---|---|
| Java | APPROVED-FOR-PLANNING | Runtime language for the backend Modular Monolith |
| Spring Boot | APPROVED-FOR-PLANNING | Primary application framework |
| Spring Modulith | APPROVED-FOR-PLANNING | Module structure, boundary enforcement, and architectural verification |
| ArchUnit | APPROVED-FOR-PLANNING | Automated architectural rule testing |
| Spring Security | APPROVED-FOR-PLANNING | Security integration framework |
| Build tool | **OPEN — O1** | Maven vs. Gradle deferred by AEOS-P03-D01; resolved by Phase 4 D01 |

### Persistence (AEOS-P03-D02)

| Technology | Status | Notes |
|---|---|---|
| PostgreSQL | APPROVED-FOR-PLANNING | Relational database engine |
| Spring Data JDBC | APPROVED-FOR-PLANNING | Primary persistence framework; aggregate-oriented, minimal ORM |
| Flyway | APPROVED-FOR-PLANNING | Database migration tooling |
| Schema-per-module isolation | APPROVED-FOR-PLANNING | Logical isolation direction; physical enforcement strategy is OPEN (O6, O8) |

### API & Transport (AEOS-P03-D03)

| Technology | Status | Notes |
|---|---|---|
| REST over HTTP | APPROVED-FOR-PLANNING | External API protocol |
| JSON | APPROVED-FOR-PLANNING | Serialization format |
| Spring Web MVC | APPROVED-FOR-PLANNING | HTTP adapter framework |
| OpenAPI 3.0 | APPROVED-FOR-PLANNING | API contract specification standard |
| Springdoc | APPROVED-FOR-PLANNING | OpenAPI integration for Spring Boot |
| OpenAPI generation approach | **OPEN — O4** | Code-first vs. design-first; resolved by Phase 4 D04 |

### Messaging (AEOS-P03-D04)

| Technology | Status | Notes |
|---|---|---|
| Spring Modulith durable in-process events | APPROVED-FOR-PLANNING | Asynchronous inter-module collaboration mechanism |
| PostgreSQL-backed event publication | APPROVED-FOR-PLANNING | Durability backing store |
| At-least-once delivery | APPROVED-FOR-PLANNING | Delivery guarantee; requires idempotent consumers |
| External message broker (Kafka, RabbitMQ, etc.) | **PROHIBITED** | No external broker is introduced unless an explicit, authorized architectural decision changes this |

### Client (AEOS-P03-D05)

| Technology | Status | Notes |
|---|---|---|
| React | APPROVED-FOR-PLANNING | Web application UI framework |
| TypeScript | APPROVED-FOR-PLANNING | Type-safe language for both client surfaces |
| SPA approach | APPROVED-FOR-PLANNING | Web application delivery model |
| React Native | APPROVED-FOR-PLANNING | Mobile application framework |
| Expo | APPROVED-FOR-PLANNING | React Native toolchain and runtime |
| OpenAPI client generation tool | **OPEN — O5** | Specific generator tool; resolved by Phase 4 D07 |

## 7.3 Decisions Explicitly Deferred to Phase 4 by Phase 3

| Open Decision | Deferred By | Resolved By |
|---|---|---|
| O1 — Build tool (Maven vs. Gradle) | AEOS-P03-D01 | Phase 4 D01 |
| O4 — OpenAPI code-first vs. design-first | AEOS-P03-D03 | Phase 4 D04 |
| O5 — OpenAPI client generator tool | AEOS-P03-D05 | Phase 4 D07 |
| O8 — DataSource isolation pattern | AEOS-P03-D02 | Phase 4 D03 |

Additional Phase 4 decisions (O2, O3, O6, O7, O9, O10, O11, O12, O13, O14, O15, O16) are natural elaborations of Phase 2 and Phase 3 decisions that Phase 4 must formally resolve. See Section 27 for the complete open-decision register.

---

# 8. Phase 4 Responsibility Boundary

## 8.1 What Phase 4 Is Responsible For

Phase 4 is responsible for translating the established architecture and approved-for-planning technology into a complete, implementation-ready system design. This includes:

**Backend Implementation Architecture (D01)**
- Build tool selection: Maven or Gradle (O1)
- Spring Boot project structure and autoconfiguration conventions
- Spring Modulith module declaration pattern
- ArchUnit rule categories and architectural test organization
- Java package naming convention and root package (O2)
- Platform infrastructure organization (`platform/` sub-packages)

**Module Implementation Architecture (D02)**
- Canonical internal directory structure for all seven modules
- Resolution of `interfaces/` vs. `adapter/inbound/` naming (O3)
- Spring Modulith package-level module boundary enforcement
- Java package visibility rules for module encapsulation
- Public contract (`contracts/`) and public event (`events/`) package conventions
- `configuration/` placement within the module structure

**Persistence Implementation Architecture (D03)**
- Physical persistence isolation enforcement strategy
- Schema naming convention (O6)
- Database role and privilege strategy
- DataSource configuration pattern: per-module vs. shared (O8)
- Flyway migration classpath convention per module
- Transaction boundary ownership convention
- Repository interface placement and implementation placement

**Configuration & Environment Architecture (D09)**
- Spring Boot profile naming convention
- Configuration property namespace convention
- Secrets management boundary and local development secret pattern
- Per-environment configuration separation

**API Implementation Architecture (D04)**
- REST controller adapter placement and naming convention
- DTO placement: request and response DTOs in the adapter layer
- DTO mapping pattern convention
- OpenAPI implementation approach: code-first or design-first (O4)
- HTTP error response format and `@ControllerAdvice` placement
- Request validation placement and Bean Validation integration
- Security filter integration point (before controllers)

**Asynchronous Processing Architecture (D05)**
- Spring Modulith event publication API pattern
- Transaction relationship between event publication and the originating transaction
- Durable event publication configuration
- Technical event-publication persistence topology
- Listener placement convention within consuming modules
- Listener transaction model
- Idempotency convention (O7)
- Retry and recovery behavior
- Dead-letter/failure visibility
- Observability integration

**Security Implementation Architecture (D06)**
- Spring Security filter chain placement in platform infrastructure
- Authentication mechanism integration architecture (without selecting the authentication protocol)
- Security context propagation convention
- Authorization check placement within the application layer
- Domain layer isolation from security infrastructure
- Secrets management boundary

**Client Implementation Architecture (D07)**
- Web application (`frontend/`) source organization
- Mobile application (`mobile/`) source organization
- OpenAPI client generation tool selection (O5)
- Generated client file placement and generation trigger
- Server-state vs. local-state boundary convention
- Authentication credential handling placement
- Navigation structure conventions
- Shared vs. independently generated API types between Web and Mobile (O9)

**Testing Architecture (D08)**
- Backend test pyramid: domain unit, application service, architecture (ArchUnit), module integration, API adapter, cross-module integration, persistence
- Web application test pyramid: component, integration, end-to-end
- Mobile application test pyramid: component, integration, device
- Test framework selection
- `@ApplicationModuleTest` usage pattern
- Testcontainers usage convention

**Repository Implementation Structure (D10)**
- Physical repository tree to Java package depth for the backend
- Frontend and mobile source organization to the feature/layer depth
- Test directory conventions
- CI configuration file placement
- Build file location and naming

**Phase 4 Validation & Implementation Readiness (D11)**
- Verification of all Phase 4 exit criteria against D00–D10
- Consolidated AI implementation constraints
- Implementation readiness declaration

## 8.2 What Phase 4 Explicitly Inherits Without Re-Evaluation

The following are ESTABLISHED and must not be re-evaluated, renegotiated, or weakened in Phase 4:

- The seven approved business modules and their ownership boundaries (Phase 2)
- The prohibition on cross-module persistence access (Phase 1)
- The prohibition on inter-module HTTP communication (Phase 1)
- The prohibition on circular module dependencies (Phase 1)
- The Hexagonal Architecture dependency direction (Phase 1)
- The `contracts/` and `events/` public surface model (Phase 2)
- The backend as a single deployable Modular Monolith (Phase 1 + Phase 2)
- The prohibition on shared business-domain packages (Phase 1)
- The restriction of business logic to the backend (Phase 1)

---

# 9. Phase 4 Scope

Phase 4 scope is:

> **Design the implementation-ready architecture for the AnverraGlobal backend Modular Monolith, its seven business modules, its persistence layer, its API layer, its asynchronous processing layer, its security integration, the Web and Mobile client applications, the testing strategy, the configuration model, and the physical repository structure — such that a developer or AI agent can begin implementation of the first business module without making architectural guesses.**

Phase 4 scope ends where business logic, domain model specification, API contract design, database schema design, and business event design begin.

---

# 10. Explicit Non-Scope — What Phase 4 Must Not Define

The following are explicitly PROHIBITED from all Phase 4 documents. Producing any of these artifacts in Phase 4 is a governance violation.

## 10.1 Business Rules — PROHIBITED

Phase 4 must not define:
- Policy lifecycle rules (underwriting, endorsement, cancellation, renewal)
- Commission calculation formulas or commission event triggers
- Product pricing or rating rules
- KYC workflow steps or verification criteria
- Customer eligibility rules
- Notification trigger conditions or notification content
- Reporting metric definitions or KPIs
- Any business workflow, approval process, or compensation logic

## 10.2 Domain Models — Rules

The following distinctions govern domain model work in Phase 4:

**PERMITTED:** Defining implementation-level structure, patterns, and placement conventions for domain constructs (aggregates, entities, value objects, domain services) whose business concepts are legitimately authorized by Phase 2 business blueprints. D02 may define how such constructs are organized within a module's `domain/` layer without inventing new business concepts.

**PROHIBITED:** Inventing new business concepts, new aggregates, or new entities that are not authorized by Phase 2 blueprints. The following are explicitly prohibited:
- Domain objects for unresolved capabilities: Agent, Dealer, Partner, Organization, Proposal, Document, KYC, or Administration
- Any aggregate or entity whose business concept is not established by an authoritative Phase 2 module blueprint
- New business rules or business invariants invented to make implementation appear complete
- Any domain model structure for a capability listed as DEFERRED in Section 24

## 10.3 API Contracts — PROHIBITED

Phase 4 must not define:
- Endpoint URLs (e.g., `/api/v1/policies`)
- HTTP methods assigned to specific business operations
- Request body field names or types
- Response body field names or types
- Concrete DTO schemas
- Business-specific API error codes or error messages
- Pagination contract fields

## 10.4 Persistence Schema — Business vs. Technical

### 10.4.1 Business Persistence — PROHIBITED

Phase 4 must not define any of the following for business-owned data:
- Business table names
- Business column names or types
- Business primary key strategies
- Business foreign key relationships
- Business index definitions
- Business constraint names
- Business migration file contents
- Business-specific persistence models

These belong to the application design phase that follows Phase 4.

### 10.4.2 Technical Framework Persistence — PERMITTED WHERE REQUIRED

Phase 4 MAY evaluate and define the architectural topology, placement, and migration organization of technical persistence required by an already-approved technology (such as Spring Modulith's durable event publication mechanism), subject to the following constraints:

- D00 must not define the actual table or column schema of any technical framework table.
- D03 and D05 are jointly responsible for evaluating where technical framework persistence resides and how it is managed within the approved persistence isolation strategy.
- Any technical framework persistence must conform to D03's physical isolation architecture and must not bypass D03's DataSource and migration structure.
- Technical framework persistence must never become a mechanism for sharing business data across modules.
- The distinction between business persistence and technical framework persistence must be made explicit in any Phase 4 document that references both.

## 10.5 Integration Event Contracts — PROHIBITED

Phase 4 must not define:
- Concrete business integration event names (e.g., `PolicyIssued`, `CustomerRegistered`)
- Event payload field names or types
- Event routing or topic configuration
- Business workflows implemented through event chains

When illustrating the asynchronous architecture, Phase 4 documents must use placeholder notation such as `[SomeDomainEvent]` and must not establish placeholder names as binding decisions.

## 10.6 Security Specifics — PROHIBITED

Phase 4 must not define:
- JWT claim names or structure
- OAuth or OIDC provider selection
- Token expiry policies
- Role names or role hierarchy
- Permission names or permission assignments
- MFA workflow or authentication factors
- Business authorization rules

## 10.7 Notification Details — PROHIBITED

Phase 4 must not define:
- Email, SMS, or push channel selection
- Notification provider or vendor
- Notification template content or structure
- Notification retry policies
- Recipient model or notification preferences

## 10.8 Reporting Details — PROHIBITED

Phase 4 must not define:
- Report names or structure
- Dashboard design or layout
- KPI definitions or business metrics
- Analytical query models
- Data aggregation logic

---

# 11. Architectural Principles Operative in Phase 4

The following principles govern every Phase 4 document. They are stated here for operational clarity. Their authoritative definitions remain in Phase 1 (AEC-ARC-001 through AEC-ARC-014; the overall index document is AEC-ARC-000).

## 11.1 Architecture First (AEC-ARC-001)

Implementation structure follows from architecture. No implementation convenience, framework default, or tooling preference may alter the architectural structure established by Phases 1 and 2. Phase 4 design decisions must justify how they serve the architecture — not how the architecture might accommodate a tool preference.

## 11.2 Domain-Driven Design (AEC-ARC-002)

Each module's internal organization is centered on the business domain it owns. The domain layer must remain free from framework dependencies, persistence annotations, and infrastructure concerns. Value Objects, Entities, Aggregates, and Domain Services exist to model the business problem, not to satisfy database schema or API contract requirements.

## 11.3 Modular Monolith (AEC-ARC-003)

The backend is a single deployable application process. Modules are logical architectural boundaries enforced at compile time, test time, and runtime — not separate services, processes, or deployable units. No Phase 4 design may recommend or imply a migration toward microservices, separate deployment units, or service meshes for the modules.

## 11.4 Hexagonal Architecture (AEC-ARC-004)

The domain is the stable center. All technical concerns (HTTP, database, messaging, security) are adapters that surround the domain. The dependency direction is strictly inward:

```
External World
       ↓
Inbound Adapters  (REST controllers, event listeners consuming external triggers)
       ↓
Application Layer  (use cases, application services)
       ↓
Domain Layer  (aggregates, domain services, port interfaces)
       ↑
Outbound Adapters  (repository implementations, external API clients)
       ↑
Infrastructure  (Spring Data JDBC, HTTP clients, Spring configuration)
```

Inbound adapters depend on the application layer. The application layer depends on the domain. Outbound adapters implement port interfaces defined in the domain or application layer. The domain depends on nothing outside itself.

## 11.5 Separation of Concerns (AEC-ARC-005)

Each concern has exactly one home:

| Concern | Home |
|---|---|
| Structural request validation | Inbound adapter layer |
| Business pre-condition checks | Application service layer |
| Business invariant enforcement | Domain layer |
| Persistence | Outbound persistence adapter |
| Authorization enforcement | Application service layer |
| HTTP error translation | Inbound adapter layer (`@ControllerAdvice`) |
| Security credential validation | Platform security infrastructure (before controllers) |

Mixing concerns across layers is a Phase 4 governance violation.

## 11.6 Dependency Direction (AEC-ARC-006)

No outward dependency from the domain is permitted. The domain does not know about Spring, HTTP, JDBC, Flyway, or any external infrastructure. Abstractions (port interfaces) are defined in the domain or application layer and implemented in the infrastructure/outbound adapter layer.

## 11.7 Module Boundaries (AEC-ARC-007)

A module's boundary is enforced by:

1. **Java package visibility** — internal packages are not accessible outside the module package tree.
2. **Spring Modulith** — verifies at test time that no inter-module package access violates the declared module surface.
3. **ArchUnit** — verifies dependency direction, layer rules, and cross-module access at test time.
4. **Database isolation** — prevents database-level cross-module access (strategy defined in D03).

A module's public surface (per AEOS-P02-S01-D04 §5.1) consists of:
- Explicitly declared types in the `contracts/` package — the **formal synchronous cross-module mechanism**
- Published integration events in the `events/` package — the **formal asynchronous cross-module mechanism**
- Public application services — accessible to other modules only through the module's `contracts/` surface

`contracts/` is the sole point of entry for cross-module synchronous access. A public application service that must be accessible to another module must be declared through — or exposed via — the module's `contracts/` package. No consuming module may directly import from another module's `domain/`, `application/` internals, or `infrastructure/` packages. D02 determines the exact Java mechanism by which public application services are made accessible in a way that enforces this constraint and is consistent with AEC-ARC-009.

## 11.8 Business Capability Ownership (AEC-ARC-008)

Exactly one module owns each business capability. Shared ownership of a business capability is a governance violation. When Phase 4 documents refer to inter-module collaboration, the collaboration must be between clearly identified owner modules through the approved mechanisms.

## 11.9 Explicit Contracts (AEC-ARC-009)

Cross-module synchronous collaboration occurs only through the producing module's `contracts/` package. A public application service is part of a module's public surface; however, it is accessible to other modules only when it is declared through, or its interface is placed in, the `contracts/` package. Direct access to another module's internal `application/` classes, domain objects, or infrastructure is prohibited regardless of whether those classes are declared public in Java. D02 determines the exact Java enforcement mechanism.

## 11.10 Event-Driven Collaboration (AEC-ARC-010)

Cross-module asynchronous collaboration occurs through published integration events. The publishing module has no knowledge of consumers. Consumer modules react independently. Event delivery is at-least-once; consumer implementations must be idempotent.

## 11.11 Data Ownership (AEC-ARC-011)

Each module owns its authoritative persistence exclusively. No module may:
- Directly query another module's database schema
- Directly write to another module's database schema
- Share a database table with another module
- Hold a database-level foreign key reference to a table in another module's schema

Cross-module data needs are satisfied through public contracts (synchronous) or integration events (asynchronous), not through shared database access.

## 11.12 Evolutionary Architecture (AEC-ARC-012)

Architectural decisions are recorded as Architecture Decision Records (ADRs). When Phase 4 resolves an open decision (O1–O16), the resolution must be traceable within the applicable Phase 4 document. Decisions must not drift silently; they must be made explicitly with documented rationale.

## 11.13 Architecture Decision Records (AEC-ARC-013)

Every Phase 4 document that resolves an open decision must produce a traceable decision record containing: context (why the decision is needed), alternatives evaluated, decision made, rationale, and consequences. Phase 4 decision records are immutable once the containing document is baselined. Superseded candidate options remain visible in the document for historical traceability. AI agents must reference applicable decision records before proposing changes to areas governed by resolved open decisions.

## 11.14 Architecture Review (AEC-ARC-014)

No significant Phase 4 design decision may be recorded without verifying compliance with the Engineering Constitution. Phase 4 document review must evaluate: business alignment, module boundary integrity, dependency direction correctness, contract explicitness, data ownership preservation, and security placement. Critical violations must be resolved before a Phase 4 document may be baselined. AI agents must perform an explicit self-review against the architectural principles before presenting any Phase 4 design artifact.

---

# 12. Backend Design Governance

Phase 4 backend design (D01) must comply with the following governance rules:

1. **One backend process.** There is one Spring Boot application. All seven modules run within the same JVM process. No module is extracted into a separate process or service.

2. **Build tool must be resolved.** D01 must select Maven or Gradle (Open Decision O1) based on structured evaluation against the established technology stack. Both are compatible; the selection must be justified with rationale.

3. **Java package naming must be resolved.** D01 must define the root package name (Open Decision O2). Any example package names used in pre-analysis documents are illustrative candidates, not architectural decisions.

4. **Spring Modulith module registration — required outcome.** Each of the seven business modules must be registered as a Spring Modulith application module so that the framework enforces the module boundary at verification time. The required architectural outcome is: inter-module package access violations are detected and fail the build. The exact configuration mechanism — package naming, annotation usage, or module descriptor placement — is an implementation decision assigned to D01.

5. **ArchUnit architectural tests — required outcome.** Automated ArchUnit tests must enforce the following architectural rules at minimum:
   - Dependency direction within each module: the domain layer must not import from infrastructure or adapter layers
   - No direct access to another module's non-public packages
   - No circular inter-module dependencies
   - Absence of framework or infrastructure annotations in the domain layer

   These are the required rule categories. The exact ArchUnit rule selectors, test class organization, and rule configuration are implementation decisions assigned to D01.

6. **Platform infrastructure.** The `platform/` package provides shared technical infrastructure only. It contains no business logic. Its internal organization is a D01 decision.

7. **No microservices.** The backend architecture must not introduce service-to-service HTTP communication between modules. Spring Modulith in-process communication is the only approved inter-module mechanism for synchronous calls.

---

# 13. Module Boundary Governance

Phase 4 module design (D02) must comply with the following governance rules:

1. **Seven modules only.** Exactly the seven modules listed in Section 6.2 are implemented. No additional modules are introduced.

2. **Canonical internal structure.** Every module follows the canonical internal structure established by Phase 2 (AEOS-P02-S01-D04), elaborated with Java package conventions by D02. The `interfaces/` vs. `adapter/` naming question (O3) is resolved in D02 and applied uniformly across all seven modules.

3. **Public surface is strictly bounded.** Per AEOS-P02-S01-D04 §5.1, a module's public surface consists of: `contracts/`, `events/`, and public application services. D02 must define the exact Java mechanism for enforcing this boundary — how `contracts/` types are accessed, how public application services are exposed, and how Spring Modulith's package visibility rules enforce the complete boundary so that no consuming module can reach internal packages.

4. **Domain layer purity.** The `domain/` package of every module must not contain Spring Framework annotations, JPA or JDBC annotations, HTTP or web framework imports, or Spring Security imports.

5. **Configuration placement.** Module-specific Spring `@Configuration` classes are placed within a designated configuration package inside the module. D02 determines the exact placement.

6. **Spring Modulith verification.** Spring Modulith's `ApplicationModules.verify()` must be executed as an architectural test. A failing verification test must block the CI pipeline.

---

# 14. Persistence Design Governance

Phase 4 persistence design (D03) must comply with the following governance rules:

1. **Logical isolation is ESTABLISHED.** No module may access another module's authoritative persistence. This is a non-negotiable constitutional requirement (AEC-ARC-011).

2. **Physical isolation strategy is OPEN.** Phase 3 proposed schema-per-module as the isolation direction (APPROVED-FOR-PLANNING). D03 must formally evaluate and decide:
   - Whether schema-per-module is the appropriate physical isolation mechanism
   - The schema naming convention (O6)
   - The DataSource configuration pattern: per-module `DataSource` bean vs. controlled shared `DataSource` (O8)
   - The database role and privilege enforcement model
   - The Flyway migration classpath organization per module

3. **No business schemas, tables, or columns are defined.** D03 defines the physical isolation architecture — conventions, patterns, privilege structure. It does not create business tables, business columns, business constraints, or business migration file contents. Technical persistence infrastructure required by the approved-for-planning technology stack (such as internal tables used by Spring Modulith's durable event publication mechanism) is distinct from business persistence schema and may be evaluated by D03 in conjunction with D05. That evaluation must not produce business schema definitions.

4. **Repository placement constraint.** The hexagonal dependency direction (AEC-ARC-006) requires that the domain layer does not depend on persistence technology. D03 must define the exact package placement convention for repository port interfaces and repository implementations in a way that satisfies this constitutional constraint. The specific sub-package naming and location within each module are D02 and D03 decisions.

5. **Flyway per-module ownership.** Each module that owns persistence must own its Flyway migration scripts. Migrations must not be shared across modules. The exact classpath convention and directory structure are D03 decisions.

6. **Transaction boundary constraint.** The constitutional architecture (AEC-ARC-004, AEC-ARC-005) requires that the domain layer and persistence adapters remain decoupled from transaction management concerns. D03 must formally evaluate and decide the transaction boundary convention, including which layer owns the transaction boundary and how cross-module synchronous calls behave within a transaction context. This evaluation and its outcome are D03 decisions.

7. **No cross-module foreign keys.** Database-level foreign key constraints between schemas belonging to different modules are prohibited. Cross-module data references are resolved through application-layer contracts or events.

---

# 15. API Design Governance

Phase 4 API design (D04) must comply with the following governance rules:

1. **REST adapters only.** REST controllers are inbound adapters. They translate HTTP requests into application commands or queries. They do not contain business logic.

2. **Adapter placement.** REST controllers are placed in the module's inbound adapter package, using the directory/package convention selected by D02 under Open Decision O3.

3. **DTO placement.** Request DTOs and response DTOs belong in the inbound adapter layer. DTOs must not be placed in the domain or application layer.

4. **Application service invocation.** Controllers must call application services only. Controllers must not directly invoke repository interfaces, domain objects, or another module's internals.

5. **OpenAPI approach is OPEN.** D04 must evaluate and select between code-first and design-first approaches (O4). No preference is pre-established by D00. D04 must document the evaluation rationale.

6. **No endpoints invented.** D04 establishes the API adapter pattern, DTO placement convention, and OpenAPI integration approach. It does not define endpoint URLs, HTTP methods for specific business operations, or DTO field names.

7. **Error handling.** D04 must define the HTTP error response format and the global exception translation mechanism. D04 evaluates and decides this; no format is pre-selected by D00.

8. **Security filter integration point.** The Spring Security filter chain intercepts requests before they reach controllers. D04 documents the integration point; D06 defines the authentication mechanism details.

---

# 16. Asynchronous Processing Governance

## 16.0 Event Terminology

The following terms are used precisely throughout Phase 4. Conflating them is a governance violation.

| Term | Definition |
|---|---|
| **Domain Event** | An internal domain or application concept representing something that occurred within a module's bounded context. A domain event is a private implementation detail. It is NOT automatically a cross-module contract and must NOT be published directly to other modules. |
| **Integration Event** | A public event explicitly declared in a module's `events/` package and authorized for asynchronous cross-module consumption. Integration events are the module's public asynchronous surface. A domain event may become an integration event only through an explicit, deliberate design decision recorded in the applicable Phase 4 document. |
| **Durable Event Publication** | The technical Spring Modulith mechanism that persists and delivers integration events asynchronously within the Modular Monolith. It is infrastructure — not a business concept. It solves the dual-write problem by persisting event publication state transactionally with the originating business operation. |
| **External Message Broker** | Kafka, RabbitMQ, Redis Streams, or equivalent external messaging infrastructure. This is **PROHIBITED** unless an explicitly authorized future architectural decision changes that constraint. |

`Domain Event ≠ Integration Event ≠ Durable Event Publication ≠ External Message Broker`

No Phase 4 document may treat these terms interchangeably or conflate them.

## 16.1 Asynchronous Processing Rules

Phase 4 async processing design (D05) must comply with the following governance rules:

1. **Spring Modulith in-process events only.** The approved asynchronous mechanism is Spring Modulith's durable in-process event publication backed by PostgreSQL. No external message broker may be introduced by D05 or any other Phase 4 document without an explicit, authorized architectural decision.

2. **No business event names.** D05 defines the event publication architecture. It does not name or define any business integration events. Illustrative examples must use placeholder notation (e.g., `[SomeDomainEvent]`).

3. **Technical event publication persistence topology.** Spring Modulith's durable event publication mechanism requires internal technical persistence (event publication log storage). This is technical framework persistence (see §10.4.2) — not business persistence. D05, in conjunction with D03, must evaluate where this technical infrastructure resides within the persistence architecture established by D03. This evaluation must:
   - Conform to D03's physical isolation strategy and schema conventions
   - Not produce any business persistence schema, business table, or business column definition
   - Not bypass D03's established DataSource and migration architecture

   D03 owns the persistence architecture; D05 must operate within it. D05 owns the async event-processing behavior; D03 must not pre-decide event-processing configuration.

4. **At-least-once delivery and idempotency.** At-least-once delivery is APPROVED-FOR-PLANNING. Consumer implementations must be idempotent. D05 must evaluate and define the technical idempotency convention (O7).

5. **Retry and failure visibility.** D05 must evaluate what Spring Modulith provides natively for incomplete publication recovery and failure visibility, and determine whether additional mechanisms are warranted. D05 must not prescribe retry behavior before this evaluation.

6. **Listener transaction model.** D05 must evaluate the appropriate transaction phase and propagation for event listeners based on what Spring Modulith's event model supports. The specific configuration pattern is a D05 decision, constrained by the transaction-boundary convention established by D03.

7. **Observability.** D05 must define observability integration points for event publication and listener execution.

---

# 17. Security Design Governance

Phase 4 security design (D06) must comply with the following governance rules:

1. **Identity module is the business authority.** The Identity module (AEOS-P02-S02-D01) owns authentication and authorization as business capabilities. Phase 4 defines the technical integration architecture. It does not redefine business authorization responsibility.

2. **Authentication mechanism integration.** D06 must define how Spring Security is integrated into the request lifecycle to validate incoming credentials. The specific authentication protocol (JWT, session, OAuth token introspection, or equivalent) is deferred — JWT/OAuth provider selection is explicitly **PROHIBITED** in Phase 4. D06 defines the integration plumbing architecture, not the protocol.

3. **Spring Security placement.** Spring Security's filter chain is placed in the platform infrastructure layer, not in any business module. D06 must define the configuration ownership.

4. **Authorization check placement.** The Engineering Constitution places authorization enforcement at the business module level, not in the HTTP transport adapter. D06 must evaluate the precise technical enforcement mechanism consistent with the constitutional rule.

5. **Domain layer isolation.** The domain layer of every module must have zero knowledge of Spring Security or any security infrastructure. Domain objects must not carry security annotations.

6. **Client-side authorization is UX only.** Authorization decisions made by client applications are user experience aids only (e.g., hiding UI elements). They are not authoritative. The backend remains the sole authority for authorization decisions.

7. **Secrets.** Secrets must not be hardcoded in source code or committed to the repository. D06, in conjunction with D09, defines the secrets management boundary.

8. **Not defined in D06 or any Phase 4 document:** JWT claims, OAuth/OIDC provider, token expiry policies, role names, permission names, role hierarchy, MFA workflow, or any business authorization rule.

---

# 18. Client Design Governance

Phase 4 client design (D07) must comply with the following governance rules:

1. **Client boundary.** Web and Mobile applications are consumers of the backend REST/JSON/OpenAPI interface. They exist outside the Modular Monolith boundary:

   ```
   Client Applications (Web / Mobile)
             ↓
   REST / JSON / OpenAPI boundary
             ↓
   Backend Modular Monolith
   ```

2. **No business logic in clients.** Client applications must not contain business rules, business validation logic, or authoritative business state. UI-appropriate validation (input format, required field indicators) is permitted as a user experience aid but does not replace backend validation.

3. **No direct backend access.** Client applications must not access PostgreSQL directly, access Spring Modulith events or event publication infrastructure, invoke Spring application service interfaces or module internal contracts, or connect to any internal backend infrastructure.

4. **Feature organization is an option.** Organizing client source code by feature areas that align with backend capability names is one organizational approach that D07 may evaluate and select. It is not a requirement to reproduce backend bounded contexts in the client. The client must not mirror backend domain models or share business types with the backend.

5. **API client generation.** The OpenAPI specification produced by the backend drives client API type and function generation. The specific generation tool is selected in D07 (O5). Generated file placement is a D07 decision.

6. **Server-state vs. local-state.** D07 must define the boundary between generated API clients, server-state management, and local UI state. Server-state must remain distinct from local UI state. The specific server-state management mechanism or library is a D07 implementation decision.

7. **Cross-client type sharing.** D07 must decide whether generated TypeScript types are shared between Web and Mobile applications or generated independently per client (O9).

8. **Authentication credential handling.** Token storage, attachment to outgoing requests, and session refresh logic are placed in the API client infrastructure layer, not in business feature components. D07 defines the placement convention.

9. **No endpoint contracts invented.** D07 establishes client source organization, API client generation, and state-management conventions. It does not define API endpoints, DTO field names, or business-specific client workflows.

---

# 19. Testing Governance

Phase 4 testing design (D08) must comply with the following governance rules:

1. **Architecture tests are mandatory.** ArchUnit architectural tests and Spring Modulith's `ApplicationModules.verify()` are not optional. They must be part of the standard test suite and must gate the CI pipeline.

2. **Test pyramid discipline.** Each application surface has a test pyramid. Domain unit tests are the most numerous. Integration tests are selective. End-to-end tests are minimal and high-value.

3. **Module isolation in tests.** Module integration tests use Spring Modulith's `@ApplicationModuleTest` to run in module-isolated Spring contexts. Cross-module tests use the full application context.

4. **Real database for persistence tests.** Integration and persistence tests use Testcontainers to provide a real PostgreSQL instance. Tests must not use embedded in-memory database substitutes for persistence tests.

5. **Domain unit tests use no Spring context.** Domain unit tests must not load any Spring application context. Domain objects are plain Java objects testable in isolation.

6. **No business scenarios invented.** When D08 illustrates test patterns, it uses generic placeholder notation (e.g., `[ModuleName]`, `[SomeAggregate]`) rather than inventing specific business scenarios, endpoint URLs, or domain object names.

---

# 20. Configuration Governance

Phase 4 configuration design (D09) must comply with the following governance rules:

1. **Profile naming is OPEN.** D09 must define the Spring Boot profile naming convention. Any candidate names used in pre-analysis documents are illustrative only.

2. **Property namespacing is OPEN.** D09 must define the configuration property namespace convention. Any candidate namespace patterns used in pre-analysis documents are illustrative only.

3. **Secrets must not be committed.** Configuration that includes secrets, credentials, API keys, or database passwords must not be committed to the repository. D09 defines the local development secret pattern and the production secret management boundary.

4. **Per-environment separation.** Application configuration is layered: default properties (committed for non-secret values), profile-specific overrides (committed for non-secret values), and environment-specific secret values (not committed).

5. **Module-specific configuration.** Module-specific Spring `@Configuration` classes reside within the module's designated configuration package, as established by D02.

---

# 21. Repository Design Governance

Phase 4 repository design (D10) must comply with the following governance rules:

1. **Phase 2 outer shell is ESTABLISHED.** The root-level directory structure established by AEOS-P02-S01-D02 is not changed by D10.

2. **D10 adds implementation depth.** D10 elaborates the repository to include the Java source tree, test directory conventions, Flyway migration directory structure (based on D03 decisions), build file placement (based on D01 decisions), and CI configuration file placement.

3. **Illustrative examples become decisions.** Where prior analysis documents used illustrative package paths and directory patterns, D10 produces the authoritative physical tree based on the decisions made in D01–D09.

4. **No business content.** D10 defines the repository structure. It does not create source files, migration scripts, or application code.

---

# 22. Shared Infrastructure Governance

The `backend/src/platform/` package provides shared technical infrastructure. The following rules govern its use in Phase 4:

| Rule | Constraint |
|---|---|
| No business logic | `platform/` must contain zero business logic, business rules, or domain objects |
| Technical utilities only | Logging configuration, security filter chain configuration, web framework configuration, exception translation, shared technical utilities |
| No module internals | `platform/` must not access the internal packages of any business module |
| Accessible from modules | Business modules may depend on platform infrastructure for technical concerns |
| No reverse dependency | `platform/` must not depend on any business module |

The internal sub-packages of `platform/` are defined by D01. No sub-package of `platform/` may contain business capability logic.

---

# 23. Cross-Module Collaboration Rules

## 23.1 Synchronous Collaboration

**Mechanism:** `contracts/` is the only authorized synchronous cross-module mechanism. The consuming module's application service depends on a public contract interface declared in the producing module's `contracts/` package. Spring DI provides the implementation. A public application service of the producing module is accessible to other modules only when it is declared through, or its interface is placed in, the `contracts/` package.

**Rules:**
- The consuming module may only access types declared in the producing module's `contracts/` package.
- Spring Modulith enforces this at verification time.
- No consuming module may directly access a producing module's `application/`, `domain/`, or `infrastructure/` packages — including Java-public classes within those packages.
- Any module collaboration that requires a public application service must route through that service's interface declared in `contracts/`.

**Governance:** D02 must define the naming convention for public contract interfaces, determine how public application services are exposed through `contracts/`, and confirm that Spring Modulith's package visibility rules enforce the complete boundary.

## 23.2 Asynchronous Collaboration

**Mechanism:** The publishing module publishes integration events using the Spring Modulith durable in-process event mechanism backed by PostgreSQL (APPROVED-FOR-PLANNING, from AEOS-P03-D04). Integration events are defined in the publishing module's `events/` package and consumed by listener implementations in consuming modules. The exact transaction relationship between event publication and the originating application operation, the listener placement convention, the listener transaction model, and the idempotency mechanism are all **OPEN** implementation decisions assigned to AEOS-P04-D05.

**Rules:**
- Only event types declared in a module's `events/` package may be consumed by external modules.
- The publishing module has no knowledge of its consumers.
- Consumer implementations must be idempotent (specific idempotency mechanism assigned to D05 — Open Decision O7).
- No external message broker may be introduced; all asynchronous collaboration uses the approved Spring Modulith in-process mechanism.

**Governance:** Phase 4 must not name specific business events or define event payload fields. Placeholder notation is mandatory for all illustrative examples. All async processing implementation details (event publication API, transaction model, listener placement, retry behavior, failure visibility, observability) are D05 decisions and must not be pre-decided in D00 or any earlier Phase 4 document.

## 23.3 Prohibited Collaboration Patterns

| Prohibited Pattern | Description |
|---|---|
| Direct cross-module persistence | Any module reading or writing another module's database schema |
| Internal type access | Importing from another module's `domain/`, `application/`, or `infrastructure/` packages |
| Shared business aggregate | A domain aggregate jointly owned by two modules |
| Hidden synchronous coupling | Calling another module's internal class through a shared supertype not declared in `contracts/` |
| Cross-module transaction coupling | A single transaction spanning two modules' persistence boundaries without explicit architectural justification |
| Network-based inter-module communication | HTTP, gRPC, or any network call between modules within the backend process |
| External message broker without authorization | Introducing Kafka, RabbitMQ, or equivalent without an explicit authorized architectural decision |

---

# 24. Unresolved Capability Protection

## 24.1 Protected Unresolved Capabilities

| Capability | Status in Phase 4 |
|---|---|
| Agent Management | DEFERRED — not modeled in any Phase 4 document |
| Dealer Management | DEFERRED — not modeled in any Phase 4 document |
| Partner Management | DEFERRED — not modeled in any Phase 4 document |
| Organization Management | DEFERRED — not modeled in any Phase 4 document |
| Proposal Management | DEFERRED — not modeled in any Phase 4 document |
| Document & KYC Management | DEFERRED — not modeled in any Phase 4 document |
| Administration | DEFERRED — not modeled in any Phase 4 document |

## 24.2 Protection Rules

- No Phase 4 document may create a module, package, directory, or configuration artifact for any of the above capabilities.
- No Phase 4 document may reference these capabilities as implicit consumers, producers, or dependencies of an approved module, except to acknowledge that the inter-capability dependency is unresolved and deferred.
- No placeholder directory (e.g., `backend/src/modules/agent/`) may be created in Phase 4.
- No Phase 4 document may design an API endpoint, database table, or integration event that implicitly assumes one of these capabilities is resolved.

---

# 25. Phase 4 Document Inventory

The following documents constitute the complete Phase 4 document set. No additional Phase 4 design documents may be introduced without explicit architectural justification and a governance update to D00.

> [!NOTE]
> The **Document ID** reflects the logical numbering of each document within the Phase 4 design system. The **Authoring Position** reflects the dependency-driven authoring sequence defined in Section 26. These are different concepts. D09, for example, carries numeric ID 09 but is authored fourth due to its dependency on D03. The authoring dependency sequence governs; the document ID is an identifier, not an ordering instruction.

| Document ID | Authoring Position | Title | Primary Responsibility |
|---|---|---|---|
| AEOS-P04-D00 | 1 | Phase 4 System Design Overview | Governance, authority, boundaries, open decisions, AI constraints, exit criteria |
| AEOS-P04-D01 | 2 | Backend Implementation Architecture | Build tool, Spring Boot structure, Spring Modulith, ArchUnit, Java package naming, platform organization |
| AEOS-P04-D02 | 3 | Module Implementation Architecture | Internal module structure, adapter naming, Spring Modulith boundaries, public surface conventions |
| AEOS-P04-D03 | 4 | Persistence Implementation Architecture | Physical isolation strategy, schema naming, DataSource pattern, Flyway organization, transaction conventions |
| AEOS-P04-D09 | 5 | Configuration & Environment Architecture | Profile naming, property namespacing, secrets boundary, per-environment separation |
| AEOS-P04-D04 | 6 | API Implementation Architecture | REST adapter pattern, DTO conventions, OpenAPI approach, error handling, validation placement |
| AEOS-P04-D05 | 7 | Asynchronous Processing Architecture | Event publication, listener placement, transaction model, idempotency, retry, observability |
| AEOS-P04-D06 | 8 | Security Implementation Architecture | Spring Security integration, authentication mechanism integration, authorization placement, secrets |
| AEOS-P04-D07 | 9 | Client Implementation Architecture | Web/Mobile source organization, client generation, state-management boundary, authentication plumbing |
| AEOS-P04-D08 | 10 | Testing Architecture | Test pyramid, ArchUnit scope, Testcontainers, Spring Modulith test support, framework selection |
| AEOS-P04-D10 | 11 | Repository Implementation Structure | Physical repository tree, Java source depth, frontend/mobile structure, CI file placement |
| AEOS-P04-D11 | 12 | Phase 4 Validation & Implementation Readiness | Exit criteria verification, consolidated AI constraints, implementation readiness declaration |

---

# 26. Authoring Order

Phase 4 documents must be authored in the following sequence. Dependencies between documents require this ordering. No document may be authored before its dependencies are complete and approved.

```
AEOS-P04-D00  ← this document (governance foundation)
      ↓
AEOS-P04-D01  Backend Implementation Architecture
      ↓
AEOS-P04-D02  Module Implementation Architecture
      ↓
AEOS-P04-D03  Persistence Implementation Architecture
      ↓
AEOS-P04-D09  Configuration & Environment Architecture
      ↓
AEOS-P04-D04  API Implementation Architecture
      ↓
AEOS-P04-D05  Asynchronous Processing Architecture
      ↓
AEOS-P04-D06  Security Implementation Architecture
      ↓
AEOS-P04-D07  Client Implementation Architecture
      ↓
AEOS-P04-D08  Testing Architecture
      ↓
AEOS-P04-D10  Repository Implementation Structure
      ↓
AEOS-P04-D11  Phase 4 Validation & Implementation Readiness
```

## 26.1 Dependency Rationale

**D00 first.** D00 is the governance document. No Phase 4 document may be authored before D00 is baselined. D00 establishes the rules, boundaries, and open-decision assignments that all subsequent documents must follow.

**D01 before D02.** D01 resolves the build tool (O1) and Java package naming convention (O2). Both determine the physical package structure that D02 must define. D01 also establishes the Spring Modulith configuration approach and ArchUnit organization, which D02 elaborates at the module level.

**D02 before D03.** D02 defines the internal module package structure, including where the persistence outbound adapter and domain port interfaces reside within each module. D03 must know the exact package and directory conventions before it can specify where repository implementations and Flyway scripts reside.

**D03 before D09.** The persistence configuration (DataSource beans, Flyway locations, database connection properties) is a significant part of the configuration model. D09 must understand the DataSource pattern (O8) and schema organization (O6) decided by D03 before it can specify the configuration file structure.

**D09 before D04/D05/D06.** Configuration and environment separation is cross-cutting. D04 (API), D05 (async processing), and D06 (security) all reference application profiles, configuration property namespaces, and secrets boundaries. D09 must establish the configuration model before these documents make profile-specific or environment-specific assumptions.

**D04 before D07.** D07 defines the client implementation architecture, including the API client generation strategy (O5). The generation strategy depends on the OpenAPI implementation approach (O4) selected by D04 — code-first vs. design-first determines what artifact the client generator consumes (annotations vs. spec file).

**D05 after D03.** The technical event-publication persistence topology is evaluated jointly by D05 and D03. D03 must establish the persistence isolation strategy before D05 can evaluate where the event publication infrastructure resides.

**D08 after D01–D07.** D08 defines the testing architecture across all layers. It must understand the implementation structure of every layer — module internals, persistence adapters, REST controllers, event listeners, security integration — before it can define test types, frameworks, and placement.

**D10 after D01–D09.** D10 is a synthesis document. It produces the authoritative physical repository tree by combining all implementation decisions made in D01–D09.

**D11 last.** D11 validates all of Phase 4. It must verify D00 exit criteria against D01–D10. It cannot be authored until all other documents are complete and approved.

---

# 27. Phase 4 Open Decisions

The following decisions are formally registered for Phase 4. (Note: O1–O16 were registered as OPEN at the start of Phase 4. O17–O53 were subsequently identified and resolved during downstream document authoring). Any Phase 4 document that encounters a dependency on an unresolved open decision must escalate rather than silently resolve it.

| ID | Decision | Why Open | Assigned To | Impact If Unresolved |
|---|---|---|---|---|
| O1 | Build tool: Maven vs. Gradle | Explicitly deferred by AEOS-P03-D01 | AEOS-P04-D01 | Cannot determine source tree structure (`src/main/java/` convention), build lifecycle, or CI tool integration |
| O2 | Java root package naming convention | Not established by Phase 2 or Phase 3; any examples in prior documents are illustrative candidates | AEOS-P04-D01 / D02 | Cannot define Spring Modulith module package boundaries or ArchUnit rule selectors |
| O3 | Inbound adapter directory naming: `interfaces/` vs. `adapter/inbound/` | Phase 2 used `interfaces/`; hexagonal convention is `adapter/inbound/` and `adapter/outbound/`; both are structurally valid | AEOS-P04-D02 | Must be resolved and applied uniformly across all seven modules before any module implementation begins |
| O4 | OpenAPI implementation approach: code-first vs. design-first | Both approaches are compatible with the Phase 3 technology selection; D00 does not pre-select | AEOS-P04-D04 | Determines whether the OpenAPI spec is an input or an output of implementation; directly drives the client generation strategy |
| O5 | OpenAPI client generation tool | Specific tool not selected by Phase 3; candidates identified in pre-analysis remain candidates | AEOS-P04-D07 | Determines client build step, generated file format, and client code structure |
| O6 | PostgreSQL schema naming convention | Schema-per-module is the approved-for-planning isolation direction; exact naming pattern is a Phase 4 decision | AEOS-P04-D03 | Determines DataSource configuration naming, Flyway migration classpath, and database privilege structure |
| O7 | Event listener idempotency mechanism | At-least-once delivery is established; the specific consumer-side idempotency pattern is a Phase 4 evaluation | AEOS-P04-D05 | Determines how all consuming module listeners handle duplicate event delivery |
| O8 | DataSource configuration pattern: per-module bean vs. controlled shared DataSource | Both patterns are technically feasible with the approved stack; trade-offs in complexity and enforcement strength must be evaluated | AEOS-P04-D03 | Significantly affects Spring Boot configuration complexity and the strength of database-level isolation enforcement |
| O9 | Shared vs. independently generated Web/Mobile API types | Whether generated TypeScript types are shared between `frontend/` and `mobile/` or generated independently | AEOS-P04-D07 | Determines whether a shared type package is introduced into the repository |
| O10 | Messaging Architecture / External Broker Strategy | Spring Modulith supports durable events and externalization (Kafka/RabbitMQ); whether to use a broker or in-process events must be formally evaluated based on async processing requirements (notification, agents) | AEOS-P04-D05 | Determines whether external broker infrastructure is introduced and dictates the physical boundaries of asynchronous processing |
| O11 | Authentication Architecture | Multiple candidates exist (OAuth2/OIDC, JWT, Session); must be evaluated for compatibility with client architecture | AEOS-P04-D06 | Determines the fundamental mechanism for establishing identity across the API boundary |
| O12 | Authorization Enforcement Model | Spring Security supports URL, method, and application-level checks; the appropriate boundary must be defined | AEOS-P04-D06 | Determines where and how authorization constraints are applied without leaking into the domain |
| O13 | Client Credential Storage Strategy | Browser and Mobile clients have different secure storage requirements | AEOS-P04-D06 | Determines how sensitive tokens/credentials are protected on the client side |
| O14 | CSRF Strategy | Required mitigation depends heavily on the selected authentication architecture | AEOS-P04-D06 | Determines whether CSRF protection is active and how tokens are managed |
| O15 | Secret Management Architecture | Necessary for DB credentials, API keys, and JWT signing keys | AEOS-P04-D06 | Determines how secrets are injected without exposing them in source code or configuration files |
| O16 | Security Context Propagation Across Asynchronous Boundaries | Determines what context is propagated and what must never be propagated across async boundaries | AEOS-P04-D06 | Defines how trusted metadata crosses asynchronous boundaries and prevents token leakage |
| O17–O26 | Client Architecture & Contract Sharing | Generated during client architecture evaluation | AEOS-P04-D07 | Resolved |
| O27–O36 | Testing Architecture & Boundaries | Generated during testing strategy evaluation | AEOS-P04-D08 | Resolved |
| O37–O45 | Configuration, Secrets, & Environments | Generated during environment architecture evaluation | AEOS-P04-D09 | Resolved |
| O46–O53 | Repository Physical Structure | Generated during repository structure evaluation | AEOS-P04-D10 | Resolved |

---

# 28. AI Implementation Governance

This section establishes mandatory requirements for any AI agent — coding assistant, code generation agent, or automated implementation tool — participating in the implementation of the AnverraGlobal system.

## 28.1 Mandatory Pre-Implementation Reading Order

Before producing any implementation artifact, an AI agent must read and understand the following documents in order:

1. Phase 1 Engineering Constitution — all applicable architectural principles (AEC-ARC-000 and constituent documents)
2. Phase 2 Application & Repository Blueprint — AEOS-P02-S01-D01 through D05 and AEOS-P02-S02-D00 through D07
3. Phase 3 Technology Selection — AEOS-P03-D00 through D05
4. AEOS-P04-D00 — this document (Phase 4 governance)
5. The applicable Phase 4 document for the area being implemented (e.g., AEOS-P04-D02 before implementing any module structure)

An AI agent that implements code without reading the applicable architecture documents is in violation of the AI implementation governance rules established by the Engineering Constitution.

## 28.2 Mandatory Behavioral Rules

| # | Rule |
|---|---|
| 1 | Read Phase 1 constitution before any implementation |
| 2 | Read Phase 2 architecture before any implementation |
| 3 | Read Phase 3 technology decisions before any implementation |
| 4 | Read AEOS-P04-D00 before implementing any Phase 4 structure |
| 5 | Read the applicable D01–D11 document before modifying its area of concern |
| 6 | Never invent a module for an unresolved business capability |
| 7 | Never create an API endpoint URL or assign an HTTP method to a specific business operation |
| 8 | Never create a DTO with named business fields |
| 9 | Never create a database table, column definition, or migration file content |
| 10 | Never assign a name to a business integration event |
| 11 | Never define an integration event payload structure |
| 12 | Never introduce direct cross-module persistence access |
| 13 | Never create a circular module dependency |
| 14 | Never introduce microservices, network-based inter-module communication, or distributed deployment of backend modules |
| 15 | Never introduce an external message broker without an explicit, authorized architectural decision |
| 16 | Never move business logic into a client application |
| 17 | Never create a shared business-domain package accessible to multiple modules |
| 18 | Never bypass a module's public `contracts/` surface to access internal packages |
| 19 | Never treat an illustrative example from a pre-analysis or analysis document as a binding architectural decision |
| 20 | Stop implementation and report the gap when a required implementation detail has not been established by the applicable Phase 4 document |

## 28.3 The Anti-Invention Principle

AI implementation must realize established architecture, not invent it.

If the architecture does not specify something, the correct behavior is to stop and report the gap:

> "I cannot implement [X] because [the applicable Phase 4 document] has not established [the required design decision]. The following decision is required before implementation can proceed: [decision description]. This decision should be resolved by [the appropriate Phase 4 document]."

Proceeding with a plausible-looking implementation that silently resolves an unresolved architectural decision is a governance violation. Explicit failure is preferred over architectural invention.

## 28.4 Scope Compliance

AI agents must implement exactly what the architecture specifies — no more, no less. "Approximately correct" or "good enough for now" implementations that deviate from the architectural specification introduce technical debt that undermines the engineering program and makes the codebase inconsistent with the documents that govern it.

## 28.5 Reporting and Escalation

If an AI agent encounters any of the following situations, it must stop and escalate:

- The implementation requires a business rule not established by Phase 2.
- The implementation requires an API endpoint not defined by an authorized business API contract.
- The implementation requires a database table that has not been designed through an authorized process.
- The implementation requires an integration event name that has not been established.
- The applicable Phase 4 document for the area being implemented has not been authored or baselined.
- The implementation would require resolving an OPEN decision (O1–O16) not yet assigned to the current document.
- The implementation would require a module for an unresolved business capability.

---

# 29. Traceability Requirements

## 29.1 Traceability to Phase 1

Every Phase 4 document must explicitly trace to the Phase 1 constitutional rules it operationalizes, using the principle ID (e.g., AEC-ARC-004) and the associated document name.

## 29.2 Traceability to Phase 2

| Phase 2 Document | Document ID | Relevance to Phase 4 |
|---|---|---|
| System Blueprint | AEOS-P02-S01-D01 | System identity, component roles, application surfaces |
| Repository Architecture | AEOS-P02-S01-D02 | Physical repository structure (ESTABLISHED outer shell) |
| Application Boundaries | AEOS-P02-S01-D03 | Web, Mobile, and Backend boundary and interaction rules |
| Architectural Boundaries | AEOS-P02-S01-D04 | Module internal structure, encapsulation rules, collaboration strategies |
| Blueprint Traceability | AEOS-P02-S01-D05 | Cross-document consistency and traceability audit |
| Stage 2 Overview | AEOS-P02-S02-D00 | Module governance rules, authoring constraints |
| Identity Blueprint | AEOS-P02-S02-D01 | Identity module ownership, capabilities, non-responsibilities |
| Customer Blueprint | AEOS-P02-S02-D02 | Customer module ownership, capabilities, non-responsibilities |
| Product Blueprint | AEOS-P02-S02-D03 | Product module ownership, capabilities, non-responsibilities |
| Policy Blueprint | AEOS-P02-S02-D04 | Policy module ownership, capabilities, non-responsibilities |
| Commission Blueprint | AEOS-P02-S02-D05 | Commission module ownership, capabilities, non-responsibilities |
| Notification Blueprint | AEOS-P02-S02-D06 | Notification module ownership, capabilities, non-responsibilities |
| Reporting Blueprint | AEOS-P02-S02-D07 | Reporting module ownership, capabilities, non-responsibilities |

## 29.3 Traceability to Phase 3

| Phase 3 Document | Document ID | Status | Phase 4 Dependence |
|---|---|---|---|
| Phase 3 Technology Overview | AEOS-P03-D00 | Baseline | Governance principles and evaluation rules |
| Backend Technology Blueprint | AEOS-P03-D01 | Proposed | Java, Spring Boot, Spring Modulith, ArchUnit, Spring Security; build tool deferred (O1) |
| Persistence Technology Blueprint | AEOS-P03-D02 | Proposed | PostgreSQL, Spring Data JDBC, Flyway, schema-per-module direction; DataSource pattern deferred (O8) |
| API & Transport Technology Blueprint | AEOS-P03-D03 | Proposed | REST, HTTP, JSON, Spring Web MVC, OpenAPI 3.0, Springdoc; generation approach deferred (O4) |
| Messaging Technology Blueprint | AEOS-P03-D04 | Proposed | Spring Modulith durable in-process events, PostgreSQL-backed publication, at-least-once delivery |
| Client Technology Blueprint | AEOS-P03-D05 | Proposed | React, TypeScript, SPA, React Native, Expo; client generator deferred (O5) |

---

# 30. Phase 4 Exit Criteria

Phase 4 is complete and may be closed only when all of the following conditions are satisfied:

## 30.1 Document Completeness

- [ ] AEOS-P04-D00 is baselined
- [ ] AEOS-P04-D01 is baselined
- [ ] AEOS-P04-D02 is baselined
- [ ] AEOS-P04-D03 is baselined
- [ ] AEOS-P04-D04 is baselined
- [ ] AEOS-P04-D05 is baselined
- [ ] AEOS-P04-D06 is baselined
- [ ] AEOS-P04-D07 is baselined
- [ ] AEOS-P04-D08 is baselined
- [ ] AEOS-P04-D09 is baselined
- [ ] AEOS-P04-D10 is baselined
- [ ] AEOS-P04-D11 is baselined

## 30.2 Open Decisions Resolved

- [ ] O1 — Build tool resolved by D01
- [ ] O2 — Java root package naming resolved by D01 / D02
- [ ] O3 — Inbound adapter naming resolved by D02
- [ ] O4 — OpenAPI implementation approach resolved by D04
- [ ] O5 — OpenAPI client generator resolved by D07
- [ ] O6 — Schema naming convention resolved by D03
- [ ] O7 — Idempotency mechanism resolved by D05
- [ ] O8 — DataSource isolation pattern resolved by D03
- [ ] O9 — Shared vs. independent client type generation resolved by D07

## 30.3 Architecture Preservation

- [ ] No Phase 1 constitutional rule has been weakened by any Phase 4 document
- [ ] No Phase 2 module boundary or ownership has been changed
- [ ] No Phase 2 unresolved capability has been modeled, partially implemented, or implicitly introduced
- [ ] Phase 3 technology direction is represented without contradiction

## 30.4 Implementation Specificity

- [ ] Build tool selected; Spring Boot/Spring Modulith structure is explicit
- [ ] Java package naming convention is decided
- [ ] ArchUnit rule categories are defined
- [ ] Module internal structure is explicit for all seven modules
- [ ] Module boundary enforcement mechanisms are explicit
- [ ] Physical persistence isolation strategy is formally decided and documented
- [ ] Schema naming convention is decided
- [ ] DataSource configuration pattern is decided
- [ ] Flyway migration organization is decided
- [ ] Transaction ownership convention is explicitly documented
- [ ] REST adapter pattern is explicitly documented
- [ ] OpenAPI implementation approach is decided and documented
- [ ] HTTP error response format is decided
- [ ] Async event publication mechanism is explicitly documented
- [ ] Technical event-publication persistence topology is evaluated
- [ ] Event listener placement and transaction model are explicitly documented
- [ ] Idempotency convention is explicitly documented
- [ ] Spring Security integration architecture is explicitly documented
- [ ] Authentication mechanism integration architecture is defined without selecting the authentication protocol
- [ ] Authorization check placement is defined
- [ ] Client source organization is documented for Web and Mobile
- [ ] API client generation approach is documented
- [ ] Server-state vs. local-state boundary is documented
- [ ] Testing architecture is documented for all three application surfaces
- [ ] Test framework selections are documented
- [ ] Configuration profile convention is documented
- [ ] Property namespace convention is documented
- [ ] Secrets management boundary is documented
- [ ] Physical repository structure is documented to the Java package depth

## 30.5 AI Implementation Readiness

- [ ] AI implementation constraints are consolidated in D11
- [ ] A developer or AI agent can begin implementing the approved implementation architecture and structural foundation of the first business module (Identity) without making architectural guesses
- [ ] All illustrative examples in Phase 4 documents are explicitly labeled as illustrative
- [ ] All open decisions have been formally resolved and are traceable to their resolving document

## 30.6 Anti-Invention Verification (must pass before Phase 4 closure)

- [ ] No new business modules invented across all Phase 4 documents
- [ ] No unresolved capabilities modeled in any Phase 4 document
- [ ] No business rules invented
- [ ] No business concepts or domain entities invented for capabilities not authorized by Phase 2 blueprints
- [ ] No API endpoints invented
- [ ] No DTO field names invented
- [ ] No business database tables invented
- [ ] No business database columns invented
- [ ] No business integration event names invented
- [ ] No integration event payload fields invented
- [ ] No external messaging broker introduced without authorization
- [ ] No OAuth/OIDC provider selected
- [ ] No JWT claim structure defined
- [ ] No notification provider selected
- [ ] No reporting KPI defined
- [ ] No microservice architecture introduced
- [ ] No cross-module database access introduced
- [ ] No circular dependency introduced
- [ ] No business logic moved to client applications

---

# 31. Definition of Done

AEOS-P04-D00 is complete and eligible for baseline approval when:

- [x] Document identity, version, and status are established.
- [x] Phase 4 objectives are explicitly defined across D00–D11.
- [x] The Phase 4 authority hierarchy is formally established with clear prohibition rules.
- [x] The relationship to Phase 1 (constitution inheritance and operationalization) is explicit.
- [x] The relationship to Phase 2 (module inventory, structure, unresolved capability protection) is explicit.
- [x] The relationship to Phase 3 (technology direction, status distinctions) is explicit.
- [x] Phase 4 responsibility boundary (what Phase 4 may and may not decide) is explicit.
- [x] Phase 4 scope and non-scope are explicitly stated.
- [x] All 14 applicable architectural principles (AEC-ARC-001 through AEC-ARC-014) are operationalized for Phase 4.
- [x] Backend, module, persistence, API, async, security, client, testing, configuration, and repository governance rules are established.
- [x] Shared infrastructure governance is established.
- [x] Cross-module collaboration rules (synchronous, asynchronous, prohibited) are complete.
- [x] Unresolved capability protection is formally established.
- [x] The Phase 4 document inventory (D00–D11) is formally established.
- [x] The authoring order and dependency rationale are established.
- [x] All sixteen open decisions (O1–O16) are registered and assigned to their resolving documents.
- [x] AI implementation governance is comprehensive, unambiguous, and includes mandatory pre-implementation reading order.
- [x] Traceability to Phase 1, Phase 2, and Phase 3 is complete with document IDs confirmed from the repository.
- [x] Phase 4 exit criteria are formally defined across six categories.
- [x] D00 does not independently resolve implementation decisions assigned to D01–D11. D00 establishes mandatory architectural outcomes inherited from Phase 1–3 and constraints required to preserve constitutional boundaries. The assigned Phase 4 document remains responsible for selecting the concrete implementation mechanism, configuration, naming, and detailed design within those constraints.
- [x] Status vocabulary is used consistently throughout.
- [x] Anti-invention self-check is clean.

---

# 32. Final Status and Authorization

## 32.1 Anti-Invention Self-Check

| Check | Result |
|---|---|
| No new business modules | ✓ Pass |
| No unresolved capabilities modeled | ✓ Pass |
| No business rules invented | ✓ Pass |
| No business concepts invented for unauthorized capabilities | ✓ Pass |
| No API endpoints invented | ✓ Pass |
| No DTO field names invented | ✓ Pass |
| No business database tables invented | ✓ Pass |
| No business database columns invented | ✓ Pass |
| No business event names invented | ✓ Pass |
| No event payload fields invented | ✓ Pass |
| No external messaging broker introduced | ✓ Pass |
| No OAuth/OIDC provider selected | ✓ Pass |
| No JWT claim structure defined | ✓ Pass |
| No notification provider selected | ✓ Pass |
| No reporting KPI defined | ✓ Pass |
| No microservice architecture introduced | ✓ Pass |
| No cross-module database access introduced | ✓ Pass |
| No circular dependency introduced | ✓ Pass |
| No business logic moved to clients | ✓ Pass |
| No Phase 1 rule weakened | ✓ Pass |
| No Phase 2 ownership changed | ✓ Pass |
| No Phase 3 technology contradicted | ✓ Pass |
| Illustrative examples labeled | ✓ Pass |
| Open decisions assigned to later documents | ✓ Pass |
| D00 remains governance-focused | ✓ Pass |

## 32.2 Current Status

| Field | Value |
|---|---|
| **Status** | Baseline Candidate |
| **Version** | 1.0 |
| **Anti-Invention Check** | Passed |
| **Phase 1 Compliance** | Verified |
| **Phase 2 Compliance** | Verified |
| **Phase 3 Alignment** | Verified |
| **Open Decisions** | 9 registered; 0 prematurely resolved |
| **Governance Coverage** | Complete (32 sections) |
| **Ready for Baseline Approval** | Yes — pending review |

## 32.3 Authorization of the Next Document

Upon baseline approval of AEOS-P04-D00, the following document is authorized to proceed:

> ### AEOS-P04-D01 — Backend Implementation Architecture

**AEOS-P04-D01 must not begin authoring until AEOS-P04-D00 is formally baselined.**

D01 is authorized to:
- Evaluate and resolve Open Decision O1 (Maven vs. Gradle)
- Evaluate and resolve Open Decision O2 (Java root package naming convention)
- Define the Spring Boot project structure
- Define the Spring Modulith module declaration pattern
- Define the ArchUnit rule categories
- Define the `platform/` internal organization

D01 must read this document in its entirety before authoring begins.

---

*End of AEOS-P04-D00 — Phase 4 System Design Overview.*

*This document is the authoritative governance baseline for Phase 4 — System Design & Implementation Planning of the AnverraGlobal engineering program. All Phase 4 documents (D01–D11) inherit from and must comply with this document.*
