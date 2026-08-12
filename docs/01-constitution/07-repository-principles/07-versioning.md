---
document: Versioning
id: AEC-REP-007
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-000
  - AEC-REP-005
  - AEC-REP-006
---

# Purpose

Define the constitutional standards governing versioning across repositories managed under the Anverra Engineering Operating System (AEOS).

Versioning communicates the evolution of software systems.

A version is more than a release identifier—it is a contractual statement describing compatibility, stability, maturity, and engineering change.

Every released artifact shall follow a consistent versioning strategy.

---

# Intent

Version numbers shall communicate engineering meaning.

A version should clearly indicate:

- Compatibility
- Release maturity
- Breaking changes
- Feature additions
- Defect corrections
- Operational readiness

Engineering teams, customers, and AI agents shall be able to understand software evolution by inspecting version history.

---

# Problem Statement

Inconsistent versioning frequently results in:

- Unexpected breaking changes
- Difficult upgrades
- API incompatibilities
- Customer confusion
- Deployment uncertainty
- Unclear release history

Poor versioning increases operational risk.

---

# Repository Decision

AEOS adopts **Semantic Versioning (SemVer 2.0.0)** as the constitutional versioning standard.

Every version shall communicate the impact of engineering changes.

---

# Rationale

Software evolves continuously.

Consumers rely on version numbers to determine:

- Upgrade safety
- Compatibility
- Risk
- Required testing
- Migration effort

Meaningful version numbers improve engineering predictability.

---

# Versioning Philosophy

Version numbers represent engineering contracts.

Incrementing a version is an engineering decision—not a deployment activity.

Every version change shall have documented justification.

---

# Semantic Version Format

```
MAJOR.MINOR.PATCH
```

Example

```
1.0.0

2.4.1

5.12.3
```

Optional pre-release identifiers:

```
2.0.0-alpha.1

2.0.0-beta.3

2.0.0-rc.2
```

Build metadata:

```
2.1.0+20260803

2.1.0+git.a32bc4
```

---

# Version Components

## Major Version

Increment when:

- Public APIs break
- Database compatibility changes
- Business contracts change
- Migration is required

Examples

```
1.x.x → 2.0.0
```

Major versions indicate incompatible changes.

---

## Minor Version

Increment when:

- New functionality is added
- Existing APIs remain compatible
- New modules are introduced
- New capabilities become available

Examples

```
2.4.0 → 2.5.0
```

Minor versions remain backward compatible.

---

## Patch Version

Increment when:

- Bugs are fixed
- Performance improves
- Documentation changes
- Internal refactoring occurs
- Security patches are applied

Examples

```
2.5.1 → 2.5.2
```

Patch releases must never introduce breaking behavior.

---

# Release Maturity Levels

## Alpha

Internal experimentation.

Features incomplete.

Interfaces unstable.

---

## Beta

Feature complete.

Testing continues.

Limited production use.

---

## Release Candidate

Production candidate.

Only critical fixes allowed.

No new functionality.

---

## General Availability

Stable production release.

Fully supported.

---

## Long-Term Support

Supported for extended maintenance.

Security updates continue.

---

# Compatibility Principles

Repositories shall preserve:

- API compatibility
- Event compatibility
- Database compatibility
- Configuration compatibility
- Deployment compatibility

Breaking compatibility requires a Major version.

---

# API Versioning

Public APIs shall be versioned.

Preferred examples:

```
/api/v1

/api/v2
```

Avoid versioning individual endpoints independently.

API deprecation shall precede removal.

---

# Event Versioning

Published events are contracts.

Versioning strategies may include:

```
PolicyIssuedV2
```

or

Schema version metadata.

Consumers shall receive migration guidance.

---

# Database Versioning

Database evolution shall use migrations.

Rules:

- Forward-only migrations
- Version-controlled
- Repeatable where appropriate
- Reviewed
- Tested

Database schema changes shall align with application versions.

---

# Dependency Versioning

Internal dependencies shall:

- follow SemVer,
- document compatibility,
- avoid hidden breaking changes.

Dependency upgrades require validation.

---

# Configuration Versioning

Configuration changes affecting behavior shall be versioned and documented.

Backward compatibility should be preserved whenever practical.

---

# Documentation Versioning

Documentation shall evolve with releases.

Every released version should reference:

- Architecture
- APIs
- Configuration
- Upgrade Guide
- Release Notes

Documentation versions shall remain synchronized with software versions.

---

# Release Notes

Every release shall include:

- Summary
- Features
- Bug Fixes
- Breaking Changes
- Migration Guidance
- Security Updates
- Known Limitations

Release notes are mandatory.

---

# Deprecation Policy

Deprecation shall follow this lifecycle:

```
Announce

↓

Document

↓

Warn

↓

Provide Alternative

↓

Remove (Major Release)
```

Deprecated functionality shall never disappear without notice.

---

# AI Guidance

AI shall:

- Recommend appropriate version increments.
- Detect breaking changes.
- Identify compatibility risks.
- Suggest migration documentation.
- Preserve semantic versioning rules.

AI shall never recommend arbitrary version changes.

---

# Mandatory Rules

Repositories shall:

- Use Semantic Versioning.
- Maintain release notes.
- Document breaking changes.
- Preserve backward compatibility.
- Version public APIs.
- Review version changes.

---

# Recommended Practices

Release frequently.

Keep changes small.

Automate version generation.

Tag releases.

Document compatibility.

Review version history.

---

# Prohibited Practices

Do not:

- Skip versions.
- Reuse released versions.
- Introduce breaking changes in patch releases.
- Hide incompatible changes.
- Remove deprecated functionality without notice.
- Change version numbers arbitrarily.

---

# Allowed Exceptions

Experimental repositories may use:

```
0.x.y
```

until public stability is achieved.

Such repositories shall clearly indicate experimental status.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Semantic Version Compliance | 100% |
| Breaking Changes Documented | 100% |
| Release Notes Coverage | 100% |
| Backward Compatibility | 100% (unless Major) |
| Version Tag Accuracy | 100% |

---

# Review Checklist

Reviewers shall verify:

- Is the version increment correct?
- Are breaking changes documented?
- Are release notes complete?
- Are migrations documented?
- Is compatibility preserved?
- Are APIs versioned correctly?
- Are deprecations communicated?

---

# Examples

## Good

```
2.3.4

↓

Bug Fix

↓

2.3.5
```

---

```
2.4.0

↓

New Feature

↓

2.5.0
```

---

```
2.9.0

↓

Breaking API

↓

3.0.0
```

---

## Poor

```
2.5.1

↓

Breaking Database Change

↓

2.5.2
```

Patch releases must not introduce incompatible behavior.

---

# Anti-patterns

Version Inflation

Hidden Breaking Changes

Silent API Removal

Undocumented Deprecation

Skipping Major Releases

Arbitrary Version Numbers

Compatibility by Assumption

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

Version numbers are engineering contracts.

Every released artifact shall communicate compatibility, stability, and engineering intent through a consistent Semantic Versioning strategy.

Versioning shall preserve trust between engineering teams, AI systems, and software consumers.

---

# References

- Semantic Versioning 2.0.0
- Engineering Constitution
- Continuous Delivery
- Release Engineering

---

# Related Documents

- Git Standards
- Branching Strategy
- Build Standards
- Dependency Management
- Release Management
- Engineering Governance