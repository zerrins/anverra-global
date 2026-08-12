---
document: Diagrams and Models
id: AEC-DOC-004
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
  - AEC-ARC-000
---

# Purpose

Define the constitutional standards for creating, maintaining, reviewing, and governing engineering diagrams and models within the Anverra Engineering Operating System (AEOS).

Diagrams and models provide visual representations of engineering systems, relationships, flows, boundaries, and decisions.

They shall be treated as engineering artifacts rather than decorative documentation.

---

# Intent

Diagrams shall make complex engineering concepts easier to understand.

A useful diagram should help an engineer answer questions such as:

- What systems exist?
- Where are the boundaries?
- How do components interact?
- Where does data flow?
- Which system owns a capability?
- Where does a dependency exist?
- What happens during an important workflow?
- Which decisions shape the architecture?

Diagrams shall communicate information that is difficult to communicate efficiently through prose alone.

---

# Problem Statement

Poorly governed diagrams frequently become:

- Outdated
- Misleading
- Overly complex
- Inconsistent
- Unowned
- Difficult to maintain
- Detached from implementation

An incorrect architecture diagram can create more engineering risk than having no diagram because engineers may make decisions based on false information.

---

# Constitutional Decision

Engineering diagrams shall be treated as versioned engineering artifacts.

A diagram shall have a defined purpose, appropriate scope, identifiable ownership, and a clear relationship to the system or concept it represents.

Diagrams shall be updated when meaningful changes invalidate them.

---

# Rationale

Visual representations are particularly effective for communicating:

- Relationships
- Dependencies
- Boundaries
- Sequences
- Data flows
- State transitions
- Deployment topology

However, diagrams become harmful when they attempt to represent every implementation detail.

The objective is useful abstraction—not visual completeness.

---

# Diagram Philosophy

## Diagrams Explain Structure

Architecture diagrams should communicate system structure.

They should not attempt to replace source code.

---

## Diagrams Explain Relationships

The most valuable information in many diagrams is not the individual component but the relationship between components.

Examples:

```text
Service A
    ↓
API
    ↓
Service B
```

or:

```text
User
 ↓
Frontend
 ↓
Backend
 ↓
Database
```

---

## Diagrams Explain Behavior

Sequence and workflow diagrams should communicate how a system behaves over time.

---

## Diagrams Explain Boundaries

Architecture diagrams should make ownership and responsibility boundaries visible.

---

# Diagram Types

AEOS recognizes several diagram categories.

## Context Diagrams

Show the system and its relationship to external actors and systems.

Useful for answering:

> What is inside and outside the system boundary?

---

## Container / System Diagrams

Show major deployable or independently meaningful system components.

Useful for answering:

> What are the major parts of the system?

---

## Component Diagrams

Show the major internal components of a system.

Useful for answering:

> How is a subsystem organized?

---

## Sequence Diagrams

Show interactions over time.

Useful for:

- API workflows
- Authentication
- Event processing
- Distributed transactions
- Failure scenarios

---

## Data Flow Diagrams

Show how information moves through systems.

Useful for:

- Data processing
- Security analysis
- Integration design
- Privacy analysis

---

## Deployment Diagrams

Show deployment topology.

Useful for:

- Environments
- Infrastructure
- Network boundaries
- Runtime relationships

---

## State Diagrams

Show lifecycle transitions.

Useful for:

- Orders
- Policies
- Payments
- Jobs
- Workflow states

---

## Domain Models

Show important domain concepts and relationships.

Useful for communicating business structure.

---

# Diagram Selection

The diagram type shall be selected according to the question being answered.

| Question | Recommended Model |
|---|---|
| What surrounds the system? | Context |
| What are the major system parts? | Container |
| How is a subsystem structured? | Component |
| How does a workflow execute? | Sequence |
| How does information move? | Data Flow |
| Where does software run? | Deployment |
| How does an entity change state? | State |
| What business concepts exist? | Domain Model |

The goal is communication, not diagram quantity.

---

# Diagram Scope

Every diagram should define its scope.

Examples:

```text
System Level
Service Level
Module Level
Workflow Level
Infrastructure Level
Domain Level
```

A diagram should not mix unrelated abstraction levels without a clear reason.

---

# Diagram Abstraction

Diagrams should show the minimum information necessary to communicate their purpose.

Avoid:

- Every class
- Every database column
- Every method
- Every configuration property

unless the diagram specifically exists for that level of detail.

---

# Diagram Naming

Diagram titles shall communicate their subject.

Preferred:

```text
Policy Issuance — System Context
Policy Issuance — Sequence
Production Deployment Architecture
```

Avoid:

```text
Architecture
Diagram
Flow
New Diagram
```

---

# Diagram Legend

A legend should be included when symbols or notation may be ambiguous.

Notation shall remain consistent within a diagram set.

---

# Diagram Relationships

Where multiple diagrams describe the same system, they should be consistent.

For example:

```text
Context Diagram
       ↓
Container Diagram
       ↓
Component Diagram
       ↓
Sequence Diagram
```

A service appearing in one architectural model should not silently contradict another.

---

# Diagram Source Format

Where practical, diagrams should be stored in a source-controlled, text-based representation.

Preferred technologies may include:

- Mermaid
- PlantUML
- Structurizr
- Graphviz
- Other repository-compatible declarative formats

Binary-only diagrams should be avoided when an equivalent source representation is practical.

---

# Diagram Rendering

Rendered diagrams should be reproducible from their source.

Where possible:

```text
Diagram Source
      ↓
Automated Rendering
      ↓
Published Diagram
```

This reduces manual synchronization errors.

---

# Diagram Version Control

Diagram source shall be version controlled with the documentation and code it describes.

Important architectural changes should result in corresponding diagram changes.

---

# Diagram Review

Diagrams shall be reviewed when:

- Architecture changes.
- Major integrations change.
- Deployment topology changes.
- Ownership changes.
- Important workflows change.

---

# Diagram Accuracy

A diagram shall not intentionally represent a system state known to be incorrect.

If a diagram is intentionally conceptual or aspirational, it shall be clearly labeled.

Examples:

```text
Current Architecture
Target Architecture
Proposed Architecture
```

---

# Current vs Target Architecture

The distinction between current and future architecture is mandatory when both exist.

Never present a planned architecture as though it already exists.

---

# Architecture Models and Decisions

Important architectural diagrams should reference related decisions.

For example:

```text
Architecture Diagram
        ↓
ADR-007
        ↓
Decision Rationale
```

This preserves both structure and reasoning.

---

# Diagrams and Operations

Deployment and operational diagrams should reflect actual environments where operational decisions depend on them.

Examples:

- Production topology
- Network flows
- Dependency relationships
- Failover paths

---

# Diagrams and Security

Security-sensitive diagrams may communicate:

- Trust boundaries
- Data flows
- Authentication paths
- Authorization boundaries
- External exposure

Security diagrams shall follow information classification requirements.

---

# Diagram Maintenance

When an implementation change invalidates a diagram, the diagram shall be updated as part of the same engineering change where practical.

Documentation debt shall not be intentionally accumulated.

---

# AI Guidance

AI shall:

- Inspect existing diagrams before creating new ones.
- Reuse established notation.
- Identify diagrams affected by architecture changes.
- Avoid creating duplicate diagrams.
- Distinguish current and proposed architecture.
- Validate relationships against repository evidence.
- Update diagram source when appropriate.

AI shall not invent architecture merely to produce a visually complete diagram.

---

# Human Responsibilities

Humans remain responsible for:

- Architectural truth
- Diagram intent
- Major modeling decisions
- Notation conventions
- Approval of significant architecture representations

AI may assist with generation and maintenance.

---

# Mandatory Rules

Diagrams shall:

- Have a defined purpose.
- Have an appropriate scope.
- Identify current vs target state where relevant.
- Be maintained with meaningful architecture changes.
- Avoid knowingly false representations.
- Use understandable notation.
- Be reviewable.

---

# Recommended Practices

Prefer text-based diagram sources.

Keep diagrams focused.

Use consistent notation.

Link diagrams to related documentation.

Use diagrams to answer specific engineering questions.

Automate rendering where practical.

---

# Prohibited Practices

Do not:

- Maintain intentionally misleading diagrams.
- Create diagrams without purpose.
- Mix incompatible abstraction levels without explanation.
- Present target architecture as current.
- Maintain binary-only diagrams when source representation is practical.
- Duplicate architecture diagrams unnecessarily.

---

# Allowed Exceptions

Temporary exploratory diagrams may be created without full governance during design exploration.

Before becoming authoritative architecture documentation, they shall satisfy applicable documentation standards.

---

# Success Metrics

| Metric | Target |
|---|---:|
| Critical Architecture Diagrams Current | 100% |
| Broken Diagram References | 0 |
| Current/Target Ambiguity | 0 |
| Major Architecture Changes Reflected | 100% |
| Diagram Ownership Coverage | 100% |
| Reproducible Diagram Sources | ≥95% |

---

# Review Checklist

Reviewers shall verify:

- Purpose is clear.
- Scope is appropriate.
- Abstraction level is appropriate.
- Relationships are accurate.
- Current/target state is explicit.
- Notation is understandable.
- Related decisions are referenced.
- Source representation is maintained.
- Diagram reflects current architecture.

---

# Examples

## Good

```text
Production Architecture

Internet
   ↓
Cloudflare
   ↓
Frontend
   ↓
Backend API
   ↓
PostgreSQL
```

The diagram communicates a specific deployment relationship.

---

## Poor

```text
Frontend
Backend
Database
Kafka
Redis
Cloud
User
API
Network
Security
```

The diagram lists technologies without communicating meaningful relationships.

---

# Anti-patterns

Architecture Wallpaper

Diagram Dumping

Stale Architecture

Target-as-Current

Visual Noise

Binary-Only Architecture

Unowned Diagrams

Diagram Without Purpose

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

---

# Engineering Decision

Diagrams and models are first-class engineering artifacts.

They shall communicate architecture, behavior, relationships, boundaries, and system structure at appropriate levels of abstraction.

Visual documentation shall remain accurate, purposeful, maintainable, and synchronized with engineering reality.

---

# References

- C4 Model
- UML
- arc42
- Mermaid
- PlantUML
- Structurizr

---

# Related Documents

- Documentation Philosophy
- Documentation Architecture
- Documentation Standards
- API Documentation
- Decision Documentation
- Architecture Principles