---
document: AI Code Review
id: AEC-AI-007
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-AI-000
  - AEC-DEV-000
  - AEC-QLT-000
  - AEC-REV-000
---

# Purpose

Define the constitutional standards governing AI-assisted code reviews within the Anverra Engineering Operating System (AEOS).

AI code review augments engineering review by improving consistency, identifying common defects, enforcing engineering standards, and accelerating developer feedback.

AI shall improve review quality without replacing engineering judgment.

---

# Intent

AI-assisted code review exists to improve engineering quality through consistent and repeatable analysis.

Every code review should answer:

- Does the implementation satisfy the business objective?
- Does it comply with the Engineering Constitution?
- Does it preserve architectural integrity?
- Is the implementation maintainable?
- Does it introduce security or performance risks?
- Are sufficient tests provided?
- Is documentation updated?

AI shall assist reviewers by identifying risks, inconsistencies, and opportunities for improvement.

---

# Problem Statement

Manual code reviews frequently suffer from:

- Reviewer fatigue
- Inconsistent review quality
- Missed defects
- Personal bias
- Architectural drift
- Incomplete testing validation
- Security oversights
- Style-focused discussions

As repositories grow, maintaining consistent review quality becomes increasingly difficult.

---

# AI Decision

Every Pull Request shall undergo AI-assisted review before human approval.

AI review complements—not replaces—human engineering review.

Final approval remains a human responsibility.

---

# Rationale

AI excels at:

- Pattern recognition
- Standards enforcement
- Large-scale comparison
- Static reasoning
- Repetitive validation

Human engineers excel at:

- Business understanding
- Trade-off evaluation
- Architectural judgment
- Product decisions
- Contextual reasoning

Combining both produces higher engineering quality than either independently.

---

# AI Code Review Philosophy

AI reviews code.

Engineers review engineering decisions.

AI should detect objective engineering issues while humans evaluate subjective design decisions and business correctness.

Review automation should reduce repetitive work while preserving thoughtful engineering discussions.

---

# Code Review Principles

Every AI review shall be:

## Objective

Recommendations shall be based upon engineering evidence rather than stylistic preference.

---

## Explainable

Every finding shall explain:

- What was detected
- Why it matters
- Potential impact
- Recommended resolution

AI shall never produce unexplained recommendations.

---

## Context Aware

Reviews shall consider:

- Existing architecture
- Module boundaries
- Business capability
- Coding standards
- Historical repository patterns

AI shall not recommend changes that violate established repository conventions.

---

## Constitution Driven

Every review shall validate compliance with:

- Engineering Principles
- Architecture Principles
- Development Principles
- Quality Principles
- Repository Principles
- AI Principles

The Engineering Constitution is the authoritative review standard.

---

## Actionable

Every finding should provide a clear remediation path.

Recommendations shall be specific enough for implementation.

---

# AI Review Lifecycle

Every review follows the same workflow.

```
Pull Request Created

↓

Repository Discovery

↓

Constitution Review

↓

Architecture Review

↓

Code Analysis

↓

Security Analysis

↓

Performance Analysis

↓

Testing Validation

↓

Documentation Validation

↓

Review Summary

↓

Human Review

↓

Merge Decision
```

AI review precedes human approval.

---

# AI Review Scope

Every review shall evaluate:

## Repository Standards

- Folder structure
- Module organization
- Naming conventions
- Repository consistency

---

## Architecture

- Layer boundaries
- Dependency direction
- Module ownership
- Separation of concerns
- Interface contracts

---

## Development

- Clean Code
- SOLID
- Readability
- Error handling
- Defensive programming
- Refactoring opportunities

---

## Quality

- Test coverage
- Test quality
- Assertions
- Edge cases
- Failure scenarios

---

## Performance

- Time complexity
- Memory allocation
- Database access
- Network usage
- Caching
- Concurrency

---

## Security

- Injection vulnerabilities
- Authentication
- Authorization
- Secret exposure
- Input validation
- Dependency risk

---

## Documentation

- README updates
- API documentation
- ADR updates
- Code comments
- Public contracts

---

# Review Severity Levels

## Critical

Examples:

- Security vulnerabilities
- Data corruption
- Authentication failures
- Breaking architectural violations

Critical findings shall block merge.

---

## High

Examples:

- Missing validation
- Incorrect business logic
- Major performance issues
- Missing tests

Requires remediation before approval.

---

## Medium

Examples:

- Maintainability concerns
- Code duplication
- Naming inconsistencies
- Refactoring recommendations

Reviewer discretion applies.

---

## Low

Examples:

- Documentation improvements
- Readability suggestions
- Style consistency

Non-blocking recommendations.

---

# AI Review Responsibilities

AI shall:

- Detect objective engineering issues.
- Explain recommendations.
- Identify constitutional violations.
- Validate architectural consistency.
- Recommend tests.
- Highlight security risks.
- Detect performance regressions.
- Preserve repository conventions.

AI shall not:

- Approve Pull Requests.
- Override engineering decisions.
- Rewrite unrelated code.
- Recommend unnecessary refactoring.
- Ignore repository context.

---

# Human Reviewer Responsibilities

Human reviewers shall evaluate:

- Business correctness
- Product requirements
- Domain behavior
- Architectural trade-offs
- Operational implications
- Risk acceptance
- Customer impact

Final engineering responsibility remains human.

---

# AI Review Output

Every review should include:

- Executive Summary
- Constitutional Compliance
- Architecture Findings
- Security Findings
- Performance Findings
- Testing Assessment
- Documentation Assessment
- Positive Observations
- Improvement Opportunities
- Merge Recommendation

Recommendations shall be prioritized by severity.

---

# Mandatory Rules

AI reviews shall:

- Execute before merge.
- Explain every finding.
- Respect repository conventions.
- Validate constitutional compliance.
- Classify findings by severity.
- Avoid subjective stylistic preferences.

---

# Recommended Practices

Review Pull Requests early.

Prefer smaller Pull Requests.

Review one business capability at a time.

Validate architecture before implementation details.

Celebrate good engineering—not only identify problems.

---

# Prohibited Practices

AI shall not:

- Rewrite entire implementations unnecessarily.
- Recommend architectural changes without justification.
- Ignore established patterns.
- Generate contradictory recommendations.
- Approve insecure implementations.
- Block merges without explanation.

---

# Allowed Exceptions

Emergency production hotfixes may receive abbreviated AI review provided:

- Critical security validation is completed.
- Post-release review is mandatory.
- Engineering debt is documented.

---

# AI Guidance

AI shall:

- Read repository context before review.
- Apply repository-specific standards.
- Detect duplicated logic.
- Preserve architectural integrity.
- Explain trade-offs.
- Recommend improvements incrementally.
- Prefer reuse over replacement.

---

# Implementation Guidance

Engineering teams should integrate AI review into Pull Request workflows.

Recommended sequence:

1. Static Analysis
2. Unit Tests
3. Security Scan
4. AI Review
5. Human Review
6. Merge

AI review should complement existing quality gates.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| AI Review Coverage | 100% |
| Constitutional Compliance Detection | 100% |
| Critical Defects Escaping Review | 0 |
| False Positive Rate | <10% |
| Review Explanation Coverage | 100% |
| Human Review Satisfaction | ≥95% |

---

# Review Checklist

AI review shall verify:

- Repository standards followed
- Architecture preserved
- Business capability respected
- Naming conventions followed
- Tests sufficient
- Documentation updated
- Security validated
- Performance acceptable
- Dependencies appropriate
- Constitutional compliance maintained

---

# Examples

## Good Review

```
Finding

Missing validation in PolicyService.

Severity

High

Impact

Invalid policies may be persisted.

Recommendation

Validate policy state before persistence using the existing PolicyValidator.

Reference

Development Principles §06
```

---

## Poor Review

```
Looks wrong.

Maybe improve this.
```

The recommendation is vague, lacks context, and provides no actionable guidance.

---

# Anti-patterns

Style Over Substance

Architecture Blindness

Context-Free Recommendations

Large-Scale Automatic Refactoring

Business Logic Ignorance

Contradictory Recommendations

Constitution Violations

AI Approval Without Human Review

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

AI-assisted code review is a mandatory engineering quality capability.

AI shall continuously improve review consistency, engineering quality, architectural compliance, and developer productivity while preserving human ownership of engineering decisions.

AI augments engineering judgment—it does not replace it.

---

# References

- Engineering Constitution
- Clean Code
- Code Complete
- Secure Coding Guidelines
- OWASP
- Domain-Driven Design

---

# Related Documents

- AI Engineering Philosophy
- AI Decision Framework
- AI Security
- AI Testing
- AI Collaboration
- Development Review Checklist
- Repository Checklist