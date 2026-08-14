---
description: Create an implementation-ready technical design from approved requirements and architecture without modifying application code.
---

# Technical Design Workflow

## Purpose

Transform approved requirements and architecture into an implementation-ready technical design.

This workflow MUST NOT implement application code.

It MUST NOT modify:

- Java/TypeScript source
- tests
- dependencies
- database migrations
- application configuration
- infrastructure

Documentation changes are permitted only when explicitly authorized by the calling workflow.

---

## Required Skills

Use:

    .ai/skills/repository-discovery/SKILL.md
    .ai/skills/requirement-analysis/SKILL.md
    .ai/skills/architecture-analysis/SKILL.md
    .ai/skills/technical-design/SKILL.md

---

# 1. Preconditions

Before technical design begins, verify:

- repository discovery is complete
- requirements are resolved
- architecture review has passed
- required human architecture decisions are approved
- no authoritative contradictions remain

If any prerequisite fails:

    STOP

Do not invent a resolution.

---

# 2. Repository Reality

Inspect the current implementation before designing changes.

Explicitly separate:

## Exists Today

Verified repository facts.

## Planned

Approved design components that do not yet exist.

Never treat planned components as existing implementation.

---

# 3. Requirements Baseline

Load the authoritative requirements.

For every requirement identify:

- source
- business rule
- affected capability
- affected module
- expected behavior

Do not alter business meaning.

---

# 4. Architecture Baseline

Load:

- Engineering Constitution
- Module Architecture
- Persistence Architecture
- API Architecture
- Event Architecture
- Security Architecture
- approved architecture decisions

Verify that the technical design stays within these boundaries.

---

# 5. Technical Design Areas

The design MUST cover the applicable areas:

## Domain

- aggregates
- entities
- value objects
- invariants
- lifecycle
- domain services

## Application

- commands
- queries
- application services
- transactions
- cross-module collaboration

## Persistence

- schemas
- tables
- mappings
- repositories
- migrations
- constraints

## API

- controllers
- DTOs
- commands
- response models
- errors
- authorization

## Events

- producers
- consumers
- payloads
- publication
- transaction semantics
- idempotency
- ordering

## Security

- authentication
- RBAC
- organization scope
- resource authorization
- existence concealment

## External Integrations

- ports
- adapters
- provider boundaries
- failure handling

## Frontend

- user flows
- state management
- validation
- API integration
- UX constraints

---

# 6. Module Boundary Rule

Only approved top-level modules may be designed.

For the current architecture:

    identity
    customer
    product
    policy
    commission
    notification
    reporting
    organization

Do NOT introduce:

    document
    orchestration
    agent
    subagent
    dealer
    admin

as new top-level business modules unless a later authoritative decision explicitly approves them.

Infrastructure capabilities MUST live inside the owning module.

---

# 7. Cross-Module Transaction Rule

Any cross-module transaction MUST:

1. Have explicit architecture authorization.
2. Identify the owning application service.
3. Identify the called public contract.
4. Define rollback semantics.
5. Explain why eventual consistency is insufficient.

Do not create a generic orchestration module.

Do not introduce distributed transactions or sagas unless explicitly approved.

---

# 8. Persistence Rules

Follow D03.

Verify:

- module-owned schemas
- UUID identifiers
- sequential Flyway migrations
- no cross-schema physical FKs
- logical external references where required
- persistence mappings outside domain
- transactions in application layer

Do not invent indexes without query justification.

---

# 9. API Rules

Follow D04.

Verify:

- controller ownership
- API versioning
- DTO/application/domain separation
- ProblemDetail errors
- authorization delegation
- Reporting API ownership
- no operational analytics endpoints

Concrete API designs MUST be explicitly classified as:

- AUTHORITATIVE
- APPROVED DESIGN DECISION
- DESIGN PROPOSAL
- DEFERRED

Do not silently turn proposals into requirements.

---

# 10. Event Rules

Follow D05.

For every event specify:

- producer
- consumer
- purpose
- payload
- aggregate ID
- aggregate version
- occurrence timestamp
- idempotency
- ordering behavior

Do not invent events without a consumer or requirement.

Do not introduce Kafka/RabbitMQ/SQS unless separately approved.

---

# 11. Authorization Rules

Follow D06.

Explicitly model:

    Principal
        ↓
    Identity / RBAC
        ↓
    OrganizationScope
        ↓
    Resource Authorization
        ↓
    Repository Query

JWT MUST NOT become the authoritative organizational hierarchy.

Reporting MUST NOT independently resolve hierarchy.

---

# 12. External Storage Rule

Provider-specific infrastructure MUST remain behind an outbound port.

For Policy-owned documents:

    policy.port.outbound.DocumentStoragePort
                ↓
    policy.adapter.outbound.<provider>DocumentStorageAdapter

Do NOT create a top-level document module.

Do not expose provider-specific concepts in the domain.

---

# 13. Technical Decision Classification

Every significant design choice MUST be classified:

- AUTHORITATIVE
- APPROVED DECISION
- REQUIRED CONSEQUENCE
- DESIGN DECISION
- DESIGN PROPOSAL
- INFERRED
- DEFERRED
- SUPERSEDED
- UNRESOLVED

If implementation depends on an unresolved decision:

    STOP

Request human approval.

---

# 14. Implementation Readiness

The technical design is implementation-ready only when an engineer can determine:

- what to build
- where to build it
- how modules interact
- how persistence works
- how APIs behave
- how events behave
- how authorization works
- how failures behave
- how it will be tested

without making a business or architectural decision.

---

# 15. Required Output

Produce:

1. Executive Summary
2. Repository Reality
3. Requirements Traceability
4. Module Design
5. Domain Design
6. Application Design
7. Persistence Design
8. API Design
9. Event Design
10. Authorization Design
11. External Integration Design
12. Frontend Design
13. Error Handling
14. Testing Strategy
15. Technical Decisions
16. Open Decisions
17. Implementation Boundaries
18. Final Readiness Gate

---

# 16. Final Safety Gate

The technical-design workflow MUST end with exactly one of:

    TECHNICAL DESIGN COMPLETE — READY FOR IMPLEMENTATION PLANNING

    TECHNICAL DESIGN BLOCKED — HUMAN DECISION REQUIRED

    TECHNICAL DESIGN BLOCKED — ARCHITECTURE CONFLICT

    TECHNICAL DESIGN BLOCKED — REQUIREMENT CONFLICT

    TECHNICAL DESIGN BLOCKED — INFORMATION MISSING

Technical design completion MUST NOT authorize implementation.

Implementation requires a separate implementation plan and explicit human authorization.
