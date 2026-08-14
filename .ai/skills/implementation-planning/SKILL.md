---
name: implementation-planning
description: Convert an approved technical design into a concrete, traceable, file-level implementation plan without modifying source code.
---

# Implementation Planning

## Purpose

Create a complete implementation plan from approved:

1. Business requirements
2. Architecture decisions
3. Technical design
4. API contracts
5. Persistence design
6. Event design
7. Security design

This skill MUST NOT implement the plan.

## Core Principle

The implementation plan answers:

    WHAT must be changed, WHERE, IN WHAT ORDER, and WHY?

It MUST NOT silently decide:

    SHOULD we change the architecture or requirements?

Any missing requirement, architecture decision, or technical decision MUST be surfaced as a blocker.

---

# 1. Required Inputs

Before creating an implementation plan, inspect:

1. Repository structure.
2. Existing source code.
3. Existing tests.
4. Existing dependencies.
5. Existing configuration.
6. Requirements.
7. Architecture documents.
8. Technical design.
9. API contract design.
10. Persistence design.
11. Event design.
12. Security design.
13. Existing coding conventions.
14. Existing migration conventions.
15. Existing AI rules and skills.

The planner MUST distinguish:

    EXISTS TODAY

from:

    PLANNED / DOCUMENTED

Never assume documented components already exist.

---

# 2. Repository Reality Check

Document:

- existing modules
- existing packages
- existing application layers
- existing persistence
- existing controllers
- existing configuration
- existing dependencies
- existing tests
- existing infrastructure
- existing migrations

For every major implementation area classify it as:

- EXISTING
- MODIFY
- CREATE
- REMOVE
- NOT APPLICABLE

Do not claim an existing implementation merely because documentation describes it.

---

# 3. Requirement Traceability

Every implementation task MUST map to one or more requirements.

Use:

    Requirement
        ↓
    Architecture
        ↓
    Technical Design
        ↓
    Implementation Task
        ↓
    Verification

Example:

    REQ-DEC-005
        ↓
    Commission Architecture
        ↓
    Commission Technical Design
        ↓
    Commission aggregate + activation validation
        ↓
    Unit + integration tests

Tasks without architectural or requirement justification MUST be identified.

---

# 4. Architecture Boundary Verification

Before generating tasks, verify:

## Modules

Only approved modules may be created.

For Phase 5:

- identity
- customer
- product
- policy
- commission
- notification
- reporting
- organization

Do NOT create:

- document
- orchestration
- agent
- subagent
- dealer
- admin
- other unresolved top-level modules

unless a later authoritative decision explicitly approves them.

## Dependencies

Every cross-module dependency MUST identify:

- source module
- target module
- public contract/event
- reason
- direction

No dependency may bypass module boundaries.

## Persistence

Verify:

- module-owned schemas
- UUID identifiers
- no cross-module physical FKs
- transaction placement
- Flyway rules
- schema ownership

## APIs

Verify:

- controller ownership
- API version
- DTO/application/domain separation
- error semantics
- authorization boundary

## Events

Verify:

- producer
- consumer
- event mechanism
- transaction semantics
- idempotency
- ordering behavior

---

# 5. Implementation Sequencing

Tasks MUST be ordered according to dependency.

Use a sequence such as:

### Stage 0 — Dependencies / Foundation

Only dependencies explicitly required by approved architecture.

### Stage 1 — Organization

Implement authoritative organizational hierarchy and scope resolution.

### Stage 2 — Domain Models

Implement Policy and Commission domain behavior.

### Stage 3 — Application Services

Implement use cases and approved transaction boundaries.

### Stage 4 — Persistence

Implement schemas, migrations, mappings, repositories.

### Stage 5 — Authorization

Integrate OrganizationScope with resource authorization.

### Stage 6 — APIs

Implement controllers, DTOs, mappers, error handling, OpenAPI.

### Stage 7 — Document Storage

Implement provider-neutral storage port and Cloudflare R2 adapter.

### Stage 8 — Events

Implement Spring Modulith events and publication.

### Stage 9 — Reporting

Implement event consumers and read models.

### Stage 10 — Frontend

Implement UI behavior against approved APIs.

### Stage 11 — Verification

Run unit, integration, architecture, API, persistence, event, authorization, and frontend tests.

The exact sequence may differ if repository discovery reveals dependencies requiring another order.

---

# 6. File-Level Task Design

Every implementation task MUST identify:

- Task ID
- Stage
- Requirement
- Architecture authority
- Technical design section
- Module
- Layer
- File path
- Action: CREATE / MODIFY / DELETE
- Purpose
- Dependencies
- Acceptance criteria
- Tests

Example:

    Task: P5-POL-APP-001
    Action: CREATE
    File:
      backend/src/main/java/com/anverraglobal/policy/application/PolicyManagementApplicationService.java

    Purpose:
      Implement Policy application use cases and the approved
      Premium Update + Commission RESET -> UNSET transaction.

    Requirements:
      REQ-DEC-005

    Architecture:
      D02 / D03 / D16

    Acceptance:
      - Policy premium updates atomically.
      - Commission is reset to UNSET.
      - Rollback affects both operations.
      - No distributed transaction is introduced.
      - No orchestration module exists.

---

# 7. Dependency Plan

For every dependency change specify:

- dependency name
- reason
- owning module
- architecture authority
- whether required or optional
- expected configuration impact

For Phase 5:

Required:

- springdoc-openapi-starter-webmvc-ui
- AWS SDK S3 client for Cloudflare R2 compatibility

Prohibited unless separately approved:

- Kafka
- RabbitMQ
- AWS SQS

Do not add dependencies merely because they are convenient.

---

# 8. Persistence Implementation Plan

Define:

- migration files
- migration ordering
- schema creation
- tables
- indexes
- constraints
- Spring Data JDBC mappings
- repositories

Follow D03:

- sequential numeric Flyway versions
- UUID identifiers
- no BIGSERIAL
- no cross-schema physical FKs
- no persistence annotations in domain
- transactions in application layer

Do not invent indexes unless justified by actual lookup/query requirements.

---

# 9. Event Implementation Plan

For every event define:

- event name
- producer
- consumer
- triggering operation
- payload
- aggregate identifier
- aggregate version
- occurrence timestamp
- idempotency key
- ordering behavior

Reporting MUST handle out-of-order events.

Do not create events that have no requirement or consumer.

Do not add an external broker.

---

# 10. API Implementation Plan

For every endpoint define:

- HTTP method
- URI
- controller
- request DTO
- application command/query
- mapper
- application service
- response DTO
- authorization requirement
- error responses
- tests

Controllers MUST contain no business logic.

For Phase 5:

    /api/v1/policies
    /api/v1/policies/{id}
    /api/v1/policies/resolve
    /api/v1/policies/{id}/lifecycle/*
    /api/v1/policies/{id}/document
    /api/v1/reporting/policies/statistics
    /api/v1/reporting/commissions/statistics

Do not invent additional endpoints.

---

# 11. Authorization Implementation Plan

For every protected operation identify:

- authenticated principal
- OrganizationScope resolution
- resource scope
- authorization check
- repository filtering
- existence concealment behavior
- expected HTTP status

JWT MUST NOT become the authoritative organization hierarchy.

Reporting MUST NOT independently resolve organizational hierarchy.

---

# 12. Document Storage Plan

Implement only inside Policy.

Expected structure:

    policy/
      port/
        outbound/
          DocumentStoragePort
      adapter/
        outbound/
          CloudflareR2DocumentStorageAdapter

Do NOT create:

    document/

Do not leak Cloudflare-specific concepts into domain code.

The adapter is responsible for provider-specific signing and storage operations.

---

# 13. Testing Plan

Every implementation area MUST have corresponding tests.

## Domain Tests

Verify:

- lifecycle invariants
- immutable identity
- commission rules
- UNSET vs ZERO
- activation matrix

## Application Tests

Verify:

- use-case orchestration
- authorization context
- transaction boundaries
- Premium + Commission atomicity

## Persistence Tests

Verify:

- schema mappings
- repository queries
- constraints
- migrations

## API Tests

Verify:

- status codes
- ProblemDetail
- validation
- authorization
- existence concealment
- pagination
- concurrency conflicts

## Event Tests

Verify:

- publication
- transactional consistency
- listener execution
- idempotency
- out-of-order handling

## Architecture Tests

Verify:

- approved module inventory
- dependency direction
- no document module
- no orchestration module
- no cross-schema FKs
- domain purity

---

# 14. Acceptance Criteria

The plan MUST define a measurable Definition of Done.

Phase 5 is complete only when:

- all approved requirements are implemented
- all approved architecture boundaries are preserved
- all planned APIs are implemented
- persistence is migrated
- authorization is enforced
- document storage works
- events are durable
- reporting read models work
- out-of-order events are handled
- tests pass
- architecture tests pass
- OpenAPI generation succeeds
- no prohibited modules/dependencies exist

---

# 15. Risk Register

Identify implementation risks including:

- module dependency violations
- transaction rollback inconsistencies
- authorization scope leakage
- duplicate policy race conditions
- event ordering
- duplicate event delivery
- R2 upload/download failures
- reporting eventual consistency
- migration ordering
- API contract drift

Each risk MUST have:

- impact
- likelihood
- mitigation
- verification

---

# 16. Change Boundary

The implementation plan MUST clearly identify:

## Files to CREATE

## Files to MODIFY

## Files to DELETE

## Dependencies to ADD

## Configuration to MODIFY

## Database Changes

## Tests to CREATE/MODIFY

No unspecified file may be modified during implementation.

If implementation discovers an additional required change, STOP and request approval when it changes:

- architecture
- requirements
- module boundaries
- public API contracts
- persistence ownership
- security model
- event architecture
- dependencies outside the approved plan

---

# 17. Human Approval Gate

The implementation plan MUST end with:

    IMPLEMENTATION AUTHORIZED: NO

The plan becomes executable only after explicit human approval.

Approval must be explicit and unambiguous.

Examples:

    "Approved. Proceed with implementation."

or:

    "Implementation is authorized."

A technical design being complete does NOT constitute implementation authorization.

---

# 18. Completion Rule

The implementation plan is complete only when an engineer or AI coding agent can execute it without making architectural or business decisions.

If the implementer must decide:

- module ownership
- business rules
- API semantics
- persistence ownership
- event contracts
- authorization behavior

then the plan is incomplete.

The implementation plan MUST NOT start implementation automatically.

## Output Structure

Produce:

1. Executive Summary
2. Repository Reality Check
3. Requirement Traceability
4. Architecture Boundary Verification
5. Implementation Sequence
6. Detailed Task Register
7. File Change Matrix
8. Dependency Plan
9. Persistence Plan
10. API Plan
11. Event Plan
12. Authorization Plan
13. Document Storage Plan
14. Frontend Plan
15. Testing Plan
16. Risk Register
17. Definition of Done
18. Open Decisions / Blockers
19. Final Safety Gate

Final status MUST be one of:

- READY FOR HUMAN APPROVAL
- BLOCKED — REQUIREMENT
- BLOCKED — ARCHITECTURE
- BLOCKED — TECHNICAL DESIGN
