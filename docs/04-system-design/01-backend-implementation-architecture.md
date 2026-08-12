# Backend Implementation Architecture

**Document ID:** AEOS-P04-D01  
**Version:** 1.0  
**Status:** Baseline Candidate  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 2  

---

# 1. Document Identity

- **Document Title:** Backend Implementation Architecture  
- **Document ID:** AEOS-P04-D01  
- **Phase:** 4 — System Design & Implementation Planning  
- **System:** AnverraGlobal  
- **Status:** Baseline Candidate  
- **Version:** 1.0  
- **Immediate Governing Document:** AEOS-P04-D00 — Phase 4 System Design Overview  

---

# 2. Purpose

The purpose of this document is to establish the authoritative backend implementation architecture for the AnverraGlobal system. 

Where Phase 1 established the constitutional architecture, Phase 2 established application and repository boundaries, Phase 3 selected the technology baseline, and Phase 4 D00 established overall governance and system design constraints, this document (AEOS-P04-D01) translates those foundations into a concrete, implementation-ready backend architectural framework.

Specifically, this document:
1. Evaluates and resolves Open Decision **O1** (Build Tool: Maven vs. Gradle).
2. Evaluates and resolves Open Decision **O2** (Java Root Package Naming Convention).
3. Defines the Spring Boot application entry point and component scanning architectural baseline.
4. Establishes the Spring Modulith module discovery, structural registration, and boundary verification architecture.
5. Defines the ArchUnit architecture verification framework, rule categories, and enforcement triggers.
6. Establishes the technical organization and strict boundary rules for shared `platform/` infrastructure.
7. Defines the build lifecycle, centralized dependency management approach, and CI/CD verification integration gates.
8. Enforces unconditional domain purity and hexagonal dependency direction across all backend components.
9. Consolidates mandatory implementation constraints for human software engineers and AI coding agents.

This document governs the backend implementation framework without pre-empting module-internal package layouts and adapter directory naming (assigned to **AEOS-P04-D02**), persistence physical isolation and schema strategies (assigned to **AEOS-P04-D03**), configuration and environment architecture (assigned to **AEOS-P04-D09**), API implementation specs (assigned to **AEOS-P04-D04**), asynchronous event processing (assigned to **AEOS-P04-D05**), security integration plumbing (assigned to **AEOS-P04-D06**), client implementation architecture (assigned to **AEOS-P04-D07**), testing framework configurations (assigned to **AEOS-P04-D08**), physical repository layout (assigned to **AEOS-P04-D10**), or final readiness validation (assigned to **AEOS-P04-D11**).

---

# 3. Scope

## 3.1 In Scope

The scope of this document is strictly limited to backend-level implementation architecture:
- Build tool evaluation and selection (**O1**).
- Java root package naming convention evaluation and selection (**O2**).
- Standard backend source directory structure layout (`src/main/java`, `src/main/resources`, `src/test/java`, `src/test/resources`).
- Main Spring Boot application class placement and component scanning boundary rules.
- Spring Modulith application module registration, boundary modeling, and automated verification architecture.
- ArchUnit rule categories, execution triggers, and enforcement strategy.
- Platform layer (`platform/`) technical boundary rules, zero-business-logic rule, and reverse-dependency prohibitions.
- Dependency direction enforcement mechanisms for Hexagonal Architecture and pure Domain-Driven Design (DDD).
- Maven dependency management, BOM import strategy, and build lifecycle configuration gates.
- CI/CD build pipeline verification triggers.
- Consolidated AI implementation rules for backend code construction.

## 3.2 Explicitly Out of Scope

The following concerns are explicitly outside the scope of D01 and belong to designated Phase 4 design documents:
- Module-internal package organization and adapter directory naming (`interfaces/` vs. `adapter/inbound/`) — Open Decision **O3** assigned to **AEOS-P04-D02**.
- PostgreSQL physical isolation strategy, schema naming (**O6**), DataSource pattern (**O8**), and Flyway migration organization — assigned to **AEOS-P04-D03**.
- Configuration profiles, property namespaces, and secrets management boundaries — assigned to **AEOS-P04-D09**.
- OpenAPI implementation approach (**O4**), REST DTO field specs, and controller endpoint mappings — assigned to **AEOS-P04-D04**.
- Integration event payload fields, event listener transaction models, and idempotency mechanisms (**O7**) — assigned to **AEOS-P04-D05**.
- Authentication protocols, JWT claim structures, and Spring Security filter implementations — assigned to **AEOS-P04-D06**.
- Web/Mobile client source layout, client generation tooling (**O5**), and client type sharing (**O9**) — assigned to **AEOS-P04-D07**.
- Testcontainers setup, dataset strategy, and unit/integration test suite details — assigned to **AEOS-P04-D08**.
- Physical multi-root repository directory tree — assigned to **AEOS-P04-D10**.
- Business rules, domain entity attributes, business table schemas, API endpoints, or integration event names — **PROHIBITED** across all Phase 4 documents.

---

# 4. Architectural Context

The AnverraGlobal backend is a **Modular Monolith**. 

As established by Phase 1 (AEC-ARC-003), Phase 2 (AEOS-P02-S01-D01), Phase 3 (AEOS-P03-D01), and Phase 4 D00 (AEOS-P04-D00 §12), the backend executes as a single deployable application process within a single Java Virtual Machine (JVM). It is internally structured into strict, cohesive, bounded business capability modules.

## 4.1 Seven Approved Business Modules

The backend contains exactly seven approved business capability modules established by Phase 2:
1. **`identity`** — Identity & Access (Platform Business Category, AEOS-P02-S02-D01)
2. **`customer`** — Customer Lifecycle Management (Core Business Category, AEOS-P02-S02-D02)
3. **`product`** — Insurance Product Catalogue (Core Business Category, AEOS-P02-S02-D03)
4. **`policy`** — Policy Lifecycle Management (Core Business Category, AEOS-P02-S02-D04)
5. **`commission`** — Commission & Financial Settlement (Core Business Category, AEOS-P02-S02-D05)
6. **`notification`** — Notification & Operational Messaging (Supporting Business Category, AEOS-P02-S02-D06)
7. **`reporting`** — Operational Reporting & Analytics (Supporting Business Category, AEOS-P02-S02-D07)

No additional business modules may be created in Phase 4.

## 4.2 Protected Unresolved Capabilities

Per AEOS-P04-D00 §24, seven business capabilities remain unresolved and deferred: `Agent Management`, `Dealer Management`, `Partner Management`, `Organization Management`, `Proposal Management`, `Document & KYC Management`, and `Administration`.

D01 creates zero packages, classes, build modules, or placeholders for these unresolved capabilities.

## 4.3 Monolith Integrity

D01 explicitly preserves single-process deployment. The backend does not use microservices, network-based inter-module communication (HTTP, gRPC), internal REST calls between modules, service discovery, Kubernetes service decomposition, external message brokers, or distributed deployment topologies. All inter-module collaboration occurs strictly in-process through approved synchronous (`contracts`) or asynchronous (`events`) boundaries.

---

# 5. Technology Baseline

D01 inherits and operates directly upon the approved-for-planning technology stack established in Phase 3 and recognized by AEOS-P04-D00 §7.2:

| Layer / Concern | Selected Technology | Phase 3 Reference | Phase 4 Governance |
|---|---|---|---|
| Language & Runtime | **Java 21 (LTS)** | AEOS-P03-D01 | Approved-for-Planning Baseline |
| Core Application Framework | **Spring Boot 3.x** | AEOS-P03-D01 | Approved-for-Planning Baseline |
| Modular Monolith Engine | **Spring Modulith** | AEOS-P03-D01 | Approved-for-Planning Baseline |
| Architecture Verification | **ArchUnit** | AEOS-P03-D01 | Approved-for-Planning Baseline |
| Security Framework | **Spring Security** | AEOS-P03-D01 | Approved-for-Planning Baseline |
| Primary DBMS | **PostgreSQL** | AEOS-P03-D02 | Approved-for-Planning Baseline |
| Database Migration Tool | **Flyway** | AEOS-P03-D02 | Approved-for-Planning Baseline |
| Web Framework | **Spring Web MVC** | AEOS-P03-D03 | Approved-for-Planning Baseline |
| API Protocol & Format | **REST / JSON / OpenAPI 3.0** | AEOS-P03-D03 | Approved-for-Planning Baseline |
| Internal Async Messaging | **Spring Modulith Durable In-Process Events** | AEOS-P03-D04 | Approved-for-Planning Baseline |

D01 elaborates how these technologies are structured at the backend implementation level. D01 does not alter, replace, or re-evaluate any of these choices.

---

# 6. Build Tool Evaluation

Phase 3 (AEOS-P03-D01) deferred the explicit selection of backend build tool to Phase 4 (registered as Open Decision **O1** in AEOS-P04-D00 §27). 

This section evaluates the two primary candidate build tools for the JVM ecosystem: **Apache Maven** and **Gradle**.

## 6.1 Evaluation Criteria

Candidates are evaluated across technical and operational criteria relevant to AnverraGlobal:

1. **Modular Monolith Compatibility:** Ability to manage clean modular backend builds without script complexity.
2. **Spring Ecosystem Integration:** Maturity and native support of official Spring Boot plugins and Spring Modulith tooling.
3. **Build Predictability & Consistency:** Stability of build lifecycle execution across developer environments and CI runners.
4. **Dependency Management & BOM Alignment:** Precision in importing Spring Boot and Spring Modulith Bill of Materials (BOMs) without transitive version drift.
5. **CI/CD Pipeline Integration:** Simplicity and reliability of execution in automated CI runners.
6. **Governance & Auditability:** Ease of inspecting build files for compliance with architectural policies.
7. **Developer Experience & Tooling:** IDE integration support, local build workflow clarity, and developer familiarity.
8. **AI-Assisted Development Safety:** Structural guardrails reducing AI coding agent generation errors in build configuration files.
9. **Ecosystem Stability:** Long-term backward compatibility stability and enterprise maintainability.

## 6.2 Candidate 1 — Apache Maven

Apache Maven provides a standardized, convention-driven project model and lifecycle. Its POM is declarative in structure, while build behavior is supplied through configured plugins.

- **Strengths:**
  - *Explicit Configuration Model:* `pom.xml` uses a declarative XML structure that provides a constrained, highly inspectable build configuration surface.
  - *Standardized Lifecycle:* Lifecycle phases (`compile`, `test`, `package`, `verify`) operate conventionally across local and CI environments.
  - *BOM Management:* Excellent native support for `<dependencyManagement>` and imported BOMs (`spring-boot-dependencies`, `spring-modulith-bom`), ensuring controlled dependency version resolution.
  - *AI Readability & Governance:* Declarative XML schemas provide explicit structural guardrails. AI coding agents generate XML dependency blocks with high consistency, reducing script configuration syntax errors.
  - *Spring Plugin Support:* Native, battle-tested Maven plugins provided directly by the Spring team (`spring-boot-maven-plugin`).
- **Considerations:**
  - *XML Syntax:* Configuration structure is more verbose than script-based domain-specific languages.

## 6.3 Candidate 2 — Gradle

Gradle is a flexible build automation tool utilizing Groovy or Kotlin Domain-Specific Languages (`build.gradle.kts`).

- **Strengths:**
  - *Build Speed & Task Caching:* Advanced task DAG execution, incremental compilation, and local/remote build caching.
  - *Concise Syntax:* Kotlin DSL (`build.gradle.kts`) offers concise configuration syntax.
  - *Dependency Locking:* Supports explicit dependency locking mechanisms for reproducible build setups.
- **Considerations:**
  - *Script Model Governance:* Gradle build scripts execute as code blocks. Without strict governance, imperative build script logic can accumulate, increasing project maintenance overhead.
  - *AI Generation Consistency:* AI coding agents can introduce DSL syntax variations or API mismatches across different Gradle major releases, requiring periodic manual configuration tuning.
  - *Ecosystem Evolution:* Gradle releases periodically evolve build script APIs, requiring ongoing maintenance of build scripts.

## 6.4 Evaluation Summary Matrix

| Criterion | Apache Maven | Gradle | Preferred Alignment |
|---|---|---|---|
| 1. Modular Monolith Fit | High | High | Balanced |
| 2. Spring Ecosystem Integration | Native / First-class | Native / First-class | Balanced |
| 3. Build Lifecycle Predictability | High (Standard Lifecycle) | High (Task DAG) | **Apache Maven** |
| 4. Dependency BOM Management | Native `<dependencyManagement>` | Supported via plugins/DSL | **Apache Maven** |
| 5. CI/CD Pipeline Integration | Simple, standard runner setup | Requires cache management | **Apache Maven** |
| 6. Governance & Declarative Safety | Declarative XML model | Executable script model | **Apache Maven** |
| 7. AI Development Safety | **High (Schema guarded XML)** | Moderate (Script variations) | **Apache Maven** |
| 8. Long-Term Ecosystem Stability | High (Backward compatible) | Moderate (DSL evolution) | **Apache Maven** |

---

# 7. Build Tool Decision (Resolves Open Decision O1)

## 7.1 Formal Resolution

**Open Decision O1 is formally resolved:** **Apache Maven** is selected as the authoritative build tool for the AnverraGlobal backend.

## 7.2 Decision Rationale

Apache Maven is selected for AnverraGlobal because it provides a **standardized convention-driven lifecycle, robust Spring BOM integration, and explicit build governance that supports AI-assisted development workflows**.

1. **Governance & AI Safety:** Maven is preferred for this project because its conventional POM/lifecycle model provides a constrained and highly inspectable build configuration surface that is easier to govern consistently in an AI-assisted development workflow. XML configuration blocks are predictable, inspectable, and machine-verifiable.
2. **Spring Ecosystem Alignment:** Maven's `<dependencyManagement>` mechanism integrates natively with official Spring Boot and Spring Modulith BOMs, ensuring that library versions across all seven business modules remain centrally controlled and synchronized.
3. **Reproducibility & Governance:** Reproducible builds depend on controlled dependency versions, plugin versions, build environment, JDK version, timestamps, and other build inputs. Maven provides mechanisms and conventions that support this governance.
4. **Gradle Comparison:** Gradle remains technically capable of reproducible and highly governed builds. It is rejected here primarily because the project prioritizes Maven's conventional lifecycle, enterprise familiarity, explicit POM model, and simpler governance model for the intended AI-assisted workflow.

## 7.3 Decision Consequences

- The backend directory structure will be governed by a master project Maven `pom.xml`.
- Project execution will utilize the standard Maven Wrapper (`mvnw` and `.mvn/`) to ensure consistent Maven version baselines across environments.
- Project build scripts must remain declarative and inspectable. Inline custom scripting or arbitrary external execution hooks in `pom.xml` are prohibited.

---

# 8. Java Root Package Evaluation

Phase 2 and Phase 3 documents used illustrative package names in examples (e.g., `com.anverra.global`, `global.anverra`, `com.anverraglobal`). AEOS-P04-D00 §27 explicitly designated the Java root package naming convention as **Open Decision O2**, to be evaluated and resolved by D01.

This section evaluates the root package candidates against Java naming standards and AnverraGlobal architectural requirements.

## 8.1 Candidate Packages

1. **Candidate A: `com.anverra.global`**
   - Reverse domain hierarchy mapping `global.anverra.com`.
   - Divides corporate identity into two package segments (`anverra` and `global`).
2. **Candidate B: `global.anverra`**
   - Non-standard top-level domain prefix (`global`).
3. **Candidate C: `com.anverraglobal`**
   - Single unified package segment representing the canonical system identity `AnverraGlobal` established in Phase 2 (AEOS-P02-S01-D01).

## 8.2 Evaluation Criteria

- **System Identity Traceability:** Direct alignment with system name `AnverraGlobal`.
- **Spring Component Scanning Predictability:** Clean base package path for Spring Boot application configuration without wildcard ambiguities.
- **Spring Modulith Module Discovery:** Top-level package structure where business modules reside cleanly under the root package (`com.anverraglobal.<module>`).
- **ArchUnit Selector Clarity:** Unambiguous package selector strings for automated architecture tests (`com.anverraglobal..`).
- **Maintainability & AI Readability:** Clear distinction between the root package boundary and individual module sub-packages.

## 8.3 Comparison

`com.anverra.global` introduces a two-tier package hierarchy (`com.anverra` containing `global`), which requires additional explicit configuration when declaring Spring Modulith module boundaries.

`com.anverraglobal` provides a single, unified root package anchor. Business module sub-packages directly under `com.anverraglobal` correspond cleanly to the seven approved business modules.

---

# 9. Java Root Package Decision (Resolves Open Decision O2)

## 9.1 Formal Resolution

**Open Decision O2 is formally resolved:** **`com.anverraglobal`** is selected as the authoritative Java root package naming convention for the backend.

## 9.2 Package Structure Anchors

All Java source code in the AnverraGlobal backend resides under package `com.anverraglobal`. 

The root package structure is anchored as follows:

```text
com.anverraglobal
├── AnverraApplication (Main Spring Boot Application Class)
├── platform            (Shared Technical Infrastructure Boundary)
├── identity            (Business Module 1: Identity & Access)
├── customer            (Business Module 2: Customer Management)
├── product             (Business Module 3: Product Catalogue)
├── policy              (Business Module 4: Policy Lifecycle)
├── commission          (Business Module 5: Commission Settlement)
├── notification        (Business Module 6: Notification Operations)
└── reporting           (Business Module 7: Reporting & Analytics)
```

## 9.3 Rationale

1. **Spring Modulith Alignment:** Under Spring Modulith's discovery convention, top-level packages directly under the application root package are recognized as application modules. With root `com.anverraglobal`, packages `com.anverraglobal.identity`, `com.anverraglobal.customer`, etc., map cleanly as application modules.
2. **ArchUnit Pattern Clarity:** ArchUnit package selectors become crisp and deterministic: `com.anverraglobal.identity..`, `com.anverraglobal.platform..`, etc.
3. **Identity Consistency:** Aligns package naming directly with system identity `AnverraGlobal`.

---

# 10. Backend Source Structure

The backend source directory layout follows standard Maven project conventions.

## 10.1 Directory Layout Baseline

```text
backend/
├── pom.xml                   (Master Project Maven POM)
├── mvnw                      (Maven Wrapper Executable - Unix)
├── mvnw.cmd                  (Maven Wrapper Executable - Windows)
├── .mvn/                     (Maven Wrapper Configuration)
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── anverraglobal/
    │   │           ├── AnverraApplication
    │   │           ├── platform/
    │   │           ├── identity/
    │   │           ├── customer/
    │   │           ├── product/
    │   │           ├── policy/
    │   │           ├── commission/
    │   │           ├── notification/
    │   │           └── reporting/
    │   └── resources/
    │       └── application.yml
    └── test/
        ├── java/
        │   └── com/
        │       └── anverraglobal/
        │           ├── ArchitectureTests (ArchUnit Suite)
        │           └── ModulithTests     (Spring Modulith Verification)
        └── resources/
```

## 10.2 Structural Rules

- The application root is `com.anverraglobal`. The seven approved business capability modules are the only business application module roots directly beneath it. Technical root-level infrastructure is restricted to explicitly approved technical boundaries.
- No package or directory may be created for unresolved capabilities (`agent`, `dealer`, `partner`, `organization`, `proposal`, `document`, `kyc`, `administration`).
- The final physical repository and package tree is governed by **AEOS-P04-D10**, while module-internal package organization is governed by **AEOS-P04-D02**. D01 establishes root architectural boundaries and layering rules, but does not pre-empt D02's module-internal package decisions or Open Decision **O3**.

---

# 11. Spring Boot Application Structure

## 11.1 Application Entry Point

The backend Modular Monolith features a single Spring Boot application entry point located directly in the root package (`com.anverraglobal.AnverraApplication`).

## 11.2 Component Scanning Governance

- Standard Spring Boot application configuration implicitly scans package `com.anverraglobal` and all sub-packages.
- Custom component scan overrides or manual package inclusion/exclusion filters are prohibited unless required for explicit architectural testing harnesses.
- Spring Boot component scanning instantiates backend components across all seven modules into a single Spring ApplicationContext.
- Module encapsulation and inter-module access restrictions are enforced at compile/test time by Spring Modulith verification and ArchUnit rules, rather than by runtime ApplicationContext partitioning.

---

# 12. Spring Modulith Structure

## 12.1 Module Discovery and Modeling

Spring Modulith (selected in Phase 3 AEOS-P03-D01) provides the structural framework for organizing, modeling, and verifying the single-process Modular Monolith.

In accordance with Spring Modulith conventions:
- The main application class in `com.anverraglobal` defines the root of the application module tree.
- Direct sub-packages of `com.anverraglobal` representing business capabilities (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`) are discovered as **Application Modules**.
- `platform` is a shared technical infrastructure boundary and is not a business capability module. Because it resides beneath the application root, its treatment in Spring Modulith module detection must be explicitly configured and verified so that it does not become an unintended business application module. The implementation shall use Spring Modulith's supported shared/excluded/custom module-detection mechanism as appropriate, with automated verification preventing business-module dependencies on `platform` and preventing `platform` from depending on business modules.

## 12.2 Module Public API Surface & Verification

Spring Modulith provides structural modeling and verification (`ApplicationModules.of(...).verify()`).

- The `contracts` package is the authoritative synchronous cross-module collaboration boundary. Its exposure to consuming modules must be explicitly modeled through Spring Modulith named interfaces or an equivalent verified module-configuration mechanism. ArchUnit shall additionally enforce that cross-module dependencies do not bypass this boundary.
- All internal sub-packages of a module are private to that module. Architectural verification checks verify at build time that no cross-module imports target internal sub-packages.

---

# 13. Module Discovery and Verification

## 13.1 Automated Verification Requirement

The project shall configure architectural verification tests to execute during the Maven test lifecycle.

Spring Modulith inspects the application package structure anchored at `com.anverraglobal.AnverraApplication`, verifies module encapsulation boundaries, and validates that cross-module dependencies conform to declared public surfaces.

## 13.2 CI/CD Verification Gate

Spring Modulith verification runs as part of automated test execution in the build pipeline. If a class in Module A directly references an un-exported private package in Module B, verification throws a structural violation exception, failing the test execution and breaking the CI build.

---

# 14. ArchUnit Architecture

While Spring Modulith models and verifies module-level package boundaries, **ArchUnit** (`com.tngtech.archunit`) provides fine-grained enforcement of Hexagonal Architecture layer rules, dependency directions, domain purity, and platform isolation constraints.

## 14.1 ArchUnit Framework Integration

ArchUnit tests reside in the test source tree (`src/test/java/com/anverraglobal/ArchitectureTests`). The test suite inspects compiled Java bytecode in package `com.anverraglobal` and enforces five mandatory architectural rule categories.

## 14.2 ArchUnit Rule Categories

### Category 1: Cross-Module Internal Access Prevention
No class in module `com.anverraglobal.<moduleA>` may access private internal packages (`domain`, internal `application`, or `infrastructure`) of module `com.anverraglobal.<moduleB>`. Synchronous cross-module access must target `com.anverraglobal.<moduleB>.contracts`.

### Category 2: Hexagonal Layer Dependency Direction
Within every business module:
- Domain layer classes must not depend on application, infrastructure, or adapter layers.
- Application layer classes must not depend on infrastructure or adapter layers.
- Outbound infrastructure adapters must implement abstractions defined in the domain or application layers.

### Category 3: Unconditional Domain Purity
Classes residing in `com.anverraglobal..<module>.domain..` must not import framework or infrastructure packages:
- `org.springframework..`
- `jakarta.persistence..` / `javax.persistence..`
- `org.springframework.data..`
- `org.springframework.security..`
- `org.springframework.web..` / `jakarta.ws.rs..`

### Category 4: Platform Boundary Protection
Classes in `com.anverraglobal.platform..` must contain zero business logic and must not access any business module package (`com.anverraglobal.identity..`, `customer..`, etc.).

### Category 5: Zero Circular Module Dependencies
There must be zero dependency cycles between top-level packages under `com.anverraglobal`.

---

# 15. Dependency Direction

In accordance with Phase 1 (AEC-ARC-004, AEC-ARC-006) and AEOS-P04-D00 §11.4, dependency directions flow strictly inward toward the domain core.

```text
               External Triggers (HTTP, Events)
                              │
                              ▼
                Inbound Driving Adapters
                              │
                              ▼
                      Application Layer
                  (Orchestration / Use Cases)
                              │
                              ▼
                         Domain Layer
                (Aggregates, Ports, Entities)
                              ▲
                              │
               Outbound Driven Adapters (Persistence)
                              │
                              ▲
                 Technical Infrastructure (Spring)
```

- **Inbound Driving Adapters** depend inward on application services or public contract interfaces.
- **Application Services** depend inward on domain aggregates and port abstractions.
- **Domain Core** depends on nothing outside the domain package.
- **Outbound Driven Adapters** (e.g., persistence implementations) depend inward by implementing port abstractions declared in the domain or application layer.
- **Frameworks & DI Infrastructure** instantiate and wire adapters, while domain abstractions remain completely framework-independent.

---

# 16. Domain Purity Enforcement

AEOS-P04-D00 §13.4 establishes an **unconditional prohibition** on framework leaks in the domain layer.

## 16.1 Mandatory Rule

The `domain/` package of every business module must contain **zero** Spring Framework annotations, ORM annotations, HTTP/web imports, or security imports.

## 16.2 Explicitly Prohibited Import Namespaces in Domain

```text
// Strictly Prohibited in Domain Packages:
org.springframework.stereotype.*
org.springframework.beans.*
org.springframework.transaction.*
org.springframework.web.*
org.springframework.security.*
jakarta.persistence.*
org.springframework.data.*
```

## 16.3 Enforcement Mechanism

Domain constructs are authored as pure Java classes instantiated via constructors or domain factory methods. Repositories and external services are accessed via port interfaces defined in the domain or application layer. ArchUnit is configured to scan compiled bytecode during build test execution and fail the build if any prohibited framework import is detected in a domain package.

---

# 17. Platform Organization

Package `com.anverraglobal.platform` provides shared technical infrastructure for the backend.

## 17.1 Governing Rules for `platform/`

1. **Zero Business Logic:** `platform/` must contain no business rules, business domain concepts, business calculations, or domain entities.
2. **No Reverse Dependencies:** `platform/` must never import classes from any business module (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`).
3. **No Capability Placeholders:** `platform/` must not contain sub-packages or classes for unresolved capabilities (e.g., `platform/agent`, `platform/kyc`, `platform/proposal`).
4. **Technical Infrastructure Boundary:** `platform` is a shared technical infrastructure boundary and is not a business capability module. Because it resides beneath the application root, its treatment in Spring Modulith module detection must be explicitly configured and verified so that it does not become an unintended business application module. The implementation shall use Spring Modulith's supported shared/excluded/custom module-detection mechanism as appropriate, with automated verification preventing business-module dependencies on `platform` and preventing `platform` from depending on business modules.

## 17.2 Sub-Package Governance

Concrete technical sub-packages within `platform/` are established by the respective technical implementation documents (**AEOS-P04-D04** for Web MVC hooks, **AEOS-P04-D06** for Security plumbing, **AEOS-P04-D09** for Property/Environment hooks). D01 establishes the strict boundary rules for `platform/` without pre-empting those specific technical design documents.

---

# 18. Cross-Module Boundary Enforcement

Synchronous and asynchronous inter-module collaboration rules established by AEOS-P04-D00 §23 are enforced at the backend implementation level as follows.

## 18.1 Synchronous Boundary Enforcement

1. **Authoritative Boundary:** The `contracts` package is the authoritative synchronous cross-module collaboration boundary. Its exposure to consuming modules must be explicitly modeled through Spring Modulith named interfaces or an equivalent verified module-configuration mechanism. ArchUnit shall additionally enforce that cross-module dependencies do not bypass this boundary.
2. **Public Application Services:** A public application service of a producing module is accessible to other modules **only** when declared through, or exposed via, the producing module's `contracts` surface.
3. **Boundary Controls:** Java `public` modifiers on classes within a module's internal packages do **not** grant cross-module access authority. Spring Modulith verification and ArchUnit rules treat non-`contracts` internal packages as private to the module, failing the build if an unauthorized cross-module import occurs. Concrete package layout and contract organization are established by **AEOS-P04-D02**.

## 18.2 Asynchronous Boundary Enforcement

1. **Formal Surface:** The `events` boundary (`com.anverraglobal.<module>.events`) is the **sole approved asynchronous cross-module collaboration surface**.
2. **Event Terminology Strictness:** Per AEOS-P04-D00 §16.0:
   - `Domain Event` (internal to module) $\neq$ `Integration Event` (public contract in `events`) $\neq$ `Durable Event Publication` (Spring Modulith infrastructure) $\neq$ `External Message Broker` (PROHIBITED).
3. **In-Process Mechanism:** All asynchronous event publication uses Spring Modulith durable in-process events backed by PostgreSQL. No external message broker dependencies (Kafka, RabbitMQ) are permitted in project build files. Concrete event payload types and listener models are established by **AEOS-P04-D05**.

---

# 19. Build Lifecycle

Backend compilation, testing, architectural verification, and packaging follow standard Apache Maven build lifecycle phases.

- **`test` phase:** The project shall configure architectural verification tests (ArchUnit and Spring Modulith) to execute during the Maven test lifecycle as developer inner-loop structural verification.
- **`package` phase:** Assembles the Spring Boot application archive.
- **`verify` phase:** The `verify` phase shall serve as the project's final pre-artifact verification gate for checks explicitly bound to that phase in CI/CD environments.

---

# 20. Dependency Management

To prevent transitive dependency conflicts and ensure synchronized library versions across all backend components, Maven dependency management is centralized in the master project build file.

## 20.1 Centralized BOM Imports

Project dependency versions are governed using imported Bill of Materials (BOM) files in `<dependencyManagement>`:
- Official Spring Boot Dependencies BOM (`spring-boot-dependencies`)
- Official Spring Modulith BOM (`spring-modulith-bom`)

## 20.2 Governance Rules

- Individual business module build declarations must not specify explicit version tags for dependencies managed by imported BOMs.
- Adding unapproved third-party framework dependencies without architecture review is prohibited.
- Technical dependencies and testing libraries shall have their versions centrally governed. Dependencies managed by imported BOMs shall inherit those BOM versions; dependencies not covered by an approved BOM shall use centrally defined explicit versions. Concrete dependency artifact definitions belong to the respective technical implementation documents (**AEOS-P04-D03** for persistence, **AEOS-P04-D04** for web, etc.).

---

# 21. Developer Workflow

Developers and AI coding agents follow a consistent local development inner-loop workflow:

1. **Localized Edit:** Implement code within the established module boundaries.
2. **Local Verification:** Execute `mvn test` using the Maven Wrapper.
3. **Automated Verification Execution:**
   - The project configures architectural verification tests to execute during the test lifecycle.
   - Business unit tests execute.
   - Spring Modulith verifies module package encapsulation boundaries.
   - ArchUnit verifies layer dependency directions and unconditional domain purity.
4. **Boundary Violation Resolution:** If an architectural check fails (e.g., attempting to import internal application classes from another module), the developer must refactor the interaction to use an exposed `contracts` interface or published `events` type before committing.
5. **Commit Gate:** Code is committed only after local verification passes cleanly.

---

# 22. CI/CD Build Integration

Automated Continuous Integration (CI) pipelines enforce architecture governance on every repository commit and pull request.

## 22.1 Execution Requirements

CI runners shall execute the Maven verification lifecycle using the Maven Wrapper (`./mvnw clean verify`) and the approved Java runtime environment.

## 22.2 Hard CI Failure Conditions

The CI pipeline **MUST FAIL** immediately if:
- Any unit or integration test fails.
- Spring Modulith verification detects an unauthorized cross-module internal package reference.
- ArchUnit detects a framework import in any domain package.
- ArchUnit detects a reverse dependency from `platform` onto a business module.
- ArchUnit detects a circular dependency between top-level packages.
- Manual rule suppression annotations are introduced without architectural approval.

---

# 23. Testing Integration Boundary

D01 defines the structural placement and build lifecycle execution points for architecture verification tests.

- **Placement:** Architecture verification tests reside under `src/test/java/com/anverraglobal/`.
- **Execution:** The project shall configure architectural verification tests to execute during the Maven test lifecycle.
- **Scope:** D01 governs structural architecture tests (Spring Modulith verification and ArchUnit rules).
- **Deferred:** Testcontainers setup, database integration test harnesses, Mockito conventions, and test coverage thresholds are governed by **AEOS-P04-D08 (Testing Architecture)**.

---

# 24. Observability Integration Boundary

- **Technical Concept:** Logging framework integration and Micrometer tracing hooks are anchored conceptually under `platform`.
- **Module Usage:** Business modules utilize standard logging abstractions (SLF4J `Logger`). Modules do not import vendor-specific logging implementations.
- **Deferred:** Specific log aggregation servers, Prometheus/Grafana metric exporters, OpenTelemetry collectors, and tracing exporters belong to platform deployment design and later Phase 4 documents.

---

# 25. Security Integration Boundary

- **Technical Concept:** Spring Security is an established technology baseline. Security is cross-cutting infrastructure, while Identity remains the business authority for identity and access capabilities (AEOS-P02-S02-D01).
- **Domain Purity:** Zero security annotations (`@PreAuthorize`, `@Secured`) or SecurityContext imports are permitted in any module's `domain/` layer.
- **Deferred:** JWT claims, OAuth/OIDC providers, token parsing, password hashing algorithms, and method security setups are governed by **AEOS-P04-D06 (Security Implementation Architecture)**.

---

# 26. Configuration Integration Boundary

- **File Baseline:** Backend application configuration is externalized in `src/main/resources/application.yml`.
- **Deferred:** Active environment profile names (`dev`, `staging`, `prod`), property namespacing rules, Flyway migration locations, DataSource parameters, and secrets management boundaries are governed by **AEOS-P04-D09 (Configuration & Environment Architecture)**.

---

# 27. AI Development Constraints

All AI coding agents working on the AnverraGlobal backend **MUST** adhere to the following 22 mandatory constraints. These rules are architectural requirements and must be enforced through automated verification wherever technically applicable.

1. **Read Governing Docs First:** Read D00 and D01 before proposing or writing backend Java code.
2. **Preserve Monolith:** Preserve the single-process Modular Monolith. Never create microservices, separate deployables, or multi-jar builds.
3. **Respect Root Package:** Place all backend code under package `com.anverraglobal`. Never invent alternative root package paths.
4. **Respect Seven Modules:** Organize business logic strictly within the seven approved capability modules (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`). Never invent new business modules.
5. **Protect Unresolved Capabilities:** Never create packages, classes, interfaces, or placeholders for `agent`, `dealer`, `partner`, `organization`, `proposal`, `document`, `kyc`, or `administration`.
6. **No Inter-Module Network Calls:** Never introduce HTTP, REST, gRPC, or socket network calls between modules within the backend.
7. **No External Brokers:** Never introduce Kafka, RabbitMQ, Pulsar, or Redis Streams. All async messaging MUST use Spring Modulith durable in-process events.
8. **Synchronous Access via `contracts` Only:** Cross-module synchronous calls MUST target interfaces declared in or exposed through the producing module's `contracts` surface.
9. **No Access to Private Sub-packages:** Never import classes from another module's `domain/`, internal `application/`, or `infrastructure/` packages.
10. **Unconditional Domain Purity:** Never add Spring annotations (`@Component`, `@Service`, `@Autowired`), JPA/JDBC annotations (`@Entity`, `@Table`, `@Column`), HTTP imports, or Security imports inside any `domain/` package.
11. **Platform Layer Purity:** Never put business logic, domain entities, or business capability classes inside `platform`.
12. **No Reverse Platform Dependencies:** Code in `platform` MUST NEVER import code from any business module.
13. **Declarative Maven Only:** Never add inline script plugins, dynamic Groovy/Kotlin execution snippets, or dynamic version ranges to build configuration files.
14. **Centralized Versioning:** Never add hardcoded `<version>` tags to individual Spring or module dependencies in `pom.xml`. Versions MUST inherit from imported BOMs.
15. **No Business Tables Invented:** Never invent business database schemas, table definitions, columns, or Flyway SQL migration scripts in D01.
16. **No API Endpoints Invented:** Never invent REST endpoints, URL paths, DTO schemas, or controllers for business operations in D01.
17. **No Business Events Invented:** Never invent concrete integration event names (e.g., `PolicyCreated`) or event payload DTOs in D01.
18. **No Security Claims Invented:** Never invent JWT claims, OAuth providers, or role hierarchies in D01.
19. **Do Not Pre-resolve O3:** Do not enforce adapter directory naming (`interfaces/` vs `adapter/inbound/`) in D01 (assigned to **AEOS-P04-D02**).
20. **Do Not Pre-resolve O6/O8:** Do not define PostgreSQL schema naming or DataSource bean configurations in D01 (assigned to **AEOS-P04-D03**).
21. **Do Not Pre-resolve O4/O5:** Do not select OpenAPI code-first vs design-first or client generation tools in D01 (assigned to **AEOS-P04-D04** / **AEOS-P04-D07**).
22. **Stop and Escalate on Ambiguity:** If an implementation step requires an unestablished business rule, API endpoint, or database table, stop and report the gap rather than inventing a solution.

---

# 28. Rejected Alternatives

## 28.1 Rejected Build Tool — Gradle (`build.gradle.kts`)

- **Evaluated:** Gradle with Groovy or Kotlin DSL.
- **Reason for Rejection:** Gradle remains technically capable of reproducible and highly governed builds. It is rejected here primarily because the project prioritizes Maven's conventional lifecycle, enterprise familiarity, explicit POM model, and simpler governance model for the intended AI-assisted workflow.

## 28.2 Rejected Root Package — `com.anverra.global`

- **Evaluated:** Two-segment domain packaging `com.anverra.global`.
- **Reason for Rejection:** Creates a two-tier package hierarchy (`com.anverra` containing `global`) that requires custom package filter configuration when declaring Spring Modulith module boundaries. Package `com.anverraglobal` provides a single, clean root anchor matching canonical system identity `AnverraGlobal`.

---

# 29. Architectural Consequences

## 29.1 Positive Consequences

- **Build Governance:** Maven provides a standardized, convention-driven project model and lifecycle. Its POM is declarative in structure, giving AnverraGlobal a comparatively explicit and inspectable build configuration model.
- **Automated Boundary Enforcement:** Spring Modulith structural verification and ArchUnit rules detect encapsulation leaks and domain purity violations during automated build test execution.
- **AI Governance Alignment:** Conventional build structure and clear root package anchors assist AI agents in avoiding improper cross-module package references.
- **Decoupled Domain Core:** Hexagonal layer dependency enforcement keeps business domain logic decoupled from framework and database mechanics.

## 29.2 Trade-offs

- **Explicit Contract Governance:** Synchronous inter-module collaboration requires declaring public interfaces in `contracts`, prohibiting direct invocation of private application service implementations.

---

# 30. Risks and Mitigations

| Identified Risk | Severity | Mitigation Strategy |
|---|---|---|
| Developer or AI attempts to bypass `contracts` by marking an internal class `public` | High | Spring Modulith verification and ArchUnit Rule Category 1 fail test execution if private sub-packages are imported across modules. |
| Framework annotations accidentally introduced into `domain/` model classes | High | ArchUnit Rule Category 3 scans `com.anverraglobal..<module>.domain..` on every build and fails if any Spring, JPA, JDBC, or Security import exists. |
| Unintended build script complexity injected into build pipeline | Medium | Apache Maven enforced; project build configuration uses declarative XML schema structure. |
| Transitive dependency version drift across modules | Medium | Centralized Maven `<dependencyManagement>` importing official Spring Boot and Spring Modulith BOMs. |
| Business logic leaking into `platform/` package | Medium | ArchUnit Rule Category 4 prohibits `platform` from referencing any business module package. |

---

# 31. Implementation Constraints

1. **One Application Process:** All backend code MUST compile into and execute within the single `AnverraApplication` process.
2. **Maven Build Tool Only:** The build system MUST use Apache Maven (`pom.xml`) with the root Maven Wrapper (`mvnw`).
3. **Root Package Compliance:** All Java source files MUST reside under package `com.anverraglobal`.
4. **Module Package Boundaries:** Business logic MUST be placed within one of the seven approved capability modules (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`).
5. **No Placeholders for Unresolved Capabilities:** No package or directory may be created for Agent, Dealer, Partner, Organization, Proposal, Document, KYC, or Administration.
6. **Zero Framework Annotations in Domain:** The `domain/` package of every module MUST remain 100% free of framework, ORM, and web annotations.
7. **`contracts` Surface Enforcement:** Synchronous cross-module imports MUST target interfaces declared in or exposed through `com.anverraglobal.<module>.contracts`.
8. **No Microservices or External Brokers:** No microservices, internal REST clients between modules, Kafka, or RabbitMQ dependencies may be introduced.

---

# 32. Deferred Decisions

The following decisions are explicitly deferred to subsequent Phase 4 architectural documents:

| Decision Concern | Owning Document | Status in D01 |
|---|---|---|
| Module-internal package layout & adapter directory naming (**O3**: `interfaces/` vs `adapter/inbound/`) | **AEOS-P04-D02** | Deferred |
| PostgreSQL physical isolation, schema naming (**O6**), DataSource pattern (**O8**), & Flyway layout | **AEOS-P04-D03** | Deferred |
| YAML environment profiles, property namespacing rules, & secrets management boundary | **AEOS-P04-D09** | Deferred |
| OpenAPI implementation approach (**O4**), REST DTO field specs, & HTTP controller endpoints | **AEOS-P04-D04** | Deferred |
| Integration event payload schemas, listener transaction models, & idempotency mechanisms (**O7**) | **AEOS-P04-D05** | Deferred |
| Authentication mechanisms, JWT/OAuth providers, & Spring Security filter implementations | **AEOS-P04-D06** | Deferred |
| Client source structure, client generation tooling (**O5**), & cross-client type sharing (**O9**) | **AEOS-P04-D07** | Deferred |
| Testcontainers configuration, integration test suite layout, & test dataset strategy | **AEOS-P04-D08** | Deferred |
| Physical multi-root repository folder tree layout | **AEOS-P04-D10** | Deferred |
| Final Phase 4 implementation readiness verification & readiness declaration | **AEOS-P04-D11** | Deferred |

---

# 33. Definition of Done

AEOS-P04-D01 is complete and eligible for baseline candidate status when:

- [x] Document identity, version, and status are established.
- [x] Open Decision **O1** (Build Tool) is explicitly evaluated and resolved as **Apache Maven**.
- [x] Open Decision **O2** (Java Root Package Naming) is explicitly evaluated and resolved as **`com.anverraglobal`**.
- [x] Backend source directory layout baseline (`src/main/java`, `src/test/java`, etc.) is fully defined.
- [x] Spring Boot application entry point placement and component scanning boundary rules are established.
- [x] Spring Modulith application module registration, boundary modeling, and automated verification architecture are established.
- [x] ArchUnit test framework, mandatory rule categories, and automated build triggers are established.
- [x] Hexagonal dependency directions and inward-flowing layer constraints are explicitly documented.
- [x] Domain layer purity rule is established as an unconditional prohibition on framework annotations in `domain/`.
- [x] Shared `platform/` technical infrastructure boundary, zero-business-logic rule, and reverse-dependency prohibitions are established.
- [x] Cross-module synchronous (`contracts`) and asynchronous (`events`) boundary enforcement rules are explicit.
- [x] Maven build lifecycle phases and centralized BOM dependency management are established.
- [x] CI/CD build integration and hard failure criteria are explicit.
- [x] Consolidated AI development constraints (22 rules) are explicitly documented.
- [x] Rejected alternatives (Gradle, multi-segment package) and architectural consequences are documented.
- [x] Deferred decisions (D02–D11) are mapped explicitly without pre-empting later documents.
- [x] Traceability to Phase 1, Phase 2, Phase 3, and Phase 4 D00 is complete.
- [x] Anti-invention check is clean across all categories.

---

# 34. Traceability

This document (AEOS-P04-D01) explicitly traces to and operationalizes the following authoritative repository documents:

## 34.1 Traceability to Phase 1 (Engineering Constitution)
- **AEC-ARC-001 (Architecture First):** Build tool and package structure serve established architecture.
- **AEC-ARC-003 (Modular Monolith):** Single deployable application process running all 7 modules.
- **AEC-ARC-004 (Hexagonal Architecture):** Inward dependency directions enforced by ArchUnit.
- **AEC-ARC-005 (Separation of Concerns):** Layer separation across domain, application, adapters, and platform.
- **AEC-ARC-006 (Dependency Direction):** Inward flow; zero outward domain dependencies.
- **AEC-ARC-007 (Module Boundaries):** Encapsulation enforced by Spring Modulith and ArchUnit.
- **AEC-ARC-008 (Capability Ownership):** Modules mapped 1:1 to single business capabilities.
- **AEC-ARC-009 (Explicit Contracts):** `contracts` surface as sole synchronous cross-module path.
- **AEC-ARC-010 (Event-Driven Collaboration):** `events` surface and in-process durable publication.
- **AEC-ARC-012 (Evolutionary Architecture):** Decisions O1 and O2 recorded with explicit rationale.
- **AEC-ARC-013 (Architecture Decision Records):** Traceable decision records for O1 and O2.
- **AEC-ARC-014 (Architecture Review):** Verified against constitutional guidelines.

## 34.2 Traceability to Phase 2 (Application & Repository Blueprint)
- **AEOS-P02-S01-D01 (System Blueprint):** System identity `AnverraGlobal` and backend monolith classification.
- **AEOS-P02-S01-D02 (Repository Architecture):** Monorepo backend structural foundation.
- **AEOS-P02-S01-D03 (Application Boundaries):** Backend boundary separate from Web and Mobile clients.
- **AEOS-P02-S01-D04 (Architectural Boundaries):** Module encapsulation and public surface rules.
- **AEOS-P02-S01-D05 (Blueprint Traceability):** Cross-document consistency baseline.
- **AEOS-P02-S02-D00 through D07 (Module Blueprints):** 7 approved business capability modules (`notification` and `reporting` classified as Supporting Business).

## 34.3 Traceability to Phase 3 (Technology Selection)
- **AEOS-P03-D00 (Phase 3 Overview):** Approved-for-Planning technology baseline.
- **AEOS-P03-D01 (Backend Technology):** Java 21, Spring Boot 3.x, Spring Modulith, ArchUnit.
- **AEOS-P03-D02 (Persistence Technology):** PostgreSQL, Spring Data JDBC, Flyway context.
- **AEOS-P03-D03 (API & Transport Technology):** REST, JSON, OpenAPI 3.0 context.
- **AEOS-P03-D04 (Messaging Technology):** Spring Modulith durable in-process events.

## 34.4 Traceability to Phase 4 (System Design & Implementation Planning)
- **AEOS-P04-D00 (System Design Overview):** Immediate governing authority; operationalizes O1 and O2.

---

# 35. Decision Status

| Open Decision ID | Decision Description | Resolving Document | Status in D01 | Resolution Summary |
|---|---|---|---|---|
| **O1** | Build tool: Maven vs. Gradle | **AEOS-P04-D01** | **RESOLVED** | **Apache Maven** selected for convention-driven lifecycle, BOM alignment, and AI safety. |
| **O2** | Java root package naming convention | **AEOS-P04-D01** | **RESOLVED** | **`com.anverraglobal`** selected for clean Spring Modulith module discovery and identity alignment. |
| **O3** | Inbound adapter directory naming (`interfaces/` vs `adapter/inbound/`) | AEOS-P04-D02 | OPEN | Assigned to AEOS-P04-D02; not pre-empted in D01. |
| **O4** | OpenAPI approach (code-first vs design-first) | AEOS-P04-D04 | OPEN | Assigned to AEOS-P04-D04; not pre-empted in D01. |
| **O5** | OpenAPI client generation tool | AEOS-P04-D07 | OPEN | Assigned to AEOS-P04-D07; not pre-empted in D01. |
| **O6** | PostgreSQL schema naming convention | AEOS-P04-D03 | OPEN | Assigned to AEOS-P04-D03; not pre-empted in D01. |
| **O7** | Event listener idempotency mechanism | AEOS-P04-D05 | OPEN | Assigned to AEOS-P04-D05; not pre-empted in D01. |
| **O8** | DataSource configuration pattern | AEOS-P04-D03 | OPEN | Assigned to AEOS-P04-D03; not pre-empted in D01. |
| **O9** | Shared vs. independent client API types | AEOS-P04-D07 | OPEN | Assigned to AEOS-P04-D07; not pre-empted in D01. |

---

# 36. Final Baseline Status

| Attribute | Value |
|---|---|
| **Document ID** | AEOS-P04-D01 |
| **Document Title** | Backend Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Authoring Position** | 2 |
| **Open Decision O1 (Build Tool)** | **RESOLVED → Apache Maven** |
| **Open Decision O2 (Root Package)** | **RESOLVED → `com.anverraglobal`** |
| **Governance Coverage** | Complete (36 sections) |
| **Anti-Invention Self-Check** | PASSED (Clean across all categories) |
| **Phase 1-3 Compliance** | Verified (Zero contradictions) |
| **D00 Governance Compliance** | Verified (Full alignment) |
| **Ready for Review** | Yes — Pending Project Lead Review & Baseline Approval |

---
