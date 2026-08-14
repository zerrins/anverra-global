---
name: requirement-analysis
description: Analyze business and functional requirements and convert them into explicit, traceable engineering rules before design or implementation. Use after repository discovery and whenever requirements are ambiguous, conflicting, or changing.
---

# Requirement Analysis

## Purpose

Convert business requirements into precise, testable, implementation-independent engineering rules.

This skill is read-only unless a calling workflow explicitly authorizes documentation updates.

## Core Principle

Do not implement what the user appears to mean.

First determine what the authoritative requirements actually require.

The agent MUST distinguish:

- business requirement
- business rule
- invariant
- validation rule
- authorization rule
- lifecycle rule
- data constraint
- integration requirement
- UX consequence
- technical proposal

## Required Inputs

Before analysis, inspect:

1. Relevant authoritative requirements.
2. Existing requirement decision documents.
3. Relevant architecture decisions.
4. Relevant technical design.
5. Existing implementation where necessary to understand current behavior.
6. Existing tests where they encode current requirements.

## Authority

When requirements are classified, preserve their authority.

Use classifications such as:

- AUTHORITATIVE
- REQUIRED
- DESIGN DECISION
- RECOMMENDED
- PROPOSED
- UNRESOLVED
- SUPERSEDED
- HISTORICAL

Never silently upgrade a proposal into a requirement.

## Requirement Decomposition

For each relevant requirement identify:

### Identity

- What entity or capability is involved?
- What makes it unique?
- Which fields are immutable?
- What constitutes the same business entity?

### Lifecycle

Identify:

- valid states
- allowed transitions
- prohibited transitions
- creation state
- terminal states
- deletion semantics

Do not invent additional lifecycle states.

### Business Rules

Identify:

- mandatory fields
- optional fields
- conditional fields
- numerical limits
- cardinality rules
- relationships
- derived values
- state-dependent rules

### Authorization

Identify:

- who can see the resource
- who can create it
- who can edit it
- who can activate/deactivate it
- who can delete/remove information
- what scope applies
- what information must be concealed

Authorization MUST be treated independently from UI visibility.

### Invariants

Explicitly identify rules that must always hold.

For example:

    Premium changes
        ->
    Commission becomes UNSET

If an invariant requires atomicity, mark it for architecture analysis rather than deciding the transaction mechanism here.

### Edge Cases

Explicitly enumerate cases such as:

- zero related entities
- one related entity
- maximum allowed related entities
- missing optional data
- duplicate requests
- concurrent updates
- unauthorized access
- lifecycle conflicts
- partially configured state

Do not invent edge cases that have no meaningful relationship to the requirement.

## Requirement Traceability

Every implementation-relevant rule MUST be traceable to an authoritative source.

Use a structure such as:

| Rule | Source | Classification | Consequence |
|---|---|---|---|
| Policy identity is immutable | REQ-DEC-004 | AUTHORITATIVE | Domain invariant |
| Maximum two agents | REQ-DEC-006 | AUTHORITATIVE | Validation |
| Commission max 50% | REQ-DEC-005 | AUTHORITATIVE | Domain validation |
| Reporting owns statistics | D16 | APPROVED DECISION | Module boundary |

## Conflict Detection

If requirements conflict:

1. Identify the exact conflicting rules.
2. Identify their authoritative sources.
3. Determine whether one supersedes the other.
4. If no authority resolves the conflict, mark it UNRESOLVED.
5. Do not invent a compromise.
6. Do not proceed to implementation when the conflict affects correctness.

## Ambiguity Detection

Examples of ambiguity that MUST be surfaced:

- unspecified cardinality
- unclear ownership
- undefined lifecycle transition
- unclear authorization scope
- conflicting status semantics
- undefined null/zero distinction
- unclear failure behavior
- undefined concurrency behavior
- missing event semantics
- unclear data retention rules

## Business vs Technical Decisions

Do not solve technical questions inside business analysis.

For example:

Business requirement:

    Premium change must reset Commission.

Requirement analysis records:

    Invariant:
    Premium change -> Commission UNSET.
    Atomicity required.

It does NOT decide:

    @Transactional

That belongs to architecture/technical design.

Similarly:

Business requirement:

    Documents must be downloadable.

Requirement analysis does NOT choose:

    S3
    R2
    Presigned URL
    Backend streaming

Those are technical decisions.

## Negative Requirements

Explicitly record what the system MUST NOT do.

Examples:

- MUST NOT physically delete Policy.
- MUST NOT expose unauthorized data.
- MUST NOT create Agent C.
- MUST NOT expose Reporting statistics from operational modules.
- MUST NOT introduce prohibited modules.
- MUST NOT treat UNSET as ZERO.

Negative requirements are first-class requirements.

## Acceptance Criteria

Convert requirements into observable acceptance criteria.

Good:

    Given a Policy with one Agent and UNSET Commission,
    when activation is requested,
    then activation is rejected.

Avoid implementation-specific criteria such as:

    The service must call repository X.

unless the architecture explicitly requires that behavior.

## Requirement Change Analysis

When a requirement changes:

1. Identify the previous rule.
2. Identify the new rule.
3. Determine whether the change supersedes the old rule.
4. Identify affected architecture.
5. Identify affected technical design.
6. Identify affected APIs.
7. Identify affected persistence.
8. Identify affected events.
9. Identify affected tests.
10. Identify affected documentation.

Do not immediately implement a changed requirement.

## Output

Produce:

### Requirement Summary

What the system must accomplish.

### Authoritative Rules

Explicit business rules with sources.

### Invariants

Rules that must always hold.

### Lifecycle

States and valid transitions.

### Authorization

Roles, scopes, and access boundaries.

### Validation

Required and conditional validation.

### Negative Requirements

What must not happen.

### Edge Cases

Relevant boundary conditions.

### Acceptance Criteria

Observable behavior.

### Traceability

Requirement -> source -> consequence.

### Ambiguities

Anything requiring clarification.

### Downstream Impact

Potential impact on:

- architecture
- technical design
- persistence
- APIs
- events
- security
- frontend
- testing

### Readiness

State one:

- READY FOR ARCHITECTURE ANALYSIS
- BLOCKED — REQUIREMENT AMBIGUITY
- BLOCKED — REQUIREMENT CONFLICT
- BLOCKED — MISSING AUTHORITATIVE SOURCE

## Completion Rule

Requirement analysis is complete only when every implementation-relevant business rule is explicit, traceable, and classified.

A complete requirement analysis MUST NOT be treated as implementation authorization.
