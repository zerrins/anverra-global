# Persistence Implementation Architecture

**Document ID:** AEOS-P04-D03  
**Version:** 1.0  
**Status:** Baseline Candidate  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 4  
**Depends on:** Phase 1 Engineering Constitution · AEOS-P02-S01-D01 through D05 · AEOS-P02-S02-D00 through D07 · AEOS-P03-D00 through D05 · AEOS-P04-D00 · AEOS-P04-D01 · AEOS-P04-D02  

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D03 |
| **Title** | Persistence Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Authoring Position** | 4 |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |
| **Immediately Preceding Document** | AEOS-P04-D02 — Module Implementation Architecture |

---

# 2. Purpose

This document establishes the authoritative persistence implementation architecture for the AnverraGlobal Modular Monolith backend.

Where Phase 1 established data ownership principles, Phase 2 defined application boundaries, Phase 3 selected PostgreSQL, Spring Data JDBC, and Flyway as the persistence baseline, Phase 4 D00 established overall governance, D01 established Maven and Java root package conventions, and D02 established canonical module layouts and unconditional domain purity, this document (AEOS-P04-D03) establishes how persistence is physically structured and executed.

Specifically, this document:
1. Evaluates and formally resolves Open Decision **O6** (PostgreSQL Schema Naming Strategy).
2. Evaluates and formally resolves Open Decision **O8** (DataSource Configuration Pattern).
3. Defines the Spring Data JDBC repository integration pattern inside `<module>.adapter.outbound.persistence`.
4. Establishes the strict separation between Pure Domain Models (`domain/`) and Relational Persistence Entities (`adapter.outbound.persistence/`).
5. Establishes how persistence adapters implement application-owned outbound ports (`port.outbound`).
6. Defines the Flyway migration architecture, module migration ownership, and script directory organization.
7. Establishes technical enforcement mechanisms intended to prevent direct database coupling across business modules.
8. Defines transaction boundary placement, exception translation, Testcontainers integration testing, HikariCP connection pooling, and persistence observability.
9. Provides explicit AI implementation governance rules for backend persistence authoring.

> "D03 establishes how data persistence is physically structured, isolated, and executed across all business modules. It does not define specific business database tables, columns, indexes, Flyway SQL DDL scripts, or domain entity fields, which remain governed by authoritative business requirements and downstream implementation tasks."

---

# 3. Scope

## 3.1 In Scope

The scope of this document is strictly limited to backend persistence implementation architecture:
- Evaluation and resolution of Open Decision **O6** (PostgreSQL Schema Naming Strategy).
- Evaluation and resolution of Open Decision **O8** (DataSource Configuration Pattern).
- Technical organization of outbound persistence adapters inside `com.anverraglobal.<module>.adapter.outbound.persistence`.
- Implementation relationship between Spring Data JDBC repositories, persistence adapters, and application-owned outbound ports (`port.outbound`).
- Model separation between Pure Domain Models and Relational Persistence Entities.
- Flyway database migration architecture, module script directory conventions (`db/migration/<module>/`), and versioning rules.
- Technical enforcement of cross-module data isolation (ArchUnit rules, Spring Modulith verification, database roles/grants).
- Transaction boundary placement and rules for `@Transactional` usage outside `domain/`.
- Identifier generation strategy: Application-generated UUID-based identifiers as the preferred identifier family; specific UUID version/generation strategy remains subject to implementation standards.
- Concurrency control: Evaluation and implementation guidance for optimistic locking where applicable.
- Relational naming conventions for schemas, tables, columns, indexes, foreign keys, and constraints.
- HikariCP connection pool configuration strategy.
- Persistence exception translation architecture (`DataAccessException` ➔ Application Exception).
- Persistence integration testing strategy using Testcontainers and `@DataJdbcTest`.
- Persistence observability (HikariCP metrics, Micrometer, slow query logging).
- Governance of PostgreSQL-specific features (JSONB, arrays, custom types).
- AI implementation governance rules for database persistence authoring.

## 3.2 Explicitly Out of Scope

The following concerns are explicitly outside the scope of D03 and belong to designated Phase 4 design documents or downstream implementation tasks:
- **O4:** OpenAPI implementation approach — assigned to **AEOS-P04-D04**.
- **O5:** OpenAPI client generation — assigned to **AEOS-P04-D07**.
- **O7:** Event listener idempotency mechanism — assigned to **AEOS-P04-D05**.
- **O9:** Shared vs independently generated client API types — assigned to **AEOS-P04-D07**.
- Business database table schemas, column DDLs, index definitions, foreign key constraints, or SQL scripts for specific features.
- Domain entity attributes, value object fields, or aggregate root definitions.
- Modeling or persistence design for unresolved business capabilities (`Agent`, `Sub-Agent`, `Dealer`, `Partner`, `Organization`, `Proposal`, `Document`, `KYC`, `Administration`).
- REST controller mappings, HTTP DTOs, or OpenAPI validation annotations — assigned to **AEOS-P04-D04**.
- Event listener retry queues, dead-letter tables, or outbox schema details — assigned to **AEOS-P04-D05**.
- Security filter chains, OAuth2/OIDC token stores, or authorization matrix tables — assigned to **AEOS-P04-D06**.
- Production connection pool capacity tuning based on unverified workload assumptions.

---

# 4. Architectural Context

The AnverraGlobal system executes as a single-process **Modular Monolith** running on Java 21, Spring Boot 3, and PostgreSQL within a single JVM, as established by Phase 1 ([AEC-ARC-003](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-003-modular-monolith.md)), Phase 2 ([AEOS-P02-S01-D01](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md)), Phase 3 ([AEOS-P03-D02](file:///Users/shashank/Projects/anverra-global/docs/03-technology/02-persistence-blueprint.md)), and Phase 4 D00 ([AEOS-P04-D00](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/00-phase-4-overview.md)).

Prior Phase 4 documents established the backend implementation baseline:
- **Build Tool (O1):** Apache Maven (`pom.xml`) — resolved in D01 ([AEOS-P04-D01](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/01-backend-implementation-architecture.md)).
- **Java Root Package (O2):** `com.anverraglobal` — resolved in D01.
- **Inbound Adapter Directory (O3):** `adapter/inbound/` & `adapter/outbound/` — resolved in D02 ([AEOS-P04-D02](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/02-module-implementation-architecture.md)).
- **Domain Purity Rule:** `com.anverraglobal.<module>.domain` has ZERO framework, Spring, or relational annotations — established in D02.
- **Outbound Ports:** Application-owned interfaces inside `<module>.port.outbound` — established in D02.
- **Persistence Adapter Location:** `<module>.adapter.outbound.persistence` — established in D02.

D03 builds directly upon these baselines by defining the physical persistence architecture for the backend database layer.

```
com.anverraglobal.<module>/
├── domain/                                    (Pure Business Models — ZERO Relational Annotations)
├── application/                               (Use-Case Orchestration Services — @Transactional Boundary)
├── port/
│   └── outbound/                              (Application-Owned Outbound Repository Ports)
└── adapter/
    └── outbound/
        └── persistence/                       (Persistence Adapters & Relational Mapping)
            ├── <Concept>PersistenceAdapter.java (Implements Port; Calls Spring Data JDBC)
            ├── <Concept>RelationalEntity.java   (@Table, @Id, @Column Relational Representation)
            ├── <Concept>JdbcRepository.java     (Private Spring Data CrudRepository Interface)
            └── <Concept>PersistenceMapper.java  (Maps Relational Entity ↔ Pure Domain Model)
```

---

# 5. Persistence Implementation Model

AnverraGlobal persistence operates on three core principles:

1. **Constitutional Data Ownership:** Each of the seven approved business modules (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`) exclusively owns its authoritative persistence. No module may directly read or write another module's database tables.
2. **Hexagonal Persistence Decoupling:** The domain layer defines pure domain models. The application layer defines outbound repository ports (`port.outbound`). Concrete database access code resides exclusively inside `adapter.outbound.persistence`.
3. **Aggregate-Oriented Relational Mapping:** Spring Data JDBC maps aggregate roots to relational tables without Hibernate proxying, session caching, or lazy loading side-effects.

```
┌──────────────────────────────────────────────────────────────────────────────────────────┐
│                                   BUSINESS MODULE LAYER                                  │
│                                                                                          │
│  ┌───────────────────────────┐                    ┌───────────────────────────────────┐  │
│  │       DOMAIN LAYER        │                    │         APPLICATION LAYER         │  │
│  │   (Pure Java Models)      │                    │    (Use-Case Services; Tx Boundary)│  │
│  │   PureDomainModel.java    │◄───────────────────┤                                   │  │
│  └───────────────────────────┘    (uses model)    └─────────────────┬─────────────────┘  │
│                                                                     │                    │
│                                                                     │ (uses port)        │
│                                                                     ▼                    │
│                                                   ┌───────────────────────────────────┐  │
│                                                   │       OUTBOUND PORT LAYER         │  │
│                                                   │    RepositoryPort.java (Interface)│  │
│                                                   └─────────────────▲─────────────────┘  │
│                                                                     │                    │
│                                                                     │ (implements)       │
│  ┌──────────────────────────────────────────────────────────────────┴─────────────────┐  │
│  │                             OUTBOUND PERSISTENCE ADAPTER                           │  │
│  │                        (adapter.outbound.persistence)                              │  │
│  │                                                                                    │  │
│  │  ┌─────────────────────────────┐   maps ↔    ┌──────────────────────────────────┐  │  │
│  │  │ PersistenceAdapter.java     │────────────►│ PersistenceMapper.java           │  │  │
│  │  └──────────────┬──────────────┘             └──────────────────────────────────┘  │  │
│  │                 │ (uses)                                                           │  │
│  │                 ▼                                                                  │  │
│  │  ┌─────────────────────────────┐             ┌──────────────────────────────────┐  │  │
│  │  │ SpringDataJdbcRepository    │────────────►│ RelationalEntity.java            │  │  │
│  │  │ (CrudRepository Interface)  │  persists   │ (@Table, @Id, @Column)           │  │  │
│  │  └─────────────────────────────┘             └──────────────────────────────────┘  │  │
│  └─────────────────────────────────────────┬──────────────────────────────────────────┘  │
└────────────────────────────────────────────┼─────────────────────────────────────────────┘
                                             │ SQL / JDBC
                                             ▼
┌──────────────────────────────────────────────────────────────────────────────────────────┐
│                                  POSTGRESQL DATABASE                                     │
│                                                                                          │
│  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐ ┌────────────┐  │
│  │ identity      │ │ customer      │ │ product       │ │ policy        │ │ ...        │  │
│  │ schema        │ │ schema        │ │ schema        │ │ schema        │ │ schemas    │  │
│  └───────────────┘ └───────────────┘ └───────────────┘ └───────────────┘ └────────────┘  │
└──────────────────────────────────────────────────────────────────────────────────────────┘
```

---

# 6. PostgreSQL Baseline Standards

D03 inherits **PostgreSQL** as the selected relational persistence engine from Phase 3 ([AEOS-P03-D02](file:///Users/shashank/Projects/anverra-global/docs/03-technology/02-persistence-blueprint.md)).

## 6.1 PostgreSQL Engine Requirements
- **Supported PostgreSQL Major Release:** Use a PostgreSQL major version within the PostgreSQL project's supported lifecycle. The project will pin a specific supported major version through environment/build/deployment configuration rather than treating "LTS" as a PostgreSQL release classification.
- **Encoding:** UTF-8 encoding (`UTF8` character set). Database locale and collation strategy remain governed by operational deployment requirements.
- **Time Zone:** Coordinated Universal Time (`UTC`) enforced at database, connection, and JVM levels.
- **ACID Compliance:** Standard PostgreSQL Read Committed isolation level default.

## 6.2 Relational Design Defaults
- Primary keys must be immutable and non-business identifiers (application-generated UUID-based identifiers).
- Foreign key constraints must be enforced strictly within a single module schema boundary.
- **HARD ARCHITECTURAL RULE:** Foreign keys MUST NOT reference another module's database schema. Cross-module data relationships must be represented logically via approved module contracts or integration events rather than database-level foreign key constraints (e.g. `policy.customer_id` must NOT have a PostgreSQL foreign key constraint referencing `customer.customers(id)`).
- Indexes must be created explicitly to support query access patterns.
- Table and column names must follow lowercase snake_case conventions.

---

# 7. Spring Data JDBC Architecture

D03 inherits **Spring Data JDBC** as the aggregate persistence framework from Phase 3 ([AEOS-P03-D02](file:///Users/shashank/Projects/anverra-global/docs/03-technology/02-persistence-blueprint.md)).

## 7.1 Key Spring Data JDBC Characteristics
Unlike JPA/Hibernate, Spring Data JDBC:
- Has **no session**, **no L1/L2 cache**, **no dirty checking**, and **no proxy objects**.
- Loads and persists aggregate roots in their entirety.
- Uses simple SQL queries emitted directly via standard JDBC.
- Requires explicit model saves via repository calls.

## 7.2 Repository Abstraction Rules
- Spring Data JDBC repository interfaces extend `org.springframework.data.repository.CrudRepository` or `PagingAndSortingRepository`.
- Repositories reside exclusively inside `com.anverraglobal.<module>.adapter.outbound.persistence`.
- Repositories are package-private or internal implementation components. They are NEVER exposed outside `adapter.outbound.persistence`.

---

# 8. Domain vs. Persistence Model Separation

## 8.1 Non-Negotiable Model Separation Rule
Phase 4 D02 ([AEOS-P04-D02](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/02-module-implementation-architecture.md)) established unconditional domain purity. Domain objects inside `com.anverraglobal.<module>.domain` MUST NOT contain relational or framework annotations.

> [!IMPORTANT]
> **CONSTITUTIONAL MANDATE:** Relational mapping annotations (`@Table`, `@Id`, `@Column`, `@MappedCollection`, `@PersistenceCreator`, `@Version`) MUST NOT be added to pure domain objects. They belong exclusively to separate Relational Entity classes residing inside `adapter.outbound.persistence`.

## 8.2 Model Comparison & Mapping

| Model Type | Package Path | Annotations & Libraries | Purpose |
|---|---|---|---|
| **Pure Domain Model** | `com.anverraglobal.<module>.domain` | Pure Java only (ZERO framework imports) | Encapsulates core business invariants and business rules |
| **Relational Entity** | `com.anverraglobal.<module>.adapter.outbound.persistence` | `@Table`, `@Id`, `@Column`, `@Version`, Spring Data JDBC | Maps to PostgreSQL table schema for JDBC persistence |

```java
// CONCEPTUAL EXAMPLE — Pure Domain Model (domain/)
package com.anverraglobal.sample.domain;

public final class SampleDomainModel {
    private final SampleId id;
    private final String name;
    // Pure business methods and constructor validation; NO framework annotations
}
```

```java
// CONCEPTUAL EXAMPLE — Relational Entity (adapter/outbound/persistence/)
package com.anverraglobal.sample.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "sample_table", schema = "sample_schema")
final class SampleRelationalEntity {
    @Id
    private UUID id;
    private String name;
    @Version
    private Long version;
    // Framework getters/setters/constructors for JDBC mapping
}
```

---

# 9. Persistence Adapter Layout

Every business module implements persistence inside a dedicated sub-package under `adapter.outbound`:

```text
com.anverraglobal.<module>.adapter.outbound.persistence/
├── <Concept>PersistenceAdapter.java     (Implements Outbound Port; Public Spring @Component)
├── <Concept>RelationalEntity.java       (Spring Data JDBC Entity; Package-Private)
├── <Concept>JdbcRepository.java         (Spring Data CrudRepository; Package-Private)
└── <Concept>PersistenceMapper.java      (Stateless Entity ↔ Domain Mapper; Package-Private)
```

## 9.1 Component Responsibilities

1. **`PersistenceAdapter`:** Implements the application-owned outbound port interface from `port.outbound`. It delegates database execution to `JdbcRepository` and uses `PersistenceMapper` to convert objects.
2. **`RelationalEntity`:** Spring Data JDBC record/class carrying `@Table(value = "...", schema = "...")` and column annotations matching the module's PostgreSQL schema.
3. **`JdbcRepository`:** Interface extending `CrudRepository<RelationalEntity, UUID>`.
4. **`PersistenceMapper`:** Pure mapping utility that converts `RelationalEntity` to `DomainModel` and `DomainModel` to `RelationalEntity`.

---

# 10. Application-Owned Outbound Ports Integration

Following D02 §14, outbound repository ports are owned by the application layer (`port.outbound`) and expressed as technology-agnostic Java interfaces.

```text
┌────────────────────────────────────────────────────────┐
│ com.anverraglobal.<module>.port.outbound               │
│                                                        │
│   public interface SampleRepositoryPort {              │
│       Optional<SampleDomainModel> findById(SampleId id);│
│       SampleDomainModel save(SampleDomainModel model); │
│   }                                                    │
└──────────────────────────▲─────────────────────────────┘
                           │
                           │ (implements)
┌──────────────────────────┴─────────────────────────────┐
│ com.anverraglobal.<module>.adapter.outbound.persistence│
│                                                        │
│   @Component                                           │
│   final class SamplePersistenceAdapter                 │
│       implements SampleRepositoryPort {                │
│       // Delegates to JdbcRepository & Mapper          │
│   }                                                    │
└────────────────────────────────────────────────────────┘
```

- Application services depend strictly on `SampleRepositoryPort`.
- Application services NEVER import `SamplePersistenceAdapter` or `SampleJdbcRepository`.
- Inversion of Control (IoC) wires `SamplePersistenceAdapter` into application services at runtime.

---

# 11. O6 Evaluation & Resolution (PostgreSQL Schema Strategy)

Phase 4 Open Decision **O6** requires formally resolving the PostgreSQL schema naming and isolation strategy.

## 11.1 Candidate Evaluation

### Candidate A — Single Default Schema (`public`) with Table Name Prefixes
All module tables reside in the PostgreSQL `public` schema with table prefixes (e.g., `identity_users`, `policy_policies`).
- *Disadvantages:* Causes global schema clutter, prevents database-level role isolation, and increases risk of cross-module SQL joins.

### Candidate B — Logical Module-Owned Schemas in a Single PostgreSQL Database
Each of the seven approved business modules owns a dedicated PostgreSQL schema within a single database instance (`identity`, `customer`, `product`, `policy`, `commission`, `notification`, `reporting`).
- *Advantages:* Enforces strict logical isolation, enables schema-level Flyway migrations, supports schema-level DB user permissions, and aligns perfectly with the single-process Modular Monolith architecture.

### Candidate C — Separate Physical PostgreSQL Databases per Business Module
Each module connects to its own independent PostgreSQL database instance.
- *Disadvantages:* Introduces multi-database connection pooling complexity, distributed transaction overhead, and deployment friction. (Candidate C does not violate single-process Modular Monolith execution, but adds significant operational and transactional complexity).

## 11.2 Formal Resolution of O6

> [!IMPORTANT]
> **OPEN DECISION O6 IS FORMALLY RESOLVED:**  
> **Candidate B — Logical Module-Owned Schemas in a Single PostgreSQL Database** is selected as the authoritative schema architecture for AnverraGlobal.

## 11.3 Approved Schema Names

Every business module exclusively owns its matching PostgreSQL schema:

| Module | Java Package Root | PostgreSQL Schema Name |
|---|---|---|
| **Identity & Access** | `com.anverraglobal.identity` | `identity` |
| **Customer Management** | `com.anverraglobal.customer` | `customer` |
| **Product Catalogue** | `com.anverraglobal.product` | `product` |
| **Policy Lifecycle** | `com.anverraglobal.policy` | `policy` |
| **Commission Management** | `com.anverraglobal.commission` | `commission` |
| **Notification Management** | `com.anverraglobal.notification` | `notification` |
| **Reporting & Analytics** | `com.anverraglobal.reporting` | `reporting` |

---

# 12. O8 Evaluation & Resolution (DataSource Configuration Pattern)

Phase 4 Open Decision **O8** requires formally resolving the DataSource configuration pattern.

## 12.1 Candidate Evaluation

### Candidate A — Single Centralized HikariCP DataSource
A single HikariCP connection pool is configured centrally by Spring Boot and shared across all modules in the application context.
- *Advantages:* Optimal connection utilization, zero pool fragmentation, single `PlatformTransactionManager`, native Spring Boot autoconfiguration, simple local and CI setup.
- *Consideration:* Executes using a common database connection identity; requires explicit schema qualification in relational entities (`@Table(value = "...", schema = "...")`) and reliance on application/build-time isolation mechanisms.

### Candidate B — Multiple Module-Scoped DataSources
Seven separate HikariCP connection pools configured individually for each module.
- *Disadvantages:* Multiple pools introduce additional connection-pool configuration, connection management, transaction configuration, and operational complexity without architectural benefit in a single JVM.

## 12.2 Formal Resolution of O8

> [!IMPORTANT]
> **OPEN DECISION O8 IS FORMALLY RESOLVED:**  
> **Candidate A — Single Centralized HikariCP DataSource with Schema Qualification** is selected as the authoritative DataSource configuration pattern.

## 12.3 DataSource Execution Pattern
- Spring Boot autoconfigures a single `javax.sql.DataSource` backed by HikariCP.
- Relational entities explicitly specify their target schema in `@Table` (e.g., `@Table(value = "policies", schema = "policy")`).
- Connection execution operates within a single transaction manager (`JdbcTransactionManager`).
- Module isolation is primarily enforced through Java package/architecture verification, Spring Modulith verification, repository encapsulation, explicit schema ownership, SQL/repository restrictions, and database-level least privilege where a deployment topology provides distinct roles/credentials.

---

# 13. Cross-Module Data Isolation Enforcement

Phase 1 ([AEC-ARC-011](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-011-data-ownership.md)) mandates that no module may directly access another module's authoritative persistence.

D03 establishes a multi-tiered enforcement strategy that explicitly distinguishes build-time architectural verification from runtime database security:

```text
┌──────────────────────────────────────────────────────────────────────────┐
│                   CROSS-MODULE DATA ISOLATION TIERS                      │
├──────────────────────────────────────────────────────────────────────────┤
│ Tier 1: Java Package Encapsulation (Spring Data Repositories private)    │
│ Tier 2: Spring Modulith Verification (Blocks cross-module package imports)│
│ Tier 3: ArchUnit Rules (Fails build on Java-level boundary violations)  │
│ Tier 4: Database Role Permissions (Where deployment topology permits)    │
└──────────────────────────────────────────────────────────────────────────┘
```

> [!WARNING]
> **RUNTIME ISOLATION NOTICE:** Module data isolation is primarily enforced through Java package/architectural verification (ArchUnit rules — failing the build when Java-level module/persistence boundary violations are detected), Spring Modulith verification, repository encapsulation, explicit schema ownership, and SQL/repository restrictions. A shared DataSource using a common PostgreSQL database role does not by itself provide database-level isolation between module schemas. Where a deployment topology provides distinct database roles/credentials per module, database-level least privilege may be applied. Foreign keys MUST NOT cross module schema boundaries.

---

# 14. Flyway Migration Architecture

D03 establishes **Flyway** as the authoritative database schema migration tool.

## 14.1 Migration File Organization
Flyway migrations are organized per module in standard backend resources:

```text
src/main/resources/
└── db/
    └── migration/
        ├── identity/      ──► Module Migration Directory
        ├── customer/      ──► Module Migration Directory
        ├── product/       ──► Module Migration Directory
        ├── policy/        ──► Module Migration Directory
        ├── commission/    ──► Module Migration Directory
        ├── notification/  ──► Module Migration Directory
        └── reporting/     ──► Module Migration Directory
```

## 14.2 Migration Execution Lifecycle
- **Schema Creation:** The initial migration for a module must execute `CREATE SCHEMA IF NOT EXISTS <module>;`.
- **Flyway Lifecycle:** D03 supports Flyway execution during application startup for development and local testing, while supporting CI/CD-driven pre-deployment migration execution for production environments.
- **Versioning & Ordering Strategy:** When multiple module-specific locations participate in Flyway execution, a deterministic versioning strategy (such as explicit module-prefixed or timestamp-ordered version keys) and central location search path configuration must be maintained to ensure ordered, unambiguous execution.

---

# 15. Transaction Boundary Implementation

D02 established that transaction orchestration must remain outside the domain layer. D03 establishes concrete transaction implementation rules:

## 15.1 Transaction Placement Rules
- **Application Services (`application/`):** The primary location for `@org.springframework.transaction.annotation.Transactional`. Application services define transaction boundaries when orchestrating use cases.
- **Persistence Adapters (`adapter.outbound.persistence/`):** May use `@Transactional(propagation = Propagation.MANDATORY)` to ensure repository operations execute within an active application transaction.
- **Domain Layer (`domain/`):** `@Transactional` is **STRICTLY PROHIBITED** inside `domain/`.

```java
// CONCEPTUAL EXAMPLE — Application Service Transaction Boundary
package com.anverraglobal.sample.application;

import org.springframework.transaction.annotation.Transactional;

public class SampleApplicationService implements SampleInboundPort {
    
    @Override
    @Transactional
    public void executeUseCase(SampleCommand command) {
        // 1. Retrieve entity via outbound port
        // 2. Execute pure domain logic
        // 3. Persist updated entity via outbound port
    }
}
```

---

# 16. Entity Mapping Strategy & Converters

## 16.1 Stateless Persistence Mappers
To translate between pure domain models and Spring Data JDBC relational entities, each persistence adapter includes a stateless `PersistenceMapper`:

```java
// CONCEPTUAL EXAMPLE — Stateless Persistence Mapper
package com.anverraglobal.sample.adapter.outbound.persistence;

final class SamplePersistenceMapper {

    SampleRelationalEntity toRelationalEntity(SampleDomainModel domain) {
        SampleRelationalEntity entity = new SampleRelationalEntity();
        entity.setId(domain.getId().value());
        entity.setName(domain.getName());
        return entity;
    }

    SampleDomainModel toDomainModel(SampleRelationalEntity entity) {
        return SampleDomainModel.reconstitute(
            new SampleId(entity.getId()),
            entity.getName()
        );
    }
}
```

## 16.2 Custom Spring Data JDBC Writing/Reading Converters
For standard Java value types (e.g., custom Strong Typed IDs), custom Spring Data JDBC `@ReadingConverter` and `@WritingConverter` classes may be registered inside `adapter.outbound.persistence` to handle JDBC type conversions natively.

---

# 17. Identifier Generation Architecture

## 17.1 Identifier Strategy Baseline
D03 establishes that **Application-Generated UUID-Based Identifiers** are the preferred identifier family for domain aggregate roots. The specific UUID version (such as random UUIDv4 or time-ordered UUIDv7) and generation strategy are governed by implementation standards and may be standardized separately.

## 17.2 Identifier Rationale
- **Decoupled from Database:** Entities receive their unique identifier before persistence, allowing domain models to be constructed and validated in memory.
- **PostgreSQL Compatibility:** Maps natively to PostgreSQL's 128-bit `uuid` column type.

Database auto-increment sequence identifiers (`BIGSERIAL`, `AUTO_INCREMENT`) are prohibited for domain aggregate root primary keys.

---

# 18. Optimistic Locking & Concurrency Control

D03 establishes **Optimistic Locking** as the preferred concurrency mechanism where concurrent modification risk warrants it. Individual persistence models may use `@Version` when the applicable domain/use-case requirements justify it.

## 18.1 Spring Data JDBC `@Version`
When an aggregate requires concurrency versioning, relational entity classes include a version attribute annotated with `@org.springframework.data.annotation.Version`:

```java
@Table(value = "policies", schema = "policy")
final class PolicyRelationalEntity {
    @Id
    private UUID id;
    
    @Version
    private Long version;
    // ...
}
```

## 18.2 Concurrency Execution
- Spring Data JDBC automatically appends `WHERE version = :currentVersion` to update queries and increments version on save when `@Version` is present.
- If a concurrent modification occurs, Spring Data JDBC throws an optimistic locking exception (`OptimisticLockingFailureException`).
- The persistence adapter catches database concurrency exceptions and translates them into appropriate application-layer abstractions where required.

---

# 19. Relational Naming Conventions

D03 standardizes relational database naming across all modules:

| Relational Construct | Naming Convention | Pattern Example | Example |
|---|---|---|---|
| **Schema Name** | Lowercase exact module name | `<module>` | `policy`, `customer` |
| **Table Name** | Lowercase plural snake_case | `<entity_plural>` | `policies`, `customers` |
| **Column Name** | Lowercase singular snake_case | `<attribute_name>` | `first_name`, `created_at` |
| **Primary Key Column** | Singular `id` | `id` | `id` |
| **Foreign Key Column** | Target entity singular + `_id` | `<target>_id` | `customer_id` |
| **Primary Key Constraint**| `pk_` + table name | `pk_<table_name>` | `pk_policies` |
| **Foreign Key Constraint**| `fk_` + source + `_` + target | `fk_<source>_<target>` | `fk_policies_customers` |
| **Index Name** | `idx_` + table + `_` + column | `idx_<table>_<col>` | `idx_policies_status` |

> **FOREIGN KEY SCOPE RULE:** Foreign keys MUST NOT reference another module's database schema. Cross-module data relationships must be represented logically via approved module contracts or integration events rather than database-level foreign key constraints.

---

# 20. Connection Pooling Architecture (HikariCP)

D03 leverages **HikariCP** as the production connection pool manager autoconfigured by Spring Boot.

## 20.1 Property Governance Structure
HikariCP configuration parameters are managed centrally via Spring Boot environment properties (`application.yml`):

```yaml
# Conceptual Property Governance Structure
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    hikari:
      pool-name: AnverraHikariPool
      # Connection pool capacity, idle timeouts, and lifetime parameters
      # are environment-specific and must be configured outside this document.
```

## 20.2 Capacity Sizing Rule
Capacity-related values (pool sizes, minimum idle connections, timeouts) are environment-specific and must be configured outside this architecture document. Technology selection is established; capacity tuning is deferred.

---

# 21. Persistence Exception Translation Architecture

Persistence exceptions MUST NOT leak database or framework abstractions into application or domain layers.

```text
PostgreSQL Driver / JDBC ──► SQLException
                                │
                                ▼
Spring Data JDBC         ──► DataAccessException
                                │
                                ▼
Outbound Persistence     ──► Catches DataAccessException;
Adapter                      Translates to Application Exception
                                │
                                ▼
Application Layer        ──► Receives Application Exception
```

- Persistence adapters wrap repository operations in exception translation handling for infrastructure failures where required.
- Caught Spring `DataAccessException` instances are translated into approved application-layer abstractions where required. Concrete exception taxonomy is defined by the applicable application/error architecture.

---

# 22. Persistence Testing Strategy

D03 establishes a two-tier persistence testing strategy:

## 22.1 Testcontainers Integration Tests
Integration tests verifying real PostgreSQL interaction use **Testcontainers** (`org.testcontainers.containers.PostgreSQLContainer`):
- Runs a real, isolated PostgreSQL Docker container during test execution.
- Validates PostgreSQL-compatible persistence behavior, migrations, mappings, repository operations, and PostgreSQL-specific integration behavior.

## 22.2 `@DataJdbcTest` Slice Tests
For rapid adapter testing, `@DataJdbcTest` configures a lightweight Spring context containing only Spring Data JDBC repositories and DataSources.

---

# 23. Observability & Metrics Integration

Persistence health and performance are monitored via Spring Boot Actuator and Micrometer:

1. **HikariCP Pool Metrics:** `hikaricp.connections.active`, `hikaricp.connections.idle`, `hikaricp.connections.pending` exposed to Prometheus/Actuator.
2. **Query Execution Timing:** Persistence query timing and slow-query diagnostics shall be supported; concrete thresholds are environment- and workload-specific and are deferred to operational/performance governance.
3. **Flyway Migration Status:** Migration status tracked via Spring Boot Actuator `/actuator/flyway` endpoint.
4. **Data Masking:** Database connection passwords and sensitive parameters must never be logged.

---

# 24. PostgreSQL-Specific Features Governance

To maintain long-term maintainability, D03 establishes rules for PostgreSQL-specific features:

- **Relational Standard Default:** Standard ANSI SQL / PostgreSQL relational constructs (tables, FKs, indexes, standard types) are preferred.
- **JSONB Usage:** `JSONB` columns are permitted ONLY when storing unstructured third-party payloads or dynamic attributes where schema normalization is demonstrably impractical.
- **Database Functions & Triggers:** Stored procedures, PL/pgSQL functions, and triggers are PROHIBITED for business logic. Business logic belongs exclusively in the application and domain layers.

---

# 25. Security & Secrets Management Boundaries

1. **Least Privilege Credentials:** Application runtime database credentials shall possess only the database privileges required by the approved persistence architecture and deployment topology. Where a shared runtime database role is used, schema ownership and application-level architectural controls remain the primary module-isolation mechanisms. Where separate roles are introduced by a deployment topology, PostgreSQL schema-level privileges shall enforce additional least-privilege isolation.
2. **Migration User Separation:** Flyway migrations in CI/CD execute using a dedicated migration user with schema DDL privileges.
3. **Secrets Management:** Database passwords and connection URLs must be supplied via environment variables (`DB_PASSWORD`, `DB_URL`) injected at runtime. Hardcoding credentials or fallback default secrets in source files or POMs is strictly prohibited.

---

# 26. ArchUnit Persistence Verification Rules

D03 mandates that persistence structural rules are verified automatically on every build via ArchUnit:

```java
// CONCEPTUAL ARCHUNIT RULES FOR PERSISTENCE

// Rule 1: Repositories reside only in adapter.outbound.persistence
ArchRule repositoryPlacementRule = classes()
    .that().areAssignableTo(org.springframework.data.repository.Repository.class)
    .should().resideInAPackage("com.anverraglobal..adapter.outbound.persistence..");

// Rule 2: Domain has zero relational annotations
ArchRule domainPurityPersistenceRule = noClasses()
    .that().resideInAPackage("com.anverraglobal..domain..")
    .should().beAnnotatedWith(org.springframework.data.relational.core.mapping.Table.class)
    .orShould().beAnnotatedWith(org.springframework.data.annotation.Id.class);

// Rule 3: Application layer does not import Spring Data JDBC repositories
ArchRule applicationNoDirectRepositoryRule = noClasses()
    .that().resideInAPackage("com.anverraglobal..application..")
    .should().dependOnClassesThat()
    .resideInAPackage("com.anverraglobal..adapter.outbound.persistence..");
```

---

# 27. AI Implementation Governance for Persistence

When generating or modifying persistence code, human engineers and AI coding agents MUST strictly follow these rules:

1. **Obey Outbound Persistence Location:** Place all persistence adapters, entities, mappers, and repositories strictly inside `com.anverraglobal.<module>.adapter.outbound.persistence`.
2. **Never Annotate Domain Objects:** NEVER place `@Table`, `@Id`, `@Column`, or `@Version` annotations on classes inside `domain/`.
3. **Implement Outbound Ports:** Persistence adapters MUST implement application-owned outbound ports defined in `port.outbound`.
4. **Keep Repositories Private:** Never make Spring Data `CrudRepository` interfaces public or accessible outside `adapter.outbound.persistence`.
5. **No Cross-Module Queries or Foreign Keys:** Never write SQL queries or Spring Data methods that reference another module's database schema. Foreign keys MUST NOT cross module schema boundaries.
6. **No Unresolved Capability Persistence:** NEVER create schemas, tables, entities, or repositories for `Agent`, `Partner`, `Dealer`, `Organization`, `Proposal`, `Document`, `KYC`, or `Admin`.
7. **Use Schema Attribute in `@Table`:** Specify explicit schema names in `@Table` matching the approved module schema (`@Table(value = "policies", schema = "policy")`).
8. **No Business Tables in D03:** Do not create SQL migration files or table DDLs within D03 itself.
9. **Never Place `@Transactional` in Domain:** `@Transactional` belongs in `application/` services, never in `domain/`.
10. **Do Not Resolve Downstream Open Decisions:** Keep O4, O5, O7, and O9 open.

---

# 28. Deferred Decisions Register

D02 and D03 explicitly preserve the open status of downstream design decisions:

| Decision ID | Description | Assigned Document | Status |
|---|---|---|---|
| **O1** | Build Tool (Apache Maven) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O2** | Java Root Package (`com.anverraglobal`) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O3** | Inbound Adapter Package (`adapter/inbound/`) | **AEOS-P04-D02** | **RESOLVED (D02)** |
| **O4** | OpenAPI implementation approach | **AEOS-P04-D04** | **OPEN** |
| **O5** | OpenAPI client generation approach | **AEOS-P04-D07** | **OPEN** |
| **O6** | PostgreSQL schema naming strategy | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O7** | Event listener idempotency mechanism | **AEOS-P04-D05** | **OPEN** |
| **O8** | DataSource configuration pattern | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O9** | Shared vs independently generated client API types | **AEOS-P04-D07** | **OPEN** |

Additionally, D03 defers specific business database tables, columns, indexes, Flyway SQL scripts, and API payload schemas to downstream feature requirements.

---

# 29. Traceability

D03 maintains complete traceability to prior authoritative documents:

## 29.1 Phase 1 — Engineering Constitution
- [AEC-ARC-004 — Hexagonal Architecture](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-004-hexagonal-architecture.md)
- [AEC-ARC-006 — Dependency Direction](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-006-dependency-direction.md)
- [AEC-ARC-011 — Data Ownership](file:///Users/shashank/Projects/anverra-global/docs/01-constitution/03-architecture-principles/AEC-ARC-011-data-ownership.md)

## 29.2 Phase 2 — System & Module Blueprints
- [AEOS-P02-S01-D01 — System Blueprint](file:///Users/shashank/Projects/anverra-global/docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md)
- AEOS-P02-S02-D01 through D07 (Business Module Blueprints)

## 29.3 Phase 3 — Technology Blueprints
- [AEOS-P03-D02 — Persistence Technology Blueprint](file:///Users/shashank/Projects/anverra-global/docs/03-technology/02-persistence-blueprint.md)

## 29.4 Phase 4 — System Design Documents
- [AEOS-P04-D00 — Phase 4 System Design Overview](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/00-phase-4-overview.md)
- [AEOS-P04-D01 — Backend Implementation Architecture](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/01-backend-implementation-architecture.md)
- [AEOS-P04-D02 — Module Implementation Architecture](file:///Users/shashank/Projects/anverra-global/docs/04-system-design/02-module-implementation-architecture.md)

---

# 30. Definition of Done & Final Baseline Status

## 30.1 Definition of Done
This document (AEOS-P04-D03) is complete when:
1. Open Decision **O6** (PostgreSQL Schema Strategy) is formally resolved (Logical Module-Owned Schemas selected).
2. Open Decision **O8** (DataSource Pattern) is formally resolved (Centralized HikariCP DataSource selected).
3. The Spring Data JDBC integration pattern and outbound persistence adapter structure are defined.
4. Model separation between pure domain objects and relational entities is explicitly enforced.
5. Application ownership of outbound repository ports is integrated.
6. Flyway migration file organization (`db/migration/<module>/`) is established.
7. Technical enforcement of cross-module data isolation is specified.
8. Transaction placement, exception translation, Testcontainers testing, and HikariCP pooling are defined.
9. Complete AI implementation governance rules for persistence are recorded.
10. All 30 required sections are present and fully articulated.
11. No source code, Java classes, business database tables, or downstream documents (D04+) were created.

## 30.2 Final Status
This document is authored and recorded as **Baseline Candidate**.

## 30.3 Stop Rule & Next Step
- **Authoring Position 4 Complete:** AEOS-P04-D03 is fully authored.
- **Do NOT proceed to AEOS-P04-D04.**
- **Do NOT create Java classes, database schemas, or API endpoints.**
- **Awaiting formal architectural review before proceeding to D04 (API & Transport Implementation Architecture).**
