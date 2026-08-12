---
document: AI Prompt Engineering
id: AEC-AI-005
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
---

# Purpose

Define the constitutional standards governing prompt engineering within the Anverra Engineering Operating System (AEOS).

Prompt engineering is the process of communicating engineering intent to Artificial Intelligence systems in a structured, deterministic, and reproducible manner.

The quality of AI-generated engineering artifacts depends directly upon the quality of the prompt supplied to the AI agent.

Prompt engineering is therefore considered an engineering discipline rather than an interaction technique.

---

# Intent

Every prompt shall maximize engineering understanding before requesting implementation.

Prompts exist to communicate:

- business intent,
- engineering constraints,
- architectural context,
- repository knowledge,
- desired outcomes,
- acceptance criteria.

A prompt should minimize ambiguity while maximizing useful engineering context.

---

# Problem Statement

Poor prompts frequently produce:

- hallucinated APIs,
- duplicate implementations,
- architectural violations,
- incorrect assumptions,
- missing validation,
- inconsistent naming,
- incomplete testing,
- undocumented decisions,
- security weaknesses.

The majority of AI engineering failures originate before code generation begins.

---

# AI Operational Decision

Prompt engineering is mandatory for every engineering activity.

No significant implementation shall begin without a sufficiently complete engineering prompt.

Prompt quality directly influences engineering quality.

---

# Rationale

Artificial Intelligence reasons only from the information available to it.

Missing context forces AI to infer.

Inference increases engineering risk.

A well-constructed prompt reduces uncertainty and increases deterministic behavior.

---

# Why This Matters

Traditional software engineering begins with requirements.

AI engineering begins with prompts.

Therefore prompts become engineering artifacts.

Poor prompts create poor software.

High-quality prompts create high-quality engineering outcomes.

---

# Prompt Engineering Philosophy

Prompts communicate engineering intent.

They are not conversations.

They are structured engineering specifications.

Every prompt should resemble an engineering design brief rather than an informal request.

---

# Prompt Engineering Objectives

Every prompt should:

- establish business context,
- establish engineering context,
- establish repository context,
- establish architectural constraints,
- establish quality expectations,
- define success criteria,
- define completion criteria.

---

# Prompt Categories

## Business Prompts

Purpose

Understand business problems.

Examples

- Requirement analysis
- User stories
- Acceptance criteria
- Business workflows

---

## Architecture Prompts

Purpose

Reason about architecture.

Examples

- Module decomposition
- DDD
- Hexagonal Architecture
- ADR creation

---

## Development Prompts

Purpose

Generate implementation.

Examples

- APIs
- Domain Models
- Services
- Event Handlers

---

## Quality Prompts

Purpose

Validate software.

Examples

- Testing
- Reviews
- Static analysis
- Security validation

---

## Documentation Prompts

Purpose

Generate engineering knowledge.

Examples

- ADRs
- README
- Architecture documents
- Runbooks

---

# Standard Prompt Structure

Every engineering prompt shall contain the following sections whenever applicable.

## 1. Objective

What problem is being solved?

---

## 2. Business Context

Why is this work required?

---

## 3. Repository Context

Which modules are affected?

Which existing implementations exist?

---

## 4. Constitutional Constraints

Which constitutional principles apply?

Examples

Architecture

Development

Quality

Security

Documentation

---

## 5. Technical Constraints

Examples

Java 21

Spring Boot

PostgreSQL

Kafka

React

Azure

---

## 6. Existing System

What currently exists?

Reuse before creation.

---

## 7. Deliverables

Examples

Implementation

Tests

Documentation

ADR

Migration

---

## 8. Acceptance Criteria

Define measurable completion.

---

## 9. Success Criteria

Examples

No duplicated code.

Architecture preserved.

Tests generated.

Documentation updated.

---

# Context Before Prompt

AI shall gather:

Business Context

↓

Architecture Context

↓

Repository Context

↓

Module Context

↓

Implementation Context

↓

Prompt

Prompt construction occurs only after sufficient context has been collected.

---

# Prompt Construction Rules

Prompts shall:

- describe the objective,
- describe constraints,
- describe success,
- reference existing artifacts,
- identify assumptions,
- minimize ambiguity.

---

# Prompt Quality Principles

A high-quality prompt is:

Specific

Complete

Deterministic

Context-rich

Measurable

Constitutionally compliant

---

# Prompt Hierarchy

Engineering Constitution

↓

Repository Documentation

↓

Architecture

↓

Requirements

↓

Prompt

↓

Implementation

Lower levels shall never contradict higher levels.

---

# Prompt Reuse

Prompts are reusable engineering assets.

Frequently used prompts shall be version-controlled.

Examples

Feature implementation

Bug fixing

Architecture review

Performance review

Security review

Documentation generation

---

# AI SHALL

AI SHALL:

Retrieve repository context before constructing prompts.

Understand the business objective.

Reference applicable constitutional principles.

Prefer existing implementations.

Generate implementation plans before coding.

Document assumptions.

Generate tests.

Generate documentation.

Validate completion.

---

# AI SHOULD

AI SHOULD:

Suggest better prompt structure.

Identify missing context.

Recommend architectural improvements.

Highlight risks.

Reduce ambiguity.

Improve engineering clarity.

Generate ADR recommendations when necessary.

---

# AI MAY

AI MAY:

Suggest refactoring.

Suggest optimization.

Suggest alternative architectures.

Generate diagrams.

Recommend tooling improvements.

---

# Mandatory Rules

Prompt construction precedes implementation.

Context retrieval is mandatory.

Architecture constraints shall be respected.

Repository conventions shall be followed.

Acceptance criteria shall be explicit.

Missing information shall be identified.

Hallucination shall be avoided.

---

# Recommended Practices

Prefer structured prompts.

Use engineering terminology.

Reference existing modules.

Break large requests into phases.

Reuse engineering templates.

Generate plans before implementation.

---

# Prohibited Practices

Do not prompt without context.

Do not request implementation before understanding the problem.

Do not omit acceptance criteria.

Do not assume repository structure.

Do not ignore constitutional requirements.

Do not encourage speculative implementation.

---

# Allowed Exceptions

Small exploratory prototypes may use lightweight prompts when explicitly identified as experimental.

Educational examples may omit repository context when isolated from production engineering.

---

# AI Self-Validation

Before acting upon a prompt, AI shall verify:

✓ Business objective understood

✓ Repository context retrieved

✓ Applicable constitutional principles identified

✓ Existing implementation reviewed

✓ Acceptance criteria defined

✓ Risks identified

✓ Assumptions documented

✓ Clarification requested if necessary

---

# Prompt Review Checklist

Reviewers shall verify:

- Is the objective clear?
- Is sufficient context provided?
- Are constitutional principles referenced?
- Are deliverables defined?
- Are success criteria measurable?
- Are assumptions documented?
- Is repository reuse encouraged?
- Are acceptance criteria complete?

---

# Success Metrics

| Metric | Target |
|----------|---------|
| Context Completeness | 100% |
| Hallucinated Artifacts | 0 |
| Missing Acceptance Criteria | 0 |
| Constitution Compliance | 100% |
| Prompt Reusability | Continuous Improvement |

---

# AI Failure Modes

Poor prompts frequently produce:

- Hallucinated APIs
- Duplicate implementations
- Missing validation
- Missing tests
- Incorrect assumptions
- Architecture drift
- Security omissions
- Inconsistent naming
- Unnecessary abstractions
- Context blindness

Every prompt should actively reduce these risks.

---

# Examples

## Good Prompt

Objective:
Implement policy renewal.

Context:
Existing Policy Aggregate.
Hexagonal Architecture.
DDD.
Reuse existing repositories.

Deliverables:
Implementation.
Unit Tests.
Integration Tests.
Documentation.

Acceptance Criteria:
Policy renews successfully.
Expired policies rejected.
Domain events published.
Tests passing.

---

## Poor Prompt

"Create policy renewal."

No context.

No architecture.

No constraints.

No repository information.

No acceptance criteria.

---

# Anti-patterns

Prompt Driven Development

Code First Thinking

Context-Free Engineering

Hallucinated Architecture

Repository Blindness

Requirement Guessing

Specification by Assumption

Implementation Without Planning

---

# Constitutional Compliance Matrix

| Requirement | Status |
|-------------|--------|
| Business Context | Mandatory |
| Repository Context | Mandatory |
| Constitution Compliance | Mandatory |
| Acceptance Criteria | Mandatory |
| Context Retrieval | Mandatory |
| Planning | Mandatory |

---

# Engineering Decision

Prompt engineering is a constitutional engineering discipline.

Every engineering artifact begins with a high-quality prompt.

Prompt quality directly determines engineering quality.

---

# References

- AI Engineering Philosophy
- AI Decision Framework
- AI Context Management
- Engineering Principles
- Architecture Principles

---

# Related Documents

- AI Code Generation
- AI Documentation
- AI Testing
- AI Code Review