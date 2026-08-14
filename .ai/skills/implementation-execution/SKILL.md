---
name: implementation-execution
description: Execute one explicitly authorized implementation task from the approved implementation plan while preserving requirements, architecture, technical design, repository boundaries, and human-controlled Git operations.
---

# Implementation Execution

## Purpose

Execute an implementation task that is already authorized by the calling implementation workflow and approved implementation plan.

This skill does NOT grant implementation authorization.

The calling workflow MUST establish that implementation has been explicitly authorized before this skill is used.

## Core Principle

Implement the approved plan exactly within the approved architecture.

Do not invent business rules, architectural structures, API contracts, persistence boundaries, event contracts, dependencies, or security behavior during implementation.

If implementation requires a decision that is not already authorized:

    STOP

Report the decision required to the human.

## 1. Required Inputs

Before executing a task, establish:

1. Approved implementation plan.
2. Current repository state.
3. Applicable requirements.
4. Applicable architecture decisions.
5. Applicable technical design.
6. Applicable API, persistence, event, and security contracts.
7. Exact implementation task being executed.
8. Explicit implementation authorization from the calling workflow.

Never assume that documentation describes an existing implementation.

Repository reality MUST be verified before modification.

## 2. Task Boundary

Execute only the current approved task.

Every modification MUST be traceable to:

    Requirement
        →
    Architecture
        →
    Technical Design
        →
    Implementation Plan
        →
    Current Task

Do not expand the task merely because an adjacent improvement appears useful.

## 3. Repository Reality

Before modifying a file:

- inspect the existing file
- inspect nearby implementation patterns
- inspect relevant package structure
- inspect relevant dependencies
- inspect relevant tests
- inspect applicable configuration
- inspect current Git state when required by the workflow

If the repository differs materially from the approved plan:

    STOP

Do not silently rewrite the plan.

## 4. Change Boundary

Modify only files authorized by the implementation plan or clearly required as a mechanical consequence of the approved task.

A mechanical consequence MUST NOT change:

- business requirements
- architecture
- module boundaries
- API contracts
- persistence ownership
- event contracts
- authorization model
- dependency strategy

If a required change crosses one of these boundaries:

    STOP

Request human decision.

## 5. Architecture Protection

Phase 5 approved business modules are:

    identity
    customer
    product
    policy
    commission
    notification
    reporting
    organization

Never create these as top-level business modules:

    document
    orchestration
    agent
    subagent
    dealer
    admin

Infrastructure capabilities MUST remain inside their owning module.

## 6. Module Boundaries

Cross-module collaboration MUST use approved mechanisms:

- public application contracts
- governed Spring Modulith events
- approved ports and adapters

Never:

- access another module's repository directly
- access another module's internal application service directly
- access another module's database tables directly
- create cross-schema physical foreign keys
- introduce internal HTTP calls between modules
- bypass a governed public contract

## 7. Policy / Commission Transaction

The approved cross-module exception is:

    Policy Premium Update
            +
    Commission RESET → UNSET

It MUST remain:

- owned by the Policy application layer
- narrowly scoped
- atomic
- implemented through the Commission public contract

Never create:

    orchestration module
    PolicyCommissionOrchestrator
    distributed saga
    general-purpose transaction coordinator

## 8. Commission Rules

Preserve:

- Fixed commission
- Percentage commission
- maximum 50%
- UNSET
- ZERO / CONFIGURED
- POSITIVE / CONFIGURED

UNSET and ZERO are semantically different.

Activation rules:

| Agents | UNSET | ZERO | POSITIVE |
|--------|-------|------|----------|
| 0 | Allowed | Allowed | Allowed |
| 1 | Prohibited | Allowed | Allowed |
| 2 | Prohibited | Allowed | Allowed |

Never represent UNSET as ZERO.

## 9. Policy Rules

Policy lifecycle states are exclusively:

    DRAFT
    ACTIVE
    INACTIVE

Never introduce another lifecycle state without explicit approval.

Policy identity is immutable.

Policy deletion MUST NOT be implemented.

## 10. Organization Rules

Organization owns authoritative organizational hierarchy and scope resolution.

Preserve:

- Branch has exactly one Branch Admin.
- Branch Admin acts as Agent A only for their administered Branch.
- Regular Agents are not automatically branch-bound.
- Data Entry inherits applicable parent scope.
- Identity remains separate from Organization.

JWT MUST NOT become the authoritative organization hierarchy.

## 11. Persistence Rules

Follow D03:

- PostgreSQL
- Spring Data JDBC
- module-owned schemas
- UUID identifiers
- sequential numeric Flyway migrations

Never:

- use BIGSERIAL
- create cross-schema physical FKs
- place persistence annotations in domain
- place transaction annotations in domain
- invent migration versioning
- invent indexes without query justification

## 12. API Rules

Phase 5 APIs use:

    /api/v1

Controllers MUST:

- extract authenticated context
- map HTTP DTOs
- delegate to application services
- return API DTOs

Controllers MUST NOT contain business logic.

Reporting owns analytical/statistics APIs.

Do not introduce standalone Organization or Commission HTTP APIs unless explicitly required by an authoritative requirement.

## 13. Error Semantics

Preserve the approved API contract:

    401 — unauthenticated
    403 — authenticated but unauthorized
    404 — nonexistent / concealed existence according to D04/D06
    400 — malformed request
    409 — concurrency/integrity conflict
    422 — approved business-validation semantics

Do not invent alternative status-code behavior.

## 14. Document Storage

Documents belong to Policy.

Use:

    policy.port.outbound.DocumentStoragePort

with a provider-specific adapter inside Policy.

Approved provider:

    Cloudflare R2

Never create:

    com.anverraglobal.document
    document/

Keep provider-specific R2 concepts outside the domain.

## 15. Events

Use Spring Modulith event infrastructure.

Do not add Kafka, RabbitMQ, or SQS unless separately approved.

Events MUST:

- contain stable identifiers
- avoid persistence entities
- be transactionally published
- support idempotent consumption
- contain information required by approved consumers

Do not invent event types merely because they appear convenient.

Reporting event consumers MUST handle duplicate and out-of-order delivery according to the approved event design.

## 16. Reporting

Reporting owns:

- read models
- Policy statistics
- Commission statistics
- analytical APIs

Reporting MUST NOT:

- directly query operational schemas
- independently resolve organization hierarchy
- expose operational CRUD
- expose prohibited commission percentage analytics

Commission statistics:

    UNSET → excluded
    ZERO → included
    POSITIVE → included

## 17. Dependency Protection

Only add dependencies explicitly authorized by the approved plan.

Phase 5 approved dependencies include:

    springdoc-openapi-starter-webmvc-ui
    software.amazon.awssdk:s3

Do not add:

    Kafka
    RabbitMQ
    AWS SQS

Do not add a dependency merely because it simplifies implementation.

## 18. Implementation Style

Prefer:

- small cohesive changes
- existing repository conventions
- immutable application commands where specified
- clear domain methods
- explicit validation
- constructor injection
- provider-neutral ports
- module-local adapters

Avoid:

- speculative abstractions
- generic frameworks
- unnecessary base classes
- unrelated refactoring
- premature optimization

## 19. Testing During Execution

When implementing behavior, add or update the corresponding tests required by the approved plan.

At minimum, preserve coverage for relevant:

- domain invariants
- application behavior
- persistence behavior
- API behavior
- authorization
- events
- reporting projections
- architecture boundaries

Critical Phase 5 behavior includes:

- UNSET versus ZERO
- commission maximum
- activation matrix
- premium update resetting commission
- atomic transaction behavior
- authorization scope
- reporting event idempotency
- reporting out-of-order handling

## 20. Verification After the Task

Before declaring the task complete:

1. Compile the affected module.
2. Run relevant tests.
3. Run relevant architecture tests.
4. Inspect the resulting diff.
5. Verify no prohibited module/package appeared.
6. Verify no unauthorized dependency appeared.
7. Verify no API contract changed unexpectedly.
8. Verify no persistence boundary changed unexpectedly.
9. Verify no business rule changed.
10. Verify no unrelated files were modified.

If verification fails:

    STOP

Do not continue to the next task while the failure remains unexplained.

## 21. Unexpected Discovery

If implementation reveals:

- contradictory requirements
- architecture contradiction
- missing business decision
- missing API decision
- missing persistence decision
- missing event contract
- security ambiguity
- new top-level module requirement
- unapproved dependency requirement
- API contract change
- database ownership change

STOP the affected task.

Report:

1. Discovery
2. Evidence
3. Affected task
4. Impact
5. Existing approved rule
6. Decision required

Do not silently resolve the issue.

## 22. Git Safety

This skill MUST NOT automatically:

- git commit
- git push
- git reset
- git restore unrelated changes
- git clean
- delete unrelated user changes

Implementation completion does not imply Git publication.

Git operations remain human-controlled.

## 23. Completion

For each completed task, report:

### Task

What was implemented.

### Files Created

List exact files.

### Files Modified

List exact files.

### Dependencies

List additions, if any.

### Tests

List tests added or executed.

### Verification

List validation performed.

### Requirement Traceability

Identify the requirement and approved plan task satisfied.

### Remaining Issues

List unresolved issues, if any.

The skill MUST NOT claim broader completion than the task actually executed.
