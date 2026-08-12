---
document: Design Review
id: AEC-REV-005
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-ARC-000
---

# Purpose

Define the principles, workflow, responsibilities, and completion criteria for engineering design reviews.

Design review evaluates proposed solutions before implementation or before major implementation commitments make changes expensive.

---

# Intent

Design review should answer:

- Is the problem correctly understood?
- Is the proposed solution appropriate?
- Are important alternatives considered?
- Are assumptions explicit?
- Are risks understood?
- Does the design fit the architecture?
- Is the design sufficiently simple?
- Can the design be implemented and operated safely?
- What evidence is still required?

---

# Constitutional Decision

Significant engineering designs shall be reviewed before implementation when early review can materially reduce risk or rework.

---

# Why Design Review Exists

Design errors become increasingly expensive to correct as implementation progresses.

Typical progression:

```text
Requirement Error
      ↓
Design Error
      ↓
Implementation
      ↓
Integration
      ↓
Production
```

The cost and impact generally increase as the error moves downstream.

---

# When Design Review Is Required

Design review should be considered for:

- New features with meaningful complexity.
- Cross-service changes.
- New integrations.
- Significant data model changes.
- New asynchronous workflows.
- Significant infrastructure changes.
- Security-sensitive designs.
- Performance-sensitive designs.
- Changes with substantial operational impact.

---

# Design Review Inputs

A design proposal should provide sufficient information to evaluate:

- Problem.
- Goals.
- Non-goals.
- Requirements.
- Constraints.
- Proposed solution.
- Alternatives.
- Risks.
- Operational considerations.
- Security considerations.
- Testing strategy.

---

# Problem Definition

The design should clearly explain:

```text
What problem exists?

Who experiences it?

Why does it matter?

What outcome is required?
```

A solution should not be evaluated before the problem is understood.

---

# Goals

Goals should describe desired outcomes.

Examples:

- Reduce processing latency.
- Support additional consumers.
- Improve reliability.
- Enable a new business capability.

---

# Non-Goals

Non-goals define what the design intentionally does not address.

They prevent scope expansion.

---

# Requirements

Design should distinguish:

- Functional requirements.
- Non-functional requirements.
- Constraints.
- Assumptions.

---

# Constraints

Constraints may include:

- Existing architecture.
- Technology.
- Budget.
- Compatibility.
- Regulatory requirements.
- Operational limitations.
- Migration requirements.

---

# Proposed Solution

The design should explain:

- Components.
- Responsibilities.
- Interfaces.
- Data flow.
- Control flow.
- Dependencies.
- Failure behavior.

Diagrams should be used where they improve understanding.

---

# Alternatives

Significant design decisions should consider meaningful alternatives.

For each alternative, consider:

- Advantages.
- Disadvantages.
- Complexity.
- Cost.
- Risk.
- Operational impact.

Not every trivial alternative needs documentation.

---

# Trade-offs

Design review should explicitly identify important trade-offs.

Examples:

```text
Simplicity vs Flexibility

Latency vs Consistency

Cost vs Availability

Centralization vs Autonomy
```

---

# Failure Analysis

Design review should ask:

- What can fail?
- How does failure propagate?
- What happens during partial failure?
- What happens during retries?
- Can operations recover?
- Is data lost?
- Can the operation be repeated safely?

---

# Security Analysis

Where relevant, evaluate:

- Trust boundaries.
- Authentication.
- Authorization.
- Data sensitivity.
- Attack surface.
- Secret handling.
- Abuse scenarios.

---

# Data Design

Where data is involved, evaluate:

- Ownership.
- Schema.
- Lifecycle.
- Integrity.
- Consistency.
- Migration.
- Retention.
- Recovery.

---

# API Design

Where APIs are involved, evaluate:

- Contract.
- Semantics.
- Versioning.
- Error behavior.
- Idempotency.
- Compatibility.
- Consumer impact.

Detailed API review is defined separately in `07-api-review.md`.

---

# Operational Design

Evaluate:

- Deployment.
- Configuration.
- Monitoring.
- Alerting.
- Capacity.
- Recovery.
- Rollback.
- Supportability.

---

# Performance Design

Performance requirements should be explicit where relevant.

Consider:

- Expected load.
- Latency.
- Throughput.
- Resource usage.
- Scaling.
- Bottlenecks.

Performance assumptions should be validated with evidence when practical.

---

# Maintainability

A design should remain understandable.

Consider:

- Number of components.
- Coupling.
- Operational complexity.
- Dependency count.
- Abstraction.
- Ownership.

---

# Simplicity

Prefer designs that satisfy requirements with the least unnecessary complexity.

Do not optimize for architectural sophistication.

---

# Design Review Questions

Reviewers should ask:

### Problem

- Are we solving the correct problem?

### Scope

- Is the scope appropriate?

### Architecture

- Does this fit existing boundaries?

### Reliability

- What happens when dependencies fail?

### Security

- What new trust boundaries exist?

### Operations

- How will this be deployed and recovered?

### Testing

- How will correctness be demonstrated?

### Migration

- How do we move from the current state to the target state?

---

# Migration

For changes to existing systems, design should explain:

```text
Current State
      ↓
Transition
      ↓
Target State
```

Migration should consider:

- Compatibility.
- Rollback.
- Data migration.
- Consumer migration.
- Deployment sequencing.

---

# Rollback

Design review should determine whether rollback is:

- Straightforward.
- Possible with limitations.
- Difficult.
- Impossible.

If rollback is impossible, compensating controls should be considered.

---

# Design Review Workflow

```text
Problem
   ↓
Requirements
   ↓
Design Draft
   ↓
Self Review
   ↓
Peer Review
   ↓
Specialist Review
   ↓
Findings
   ↓
Design Revision
   ↓
Approval
   ↓
Implementation
```

---

# Design Review Outcomes

Possible outcomes:

```text
Approved

Approved with Conditions

Changes Required

More Evidence Required

Escalate

Rejected
```

---

# Evidence

Evidence may include:

- Prototype.
- Benchmark.
- Proof of concept.
- Threat model.
- Load test.
- Failure experiment.
- Architecture analysis.

Use evidence when assumptions materially affect the decision.

---

# Design Review and ADRs

Significant design decisions should result in an ADR where appropriate.

The design document describes the proposal.

The ADR records the durable decision.

These are related but distinct artifacts.

---

# AI-Assisted Design Review

AI may assist with:

- Identifying missing considerations.
- Comparing alternatives.
- Finding inconsistencies.
- Generating review questions.
- Analyzing dependencies.

AI recommendations require engineering validation.

---

# Design Review Anti-Patterns

## Solution Before Problem

Starting implementation before establishing the problem.

## Architecture Theater

Creating elaborate architecture without corresponding business or technical need.

## Alternative Explosion

Documenting every imaginable alternative.

## Premature Optimization

Optimizing without evidence.

## Hidden Assumptions

Leaving important assumptions unstated.

## Review After Commitment

Waiting until implementation is too expensive to change.

---

# Mandatory Rules

Significant designs shall:

- Clearly state the problem.
- Define scope.
- Identify important constraints.
- Explain the proposed solution.
- Identify significant risks.
- Consider operational impact.
- Receive appropriate review before major implementation commitment.

---

# Recommended Practices

Review early.

Use diagrams.

Document important alternatives.

Prototype uncertain areas.

Create ADRs for durable decisions.

---

# Prohibited Practices

Do not:

- Approve designs without understanding the problem.
- Hide important trade-offs.
- Ignore migration complexity.
- Treat diagrams as proof of correctness.
- Use AI-generated designs without validation.

---

# Definition of Done

A design review is complete when:

- Problem is understood.
- Requirements are clear.
- Scope is defined.
- Proposed design is documented.
- Important alternatives are considered.
- Major risks are identified.
- Required specialist reviews are complete.
- Required evidence exists.
- Blocking findings are resolved.
- Decision is recorded.

---

# Review Checklist

### Problem

- [ ] Problem clear
- [ ] Goals defined
- [ ] Non-goals defined

### Design

- [ ] Architecture explained
- [ ] Responsibilities clear
- [ ] Data flow clear
- [ ] Dependencies identified

### Risk

- [ ] Failure modes
- [ ] Security
- [ ] Performance
- [ ] Operational impact
- [ ] Migration

### Evidence

- [ ] Assumptions identified
- [ ] Required experiments completed

### Completion

- [ ] Findings resolved
- [ ] Decision recorded
- [ ] ADR created if required

---

# Engineering Decision

Design review exists to identify architectural, operational, security, and implementation risks while change is still relatively inexpensive.

The objective is to create enough shared understanding and evidence to proceed confidently—not to design every implementation detail before coding begins.