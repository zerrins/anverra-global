# Backend Technology Blueprint

**Document ID:** AEOS-P03-D01  
**Version:** 3.0  
**Status:** Proposed  
**Phase:** 3 — Technology Selection & Architecture Enablement  
**System:** AnverraGlobal

---

# 1. Document Identity
**Title:** Backend Technology Blueprint  
**ID:** AEOS-P03-D01

# 2. Purpose
The purpose of this document is to evaluate and select the backend technology stack (language, core framework, runtime, and DI mechanism) required to implement the AnverraGlobal architecture. It ensures the chosen backend explicitly supports the Modular Monolith, Domain-Driven Design (DDD), Hexagonal Architecture, and the strict encapsulation of Phase 2 business modules.

# 3. Scope
The scope of this document is strictly limited to the backend language, runtime environment, primary application framework, and dependency injection capability. It explicitly does NOT decide the persistence engine, API protocol, message broker, build tool, or client framework.

# 4. Architectural Context
Phase 2 mandates a backend architecture that runs as a single deployable process but is internally structured into strict, isolated business boundaries. The backend must enforce that modules interact only through approved contracts without circular dependencies, unauthorized cross-module persistence access, or tight coupling of domain logic to infrastructural concerns.

# 5. Authoritative Constraints
- **Modular Monolith First:** The technology must not prematurely force distributed microservices.
- **Strict Encapsulation:** The technology must provide mechanisms to prevent accidental leakage of internal domain states between modules.
- **Anti-Invention:** This document defines technology selection. It must not invent business rules, domain entities, database schemas, APIs, or integration events.

# 6. Backend Technology Responsibilities
The backend technology is responsible for providing the technical foundation to:
- Instantiate and orchestrate application components via Dependency Injection (DI).
- Enforce the separation between domain, application, infrastructure, and interface layers (Hexagonal Architecture).
- Isolate the seven Phase 2 business modules from each other structurally and logically.
- Enable automated architectural validation (e.g., verifying dependency directions at compile or test time).

# 7. Evaluation Criteria
Candidates are evaluated against:
- Modular Monolith compatibility.
- DDD and Hexagonal Architecture support.
- Module encapsulation and dependency-direction enforcement.
- Support for persistence boundary isolation.
- Domain purity and testability.
- Compatibility with future security and observability requirements.
- API and messaging integration readiness.
- Deployment compatibility.
- Developer productivity and maintainability.
- AI-assisted development suitability.

# 8. Candidate Technologies
To ensure a rigorous decision, candidates from materially different ecosystems are evaluated:
1. **Java + Spring Boot:** The enterprise standard JVM stack.
2. **TypeScript + Node.js (with NestJS):** The modern, typed JavaScript ecosystem with opinionated DI.
3. **C# + ASP.NET Core:** The enterprise standard .NET stack.

# 9. Candidate Evaluation
The following sections objectively evaluate how the candidates meet the architectural requirements.

# 10. Modular Monolith Fit
- **Java/Spring Boot:** Excellent. `Spring Modulith` provides explicit, first-class support for structuring applications into logical modules, verifying interactions, and exposing only approved APIs between modules.
- **TypeScript/NestJS:** Good. NestJS relies on module decorators to encapsulate providers, but JavaScript's dynamic nature makes enforcing strict architectural isolation slightly more reliant on tooling rather than native language constraints.
- **C#/ASP.NET Core:** Excellent. C# internal access modifiers and Roslyn analyzers provide robust compile-time enforcement of boundaries.

# 11. DDD Fit
- **Java:** High. Strong typing, rich object-orientation, and an extensive ecosystem of DDD patterns.
- **TypeScript:** High. Structural typing allows for elegant, lightweight value objects, though runtime immutability requires careful enforcement.
- **C#:** High. Structs, records, and robust OOP features make modeling complex domains natural.

# 12. Hexagonal Architecture Fit
- **Java:** Native fit. Interfaces, DI, and package visibility mechanisms naturally map to ports and adapters.
- **TypeScript:** Native fit. NestJS's DI and interface features support the inversion of control necessary for ports and adapters.
- **C#:** Native fit. Interfaces and built-in DI seamlessly support defining ports in the core and adapters in the infrastructure.

# 13. Module Encapsulation
- **Java:** Best-in-class via package visibility, logical project/module structures, and ArchUnit verification. (JPMS is not mandated unless explicitly justified).
- **TypeScript:** Moderate. TypeScript relies on ESLint rules or tooling (e.g., Nx) to enforce encapsulation.
- **C#:** Best-in-class via internal visibility and separate assemblies (projects).

# 14. Dependency Direction
Dependency Injection provides runtime dependency resolution, but architectural dependency direction requires explicit structural rules. Compilation or runtime behavior alone is not sufficient architectural enforcement. Circular module dependencies must be prevented through module structure, build boundaries where applicable, and automated architectural tests.
- **Java:** Strongly enforced via ArchUnit testing and Spring Modulith verification.
- **TypeScript:** Enforced via linting rules or monorepo tools. NestJS allows circular dependencies but requires awkward forward references.
- **C#:** Strongly enforced by the MSBuild project reference system, which natively blocks circular dependencies.

# 15. Persistence Boundary Support
- **Java:** The ecosystem supports isolated data sources per module, repository abstractions, and strict architectural tests to prevent cross-module repository injections.
- **TypeScript:** NestJS supports multiple logical database connections per module, allowing logical isolation.
- **C#:** Entity Framework Core contexts can be strictly scoped per project/module.

# 16. Testing Support
- **Java:** Extremely mature (JUnit, Mockito). ArchUnit and Spring Modulith allow writing automated tests that explicitly fail if an architectural boundary is violated.
- **TypeScript:** Very mature (Jest, Vitest).
- **C#:** Extremely mature (xUnit, NSubstitute, NetArchTest).

# 17. Security Technology Support
Identity remains the business authority for authentication and authorization. All candidates natively support OAuth2, JWT validation, and claims-based authorization logic necessary to implement the Identity module's business rules.

# 18. Observability Support
All candidates support OpenTelemetry, structured JSON logging, and health endpoints.

# 19. API Integration Readiness
All candidates have world-class support for REST, GraphQL, and gRPC, preserving flexibility for D03.

# 20. Messaging Integration Readiness
All candidates integrate seamlessly with major brokers, preserving flexibility for D04.

# 21. Deployment Compatibility
The selected backend technology must support containerized deployment and remain compatible with the infrastructure architecture established in the designated infrastructure/deployment phase.

# 22. Developer Productivity
- **Java:** Verbose but highly predictable. Refactoring is exceptionally reliable.
- **TypeScript:** Fast inner-loop, highly ubiquitous language, but tooling configuration can be fragmented.
- **C#:** Exceptional tooling (Visual Studio / Rider) and language ergonomics.

# 23. AI-Assisted Development Suitability
- **Java:** Excellent. Java's strong typing, explicit structure, mature conventions, static analysis, compilation, and architecture testing make AI-generated implementation easier to validate and reject when it violates constraints.
- **TypeScript:** Very good. Highly represented in AI training data, but the flexibility of JS can lead AI to generate implicit coupling unless strictly prompted.
- **C#:** Excellent. Similar predictability and strictness to Java.

# 24. Maintainability & Ecosystem
- **Java/Spring:** Arguably the most mature enterprise backend ecosystem in existence. Decade-long support lifecycles.
- **TypeScript/NestJS:** Extremely popular, but the Node.js ecosystem evolves rapidly, sometimes causing churn.
- **C#/.NET:** Highly stable, performant, and backed by Microsoft's unified ecosystem.

# 25. Structured Comparative Evaluation Matrix
The evaluation uses qualitative architectural ratings rather than numerical candidate scores. No empirical benchmark dataset has been established as an authoritative AnverraGlobal input, so numerical scoring would introduce false precision. The weights indicate relative architectural importance and support structured comparison rather than mathematical performance ranking.

| Criterion (Weight) | Java + Spring Boot | TS + NestJS | C# + ASP.NET Core |
|---------------------|--------------------|-------------|-------------------|
| Modular Monolith Support (20%) | Excellent | Good | Excellent |
| Dependency Direction Enforcement (15%) | Excellent | Moderate | Excellent |
| Module Encapsulation (15%) | Excellent | Moderate | Excellent |
| Persistence Boundary Support (10%) | Excellent | Excellent | Excellent |
| DDD / Hexagonal Arch Fit (10%) | Excellent | Excellent | Excellent |
| AI Validation Suitability (10%) | Excellent | Good | Excellent |
| Testing / Arch Verification (10%) | Excellent | Good | Excellent |
| Developer Productivity (10%) | Good | Excellent | Excellent |

**Rationale:** Both Java and C# score highest across critical architectural boundaries. Java + Spring Boot, specifically augmented by Spring Modulith and ArchUnit, is recommended because it provides the most explicit, first-class ecosystem tooling available today for testing and verifying strict Modular Monolith boundaries without relying solely on build configurations.

# 26. Recommended Technology
**Recommended Backend Stack:** Java + Spring Boot + Spring Modulith

- **Language:** Java
- **Framework:** Spring Boot
- **Modular Framework:** Spring Modulith
- **Testing:** JUnit + ArchUnit

*Note: The specific build tool (e.g., Maven or Gradle) is deferred to the implementation/tooling phase.*

# 27. Decision Status
**Proposed** (Pending project-level baseline approval).

# 28. Rejected / Deferred Alternatives
- **TypeScript/NestJS:** Rejected. While excellent for many applications, the reliance on linter rules to enforce architectural boundaries adds unnecessary friction for a strict Modular Monolith compared to Java or C#.
- **C#/ASP.NET Core:** Deferred/Rejected. Operationally and architecturally on par with Java, but Java + Spring Modulith currently provides slightly more explicit ecosystem tooling (Spring Modulith specifically) for verifying Modular Monolith boundaries.

# 29. Architectural Consequences
### Positive Consequences
- Boundaries (Identity, Customer, Policy, etc.) can be explicitly tested and verified using `Spring Modulith` and `ArchUnit`.
- Compiling and testing code that bypasses structural rules will natively fail architectural tests.
- AI-generated code that violates boundaries will be caught immediately by test pipelines.

### Negative Consequences
- Java/Spring introduces more boilerplate and a heavier memory footprint than lightweight Node.js or Go services.
- Requires strict adherence to package and visibility structures.

# 30. Risks & Mitigations
- **Risk:** Developers (or AI) bypassing domain abstractions and directly using framework annotations inside the Domain layer.
  - **Mitigation:** ArchUnit tests will be written to explicitly fail the build if `org.springframework.*` dependencies are imported into the domain core.
- **Risk:** Creating a distributed monolith by accident.
  - **Mitigation:** The application will remain a single deployable process. Internal module collaboration MUST occur through approved in-process module contracts or approved asynchronous integration mechanisms. Internal modules MUST NOT communicate through network calls merely to simulate microservices.

# 31. Implementation Constraints
- The backend repository MUST be structured to reflect the seven Phase 2 business modules.
- The implementation MUST provide explicit structural separation between module-public contracts and module-internal implementation details.
- No module may import classes from the internal implementation packages of another module.

# 32. AI Implementation Guidance
Future coding AI MUST:
- Use only the backend technology stack after this blueprint has been formally baselined.
- Preserve module boundaries.
- Preserve dependency direction.
- Never access another module's persistence directly.
- Never introduce microservices merely because modules exist.
- Never introduce unsupported frameworks.
- Never bypass ArchUnit or Spring Modulith architectural enforcement.
- Keep domain logic completely independent of Spring infrastructure (Hexagonal Architecture).
- Follow D02–D05 for subsequent technology decisions.
- Never treat a candidate technology as approved until its decision status is baselined.

# 33. Deferred Decisions
This document explicitly defers decisions regarding:
- Build tool (Maven/Gradle).
- Database engine (PostgreSQL, MySQL, etc.)
- ORM / Query technology (Hibernate, jOOQ, Spring Data, etc.)
- API protocol (REST, GraphQL, gRPC)
- API contracts and payloads
- Message broker (Kafka, RabbitMQ, etc.)
- Integration event design
- Frontend framework (React, Angular, iOS, etc.)
- Cloud provider and infrastructure topology
- CI/CD implementation
- Detailed security implementation (OAuth providers, etc.)
- Domain models, entities, and business rules
- Database schemas

# 34. Traceability
This document traces directly to:
- AEOS-P03-D00
- Phase 1 Engineering Constitution
- AEOS-P02-S01-D01
- AEOS-P02-S01-D02
- AEOS-P02-S01-D03
- AEOS-P02-S01-D04
- AEOS-P02-S01-D05
- AEOS-P02-S02-D00
- AEOS-P02-S02-D01 through D07

# 35. Definition of Done
The blueprint is complete when:
- [x] Evaluation criteria are explicitly defined against Phase 1 and Phase 2 constraints.
- [x] Candidates from different ecosystems were evaluated objectively.
- [x] Modular monolith fit, DDD fit, encapsulation, and boundary enforcement are explicitly analyzed.
- [x] The backend technology is proposed.
- [x] No schemas, domain models, APIs, or integration events are invented.
- [x] No message brokers, UI frameworks, or persistence technologies are prematurely selected.
- [x] AI constraints and deferred decisions are explicitly documented.
- [x] Traceability is maintained.
