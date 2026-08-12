# Persistence Technology Blueprint

**Document ID:** AEOS-P03-D02  
**Version:** 3.0  
**Status:** Proposed  
**Phase:** 3 — Technology Selection & Architecture Enablement  
**System:** AnverraGlobal

---

# 1. Document Identity
**Title:** Persistence Technology Blueprint  
**ID:** AEOS-P03-D02

# 2. Purpose
The purpose of this document is to evaluate and select the primary persistence technology stack (database engine, persistence framework, migration tooling, and isolation strategy) required to implement the AnverraGlobal architecture. It ensures the chosen persistence layer enforces the strict isolation of Phase 2 business modules within the Java/Spring Boot Modular Monolith backend.

# 3. Scope
The scope of this document covers the relational database engine, the Java/Spring persistence framework/ORM, the database migration tooling, and the technical persistence isolation strategy. It explicitly does NOT decide database schemas, business domain entities, API protocols, message brokers, or business transaction boundaries.

# 4. Architectural Context
Phase 2 mandates that no module may directly access another module's authoritative persistence. Phase 3 D01 proposes Java + Spring Boot + Spring Modulith for the backend. The persistence technology must integrate seamlessly with this stack to enforce logical and structural data isolation without prematurely forcing a physical distributed microservices topology.

# 5. Authoritative Constraints
- **Modular Monolith First:** The technology must support isolated module boundaries within a single deployable application.
- **Data Encapsulation:** Cross-module database access must be technically prevented.
- **Anti-Invention:** This document evaluates technology. It must not invent business rules, domain entities, database schemas, APIs, or integration events.

# 6. Persistence Technology Responsibilities
The persistence technology is responsible for providing the technical foundation to:
- Persist domain aggregates efficiently and securely.
- Enforce the separation between domain, application, and infrastructure layers (Hexagonal Architecture).
- Isolate the data of the established Phase 2 business modules.
- Provide a robust mechanism for schema evolution (migrations).
- Provide transaction management capabilities to support local module consistency.

# 7. Evaluation Criteria
Candidates and strategies are evaluated against:
- Modular Monolith compatibility.
- Boundary enforcement (logical, structural, schema, permission, physical).
- Operational and deployment complexity.
- Transaction management implications.
- DDD and Hexagonal Architecture fit (domain purity).
- AI-assisted development safety.
- Testing, scalability, and maintainability.

# 8. Candidate Persistence Isolation Strategies
To enforce module boundaries, three primary strategies are evaluated:
1. **Shared Database / Shared Schema:** All modules share a single schema. Isolation is purely logical (application-level).
2. **Single Database / Schema-per-Module:** A single physical database is used, but each module owns a separate logical schema with dedicated credentials/permissions.
3. **Database-per-Module:** Each module is backed by a physically separate database server/instance.

# 9. Candidate Database Engines
Appropriate relational database candidates include:
1. **PostgreSQL:** The enterprise standard open-source RDBMS.
2. **MySQL / MariaDB:** Widely used open-source RDBMS.
3. **Microsoft SQL Server:** Leading commercial enterprise RDBMS.

# 10. Candidate Persistence Frameworks (Java/Spring)
1. **Spring Data JPA (Hibernate):** The most common enterprise Java ORM.
2. **Spring Data JDBC:** A simpler, DDD-aligned alternative to JPA that avoids lazy-loading and detached state complexities.
3. **jOOQ:** A SQL-centric, typesafe query builder.

# 11. Candidate Migration Tooling
1. **Flyway:** SQL-based migration tool, widely integrated with Spring Boot.
2. **Liquibase:** XML/YAML/JSON/SQL-based migration tool supporting broader database-agnostic features.

# 12. Persistence Isolation Strategy Evaluation
- **Shared DB / Shared Schema:** Low operational complexity, but high risk of AI or developers hallucinating accidental SQL-level cross-module joins. Relies entirely on ArchUnit and discipline.
- **Single DB / Schema-per-Module:** Strong balance. PostgreSQL schema separation combined with appropriately restricted database roles and privileges can provide a database-enforced persistence boundary. The effectiveness of this enforcement depends on correct privilege configuration and prevention of privilege drift. Operational complexity is low because it remains a single physical cluster, but architectural safety is high.
- **Database-per-Module:** Highest isolation, but Database-per-Module preserves local transactional integrity within each database but makes atomic transactions spanning multiple module databases substantially more complex and may require application-level consistency mechanisms or other distributed coordination. It drastically increases operational and deployment complexity for a monolith.

# 13. Database Engine Evaluation
- **PostgreSQL:** PostgreSQL provides mature relational, transactional, schema, and JSON capabilities relevant to potential future persistence requirements. Native integration with Spring Boot and Testcontainers.
- **MySQL:** Excellent, but lacks native logical schemas in the same way PostgreSQL does (MySQL treats databases and schemas synonymously).
- **SQL Server:** Excellent enterprise features, but introduces commercial licensing constraints and heavier local development overhead compared to PostgreSQL.

# 14. Persistence Framework Evaluation
- **Spring Data JPA:** Extremely powerful, but the "magic" of Hibernate (lazy loading, dirty checking, complex caches) often leaks infrastructure concerns into the domain layer, making strict DDD and Hexagonal Architecture harder to maintain.
- **Spring Data JDBC:** Provides a much closer fit to pure DDD. It forces aggregate roots to be saved as a whole, avoids lazy loading exceptions, and encourages simpler, purer domain models.
- **jOOQ:** Exceptional for complex reporting queries, but arguably too low-level for standard domain aggregate persistence.

# 15. Transaction Management Capability Evaluation
D02 evaluates technical transaction capabilities, not business transaction semantics.
- All candidates support robust local transactions (`@Transactional`).
- D02 may evaluate whether the selected persistence technology technically supports future consistency mechanisms, but must not mandate them.
- Business transaction boundaries are deferred. Whether a business operation spans multiple modules is not decided by D02. Saga decisions are deferred. Eventual-consistency decisions are deferred. Transactional Outbox implementation is deferred.

# 16. Migration Tooling Evaluation
- **Flyway:** Native Spring Boot support. Extremely straightforward SQL-based approach. Works cleanly with a schema-per-module strategy by allowing separate Flyway instances/configurations per module schema.
- **Liquibase:** Highly capable but introduces unnecessary abstraction overhead for teams writing native PostgreSQL.

# 17. Modular Monolith Fit
The combination of Schema-per-Module in PostgreSQL with Flyway migration ownership and execution scoped to established module persistence boundaries aligns perfectly with Spring Modulith. Each module acts as a self-contained persistence boundary while running in a single application process.

# 18. DDD Fit
Spring Data JDBC is an exceptional fit for DDD because it fundamentally treats persistence at the aggregate root level, removing the temptation to navigate object graphs via lazy-loaded relationships that cross aggregate boundaries.

# 19. Hexagonal Architecture Fit
Spring Data JDBC and JPA both integrate cleanly with ports and adapters. Spring Data JDBC requires less framework-specific annotation intrusion on the core domain entities than JPA.

# 20. Module Encapsulation Support
By combining Schema-per-Module with dedicated database roles/privileges per persistence-owning module, PostgreSQL can provide database-level enforcement of persistence boundaries. Native SQL access remains subject to the configured database privileges; privilege configuration and drift prevention are therefore part of the enforcement model.

# 21. Cross-Module Isolation Enforcement
Application-level isolation (module architecture and dependency rules) combined with structural/code-level isolation (Spring Modulith and architecture tests) and database-level isolation (PostgreSQL schemas + module-specific database roles/privileges) provides defense-in-depth against architectural degradation. Operational enforcement depends on automated infrastructure/configuration and controlled privilege management.

# 22. Testing Support
PostgreSQL + Testcontainers provides a robust, identical-to-production testing environment for integration tests without requiring manual database provisioning.

# 23. Observability Support
The selected persistence stack integrates with the system's future observability mechanisms.

# 24. Deployment Compatibility
PostgreSQL has broad support across managed database services and containerized deployment environments.

# 25. Developer Productivity
PostgreSQL with Flyway and Testcontainers enables rapid local development inner-loops. Spring Data JDBC reduces debugging time typically lost to complex JPA/Hibernate state lifecycle issues.

# 26. AI-Assisted Development Suitability
Schema-per-Module makes it structurally difficult for an AI to invent a `JOIN` across module boundaries because the database permissions will reject it at runtime. Spring Data JDBC is simpler and more explicit than JPA, leading to fewer AI hallucinations regarding lazy-loading or cascading persistence rules.

# 27. Maintainability & Ecosystem
PostgreSQL and Spring Data JDBC are supported by massive, active, and mature enterprise ecosystems ensuring long-term sustainability.

# 28. Structured Comparative Evaluation Matrix

| Criterion (Weight) | Shared Schema | Schema-per-Module | DB-per-Module |
|---------------------|---------------|-------------------|---------------|
| Boundary Enforcement (30%) | Low | High | Very High |
| Operational Simplicity (25%)| Very High | High | Low |
| Transaction Support (15%) | Local | Local | Local; cross-module coordination complex |
| Monolith Compatibility (15%)| High | High | Low |
| AI Safety (15%) | Low | High | Very High |

**Rationale:**
- **Shared Schema:** Lower boundary-enforcement strength because isolation depends primarily on application architecture and developer discipline.
- **Schema-per-Module:** Strong balance because logical database boundaries and restricted privileges can provide stronger enforcement without introducing multiple physical databases.
- **Database-per-Module:** Strongest physical isolation but materially greater operational and deployment complexity and more complicated cross-database transaction semantics.

*Note: Qualitative architectural evaluation.*

# 29. Recommended Technology
**Recommended Persistence Stack:**
- **Database Engine:** PostgreSQL
- **Persistence Isolation:** Single PostgreSQL deployment with logical schema boundaries for modules whose persistence is established, with appropriately restricted database roles/privileges.
- **Persistence Framework:** Spring Data JDBC as the proposed default for aggregate-oriented transactional persistence.
- **Migration Tooling:** Flyway, with migration ownership/scoping aligned to established module persistence boundaries.
- **Enforcement Model:** Defense in depth across application/module architecture and database permissions.

# 30. Decision Status
**Proposed** (Pending project-level baseline approval).

# 31. Rejected / Deferred Alternatives
- **Database-per-Module:** Not Selected for the current Modular Monolith baseline.
- **Shared Schema:** Not Selected for the current persistence baseline.
- **Spring Data JPA:** Not Selected as the default persistence framework.

# 32. Architectural Consequences
### Positive Consequences
- Strict database-level enforcement of module boundaries via roles/privileges.
- Pure DDD modeling encouraged by Spring Data JDBC.
- Simplified operational overhead compared to microservice DB architectures.

### Negative Consequences
- Cross-module reporting or data extraction requires explicit data integration strategies rather than simple SQL joins.
- Managing multiple Flyway configurations and database users requires robust CI/CD and configuration management.

# 33. Risks & Mitigations
- **Risk:** Complex domain graphs might be harder to map in Spring Data JDBC than in JPA.
  - **Mitigation:** Fallback to raw SQL/JdbcTemplate for complex read models or evaluate jOOQ for complex query requirements, while preserving Spring Data JDBC for aggregate mutation.
- **Risk:** Developers manually granting cross-schema permissions.
  - **Mitigation:** Database roles and schema provisioning must be automated via Infrastructure-as-Code to prevent manual permission drift.

# 34. Implementation Constraints
- Any module whose persistence responsibility is established by the applicable business/application design may receive an isolated persistence boundary using the selected persistence strategy.
- Each module that owns established persistence MUST use database credentials/roles restricted to its own persistence boundary.
- Flyway migrations MUST be scoped and executed only for established module persistence boundaries.

# 35. AI Implementation Guidance
Future coding AI MUST:
- Use only the backend persistence stack after this blueprint has been formally baselined.
- Implement Schema-per-Module isolation via dedicated users/privileges.
- Never write SQL or native queries that join across module schemas.
- Never bypass Spring Modulith architectural enforcement.
- Keep domain logic completely independent of Spring Data infrastructure.
- Never treat a candidate technology as approved until its decision status is baselined.
- Spring Data JDBC is proposed as the default persistence framework for aggregate-oriented transactional persistence within the Modular Monolith. Its suitability will be validated against finalized domain models during implementation.

# 36. Deferred Decisions
This document explicitly defers decisions regarding:
- Database schemas, tables, columns, indexes, and foreign keys.
- ORM entity definitions and mapping implementations.
- Actual SQL scripts and Flyway migrations.
- Business transaction boundaries and semantics (e.g., Sagas).
- Implementation of cross-module consistency patterns (e.g., Transactional Outbox).
- API protocol, message broker, and client framework decisions (D03-D05).
- Observability and infrastructure deployment technology decisions.

# 37. Traceability
This document traces directly to:
- AEOS-P03-D00
- AEOS-P03-D01
- Phase 1 Engineering Constitution
- AEOS-P02-S01-D01 through D05
- AEOS-P02-S02-D00 through D07

# 38. Definition of Done
The blueprint is complete when:
- [x] Evaluation criteria are explicitly defined against Phase 1 and Phase 2 constraints.
- [x] Persistence isolation strategies were evaluated objectively.
- [x] The database engine, framework, and migration tooling are proposed.
- [x] No schemas, domain models, APIs, or integration events are invented.
- [x] Business transaction boundaries are explicitly deferred.
- [x] AI constraints and deferred decisions are explicitly documented.
- [x] Traceability is maintained.
