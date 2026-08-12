---
document: End-to-End Testing
id: AEC-QLT-005
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-QLT-002
---

# Purpose

Define standards for validating complete business workflows across the entire platform.

End-to-End (E2E) testing verifies that independently correct components work together to deliver business value from the user's perspective.

---

# Intent

E2E tests validate business outcomes rather than implementation details.

They provide confidence that production deployments satisfy customer expectations.

---

# Problem Statement

Systems with strong unit and integration testing can still fail because of:

- Incorrect workflow orchestration
- UI/API mismatches
- Authentication failures
- Configuration issues
- Environment differences
- Missing integrations

E2E testing addresses these gaps.

---

# Quality Decision

E2E tests shall validate only the most critical business journeys.

They shall complement—not replace—unit and integration testing.

---

# Rationale

E2E tests are valuable but expensive.

They should verify customer-facing workflows that deliver business value.

---

# Why This Matters to AI

AI frequently generates E2E tests for every scenario.

This increases execution time without improving confidence.

AI shall generate E2E tests only for critical business workflows.

---

# Quality Principles

End-to-End tests shall be:

- Business-oriented
- Stable
- Repeatable
- Environment-independent
- Maintainable

---

# Scope

Typical workflows include:

- Customer Registration
- Login
- Policy Purchase
- Premium Payment
- Claim Submission
- Claim Approval
- Commission Calculation
- Policy Renewal

---

# Mandatory Rules

- Validate complete business journeys.
- Execute against production-like environments.
- Avoid implementation-specific assertions.
- Keep tests deterministic.
- Failures must identify the affected business workflow.

---

# Recommended Practices

- Reuse test flows.
- Isolate test data.
- Keep scenarios focused.
- Automate execution in CI/CD.
- Test only high-value workflows.

---

# Prohibited Practices

- Testing every UI interaction.
- Duplicating integration tests.
- Depending on execution order.
- Hardcoded environment data.
- Large monolithic test scripts.

---

# Allowed Exceptions

Exploratory testing may supplement automated E2E testing for new features before automation is implemented.

---

# AI Guidance

AI shall:

- Identify critical business workflows.
- Generate maintainable E2E scenarios.
- Avoid duplicate verification already covered by lower-level tests.
- Prefer stable selectors and contracts.

---

# Implementation Guidance

1. Define business workflow.
2. Prepare isolated test data.
3. Execute end-to-end journey.
4. Validate expected business outcome.
5. Clean up test data.
6. Record execution metrics.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Critical Workflow Coverage | 100% |
| Flaky Tests | 0 |
| Environment Independence | 100% |
| Execution Success Rate | ≥99% |
| Mean Execution Time | <30 minutes |

---

# Review Checklist

- Does the test validate business value?
- Is the workflow complete?
- Is the environment production-like?
- Is the test deterministic?
- Is duplicate coverage avoided?

---

# Anti-patterns

- UI Pixel Testing
- Record-and-Playback Testing
- Giant Test Scripts
- Environment-Coupled Tests
- Sequential Dependencies

---

# Constitutional Compliance Matrix

| Requirement | Status |
|-------------|--------|
| Business Workflow Validation | Mandatory |
| Automation | Mandatory |
| Determinism | Mandatory |
| Environment Independence | Mandatory |

---

# Engineering Decision

End-to-End testing validates customer value—not implementation details.

---

# References

- Martin Fowler – Test Pyramid
- Google Testing Blog

---

# Related Documents

- Testing Strategy
- Integration Testing
- Definition of Done    