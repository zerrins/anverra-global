# Phase 2 — Stage 2: Business Module Blueprints Overview

**Document ID:** AEOS-P02-S02-D00  
**Version:** 1.0  
**Status:** Baseline  
**Phase:** 2 — Application & Repository Blueprint  
**Stage:** 2 — Business Module Blueprints  
**System:** AnverraGlobal

---

# 1. Purpose

This document serves as the control center, index, and governance definition for Phase 2, Stage 2: Business Module Blueprints. It establishes the canonical structure, shared authoring rules, and tracking mechanism for all subsequent individual module blueprints.

# 2. Stage 2 Objective

The objective of Stage 2 is to determine exactly how each approved business capability is modeled, bounded, and organized inside the Backend Modular Monolith before implementation begins. It establishes the blueprint for each backend business module at a conceptual, domain, and architectural level.

# 3. Relationship to Stage 1

Stage 1 established the system-level architecture, repository layout, application boundaries, and internal architectural boundaries. Stage 2 builds upon this by defining the actual business modules that will reside within those boundaries.

This document and subsequent blueprints trace directly back to:
- **System Blueprint** (AEOS-P02-S01-D01)
- **Repository Architecture** (AEOS-P02-S01-D02)
- **Application Boundaries** (AEOS-P02-S01-D03)
- **Architectural Boundaries** (AEOS-P02-S01-D04)
- **Blueprint Traceability** (AEOS-P02-S01-D05)
- **Engineering Constitution** (Phase 1)

# 4. Approved Module Inventory

Based on Stage 1 analysis, the following seven backend modules are established and approved for Stage 2 blueprinting:

1. Identity
2. Customer
3. Product
4. Policy
5. Commission
6. Notification
7. Reporting

# 5. Unresolved Capability Register

The following business capabilities remain unresolved. **No blueprints shall be created for these capabilities during this stage.**

- Agent Management
- Dealer Management
- Partner Management
- Proposal Management
- Document & KYC Management
- Administration

# 6. Platform vs Business Module Distinction

"Platform" represents two distinct concepts:
1. **Platform Business Capabilities (Modules):** (e.g., Identity). These are cohesive capability modules providing foundational services. They reside in `backend/src/modules/` and own business domains.
2. **Shared Technical Infrastructure (`platform/` directory):** Resides in `backend/src/platform/`. Contains cross-cutting technical utilities. It is NOT a business module and contains no business logic.

Stage 2 will only blueprint business capability modules (including Platform modules like Identity).

# 7. Canonical Module Blueprint Structure

Every module blueprint must strictly adhere to the following 36-section canonical structure. If a section is not applicable to a specific module, the blueprint must explicitly state: "Not applicable".

1. Module Identity
2. Business Capability
3. Module Category
4. Purpose
5. Scope
6. Responsibilities
7. Non-Responsibilities
8. Ownership
9. Authoritative Business Rules
10. Domain Model
11. Aggregates
12. Entities
13. Value Objects
14. Domain Rules / Invariants
15. Domain Services
16. Application Use Cases
17. Commands
18. Queries
19. Validation Ownership
20. Public Module Contracts
21. Consumed Module Contracts
22. Published Integration Events
23. Consumed Integration Events
24. Module Dependencies
25. Data Ownership
26. Persistence Responsibility
27. External Integrations
28. Security / Authorization Responsibilities
29. Error / Failure Responsibility
30. Testing Expectations
31. Observability Expectations
32. Documentation Expectations
33. AI Implementation Guidance
34. Deferred Decisions
35. Definition of Done
36. Traceability

# 8. Shared Module Authoring Rules

- **Backend is the Modular Monolith:** Modules represent cohesive business capabilities inside a single deployable backend application.
- **Internal implementation remains private:** A module's domain, application logic, and infrastructure details are completely encapsulated.
- **Technology decisions remain deferred:** Blueprints must remain technology and implementation independent.

# 9. Business Capability Ownership Rules

- **Exactly one owner per business capability.**
- Modules explicitly declare both what they own (Responsibilities) and what they do not own (Non-Responsibilities).

# 10. Domain Modeling Rules

- Domain remains independent of frameworks and external technical infrastructure.
- Model at the conceptual level (Aggregates, Entities, Value Objects) without designing database schemas or concrete classes.

# 11. Business Rule Ownership Rules

- Business rules remain strictly with their owning module.
- The **Authoritative Business Rules** section must capture the Rule, Owning module, Affected aggregate, Trigger, and Expected business outcome.

# 12. Module Dependency Rules

- **Module dependencies shall be derived and justified during individual module blueprint authoring from business responsibilities, ownership, use cases, and required collaboration.**
- Dependencies must be intentional and justified.
- Circular module dependencies are strictly prohibited.

# 13. Contract Rules

- Modules expose explicit contracts.
- Use technology-neutral terminology (e.g., "Policy Query Contract" instead of `IPolicyQueryService`).
- Do not define programming-language interfaces, HTTP endpoints, or specific API payloads.

# 14. Integration Event Rules

- Modules may publish approved integration events to allow downstream modules to react asynchronously.
- Events must be documented conceptually.

# 15. Data Ownership Rules

- **No cross-module database access.**
- Each module is responsible for the persistence of its own authoritative data.

# 16. Security / Authorization Boundary

- Security and authorization requirements may be defined conceptually (e.g., "Action requires appropriate authorization").
- Do not establish a complete RBAC permission taxonomy unless supported by an authoritative source.
- Do not define JWT/OIDC/session implementation.

# 17. External Integration Boundary

- External integrations are documented conceptually (e.g., "Integrates with External Communication Provider").
- Do not specify vendor SDKs or exact integration protocols.

# 18. AI Implementation Guidance

Future AI implementation requires an approved module blueprint. Before implementation, AI must understand:
- ownership
- non-ownership
- authoritative rules
- domain concepts
- use cases
- contracts
- events
- dependencies
- data ownership
- prohibited dependencies
- unresolved blockers

AI must not invent unresolved capabilities or implementation technologies.

# 19. Stage 2 vs Later-Phase Boundary

Stage 2 remains at the Business/Domain/Architectural blueprint level. 
The following are deferred to a **designated later phase**:
- programming language
- framework
- database
- ORM
- API protocol
- HTTP endpoints
- URL structures
- serialization
- vendor SDKs
- deployment implementation
- CI/CD implementation

# 20. Proposed Module Authoring Order

This sequence is a PROPOSED authoring sequence, not a pre-approved dependency graph:

1. **Identity:** Identity & Access is a foundational capability whose boundaries should be established early because identity and access-control concerns may affect multiple modules.
2. **Customer**
3. **Product**
4. **Policy:** Policy defines the policy lifecycle and may require collaboration with Customer and Product capabilities where required by authoritative business responsibilities. Exact dependencies will be established in the Policy blueprint.
5. **Commission:** Commission handles commission-related business responsibilities associated with policy lifecycle information. Exact dependency and collaboration requirements will be established in the Commission blueprint.
6. **Notification**
7. **Reporting**

# 21. Module Document Index

All module blueprints will be located in the `docs/repository-blueprint/02-business-modules/` directory alongside this document.

# 22. Completion Tracking

| Module | Blueprint | Status |
|--------|-----------|--------|
| Identity | `01-identity-blueprint.md` | Pending |
| Customer | `02-customer-blueprint.md` | Pending |
| Product | `03-product-blueprint.md` | Pending |
| Policy | `04-policy-blueprint.md` | Pending |
| Commission | `05-commission-blueprint.md` | Pending |
| Notification | `06-notification-blueprint.md` | Pending |
| Reporting | `07-reporting-blueprint.md` | Pending |

# 23. Stage 2 Traceability

All blueprints trace back to the authoritative Phase 1 (Engineering Constitution) and Phase 2, Stage 1 documentation (AEOS-P02-S01-D01 through D05).

# 24. Stage 2 Exit Criteria

Stage 2 is considered complete when:
- All established modules have approved blueprints.
- Ownership and non-ownership are explicit.
- Authoritative business rules are identified.
- Domain concepts are documented.
- Use cases are documented.
- Dependencies are justified.
- Contracts are conceptually defined.
- Events are conceptually defined.
- Data ownership is explicit.
- Unresolved decisions are explicit.
- AI constraints are explicit.
- No technology implementation has been invented.
- Stage 2 traceability is complete.

# 25. Deferred Decisions

Decisions regarding technology stack, API design, database schema, physical architecture, and unresolved business capabilities (Agent, Dealer, Partner, Proposal, Document/KYC, Administration) are explicitly deferred to designated later phases.
