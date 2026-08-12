---
document: Technical Debt Governance
id: AEC-GOV-010
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-004
  - AEC-GOV-005
  - AEC-ARC-000
  - AEC-REV-014
---

# Purpose

Define how technical debt is identified, classified, owned, prioritized, monitored, and reduced across engineering systems.

---

# Intent

Technical debt governance should prevent technical debt from becoming:

- Invisible.
- Unowned.
- Permanently deferred.
- A source of repeated incidents.
- A barrier to engineering evolution.

---

# Constitutional Decision

Technical debt is an engineering liability that should be managed as part of the engineering portfolio.

Not all technical debt must be eliminated.

Debt should be understood, owned, prioritized, and deliberately managed.

---

# What Is Technical Debt?

Technical debt is a current engineering compromise that creates future cost, risk, or reduced capability.

Examples:

- Outdated dependencies.
- Temporary architecture.
- Missing automation.
- Weak test coverage.
- Manual operational processes.
- Deprecated technologies.
- Poorly understood systems.

---

# Intentional vs Unintentional Debt

## Intentional Debt

A conscious trade-off.

Example:

```text
Deliver minimal implementation now
        ↓
Plan architectural improvement later
```

Intentional debt should be documented.

---

## Unintentional Debt

Debt created without deliberate recognition.

Examples:

- Poor implementation.
- Missing tests.
- Architecture drift.
- Documentation decay.

Unintentional debt should be identified and corrected where practical.

---

# Technical Debt Lifecycle

```text
Identify
   ↓
Classify
   ↓
Estimate Impact
   ↓
Assign Owner
   ↓
Prioritize
   ↓
Plan
   ↓
Remediate
   ↓
Verify
   ↓
Close
```

---

# Debt Ownership

Every significant technical debt item should have an owner.

The owner is responsible for:

- Understanding the debt.
- Assessing impact.
- Maintaining priority.
- Planning remediation.
- Escalating risk.

---

# Debt Classification

Technical debt may be classified by type:

```text
Architecture
Code
Testing
Infrastructure
Security
Dependency
Documentation
Operations
Data
Tooling
```

---

# Debt Severity

A conceptual model:

```text
Critical
High
Medium
Low
```

Severity should reflect engineering and business impact.

---

# Debt Impact

Consider:

- Development slowdown.
- Reliability.
- Security.
- Cost.
- Performance.
- Scalability.
- Maintainability.
- Customer impact.

---

# Debt Interest

Technical debt creates ongoing "interest."

Examples:

```text
Old dependency
    ↓
Security patch difficulty
    ↓
More engineering effort
```

or:

```text
Poor architecture
    ↓
Every feature requires workarounds
    ↓
Delivery slows
```

---

# Debt Principal

Principal is the effort required to eliminate or substantially reduce the debt.

---

# Debt Prioritization

Prioritize using:

```text
Impact
+
Risk
+
Frequency
+
Interest
+
Remediation Cost
+
Strategic Importance
```

---

# High-Priority Debt

Examples:

- Security vulnerabilities.
- Repeated production failures.
- Unsupported infrastructure.
- Data integrity risks.
- Architectural constraints blocking important capabilities.

---

# Low-Priority Debt

Examples:

- Minor code cleanup.
- Non-critical documentation gaps.
- Cosmetic inconsistencies.

Low priority does not mean no value.

---

# Technical Debt Register

A debt register may include:

```text
ID
Description
Type
System
Owner
Severity
Impact
Interest
Estimated Effort
Priority
Target
Status
```

---

# Debt and Roadmaps

Significant debt should influence roadmap planning.

Technical debt should compete for capacity alongside:

- Features.
- Reliability.
- Security.
- Infrastructure.
- Developer productivity.

---

# Debt Budget

Organizations may allocate explicit engineering capacity to debt reduction.

The exact percentage should depend on system condition and organizational priorities.

---

# Debt and Incidents

Repeated incidents may indicate technical debt.

Example:

```text
Repeated memory failure
        ↓
Known architectural limitation
        ↓
Technical debt
        ↓
Remediation priority
```

---

# Debt and Security

Security debt should receive appropriate priority because accumulated security weaknesses may increase organizational risk.

---

# Debt and Architecture

Architecture debt may include:

- Tight coupling.
- Unclear boundaries.
- Legacy dependencies.
- Scaling limitations.
- Duplicate systems.

---

# Debt and Testing

Testing debt may include:

- Missing regression tests.
- Unstable test suites.
- Slow test execution.
- Poor test isolation.

---

# Debt and Documentation

Documentation debt includes:

- Missing architecture.
- Outdated runbooks.
- Incorrect API documentation.
- Missing operational procedures.

---

# Debt and AI

AI can help identify:

- Duplicated code.
- Outdated patterns.
- Missing tests.
- Documentation gaps.
- Potential architectural inconsistencies.

AI findings require validation.

---

# Debt Remediation

Debt remediation should ideally be connected to normal engineering work.

Examples:

```text
Feature Change
    +
Relevant Refactoring
```

This can reduce the cost of separate remediation projects.

---

# Debt Retirement

A debt item should be closed when:

- Root cause is addressed.
- Required validation is complete.
- Risk is reduced sufficiently.
- Documentation is updated.

---

# Debt Acceptance

Some debt may be intentionally retained.

Accepted debt should have:

- Owner.
- Reason.
- Risk.
- Review point.

---

# Permanent Debt

Some debt is effectively permanent because remediation cost exceeds value.

This should be a deliberate decision rather than accidental neglect.

---

# Debt Governance Review

Review debt periodically for:

- Aging.
- Risk.
- Interest.
- Ownership.
- Priority.

---

# Debt Metrics

Useful indicators:

- Critical debt count.
- Aging debt.
- Security debt.
- Debt remediation rate.
- Repeated debt-related incidents.
- Unowned debt.

---

# Metrics Anti-Pattern

Do not measure success simply as:

```text
Number of Debt Tickets Closed
```

Closing easy items while critical debt remains unresolved is not meaningful improvement.

---

# Debt Escalation

Escalate when:

- Debt creates unacceptable risk.
- Debt blocks strategic initiatives.
- Debt repeatedly causes incidents.
- No team has capacity to address critical debt.

---

# Technical Debt and Architecture Decisions

New architecture decisions should consider:

> What debt does this decision create?

Every solution creates trade-offs.

The objective is to make important trade-offs visible.

---

# Technical Debt and Delivery Pressure

Short-term delivery pressure may justify debt.

However:

```text
Fast Delivery
    ↓
Technical Debt
    ↓
Future Cost
```

should be visible to decision-makers.

---

# Debt Anti-Patterns

## Debt Denial

Pretending debt does not exist.

## Debt Dump

Creating thousands of low-value tickets.

## Debt Abandonment

No owner.

## Debt as Excuse

Using technical debt to avoid legitimate delivery.

## Debt by Accident

Creating debt without recognizing the trade-off.

## Debt by Convenience

Repeatedly choosing the easiest implementation without considering long-term impact.

---

# Mandatory Rules

Technical debt governance shall:

- Make significant debt visible.
- Assign ownership.
- Classify impact.
- Prioritize meaningful debt.
- Consider debt in roadmaps.
- Review critical debt periodically.

---

# Recommended Practices

Track debt close to the systems it affects.

Address debt during related feature work.

Use incidents to identify hidden debt.

Make intentional trade-offs explicit.

---

# Prohibited Practices

Do not:

- Leave critical debt unowned.
- Treat all debt as equally important.
- Use ticket count as the primary debt metric.
- Hide debt to improve roadmap appearance.
- Assume all technical debt must be eliminated.

---

# Definition of Done

Technical debt governance is effective when:

- Significant debt is visible.
- Owners exist.
- Impact is understood.
- Priorities are explicit.
- Critical debt receives appropriate attention.
- Accepted debt is deliberate.

---

# Engineering Decision

Technical debt shall be managed as an engineering investment and risk portfolio.

The objective is not zero technical debt.

The objective is to maintain a level of technical debt that the organization can **understand, afford, operate, and evolve safely**.