---
document: Module Boundaries
id: AEC-ARC-007
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-002
  - AEC-ARC-003
  - AEC-ARC-006
---

# Purpose

Define the architectural boundaries of business modules within the Anverra Global platform.

A module boundary establishes ownership, encapsulation, and interaction rules for a business capability.

---

# Intent

Business modules shall be autonomous units of business functionality.

Each module exposes only what is necessary and protects its internal implementation from external consumers.

Clear module boundaries reduce coupling, improve maintainability, and enable independent evolution.

---

# Problem Statement

Without explicit module boundaries:

- Business logic becomes scattered.
- Internal implementation leaks across modules.
- Changes propagate unexpectedly.
- Dependencies increase over time.
- The Modular Monolith gradually becomes a traditional monolith.

---

# Architectural Decision

Every business capability shall be implemented within a dedicated module.

Each module shall define a clear public interface and hide all internal implementation details.

A module boundary is both a logical and architectural contract.

---

# Boundary Definition

Every module owns:

- Business rules
- Domain model
- Application services
- Persistence
- Domain events
- Validation
- Internal workflows

Modules shall not expose these internal details directly.

---

# Public Surface

A module may expose:

- Public application services
- Published domain events
- Public DTOs (when required)
- Explicit APIs

Everything else is private.

---

# Internal Components

The following remain internal:

- Entities
- Aggregate implementation
- Repository implementations
- Infrastructure configuration
- Internal validation logic
- Database schema

---

# Mandatory Rules

Every module has one business purpose.

Internal implementation is private.

Cross-module access occurs only through public contracts.

Module internals shall never be referenced directly.

Business rules remain within the owning module.

---

# Recommended Practices

Keep public interfaces small.

Hide implementation details.

Review module boundaries before introducing dependencies.

Prefer event publication over exposing internal behavior.

---

# Prohibited Practices

Do not expose repositories.

Do not expose entities.

Do not expose persistence models.

Do not bypass module APIs.

Do not share internal packages.

---

# Allowed Exceptions

Shared technical libraries may expose reusable technical abstractions.

Migration utilities may temporarily access multiple modules when documented.

---

# AI Guidance

Before generating new functionality, AI shall determine:

- Which module owns the capability?
- Does the required public interface already exist?
- Is a new contract required?

AI shall never access another module's internal implementation.

---

# Implementation Guidance

New functionality shall extend the owning module rather than introducing cross-module behavior.

If multiple modules require changes, interaction shall occur through contracts or events.

---

# Review Checklist

Reviewers shall verify:

- Is ownership clear?
- Are boundaries respected?
- Is implementation hidden?
- Are only approved interfaces exposed?
- Are internal classes referenced externally?

---

# Examples

Good

Customer Module publishes CustomerRegistered event.

Notification Module reacts independently.

---

Bad

Customer Module calls NotificationRepository directly.

---

# Anti-patterns

Shared Internal Packages

Cross-Module Repository Access

Leaky Modules

Hidden Coupling

---

# Engineering Decision

Module boundaries are mandatory architectural constraints and shall not be bypassed.

---

# Related Documents

- Modular Monolith
- Business Capability Ownership
- Explicit Contracts
- Dependency Direction