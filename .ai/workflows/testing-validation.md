---
description: Validate an implemented Phase 5 change against requirements, architecture, technical design, tests, runtime behavior, and repository safety without modifying implementation artifacts.
---

# Testing Validation Workflow

## 1. Purpose

Validate implementation after authorized coding.

This workflow is READ-ONLY.

It MUST NOT:

- modify source code
- modify tests
- modify dependencies
- modify migrations
- modify configuration
- fix implementation defects
- silently change requirements
- silently change architecture
- commit
- push
- reset
- restore unrelated changes

Its purpose is verification, not correction.

## 2. Required Preconditions

Before validation, verify:

1. Implementation was explicitly authorized.
2. The approved implementation plan exists.
3. Implementation has completed or reached a reviewable checkpoint.
4. The current Git state is understood.
5. The applicable requirements are available.
6. The applicable architecture is available.
7. The applicable technical design is available.
8. The API contract is available where relevant.
9. The testing-validation skill is available.

If these prerequisites are missing:

    BLOCKED — VERIFICATION FAILURE

## 3. Required Skill

Use:

    .ai/skills/testing-validation/SKILL.md

The skill is the primary validation authority.

Use relevant supporting skills when needed:

    .ai/skills/repository-discovery/SKILL.md
    .ai/skills/requirement-analysis/SKILL.md
    .ai/skills/architecture-analysis/SKILL.md
    .ai/skills/technical-design/SKILL.md

## 4. Repository Safety Check

Before running tests, inspect:

    git status
    git diff --stat
    git diff

Determine:

- what changed
- what files were created
- what files were deleted
- whether changes match the approved plan
- whether unrelated files changed

Unexpected changes MUST be reported.

Do not restore or delete them automatically.

## 5. Test Execution

Identify the project's actual build and test commands from repository configuration.

Do not invent commands when repository evidence provides the correct command.

Run appropriate:

- unit tests
- integration tests
- persistence tests
- API tests
- authorization tests
- event tests
- architecture tests

Capture failures exactly.

A failing test MUST NOT be ignored merely because the implementation appears correct.

## 6. Requirement Validation

Validate every implemented requirement against actual behavior.

At minimum verify:

### Policy

- immutable identity
- no physical deletion
- DRAFT / ACTIVE / INACTIVE only
- progressive updates
- lifecycle transitions

### Organization

- Branch Admin behavior
- Agent A rules
- regular Agent behavior
- Data Entry inherited scope
- OrganizationScope resolution

### Commission

- Fixed commission
- Percentage commission
- maximum 50%
- UNSET distinct from ZERO
- Agent allocation rules

### Activation Matrix

Verify:

    0 agents + UNSET    → allowed
    0 agents + ZERO     → allowed
    0 agents + POSITIVE → allowed

    1 agent + UNSET     → prohibited
    1 agent + ZERO      → allowed
    1 agent + POSITIVE  → allowed

    2 agents + UNSET    → prohibited
    2 agents + ZERO     → allowed
    2 agents + POSITIVE → allowed

## 7. Premium / Commission Atomicity

Verify:

    Premium Update
        +
    Commission RESET → UNSET

is executed by:

    policy.application.PolicyManagementApplicationService

Verify:

- both changes commit together
- failure rolls back both changes
- no partial state is committed
- no orchestration module exists
- no distributed saga exists

## 8. Persistence Validation

Verify D03 rules:

- PostgreSQL
- Spring Data JDBC
- module-owned schemas
- sequential numeric Flyway migrations
- UUID identifiers
- no BIGSERIAL
- no cross-schema physical FKs
- no persistence annotations in domain
- @Transactional only at appropriate application boundaries

Inspect actual migrations and database behavior where applicable.

## 9. API Validation

Verify:

    /api/v1

Verify:

- controller ownership
- DTO / Command / Domain separation
- zero business logic in controllers
- Policy endpoints
- lifecycle endpoints
- Policy resolution
- document endpoints
- Reporting statistics endpoints
- RFC 7807 ProblemDetail behavior

Verify status-code behavior against the approved API contract.

Do not invent new endpoint behavior during validation.

## 10. Authorization Validation

Test relevant scope boundaries:

- Customer
- Agent
- Branch Admin
- Dealer
- Data Entry

Verify:

- authenticated Principal is propagated correctly
- OrganizationScope is resolved by the application boundary
- filters do not expand authorization scope
- unauthorized resources are protected
- Reporting does not reconstruct hierarchy independently

## 11. Document Validation

Verify:

    policy.port.outbound.DocumentStoragePort

and Policy-owned adapter placement.

Verify:

- no document module
- no top-level document package
- 0..1 document relationship
- upload
- replacement
- removal
- signed URL download
- Policy authorization applies
- provider-specific logic remains outside domain

## 12. Event Validation

Verify Spring Modulith event behavior:

- events are persisted transactionally
- events appear only after successful producer transaction
- rollback prevents publication
- listeners execute asynchronously where specified
- consumers are idempotent
- duplicate events are safe
- out-of-order events are handled

Verify event payloads do not expose persistence entities.

## 13. Reporting Validation

Verify Reporting owns:

- read models
- Policy statistics
- Commission statistics
- analytical APIs

Verify:

    UNSET commission → excluded
    ZERO commission  → included
    POSITIVE commission → included

Verify Reporting:

- consumes approved events
- does not query operational module databases
- tolerates duplicate events
- tolerates out-of-order events
- does not independently resolve organizational hierarchy

## 14. Architecture Validation

Verify no unauthorized structure exists:

    document/
    orchestration/
    agent/
    subagent/
    dealer/
    admin/

Verify approved modules remain bounded.

Verify cross-module collaboration follows approved contracts/events.

Verify no physical cross-module foreign keys exist.

## 15. Dependency Validation

Compare actual dependencies with the approved implementation plan.

Expected:

    springdoc-openapi-starter-webmvc-ui
    software.amazon.awssdk:s3

Must remain absent unless separately approved:

    Kafka
    RabbitMQ
    AWS SQS

## 16. Test Coverage Review

Verify tests exist for critical invariants.

### Domain

- lifecycle
- commission rules
- activation matrix
- immutable identity

### Application

- authorization
- premium/commission atomicity
- cross-module contract behavior

### Persistence

- schema constraints
- UUID identifiers
- migrations
- optimistic locking where implemented

### API

- success paths
- malformed requests
- unauthorized access
- validation failures
- concurrency conflicts
- Policy resolution

### Events

- transactional publication
- duplicate delivery
- out-of-order delivery
- listener failures/retries

### Reporting

- UNSET exclusion
- ZERO inclusion
- projection updates

## 17. Failure Handling

When validation fails:

    STOP

Do not modify the implementation to make validation pass.

Classify the failure as:

- REQUIREMENT FAILURE
- ARCHITECTURE FAILURE
- TECHNICAL DESIGN FAILURE
- IMPLEMENTATION DEFECT
- TEST FAILURE
- VERIFICATION FAILURE
- MISSING EVIDENCE

Report:

- command
- failure
- evidence
- affected requirement
- affected file
- impact
- recommended next action

## 18. Final Validation Report

Produce:

### Validation Scope

What was validated.

### Repository State

Current Git state.

### Test Execution

Commands and results.

### Requirement Validation

Requirement-by-requirement results.

### Architecture Validation

Module and boundary results.

### Persistence Validation

Schema and migration results.

### API Validation

Endpoint and error results.

### Authorization Validation

Scope results.

### Event Validation

Publication and consumer results.

### Reporting Validation

Projection results.

### Dependency Validation

Dependency results.

### Findings

Every finding classified by severity and category.

### Final Safety Gate

Use exactly one final state:

    VALIDATED — READY FOR CODE REVIEW

or

    BLOCKED — VERIFICATION FAILURE

or

    BLOCKED — REQUIREMENT FAILURE

or

    BLOCKED — ARCHITECTURE FAILURE

or

    BLOCKED — TECHNICAL DESIGN FAILURE

or

    BLOCKED — IMPLEMENTATION DEFECT

or

    BLOCKED — TEST FAILURE

or

    BLOCKED — MISSING EVIDENCE

## 19. Completion Rule

Testing validation does not authorize:

- code changes
- dependency changes
- migration changes
- commits
- pushes
- releases

A successful validation only means the implementation is ready for the separate read-only code-review stage.
