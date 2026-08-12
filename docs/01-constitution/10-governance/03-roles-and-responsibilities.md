---
document: Roles and Responsibilities
id: AEC-GOV-003
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-002
  - AEC-GOV-000
  - AEC-REV-002
---

# Purpose

Define the major engineering roles, responsibilities, decision authority, and accountability expectations within the Anverra Engineering Operating System (AEOS).

---

# Intent

Clear roles should prevent:

- Unowned decisions.
- Duplicate responsibility.
- Approval ambiguity.
- Risk ownership gaps.
- Operational handoff failures.

Roles describe accountability, not necessarily job titles.

---

# Constitutional Decision

Responsibilities shall be assigned explicitly for important engineering outcomes.

Organizational titles may vary, but accountability for critical engineering responsibilities must remain clear.

---

# Role vs Job Title

A role represents a responsibility.

A person may hold multiple roles.

A role may also be distributed across multiple people.

Example:

```text
Senior Engineer
    +
System Owner
    +
Reviewer
```

The role model should describe what must be done rather than depend entirely on organizational titles.

---

# Core Engineering Roles

The core role categories are:

```text
Engineering Leader
Engineering Manager
Technical Lead
System Owner
Engineer
Architect
Security Specialist
Quality Specialist
Operations / Platform Specialist
AI Governance Owner
Product / Business Partner
```

The exact organizational implementation may vary.

---

# Engineering Leader

## Purpose

Provide overall engineering direction and accountability.

## Responsibilities

- Engineering strategy.
- Engineering standards.
- Organizational health.
- Major technical risks.
- Engineering investment.
- Capability development.
- Governance.

## Accountable For

- Overall engineering health.
- Long-term engineering sustainability.

## Should Not

- Become the approval point for routine engineering decisions.
- Personally own every technical decision.

---

# Engineering Manager

## Purpose

Enable sustainable engineering execution and team health.

## Responsibilities

- Staffing.
- Team development.
- Delivery environment.
- Organizational risks.
- Capacity.
- Performance and growth.
- Cross-team coordination.

## Accountable For

- Team effectiveness.
- Sustainable execution.
- Organizational health.

## Technical Authority

Management authority does not automatically establish technical correctness.

Technical decisions should involve appropriate technical owners.

---

# Technical Lead

## Purpose

Provide technical direction within a team or system.

## Responsibilities

- Technical design.
- Implementation direction.
- Technical risk identification.
- Code quality.
- Mentoring.
- Technical decision-making.

## Accountable For

- Technical coherence within the relevant scope.

---

# System Owner

## Purpose

Own the technical and operational health of a system.

## Responsibilities

- Architecture.
- Code.
- Reliability.
- Security.
- Documentation.
- Technical debt.
- Operational readiness.
- Incident participation.

## Accountable For

- System health.
- System evolution.
- Technical risks.

System ownership does not mean the owner personally performs every activity.

---

# Engineer

## Purpose

Design, implement, test, operate, and improve engineering systems.

## Responsibilities

- Requirements understanding.
- Implementation.
- Testing.
- Code review.
- Documentation.
- Operational awareness.
- Risk identification.

## Accountable For

The quality and correctness of the engineering work they submit.

---

# Architect

## Purpose

Provide system-level architectural guidance.

## Responsibilities

- Architecture principles.
- Major architectural decisions.
- Cross-system dependencies.
- Architectural consistency.
- Architecture reviews.
- Architecture evolution.

## Accountable For

Architectural guidance within the assigned scope.

Architects should not become the sole owners of every architectural decision.

---

# Security Specialist

## Purpose

Provide security expertise and governance.

## Responsibilities

- Security standards.
- Threat analysis.
- Security review.
- Security architecture.
- Vulnerability management.
- Security guidance.

## Accountable For

Security governance and specialist guidance within the assigned scope.

Security remains a shared engineering responsibility.

---

# Quality Specialist

## Purpose

Provide quality engineering expertise.

## Responsibilities

- Quality standards.
- Test strategy.
- Quality tooling.
- Quality analysis.
- Defect trends.
- Verification guidance.

## Accountable For

Quality practices and specialist guidance.

Engineering teams remain responsible for the quality of their systems.

---

# Operations / Platform Specialist

## Purpose

Provide production, infrastructure, reliability, and platform expertise.

## Responsibilities

- Infrastructure.
- Deployment.
- Observability.
- Reliability.
- Capacity.
- Recovery.
- Platform tooling.

## Accountable For

Operational capability within the assigned platform scope.

---

# AI Governance Owner

## Purpose

Govern safe and effective use of AI within engineering.

## Responsibilities

- AI usage standards.
- AI risk controls.
- Approved tools.
- Data-handling requirements.
- AI evaluation.
- AI governance.

## Accountable For

AI governance policy and risk controls.

Engineering teams remain accountable for the output they create using AI.

---

# Product / Business Partner

## Purpose

Represent business outcomes and requirements.

## Responsibilities

- Business goals.
- Requirements.
- Priorities.
- Acceptance criteria.
- Business trade-offs.

## Accountable For

Business intent and prioritization.

Engineering remains accountable for technical implementation and engineering quality.

---

# Responsibility Matrix

A conceptual responsibility model:

| Responsibility | Primary Role | Supporting Roles |
|---|---|---|
| Engineering Strategy | Engineering Leader | Managers, Technical Leads |
| Team Health | Engineering Manager | Engineering Leader |
| Technical Direction | Technical Lead | Engineers, Architect |
| System Health | System Owner | Engineers, Operations |
| Architecture | Architect / Technical Lead | System Owner |
| Security | Security Specialist | Engineers, Architects |
| Quality | Engineering + Quality Specialist | Test / QA Engineers |
| Operations | System Owner + Operations | Engineers |
| AI Governance | AI Governance Owner | Security, Engineering |
| Product Requirements | Product / Business | Engineering |
| Technical Debt | System Owner | Technical Lead, Engineers |

This matrix is a responsibility model rather than a mandatory organizational hierarchy.

---

# RACI Usage

RACI may be used for complex responsibilities:

```text
R = Responsible
A = Accountable
C = Consulted
I = Informed
```

RACI should be used selectively.

Creating RACI matrices for every small engineering task creates unnecessary overhead.

---

# Decision Authority

Decision authority should depend on:

```text
Context
+
Risk
+
Impact
+
Expertise
```

---

# Local Decisions

Engineers should generally own routine implementation decisions.

Examples:

- Method structure.
- Local refactoring.
- Test implementation.
- Internal naming.

---

# Team Decisions

Teams should generally own:

- Component design.
- Internal service implementation.
- Testing strategy within standards.
- Local technology choices within approved boundaries.

---

# Cross-Team Decisions

Cross-team decisions may require:

- Technical leads.
- Architects.
- System owners.
- Security.
- Operations.

---

# Organization-Level Decisions

Organization-level decisions may include:

- Engineering standards.
- Major technology strategy.
- Security policy.
- Governance policy.
- Major architecture direction.

---

# Risk Ownership

Every significant accepted risk should have an owner.

Risk ownership means:

- Understanding the risk.
- Ensuring mitigation is considered.
- Accepting residual risk where authorized.
- Monitoring the risk.

---

# Approval vs Accountability

Approval does not automatically mean accountability.

Example:

```text
Security Specialist
→ Confirms security review

System Owner
→ Remains accountable for system risk
```

Responsibilities should remain explicit.

---

# Operational Accountability

The team responsible for a production system should retain appropriate operational accountability.

Avoid:

```text
Developer
   ↓
Deploy
   ↓
Operations Owns Everything
```

Prefer:

```text
Engineering
     +
Operations
     ↓
Production Outcome
```

---

# Review Responsibility

Authors are responsible for:

- Preparing changes.
- Self-review.
- Responding to findings.

Reviewers are responsible for:

- Independent evaluation.
- Identifying meaningful risks.
- Providing actionable feedback.

Approvers are responsible for:

- Making the approval decision within their authority.

---

# Architecture Responsibility

Architectural decisions should have:

- Decision owner.
- Relevant reviewers.
- Recorded rationale where significant.

Architecture should not remain implicit in one individual's memory.

---

# Security Responsibility

Security should be:

```text
Specialist Responsibility
        +
Engineering Responsibility
```

Security specialists establish expertise and governance.

Engineers implement secure systems.

---

# Quality Responsibility

Quality should be:

```text
Team Responsibility
        +
Quality Expertise
```

Quality specialists improve engineering capability but should not become the only people responsible for testing or quality.

---

# AI Responsibility

AI-assisted engineering creates no exemption from normal engineering accountability.

The engineer submitting the resulting work remains responsible for understanding and validating it.

---

# Escalation Responsibility

When an engineer identifies a risk beyond their authority, they are responsible for escalating it.

The receiving authority is responsible for resolving or accepting the risk appropriately.

---

# Responsibility During Incidents

During incidents:

- Incident leadership coordinates response.
- System owners provide system expertise.
- Operations provides operational coordination.
- Security participates where security impact exists.
- Engineering contributes diagnosis and remediation.

Incident responsibility should be defined before incidents occur.

---

# Responsibility for Technical Debt

Technical debt should have:

- Owner.
- Description.
- Impact.
- Priority.
- Desired resolution.

Technical debt without ownership tends to accumulate indefinitely.

---

# Responsibility for Documentation

System owners should ensure important system documentation remains accurate.

Authors remain responsible for documentation directly affected by their changes.

---

# Responsibility for Standards

Every important standard should have an owner.

The owner is responsible for:

- Accuracy.
- Maintenance.
- Evolution.
- Communication.

---

# Responsibility for Exceptions

An exception should identify:

- Requester.
- Technical owner.
- Risk owner.
- Approver.
- Expiration or review point where appropriate.

---

# Responsibility Gaps

A responsibility gap exists when:

```text
Important Outcome
        ↓
No Clear Owner
```

Responsibility gaps are governance risks.

---

# Responsibility Conflicts

A conflict exists when:

```text
Two Roles
   ↓
Both Claim Authority
```

Resolve through:

1. Scope.
2. Governance.
3. Evidence.
4. Appropriate escalation.

---

# Responsibility Overlap

Some responsibilities intentionally overlap.

Example:

```text
Security
    +
Engineering
```

Security expertise does not remove engineering responsibility.

---

# Single-Person Dependency

Critical responsibilities should not depend entirely on one person.

Where practical:

- Document knowledge.
- Establish backup ownership.
- Rotate responsibilities.
- Cross-train engineers.

---

# Responsibility Evolution

Responsibilities should change as:

- Systems grow.
- Teams change.
- Architecture changes.
- Risk changes.
- Organizational structure evolves.

Role definitions should be reviewed periodically.

---

# Anti-Patterns

## Responsibility by Seniority

The most senior person is automatically assumed to own everything.

## Responsibility by Proximity

The person who happens to discover a problem becomes permanently responsible.

## Shared Ownership Without Accountability

Everyone owns something, therefore nobody is accountable.

## Specialist Ownership of Everything

Security, architecture, or quality becomes responsible for all engineering outcomes.

## Operational Handoff

Engineering considers production responsibility complete after deployment.

---

# Mandatory Rules

Important engineering responsibilities shall:

- Have explicit ownership.
- Have appropriate decision authority.
- Have escalation paths.
- Preserve accountability.
- Avoid critical single-person dependencies.

---

# Recommended Practices

Document ownership.

Use lightweight responsibility matrices.

Distribute knowledge.

Review responsibility boundaries periodically.

Keep accountability close to the system.

---

# Prohibited Practices

Do not:

- Leave critical systems unowned.
- Treat approval as transfer of responsibility.
- Make specialists the sole owners of cross-cutting engineering outcomes.
- Allow AI to become accountable for engineering decisions.
- Use organizational hierarchy as a substitute for responsibility clarity.

---

# Definition of Done

Roles and responsibilities are adequately defined when:

- Major engineering roles are understood.
- Decision authority is clear.
- System ownership is explicit.
- Specialist responsibilities are clear.
- Risk ownership exists.
- Escalation paths exist.
- Critical single-person dependencies are identified.

---

# Review Checklist

### Roles

- [ ] Major roles identified
- [ ] Responsibilities defined
- [ ] Decision authority defined

### Ownership

- [ ] Systems owned
- [ ] Shared systems owned
- [ ] Risks owned
- [ ] Technical debt owned

### Collaboration

- [ ] Product relationship clear
- [ ] Architecture relationship clear
- [ ] Security relationship clear
- [ ] Operations relationship clear
- [ ] Quality relationship clear

### Sustainability

- [ ] Backup ownership
- [ ] Knowledge distribution
- [ ] Escalation path

---

# Engineering Decision

Engineering responsibilities shall be explicit, appropriately scoped, and aligned with decision authority.

The goal is not to assign every activity to a named individual.

The goal is to ensure that **important engineering outcomes never become nobody's responsibility**.