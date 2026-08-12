---
document: Test Data Management
id: AEC-QLT-006
version: 1.0.0
status: Draft
---

# Purpose

Define standards for creating, managing, securing, and maintaining test data.

---

# Intent

Reliable testing requires reliable data.

Test data shall be deterministic, isolated, repeatable, and safe.

---

# Quality Decision

Production data shall never be used directly in testing unless anonymized and formally approved.

Synthetic test data is the preferred default.

---

# Why This Matters to AI

AI frequently creates inconsistent fixtures and unrealistic datasets.

AI shall generate reusable, business-representative test data.

---

# Test Data Principles

- Deterministic
- Repeatable
- Minimal
- Isolated
- Realistic
- Secure

---

# Mandatory Rules

- No production secrets.
- No real customer PII.
- Every test owns its data.
- Test data shall be disposable.
- Test fixtures shall be version-controlled.

---

# Recommended Practices

- Builder Pattern
- Object Mother Pattern
- Factory Methods
- Synthetic datasets
- Randomized identifiers with deterministic seeds

---

# Prohibited Practices

- Shared mutable datasets
- Manual database preparation
- Hardcoded production IDs
- Environment-specific assumptions

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Production PII | 0 |
| Test Isolation | 100% |
| Deterministic Execution | 100% |
| Data Cleanup Success | 100% |

---

# Engineering Decision

Test data is an engineering asset and shall be maintained with the same discipline as production code.

---

# Related Documents

- Unit Testing
- Integration Testing