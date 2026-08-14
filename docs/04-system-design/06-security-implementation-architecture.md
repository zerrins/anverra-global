# Security Implementation Architecture

**Document ID:** AEOS-P04-D06  
**Version:** 1.0  
**Status:** Baseline Candidate  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Authoring Position:** 7  
**Depends on:** Phase 1 Engineering Constitution · AEOS-P02-S01-D01 through D05 · AEOS-P02-S02-D00 through D07 · AEOS-P03-D00 through D05 · AEOS-P04-D00 · AEOS-P04-D01 · AEOS-P04-D02 · AEOS-P04-D03 · AEOS-P04-D04 · AEOS-P04-D05

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D06 |
| **Title** | Security Implementation Architecture |
| **Version** | 1.0 |
| **Status** | Baseline Candidate |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Authoring Position** | 7 |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |
| **Immediately Preceding Document** | AEOS-P04-D05 — Event & Asynchronous Implementation Architecture |

---

# 2. Purpose

This document establishes the authoritative security implementation architecture for the AnverraGlobal Modular Monolith. 

It translates the established security responsibilities into technical mechanisms without redefining business authorization rules, module ownership, API endpoints, or database schemas. 

Specifically, this document:
1. Formally evaluates and resolves Open Decisions **O11** (Authentication Architecture), **O12** (Authorization Enforcement Model), **O13** (Client Credential Storage), **O14** (CSRF Strategy), **O15** (Secret Management), and **O16** (Asynchronous Security Context Propagation).
2. Defines the Spring Security integration architecture.
3. Establishes the boundary between technical security enforcement and business domain rules.
4. Mandates how security context propagates across synchronous and asynchronous boundaries.
5. Defines security guardrails for AI-assisted development.

**Core Principle:** Security controls protect the architectural boundaries; they do not become the owner of business rules.

---

# 3. Scope

## 3.1 In Scope
- Authentication implementation architecture (O11).
- Authentication protocol/mechanism.
- Credential/token processing architecture.
- Spring Security integration.
- Security filter architecture.
- Security context propagation.
- API authentication boundary.
- Authorization enforcement mechanism (O12).
- Permission evaluation architecture.
- Method/service-level security principles.
- Security exception handling.
- Password/credential handling architecture where applicable.
- Secret/configuration handling principles (O15).
- Security-related persistence integration at the architectural level.
- CORS/security-header integration boundaries.
- CSRF strategy based on the selected authentication model (O14).
- Security logging/audit requirements.
- Authentication/authorization testing architecture.
- Security architecture for asynchronous processing.
- Security implications of external broker integration.
- AI-development security guardrails.
- Client credential storage principles (O13).

## 3.2 Explicitly Out of Scope
- Specific business roles unless already formally established.
- Specific permissions.
- Specific users.
- Business authorization workflows (e.g., how a user requests access).
- Specific API endpoints.
- Specific DTOs.
- Client framework UI implementation.
- Database schemas.
- Event payloads.
- Agent/Sub-Agent business models.
- Notification business rules.
- Infrastructure topology beyond what is required to establish security boundaries.

---

# 4. Architectural Security Principles

1. **Security Protects Boundaries, Not Business Logic:** Spring Security protects the API and application boundaries. Business rules belong in the domain.
2. **Defense in Depth:** Security is enforced at multiple layers (API edge, application service, persistence isolation).
3. **Least Privilege:** Systems, modules, and users operate with the minimum permissions necessary.
4. **Secure Defaults:** Security mechanisms (e.g., CSRF, secure headers) are active by default and require explicit justification to disable.
5. **Fail Closed:** Security Failure Behavior: Authentication and authorization failures must fail closed. Security infrastructure failures must not silently grant access or bypass authorization. Any fallback/degraded behavior must be explicitly architected and must preserve the system's security boundary.
6. **No Security in the Domain:** The core business domain must remain framework-agnostic, including unawareness of Spring Security classes.

---

# 5. Security Boundary Model

The target architecture conceptually isolates security from the domain:

```text
Security Boundary
      │
      ▼
Authentication
      │
      ▼
Security Context
      │
      ▼
API / Application Authorization Enforcement
      │
      ▼
Application / Domain Rules where business context is required
```

Authentication establishes *who* or *what* is acting; authorization determines whether that actor may perform the requested operation.

**Internal Module Trust Boundary:** In-process module collaboration does not require network authentication. Independently deployed external consumers or broker-connected components constitute separate trust boundaries and require explicit authentication and authorization.

---

# 6. Authentication Architecture

Open Decision **O11** requires resolving the fundamental mechanism for establishing identity across the API boundary.

## 6.1 Authentication Technology Evaluation
- **OAuth 2.0 / OpenID Connect:** Standard for external identity federation, but can be complex for a self-contained monolith's primary internal API if no external IdP is initially used.
- **JWT-based Token Authentication:** Stateless, highly scalable, supports Web and Mobile clients through appropriate credential transport mechanisms, avoids server-side session replication, but requires careful token revocation/expiry management.
- **Session-based Authentication:** Native to web browsers, implicitly handles CSRF (with tokens) and revocation, but scales poorly across stateless APIs and complicates Mobile client integration.
- **Hybrid Approach (BFF):** Web client uses Session/Cookies via a Backend-For-Frontend; Mobile uses JWT. Adds significant architectural complexity to a monolithic API layer.

## 6.2 Formal Resolution of O11
> [!IMPORTANT]
> **OPEN DECISION O11 IS FORMALLY RESOLVED:**
> **JWT-based Token Authentication** is established as the formal API authentication architecture.

The backend uses stateless JWT-based authentication; authentication state is represented by signed JWT credentials rather than server-side authentication sessions. Browser clients transport these credentials using Secure HttpOnly cookies, while mobile clients use the approved Authorization-header mechanism.

```text
                 Authentication Credential
                           │
                ┌──────────┴──────────┐
                │                     │
              Web                  Mobile
                │                     │
       Secure HttpOnly Cookie    Authorization Header
                │                     │
                └──────────┬──────────┘
                           ▼
                    Spring Security
                           │
                           ▼
                    SecurityContext
```

The backend security boundary will validate the token through the configured Spring Security authentication mechanism and establish the `SecurityContext` for the request.

---

# 7. Identity Module Integration

The `identity` business module owns the business rules for users, registration, and credentials. Identity remains responsible for authentication and RBAC.

**Phase 5 Exception / Clarification:**
As established in AEOS-P04-D16, the Identity module MUST NOT encode or own the authoritative organizational hierarchy (Dealer/Branch/Agent/Data Entry relationships). A dedicated Organization/Hierarchy capability owns these relationships, and Identity remains strictly restricted to identity, authentication, and RBAC.

**Architectural Boundary:**
- D06 establishes that Spring Security intercepts HTTP requests, extracts the JWT, and sets the authenticated principal.
- D06 does NOT invent the user registration workflow, password reset logic, or identity verification process. Those belong strictly to the `identity` module's application and domain layers.
- If Spring Security requires loading user details, it must do so via a formally declared internal contract (e.g., `UserDetailsServiceAdapter` calling the `identity` module's `contracts/` surface).

---

# 8. Spring Security Integration

The Spring Security integration is centralized in the `platform/` infrastructure package to prevent security configuration from fracturing across business modules.

The flow is strictly governed:
1. **HTTP Request** arrives.
2. **SecurityFilterChain** intercepts the request.
3. **The configured Spring Security JWT authentication mechanism** validates the token.
4. **SecurityContextHolder** is populated with an abstract `Authentication` object.
5. **Controller** processes the request without directly interacting with HTTP headers.
6. **Application Service** enforces authorization via method security.

---

# 9. Security Context Architecture

To maintain Domain-Driven Design purity (AEC-ARC-002), the domain layer must not depend on Spring Security.

- **Prohibited:** Passing `HttpServletRequest`, `Authentication`, or `Jwt` objects into the domain.
- **Mandated:** The inbound adapter (controller) or application service extracts necessary identity claims (e.g., authenticated subject identifier or other formally established security attributes) from the `SecurityContext` and maps them into domain-native abstractions (e.g., an `Actor` or `UserId` value object) before invoking the domain.

---

# 10. Authorization Architecture

Open Decision **O12** requires resolving the authorization enforcement model.

## 10.1 Evaluation
- **URL-level (SecurityFilterChain):** Enforces baseline authentication for protected API operations, with only formally designated public authentication operations exempted. Easy to configure but lacks business context.
- **Method-level (`@PreAuthorize`):** Fine-grained. Applied at the Application Service layer. Can evaluate method arguments.
- **Application-level (Business Logic):** Context-dependent authorization may require application-layer policy evaluation and, where authorization is inseparable from a domain invariant, enforcement through domain rules. Spring Security framework types must not enter the domain.

## 10.2 Formal Resolution of O12
> [!IMPORTANT]
> **OPEN DECISION O12 IS FORMALLY RESOLVED:**
> A **Combined Model (Method-Level primary)** is established.

- **URL-Level:** Enforces baseline authentication (protected API operations require authentication, with only formally designated public authentication operations exempted).
- **Method-Level (Default):** Method-level authorization is the primary technical enforcement mechanism at appropriate application-service boundaries. Spring Security method-security facilities such as `@PreAuthorize` may be used where appropriate to enforce formally established authorization policies and constraints.
- **Application-Level:** Context-dependent authorization remains in the application layer and, where inseparable from domain invariants, enforced through domain rules without framework coupling.

---

# 11. Authorization Policy Boundary

D06 defines *how* authorization is enforced, not *what* the rules are.

- D06 mandates the use of method security.
- D06 **PROHIBITS** inventing roles (e.g., `ROLE_AGENT`) or permissions (e.g., `POLICY_WRITE`) in the security architecture. The actual authorization matrix is defined by the business blueprints and implemented progressively by the modules.

---

# 12. Token / Credential Lifecycle

The architecture mandates distinct lifecycle phases for JWTs:
- **Issuance:** Issued through the `identity` module's formally established authentication mechanism.
- **Expiry:** Access tokens must have a bounded and appropriately short lifetime determined through approved security policy and implementation configuration.
- **Refresh:** A secure refresh-token mechanism must be implemented to obtain new JWTs without requiring re-authentication, subject to revocation checks.
- **Revocation:** Refresh tokens must be revocable via the database. JWTs are practically immutable until expiry, enforcing the need for short lifespans.

---

# 13. Client Credential Storage Boundary

Open Decision **O13** requires defining how sensitive tokens are stored by clients.

## 13.1 Formal Resolution of O13
> [!IMPORTANT]
> **OPEN DECISION O13 IS FORMALLY RESOLVED:**
> Client-specific secure storage is established.

- **Web Applications (Browsers):** Web authentication cookies MUST be Secure and HttpOnly. An explicit SameSite policy must be selected based on the final deployment topology and cross-origin requirements; insecure or unnecessarily permissive cookie settings are prohibited. Storing sensitive tokens in `localStorage` or `sessionStorage` is strictly prohibited due to XSS vulnerability.
- **Mobile Applications:** JWTs and refresh credentials MUST be stored using OS-provided secure credential storage appropriate to the selected mobile technology (e.g., iOS Keychain, Android Keystore).

---

# 14. CSRF Architecture

Open Decision **O14** requires resolving the Cross-Site Request Forgery (CSRF) strategy based on the authentication model.

## 14.1 Formal Resolution of O14
> [!IMPORTANT]
> **OPEN DECISION O14 IS FORMALLY RESOLVED:**
> **CSRF Protection is Mandatory for Web Clients.**

Because O13 mandates HttpOnly cookies for Web clients, the API is vulnerable to CSRF.
Spring Security's CSRF protection must be **ENABLED**. 
- The Web client authentication model requires a CSRF token mechanism in addition to the HttpOnly authentication cookie. The selected Spring Security CSRF mechanism must require the client to provide the CSRF token through the approved request-header mechanism for state-changing operations.
- Mobile clients use Authorization-header credential transport; CSRF is not applicable to that credential transport.

---

# 15. CORS & Browser Security Boundary

- **CORS:** Cross-Origin Resource Sharing must be configured at the Spring Security level. It must explicitly allow the specific origins of the deployed Web applications. Wildcard (`*`) origins for credentialed requests are strictly prohibited.
- **Security Headers:** Spring Security must enforce strict HTTP response headers (e.g., `Strict-Transport-Security`, `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, `Content-Security-Policy`).

---

# 16. Secrets & Configuration Security

Open Decision **O15** requires resolving the Secret Management Architecture.

## 16.1 Formal Resolution of O15
> [!IMPORTANT]
> **OPEN DECISION O15 IS FORMALLY RESOLVED:**
> **Runtime Secret Injection through an approved deployment secret-management mechanism** is established as the architectural secret mechanism.

- Secrets (DB passwords, JWT signing keys, API keys) must **NEVER** be committed to source control (Git).
- Secrets must not be hardcoded in application code or bundled in frontend assets.
- The architecture requires secrets to be supplied through the runtime configuration boundary from the approved deployment secret provider.

---

# 17. Credential / Password Security

Local credential handling is applicable only if the selected authentication architecture includes application-managed credentials. When the `identity` module processes local passwords:
- Passwords must be hashed using a strong, adaptive algorithm (e.g., **Argon2** or **Bcrypt**).
- Passwords must **never** be stored in reversible encrypted form or plain text.
- Password hashing must be exposed to the `identity` module through an application-level abstraction/port. The concrete password-hashing implementation and framework integration belong to the `platform/` infrastructure layer.

---

# 18. Security Error Handling

Security exceptions (e.g., `AuthenticationException`, `AccessDeniedException`) must be intercepted at the API boundary.
- They must be translated into standardized HTTP responses (e.g., `401 Unauthorized`, `403 Forbidden`) conforming to the API error architecture defined in D04.
- Security error responses must **never** leak internal system details, stack traces, or precise reasons for authentication failure (e.g., do not differentiate between "user not found" and "invalid password").

---

# 19. Security Logging & Audit

The architecture requires auditability for:
- Authentication successes and failures.
- Authorization failures (Access Denied).
- Critical security configuration changes.
- Credential lifecycle security events.
- Security-sensitive administrative actions where formally established.
- Security policy violations and suspicious authentication patterns.

*Architectural Boundary:* D06 establishes *what* must be auditable. It does not invent the audit database schema. The application must produce structured logs for these events.

---

# 20. Sensitive Data Protection

- **Logs and Traces:** Passwords, full JWT strings, and PII must be masked or excluded from application logs and distributed traces.
- **API Responses:** Domain entities must be carefully mapped to DTOs to ensure sensitive internal fields (e.g., password hashes, internal system IDs) are not accidentally leaked to external clients.

---

# 21. Asynchronous Security Context

Open Decision **O16** requires resolving the security context propagation across asynchronous boundaries.

## 21.1 Formal Resolution of O16
> [!IMPORTANT]
> **OPEN DECISION O16 IS FORMALLY RESOLVED:**
> **No raw authentication/security context propagation across durable async events.**

- Raw JWTs, passwords, HTTP request objects, and SecurityContext objects MUST NOT be serialized into durable events.
- Only the minimum trusted metadata required for processing may cross the boundary.
- Correlation and trace metadata may cross the boundary according to the approved observability architecture.
- Asynchronous consumers must not treat actor metadata alone as proof of authorization. Event provenance must originate from the trusted application event-publication mechanism, and consumers must apply the appropriate authorization/business policy for the operation being performed.

---

# 22. Broker Security Requirements

If an external broker (Kafka/RabbitMQ) is introduced per D05's O10 resolution:
- **Authentication:** The monolith must authenticate with the broker using secret-managed credentials.
- **Transport:** All broker traffic must traverse TLS.
- **Isolation:** Producer and consumer privileges must be restricted to explicitly authorized topics/queues.

---

# 23. Database Security Boundary

D06 integrates with D03 (Persistence) by establishing:
- Application database connections must use least-privilege service accounts (e.g., restricted from running DDL statements outside of Flyway migrations).
- Database credentials must be managed via the O15 Secret Management Architecture.

---

# 24. Dependency & Supply-Chain Security

- All third-party dependencies must be subject to approved automated dependency vulnerability scanning.
- Security-critical dependencies (e.g., cryptographic libraries, JWT parsers) must be explicitly approved and centrally managed in the project's Maven POM.

---

# 25. Security Testing Architecture

Security must be tested at multiple levels:
1. **Unit Tests:** Verify method-level authorization policies using approved security testing mechanisms.
2. **Integration Tests:** Verify the SecurityFilterChain, CSRF, and CORS behaviors at the API boundary using approved web integration testing frameworks.
3. **Architecture Tests:** Enforce that domain classes do not import `org.springframework.security.*`.

---

# 26. Security Observability

Security monitoring must integrate with the application's observability stack:
- High rates of `401 Unauthorized` or `403 Forbidden` must trigger operational alerts (indicating potential brute-force or credential stuffing).
- Spring Security events must be bridged to Micrometer metrics.

---

# 27. AI Security Governance

AI agents and automated tools MUST:
1. **Never** disable Spring Security or CSRF to "make tests pass."
2. **Never** hardcode credentials or API keys in source files.
3. **Never** log sensitive security objects (e.g., `Authentication`, raw tokens).
4. **Never** place backend secrets in frontend configuration files.
5. **Never** bypass method authorization checks for convenience.
6. **Never** invent new business roles (e.g., `ROLE_SUPERUSER`) to circumvent proper access design.
7. **Never** copy raw authentication tokens into durable asynchronous events.

---

# 28. Deferred Decisions Register

| Decision ID | Description | Assigned Document | Status |
|---|---|---|---|
| **O1** | Build Tool (Apache Maven) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O2** | Java Root Package (`com.anverraglobal`) | **AEOS-P04-D01** | **RESOLVED (D01)** |
| **O3** | Inbound Adapter Package (`adapter/inbound/`) | **AEOS-P04-D02** | **RESOLVED (D02)** |
| **O4** | OpenAPI implementation approach | **AEOS-P04-D04** | **RESOLVED (D04)** |
| **O5** | OpenAPI client generation approach | **AEOS-P04-D07** | **OPEN** |
| **O6** | PostgreSQL schema naming strategy | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O7** | Event listener idempotency mechanism | **AEOS-P04-D05** | **RESOLVED (D05)** |
| **O8** | DataSource configuration pattern | **AEOS-P04-D03** | **RESOLVED (D03)** |
| **O9** | Shared vs independently generated client API types | **AEOS-P04-D07** | **OPEN** |
| **O10** | Messaging Architecture / External Broker Strategy | **AEOS-P04-D05** | **RESOLVED (D05)** |
| **O11** | Authentication Architecture | **AEOS-P04-D06** | **RESOLVED (D06)** |
| **O12** | Authorization Enforcement Model | **AEOS-P04-D06** | **RESOLVED (D06)** |
| **O13** | Client Credential Storage Strategy | **AEOS-P04-D06** | **RESOLVED (D06)** |
| **O14** | CSRF Strategy | **AEOS-P04-D06** | **RESOLVED (D06)** |
| **O15** | Secret Management Architecture | **AEOS-P04-D06** | **RESOLVED (D06)** |
| **O16** | Security Context Propagation Across Asynchronous Boundaries | **AEOS-P04-D06** | **RESOLVED (D06)** |

---

# 29. Traceability

## 29.1 Phase 1 — Engineering Constitution
- `docs/01-constitution/03-architecture-principles/AEC-ARC-004-hexagonal-architecture.md`
- `docs/01-constitution/03-architecture-principles/AEC-ARC-005-separation-of-concerns.md`

## 29.2 Phase 4 — System Design Documents
- `docs/04-system-design/00-phase-4-overview.md`
- `docs/04-system-design/04-api-transport-implementation-architecture.md`
- `docs/04-system-design/05-event-async-implementation-architecture.md`

---

# 30. AI Anti-Invention Compliance Check

The authoring of this document successfully adhered to the strict anti-invention guidelines:
- No specific users, roles, or permissions were defined.
- No business authorization workflows were modeled.
- No API endpoints or DTOs were specified.
- No database schemas were designed.
- No security domains leaked into business logic.

---

# 31. Definition of Done

This document is complete when:
1. Authentication architecture (**O11**) is resolved (JWT).
2. Spring Security architecture and filter boundaries are established.
3. Security-context isolation from the domain is mandated.
4. Authorization enforcement architecture (**O12**) is resolved (Method-level primary).
5. No business roles/permissions have been invented.
6. Token/credential lifecycle is defined.
7. Client credential storage principles (**O13**) are established (HttpOnly cookies for Web, OS-provided secure credential storage for Mobile).
8. CSRF strategy (**O14**) is resolved (Enabled for Web).
9. CORS and security-header responsibilities are clearly defined.
10. Secrets management architecture (**O15**) is established as runtime secret injection through an approved deployment secret-management mechanism.
11. Credential/password security rules are established.
12. Security error handling integrates with D04.
13. Security auditing and sensitive data protection requirements are defined.
14. Asynchronous security context boundaries (**O16**) are explicitly addressed (no raw context propagation).
15. Database and supply-chain security guidelines integrate with existing architecture.
16. AI security guardrails are explicit.

---

# 32. Final Baseline Status & Next Step

## 32.1 Final Status
This document is authored and recorded as **Baseline Candidate**.

## 32.2 Stop Rule & Next Step
- **Authoring Position 7 Complete:** AEOS-P04-D06 is fully authored.
- **Do NOT proceed to AEOS-P04-D07.**
- **Do NOT create Java classes, API endpoints, or database schemas.**
- **Awaiting formal architectural review before proceeding to D07 (Client Implementation Architecture).**
