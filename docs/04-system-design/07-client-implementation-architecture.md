# Client Implementation Architecture

**Document ID:** AEOS-P04-D07  
**Version:** 1.0  
**Status:** Baselined  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 8  
**Depends on:** Phase 1 Engineering Constitution · Phase 2 System Architecture · Phase 3 Technology Architecture · AEOS-P04-D00 through D06

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D07 |
| **Title** | Client Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baselined |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |

---

# 2. Purpose

This document establishes the implementation architecture for external Web and Mobile clients that consume the backend REST/JSON API boundary established in AEOS-P04-D04. It evaluates and resolves key technology decisions regarding client frameworks, rendering models, API consumption, state management, and security integration, strictly adhering to the backend architectural constraints.

---

# 3. Scope

## 3.1 In Scope
- Web and Mobile client technology selection.
- Rendering and application models.
- Language strategy.
- API client and OpenAPI consumption architecture.
- Shared client code strategy.
- Client state architecture.
- Client authentication and security integration (consuming D06).
- Error handling, real-time updates, accessibility, performance, and observability.
- Testing, build, and dependency architecture.
- AI development safety guardrails.

## 3.2 Out of Scope
- Backend domain models and business workflows.
- API endpoints, URLs, request/response schemas, and DTO definitions.
- Database schemas and persistence implementation.
- Internal module contracts and asynchronous messaging (Kafka/RabbitMQ/Spring Modulith).
- Backend authorization policies, roles, and permissions.
- Deployment infrastructure.
- Specific UI component library selection (deferred).

---

# 4. Architectural Context

The authoritative architecture dictates that the client is strictly an external consumer of the backend.

```text
       Client (Web / Mobile)
                 │
                 ▼
    D04 REST/JSON API Boundary
                 │
                 ▼
      Backend Modular Monolith
                 │
                 ▼
       Application / Domain
```

---

# 5. Client Trust Boundary

The client is an **external, untrusted consumer**.
It operates outside the secure backend perimeter. All data provided by the client must be treated as untrusted and subject to rigorous validation and authorization by the backend Modular Monolith.

---

# 6. Client Responsibilities

The client:
- Owns presentation and user interface rendering.
- Owns interaction state (e.g., dropdowns, form inputs).
- Owns temporary UI state.
- May cache remote/server state for performance.
- Consumes the backend API according to the established OpenAPI contract.

---

# 7. Client Non-Responsibilities

The client:
- Does NOT own authoritative business data.
- Does NOT own business rules or invariants.
- Does NOT own authorization authority.
- Does NOT access persistence directly.
- Does NOT access internal backend modules.
- Does NOT access backend event infrastructure.

> [!IMPORTANT]  
> **Client-side state is never the authoritative source of business truth.**

---

# 8. Web Technology Evaluation

Candidates considered: React + TypeScript, Next.js + TypeScript, Vue + TypeScript, Angular + TypeScript.

The candidates were evaluated against AnverraGlobal's enterprise requirements: API boundary separation, SPA suitability, TypeScript/type safety, client/backend separation, authentication integration, state-management fit, testing, accessibility, performance, maintainability, developer experience, AI-development safety, ecosystem maturity, and architectural complexity.

**Evaluation & Decision:**
**React + TypeScript** is selected. It enforces a strict client/backend separation by remaining a pure SPA without introducing a secondary server runtime. It aligns closely with the selected mobile architecture (React Native) allowing shared API and presentation paradigms, and its state management and testing ecosystems strongly support AnverraGlobal's requirement for decoupled, contract-driven API consumption. Next.js is explicitly rejected because its SSR and server-component models risk creating redundant backend logic and duplicated authorization layers.

---

# 9. Web Rendering/Application Model

Candidates considered: Single Page Application (SPA), Server-Side Rendering (SSR), Static Site Generation (SSG), Hybrid.

**Evaluation & Decision:**
**Single Page Application (SPA)** is selected.
AnverraGlobal is a complex, highly interactive business application. It does not require SEO-optimized public landing pages for its core authenticated workflows. A strict SPA model enforces clean client/backend separation. Introducing SSR would require a Node.js runtime, adding deployment complexity and risking the migration of backend logic into the presentation tier.

---

# 10. Mobile Technology Evaluation

Candidates considered: React Native + Expo, Flutter, Native Android/iOS.

The candidates were evaluated against: API integration, D06 authentication integration, secure credential storage, native capability, performance, platform integration, Web alignment, maintainability, testing, build/release complexity, AI-development safety, ecosystem maturity, and architectural risks.

**Evaluation & Decision:**
**React Native + Expo** is selected. It maintains TypeScript alignment across Web and Mobile, enabling reliable consumption of generated OpenAPI client artifacts. It provides access to OS-provided secure credential storage required by D06 while offering sufficient native performance and platform integration for AnverraGlobal's business application requirements.

---

# 11. Language Strategy

Candidates considered: TypeScript, Dart, Kotlin/Swift.

**Evaluation & Decision:**
**TypeScript** is selected for both Web and Mobile clients.
TypeScript provides strong compile-time type safety and aligns with OpenAPI contract generation. Generated types improve compile-time confidence when consuming the governed API contract; runtime validation remains the responsibility of the backend, with client-side runtime validation used only where technically justified. The selection is driven by type safety and API contract alignment, not merely the ability to share UI code.

---

# 12. API Client Architecture

Clients depend strictly on the external API contract.

- **Boundary:** Clients depend on the external API contract rather than backend Java DTO classes or implementation types.
- **Prohibited:** Clients MUST NOT depend on Java classes, Spring classes, backend domain entities, persistence entities, internal module interfaces, or backend implementation classes.
- **Decision:** The architecture mandates **Generated API Clients and TypeScript Types** derived directly from the D04 OpenAPI specification.
- **Rule:** Generated API clients are the primary mechanism for API communication. Any hand-written client adapters must remain thin, contract-preserving wrappers and must not redefine API schemas or business rules. Runtime validation on the client is generally deferred to TypeScript compile-time checks, relying on the backend for authoritative runtime validation.

---

# 13. OpenAPI Consumption Strategy

The D04 OpenAPI contract is the authoritative source of truth.

- **Strategy:** The client build process shall generate strongly typed API client artifacts from the governed OpenAPI contract using an approved OpenAPI-compatible generation tool. The exact generator and transport/client library are implementation-level decisions unless separately baselined.
- **Versioning:** The generated client artifacts will be versioned alongside the OpenAPI spec to ensure compile-time failure if breaking API changes occur.

---

# 14. Shared Client Code Strategy

Candidates considered: No sharing, Shared generated API types/clients, Shared UI components.

**Evaluation & Decision:**
**Shared Generated API Contract/Client Package** is approved.
Shared API contract/client artifacts are preferred where technically compatible; Web and Mobile remain independently bounded applications. Shared code must not create runtime coupling between the applications.
- **Explicit Prohibition:** Sharing is strictly limited to governed API contracts, generated API types, and generated API client artifacts where technically compatible. It is explicitly prohibited to share: backend domain logic, backend application services, persistence logic, Spring components, Java classes, or backend implementation code.

---

# 15. Client State Architecture

Client state is fundamentally divided into:
1. **Server/API State:** Remote data cached locally (e.g., using TanStack Query). The client merely reflects this state.
2. **Local UI State:** Ephemeral interaction state (e.g., open modals, form input).
3. **Derived Presentation State:** Computed from Server/API state for rendering.

**Decision:** The client will utilize **Server-State Caching** for API data and localized component state (e.g., React `useState`) for UI state. Global client-state management should be introduced only where genuinely required for cross-component client state that is neither server state nor derived presentation state. The client must not duplicate backend business rules in its state management.

---

# 16. Client Authentication Integration

D06 is authoritative (O11). D07 establishes the integration:

- **Web:** JWT credentials are transported via the **Secure + HttpOnly cookie** mechanism. JavaScript application code does not access or store the JWT.
- **Mobile:** JWT credentials are transported via the **Authorization-header** mechanism. The credentials MUST be stored using OS-provided secure credential storage appropriate to the selected mobile platform (for example, the secure storage facilities exposed by the selected mobile framework).

The client does not invent authentication endpoints or workflows; it integrates with the formally established authentication mechanism without defining authentication operations or endpoints.

---

# 17. Client Authorization and Routing

D06 is authoritative (O12).

- **Principle:** Client-side authorization is **NOT authoritative**.
- **Mechanisms:** Client route guards, protected navigation, and conditional UI visibility (e.g., hiding buttons based on claims) are purely **UX mechanisms**.
- The client may use formally established authorization-related information to improve user experience, such as hiding or disabling unavailable actions. Such client-side behavior is never authoritative; the backend independently enforces authorization.

---

# 18. Error Handling Architecture

The client must handle D04's API error contract consistently.

- **Network/timeout failures:** Provide resilient user-facing handling, retry behavior where appropriate, and clear recovery paths.
- **Authentication failures:** Handle according to the D04 API error contract and D06 authentication architecture.
- **Authorization failures:** Provide appropriate user experience without exposing backend implementation details.
- **Validation failures:** Map contract-defined validation information to the appropriate UI.
- **Server failures:** Provide generic user-facing failure handling while preserving diagnostic information through approved observability mechanisms.

---

# 19. Real-Time / Asynchronous Client Updates

D05 is authoritative for backend async messaging. Clients MUST NOT directly connect to Spring Modulith, PostgreSQL events, Kafka, or RabbitMQ.

**Evaluation & Decision:**
Manual refresh may be implemented where appropriate. Any automated polling or server-push mechanism requiring backend support must be separately architected at the API/infrastructure boundary.

---

# 20. Client Security Architecture

D06 is authoritative. Client security supplements, but does not replace, backend security.

- **XSS Protection:** The client must use framework-safe rendering practices and comply with the approved Content Security Policy and security architecture. Client-side protections supplement backend validation and security controls.
- **Secure Storage:** As defined in Section 16 (HttpOnly cookies / OS secure storage).
- **Secrets:** The client application MUST NEVER embed secrets, private credentials, signing keys, or other confidential configuration in the client bundle. Public configuration may be exposed only where it is intentionally classified as public by the governing architecture.
- **Authoritative Checks:** Client-side checks are never treated as authoritative security.

---

# 21. Accessibility Architecture

Accessibility is a fundamental architectural requirement.

- **Requirements:** Semantic HTML/UI components, keyboard navigation, focus management, and WCAG alignment.
- **UI Library:** The specific UI component library is **Deferred**. Any selected UI library must be evaluated for accessibility support and must not prevent the application from meeting its accessibility requirements.

---

# 22. Performance Architecture

- **Web:** Strict code splitting, lazy loading of distinct application boundaries, and optimized bundle sizes.
- **Mobile:** Optimized startup time, efficient memory usage, and minimizing rendering thrash.
- No specific numeric performance targets are established at this architectural stage.

---

# 23. Client Observability

- **Telemetry:** Client error reporting and performance telemetry must be supported through the approved observability mechanism.
- **Correlation:** Clients should propagate approved correlation/trace metadata according to the established API and observability architecture.
- **Privacy:** Client telemetry MUST NOT expose JWTs, credentials, secrets, or unnecessary sensitive business information.

---

# 24. Client Testing Architecture

- **Unit/Component Testing:** For isolated UI logic and component rendering.
- **Integration Testing:** For complex component interactions and form behaviors.
- **API Mocks:** API integration testing should rely on OpenAPI-driven mock handlers using an approved contract-mocking mechanism (for example, MSW), rather than brittle manual mocks.
- **E2E Testing:** Critical paths verified via automated browser/device testing.

---

# 25. Build, Package and Dependency Architecture

Client builds must use lockfile-controlled dependency resolution and reproducible build processes. Web and Mobile retain independently bounded build pipelines while consuming approved shared generated API artifacts where technically compatible. The exact package manager and build tooling are implementation-level decisions unless separately baselined.

- **Dependency Governance:** Automated vulnerability scanning must be integrated into the CI pipeline.
- **Environment Configuration:** Build-time environment variables for public configuration (e.g., API base URL). Secrets are prohibited.

---

# 26. AI Development Safety

AI agents and automated tools MUST NOT:
- Invent API endpoints, payloads, or API representations defined by the governed OpenAPI contract.
- Invent business workflows, Agent/Sub-Agent workflows, or notification workflows.
- Invent roles or permissions.
- Create fake backend APIs merely to satisfy UI requirements.
- Bypass the D04 OpenAPI contract, authentication, or authorization.
- Store credentials insecurely or place secrets in client bundles.
- Implement authoritative backend business logic in clients.
- Directly access databases or backend messaging infrastructure.
- Make client state authoritative.

All AI-generated client code must be traceable to D07, the D04 API contract, and D06 security architecture.

---

# 27. Technology Decision Register

| Decision ID | Decision | Selected Option | Rationale & Architectural Consequences |
|---|---|---|---|
| **O17** | Web Technology | **React + TypeScript** | Strong ecosystem, aligns with mobile strategy. |
| **O18** | Web Rendering Model | **SPA** | SSR introduces redundant backend complexity. SPA enforces clean boundaries. |
| **O19** | Mobile Technology | **React Native + Expo** | Aligns language (TS) and enables shared API client consumption. |
| **O20** | Client Language Strategy | **TypeScript** | Type safety and exact OpenAPI contract alignment. |
| **O21** | API Client Consumption (O5) | **Generated API Client / Types from governed OpenAPI contract.** (Resolves O5 — OpenAPI client generation approach: Architectural strategy resolved through O21. The concrete generator tool is an implementation-level decision deferred to client implementation planning). | Prevents drift; client strictly depends on external contract. |
| **O22** | Shared Client Code (O9) | **Shared Generated API Contract/Client where technically compatible.** (Resolves O9 — Shared vs independently generated Web/Mobile API types). | Reduces duplication of API boilerplate. Domain logic sharing is prohibited. |
| **O23** | Client State Architecture | **Server-State Caching + Local UI State.** | Prevents client from becoming an authoritative data store. |
| **O24** | Client Testing Architecture | **Component + API Contract Mock Testing Architecture.** | Isolates client testing to the OpenAPI contract boundary. |
| **O25** | Reproducible Client Build/Package Strategy | **Lockfile-controlled, reproducible client builds with independently bounded Web and Mobile build pipelines.** | Provides deterministic dependency resolution and reproducible builds while preventing shared client code from creating runtime coupling between Web and Mobile. |
| **O26** | Real-Time Update Strategy | **Deferred — no client real-time transport established at current architectural stage.** | Any future client-visible real-time requirements require a separate API/infrastructure architecture decision. |

---

# 28. Alternatives Considered and Rejected

- **Next.js (SSR):** Rejected. Next.js is not selected for the current architecture because using its SSR/server-side application capabilities would introduce an additional server runtime and could create redundant backend/API or authorization responsibilities. The current authenticated business application does not establish a requirement that justifies that additional runtime boundary.
- **Flutter:** Rejected. While high-performing, the required Dart ecosystem diverges from the selected Web TypeScript strategy. Maintaining a unified TypeScript language strategy provides stronger overall architectural consistency.
- **Native Android/iOS:** Rejected. While offering superior platform capability, the additional implementation cost is not justified by current business-client requirements.
- **Hand-written API Clients:** Rejected. Brittle, prone to drift, and lacks guaranteed alignment with the authoritative OpenAPI contract.

---

# 29. Deferred Decisions

The following are explicitly deferred:
- Specific UI component library (e.g., MUI, Tailwind, Radix).
- Exact API endpoints and payload schemas (deferred to implementation).
- Exact authentication operations (deferred to implementation).
- Server-push/WebSocket infrastructure.
- Deployment-specific client configuration and production domains.
- Business workflows, roles, permissions, and Agent models.

---

# 30. Traceability

- **Phase 1 (Engineering Constitution):** Upholds client/server separation and domain logic isolation.
- **AEOS-P04-D04:** Strict reliance on the OpenAPI contract.
- **AEOS-P04-D05:** Prohibits direct client connection to async messaging infrastructure.
- **AEOS-P04-D06:** Implements specified JWT transport mechanisms and respects backend authorization authority.

---

# 31. Definition of Done

This document is complete when:
- [x] Client boundary is preserved and Backend remains authoritative.
- [x] No direct database or messaging infrastructure access is permitted.
- [x] Authentication aligns completely with D06.
- [x] Authorization remains backend-authoritative.
- [x] No business logic is migrated into clients.
- [x] OpenAPI remains the strict API contract.
- [x] No API endpoints, payloads, or schemas are invented.
- [x] Client state remains non-authoritative.
- [x] Secure credential handling is established.
- [x] AI development guardrails are explicitly defined.
- [x] All decisions have objective rationale and deferred decisions are explicit.

---

# 32. Final Decision Status

**Final Decision Status:** Baselined
