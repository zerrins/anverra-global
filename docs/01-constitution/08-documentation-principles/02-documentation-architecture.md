---
document: Documentation Architecture
id: AEC-DOC-002
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-ARC-000
  - AEC-REP-002
---

# Purpose

Define the architectural model for organizing engineering documentation within repositories and across the broader AEOS engineering ecosystem.

Documentation architecture defines how knowledge is structured, categorized, connected, discovered, maintained, and governed.

The objective is to prevent documentation from becoming an unstructured collection of files.

---

# Intent

Engineering knowledge shall have a predictable architecture.

An engineer, operator, architect, product stakeholder, or AI agent should be able to determine:

- Where information belongs.
- Where to find it.
- Which document is authoritative.
- How documents relate to one another.
- How information should evolve.

Documentation architecture shall make knowledge navigable.

---

# Problem Statement

Unstructured documentation commonly results in:

- Duplicate information
- Conflicting sources
- Unclear ownership
- Difficult discovery
- Stale documentation
- Large unmaintainable documents
- Knowledge fragmentation
- AI context inefficiency

Documentation requires architecture just as software requires architecture.

---

# Constitutional Decision

Documentation shall be organized according to a defined information architecture.

Documents shall be grouped by purpose, audience, lifecycle, and authority.

Repositories shall avoid uncontrolled documentation structures.

---

# Rationale

Documentation systems grow over time.

Without an architectural model, documentation tends to evolve through accumulation rather than design.

A documentation architecture provides:

- Discoverability
- Consistency
- Ownership
- Maintainability
- Traceability

---

# Documentation Architecture Philosophy

Documentation should be:

```text
Discoverable
    ↓
Structured
    ↓
Connected
    ↓
Authoritative
    ↓
Maintainable
```

A documentation system should help users move from high-level understanding to detailed implementation.

---

# Documentation Layers

AEOS defines the following documentation layers.

## Level 1 — Orientation

Answers:

> What is this system?

Examples:

- README
- Product Overview
- System Overview

---

## Level 2 — Architecture

Answers:

> How is this system structured?

Examples:

- Context diagrams
- Container diagrams
- Component descriptions
- Architecture documentation

---

## Level 3 — Engineering

Answers:

> How is this system developed?

Examples:

- Development guides
- Coding standards
- Build instructions
- Testing standards

---

## Level 4 — Interface

Answers:

> How do systems interact with this system?

Examples:

- OpenAPI
- AsyncAPI
- GraphQL schemas
- Event contracts

---

## Level 5 — Operations

Answers:

> How is this system operated?

Examples:

- Deployment guides
- Runbooks
- Monitoring guides
- Recovery procedures

---

## Level 6 — Decisions

Answers:

> Why was the system designed this way?

Examples:

- ADRs
- Architecture decisions
- Trade-off records

---

## Level 7 — Governance

Answers:

> What rules govern this system?

Examples:

- Engineering standards
- Security policies
- Compliance requirements
- Governance decisions

---

# Canonical Documentation Structure

A repository should use a predictable structure.

Example:

```text
docs/
├── README.md
│
├── architecture/
│   ├── README.md
│   ├── context.md
│   ├── system-overview.md
│   ├── components.md
│   └── diagrams/
│
├── api/
│   ├── README.md
│   ├── openapi.yaml
│   └── events/
│
├── development/
│   ├── setup.md
│   ├── build.md
│   ├── testing.md
│   └── contributing.md
│
├── operations/
│   ├── deployment.md
│   ├── monitoring.md
│   ├── troubleshooting.md
│   └── runbooks/
│
├── decisions/
│   ├── README.md
│   └── adr/
│
├── security/
│   ├── security-model.md
│   └── threat-model.md
│
└── glossary.md
```

The exact structure may vary by repository, but the underlying information architecture shall remain consistent.

---

# Documentation Hierarchy

Documentation shall support progressive understanding.

Preferred hierarchy:

```text
System

↓

Capability

↓

Architecture

↓

Component

↓

Interface

↓

Implementation

↓

Operations
```

Users should not need to begin with low-level implementation details.

---

# Navigation

Every major documentation area should provide navigation.

Examples:

- README
- Table of Contents
- Index
- Cross-links
- Breadcrumbs where supported

Documentation should be navigable without relying exclusively on search.

---

# Documentation Relationships

Documents should reference related knowledge.

Example:

```text
Architecture Decision
        ↓
Architecture Documentation
        ↓
API Contract
        ↓
Implementation
        ↓
Operational Runbook
```

Cross-references preserve traceability.

---

# Source of Truth Architecture

Every important concept shall have one authoritative source.

Examples:

| Knowledge | Source of Truth |
|-----------|----------------|
| API Contract | OpenAPI |
| Event Contract | AsyncAPI / Schema |
| Architecture Decision | ADR |
| Build Process | Build Documentation |
| Repository Rules | Repository Constitution |
| Operational Recovery | Runbook |
| Business Terminology | Glossary |

Other documents should reference rather than duplicate authoritative information.

---

# Documentation Ownership Model

Documentation ownership should follow engineering ownership.

For example:

```text
Business Capability Owner
        ↓
Capability Documentation

Architecture Owner
        ↓
Architecture Documentation

Service Owner
        ↓
Operational Documentation

Platform Owner
        ↓
Infrastructure Documentation
```

Ownership should be explicit.

---

# Documentation Boundaries

Documentation boundaries should align with system boundaries where practical.

A module should not require engineers to navigate unrelated documentation to understand its responsibilities.

---

# Documentation Granularity

Documentation should be divided according to meaningful concepts.

Avoid:

```text
Everything.md
```

Avoid excessive fragmentation:

```text
one-sentence-file.md
```

Prefer documents that represent coherent engineering concepts.

---

# Documentation Modularity

Documentation should be independently maintainable.

A change to one concept should not require rewriting unrelated documentation.

---

# Documentation Metadata

Important documents should include metadata such as:

```yaml
document:
id:
version:
status:
owner:
created:
last-reviewed:
depends-on:
```

Metadata improves:

- Ownership
- Discovery
- Lifecycle management
- AI context
- Governance

---

# Documentation Status

Documents may use statuses such as:

- Draft
- Active
- Deprecated
- Superseded
- Archived

Status shall communicate whether information should be relied upon.

---

# Documentation and Repository Structure

Documentation structure should complement repository structure.

The repository should make it easy to navigate between:

```text
Code

↔

Tests

↔

Documentation

↔

Architecture

↔

Decisions
```

---

# Documentation and AI Context

AI systems should be able to discover documentation efficiently.

Repositories should provide:

- Clear entry points
- Consistent naming
- Predictable locations
- Explicit relationships
- Machine-readable metadata

Documentation architecture directly affects AI effectiveness.

---

# Documentation Discovery

An engineer or AI agent should be able to answer:

- Where is architecture documented?
- Where are APIs defined?
- Where are decisions recorded?
- Where are deployment procedures?
- Where are security requirements?
- Where are engineering standards?

without requiring tribal knowledge.

---

# Documentation Evolution

The documentation architecture shall evolve with the repository.

New documentation categories may be introduced when justified.

Existing categories should not be duplicated unnecessarily.

---

# AI Guidance

AI shall:

- Follow the repository documentation architecture.
- Place documents in appropriate locations.
- Reuse existing categories.
- Avoid creating duplicate documentation.
- Update indexes and cross-references.
- Detect misplaced documentation.
- Preserve metadata.

AI shall understand documentation architecture before creating new documents.

---

# Human Responsibilities

Humans remain responsible for:

- Documentation architecture decisions
- Ownership
- Information classification
- Source-of-truth decisions
- Major structural changes

AI may recommend structural improvements.

---

# Mandatory Rules

Repositories shall:

- Maintain predictable documentation categories.
- Define authoritative sources.
- Provide navigation.
- Maintain document ownership.
- Avoid uncontrolled duplication.
- Preserve cross-references.
- Use consistent metadata for governed documents.

---

# Recommended Practices

Keep documentation close to the system it describes.

Prefer stable category names.

Use indexes for large documentation sets.

Use diagrams for structural relationships.

Automate link validation.

Review documentation structure periodically.

---

# Prohibited Practices

Do not:

- Store all documentation in a single uncontrolled file.
- Create duplicate authoritative sources.
- Hide critical documentation in arbitrary locations.
- Create documentation categories without purpose.
- Allow obsolete documents to appear authoritative.
- Break navigation without updating references.

---

# Allowed Exceptions

Small repositories may use a simplified structure when their documentation scope is limited.

The underlying principles of discoverability, ownership, and authority still apply.

---

# Success Metrics

| Metric | Target |
|---------|--------|
| Critical Documentation Discoverability | 100% |
| Authoritative Sources Defined | 100% |
| Broken Documentation Links | 0 |
| Orphaned Critical Documents | 0 |
| Duplicate Authoritative Sources | 0 |
| Documentation Ownership Coverage | 100% |

---

# Review Checklist

Reviewers shall verify:

- Is documentation logically organized?
- Are categories meaningful?
- Are authoritative sources clear?
- Can engineers navigate documentation easily?
- Are documents appropriately granular?
- Are metadata and ownership present?
- Are cross-references valid?
- Is the architecture AI-discoverable?

---

# Examples

## Good

```text
README
  ↓
Architecture
  ↓
API
  ↓
Development
  ↓
Operations
  ↓
Decisions
```

A new engineer can progressively understand the system.

---

## Poor

```text
docs/
├── final.md
├── final2.md
├── architecture-new.md
├── old-architecture.md
├── notes.md
├── misc.md
└── stuff.md
```

The structure provides no reliable knowledge architecture.

---

# Anti-patterns

Documentation Dump

Everything-in-One-File

Duplicate Sources of Truth

Orphaned Documentation

Unclear Ownership

Hidden Architecture

Unstructured Knowledge

Documentation Folder as Storage Bin

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |
| Documentation Principles | Mandatory |

---

# Engineering Decision

Documentation shall have architecture.

Engineering knowledge shall be deliberately structured into discoverable, connected, authoritative, and maintainable information domains.

Documentation architecture exists to make engineering knowledge understandable to humans and efficiently consumable by AI systems.

---

# References

- C4 Model
- arc42
- Diátaxis
- Docs-as-Code
- Information Architecture Principles

---

# Related Documents

- Documentation Philosophy
- Documentation Standards
- Diagrams and Models
- API Documentation
- Operational Documentation
- Decision Documentation
- Knowledge Management
- AI Documentation
- Repository Principles