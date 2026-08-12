# Client Technology Blueprint

**Document ID:** AEOS-P03-D05  
**Version:** 3.0  
**Status:** Proposed  
**Phase:** 3 — Technology Selection & Architecture Enablement  
**System:** AnverraGlobal

---

# 1. Document Identity
**Title:** Client Technology Blueprint  
**ID:** AEOS-P03-D05

# 2. Purpose
The purpose of this document is to evaluate and select the Web and Mobile client technology stacks required to implement the external application boundaries for AnverraGlobal. It ensures the chosen technologies properly consume the established REST API boundary without duplicating backend business logic or violating Phase 1 and Phase 2 constraints.

# 3. Scope
The scope covers Web frameworks, Mobile approaches, client languages, state management, UI architecture, rendering models, and API client integration strategies. It explicitly does NOT design backend business modules, domain models, API endpoints, integration events, or unresolved business capabilities.

# 4. Architectural Context
Phase 2 established that Web and Mobile applications reside completely outside the backend Modular Monolith. The backend remains the authoritative source for business truth, persistence, and authorization. Phase 3 has established the API boundary (D03: REST/JSON/OpenAPI) that clients must consume and the internal messaging boundary (D04: Spring Modulith in-process events) from which clients are completely isolated.

# 5. Web Technology
This section evaluates the primary Web presentation technology against AnverraGlobal's requirement for a decoupled API consumer.

- **React + TypeScript:** Provides a mature component model, unopinionated architecture, and strong ecosystem for building SPAs. Relies heavily on external libraries for routing and state.
- **Next.js + TypeScript:** Extends React with opinionated routing, Server-Side Rendering (SSR), and full-stack capabilities. Introduces an intermediate server runtime.
- **Vue + TypeScript:** Provides a highly approachable, progressive framework with excellent built-in state management and reactivity. Smaller enterprise ecosystem than React.
- **Angular + TypeScript:** Provides a highly opinionated, full-featured enterprise framework with built-in DI, routing, and HTTP clients. Steeper learning curve.

**Decision:** **React + TypeScript** is selected. The architecture requires a strict client-side consumer of the REST API boundary. React provides the most flexible, decoupled SPA foundation with the strongest ecosystem for tooling and AI predictability, without imposing an opinionated full-stack architecture that blurs the frontend/backend boundary.

# 6. Mobile Technology
This section evaluates the approach for delivering the AnverraGlobal mobile experience.

- **React Native + Expo:** Allows building cross-platform (iOS/Android) mobile apps using React and TypeScript. Shares conceptual architecture and potentially API-client logic with the Web stack.
- **Flutter:** Google's cross-platform UI toolkit using Dart. Provides excellent performance and highly custom UI rendering. Completely isolates the Mobile ecosystem from the Web ecosystem in language and tooling.
- **Native Android/iOS (Kotlin/Swift):** Unmatched performance and deep hardware integration. Requires maintaining two completely separate codebases, teams, and CI/CD pipelines.

**Decision:** **React Native + Expo** is selected. The architectural benefit of unifying Web and Mobile around a single language (TypeScript), a shared API consumption strategy (generated OpenAPI clients), and a shared conceptual component model (React) drastically outweighs the minor performance gains of Native or the custom rendering engine of Flutter for standard business forms and data presentation.

# 7. Language Strategy
**Evaluation:** TypeScript vs. Native Web/Mobile Languages (JavaScript, Dart, Kotlin, Swift).
**Decision:** **TypeScript** is selected for the approved Web and Mobile client stacks.
**Rationale:** TypeScript provides strict structural typing that is crucial for consuming the backend's OpenAPI 3.0 contracts. It eliminates a massive class of runtime errors, ensures frontend types stay aligned with backend DTOs, and provides the structural predictability required for safe AI-assisted development.

# 8. Rendering / Application Model
The architecture requires evaluating the deployment and execution model of the Web client.

- **Single Page Application (SPA):** Static HTML/JS/CSS served to the browser; rendering and routing happen entirely client-side.
- **Server-Side Rendering (SSR):** Pages are rendered dynamically on a Node.js server per request (e.g., Next.js).
- **Static Site Generation (SSG):** Pages are rendered at build time into static files.
- **Hybrid:** Combining SSR/SSG/SPA depending on the route.

**Decision:** **Single Page Application (SPA)**. AnverraGlobal is an authenticated, dynamic business application. The established architecture dictates that the client is a decoupled consumer of the Modular Monolith's REST APIs. SSR introduces an unnecessary intermediate server runtime, complicating deployment and blurring the backend boundary without sufficient business justification (such as consumer SEO). An SPA fulfills the current architectural and business requirements without introducing runtime complexity.

# 9. API Integration
**Strategy:** Clients MUST strictly consume the established D03 REST/JSON/OpenAPI boundary.
**Evaluation:** API clients and TypeScript interfaces should be generated directly from the backend OpenAPI 3.0 specification. This guarantees that the client strictly adheres to the backend's typed DTOs and prevents manual drift. Manual duplication of DTOs is rejected to maintain contract integrity.

# 10. Authentication Boundary
**Strategy:** The Identity module (Phase 2) remains the sole authoritative source for authentication.
**Constraint:** The client technology is responsible only for the technical handling of credentials (e.g., storing tokens securely, attaching them to HTTP requests, managing local session state) as prescribed by the final security implementation. It MUST NOT invent authentication workflows or validation rules.

# 11. Authorization Boundary
**Strategy:** The backend is the sole authority for authorization.
**Constraint:** Client-side route guards, conditional rendering, and UI state are UX mechanisms only. The client is fundamentally untrusted. Sensitive business authorization MUST rely entirely on the backend API rejecting unauthorized requests. The client does not evaluate RBAC, roles, or permissions independently of the backend representation.

# 12. State Management
Client state must be segregated by responsibility to prevent recreating backend business truth.

- **Server/API State:** The established D05 architectural decision is that client server/API state must remain conceptually separate from transient local UI state. A dedicated server-state/cache solution is architecturally justified because it provides synchronization, caching, loading/error management, and avoids recreating backend truth. The client does not become an authoritative business-state store. The exact server-state library selection (e.g., TanStack Query) is deferred to implementation tooling selection.
- **Local UI State:** Transient interaction state (e.g., dropdown toggles, form input). Native React state mechanisms (Context, Hooks) are entirely sufficient.
- **Global Application State:** Widespread state accessed across disconnected trees.

**Decision:** Architecturally separate server/API state from local UI state. Rely on native React mechanisms for local UI state. A dedicated global state library (e.g., Zustand, Redux) is deferred and should only be introduced during implementation if actual UI complexity necessitates it.

# 13. Client Data Ownership
**Constraint:** The client owns **zero authoritative business data.**
All client state is presentation state, temporary interaction state, or a cached representation of authoritative backend data. The backend remains the undisputed source of truth.

# 14. Shared Code Strategy
**Evaluation:** Sharing code between Web and Mobile clients.

- **Potentially Shareable:** Generated OpenAPI TypeScript interfaces, generated API client functions, and purely technical formatting utilities.
- **MUST NOT Be Shared:** Backend domain models, entities, aggregates, business rules, application services, or persistence models.

**Decision:** Sharing generated API types and clients is architecturally sound and encouraged to ensure consistency. However, sharing must remain restricted to these narrow technical bounds. If sharing tooling introduces unnecessary coupling between Web and Mobile repositories, the clients should remain independently bounded.

# 15. UI / Component Strategy
**Decision:** **Deferred.**
The specific UI component library, design system, or CSS framework (e.g., Tailwind, Material UI, custom CSS) is deferred to the design and implementation phase. UI library selection is a presentation detail and must not drive business architecture.

# 16. Testing Strategy
The objective is minimum sufficient testing infrastructure to ensure predictable UI validation.

- **Unit/Component Testing:** Validation of isolated UI components.
- **Integration Testing:** Validation of component interactions and mocked API consumption.
- **Browser/E2E Testing:** Validation of the application in a real browser.
- **Mobile Testing:** Validation of native interactions.

**Decision:** Exact testing frameworks (e.g., Vitest, Jest, React Testing Library, Playwright, Cypress) are deferred to implementation tooling selection. The architectural requirement is that client testing must focus on UI behavior and accurate API consumption, not business logic validation, which belongs to the backend.

# 17. Build Tooling
**Evaluation:** Web (Vite vs Webpack) and Mobile (Expo/Metro).
**Decision:** Exact build tooling is deferred to the implementation phase, provided it aligns with the framework selection (React/React Native). The tooling must not introduce unnecessary backend runtimes or alter the SPA rendering model constraint.

# 18. Package Management
**Evaluation:** npm, yarn, pnpm.
**Decision:** Deferred. The selection of a specific package manager (e.g., npm or pnpm) is deferred to implementation tooling configuration, provided it enforces deterministic lockfiles.

# 19. Type Safety
**Decision:** Strict TypeScript combined with OpenAPI generation.
**Rationale:** The API boundary is the most critical risk area. Strict type safety, enforced by generating TypeScript interfaces directly from the backend's OpenAPI contract, ensures the client cannot drift from the backend's authoritative DTOs.

# 20. Accessibility
**Strategy:** The client technologies must possess the capability to output WCAG-compliant accessible applications. Accessibility compliance remains a design and implementation requirement. The selected React/React Native frameworks possess the necessary primitives to support this without imposing architectural limitations.

# 21. Performance
**Strategy:** The architecture does not prescribe application-specific performance targets (e.g., bundle-size thresholds) at this stage. Performance characteristics such as rendering speed, bundle optimization, and caching efficiency must be considered during implementation design. Code splitting and lazy loading are implementation optimizations, not mandatory architecture.

# 22. Maintainability
**Strategy:** Client technology must be selected from ecosystems with massive, stable backing and a clear, predictable upgrade path to ensure long-term viability. Niche frameworks are rejected to mitigate the risk of abandonment.

# 23. Developer Experience
**Strategy:** The client technology must provide immediate, strongly-typed feedback loops, robust developer tooling, and predictable workflows to maximize developer productivity while enforcing the architectural boundaries.

# 24. AI Development Safety
**Strategy:** The selected technology stack must optimize for AI-assisted development by exhibiting:
- predictable project structure
- strong typing (TypeScript)
- explicit conventions
- ease of validating AI-generated code against OpenAPI contracts
- clear boundaries that prevent AI from hallucinating backend logic into the frontend

# 25. Security Boundary
**Strategy:** Clients are completely untrusted.
- Secrets (API keys, passwords, private keys) MUST NOT be embedded in client bundles.
- Token/session handling is a technical client concern only.
- Authentication implementation follows the approved Identity/security architecture.
- Client-side route guards are not security controls.

# 26. Backend Boundary Preservation
**Strategy:** The client must NEVER bypass the D03 API boundary. It must not connect to backend persistence directly, attempt to evaluate internal backend logic, or duplicate backend business rules unnecessarily.

# 27. Async Messaging Boundary
**Strategy:** Client applications do not directly participate in backend internal asynchronous messaging. D04 establishes Spring Modulith internal events; the client is entirely isolated from this. The client must not connect directly to the internal event infrastructure. If real-time client notifications are required, they remain a deferred API/infrastructure concern to be architected securely at the network edge.

# 28. External Integration
**Strategy:** External third-party integrations should route through the backend Modular Monolith, which handles authoritative business logic and secrets. Direct client-to-third-party integrations (e.g., secure payment tokenization) must be explicitly authorized during implementation design.

# 29. Technology Evaluation

### Web Technology Candidates

| Criterion                 | React SPA | Next.js (SSR) | Vue SPA  | Angular SPA |
| ------------------------- | --------- | ------------- | -------- | ----------- |
| Existing Arch. Fit        | Excellent | Moderate      | Good     | Good        |
| Rendering Model           | SPA       | SSR/Hybrid    | SPA      | SPA         |
| Runtime Complexity        | Minimal   | High (Server) | Minimal  | Minimal     |
| TypeScript Integration    | Excellent | Excellent     | Excellent| Excellent   |
| API Contract Tooling      | Excellent | Excellent     | Good     | Excellent   |
| Mobile Alignment          | Excellent | Moderate      | Poor     | Poor        |
| AI Development Safety     | Strong    | Good          | Good     | Moderate    |

**Web Conclusion:** React + TypeScript (SPA) is selected. It avoids the unnecessary server runtime complexity of Next.js, fulfills the strict API-consumer architectural requirement, provides strong AI-development safety due to its predictable component model, explicit OpenAPI contracts, and mature tooling, and aligns perfectly with React Native for the mobile boundary.

### Mobile Technology Candidates

| Criterion                 | React Native + Expo | Flutter       | Native Android/iOS |
| ------------------------- | ------------------- | ------------- | ------------------ |
| Architectural Fit         | Excellent           | Good          | Good               |
| Cross-Platform Capability | Excellent           | Excellent     | None (Two codes)   |
| Language/Type Safety      | TypeScript (Shared) | Dart (Strict) | Kotlin/Swift       |
| API/OpenAPI Integration   | Excellent           | Good          | Good               |
| Native Capability Access  | Good                | Good          | Excellent          |
| Performance               | Good                | Very Good     | Excellent          |
| Maintainability           | High                | High          | Low (Dual code)    |
| Developer Experience      | Excellent           | Excellent     | Good               |
| AI Development Safety     | Strong              | Good          | Moderate           |
| Web Arch. Alignment       | Excellent           | Poor          | Poor               |

**Mobile Conclusion:** React Native + Expo is selected. The architectural benefit of unifying Web and Mobile around a single language (TypeScript), a shared API consumption strategy (generated OpenAPI clients), and a shared conceptual component model (React) drastically reduces long-term project complexity compared to maintaining separate Dart or Native ecosystems, without compromising on standard business application performance.

# 30. Recommended Stack
- **Web Framework:** React (Single Page Application)
- **Mobile Framework:** React Native + Expo
- **Language:** TypeScript
- **API Client:** Generated from OpenAPI 3.0
- **State (Remote):** Architecturally separated; exact library (e.g., TanStack Query) deferred.
- **State (Local):** React Native primitives
- **Rendering:** SPA (No intermediate server runtime)

# 31. Alternatives Rejected
- **Next.js (SSR):** Rejected. While viable, AnverraGlobal is an authenticated business application that does not require consumer SEO. The SSR architecture introduces an intermediate server runtime that complicates deployment and blurs the strict REST API consumer boundary without sufficient business justification.
- **Angular:** Rejected. Angular provides a highly opinionated framework that introduces a steeper learning curve and offers significantly less architectural synergy with the selected Mobile strategy (React Native). Furthermore, its development model and tooling ecosystem diverge from the unified React/TypeScript model that provides optimal maintainability across Web and Mobile.
- **Vue:** Rejected. While technically viable and approachable, Vue offers weaker cross-client consistency (no direct equivalent to React Native's architectural synergy) and its ecosystem maturity for highly typed, generated OpenAPI client integration is less established compared to the React ecosystem, making it slightly less optimal for strict AI-assisted development.
- **Flutter:** Rejected. While providing excellent performance and custom rendering, React Native was preferred because it aligns the Web and Mobile ecosystems around a single language (TypeScript) and a shared conceptual component model (React), significantly reducing architectural complexity.
- **Native Android/iOS:** Rejected. Requires maintaining two completely separate codebases, teams, and CI/CD pipelines, which is unnecessary overhead for a standard business application without extreme hardware integration needs.

# 32. Deferred Decisions
- Specific UI component libraries, design systems, and CSS frameworks.
- Specific endpoint URLs, payloads, HTTP methods, and DTO definitions.
- Specific authentication workflows and secure token storage mechanics.
- Specific package managers (npm vs pnpm) and testing frameworks.
- Exact server-state caching library (e.g., TanStack Query).
- Agent, Sub-Agent, Dealer, Partner, Organization, Proposal, Document, KYC, and Administration domains remain unresolved and unmodeled.
- Real-time client updates (WebSockets/SSE).

# 33. AI Implementation Guide
Future coding AI MUST:
- Use only the client technology stack after this blueprint is formally baselined.
- Consume generated OpenAPI TypeScript interfaces; do not invent API payload types manually.
- Treat client-side state as transient or cached; never recreate backend business truth.
- Implement route guards strictly for UX, recognizing the backend enforces true authorization.
- Never write direct database queries or internal messaging consumers in the client.

# 34. Traceability
This document traces directly to:
- AEOS-P03-D00, D01, D02, D03, D04
- Phase 1 Engineering Constitution
- AEOS-P02-S01-D01 through D05
- AEOS-P02-S02-D00 through D07

# 35. Definition of Done
The blueprint is complete when:
- [x] Web and Mobile technologies were objectively evaluated.
- [x] Language, State, and API integration strategies were defined.
- [x] Rendering model (SPA) was explicitly evaluated and justified over SSR/SSG.
- [x] Client security, data ownership, and backend boundaries were rigidly defined.
- [x] D03 (REST/OpenAPI) and D04 (No internal messaging) boundaries were respected.
- [x] No backend domain concepts or API payloads were invented.
- [x] No unresolved capabilities (Agent/Partner) were modeled.
- [x] AI constraints and deferred decisions are explicitly documented.
- [x] Traceability is maintained.

# 36. Final Decision Status
**Proposed** (Pending project-level baseline approval).
