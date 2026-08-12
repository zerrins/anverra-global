---
document: Review Philosophy
id: AEC-REV-001
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-000
  - AEC-ENG-000
  - AEC-QLT-000
---

# Purpose

Define the fundamental philosophy, principles, objectives, and behavioral expectations governing engineering review within the Anverra Engineering Operating System (AEOS).

This document establishes why engineering review exists and how engineers should think about review.

It does not define the detailed mechanics of individual review types.

---

# Intent

Engineering review exists to improve the quality of engineering decisions before those decisions become expensive, difficult, or dangerous to change.

Review should help engineers answer:

- Is this the right solution?
- Is the implementation correct?
- Is the design understandable?
- Is the change safe?
- Does it fit the architecture?
- What assumptions are being made?
- What could fail?
- What will this change make harder in the future?
- What evidence supports the decision?

---

# Problem Statement

Software changes are often created by individuals or small groups.

Individual reasoning is necessarily incomplete.

An engineer may miss:

- A requirement.
- An edge case.
- A security implication.
- An operational consequence.
- An architectural conflict.
- A compatibility problem.
- A maintenance burden.
- A failure mode.

Engineering review provides additional perspectives before the change becomes part of the system.

---

# Constitutional Decision

Engineering work shall be reviewed according to its risk, complexity, impact, and reversibility.

Review shall be used to improve engineering outcomes rather than merely provide procedural approval.

---

# What Review Is

Review is a structured engineering activity in which one or more people or automated systems examine an engineering artifact or change against relevant:

- Requirements
- Standards
- Architecture
- Security principles
- Quality principles
- Operational constraints
- Test expectations
- Historical decisions

The purpose is to identify meaningful risks and improve the result.

---

# What Review Is Not

Review is not:

- A popularity contest.
- A demonstration of reviewer authority.
- A mechanism for enforcing personal coding preferences.
- A substitute for testing.
- A substitute for architecture.
- A substitute for requirements.
- A guarantee that defects do not exist.
- A transfer of responsibility from author to reviewer.

---

# Author Responsibility

The author remains responsible for the engineering work.

Approval does not mean:

> "The reviewer now owns this code."

Approval means:

> "The applicable review process found no unresolved issue that prevents acceptance."

The author remains accountable for:

- Correctness.
- Completeness.
- Testing.
- Documentation.
- Risk identification.
- Honest communication.

---

# Reviewer Responsibility

Reviewers are responsible for providing meaningful engineering judgment.

A reviewer should:

- Understand the change.
- Understand its purpose.
- Consider its context.
- Identify meaningful risks.
- Ask useful questions.
- Provide actionable feedback.
- Distinguish defects from preferences.

Reviewers should not approve work they know violates an applicable critical requirement.

---

# Evidence Over Preference

Review comments should be based on evidence whenever possible.

Useful evidence includes:

- Requirements.
- Architecture decisions.
- Standards.
- Tests.
- Production behavior.
- Security requirements.
- Performance measurements.
- API contracts.

Avoid:

```text
"I would have implemented this differently."
```

unless the difference creates a meaningful engineering consequence.

Prefer:

```text
"This implementation bypasses the established retry policy defined by X.
That can cause duplicate processing when the dependency times out."
```

---

# Risk-Based Review

Review effort should be proportional to risk.

A useful conceptual model is:

```text
Review Effort ∝ Risk × Impact × Uncertainty
```

Risk may increase with:

- Larger blast radius.
- Greater security impact.
- Greater architectural significance.
- More consumers.
- Higher operational criticality.
- Lower reversibility.
- Greater uncertainty.

---

# Reversibility

Reversible changes generally require less review than difficult-to-reverse changes.

Examples:

### Highly Reversible

- Documentation correction.
- Internal refactoring with strong tests.
- Non-production configuration change.

### Difficult to Reverse

- Database migration.
- Public API contract.
- Authentication architecture.
- Data model change.
- Production infrastructure change.

Irreversible or expensive decisions deserve earlier and deeper review.

---

# Review Early

Important decisions should be reviewed before implementation becomes expensive to change.

Prefer:

```text
Problem
   ↓
Design
   ↓
Review
   ↓
Implementation
```

over:

```text
Problem
   ↓
Large Implementation
   ↓
Review
   ↓
Discover Design Problem
```

Early review is particularly valuable for:

- Architecture.
- Data models.
- APIs.
- Security.
- Infrastructure.
- Cross-service behavior.

---

# Review the Change

Reviewers should first understand:

- What changed?
- Why did it change?
- What behavior is different?
- Which requirements are affected?
- Which risks were introduced?

Review should not begin with arbitrary line-by-line criticism without understanding intent.

---

# Review the System

A change must be evaluated in context.

A small code change may have a large system impact.

Example:

```text
One Configuration Change
        ↓
Connection Pool Size
        ↓
Database Load
        ↓
Application Throughput
        ↓
Production Stability
```

The number of changed lines is not a measure of impact.

---

# Review for Failure

Review should actively consider how the change behaves when things go wrong.

Questions include:

- What if the dependency is unavailable?
- What if input is invalid?
- What if the operation is repeated?
- What if a timeout occurs?
- What if data is partially written?
- What if the service restarts?
- What if the system receives unexpected load?
- What if a downstream system changes?

---

# Review for Security

Security should be considered when relevant.

Questions include:

- Can an unauthorized user access this?
- Are privileges correctly scoped?
- Can user input cross a trust boundary?
- Are secrets exposed?
- Is sensitive data logged?
- Does this introduce a new attack surface?

Security review depth should be proportional to risk.

---

# Review for Maintainability

Review should consider the future cost of the implementation.

Questions include:

- Is the design understandable?
- Is complexity justified?
- Is duplication creating future risk?
- Is the abstraction appropriate?
- Can another engineer safely modify this?
- Does this create hidden coupling?

---

# Review for Consistency

A change should normally follow established conventions.

Relevant conventions may include:

- Architecture.
- APIs.
- Error handling.
- Logging.
- Testing.
- Configuration.
- Security.
- Documentation.

Deviation may be appropriate when justified.

---

# Review for Simplicity

Prefer the simplest solution that satisfies the requirements and constraints.

Reviewers should challenge unnecessary:

- Abstractions.
- Frameworks.
- Dependencies.
- Layers.
- Configuration.
- Distributed complexity.

Complexity should have a reason.

---

# Review and Engineering Trade-offs

Good engineering frequently involves trade-offs.

Examples:

```text
Performance vs Simplicity
Consistency vs Availability
Cost vs Reliability
Flexibility vs Complexity
Delivery Speed vs Long-Term Maintainability
```

Review should expose important trade-offs rather than pretending they do not exist.

---

# Review and Uncertainty

Uncertainty should be made visible.

If the team does not know whether a solution will meet a performance requirement, the review should not pretend certainty.

Instead:

```text
Assumption
   ↓
Experiment
   ↓
Evidence
   ↓
Decision
```

---

# Review Comments

A useful review comment generally contains:

```text
Observation
+
Impact
+
Recommendation
```

Example:

```text
Observation:
The retry occurs after the transaction is committed.

Impact:
A timeout can cause duplicate processing.

Recommendation:
Move the retry boundary outside the transaction or make the operation idempotent.
```

---

# Comment Classification

Reviewers should distinguish:

## Blocking

The issue must be resolved before acceptance.

Examples:

- Security vulnerability.
- Incorrect business behavior.
- Data corruption risk.
- Broken required contract.

---

## Non-Blocking

The issue should be addressed but does not prevent acceptance.

Examples:

- Maintainability improvement.
- Minor documentation gap.
- Small refactoring opportunity.

---

## Question

Used when clarification is required.

---

## Suggestion

Used for an optional improvement.

---

## Nit

A very minor issue that should not consume significant review effort.

---

# Review Tone

Review should be:

- Professional.
- Specific.
- Respectful.
- Evidence-based.
- Constructive.

Critique the engineering artifact, not the engineer.

Prefer:

```text
"This implementation can produce duplicate events."
```

over:

```text
"You implemented this incorrectly."
```

---

# Psychological Safety

Engineers should be able to raise concerns without fear of personal retaliation.

Review culture should encourage:

- Questions.
- Disagreement.
- Early escalation.
- Admission of uncertainty.
- Reporting mistakes.

A review system that discourages honest feedback creates hidden engineering risk.

---

# Disagreement

Technical disagreement is expected.

When disagreement occurs:

1. Clarify the underlying concern.
2. Identify relevant requirements.
3. Gather evidence.
4. Consult applicable standards.
5. Evaluate trade-offs.
6. Escalate when necessary.

Do not resolve technical disagreement solely through hierarchy when evidence can resolve it.

---

# Review Authority

Reviewers should have authority appropriate to the review domain.

Examples:

- Security changes → security expertise.
- Architecture changes → architecture expertise.
- Production changes → operational expertise.
- Public API changes → API/consumer expertise.

Review authority should be based on relevant knowledge, not merely organizational seniority.

---

# Review Independence

High-risk changes should receive review from someone sufficiently independent from the original decision where practical.

Independence reduces confirmation bias.

---

# Review and Trust

Review should increase confidence.

It should not create the illusion of certainty.

A successful review means:

> The identified risks have been sufficiently understood and addressed for the change to proceed.

It does not mean:

> The change cannot fail.

---

# Review and Testing

Review and testing solve different problems.

Testing primarily provides executable evidence about behavior.

Review provides human or machine reasoning about:

- Intent.
- Design.
- Risk.
- Maintainability.
- Architecture.
- Uncovered scenarios.

Both are required.

---

# Review and Documentation

Important changes should update relevant documentation.

Review should identify documentation impact.

Examples:

```text
API Change
   ↓
API Documentation

Architecture Change
   ↓
Architecture Documentation + ADR

Operational Change
   ↓
Runbook

Security Change
   ↓
Security Documentation
```

---

# Review and Historical Knowledge

Reviewers should consult existing decisions before proposing alternatives.

Examples:

```text
Proposed Architecture
       ↓
Search ADRs
       ↓
Existing Decision?
       ↓
Understand Why
```

This prevents repeatedly reopening previously resolved decisions.

---

# Review and AI

AI can improve review coverage but must not create false confidence.

AI may:

- Find suspicious code.
- Identify missing tests.
- Detect inconsistencies.
- Compare implementation against standards.
- Summarize changes.
- Identify possible security issues.

AI findings require appropriate validation.

---

# Human Judgment

Human judgment remains essential for:

- Business semantics.
- Architectural intent.
- Risk acceptance.
- Security decisions.
- Trade-offs.
- Organizational constraints.

AI may assist but does not independently establish organizational truth.

---

# Review Efficiency

Good review should maximize signal and minimize noise.

Reviewers should prioritize:

1. Correctness.
2. Security.
3. Data integrity.
4. Reliability.
5. Architecture.
6. Maintainability.
7. Performance.
8. Style.

Style should not dominate substantive engineering concerns.

---

# Review Limits

Reviewers should not attempt to prove every possible property of the system through manual inspection.

Use:

- Tests.
- Static analysis.
- Linters.
- Security scanners.
- Contract validation.
- Architecture checks.

for deterministic properties.

Human review should focus on judgment.

---

# Review as Learning

Review should improve both the artifact and the engineering organization.

Repeated review findings may indicate:

- Missing standards.
- Missing automation.
- Poor tooling.
- Training gaps.
- Architectural problems.

Repeated findings should trigger systemic improvement.

---

# Review Feedback Loop

```text
Review Finding
      ↓
Correction
      ↓
Pattern Identified
      ↓
Standard / Automation / Training
      ↓
Future Defect Prevented
```

---

# Review Anti-Patterns

## Rubber Stamping

Approving without meaningful review.

---

## Preference Policing

Blocking changes because they differ from personal preference.

---

## Line-by-Line Tunnel Vision

Ignoring system-level impact.

---

## Late Discovery

Finding major design problems after implementation.

---

## Review Theater

Performing review primarily to satisfy process requirements.

---

## Excessive Nitpicking

Spending disproportionate effort on low-value details.

---

## Reviewer Heroics

Relying on one expert to catch everything.

---

## AI Blind Trust

Assuming AI review guarantees correctness.

---

# Mandatory Rules

Engineering review shall:

- Be risk-based.
- Focus on engineering outcomes.
- Use evidence.
- Identify meaningful risks.
- Remain respectful.
- Preserve author accountability.
- Use appropriate automation.
- Escalate high-risk concerns.

---

# Recommended Practices

Review designs early.

Keep review comments actionable.

Use checklists for high-risk changes.

Automate deterministic checks.

Record significant decisions.

Learn from recurring review findings.

---

# Prohibited Practices

Do not:

- Use review to enforce personal preferences.
- Approve known critical defects.
- Attack contributors personally.
- Treat review approval as a guarantee.
- Ignore system-level consequences.
- Depend entirely on AI review.

---

# Success Metrics

Review quality should be measured through outcomes rather than review volume.

Useful indicators include:

| Metric | Desired Direction |
|---|---|
| Defects Found Before Production | Increase |
| Critical Escaped Defects | Decrease |
| Review Rework Caused by Late Design Discovery | Decrease |
| Repeated Review Findings | Decrease |
| Review Cycle Time | Healthy / Stable |
| Review Findings Resolved | Increase |
| Reviewer Participation | Healthy |

Metrics shall not be used to pressure engineers into minimizing legitimate findings.

---

# Review Checklist

A reviewer should ask:

- What problem does this solve?
- Is the proposed solution appropriate?
- What changed?
- What could fail?
- What are the security implications?
- What are the operational implications?
- Does it fit the architecture?
- Is it sufficiently tested?
- Is documentation affected?
- Are important trade-offs understood?
- Are there unresolved risks?

---

# Examples

## Good Review

```text
Requirement
    ↓
Design
    ↓
Risk Identification
    ↓
Implementation
    ↓
Testing
    ↓
Review
    ↓
Corrections
    ↓
Approval
```

---

## Poor Review

```text
Developer Opens PR
        ↓
Reviewer Skims
        ↓
"Looks Good"
        ↓
Approve
```

---

# Engineering Decision

Review is a mechanism for improving engineering decisions before they become expensive or dangerous to change.

The best review culture is not the one with the most comments.

It is the one that consistently identifies meaningful risks, improves engineering decisions, enables learning, and prevents avoidable defects without creating unnecessary friction.