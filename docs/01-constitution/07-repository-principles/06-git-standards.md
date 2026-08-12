---
document: Git Standards
id: AEC-REP-006
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-000
  - AEC-REP-005
---

# Purpose

Define the constitutional standards governing the use of Git within repositories managed under the Anverra Engineering Operating System (AEOS).

Git is not merely a source control system.

It is the permanent engineering history of a software system.

Every commit, branch, merge, and tag forms part of the repository's engineering record and shall therefore be created with clarity, traceability, and long-term maintainability.

---

# Intent

Git history shall communicate engineering intent.

Every commit should answer:

- What changed?
- Why did it change?
- What business capability was affected?
- What engineering decision was made?
- How can the change be traced?

Repository history shall remain understandable years after implementation.

---

# Problem Statement

Poor Git practices create:

- unreadable history,
- massive commits,
- missing traceability,
- difficult debugging,
- unclear ownership,
- unreliable releases,
- poor collaboration.

Engineering history becomes difficult to understand and maintain.

---

# Repository Decision

Every repository shall follow the Git Standards defined by AEOS.

Git history is considered an engineering asset.

Commit quality shall take precedence over commit quantity.

---

# Rationale

Git history is used for:

- Debugging
- Incident Investigation
- Security Audits
- Code Reviews
- Knowledge Transfer
- Compliance
- AI Context Discovery

Well-structured history significantly improves long-term maintainability.

---

# Git Philosophy

Git records engineering decisions.

Every commit should represent a meaningful engineering change.

Git history should tell the story of software evolution.

---

# Git Principles

Every Git operation shall be:

## Traceable

Every change can be linked to a business requirement, issue, ADR, or engineering decision.

---

## Atomic

Each commit should represent one logical engineering change.

---

## Reversible

Changes should be easy to revert without affecting unrelated functionality.

---

## Understandable

History should be readable without opening every file.

---

## Auditable

Every significant engineering decision shall be discoverable through repository history.

---

# Commit Philosophy

Commits represent engineering decisions.

A commit should answer:

- What changed?
- Why?
- What problem does it solve?
- Is it independently reviewable?

Large unrelated commits are prohibited.

---

# Commit Granularity

Prefer:

```
Policy validation added

↓

Commission calculation updated

↓

Tests added
```

Instead of:

```
500 files changed
Everything fixed
```

Small commits improve review quality.

---

# Commit Message Standard

AEOS adopts the Conventional Commits specification with engineering extensions.

Format

```
<type>(scope): <summary>
```

Examples

```
feat(policy): implement policy renewal

fix(customer): validate email uniqueness

refactor(claims): simplify claim workflow

docs(architecture): update context diagram

test(policy): add renewal integration tests

perf(reporting): optimize commission query

build(ci): upgrade Maven plugins

chore(deps): update Spring Boot
```

---

# Commit Types

| Type | Purpose |
|-------|----------|
| feat | New functionality |
| fix | Bug fix |
| refactor | Internal improvement |
| perf | Performance improvement |
| test | Testing |
| docs | Documentation |
| build | Build changes |
| ci | CI/CD |
| chore | Maintenance |
| revert | Revert previous commit |

---

# Commit Message Guidelines

Commit summaries shall:

- use imperative mood,
- remain concise,
- describe business intent,
- avoid unnecessary punctuation.

Good

```
feat(policy): support policy renewal
```

Poor

```
changes

fixed stuff

new code

updates

misc
```

---

# Commit Body

Large or significant commits should include:

- Business Context
- Engineering Decision
- Architectural Impact
- Testing Performed
- Breaking Changes
- Related ADR
- Related Issue

Example

```
Business Context

Customers can now renew policies online.

Engineering Decision

Introduced RenewalService to coordinate
the renewal workflow.

Testing

Unit and integration tests added.

ADR

ADR-015
```

---

# Atomic Commit Rules

Each commit shall contain:

- One business objective
- One engineering concern
- One logical change set

Do not mix:

- Refactoring
- Feature implementation
- Dependency upgrades
- Formatting
- Documentation

unless they are inseparable.

---

# Pull Strategy

Repositories should prefer:

```
git pull --rebase
```

to maintain a clean history.

Merge commits should occur intentionally.

---

# Merge Strategy

Preferred:

- Squash Merge
- Rebase Merge

Merge commits shall preserve engineering clarity.

---

# Tags

Every production release shall be tagged.

Format

```
v1.0.0

v2.3.1

v5.0.0-beta.1
```

Tags are immutable.

---

# Signed Commits

Repositories handling production software should require:

- Signed commits
- Verified identities

This improves traceability and auditability.

---

# Git Ignore

Repositories shall maintain a comprehensive `.gitignore`.

Generated artifacts shall not be committed.

Examples

```
target/

build/

dist/

node_modules/

.idea/

.vscode/
```

Environment-specific files shall be excluded.

---

# Binary Files

Avoid committing large binary artifacts.

Prefer:

- Package registries
- Artifact repositories
- Object storage

Version control is optimized for text.

---

# Git LFS

Git LFS should be used for:

- Large media
- ML models
- Large datasets

Use only when justified.

---

# Repository Hygiene

Repositories shall periodically remove:

- Obsolete branches
- Unused tags
- Dead configuration
- Stale documentation

Repository maintenance is continuous.

---

# AI Guidance

AI shall:

- Generate meaningful commit messages.
- Recommend atomic commits.
- Avoid unrelated changes.
- Link commits to business intent.
- Suggest Conventional Commit types.
- Preserve readable history.

AI shall never recommend vague commit messages.

---

# Mandatory Rules

Repositories shall:

- Use Conventional Commits.
- Maintain atomic commits.
- Protect Git history.
- Tag releases.
- Keep commit messages meaningful.
- Preserve traceability.

---

# Recommended Practices

Commit frequently.

Review before committing.

Write descriptive messages.

Separate formatting from functionality.

Keep history linear when practical.

Link commits to issues.

---

# Prohibited Practices

Do not:

- Commit generated artifacts.
- Commit secrets.
- Commit unrelated changes together.
- Use meaningless commit messages.
- Rewrite shared history.
- Force push protected branches.

---

# Allowed Exceptions

Emergency production fixes may temporarily bypass selected workflow requirements provided:

- approval is documented,
- review occurs afterwards,
- repository history remains traceable.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Conventional Commit Compliance | 100% |
| Atomic Commits | 100% |
| Signed Commits (where required) | 100% |
| Protected Branch Violations | 0 |
| Meaningful Commit Messages | 100% |

---

# Review Checklist

Reviewers shall verify:

- Is the commit atomic?
- Is the message meaningful?
- Does the commit represent one objective?
- Is history understandable?
- Are releases tagged?
- Are generated artifacts excluded?
- Is traceability preserved?

---

# Examples

## Good

```
feat(policy): support online renewal

↓

One feature

↓

One review

↓

One merge
```

---

## Poor

```
Updated everything

↓

400 files

↓

No explanation
```

---

# Anti-patterns

Commit Dumping

Friday Mega Commit

Formatting with Feature Changes

Force Push Culture

Developer-Specific History

Meaningless Messages

Repository Pollution

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Engineering Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

Git history is a permanent engineering artifact.

Every commit shall communicate business intent, preserve traceability, support collaboration, and contribute to a clean, auditable engineering history.

---

# References

- Conventional Commits Specification
- Git Documentation
- Engineering Constitution
- Continuous Delivery

---

# Related Documents

- Branching Strategy
- Versioning
- Build Standards
- Repository Philosophy
- Development Principles