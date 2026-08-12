---
document: Review Automation
id: AEC-REV-012
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-REV-004
  - AEC-REV-008
  - AEC-REV-010
  - AEC-REV-011
---

# Purpose

Define how automated tooling shall support engineering review within the Anverra Engineering Operating System (AEOS).

Automation exists to improve review speed, consistency, coverage, and repeatability while allowing human reviewers to focus on engineering judgment.

---

# Intent

Review automation should answer:

- Which review checks can be performed automatically?
- Which checks should remain human responsibilities?
- Where should automated checks run?
- Which checks should block progression?
- How should automated findings be handled?
- How should automation quality be measured?
- How should automated review evolve?

---

# Constitutional Decision

Deterministic and repeatable review checks should be automated whenever practical.

Automation shall complement rather than replace engineering judgment.

---

# Automation Philosophy

The preferred model is:

```text
Engineering Change
       ↓
Automated Validation
       ↓
Automated Findings
       ↓
Human Judgment
       ↓
Approval
```

Automation should remove repetitive work from reviewers.

---

# Why Automate Review

Automation provides:

- Consistency.
- Speed.
- Repeatability.
- Early feedback.
- Large-scale coverage.
- Reduced reviewer effort.

Automation is particularly valuable for checks that are:

- Deterministic.
- Repetitive.
- Easily measurable.
- Expensive to perform manually.
- Required across many repositories.

---

# Suitable Automation

Examples include:

- Compilation.
- Formatting.
- Linting.
- Unit tests.
- Static analysis.
- Dependency scanning.
- Secret scanning.
- API compatibility checks.
- Schema validation.
- Documentation link checks.
- Infrastructure validation.

---

# Unsuitable Full Automation

Some decisions require human judgment.

Examples:

- Business correctness.
- Architectural trade-offs.
- Risk acceptance.
- Ambiguous requirements.
- Strategic technology choices.
- Major security architecture decisions.

These may be assisted by automation but should not be delegated entirely to deterministic tooling.

---

# Automation Layers

Review automation may operate at several levels:

```text
Local Developer Checks
        ↓
Pull Request Checks
        ↓
CI Validation
        ↓
Repository Governance
        ↓
Release Validation
        ↓
Production Verification
```

---

# Local Automation

Local checks should provide rapid feedback.

Examples:

- Formatter.
- Linter.
- Unit tests.
- Type checking.
- Basic static analysis.

The objective is to identify inexpensive failures before the Pull Request.

---

# Pull Request Automation

Pull Request automation should provide immediate review feedback.

Typical checks:

```text
Build
Tests
Static Analysis
Security Scan
Dependency Scan
Contract Validation
Documentation Validation
```

---

# Continuous Integration

CI should establish repeatable validation independent of the developer's environment.

CI should ideally be:

- Reproducible.
- Observable.
- Fast enough for normal development.
- Deterministic where practical.

---

# Pre-Merge Gates

Automated checks may block merge when they establish deterministic violations.

Examples:

- Compilation failure.
- Required tests failing.
- Critical security vulnerability.
- Secret detected.
- Invalid API contract.
- Required policy violation.

---

# Blocking Automation

An automated check should block progression when:

- The rule is well-defined.
- The result is sufficiently reliable.
- The violation is meaningful.
- False positives are sufficiently controlled.

---

# Non-Blocking Automation

Use non-blocking feedback when:

- The check is advisory.
- False positives are relatively high.
- Human interpretation is required.
- The issue is informational.

---

# Automation Severity

Automated findings should use consistent severity:

```text
CRITICAL
HIGH
MEDIUM
LOW
INFO
```

Severity should correspond to impact.

---

# Static Analysis

Static analysis may identify:

- Bugs.
- Unsafe patterns.
- Complexity.
- Dead code.
- Security vulnerabilities.
- Maintainability issues.

Static analysis results should be interpreted according to tool reliability.

---

# Dependency Automation

Dependency automation may identify:

- Vulnerabilities.
- Outdated versions.
- License concerns where applicable.
- Dependency conflicts.

Not every outdated dependency requires immediate upgrade.

Risk should be evaluated.

---

# Secret Scanning

Secret scanning should detect accidental exposure of:

- API keys.
- Tokens.
- Passwords.
- Private keys.
- Credentials.

Detected secrets should be handled according to security procedures.

---

# Test Automation

Automated tests should execute:

- Unit tests.
- Integration tests.
- Contract tests.
- End-to-end tests where appropriate.

Test suites should be structured to provide fast feedback first and deeper validation later.

---

# API Automation

API automation may validate:

- Schema.
- Contract compatibility.
- Required fields.
- Response types.
- Version compatibility.

---

# Infrastructure Automation

Infrastructure changes may be automatically checked for:

- Syntax.
- Policy.
- Security.
- Dependency.
- Configuration.

Infrastructure validation should occur before production deployment.

---

# Documentation Automation

Automation may detect:

- Broken links.
- Missing references.
- Invalid generated documentation.
- Schema mismatches.
- Documentation drift indicators.

Automated documentation validation should not attempt to determine semantic truth without sufficient context.

---

# Architecture Automation

Architecture automation may identify:

- Forbidden dependencies.
- Layer violations.
- Circular dependencies.
- Package boundaries.
- Known architectural rules.

Architecture automation should enforce explicit rules rather than subjective design preferences.

---

# AI Review Automation

AI may be integrated into review automation for:

- Risk detection.
- Code analysis.
- Test suggestions.
- Documentation comparison.
- Change summaries.

AI findings should remain advisory unless a deterministic policy explicitly supports automated enforcement.

---

# Automation and AI Independence

An AI-generated finding should not automatically be treated as authoritative.

Use:

```text
AI Detection
     ↓
Validation
     ↓
Finding
```

rather than:

```text
AI Detection
     ↓
Automatic Rejection
```

unless organizational policy explicitly defines a safe deterministic rule.

---

# Automation Ordering

A useful validation sequence is:

```text
Formatting
    ↓
Compilation
    ↓
Unit Tests
    ↓
Static Analysis
    ↓
Security Checks
    ↓
Integration / Contract Tests
    ↓
AI-Assisted Review
    ↓
Human Review
```

The exact ordering may vary by repository.

---

# Fast Feedback

Cheap checks should generally execute before expensive checks.

Example:

```text
Syntax
  ↓
Compile
  ↓
Unit Tests
  ↓
Integration Tests
  ↓
End-to-End Tests
```

This reduces wasted compute and developer waiting time.

---

# Failure Isolation

Automated review systems should make failures easy to diagnose.

A failure should identify:

- Check.
- Cause.
- Location.
- Severity.
- Suggested remediation where appropriate.

---

# Flaky Automation

Flaky checks reduce trust.

Flaky checks should be:

- Identified.
- Investigated.
- Fixed.
- Temporarily quarantined where necessary.

Quarantine should not become permanent.

---

# Automation Exceptions

Exceptions may be required when:

- A tool produces a known false positive.
- A legacy component cannot satisfy a new rule immediately.
- A migration is underway.

Exceptions should document:

- Rule.
- Reason.
- Risk.
- Owner.
- Expiration or review date where appropriate.

---

# Automation Governance

Automation rules should have:

- Clear ownership.
- Documented purpose.
- Defined severity.
- Change control.
- Monitoring.

---

# Rule Ownership

Each important automated rule should have an owner responsible for:

- Correctness.
- Maintenance.
- False-positive management.
- Updates.

---

# Rule Changes

Changes to blocking rules should be reviewed.

A rule that blocks production changes can itself become a production delivery dependency.

---

# Automation Availability

Critical review automation should be reliable enough to support engineering workflows.

Where automation is unavailable, fallback procedures should be defined for important production changes.

---

# Automation Security

Review automation itself should be secured.

Consider:

- Credential access.
- Repository access.
- Build permissions.
- Secrets.
- External integrations.
- Code execution.

CI systems often have significant privileges and therefore represent security-sensitive infrastructure.

---

# Automation Performance

Monitor:

- Execution time.
- Queue time.
- Failure rate.
- Resource consumption.

Slow review automation can become a delivery bottleneck.

---

# Automation Metrics

Useful metrics include:

| Metric | Desired Direction |
|---|---|
| Automated Check Success Rate | High |
| False Positive Rate | Low |
| Mean Feedback Time | Low |
| Flaky Check Rate | Low |
| Defects Found Automatically | Healthy |
| Manual Review Effort | Reduced where appropriate |

---

# Automation and Review Quality

Automation should improve review quality, not merely reduce human effort.

A faster review process that misses important defects is not an improvement.

---

# Automation Feedback Loop

```text
Production Incident
      ↓
Root Cause
      ↓
Could It Be Detected Automatically?
      ↓
Yes
      ↓
New Rule / Test / Scanner
      ↓
Future Prevention
```

---

# Automation Lifecycle

Automated checks should evolve:

```text
Need Identified
      ↓
Rule Designed
      ↓
Validation
      ↓
Deployment
      ↓
Monitoring
      ↓
Tuning
      ↓
Retirement
```

---

# Retiring Automation

Automation should be removed when:

- No longer relevant.
- Replaced by a better control.
- Produces unacceptable noise.
- Enforces obsolete architecture.

Retirement should be intentional.

---

# Mandatory Rules

Review automation shall:

- Automate deterministic checks where practical.
- Have clear ownership.
- Distinguish blocking from advisory checks.
- Protect automation credentials.
- Address flaky checks.
- Provide actionable failures.

---

# Recommended Practices

Prefer fast feedback.

Automate repetitive checks.

Measure false positives.

Use AI as an assistant.

Continuously improve automation based on incidents and review findings.

---

# Prohibited Practices

Do not:

- Automatically block based on unreliable checks.
- Ignore persistent flaky automation.
- Give CI excessive privileges without justification.
- Treat automation as a replacement for architectural judgment.
- Add automation without ownership.

---

# Definition of Done

Review automation is complete when:

- Applicable automated checks are defined.
- Blocking checks are identified.
- Check ownership exists.
- Failures are actionable.
- Security is addressed.
- Exceptions are documented.
- Monitoring exists for important automation.

---

# Review Checklist

### Automation

- [ ] Appropriate checks automated
- [ ] Blocking checks identified
- [ ] Advisory checks identified
- [ ] Owners assigned

### Quality

- [ ] False positives considered
- [ ] Flakiness addressed
- [ ] Failure messages useful
- [ ] Performance acceptable

### Security

- [ ] Credentials protected
- [ ] Permissions minimized
- [ ] External integrations reviewed

### Governance

- [ ] Rules documented
- [ ] Exceptions documented
- [ ] Rule changes controlled

---

# Engineering Decision

Automation should remove repetitive review work while preserving human judgment for decisions that require context, trade-offs, and risk acceptance.

The goal is not maximum automation.

The goal is **the right automation at the right review layer**.