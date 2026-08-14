# Architecture Decisions Required

## Decision 1 — Policy/Commission consistency

**Problem:** 
The business requires that a change to a Policy's premium automatically nullifies any existing Commission fields, ensuring they are recalculated. 

**Existing architectural constraint:** 
Phase 4 architecture prohibits cross-module transaction coupling unless explicitly architecturally justified. The business transaction boundary spanning Policy and Commission remains an unresolved architectural decision.

**Business requirement:** 
Premium change -> Commission RESET to UNSET as an atomic invariant (REQ-DEC-005 / REQ-DEC-007).

**Options:**
- **A. Allow an explicit cross-module transactional exception.** Use `@Transactional` at the Application Service layer to orchestrate both modules.
- **B. Redesign ownership so the invariant is within one transactional ownership boundary.** Collapse Commission state into the Policy domain, removing the separate Commission module.
- **C. Accept eventual consistency through asynchronous events.** Policy emits a `PremiumChangedEvent`; Commission listens and resets asynchronously.
- **D. Other architecture-approved solution.** 

**Consequences:**
- **A (Exception):** *Consistency*: Immediate. *Failure behavior*: Clean rollback. *Coupling*: High transaction coupling. *Impact*: Introduces deliberate cross-module transactional coupling and therefore requires explicit architectural justification and documentation under D02/D03.
- **B (Redesign):** *Consistency*: Immediate. *Failure behavior*: Clean rollback. *Coupling*: Eliminates the Commission module boundary. *Impact*: Contradicts Phase 2 approved module lists.
- **C (Eventual):** *Consistency*: Eventual. *Failure behavior*: Requires dead-letter queues or manual reconciliation if the Commission reset fails. *Coupling*: Decoupled. *Impact on REQ-DEC-005 / Stats*: Commission dashboards may briefly show stale percentages/amounts until the event processes.
- **Eventing / Complexity:** The architecture authorizes both synchronous collaboration through public contracts and asynchronous collaboration through the approved Spring Modulith event mechanism. The transaction semantics of asynchronous collaboration remain open under D05.

**Decision required from architecture owner:** 
How can the Premium → Commission UNSET invariant be maintained while respecting module boundaries?

---

## Decision 2 — Organizational hierarchy ownership

**Problem:** 
Phase 5 authorization (list, search, statistics, and editability) requires deep knowledge of the corporate hierarchy to determine visibility. However, no authoritative owner for this hierarchy has been baselined in the architecture.

**Existing architectural constraint:** 
Phase 2 approved modules (Identity, Customer, Product, Policy, Commission, Notification, Reporting) do not include an "Organization" or "Hierarchy" module.

User identity and RBAC are owned by Identity; Agent as an organizational/business entity and its relationships remain unresolved. Similarly, Dealer and Branch organizational ownership are unresolved.

**Business requirement:** 
- Customer scope (own Policies)
- Agent scope (involved Policies)
- Branch Admin scope (Branch Policies)
- Dealer multi-Branch scope (Policies across their Branches)
- Data Entry inherited scope (inherited parent scope)

**Options:**
- **A. Extend an existing approved module.** (e.g., expand the `Customer` or `Identity` module to store hierarchy data).
- **B. Expand Identity responsibilities.** (e.g., force the Identity Provider / JWT to encode the entire resolved hierarchy as custom claims).
- **C. Introduce a formally approved Organization/Hierarchy capability.** (A new foundational module).
- **D. Other existing-system integration.** (e.g., querying an external corporate Active Directory/HR system dynamically).

**Consequences:**
- Inventing modules or claims ad-hoc violates D01 and D06. 
- Without an authoritative source for Dealer/Branch/Agent relationships, the backend cannot securely enforce Phase 5 authorization rules.

**Decision required from architecture owner:** 
Where does the authoritative organizational hierarchy live?

---

## Decision 3 — Reporting ownership/integration

**Problem:** 
Phase 5 introduces analytical queries (Policy and Commission Statistics) which have been mistakenly routed into the operational Policy/Commission modules during preliminary design, bypassing the established Reporting boundaries.

**Existing architectural constraint:** 
Phase 2 establishes the `Reporting` module as the supporting module explicitly responsible for analytics and read models.

**Business requirement:** 
- Policy Statistics APIs (counts by status, branch, etc.)
- Commission Statistics APIs (aggregated amounts)

**Options (Proposed Direction):**
Operational modules may expose governed public contracts/events to Reporting, subject to the applicable D04/D05 design decisions. The `Reporting` module consumes these, constructs analytical read models, and serves the Statistics APIs.

**Consequences:**
- Protects operational persistence from heavy analytical queries.
- Requires finalizing the eventing payload schemas (D05) and establishing read-model persistence strategies (D03).

**Decision required from architecture owner:** 
Does Reporting own Policy/Commission statistics and how should it receive the data?
Are read models the recommended implementation direction? Yes. Whether they are mandatory for these specific statistics remains a D03/D05 technical-design decision.

---

PHASE 5 REMAINS FROZEN.
NO IMPLEMENTATION AUTHORIZED.
TECHNICAL DESIGN REMAINS PAUSED UNTIL THESE ARCHITECTURE DECISIONS ARE RESOLVED.
