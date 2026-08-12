---
document: Quality Governance
id: AEC-GOV-006
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering Quality
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-003
  - AEC-REV-010
  - AEC-QLT-000
---

# Purpose

Define the governance model for engineering quality across the Anverra Engineering Operating System (AEOS).

Quality governance establishes expectations, ownership, measurement, escalation, and continuous improvement for engineering quality.

---

# Intent

Quality governance should ensure:

- Quality is an engineering responsibility.
- Minimum quality expectations are clear.
- Testing is appropriate to risk.
- Defects are visible.
- Quality trends are understood.
- Production feedback improves engineering practices.

---

# Constitutional Decision

Quality is a shared engineering responsibility.

Specialist quality functions provide expertise, standards, and tooling, but quality shall not be delegated entirely to a separate testing function.

---

# What Is Engineering Quality?

Engineering quality includes:

```text
Correctness
Reliability
Security
Maintainability
Performance
Usability
Operability
Testability
```

The exact dimensions depend on the system.

---

# Quality Principles

## 1. Quality Is Built In

Quality should be addressed during:

```text
Requirements
    ↓
Design
    ↓
Implementation
    ↓
Testing
    ↓
Deployment
    ↓
Operations
```

---

# 2. Risk-Based Quality

Testing and quality controls should match:

- Impact.
- Complexity.
- Criticality.
- Change size.
- Failure consequences.

---

# 3. Evidence-Based Quality

Quality decisions should use evidence where practical.

Examples:

- Test results.
- Production metrics.
- Defect trends.
- Performance measurements.
- Security findings.

---

# 4. Prevention Over Detection

Prefer preventing defects over relying entirely on late detection.

Example:

```text
Clear API Contract
    ↓
Validation
    ↓
Automated Tests
```

is preferable to discovering contract failures only in production.

---

# Quality Ownership

Engineering teams own the quality of the systems they build and operate.

Roles may include:

```text
Engineer
→ Implementation quality

Technical Lead
→ Technical coherence

System Owner
→ System quality

Quality Specialist
→ Quality expertise and standards
```

---

# Quality Gates

Quality gates may include:

- Compilation.
- Unit tests.
- Integration tests.
- Static analysis.
- Security scanning.
- API validation.
- Performance validation.

Only meaningful and reliable gates should block delivery.

---

# Quality Gate Design

A gate should have:

- Clear purpose.
- Defined pass criteria.
- Appropriate severity.
- Owner.
- Exception process.

---

# Testing Governance

Testing expectations should consider:

- Unit testing.
- Integration testing.
- Contract testing.
- End-to-end testing.
- Performance testing.
- Security testing.
- Resilience testing.

Not every change requires every testing level.

---

# Test Strategy Ownership

The engineering team should define the appropriate test strategy for its system within organizational standards.

Specialists may provide guidance for complex systems.

---

# Defect Management

Important defects should be:

- Recorded.
- Prioritized.
- Owned.
- Resolved or explicitly accepted.

---

# Defect Severity

A conceptual model:

```text
Critical
High
Medium
Low
```

Severity should reflect impact.

---

# Production Defects

Production defects should trigger:

```text
Detection
   ↓
Containment
   ↓
Correction
   ↓
Root Cause
   ↓
Prevention
```

Where appropriate, the organization should ask:

> Could this defect have been prevented or detected earlier?

---

# Quality and Incidents

Incidents are quality signals.

Repeated incidents may indicate:

- Test gaps.
- Architecture problems.
- Operational weaknesses.
- Process weaknesses.

---

# Quality Metrics

Useful quality indicators include:

- Defect escape rate.
- Critical defects.
- Test reliability.
- Production incidents.
- Regression frequency.
- Mean time to resolution.

---

# Quality Metrics Should Not Become Targets

Metrics can create undesirable behavior.

For example:

```text
Target:
Zero defects

Potential behavior:
Do not report defects.
```

Therefore metrics should be interpreted in context.

---

# Test Coverage

Coverage may be useful as an indicator.

It should not be treated as proof of quality.

High coverage with weak assertions may still provide poor protection.

---

# Quality and Code Review

Code review should evaluate:

- Correctness.
- Maintainability.
- Error handling.
- Security.
- Test adequacy.

---

# Quality and Architecture

Architecture affects quality characteristics such as:

- Reliability.
- Performance.
- Scalability.
- Maintainability.

Quality governance should therefore consider architectural decisions.

---

# Quality and Security

Security is part of engineering quality.

A function that works correctly but exposes sensitive data is not a high-quality implementation.

---

# Quality and Operations

Operational quality includes:

- Observability.
- Recovery.
- Deployment safety.
- Reliability.

---

# Quality and Documentation

Documentation quality matters where documentation affects:

- Correct usage.
- Operations.
- Security.
- Architecture.
- Maintenance.

---

# Quality and AI

AI-generated code must meet the same quality expectations as human-written code.

AI should not reduce:

- Testing.
- Review.
- Validation.
- Accountability.

---

# AI Quality Risks

AI may introduce:

- Incorrect assumptions.
- Duplicated logic.
- Unnecessary complexity.
- Inconsistent patterns.
- Weak tests.

These risks should be addressed through review and validation.

---

# Quality Automation

Automate deterministic quality checks where practical.

Examples:

- Formatting.
- Linting.
- Tests.
- Static analysis.
- Dependency scanning.

---

# Quality Exceptions

Exceptions should identify:

- Requirement.
- Reason.
- Risk.
- Mitigation.
- Owner.
- Approval.

---

# Quality Escalation

Escalate when:

- Critical defects remain unresolved.
- Quality risk exceeds team authority.
- Repeated failures indicate systemic problems.
- Required quality evidence is unavailable.

---

# Quality Governance Review

Quality governance should periodically evaluate:

- Defect trends.
- Production failures.
- Test effectiveness.
- Quality gate reliability.
- Repeated findings.

---

# Continuous Improvement

Quality improvement should follow:

```text
Failure
  ↓
Analysis
  ↓
Cause
  ↓
Prevention
  ↓
Automation / Standard
  ↓
Measurement
```

---

# Quality Culture

A healthy quality culture encourages engineers to:

- Report problems.
- Challenge assumptions.
- Improve tests.
- Discuss risks.
- Learn from failures.

Quality should not be associated exclusively with blame.

---

# Quality Anti-Patterns

## QA as Final Gate

Engineering ignores quality until the end.

## Coverage Theater

Optimizing coverage numbers without meaningful tests.

## Zero-Defect Theater

Discouraging defect reporting to improve metrics.

## Test Everything

Applying identical testing requirements regardless of risk.

## Quality by Approval

Assuming approval means quality is guaranteed.

---

# Mandatory Rules

Quality governance shall:

- Establish minimum quality expectations.
- Assign quality ownership.
- Use risk-based testing.
- Track important defects.
- Support quality automation.
- Learn from production failures.

---

# Recommended Practices

Automate repeatable checks.

Test behavior rather than implementation details.

Use production evidence.

Review repeated defects for systemic causes.

Keep quality controls proportional to risk.

---

# Prohibited Practices

Do not:

- Delegate all quality responsibility to QA.
- Treat coverage as proof of correctness.
- Hide defects to improve metrics.
- Require unnecessary tests for low-risk changes.
- Allow AI-generated code to bypass normal quality controls.

---

# Definition of Done

Quality governance is effective when:

- Quality responsibilities are clear.
- Minimum standards exist.
- Appropriate tests are defined.
- Quality gates are meaningful.
- Defects are owned.
- Production feedback is incorporated.
- Quality improvement is continuous.

---

# Review Checklist

### Ownership

- [ ] Engineering ownership
- [ ] System ownership
- [ ] Specialist responsibilities

### Controls

- [ ] Test strategy
- [ ] Quality gates
- [ ] Static analysis
- [ ] Security validation

### Defects

- [ ] Severity
- [ ] Ownership
- [ ] Resolution
- [ ] Risk acceptance

### Measurement

- [ ] Defect trends
- [ ] Test reliability
- [ ] Production feedback
- [ ] Quality improvement

---

# Engineering Decision

Quality governance shall establish the conditions under which engineering teams can consistently produce reliable, maintainable, secure, and operable systems.

Quality is not a final inspection step.

It is a property that must be designed, implemented, verified, operated, and continuously improved.