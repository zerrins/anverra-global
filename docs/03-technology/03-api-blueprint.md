# API & Transport Technology Blueprint

**Document ID:** AEOS-P03-D03  
**Version:** 4.0  
**Status:** Proposed  
**Phase:** 3 — Technology Selection & Architecture Enablement  
**System:** AnverraGlobal

---

# 1. Document Identity
**Title:** API & Transport Technology Blueprint  
**ID:** AEOS-P03-D03

# 2. Purpose
The purpose of this document is to evaluate and select the external API technology and transport protocols required to expose the AnverraGlobal system to external Web and Mobile clients. It ensures the chosen API stack supports the Modular Monolith, Hexagonal Architecture, and the strict isolation of Phase 2 business modules.

# 3. Scope
The scope of this document covers the primary external API protocol(s), Java/Spring implementation frameworks, API contract definition and documentation tooling, serialization formats, and the technical mechanism for external request routing. It explicitly does NOT decide messaging technology (D04), client technology (D05), backend language (D01), or persistence (D02). It does NOT invent specific API endpoints, routes, payloads, DTOs, or business rules.

# 4. Architectural Context
Phase 2 mandates that external Web/Mobile applications communicate with the backend through the Application Boundary. The backend remains a Modular Monolith. The API layer acts solely as a Driving Adapter in the Hexagonal Architecture. Internal business modules communicate via in-process Spring Modulith contracts, not through network APIs.

# 5. Authoritative Constraints
- **Modular Monolith First:** The API technology must expose a unified external boundary without fragmenting the backend into network-partitioned microservices.
- **Internal Communication:** HTTP, REST, GraphQL, or gRPC MUST NOT be used for internal module-to-module communication.
- **Anti-Invention:** This document evaluates technology. It must not invent API URLs, DTO schemas, business commands/queries, JWT claims, or integration events.
- **Delegated Security:** Identity remains the authoritative business module for authentication and authorization. The API technology must not become a secondary authorization engine with redundant business rules.

# 6. API Technology Responsibilities
The API and transport technology is responsible for providing the technical foundation to:
- Receive and deserialize external requests from Web/Mobile clients.
- Delegate valid requests to the appropriate internal Module API/Contract.
- Serialize and return responses.
- Provide clear, typed, discoverable API contracts.
- Support HTTP-based security (e.g., token validation) orchestrated by the Identity module.
- Enforce the boundary between external HTTP concerns and internal domain models.

# 7. Evaluation Criteria
Candidates are evaluated against:
- Modular Monolith compatibility.
- Web/Mobile client suitability.
- Hexagonal Architecture fit (separation of HTTP from domain logic).
- DDD boundary preservation.
- Module isolation and routing clarity.
- Contract clarity and type safety.
- Developer productivity and AI-assisted development safety.
- Testing and documentation ease.
- Operational complexity, maintainability, and deployment compatibility.

# 8. Candidate API Protocols
1. **REST over HTTP:** A mature architectural style widely used for Web and Mobile application APIs.
2. **GraphQL:** A query language allowing clients to request precisely the shape of data they need.
3. **gRPC:** A high-performance, strictly typed RPC framework using HTTP/2.

# 9. Candidate Java/Spring API Frameworks
1. **Spring Web MVC:** The standard synchronous/servlet-based REST framework for Spring Boot.
2. **Spring for GraphQL:** Spring's native GraphQL integration.
3. **gRPC-Spring-Boot-Starter:** Ecosystem integration for gRPC servers within Spring.

# 10. Contract & Documentation Candidates
1. **OpenAPI (Swagger):** The industry standard for defining REST API contracts.
2. **GraphQL Schema Definition Language (SDL):** Native schema system for GraphQL.
3. **Protobuf (Protocol Buffers):** Native contract definition language for gRPC.

# 11. Serialization / Representation Candidates
1. **JSON:** Universal, human-readable, standard for Web/Mobile.
2. **Protobuf binary:** Highly compact, strict binary format.

# 12. API Boundary Strategy Evaluation
The external API acts as a gateway into the monolithic application. The external API boundary uses the approved application/API adapter mechanism to route requests through the appropriate internal application/module boundary without bypassing module encapsulation. Dedicated API Gateway topology remains deferred. Internal modules must never expose their internal domain models directly as HTTP responses; explicit DTO translation is technically required by the API boundary.

# 13. REST Evaluation
- **Pros:** Extremely mature. Unmatched ecosystem for Web/Mobile integration. Native, frictionless support in Spring Web MVC. Caching semantics are natively understood by all CDN and HTTP infrastructure. Easy to map strictly to bounded contexts using distinct base paths.
- **Cons:** Can lead to over-fetching or under-fetching of data on complex mobile screens.
- **Architectural Fit:** Excellent for exposing well-defined business capabilities while avoiding complex cross-module data joining requirements at the API edge.

# 14. GraphQL Evaluation
- **Pros:** Perfect for complex UIs avoiding over/under-fetching. Strong type schema.
- **Cons:** In a strict Modular Monolith, a single monolithic GraphQL schema often encourages resolvers that silently traverse across module boundaries to fulfill deep object graphs, violating module isolation. Caching and security (field-level authorization) introduce massive complexity.
- **Architectural Fit:** Poor for strict Modular Monolith isolation unless extreme discipline is applied to schema stitching/federation within the monolith.

# 15. gRPC Evaluation
- **Pros:** Exceptional performance and strict Protobuf contracts.
- **Cons:** Not natively supported by web browsers without proxies (gRPC-Web). Overkill for standard CRUD and business operations originating from standard Web/Mobile clients.
- **Architectural Fit:** Better suited for high-throughput service-to-service communication, which is explicitly prohibited internally by the Modular Monolith constraints.

# 16. Framework Evaluation
- **Spring Web MVC:** Integrates seamlessly with Spring Boot and Spring Modulith. Allows explicit, code-driven separation of HTTP controllers into module-specific adapters.
- **Spring WebFlux (Reactive):** Not evaluated as the primary default because reactive programming introduces immense debugging complexity and is unjustified without a specific massive-concurrency requirement.
- **Spring for GraphQL:** Requires complex data loaders that risk bleeding across module boundaries.

# 17. Contract/Documentation Evaluation
- **OpenAPI 3.0 via Springdoc:** Allows code-first or design-first contract generation. Springdoc integrates cleanly with Spring Web MVC, producing highly accurate OpenAPI specs that can be used to auto-generate client SDKs. This provides exceptional predictability for AI assistants.

# 18. Hexagonal Architecture Fit
Spring Web MVC maps perfectly to the concept of Driving Adapters. `@RestController` classes can be placed in an adapter package, translate HTTP requests into business commands, and invoke the pure domain/application layer. The domain layer remains completely oblivious to HTTP, JSON, or Spring Web.

# 19. Modular Monolith Fit
REST API capabilities can be organized according to the approved application and module boundaries without requiring network separation between modules. Spring Modulith easily supports independent HTTP adapters per module without requiring network calls to bridge them. 

# 20. Module Encapsulation / Routing
External request routing is handled natively by the embedded Spring Web container (e.g., Tomcat). Each module exposes its own Driving Adapters. Cross-module aggregation requirements are deferred. If future application requirements require aggregated client-facing representations, the appropriate architectural mechanism must be established through the relevant application and integration design before implementation. A BFF may be evaluated as a possible future option if justified by client requirements. The API technology itself will not invent cross-module API joins.

# 21. Internal Module Communication Boundary
**Crucial Architectural Rule:** The backend modules MUST NOT communicate with each other over HTTP, REST, gRPC, or GraphQL. Internal communication strictly relies on in-process Java method calls via exported module interfaces (enforced by Spring Modulith).

# 22. Security Boundary
The API layer may participate in the technical processing of authentication credentials as configured by the approved security architecture. Identity remains authoritative for authentication and authorization decisions. API Driving Adapters MUST NOT contain independent authorization business rules.

# 23. Testing Support
Spring's `@WebMvcTest` allows isolated testing of API adapters without loading the entire application context or database, perfectly supporting Hexagonal testing strategies.

# 24. Documentation / Discoverability
OpenAPI enables interactive Swagger UI documentation, making the monolithic backend's capabilities immediately discoverable by frontend/mobile teams and AI coding assistants.

# 25. AI-Assisted Development Suitability
REST + OpenAPI + Spring Web MVC provides the strongest environment for AI code generation. The explicit nature of DTOs, HTTP verbs, and `@RestController` annotations provides rigid, predictable patterns. OpenAPI provides a structured, machine-readable contract that improves consistency for frontend/mobile development, client generation, testing, and AI-assisted implementation.

# 26. Operational / Deployment Considerations
A Spring Web MVC application using embedded Tomcat deployed as a single container is the industry standard for operational simplicity. It requires no complex API Gateway mesh or gRPC proxy infrastructure.

# 27. Structured Comparative Evaluation Matrix
The evaluation uses qualitative architectural ratings rather than numerical candidate scores. The weights indicate relative architectural importance and support structured comparison.

| Criterion (Weight) | REST / Spring MVC | GraphQL / Spring | gRPC |
|---------------------|-------------------|------------------|------|
| Modular Monolith Fit (20%) | Excellent | Moderate | Moderate |
| Web/Mobile Client Fit (20%)| Excellent | Excellent | Low |
| Module Isolation (15%) | Excellent | Low | High |
| Contract Clarity (15%) | Excellent (OpenAPI)| Excellent (SDL) | Excellent (Proto) |
| Hexagonal Arch Fit (10%) | Excellent | Good | Good |
| AI Validation Safety (10%) | Excellent | Good | High |
| Operational Simplicity (10%)| Very High | Moderate | Low |

**Rationale:**
- **REST:** Scores highest due to its native web support, explicit boundary mapping capability (endpoints align easily with modules), and operational simplicity.
- **GraphQL:** High risk of violating strict module isolation due to cross-graph resolving.
- **gRPC:** Poor fit for native web browser consumption without complex proxy infrastructure.

# 28. Recommended Technology
**Recommended External API & Transport Stack:**
- **Primary Protocol:** REST over HTTP
- **Implementation Framework:** Java + Spring Web MVC
- **Serialization / Representation:** JSON
- **Contract / Documentation:** OpenAPI 3.0 (via Springdoc OpenAPI)
- **Internal Communication:** Strictly In-Process Java Contracts (No Network APIs)

# 29. Decision Status
**Proposed** (Pending project-level baseline approval).

# 30. Rejected / Deferred Alternatives
- **GraphQL:** Not Selected for the primary external API baseline. The risk of monolithic schema resolvers breaching module boundaries outweighs the client data-fetching benefits for the initial architecture.
- **gRPC:** Not Selected for the primary external API baseline. Native web/mobile consumption friction is too high.
- **Spring WebFlux:** Not Selected. Synchronous Spring Web MVC is chosen for simplicity and debugging ease.

# 31. Architectural Consequences
### Positive Consequences
- Clear, highly cacheable HTTP APIs that respect the established application and module boundaries.
- Explicit, AI-readable API contracts (OpenAPI).
- Excellent Hexagonal separation; domain models remain ignorant of HTTP.

### Negative Consequences
- Clients requiring complex, aggregated data across modules must either orchestrate multiple REST calls or rely on explicit Backend-For-Frontend (BFF) endpoints designed later.
- Over-fetching/under-fetching compared to GraphQL.

# 32. Risks & Mitigations
- **Risk:** Driving adapters leaking HTTP request/response objects into the Application/Domain layer.
  - **Mitigation:** Strict architectural checks must prevent servlet/web framework dependencies from crossing into Domain and appropriately isolated Application layers.
- **Risk:** Developers creating network-based microservices internally.
  - **Mitigation:** Explicit enforcement that modules only communicate via in-process Spring Modulith contracts.

# 33. Implementation Constraints
- External API Driving Adapters MUST route requests through the appropriate approved application/module boundary without bypassing module encapsulation.
- API requests MUST be deserialized into explicit Request DTOs before entering the Application layer.
- API responses MUST be serialized from explicit Response DTOs, not direct Domain Entities.
- Internal modules MUST NOT communicate with each other over HTTP/REST.

# 34. AI Implementation Guidance
Future coding AI MUST:
- Use only the API technology stack after this blueprint has been formally baselined.
- Implement strictly synchronous Spring Web MVC REST controllers as Driving Adapters.
- Generate explicit Request and Response DTOs.
- Never leak Spring Web annotations into Domain models.
- Never write HTTP clients to invoke internal modules from within the backend.
- Never invent business logic or endpoints not authorized by later application design.
- Treat the formally approved OpenAPI contract as the authoritative representation of the external API contract once API contracts are designed and baselined.

# 35. Deferred Decisions
This document explicitly defers decisions regarding:
- Specific API routes, endpoints, HTTP methods, and URL structures.
- Specific JSON payload schemas, Request DTOs, and Response DTOs.
- Specific HTTP error codes and exception payload structures.
- Authentication token structures (e.g., JWT claims) and OAuth flows.
- Specific Authorization permissions/roles mapping.
- Dedicated API Gateway or Reverse Proxy infrastructure topologies.
- Message broker and asynchronous event technology (D04).
- Client Web/Mobile technology (D05).
- Cross-module API aggregation or Backend-For-Frontend (BFF) structures.

# 36. Traceability
This document traces directly to:
- AEOS-P03-D00
- AEOS-P03-D01
- AEOS-P03-D02
- Phase 1 Engineering Constitution
- AEOS-P02-S01-D01 through D05
- AEOS-P02-S02-D00 through D07

# 37. Definition of Done
The blueprint is complete when:
- [x] Evaluation criteria are explicitly defined against Phase 1 and Phase 2 constraints.
- [x] REST, GraphQL, and gRPC were evaluated objectively.
- [x] The primary API protocol, implementation framework, and contract tooling are proposed.
- [x] Internal module communication is explicitly restricted from using network APIs.
- [x] No endpoints, DTOs, URLs, or business schemas are invented.
- [x] AI constraints and deferred decisions are explicitly documented.
- [x] Traceability is maintained.
