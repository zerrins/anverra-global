---
document: Review Principles
id: AEC-REV-000
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
---

# Review Principles

## Purpose

Review Principles define how engineering work is evaluated before it becomes part of the system.

The purpose of engineering review is to improve correctness, quality, security, maintainability, reliability, and architectural integrity while enabling efficient engineering delivery.

Review is an engineering control, not an approval ceremony.

---

# Scope

These principles govern review of:

- Code
- Designs
- Architecture
- APIs
- Security
- Operations
- Tests
- Documentation
- AI-assisted engineering
- Infrastructure
- Configuration
- Other engineering artifacts

---

# Core Principle

> Engineering work shall be reviewed according to its risk, complexity, impact, and reversibility.

Not every change requires the same review depth.

A one-line typo and a production authentication architecture change should not follow identical review processes.

---

# Review Philosophy

Review exists to answer:

```text
Is this correct?

Is this safe?

Is this maintainable?

Is this consistent with the architecture?

Does it satisfy the requirements?

What could fail?

What assumptions are being made?

What should be changed before acceptance?