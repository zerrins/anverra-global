# Phase 5 Architecture Clarification Decision Pack

## DECISION 1 — POLICY / COMMISSION CONSISTENCY

**Problem:** The business invariant requires that a Premium change instantly resets Commission to UNSET. Phase 4 architecture prohibits cross-module transaction coupling without explicit justification.

**Evaluation of Options:**

**A. Commission state persisted as part of Policy ownership.**
- *Consistency semantics:* Immediate atomic consistency.
- *Module coupling:* High. Collapses Commission into Policy, destroying the standalone Commission module boundary.
- *Transaction behavior:* Single relational transaction.
- *Failure behavior:* Native rollback.
- *Spring Modulith compatibility:* Removes the module entirely, bypassing Modulith rules.
- *Complexity:* Low.
- *Recommendation:* Rejected. Violates Phase 2 module definitions.

**B. Public Commission module contract.**
- *Consistency semantics:* Immediate consistency.
- *Module coupling:* Synchronous direct call. Policy calls Commission API (or via Application Service).
- *Transaction behavior:* Two separate transactions if Modulith strictly prohibits cross-module `@Transactional`.
- *Failure behavior:* If the second transaction fails, partial state exists (Premium updated, Commission not reset).
- *Spring Modulith compatibility:* Allowed only if transactions are separated, which breaks the invariant.
- *Complexity:* Medium.
- *Recommendation:* Rejected due to failure behavior and loss of atomicity.

**C. Application-level orchestration.**
- *Consistency semantics:* Immediate consistency.
- *Module coupling:* Decoupled domains, but tightly couples the database transaction in the application layer.
- *Transaction behavior:* Cross-module spanning transaction via `@Transactional`.
- *Failure behavior:* Clean rollback.
- *Spring Modulith compatibility:* Violates the strict prohibition on cross-module transaction coupling unless explicitly exempted by D02.
- *Complexity:* Medium.
- *Recommendation:* Rejected (without exception) as it violates D02.

**D. Synchronous Domain Event.**
- *Consistency semantics:* Immediate consistency.
- *Module coupling:* Decoupled logically, coupled transactionally.
- *Transaction behavior:* Spring `@TransactionalEventListener` running in the same transaction phase.
- *Failure behavior:* Rollback triggers if the listener fails.
- *Spring Modulith compatibility:* Often restricted in strict CQRS/event architectures unless exempted by D05.
- *Complexity:* Medium.
- *Recommendation:* Rejected (without exception). Modulith strongly prefers asynchronous boundaries for cross-module events.

**E. Asynchronous Domain Event.**
- *Consistency semantics:* Eventual consistency.
- *Module coupling:* Fully decoupled.
- *Transaction behavior:* Isolated transactions. Policy commits, publishes event, Commission consumes and commits.
- *Failure behavior:* Dead-letter queue / retry mechanism required if Commission reset fails.
- *Spring Modulith compatibility:* Perfectly compatible.
- *Complexity:* High (requires robust event infrastructure).
- *Recommendation:* Fails the rigid business invariant if immediate consistency is demanded by compliance.

**F. Other architecture-compatible solution.**
- Creating a shared transactional Saga or using a distributed lock, but this vastly overcomplicates a simple CRUD reset.

**Conclusion:** 
ARCHITECTURE DECISION REQUIRED. There is no mechanism to enforce strict relational atomicity between two independent modules without violating Phase 4 boundaries (D01/D02).

## DECISION 2 — ORGANIZATIONAL HIERARCHY / AUTHORIZATION

**Problem:** Phase 5 authorization depends heavily on organizational relationships (Dealer → Branches → Agents), but no Organization module exists in Phase 2.

1. **Where Customer identity is owned:** `Customer` module.
2. **Where Agent identity is owned:** Undefined/Missing.
3. **Where Branch Admin identity/relationship is represented:** Undefined/Missing.
4. **Where Dealer/Branch relationship is represented:** Undefined/Missing.
5. **Where Data Entry/parent relationship is represented:** Undefined/Missing.
6. **What information D06 Security receives:** Currently undefined (standard generic JWT claims).
7. **What information can legitimately be placed in the security context:** Identity sub, basic roles. Large nested hierarchies usually exceed JWT best practices.
8. **Whether organizational relationships are already represented elsewhere:** No approved module handles this.
9. **Whether an architecture gap exists:** Yes.

**Conclusion:**
ARCHITECTURE GAP — ORGANIZATIONAL HIERARCHY SOURCE NOT BASELINED.

## DECISION 3 — REPORTING OWNERSHIP

**Problem:** Phase 5 requires Policy and Commission Statistics, but Phase 2 designates the `Reporting` module as the owner of analytics and read models.

1. **Do these statistics belong to Reporting?** Yes. Aggregations and analytical queries belong to the Reporting module to protect operational databases from analytics strain.
2. **Should operational Policy/Commission modules expose data/contracts to Reporting?** Yes, via immutable domain events.
3. **Should Reporting consume module events?** Yes, asynchronously building its read models.
4. **Are read models required?** Yes, to fulfill cross-module statistical views without performing cross-module SQL joins.
5. **Would direct SQL aggregation inside Policy/Commission violate module ownership?** Yes. Serving analytics from operational domains violates Phase 2 boundaries.
6. **How should authorization scope be represented in Reporting?** The Reporting read models must ingest the hierarchical authorization rules (once the Organization gap is resolved) to securely filter aggregations at the query level.

**Conclusion:**
- *Business Capability:* Statistics APIs.
- *Module Ownership:* Owned by `Reporting`.
- *Query Implementation:* Read models consuming events (Deferred to D03/Reporting architecture).

## ARCHITECTURE DOCUMENT IMPACT

- **Decision 1 (Consistency):** Impacts **D01/D02** (Module Boundaries and Transaction Rules). If synchronous orchestration is required, an exception must be documented.
- **Decision 2 (Organization Hierarchy):** Impacts **D01/D02** (Requires baseline for an Organization/Hierarchy module or defining Identity expansion) and **D06** (Security Context mapping).
- **Decision 3 (Reporting):** Impacts **D01/D02** (Reaffirming Reporting boundaries) and **D05** (Eventing architecture for Reporting consumption).

---

## DECISION REGISTER

| Decision | Current Architecture | Phase 5 Requirement | Gap/Conflict | Options | Recommended Direction | Required Authority |
|---|---|---|---|---|---|---|
| 1. Policy/Commission Consistency | Cross-module transactions prohibited | Atomic Premium update + Commission reset | Modulith isolation vs Business atomicity | Application `@Transactional`, Sync Event, Async Event | ARCHITECTURE DECISION REQUIRED | Architecture Owner (D02/D03; D05 if asynchronous eventing is selected) |
| 2. Hierarchical Authorization | No Organization module approved | Dealer/Branch/Agent scope enforcement | Authoritative source of hierarchy is missing | Invent module, Invent claims, Defer | ARCHITECTURE GAP — ORGANIZATIONAL HIERARCHY SOURCE NOT BASELINED | Architecture Owner (D01/D06) |
| 3. Statistics Ownership | Reporting module owns analytics | Policy/Commission Statistics APIs | Exposing analytics from operational modules | Policy SQL, Commission SQL, Reporting Read Model | DEFERRED TO D03 / D05 (Reporting owns analytics) | Architecture Owner (D02) |

---

# Architecture Decisions Required

1. **Cross-Module Transaction Exemption:** Do we allow a cross-module `@Transactional` boundary between Policy and Commission to enforce the atomic reset invariant, or do we loosen the invariant to eventual consistency?
2. **Authoritative Organization Hierarchy:** Which module or system will own the Dealer → Branch → Agent structural hierarchy required to evaluate Phase 5 authorization boundaries?
3. **Reporting Event Integration:** Do we authorize the creation of `Policy` and `Commission` domain events specifically to feed the Phase 2 `Reporting` module for statistical aggregation?

---

**PHASE 5 FROZEN**
**TECHNICAL DESIGN PAUSED**
**ARCHITECTURE CLARIFICATIONS REQUIRED**
