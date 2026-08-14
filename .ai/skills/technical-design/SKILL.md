---
name: technical-design
description: Produce implementation-independent technical designs from approved requirements and architecture. Use after requirement and architecture analysis and before implementation planning.
---

# Technical Design

## Purpose

Translate approved business requirements and architecture into a concrete, implementation-independent technical design.

This skill MUST NOT implement the design.

## Core Principle

Technical design answers:

    HOW should the approved requirement be implemented within the approved architecture?

It MUST NOT silently change:

- business requirements
- architecture
- module boundaries
- security ownership
- persistence ownership
- API ownership
- event architecture

If the design requires changing an approved decision, stop and request the appropriate decision.

## Required Inputs

Before producing a technical design, inspect:

1. Repository discovery results.
2. Requirement analysis.
3. Authoritative business requirements.
4. Approved architecture decisions.
5. Relevant D01-D07 architecture documents.
6. Existing technical designs.
7. Existing implementation patterns.
8. Relevant API, persistence, event, and security architecture.

## Authority Classification

Every significant technical decision MUST be classified as one of:

- AUTHORITATIVE
- REQUIRED CONSEQUENCE
- APPROVED DESIGN DECISION
- RECOMMENDED IMPLEMENTATION MECHANISM
- DESIGN PROPOSAL
- UNRESOLVED
- SUPERSEDED
- HISTORICAL

A recommendation MUST NOT be presented as an approval.

## Design Sequence

### 1. Scope

Define:

- capabilities affected
- requirements covered
- modules affected
- explicit exclusions
- assumptions

### 2. Module Design

For each affected module identify:

- domain
- application
- inbound adapters
- outbound ports
- outbound adapters
- public contracts
- events

Do not create new top-level modules unless architecture explicitly authorizes them.

### 3. Dependency Design

For each dependency define:

- source
- target
- reason
- mechanism
- direction
- architectural authority

Prefer governed contracts and approved event mechanisms.

Never depend directly on another module's internal implementation or persistence.

### 4. Domain Design

Define:

- aggregates
- entities
- value objects
- domain services
- invariants
- lifecycle states
- domain operations

Domain design MUST remain independent of infrastructure.

Do not put transaction management into domain objects.

### 5. Application Design

Define:

- application services
- use cases
- commands
- queries
- orchestration responsibilities
- transaction boundaries

Cross-module application orchestration MUST remain inside an approved module/application boundary.

For Phase 5:

    PolicyManagementApplicationService

may coordinate the approved:

    Premium update
        +
    Commission RESET -> UNSET

transaction.

Do NOT create a top-level orchestration module.

### 6. Persistence Design

Define conceptually:

- module-owned schemas
- tables
- identifiers
- logical relationships
- constraints
- indexes when justified
- versioning
- migration requirements

Respect D03.

For Phase 5:

- PostgreSQL
- Spring Data JDBC
- UUID identifiers
- module-owned schemas
- sequential Flyway migrations
- no cross-module physical foreign keys

Do not create migration files during technical design.

### 7. API Design

Define:

- endpoint intent
- ownership
- request/response concepts
- application commands
- authorization boundary
- error semantics
- pagination/search behavior
- concurrency behavior

Controllers MUST remain thin.

Do not create controllers or DTO classes during technical design.

### 8. Event Design

Define:

- event purpose
- producer
- consumer
- triggering state change
- payload requirements
- transaction semantics
- durability
- idempotency
- ordering
- failure behavior

Do not invent event payloads without tracing them to a requirement.

For unordered asynchronous events, explicitly define the strategy for stale/out-of-order events.

Do not add Kafka/RabbitMQ/SQS unless architecture explicitly authorizes it.

### 9. Security Design

Define:

- authentication source
- identity boundary
- RBAC boundary
- organizational scope
- resource authorization
- existence concealment
- data exposure constraints

Identity MUST NOT become the authoritative owner of organizational hierarchy.

### 10. Infrastructure Design

External services MUST be accessed through provider-neutral ports where required.

For Phase 5 document storage:

    Policy
      ↓
    DocumentStoragePort
      ↓
    Cloudflare R2 adapter

There MUST NOT be a top-level document module.

Provider-specific concepts MUST NOT leak into the domain layer.

### 11. Frontend Design

Define UX behavior and integration requirements.

Specific libraries are recommendations unless explicitly approved.

Do not make a library mandatory merely because it is convenient.

### 12. Failure and Recovery Design

Define behavior for:

- validation failure
- authorization failure
- concurrency conflict
- persistence failure
- event failure
- external storage failure
- partial operations

Do not invent recovery mechanisms unsupported by architecture.

## Consistency Checks

Before completing the design, verify:

### Requirements

Every implementation-relevant requirement has a technical consequence.

### Architecture

No design element contradicts approved architecture.

### Modules

No unauthorized top-level modules exist.

### Dependencies

All cross-module dependencies are authorized.

### Transactions

Transactions are placed in application services.

### Persistence

No prohibited cross-module physical FKs exist.

### APIs

API ownership matches D04.

### Events

Event behavior matches D05.

### Security

Authorization ownership matches D06.

### Infrastructure

External systems remain behind appropriate ports/adapters.

## Decision Register

Every unresolved technical choice MUST be recorded.

Example:

| Decision | Status | Reason |
|---|---|---|
| Cloudflare R2 | APPROVED | Phase 5 decision |
| Optimistic locking | RECOMMENDED | Implementation mechanism |
| Event payload schema | UNRESOLVED | Requires detailed reporting design |

Never silently resolve an unresolved decision.

## Design Traceability

Every major design element MUST map to:

    Requirement
        ↓
    Architecture
        ↓
    Technical Design
        ↓
    Planned Implementation

If a design element has no requirement or architectural justification, identify it as a proposal.

## Output

Produce:

### Technical Scope

### Module Architecture

### Dependency Graph

### Domain Design

### Application Design

### Transaction Boundaries

### Persistence Design

### API Design

### Event Design

### Security Design

### Infrastructure Design

### Frontend Design

### Failure / Recovery Design

### Decision Register

### Requirement Traceability

### Implementation Constraints

Explicitly state what implementation MUST and MUST NOT do.

### Open Questions

Only genuine unresolved decisions.

### Readiness

State one:

- READY FOR IMPLEMENTATION PLANNING
- BLOCKED — REQUIREMENT ISSUE
- BLOCKED — ARCHITECTURE ISSUE
- BLOCKED — TECHNICAL DECISION REQUIRED

## Implementation Boundary

This skill MUST NOT:

- create Java source files
- create TypeScript/React source files
- create migrations
- modify dependencies
- modify application configuration
- create controllers
- create repositories
- create event classes
- create tests

unless a separate explicitly authorized workflow allows those actions.

## Completion Rule

Technical design is complete only when the approved requirements have an implementation-independent technical solution that fits the approved architecture.

Completion of technical design MUST NOT be interpreted as implementation authorization.
