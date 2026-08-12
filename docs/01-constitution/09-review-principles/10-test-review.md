---
document: Test Review
id: AEC-REV-010
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering Quality
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-003
  - AEC-REV-004
  - AEC-QLT-000
---

# Purpose

Define the principles, standards, workflow, and completion criteria for reviewing engineering tests and verification strategies.

Test review determines whether the available verification provides sufficient evidence that the change behaves as intended and fails safely.

---

# Intent

Test review should answer:

- What behavior must be verified?
- Which tests provide evidence?
- Are important edge cases covered?
- Are failure scenarios covered?
- Are tests meaningful?
- Are tests stable?
- Is the testing level appropriate?
- What remains unverified?

---

# Constitutional Decision

Engineering changes shall have verification appropriate to their risk and behavior.

Passing tests alone do not establish correctness.

---

# Test Review Philosophy

Testing provides evidence.

It does not provide absolute proof.

A useful model is:

```text
Requirement
    ↓
Expected Behavior
    ↓
Verification Strategy
    ↓
Test Evidence
    ↓
Confidence
```

---

# Test Strategy

The test strategy should match the change.

Possible levels include:

- Unit.
- Component.
- Integration.
- Contract.
- End-to-end.
- Performance.
- Security.
- Resilience.
- Operational verification.

---

# Test Pyramid

Where applicable, prefer a healthy distribution:

```text
       E2E
      /   \
 Integration
    /     \
   Unit / Component
```

The exact distribution depends on system architecture.

---

# Requirements Traceability

Important requirements should map to verification.

Example:

```text
Requirement R1
      ↓
Test T1
      ↓
Evidence
```

Not every requirement requires a single test, but important behavior should have explicit evidence.

---

# Positive Testing

Verify expected valid behavior.

Examples:

- Valid input.
- Successful authorization.
- Successful processing.
- Expected response.

---

# Negative Testing

Verify invalid or unexpected behavior.

Examples:

- Invalid input.
- Unauthorized access.
- Missing dependency.
- Timeout.
- Duplicate request.

---

# Boundary Testing

Review values at:

- Minimum.
- Maximum.
- Just below.
- Just above.

Boundary errors are common sources of defects.

---

# State Testing

Where state exists, test:

- Initial state.
- Valid transitions.
- Invalid transitions.
- Repeated operations.
- Recovery.

---

# Failure Testing

Where meaningful, test:

- Dependency failure.
- Timeout.
- Network failure.
- Database failure.
- Partial processing.
- Restart.

---

# Concurrency Testing

For concurrent systems, consider:

- Race conditions.
- Ordering.
- Duplicate execution.
- Locking.
- Contention.

---

# Integration Testing

Integration tests should validate interactions between real components where those interactions are important.

Avoid mocking away the behavior that actually needs verification.

---

# Contract Testing

Contract tests should validate important interfaces between:

- Services.
- APIs.
- Event producers and consumers.

---

# End-to-End Testing

E2E tests should validate critical user or business workflows.

They should not become the only source of confidence.

---

# Test Isolation

Tests should avoid unnecessary dependence on:

- Execution order.
- Shared mutable state.
- External availability.
- Time.
- Randomness.

When such dependencies are necessary, they should be controlled and understood.

---

# Test Determinism

Tests should produce predictable results.

Flaky tests should be investigated rather than normalized.

---

# Test Data

Test data should:

- Be representative where needed.
- Avoid unnecessary sensitive data.
- Be isolated.
- Be reproducible.

---

# Test Maintainability

Tests should be:

- Readable.
- Focused.
- Stable.
- Relevant.

Avoid tests that break whenever implementation details change despite behavior remaining correct.

---

# Test Coverage

Coverage metrics can identify untested areas.

Coverage percentage alone does not establish test quality.

Prefer meaningful behavioral coverage.

---

# Mutation Testing

Mutation testing may be useful for critical logic where confidence in test effectiveness is important.

It should be applied selectively.

---

# Performance Testing

Performance-sensitive changes should use appropriate evidence.

Consider:

- Latency.
- Throughput.
- Resource usage.
- Scaling.

Do not rely solely on local developer-machine results for production-scale claims.

---

# Security Testing

Security-sensitive changes may require:

- Authorization tests.
- Input validation tests.
- Dependency scanning.
- Static analysis.
- Penetration testing.

---

# Resilience Testing

Critical systems may require testing of:

- Dependency failures.
- Timeouts.
- Restart.
- Network partitions.
- Resource exhaustion.

---

# Test Review Workflow

```text
Requirement
    ↓
Behavior
    ↓
Risk
    ↓
Test Strategy
    ↓
Implementation
    ↓
Test Execution
    ↓
Evidence Review
    ↓
Gap Identification
    ↓
Approval
```

---

# Test Evidence

Evidence may include:

- Test results.
- Coverage.
- Contract validation.
- Performance results.
- Security scans.
- Resilience tests.

Evidence should be appropriate to the claim being made.

---

# Test Gaps

A test gap should identify:

- Missing behavior.
- Risk.
- Recommended verification.

Example:

```text
Current tests cover successful payment processing.

They do not cover duplicate requests.

Duplicate processing could create financial impact.

Add idempotency verification.
```

---

# Test Review and Code Review

Code review evaluates implementation.

Test review evaluates whether verification is sufficient.

A reviewer should challenge:

```text
"Does this test prove the behavior that matters?"
```

rather than only:

```text
"Is there a test?"
```

---

# Test Review and Design Review

Design review should influence test strategy.

Example:

```text
Design requires eventual consistency

↓

Tests should verify expected consistency behavior
```

---

# Test Review and Operations

Production changes may require:

- Deployment tests.
- Smoke tests.
- Health checks.
- Rollback verification.
- Recovery testing.

---

# AI-Assisted Test Review

AI may assist with:

- Identifying missing test cases.
- Generating edge cases.
- Reviewing test-to-code relationships.
- Detecting duplicated tests.
- Suggesting failure scenarios.

AI-generated tests must be reviewed for correctness and usefulness.

---

# Generated Tests

Generated tests should not be accepted merely because they increase coverage.

The test must meaningfully detect incorrect behavior.

---

# Flaky Tests

Flaky tests should be:

- Identified.
- Investigated.
- Fixed or quarantined according to policy.

Permanent flakiness reduces trust in the test system.

---

# Test Exceptions

Exceptions may be appropriate when:

- Behavior is difficult to reproduce.
- Testing cost is disproportionate.
- External systems cannot be controlled.

Exceptions should document:

- Risk.
- Reason.
- Compensating controls.

---

# Mandatory Rules

Test review shall:

- Evaluate verification against requirements.
- Consider important failure paths.
- Consider edge cases.
- Evaluate test quality.
- Identify meaningful test gaps.
- Avoid treating coverage percentage as sufficient evidence.

---

# Recommended Practices

Test behavior rather than implementation details.

Automate tests.

Keep tests deterministic.

Use production-like integration testing where meaningful.

Review test gaps explicitly.

---

# Prohibited Practices

Do not:

- Treat test existence as proof of quality.
- Ignore flaky tests indefinitely.
- Use coverage percentage as the only quality metric.
- Generate tests without reviewing their assertions.
- Mock away the behavior that needs verification.

---

# Definition of Done

Test review is complete when:

- Requirements have appropriate verification.
- Important behavior is covered.
- Relevant failure paths are tested.
- Test results are available.
- Significant gaps are understood.
- Required specialist tests are complete.
- Blocking test findings are resolved.

---

# Review Checklist

### Requirements

- [ ] Requirements mapped
- [ ] Expected behavior defined

### Coverage

- [ ] Happy path
- [ ] Negative path
- [ ] Boundaries
- [ ] Failure behavior
- [ ] State transitions

### Quality

- [ ] Tests meaningful
- [ ] Tests deterministic
- [ ] Tests maintainable
- [ ] Flakiness addressed

### Specialized

- [ ] Integration
- [ ] Contract
- [ ] Performance
- [ ] Security
- [ ] Resilience

### Completion

- [ ] Evidence reviewed
- [ ] Gaps understood
- [ ] Required tests passed

---

# Engineering Decision

Test review exists to ensure that engineering confidence is supported by meaningful evidence.

The objective is not maximum test count or coverage percentage.

The objective is sufficient verification of the behaviors and risks that matter.