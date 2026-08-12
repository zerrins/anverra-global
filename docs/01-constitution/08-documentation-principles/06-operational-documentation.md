---
document: Operational Documentation
id: AEC-DOC-006
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-DOC-002
  - AEC-DOC-003
  - AEC-QLT-000
  - AEC-DEV-000
---

# Purpose

Define the constitutional standards for documenting the operation, deployment, monitoring, troubleshooting, recovery, and lifecycle management of software systems.

Operational documentation enables engineers to operate systems safely and consistently without relying on tribal knowledge.

---

# Intent

An engineer responding to an operational problem should be able to determine:

- What is happening?
- What should normally happen?
- How do I verify the state?
- What actions are safe?
- How do I recover?
- When should I escalate?
- How do I prevent recurrence?

Operational documentation shall support real-world engineering action.

---

# Problem Statement

Missing or outdated operational documentation increases:

- Mean time to recovery
- Incident severity
- Operational dependency on individuals
- Deployment risk
- Recovery errors
- Knowledge loss

Production systems require documented operational knowledge.

---

# Constitutional Decision

Every production system shall maintain sufficient operational documentation to support:

- Deployment
- Configuration
- Monitoring
- Troubleshooting
- Recovery
- Rollback
- Incident response
- Routine maintenance

---

# Rationale

Production incidents are time-sensitive.

Engineers should not need to reconstruct operational procedures while a system is failing.

Operational documentation reduces decision time and operational risk.

---

# Operational Documentation Philosophy

## Actionable

Operational documentation should tell engineers what to do.

---

## Evidence Driven

Procedures should be based on actual system behavior.

---

## Safe

Dangerous actions should be clearly identified.

---

## Recoverable

Critical systems should have documented recovery paths.

---

## Tested

Important procedures should be validated periodically.

---

# Operational Documentation Categories

Operational documentation includes:

- Deployment guides
- Rollback procedures
- Runbooks
- Playbooks
- Monitoring guides
- Troubleshooting guides
- Incident procedures
- Disaster recovery procedures
- Backup and restore procedures
- Configuration guides
- Capacity guidance
- Maintenance procedures

---

# Deployment Documentation

Deployment documentation should explain:

- Prerequisites
- Build requirements
- Configuration
- Deployment sequence
- Verification
- Rollback
- Known risks

---

# Rollback Documentation

Rollback procedures shall explain:

- When rollback is appropriate
- How to execute it
- Expected behavior
- Verification
- Data implications
- Follow-up actions

Rollback procedures should be tested where practical.

---

# Runbooks

A runbook should describe a repeatable operational procedure.

A runbook should include:

```text
Purpose

Prerequisites

Symptoms

Diagnosis

Procedure

Verification

Rollback / Recovery

Escalation
```

---

# Playbooks

Playbooks are broader than runbooks.

They guide engineers through complex operational scenarios.

Examples:

- Major outage
- Security incident
- Database failure
- Region failure
- Message backlog
- Dependency outage

---

# Monitoring Documentation

Monitoring documentation should explain:

- Important metrics
- Expected ranges
- Alert meaning
- Dashboards
- Log locations
- Tracing
- Escalation paths

An alert without documented interpretation is incomplete operational knowledge.

---

# Alert Documentation

Important alerts should communicate:

- What triggered the alert
- Why it matters
- First diagnostic actions
- Common causes
- Recovery options
- Escalation

---

# Troubleshooting Documentation

Troubleshooting guides should be structured around symptoms and evidence.

Example:

```text
Symptom
 ↓
Possible Causes
 ↓
Diagnostic Commands
 ↓
Expected Results
 ↓
Corrective Action
 ↓
Verification
```

---

# Incident Documentation

Operational incidents should produce durable knowledge where appropriate.

Documentation may include:

- Timeline
- Root cause
- Contributing factors
- Detection
- Resolution
- Preventive actions

Incident documentation should focus on learning rather than blame.

---

# Disaster Recovery Documentation

Critical systems shall document:

- Recovery objectives
- Backup strategy
- Restoration procedures
- Dependencies
- Recovery sequence
- Validation
- Failure scenarios

---

# Backup Documentation

Documentation should identify:

- What is backed up
- Backup frequency
- Retention
- Storage
- Encryption
- Restoration process
- Verification

A backup strategy without a tested restore process is incomplete.

---

# Configuration Documentation

Important configuration should document:

- Purpose
- Default behavior
- Allowed values
- Environment differences
- Security implications

Secrets shall never be documented directly.

---

# Environment Documentation

Differences between:

- Local
- Development
- Test
- Staging
- Production

should be documented where they affect engineering behavior.

---

# Dependency Documentation

Critical runtime dependencies should be documented.

Examples:

- Databases
- Message brokers
- External APIs
- Identity providers
- Storage
- Infrastructure services

---

# Operational Ownership

Operational documentation should identify the responsible team or ownership boundary.

Ownership should not depend on one individual.

---

# Operational Documentation Lifecycle

```text
Design

↓

Deploy

↓

Operate

↓

Observe

↓

Incident

↓

Learn

↓

Update

↓

Validate

↓

Repeat
```

Operational knowledge should improve through real operational experience.

---

# Procedure Validation

Critical procedures should be tested periodically.

Examples:

- Restore database
- Roll back deployment
- Rotate credentials
- Recover service
- Rebuild infrastructure

Untested procedures should be clearly identified.

---

# AI Guidance

AI shall:

- Read operational documentation before suggesting production actions.
- Prefer documented recovery procedures.
- Identify missing runbooks.
- Update procedures after meaningful operational changes.
- Distinguish verified procedures from suggestions.
- Avoid inventing operational commands.

AI shall not recommend dangerous production actions without appropriate context and human validation.

---

# Human Responsibilities

Humans remain responsible for:

- Production decisions
- Operational risk
- Recovery approval
- Incident command
- Disaster recovery validation
- Procedure ownership

---

# Mandatory Rules

Production systems shall maintain:

- Deployment documentation
- Rollback procedures
- Monitoring guidance
- Troubleshooting guidance
- Recovery procedures
- Ownership information

Critical procedures shall be reviewed periodically.

---

# Recommended Practices

Test runbooks.

Automate repetitive procedures.

Keep operational commands current.

Link alerts to relevant runbooks.

Capture incident learning.

Prefer executable documentation where practical.

---

# Prohibited Practices

Do not:

- Depend solely on tribal knowledge.
- Document untested recovery procedures as guaranteed.
- Store production secrets in runbooks.
- Leave critical alerts without interpretation.
- Publish dangerous commands without context.

---

# Allowed Exceptions

Low-risk experimental environments may maintain simplified operational documentation.

Production-critical systems shall satisfy full requirements.

---

# Success Metrics

| Metric | Target |
|---|---:|
| Production Systems with Operational Documentation | 100% |
| Critical Alerts with Runbooks | 100% |
| Critical Recovery Procedures Tested | 100% |
| Undocumented Production Dependencies | 0 |
| Operational Documentation Drift | 0 |

---

# Review Checklist

Verify:

- Deployment documented.
- Rollback documented.
- Monitoring documented.
- Critical alerts linked to guidance.
- Troubleshooting documented.
- Recovery documented.
- Ownership identified.
- Secrets excluded.
- Procedures validated.

---

# Examples

## Good

```text
Alert: Database Connection Pool Exhausted

↓

Check Pool Metrics

↓

Check Active Connections

↓

Check Slow Queries

↓

Apply Approved Remediation

↓

Verify Recovery

↓

Document Incident
```

---

## Poor

```text
Production Down

↓

Ask Someone Who Knows
```

This is tribal knowledge rather than operational engineering.

---

# Anti-patterns

Tribal Operations

Untested Runbooks

Runbook Drift

Alert Without Guidance

Recovery by Memory

Secret Leakage

Single-Person Operations

---

# Constitutional Compliance Matrix

| Constitution | Status |
|---|---|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |
| Documentation Principles | Mandatory |

---

# Engineering Decision

Operational documentation is a production reliability capability.

Every production system shall preserve sufficient operational knowledge to allow trained engineers to deploy, diagnose, recover, and safely operate the system without depending on undocumented individual knowledge.

---

# References

- Site Reliability Engineering
- Incident Management Practices
- Disaster Recovery Principles
- Engineering Constitution

---

# Related Documents

- Documentation Philosophy
- Documentation Standards
- API Documentation
- Decision Documentation
- Quality Principles
- Security Principles
- AI Documentation