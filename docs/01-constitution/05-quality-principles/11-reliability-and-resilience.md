---
document: Reliability and Resilience
id: AEC-QLT-011
version: 1.0.0
status: Draft
---

# Purpose

Define architectural and implementation practices that enable software to remain reliable during failures.

---

# Quality Decision

Failures are expected.

Systems shall degrade gracefully rather than fail catastrophically.

---

# Core Principles

- Retry only transient failures.
- Use bounded retries.
- Define timeouts explicitly.
- Apply circuit breakers.
- Ensure idempotency.
- Fail fast when recovery is impossible.
- Prefer graceful degradation.

---

# Standard Patterns

- Retry
- Timeout
- Circuit Breaker
- Bulkhead
- Rate Limiting
- Fallback
- Dead Letter Queue
- Idempotency Keys

---

# Mandatory Rules

- Every external call defines a timeout.
- Retry policies are documented.
- Critical operations are idempotent.
- Circuit breakers protect unstable dependencies.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Timeout Coverage | 100% |
| Retry Policy Coverage | 100% |
| Idempotent Operations | 100% |
| Cascading Failure Prevention | 100% |

---

# Engineering Decision

Reliability is achieved through preparation—not hope.

---

# Related Documents

- Error Handling
- Defensive Programming