---
description: Perform a final read-only engineering review of an implemented change against approved requirements, architecture, technical design, implementation plan, tests, and repository governance.
---

# Code Review Workflow

## 1. Purpose

Perform the final AI-assisted engineering review after implementation and testing validation.

This workflow is READ-ONLY.

It MUST NOT:

- modify source code
- modify tests
- modify dependencies
- modify migrations
- modify configuration
- fix defects
- refactor code
- silently change requirements
- silently change architecture
- commit
- push
- reset
- restore unrelated changes

The purpose is to identify whether the implementation is correct, complete, compliant, and ready for human acceptance.

## 2. Required Preconditions

Before reviewing, verify:

1. Implementation was explicitly authorized.
2. An approved implementation plan exists.
3. Implementation has been performed against that plan.
4. Testing validation has been completed or its result is available.
5. Requirements are available.
6. Architecture documents are available.
7. Technical design documents are available.
8. API contract is available where applicable.
9. The code-review skill is available.

If required evidence is missing:

    BLOCKED — MISSING EVIDENCE

## 3. Required Skill

Use:

    .ai/skills/code-review/SKILL.md

The code-review skill is the primary authority for this workflow.

Use supporting skills where required:

    .ai/skills/repository-discovery/SKILL.md
    .ai/skills/requirement-analysis/SKILL.md
    .ai/skills/architecture-analysis/SKILL.md
    .ai/skills/technical-design/SKILL.md
    .ai/skills/testing-validation/SKILL.md

## 4. Review Boundary

Review only the implementation covered by the approved implementation plan and its directly affected dependencies.

Do not broaden the review into unrelated repository cleanup.

If unrelated changes are discovered:

- identify them
- report them
- do not modify them

## 5. Repository State

Inspect:

    git status
    git diff --stat
    git diff

Also inspect:

- newly created files
- deleted files
- modified configuration
- modified dependencies
- migrations
- tests

Determine whether every implementation change is explainable by the approved plan.

## 6. Requirements Compliance

For every planned requirement:

1. Identify the requirement.
2. Identify the implementation.
3. Identify the test evidence.
4. Determine whether behavior matches the requirement.

Classify each as:

    PASS
    FAIL
    PARTIAL
    NOT VERIFIED

Do not treat implementation intent as proof of behavior.

## 7. Architecture Compliance

Verify:

- approved module boundaries
- dependency direction
- domain/application/adapter separation
- cross-module contracts
- transaction boundaries
- authorization boundaries
- persistence ownership
- event ownership

No implementation convenience may override authoritative architecture.

## 8. Module Boundary Review

Phase 5 must contain the approved modules and boundaries.

Verify:

- Organization owns organizational hierarchy
- Identity owns authentication and RBAC
- Policy owns Policy
- Commission owns Commission
- Reporting owns analytical projections and statistics
- Documents remain a Policy capability

Verify there is no unauthorized:

    document/
    orchestration/
    agent/
    subagent/
    dealer/
    admin/

top-level business module.

## 9. Domain Purity

Verify domain models do not contain prohibited infrastructure concerns.

Specifically check:

- no Spring Data JDBC mapping annotations in domain
- no persistence repositories in domain
- no HTTP types in domain
- no Spring Security types in domain
- no storage-provider types in domain
- no `@Transactional` in domain

Domain behavior must remain independent of infrastructure.

## 10. Policy Review

Verify:

- Policy identity is immutable
- Policy lifecycle contains only:

    DRAFT
    ACTIVE
    INACTIVE

- Policy cannot be physically deleted
- progressive updates do not implicitly change lifecycle
- lifecycle transitions are validated
- Policy authorization is enforced through the application boundary

## 11. Commission Review

Verify:

- Fixed and Percentage commission rules
- percentage maximum of 50%
- Agent A / Agent B allocation rules
- `UNSET` is distinct from `ZERO`
- `UNSET` is never silently converted to `ZERO`

Verify the activation matrix:

    0 agents + UNSET    → allowed
    0 agents + ZERO     → allowed
    0 agents + POSITIVE → allowed

    1 agent + UNSET     → prohibited
    1 agent + ZERO      → allowed
    1 agent + POSITIVE  → allowed

    2 agents + UNSET    → prohibited
    2 agents + ZERO     → allowed
    2 agents + POSITIVE → allowed

## 12. Premium / Commission Atomicity Review

Verify the approved transaction:

    Policy Premium Update
            +
    Commission RESET → UNSET

is orchestrated by:

    policy.application.PolicyManagementApplicationService

Verify:

- both state changes occur within the approved transaction
- rollback protects the invariant
- no distributed transaction exists
- no saga was introduced
- no generic orchestration module was introduced

## 13. Organization / Authorization Review

Verify:

- Organization owns hierarchy
- OrganizationScope is resolved at the application boundary
- Identity remains responsible for authentication/RBAC
- JWT is not treated as the authoritative hierarchy store
- resource authorization uses the resolved scope
- filters cannot expand authorization scope
- Data Entry restrictions are enforced

## 14. Persistence Review

Verify D03 compliance:

- PostgreSQL
- Spring Data JDBC
- module-owned schemas
- explicit schema mapping
- UUID identifiers
- no BIGSERIAL
- no cross-schema physical foreign keys
- sequential numeric Flyway migrations
- no repeatable production baseline migrations
- no prohibited test-data migrations
- appropriate optimistic locking where designed

Verify domain purity with respect to persistence.

## 15. API Review

Verify:

    /api/v1

for Phase 5 endpoints.

Check:

- controller ownership
- DTO / Command / Domain separation
- no domain objects exposed directly
- no business logic in controllers
- Policy endpoints
- Policy resolution endpoint
- lifecycle endpoints
- document endpoints
- Reporting statistics endpoints

Verify RFC 7807 `ProblemDetail` behavior and approved status semantics.

Do not accept invented endpoint behavior that is not supported by the API contract.

## 16. Concurrency Review

Verify the implementation follows the approved concurrency design.

Check:

- optimistic locking where implemented
- version mismatch handling
- appropriate 409 behavior
- no unapproved ETag / If-Match requirement
- no accidental last-write-wins behavior where optimistic locking is required

## 17. Document Storage Review

Verify:

    policy.port.outbound.DocumentStoragePort

and the Policy-owned storage adapter.

Check:

- provider-neutral domain boundary
- Cloudflare R2 integration remains outside domain
- 0..1 document invariant
- upload
- replacement
- removal
- temporary signed URL download
- no document module

## 18. Event Architecture Review

Verify Spring Modulith is used according to D05.

Check:

- transactional event publication
- durable event registry
- asynchronous inter-module listeners
- event payload isolation from persistence entities
- consumer idempotency
- retry/failure handling
- out-of-order handling
- aggregate version or approved occurrence-order strategy

Verify no unauthorized external broker was introduced.

Must remain absent unless separately approved:

    Kafka
    RabbitMQ
    AWS SQS

## 19. Reporting Review

Verify Reporting:

- owns analytical APIs
- owns projection/read-model persistence
- consumes approved Policy/Commission events
- does not directly join operational databases
- handles duplicate events
- handles out-of-order events
- excludes UNSET commissions from commission statistics
- includes ZERO commissions in commission statistics

Verify Reporting does not independently reconstruct organizational authorization hierarchy.

## 20. Dependency Review

Compare `pom.xml` with the approved implementation plan.

Expected Phase 5 additions include:

    springdoc-openapi-starter-webmvc-ui
    software.amazon.awssdk:s3

Verify no dependency was introduced merely for convenience.

Flag:

- unapproved dependencies
- duplicate dependencies
- incompatible versions
- dependencies violating architecture

## 21. Test Review

Review whether tests adequately prove the critical behavior.

At minimum verify tests cover:

### Domain

- Policy lifecycle
- Policy invariants
- Commission validation
- UNSET/ZERO distinction
- activation matrix

### Application

- authorization
- OrganizationScope
- Premium/Commission atomicity
- rollback behavior

### Persistence

- migrations
- schema isolation
- repository behavior
- optimistic locking

### API

- successful requests
- validation failures
- unauthorized access
- Policy resolution
- concurrency conflicts
- document APIs
- Reporting APIs

### Events

- transactional publication
- duplicate delivery
- listener failure
- retry behavior
- out-of-order delivery

### Reporting

- projection updates
- UNSET exclusion
- ZERO inclusion
- out-of-order projection handling

## 22. Code Quality Review

Review:

- naming
- cohesion
- duplication
- unnecessary abstraction
- exception handling
- null handling
- transaction boundaries
- logging
- security-sensitive logging
- maintainability
- testability

Do not demand stylistic changes that contradict the repository's existing conventions.

## 23. Security Review

Check for:

- authorization bypasses
- IDOR/resource exposure
- scope expansion through filters
- sensitive data leakage
- unsafe error messages
- credentials/secrets in source
- storage URL leakage
- insecure document access
- authentication assumptions

Security findings must be clearly separated from style findings.

## 24. Traceability Review

For every implementation-plan task:

    Requirement
        ↓
    Architecture
        ↓
    Technical Design
        ↓
    Implementation Task
        ↓
    Code
        ↓
    Test
        ↓
    Review Evidence

Identify any broken link.

A missing traceability link is a finding even if the code appears to work.

## 25. Plan Deviation Review

Compare implementation against the approved plan.

Classify deviations:

### ACCEPTABLE

Mechanical implementation detail that does not change architecture, behavior, or requirements.

### REQUIRES HUMAN REVIEW

Behavioral or structural deviation that may be valid but was not explicitly approved.

### PROHIBITED

Contradicts an authoritative requirement or architecture rule.

Do not silently approve architectural deviations.

## 26. Findings Severity

Classify findings as:

### CRITICAL

Security issue, data corruption risk, broken transaction invariant, or severe architectural violation.

### HIGH

Requirement violation, authorization defect, persistence integrity issue, or major functional defect.

### MEDIUM

Important maintainability, testing, or contract issue that should be resolved before acceptance.

### LOW

Non-blocking improvement.

Do not inflate severity merely because a change is stylistically undesirable.

## 27. Final Review Report

Produce:

### Executive Verdict

Overall result.

### Repository State

Current Git state.

### Requirements

Requirement-by-requirement status.

### Architecture

Boundary and architecture status.

### Persistence

Schema and migration status.

### API

Contract compliance.

### Security

Authorization and security status.

### Events

Event architecture status.

### Reporting

Projection and analytics status.

### Dependencies

Dependency status.

### Tests

Test evidence and gaps.

### Plan Deviations

All deviations from the approved plan.

### Findings

Each finding with:

- severity
- category
- file
- evidence
- violated rule/requirement
- impact
- recommended action

Recommendations MUST NOT modify the repository automatically.

## 28. Human Acceptance Boundary

Code review does not authorize:

- fixes
- refactoring
- dependency changes
- migration changes
- commits
- pushes
- releases

If findings require changes:

    STOP

Return the findings to the human/implementation workflow.

## 29. Final Safety Gate

The final status MUST be exactly one of:

    REVIEW PASSED — READY FOR HUMAN ACCEPTANCE

or

    REVIEW BLOCKED — CRITICAL FINDINGS

or

    REVIEW BLOCKED — REQUIREMENT VIOLATION

or

    REVIEW BLOCKED — ARCHITECTURE VIOLATION

or

    REVIEW BLOCKED — SECURITY FINDING

or

    REVIEW BLOCKED — IMPLEMENTATION DEFECT

or

    REVIEW BLOCKED — MISSING EVIDENCE

A successful code review means the implementation is ready for human acceptance. It does not itself authorize release, commit, push, or further implementation.
