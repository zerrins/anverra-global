# Application Boundaries

**Document ID:** AEOS-P02-S01-D03  
**Version:** 1.0  
**Status:** Baseline  
**Phase:** 2 — Application & Repository Blueprint  
**Stage:** 1 — System & Repository Blueprint  
**System:** AnverraGlobal

---

# 1. Purpose

This document defines the application-level boundaries for the AnverraGlobal system. 

Where Document 01 established *what* AnverraGlobal is, and Document 02 established *where* components live in the repository, this document answers:

> **What does each application surface own, what may it depend on, what must it not own, and how do applications interact?**

This document establishes the boundaries between the primary runtime applications. It does not define detailed APIs, specific technologies, deployment architecture, or module-internal hexagonal architectures.

---

# 2. Application Landscape

The AnverraGlobal system contains three distinct primary application surfaces, supported by non-runtime engineering concerns.

## 2.1 Backend Application
The core of the AnverraGlobal system. It is implemented as a Modular Monolith, composed of independent business-capability modules.

## 2.2 Web Application
A client application surface providing a browser-based user interface to interact with the system.

## 2.3 Mobile Application
A client application surface providing a mobile interface, tailored for field users or specific mobile use cases.

## 2.4 Supporting Concerns
The repository also contains supporting engineering concerns, such as:
- Infrastructure definitions (`infrastructure/`)
- AI Engineering assets (`.ai/`)
- System Documentation (`docs/`)
- Architecture models (`architecture/`)
- Automation scripts and engineering tools (`scripts/`, `tools/`)

Supporting concerns are not applications. They do not execute business logic and do not own business capabilities.

---

# 3. Application Ownership

Application ownership defines the high-level responsibilities assigned to each application surface.

## 3.1 Backend Ownership
The Backend Application is the authoritative owner of the system's business capabilities. It owns the business rules, validation, persistence, and external system integrations that fulfill the Product Vision.

## 3.2 Web Ownership
The Web Application owns the browser-based presentation and interaction experience. It is responsible for client-side state, navigation, and client-appropriate validation required for a high-quality user experience.

## 3.3 Mobile Ownership
The Mobile Application owns the mobile-device presentation and interaction experience. It is responsible for mobile-specific navigation, device interactions, offline UI state (where applicable), and client-appropriate validation.

---

# 4. Backend as the Authoritative Business Boundary

The Backend Application is the sole authoritative source of truth for AnverraGlobal business operations.

It is strictly responsible for:
- Authoritative business rules and logic
- Business workflows and aggregate lifecycles
- Authoritative business validation
- Authoritative business data and persistence
- The integration boundary to external business systems (e.g., insurers, payment providers)

The Backend acts as the ultimate guarantor of business invariants. No client application may bypass the Backend to manipulate business data or execute authoritative business workflows.

---

# 5. Client Application Boundaries

The Web and Mobile applications are **client application surfaces**. They are consumers of the business capabilities provided by the Backend.

## 5.1 Web → Backend
The Web Application consumes explicit backend contracts to present data and capture user intent. It displays backend-authoritative results. It does not implement authoritative business workflows or calculate authoritative business outcomes independently.

## 5.2 Mobile → Backend
The Mobile Application consumes explicit backend contracts to present data and capture user intent on mobile devices. While it may utilize device-specific APIs (e.g., camera, local storage) to fulfill its client responsibilities, it relies entirely on the Backend for authoritative business execution.

## 5.3 Client-Side Logic vs Authoritative Business Logic
Client applications (Web/Mobile) **may** contain:
- Presentation and formatting logic
- Interaction and navigation logic
- Client-side state and local models
- Client-appropriate validation (e.g., required fields, format checks) to improve user experience

Client applications **must not** contain:
- Authoritative business validation (e.g., complex underwriting rules, authoritative commission calculations)
- Authoritative state mutation logic that bypasses backend rules

The Backend must still enforce all authoritative validation, regardless of any validation performed by the client.

---

# 6. Data Ownership

**Authoritative business data is owned exclusively by the Backend Application**, and specifically by the appropriate business module within the Backend.

- Web and Mobile do not own authoritative backend business data.
- Web and Mobile may not directly access the backend database.
- Web and Mobile may not directly modify backend persistence mechanisms.

Client applications may maintain local UI state, cached data representations, or device-local data required for their operation, but this data is a localized projection and is never the authoritative system of record.

---

# 7. Business Logic Ownership

Business logic ownership is categorized across the application boundaries as follows:

1. **Authoritative Business Logic** → **Backend**: Determines the success, failure, and state transition of any business operation. Enforces business invariants.
2. **Presentation/Interaction Logic** → **Web/Mobile**: Determines how data is displayed, how the user navigates, and how input is collected.
3. **Client-Appropriate Validation** → **Web/Mobile**: Catches obvious input problems early to provide immediate UX feedback.

---

# 8. Authentication and Authorization Boundary

The boundary for security concerns separates interaction from authority:

- **Clients (Web/Mobile)**: Participate in authentication flows and manage session or token state as required by the chosen security architecture. They may use this state to drive UI behavior (e.g., hiding a button if a user lacks a role).
- **Backend**: Remains strictly authoritative for identity, authorization, and access decisions. The Backend never trusts the client's authorization assertions; it independently validates permissions for every business operation.

The specific implementation (JWT, OAuth, OIDC, session cookies) is deferred to later architecture decisions.

---

# 9. Contract Boundaries

Communication between application surfaces occurs exclusively through explicit contracts.

- **Web ↔ Backend**
- **Mobile ↔ Backend**

Clients consume defined backend contracts. They do not depend on backend implementation details, backend database schemas, or internal backend entities. 

The specific contract formats, protocols (e.g., REST, GraphQL, gRPC), request/response DTO structures, and versioning strategies are deferred to later technology decisions.

---

# 10. External Integration Boundary

The **Backend Application** is the primary system integration boundary for external business systems.

Integrations with external entities established in the System Blueprint—such as Insurance Providers, Payment Systems, Communication Systems, and Document Services—are owned by the appropriate backend business capability. 

The Web and Mobile applications consume the results of these integrations strictly through backend contracts. Clients do not integrate directly with external business systems unless a specific, later architectural decision explicitly mandates a client-side platform integration (e.g., a direct client-side payment tokenization flow).

---

# 11. Dependency Direction

Application dependency strictly flows downward from the client to the Backend contracts.

- **Allowed**: Web and Mobile depend on Backend contracts.
- **Prohibited**: Web and Mobile importing backend implementation code.
- **Prohibited**: Backend importing Web or Mobile implementation code.
- **Prohibited**: Backend depending on a specific client's internal behavior.

---

# 12. Shared Code and Shared Contracts

Shared code across application boundaries must be handled carefully to preserve the separation of concerns:

- **Shared Technical Utilities**: Permitted (e.g., generic formatting libraries), though specific package mechanics are deferred.
- **Shared Contracts**: Permitted (e.g., shared interface definitions or DTO schemas), pending technology decisions.
- **Shared Business Logic**: **Prohibited**. Shared business ownership must remain within the owning backend capability. There shall be no "shared business" library distributed across Web, Mobile, and Backend.

---

# 13. Application vs Module Boundaries

It is critical to distinguish between Application Boundaries and Backend Module Boundaries.

**Application Boundaries** answer: *"Which application surface owns this responsibility?"*
- Surfaces: Web, Mobile, Backend.

**Module Boundaries** answer: *"Which backend business capability owns this responsibility?"*
- Capabilities: Identity, Customer, Product, Policy, Commission, Notification, Reporting, etc.

Web and Mobile are distinct applications; they are not backend modules. Supporting concerns (Infrastructure, AI Engineering) are engineering domains; they are not business capability owners.

---

# 14. Boundary Violations and Anti-Patterns

The following are architectural anti-patterns that violate the boundaries established in this document:

- **Frontend implementing authoritative business rules**: e.g., the Web app calculating complex insurance premium discounts independently of the backend.
- **Mobile directly updating data**: e.g., the Mobile app connecting directly to the PostgreSQL database to update a policy status.
- **Direct external business calls from clients**: e.g., the Mobile app calling an insurer's API directly to issue a policy instead of routing through the Backend.
- **Backend dependency on clients**: e.g., the Backend importing UI components or relying on the Web app to perform data sanitization.
- **Duplicate authorization**: e.g., trusting the client to enforce security policy without the Backend independently verifying it.
- **Shared logic dumping ground**: e.g., creating a cross-application shared library that calculates agent commissions.

---

# 15. Deferred Decisions

The following decisions are explicitly deferred and are not resolved by this document:

- Exact API technology (e.g., REST, GraphQL, gRPC)
- API contract format and DTO structures
- Database schemas and ORM strategies
- Frontend framework (e.g., React, Vue, Angular)
- Mobile framework (e.g., Native, React Native, Flutter)
- Backend language and framework implementation
- Authentication protocol (e.g., OAuth2, OIDC) and token storage
- Shared contract package mechanics and monorepo tooling
- Detailed module-internal architecture and hexagonal layers
- Infrastructure implementation and deployment topology
- CI/CD pipeline design

These will be resolved in subsequent Architectural Boundary documents or Phase 3 Technology decisions.

---

# 16. Definition of Done

This Application Boundaries document is considered complete and baseline when:

- [x] The Backend is established as the authoritative business capability owner.
- [x] Web and Mobile are established as client application surfaces.
- [x] Web and Mobile are clearly distinguished from backend modules.
- [x] The Backend is defined as the owner of authoritative business data and business rules.
- [x] Clients are permitted presentation, interaction, local state, and UX validation logic without assuming business authority.
- [x] Clients are explicitly prohibited from bypassing backend business contracts or accessing backend databases.
- [x] External business integrations are positioned behind the Backend boundary.
- [x] Supporting engineering concerns are separated from business capability ownership.
- [x] Application Boundaries are clearly differentiated from Module Boundaries.
- [x] Dependency direction is explicitly downward (Clients → Backend Contracts).
- [x] Anti-patterns are documented conceptually.
- [x] No technology decisions, API designs, deployment architectures, or detailed module architectures have been invented.
- [x] Open decisions are explicitly preserved and deferred.
- [x] The document does not contradict the System Blueprint (Doc 01) or Repository Architecture (Doc 02).

---

*This document is the authoritative application boundary definition for AnverraGlobal. Detailed internal module structures and architectural layers are addressed in companion Stage 1 documents.*
