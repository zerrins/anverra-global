---
document: Code Coverage
id: AEC-QLT-007
version: 1.0.0
status: Draft
---

# Purpose

Define how code coverage is measured and interpreted.

---

# Intent

Coverage measures confidence—not quality.

High coverage alone does not indicate good tests.

---

# Quality Decision

Meaningful coverage shall take precedence over percentage targets.

---

# Coverage Targets

| Layer | Target |
|--------|--------:|
| Domain | ≥95% |
| Application | ≥90% |
| Infrastructure | ≥80% |
| Overall | ≥90% |

---

# Mutation Testing

Mutation score target:

≥75%

Mutation testing is preferred over line coverage where practical.

---

# Mandatory Rules

- Every bug fix adds regression coverage.
- Critical business logic requires high coverage.
- Untested critical code shall not be merged.

---

# Prohibited Practices

- Writing meaningless tests to increase coverage.
- Ignoring uncovered critical paths.
- Measuring only line coverage.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Overall Coverage | ≥90% |
| Domain Coverage | ≥95% |
| Mutation Score | ≥75% |
| Critical Paths | 100% |

---

# Engineering Decision

Coverage is evidence—not proof—of software quality.

---

# Related Documents

- Unit Testing
- Testing Strategy