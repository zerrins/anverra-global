# Customer Management Module Blueprint

**Document ID:** AEOS-P02-S02-D02  
**Version:** 1.0  
**Status:** Baseline  
**Phase:** 2 — Application & Repository Blueprint  
**Stage:** 2 — Business Module Blueprints  
**System:** AnverraGlobal

---

# 1. Module Identity
**Canonical Module Name:** `customer`

# 2. Business Capability
**Capability Name:** Customer Management

# 3. Module Category
**Category:** Core Business

# 4. Purpose
The purpose of the Customer Management module is to manage authoritative insurance-customer business information. It serves as the primary source of truth for customer profiles and demographics across the backend.

# 5. Scope
The scope is strictly bounded to the customer business entity, encompassing profile management and customer business data ownership. It explicitly excludes system identity, authentication, access control, and unresolved intermediaries.

# 6. Responsibilities
- Customer profile management
- Customer business data ownership

# 7. Non-Responsibilities
- **System Identity/Access:** **Identity ≠ Customer.** Identity owns the system actor identity, authentication, authorization, and roles. Customer owns insurance customer business information. Customer MUST NOT own passwords, credentials, login/session state, roles, or authentication mechanisms. We do not duplicate identity data unnecessarily.
- **Intermediaries/Organizations:** Agent, Dealer, Partner, and Organization models are explicitly unresolved. Customer must not create models or relationships for these capabilities (e.g., no `agentId` or `organizationId` concepts).
- **Proposal Management:** Prospect, Lead, Applicant, and Proposal workflows are unresolved. Customer must not introduce these domain concepts. The relationship between customer acquisition and Proposal Management is unresolved.
- **Document & KYC Management:** Gathering and verifying identity documents and KYC workflows remain unresolved. Customer must not implement KYC state machines or document ownership.
- **Policy/Product/Commission/Reporting:** Customer does not own insurance products, policy lifecycles, commissions, or analytical reporting.

# 8. Ownership
Customer owns authoritative insurance-customer business information. It is the definitive source of truth for the customer business entity within the system.

# 9. Authoritative Business Rules
- **Rule:** Customer data is the authoritative source for insurance-related client information across the backend.
- *Proposed / Requires Confirmation:* The customer information required before participation in a policy lifecycle has not yet been established. (No mandatory customer fields are invented).

# 10. Domain Model
Established concepts:
- Customer

Candidate concepts (Proposed / Requires Confirmation):
- CustomerProfile
- ContactMethod
- Address
- CustomerId
- EmailAddress
- PhoneNumber
- PostalAddress
- Customer Status (Lifecycle semantics require business clarification. Do not invent states like ACTIVE, PROSPECT, or LEAD).

# 11. Aggregates
Requires clarification. We do not automatically declare `CustomerAggregate` or assume Customer is an aggregate root. The conceptual aggregate boundary and root status require confirmation during detailed domain modeling. We do not invent aggregate boundaries.

# 12. Entities
Proposed / Requires Confirmation: Candidate concepts such as `CustomerProfile` and `ContactMethod` may become entities, but their final representation requires domain modeling. We do not automatically turn candidate concepts into entities.

# 13. Value Objects
Proposed / Requires Confirmation: Candidate concepts such as `CustomerId`, `Address`, `EmailAddress`, `PhoneNumber`, and `PostalAddress` may become value objects, but their final representation requires domain modeling.

# 14. Domain Rules / Invariants
Requires clarification. Lifecycle invariants for the Customer business entity must be identified during modeling. No mandatory customer fields are established yet.

# 15. Domain Services
Requires clarification. Included only if cross-aggregate or cross-entity logic is conceptually justified during implementation modeling.

# 16. Application Use Cases
Proposed / Requires Confirmation:
- Register Customer
- View Customer Profile
- Update Customer Profile
- Deactivate Customer
- Update Contact Methods

# 17. Commands
Proposed / Requires Confirmation (Conceptual only):
- `RegisterCustomer`
- `UpdateCustomerProfile`
- `DeactivateCustomer`

# 18. Queries
Proposed / Requires Confirmation (Conceptual only):
- `GetCustomerProfile`
- `SearchCustomers`

# 19. Validation Ownership
Customer owns the validation of customer profile integrity based on its internal domain rules.

# 20. Public Module Contracts
Candidate conceptual contracts (Proposed / Requires Confirmation):
- **Customer Profile Contract:** Allows other modules to query authoritative business details of a Customer.
- **Customer Status Contract:** Candidate conceptual contract for exposing authoritative Customer lifecycle/status information, if such a status concept is established during subsequent domain modeling. Exact lifecycle states and their business meanings remain unresolved.
*We do not assume Policy, Commission, or Reporting consume these contracts yet.*

# 21. Consumed Module Contracts
Proposed / Requires Confirmation: Customer may conceptually require an Authorization Context Contract from Identity to enforce access rules. Any consumed contract must be explicitly justified and confirmed.

# 22. Published Integration Events
Candidate integration events (Proposed / Requires Confirmation):
- `CustomerRegistered` (Candidate business fact representing creation/registration of a Customer record. It does not imply KYC is completed, verified, policy-ready, or insurance eligible).
- `CustomerProfileUpdated` (Candidate business fact).
- `CustomerDeactivated` (Candidate business fact. Exact lifecycle semantics require confirmation).
*We do not establish consumers yet.*

# 23. Consumed Integration Events
Requires clarification. No consumed integration events are currently established.

# 24. Module Dependencies
No outbound module dependency is currently established. Potential collaboration with other modules must be established by those module blueprints. Potential Identity interaction is Proposed / Requires Confirmation. We do not invent a dependency graph.

# 25. Data Ownership
Customer owns authoritative customer business data conceptually. Persistence details (tables, columns, foreign keys, indexes, schemas, ORM mappings, migrations, database technology) are explicitly deferred.

# 26. Persistence Responsibility
Customer is responsible for persistence of its authoritative customer business information. The exact domain objects that participate in persistence remain subject to domain modeling. Database technology, schemas, ORM mappings, and physical persistence design are deferred.

# 27. External Integrations
Deferred / Requires Confirmation: External CRMs, KYC providers, address validation services, or other external integrations are unconfirmed. No vendors or protocols are defined.

# 28. Security / Authorization Responsibilities
Identity determines authorization. Customer performs the authorized Customer operation and enforces Customer-specific business rules. Customer must NOT become a second authorization engine. JWT, OAuth/OIDC implementation, sessions, middleware, and permission taxonomies are explicitly deferred.

# 29. Error / Failure Responsibility
Proposed / Requires Confirmation (Conceptual failures only):
- Customer Not Found
- Invalid Customer Data
- Authorization Denied — an authorization decision determined by the Identity capability prevents the requested Customer operation.

# 30. Testing Expectations
Conceptual testing expectations include verification of Customer invariants and profile lifecycle behavior.

# 31. Observability Expectations
Conceptual observability expectations include tracking major profile lifecycle events. PII must not be logged.

# 32. Documentation Expectations
Internal module documentation must document finalized domain concepts, business rules, conceptual contracts, and relevant module decisions. Database schemas and implementation-specific artifacts are documented in their designated later phase.

# 33. AI Implementation Guidance
Future coding AI MUST:
- read this blueprint before modifying Customer
- preserve Identity ≠ Customer
- never place authentication credentials in Customer
- never create Agent/Dealer/Partner/Organization models
- never create Prospect/Lead models
- never implement Proposal logic
- never implement KYC/Document logic
- preserve module encapsulation
- avoid cross-module persistence
- avoid inventing mandatory customer fields
- avoid inventing aggregate boundaries
- avoid converting candidate concepts into implementation structures without approved design
- avoid technology decisions before the designated later phase

# 34. Deferred Decisions
Decisions regarding the following are deferred to a designated later phase:
- Unresolved capabilities (Agent, Dealer, Partner, Organization, Proposal, Document/KYC, Administration)
- Database technology, schemas, and ORM
- Programming-language interfaces, REST/GraphQL/gRPC endpoints, serialization
- Specific mandatory customer fields
- External CRM/KYC integrations

# 35. Definition of Done
The module blueprint is complete when:
- [x] Ownership is explicit.
- [x] Non-ownership is explicit.
- [x] Identity/Customer separation is explicit.
- [x] Established vs candidate concepts are distinguished.
- [x] Aggregate boundaries are not invented.
- [x] Business rules are identified without invented mandatory fields.
- [x] Use cases are identified with status.
- [x] Contracts are conceptual.
- [x] Events are candidate and appropriately qualified.
- [x] Dependencies are not prematurely established.
- [x] Data ownership is explicit.
- [x] Unresolved capabilities are preserved.
- [x] Technology decisions are deferred.
- [x] AI constraints are explicit.
- [x] Traceability is complete.

# 36. Traceability
This document traces to:
- Stage 2 Overview (AEOS-P02-S02-D00)
- Identity & Access Blueprint (AEOS-P02-S02-D01)
- System Blueprint (AEOS-P02-S01-D01)
- Repository Architecture (AEOS-P02-S01-D02)
- Application Boundaries (AEOS-P02-S01-D03)
- Architectural Boundaries (AEOS-P02-S01-D04)
- Blueprint Traceability (AEOS-P02-S01-D05)
- Relevant Phase 1 constitutional documents
