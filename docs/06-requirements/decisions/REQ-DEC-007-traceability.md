# Phase 5 Traceability Matrix

This document provides a requirements-to-design traceability matrix for the Phase 5 Policy, Commission, and Organizational Integration capabilities. It maps authoritative business requirements to their design consequences and current technical proposals.

## Traceability Matrix

| Requirement ID | Requirement | Authoritative Source | Design Consequence | Status |
|---|---|---|---|---|
| POL-01 | Policy identity and immutable resolution (UX/Business) | REQ-DEC-004, REQ-DEC-008 | Existing authorized Policy should be resolved/displayed rather than presenting a uniqueness error. Agent enrichment updates the existing Policy. Unauthorized users must not learn an inaccessible Policy exists. | REQUIRED |
| POL-02 | Policy can exist without agent or branch | REQ-DEC-006, REQ-DEC-007 | System must allow null agent/branch assignments for Customer-created policies. | REQUIRED |
| POL-03 | Regular Agent-created Policy is non-branch-bound | REQ-DEC-006, REQ-DEC-007 | Policy tied to Agent A, not assigned to Branch. | REQUIRED |
| POL-04 | Branch Admin-created Policy is branch-bound | REQ-DEC-006, REQ-DEC-007 | Policy tied to Branch Admin and their Branch. | REQUIRED |
| POL-05 | Dealer-created Policy requires Branch Admin selection | REQ-DEC-006, REQ-DEC-007 | Dealer cannot be Agent A; UI forces Branch Admin selection. | REQUIRED |
| POL-06 | Data Entry inherits parent scope | REQ-DEC-006, REQ-DEC-007 | No global access; acts on behalf of parent. | REQUIRED |
| POL-07 | 0–2 Agents overall | REQ-DEC-005, REQ-DEC-006 | Customer-created may initially have 0. Once involved, Agent A is established. Agent B optional. No Agent C. | REQUIRED |
| POL-08 | Conditional edit access for Customers | REQ-DEC-006, REQ-DEC-008 | Customer-created Policy with no Commission → Customer may edit. Once Agent/Commission involvement occurs → Customer becomes View Only. | REQUIRED |
| POL-09 | Policy visibility strict boundaries | REQ-DEC-006, REQ-DEC-007 | API must strictly enforce Customer, Agent, Branch Admin, Dealer scopes. | REQUIRED |
| POL-10 | Preservation of creation context | REQ-DEC-007 | Separate "created by" from "current agent". | REQUIRED |
| POL-11 | Policy Lifecycle: DRAFT, ACTIVE, INACTIVE | REQ-DEC-004, REQ-DEC-007 | Strict state machine with explicit transitions. | REQUIRED |
| POL-12 | No physical Policy deletion | REQ-DEC-004, REQ-DEC-007 | Historical integrity must be maintained. | REQUIRED |
| COM-01 | Commission Type: Fixed vs Percentage | REQ-DEC-005 | Form accepts dual modes. | REQUIRED |
| COM-02 | Commission Limit (Max 50% of Premium) | REQ-DEC-005 | Validation rule applied unconditionally on save. | REQUIRED |
| COM-03 | Agent A / Agent B Allocation totals 100% | REQ-DEC-005 | Derived Agent A remainder. | REQUIRED |
| COM-04 | Premium change resets Commission to UNSET | REQ-DEC-005, REQ-DEC-007 | Updating premium clears commission state. | REQUIRED |
| COM-05 | Commission Dashboard & Policy-level visibility | REQ-DEC-007, REQ-DEC-008 | Role-based aggregate vs item-level access. | REQUIRED |
| COM-06 | Dashboard percentage prohibition | REQ-DEC-007, REQ-DEC-008 | Absolute amounts only in aggregates. | REQUIRED |
| COM-07 | Data Entry Commission Dashboard restriction | REQ-DEC-007 | Data Entry cannot view aggregates. | REQUIRED |
| COM-08 | Valid Commission inclusion in statistics | REQ-DEC-007 | Included regardless of DRAFT/ACTIVE/INACTIVE status. | REQUIRED |
| STA-01 | Policy Statistics constraints | REQ-DEC-007 | Only explicit dimensions (status, product, insurer, branch, agent). | REQUIRED |
| AUTH-01 | Backend is the authoritative authorization boundary | REQ-DEC-007, D06 | UI visibility is not authorization. | REQUIRED |
| AUTH-02 | Dealer sees Policies across their Branches | REQ-DEC-006 | API authorizes multi-branch view for Dealers. | REQUIRED |
| DOC-01 | 0..1 document MVP | REQ-DEC-007, REQ-DEC-008 | Optional, no versioning. | REQUIRED |
| DOC-02 | Document upload, replace, remove, download | REQ-DEC-007, REQ-DEC-008 | Standard file lifecycle actions with missing-document warnings. | REQUIRED |
| DOC-03 | Document Authorization inherited from Policy | REQ-DEC-007 | No independent document ACL. | REQUIRED |

## Technical Proposals

| Proposal ID | Technical Proposal | Originating Requirement | Status |
|---|---|---|---|
| TECH-01 | DB `policy_number` unique constraint returning 409 Conflict for concurrency/integrity failures | POL-01 | DESIGN PROPOSAL |
| TECH-02 | `agent_id` and `branch_id` nullable in Database | POL-02 | DESIGN PROPOSAL |
| TECH-03 | Encoding `branch_ids` and `parent_role` in JWT<br/><br/>**Superseded by Phase 5 Architecture Decision 2. The organizational hierarchy must not be made authoritative through JWT claims. Identity remains responsible for authentication/RBAC while Organization/Hierarchy is the authoritative source of organizational relationships.** | POL-09, AUTH-02 | SUPERSEDED |
| TECH-04 | Enforcing access via Repository-level `WHERE` clauses | POL-09 | DESIGN PROPOSAL |
| TECH-05 | Commission Type enum representation & DB columns | COM-01 | DESIGN PROPOSAL |
| TECH-06 | AWS S3 for storage architecture | DOC-02 | DESIGN PROPOSAL |
| TECH-07 | AWS Presigned URLs for secure access | DOC-02 | DESIGN PROPOSAL |
| TECH-08 | Single-page Policy form with conditional validation | POL-08, UX Form UX | REQUIRED |
| TECH-09 | React Hook Form & Zod for complex frontend state | TECH-08 | DESIGN PROPOSAL |
| TECH-10 | TanStack Query for API caching | General UX | DESIGN PROPOSAL |
| TECH-11 | Concrete OpenAPI endpoint design | API Guidelines | DEFERRED |
| TECH-12 | PostgreSQL schema & Flyway migrations | Persistence Guidelines | DEFERRED |

# Phase 5 Architecture Decisions Traceability

The following Phase 5 architecture decisions have been explicitly approved by the Architecture Owner.

### Decision 1: Policy / Commission Atomic Consistency
- **Source:** Phase 5 → REQ-DEC-005 / REQ-DEC-007
- **Resolution:** Approved architectural exception for narrow cross-module transaction (Premium update + Commission RESET → UNSET).
- **Target Architecture Documents:** D02 / D03

### Decision 2: Organizational Hierarchy Ownership
- **Source:** Phase 5 → REQ-DEC-006 / REQ-DEC-007 / REQ-DEC-008
- **Resolution:** Dedicated Organization/Hierarchy capability established. Identity remains restricted to RBAC/Auth.
- **Target Architecture Documents:** D02 / D06

### Decision 3: Reporting Ownership
- **Source:** Phase 5 → REQ-DEC-007 / REQ-DEC-008
- **Resolution:** Reporting owns analytics/read models. Policy/Commission provide governed events/contracts.
- **Target Architecture Documents:** D02 / D03 / D05 / D04
