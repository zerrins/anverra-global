---
document: Architecture Review
id: AEC-REV-006
version: 1.0.0
status: Draft
stability: Level 3
owner: Architecture
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-ARC-000
  - AEC-DOC-007
---

# Purpose

Define the standards, responsibilities, review triggers, evaluation criteria, and completion requirements for architecture reviews.

Architecture review determines whether significant engineering changes preserve or appropriately evolve the architectural integrity of the system.

---

# Intent

Architecture review should answer:

- Does the proposed change fit the system architecture?
- Are system boundaries appropriate?
- Are responsibilities correctly assigned?
- Are dependencies justified?
- Does the design introduce harmful coupling?
- Does it satisfy scalability and reliability requirements?
- Does it preserve security boundaries?
- Does it create significant operational complexity?
- Does it require an architectural decision record?

---

# Constitutional Decision

Changes with meaningful architectural impact shall receive architecture review before implementation or commitment where practical.

---

# What Is Architectural Impact

A change may be architectural when it changes:

- System boundaries.
- Component responsibilities.
- Data ownership.
- Integration patterns.
- Communication patterns.
- Deployment topology.
- Security boundaries.
- Persistence strategy.
- Scalability model.
- Availability model.
- Technology foundations.

---

# Architecture Review Is Not Code Review

Code review primarily evaluates implementation.

Architecture review evaluates system-level consequences.

Example:

```text
Code Review

"Does this implementation correctly perform the operation?"

Architecture Review

"Should this responsibility exist in this component at all?"
```

Both may be required.

---

# Architecture Review Triggers

Architecture review should be considered for:

- New services.
- Service decomposition.
- Major integrations.
- Database architecture changes.
- Event-driven architecture changes.
- Authentication architecture.
- Infrastructure topology changes.
- Major technology changes.
- Significant scaling changes.
- Cross-domain data ownership changes.

---

# Architecture Principles

Architecture should optimize for:

- Clear boundaries.
- Explicit ownership.
- Appropriate coupling.
- Cohesion.
- Reliability.
- Security.
- Operability.
- Evolvability.
- Appropriate simplicity.

---

# Architecture Review Inputs

A proposal should provide:

- Current architecture.
- Target architecture.
- Problem.
- Requirements.
- Constraints.
- Proposed changes.
- Alternatives.
- Trade-offs.
- Risks.
- Migration strategy.

---

# Current State

The review should establish:

```text
What exists today?
```

Without understanding the current state, architectural change cannot be evaluated accurately.

---

# Target State

The proposal should establish:

```text
What should exist after the change?
```

The target state should be understandable without relying entirely on implementation details.

---

# Architecture Delta

The review should make the architectural delta visible.

Example:

```text
Current

Service A → Database


Target

Service A → Service B → Database
```

The review should explain why the additional boundary is justified.

---

# Boundaries

Review:

- Component boundaries.
- Service boundaries.
- Domain boundaries.
- Data ownership boundaries.
- Security boundaries.

Boundaries should correspond to meaningful responsibilities.

---

# Ownership

Every major architectural responsibility should have clear ownership.

Avoid ambiguous ownership such as:

```text
Service A sometimes owns the data.
Service B sometimes owns the data.
```

unless the architecture explicitly defines the coordination model.

---

# Coupling

Review should identify:

- Synchronous coupling.
- Data coupling.
- Deployment coupling.
- Temporal coupling.
- Operational coupling.

Adding a component may reduce one type of coupling while increasing another.

---

# Cohesion

Components should group responsibilities that naturally belong together.

Avoid creating services or modules solely because decomposition appears architecturally sophisticated.

---

# Dependency Review

Review dependencies for:

- Direction.
- Ownership.
- Failure behavior.
- Availability.
- Versioning.
- Security.
- Operational impact.

---

# Communication Patterns

Evaluate whether communication should be:

- Synchronous.
- Asynchronous.
- Event-driven.
- Batch.
- Streaming.

The choice should reflect business and technical requirements.

---

# Data Ownership

Architecture review should establish:

- Who owns the data?
- Who can modify it?
- Who can consume it?
- What consistency guarantees exist?
- How is data synchronized?

---

# Consistency

Evaluate required consistency rather than assuming:

```text
Strong consistency is always better.
```

The architecture should use the consistency model appropriate to the business requirement.

---

# Reliability

Architecture review should evaluate:

- Single points of failure.
- Dependency failure.
- Retry storms.
- Cascading failure.
- Recovery.
- Availability.

---

# Scalability

Evaluate:

- Expected load.
- Growth.
- Bottlenecks.
- Horizontal scaling.
- Vertical scaling.
- Data scaling.
- Operational scaling.

Avoid designing for arbitrary scale without meaningful requirements.

---

# Security Architecture

Evaluate:

- Trust boundaries.
- Identity.
- Authorization.
- Secrets.
- Data sensitivity.
- Network boundaries.
- Privilege.

Security review may be required separately.

---

# Observability Architecture

Significant architectural changes should consider:

- Metrics.
- Logging.
- Tracing.
- Health checks.
- Diagnostics.
- Alerting.

---

# Operational Architecture

Consider:

- Deployment.
- Configuration.
- Service ownership.
- Recovery.
- Rollback.
- Capacity management.
- Monitoring.

---

# Technology Selection

When introducing significant technology, review:

- Problem fit.
- Complexity.
- Operational cost.
- Team capability.
- Security.
- Maturity.
- Long-term maintenance.
- Integration.

Technology novelty is not itself a justification.

---

# Architecture Trade-offs

Important trade-offs should be explicit.

Examples:

```text
Centralization vs Autonomy

Synchronous vs Asynchronous

Consistency vs Availability

Simplicity vs Flexibility

Build vs Buy

Performance vs Cost
```

---

# Alternatives

Architecture review should evaluate meaningful alternatives.

Do not create artificial alternatives merely to satisfy a template.

---

# Migration

Architecture changes should explain how the system moves from:

```text
Current Architecture
        ↓
Migration Strategy
        ↓
Target Architecture
```

---

# Compatibility

Consider compatibility between:

- Old and new services.
- Old and new schemas.
- Existing clients.
- Existing deployments.
- Existing events.

---

# Rollback

Determine whether architectural changes can be rolled back.

Irreversible architecture changes require stronger evidence and risk controls.

---

# Architecture Decision Records

Important architectural decisions should produce ADRs.

An ADR should capture:

- Context.
- Decision.
- Alternatives.
- Consequences.
- Status.

---

# Architecture Review Workflow

```text
Problem
   ↓
Current Architecture
   ↓
Target Architecture
   ↓
Design
   ↓
Alternatives
   ↓
Risk Analysis
   ↓
Architecture Review
   ↓
ADR
   ↓
Implementation
```

---

# Architecture Review Outcomes

Possible outcomes:

```text
Approved

Approved with Conditions

Changes Required

More Evidence Required

ADR Required

Escalation Required

Rejected
```

---

# Architecture Review Board

Organizations may use a formal architecture review board for high-impact decisions.

A board should be used where it adds meaningful architectural value.

Avoid requiring board review for ordinary implementation decisions.

---

# Architecture Review Independence

High-impact architectural decisions should receive review from engineers with appropriate architectural expertise.

The original proposer should not be the only person validating the architecture.

---

# AI-Assisted Architecture Review

AI may assist by:

- Comparing architecture documents.
- Finding dependency relationships.
- Identifying missing components.
- Detecting inconsistencies.
- Generating architecture review questions.
- Analyzing repository structure.

AI cannot independently approve architecture.

---

# Architecture Drift

Architecture documentation and implementation may diverge.

Review should identify whether:

```text
Documented Architecture
        ≠
Actual Architecture
```

Known architectural drift should be tracked and addressed.

---

# Architecture Exceptions

Architectural exceptions may be appropriate.

They should document:

- Reason.
- Scope.
- Risk.
- Owner.
- Duration.
- Exit strategy where applicable.

---

# Architecture Debt

Architecture debt includes:

- Unclear boundaries.
- Excessive coupling.
- Duplicate responsibilities.
- Unowned data.
- Unnecessary services.
- Operational complexity.
- Outdated architectural decisions.

Architecture debt should be visible.

---

# Mandatory Rules

Architecture review shall:

- Evaluate system-level impact.
- Consider boundaries and ownership.
- Consider reliability and security.
- Consider migration.
- Record significant decisions.
- Identify major trade-offs.

---

# Recommended Practices

Review architecture early.

Keep architecture diagrams current.

Use ADRs.

Prefer simple boundaries.

Use evidence for uncertain architectural assumptions.

---

# Prohibited Practices

Do not:

- Approve architecture without understanding current state.
- Introduce services without meaningful boundaries.
- Hide coupling.
- Ignore operational consequences.
- Treat diagrams as architecture by themselves.
- Allow AI to make final architectural decisions.

---

# Definition of Done

Architecture review is complete when:

- Current state is understood.
- Target state is defined.
- Architectural delta is clear.
- Responsibilities are defined.
- Dependencies are understood.
- Risks are identified.
- Alternatives are considered where meaningful.
- Migration is understood.
- Required specialist reviews are complete.
- ADR is created where required.
- Blocking findings are resolved.

---

# Review Checklist

### Architecture

- [ ] Current state
- [ ] Target state
- [ ] Architecture delta
- [ ] Boundaries
- [ ] Ownership
- [ ] Dependencies

### Quality Attributes

- [ ] Reliability
- [ ] Scalability
- [ ] Security
- [ ] Performance
- [ ] Operability
- [ ] Maintainability

### Evolution

- [ ] Migration
- [ ] Compatibility
- [ ] Rollback
- [ ] Architecture debt

### Decision

- [ ] Alternatives considered
- [ ] Trade-offs documented
- [ ] ADR created if required
- [ ] Required approvals obtained

---

# Engineering Decision

Architecture review exists to protect system-level integrity while allowing architecture to evolve.

The objective is not to prevent architectural change.

The objective is to ensure that architectural change is intentional, understandable, evidence-based, and aligned with the long-term needs of the engineering system.