---
document: Change Management
id: AEC-GOV-009
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-003
  - AEC-GOV-004
  - AEC-REV-001
  - AEC-REV-014
  - AEC-OPS-000
---

# Purpose

Define how engineering changes are classified, evaluated, approved, implemented, monitored, and recovered.

---

# Intent

Change management should provide confidence that:

- Changes are understood.
- Risk is proportional to impact.
- Required review occurs.
- Deployment is controlled.
- Recovery is possible.
- Production outcomes are observable.

---

# Constitutional Decision

Engineering changes shall be governed according to risk, impact, reversibility, and operational consequences.

Change management should enable safe delivery rather than create unnecessary deployment bureaucracy.

---

# What Is a Change?

A change includes any modification that may alter system behavior, architecture, configuration, infrastructure, security posture, data, or operational characteristics.

Examples:

- Code changes.
- Configuration changes.
- Database changes.
- Infrastructure changes.
- Dependency upgrades.
- API changes.
- Security changes.
- Production migrations.

---

# Change Lifecycle

```text
Change Identified
      ↓
Risk Classified
      ↓
Designed
      ↓
Reviewed
      ↓
Validated
      ↓
Approved
      ↓
Deployed
      ↓
Monitored
      ↓
Verified
      ↓
Closed
```

---

# Change Classification

Changes may be classified as:

```text
Standard
Normal
High Risk
Emergency
```

---

# Standard Change

A standard change is:

- Well understood.
- Repeatable.
- Low risk.
- Supported by an established procedure.

Examples may include routine maintenance with validated automation.

---

# Normal Change

A normal change requires explicit assessment and appropriate review.

Examples:

- Feature release.
- Dependency upgrade.
- API change.
- Configuration change.

---

# High-Risk Change

A high-risk change may have:

- Large blast radius.
- Difficult rollback.
- Security impact.
- Data migration.
- Architectural consequences.
- Major production impact.

These require stronger review.

---

# Emergency Change

Emergency changes address urgent situations such as:

- Critical security vulnerability.
- Major production outage.
- Severe data integrity problem.

Emergency procedures may reduce normal lead time but should not eliminate accountability.

---

# Change Risk

Risk should consider:

```text
Impact
+
Probability
+
Blast Radius
+
Reversibility
+
Observability
```

---

# Blast Radius

Consider:

- Number of users.
- Number of systems.
- Data affected.
- Geographic scope.
- Dependency impact.

---

# Reversibility

A reversible change is generally easier to govern than an irreversible one.

Examples:

```text
Feature Flag
→ Highly reversible

Database Destruction
→ Difficult / irreversible
```

---

# Change Ownership

Every significant change should have an owner responsible for:

- Preparation.
- Validation.
- Deployment.
- Monitoring.
- Recovery.

---

# Change Review

Change review should consider:

- Correctness.
- Security.
- Architecture.
- Testing.
- Operations.
- Rollback.

The depth depends on risk.

---

# Change Evidence

Evidence may include:

- Tests.
- Review results.
- Security scans.
- Performance results.
- Deployment plans.
- Rollback plans.

---

# Deployment Strategy

Where appropriate, use safer deployment patterns:

- Canary.
- Blue/green.
- Rolling deployment.
- Feature flags.
- Progressive rollout.

---

# Feature Flags

Feature flags may separate:

```text
Deployment
    from
Feature Activation
```

This can reduce deployment risk.

Feature flags themselves require lifecycle management.

---

# Database Changes

Database changes may require additional review because they can affect:

- Data integrity.
- Compatibility.
- Performance.
- Rollback.
- Existing consumers.

---

# Backward Compatibility

Where systems have independent deployment cycles, changes should consider compatibility.

Example:

```text
Old Consumer
     ↓
New API
```

should remain compatible where required.

---

# Rollback

Every significant production change should consider:

- Can it be rolled back?
- How?
- How quickly?
- What happens to data?
- What if rollback is impossible?

---

# Recovery When Rollback Is Impossible

Some changes cannot safely be rolled back.

In these cases, define:

- Backup.
- Forward repair.
- Recovery procedure.
- Data validation.

---

# Pre-Deployment Validation

Before deployment, confirm applicable:

- Tests.
- Security checks.
- Review.
- Configuration.
- Dependencies.
- Operational readiness.

---

# Deployment Authorization

Authorization should correspond to change risk.

Low-risk changes may use automated approval.

High-risk changes may require explicit human authorization.

---

# Production Monitoring

After deployment, monitor:

- Errors.
- Latency.
- Availability.
- Resource usage.
- Business metrics.
- Security signals.

---

# Change Verification

A deployment is not complete merely because it succeeded technically.

Verify:

```text
Deployment Success
        ↓
System Health
        ↓
Expected Behavior
        ↓
Business Outcome
```

---

# Failed Changes

A failed change should result in:

- Containment.
- Recovery.
- Investigation.
- Learning.

Do not treat every failed deployment as individual failure.

The system should be designed to make failure recoverable.

---

# Change Incidents

If a change causes an incident:

```text
Stabilize
   ↓
Recover
   ↓
Investigate
   ↓
Identify Cause
   ↓
Improve Control
```

---

# Change Metrics

Useful indicators:

- Change failure rate.
- Deployment frequency.
- Lead time.
- Rollback frequency.
- Mean time to recovery.
- Emergency change frequency.

Metrics should be interpreted together.

---

# Change Metrics Anti-Pattern

Do not optimize for:

```text
Maximum Deployment Frequency
```

while ignoring:

```text
Change Failure
+
Recovery
+
Customer Impact
```

---

# Emergency Changes

Emergency changes should receive retrospective review.

Questions:

- Why was emergency change necessary?
- Could normal controls have detected the issue?
- Can the emergency scenario be prevented?
- Should automation be improved?

---

# Change Exceptions

Exceptions should document:

- Normal control bypassed.
- Reason.
- Risk.
- Mitigation.
- Owner.
- Approval.

---

# Change Automation

Automate:

- Validation.
- Testing.
- Deployment.
- Rollback where practical.
- Health verification.

Automation reduces human error when appropriately designed.

---

# AI and Change Management

AI may assist with:

- Change summaries.
- Risk identification.
- Test generation.
- Deployment analysis.

AI should not independently authorize high-risk changes without explicit governance.

---

# Change Governance Review

Change governance should periodically examine:

- Failed changes.
- Emergency changes.
- Rollbacks.
- Repeated deployment issues.
- Automation opportunities.

---

# Change Anti-Patterns

## Manual Production Ritual

Large manual procedures with no technical justification.

## Approval Theater

Multiple approvals without meaningful risk reduction.

## No Rollback Thinking

Assuming deployment success means recovery is unnecessary.

## Emergency as Normal

Frequent emergency changes indicate deeper engineering problems.

## Deployment Without Verification

Assuming a successful deployment equals a successful change.

---

# Mandatory Rules

Change management shall:

- Classify meaningful changes.
- Match controls to risk.
- Assign change ownership.
- Require appropriate validation.
- Consider recovery.
- Verify production outcomes.
- Learn from failed changes.

---

# Recommended Practices

Prefer automated deployment.

Use progressive rollout for high-risk changes.

Separate deployment from activation where useful.

Automate health verification.

Keep emergency procedures well defined.

---

# Prohibited Practices

Do not:

- Deploy high-risk changes without appropriate authorization.
- Treat emergency changes as a normal shortcut.
- Assume rollback is always possible.
- Ignore post-deployment verification.
- Use approval count as the primary measure of change safety.

---

# Definition of Done

A change is governed adequately when:

- Risk is classified.
- Owner exists.
- Review is complete.
- Validation is complete.
- Deployment is authorized.
- Recovery is understood.
- Production behavior is verified.

---

# Engineering Decision

Change management shall provide the minimum controls necessary to make engineering changes safe, observable, and recoverable.

The goal is not to eliminate change risk.

The goal is to make change risk **understood, controlled, observable, and recoverable**.