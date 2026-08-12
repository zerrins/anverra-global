---
document: Engineering Maturity
id: AEC-GOV-005
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-004
  - AEC-REV-014
---

# Purpose

Define a practical model for understanding and improving engineering maturity within the Anverra Engineering Operating System (AEOS).

---

# Intent

Engineering maturity should help answer:

- How reliably do we engineer systems?
- Where are our largest capability gaps?
- Which practices are institutionalized?
- Which practices depend on individuals?
- What should improve next?

---

# Constitutional Decision

Engineering maturity shall measure the reliability and sustainability of engineering practices rather than the number of processes or documents produced.

---

# What Is Engineering Maturity?

Engineering maturity describes how consistently an organization can:

```text
Plan
 ↓
Design
 ↓
Build
 ↓
Test
 ↓
Deploy
 ↓
Operate
 ↓
Learn
 ↓
Improve
```

---

# Maturity Is Not Seniority

An organization is not mature because it has:

- Experienced engineers.
- More managers.
- More architects.
- More tools.

Maturity comes from repeatable engineering capability.

---

# Maturity Dimensions

Engineering maturity should consider:

1. Architecture.
2. Development.
3. Quality.
4. Security.
5. Operations.
6. Documentation.
7. AI usage.
8. Governance.
9. Ownership.
10. Continuous improvement.

---

# Maturity Levels

A five-level model may be used.

```text
Level 1 — Ad Hoc
Level 2 — Repeatable
Level 3 — Defined
Level 4 — Measured
Level 5 — Adaptive
```

These levels describe capability rather than organizational prestige.

---

# Level 1 — Ad Hoc

Characteristics:

- Processes depend on individuals.
- Knowledge is frequently implicit.
- Testing may be inconsistent.
- Ownership may be unclear.
- Production practices may be reactive.
- Documentation is incomplete.

Primary risk:

```text
High dependency on individual knowledge
```

---

# Level 2 — Repeatable

Characteristics:

- Basic practices exist.
- Teams repeat common workflows.
- Some automation exists.
- Ownership is increasingly explicit.
- Common engineering patterns are emerging.

Primary risk:

```text
Practices may vary significantly between teams
```

---

# Level 3 — Defined

Characteristics:

- Engineering standards are documented.
- Architecture principles are established.
- Review practices are consistent.
- Ownership is explicit.
- Quality and security practices are defined.
- Common workflows are established.

Primary risk:

```text
Standards may exist without sufficient measurement
```

---

# Level 4 — Measured

Characteristics:

- Engineering outcomes are measured.
- Reliability is observable.
- Quality trends are tracked.
- Technical debt is visible.
- Automation is mature.
- Governance decisions use evidence.

Primary risk:

```text
Optimization may focus too heavily on metrics
```

---

# Level 5 — Adaptive

Characteristics:

- Engineering continuously learns.
- Standards evolve from evidence.
- Automation improves continuously.
- Architecture adapts.
- Incidents create organizational learning.
- Teams optimize for outcomes rather than process compliance.

Primary risk:

```text
Optimization can become overly complex
```

---

# Maturity Assessment

Assessment should consider:

```text
Practice Exists
      ↓
Practice Repeatable
      ↓
Practice Consistent
      ↓
Practice Measured
      ↓
Practice Continuously Improved
```

---

# Architecture Maturity

Assess:

- Architecture documentation.
- Ownership.
- Dependency management.
- Decision records.
- Architectural consistency.
- Evolution strategy.

---

# Development Maturity

Assess:

- Coding standards.
- Code review.
- Automated validation.
- Dependency management.
- Developer tooling.
- Development consistency.

---

# Quality Maturity

Assess:

- Test strategy.
- Automated testing.
- Defect management.
- Test reliability.
- Production quality feedback.

---

# Security Maturity

Assess:

- Secure development.
- Threat modeling.
- Vulnerability management.
- Identity and access.
- Security monitoring.
- Incident readiness.

---

# Operational Maturity

Assess:

- Deployment automation.
- Observability.
- Incident response.
- Recovery.
- Capacity planning.
- Reliability engineering.

---

# Documentation Maturity

Assess:

- Documentation coverage.
- Accuracy.
- Ownership.
- Discoverability.
- Architecture documentation.
- Operational documentation.

---

# AI Maturity

Assess:

- Approved AI usage.
- AI-assisted development.
- Validation practices.
- Data protection.
- AI governance.
- AI effectiveness.

---

# Governance Maturity

Assess:

- Ownership.
- Decision authority.
- Risk management.
- Exception handling.
- Policy lifecycle.
- Governance feedback.

---

# Ownership Maturity

Assess:

- Critical systems owned.
- Backup ownership.
- Risk ownership.
- Technical debt ownership.
- Lifecycle ownership.

---

# Improvement Prioritization

Maturity gaps should be prioritized using:

```text
Risk
+
Impact
+
Frequency
+
Cost
+
Feasibility
```

---

# Maturity Assessment Should Not Become Bureaucracy

Assessment should identify meaningful improvement opportunities.

Avoid:

- Excessive questionnaires.
- Pointless scoring.
- Documentation for its own sake.
- Benchmarking teams against each other without context.

---

# Maturity Evidence

Evidence may include:

- Production metrics.
- Incident history.
- Review results.
- Test results.
- Security findings.
- Architecture records.
- Ownership records.
- Delivery data.

---

# Maturity vs Compliance

Compliance asks:

> Did we satisfy the requirement?

Maturity asks:

> How reliably do we perform this capability?

An organization can be compliant while still being immature.

---

# Maturity vs Performance

Maturity does not mean:

> Faster at everything.

A mature organization may deliberately slow down a high-risk change to improve safety.

---

# Maturity Improvement

Improvement should generally follow:

```text
Identify Gap
    ↓
Understand Cause
    ↓
Define Desired Capability
    ↓
Prioritize
    ↓
Implement
    ↓
Measure
    ↓
Learn
```

---

# Maturity and Incidents

Incidents provide maturity signals.

Example:

```text
Repeated outage
      ↓
Missing automated recovery
      ↓
Capability gap
      ↓
Engineering improvement
```

---

# Maturity and Technical Debt

Persistent technical debt may indicate:

- Poor prioritization.
- Weak ownership.
- Architecture limitations.
- Insufficient engineering capacity.

Technical debt should therefore be considered in maturity assessment.

---

# Maturity and AI

AI may accelerate engineering maturity when it improves:

- Automation.
- Documentation.
- Testing.
- Review.
- Developer productivity.

AI can also reduce maturity when teams:

- Trust generated code without validation.
- Lose system understanding.
- Increase complexity without governance.

---

# Maturity Metrics

Potential indicators:

- Deployment reliability.
- Defect escape rate.
- Mean time to recovery.
- Test reliability.
- Security findings.
- Technical debt trend.
- Documentation health.
- Ownership coverage.

Metrics should be interpreted together.

---

# Metrics Anti-Pattern

Do not define maturity as:

```text
More tests
More documents
More approvals
More tools
More meetings
```

These may be inputs, not maturity outcomes.

---

# Maturity Reviews

Maturity should be reviewed periodically.

Reviews should focus on:

- Capability gaps.
- Risks.
- Trends.
- Improvement opportunities.

---

# Maturity Roadmaps

Each organization or team may maintain a small number of high-value maturity goals.

Avoid trying to improve everything simultaneously.

---

# Mandatory Rules

Maturity assessment shall:

- Focus on engineering capability.
- Use evidence where practical.
- Identify meaningful gaps.
- Avoid equating maturity with process volume.
- Support continuous improvement.

---

# Recommended Practices

Measure outcomes.

Use incidents as learning.

Prioritize capability gaps.

Automate maturity evidence where possible.

Review maturity periodically.

---

# Prohibited Practices

Do not:

- Use maturity scores as simplistic team rankings.
- Optimize metrics instead of outcomes.
- Create documentation solely to increase maturity scores.
- Treat higher maturity as permanent.
- Assume mature organizations never experience failures.

---

# Definition of Done

Engineering maturity assessment is useful when:

- Major dimensions are evaluated.
- Current capability is understood.
- Important gaps are identified.
- Improvement priorities exist.
- Evidence supports major conclusions.
- Progress can be observed over time.

---

# Engineering Decision

Engineering maturity shall be measured by the organization's ability to consistently produce, operate, secure, and evolve reliable systems.

The objective is not to reach the highest maturity level everywhere.

The objective is to achieve **appropriate maturity for the risks and responsibilities being managed**.