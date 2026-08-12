---
document: Branching Strategy
id: AEC-REP-005
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-000
  - AEC-REP-001
  - AEC-REP-002
---

# Purpose

Define the constitutional branching strategy for all repositories governed by the Anverra Engineering Operating System (AEOS).

A standardized branching strategy enables consistent collaboration, controlled releases, predictable deployments, simplified code reviews, and reliable automation across engineering teams and AI agents.

Branching is an engineering workflow rather than a Git feature.

---

# Intent

Every branch shall represent a clearly defined engineering objective.

Branches shall:

- isolate work,
- support parallel development,
- enable continuous integration,
- simplify reviews,
- preserve production stability,
- provide complete traceability.

Branch lifetime should be as short as practical.

---

# Problem Statement

Inconsistent branching strategies often result in:

- Long-lived branches
- Merge conflicts
- Unclear ownership
- Difficult releases
- Inconsistent deployments
- Broken main branches
- Lost engineering history
- Reduced development velocity

These issues increase engineering cost and delivery risk.

---

# Repository Decision

AEOS adopts a **Trunk-Based Development model with short-lived feature branches**.

The repository shall always maintain a deployable default branch.

Feature branches exist only to isolate implementation until review is complete.

---

# Rationale

Modern engineering emphasizes:

- Continuous Integration
- Continuous Delivery
- Small incremental changes
- Frequent merges
- Automated validation

Long-lived branches delay integration and increase engineering risk.

---

# Why This Matters

A predictable branching strategy improves:

- Collaboration
- Review quality
- Deployment frequency
- Release confidence
- Rollback capability
- AI-assisted development

Repository history becomes easier to understand.

---

# Branching Philosophy

Branches represent work—not ownership.

The default branch always represents the latest stable engineering state.

Every change should move toward integration rather than divergence.

---

# Branch Types

AEOS recognizes the following branch categories.

---

## Main Branch

Purpose

Represents production-ready software.

Characteristics

- Always deployable
- Protected
- Reviewed
- Fully validated

Naming

```
main
```

---

## Feature Branch

Purpose

Implements a single engineering feature.

Naming

```
feature/policy-renewal

feature/customer-search

feature/commission-engine
```

Characteristics

- Short-lived
- Small scope
- One engineering objective
- Deleted after merge

---

## Bug Fix Branch

Purpose

Resolve production or development defects.

Naming

```
bugfix/policy-validation

bugfix/login-timeout
```

---

## Hotfix Branch

Purpose

Critical production fixes.

Naming

```
hotfix/payment-timeout

hotfix/security-patch
```

Hotfixes shall receive expedited review but remain subject to constitutional compliance.

---

## Release Branch (Optional)

Used only for organizations requiring staged release validation.

Naming

```
release/1.4.0
```

Organizations practicing continuous deployment should avoid long-lived release branches.

---

## Experiment Branch

Purpose

Research or prototype work.

Naming

```
experiment/new-rating-engine

experiment/vector-search
```

Experimental branches shall never merge directly into `main` without formal review.

---

# Branch Lifecycle

```
Task Created
      │
      ▼
Feature Branch Created
      │
      ▼
Implementation
      │
      ▼
Automated Validation
      │
      ▼
Pull Request
      │
      ▼
Engineering Review
      │
      ▼
Merge to Main
      │
      ▼
Branch Deleted
```

Branches should exist only as long as required.

---

# Branch Naming Standards

Branches shall:

- use lowercase
- use hyphens
- begin with branch category
- describe business intent

Good

```
feature/policy-renewal

bugfix/claim-validation

hotfix/payment-timeout
```

Poor

```
new

test

branch1

shashank-work

changes
```

---

# Branch Protection

The default branch shall enforce:

- Pull Requests required
- Passing CI required
- Required approvals
- Linear history (recommended)
- Signed commits (recommended)
- No force pushes
- No direct commits

---

# Pull Request Requirements

Every Pull Request shall include:

- Business objective
- Summary of changes
- Testing evidence
- Documentation updates
- Related issue or ADR
- Risk assessment (where applicable)

---

# Merge Strategy

Preferred merge strategy:

**Squash Merge** for small, self-contained features.

Alternative strategies:

- Merge Commit (for preserving branch history)
- Rebase and Merge (where linear history is preferred)

The selected strategy should remain consistent across the repository.

---

# AI Guidance

AI shall:

- Create appropriately named branches.
- Keep branches focused on a single objective.
- Recommend frequent integration.
- Avoid large, unrelated changes in one branch.
- Suggest branch cleanup after merge.

AI shall never recommend direct commits to protected branches.

---

# Mandatory Rules

Repositories shall:

- Protect the default branch.
- Require Pull Requests.
- Require successful CI.
- Use short-lived feature branches.
- Delete merged branches.
- Keep the main branch deployable.

---

# Recommended Practices

- Merge frequently.
- Keep commits small.
- Review early.
- Prefer incremental delivery.
- Automate branch validation.
- Link branches to work items.

---

# Prohibited Practices

Do not:

- Develop directly on `main`.
- Keep feature branches for weeks.
- Mix unrelated work.
- Merge failing builds.
- Bypass reviews.
- Force push protected branches.

---

# Allowed Exceptions

Emergency hotfixes may follow an expedited review process, provided:

- Changes are minimal.
- Risks are documented.
- Post-release review is completed.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Main Branch Stability | 100% |
| Successful CI Before Merge | 100% |
| Branch Naming Compliance | 100% |
| Average Feature Branch Lifetime | < 5 Days |
| Direct Commits to Main | 0 |

---

# Review Checklist

Reviewers shall verify:

- Is the branch correctly named?
- Does it represent a single objective?
- Is CI passing?
- Are reviews complete?
- Is documentation updated?
- Is the branch ready for deletion?

---

# Examples

## Good

```
feature/customer-search

↓

2 days

↓

Reviewed

↓

Merged

↓

Deleted
```

---

## Poor

```
new-feature

↓

45 days

↓

500 commits

↓

Merge conflicts

↓

Unclear ownership
```

---

# Anti-patterns

Long-Lived Feature Branches

Developer Branches

Merge Hell

Direct Production Commits

Massive Pull Requests

Branch Hoarding

Feature Branch as Environment

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Engineering Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

Repositories shall adopt Trunk-Based Development supported by short-lived feature branches.

Branching exists to enable safe collaboration, rapid integration, and continuous delivery—not to create long-lived parallel development streams.

---

# References

- Trunk-Based Development
- Continuous Delivery
- Engineering Constitution

---

# Related Documents

- Git Standards
- Versioning
- Build Standards
- Repository Philosophy
- Development Principles