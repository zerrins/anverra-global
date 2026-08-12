---
document: Review Governance
id: AEC-REV-013
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering Governance
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-REV-012
  - AEC-GOV-000
---

# Purpose

Define the governance model for engineering review across the Anverra Engineering Operating System (AEOS).

Review governance establishes who defines review requirements, who owns review standards, how exceptions are handled, and how the review system evolves.

---

# Intent

Review governance should ensure:

- Review requirements are consistent.
- Responsibilities are clear.
- High-risk changes receive appropriate oversight.
- Exceptions are controlled.
- Review standards evolve based on evidence.
- Review does not become unnecessary bureaucracy.

---

# Constitutional Decision

Engineering review governance shall be risk-based, explicit, accountable, and continuously improved.

Governance shall define the minimum required controls without unnecessarily constraining engineering judgment.

---

# Governance Principles

Review governance shall follow:

1. Risk proportionality.
2. Clear ownership.
3. Explicit accountability.
4. Evidence-based decisions.
5. Minimal necessary bureaucracy.
6. Transparent exceptions.
7. Continuous improvement.

---

# Governance Layers

Review governance may operate at:

```text
Organization
     ↓
Engineering Domain
     ↓
Team
     ↓
Repository
     ↓
Change
```

Higher-level rules establish minimum expectations.

Lower-level rules may add stricter controls where justified.

---

# Governance Authority

Governance responsibilities may include:

## Engineering Leadership

Owns overall review policy.

## Architecture

Owns architectural review standards.

## Security

Owns security review requirements.

## Quality

Owns quality and testing standards.

## Operations

Owns operational readiness requirements.

## Team / Repository Owners

Implement applicable review controls.

---

# Review Policy Ownership

Every significant review policy should have:

- Owner.
- Purpose.
- Scope.
- Version.
- Review frequency.
- Exception mechanism.

Policies without ownership tend to become outdated.

---

# Review Requirements

Review requirements should specify:

- Trigger.
- Required reviewers.
- Required evidence.
- Blocking conditions.
- Approval requirements.

---

# Risk Classification

Governance should establish criteria for:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

Risk classification determines review depth.

---

# Governance Matrix

A conceptual model:

| Risk | Peer | Specialist | Architecture | Security | Operations |
|---|---|---|---|---|---|
| Low | Usually | As needed | No | As needed | As needed |
| Medium | Yes | As needed | As needed | As needed | As needed |
| High | Yes | Usually | Often | Often | Often |
| Critical | Yes | Required | Required where applicable | Required where applicable | Required where applicable |

The exact requirements may vary by change type.

---

# Mandatory Review Domains

Certain change categories may automatically trigger specialist review.

Examples:

```text
Authentication
→ Security

Public API
→ API Review

Architecture Change
→ Architecture

Production Infrastructure
→ Operations

Sensitive Data
→ Security + Data Governance where applicable
```

---

# Approval Authority

Approval authority should match risk.

A reviewer may identify a risk without having authority to accept that risk.

Risk acceptance should occur at the appropriate governance level.

---

# Separation of Responsibilities

Where practical, separate:

```text
Author
Reviewer
Approver
Risk Owner
```

For high-risk changes, one person should not perform every role without explicit justification.

---

# Exceptions

Exceptions are permitted when strict adherence would create greater risk or unreasonable constraints.

Every significant exception should include:

- Requirement being bypassed.
- Reason.
- Risk.
- Compensating controls.
- Owner.
- Approval.
- Duration.

---

# Emergency Exceptions

Emergency changes may use abbreviated governance.

The change should receive retrospective review after stabilization.

Emergency procedures should not become the normal path.

---

# Risk Acceptance

Risk acceptance should be explicit.

A useful structure is:

```text
Risk
 ↓
Impact
 ↓
Mitigation
 ↓
Residual Risk
 ↓
Risk Owner
 ↓
Acceptance
```

---

# Review Evidence

Governance may require evidence such as:

- Review records.
- Test results.
- Security assessments.
- Architecture decisions.
- Operational plans.
- Approval records.

---

# Review Records

Important review decisions should be traceable.

Records may include:

- Pull Requests.
- Design documents.
- ADRs.
- Security reviews.
- Governance approvals.
- Exception records.

---

# Review Auditability

The organization should be able to answer:

- What changed?
- Why?
- Who reviewed it?
- What findings existed?
- What was accepted?
- What risks remained?

---

# Review Governance and AI

AI does not receive independent governance authority merely because it can analyze large amounts of information.

AI may assist governance by:

- Identifying policy violations.
- Summarizing evidence.
- Finding missing approvals.
- Detecting inconsistent review records.

Final governance decisions remain accountable to authorized humans.

---

# Governance Automation

Governance rules may be automated where deterministic.

Examples:

- Required reviewers.
- Required status checks.
- Protected branches.
- Security gates.
- Required documentation.
- Approval counts.

Automation should enforce explicit policy rather than hidden assumptions.

---

# Governance Drift

Review governance can become obsolete.

Examples:

- Technology changed.
- Architecture changed.
- Team structure changed.
- Security threats changed.
- Automation replaced manual controls.

Governance should be reviewed periodically.

---

# Review Governance Review

The review system itself should be reviewed.

Questions:

- Are important defects escaping?
- Are review requirements too heavy?
- Are reviewers overloaded?
- Are automated checks effective?
- Are exceptions increasing?
- Are repeated findings decreasing?

---

# Metrics

Useful governance metrics include:

| Metric | Purpose |
|---|---|
| Critical Changes Reviewed | Coverage |
| Escaped Critical Defects | Effectiveness |
| Exception Rate | Governance health |
| Review Cycle Time | Delivery impact |
| Repeated Findings | Learning |
| Reviewer Load | Capacity |
| Automation Failure Rate | Control reliability |

Metrics should support improvement rather than punishment.

---

# Reviewer Capacity

Governance should consider reviewer availability.

A policy requiring a specialist who is unavailable for days may become a delivery bottleneck.

Possible solutions:

- Reviewer pools.
- Delegated authority.
- Rotation.
- Automated checks.
- Risk-based exceptions.

---

# Review Governance and Teams

Teams may establish stricter local standards when justified.

Local standards shall:

- Identify their scope.
- Not contradict higher-level mandatory requirements.
- Have ownership.
- Be documented.

---

# Review Governance and Repositories

Repositories should identify applicable:

- Review policies.
- Required checks.
- Ownership.
- Protected branches.
- Specialist requirements.

---

# Policy Versioning

Review policies should be versioned.

Changes should communicate:

- What changed.
- Why.
- Effective date.
- Impact.

---

# Policy Deprecation

Obsolete policies should be explicitly deprecated or removed.

Do not leave contradictory policies active indefinitely.

---

# Governance Escalation

Escalate when:

- Policies conflict.
- Risk authority is unclear.
- Required reviewer is unavailable.
- An exception exceeds local authority.
- Security or architecture concerns remain unresolved.

---

# Review Disputes

Technical disagreements should be resolved through:

1. Evidence.
2. Standards.
3. Architecture.
4. Risk analysis.
5. Appropriate escalation.

Governance should not be used to suppress legitimate technical disagreement.

---

# Review Anti-Patterns

## Governance by Habit

"We always do it this way."

## Approval Inflation

Adding more approvers without additional risk reduction.

## Exception Normalization

Treating exceptions as normal workflow.

## Policy Without Ownership

Creating requirements nobody maintains.

## Metrics as Targets

Optimizing metrics rather than engineering outcomes.

---

# Mandatory Rules

Review governance shall:

- Define ownership.
- Define risk-based requirements.
- Define approval authority.
- Support exceptions.
- Maintain auditability.
- Review governance effectiveness.

---

# Recommended Practices

Automate deterministic governance.

Keep approval paths proportional.

Review reviewer capacity.

Retire obsolete policies.

Use incidents to improve governance.

---

# Prohibited Practices

Do not:

- Create approval requirements without risk justification.
- Allow critical risks to be accepted without appropriate authority.
- Make AI the final governance authority.
- Hide exceptions.
- Use review metrics as individual performance scores without context.

---

# Definition of Done

Review governance is complete when:

- Review ownership is defined.
- Risk categories are defined.
- Approval authority is defined.
- Specialist triggers are defined.
- Exception handling exists.
- Records are auditable.
- Governance review occurs periodically.

---

# Review Checklist

### Ownership

- [ ] Policy owner
- [ ] Domain owners
- [ ] Repository ownership

### Risk

- [ ] Risk classification
- [ ] Review requirements
- [ ] Specialist triggers

### Authority

- [ ] Reviewer authority
- [ ] Approval authority
- [ ] Risk acceptance

### Exceptions

- [ ] Exception process
- [ ] Emergency process
- [ ] Compensating controls

### Governance Health

- [ ] Metrics
- [ ] Reviewer capacity
- [ ] Periodic review
- [ ] Policy versioning

---

# Engineering Decision

Review governance shall provide sufficient control to protect engineering quality and organizational risk while avoiding unnecessary approval bureaucracy.

Governance exists to make accountability and risk decisions clear—not to replace engineering judgment.