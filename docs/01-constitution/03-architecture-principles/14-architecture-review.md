---
document: Architecture Review
id: AEC-ARC-014
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
---

# Purpose

Define the architecture review process.

---

# Intent

Architecture review ensures that implementations remain aligned with the Engineering Constitution.

---

# Scope

Architecture review applies to:

- New modules
- Significant features
- External integrations
- Database changes
- Architectural refactoring

---

# Review Areas

Reviewers shall evaluate:

- Business alignment
- Module ownership
- Dependency direction
- Contracts
- Security
- Performance
- Testability
- Maintainability

---

# Mandatory Rules

No significant implementation proceeds without architectural review.

Review findings shall be documented.

Critical violations shall be resolved before implementation.

---

# AI Guidance

AI shall perform a self-review against the Architecture Principles before presenting implementation.

---

# Review Checklist

- Does architecture follow the Constitution?
- Are module boundaries respected?
- Are dependencies correct?
- Are responsibilities clear?
- Are contracts explicit?
- Is data ownership preserved?

---

# Engineering Decision

Architecture review is mandatory for significant engineering work.

---

# Related Documents

- Architecture First
- Architecture Decision Records
- Engineering Principles