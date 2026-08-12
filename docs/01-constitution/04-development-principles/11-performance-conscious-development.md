---
document: Performance Conscious Development
id: AEC-DEV-011
version: 1.0.0
status: Draft
---

# Purpose

Ensure software delivers acceptable performance without sacrificing correctness, readability, or maintainability.

---

# Intent

Performance is an engineering requirement, not an excuse for premature optimization.

---

# Development Decision

Correctness first.

Maintainability second.

Performance optimization only after measurement.

---

# Why This Matters to AI

AI frequently performs premature optimization.

AI shall optimize only when measurable evidence exists.

---

# Performance Principles

Optimize:

- Algorithms
- Database access
- Network calls
- Memory allocation
- Concurrency
- Serialization

Avoid optimizing code that is not a proven bottleneck.

---

# Mandatory Rules

- Measure before optimizing.
- Eliminate N+1 queries.
- Batch expensive operations.
- Avoid repeated remote calls.
- Profile production workloads.

---

# Recommended Practices

- Lazy loading where appropriate.
- Pagination.
- Caching.
- Connection pooling.
- Streaming large datasets.

---

# Prohibited Practices

- Premature optimization.
- Micro-optimizations without evidence.
- Sacrificing readability.
- Hidden caching.

---

# AI Guidance

AI shall always ask:

"Has a bottleneck been measured?"

---

# Review Checklist

- Is optimization justified?
- Is performance measurable?
- Is readability preserved?
- Is memory usage acceptable?

---

# Engineering Decision

Measured performance always takes precedence over assumed performance.

---

# Constitutional Compliance Matrix

| Requirement | Status |
|------------|--------|
| Measure First | Mandatory |
| Readability | Mandatory |
| Optimization | Evidence Based |
| Profiling | Mandatory |

---

# Related Documents

- Refactoring
- API Design