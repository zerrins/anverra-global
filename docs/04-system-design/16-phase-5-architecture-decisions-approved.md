# Phase 5 Architecture Decisions — Approved

## Decision 1 — Policy / Commission Atomic Consistency

**Status:**
APPROVED

**Decision:**
Narrow cross-module transactional exception for Premium Update + Commission RESET → UNSET.

**Scope:**
Only this business invariant.

**Rationale:**
Strong consistency is required by the approved Phase 5 business rules.

**Constraints:**
No general cross-module transaction permission.

## Decision 2 — Organization / Hierarchy Ownership

**Status:**
APPROVED

**Decision:**
Dedicated Organization/Hierarchy capability becomes the authoritative owner of Dealer/Branch/Agent/Data Entry organizational relationships.

Identity remains responsible for identity, authentication and RBAC.

**Constraints:**
Do not encode the entire organizational hierarchy into JWT as the authoritative source.

## Decision 3 — Reporting Ownership

**Status:**
APPROVED

**Decision:**
Reporting owns Policy and Commission analytics/statistics and their read-model architecture.

Policy/Commission provide governed information to Reporting.

**Constraints:**
Exact event contracts and persistence remain D03/D05 technical-design work.
