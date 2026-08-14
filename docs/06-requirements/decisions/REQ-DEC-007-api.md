# REQ-DEC-007: API Requirements

- **Capability:** Policy, Commission, Organizational Integration, Policy Statistics, Policy Document
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human / Product / Business decision

### 1. Purpose and Scope
This document consolidates the API-facing business and operational requirements resulting from the approved Phase 5 decisions. It serves as the authoritative decision for API behaviors. Web UX requirements have been decoupled into REQ-DEC-008.

### 2. Approved Policy / Organizational Model API Rules
- **Customer-created:** Initially 0 Agents, no Commission.
- **Agent-created:** Agent A is established. Not branch-bound.
- **Branch Admin-created:** Agent A is established. Policy belongs to the Branch.
- **Dealer-created:** Requires selecting a Branch Admin (who becomes Agent A). Policy belongs to the Branch.
- **Data Entry:** Acts on behalf of parent Agent or Branch Admin; inherits parent access scope.
- **Access Boundary:** API must strictly enforce Customer (self), Agent (involved), Branch Admin (branch-wide), Dealer (multi-branch) scopes.

### 3. Existing Policy Resolution
- API must not throw a generic uniqueness error (e.g., 409) as the normal business workflow when an authorized user attempts to add a duplicate.
- API should facilitate displaying the existing authorized Policy.
- API must update the existing Policy rather than creating a duplicate upon agent enrichment.
- API must conceal the existence of policies from unauthorized users (do not return "exists" errors for inaccessible IDs).
- **Customer Editability:** API must allow a Customer to edit a self-created policy only if no Agent/Commission is involved. If Agent/Commission is present, API must block customer modifications (e.g., return 403 Forbidden).

### 4. Agent Involvement (0-2 Agents)
- A Policy may have **0-2 Agents overall**.
- Customer-created policies may have 0 Agents initially.
- Once Agent involvement exists, Agent A is the primary Agent.
- Agent B is optional.
- No Agent C model is permitted.

### 5. Commission Validation API
- **Commission Type:** API must accept only Fixed Amount or Percentage mode.
- **Commission Limits:** API must enforce that Total Commission <= 50% of Policy Premium (derived from REQ-DEC-005).
- **Agent Allocation:** API must enforce that 100% of the calculated Commission is allocated. API accepts Agent B allocation (if present) and derives Agent A's remainder.
- API must execute these commission validations upon saving an agent-managed policy.

### 6. Commission Analytics API
- Commission aggregate APIs must block Data Entry users entirely (403 Forbidden).
- Commission APIs must only return absolute commission amounts, never percentages.
- Commission RESET to UNSET means the API excludes the policy from commission statistics.
- Valid commission remains included regardless of DRAFT, ACTIVE, or INACTIVE status.

### 7. Policy Statistics API
- Allowed API aggregate metrics: Total Policies, DRAFT count, ACTIVE count, INACTIVE count, Product-wise, Insurer-wise, Creation-context, Agent-involvement, Branch-wise counts.
- Filters can narrow authorized scope but API must never expand authorization beyond the caller's baseline.

### 8. Search / Filter / Sort / Pagination API
- API must provide authorized search, filtering, and sorting capabilities for Policies.
- API must support server-side pagination for list endpoints.
- API must support authorized date-range filtering and authorized drill-down parameters for both Policy Statistics and Commission Statistics.

### 9. Policy Document API (MVP)
- Zero or one current Policy Document per Policy.
- API endpoints for upload, download, replace, remove.
- API enforces Policy access rules on document endpoints; no independent document authorization model.
- Missing document does NOT block activation API logic.
- No versioning API.

### 10. Policy Lifecycle & State Machine
- **States:** DRAFT, ACTIVE, INACTIVE.
- API enforces mandatory field validations for ACTIVE transition.
- Deactivate and Reactivate require explicit API calls.
- No Delete Policy API.
- Changing Premium API call resets Commission to UNSET. Policy lifecycle state remains unchanged.

### 11. Creation Context vs. Current Agent
- API must preserve the original creation context independently of the current Agent A assignment.
- API must not overwrite the original creator audit fields.

### 12. Authorization Principle
- **The backend/API remains authoritative.** The API must enforce all organizational and Policy boundaries regardless of UI state.
- Authorized callers receive only authorized resource data.
- Authenticated but unauthorized access or action attempts must result in a `403 Forbidden` response.
