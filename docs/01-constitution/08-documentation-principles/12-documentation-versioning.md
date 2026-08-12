---
document: Documentation Versioning
id: AEC-DOC-012
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
  - AEC-DOC-010
---

# Purpose

Define the constitutional standards for versioning, change tracking, history preservation, compatibility management, and traceability of engineering documentation within the Anverra Engineering Operating System (AEOS).

Documentation versioning ensures that changes to engineering knowledge remain understandable, traceable, and appropriately connected to the evolution of the systems they describe.

---

# Intent

Documentation versioning shall allow engineers to understand:

- What changed.
- When it changed.
- Why it changed.
- Which version the information applies to.
- Whether the change is compatible.
- What documentation superseded previous information.
- Which engineering change caused the documentation change.

Versioning shall provide historical clarity without creating unnecessary administrative overhead.

---

# Problem Statement

Documentation changes without versioning or traceability can result in:

- Unclear historical context.
- Ambiguous system behavior.
- Conflicting documentation.
- Difficult incident investigation.
- Incorrect consumer assumptions.
- Loss of architectural rationale.
- Difficulty determining when information became valid.

Documentation history is part of engineering history.

---

# Constitutional Decision

Documentation changes shall be traceable through source control and applicable versioning mechanisms.

Explicit document versioning shall be used when the content represents a contract, standard, policy, or version-sensitive engineering artifact.

Not every document requires an independent semantic version number.

---

# Rationale

Source control already provides detailed history for repository documentation.

Independent document versions are valuable when consumers need to understand compatibility or applicability independently from repository commits.

The organization shall therefore distinguish between:

- Source-control history.
- Document version.
- System/API version.

These concepts shall not be conflated.

---

# Versioning Philosophy

## History Is Valuable

Documentation history should be preserved where it provides engineering value.

---

## Version Only When Necessary

Versioning should communicate meaningful compatibility or lifecycle information.

Version numbers shall not exist merely for appearance.

---

## Traceability Over Ceremony

Every important documentation change should be traceable to its engineering reason.

---

## Preserve Historical Truth

Historical documentation should represent what was known or decided at the time.

It should not be rewritten to make historical decisions appear consistent with the present.

---

# Versioning Dimensions

Documentation may be associated with several types of version.

## Repository Version

The source-control revision containing the document.

Example:

```text
Git commit
```

---

## Document Version

An explicit version assigned to the document.

Example:

```text
1.0.0
1.1.0
2.0.0
```

---

## API Version

The version of an externally consumed interface.

Example:

```text
v1
v2
```

---

## System Version

The version of a product or software release.

Example:

```text
2026.08
2.4.0
```

These versions may correlate but do not necessarily need to be identical.

---

# When to Use Explicit Document Versioning

Explicit document versions should be used for:

- Engineering standards
- Architecture standards
- Policies
- Public contracts
- API documentation
- Security standards
- Governance documents
- Other documents where consumers depend on a specific version

---

# When Explicit Versioning Is Usually Unnecessary

Independent version numbers are generally unnecessary for:

- Simple README files
- Informational guides
- Temporary working notes
- Generated reference pages
- Documents whose history is already adequately represented by source control

Repository history remains authoritative for these cases.

---

# Semantic Versioning

Where semantic versioning is used, the meaning shall be consistent.

A typical interpretation is:

```text
MAJOR.MINOR.PATCH
```

## MAJOR

Use for incompatible changes.

Examples:

- Major restructuring that changes interpretation.
- Removal of contractual requirements.
- Significant semantic changes.

---

## MINOR

Use for backward-compatible additions.

Examples:

- New guidance.
- Additional requirements that do not invalidate existing consumers.
- Additional sections.

---

## PATCH

Use for corrections that do not materially change meaning.

Examples:

- Typographical fixes.
- Clarifications.
- Broken-link corrections.
- Formatting corrections.

The exact versioning strategy may be adapted for specific document categories.

---

# API Documentation Versioning

API documentation shall align with the API's compatibility strategy.

A breaking API change shall not be hidden by a documentation-only version change.

API versioning shall communicate actual contract evolution.

---

# Architecture Documentation Versioning

Architecture documentation should generally rely on:

- Source-control history.
- ADRs.
- Architecture change records.

Explicit version numbers may be used for formally governed architecture standards.

---

# Decision Documentation Versioning

ADRs shall primarily use:

- Stable identifiers.
- Status.
- Creation history.
- Supersession relationships.

ADR numbering should normally remain stable.

Example:

```text
ADR-001
ADR-002
ADR-003
```

An ADR should not be renumbered merely because its status changes.

---

# Documentation Change Classification

Changes may be classified as:

```text
Clarification
Correction
Addition
Modification
Deprecation
Supersession
Removal
```

Classification should communicate the nature of the change.

---

# Breaking Documentation Changes

A documentation change may be considered breaking when it changes a contract or requirement relied upon by consumers.

Examples:

- Changing API behavior documented as guaranteed.
- Changing a security requirement.
- Changing an operational recovery procedure in a way that invalidates existing practice.
- Changing an engineering standard relied upon by multiple teams.

Breaking documentation changes require appropriate review.

---

# Changelog

A changelog may be maintained for documents where consumers benefit from understanding changes.

Changelogs should focus on meaningful changes.

Avoid entries such as:

```text
Fixed typo.
Updated formatting.
Changed heading.
```

unless those changes have operational significance.

---

# Change Traceability

Important documentation changes should be traceable to:

- Requirement
- Issue
- Pull Request
- Architecture Decision
- Incident
- Security finding
- Release
- Governance decision

Traceability should be proportional to impact.

---

# Documentation and Git

Repository documentation shall use source control.

Git history should provide:

- Author
- Timestamp
- Commit
- Diff
- Review
- Merge history

Documentation changes should follow repository branching and review standards.

---

# Documentation and Releases

Release processes should identify documentation changes that affect:

- Consumers
- Operations
- Architecture
- Security
- Configuration
- APIs

Documentation changes should be released with the corresponding engineering change where practical.

---

# Documentation Snapshots

Snapshots may be useful for:

- Product releases
- API versions
- Compliance evidence
- Historical architecture
- Customer-facing contracts

Snapshots should identify their applicability clearly.

---

# Generated Documentation

Generated documentation shall be reproducible where practical.

The source artifact should remain authoritative.

Example:

```text
OpenAPI Specification
        ↓
Documentation Generator
        ↓
Published API Reference
```

The generated output should not become an independent source of truth.

---

# Documentation Rollback

Documentation should be revertible through source control.

When documentation changes are incorrect:

- Revert the change where appropriate.
- Correct the underlying issue.
- Preserve useful historical context.

---

# Documentation and AI

AI-generated documentation changes shall follow the same versioning and traceability requirements as human-generated changes.

AI shall not bypass version control.

AI-generated changes should be identifiable through normal engineering history where required by organizational policy.

---

# AI Guidance

AI shall:

- Preserve document metadata.
- Increment versions when required.
- Avoid unnecessary version changes.
- Identify potentially breaking documentation changes.
- Preserve historical information.
- Update changelogs when required.
- Maintain supersession relationships.

AI shall not fabricate version history.

---

# Human Responsibilities

Humans remain responsible for:

- Versioning strategy.
- Compatibility interpretation.
- Release impact.
- Historical accuracy.
- Approval of breaking documentation changes.

---

# Mandatory Rules

Governed documentation shall:

- Remain under source control.
- Preserve change history.
- Use explicit versions where required.
- Clearly identify deprecated or superseded information.
- Maintain traceability for significant changes.

---

# Recommended Practices

Prefer source-control history for ordinary documents.

Use semantic versioning for contractual artifacts where appropriate.

Maintain changelogs for high-impact documents.

Link documentation changes to engineering work.

Automate metadata validation.

---

# Prohibited Practices

Do not:

- Change historical versions without justification.
- Create arbitrary version numbers.
- Hide breaking changes.
- Maintain generated output as an independent authority.
- Delete history unnecessarily.

---

# Allowed Exceptions

Temporary working documents may rely entirely on source-control history.

Generated documentation may omit independent versioning when the source artifact provides authoritative version information.

---

# Success Metrics

| Metric | Target |
|---|---:|
| Governed Documents Under Source Control | 100% |
| Significant Changes Traceable | 100% |
| Breaking Documentation Changes Reviewed | 100% |
| Unlinked Superseded Documents | 0 |
| Fabricated Documentation History | 0 |

---

# Review Checklist

Verify:

- Versioning strategy is appropriate.
- Source-control history exists.
- Significant changes are traceable.
- Breaking changes are identified.
- Supersession is clear.
- Generated content has an authoritative source.
- Historical information is preserved.

---

# Examples

## Good

```text
API Contract v1

↓

Breaking Requirement

↓

API Contract v2

↓

v1 Deprecated

↓

v2 Published

↓

Migration Guidance
```

---

## Poor

```text
api-final.md
api-final-new.md
api-final-v2.md
```

No reliable version semantics exist.

---

# Anti-patterns

Version Number Inflation

History Rewriting

Hidden Breaking Changes

Fake Changelogs

Generated Documentation as Authority

Unversioned Contracts

---

# Constitutional Compliance Matrix

| Constitution | Status |
|---|---|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |
| Documentation Principles | Mandatory |
| Review Principles | Mandatory |

---

# Engineering Decision

Documentation versioning exists to preserve engineering history, communicate meaningful change, and protect consumers of engineering knowledge.

Versioning shall be applied deliberately, with source-control history serving as the default historical mechanism and explicit versions used where contractual or lifecycle clarity requires them.

---

# References

- Semantic Versioning
- Git
- Docs-as-Code
- API Versioning Principles
- Architecture Decision Records

---

# Related Documents

- Documentation Philosophy
- Documentation Standards
- Documentation Lifecycle
- Documentation Review
- Documentation Governance
- Documentation Definition of Done