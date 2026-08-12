---
document: Constitution Index
id: AEC-FND-001
version: 1.0.0
status: Draft
stability: Level 5
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
- AEC-FND-000
---

# Purpose

Provide the authoritative navigation structure for the Engineering Constitution.

---

# Intent

Ensure every constitutional document has a well-defined location and hierarchy.

---

# Constitution Structure

```
Engineering Constitution

00 Foundation

01 Vision

02 Engineering Principles

03 Architecture Principles

04 Development Principles

05 Quality Principles

06 AI Principles

07 Repository Principles

08 Documentation Principles

09 Review Principles

10 Governance
```

---

# Navigation Rules

- Every stage owns a single engineering concern.
- Every stage contains a README.
- Documents belong to exactly one stage.
- Cross references shall use document IDs.

---

# Responsibilities

Architecture owns the Constitution Index.

---

# Related Documents

- constitution-glossary.md
- constitution-lifecycle.md