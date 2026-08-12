# Testing Architecture

**Document ID:** AEOS-P04-D08  
**Version:** 1.0  
**Status:** Proposed  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Depends on:** Phase 1 Engineering Constitution · Phase 2 System Architecture · Phase 3 Technology Architecture · AEOS-P04-D00 through D07

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D08 |
| **Title** | Testing Architecture |
| **Version** | 1.0 |
| **Status** | Proposed |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |

---

# 2. Purpose

This document establishes the testing architecture required to verify the implementation of AnverraGlobal's established architectural boundaries, domain purity, API contracts, security controls, and module integrations.

**Core Principle:**
Tests are executable verification of established architectural and behavioral contracts; tests must not become an alternative source of architectural truth.

---

# 3. Testing Boundary and Scope

D08 defines testing strategies across the system's core boundaries:
- **Backend:** Domain, Application, Module boundaries, Persistence adapters, REST adapters, Security, and Asynchronous event processing.
- **Client:** Web/Mobile component behavior, API integration, Authentication/Authorization UX, and End-to-end flows.
- **Cross-cutting:** Architecture verification, Contract verification, Dependency scanning, and AI-generated code verification.

D08 **verifies** decisions made by upstream architecture documents (D01-D07). It does not invent new business workflows, schemas, endpoints, or infrastructure. Configuration and environment testing requirements dependent on D09 remain deferred until D09 is established.

---

# 4. Test Pyramid & Testing Levels

The testing architecture adheres to a balanced test pyramid to avoid reliance on slow, brittle End-to-End (E2E) tests. Verification is pushed to the lowest possible level:

1. **Static & Architecture Tests:** Verify module boundaries, dependency rules, and domain purity (Fastest).
2. **Domain Unit Tests:** Verify business invariants and value objects in complete isolation (Fast).
3. **Application & Component Tests:** Verify use cases and adapters using targeted test doubles and mocks (Fast/Medium).
4. **Integration Tests (API, Persistence, Async):** Verify interactions with technical infrastructure and D04/D05/D06 contracts (Medium).
5. **Contract Tests:** Verify Web/Mobile client alignment with the governed OpenAPI contract (Medium).
6. **End-to-End (E2E) Tests:** Verify critical cross-boundary business capabilities (Slow, high value, low volume).

---

# 5. Domain Testing

Domain tests must preserve absolute domain purity.

- **Requirement:** Domain tests must execute entirely in memory without requiring Spring Framework, Spring Security, JPA, JDBC, HTTP, database infrastructure, or external brokers.
- **Scope:** Verification of domain invariants, value objects, domain behavior, and formally established domain policies.

---

# 6. Application-Layer Testing

Application use cases and services are tested to preserve dependency inversion.

- **Requirement:** Application tests must verify established application contracts rather than defining new workflows.
- **Test Doubles:** The application layer must be tested using appropriate test doubles, ports, mocks, or fakes for outbound infrastructure. Genuine integration tests should be used only when evaluating the integration itself.

---

# 7. Module Boundary Testing

The modular-monolith boundaries established in D01/D02 must be verified via automated tests.

- **Mechanism:** Spring Modulith module verification mechanisms, including `ApplicationModules.verify()` and appropriate Spring Modulith test support, shall be used where applicable to verify module boundaries. This ensures:
  - Allowed dependencies are respected.
  - Forbidden dependencies trigger build failures.
  - Public module surfaces (`contracts/` and `events/`) are encapsulated correctly.
  - Synchronous and asynchronous boundaries remain intact.

---

# 8. Architecture Testing

Architecture tests are treated as executable constitutional constraints.

- **Mechanism:** ArchUnit (or equivalent structural testing) must be used to verify:
  - Domain purity (absence of framework annotations).
  - Dependency direction (inward).
  - Proper adapter layering.
  - Prohibited dependency patterns (e.g., circular dependencies).

---

# 9. Persistence Testing

Persistence tests evaluate integration with the architecture defined in D03.

- **Scope:** Repository adapter integration, database migrations, and mapping verifications.
- **Isolation:** Tests must respect the physical and logical schema isolation strategies defined in D03. Cross-module data bleeding during tests is strictly prohibited.
- **Infrastructure:** Testcontainers (PostgreSQL) is the approved mechanism for providing realistic, isolated database integration testing environments.

---

# 10. API & REST Testing

API tests verify adherence to the authoritative REST/JSON boundary established in D04.

- **Scope:** Controller adapters, JSON serialization/deserialization, validation, HTTP error handling, and security filter integration.
- **Constraint:** Tests must not invent endpoints or schemas. They must verify "approved API operations" and "governed API representations."

---

# 11. OpenAPI Contract Testing

The D04/D07 governed OpenAPI contract is the absolute source of truth for client/backend communication.

- **Backend:** Must be verified to ensure the implementation strictly adheres to the OpenAPI specification, preventing API drift.
- **Client:** Must consume generated API client artifacts. Contract verification must detect incompatible changes between the governed API contract and its consumers/implementation according to the established CI verification process.

---

# 12. Asynchronous / Event Testing

Event testing verifies the architecture established in D05.

- **Scope:** Event publication, transaction synchronization, event handling, idempotency, retry behavior, and failure handling.
- **Constraint:** Raw authentication credentials and `SecurityContext` must not be propagated across durable event boundaries. Tests must verify that asynchronous consumers independently apply established security and business policy rules.

---

# 13. Security Testing

Security testing verifies the implementation of the D06 security architecture.

- **Authentication:** Verify successful authentication, invalid authentication credentials, and authentication failure handling according to the established authentication mechanism.
- **Authorization:** Verify method-level access control based on "formally established authorization policies." Ensure standard fail-closed behavior.
- **Web:** Verify Secure HttpOnly cookies and CSRF protection.
- **Secrets:** Ensure tests do not leak secrets into logs and verify the absence of secrets in source code/client bundles.

---

# 14. Client Testing

Client testing follows the Web and Mobile boundaries defined in D07.

- **Scope:** Component behavior, local state management, and UX logic.
- **State Testing:** Tests must evaluate loading, success, failure, cache invalidation, and retry behavior. Client state tests must *never* treat client-side data or authorization evaluation as authoritative.
- **Contract Mocking:** Integration testing should rely on OpenAPI-driven mock handlers (e.g., MSW) rather than brittle manual mocks.

---

# 15. End-to-End, Accessibility, and Performance Testing

- **End-to-End (E2E):** Reserved exclusively for verifying critical cross-boundary business behavior using formally established application capabilities.
- **Accessibility:** UI components must be verified for WCAG alignment, keyboard navigation, and semantic structure.
- **Performance & Resilience:** Performance and resilience testing should cover relevant API, persistence, asynchronous processing, concurrency, retry behavior, resource usage, and client-performance characteristics once corresponding requirements are established. Numerical SLAs are not invented here, but the testing structure must support measuring them once formally established.

---

# 16. Test Data, Mocking, and Environment Isolation

- **Test Data:** Sensitive or production data must never be used. Use deterministic fixtures, builders, or synthetic data matching established domain models.
- **Mocking:** Avoid excessive mocking of internal implementation details. Mock at established architectural boundaries (ports/adapters).
- **Environment Isolation:** Tests must be deterministic, repeatable, safe to run in parallel, and fully isolated across test classes and CI runs.

---

# 17. Dependency and Supply-Chain Verification

- **Scope:** Automated verification of lockfile integrity, known vulnerabilities, and prohibited dependencies (e.g., via CI integrated scanning tools).
- **Constraint:** Specific scanning vendors are implementation-level decisions and are not mandated here.

---

# 18. Coverage and CI/CD Test Gates

- **Coverage:** Coverage metrics are supporting indicators rather than the sole quality criterion. Coverage expectations should prioritize behavior and architectural risk, with quantitative thresholds applied where they provide useful enforcement. Prioritize testing domain logic, application logic, module boundaries, security controls, and API contracts.
- **CI/CD Gates:**
  Test execution shall use tiered quality gates based on test cost, architectural risk, and feedback requirements:
  - Fast unit and architecture verification should execute at the earliest practical development/CI stage.
  - Integration and contract tests should execute at an appropriate CI gate before changes are considered releasable.
  - Security verification should execute at appropriate development, CI, and release gates according to risk.
  - E2E tests should execute at controlled merge, release, or scheduled verification stages where their broader-system coverage provides sufficient value.
  
  Exact pipeline stages, scheduling, and vendor-specific implementation remain implementation-level decisions.

---

# 19. AI Development Testing Governance

AI agents and automated coding tools MUST:
- Run relevant tests after code changes and add tests for new behavior.
- Preserve architecture tests; they must not disable ArchUnit or Spring Modulith verification.
- Not delete failing tests or weaken assertions merely to make builds pass without architectural justification.
- Not replace integration tests with mocks merely to avoid failures.
- Not fabricate API mocks that contradict the governed OpenAPI contract.
- Treat test failures as diagnostic signals, not obstacles to suppress.

---

# 20. Technology Decision Register

*Note: O1-O16 are tracked in D00; O17-O26 are tracked in D07. D08 utilizes O27-O36.*

| Decision ID | Decision | Selected Option | Rationale & Architectural Consequences |
|---|---|---|---|
| **O27** | Test Strategy / Pyramid | **Heavy reliance on Fast/Module Tests + Targeted Integration.** | Prevents brittle E2E tests from dominating the CI pipeline while ensuring module integrity. |
| **O28** | Architecture Verification | **ArchUnit + Spring Modulith Verification.** | Treats architectural constraints as executable, failing the build on violations. |
| **O29** | Persistence Testing | **Testcontainers (PostgreSQL).** | Ensures isolated, realistic database integration aligned with D03 isolation. |
| **O30** | API Contract Testing | **OpenAPI-driven contract verification.** | Prevents API drift and ensures generated clients remain strictly aligned with the backend. |
| **O31** | Async/Event Testing | **Spring Modulith-aligned asynchronous event testing strategy.** | Verifies event publication, transactional interaction, idempotency, retry, and failure behavior within the established D05 architecture while keeping concrete test-support APIs at implementation level. |
| **O32** | Security Testing Strategy | **Framework-integrated security testing using the approved Spring Security testing mechanisms appropriate to the selected API test architecture.** | Provides verification of authentication and authorization behavior while keeping concrete test-library selection at the implementation level unless separately baselined. |
| **O33** | Client Testing Strategy | **Layered client testing: component/UI tests + API contract mocking + targeted integration/E2E verification.** | Component and API-contract mocking form the primary fast client-testing layers, while integration/E2E testing is reserved for behavior that genuinely requires broader system verification. |
| **O34** | Test Data Strategy | **Deterministic Fixtures / Builders.** | Prevents test pollution and reliance on sensitive production data. |
| **O35** | Test Isolation Strategy | **Stateless, Parallel-safe execution.** | Ensures fast CI feedback loops without cross-test contamination. |
| **O36** | CI Test Gate Strategy | **Tiered CI test gates based on test cost and risk: fast/architecture verification early; integration/contract/security tests in appropriate CI gates; E2E tests at controlled merge/release gates or scheduled verification as appropriate.** | Defines the architectural quality gates without prematurely defining the CI pipeline topology. |

---

# 21. Traceability

- **Phase 1 (Constitution):** Testing architecture enforces domain purity, dependency direction, and module boundaries.
- **AEOS-P04-D01/D02:** ArchUnit and Spring Modulith tests execute the boundaries defined in Backend/Module Architecture.
- **AEOS-P04-D03/D04/D05/D06:** Persistence, API, Async, and Security tests consume these established architectures without redefining their schemas, payloads, or policies.
- **AEOS-P04-D07:** Client tests consume the SPA, React/React Native, and OpenAPI-client strategies.

---

# 22. Definition of Done

This document is complete when:
- [x] Test boundaries map accurately to D01-D07.
- [x] No business workflows, rules, or schemas are invented.
- [x] Architecture tests are established as executable constraints.
- [x] AI Testing Governance is explicitly enforced.
- [x] O27-O36 open decisions are cleanly resolved.
- [x] D09 configuration testing is appropriately deferred.

---

# 23. Final Decision Status

**Final Decision Status:** Proposed

*(Awaiting formal architectural review)*
