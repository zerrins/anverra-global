---
document: Clean Code
id: AEC-DEV-002
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-DEV-001
  - AEC-ARC-000
---

# Purpose

Define the coding principles that ensure software remains readable, maintainable, testable, and evolvable throughout its lifecycle.

Clean Code is not a stylistic preference. It is an engineering discipline that reduces complexity, improves collaboration, and minimizes the long-term cost of software ownership.

Every engineer and AI agent shall produce code that is intentionally clean rather than accidentally functional.

---

# Intent

Code is read significantly more often than it is written.

The primary audience of source code is not the compiler—it is the next engineer who must understand, debug, extend, and maintain it.

The objective of Clean Code is to minimize the cognitive effort required to understand software.

---

# Problem Statement

Poor implementation quality leads to:

- increasing technical debt,
- duplicated logic,
- fragile implementations,
- hidden dependencies,
- inconsistent patterns,
- difficult debugging,
- slow feature delivery.

Most software systems become difficult to evolve because implementation quality continuously degrades.

Without explicit standards, code quality naturally declines over time.

---

# Development Decision

Anverra Global adopts Clean Code as the default implementation philosophy.

Every implementation shall prioritize readability, maintainability, and simplicity over cleverness or personal coding style.

Working code is the minimum requirement.

Production-quality code is the constitutional requirement.

---

# Rationale

Software has a long lifespan.

Most engineering effort is spent maintaining and evolving existing systems rather than creating new ones.

Every implementation decision should reduce the cost of future change.

Readable software:

- reduces defects,
- improves onboarding,
- simplifies reviews,
- accelerates delivery,
- enables AI-assisted engineering.

---

# Why This Matters to AI

AI models naturally optimize for generating syntactically correct code.

Without explicit guidance, generated implementations may:

- become unnecessarily complex,
- duplicate logic,
- introduce inconsistent naming,
- violate architectural conventions,
- optimize for completion rather than maintainability.

Clean Code principles ensure AI generates software that engineers are willing to own.

---

# Principles

Clean code shall be:

- Simple
- Readable
- Intentional
- Consistent
- Maintainable
- Testable
- Explicit
- Predictable

---

# Mandatory Rules

## Naming

Names shall clearly express business intent.

Avoid abbreviations unless universally understood.

Variables, methods, classes, packages, APIs, and modules shall use meaningful names.

Business terminology takes precedence over technical terminology.

---

## Functions

Functions shall perform one responsibility.

Functions shall remain small.

Function names shall describe behavior.

Avoid hidden side effects.

---

## Classes

Classes shall have one primary responsibility.

Large classes shall be decomposed.

Classes shall expose behavior rather than data.

---

## Duplication

Business logic shall never be duplicated intentionally.

Common behavior shall be extracted only after confirming that the abstraction represents a stable business concept.

---

## Complexity

Implementation complexity shall be minimized.

Prefer explicit solutions over clever solutions.

Every additional abstraction must justify its existence.

---

## Comments

Code should explain itself whenever practical.

Comments explain:

- why,
- business context,
- architectural decisions,
- non-obvious behavior.

Comments shall never compensate for poorly written code.

---

## Formatting

Formatting shall remain consistent throughout the repository.

Consistent formatting improves readability and AI generation quality.

---

## Error Handling

Errors shall be handled explicitly.

Hidden failures are prohibited.

Unexpected situations shall be observable.

---

## Dependencies

Dependencies shall remain explicit.

Avoid hidden coupling.

Favor dependency injection.

---

# Recommended Practices

Prefer immutable objects.

Prefer composition over inheritance.

Prefer explicit construction.

Prefer early validation.

Prefer guard clauses.

Prefer expressive domain behavior.

Remove dead code.

Delete unused abstractions.

Refactor continuously.

Improve nearby code when making changes.

---

# Prohibited Practices

Do not write methods that perform multiple unrelated tasks.

Do not create "God Classes."

Do not expose mutable internal state unnecessarily.

Do not duplicate business rules.

Do not ignore compiler warnings.

Do not suppress exceptions without justification.

Do not use magic numbers.

Do not create deeply nested conditional logic.

Do not introduce unnecessary inheritance.

Do not optimize prematurely.

---

# Allowed Exceptions

Generated code may temporarily violate formatting conventions when regenerated automatically.

Performance-critical sections may sacrifice readability only when:

- measured,
- justified,
- documented,
- approved during review.

Migration code may temporarily contain duplicated logic while transitioning between implementations.

---

# AI Guidance

Before generating implementation, AI shall verify:

- Does the name express business intent?
- Does each method have one responsibility?
- Is duplication introduced?
- Is complexity minimized?
- Can another engineer understand this immediately?
- Are comments necessary?
- Does architecture remain respected?

AI shall prefer readability over brevity.

AI shall never intentionally generate obscure implementations.

---

# Implementation Guidance

For every implementation:

1. Use business terminology.
2. Create expressive names.
3. Keep methods focused.
4. Minimize dependencies.
5. Eliminate duplication.
6. Simplify logic.
7. Remove dead code.
8. Validate readability.
9. Add tests.
10. Perform self-review.

Implementation is complete only after readability has been evaluated.

---

# Review Checklist

Reviewers shall verify:

- Are names meaningful?
- Does every class have one responsibility?
- Are methods appropriately sized?
- Is duplication present?
- Is complexity justified?
- Are dependencies explicit?
- Is formatting consistent?
- Are comments meaningful?
- Can the implementation be understood quickly?
- Would another engineer confidently modify this code?

---

# Examples

## Good

```
policy.issue()
```

Business intent is immediately clear.

---

```
customer.updateAddress(newAddress)
```

Behavior is explicit.

---

```
commission.calculate(policy)
```

Domain language is preserved.

---

## Bad

```
process()
```

Purpose unclear.

---

```
doStuff()
```

Behavior unknown.

---

```
ManagerUtil
```

No clear responsibility.

---

```
Helper
```

Meaningless abstraction.

---

# Anti-patterns

God Class

God Method

Boolean Flag Explosion

Deep Nesting

Magic Numbers

Primitive Obsession

Copy-Paste Programming

Shotgun Surgery

Feature Envy

Long Parameter Lists

Comment-Driven Code

Utility Everything

Framework-Driven Naming

---

# Engineering Decision

Readability has precedence over cleverness.

Maintainability has precedence over implementation speed.

Business clarity has precedence over technical elegance.

---

# References

- Robert C. Martin — Clean Code
- Steve McConnell — Code Complete
- Martin Fowler — Refactoring

---

# Related Documents

- Development Philosophy
- SOLID Principles
- Code Readability
- Refactoring
- Development Review Checklist
- Architecture Principles