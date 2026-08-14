# API & Transport Implementation Architecture

**Document ID:** AEOS-P04-D04  
**Version:** 1.0  
**Status:** Baseline Candidate  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 5  
**Depends on:** Phase 1 Engineering Constitution · AEOS-P02-S01-D01 through D05 · AEOS-P02-S02-D00 through D07 · AEOS-P03-D00 through D05 · AEOS-P04-D00 · AEOS-P04-D01 · AEOS-P04-D02 · AEOS-P04-D03  

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D04 |
| **Title** | API & Transport Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Authoring Position** | 5 |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |
| **Immediately Preceding Document** | AEOS-P04-D03 — Persistence Implementation Architecture |

---

# 2. Purpose

This document establishes the authoritative API and transport implementation architecture for the AnverraGlobal Modular Monolith backend.

Where Phase 1 established architectural principles, Phase 2 defined business application boundaries, Phase 3 selected REST over HTTP, JSON serialization, OpenAPI 3.x, and Spring Web MVC as the API technology baseline, Phase 4 D00 established overall governance, D01 established Maven and Java root package conventions, D02 established canonical module layouts and inbound adapter rules, and D03 established persistence architecture, this document (AEOS-P04-D04) establishes how external API transport adapters are physically structured, governed, and executed.

Specifically, this document:
1. Evaluates and formally resolves Open Decision **O4** (OpenAPI Implementation Approach).
2. Establishes the Spring Web MVC REST controller implementation architecture inside `<module>.adapter.inbound.web`.
3. Defines the strict 3-tier model separation between HTTP DTOs, Application Commands/Queries, and Pure Domain Models.
4. Defines the boundary between transport syntactic validation (Bean Validation) and business invariant validation.
5. Establishes transport error translation architecture (`ProblemDetail` / RFC 7807) and HTTP status code governance.
6. Defines the architectural API versioning strategy, content negotiation, and governed Jackson JSON serialization conventions.
7. Establishes technical integration with security context abstractions, delegating security plumbing to D06.
8. Enforces strict in-process module isolation, prohibiting inter-module HTTP REST calls across business modules.
9. Defines API testing strategy (`@WebMvcTest`), OpenAPI contract verification, and AI implementation governance for APIs.

> "D04 establishes HOW external HTTP/REST transport adapters and OpenAPI specifications are structured and executed across all business modules. It does not define specific business API endpoints, URLs, request/response DTO fields, or business workflows, which remain governed by authoritative business requirements and downstream implementation tasks."

---

# 3. Scope

## 3.1 In Scope

The scope of this document is strictly limited to backend API and transport implementation architecture:
- Evaluation and resolution of Open Decision **O4** (OpenAPI Implementation Approach).
- Organization of Spring Web MVC `@RestController` classes inside `com.anverraglobal.<module>.adapter.inbound.web`.
- Relationship between controllers, HTTP DTOs, API Mappers, and application-owned inbound ports (`port.inbound`).
- Model separation between HTTP Request/Response DTOs, Application Commands/Queries, and Pure Domain Models.
- Transport syntactic validation using Jakarta Bean Validation (`@Valid`, `@NotNull`, `@Size`, `@Pattern`).
- Transport error handling architecture (`@RestControllerAdvice`, RFC 7807 `ProblemDetail`).
- HTTP status code governance principles (2xx, 4xx, 5xx).
- API versioning architecture (URI path vs header vs media-type versioning mechanisms).
- Governed Jackson JSON object mapper serialization conventions (date/time representation, property naming, null handling).
- Integration between HTTP web adapters and security context abstractions.
- Hard isolation rule prohibiting inter-module HTTP REST calls.
- API testing strategy using Spring MVC slice tests (`@WebMvcTest`) and contract verification tools.
- CORS configuration boundaries and browser security defaults.
- ArchUnit rules for web adapter structural verification.
- AI implementation governance rules for API controller and transport authoring.

## 3.2 Explicitly Out of Scope

The following concerns are explicitly outside the scope of D04 and belong to designated Phase 4 design documents or downstream implementation tasks:
- **O5:** OpenAPI client generation approach — assigned to **AEOS-P04-D07**.
- **O9:** Shared vs independently generated client API types — assigned to **AEOS-P04-D07**.
- **O7:** Event listener idempotency mechanism — assigned to **AEOS-P04-D05**.
- Business API endpoints, URL paths (e.g., `/api/customers`), HTTP DTO Java fields, or JSON payload schemas.
- Domain model attributes, aggregate root methods, or business calculation logic.
- Persistence schemas, database tables, Flyway SQL scripts, or Spring Data JDBC repositories — governed by **AEOS-P04-D03**.
- Event listener retry queues, outbox tables, or event payload schemas — assigned to **AEOS-P04-D05**.
- Security filter chains, OAuth2/OIDC token verification details, JWT parsing, or role permission matrix tables — assigned to **AEOS-P04-D06**.
- API Modeling or transport design for unresolved business capabilities (`Agent`, `Sub-Agent`, `Dealer`, `Partner`, `Organization`, `Proposal`, `Document`, `KYC`, `Administration`).

---

# 4. Architectural Context

The AnverraGlobal system executes as a single-process **Modular Monolith** running on Java 21, Spring Boot 3, and Spring Web MVC within a single JVM, as established by Phase 1 (AEC-ARC-003), Phase 2 (AEOS-P02-S01-D01), Phase 3 (AEOS-P03-D01), and Phase 4 D00 (AEOS-P04-D00).

Prior Phase 4 documents established backend implementation baselines:
- **Build Tool (O1):** Apache Maven (`pom.xml`) — resolved in D01.
- **Java Root Package (O2):** `com.anverraglobal` — resolved in D01.
- **Inbound Adapter Directory (O3):** `adapter/inbound/` & `adapter/outbound/` — resolved in D02.
- **Inbound Web Adapter Location:** `com.anverraglobal.<module>.adapter.inbound.web` — established in D02.
- **Persistence Architecture:** `adapter.outbound.persistence`, application-owned outbound ports — established in D03.

D03 established persistence architecture. D04 defines the external HTTP transport layer that invokes application use cases.

```
com.anverraglobal.<module>/
├── domain/                                    (Pure Business Models — ZERO Framework Annotations)
├── application/                               (Use-Case Services — Implements Inbound Ports)
├── port/
│   ├── inbound/                               (Application-Owned Inbound Use-Case Interfaces)
│   └── outbound/                              (Application-Owned Outbound Repository Interfaces)
└── adapter/
    ├── inbound/
    │   └── web/                               (REST Controllers, HTTP DTOs, API Mappers)
    └── outbound/
        └── persistence/                       (Persistence Adapters, JDBC Repositories, Entities)
```

---

# 5. API Implementation Model

AnverraGlobal API transport operates on three core principles:

1. **Hexagonal Transport Adapter Role:** Web controllers are pure transport adapters. They receive HTTP requests, parse JSON into HTTP DTOs, validate syntax, map DTOs to application commands/queries, invoke inbound ports (`port.inbound`), map application results to response DTOs, and serialize JSON responses.
2. **Inbound Port Enforcement:** Web controllers depend strictly on application-owned inbound port interfaces (`port.inbound`). They NEVER depend directly on application service concrete classes, domain entities, or database repositories.
3. **Strict Model Isolation:** HTTP DTOs exist exclusively within `adapter.inbound.web` and are NEVER passed into the application or domain layers. Pure domain models are NEVER returned directly across the HTTP boundary.

```
┌──────────────────────────────────────────────────────────────────────────────────────────┐
│                                   EXTERNAL API CONSUMER                                  │
└────────────────────────────────────────────┬─────────────────────────────────────────────┘
                                             │ HTTP / JSON
                                             ▼
┌──────────────────────────────────────────────────────────────────────────────────────────┐
│                             INBOUND WEB TRANSPORT ADAPTER                                │
│                         (com.anverraglobal.<module>.adapter.inbound.web)                 │
│                                                                                          │
│  ┌─────────────────────────────┐   maps ↔    ┌──────────────────────────────────┐  │
│  │ <Concept>Controller.java    │────────────►│ <Concept>ApiMapper.java          │  │
│  │ (@RestController)           │             │ (HTTP DTO ↔ App Command/Query)  │  │
│  └──────────────┬──────────────┘             └──────────────────────────────────┘  │
│                 │ (uses)                                                           │
│                 ▼                                                                  │
│  ┌─────────────────────────────┐             ┌──────────────────────────────────┐  │
│  │ <Concept>RequestDto.java    │             │ <Concept>ResponseDto.java        │  │
│  │ (@Valid Syntactic Rules)    │             │ (JSON Wire Format)               │  │
│  └─────────────────────────────┘             └──────────────────────────────────┘  │
└────────────────────────────────────────────┬─────────────────────────────────────────────┘
                                             │ (invokes port)
                                             ▼
┌──────────────────────────────────────────────────────────────────────────────────────────┐
│                                   BUSINESS MODULE CORE                                   │
│                                                                                          │
│  ┌───────────────────────────┐                    ┌───────────────────────────────────┐  │
│  │       INBOUND PORT        │                    │         APPLICATION LAYER         │  │
│  │   (port.inbound)          │◄───────────────────┤        (application/)             │  │
│  │   UseCaseInboundPort.java │    (implements)    │   ApplicationService.java         │  │
│  └───────────────────────────┘                    └─────────────────┬─────────────────┘  │
│                                                                     │ (orchestrates)     │
│                                                                     ▼                    │
│                                                   ┌───────────────────────────────────┐  │
│                                                   │           DOMAIN LAYER            │  │
│                                                   │            (domain/)              │  │
│                                                   │   PureDomainModel.java            │  │
│                                                   └───────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────────────────────────┘
```

---

# 6. REST / Spring Web MVC Baseline

D03 inherited PostgreSQL and Spring Data JDBC from Phase 3. D04 inherits **REST over HTTP**, **JSON serialization**, **OpenAPI 3.x**, and **Spring Web MVC** as the API technology baseline from Phase 3 (AEOS-P03-D01).

## 6.1 Framework & Transport Standards
- **Framework:** Spring Web MVC (`org.springframework.web.bind.annotation.*`).
- **Protocol:** REST over HTTP(S).
- **Default Media Type:** `application/json`.
- **Contract Standard:** OpenAPI 3.0 / 3.1 specification.

## 6.2 Controller Stereotype Rules
- Web controllers use the `@RestController` annotation.
- Request paths apply explicit `@RequestMapping` base paths matching module namespace boundaries.
- Controllers use explicit HTTP method annotations (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`).

---

# 7. Inbound Adapter Architecture

Every business module implements its web transport layer inside `com.anverraglobal.<module>.adapter.inbound.web`:

```text
com.anverraglobal.<module>.adapter.inbound.web/
├── <Concept>Controller.java          (Spring @RestController; Public)
├── <Concept>RequestDto.java          (HTTP Request DTO Record; Package-Private)
├── <Concept>ResponseDto.java         (HTTP Response DTO Record; Package-Private)
├── <Concept>ApiMapper.java           (Stateless DTO ↔ Command Mapper; Package-Private)
└── <Concept>ApiExceptionHandler.java (Transport Error Handler Component; Package-Private)
```

## 7.1 Component Responsibilities

1. **`<Concept>Controller`:** Spring `@RestController` exposing HTTP endpoints. It accepts HTTP DTOs, triggers Bean Validation, calls `ApiMapper` to convert DTOs to application commands/queries, invokes `port.inbound`, maps return values to Response DTOs, and returns `ResponseEntity`.
2. **`<Concept>RequestDto`:** Java `record` carrying HTTP request parameters and `@Valid` syntactic annotations. API DTOs should have the narrowest visibility compatible with Spring MVC, serialization, OpenAPI generation, and testing requirements.
3. **`<Concept>ResponseDto`:** Java `record` representing HTTP JSON response payloads.
4. **`<Concept>ApiMapper`:** Stateless mapping component converting HTTP DTOs to/from Application Commands/Queries.
5. **`<Concept>ApiExceptionHandler`:** Component mapping exceptions thrown during use-case execution into standard JSON error responses.

---

# 8. Controller Responsibility Rules

To preserve Hexagonal Architecture purity, controllers MUST adhere to strict boundary rules:

> [!IMPORTANT]
> **CONTROLLER RESPONSIBILITY BOUNDARIES:**
> 1. **Zero Business Logic:** Controllers MUST NOT calculate business values, evaluate business rules, or manipulate domain state.
> 2. **Zero Database Access:** Controllers MUST NOT import or reference Spring Data repositories, JPA/JDBC classes, or outbound persistence adapters.
> 3. **Zero Direct Domain Returns:** Controllers MUST NOT return classes from `domain/` directly across the HTTP network boundary.
> 4. **Port Inversion:** Controllers MUST invoke application use cases exclusively through interfaces in `port.inbound`.
> 5. **Phase 5 Clarification (Analytical APIs):** Operational modules (e.g., Policy, Commission) MUST NOT expose analytical or aggregate statistics APIs. As established in AEOS-P04-D16, all analytical/statistics APIs must be exposed by the `Reporting` module.

---

# 9. DTO Separation Strategy

D04 establishes a strict 3-tier model separation architecture:

$$\text{HTTP Request/Response DTO} \neq \text{Application Command/Query} \neq \text{Pure Domain Model}$$

## 9.1 Model Tier Definitions

1. **HTTP DTOs (`adapter.inbound.web`):** Wire representation tailored for external API consumers. Carries `@Valid` syntactic annotations and Jackson serialization metadata.
2. **Application Commands/Queries (`application` / `port.inbound`):** Application-layer models associated with inbound use cases. Technology-agnostic Java records/classes expressing use-case input parameters without transport or framework annotations.
3. **Pure Domain Models (`domain`):** Pure Java domain entities, value objects, and aggregates encapsulating business rules and invariants.

```java
// CONCEPTUAL EXAMPLE — HTTP Request DTO (adapter.inbound.web)
package com.anverraglobal.sample.adapter.inbound.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SampleRequestDto(
    @NotBlank String name,
    @NotNull Integer amount
) {}
```

```java
// CONCEPTUAL EXAMPLE — Application Command (application / port.inbound)
package com.anverraglobal.sample.application;

public record ExecuteSampleCommand(
    String name,
    int amount
) {}
```

*(Note: The `Sample*` examples are non-business placeholders and do not represent approved AnverraGlobal capabilities.)*

---

# 10. Inbound Port Integration

Following D02 §13, inbound ports are application-owned Java interfaces inside `port.inbound` expressing use-case entry points.

```text
┌────────────────────────────────────────────────────────┐
│ com.anverraglobal.<module>.adapter.inbound.web          │
│                                                        │
│   @RestController                                      │
│   public class SampleController {                      │
│       private final ExecuteSampleInboundPort port;     │
│                                                        │
│       @PostMapping                                     │
│       public ResponseEntity<SampleResponseDto> execute(│
│           @Valid @RequestBody SampleRequestDto dto) {  │
│           ExecuteSampleCommand cmd = mapper.toCommand(dto);│
│           SampleResult result = port.execute(cmd);     │
│           return ResponseEntity.ok(mapper.toDto(result));│
│       }                                                │
│   }                                                    │
└──────────────────────────┬─────────────────────────────┘
                           │
                           │ (invokes interface)
                           ▼
┌────────────────────────────────────────────────────────┐
│ com.anverraglobal.<module>.port.inbound                │
│                                                        │
│   public interface ExecuteSampleInboundPort {          │
│       SampleResult execute(ExecuteSampleCommand cmd);  │
│   }                                                    │
└────────────────────────────────────────────────────────┘
```

*(Note: The `Sample*` examples are non-business placeholders and do not represent approved AnverraGlobal capabilities.)*

---

# 11. O4 Evaluation & Resolution (OpenAPI Implementation Approach)

Phase 4 Open Decision **O4** requires formally resolving the OpenAPI implementation and specification governance approach.

## 11.1 Candidate Evaluation

### Candidate A — Code-First OpenAPI (`springdoc-openapi`)
Spring MVC controllers and DTOs are the authoritative source code artifacts. OpenAPI 3.x specifications are dynamically generated at runtime or build time using `springdoc-openapi`.
- *Tradeoffs:* Low contract duplication and strong Spring Boot integration. Risk of uncommitted contract drift if specifications are not validated in CI/CD.

### Candidate B — Design-First OpenAPI (`openapi-generator-maven-plugin`)
OpenAPI 3.x specification files are authored first as authoritative contracts, with Spring MVC controller interfaces and DTOs generated via Maven plugin during build execution.
- *Tradeoffs:* Strict contract discipline. Higher build tooling complexity, risk of spec-code drift, and generator customization friction.

### Candidate C — Hybrid / Governed Code-First OpenAPI
Source code implementation remains Java/Spring Web MVC-owned, but generated OpenAPI 3.x specifications are committed or extracted during CI/CD build execution and validated against contract breaking-change governance rules.
- *Tradeoffs:* Combines native Spring Boot implementation velocity with strict build-time contract verification, contract drift detection, and AI coding safety.

## 11.2 Core Contract Ownership Resolution
Central to Candidate evaluation is resolving the fundamental contract ownership question: *What is authoritative?*

D04 resolves that **Java/Spring Web MVC source code is the implementation source-of-truth**, while the **committable/extracted OpenAPI 3.x specification is the governed external API contract source-of-truth**.

## 11.3 Formal Resolution of O4

> [!IMPORTANT]
> **OPEN DECISION O4 IS FORMALLY RESOLVED:**  
> **Candidate C — Hybrid / Governed Code-First OpenAPI** is selected as the authoritative OpenAPI implementation approach for AnverraGlobal.

- Spring Web MVC controllers and HTTP DTOs are authored natively in Java.
- Build execution (via `springdoc-openapi-maven-plugin` during `mvn verify` / CI) extracts the OpenAPI 3.x specification.
- Extracted OpenAPI specifications are validated for backward compatibility and contract compliance before release.

---

# 12. OpenAPI Contract Architecture & Specification Governance

## 12.1 Contract Metadata Baseline
OpenAPI specifications generated across all business modules include standardized metadata:
- `openapi`: OpenAPI 3.0.x, with the exact supported patch version governed by the approved Springdoc/toolchain baseline.
- `info.title`: AnverraGlobal `<Module>` API.
- `info.version`: Matching project release version.
- `servers`: Environment-configurable base URLs.

## 12.2 Documentation Tooling & Discoverability
Generated OpenAPI specifications shall be discoverable through an agreed documentation/discovery mechanism. Concrete documentation routes remain deployment/configuration details.

---

# 13. Request Validation Architecture

D04 establishes a strict distinction between syntactic transport validation and business invariant validation:

```text
HTTP Request DTO
      │
      ▼
@Valid (Bean Validation)
      │
      ▼
Syntactic / Structural Validation
      │
      ▼
Application Command ──► Application Guard ──► Precondition Check
                                                        │
                                                        ▼ (valid precondition)
Pure Domain Model ──► Pure Java Method   ──► Business Invariant Validation
```

1. **Syntactic Transport Validation (`adapter.inbound.web`):** Evaluates JSON structure, required non-null attributes, string lengths, and format regexes using Jakarta Bean Validation (`@NotNull`, `@NotBlank`, `@Size`, `@Pattern`).
2. **Business Invariant Validation (`domain`):** Evaluates business rules, state transitions, and domain calculations inside pure Java domain constructors and methods. Bean Validation MUST NOT be used for domain business rules.

---

# 14. Response Serialization Architecture (Jackson Standards)

D04 establishes centralized Jackson serialization conventions covering date/time representation, property naming, enum representation, null handling, and unknown-property behavior. Exact Jackson configuration values shall be selected consistently with API compatibility, client interoperability, and downstream API design requirements.

- **Date/Time Formatting:** ISO-8601 JSON date/time representation is the default architectural direction.
- **Property Naming:** Property names follow lowercase camelCase conventions (`firstName`, `createdAt`).
- **Additional Conventions:** Null handling, unknown properties, and enum capitalization conventions shall be governed centrally during implementation to ensure uniformity.

---

# 15. Error Handling Architecture

D04 standardizes transport error translation without inventing concrete business error codes:

```text
Domain / Application Exception
       │
       ▼
Web Transport Exception Handler (adapter.inbound.web)
       │
       ▼ (maps to)
Standard JSON Error Payload (RFC 7807 ProblemDetail / Error Response)
       │
       ▼
HTTP 4xx / 5xx Status Code
```

## 15.1 Transport Exception Translation
- D04 evaluates exception handling architecture (module-local `@RestControllerAdvice` components, centralized API exception handling, or a combined approach) to establish a standardized technical mechanism for translating failures at the transport boundary into consistent JSON error responses.
- Exceptions thrown by application services or domain models (e.g. validation failure, resource absence, state conflict) are caught at the web adapter boundary and mapped to standard HTTP status codes.

## 15.2 Standardized JSON Error Payload (RFC 7807)
Errors return standardized JSON payloads adhering to RFC 7807 (`ProblemDetail`):
- `type`: URI reference identifying problem type.
- `title`: Short human-readable summary of HTTP error.
- `status`: HTTP status code.
- `detail`: Human-readable explanation of failure.
- `instance`: Request URI path.
- `traceId`: Request correlation identifier for operational tracking.

---

# 16. HTTP Status Code Governance

D04 establishes general status-code governance principles across all modules:

| HTTP Status | Category | Architectural Trigger |
|---|---|---|
| **`2xx`** | Success | Successful processing, retrieval, creation, or deletion. |
| **`4xx`** | Client Error | Client-side, request, resource, or authorization-related failures. |
| **`5xx`** | Server Error | Unexpected server-side infrastructure or processing failures. |

Where technically unambiguous, the following mapping principles apply:
- **Malformed Request:** Syntactic validation failure or parameter parsing error maps to `400 Bad Request`.
- **Unauthenticated:** Missing or invalid credentials maps to `401 Unauthorized`.
- **Unauthorized:** Authenticated user lacks authorization for the requested use case maps to `403 Forbidden`.

*(Note: Endpoint-specific decisions such as `404 Not Found`, `409 Conflict`, or `422 Unprocessable Entity` remain deferred to API contract design.)*

---

# 17. API Versioning Architecture

D04 establishes the architectural API versioning strategy:

## 17.1 Versioning Mechanism
D04 establishes that API versioning will be introduced only when backward-incompatible contract evolution requires it.

## 17.2 Route Governance Rule
Concrete version identifiers and versioned route paths (e.g., `/api/v1/...`) remain deferred to specific business API design tasks. D04 establishes the versioning strategy without inventing premature versioned route strings for uncreated business resources.

---

# 18. Content Negotiation & Media Types

1. **Default Request/Response Media Type:** `application/json`.
2. **Problem Detail Media Type:** `application/problem+json`.
3. **Accept Header Governance:** Controllers explicitly declare supported consumption (`consumes = MediaType.APPLICATION_JSON_VALUE`) and production (`produces = MediaType.APPLICATION_JSON_VALUE`) media types.

---

# 19. API Security Technical Boundary Integration

D04 defines the API web adapter's interaction with authenticated request security abstractions:

External authentication credentials are processed by the security infrastructure governed by D06. The API adapter consumes the resulting authenticated principal/security context without depending on the underlying authentication mechanism.

- **D04 Scope:** Controllers extract authenticated security context primitives (e.g. User Principal ID) and pass them as parameters into application commands.
- **D06 Delegation:** Detailed authentication mechanisms, token processing, security filter chains, and authorization role permissions are explicitly delegated to **AEOS-P04-D06**.

---

# 20. Cross-Module API Isolation

Phase 1 (`docs/01-constitution/03-architecture-principles/AEC-ARC-003-modular-monolith.md`) mandates that AnverraGlobal executes as a single-process Modular Monolith within a single JVM.

```text
PROHIBITED INTER-MODULE HTTP COUPLING:
com.anverraglobal.modulea.adapter.inbound.web.ModuleAController
       │
       │ (HTTP REST Call — PROHIBITED VIOLATION)
       ▼
com.anverraglobal.moduleb.adapter.inbound.web.ModuleBController


MANDATORY IN-PROCESS MODULE COLLABORATION:
com.anverraglobal.modulea.application.ModuleAApplicationService
       │
       │ (In-Process Java Method Call via Approved Public Surface)
       ▼
com.anverraglobal.moduleb.contracts.ModuleBContract (or Integration Event)
```

> [!IMPORTANT]
> **HARD ISOLATION RULE:** Business modules MUST NOT communicate with each other over HTTP/REST. Web controllers in `adapter.inbound.web` exist exclusively for external HTTP consumers. Internal module collaboration must execute strictly in-process through approved synchronous module interfaces (`contracts/`) or asynchronous integration events (`events/`).

---

# 21. API Testing Strategy

D04 establishes a testing strategy comprising:
- API adapter/slice tests
- API integration tests
- OpenAPI contract verification

## 21.1 Slice Testing
Spring MVC slice testing (such as `@WebMvcTest`) is the preferred mechanism where appropriate.
- Validates HTTP request mapping, URL routing, and parameter extraction.
- Verifies Bean Validation syntax rules.
- Verifies JSON serialization and error translation.

## 21.2 OpenAPI Contract Verification
CI/CD build execution verifies that generated or committed OpenAPI specifications remain backwards-compatible and conform to project API governance rules.

---

# 22. API Documentation & Discoverability

1. **Centralized Discovery:** Generated OpenAPI specifications shall be discoverable through the governed API documentation/discovery mechanism. Concrete specification routes remain deployment/configuration details.
2. **Contract Artifacts:** Generated OpenAPI specifications may be artifacted during build execution to support client generation in **AEOS-P04-D07**.

---

# 23. API Observability & Request Tracing

1. **Request Trace Propagation:** Web controllers integrate with HTTP tracing filters to extract or generate W3C Trace Context headers (`traceparent`, `tracestate`).
2. **Correlation ID:** Every HTTP request carries or receives a unique `X-Correlation-ID` header included in log MDC context and error payloads.
3. **HTTP Metrics:** Controller request execution timing and status code distributions monitored via Spring Boot Actuator and Micrometer HTTP metrics (`http.server.requests`).

---

# 24. API Boundary Governance (CORS & Rate-Limiting Interfaces)

1. **CORS Boundary:** CORS configuration is established at the appropriate backend HTTP/application boundary. Whether it is implemented within Spring Web MVC/Spring Security or an external infrastructure layer is determined by the deployment topology.
2. **Rate-Limiting Interfaces:** Rate limiting is a cross-cutting concern. D04 defines only the API boundary's compatibility requirements; the enforcement mechanism and infrastructure ownership are deferred to the appropriate security/infrastructure architecture.

---

# 25. Browser & Cross-Origin Security Boundaries

1. **Security Headers:** HTTP responses shall comply with approved browser security requirements established by the security architecture (D06).
2. **Content Type Enforcement:** Requests carrying JSON request bodies must declare a compatible JSON Content-Type. Requests without bodies are not required to provide a Content-Type header.

---

# 26. ArchUnit API Structural Verification Rules

D04 mandates that API structural rules are verified automatically on every build via ArchUnit:

```java
// CONCEPTUAL ARCHUNIT RULES FOR API TRANSPORT

// Rule 1: Controllers reside only in adapter.inbound.web
ArchRule controllerPlacementRule = classes()
    .that().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
    .should().resideInAPackage("com.anverraglobal..adapter.inbound.web..");

// Rule 2: Controllers do not access repositories or DB entities
ArchRule controllerNoPersistenceRule = noClasses()
    .that().resideInAPackage("com.anverraglobal..adapter.inbound.web..")
    .should().dependOnClassesThat()
    .resideInAPackage("com.anverraglobal..adapter.outbound.persistence..");

// Rule 3: Controllers depend on port.inbound, not concrete application services
ArchRule controllerPortInversionRule = noClasses()
    .that().resideInAPackage("com.anverraglobal..adapter.inbound.web..")
    .should().dependOnClassesThat()
    .resideInAPackage("com.anverraglobal..application..");
```

---

# 27. AI Implementation Governance for APIs

When generating or modifying API code, human engineers and AI coding agents MUST strictly follow these rules:

1. **Obey Inbound Web Location:** Place all REST controllers, HTTP DTOs, API mappers, and exception handlers strictly inside `com.anverraglobal.<module>.adapter.inbound.web`.
2. **Never Put Business Logic in Controllers:** Controllers MUST NOT calculate business values or evaluate domain invariants.
3. **Never Access Repositories:** Controllers MUST NOT import repositories or entities from `adapter.outbound.persistence`.
4. **Never Call Modules Over HTTP:** Web controllers MUST NOT make HTTP REST calls to another module's web endpoints.
5. **Never Expose Domain Models:** Controllers MUST NOT return pure domain models directly across the HTTP network boundary.
6. **No Business Endpoints in D04:** Do not invent specific URL routes, endpoints, or DTO fields within D04 itself.
7. **No Unresolved Capability APIs:** NEVER create controllers or DTOs for `Agent`, `Partner`, `Dealer`, `Organization`, `Proposal`, `Document`, `KYC`, or `Admin`.
8. **Use Inbound Ports:** Controllers MUST invoke application use cases strictly via interfaces in `port.inbound`.
9. **Do Not Resolve Downstream Open Decisions:** Keep O5 and O9 open for D07, and O7 open for D05.

---

# 28. Deferred Decisions Register

D02, D03, and D04 explicitly preserve the open status of downstream design decisions:

| Decision ID | Description | Assigned Document | Status |
|---|---|---|---|
| **O1** | Build Tool (Apache Maven) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O2** | Java Root Package (`com.anverraglobal`) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O3** | Inbound Adapter Package (`adapter/inbound/`) | **AEOS-P04-D02** | **RESOLVED (D02)** |
| **O4** | OpenAPI implementation approach | **AEOS-P04-D04** | **RESOLVED (D04)** |
| **O5** | OpenAPI client generation approach | **AEOS-P04-D07** | **OPEN** |
| **O6** | PostgreSQL schema naming strategy | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O7** | Event listener idempotency mechanism | **AEOS-P04-D05** | **OPEN** |
| **O8** | DataSource configuration pattern | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O9** | Shared vs independently generated client API types | **AEOS-P04-D07** | **OPEN** |

Additionally, D04 defers specific business API endpoints, URLs, HTTP request/response DTOs, and client generation choices to downstream feature requirements and D07.

---

# 29. Traceability

D04 maintains complete traceability to prior authoritative documents:

## 29.1 Phase 1 — Engineering Constitution
- `docs/01-constitution/03-architecture-principles/AEC-ARC-003-modular-monolith.md`
- `docs/01-constitution/03-architecture-principles/AEC-ARC-004-hexagonal-architecture.md`
- `docs/01-constitution/03-architecture-principles/AEC-ARC-006-dependency-direction.md`

## 29.2 Phase 2 — System & Module Blueprints
- `docs/02-repository-blueprint/01-system-repository-blueprint/01-system-blueprint.md`
- AEOS-P02-S02-D01 through D07 (Business Module Blueprints)

## 29.3 Phase 3 — Technology Blueprints
- `docs/03-technology/01-backend-technology-blueprint.md`

## 29.4 Phase 4 — System Design Documents
- `docs/04-system-design/00-phase-4-overview.md`
- `docs/04-system-design/01-backend-implementation-architecture.md`
- `docs/04-system-design/02-module-implementation-architecture.md`
- `docs/04-system-design/03-persistence-implementation-architecture.md`

---

# 30. Definition of Done & Final Baseline Status

## 30.1 Definition of Done
This document (AEOS-P04-D04) is complete when:
1. Open Decision **O4** (OpenAPI Implementation Approach) is formally resolved (Hybrid / Governed Code-First OpenAPI selected).
2. The Spring Web MVC REST controller implementation architecture inside `<module>.adapter.inbound.web` is defined.
3. Strict 3-tier model separation (HTTP DTO $\neq$ Application Command $\neq$ Domain Model) is enforced.
4. Transport validation (Bean Validation) is distinguished from domain invariant validation.
5. Transport error translation (`ProblemDetail` / RFC 7807) and HTTP status code governance principles are established.
6. API versioning strategy and Jackson JSON serialization conventions are established as governed abstractions without premature hardcoding.
7. Technical integration with security context abstractions is defined, delegating filter plumbing to D06.
8. Hard prohibition of inter-module HTTP REST calls is established.
9. API adapter/slice testing, integration testing, and OpenAPI contract verification are defined.
10. All 30 required sections are present and fully articulated.
11. No source code, Java classes, business API endpoints, URLs, or downstream documents (D05+) were created.

## 30.2 Final Status
This document is authored and recorded as **Baseline Candidate**.

## 30.3 Stop Rule & Next Step
- **Authoring Position 5 Complete:** AEOS-P04-D04 is fully authored.
- **Do NOT proceed to AEOS-P04-D05.**
- **Do NOT create Java classes, API endpoints, or database schemas.**
- **Awaiting formal architectural review before proceeding to D05 (Event & Asynchronous Architecture).**
