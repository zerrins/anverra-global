---
description: Execute an explicitly approved implementation plan while enforcing repository, architecture, requirement, and change-boundary controls.
---

# Implementation Workflow

## Purpose

Execute an implementation plan that has already been explicitly approved by the human Architecture/Engineering Owner.

This is the ONLY workflow in the AI workflow system that may modify application source code.

Implementation MUST NOT begin unless the explicit human approval gate has been satisfied.

## 1. Required Preconditions

Before modifying anything, verify ALL of the following:

1. Repository discovery is complete.
2. Requirements are resolved.
3. Architecture review has passed.
4. Technical design is complete.
5. Implementation plan exists.
6. Implementation plan is internally consistent.
7. No unresolved requirement decisions remain.
8. No unresolved architecture decisions remain.
9. No unresolved technical decisions affecting implementation remain.
10. The implementation plan identifies intended files, modules, dependencies, and sequencing.
11. Explicit human implementation authorization has been provided.

If ANY condition fails:

    STOP

Do NOT implement.

## 1.1 Primary Execution Skill

The primary skill for this workflow is:

    .ai/skills/implementation-execution/SKILL.md

This skill governs execution of the approved implementation plan, including:
- implementation boundaries
- architecture and business-rule preservation
- repository change scope
- testing expectations
- stop conditions
- Git safety

The implementation-execution skill does NOT grant implementation authorization.
Implementation remains prohibited until the explicit human authorization gate defined by this workflow is satisfied.

## 2. Explicit Human Authorization

The implementation plan MUST contain:

    IMPLEMENTATION AUTHORIZED: NO

until the human explicitly approves implementation.

Valid approval examples include:

    "Approved. Proceed with implementation."

    "Implementation is authorized."

    "Proceed with the implementation plan."

Do NOT interpret these as implementation authorization unless the human clearly authorizes implementation:

- "Looks good."
- "Plan is complete."
- "Technical design is approved."
- "Ready."
- "Continue planning."

## 3. Pre-Implementation Repository Check

Before coding, inspect:

    git status
    git diff
    git diff --stat

Establish:

- current branch
- existing uncommitted changes
- existing untracked files
- files changed by previous work
- whether changes belong to the approved implementation plan

NEVER overwrite unrelated user changes.

If unrelated changes are detected:

    STOP

Ask the human how to proceed.

## 4. Baseline Verification

Verify the approved implementation plan against the current repository.

Confirm:

- module structure
- package structure
- dependencies
- configuration
- migration state
- tests
- existing conventions

If repository reality differs materially from the implementation plan:

    STOP

Do not silently rewrite the plan.

## 5. Change Boundary

Only modify files explicitly authorized by the implementation plan.

Allowed categories:

- CREATE
- MODIFY
- DELETE
- DEPENDENCY ADDITION
- CONFIGURATION CHANGE
- DATABASE MIGRATION
- TEST

Every change MUST have a traceable reason.

If a new file becomes necessary and it is not in the approved plan:

    STOP

unless the change is clearly mechanical and does not alter:

- architecture
- business rules
- API contracts
- persistence ownership
- event contracts
- security model
- dependencies
- module boundaries

Otherwise request human approval.

## 6. Architecture Protection

The approved Phase 5 business modules are:

    identity
    customer
    product
    policy
    commission
    notification
    reporting
    organization

Do NOT create these as top-level business modules:

    document
    orchestration
    agent
    subagent
    dealer
    admin

Infrastructure capabilities must remain within their owning module.

## 7. Module Dependency Protection

Cross-module collaboration MUST use approved mechanisms:

- public application contracts
- governed events
- approved ports/adapters

Do NOT:

- import internal implementation classes from another module
- access another module's repositories
- access another module's database tables directly
- create cross-module physical foreign keys
- create internal HTTP calls between modules when a governed contract is required

## 8. Policy / Commission Transaction Protection

The approved Phase 5 exception is:

    Policy Premium Update
            +
    Commission RESET → UNSET

This transaction MUST:

- be owned by the Policy application layer
- call the Commission public management contract
- execute atomically
- remain narrowly scoped

Do NOT create:

    orchestration module
    PolicyCommissionOrchestrator
    distributed saga
    general cross-module transaction framework

## 9. Commission Rules

Commission types:

- Fixed
- Percentage

Maximum commission:

    50% of Premium

Commission states:

    UNSET
    ZERO / CONFIGURED
    POSITIVE / CONFIGURED

UNSET and ZERO MUST remain semantically distinct.

Activation:

| Agents | Commission | Activation |
|--------|------------|------------|
| 0 | UNSET | Allowed |
| 0 | ZERO | Allowed |
| 0 | POSITIVE | Allowed |
| 1 | UNSET | Prohibited |
| 1 | ZERO | Allowed |
| 1 | POSITIVE | Allowed |
| 2 | UNSET | Prohibited |
| 2 | ZERO | Allowed |
| 2 | POSITIVE | Allowed |

Never represent UNSET as ZERO.

## 10. Policy Lifecycle Protection

Policy lifecycle states are exclusively:

    DRAFT
    ACTIVE
    INACTIVE

No additional lifecycle state may be introduced without explicit approval.

Policy deletion MUST NOT be implemented.

Policy identity MUST remain immutable.

## 11. Organization Protection

Organization owns authoritative organizational relationships.

Rules include:

- Branch has exactly one Branch Admin.
- Branch Admin acts as Agent A only for their administered Branch.
- Regular Agents are not automatically branch-bound.
- Data Entry inherits the applicable parent scope.
- Organization is separate from Identity.

JWT MUST NOT become the authoritative organization hierarchy.

## 12. Persistence Protection

Follow D03:

- PostgreSQL
- Spring Data JDBC
- module-owned schemas
- UUID identifiers
- sequential numeric Flyway migrations

Do NOT:

- use BIGSERIAL
- create cross-schema physical FKs
- place persistence annotations in domain
- place transaction annotations in domain
- invent migration conventions
- invent indexes without query justification

## 13. API Protection

Phase 5 APIs use:

    /api/v1

Controllers MUST:

- extract authentication context
- map DTOs
- delegate to application services
- return DTOs

Controllers MUST NOT contain business logic.

Reporting owns analytical/statistics APIs.

Policy and Commission MUST NOT expose aggregate statistics APIs.

## 14. Error Protection

Use RFC 7807 ProblemDetail.

Preserve approved semantics:

    401 — unauthenticated
    403 — authenticated but unauthorized
    404 — nonexistent / concealed existence according to D04/D06
    400 — malformed request
    409 — concurrency/integrity conflict
    422 — approved API business-validation semantics

Do not invent new status-code behavior.

## 15. Document Storage Protection

Documents are NOT a standalone module.

Use:

    policy.port.outbound.DocumentStoragePort

and a provider-specific adapter under Policy.

Approved provider:

    Cloudflare R2

Do NOT create:

    com.anverraglobal.document
    document/

Policy owns:

- document metadata
- 0..1 invariant
- authorization

Storage adapter owns:

- object storage
- upload
- replacement
- removal
- signed URL generation

Do not expose R2-specific concepts in the domain.

## 16. Event Protection

Use Spring Modulith event infrastructure.

Do NOT add:

    Kafka
    RabbitMQ
    SQS

unless separately approved.

Events MUST:

- be transactionally published
- contain stable identifiers
- avoid persistence entities
- support idempotent consumption
- contain information required by approved consumers

Reporting MUST tolerate out-of-order delivery.

Do not invent unrelated events.

## 17. Reporting Protection

Reporting owns:

- Policy statistics
- Commission statistics
- read models
- analytical APIs

Reporting MUST NOT:

- access operational module databases directly
- resolve organization hierarchy independently
- expose operational CRUD
- expose prohibited commission percentage analytics

Commission statistics:

    UNSET → excluded
    ZERO → included
    POSITIVE → included

## 18. Dependency Protection

Required Phase 5 dependencies include:

    springdoc-openapi-starter-webmvc-ui
    software.amazon.awssdk:s3

The AWS S3 SDK is used for Cloudflare R2's S3-compatible API.

Do NOT add:

    Kafka
    RabbitMQ
    AWS SQS

Frontend libraries such as:

    TanStack Query
    React Hook Form
    Zod

remain implementation recommendations rather than mandatory architecture dependencies.

## 19. Implementation Sequence

Follow the approved implementation plan.

Default sequence:

### Stage 0

Dependencies and repository foundation.

### Stage 1

Organization.

### Stage 2

Policy and Commission domain.

### Stage 3

Application services.

### Stage 4

Persistence and migrations.

### Stage 5

Authorization.

### Stage 6

Policy APIs.

### Stage 7

Document storage.

### Stage 8

Events.

### Stage 9

Reporting.

### Stage 10

Frontend.

### Stage 11

Testing and integration.

Do not skip dependency prerequisites merely to implement a later feature first.

## 20. Verification After Each Stage

After each major stage:

1. Compile/build.
2. Run relevant tests.
3. Run architecture tests.
4. Inspect git diff.
5. Verify no prohibited module/package appeared.
6. Verify no unauthorized dependency appeared.
7. Verify no requirement changed.
8. Verify no API contract changed.
9. Verify no persistence boundary changed.

If verification fails:

    STOP

Do not continue accumulating failures.

## 21. Testing Requirements

Implement tests alongside functionality.

Required categories:

- domain unit tests
- application service tests
- persistence integration tests
- API/controller tests
- authorization tests
- event tests
- reporting projection tests
- architecture tests
- frontend tests where applicable

Critical Phase 5 tests MUST cover:

### Commission

- 50% maximum
- fixed commission
- percentage commission
- UNSET
- ZERO
- positive commission
- Agent A/B allocation

### Activation

- 0 agents + UNSET
- 0 agents + ZERO
- 0 agents + POSITIVE
- 1 agent + UNSET
- 1 agent + ZERO
- 1 agent + POSITIVE
- 2 agents + UNSET
- 2 agents + ZERO
- 2 agents + POSITIVE

### Transaction

- Premium update resets commission.
- Both operations commit atomically.
- Failure rolls both back.

### Authorization

- Customer scope
- Agent scope
- Branch Admin scope
- Dealer multi-branch scope
- Data Entry inherited scope
- unauthorized resource protection

### Reporting

- UNSET excluded
- ZERO included
- POSITIVE included
- out-of-order events
- duplicate events

## 22. Handling Unexpected Discoveries

During implementation, STOP if discovering:

- contradictory requirements
- architecture contradiction
- missing business decision
- missing API decision
- missing persistence decision
- missing event contract
- security ambiguity
- need for a new top-level module
- need for an unapproved dependency
- need to alter an approved API contract
- need to change a database ownership boundary

Report:

1. Discovery
2. Affected task
3. Evidence
4. Impact
5. Proposed options
6. Decision required

Do not silently fix architecture.

## 23. Git Safety

The implementation workflow MUST NOT automatically:

- git commit
- git push
- git reset
- git restore unrelated work
- delete unrelated changes

The human controls version-control operations.

Implementation completion does not imply commit or push.

## 24. Final Implementation Report

At completion produce:

## Files Created

## Files Modified

## Files Deleted

## Dependencies Added

## Configuration Changes

## Database/Migrations

## APIs Implemented

## Events Implemented

## Tests Added

## Requirements Completed

## Architecture Verification

## Remaining Known Issues

## Git Status

## 25. Final Safety Gate

The implementation workflow MUST end with exactly one of:

    IMPLEMENTATION COMPLETE — READY FOR HUMAN REVIEW

    IMPLEMENTATION BLOCKED — HUMAN DECISION REQUIRED

    IMPLEMENTATION BLOCKED — ARCHITECTURE CONFLICT

    IMPLEMENTATION BLOCKED — REQUIREMENT CONFLICT

    IMPLEMENTATION BLOCKED — VERIFICATION FAILURE

Implementation completion does NOT automatically commit or push changes.
