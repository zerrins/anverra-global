---
document: Architecture Decision Records
id: AEC-ARC-013
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
---

# Purpose

Define how significant architectural decisions are documented.

---

# Intent

Important architectural decisions should never exist only in conversations or source code.

They shall be documented for future engineers and AI agents.

---

# When an ADR is Required

Create an ADR when introducing:

- New architecture
- Major technology
- Module boundaries
- Security decisions
- Integration strategy
- Persistence strategy

---

# ADR Contents

Every ADR shall contain:

- Context
- Problem
- Alternatives
- Decision
- Consequences
- Status

---

# Mandatory Rules

- ADRs are immutable after approval.
- Superseded ADRs remain available.
- Every ADR has a unique identifier.

---

# AI Guidance

AI shall reference relevant ADRs before proposing architectural changes.

---

# Review Checklist

- Is an ADR required?
- Is it complete?
- Does implementation match the ADR?

---

# Engineering Decision

Architectural knowledge shall be preserved through ADRs.

---

# Related Documents

- Architecture Review
- Evolutionary Architecture