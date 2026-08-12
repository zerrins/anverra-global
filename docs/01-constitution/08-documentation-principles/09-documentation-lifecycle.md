
---

# `09-documentation-lifecycle.md`

```markdown
---
document: Documentation Lifecycle
id: AEC-DOC-009
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-DOC-002
  - AEC-DOC-003
  - AEC-DOC-008
---

# Purpose

Define the lifecycle governing engineering documentation from creation through maintenance, review, evolution, deprecation, and retirement.

Documentation shall be managed as a living engineering asset.

---

# Intent

Every important document should have a known lifecycle.

The lifecycle should answer:

- Why was this document created?
- Who owns it?
- When was it last reviewed?
- When should it change?
- Is it still authoritative?
- What replaced it?
- Should it be archived?

---

# Problem Statement

Documentation commonly fails through lifecycle neglect.

Typical failures include:

- Documents created but never maintained.
- Drafts mistaken for authoritative information.
- Deprecated documentation appearing current.
- Architecture changes without documentation updates.
- Historical documents silently deleted.
- Ownership becoming unclear.

Documentation requires lifecycle governance.

---

# Constitutional Decision

Governed documentation shall progress through an explicit lifecycle.

The default lifecycle is:

```text
Proposed

↓

Draft

↓

Active

↓

Reviewed

↓

Updated

↓

Deprecated

↓

Superseded / Archived

↓

Retired