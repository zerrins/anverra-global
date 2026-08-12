---
document: Folder Structure
id: AEC-REP-002
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-000
  - AEC-REP-001
---

# Purpose

Define the canonical repository folder structure for every engineering repository within the Anverra Engineering Operating System (AEOS).

A standardized folder structure enables:

- Engineering consistency
- Faster onboarding
- Improved discoverability
- Simplified maintenance
- Better automation
- Predictable AI navigation

Every repository shall organize engineering artifacts in a consistent manner regardless of technology stack.

---

# Intent

Folder organization shall communicate engineering intent.

Folders exist to represent engineering concepts rather than implementation convenience.

A repository should be understandable by:

- New Engineers
- Senior Engineers
- Architects
- DevOps Engineers
- QA Engineers
- AI Agents

without requiring additional explanation.

---

# Problem Statement

Repositories commonly suffer from:

- Random folder organization
- Technology-first structures
- Hidden documentation
- Scattered configuration
- Duplicate utilities
- Mixed responsibilities
- Poor discoverability

These problems increase engineering complexity and reduce productivity.

---

# Repository Decision

Every repository shall adopt the AEOS Canonical Folder Structure.

Additional folders may be introduced when justified, but existing constitutional organization shall not be violated.

---

# Rationale

A predictable repository structure:

- reduces cognitive load,
- accelerates onboarding,
- improves code reviews,
- enables automation,
- enables AI collaboration,
- supports long-term maintainability.

Folder organization is an architectural concern.

---

# Folder Organization Principles

Every folder shall satisfy one or more of the following principles.

## Single Responsibility

Each folder has one clearly defined purpose.

---

## Business Alignment

Business capabilities take precedence over technical layers.

---

## Discoverability

Information should be located where engineers expect it.

---

## Scalability

Folder organization should remain effective as projects grow.

---

## AI Readability

AI agents shall be able to locate important engineering artifacts using predictable locations.

---

# Canonical Repository Layout

```
repository/

├── .ai/
│   ├── prompts/
│   ├── workflows/
│   ├── memory/
│   ├── context/
│   ├── templates/
│   ├── rules/
│   └── skills/
│
├── .github/
│   ├── workflows/
│   ├── ISSUE_TEMPLATE/
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── CODEOWNERS
│
├── architecture/
│
├── docs/
│   ├── adr/
│   ├── api/
│   ├── business/
│   ├── deployment/
│   ├── operations/
│   ├── onboarding/
│   └── decisions/
│
├── infrastructure/
│
├── scripts/
│
├── tools/
│
├── src/
│
├── tests/
│
├── examples/
│
├── resources/
│
├── config/
│
├── build/
│
├── README.md
│
├── CHANGELOG.md
│
├── LICENSE
│
└── CONTRIBUTING.md
```

The exact implementation folders inside `src/` depend on the chosen technology stack, but the overall repository organization remains consistent.

---

# Folder Definitions

## .ai/

Contains AI-specific engineering assets.

Examples:

- Prompt templates
- AI workflows
- Context documents
- Engineering memory
- AI skills
- Coding rules

Purpose:

Enable consistent AI-assisted engineering.

---

## .github/

Contains repository automation.

Examples:

- CI/CD
- Pull Request Templates
- Issue Templates
- CODEOWNERS

---

## architecture/

Contains high-level architecture.

Examples:

- Context Diagrams
- Container Diagrams
- Component Diagrams
- Data Flow Diagrams
- Domain Models

---

## docs/

Contains engineering documentation.

Examples:

- ADRs
- API Specifications
- Business Documents
- Operational Guides
- Deployment Guides

---

## infrastructure/

Contains infrastructure definitions.

Examples:

- Terraform
- Kubernetes
- Docker
- Helm
- Cloud Resources

---

## scripts/

Contains automation scripts.

Examples:

- Build
- Release
- Database
- Migration
- Maintenance

---

## tools/

Contains engineering tooling.

Examples:

- Static Analysis
- Code Generation
- Internal Utilities

---

## src/

Contains production source code.

Source organization shall follow Module Organization principles.

---

## tests/

Contains automated tests.

Examples:

- Unit
- Integration
- Contract
- Performance
- End-to-End

---

## resources/

Contains non-source artifacts.

Examples:

- Images
- Templates
- Sample Data
- Static Resources

---

## config/

Contains configuration templates.

Environment-specific values shall not be committed.

---

## build/

Contains generated build artifacts.

Generated artifacts should not be version-controlled unless explicitly required.

---

# AI Repository Discovery

AI agents shall inspect folders in the following order.

```
README

↓

Engineering Constitution

↓

architecture/

↓

docs/

↓

.ai/

↓

src/

↓

tests/

↓

config/

↓

infrastructure/
```

This order maximizes contextual understanding before implementation.

---

# Folder Naming Standards

Folders shall:

- use lowercase
- use descriptive names
- avoid abbreviations
- avoid technology-specific names unless required
- remain consistent across repositories

Examples

Good

```
architecture
documentation
customer
policy
commission
```

Poor

```
misc
temp
helpers
stuff
newcode
```

---

# Technology Independence

The repository layout shall remain largely independent of programming language.

Examples

Java

```
src/main/java
```

Python

```
src/
```

Go

```
cmd/
internal/
pkg/
```

React

```
src/
public/
```

These technology-specific layouts exist within the constitutional repository organization.

---

# Mandatory Rules

Repositories shall:

- Follow the canonical layout.
- Separate documentation from implementation.
- Store architecture independently.
- Separate infrastructure from application code.
- Store AI artifacts in predictable locations.
- Avoid unrelated folders.

---

# Recommended Practices

Prefer shallow folder hierarchies.

Group related artifacts.

Archive obsolete folders.

Review repository organization periodically.

Document major organizational decisions.

---

# Prohibited Practices

Do not create "misc" folders.

Do not mix infrastructure with application code.

Do not scatter documentation.

Do not duplicate engineering artifacts.

Do not hide architecture inside implementation folders.

Do not create technology-driven root structures without constitutional justification.

---

# Allowed Exceptions

Framework-imposed folder structures may be retained when required by tooling.

Such exceptions shall be documented.

---

# AI Guidance

AI shall:

- Preserve folder organization.
- Avoid unnecessary restructuring.
- Place new artifacts in their canonical location.
- Recommend organizational improvements when appropriate.
- Respect technology-specific conventions within the constitutional layout.

---

# Implementation Guidance

Repository creation shall follow this sequence:

1. Initialize repository.
2. Apply canonical folder structure.
3. Configure version control.
4. Add documentation.
5. Configure build tooling.
6. Configure quality tooling.
7. Configure AI workspace.
8. Begin implementation.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Canonical Folder Compliance | 100% |
| Folder Discoverability | 100% |
| Documentation Placement | 100% |
| AI Navigation Success | 100% |
| Duplicate Root Folders | 0 |

---

# Review Checklist

Reviewers shall verify:

- Does the repository follow the canonical layout?
- Are folders clearly named?
- Is documentation discoverable?
- Is architecture separated?
- Are AI assets organized?
- Are tests isolated?
- Is infrastructure independent?
- Are obsolete folders removed?

---

# Examples

## Good Repository

- Clear structure
- Predictable locations
- Architecture documented
- AI-ready
- Automation-ready

---

## Poor Repository

- Random root folders
- Mixed documentation
- Duplicate utilities
- Hidden architecture
- Inconsistent naming

---

# Anti-patterns

Folder Explosion

Technology-Driven Organization

Documentation Scattering

Configuration Sprawl

AI Context Hidden

Catch-All Directories

Repository by Convenience

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| AI Engineering Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

The repository folder structure is a constitutional architectural asset.

Consistency, discoverability, and AI readability take precedence over personal preference.

---

# References

- Engineering Constitution
- Repository Philosophy
- Domain-Driven Design
- Clean Architecture

---

# Related Documents

- Repository Philosophy
- Module Organization
- Naming Conventions
- AI Context Management
- Documentation Principles