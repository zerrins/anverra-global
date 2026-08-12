
---

# `10-documentation-review.md`

```markdown
---
document: Documentation Review
id: AEC-DOC-010
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
  - AEC-REV-000
---

# Purpose

Define the standards for reviewing engineering documentation to ensure that it remains accurate, complete, discoverable, understandable, secure, and aligned with engineering reality.

Documentation review is a quality activity.

---

# Intent

Documentation review shall answer:

- Is this information still true?
- Is it complete enough?
- Can the intended audience understand it?
- Is the source of truth clear?
- Are references valid?
- Does it match implementation?
- Does it contain obsolete information?
- Does it expose sensitive information?

---

# Problem Statement

Documentation can degrade silently.

Unlike source code, stale documentation may continue to exist without causing an immediate build failure.

This makes documentation review essential.

---

# Constitutional Decision

Critical documentation shall be reviewed through defined review mechanisms.

Documentation changes should be reviewed alongside the engineering changes that affect them.

---

# Rationale

Documentation defects can cause:

- Incorrect implementations.
- Deployment failures.
- Security mistakes.
- Integration failures.
- Operational incidents.
- Architectural misunderstandings.

Documentation review reduces these risks.

---

# Review Philosophy

## Review Truth

The primary question is:

> Does this documentation accurately describe reality?

---

## Review Usefulness

Documentation should be useful to its intended audience.

---

## Review Completeness

Documentation should contain the information required for its purpose.

---

## Review Maintainability

Documentation should remain practical to update.

---

# Review Dimensions

Documentation should be reviewed across the following dimensions.

## Accuracy

Does the content match reality?

---

## Completeness

Are important concepts missing?

---

## Clarity

Can the intended audience understand it?

---

## Consistency

Does it use organizational terminology and standards consistently?

---

## Discoverability

Can engineers find it?

---

## Authority

Is the source of truth clear?

---

## Security

Does it expose sensitive information?

---

## Maintainability

Can future engineers update it safely?

---

# Review Types

## Change Review

Performed when documentation changes as part of an engineering change.

---

## Periodic Review

Performed at defined intervals for important documentation.

---

## Triggered Review

Performed when events indicate documentation may have become stale.

Examples:

- Architecture change.
- Incident.
- API change.
- Security change.
- Operational change.

---

## Audit Review

Performed to evaluate compliance with documentation standards.

---

# Documentation Review Severity

Documentation issues may be classified as:

### Critical

Information could cause serious operational, security, or engineering harm.

### High

Information is materially misleading or incomplete.

### Medium

Information reduces usability or maintainability.

### Low

Minor clarity, formatting, or style issue.

---

# Review Workflow

```text
Documentation Change

↓

Automated Validation

↓

Peer Review

↓

Accuracy Verification

↓

Approval

↓

Publication

↓

Lifecycle Update