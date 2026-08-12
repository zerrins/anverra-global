---
document: Review Architecture
id: AEC-REV-002
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-000
  - AEC-ARC-000
  - AEC-QLT-000
---

# Purpose

Define the structural model through which engineering reviews are performed across the Anverra Engineering Operating System (AEOS).

This document defines review layers, review triggers, reviewer roles, review flows, escalation paths, and relationships between different review types.

---

# Intent

Review should be a coherent engineering system rather than a collection of unrelated approval activities.

The review architecture should allow an engineering change to move through the appropriate validation layers based on its risk and impact.

---

# Constitutional Decision

Review shall be structured as a layered, risk-based system.

A change shall receive only the review layers appropriate to its characteristics.

High-risk changes may require multiple specialized reviews.

Low-risk changes should not be burdened by unnecessary review ceremony.

---

# Review Architecture Model

The review system can be represented as:

```text
Engineering Change
       │
       ▼
Change Classification
       │
       ▼
Risk Assessment
       │
       ├───────────────┐
       ▼               ▼
Standard Review    Specialist Review
       │               │
       ├───────┬───────┼────────┐
       ▼       ▼       ▼        ▼
Code       Design   Security  Operations
Review     Review   Review    Review
       │       │       │        │
       └───────┴───────┴────────┘
                   │
                   ▼
              Final Decision
                   │
             ┌─────┴─────┐
             ▼           ▼
          Accept       Reject /
                       Rework
```

---

# Review Layers

Review may occur at the following levels.

## Level 0 — Self Review

The author evaluates their own work before requesting review.

This is mandatory for meaningful changes.

---

## Level 1 — Peer Review

Another engineer evaluates the change.

Typical examples:

- Pull Request.
- Code review.
- Documentation review.

---

## Level 2 — Specialist Review

A domain expert evaluates a change requiring specialized knowledge.

Examples:

- Security.
- Database.
- Infrastructure.
- Performance.
- API design.

---

## Level 3 — Architecture Review

Significant system or architectural changes receive architecture review.

---

## Level 4 — Governance Review

Changes with organization-wide or policy implications may require governance review.

---

# Review Depth

Review depth should increase with:

```text
Risk
+
Blast Radius
+
Complexity
+
Irreversibility
+
Uncertainty
```

---

# Review Trigger Model

A review may be triggered by:

- Code change.
- Design proposal.
- Architecture change.
- API change.
- Security change.
- Infrastructure change.
- Production change.
- Data model change.
- Dependency change.
- AI-generated change.
- Governance change.

---

# Change Classification

Changes should be classified approximately as:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

Classification determines required review depth.

---

# Low-Risk Change

Typical characteristics:

- Small scope.
- Low blast radius.
- Easily reversible.
- Strong automated validation.
- No security or architecture impact.

Typical review:

```text
Self Review
+
Peer Review
+
Automated Checks
```

---

# Medium-Risk Change

Typical characteristics:

- Moderate complexity.
- Multiple components.
- Meaningful operational impact.
- Moderate reversibility.

Typical review:

```text
Self Review
+
Peer Review
+
Automated Checks
+
Relevant Specialist Review
```

---

# High-Risk Change

Typical characteristics:

- Significant production impact.
- Security implications.
- Architecture changes.
- Difficult rollback.
- Large consumer impact.

Typical review:

```text
Self Review
+
Peer Review
+
Specialist Review
+
Architecture / Security / Operations Review
+
Automated Validation
```

---

# Critical Change

Examples:

- Authentication architecture.
- Sensitive data migration.
- Major production infrastructure change.
- Irreversible data migration.
- Major public API compatibility change.

Critical changes may require:

- Multiple specialist reviews.
- Explicit risk acceptance.
- Architecture approval.
- Security approval.
- Operational readiness review.
- Release approval.

---

# Review Roles

## Author

Responsible for:

- Preparing the change.
- Providing context.
- Identifying risks.
- Providing evidence.
- Responding to findings.

---

## Peer Reviewer

Responsible for independent engineering review.

---

## Specialist Reviewer

Provides domain-specific expertise.

---

## Architecture Reviewer

Evaluates system-level consequences.

---

## Security Reviewer

Evaluates security risks.

---

## Operations Reviewer

Evaluates production and operational consequences.

---

## Approver

Provides formal approval where required.

Approval authority shall correspond to the change's governance requirements.

---

# Reviewer Selection

Reviewers should be selected based on:

- Relevant expertise.
- System ownership.
- Domain knowledge.
- Independence.
- Availability.

Do not select reviewers solely based on seniority.

---

# Review Independence

High-risk decisions should receive sufficiently independent review where practical.

The same individual should not be the sole author, reviewer, and approver for a critical irreversible change unless an explicit exception exists.

---

# Review Sequence

The preferred review sequence for significant work is:

```text
Problem
  ↓
Requirements
  ↓
Design
  ↓
Risk Assessment
  ↓
Architecture Review
  ↓
Implementation
  ↓
Testing
  ↓
Specialist Review
  ↓
Final Review
  ↓
Approval
```

Not every change requires every stage.

---

# Shift-Left Review

Important concerns should be identified as early as practical.

Examples:

### Architecture

Review before implementation.

### API

Review contract before client integration.

### Security

Review threat-sensitive designs before deployment.

### Operations

Review deployment and rollback strategy before production.

---

# Review Gates

A review gate prevents progression when required criteria have not been met.

Example:

```text
Architecture Gate
        ↓
Implementation Allowed

Security Gate
        ↓
Production Allowed
```

---

# Blocking Conditions

A review gate may block progression when:

- Critical defect exists.
- Required security control is missing.
- Required architecture approval is absent.
- Required test evidence is missing.
- Required operational capability is absent.

---

# Non-Blocking Findings

Non-blocking findings may be recorded without preventing progression.

They should still be:

- Prioritized.
- Tracked where appropriate.
- Resolved according to risk.

---

# Review Escalation

Escalate when:

- Reviewers disagree on significant risk.
- Requirements are unclear.
- Architecture conflicts exist.
- Security risk is disputed.
- Ownership is unclear.
- Risk acceptance exceeds reviewer authority.

Escalation should resolve uncertainty rather than simply transfer responsibility.

---

# Review Decision Model

A review decision should be based on:

```text
Requirements
+
Evidence
+
Risk
+
Standards
+
Architecture
+
Operational Impact
```

---

# Review Evidence

Evidence may include:

- Tests.
- Benchmarks.
- Security scans.
- Architecture diagrams.
- ADRs.
- API specifications.
- Deployment plans.
- Incident history.

The stronger the evidence, the less review must rely on speculation.

---

# Review and CI

Continuous integration should automatically execute deterministic validation before or during review.

Example:

```text
Pull Request
      ↓
Build
      ↓
Unit Tests
      ↓
Static Analysis
      ↓
Security Scan
      ↓
Contract Validation
      ↓
Reviewer
```

---

# Review and Documentation

Documentation impact should be identified during review.

A change should not be approved while knowingly leaving critical affected documentation incorrect.

---

# Review and Change Management

Significant changes should connect:

```text
Requirement
    ↓
Change
    ↓
Review
    ↓
Evidence
    ↓
Release
```

This improves traceability.

---

# Review and Incident Learning

Repeated production incidents should influence review architecture.

Example:

```text
Incident
   ↓
Root Cause
   ↓
Review Gap Identified
   ↓
New Review Check
   ↓
Automation / Standard
```

---

# Review and AI

AI-assisted review should integrate into the same review architecture.

Example:

```text
Change
  ↓
Automated Checks
  ↓
AI Review
  ↓
Human Review
  ↓
Specialist Review
```

AI findings should not automatically become blocking findings without appropriate validation.

---

# AI-Generated Changes

AI-generated changes should not receive weaker review requirements merely because the implementation was generated automatically.

Review depth should depend on the change's risk.

---

# Review Architecture for Documentation

Documentation changes may use a lighter review architecture.

Critical documentation should still receive appropriate:

- Peer review.
- Specialist review.
- Security review.
- Governance review.

---

# Review Architecture for Emergency Changes

Emergency changes may use an abbreviated review path.

Example:

```text
Incident
   ↓
Emergency Change
   ↓
Rapid Peer / Specialist Review
   ↓
Deployment
   ↓
Post-Change Review
```

Emergency procedures should not permanently bypass normal review.

---

# Post-Change Review

Important changes should be reviewed after deployment when:

- The change was high risk.
- Emergency procedures were used.
- Production behavior differed from expectations.
- Significant incidents occurred.

---

# Review Architecture Evolution

Review architecture should evolve based on:

- Escaped defects.
- Incident findings.
- Repeated review failures.
- Excessive review bottlenecks.
- New technologies.
- New security threats.

---

# Review Bottlenecks

Review should not become a delivery bottleneck due to:

- Unnecessary approvals.
- Excessive reviewers.
- Poor reviewer allocation.
- Redundant checks.

Review requirements should be periodically evaluated for effectiveness.

---

# Review Coverage

Review coverage should be measured by risk.

A 100% review rate for low-risk changes does not guarantee high-quality engineering.

The objective is appropriate review coverage for meaningful risk.

---

# Mandatory Rules

The review architecture shall:

- Use risk-based classification.
- Define applicable review layers.
- Assign appropriate reviewers.
- Provide escalation paths.
- Integrate automated validation.
- Preserve author accountability.
- Support emergency paths.
- Require post-change review where appropriate.

---

# Recommended Practices

Review designs early.

Use specialist reviewers for specialist risks.

Automate deterministic validation.

Keep approval paths proportional.

Review the review system itself.

---

# Prohibited Practices

Do not:

- Require identical review for every change.
- Allow critical changes to bypass required review.
- Make one person the sole gate for all changes.
- Treat approval as transfer of responsibility.
- Use review gates without clear criteria.

---

# Success Metrics

| Metric | Desired Direction |
|---|---|
| Critical Changes Properly Reviewed | 100% |
| Escaped Critical Defects | Decrease |
| Review Bottleneck Time | Healthy |
| Repeated Review Findings | Decrease |
| Required Specialist Review Coverage | 100% |
| Emergency Changes Receiving Retrospective Review | 100% |

---

# Review Checklist

Before selecting the review path:

- What is the change risk?
- What is the blast radius?
- Is it reversible?
- Does it affect security?
- Does it affect architecture?
- Does it affect operations?
- Does it affect external consumers?
- What specialist expertise is required?
- What automated evidence exists?
- Is governance approval required?

---

# Engineering Decision

Engineering review shall operate as a layered, risk-based architecture.

The review path shall scale with the potential impact of the change while minimizing unnecessary review overhead for low-risk work.

---

# Related Documents

- Review Philosophy
- Review Standards
- Code Review
- Design Review
- Architecture Review
- Security Review
- Operational Review
- AI-Assisted Review
- Review Automation
- Review Governance
- Review Definition of Done