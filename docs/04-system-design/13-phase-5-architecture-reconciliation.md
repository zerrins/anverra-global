# Phase 5 Architecture Reconciliation

## 1. Confirmed compatible decisions
- **Requirement:** Existing Policy resolution should not use 409 as the normal business workflow.
- **Existing architectural authority:** Standard REST APIs.
- **Current Phase 5 proposal:** Safe lookup endpoint yielding 200 OK or 403 Forbidden without throwing exceptions.
- **Conflict/gap:** None.
- **Recommendation:** Proceed with the safe lookup concept for the normal business flow.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

## 2. Decisions conflicting with Phase 4
### A. Policy / Commission Transaction Coupling
- **Requirement:** Premium update and Commission reset must execute as a single atomic business transaction.
- **Existing architectural authority:** Phase 4 prohibits cross-module transaction coupling without explicit justification.
- **Current Phase 5 proposal:** Application Service uses `@Transactional` spanning the `policy` and `commission` modules.
- **Conflict/gap:** Violates Modulith strict transaction boundaries. If Policy and Commission are truly separate modules, spanning a relational transaction across them tightly couples their persistence models.
- **Recommendation:** Determine whether Commission resets should be driven by synchronous Domain Events (e.g., `PremiumChangedEvent` published by Policy and handled by Commission) rather than an orchestrated wrapper transaction, or if a formal architectural exception is required.
- **Final status:** REQUIRES ARCHITECTURE CLARIFICATION

### B. Reporting Ownership
- **Requirement:** The API must provide Policy Statistics and Commission Statistics.
- **Existing architectural authority:** Phase 2 explicitly establishes the `Reporting` module as the owner of analytics and read models.
- **Current Phase 5 proposal:** Exposing SQL aggregations directly out of the `Policy` and `Commission` persistence layers.
- **Conflict/gap:** Violates the Reporting module boundary. Operational modules should not serve aggregate analytics.
- **Recommendation:** The `Policy` and `Commission` modules should expose data through public events or contracts. The `Reporting` module consumes these to build read models and serves the statistics APIs.
- **Final status:** REQUIRES ARCHITECTURE CLARIFICATION

## 3. Decisions requiring architecture clarification
### A. Organization Module and Hierarchy Resolution
- **Requirement:** Authorization must resolve Dealer -> Branch -> Agent scope hierarchies.
- **Existing architectural authority:** Phase 2 approved modules (Identity, Customer, Product, Policy, Commission, Notification, Reporting). Organization Management is explicitly deferred/unresolved.
- **Current Phase 5 proposal:** Dynamic backend lookup against an assumed Organization module.
- **Conflict/gap:** No Organization module exists in the approved architecture. The authoritative source for branch/dealer relationships is undefined.
- **Recommendation:** Do not invent an Organization module. Identify this as a critical architecture gap. Phase 5 cannot enforce hierarchical authorization until the platform establishes where hierarchical data lives.
- **Final status:** REQUIRES ARCHITECTURE CLARIFICATION

### B. Authorization Context Resolution
- **Requirement:** The backend must evaluate hierarchical authorization.
- **Existing architectural authority:** D06 (Security) is foundational but does not specify Identity Provider capabilities.
- **Current Phase 5 proposal:** Hybrid cached backend dynamic lookup to avoid JWT bloat.
- **Conflict/gap:** We do not know what the approved security architecture (IdP) actually provides (e.g., if it can natively provide flat scope claims).
- **Recommendation:** Do not assume `branch_ids` claims or invent caching modules. Await D06 finalization of the authorization context boundary.
- **Final status:** REQUIRES ARCHITECTURE CLARIFICATION

### C. Document Storage Provider
- **Requirement:** Policies have 0..1 Documents that can be uploaded and downloaded.
- **Existing architectural authority:** No cloud/storage architecture is established for the platform.
- **Current Phase 5 proposal:** AWS S3 or Cloudflare R2 via Presigned URLs.
- **Conflict/gap:** Recommending a specific vendor out of bounds of the platform cloud strategy.
- **Recommendation:** The Policy module should rely on a provider-neutral `DocumentStorage` internal abstraction. The exact vendor (S3, R2) and the exact mechanism (Presigned URLs vs Backend Streaming) requires overarching architecture approval.
- **Final status:** REQUIRES ARCHITECTURE CLARIFICATION

## 4. Decisions deferred to D03 (Persistence Architecture)
### A. Database Schema Design
- **Requirement:** Customer-created policies may initially have zero Agents.
- **Existing architectural authority:** D03 dictates persistence models.
- **Current Phase 5 proposal:** `agent_a_id` is a nullable foreign key.
- **Conflict/gap:** None. The nullable FK is just a design consequence, not a business requirement.
- **Recommendation:** Defer exact column layouts, nullability, unique constraints, and Flyway mappings to D03.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

### B. Statistics Query Strategy
- **Requirement:** Statistics must never aggregate unauthorized data.
- **Existing architectural authority:** D03 (Persistence).
- **Current Phase 5 proposal:** Database-side `GROUP BY` aggregations.
- **Conflict/gap:** None, but the query strategy (and indexing) belongs in D03, ideally mapped into the Reporting module's persistence store.
- **Recommendation:** Defer the physical query execution strategy to D03.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

## 5. Decisions deferred to D04 (API Architecture)
- **Requirement:** CRUD and lifecycle state machine endpoints.
- **Existing architectural authority:** D04 dictates exact REST/OpenAPI contracts.
- **Current Phase 5 proposal:** Hybrid `PATCH` for data updates and `POST /activate` for lifecycle actions.
- **Conflict/gap:** None. The hybrid style is a recommended design.
- **Recommendation:** Do not prematurely finalize exact DTO schemas, paths, or full HTTP error taxonomies. Defer to D04.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

## 6. Decisions deferred to D06 (Security Architecture)
- **Requirement:** Authenticated + unauthorized = 403 Forbidden.
- **Existing architectural authority:** D06.
- **Current Phase 5 proposal:** Enforcing 403 blocks in Application Services.
- **Conflict/gap:** None.
- **Recommendation:** Defer exact JWT claim mapping, OAuth implementation details, and permission mapping configurations to D06.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

## 7. Decisions deferred to D07 (Frontend Architecture)
- **Requirement:** Complex conditional forms for MVP Policy entry.
- **Existing architectural authority:** D07 dictates frontend technology baseline.
- **Current Phase 5 proposal:** TanStack Query, React Hook Form, Zod.
- **Conflict/gap:** None. These are design proposals to satisfy the UX complexity.
- **Recommendation:** Do not add dependencies. Defer the final library adoption decision to D07.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

## 8. Decisions that Phase 5 can approve directly
- **Requirement:** The definition of the conceptual Policy identity, its business lifecycle states, maximum Agent involvement count, and the pure Commission calculations constraints (e.g., <= 50%).
- **Existing architectural authority:** Phase 5 Requirements (REQ-DEC-004 to REQ-DEC-008).
- **Recommendation:** The pure Domain capabilities inside the `policy` and `commission` modules are fully resolved and baselined.
- **Final status:** READY FOR TECHNICAL DESIGN BASELINE

## 9. Final technical design recommendation
The Phase 5 business requirements are complete, but attempting to design the technical implementation immediately exposes deep, unresolved gaps in the overarching Phase 4 architecture. Specifically:
1. Module boundaries conflict with the requirement for atomic Policy/Commission transactions.
2. The hierarchical authorization requirement cannot be satisfied because the Organization module does not officially exist.
3. Statistics endpoints violate the Reporting module boundary.

**TECHNICAL DESIGN STATUS: REQUIRES ARCHITECTURE CLARIFICATION**
