---
document: Operational Review
id: AEC-REV-009
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering Operations
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-REV-004
  - AEC-REV-006
  - AEC-DOC-006
---

# Purpose

Define the principles, standards, workflow, and completion criteria for operational review of engineering changes.

Operational review determines whether a system or change can be safely deployed, operated, monitored, recovered, and supported.

---

# Intent

Operational review should answer:

- Can this change be deployed safely?
- Can it be rolled back?
- Can operators understand its health?
- Can failures be detected?
- Can the system recover?
- Are capacity requirements understood?
- Are operational dependencies identified?
- Is the runbook sufficient?
- Can the system be supported after release?

---

# Constitutional Decision

Changes with meaningful production impact shall receive operational review proportional to operational risk.

Production readiness is part of engineering correctness.

---

# Operational Review Scope

Operational review may apply to:

- Application changes.
- Infrastructure changes.
- Database changes.
- Configuration changes.
- Deployment changes.
- New services.
- New dependencies.
- Scaling changes.
- Reliability changes.
- Disaster recovery changes.

---

# Operational Readiness

A change is operationally ready when the organization can:

```text
Deploy
Observe
Operate
Diagnose
Recover
Rollback
```

with appropriate confidence.

---

# Deployment Review

Evaluate:

- Deployment sequence.
- Dependencies.
- Compatibility.
- Configuration.
- Migration.
- Rollout strategy.

---

# Rollout Strategy

Consider:

- Big-bang deployment.
- Rolling deployment.
- Canary.
- Blue/green.
- Feature flags.
- Incremental rollout.

The rollout strategy should match risk.

---

# Rollback

Review:

- Can application code be reverted?
- Can configuration be reverted?
- Can database changes be reverted?
- Can external side effects be reversed?
- What happens to data created by the new version?

Rollback assumptions must be explicit.

---

# Database Changes

Operational review should consider:

- Migration duration.
- Locking.
- Backward compatibility.
- Data volume.
- Rollback.
- Partial migration.
- Production performance.

---

# Configuration

Review:

- Defaults.
- Environment-specific values.
- Secret dependencies.
- Configuration rollout.
- Safe rollback.

---

# Dependencies

Evaluate:

- External services.
- Databases.
- Queues.
- APIs.
- Identity providers.
- Infrastructure services.

For each critical dependency:

- Failure behavior.
- Timeout.
- Retry.
- Monitoring.
- Recovery.

---

# Capacity

Review expected:

- CPU.
- Memory.
- Storage.
- Network.
- Database connections.
- Queue capacity.

Capacity requirements should be based on expected workload where possible.

---

# Scaling

Review:

- Horizontal scaling.
- Vertical scaling.
- Autoscaling.
- Bottlenecks.
- Scaling limits.

---

# Reliability

Consider:

- Single points of failure.
- Dependency failures.
- Retry storms.
- Cascading failures.
- Resource exhaustion.
- Recovery.

---

# Observability

Operational review should verify appropriate:

## Logs

Useful diagnostic information.

## Metrics

Health and performance indicators.

## Traces

Distributed request visibility where applicable.

## Alerts

Actionable notifications.

---

# Alert Quality

Alerts should be:

- Actionable.
- Relevant.
- Understandable.
- Appropriately prioritized.

Avoid alerts that generate noise without requiring action.

---

# Health Checks

Health checks should distinguish:

- Process availability.
- Application readiness.
- Dependency readiness.

Do not make a health check dependent on unnecessary components unless the operational semantics require it.

---

# Failure Detection

Review how operators detect:

- Service failure.
- Dependency failure.
- Performance degradation.
- Data processing failure.
- Security events.

---

# Recovery

Recovery procedures should identify:

- Preconditions.
- Steps.
- Expected outcomes.
- Validation.
- Escalation.

---

# Runbooks

Operationally significant systems should have appropriate runbooks.

Runbooks should answer:

```text
What happened?

How do I diagnose it?

What can I safely do?

How do I recover?

When should I escalate?
```

---

# Incident Readiness

For critical systems, operational review should consider:

- Incident ownership.
- Escalation.
- Diagnostics.
- Recovery.
- Communication.

---

# Disaster Recovery

Where applicable, review:

- Backup.
- Restore.
- Recovery Point Objective.
- Recovery Time Objective.
- Failover.
- Data integrity.

Claims about recovery should be supported by evidence.

---

# Operational Security

Consider:

- Access to production.
- Administrative privileges.
- Secret access.
- Auditability.
- Break-glass procedures.

---

# Cost

Operational review may consider:

- Infrastructure cost.
- Storage cost.
- Network cost.
- Monitoring cost.
- Operational effort.

Cost should be evaluated where it materially affects the design.

---

# Deployment Safety

Production changes should consider:

```text
Pre-checks
   ↓
Deployment
   ↓
Verification
   ↓
Monitoring
   ↓
Rollback / Continue
```

---

# Post-Deployment Verification

Verification should establish:

- Service health.
- Expected behavior.
- Error rates.
- Latency.
- Resource utilization.
- Business functionality where applicable.

---

# Operational Review Workflow

```text
Change
   ↓
Operational Impact Analysis
   ↓
Deployment Plan
   ↓
Monitoring Plan
   ↓
Rollback / Recovery Plan
   ↓
Operational Review
   ↓
Approval
   ↓
Deployment
   ↓
Post-Deployment Verification
```

---

# Emergency Changes

Emergency production changes may use an abbreviated operational review.

After stabilization:

- Document the change.
- Review the outcome.
- Identify operational gaps.
- Update runbooks or standards.

---

# Operational Review and Architecture

Architecture review evaluates whether the system is designed appropriately.

Operational review evaluates whether the resulting system can be safely operated.

---

# Operational Review and Testing

Operational review should use evidence from:

- Load tests.
- Failure tests.
- Recovery tests.
- Deployment tests.

---

# AI-Assisted Operational Review

AI may assist with:

- Identifying affected services.
- Generating operational checklists.
- Reviewing deployment changes.
- Finding missing runbook updates.
- Analyzing historical incidents.

AI should not invent operational procedures.

---

# Operational Documentation

Operational review should verify affected:

- Runbooks.
- Deployment guides.
- Monitoring documentation.
- Recovery procedures.

---

# Operational Exceptions

Exceptions should document:

- Risk.
- Reason.
- Owner.
- Mitigation.
- Follow-up.

---

# Mandatory Rules

Operational review shall consider, where applicable:

- Deployment.
- Rollback.
- Monitoring.
- Recovery.
- Capacity.
- Dependencies.
- Operational ownership.

---

# Recommended Practices

Test recovery.

Use staged deployment.

Automate verification.

Keep runbooks current.

Use production-like environments for meaningful operational validation.

---

# Prohibited Practices

Do not:

- Deploy critical changes without an operational recovery strategy.
- Claim rollback exists when it has not been validated.
- Depend on undocumented operational knowledge.
- Ignore monitoring requirements.
- Treat successful deployment as proof of operational readiness.

---

# Definition of Done

Operational review is complete when:

- Operational impact is understood.
- Deployment strategy is defined.
- Rollback or recovery is understood.
- Monitoring is adequate.
- Dependencies are identified.
- Capacity is considered.
- Runbooks are updated where required.
- Required operational approvals are complete.

---

# Review Checklist

### Deployment

- [ ] Deployment strategy
- [ ] Compatibility
- [ ] Rollout
- [ ] Verification

### Recovery

- [ ] Rollback
- [ ] Recovery
- [ ] Data considerations

### Operations

- [ ] Monitoring
- [ ] Logging
- [ ] Alerts
- [ ] Diagnostics

### Capacity

- [ ] Resource requirements
- [ ] Scaling
- [ ] Bottlenecks

### Documentation

- [ ] Runbook
- [ ] Deployment documentation
- [ ] Recovery documentation

### Completion

- [ ] Operational approval
- [ ] Post-deployment plan

---

# Engineering Decision

Operational review ensures that engineering changes are not merely implementable but safely deployable, observable, recoverable, and supportable in real production environments.