---
document: Integration Testing
id: AEC-QLT-004
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
---

# Purpose

Define standards for validating collaboration between components.

---

# Intent

Integration tests verify that independently correct components collaborate correctly.

---

# Quality Decision

Integration tests validate boundaries—not business logic already covered by unit tests.

---

# Why This Matters to AI

AI frequently duplicates unit tests as integration tests.

Integration testing should validate collaboration.

---

# Integration Scope

Examples:

- Spring Boot
- PostgreSQL
- Kafka
- Redis
- Azure Services
- External APIs
- File Storage

---

# Infrastructure

Prefer:

- Testcontainers
- Embedded services
- Disposable environments

Avoid shared test environments.

---

# Mandatory Rules

Use production-like infrastructure.

Test real integration points.

Keep tests isolated.

Clean test data.

Avoid dependency on execution order.

---

# Test Categories

Persistence

Messaging

Caching

REST

External APIs

Configuration

---

# Recommended Practices

Test one integration boundary.

Reuse infrastructure.

Keep datasets small.

Verify transactions.

---

# Prohibited Practices

Mocking the component being integrated.

Using production databases.

Large shared datasets.

Environment-dependent tests.

---

# AI Guidance

Generate integration tests for:

Repository

Kafka

Redis

REST

Configuration

Transactions

---

# Quality Metrics

| Metric | Target |
|---------|--------|
| Pass Rate | 100% |
| Flaky Tests | 0 |
| Environment Independence | 100% |
| Test Isolation | 100% |

---

# Review Checklist

- Real infrastructure?
- Boundary tested?
- Environment independent?
- Data isolated?
- Production representative?

---

# Anti-patterns

Fake Integration Tests

Environment Coupling

Shared Databases

Hidden State

Overlapping Unit Tests

---

# Constitutional Compliance Matrix

| Requirement | Status |
|------------|--------|
| Real Integration | Mandatory |
| Environment Isolation | Mandatory |
| Production Similarity | Mandatory |

---

# Engineering Decision

Integration tests validate collaboration—not implementation.

---

# References

- Testcontainers Documentation
- Martin Fowler — Integration Testing

---

# Related Documents

- Testing Strategy
- Unit Testing
- End-to-End Testing