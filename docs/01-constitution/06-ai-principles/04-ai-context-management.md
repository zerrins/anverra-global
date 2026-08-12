---
document: AI Context Management
id: AEC-AI-004
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
---

# Purpose

Define how AI agents discover, retrieve, prioritize, maintain, and validate context before performing engineering work.

Context is the foundation of correct AI reasoning.

Incomplete or incorrect context leads to poor engineering decisions.

---

# Intent

AI shall understand the existing system before proposing changes.

Generating code without context is prohibited except for explicitly requested prototypes or isolated examples.

---

# Problem Statement

Context-free generation causes:

- duplicate functionality,
- architectural violations,
- inconsistent naming,
- conflicting implementations,
- incorrect assumptions,
- reduced maintainability.

---

# AI Operational Decision

Context retrieval is mandatory before engineering work begins.

AI shall prefer repository knowledge over assumptions.

---

# Context Sources

AI shall retrieve context from, in order:

1. Engineering Constitution
2. ADRs
3. Repository Blueprint
4. Project Documentation
5. Module Documentation
6. Existing Source Code
7. Existing Tests
8. API Specifications
9. Build Configuration
10. User Request

Higher-priority sources override lower-priority sources.

---

# Context Categories

Business Context

Architecture Context

Repository Context

Domain Context

Implementation Context

Operational Context

Historical Context

---

# Context Retrieval Workflow

Receive Task

↓

Identify Required Context

↓

Retrieve Documents

↓

Analyze Existing Implementation

↓

Identify Gaps

↓

Request Clarification (if needed)

↓

Proceed with Engineering

---

# Mandatory Rules

- Retrieve relevant context first.
- Reuse existing knowledge.
- Respect repository conventions.
- Ask for clarification when context is insufficient.
- Never invent missing repository structures.

---

# Recommended Practices

- Summarize retrieved context.
- Identify assumptions explicitly.
- Reference applicable constitutional documents.
- Prefer repository consistency.

---

# Prohibited Practices

- Context-free implementation.
- Hallucinated files.
- Hallucinated APIs.
- Ignoring existing modules.
- Recreating existing functionality.

---

# Allowed Exceptions

Greenfield prototypes may proceed with minimal context when explicitly identified as prototypes.

---

# AI Self-Validation

Before implementation:

- Have I retrieved all relevant context?
- Am I duplicating existing functionality?
- Are assumptions documented?
- Is clarification required?

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Context Retrieved | 100% |
| Duplicate Functionality | 0 |
| Hallucinated Repository Artifacts | 0 |
| Clarification When Needed | 100% |

---

# Review Checklist

- Constitution consulted?
- Existing code reviewed?
- ADRs considered?
- Repository conventions followed?
- Assumptions disclosed?

---

# Anti-patterns

Context-Free Coding

Repository Blindness

Duplicate Features

Hallucinated APIs

Assumption Driven Engineering

---

# Constitutional Compliance Matrix

| Requirement | Status |
|-------------|--------|
| Context Retrieval | Mandatory |
| Repository Analysis | Mandatory |
| Clarification | Mandatory When Needed |

---

# Engineering Decision

Every AI engineering activity shall begin with context acquisition and end with constitutional validation.

---

# Related Documents

- AI Engineering Philosophy
- AI Decision Framework
- AI Prompt Engineering
- Repository Principles