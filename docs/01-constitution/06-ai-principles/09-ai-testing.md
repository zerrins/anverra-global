---
document: AI Testing
id: AEC-AI-009
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
---

# Purpose

Define the constitutional standards governing AI-assisted testing within the Anverra Engineering Operating System (AEOS).

AI-assisted testing improves engineering quality by helping engineers design, generate, validate, maintain, and evolve comprehensive test suites throughout the software lifecycle.

Testing verifies software correctness.

AI improves testing effectiveness.

---

# Intent

AI shall assist engineering teams in producing reliable software through continuous testing support.

AI-assisted testing shall improve:

- Test coverage
- Test quality
- Regression detection
- Edge-case discovery
- Test maintainability
- Engineering productivity
- Confidence in software changes

AI shall support—not replace—engineering validation.

---

# Problem Statement

Manual testing frequently results in:

- Missing edge cases
- Low coverage
- Fragile tests
- Duplicate tests
- Outdated tests
- Slow regression detection
- Incomplete negative testing
- Untested business rules

As systems grow, maintaining effective test suites becomes increasingly difficult.

---

# AI Decision

AI shall participate throughout the testing lifecycle.

Testing assistance begins during engineering planning and continues through production maintenance.

AI-generated tests shall always be reviewed by engineers.

---

# Rationale

Testing is repetitive, systematic, and pattern-oriented.

AI excels at:

- Pattern recognition
- Coverage analysis
- Scenario generation
- Test refactoring
- Missing case identification

Human engineers provide:

- Business understanding
- Acceptance criteria
- Risk prioritization
- Domain validation

Together they produce higher-quality testing.

---

# AI Testing Philosophy

Tests validate behavior—not implementation.

AI shall generate tests that verify observable business outcomes.

Tests should document expected system behavior.

Testing exists to increase engineering confidence.

---

# Testing Principles

Every AI-assisted testing activity shall be:

## Business Driven

Tests verify business rules before implementation details.

---

## Risk Focused

High-risk functionality receives deeper testing.

---

## Repeatable

Tests produce deterministic results.

---

## Maintainable

Tests evolve with software.

AI shall recommend simplification where appropriate.

---

## Independent

Tests shall not depend upon execution order.

---

## Fast

Feedback should be rapid while preserving correctness.

---

# AI Testing Lifecycle

Every AI-assisted testing workflow follows:

```
Requirement Analysis

↓

Business Rule Identification

↓

Test Strategy

↓

Scenario Generation

↓

Test Case Generation

↓

Coverage Analysis

↓

Execution

↓

Failure Analysis

↓

Regression Validation

↓

Continuous Improvement
```

Testing continues throughout software evolution.

---

# AI Testing Responsibilities

AI shall assist engineers in:

## Unit Testing

Generate tests for:

- Business rules
- Domain models
- Services
- Value objects
- Edge cases

Unit tests shall remain isolated.

---

## Integration Testing

Validate:

- Module interactions
- Database integration
- Messaging
- External APIs
- Infrastructure adapters

AI shall recommend realistic integration scenarios.

---

## Contract Testing

Validate:

- Public APIs
- Events
- Message schemas
- Consumer compatibility

AI shall detect breaking interface changes.

---

## End-to-End Testing

Assist in validating:

- Business workflows
- User journeys
- Cross-module interactions
- Production-like scenarios

---

## Performance Testing

Identify:

- Performance regressions
- Expensive algorithms
- Slow queries
- Resource bottlenecks

AI recommendations shall include supporting reasoning.

---

## Security Testing

Assist with:

- Input validation
- Injection testing
- Authorization scenarios
- Authentication validation

Security testing complements AI Security Principles.

---

## Regression Testing

AI shall:

- Identify impacted functionality
- Recommend regression suites
- Detect obsolete tests
- Highlight missing regression coverage

---

# Coverage Analysis

AI shall evaluate:

- Statement coverage
- Branch coverage
- Path coverage
- Business rule coverage
- Exception coverage
- Integration coverage

Coverage percentage alone shall not determine testing quality.

---

# Test Case Generation

AI should generate tests for:

Positive Scenarios

Negative Scenarios

Boundary Conditions

Null Inputs

Empty Inputs

Large Data Sets

Concurrent Operations

Failure Recovery

Unexpected User Behavior

Business Rule Violations

---

# Mutation Testing

Where practical, AI shall recommend mutation testing.

Mutation testing validates test effectiveness rather than code coverage.

High coverage with weak assertions is insufficient.

---

# Test Data Management

AI shall recommend:

- Minimal datasets
- Deterministic fixtures
- Synthetic data
- Privacy-safe data
- Independent datasets

Production data shall never be copied into development environments without approved anonymization.

---

# Test Maintainability

AI shall detect:

- Duplicate tests
- Obsolete tests
- Fragile assertions
- Overly coupled tests
- Missing abstractions

Test quality shall evolve with implementation quality.

---

# AI Testing Limitations

AI shall not:

- Invent business requirements.
- Assume undocumented behavior.
- Generate meaningless assertions.
- Prefer coverage over correctness.
- Replace engineering validation.

Generated tests require engineering review.

---

# Human Responsibilities

Engineers remain responsible for:

- Acceptance criteria
- Business correctness
- Test approval
- Risk prioritization
- Production readiness
- Exploratory testing

AI assists testing.

Engineers own quality.

---

# AI Guidance

AI shall:

- Generate meaningful assertions.
- Reuse existing testing patterns.
- Explain generated tests.
- Prefer readable tests.
- Detect redundant tests.
- Recommend improved coverage.
- Preserve testing conventions.

---

# Mandatory Rules

AI-assisted testing shall:

- Validate business rules.
- Include positive and negative cases.
- Cover edge conditions.
- Preserve deterministic execution.
- Update tests with implementation changes.
- Explain generated scenarios.

---

# Recommended Practices

Generate tests during implementation.

Keep tests independent.

Prefer business-language test names.

Review generated tests.

Measure business coverage.

Continuously refactor tests.

---

# Prohibited Practices

AI shall not:

- Generate tests without assertions.
- Duplicate existing tests.
- Ignore business rules.
- Generate implementation-dependent tests.
- Remove valuable regression tests.
- Artificially inflate coverage.

---

# Allowed Exceptions

Experimental prototypes may temporarily maintain reduced testing while validating technical feasibility.

Production systems shall satisfy constitutional testing standards before release.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| AI-Assisted Test Coverage | 100% |
| Business Rule Coverage | 100% |
| Regression Detection | 100% |
| Critical Test Failures Escaping Release | 0 |
| Duplicate Tests | 0 |
| Meaningful Assertions | 100% |

---

# Review Checklist

AI testing review shall verify:

- Business rules tested
- Edge cases included
- Negative scenarios covered
- Integration points validated
- Security scenarios included
- Performance considerations addressed
- Test names understandable
- Assertions meaningful
- Regression coverage updated
- Documentation synchronized

---

# Examples

## Good

```
Business Rule

↓

Generate Unit Test

↓

Generate Boundary Tests

↓

Generate Failure Tests

↓

Review

↓

Execute
```

---

## Poor

```
Generate 500 Tests

↓

No Assertions

↓

100% Coverage

↓

Unknown Quality
```

Coverage without meaningful validation provides little engineering value.

---

# Anti-patterns

Coverage Obsession

Assertion-Free Tests

Implementation-Coupled Tests

Duplicate Test Suites

Ignoring Business Rules

Snapshot Abuse

Flaky Tests

AI Without Review

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

AI-assisted testing is a mandatory engineering quality capability.

AI shall continuously assist engineers in designing, generating, validating, maintaining, and improving software tests while preserving human ownership of business correctness and production readiness.

Testing exists to build engineering confidence—not merely to increase coverage metrics.

---

# References

- Engineering Constitution
- Testing Pyramid
- Test-Driven Development
- Mutation Testing
- Domain-Driven Design
- OWASP Testing Guide

---

# Related Documents

- AI Code Review
- AI Security
- AI Documentation
- AI Collaboration
- Quality Principles
- Development Review Checklist
- Repository Checklist