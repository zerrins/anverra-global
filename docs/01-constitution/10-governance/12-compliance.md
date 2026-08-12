---
document: Engineering Compliance
id: AEC-GOV-012
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering Governance
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-003
  - AEC-GOV-004
  - AEC-GOV-008
  - AEC-GOV-009
---

# Purpose

Define how engineering compliance requirements are identified, interpreted, implemented, evidenced, monitored, and maintained within the Anverra Engineering Operating System (AEOS).

---

# Intent

Engineering compliance should ensure that applicable obligations are:

- Known.
- Assigned.
- Implemented.
- Evidenced.
- Reviewed.
- Maintained over time.

Compliance should be integrated into engineering rather than treated as an isolated administrative activity.

---

# Constitutional Decision

Engineering compliance shall be managed as an engineering responsibility supported by appropriate legal, security, risk, and governance expertise.

Compliance requirements shall be translated into clear engineering controls wherever practical.

---

# What Is Engineering Compliance?

Engineering compliance means satisfying applicable:

- Laws.
- Regulations.
- Contracts.
- Organizational policies.
- Industry requirements.
- Security obligations.
- Internal governance requirements.

The exact obligations depend on the organization, product, geography, industry, and systems involved.

---

# Compliance Is Not Security

Security and compliance overlap but are not identical.

```text
Security
→ Protect the system and information.

Compliance
→ Demonstrate that applicable obligations are satisfied.
```

A system can be:

- Secure but non-compliant.
- Compliant but insecure.
- Both.
- Neither.

---

# Compliance Sources

Potential sources include:

```text
Law
Regulation
Contract
Customer Requirement
Internal Policy
Industry Standard
Security Requirement
Organizational Governance
```

Applicable sources must be determined according to the organization's actual context.

---

# Compliance Ownership

Compliance should have explicit ownership.

Possible responsibilities include:

```text
Compliance / Legal
→ Interpret obligations

Security
→ Security controls

Engineering
→ Technical implementation

System Owner
→ System-level compliance

Engineering Governance
→ Governance integration
```

---

# Compliance Requirement Lifecycle

```text
Requirement Identified
        ↓
Applicability Determined
        ↓
Requirement Interpreted
        ↓
Control Defined
        ↓
Owner Assigned
        ↓
Implemented
        ↓
Evidence Generated
        ↓
Validated
        ↓
Monitored
        ↓
Reviewed
```

---

# Applicability

Not every compliance requirement applies to every system.

Applicability should consider:

- Product.
- Data.
- Geography.
- Customers.
- Industry.
- Infrastructure.
- Business model.
- Contractual obligations.

---

# Requirement Interpretation

Legal or regulatory requirements may be broad.

Engineering governance should translate applicable requirements into actionable controls where appropriate.

Example:

```text
Requirement
    ↓
Control Objective
    ↓
Engineering Control
    ↓
Evidence
```

---

# Control Objectives

A control objective describes the outcome that must be achieved.

Example:

```text
Objective:
Only authorized users may access sensitive functionality.

Possible controls:
Authentication
Authorization
Access reviews
Audit logging
```

---

# Engineering Controls

Controls may be:

### Preventive

Prevent the violation.

Example:

```text
Access Control
```

### Detective

Detect the violation.

Example:

```text
Security Monitoring
```

### Corrective

Restore compliance after a violation.

Example:

```text
Credential Revocation
```

---

# Compliance Evidence

Evidence may include:

- Configuration.
- Logs.
- Test results.
- Access reviews.
- Change records.
- Security scans.
- Architecture records.
- Approval records.
- Policies.
- Training records where applicable.

Evidence should be generated as naturally as possible from engineering workflows.

---

# Evidence Quality

Good evidence should be:

- Relevant.
- Accurate.
- Traceable.
- Current.
- Protected from unauthorized modification.

---

# Compliance Automation

Where practical, compliance evidence should be automated.

Examples:

```text
Configuration
    ↓
Automated Validation
    ↓
Evidence
```

Automation reduces manual evidence collection.

---

# Continuous Compliance

Compliance should not depend entirely on periodic manual audits.

Where possible:

```text
Control
  ↓
Continuous Monitoring
  ↓
Deviation Detection
  ↓
Remediation
```

---

# Compliance and CI/CD

Applicable compliance controls may be integrated into:

- Pull Requests.
- CI pipelines.
- Deployment pipelines.
- Infrastructure validation.
- Security scanning.

---

# Compliance and Change Management

Changes that affect compliance should receive appropriate review.

Examples:

- Data processing changes.
- Authentication changes.
- Logging changes.
- Retention changes.
- Infrastructure changes.

---

# Compliance and Security

Security controls may provide compliance evidence.

Examples:

- Access control.
- Encryption.
- Audit logs.
- Vulnerability management.

However, security controls should not automatically be assumed to satisfy every compliance obligation.

---

# Compliance and Data

Where compliance concerns data, consider:

- Collection.
- Storage.
- Access.
- Processing.
- Retention.
- Transfer.
- Deletion.

The actual requirements depend on applicable obligations.

---

# Compliance and Third Parties

Third-party services may introduce compliance considerations.

Consider:

- Data processing.
- Hosting.
- Access.
- Subprocessors.
- Contracts.
- Geographic location.
- Security controls.

---

# Compliance Exceptions

Exceptions should be:

- Explicit.
- Risk-assessed.
- Authorized.
- Documented.
- Time-bounded where appropriate.

---

# Compliance Findings

Findings should have:

- Description.
- Requirement.
- Risk.
- Owner.
- Remediation.
- Target.
- Status.

---

# Compliance Severity

A conceptual classification:

```text
Critical
High
Medium
Low
```

Severity should consider:

- Regulatory impact.
- Contractual impact.
- Security impact.
- Customer impact.
- Business impact.

---

# Compliance Escalation

Escalate when:

- A requirement is unclear.
- A violation may be material.
- Risk exceeds local authority.
- Remediation cannot meet required timelines.
- Legal interpretation is required.

---

# Compliance Audits

Audits may evaluate:

- Controls.
- Evidence.
- Ownership.
- Exceptions.
- Remediation.
- Governance.

Engineering should treat audits as evidence-based validation rather than document-production exercises.

---

# Audit Readiness

A mature engineering organization should be able to demonstrate:

```text
Requirement
    ↓
Control
    ↓
Owner
    ↓
Evidence
    ↓
Validation
```

---

# Compliance Drift

Compliance can degrade when:

- Architecture changes.
- Systems migrate.
- New vendors are introduced.
- Data flows change.
- Policies change.

Therefore compliance should be reviewed when engineering systems change materially.

---

# Compliance and AI

AI introduces additional considerations around:

- Data handling.
- Confidentiality.
- Intellectual property.
- Model providers.
- Automated decisions.
- Logging.
- Access.

Applicable requirements should be evaluated according to the organization's context.

---

# Compliance and Documentation

Important compliance controls should be documented sufficiently for:

- Engineering.
- Operations.
- Security.
- Audit.
- Governance.

---

# Compliance Metrics

Potential indicators:

- Open compliance findings.
- Overdue remediation.
- Control coverage.
- Evidence freshness.
- Exception count.
- Repeat findings.

Metrics should support risk reduction.

---

# Compliance Anti-Patterns

## Audit Preparation Only

Controls are implemented immediately before an audit.

## Evidence Theater

Large amounts of documentation without meaningful control effectiveness.

## Unowned Requirements

Requirements exist without accountable owners.

## Compliance by Assumption

Assuming a control satisfies an obligation without validation.

## Manual Everything

Collecting evidence manually when automation is practical.

---

# Mandatory Rules

Engineering compliance shall:

- Identify applicable obligations.
- Assign ownership.
- Define controls.
- Maintain evidence.
- Manage findings.
- Govern exceptions.
- Support appropriate audits.

---

# Recommended Practices

Automate evidence collection.

Map requirements to controls.

Integrate compliance into engineering workflows.

Review compliance after significant architecture or data changes.

Keep evidence close to the control that generates it.

---

# Prohibited Practices

Do not:

- Assume every regulation applies equally.
- Claim compliance without evidence.
- Hide material findings.
- Allow unapproved exceptions.
- Treat audit preparation as a substitute for continuous compliance.

---

# Definition of Done

Compliance governance is effective when:

- Applicable requirements are identified.
- Owners exist.
- Controls are defined.
- Evidence is available.
- Findings are tracked.
- Exceptions are controlled.
- Compliance can be demonstrated when required.

---

# Review Checklist

### Requirements

- [ ] Applicable obligations identified
- [ ] Scope understood
- [ ] Requirements interpreted appropriately

### Controls

- [ ] Control objectives defined
- [ ] Engineering controls implemented
- [ ] Control ownership established

### Evidence

- [ ] Evidence exists
- [ ] Evidence is current
- [ ] Evidence is traceable

### Governance

- [ ] Findings tracked
- [ ] Exceptions controlled
- [ ] Escalation path exists

---

# Engineering Decision

Engineering compliance shall be implemented as a continuous engineering capability rather than a periodic audit exercise.

The objective is to make compliance **observable, owned, evidenced, and maintainable throughout the engineering lifecycle**.