---
document: Review Definition of Done
id: AEC-REV-014
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
  - AEC-REV-005
  - AEC-REV-006
  - AEC-REV-007
  - AEC-REV-008
  - AEC-REV-009
  - AEC-REV-010
  - AEC-REV-011
  - AEC-REV-012
  - AEC-REV-013
---

# Purpose

Define the final criteria by which an engineering review is considered complete within the Anverra Engineering Operating System (AEOS).

This document provides the final review gate connecting:

```text
Engineering Change
      ↓
Review
      ↓
Findings
      ↓
Evidence
      ↓
Risk Decision
      ↓
Acceptance
```

---

# Intent

A review should not be considered complete merely because someone clicked "Approve."

Review completion requires sufficient evidence that:

- Applicable review requirements were satisfied.
- Important risks were evaluated.
- Blocking findings were resolved.
- Required evidence exists.
- Required specialists reviewed the change.
- Remaining risks are understood and accepted appropriately.

---

# Constitutional Decision

An engineering change is review-complete when all applicable review obligations have been satisfied and no unresolved issue remains that prevents acceptance.

---

# Definition of Review Complete

A review is complete when:

```text
Context Understood
        ↓
Applicable Review Identified
        ↓
Automated Checks Complete
        ↓
Human Review Complete
        ↓
Specialist Review Complete
        ↓
Findings Resolved
        ↓
Risk Accepted
        ↓
Required Documentation Complete
        ↓
Approval Complete
```

---

# Applicability

Not every change requires every review type.

The required review depth depends on:

- Risk.
- Scope.
- Blast radius.
- Security impact.
- Architecture impact.
- Operational impact.
- Consumer impact.
- Reversibility.

---

# Review Completion Levels

## Level 0 — Minimal

For very low-risk changes.

Typical requirements:

- Self-review.
- Automated checks.
- Basic validation.

---

## Level 1 — Standard

For normal engineering changes.

Typical requirements:

- Self-review.
- Automated checks.
- Peer review.
- Tests.

---

## Level 2 — Enhanced

For changes with meaningful technical risk.

Typical requirements:

- Peer review.
- Relevant specialist review.
- Expanded testing.
- Operational consideration.

---

## Level 3 — High Risk

For significant production, architecture, or security changes.

Typical requirements:

- Peer review.
- Specialist review.
- Architecture review where applicable.
- Security review where applicable.
- Operational review.
- Strong evidence.
- Explicit risk assessment.

---

## Level 4 — Critical

For highly consequential or difficult-to-reverse changes.

May require:

- Multiple specialist reviews.
- Formal architecture approval.
- Security approval.
- Operational approval.
- Explicit risk acceptance.
- Controlled rollout.
- Post-deployment verification.

---

# Universal Review Criteria

Every meaningful review should evaluate the applicable dimensions:

```text
Requirements
Correctness
Security
Reliability
Maintainability
Testing
Operations
Documentation
```

Not every dimension requires equal depth.

---

# Requirement Completion

Reviewers should confirm:

- Requirements are understood.
- Required behavior exists.
- Important constraints are satisfied.
- Known assumptions are documented.

---

# Code Completion

For code changes:

- Code review completed.
- Blocking defects resolved.
- Automated checks passed.
- Appropriate tests exist.

---

# Design Completion

For design changes:

- Problem understood.
- Goals defined.
- Scope defined.
- Design reviewed.
- Trade-offs understood.
- Risks identified.

---

# Architecture Completion

For architecture changes:

- Current state understood.
- Target state defined.
- Architectural delta understood.
- Ownership established.
- Dependencies reviewed.
- ADR created where required.

---

# API Completion

For API changes:

- Contract defined.
- Consumer impact considered.
- Compatibility assessed.
- Security assessed.
- Documentation updated.
- Contract validation completed where applicable.

---

# Security Completion

For security-sensitive changes:

- Security impact understood.
- Relevant threats considered.
- Controls evaluated.
- Security testing completed where required.
- Critical security findings resolved.
- Required security approval obtained.

---

# Operational Completion

For production-impacting changes:

- Deployment plan exists.
- Rollback/recovery understood.
- Monitoring exists.
- Capacity considered.
- Runbook updated where necessary.
- Operational approval obtained where required.

---

# Test Completion

Testing is complete when:

- Appropriate tests exist.
- Important behavior is verified.
- Relevant failure scenarios are covered.
- Test results are available.
- Significant test gaps are understood.

---

# AI Review Completion

When AI-assisted review is used:

- AI findings are evaluated.
- Significant findings are validated.
- False positives are handled.
- Human review remains complete.
- AI did not become the sole authority for critical decisions.

---

# Automation Completion

Required automated checks shall:

- Execute successfully.
- Produce interpretable results.
- Have blocking failures resolved.
- Have approved exceptions documented.

---

# Findings

Every finding should have a disposition:

```text
Resolved

Accepted Risk

Deferred

Rejected / False Positive

Not Applicable
```

Blocking findings shall not remain unresolved without an explicitly authorized exception.

---

# Risk Acceptance

Residual risk may remain after review.

Risk acceptance should identify:

- Risk.
- Impact.
- Mitigation.
- Residual risk.
- Owner.
- Approval.

---

# Accepted Risk

Accepted risk should be explicit.

Example:

```text
Risk:
Migration cannot be fully rolled back after data transformation.

Mitigation:
Backup and forward-repair procedure validated.

Residual Risk:
Manual recovery required in a rare failure scenario.

Owner:
Engineering Operations.

Decision:
Accepted.
```

---

# Documentation Completion

Review should confirm that affected documentation is updated.

Possible artifacts:

- README.
- API documentation.
- Architecture documentation.
- ADR.
- Runbook.
- Configuration documentation.
- Security documentation.

---

# Approval Completion

Required approvals should correspond to:

- Risk level.
- Review type.
- Organizational governance.

Approval must not be treated as a substitute for evidence.

---

# Review Evidence

A completed review should leave sufficient evidence to reconstruct the decision.

Evidence may include:

- Pull Request.
- Review comments.
- Test results.
- Security scans.
- Architecture decision.
- Operational plan.
- Approval record.

---

# Review Traceability

Where practical:

```text
Requirement
    ↓
Implementation
    ↓
Test
    ↓
Review Finding
    ↓
Resolution
    ↓
Approval
```

This provides confidence that important requirements were not lost during implementation.

---

# Final Review Checklist

## Context

- [ ] Problem understood
- [ ] Requirements understood
- [ ] Scope understood
- [ ] Risk understood

## Review

- [ ] Applicable review types identified
- [ ] Required reviewers participated
- [ ] Required specialists participated

## Automation

- [ ] Required automated checks passed
- [ ] Exceptions documented

## Code

- [ ] Correctness reviewed
- [ ] Maintainability reviewed
- [ ] Security considered
- [ ] Error handling reviewed

## Testing

- [ ] Appropriate tests exist
- [ ] Important edge cases covered
- [ ] Failure paths considered
- [ ] Test results reviewed

## Architecture

- [ ] Architecture impact considered
- [ ] Dependencies reviewed
- [ ] ADR created if required

## Security

- [ ] Security impact considered
- [ ] Authorization reviewed
- [ ] Sensitive data considered
- [ ] Required security review complete

## Operations

- [ ] Deployment considered
- [ ] Rollback/recovery considered
- [ ] Monitoring considered
- [ ] Runbook updated where required

## API

- [ ] Contract reviewed where applicable
- [ ] Compatibility assessed
- [ ] Documentation updated

## Findings

- [ ] Blocking findings resolved
- [ ] Non-blocking findings dispositioned
- [ ] Accepted risks documented

## Completion

- [ ] Required documentation complete
- [ ] Required approvals obtained
- [ ] Evidence retained

---

# Review Completion Decision

The final review decision should be one of:

```text
APPROVED

APPROVED WITH ACCEPTED RISK

CHANGES REQUIRED

ESCALATION REQUIRED

REJECTED
```

---

# Approved

Use when:

- Requirements are satisfied.
- Required review is complete.
- Blocking findings are resolved.
- Residual risk is acceptable.

---

# Approved With Accepted Risk

Use when:

- Known non-blocking risks remain.
- The appropriate authority has accepted them.
- Compensating controls exist where necessary.

---

# Changes Required

Use when:

- Blocking findings remain.
- Required evidence is missing.
- Required review has not completed.

---

# Escalation Required

Use when:

- Risk exceeds reviewer authority.
- Technical disagreement remains significant.
- Governance requirements are unclear.

---

# Rejected

Use when:

- The solution is fundamentally unsuitable.
- Risk is unacceptable.
- Requirements cannot be satisfied adequately by the proposed approach.

---

# Post-Approval Responsibilities

Approval does not end engineering responsibility.

After approval:

- Deployment must follow operational procedures.
- Production behavior should be monitored.
- Unexpected behavior should be investigated.
- Significant incidents should feed back into review standards.

---

# Post-Deployment Review

Post-deployment review should be considered for:

- Critical changes.
- Emergency changes.
- High-risk changes.
- Changes with unexpected production behavior.

---

# Review Learning

Review completion should feed organizational learning.

```text
Review
  ↓
Finding
  ↓
Pattern
  ↓
Standard
  ↓
Automation
  ↓
Prevention
```

---

# Definition of Done Summary

A review is done when:

```text
✓ Context understood
✓ Risk classified
✓ Applicable review performed
✓ Required automation passed
✓ Required specialists reviewed
✓ Findings resolved
✓ Risks accepted appropriately
✓ Tests provide evidence
✓ Documentation updated
✓ Required approvals obtained
✓ Review evidence retained
```

---

# Mandatory Rules

A change shall not be considered review-complete when:

- Required review has not occurred.
- Critical findings remain unresolved.
- Required evidence is missing.
- Required approval is absent.
- Known unacceptable risk remains.

---

# Recommended Practices

Use the checklist consistently.

Automate deterministic completion checks.

Keep evidence close to the change.

Review high-risk changes again after deployment.

Use escaped defects to improve the review system.

---

# Prohibited Practices

Do not:

- Treat an approval click as the entire review.
- Hide unresolved critical findings.
- Accept risk without authority.
- Ignore missing evidence.
- Use AI approval as a substitute for required human review.

---

# Engineering Decision

The Definition of Done is the final control point of the engineering review system.

A change is complete not when someone approves it, but when the organization has sufficient evidence, appropriate review, resolved blocking risks, and accountable acceptance of any remaining risk.