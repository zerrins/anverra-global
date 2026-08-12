# Phase 5 Implementation Overview

**Document ID:** AEOS-P05-D00  
**Version:** 1.0  
**Status:** Baselined  
**Phase:** 5 — Implementation  
**System:** AnverraGlobal  
**Depends on:** Phase 1 (Constitution) · Phase 2 (Blueprint) · Phase 3 (Technology) · Phase 4 (System Design AEOS-P04-D00 through D11)

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P05-D00 |
| **Title** | Phase 5 Implementation Overview |
| **Version** | 1.0 |
| **Status** | Baselined |
| **Phase** | 5 — Implementation |
| **System** | AnverraGlobal |
| **Immediate Governing Document** | Phase 4 System Design (AEOS-P04-D00 through D11) |

---

# 2. Purpose and Scope

Phase 5 (Implementation) is the phase where the authoritative architecture designed in Phases 1–4 is translated into functional source code. The purpose of Phase 5 is to bootstrap the repository, wire the foundational infrastructure, and execute the iterative implementation of the core business modules. 

The scope includes writing application code, implementing tests, configuring builds, and establishing CI pipelines. The scope strictly excludes redefining architectural boundaries, inventing new business capabilities, or contradicting established Phase 1–4 governance.

---

# 3. Relationship Between Architecture and Implementation

Phase 4 produced the *System Design* — the blueprint that explicitly defines how the system will be built. Phase 5 executes that blueprint. 

Phase 4 established the "what" and the structural "how" (e.g., modular monolith boundaries, Hexagonal Architecture patterns, explicit `contracts/` and `events/`). Phase 5 makes implementation-level decisions (e.g., naming specific variables, writing exact SQL migrations, configuring the specific OpenAPI generator tool) that were intentionally deferred from the architecture baseline.

---

# 4. Implementation Authority Hierarchy

During implementation, all developers and AI agents must adhere to the following absolute authority chain. Lower levels may not contradict or weaken higher levels.

1. **Phase 1 (Engineering Constitution)** — Inviolable principles (Domain-Driven Design, Hexagonal Architecture, Modular Monolith).
2. **Phase 2 (System Architecture)** — Module boundaries and business ownership.
3. **Phase 3 (Technology Architecture)** — Approved core technologies (Spring Boot, React, PostgreSQL).
4. **Phase 4 (System Design)** — The concrete structural blueprint (D00–D11).
5. **Phase 5 (Implementation)** — The execution phase. Implementation convenience, tool defaults, or language idioms can never override Phase 1–4 architectural constraints.

---

# 5. Implementation Prerequisites

## 5.1 Repository Bootstrap
- The physical directory structure defined in AEOS-P04-D10 must be provisioned exactly as specified.
- The Git repository, primary branches, and base `.gitignore` must be initialized.
- D00 master decision register synchronization must be tracked as a governance cleanup item; it does not block Phase 5 implementation.

## 5.2 Backend Implementation
- AEOS-P04-D01 dictates that the backend is a single Spring Boot application.
- The build tool established by D01 must be initialized according to the D01 architecture.
- The root package namespace must be established.
- Spring Modulith dependencies and ArchUnit dependencies must be added to the build file.

## 5.3 Module Implementation
- AEOS-P04-D02 requires every implemented module to follow the canonical Hexagonal Architecture internal structure (`domain/`, `application/`, `adapter/`, `contracts/`, `events/`).
- Module access rules must be configured and validated via Spring Modulith.
- Module implementation must begin with the domain layer, entirely free from Spring or infrastructure dependencies.

## 5.4 Persistence Implementation
- AEOS-P04-D03 defines the physical isolation strategy (schema-per-module).
- Flyway migration paths and database connection pooling/routing must be implemented to prevent cross-module data access.
- Spring Data JDBC repositories must be implemented within the infrastructure layer.

## 5.5 API Implementation
- AEOS-P04-D04 establishes the OpenAPI architecture.
- The implementation must consume and implement the governed OpenAPI contract established by D04. Any code-first/design-first approach already established by D04 must be followed. Where a concrete implementation tool or workflow was intentionally deferred, that choice may be made during implementation without changing the governed API contract.
- REST controllers (inbound adapters) must map DTOs to/from application domain objects.
- Global exception handling (`@ControllerAdvice`) must be wired.

## 5.6 Security Implementation
- AEOS-P04-D06 dictates the placement of Spring Security filter chains in the `platform/` package.
- The authentication mechanism (e.g., OAuth2/JWT) must be configured without leaking into the domain logic.
- Method-level authorization checks must be implemented at the application service layer.

## 5.7 Client Implementation
- AEOS-P04-D07 requires independent Web and Mobile codebases with shared API contracts where technically compatible.
- **O5 / OpenAPI Client Generation Tool:** The specific generator tool is an implementation-level decision and must be evaluated and selected prior to writing the build script that generates the client.
- **O9 / Shared Types:** The implementation must correctly map the generated types as shared assets between Web and Mobile per D07/O22.

## 5.8 Async/Event Implementation
- AEOS-P04-D05 dictates the use of Spring Modulith durable events for cross-module async communication.
- Event listener idempotency must be implemented.
- Transactional event publication must be wired correctly.

## 5.9 Testing Implementation
- AEOS-P04-D08 establishes the testing pyramid.
- ArchUnit rules must be implemented and running in the CI pipeline to enforce module boundaries and Hexagonal rules.
- Spring Modulith-aligned module and integration testing mechanisms must be implemented according to D08. The specific annotation, test-support API, or mechanism remains an implementation-level choice unless explicitly mandated by the D08 baseline.

## 5.10 Configuration & Environment
- AEOS-P04-D09 defines profile and secrets management.
- Local development configuration must be provisioned according to D09 using a secure, non-committed mechanism appropriate to the selected implementation tooling. Production secrets must never be committed to the repository.

## 5.11 CI / Build
- Build pipelines must execute the build, run tests (including ArchUnit), and verify module boundaries before merging any implementation code.

---

# 6. Phase 5 Execution Workstreams

To maintain governance during implementation, Phase 5 is organized into logical implementation workstreams rather than traditional architecture documents:

1. **Workstream 1:** Repository Bootstrap & Application Platform Foundation
2. **Workstream 2:** Identity & Access Module Implementation
3. **Workstream 3:** Core Business Modules (Customer, Product, Policy, Commission) Implementation
4. **Workstream 4:** Supporting Modules (Notification, Reporting) Implementation
5. **Workstream 5:** Web & Mobile Client Implementation

---

# 7. Decision Governance & Change Control

## 7.1 Implementation-Level Decisions
Developers and AI agents are authorized to make implementation-level decisions (e.g., variable naming, algorithm choice, specific SQL query syntax, selecting the concrete OpenAPI generator tool) without architectural escalation, provided the decision does not conflict with Phase 1–4 rules.

## 7.2 Architectural Escalation Rules
Any proposed change to module boundaries, cross-module communication mechanisms, technology stack, Hexagonal Architecture dependency direction, or API contracts MUST trigger an architectural escalation and a formal Architectural Decision Record (ADR) review.

If an implementation constraint proves a Phase 4 architectural decision unworkable, implementation in that area must STOP until an ADR formally updates the Phase 4 baseline.

## 7.3 AI-Agent Implementation Governance
- AI agents MUST reference the Phase 1–4 baseline before writing code.
- AI agents MUST NOT invent business rules, endpoints, or domain models not explicitly detailed in or authorized by the requirements.
- AI agents MUST flag architectural violations when prompted to implement features that break established boundaries.

---

# 8. Non-Blocking Deferred Items

The following items are intentionally deferred and do not block Phase 5 implementation:
- Selection of the concrete OpenAPI generator tool (O5 implementation detail).
- Selection of concrete Cloud Provider (AWS/GCP/Azure).
- Selection of concrete CI/CD vendor.
- Selection of concrete Secret Manager (e.g., Vault).
- Selection of concrete external identity provider.
- D00 Master Register Synchronization (Documentation chore).

---

# 9. Exit Criteria

Phase 5 is complete when the authorized capability scope has been implemented, architectural and functional tests pass, security and configuration requirements are satisfied, and the system meets the defined implementation and release-readiness criteria.

Phase 5 implementation completion does not by itself authorize production deployment. Production deployment remains subject to separately governed infrastructure, release, and operational readiness decisions.

---

# 10. Final Decision Status

**Final Decision Status:** Baselined
