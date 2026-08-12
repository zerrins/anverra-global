---
document: Defensive Programming
id: AEC-DEV-005
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-DEV-001
  - AEC-DEV-002
  - AEC-ARC-004
---

# Purpose

Define the principles for writing software that behaves predictably in the presence of invalid input, unexpected conditions, integration failures, configuration mistakes, and programming errors.

Defensive Programming reduces production defects by assuming that incorrect inputs and failures are inevitable.

---

# Intent

Software should fail safely rather than unpredictably.

Every implementation should actively protect business integrity by validating assumptions, detecting invalid states early, and preventing data corruption.

The objective is not to hide failures but to detect them as early as possible and fail in a controlled, observable manner.

---

# Problem Statement

Many production incidents originate from assumptions that later become false.

Examples include:

- Null values where data was assumed to exist.
- Invalid user input.
- Missing configuration.
- Unexpected integration responses.
- Concurrent modifications.
- Invalid business state transitions.
- Corrupted data.

These failures frequently propagate through the system because validation occurs too late.

---

# Development Decision

Anverra Global adopts Defensive Programming as the default implementation strategy.

Software shall validate assumptions at system boundaries, preserve business invariants, and detect incorrect behavior as early as possible.

Defensive Programming protects the system without replacing proper domain modeling.

---

# Rationale

Preventing invalid state is significantly less expensive than correcting corrupted state.

Early validation improves:

- Reliability
- Security
- Debuggability
- Maintainability
- User experience

---

# Why This Matters to AI

AI-generated code often assumes that external inputs are valid unless explicitly instructed otherwise.

Without defensive rules, generated implementations may:

- Trust client input.
- Ignore null handling.
- Skip boundary validation.
- Propagate invalid state.
- Produce unpredictable failures.

These principles ensure AI generates resilient software rather than optimistic software.

---

# Principles

Software shall:

- Validate inputs.
- Preserve invariants.
- Fail early.
- Fail explicitly.
- Remain observable.
- Protect business integrity.

---

# Validation Strategy

Validation occurs at multiple levels.

## Boundary Validation

Validate all external inputs.

Examples:

- REST requests
- Kafka messages
- File imports
- External APIs
- User interfaces

---

## Business Validation

Validate business rules inside the Domain.

Examples:

- Policy cannot be issued without an approved customer.
- Commission percentage cannot be negative.
- Expired policies cannot be renewed.

---

## Infrastructure Validation

Validate configuration.

Examples:

- Missing environment variables.
- Invalid connection strings.
- Missing secrets.

---

# Mandatory Rules

Validate all external input.

Never trust client-provided data.

Preserve aggregate invariants.

Detect invalid state immediately.

Reject inconsistent business operations.

Never silently ignore failures.

---

# Recommended Practices

Prefer immutable objects.

Validate constructor arguments.

Use Value Objects for validation.

Prefer fail-fast behavior.

Use explicit exceptions.

Log unexpected situations.

---

# Prohibited Practices

Do not suppress exceptions.

Do not continue after detecting invalid business state.

Do not trust client-side validation.

Do not ignore compiler warnings.

Do not return partially valid business objects.

Do not use null as a business value when a Value Object is appropriate.

---

# Allowed Exceptions

Internal performance-critical code may omit repeated validation when correctness is already guaranteed by preceding layers.

Exceptions shall be documented.

---

# AI Guidance

AI shall:

- Validate all external inputs.
- Preserve business invariants.
- Reject invalid state.
- Never silently recover from unknown failures.
- Prefer explicit validation over implicit assumptions.

---

# Implementation Guidance

Implementation sequence:

1. Validate external input.
2. Convert to domain model.
3. Validate business rules.
4. Execute business behavior.
5. Persist valid state.
6. Publish events.
7. Handle failures explicitly.

---

# Review Checklist

Reviewers shall verify:

- Are inputs validated?
- Are business invariants protected?
- Are failures explicit?
- Are invalid states rejected?
- Are assumptions documented?
- Can corrupted state be introduced?

---

# Examples

Good

Customer age validated before policy issuance.

Good

Commission percentage validated inside Value Object.

Bad

Database exception relied upon for business validation.

Bad

Null values accepted without explanation.

---

# Anti-patterns

Trusting Client Input

Silent Failure

Boolean Error Codes

Null Propagation

Ignoring Validation

Exception Swallowing

---

# Engineering Decision

Defensive Programming protects business integrity.

Validation belongs at boundaries and within the domain—not scattered throughout the application.

---

# References

- Steve McConnell — Code Complete
- Robert C. Martin — Clean Code

---

# Related Documents

- Error Handling
- Domain Implementation
- Clean Code
- Development Review Checklist