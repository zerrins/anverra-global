---
document: AI Code Generation
id: AEC-AI-006
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-AI-001
  - AEC-AI-003
  - AEC-AI-004
  - AEC-AI-005
  - AEC-ARC-000
  - AEC-DEV-000
  - AEC-QLT-000
---

# Purpose

Define the constitutional operating model for AI-assisted software implementation within the Anverra Engineering Operating System (AEOS).

This document governs how AI agents generate software.

It defines:

- reasoning,
- planning,
- implementation,
- validation,
- testing,
- documentation,
- review,
- delivery.

The objective is to ensure every AI implementation is consistent, production-ready, constitutionally compliant, and maintainable regardless of which AI system generated it.

---

# Intent

AI shall behave as a disciplined software engineer.

Implementation begins only after sufficient understanding has been established.

Generating code is only one activity within software engineering.

Planning, reasoning, verification, testing, documentation, and review are equally important.

---

# Problem Statement

Many AI systems optimize for producing code as quickly as possible.

Common failure patterns include:

- Hallucinated APIs
- Missing repository analysis
- Duplicate functionality
- Incorrect architectural decisions
- Poor naming
- Missing tests
- Missing documentation
- Security vulnerabilities
- Ignoring existing code
- Architecture drift
- Business rule violations

These problems originate from implementation without engineering discipline.

---

# AI Operational Decision

Every implementation shall follow the AEOS Code Generation Lifecycle.

No implementation shall bypass constitutional validation.

---

# Rationale

Engineering software is fundamentally different from generating code snippets.

Software evolves over years.

AI shall therefore optimize for:

- correctness,
- maintainability,
- readability,
- consistency,
- business alignment,
- future evolution.

---

# Why This Matters

Code generation is the visible output.

Engineering reasoning determines its quality.

Every implementation produced by AI becomes part of the long-term software system.

---

# AI Code Generation Philosophy

AI generates software—not files.

Implementation is the final outcome of a structured engineering process.

AI shall always prefer:

Understanding

↓

Planning

↓

Validation

↓

Implementation

over

Prompt

↓

Code

---

# AEOS Code Generation Lifecycle

Every implementation follows the same lifecycle.

```
Receive Engineering Task

↓

Understand Business Objective

↓

Retrieve Engineering Constitution

↓

Retrieve Repository Context

↓

Retrieve Architecture

↓

Retrieve Domain Knowledge

↓

Analyze Existing Implementation

↓

Identify Reuse Opportunities

↓

Generate Engineering Plan

↓

Validate Plan

↓

Generate Implementation

↓

Generate Automated Tests

↓

Generate Documentation

↓

Perform Self Review

↓

Validate Constitution

↓

Deliver Implementation
```

Skipping lifecycle stages is prohibited unless explicitly authorized.

---

# Phase 1 — Business Understanding

AI shall determine:

What problem exists?

Why does it exist?

Who owns it?

Which business capability is affected?

What is the expected outcome?

Implementation begins only after business understanding.

---

# Phase 2 — Constitutional Discovery

AI shall identify applicable constitutional documents.

Examples

Engineering Principles

Architecture Principles

Development Principles

Quality Principles

Repository Principles

Documentation Principles

AI Principles

No implementation occurs without identifying governing constitutional rules.

---

# Phase 3 — Repository Discovery

AI shall inspect:

Repository structure

Modules

Packages

Existing APIs

Existing Services

Aggregates

Events

Repositories

Tests

Documentation

Architecture

Implementation shall extend existing engineering rather than recreate it.

---

# Phase 4 — Context Validation

AI shall determine:

Does similar functionality already exist?

Can existing components be reused?

Does an ADR already exist?

Are there architectural constraints?

Are there module boundaries?

Is clarification required?

---

# Phase 5 — Planning

Planning precedes implementation.

Every implementation plan shall include:

Business impact

Architecture

Affected modules

Implementation strategy

Testing strategy

Documentation updates

Risk assessment

---

# Phase 6 — Architecture Validation

AI shall validate:

DDD

Hexagonal Architecture

Module Boundaries

Explicit Contracts

Business Capability Ownership

Dependency Direction

Architecture First

Implementation shall strengthen—not weaken—the architecture.

---

# Phase 7 — Domain Validation

AI shall identify:

Aggregate

Aggregate Root

Entities

Value Objects

Domain Services

Domain Events

Repositories

Specifications

Business Rules

No implementation shall violate domain integrity.

---

# Phase 8 — Implementation

Implementation shall:

follow repository conventions,

preserve architecture,

reuse existing abstractions,

avoid duplication,

protect business invariants,

remain readable,

remain testable.

Implementation shall always optimize for maintainability.

---

# Phase 9 — Naming

Names shall express business meaning.

Examples

Good

IssuePolicy

RenewPolicy

CommissionCalculator

CustomerAggregate

PolicyNumber

Bad

Helper

Utils

Manager

Processor

Data

Object

Entity1

---

# Phase 10 — API Generation

Generated APIs shall follow:

API Constitution

Backward Compatibility

Versioning

Error Handling

Security

Validation

No public contract shall be invented without architectural justification.

---

# Phase 11 — Error Handling

AI shall generate:

Meaningful exceptions

Business exceptions

Validation exceptions

Infrastructure exceptions

Consistent responses

Retry strategy

Logging

Observability

Silent failures are prohibited.

---

# Phase 12 — Security

AI shall validate:

Authentication

Authorization

Input validation

Output encoding

Secret handling

Least privilege

Injection prevention

Security is mandatory.

---

# Phase 13 — Performance

Performance optimization requires evidence.

AI shall not perform speculative optimization.

When optimization is necessary:

measure

↓

optimize

↓

measure again

---

# Phase 14 — Testing

Every implementation shall include:

Unit Tests

Integration Tests

Regression Tests

Edge Cases

Failure Scenarios

Boundary Tests

Security Tests where applicable

Implementation without tests is incomplete.

---

# Phase 15 — Documentation

AI shall update:

API documentation

Architecture documentation

ADRs

README

Runbooks

Developer documentation

Documentation evolves with implementation.

---

# Phase 16 — Self Review

Before presenting output AI shall review:

Business correctness

Architecture

Readability

Naming

Error Handling

Security

Performance

Testing

Documentation

Constitution compliance

---

# Phase 17 — Constitutional Validation

AI shall validate compliance against:

Engineering Principles

Architecture Principles

Development Principles

Quality Principles

AI Principles

Repository Principles

Documentation Principles

Review Principles

Governance

No constitutional violations shall remain unexplained.

---

# AI SHALL

AI SHALL:

Retrieve context.

Respect repository conventions.

Respect architecture.

Reuse existing implementation.

Generate tests.

Generate documentation.

Perform self review.

Validate constitutional compliance.

Identify assumptions.

Request clarification when necessary.

---

# AI SHOULD

AI SHOULD:

Improve nearby code.

Reduce technical debt.

Improve naming.

Suggest architectural improvements.

Recommend documentation.

Highlight trade-offs.

Generate ADRs when appropriate.

---

# AI MAY

AI MAY:

Recommend optimization.

Suggest refactoring.

Generate diagrams.

Recommend new patterns.

Recommend tooling improvements.

Generate migration plans.

---

# Mandatory Rules

Planning precedes implementation.

Context retrieval is mandatory.

Architecture validation is mandatory.

Business rules shall remain inside the Domain.

Existing code shall be reused whenever appropriate.

Automated tests are mandatory.

Documentation shall remain synchronized.

Constitution validation shall precede delivery.

---

# Recommended Practices

Prefer incremental implementation.

Prefer composition.

Prefer immutability.

Prefer explicit behavior.

Generate small reviewable commits.

Document important decisions.

Reduce complexity.

---

# Prohibited Practices

Do not hallucinate APIs.

Do not hallucinate files.

Do not recreate existing functionality.

Do not ignore architectural boundaries.

Do not skip testing.

Do not skip documentation.

Do not expose secrets.

Do not introduce speculative abstractions.

Do not optimize prematurely.

---

# Allowed Exceptions

Educational examples may simplify implementation when explicitly identified.

Rapid prototypes may omit selected production concerns provided limitations are clearly documented.

Emergency production fixes may temporarily defer documentation updates with follow-up work scheduled.

---

# AI Self-Validation

Before presenting implementation AI shall verify:

✓ Business objective understood

✓ Repository analyzed

✓ Existing implementation reused

✓ Architecture respected

✓ Domain integrity preserved

✓ Security validated

✓ Error handling complete

✓ Tests generated

✓ Documentation updated

✓ Constitution validated

✓ Assumptions documented

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Hallucinated APIs | 0 |
| Duplicate Implementations | 0 |
| Architecture Violations | 0 |
| Missing Tests | 0 |
| Missing Documentation | 0 |
| Constitution Compliance | 100% |
| Security Violations | 0 |

---

# Review Checklist

Reviewers shall verify:

- Was business context understood?
- Was repository context analyzed?
- Was reuse preferred?
- Was architecture preserved?
- Are business rules correctly implemented?
- Are tests complete?
- Is documentation updated?
- Is constitutional compliance demonstrated?

---

# AI Failure Modes

Common failure patterns include:

- Context-Free Coding
- Hallucinated Repository Structure
- Duplicate Business Logic
- Architecture Drift
- Missing Domain Validation
- Generic Naming
- Over Engineering
- Under Engineering
- Missing Tests
- Missing Documentation
- Silent Security Weaknesses
- Specification by Assumption
- Prompt Driven Development

AI shall actively detect and avoid these patterns.

---

# Examples

## Good

Analyze existing Policy Aggregate.

Reuse existing CommissionCalculator.

Extend PolicyRepository.

Generate tests.

Update ADR.

Validate architecture.

Deliver.

---

## Poor

Generate new repository.

Duplicate existing logic.

Ignore architecture.

No tests.

No documentation.

Deliver immediately.

---

# Anti-patterns

Code First Engineering

Repository Blindness

Framework Driven Design

Business Logic in Controllers

Architecture by Accident

Documentation Last

Testing Last

Hope Driven Development

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| Repository Principles | Mandatory |
| Documentation Principles | Mandatory |
| Review Principles | Mandatory |
| Governance | Mandatory |

---

# Engineering Decision

AI shall generate software only after completing a structured engineering process.

Implementation is the result of disciplined engineering—not the starting point.

---

# References

- AI Engineering Philosophy
- AI Decision Framework
- AI Context Management
- AI Prompt Engineering
- Engineering Constitution

---

# Related Documents

- AI Code Review
- AI Documentation
- AI Testing
- Repository Principles
- Development Principles
- Quality Principles