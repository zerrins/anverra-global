---
document: Code Readability
id: AEC-DEV-004
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-DEV-001
  - AEC-DEV-002
  - AEC-DEV-003
---

# Purpose

Define the readability standards that govern every implementation within the Anverra Global platform.

Readable software minimizes cognitive effort, reduces engineering mistakes, accelerates onboarding, and improves long-term maintainability.

Readability is considered an engineering quality attribute rather than a stylistic preference.

---

# Intent

Software should communicate intent clearly.

Source code should explain business behavior through structure, naming, and organization rather than requiring extensive comments or external documentation.

Every engineer and AI agent shall prioritize readability over brevity or cleverness.

---

# Problem Statement

Poor readability introduces hidden engineering costs.

Common symptoms include:

- unclear names,
- deeply nested logic,
- inconsistent formatting,
- long methods,
- unrelated responsibilities,
- unnecessary abstractions,
- magic numbers,
- confusing control flow,
- duplicated logic.

These issues increase the time required to understand, modify, review, and test software.

Poor readability also increases the likelihood of introducing defects.

---

# Development Decision

Readability shall be treated as a primary implementation objective.

Code shall be optimized for the next engineer who must understand and safely modify it.

Readable software has precedence over compact software.

---

# Rationale

Most engineering effort occurs after the initial implementation.

Every future enhancement, bug fix, refactoring, and review depends upon understanding existing code.

Improving readability directly reduces:

- onboarding effort,
- review effort,
- debugging effort,
- maintenance cost,
- production defects.

---

# Why This Matters to AI

AI models can generate syntactically correct implementations that are difficult to understand.

Without explicit readability standards, generated code often contains:

- generic names,
- unnecessary nesting,
- excessive abstraction,
- duplicated expressions,
- inconsistent structure,
- difficult control flow.

These standards ensure AI-generated code is immediately understandable by engineers.

---

# Readability Principles

Code shall communicate:

- intent,
- responsibility,
- behavior,
- ownership,
- business meaning.

The implementation should answer:

"What does this do?"

without requiring additional explanation.

---

# Naming

Names shall describe business meaning.

Avoid technical names when business terminology exists.

Good examples:

```
PolicyIssuer
```

```
CommissionCalculator
```

```
CustomerRegistrationService
```

Poor examples:

```
Helper
```

```
Processor
```

```
Util
```

```
Manager
```

---

# Methods

Methods shall describe business actions.

Good

```
issuePolicy()
```

```
calculateCommission()
```

```
approveClaim()
```

Avoid

```
execute()
```

```
process()
```

```
handle()
```

unless context makes the behavior completely obvious.

---

# Variables

Variables shall describe the information they represent.

Avoid:

```
x

obj

data

temp

value

list1
```

Prefer:

```
customer

policy

commissionRate

effectiveDate

approvedPolicies
```

---

# Control Flow

Control flow shall remain simple.

Prefer:

- guard clauses,
- early returns,
- explicit conditions.

Avoid deeply nested logic.

Example:

Instead of:

```
if (...)

    if (...)

        if (...)
```

Prefer returning early whenever possible.

---

# Method Size

Methods should perform one logical responsibility.

Large methods reduce readability.

Extract cohesive behavior into well-named methods.

Method extraction shall improve understanding rather than increase navigation.

---

# Class Organization

Classes should follow a predictable structure.

Recommended order:

1. Constants
2. Fields
3. Constructors
4. Public Methods
5. Protected Methods
6. Private Methods

Related behavior should remain physically close.

---

# Formatting

Formatting exists to improve understanding.

Consistent formatting is mandatory.

Blank lines should separate logical concepts.

Indentation should remain consistent.

Avoid unnecessary vertical whitespace.

---

# Expressions

Prefer expressive expressions.

Example:

Instead of:

```
status == 3
```

Prefer:

```
status == PolicyStatus.APPROVED
```

Business meaning should always be visible.

---

# Conditions

Conditions should read like business rules.

Good

```
customer.isEligibleForRenewal()
```

Poor

```
customer.getStatus() == 4
```

---

# Comments

Comments explain:

- intent,
- reasoning,
- architectural decisions,
- business constraints.

Comments shall never explain obvious code.

Bad:

```
Increment i by one.
```

Good:

```
Regulatory requirement:
Commission is calculated using the policy issue date.
```

---

# Mandatory Rules

Business terminology shall be preserved.

Method names describe behavior.

Classes remain cohesive.

Variables use meaningful names.

Control flow remains understandable.

Formatting remains consistent.

Magic numbers are prohibited.

Hidden behavior is prohibited.

---

# Recommended Practices

Use expressive names.

Reduce nesting.

Prefer early returns.

Prefer immutable variables where practical.

Group related behavior.

Remove dead code.

Improve nearby readability while making changes.

---

# Prohibited Practices

Do not abbreviate business terminology.

Do not use meaningless names.

Do not create excessively long methods.

Do not use deeply nested conditions.

Do not introduce unnecessary indirection.

Do not use magic numbers.

Do not leave commented-out code.

Do not hide business behavior behind utility methods.

---

# Allowed Exceptions

Well-known abbreviations may be used.

Examples:

```
URL

UUID

JSON

SQL

JWT
```

Generated code may temporarily violate formatting rules before automatic formatting.

Performance-critical implementations may use optimized structures when documented and approved.

---

# AI Guidance

Before generating code, AI shall verify:

- Can an engineer understand this without explanation?
- Are names business-oriented?
- Is nesting minimal?
- Are methods cohesive?
- Does the structure communicate intent?
- Can readability be improved by extracting methods?
- Are comments necessary?

AI shall optimize for clarity over compactness.

---

# Implementation Guidance

During implementation:

1. Choose business-oriented names.
2. Keep methods focused.
3. Simplify control flow.
4. Remove duplication.
5. Eliminate dead code.
6. Improve nearby readability.
7. Review naming.
8. Review formatting.
9. Review cohesion.

Readability review is mandatory before implementation is considered complete.

---

# Review Checklist

Reviewers shall verify:

- Are names meaningful?
- Is business terminology preserved?
- Are methods cohesive?
- Is nesting acceptable?
- Is control flow understandable?
- Are comments valuable?
- Are magic numbers eliminated?
- Can another engineer understand this quickly?
- Does the code communicate intent?

---

# Examples

## Good

```
policy.issue()
```

Simple.

Business-oriented.

Immediate understanding.

---

```
customer.isEligibleForRenewal()
```

Expresses business intent.

---

```
commission.calculate(policy)
```

Clear domain language.

---

## Bad

```
process()
```

Unknown behavior.

---

```
execute()
```

No business meaning.

---

```
flag

temp

data

obj
```

Meaningless names.

---

```
if

↓

if

↓

if

↓

if
```

Deep nesting.

---

# Anti-patterns

Magic Numbers

Boolean Flag Explosion

Arrow Code

Deep Nesting

Temporary Variables Everywhere

Cryptic Naming

Comment Driven Development

Long Methods

Long Classes

Copy-Paste Readability

---

# Engineering Decision

Readability is a constitutional quality attribute.

Software that is difficult to understand shall not be considered production quality.

Engineering teams and AI agents shall optimize for human understanding before implementation convenience.

---

# References

- Robert C. Martin — Clean Code
- Steve McConnell — Code Complete
- Martin Fowler — Refactoring

---

# Related Documents

- Development Philosophy
- Clean Code
- SOLID Principles
- Refactoring
- Development Review Checklist
- Architecture Principles