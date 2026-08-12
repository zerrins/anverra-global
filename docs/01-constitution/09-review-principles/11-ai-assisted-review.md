---
document: AI-Assisted Review
id: AEC-REV-011
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering AI Governance
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-REV-004
  - AEC-REV-005
  - AEC-AI-000
  - AEC-AI-003
  - AEC-AI-006
---

# Purpose

Define the principles, responsibilities, controls, and operating model for using artificial intelligence to assist engineering review.

AI-assisted review shall increase review coverage and engineering effectiveness without creating false confidence or transferring engineering accountability to AI systems.

---

# Intent

AI may assist reviewers with:

- Defect discovery.
- Code analysis.
- Test analysis.
- Security analysis.
- Documentation analysis.
- Architecture analysis.
- Change summarization.
- Risk identification.
- Review checklist generation.

AI shall remain an assistant to engineering review rather than an autonomous authority.

---

# Constitutional Decision

AI-generated review findings shall be treated as review signals that require appropriate engineering judgment.

AI shall not independently approve critical engineering changes.

---

# Why AI-Assisted Review Exists

Modern repositories can contain:

- Large codebases.
- Many dependencies.
- Complex architectures.
- Extensive documentation.
- Large Pull Requests.

Human reviewers have limited attention.

AI can help increase review coverage by identifying areas that deserve human attention.

---

# AI Review Philosophy

The preferred model is:

```text
AI Detection
      ↓
Human Evaluation
      ↓
Evidence
      ↓
Finding
      ↓
Correction
      ↓
Approval
```

Not:

```text
AI Detection
      ↓
Automatic Rejection
```

and not:

```text
AI Approval
      ↓
Automatic Merge
```

for changes requiring human judgment.

---

# AI Review Capabilities

AI may assist with:

## Code Analysis

- Suspicious logic.
- Null handling.
- Error handling.
- Duplicate logic.
- Complex control flow.
- Potential race conditions.

---

## Security Analysis

- Potential injection.
- Authorization gaps.
- Secret exposure.
- Unsafe data handling.
- Suspicious dependencies.

---

## Test Analysis

- Missing edge cases.
- Missing failure tests.
- Weak assertions.
- Test duplication.

---

## Architecture Analysis

- Dependency changes.
- Boundary violations.
- Unexpected coupling.
- Architectural inconsistencies.

---

## Documentation Analysis

- Stale references.
- Missing documentation.
- Contradictions.
- Broken links.
- Documentation drift.

---

# AI Review Context

AI review quality depends heavily on context.

Useful context includes:

- Requirements.
- Architecture.
- Coding standards.
- Security standards.
- Existing tests.
- Relevant ADRs.
- Documentation.
- Change history.

AI should not evaluate a change in isolation when important context is available.

---

# Context Priority

A useful context hierarchy is:

```text
Constitutional Rules
       ↓
Engineering Standards
       ↓
Architecture
       ↓
Requirements
       ↓
Current Implementation
       ↓
Tests
       ↓
Documentation
       ↓
Historical Context
```

The exact hierarchy may vary by review type.

---

# AI Review Prompting

AI review instructions should:

- State the review objective.
- Identify relevant standards.
- Identify the change scope.
- Require evidence.
- Distinguish findings from questions.
- Avoid speculative certainty.

---

# AI Finding Classification

AI findings should be classified as:

```text
Confirmed by Evidence

Likely Issue

Potential Risk

Question

Suggestion

False Positive
```

AI should not claim certainty when evidence is incomplete.

---

# False Positives

AI review systems will produce false positives.

False positives should be:

- Evaluated.
- Dismissed when appropriate.
- Used to improve prompts or rules.

Do not allow false-positive volume to overwhelm human reviewers.

---

# False Negatives

AI may miss real defects.

Therefore:

> Absence of an AI finding does not imply absence of risk.

Human review remains necessary.

---

# AI Review Confidence

AI confidence should not be treated as equivalent to engineering certainty.

For example:

```text
AI Confidence: 95%

≠

Engineering Proof: 95%
```

Confidence scores are model outputs, not guarantees.

---

# Evidence Requirement

AI should provide reasoning or evidence for substantive findings where the tooling permits.

A useful finding should identify:

- Location.
- Behavior.
- Impact.
- Relevant rule.
- Suggested validation.

---

# Example

Weak:

```text
This code may have a security issue.
```

Better:

```text
The authorization check is performed before resolving the requested
resource owner. Verify whether a user can provide an identifier
belonging to another tenant.
```

The second finding provides a concrete validation path.

---

# AI and Blocking Findings

AI should normally recommend rather than directly block.

A human or deterministic control should establish whether a finding is actually blocking.

Exceptions may exist for highly deterministic automated checks.

---

# AI and Security Review

AI can improve security review coverage but cannot replace:

- Security expertise.
- Threat modeling.
- Penetration testing.
- Risk acceptance.

---

# AI and Architecture Review

AI may identify:

- Dependency changes.
- New components.
- Unexpected coupling.
- Repository patterns.

Architecture decisions remain human decisions.

---

# AI and Code Review

AI may identify implementation risks.

The author remains responsible for understanding and validating generated findings.

---

# AI and Test Review

AI may propose missing tests.

Reviewers should verify:

- Test relevance.
- Test correctness.
- Test assertions.
- Actual risk coverage.

---

# AI and Documentation Review

AI may compare:

```text
Code
vs
Documentation
```

and identify potential drift.

Potential drift must be validated before documentation is changed.

---

# AI Review of AI-Generated Code

AI-generated code should not be reviewed solely by the same AI system that generated it.

Where practical, review should include:

- Human review.
- Independent validation.
- Automated tests.
- Static analysis.

Independent review reduces confirmation bias.

---

# Independence

Where feasible, the review model should separate:

```text
Generation
    ↓
Review
```

An AI system should not be considered an independent reviewer of its own generated output.

---

# AI Review Auditability

Important AI-assisted review should preserve enough context to understand:

- What was reviewed.
- What context was supplied.
- What findings were produced.
- What findings were accepted or rejected.

The exact retention requirements depend on governance and risk.

---

# Sensitive Information

AI review systems must follow applicable data-handling requirements.

Do not expose unnecessary:

- Secrets.
- Credentials.
- Personal data.
- Confidential information.

AI context should be minimized to what is required for the review.

---

# External AI Systems

When external AI systems are used, review:

- Data handling.
- Retention.
- Access.
- Confidentiality.
- Organizational approval.

---

# Local AI Systems

Local AI systems may reduce some data-exposure risks but do not automatically eliminate:

- Hallucinations.
- Incorrect reasoning.
- Security issues.
- Model limitations.

Local execution does not eliminate review requirements.

---

# AI Review Automation

AI review may be integrated into Pull Requests.

Example:

```text
Pull Request
      ↓
Deterministic Checks
      ↓
AI Review
      ↓
Human Review
      ↓
Specialist Review
      ↓
Approval
```

AI should surface findings rather than create unnecessary gates.

---

# AI Review Noise

AI systems should be tuned to prioritize:

1. Correctness.
2. Security.
3. Reliability.
4. Data integrity.
5. Architecture.
6. Maintainability.

Low-value stylistic findings should be minimized.

---

# AI Review Feedback Loop

Repeated findings should be analyzed.

```text
AI Finding
   ↓
Human Validation
   ↓
Repeated Pattern
   ↓
Engineering Standard
   ↓
Automated Rule
   ↓
Future Prevention
```

---

# AI Review Metrics

Useful metrics include:

- Valid finding rate.
- False-positive rate.
- Defects found before production.
- Repeated findings.
- Review time saved.
- Findings missed by AI but found by humans.

Do not optimize solely for number of findings.

---

# AI Review Failure Modes

Common failure modes include:

## Hallucination

AI invents a defect or requirement.

## Context Loss

AI lacks important repository or architecture context.

## False Confidence

AI presents speculation as fact.

## Automation Bias

Humans trust AI output without validation.

## Review Duplication

AI generates large numbers of findings that humans must manually dismiss.

## Self-Review Bias

The same model generates and validates the same solution.

---

# Human Responsibilities

Humans remain responsible for:

- Final judgment.
- Risk acceptance.
- Security decisions.
- Architecture decisions.
- Business correctness.
- Approval.

---

# Mandatory Rules

AI-assisted review shall:

- Be treated as assistance.
- Preserve human accountability.
- Distinguish evidence from inference.
- Protect sensitive information.
- Avoid autonomous approval of critical changes.
- Validate significant findings.
- Maintain appropriate review independence.

---

# Recommended Practices

Use AI for high-volume analysis.

Give AI relevant engineering context.

Require evidence for important findings.

Combine AI with deterministic checks.

Measure false positives and false negatives.

---

# Prohibited Practices

Do not:

- Treat AI approval as human approval.
- Automatically merge critical changes based only on AI review.
- Assume AI found every defect.
- Feed unnecessary secrets into AI systems.
- Allow AI to invent requirements.
- Use the same AI output as both generation and independent proof.

---

# Allowed Exceptions

Low-risk changes may use highly automated AI-assisted workflows when deterministic validation and organizational policy permit.

Critical engineering decisions require appropriate human review.

---

# Definition of Done

AI-assisted review is complete when:

- Appropriate AI review was performed where useful.
- Significant findings were validated.
- False positives were dismissed or addressed.
- Required human review is complete.
- Security and data-handling requirements are satisfied.
- AI did not become the sole approval authority.

---

# Review Checklist

### Context

- [ ] Relevant requirements provided
- [ ] Architecture context provided
- [ ] Standards available
- [ ] Scope clear

### AI Findings

- [ ] Findings classified
- [ ] Important findings validated
- [ ] False positives handled
- [ ] Evidence reviewed

### Security

- [ ] Sensitive data protected
- [ ] AI system approved where required
- [ ] Data handling understood

### Independence

- [ ] Human review performed
- [ ] AI did not independently approve critical changes
- [ ] Generated code independently validated

### Completion

- [ ] Required findings resolved
- [ ] Required approvals obtained

---

# Engineering Decision

AI-assisted review shall increase engineering review capability without weakening engineering accountability.

AI should help humans discover risks faster and more comprehensively, while humans remain responsible for deciding what those risks mean and whether the resulting engineering change is acceptable.