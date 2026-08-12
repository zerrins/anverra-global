
---

# `11-documentation-automation.md`

```markdown
---
document: Documentation Automation
id: AEC-DOC-011
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-DOC-003
  - AEC-DOC-009
  - AEC-AI-006
  - AEC-AI-010
---

# Purpose

Define the constitutional principles for automating the creation, validation, synchronization, publication, discovery, and maintenance of engineering documentation.

Automation shall reduce documentation effort while preserving engineering accuracy and human accountability.

---

# Intent

Documentation automation should make high-quality documentation easier to maintain.

Automation should help engineers:

- Detect documentation drift.
- Validate documentation.
- Generate repetitive documentation.
- Synchronize contracts.
- Maintain navigation.
- Identify affected documents.
- Publish documentation consistently.

Automation shall improve documentation quality rather than merely increase document volume.

---

# Problem Statement

Manual documentation maintenance is vulnerable to:

- Forgetfulness.
- Inconsistent formatting.
- Broken links.
- Stale contracts.
- Missed updates.
- Repetitive work.

Automation can reduce these problems when applied appropriately.

---

# Constitutional Decision

Documentation automation shall be used wherever repetitive, deterministic, or machine-verifiable documentation activities can be automated safely.

Human judgment shall remain responsible for meaning, intent, and authority.

---

# Rationale

Some documentation tasks are highly deterministic.

Examples:

- API specification validation.
- Link checking.
- Table-of-contents generation.
- Schema documentation.
- Reference generation.
- Metadata validation.

These tasks should not depend entirely on manual effort.

---

# Automation Philosophy

## Automate the Mechanical

Automate:

- Formatting.
- Validation.
- Synchronization.
- Generation.
- Publishing.

---

## Preserve Human Judgment

Humans remain responsible for:

- Meaning.
- Intent.
- Architecture.
- Business semantics.
- Risk.

---

## Prefer Deterministic Automation

Automated documentation processes should produce predictable results.

---

## Validate Generated Content

Generated documentation shall be validated before publication when correctness matters.

---

# Automation Categories

## Validation Automation

Examples:

- Markdown linting.
- Link checking.
- Metadata validation.
- Spell checking.

---

## Contract Automation

Examples:

- OpenAPI validation.
- Schema validation.
- Contract tests.
- Generated client documentation.

---

## Diagram Automation

Examples:

- Mermaid rendering.
- PlantUML rendering.
- Architecture diagram generation.

---

## Publication Automation

Examples:

- Documentation websites.
- Static site generation.
- API documentation publishing.

---

## Discovery Automation

Examples:

- Search indexes.
- Documentation catalogs.
- Metadata indexes.

---

## Drift Detection

Automation may compare:

```text
Implementation

vs

Documentation