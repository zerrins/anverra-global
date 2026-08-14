---
name: testing-validation
description: Validate an implemented change against its approved requirements, architecture, technical design, tests, and repository boundaries without modifying the implementation.
---

# Testing Validation

## Purpose

Validate implementation work after an authorized implementation task.

This skill is primarily read-only.

It MUST NOT silently modify application code, architecture, requirements, dependencies, migrations, APIs, or tests merely to make validation pass.

If a defect is discovered:

    REPORT IT

Do not silently repair it.

## 1. Required Inputs

Before validation, establish:

1. Approved implementation plan.
2. Relevant requirements.
3. Relevant architecture decisions.
4. Relevant technical design.
5. Relevant API/persistence/event/security contracts.
6. Implementation task completed.
7. Current repository state.
8. Changes made by the implementation.

If the baseline cannot be established:

    STOP

Report missing information.

## 2. Validation Scope

Validate only the implementation covered by the approved task and its dependencies.

Check:

- functional behavior
- business invariants
- architecture boundaries
- persistence boundaries
- API behavior
- authorization
- events
- reporting
- dependency changes
- tests
- prohibited structures

Do not expand validation into unrelated refactoring.

## 3. Evidence Rules

Every validation conclusion MUST be based on repository evidence.

Acceptable evidence includes:

- source code
- tests
- build output
- test output
- dependency configuration
- migration files
- API definitions
- architecture tests
- Git diff
- repository structure

Distinguish:

    VERIFIED
    INFERRED
    NOT VERIFIED
    BLOCKED

Never present an inference as verified behavior.

## 4. Build Validation

Run the appropriate build and compilation checks defined by the implementation plan.

Verify:

- compilation succeeds
- relevant modules compile
- dependency resolution succeeds
- no unauthorized dependency was introduced

If the build fails:

    BLOCKED — VERIFICATION FAILURE

Do not modify implementation code to hide the failure.

## 5. Unit Test Validation

Verify relevant domain and application tests.

For Phase 5, validate where applicable:

### Commission

- Fixed commission
- Percentage commission
- maximum 50%
- UNSET
- ZERO
- positive commission
- Agent A/B allocation

### Activation

- 0 agents + UNSET → allowed
- 0 agents + ZERO → allowed
- 0 agents + POSITIVE → allowed
- 1 agent + UNSET → prohibited
- 1 agent + ZERO → allowed
- 1 agent + POSITIVE → allowed
- 2 agents + UNSET → prohibited
- 2 agents + ZERO → allowed
- 2 agents + POSITIVE → allowed

### Policy

- immutable identity
- DRAFT / ACTIVE / INACTIVE only
- no physical deletion
- valid lifecycle transitions

## 6. Transaction Validation

Validate the approved Premium update transaction:

    Premium Update
          +
    Commission RESET → UNSET

Verify:

- both operations occur within the approved application boundary
- both changes commit atomically
- failure rolls back the transaction
- no distributed saga exists
- no generic transaction coordinator exists
- no top-level orchestration module exists

## 7. Persistence Validation

Verify D03 requirements:

- PostgreSQL
- Spring Data JDBC
- module-owned schemas
- UUID identifiers
- sequential Flyway migrations
- no BIGSERIAL
- no cross-schema physical foreign keys
- no persistence annotations in domain
- no transaction annotations in domain

Validate migration naming against the authoritative D03 convention.

Do not invent additional migration conventions.

## 8. API Validation

Validate approved API behavior:

    /api/v1

Check:

- controller ownership
- DTO / command / domain separation
- zero business logic in controllers
- RFC 7807 ProblemDetail
- approved status semantics
- Policy resolution behavior
- lifecycle endpoints
- document endpoints
- Reporting statistics ownership

Verify that Policy and Commission do not expose prohibited analytical APIs.

## 9. Authorization Validation

Verify:

- backend is authoritative
- authenticated principal reaches application layer
- OrganizationScope is resolved by the appropriate application boundary
- JWT does not become the organization hierarchy store
- Customer scope is respected
- Agent scope is respected
- Branch Admin scope is respected
- Dealer multi-branch scope is respected
- Data Entry inherited scope is respected
- unauthorized resources are protected

Do not infer authorization correctness merely from controller annotations.

## 10. Document Storage Validation

Verify:

- no top-level document module exists
- no `document/` business package exists
- `DocumentStoragePort` belongs to Policy
- provider adapter belongs to Policy infrastructure
- Cloudflare R2 is used through the approved S3-compatible mechanism
- domain does not contain R2-specific concepts
- Policy owns the 0..1 document invariant
- signed URL behavior matches the approved contract

## 11. Event Validation

Verify:

- Spring Modulith event infrastructure is used
- events do not expose persistence entities
- events contain stable identifiers
- publication is transactionally consistent
- consumers are idempotent
- duplicate events are handled
- out-of-order events are handled
- Reporting does not directly access operational databases

Do not approve an event contract merely because an event class exists.

Verify that its payload satisfies the approved Reporting requirements.

## 12. Reporting Validation

Verify:

- Reporting owns read models
- Reporting owns statistics APIs
- Policy/Commission do not expose aggregate statistics
- UNSET commission is excluded from commission statistics
- ZERO commission is included
- positive commission is included
- organization hierarchy is not independently resolved by Reporting
- projections tolerate duplicate events
- projections tolerate out-of-order events

## 13. Architecture Validation

Verify prohibited structures do not exist:

    document
    orchestration
    agent
    subagent
    dealer
    admin

Verify approved modules remain:

    identity
    customer
    product
    policy
    commission
    notification
    reporting
    organization

Run applicable ArchUnit / Spring Modulith architecture verification.

If architecture tests fail:

    BLOCKED — ARCHITECTURE CONFLICT

## 14. Dependency Validation

Compare actual dependencies against the approved implementation plan.

Phase 5 expected additions include:

    springdoc-openapi-starter-webmvc-ui
    software.amazon.awssdk:s3

Verify that none of the following were introduced without explicit approval:

    Kafka
    RabbitMQ
    AWS SQS

Frontend libraries remain recommendations unless explicitly approved as dependencies.

## 15. Git Diff Validation

Inspect:

    git status
    git diff --stat
    git diff

Verify:

- intended files changed
- no unrelated files changed
- no generated artifacts were accidentally committed
- no secrets were introduced
- no unauthorized configuration changes exist
- no prohibited packages were created

Unexpected changes MUST be reported.

## 16. Test Coverage Validation

Check that implementation tests cover the approved behavior.

Classify missing tests as:

- REQUIRED
- RECOMMENDED
- NOT APPLICABLE

Do not invent test requirements that contradict the approved plan.

## 17. Failure Classification

Use exactly one applicable classification for each failed validation:

- REQUIREMENT FAILURE
- ARCHITECTURE FAILURE
- TECHNICAL DESIGN FAILURE
- IMPLEMENTATION DEFECT
- TEST FAILURE
- ENVIRONMENT FAILURE
- MISSING EVIDENCE

Explain the evidence supporting the classification.

## 18. Validation Report

Produce:

### Scope

What was validated.

### Repository Baseline

Relevant repository state.

### Build

Result and evidence.

### Tests

Tests executed and results.

### Requirements

Requirement-by-requirement validation.

### Architecture

Architecture boundary validation.

### Persistence

Persistence validation.

### API

API validation.

### Authorization

Security validation.

### Events

Event validation.

### Reporting

Reporting validation.

### Dependencies

Dependency validation.

### Git Diff

Changed files and unexpected changes.

### Findings

All failures, risks, and missing evidence.

### Recommendation

One of:

    VALIDATED — READY FOR CODE REVIEW

    BLOCKED — REQUIREMENT FAILURE

    BLOCKED — ARCHITECTURE FAILURE

    BLOCKED — TECHNICAL DESIGN FAILURE

    BLOCKED — IMPLEMENTATION DEFECT

    BLOCKED — TEST FAILURE

    BLOCKED — MISSING EVIDENCE

## 19. Safety Boundary

This skill MUST NOT:

- commit changes
- push changes
- reset changes
- restore unrelated changes
- silently modify implementation
- silently modify tests to hide failures
- change requirements
- change architecture
- change API contracts
- change persistence ownership
- add dependencies

Validation is evidence gathering and reporting.

## 20. Completion Rule

Testing validation is complete only when the implementation has been evaluated against the approved baseline and every material finding has been classified.

A successful validation does NOT mean the implementation is automatically approved for commit or release.
