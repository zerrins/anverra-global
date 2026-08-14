---
description: Perform a strict read-only architecture review against the repository constitution, approved architecture, module boundaries, and authoritative decisions.
---

# Architecture Review Workflow

## Purpose

Validate that a proposed requirement or technical design is compatible with the existing architecture.

This workflow is READ-ONLY.

It MUST NOT:

- implement source code
- modify dependencies
- create migrations
- create modules
- change architecture documents
- silently resolve architecture conflicts

---

## Required Skills

Use:

    .ai/skills/repository-discovery/SKILL.md
    .ai/skills/requirement-analysis/SKILL.md
    .ai/skills/architecture-analysis/SKILL.md

---

# 1. Repository Discovery

First establish repository reality.

Identify:

- existing modules
- existing packages
- existing dependencies
- existing persistence
- existing APIs
- existing events
- existing security infrastructure
- existing tests
- existing configuration

Explicitly distinguish:

    EXISTS TODAY

from:

    DOCUMENTED / PLANNED

---

# 2. Architecture Baseline

Inspect the authoritative architecture documents.

At minimum inspect the applicable:

- Engineering Constitution
- Architecture Principles
- Module Architecture
- Persistence Architecture
- API / Transport Architecture
- Event / Async Architecture
- Security Architecture
- Approved Architecture Decisions

Identify:

- approved modules
- prohibited modules
- module ownership
- dependency direction
- persistence ownership
- transaction rules
- API ownership
- event ownership
- authorization boundaries

---

# 3. Requirement Compatibility

Compare the analyzed requirements against the architecture.

For every requirement determine:

- compatible
- compatible with architectural consequence
- requires architecture decision
- contradicts architecture

Do not silently reinterpret a requirement.

---

# 4. Module Boundary Audit

For every affected capability determine:

| Capability | Owning Module | Existing? | Approved? | Required Action |
|---|---|---|---|---|

Verify that no new top-level module is introduced without explicit approval.

For Phase 5-style architectures, explicitly check that unresolved capabilities do not become accidental modules.

Examples of prohibited accidental modules include:

- document
- orchestration
- agent
- subagent
- dealer
- admin

unless an authoritative architecture decision explicitly approves them.

---

# 5. Dependency Direction Audit

For every cross-module interaction document:

- source module
- target module
- dependency mechanism
- public contract/event
- reason
- direction

Verify:

- no circular dependency
- no direct database coupling
- no cross-module physical foreign keys
- no internal package access across module boundaries
- no HTTP calls between internal modules when governed contracts are required

---

# 6. Transaction Boundary Audit

Identify every transaction involving multiple components.

Classify it as:

- single-module transaction
- approved narrow cross-module transaction
- distributed transaction
- eventual consistency

Any cross-module transaction MUST have explicit architectural authorization.

Never introduce a saga, distributed transaction, or general cross-module transaction merely for convenience.

---

# 7. Persistence Boundary Audit

Verify:

- module-owned schemas
- table ownership
- UUID identifiers
- Flyway conventions
- no cross-module physical FKs
- persistence annotations remain outside domain
- transaction annotations remain outside domain

Any persistence design contradicting the persistence architecture MUST be reported.

---

# 8. API Boundary Audit

Verify:

- controller ownership
- DTO/application/domain separation
- API versioning
- error semantics
- authorization delegation
- reporting API ownership
- document API ownership
- no analytical APIs inside operational modules

Do not invent endpoint semantics that are not supported by the API contract.

---

# 9. Event Boundary Audit

Verify:

- event producer ownership
- event consumer ownership
- event publication mechanism
- transactional publication
- listener transaction semantics
- idempotency
- ordering behavior
- payload boundaries

Do not invent event payloads merely because a consumer seems likely to need them.

If event contracts are unresolved, classify them as unresolved.

---

# 10. Security Boundary Audit

Verify:

- authentication ownership
- RBAC ownership
- organization hierarchy ownership
- OrganizationScope resolution
- resource authorization
- repository-level filtering
- existence-concealment rules

JWT MUST NOT silently become the authoritative organizational hierarchy.

Reporting MUST NOT independently resolve organizational hierarchy if architecture delegates that responsibility upstream.

---

# 11. Architecture Decision Classification

Every important finding MUST be classified as one of:

### AUTHORITATIVE

Explicitly required by architecture.

### APPROVED DECISION

Explicitly approved architecture decision.

### REQUIRED CONSEQUENCE

Logically required by an authoritative rule.

### DESIGN DECISION

A design choice that has been explicitly accepted.

### DESIGN PROPOSAL

Suggested but not approved.

### INFERRED

Reasonably derived but not explicitly stated.

### SUPERSEDED

Previously valid but replaced by a newer decision.

### CONTRADICTION

Conflicts with authoritative architecture.

### UNRESOLVED

Requires human decision.

---

# 12. Contradiction Handling

When a contradiction is found:

DO NOT:

- modify architecture automatically
- choose one interpretation silently
- proceed to implementation

Instead report:

1. Contradiction
2. Authoritative source A
3. Conflicting source B
4. Why they conflict
5. Impact
6. Possible resolutions
7. Decision required

---

# 13. Architecture Decision Gate

The workflow MUST STOP if any unresolved architectural decision affects implementation.

Examples:

- new top-level module
- module ownership
- dependency direction
- transaction boundary
- database ownership
- API ownership
- event ownership
- security model

The human must explicitly approve the resolution before technical design continues.

---

# 14. Required Output

Produce:

## 1. Architecture Baseline

Authoritative architecture currently in force.

## 2. Module Boundary Matrix

| Module | Owner | Dependencies | Status |
|---|---|---|---|

## 3. Dependency Matrix

| Source | Target | Mechanism | Allowed? |
|---|---|---|---|

## 4. Transaction Boundary Assessment

All single-module and cross-module transactions.

## 5. Persistence Assessment

Schemas, tables, FKs, migrations, and ownership.

## 6. API Assessment

Controllers, endpoints, DTO boundaries, errors, and ownership.

## 7. Event Assessment

Producers, consumers, contracts, ordering, and idempotency.

## 8. Security Assessment

Identity, RBAC, organization scope, and authorization.

## 9. Contradictions

Every discovered conflict.

## 10. Unresolved Decisions

Every decision that requires human approval.

## 11. Required Architecture Changes

Only if explicitly authorized or required by a discovered contradiction.

## 12. Implementation Boundary

What implementation would be allowed IF the architecture is approved.

Do not implement it.

---

# 15. Final Safety Gate

The workflow MUST end with exactly one of:

    ARCHITECTURE PASS — READY FOR TECHNICAL DESIGN

    ARCHITECTURE PASS WITH APPROVED DECISIONS — READY FOR TECHNICAL DESIGN

    ARCHITECTURE BLOCKED — HUMAN DECISION REQUIRED

    ARCHITECTURE BLOCKED — AUTHORITATIVE CONTRADICTION

    ARCHITECTURE BLOCKED — INFORMATION MISSING

Architecture review completion MUST NOT authorize implementation.
