---
document: Environment Management
id: AEC-REP-011
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-000
  - AEC-REP-008
  - AEC-REP-010
---

# Purpose

Define the constitutional standards governing software environments within the Anverra Engineering Operating System (AEOS).

An environment represents a controlled execution context in which software is built, tested, validated, deployed, or operated.

Every environment shall be standardized, reproducible, isolated, secure, and continuously governed.

---

# Intent

Every environment shall provide a predictable execution platform.

Software shall progress through environments in a controlled and repeatable manner.

Environment management exists to ensure:

- Reliable deployments
- Configuration consistency
- Operational stability
- Security
- Quality validation
- Release confidence

Environments are engineering assets.

---

# Problem Statement

Poor environment management frequently results in:

- Environment drift
- "Works on my machine"
- Configuration inconsistencies
- Deployment failures
- Production incidents
- Data corruption
- Manual environment setup
- Security exposure

Engineering quality cannot exceed environment quality.

---

# Repository Decision

Every repository shall follow the AEOS Environment Model.

Environment creation, configuration, deployment, and retirement shall be standardized.

---

# Rationale

Software should behave consistently regardless of execution location.

Differences between environments should be intentional, documented, and minimal.

Engineering effort should focus on software—not environment troubleshooting.

---

# Environment Philosophy

An environment is an isolated execution boundary.

Every environment should differ only in:

- Configuration
- Infrastructure
- Scale
- Data
- Security Policies

Application behavior shall remain consistent.

---

# Environment Principles

Every environment shall be:

## Isolated

Changes in one environment shall not affect another.

---

## Reproducible

Environments shall be recreated using automation.

---

## Documented

Every environment shall document:

- Purpose
- Configuration
- Access
- Dependencies
- Ownership

---

## Observable

Health, logs, metrics, and alerts shall be available.

---

## Secure

Access shall follow least privilege.

---

## Disposable

Non-production environments should be reproducible and replaceable.

---

# Canonical Environment Model

AEOS recognizes the following environments.

```
Developer Workstation

↓

Local

↓

Development

↓

Integration

↓

QA

↓

User Acceptance Testing (UAT)

↓

Staging

↓

Production

↓

Disaster Recovery
```

Promotion follows this sequence unless an approved exception exists.

---

# Environment Definitions

## Local

Purpose

Individual development.

Characteristics

- Developer owned
- Local configuration
- Disposable
- No shared data

---

## Development

Purpose

Collaborative development.

Characteristics

- Shared environment
- Continuous deployment
- Early integration
- Frequent changes

---

## Integration

Purpose

Validate communication between services.

Characteristics

- Multiple systems
- Event validation
- API compatibility
- Contract verification

---

## QA

Purpose

Functional and regression testing.

Characteristics

- Stable deployments
- Test automation
- Performance validation
- Defect verification

---

## UAT

Purpose

Business validation.

Characteristics

- Business-owned testing
- Acceptance criteria verification
- User workflows
- Release approval

---

## Staging

Purpose

Production rehearsal.

Characteristics

- Production-like infrastructure
- Final validation
- Operational testing
- Deployment verification

Staging should closely resemble Production.

---

## Production

Purpose

Serve customers.

Characteristics

- Highest availability
- Security hardened
- Monitored
- Controlled changes

---

## Disaster Recovery

Purpose

Business continuity.

Characteristics

- Backup infrastructure
- Recovery validation
- Disaster exercises
- Operational readiness

---

# Environment Promotion

Software shall move through environments in a controlled manner.

```
Build

↓

Development

↓

Integration

↓

QA

↓

UAT

↓

Staging

↓

Production
```

Skipping validation stages requires formal approval.

---

# Environment Isolation

Each environment shall have:

- Independent configuration
- Independent databases
- Independent messaging
- Independent storage
- Independent secrets
- Independent monitoring

Cross-environment dependencies shall be minimized.

---

# Environment Configuration

Configuration differences shall exist only where necessary.

Examples include:

- Connection endpoints
- Scaling parameters
- Feature flags
- Security settings
- Resource limits

Application code shall remain unchanged across environments.

---

# Test Data Management

Each environment shall define:

- Data ownership
- Refresh policy
- Retention policy
- Privacy controls

Production data shall never be copied into lower environments without approved anonymization.

---

# Environment Access

Access shall follow least privilege.

Every environment shall define:

- Owners
- Administrators
- Developers
- Operators
- Auditors

Administrative access shall be logged.

---

# Deployment Rules

Deployments shall be:

- Automated
- Repeatable
- Auditable
- Versioned
- Observable

Manual deployments to Production are prohibited except under documented emergency procedures.

---

# Monitoring

Every environment shall expose:

- Logs
- Metrics
- Health Checks
- Alerts
- Deployment Status

Production environments require comprehensive observability.

---

# Environment Drift

Environment drift shall be monitored continuously.

Unexpected drift shall trigger investigation.

Infrastructure as Code should minimize drift.

---

# Environment Retirement

Environment retirement follows:

```
Review

↓

Archive Required Data

↓

Disable Access

↓

Remove Infrastructure

↓

Update Documentation
```

Retired environments shall not retain unnecessary resources.

---

# AI Guidance

AI shall:

- Preserve environment consistency.
- Recommend Infrastructure as Code.
- Avoid environment-specific implementation.
- Validate deployment targets.
- Detect configuration drift.
- Recommend environment documentation updates.

AI shall never hardcode environment assumptions.

---

# Mandatory Rules

Repositories shall:

- Follow the canonical environment model.
- Automate environment creation.
- Separate environments.
- Monitor environment health.
- Restrict production access.
- Version deployment artifacts.

---

# Recommended Practices

Use Infrastructure as Code.

Treat environments as disposable.

Continuously validate deployments.

Maintain production parity in Staging.

Review environment health regularly.

---

# Prohibited Practices

Do not:

- Share production databases.
- Hardcode environment values.
- Deploy manually to Production.
- Modify Production directly.
- Skip validation environments.
- Allow undocumented configuration differences.

---

# Allowed Exceptions

Short-lived experimental environments may simplify selected controls while remaining isolated and clearly identified.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Environment Drift | 0 |
| Automated Environment Provisioning | 100% |
| Deployment Automation | 100% |
| Production Access Compliance | 100% |
| Infrastructure as Code Coverage | 100% |

---

# Review Checklist

Reviewers shall verify:

- Is the environment documented?
- Is configuration isolated?
- Are deployments automated?
- Is Infrastructure as Code used?
- Is monitoring configured?
- Is production protected?
- Are environment differences intentional?
- Is data handled appropriately?

---

# Examples

## Good

```
Infrastructure as Code

↓

Provision Environment

↓

Deploy

↓

Validate

↓

Monitor
```

---

## Poor

```
Manual Server Setup

↓

Manual Configuration

↓

Manual Deployment

↓

Unknown Differences
```

---

# Anti-patterns

Works on My Machine

Snowflake Servers

Shared Test Databases

Manual Production Changes

Configuration Drift

Environment by Memory

Hidden Infrastructure

Production Debugging Environment

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| Repository Principles | Mandatory |
| AI Engineering Principles | Mandatory |

---

# Engineering Decision

Environments are governed engineering assets.

Every environment shall be reproducible, isolated, observable, secure, and consistently managed throughout its lifecycle.

Environment consistency is essential for reliable software delivery.

---

# References

- Twelve-Factor App
- Infrastructure as Code Principles
- Engineering Constitution
- Build Standards

---

# Related Documents

- Configuration Management
- Secrets Management
- Build Standards
- Deployment Standards
- Infrastructure Principles