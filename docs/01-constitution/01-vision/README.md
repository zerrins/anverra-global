# AEOS — Stage 1: Vision

**Stage:** 1 — Vision  
**Version:** 1.0  
**Status:** Expanded Draft  
**System:** Anverra Engineering Operating System (AEOS)

---

## 1. Purpose

Stage 1 establishes the highest-level intent that governs Anverra, Anverra Global, engineering, and AI-assisted engineering.

The purpose of this stage is to answer five foundational questions:

1. Why does Anverra exist?
2. What future is Anverra trying to create?
3. What product is being built to realize that future?
4. What kind of engineering organization is required to build and sustain it?
5. What role should AI play in that engineering system?

These documents intentionally remain above detailed implementation.

Stage 1 defines **intent**.

Later AEOS stages progressively convert that intent into:

- goals
- requirements
- decisions
- constraints
- specifications
- standards
- knowledge
- skills
- workflows
- tasks
- artifacts
- validation
- metrics
- releases

---

# 2. AEOS Position

The AEOS Engineering Meta Model establishes the following dependency chain:

```text
Vision
  ↓
Goal
  ↓
Requirement
  ↓
Decision
  ↓
Constraint
  ↓
Specification
  ↓
Standard
  ↓
Knowledge
  ↓
Skill
  ↓
Workflow
  ↓
Task
  ↓
Artifact
  ↓
Validation
  ↓
Metric
  ↓
Release
```

Vision is therefore not merely introductory documentation.

It is the highest-level source of intent from which later engineering artifacts should be derived.

The AEOS execution model is:

```text
Knowledge
    ↓
Reasoning
    ↓
Planning
    ↓
Execution
    ↓
Validation
    ↓
Feedback
    ↓
Improvement
```

Stage 1 provides the intent that the later stages consume.

---

# 3. Stage 1 Documents

| Document | Responsibility |
|---|---|
| `01-company-vision.md` | Defines the long-term organizational direction |
| `02-mission.md` | Defines what Anverra exists to do continuously |
| `03-product-vision.md` | Defines the desired future state of Anverra Global |
| `04-engineering-vision.md` | Defines the desired engineering operating model |
| `05-ai-engineering-vision.md` | Defines the intended role of AI in engineering |
| `06-core-values.md` | Defines stable principles for organizational and engineering decisions |
| `07-success-criteria.md` | Defines how realization of the vision should be evaluated |
| `README.md` | Defines how the Stage 1 documents relate to one another |

---

# 4. Relationship Between the Documents

The documents form a hierarchy rather than seven independent statements.

## Company Vision

Defines the future Anverra wants to create.

↓

## Mission

Defines the enduring activity through which Anverra works toward that future.

↓

## Product Vision

Defines the product that realizes the company mission.

↓

## Engineering Vision

Defines how Anverra should engineer and operate the product.

↓

## AI Engineering Vision

Defines how AI should participate in that engineering system.

↓

## Core Values

Defines the principles that should remain stable while all of the above evolve.

↓

## Success Criteria

Defines observable evidence that the intended direction is being achieved.

---

# 5. Existing AEOS Source Alignment

Stage 1 preserves the existing AEOS direction.

The existing product vision establishes Anverra Global as an enterprise insurance distribution platform designed to manage the lifecycle of insurance distribution across multiple insurance providers.

Anverra is explicitly **not an insurance carrier** and does not underwrite insurance.

The existing product direction describes the platform as:

- AI-first
- enterprise-grade
- modular
- maintainable
- scalable

The existing product goals include:

- AI-assisted software engineering
- modular architecture
- enterprise scalability
- excellent developer experience
- high maintainability
- security by default
- documentation as code

The mission centers on enabling insurance distributors to manage:

- customers
- intermediaries
- products
- policies
- commissions
- documents
- operational workflows

The expanded Stage 1 documents preserve these concepts while providing enough depth for them to serve as genuine foundational documents.

---

# 6. Scope of Stage 1

Stage 1 defines:

- organizational intent
- company direction
- product intent
- engineering intent
- AI engineering intent
- stable values
- high-level measures of success

Stage 1 does not define:

- detailed business requirements
- detailed user stories
- API contracts
- database schemas
- application class structures
- deployment configuration
- repository implementation details
- individual AI prompts
- individual AI skills
- detailed workflows
- technology-specific implementation rules

Those belong to later AEOS artifacts.

---

# 7. Traceability Rule

Every important later engineering artifact should be capable of answering:

> **What intent does this exist to serve?**

The expected direction is:

```text
Vision
  ↓
Goal
  ↓
Requirement
  ↓
Decision
  ↓
Implementation
```

If a later artifact cannot reasonably be connected to a business, product, engineering, quality, security, or AI-engineering objective, its purpose should be questioned.

This does not mean every line of code requires a direct citation to the company vision.

It means significant engineering decisions should remain explainable in terms of upstream intent.

---

# 8. Stability Rule

Stage 1 should change more slowly than lower-level engineering artifacts.

For example:

```text
Code
→ may change daily

Workflow
→ may change frequently

Technology choice
→ changes when justified

Product requirement
→ changes as business needs evolve

Engineering principle
→ changes rarely

Vision
→ changes very rarely
```

A vision should not be rewritten merely because implementation changed.

---

# 9. Human Accountability

AI may assist with:

- reasoning
- planning
- implementation
- documentation
- testing
- validation
- review

However, the vision remains human-owned.

Humans retain accountability for:

- business direction
- product direction
- architecture
- risk
- governance
- strategic decisions
- production outcomes

AI can help execute intent.

AI does not become the owner of the intent.

---

# 10. Source-of-Truth Rule

Important concepts should have an authoritative source.

Stage 1 should not become a duplicate requirements repository.

For example:

```text
Vision
→ defines direction

Product Specification
→ defines behavior

Architecture Decision
→ defines an architectural choice

Engineering Standard
→ defines mandatory engineering behavior

Workflow
→ defines how work is executed
```

Where a later document needs to reference an upstream concept, it should preserve traceability rather than redefining the concept differently.

---

# 11. Change Governance

Changes to Stage 1 should be treated as strategic changes.

A proposed change should identify:

- what statement is changing
- why it is changing
- what evidence supports the change
- which downstream artifacts depend on it
- whether existing goals remain valid
- whether product direction must change
- whether engineering direction must change
- whether metrics need adjustment

A change to Stage 1 should not silently invalidate downstream documents.

---

# 12. Stage 1 Completion Criteria

Stage 1 is sufficiently complete when:

- company direction is explicit
- mission is explicit
- product direction is explicit
- engineering direction is explicit
- AI engineering direction is explicit
- core values are explicit
- success dimensions are explicit
- terminology is consistent
- later AEOS stages can trace their intent back to Stage 1

---

# 13. Stage 1 Mental Model

A simple way to understand Stage 1 is:

> **Company Vision = where we want to go.**

> **Mission = what we continuously do.**

> **Product Vision = what we are building to get there.**

> **Engineering Vision = how we want to build and sustain it.**

> **AI Engineering Vision = how AI participates in that system.**

> **Core Values = how we make decisions while doing it.**

> **Success Criteria = how we know it is working.**

This model should remain understandable to:

- a new engineer
- a product owner
- an architect
- an engineering manager
- an AI agent
- a future contributor

---

# 14. Guiding Principle

Stage 1 should provide enough clarity that someone can understand:

> **What Anverra is trying to become before they need to understand how Anverra is implemented.**

That distinction is fundamental to the AEOS documentation architecture.