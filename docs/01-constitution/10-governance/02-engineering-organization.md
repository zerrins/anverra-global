---
document: Engineering Organization
id: AEC-GOV-002
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-000
  - AEC-ARC-000
---

# Purpose

Define the conceptual engineering organizational model used to distribute responsibility, ownership, decision-making, and accountability across the engineering organization.

---

# Intent

The organization model should make it clear:

- How engineering responsibilities are grouped.
- Where decisions are made.
- How teams interact.
- How shared systems are governed.
- How specialist expertise is organized.
- How accountability flows.

---

# Constitutional Decision

Engineering organization should align responsibility with the systems, capabilities, and risks being managed.

Organizational structure should support clear ownership rather than create unnecessary hierarchy.

---

# Organizational Model

A useful engineering organization can be understood as:

```text
Engineering Leadership
        ↓
Engineering Domains
        ↓
Product / Platform Teams
        ↓
Systems / Services
        ↓
Repositories / Components
```

Specialist functions may operate across these levels.

---

# Engineering Leadership

Engineering leadership is responsible for:

- Engineering strategy.
- Organizational health.
- Resource allocation.
- Major engineering risks.
- Engineering standards.
- Capability development.
- Governance.

Leadership should establish direction without becoming the bottleneck for normal engineering decisions.

---

# Engineering Domains

Domains group related engineering capabilities or systems.

Examples may include:

- Product Engineering.
- Platform Engineering.
- Data Engineering.
- Infrastructure.
- Security.
- Quality.
- Architecture.

The exact structure may vary by organization.

---

# Product Teams

Product-oriented teams typically own a business capability or product area.

They may be responsible for:

- Requirements implementation.
- Product functionality.
- Service ownership.
- Testing.
- Operational support.

---

# Platform Teams

Platform teams provide capabilities consumed by other engineering teams.

Examples:

- CI/CD.
- Authentication infrastructure.
- Shared libraries.
- Infrastructure platforms.
- Developer tooling.

Platform teams should treat internal engineers as consumers.

---

# Specialist Functions

Specialist functions provide expertise across teams.

Examples:

```text
Security
Architecture
Quality
Infrastructure
Data
AI Governance
```

Specialists should enable teams rather than centralize every decision.

---

# Centralized vs Distributed Responsibility

A responsibility may be:

### Centralized

When consistency and specialization are critical.

Examples:

- Security policy.
- Enterprise identity.
- Critical compliance controls.

### Distributed

When local context matters more.

Examples:

- Feature implementation.
- Local testing.
- Component-level design.

### Federated

When global standards and local execution both matter.

Example:

```text
Central Security Standards
        ↓
Team-Level Security Implementation
```

---

# Team Autonomy

Teams should have autonomy over implementation decisions when:

- They own the relevant system.
- The decision does not violate mandatory standards.
- The risk remains within their authority.

---

# Decision Boundaries

Each organizational level should have an appropriate decision scope.

Example:

```text
Organization
→ Strategy / Global Policy

Domain
→ Cross-Team Standards

Team
→ Product / System Decisions

Engineer
→ Implementation Decisions
```

These boundaries may overlap for high-risk decisions.

---

# Cross-Team Collaboration

Cross-team changes should establish:

- Responsible teams.
- Dependent teams.
- Decision owners.
- Integration points.
- Escalation paths.

---

# Shared Systems

Shared systems require explicit ownership.

Examples:

- Shared databases.
- Authentication.
- CI infrastructure.
- Shared APIs.
- Developer platforms.

No critical shared system should depend on informal ownership.

---

# System Ownership

System ownership should include responsibility for:

- Architecture.
- Code.
- Operational health.
- Security.
- Documentation.
- Technical debt.

Ownership does not mean one team performs every task.

---

# Product and Engineering Alignment

Engineering organization should maintain connection between:

```text
Business Outcome
      ↓
Product Requirement
      ↓
Engineering Work
      ↓
Technical System
      ↓
Operational Outcome
```

Engineering should understand the business impact of systems it owns.

---

# Engineering and Product

Engineering should collaborate with product functions on:

- Requirements.
- Priorities.
- Feasibility.
- Risk.
- Technical trade-offs.
- Delivery sequencing.

Engineering should retain responsibility for engineering quality and technical decisions within its authority.

---

# Engineering and Security

Security should collaborate with engineering throughout the lifecycle.

Security should not operate solely as a final approval gate.

---

# Engineering and Architecture

Architecture should help teams:

- Understand system boundaries.
- Evaluate major changes.
- Resolve cross-team architectural concerns.
- Maintain architectural consistency.

Architecture should avoid unnecessary centralization of routine decisions.

---

# Engineering and Operations

Engineering and operations should share responsibility for production outcomes.

The organization should avoid a model where:

```text
Engineering builds
        ↓
Operations inherits
```

Instead:

```text
Engineering + Operations
        ↓
Production Ownership
```

---

# Engineering and Quality

Quality should be treated as an engineering responsibility.

Specialist quality functions may provide:

- Standards.
- Tooling.
- Guidance.
- Test strategy.
- Quality analysis.

They should not become the sole owners of product quality.

---

# Engineering and AI

AI capabilities should be governed while remaining accessible to engineering teams where appropriate.

Responsibilities may include:

- AI usage standards.
- Model evaluation.
- Data protection.
- AI-assisted development.
- AI risk management.

---

# Organizational Interfaces

Teams should have defined interfaces for:

- Requirements.
- Architecture.
- Security.
- Operations.
- Product.
- Quality.

Clear interfaces reduce organizational coupling.

---

# Organizational Coupling

Organizational coupling increases when:

- Too many approvals are required.
- Teams depend on specific individuals.
- Ownership is ambiguous.
- Shared systems lack clear boundaries.

Organizational design should reduce unnecessary coupling.

---

# Single Points of Organizational Failure

Avoid dependencies on:

- One architect.
- One security expert.
- One database expert.
- One developer.

Critical knowledge should be distributed.

---

# Knowledge Distribution

Important systems should have:

- Documentation.
- Multiple engineers familiar with the system.
- Runbooks.
- Ownership records.

---

# Succession

Critical systems should have backup ownership.

A system should remain operable when its primary owner is unavailable.

---

# Engineering Communities

Organizations may create communities of practice for:

- Architecture.
- Security.
- Testing.
- AI.
- Performance.
- Developer productivity.

Communities should share knowledge rather than become unnecessary approval boards.

---

# Technical Leadership

Technical leaders should:

- Establish direction.
- Resolve ambiguity.
- Mentor engineers.
- Identify risks.
- Maintain technical quality.

Leadership should not require personally reviewing every engineering decision.

---

# Engineering Management

Engineering management is responsible for:

- Team health.
- Delivery environment.
- Staffing.
- Capability development.
- Organizational risks.
- Sustainable execution.

Engineering management and technical authority may overlap but are not identical.

---

# Decision-Making Model

A useful model is:

```text
Closest Competent Decision Maker
             ↓
Makes the Decision
             ↓
Escalates Only When Necessary
```

Escalation should occur when:

- Risk exceeds authority.
- Impact crosses organizational boundaries.
- Expertise is insufficient.
- Policies conflict.

---

# Organizational Governance Flow

```text
Strategic Direction
        ↓
Engineering Standards
        ↓
Domain Guidance
        ↓
Team Decisions
        ↓
Implementation
        ↓
Operational Feedback
        ↓
Governance Improvement
```

---

# Organizational Evolution

Engineering organization should evolve when:

- Systems become too coupled.
- Ownership becomes unclear.
- Teams become overloaded.
- Product boundaries change.
- Technology changes.
- Operational responsibilities change.

Organization should follow engineering reality rather than preserve historical structure indefinitely.

---

# Organizational Anti-Patterns

## Hero Engineer

One person becomes indispensable.

## Architecture Bottleneck

All decisions require one central architect.

## Security Gatekeeper

Security becomes a final-stage approval bottleneck.

## Operations Handoff

Engineering stops caring after deployment.

## Ownership Vacuum

Multiple teams use a system but nobody owns it.

## Team Silos

Teams optimize locally while harming the overall system.

## Excessive Centralization

Every decision requires centralized approval.

---

# Mandatory Rules

Engineering organization shall:

- Establish clear ownership.
- Define decision boundaries.
- Support cross-team collaboration.
- Maintain specialist capabilities.
- Avoid critical single-person dependencies.
- Provide escalation paths.

---

# Recommended Practices

Prefer clear team boundaries.

Distribute critical knowledge.

Use communities of practice.

Keep ownership records current.

Let teams make decisions close to the work.

---

# Prohibited Practices

Do not:

- Leave critical systems without owners.
- Make one person the permanent knowledge bottleneck.
- Require central approval for routine low-risk decisions.
- Separate engineering from operational accountability.
- Use organizational hierarchy as a substitute for technical reasoning.

---

# Definition of Done

The engineering organizational model is established when:

- Engineering domains are understood.
- Team responsibilities are defined.
- Specialist functions are identified.
- Decision boundaries are clear.
- Shared-system ownership is defined.
- Escalation paths exist.
- Critical knowledge dependencies are addressed.

---

# Review Checklist

### Structure

- [ ] Engineering domains defined
- [ ] Team boundaries clear
- [ ] Specialist functions identified

### Ownership

- [ ] Systems have owners
- [ ] Shared systems have owners
- [ ] Backup ownership exists for critical systems

### Decision Making

- [ ] Decision authority defined
- [ ] Escalation path defined
- [ ] Team autonomy preserved

### Collaboration

- [ ] Cross-team interfaces clear
- [ ] Product relationship clear
- [ ] Security relationship clear
- [ ] Operations relationship clear

### Sustainability

- [ ] Knowledge distributed
- [ ] No critical single-person dependency
- [ ] Organizational risks visible

---

# Engineering Decision

The engineering organization shall be structured around clear ownership, appropriate decision authority, strong team interfaces, and distributed technical knowledge.

The organization should place decisions as close as practical to the engineers who possess the necessary context while retaining stronger governance for decisions whose impact crosses team or organizational boundaries.