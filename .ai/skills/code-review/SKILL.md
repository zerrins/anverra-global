---
name: code-review
description: Perform a read-only engineering review of authorized implementation changes against approved requirements, architecture, technical design, API contracts, persistence rules, event contracts, security, and implementation quality.
---

# Code Review

## Purpose

Review implementation changes after authorized implementation and validation.

This skill is READ-ONLY.

It MUST NOT modify source code, tests, dependencies, migrations, configuration, requirements, architecture, API contracts, or documentation merely to resolve findings.

If a defect is found:

    REPORT IT

Do not silently fix it.

## 1. Required Inputs

Before reviewing, establish:

1. Approved requirements.
2. Approved architecture.
3. Approved technical design.
4. Approved API contract.
5. Approved persistence design.
6. Approved event design.
7. Approved implementation plan.
8. Implementation changes.
9. Testing-validation results.
10. Current Git diff.

If any required baseline is missing:

    BLOCKED — MISSING EVIDENCE

## 2. Review Principle

Review the implementation against the approved baseline, not personal preference.

Distinguish:

- REQUIRED
- ARCHITECTURALLY REQUIRED
- DESIGN DECISION
- RECOMMENDATION
- IMPLEMENTATION PREFERENCE

Do not report a personal preference as an architectural defect.

## 3. Change Scope Review

Inspect:

    git status
    git diff --stat
    git diff

Verify:

- every changed file belongs to the approved plan
- every new file has a traceable purpose
- no unrelated refactoring was introduced
- no generated artifacts were introduced accidentally
- no secrets or credentials were introduced
- no unrelated configuration changes were made

Unexpected changes MUST be reported.

## 4. Requirements Traceability

For every implemented requirement, verify:

    Requirement
        →
    Approved Design
        →
    Implementation
        →
    Test

Check that implementation behavior matches the approved requirement exactly.

Pay particular attention to:

- Policy lifecycle
- immutable Policy identity
- Agent/Branch rules
- Organization scope
- Commission semantics
- activation matrix
- document ownership
- Reporting ownership

## 5. Architecture Review

Verify the implementation respects the eight approved modules:

    identity
    customer
    product
    policy
    commission
    notification
    reporting
    organization

Verify that no prohibited top-level business module/package exists:

    document
    orchestration
    agent
    subagent
    dealer
    admin

Do not accept architectural shortcuts simply because they reduce implementation effort.

## 6. Module Boundary Review

Verify cross-module collaboration uses only approved mechanisms:

- public application contracts
- governed events
- approved ports/adapters

Reject:

- direct repository access across modules
- direct database access across module schemas
- internal implementation imports across modules
- cross-schema physical foreign keys
- unapproved internal HTTP calls
- bypassing public module contracts

## 7. Policy / Commission Transaction Review

Verify the Premium update transaction:

    Policy Premium Update
            +
    Commission RESET → UNSET

is:

- owned by Policy application layer
- narrowly scoped
- atomic
- implemented through the Commission public contract

Reject:

    orchestration module
    PolicyCommissionOrchestrator
    distributed saga
    generic transaction coordinator

Verify the transaction does not accidentally broaden into unrelated cross-module operations.

## 8. Commission Review

Verify:

- Fixed commission supported
- Percentage commission supported
- maximum 50%
- UNSET distinct from ZERO
- positive commission supported
- Agent A/B allocation rules preserved

Activation matrix MUST remain:

| Agents | UNSET | ZERO | POSITIVE |
|--------|-------|------|----------|
| 0 | Allowed | Allowed | Allowed |
| 1 | Prohibited | Allowed | Allowed |
| 2 | Prohibited | Allowed | Allowed |

Reject any implementation that treats:

    UNSET == ZERO

## 9. Policy Lifecycle Review

Verify lifecycle states are exclusively:

    DRAFT
    ACTIVE
    INACTIVE

Verify:

- identity remains immutable
- deletion is not implemented
- activation rules are enforced
- deactivation rules are enforced
- reactivation rules are enforced
- PATCH does not implicitly change lifecycle

Any additional lifecycle state requires explicit approval.

## 10. Organization Review

Verify:

- Branch has exactly one Branch Admin
- Branch Admin acts as Agent A only for their administered Branch
- regular Agents are not automatically branch-bound
- Data Entry scope is inherited correctly
- Organization owns hierarchy
- Identity owns authentication/RBAC
- JWT is not used as the authoritative organization hierarchy

## 11. Persistence Review

Verify D03 compliance:

- PostgreSQL
- Spring Data JDBC
- module-owned schemas
- UUID identifiers
- sequential numeric Flyway versions
- no BIGSERIAL
- no cross-schema physical FKs
- no persistence annotations in domain
- no transaction annotations in domain

Review migrations for:

- deterministic naming
- correct schema ownership
- correct constraints
- required uniqueness constraints
- required nullability
- correct logical cross-module references

Do not demand indexes unless justified by approved lookup/query requirements.

## 12. API Review

Verify:

    /api/v1

Verify:

- controller ownership
- DTO / Command / Domain separation
- no business logic in controllers
- correct application-layer delegation
- RFC 7807 ProblemDetail
- correct HTTP semantics
- Policy resolution endpoint
- lifecycle endpoints
- document endpoints
- Reporting statistics endpoints

Verify Policy and Commission do not expose prohibited analytical APIs.

Do not invent additional API endpoints during review.

## 13. Authorization Review

Verify authorization is enforced at the application/domain boundary rather than relying only on controllers.

Check:

- Customer scope
- Agent scope
- Branch Admin scope
- Dealer multi-branch scope
- Data Entry inherited scope
- OrganizationScope resolution
- authenticated Principal propagation
- unauthorized resource protection

Verify that Reporting does not independently reconstruct organizational hierarchy.

## 14. Document Storage Review

Verify:

    policy.port.outbound.DocumentStoragePort

and provider adapter placement inside Policy.

Approved provider:

    Cloudflare R2

Reject:

    com.anverraglobal.document
    document/

Verify:

- Policy owns document metadata
- Policy owns 0..1 invariant
- provider-specific concepts remain outside domain
- signed URL mechanism matches approved API contract
- binary payloads are not unnecessarily proxied through the JVM

## 15. Event Review

Verify Spring Modulith event infrastructure.

Check that events:

- are published transactionally
- contain stable identifiers
- do not expose persistence entities
- contain only approved consumer data
- support idempotency
- support out-of-order handling

Reporting consumers MUST:

- tolerate duplicate events
- tolerate out-of-order events
- avoid direct operational database access

Do not approve speculative or unrelated event contracts.

## 16. Reporting Review

Verify Reporting owns:

- read models
- Policy statistics
- Commission statistics
- analytical APIs

Verify:

    UNSET → excluded
    ZERO → included
    POSITIVE → included

Reject:

- Policy statistics APIs in Policy
- Commission statistics APIs in Commission
- direct operational database joins
- independent organization hierarchy resolution
- prohibited percentage analytics

## 17. Dependency Review

Compare actual dependencies with the approved plan.

Expected Phase 5 dependencies include:

    springdoc-openapi-starter-webmvc-ui
    software.amazon.awssdk:s3

Reject unapproved additions such as:

    Kafka
    RabbitMQ
    AWS SQS

Frontend libraries remain recommendations unless explicitly approved.

## 18. Code Quality Review

Review for:

- clear naming
- cohesive classes
- appropriate method sizes
- constructor injection
- immutable commands where specified
- explicit validation
- meaningful domain methods
- appropriate exception handling
- no duplicated business rules
- no dead code
- no speculative abstractions
- no unnecessary framework coupling
- no accidental persistence leakage into domain
- no unnecessary comments that merely restate code

Do not demand stylistic changes that have no meaningful engineering benefit.

## 19. Security Review

Check for:

- authorization bypasses
- scope escalation
- insecure direct object references
- sensitive information leakage
- existence leakage
- unsafe document access
- insecure signed URL handling
- secrets in source/configuration
- improper error details
- trust placed incorrectly in client-provided authorization data

Any security ambiguity MUST be escalated rather than guessed.

## 20. Test Review

Verify that tests demonstrate the implemented behavior.

Critical Phase 5 tests include:

### Commission

- 50% maximum
- Fixed
- Percentage
- UNSET
- ZERO
- POSITIVE
- Agent A/B allocation

### Activation

- all 0/1/2 agent combinations
- UNSET/ZERO/POSITIVE combinations

### Transaction

- Premium update resets commission
- atomic commit
- rollback on failure

### Authorization

- Customer scope
- Agent scope
- Branch Admin scope
- Dealer multi-branch scope
- Data Entry scope

### Reporting

- UNSET excluded
- ZERO included
- POSITIVE included
- duplicate events
- out-of-order events

## 21. Findings Classification

Every finding MUST be classified as one of:

    BLOCKER
    HIGH
    MEDIUM
    LOW
    INFORMATIONAL

Also classify its source:

    REQUIREMENT
    ARCHITECTURE
    TECHNICAL DESIGN
    API
    PERSISTENCE
    EVENT
    SECURITY
    TEST
    CODE QUALITY

Do not classify a style preference as a blocker.

## 22. Review Report

Produce:

### Review Scope

What was reviewed.

### Baseline

Requirements, architecture, technical design, API contract, persistence, events, and implementation plan used.

### Files Reviewed

Exact files.

### Requirements Review

Requirement-by-requirement findings.

### Architecture Review

Module and dependency findings.

### Persistence Review

Schema and migration findings.

### API Review

Endpoint and DTO findings.

### Security Review

Authorization and security findings.

### Event Review

Event and Reporting findings.

### Test Review

Coverage and quality findings.

### Dependency Review

Dependency findings.

### Code Quality Review

Implementation quality findings.

### Findings

Each finding with:

- severity
- category
- file
- evidence
- impact
- recommendation

### Final Recommendation

One of:

    APPROVED — READY FOR CHANGE VERIFICATION

    BLOCKED — BLOCKER FINDINGS

    BLOCKED — HIGH-SEVERITY FINDINGS

    BLOCKED — MISSING EVIDENCE

## 23. Safety Boundary

This skill is READ-ONLY.

It MUST NOT:

- modify implementation code
- modify tests
- modify dependencies
- modify migrations
- modify configuration
- modify architecture documents
- modify requirements
- commit
- push
- reset
- restore unrelated changes
- delete files

Review findings must be reported for a separate authorized action.

## 24. Completion Rule

Code review is complete only when the implementation has been evaluated against the approved baseline and every material finding has been classified.

A successful code review does NOT authorize commit, push, or release.
