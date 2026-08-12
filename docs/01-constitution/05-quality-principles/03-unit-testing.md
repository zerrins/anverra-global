---
document: Unit Testing
id: AEC-QLT-003
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
---

# Purpose

Define standards for unit testing business logic.

---

# Intent

Unit tests provide the fastest feedback and the highest engineering confidence.

They validate business behavior in complete isolation.

---

# Quality Decision

Business logic shall be validated primarily through unit tests.

Infrastructure shall not participate.

---

# Why This Matters to AI

AI shall generate unit tests for every business behavior.

Tests are not optional.

---

# Unit Test Principles

Unit tests shall be:

- Fast
- Independent
- Repeatable
- Deterministic
- Readable

---

# Scope

Unit tests validate:

- Aggregates
- Value Objects
- Domain Services
- Specifications
- Business Policies
- Utility Components

---

# Mandatory Rules

No database.

No network.

No filesystem.

No Kafka.

No Redis.

No Spring Context.

---

# Test Structure

Preferred pattern:

Arrange

↓

Act

↓

Assert

---

# Naming Convention

```
shouldIssuePolicyWhenCustomerIsEligible()

shouldRejectDuplicateCustomer()

shouldCalculateCommissionCorrectly()
```

---

# Assertions

Assertions validate business outcomes.

Avoid asserting implementation details.

---

# Mocking

Mock only external collaborators.

Never mock business behavior.

Avoid excessive mocking.

---

# Edge Cases

Every unit should test:

- Null input
- Boundary values
- Invalid values
- Exceptions
- Business failures

---

# Recommended Practices

One behavior per test.

Keep tests small.

Use builders.

Prefer immutable fixtures.

---

# Prohibited Practices

Testing private methods.

Random data.

Time-dependent tests.

Hidden dependencies.

Large fixture setup.

---

# AI Guidance

AI shall generate:

Positive tests.

Negative tests.

Boundary tests.

Exception tests.

---

# Quality Metrics

| Metric | Target |
|---------|--------|
| Execution Time | <5 minutes |
| Mutation Score | ≥75% |
| Flaky Tests | 0 |
| Average Test Length | <40 lines |

---

# Review Checklist

- Fast?
- Independent?
- Readable?
- Business-oriented?
- Deterministic?

---

# Anti-patterns

Testing Getters

Testing Framework

Mock Everything

Huge Fixtures

Slow Unit Tests

---

# Constitutional Compliance Matrix

| Requirement | Status |
|-------------|--------|
| Isolation | Mandatory |
| Repeatability | Mandatory |
| Business Behavior | Mandatory |

---

# Engineering Decision

Business logic shall be primarily protected through unit tests.

---

# References

- Kent Beck — TDD
- xUnit Patterns

---

# Related Documents

- Testing Strategy
- Integration Testing