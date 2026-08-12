# Consistency

**Stage:** 2 — Engineering Principles  
**Document:** 05 — Consistency  
**Version:** 1.0  
**Status:** Expanded Draft  
**Principle ID:** AEOS-EP-005

---

# 1. Purpose

This document defines the **Consistency** engineering principle.

Consistency establishes that related business concepts, APIs, code, documentation, processes, and engineering practices should use predictable and coherent conventions.

The objective is to reduce unnecessary translation and cognitive overhead.

---

# 2. Principle Statement

> **Use consistent terminology, structures, interfaces, behaviors, and engineering practices so that similar concepts can be understood and handled predictably.**

Consistency does not mean that everything must look identical.

It means that differences should exist for meaningful reasons.

---

# 3. Existing AEOS Direction

Consistency is already an explicit AEOS Product Principle.

The existing principle states that:

> **Business terminology, APIs, documentation, and code shall use a shared ubiquitous language.** :contentReference[oaicite:12]{index=12}

The expanded Core Values document further explains that consistency reduces translation between:

- business
- product
- engineering
- documentation
- AI agents

and that the same concept should not casually acquire multiple competing names. :contentReference[oaicite:13]{index=13}

---

# 4. Why Consistency Matters

Without consistency, engineers repeatedly have to translate between representations.

For example:

```text
Business:
Customer

API:
client

Database:
party

Code:
accountHolder

Documentation:
consumer
```

These names may represent the same concept.

The result is unnecessary cognitive overhead.

A consistent system aims for:

```text
Business
   ↓
Customer
   ↓
API
   ↓
Customer
   ↓
Code
   ↓
Customer
   ↓
Documentation
   ↓
Customer
```

unless a meaningful distinction exists.

---

# 5. Consistency and Ubiquitous Language

The primary form of consistency in Anverra should be **domain consistency**.

Business terminology should remain aligned across:

- requirements
- architecture
- APIs
- code
- database models
- documentation
- tests
- user-facing concepts
- AI context

This is especially important because Anverra operates in a domain with complex insurance-distribution terminology.

---

# 6. One Concept, One Name

Where two things represent the same concept, avoid arbitrary naming differences.

For example, if the business concept is:

```text
Insurance Agent
```

do not casually use:

```text
Agent
Representative
Advisor
SalesUser
IntermediaryUser
AgentUser
```

interchangeably unless these represent genuinely different concepts.

A consistent vocabulary improves:

- understanding
- communication
- searchability
- code navigation
- documentation
- AI reasoning

---

# 7. When Different Names Are Correct

Consistency does not mean forcing one name when concepts are actually different.

For example:

```text
Customer
Agent
Dealer
Partner
```

may all be legitimate distinct business concepts.

The correct rule is:

> **Different concepts should have different names; the same concept should not casually have multiple names.**

---

# 8. Consistency Across Layers

Consistency should exist across the engineering stack.

For example:

```text
Requirement
    ↓
Customer

Domain Model
    ↓
Customer

API
    ↓
Customer

Database
    ↓
Customer

Test
    ↓
Customer

Documentation
    ↓
Customer
```

The exact representation may differ because of technical constraints.

However, the semantic meaning should remain clear.

---

# 9. API Consistency

APIs should follow predictable conventions.

Consistency may include:

- naming
- resource structures
- response formats
- error representations
- pagination
- filtering
- validation behavior
- status semantics

The objective is that an engineer familiar with one API should not have to relearn unrelated conventions for every other API.

---

# 10. Code Consistency

Code should use established patterns when those patterns remain appropriate.

Examples include consistency in:

- naming
- package/module structure
- error handling
- validation
- dependency injection
- configuration
- logging
- testing
- mapping
- API implementation

Consistency should reduce cognitive load.

---

# 11. Consistency and Simplicity

Consistency and simplicity reinforce each other.

If every feature uses a different pattern:

```text
Feature A → Pattern 1
Feature B → Pattern 2
Feature C → Pattern 3
Feature D → Pattern 4
```

the system becomes harder to learn.

If appropriate features use predictable patterns:

```text
Feature A ─┐
Feature B ─┼→ Common Pattern
Feature C ─┤
Feature D ─┘
```

developers can reuse existing knowledge.

---

# 12. Consistency and Modularity

Modules should have consistent expectations.

For example, if each module follows a predictable structure:

```text
module/
 ├── domain/
 ├── application/
 ├── infrastructure/
 ├── api/
 └── tests/
```

engineers and AI agents can navigate new modules more quickly.

The exact repository structure belongs to later architecture and standards artifacts.

The principle is predictability.

---

# 13. Consistency and Documentation

Documentation should use the same terminology as implementation.

A common failure mode is:

```text
Code says:
PolicyHolder

Documentation says:
Customer

Requirement says:
Insured Person
```

If these are the same concept, the inconsistency should be resolved.

Documentation should not create a parallel vocabulary.

---

# 14. Consistency and Testing

Tests should also use domain terminology.

A test should communicate business intent.

Prefer:

```text
shouldRenewPolicyWhenEligible
```

over an opaque implementation-oriented name such as:

```text
testMethod42
```

The test should help communicate the behavior being protected.

---

# 15. Consistency and Error Handling

Error behavior should be predictable.

Where similar situations occur, the system should avoid unrelated conventions unless there is a meaningful reason.

Consistency may apply to:

- error codes
- error structures
- validation messages
- logging
- API status behavior
- retry semantics

The exact standards belong to later artifacts.

---

# 16. Consistency and Configuration

Configuration should follow predictable conventions.

Avoid making every module invent its own configuration model.

Consistent configuration improves:

- deployment
- troubleshooting
- documentation
- AI understanding
- operational support

---

# 17. Consistency and Observability

Logs and metrics should use consistent terminology.

For example, if the business concept is `policyId`, observability should not randomly use:

```text
policyId
policy_id
policyNumber
policyKey
policyRef
```

when those values mean the same thing.

Consistent observability improves operational diagnosis.

---

# 18. Consistency and AI Engineering

Consistency is particularly important for AI-assisted development.

AI agents depend heavily on patterns and terminology.

If repositories contain:

```text
CustomerService
ClientManager
PartyHandler
AccountCoordinator
```

for similar responsibilities, AI reasoning becomes more difficult.

Predictable conventions make it easier for AI to:

- discover relevant code
- infer responsibilities
- locate tests
- understand dependencies
- generate compatible changes
- follow existing patterns

---

# 19. Consistency and Context Loading

The AEOS context-loading model aims to load only the minimum relevant context while preserving traceability. :contentReference[oaicite:14]{index=14}

Consistency supports this objective.

If repositories use predictable structures and terminology, an agent can locate relevant information without loading the entire system.

```text
Consistent Repository
        ↓
Predictable Discovery
        ↓
Smaller Relevant Context
        ↓
Better AI Reasoning
```

---

# 20. Consistency and One Source of Truth

Consistency should not be achieved by copying information everywhere.

The Engineering Vision establishes that important information should have a clear authoritative location and duplication should be minimized. :contentReference[oaicite:15]{index=15}

Therefore:

```text
One Authoritative Source
          ↓
Derived Representations
          ↓
Consistent Usage
```

is preferable to:

```text
Multiple Independent Sources
          ↓
Potential Divergence
```

---

# 21. Consistency and Standards

Consistency should eventually be enforced through appropriate standards.

Examples may include:

- naming standards
- API standards
- error-handling standards
- testing standards
- documentation standards
- logging standards
- module standards

This document defines the principle.

The detailed rules belong to later AEOS standards.

---

# 22. Consistency Does Not Mean Uniformity

Uniformity can become harmful.

For example, forcing every module to use an identical implementation pattern may create unnecessary abstraction.

The correct question is:

> **Does this difference represent a meaningful requirement or merely an accidental inconsistency?**

Meaningful differences should remain.

Accidental differences should generally be reduced.

---

# 23. Consistency and Evolution

Existing conventions should not become permanent simply because they already exist.

If an established pattern is discovered to be harmful, it may be changed.

However, such changes should be deliberate.

A new convention should consider:

- existing usage
- migration cost
- compatibility
- maintainability
- business value
- developer impact
- AI impact

---

# 24. Consistency and Backward Compatibility

Consistency sometimes requires preserving existing contracts.

The architectural principles include:

> **Backward compatibility where practical.** :contentReference[oaicite:16]{index=16}

A change should therefore consider whether introducing a new convention would unnecessarily break consumers.

---

# 25. Consistency and Refactoring

Inconsistency can be a valid reason for refactoring.

For example:

```text
Module A → error handling pattern A
Module B → error handling pattern B
Module C → error handling pattern C
```

may justify standardization if the differences provide no meaningful value.

Refactoring should still be justified by actual engineering benefit.

---

# 26. Consistency Anti-Patterns

Avoid:

- arbitrary naming differences
- multiple names for the same business concept
- inconsistent API conventions
- unrelated error formats
- different module structures without justification
- duplicated sources of truth
- undocumented deviations
- inconsistent observability terminology
- introducing new patterns without evaluating existing ones
- enforcing uniformity where domain differences are meaningful

---

# 27. Consistency Review Questions

Before introducing a new pattern, ask:

1. Does an existing pattern already solve this problem?
2. If yes, why is a new pattern required?
3. Is the business concept genuinely different?
4. Does the terminology align with the ubiquitous language?
5. Will this increase cognitive load?
6. Will it affect AI-assisted development?
7. Will documentation need a separate explanation?
8. Does it create compatibility concerns?
9. Should the existing pattern itself be improved instead?

---

# 28. Consistency Checklist

- [ ] Business terminology is consistent.
- [ ] APIs use established conventions.
- [ ] Code follows appropriate existing patterns.
- [ ] Documentation uses the same conceptual vocabulary.
- [ ] Tests communicate the same domain terminology.
- [ ] Error behavior is predictable.
- [ ] Observability uses consistent terminology.
- [ ] Configuration follows established conventions.
- [ ] New patterns have a documented reason.
- [ ] Meaningful domain differences are preserved.
- [ ] Duplicate sources of truth are avoided.
- [ ] AI agents can discover and reuse established patterns.

---

# 29. Final Rule

> **Be consistent by default, deviate deliberately.**

Consistency should make the system easier to understand without preventing legitimate domain-specific behavior.

The objective is:

> **Predictability without unnecessary uniformity.**