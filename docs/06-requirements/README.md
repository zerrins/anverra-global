# Phase 5 Requirements Governance Area

## Purpose
This directory (`docs/06-requirements/`) is the authoritative Phase 5 requirements documentation area for AnverraGlobal. It is the controlled location for authoritative business/product requirements that will eventually drive functional implementation.

The directory distinguishes:
- authoritative requirements
- proposed / requires-confirmation requirements
- explicitly deferred requirements
- requirement decisions awaiting human approval
- traceability and readiness evidence

Architecture remains governed by Phase 1–4 documentation. Requirements documentation must NOT redefine architecture.

## Authority Rule
**Architecture documents define architectural constraints. They do NOT automatically constitute detailed business requirements.**

Only requirements explicitly approved by the appropriate business/product/domain owner may become authoritative functional requirements.

Engineering and AI agents may:
- discover evidence
- analyze evidence
- identify gaps
- draft requirement questions
- prepare requirement documents for review
- maintain traceability

Engineering and AI agents MUST NOT silently convert assumptions, industry conventions, blueprint examples, or implementation preferences into business requirements.

## Requirement Classification
Any discovered requirement or statement must be strictly classified into one of the following states:
- **A. AUTHORITATIVE:** The requirement is explicitly established by an approved business/product/requirements source. Only A. AUTHORITATIVE requirements may directly authorize functional implementation.
- **B. EXPLICITLY DEFERRED:** Addressed in a future phase or specific artifact.
- **C. PROPOSED / REQUIRES CONFIRMATION:** Candidate, conceptual, or proposed behavior.
- **D. IMPLEMENTATION-LEVEL:** Technical choices constrained by architecture.
- **E. UNKNOWN / UNSUPPORTED:** Absent from documentation.

Technical scaffolding may still be justified by existing architectural constraints or verified tooling requirements, consistent with the Phase 5 Requirements & Capability Definition Plan.

## Human Decision Register
The following identifiers represent requirements-resolution questions awaiting human/business approval. They are NOT architectural decisions (do not confuse with Phase 4 O1-O53 identifiers). No new architectural decisions (O54+) are created here.

### Resolved Decisions
- [REQ-DEC-001 (Identity Authentication Architecture)](decisions/REQ-DEC-001-identity.md)
- [REQ-DEC-002 (Customer Definition & MVP Lifecycle)](decisions/REQ-DEC-002-customer.md)
- [REQ-DEC-003 (Product Definition & MVP Lifecycle)](decisions/REQ-DEC-003-product.md)
- [REQ-DEC-004 (Policy)](decisions/REQ-DEC-004-policy.md)
- [REQ-DEC-005 (Commission)](decisions/REQ-DEC-005-commission.md)
- [REQ-DEC-006 (Cross-Module & Organizational Integration)](decisions/REQ-DEC-006-integration.md)
- [REQ-DEC-007 (API Requirements)](decisions/REQ-DEC-007-api.md)
- [REQ-DEC-008 (Web UX Requirements)](decisions/REQ-DEC-008-web-ux.md)
- `REQ-DEC-009` - Mobile UX Requirements
  - *Traceability Matrix: [Phase 5 Traceability](decisions/REQ-DEC-007-traceability.md)*
- [REQ-DEC-011 (Customer & Product Implementation Contract)](decisions/REQ-DEC-011-customer-product-contract.md)

### Pending Decisions

These pending decisions must be resolved by their respective owners before functional implementation proceeds.

## Current State
The current repository assessment has identified:
- **REQ-DEC-001 (Identity Authentication Architecture)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-002 (Customer Definition & MVP Lifecycle)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-003 (Product Definition & MVP Lifecycle)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-004 (Policy)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-005 (Commission)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-006 (Cross-Module & Organizational Integration)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-007 (API Requirements)** is AUTHORITATIVE and RESOLVED.
- **REQ-DEC-008 (Web UX Requirements)** is AUTHORITATIVE and RESOLVED.
- All other capabilities have **ZERO AUTHORITATIVE FUNCTIONAL REQUIREMENTS**.

Existing Phase 1–5 architecture remains authoritative for architecture. Existing Workstream 1 and Workstream 2 implementation foundations remain valid. Functional capability implementation remains gated by the availability of authoritative requirements. Identity & Access contract is frozen via REQ-DEC-010. Customer and Product implementation contracts are fully frozen and implementation-ready via REQ-DEC-011.

## Requirements Workflow
1. Discover existing evidence.
2. Classify evidence.
3. Identify genuine gaps.
4. Prepare requirement questions.
5. Obtain appropriate human/business/product approval.
6. Record approved requirements.
7. Derive domain/use-case requirements.
8. Derive only required API/persistence/event/security/UX artifacts.
9. Perform implementation-readiness review.
10. Implement only capabilities that pass the readiness gate.

## Traceability
Traceability must be maintained across the entire requirements lifecycle:
```text
Business requirement
    ↓
Capability
    ↓
Domain/use-case requirement
    ↓
API / Persistence / Event / Security / UX artifact
    ↓
Acceptance criteria
    ↓
Implementation
    ↓
Tests
```
Only create links between artifacts when the underlying requirement actually exists.

## Anti-Invention Rules
The following inventions are explicitly PROHIBITED without authoritative requirements:
- inventing business rules
- inventing domain entities
- inventing API endpoints
- inventing DTOs
- inventing database schemas
- inventing events
- inventing roles/permissions
- inventing authentication flows
- inventing UX flows
- inventing cross-module dependencies
- promoting Phase 2 proposed concepts into requirements
- treating existing code as authoritative business requirements
- creating requirements merely to unblock implementation
