---
document: Data Ownership
id: AEC-ARC-011
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
---

# Purpose

Define ownership rules for business data across the platform.

---

# Intent

Every piece of business data has exactly one authoritative owner.

Ownership ensures consistency, integrity, and accountability.

---

# Architectural Decision

Each module owns its own business data.

Other modules consume data through contracts or events.

No module directly modifies another module's persistence.

---

# Ownership Rules

Ownership includes:

- Validation
- Persistence
- Lifecycle
- Business rules
- State transitions

---

# Mandatory Rules

- One owner per business entity.
- No cross-module updates.
- No shared database tables.
- No shared persistence layer.

---

# Recommended Practices

- Duplicate read models when necessary.
- Synchronize using events.
- Keep ownership explicit.

---

# Prohibited Practices

- Cross-module SQL.
- Shared repositories.
- Shared aggregate persistence.
- Hidden ownership.

---

# AI Guidance

AI shall always identify the data owner before generating persistence code.

---

# Review Checklist

- Is ownership explicit?
- Does another module update this data?
- Is persistence encapsulated?

---

# Engineering Decision

Business data belongs to exactly one module.

---

# Related Documents

- Module Boundaries
- Business Capability Ownership