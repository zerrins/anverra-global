---
name: architecture-analysis
description: Analyze proposed changes against the Engineering Constitution, approved architecture, module boundaries, and architectural decisions. Use after requirement analysis and before technical design or implementation.
---

# Architecture Analysis

## Purpose

Determine whether a requirement or proposed change is compatible with the repository's authoritative architecture.

This skill is read-only unless a calling workflow explicitly authorizes documentation changes.

## Core Principle

Architecture is a constraint, not a suggestion.

The agent MUST identify the authoritative architecture before proposing implementation structures.

The agent MUST NOT introduce a convenient technical structure that contradicts an approved architectural boundary.

## Required Inputs

Before analysis, inspect:

1. Engineering Constitution.
2. Relevant architecture documents.
3. Approved architecture decisions.
4. Relevant technical designs.
5. Relevant module inventory.
6. Relevant security, persistence, API, and event architecture.
7. Existing implementation structure.

For Phase 5, this includes the approved Phase 5 architecture decisions and their reconciled D01-D07 documentation.

## Authority Classification

Classify architectural information as:

- AUTHORITATIVE
- APPROVED DECISION
- REQUIRED CONSEQUENCE
- DESIGN DECISION
- RECOMMENDED
- PROPOSED
- UNRESOLVED
- SUPERSEDED
- HISTORICAL

Never treat a recommendation or proposal as an architectural mandate.

## Architecture Analysis Sequence

### 1. Identify Affected Capabilities

Determine:

- modules affected
- bounded contexts affected
- application layers affected
- persistence boundaries affected
- API boundaries affected
- event boundaries affected
- security boundaries affected
- infrastructure boundaries affected

### 2. Validate Module Inventory

Verify that every proposed top-level module exists in the approved module inventory.

Do NOT create a new top-level business module unless an explicit architecture decision authorizes it.

For the current Phase 5 baseline:

- Identity
- Customer
- Product
- Policy
- Commission
- Notification
- Reporting
- Organization

are the approved business modules.

The following MUST NOT be invented as top-level business modules unless architecture explicitly changes:

- document
- orchestration
- agent
- subagent
- dealer
- proposal
- kyc
- admin

### 3. Validate Dependency Direction

For every cross-module dependency determine:

- source module
- target module
- dependency mechanism
- direction
- whether the dependency is authorized

Preferred mechanisms include:

- governed public contracts
- approved application-layer collaboration
- approved events

A module MUST NOT directly access another module's internal implementation or persistence.

### 4. Validate Transaction Boundaries

Determine where transactions belong.

Transactions MUST be placed in the application layer according to D03.

Domain objects MUST NOT own transaction management.

For approved exceptions, verify:

- exception is explicitly authorized
- scope is narrow
- no general cross-module transaction permission is inferred
- no distributed saga is introduced unnecessarily

For the Phase 5 Premium invariant:

    Policy premium update
        +
    Commission RESET -> UNSET

is the approved narrow cross-module transaction.

The transaction belongs in the Policy application layer using the governed Commission public contract.

Do NOT create a top-level orchestration module.

### 5. Validate Persistence Boundaries

Check:

- module-owned schemas
- ownership of tables
- cross-module relationships
- foreign keys
- identifiers
- transaction placement
- mapping-layer purity

Cross-module physical foreign keys MUST NOT be introduced.

Logical UUID references are permitted where the architecture requires them.

Domain layers MUST remain free of persistence mapping annotations where D03 prohibits them.

### 6. Validate Organization Ownership

Identity owns:

- identity
- authentication
- RBAC

Organization owns:

- Dealer
- Branch
- Agent
- Data Entry organizational relationships
- hierarchy resolution
- OrganizationScope

The organizational hierarchy MUST NOT be made authoritative through JWT claims.

Application authorization may consume a resolved OrganizationScope.

### 7. Validate Reporting Ownership

Reporting owns:

- analytics
- statistics
- analytical read models

Policy and Commission MUST NOT expose analytical/statistics APIs directly.

Reporting MUST NOT independently resolve organizational hierarchy during event processing.

Operational modules provide governed information to Reporting.

### 8. Validate Event Architecture

Verify:

- event mechanism
- transactional publication
- durability
- listener behavior
- idempotency
- failure handling
- ordering assumptions

Do not introduce Kafka, RabbitMQ, SQS, or another external broker unless architecture explicitly authorizes it.

Spring Modulith JDBC event publication is the current approved event foundation.

Do not invent event payloads merely because an event would be convenient.

Event schemas belong to technical design and must trace to reporting requirements.

### 9. Validate API Boundaries

Verify:

- controller ownership
- application-layer delegation
- DTO/application-command/domain separation
- authorization responsibility
- error ownership
- Reporting API ownership

Controllers MUST contain no business logic.

Operational modules MUST NOT expose Reporting statistics APIs.

Do not introduce Organization HTTP APIs unless an authoritative requirement requires them.

Do not introduce standalone Commission CRUD APIs unless an authoritative requirement requires them.

### 10. Validate Infrastructure Boundaries

External systems MUST be accessed through appropriate outbound ports and adapters.

For Phase 5 document storage:

- Policy owns document metadata and business rules.
- `DocumentStoragePort` belongs inside Policy's outbound port layer.
- Cloudflare R2 is an infrastructure implementation.
- No top-level `document` module is permitted.

Infrastructure provider details MUST NOT leak into the Policy domain.

### 11. Validate Security Boundaries

Verify:

- authentication ownership
- RBAC ownership
- organization scope resolution
- resource authorization
- existence-concealment requirements
- data exposure

The backend remains the authoritative authorization boundary.

UI visibility MUST NOT be treated as authorization.

### 12. Validate Architecture Evolution

When a proposed change appears to require a new module, dependency, technology, or architectural exception:

1. Check whether an existing approved boundary already supports it.
2. Check whether the architecture explicitly permits the exception.
3. If not, classify the change as requiring architecture approval.
4. Do NOT silently modify architecture documents to legalize the change.

## Contradiction Detection

For every contradiction:

1. Identify source document.
2. Identify conflicting statement.
3. Identify higher-authority rule.
4. Determine whether the lower-level statement is stale or genuinely conflicting.
5. Mark it as:
   - RESOLVED
   - SUPERSEDED
   - UNRESOLVED
   - BLOCKING CONTRADICTION

If architecture is genuinely contradictory, stop implementation planning for the affected area.

## Architecture Decision Test

For each significant technical choice ask:

1. Is it required by architecture?
2. Is it explicitly approved?
3. Is it a technical recommendation?
4. Is it unresolved?
5. Does choosing it create a new architectural dependency?

Do not convert a recommendation into an approval.

## Output

Produce:

### Affected Architecture

Modules and boundaries affected.

### Applicable Authority

Relevant architecture documents and decisions.

### Dependency Analysis

Allowed and prohibited dependencies.

### Transaction Analysis

Transaction ownership and cross-module exceptions.

### Persistence Analysis

Schemas, ownership, relationships, and FK constraints.

### Security Analysis

Identity, RBAC, organization scope, and authorization.

### Event Analysis

Events, listeners, durability, idempotency, and ordering.

### API Analysis

Controller ownership and API boundaries.

### Infrastructure Analysis

Ports, adapters, and external systems.

### Contradictions

Every detected conflict with its authority and status.

### Required Decisions

Architecture decisions that cannot safely be inferred.

### Recommendation

One of:

- ARCHITECTURALLY COMPATIBLE
- COMPATIBLE WITH DESIGN CONSTRAINTS
- BLOCKED — ARCHITECTURAL CONTRADICTION
- BLOCKED — ARCHITECTURE DECISION REQUIRED

## Completion Rule

Architecture analysis is complete only when every affected architectural boundary is identified and validated against authoritative architecture.

A successful architecture analysis MUST NOT be treated as implementation authorization.
