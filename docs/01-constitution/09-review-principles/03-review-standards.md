---
document: Review Standards
id: AEC-REV-003
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-000
---

# Purpose

Define the common standards that apply across engineering reviews within the Anverra Engineering Operating System (AEOS).

This document establishes a common baseline for review quality regardless of whether the review concerns code, design, architecture, APIs, security, operations, tests, or AI-generated work.

---

# Intent

A review should consistently evaluate engineering work for:

- Correctness.
- Requirements alignment.
- Risk.
- Security.
- Reliability.
- Maintainability.
- Architecture.
- Test adequacy.
- Operational impact.
- Documentation impact.

---

# Constitutional Decision

Engineering reviews shall use consistent principles while allowing review depth and specialized criteria to vary according to change type and risk.

---

# Review Preparation

Before beginning a substantive review, the reviewer should understand:

- What problem is being solved.
- Why the change is needed.
- What requirements apply.
- What changed.
- What does not change.
- What risks were identified.
- What evidence is available.

---

# Review Context

Every meaningful change should provide sufficient context for review.

Useful context includes:

```text
Problem

Goal

Scope

Design

Implementation

Testing

Risks

Operational Impact

Documentation Impact
```

---

# Review Scope

Reviewers should understand the boundaries of the review.

Examples:

```text
Code Review
→ Implementation correctness

Architecture Review
→ System-level design

Security Review
→ Security risks

Operational Review
→ Production readiness
```

A reviewer should not assume that one review type covers another.

---

# Review Checklist

The following baseline checklist applies to meaningful engineering changes.

---

## 1. Requirements

Verify:

- The change satisfies stated requirements.
- Important assumptions are identified.
- Requirements are not unintentionally changed.
- Edge cases are considered.

---

## 2. Correctness

Verify:

- Logic is correct.
- Data transformations are correct.
- Error handling is appropriate.
- Boundary conditions are addressed.

---

## 3. Architecture

Verify:

- Change fits the architecture.
- Responsibilities remain clear.
- Dependencies are appropriate.
- New coupling is justified.
- Existing architectural decisions are respected or explicitly reconsidered.

---

## 4. Security

Verify where relevant:

- Authentication.
- Authorization.
- Input validation.
- Secret handling.
- Data exposure.
- Trust boundaries.
- Logging.
- Dependency risks.

---

## 5. Reliability

Consider:

- Failure modes.
- Retries.
- Timeouts.
- Idempotency.
- Partial failure.
- Dependency failure.
- Recovery.

---

## 6. Performance

Consider:

- Computational complexity.
- Database usage.
- Network calls.
- Memory usage.
- Concurrency.
- Scaling behavior.

Performance review should be evidence-driven where meaningful.

---

## 7. Maintainability

Consider:

- Complexity.
- Readability.
- Coupling.
- Cohesion.
- Duplication.
- Abstraction quality.
- Future modification cost.

---

## 8. Testing

Verify:

- Appropriate tests exist.
- Important behavior is covered.
- Failure scenarios are tested.
- Tests provide meaningful evidence.

---

## 9. Observability

Where applicable, verify:

- Logging.
- Metrics.
- Tracing.
- Alerts.
- Diagnostics.

A production change should remain diagnosable.

---

## 10. Operations

Consider:

- Deployment.
- Rollback.
- Configuration.
- Monitoring.
- Capacity.
- Recovery.
- Operational dependencies.

---

## 11. Documentation

Verify that affected documentation is:

- Updated.
- Accurate.
- Discoverable.
- Reviewed.

---

# Review Severity

Review findings should use consistent severity.

## BLOCKER

Prevents meaningful review or progression.

Examples:

- Missing required design information.
- Build cannot execute.
- Required evidence unavailable.

---

## CRITICAL

Creates unacceptable risk.

Examples:

- Data loss.
- Critical security vulnerability.
- Severe correctness failure.
- Irreversible production damage.

Critical findings must be resolved or explicitly escalated according to governance.

---

## HIGH

Material risk requiring correction before normal acceptance.

Examples:

- Incorrect business behavior.
- Significant reliability issue.
- Major compatibility problem.

---

## MEDIUM

Meaningful issue that should normally be corrected but may not prevent acceptance depending on context.

Examples:

- Maintainability issue.
- Missing non-critical test.
- Moderate operational concern.

---

## LOW

Minor engineering issue.

Examples:

- Small clarity improvement.
- Minor documentation omission.

---

## NIT

Very minor observation.

Nits should never dominate a review.

---

# Blocking Rules

A finding should block acceptance when:

- It violates a mandatory requirement.
- It creates unacceptable security risk.
- It creates significant correctness risk.
- It can cause data corruption or loss.
- It breaks an important contract.
- It creates unacceptable production risk.

Severity should reflect actual impact, not reviewer preference.

---

# Review Comment Standards

Review comments should be:

- Specific.
- Actionable.
- Respectful.
- Evidence-based.
- Relevant to the change.

---

# Comment Structure

A useful comment can follow:

```text
Observation

Impact

Recommendation
```

Example:

```text
Observation:
This operation retries after the external side effect.

Impact:
A timeout may cause duplicate processing.

Recommendation:
Make the operation idempotent or move the retry boundary.
```

---

# Questions

Use questions when the reviewer genuinely needs clarification.

Example:

```text
How is this protected against duplicate delivery?
```

Do not use questions as indirect criticism when the reviewer already knows the expected correction.

---

# Suggestions

Suggestions should be clearly identified as optional unless they are actually required.

Avoid presenting personal preferences as mandatory engineering standards.

---

# Review Conversations

When a review comment leads to disagreement:

1. Clarify the concern.
2. Identify the requirement.
3. Identify evidence.
4. Explain the trade-off.
5. Escalate if necessary.

The goal is to resolve the engineering issue, not win the discussion.

---

# Review Findings and Risk

A finding's severity should consider:

```text
Likelihood
×
Impact
×
Exposure
```

The exact calculation may vary by review domain.

---

# Review Evidence

Evidence may include:

- Unit tests.
- Integration tests.
- End-to-end tests.
- Benchmarks.
- Static analysis.
- Security scans.
- Architecture diagrams.
- API specifications.
- Production observations.
- Experiments.

---

# Review Confidence

Reviewers should communicate uncertainty when evidence is incomplete.

Examples:

```text
Confirmed defect

Likely defect

Potential risk

Question requiring validation
```

Do not present speculation as fact.

---

# Review Completeness

A review should consider whether all applicable dimensions were evaluated.

For a typical production code change:

```text
Correctness
Security
Reliability
Performance
Testing
Operations
Documentation
```

Not every dimension requires equal depth.

---

# Review and Automated Checks

Automated checks should handle deterministic properties whenever practical.

Examples:

- Formatting.
- Compilation.
- Unit tests.
- Static analysis.
- Dependency scanning.
- Secret scanning.
- API schema validation.

Human review should not spend substantial time reproducing deterministic checks that automation already validates.

---

# Review and AI Findings

AI-generated findings should be classified according to evidence.

For example:

```text
AI Suggestion
    ↓
Human Validation
    ↓
Confirmed Finding
```

An AI-generated finding should not automatically become a blocker.

---

# Review and Generated Code

Generated code must be reviewed according to the risk of the resulting implementation.

The fact that code was generated does not reduce the responsibility to understand it.

---

# Review and External Dependencies

Changes introducing or modifying dependencies should consider:

- Security.
- Licensing where applicable.
- Maintenance.
- Compatibility.
- Performance.
- Operational impact.

---

# Review and Database Changes

Database changes should consider:

- Data integrity.
- Migration safety.
- Rollback.
- Compatibility.
- Performance.
- Locking.
- Production scale.

High-risk migrations require appropriate specialist review.

---

# Review and Configuration

Configuration changes should consider:

- Defaults.
- Environment differences.
- Security.
- Backward compatibility.
- Operational impact.

Configuration should be reviewed as code when it can materially affect system behavior.

---

# Review and API Changes

API changes should consider:

- Contract correctness.
- Backward compatibility.
- Error semantics.
- Authentication.
- Authorization.
- Consumer impact.
- Versioning.
- Documentation.

---

# Review and Architecture Changes

Architecture changes should consider:

- Boundaries.
- Ownership.
- Dependencies.
- Scalability.
- Reliability.
- Security.
- Operational complexity.
- Migration path.

---

# Review and Security Changes

Security-sensitive changes should consider:

- Threat model.
- Trust boundaries.
- Privilege.
- Data sensitivity.
- Attack surface.
- Failure behavior.
- Monitoring.

---

# Review and Operational Changes

Production-impacting changes should consider:

- Deployment strategy.
- Rollback.
- Monitoring.
- Alerting.
- Capacity.
- Recovery.
- Failure scenarios.

---

# Review and Documentation

Documentation changes should be reviewed for:

- Accuracy.
- Completeness.
- Authority.
- Security.
- Lifecycle.

---

# Review Approval

Approval means:

- Required review was completed.
- Blocking findings are resolved.
- Required evidence exists.
- Remaining risk is acceptable.

Approval does not mean:

- No defects exist.
- The implementation is perfect.
- Future failures are impossible.

---

# Conditional Approval

Conditional approval may be used where:

- Remaining findings are non-blocking.
- Risk is understood.
- Follow-up work is tracked.

Conditional approval shall not be used to hide critical unresolved issues.

---

# Review Exceptions

Exceptions should be explicit.

They should include:

- Reason.
- Risk.
- Approver.
- Scope.
- Follow-up.

---

# Review Records

Important review decisions should remain traceable through:

- Pull Requests.
- Design reviews.
- ADRs.
- Approval records.
- Governance records.

---

# Review Metrics

Useful metrics include:

- Escaped defects.
- Review cycle time.
- Defects found during review.
- Repeated findings.
- Review coverage.
- Review bottlenecks.

Avoid measuring reviewer performance primarily by:

- Number of comments.
- Number of approvals.
- Lines reviewed.

These metrics can encourage harmful behavior.

---

# Review Quality

A high-quality review:

```text
Understands Context
        ↓
Identifies Risk
        ↓
Uses Evidence
        ↓
Provides Actionable Feedback
        ↓
Improves the Change
```

---

# Mandatory Rules

Reviews shall:

- Use applicable standards.
- Consider risk.
- Use evidence.
- Identify blocking findings.
- Preserve respectful communication.
- Maintain author accountability.
- Apply appropriate review depth.
- Record required approvals.

---

# Recommended Practices

Use checklists for high-risk changes.

Automate deterministic checks.

Review designs early.

Keep comments concise and actionable.

Separate defects from preferences.

---

# Prohibited Practices

Do not:

- Block work over personal preference.
- Approve known critical defects.
- Hide uncertainty.
- Treat AI output as automatically correct.
- Use review metrics to reward excessive comments.
- Require unnecessary approval layers.

---

# Success Metrics

| Metric | Target / Direction |
|---|---|
| Critical Changes Properly Reviewed | 100% |
| Critical Findings Resolved Before Acceptance | 100% |
| Escaped Critical Defects | Decrease |
| Repeated Findings | Decrease |
| Review Bottleneck Time | Healthy |
| Required Evidence Available | 100% |

---

# Review Checklist

### Context

- [ ] Problem understood
- [ ] Requirements understood
- [ ] Scope understood
- [ ] Risks identified

### Engineering

- [ ] Correctness evaluated
- [ ] Architecture evaluated
- [ ] Security evaluated
- [ ] Reliability evaluated
- [ ] Performance considered
- [ ] Maintainability evaluated

### Verification

- [ ] Tests reviewed
- [ ] Automated checks passed
- [ ] Evidence evaluated

### Operations

- [ ] Deployment considered
- [ ] Rollback considered
- [ ] Observability considered

### Documentation

- [ ] Affected documentation identified
- [ ] Documentation updated where required

### Completion

- [ ] Blocking findings resolved
- [ ] Required approvals obtained
- [ ] Remaining risks understood
- [ ] Review evidence retained

---

# Examples

## Strong Review

```text
Requirement
    ↓
Change Context
    ↓
Risk Analysis
    ↓
Automated Validation
    ↓
Peer Review
    ↓
Specialist Review
    ↓
Findings
    ↓
Corrections
    ↓
Approval
```

---

## Weak Review

```text
PR Opened
    ↓
Reviewer Scans Diff
    ↓
"LGTM"
    ↓
Merge
```

---

# Anti-patterns

Rubber Stamp

Preference Blocking

Review by Line Count

Comment Quantity Metrics

Reviewer Bottleneck

Unclear Severity

Unvalidated AI Findings

Approval Without Evidence

---

# Engineering Decision

Review standards provide the common quality baseline for all engineering review activities.

Specialized review documents may add domain-specific requirements, but they shall not silently weaken the common principles established here.