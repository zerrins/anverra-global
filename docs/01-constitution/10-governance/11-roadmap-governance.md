---
document: Roadmap Governance
id: AEC-GOV-011
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-004
  - AEC-GOV-005
  - AEC-GOV-010
  - AEC-ARC-000
---

# Purpose

Define how engineering roadmaps are created, evaluated, prioritized, governed, communicated, and evolved.

---

# Intent

Roadmap governance should ensure that engineering investment considers:

- Business outcomes.
- Technical strategy.
- Architecture.
- Reliability.
- Security.
- Technical debt.
- Developer productivity.
- Operational sustainability.

---

# Constitutional Decision

Engineering roadmaps shall balance short-term delivery with long-term technical sustainability.

Feature delivery shall not be the sole measure of engineering investment.

---

# What Is an Engineering Roadmap?

An engineering roadmap describes significant future engineering outcomes and investments.

It may include:

- Product capabilities.
- Architecture changes.
- Platform work.
- Reliability improvements.
- Security initiatives.
- Technical debt reduction.
- Infrastructure evolution.

---

# Roadmap Hierarchy

A useful model is:

```text
Business Strategy
       ↓
Product Strategy
       ↓
Engineering Strategy
       ↓
Engineering Roadmap
       ↓
Team Plans
       ↓
Implementation
```

---

# Roadmap Inputs

Engineering roadmaps should consider:

- Business priorities.
- Customer needs.
- Architecture.
- Technical debt.
- Incidents.
- Security risks.
- Reliability.
- Capacity.
- Technology evolution.

---

# Roadmap Categories

Roadmap items may be classified as:

```text
Product Enablement
Architecture
Reliability
Security
Technical Debt
Infrastructure
Developer Productivity
Compliance
Innovation
```

---

# Product Work

Product work delivers business capabilities.

Engineering should evaluate:

- Technical feasibility.
- Dependencies.
- Complexity.
- Risk.
- Operational impact.

---

# Architecture Work

Architecture initiatives may address:

- Scaling.
- Modularity.
- Platform evolution.
- Dependency reduction.
- System modernization.

---

# Reliability Work

Reliability initiatives may include:

- Observability.
- Resilience.
- Recovery.
- Capacity.
- Performance.

---

# Security Work

Security initiatives may include:

- Vulnerability remediation.
- Identity improvements.
- Access control.
- Security architecture.

---

# Technical Debt Work

Technical debt should be represented where:

- Risk is material.
- Debt blocks strategy.
- Maintenance cost is significant.

---

# Infrastructure Work

Infrastructure roadmaps should consider:

- Lifecycle.
- Capacity.
- Reliability.
- Security.
- Cost.
- Operational complexity.

---

# Developer Productivity

Engineering productivity investments may include:

- Tooling.
- Automation.
- CI/CD improvements.
- Development environments.
- AI-assisted workflows.

---

# Roadmap Prioritization

A roadmap item should be evaluated using:

```text
Value
+
Risk
+
Urgency
+
Strategic Alignment
+
Cost
+
Dependencies
+
Reversibility
```

---

# Prioritization Is Not Feature Count

A roadmap with more items is not necessarily better.

Prioritize outcomes rather than activity volume.

---

# Capacity Allocation

Engineering capacity should account for multiple categories.

Example:

```text
Product Delivery
+
Reliability
+
Security
+
Technical Debt
+
Platform
+
Engineering Improvement
```

The exact allocation should vary based on organizational context.

---

# Reliability Investment

When reliability risk is increasing, roadmap priorities should adapt.

Example:

```text
Repeated Incidents
      ↓
Reliability Risk
      ↓
Roadmap Priority
```

---

# Security Investment

Critical security risks may override normal roadmap priorities.

---

# Technical Debt Investment

Technical debt should be included when its cost or risk becomes material.

---

# Roadmap Dependencies

Important roadmap items should identify dependencies.

Examples:

- Teams.
- APIs.
- Infrastructure.
- Data.
- Architecture.
- External vendors.

---

# Dependency Risk

A roadmap should not assume dependency availability without reasonable validation.

---

# Roadmap Uncertainty

Long-term engineering estimates are uncertain.

Roadmaps should distinguish:

```text
Committed
Likely
Exploratory
```

rather than presenting every item as equally certain.

---

# Committed Work

Work that the organization has explicitly committed to deliver.

---

# Likely Work

Work expected to occur but subject to change.

---

# Exploratory Work

Work requiring investigation before commitment.

---

# Discovery

For uncertain initiatives, roadmap governance may require:

- Spike.
- Prototype.
- Proof of concept.
- Architecture investigation.

---

# Roadmap Changes

Roadmaps should evolve when:

- Business priorities change.
- Risk changes.
- New technical information emerges.
- Incidents occur.
- Dependencies change.
- Capacity changes.

Changing a roadmap is not inherently a failure.

---

# Roadmap Change Governance

Significant changes should explain:

- What changed.
- Why.
- Impact.
- New priority.
- Deferred work.

---

# Deferred Work

Deferred work should not disappear.

Where meaningful, retain:

- Reason.
- Future trigger.
- Owner.
- Risk.

---

# Roadmap and Technical Debt

Technical debt should influence roadmap decisions.

Example:

```text
Critical Architecture Debt
        ↓
Feature delivery becomes expensive
        ↓
Architecture initiative
        ↓
Future delivery improves
```

---

# Roadmap and Architecture

Major architectural changes should be represented explicitly rather than hidden inside unrelated feature work.

---

# Roadmap and Security

Security work should have visibility appropriate to its risk.

Security should not be treated only as unplanned operational work.

---

# Roadmap and Operations

Operational sustainability should be represented in engineering planning.

Examples:

- Capacity.
- Observability.
- Recovery.
- Platform upgrades.

---

# Roadmap Governance and AI

AI may assist roadmap planning by:

- Summarizing dependencies.
- Identifying technical debt.
- Estimating change impact.
- Finding roadmap conflicts.

AI recommendations must be validated.

---

# Roadmap Evidence

Roadmap decisions should use available evidence:

- Incident data.
- Customer impact.
- Reliability metrics.
- Security findings.
- Technical debt.
- Engineering capacity.

---

# Roadmap Metrics

Useful indicators:

- Roadmap predictability.
- Delivery outcome.
- Deferred work.
- Technical debt trend.
- Reliability investment.
- Security investment.
- Strategic alignment.

---

# Metrics Anti-Pattern

Do not optimize:

```text
Percentage of Roadmap Completed
```

without considering:

- Value delivered.
- Quality.
- Risk.
- Changes in priorities.

---

# Roadmap Review

Roadmaps should be reviewed periodically.

Review:

- Priorities.
- Risks.
- Dependencies.
- Capacity.
- Outcomes.
- Assumptions.

---

# Roadmap and Governance

Governance should ensure that important engineering investments have:

- Owner.
- Objective.
- Rationale.
- Expected outcome.
- Priority.
- Dependencies.

---

# Roadmap Anti-Patterns

## Feature-Only Roadmap

Ignores technical sustainability.

## Everything Is Priority

No meaningful prioritization exists.

## False Precision

Long-term estimates presented as certainty.

## Hidden Technical Work

Engineering work exists but is invisible to planning.

## Roadmap by Habit

Continuing initiatives because they were historically planned.

## Commitment Without Capacity

Promising work without sufficient engineering resources.

---

# Mandatory Rules

Roadmap governance shall:

- Balance business and engineering needs.
- Represent significant technical investments.
- Consider security and reliability.
- Include technical debt where material.
- Identify important dependencies.
- Allow evidence-based reprioritization.

---

# Recommended Practices

Separate committed from exploratory work.

Use outcomes rather than task counts.

Review assumptions regularly.

Make technical investments visible.

Use incidents and production data to influence priorities.

---

# Prohibited Practices

Do not:

- Hide technical debt.
- Treat every roadmap item as equally certain.
- Optimize roadmap completion percentage alone.
- Commit work without considering capacity.
- Remove deferred work without recording the reason when risk remains.

---

# Definition of Done

Roadmap governance is effective when:

- Strategic alignment exists.
- Engineering investment categories are visible.
- Priorities are explicit.
- Dependencies are understood.
- Technical debt is represented.
- Security and reliability are considered.
- Roadmaps can adapt to new evidence.

---

# Review Checklist

### Strategy

- [ ] Business alignment
- [ ] Engineering alignment
- [ ] Strategic objective

### Engineering Health

- [ ] Reliability
- [ ] Security
- [ ] Technical debt
- [ ] Infrastructure

### Execution

- [ ] Capacity
- [ ] Dependencies
- [ ] Ownership
- [ ] Uncertainty

### Governance

- [ ] Priority
- [ ] Rationale
- [ ] Expected outcome
- [ ] Review cadence

---

# Engineering Decision

Engineering roadmaps shall represent the complete engineering investment required to build, secure, operate, and evolve sustainable systems.

A healthy roadmap does not merely describe **what features will be built**.

It describes **how engineering capability will evolve to support the organization over time**.