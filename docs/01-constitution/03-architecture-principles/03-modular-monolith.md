---
document: Modular Monolith
id: AEC-ARC-003
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-001
  - AEC-ARC-002
---

# Purpose

Define the Modular Monolith as the primary architectural style for the Anverra Global platform.

This document establishes the principles, constraints, responsibilities, and evolution strategy for organizing business capabilities within a single deployable application.

The Modular Monolith is the architectural foundation of the backend platform.

---

# Intent

Anverra Global is expected to evolve into a large enterprise application.

The architecture must therefore:

- remain understandable,
- evolve safely,
- avoid unnecessary operational complexity,
- support independent business capabilities,
- allow future extraction when justified.

The Modular Monolith provides these characteristics while avoiding the operational cost of premature microservices.

---

# Problem Statement

Enterprise systems frequently suffer from one of two architectural extremes.

## Extreme 1

Large monolithic applications with no internal boundaries.

Typical symptoms include:

- shared business logic,
- circular dependencies,
- duplicated code,
- poor ownership,
- difficult testing,
- difficult maintenance.

---

## Extreme 2

Premature microservices.

Typical symptoms include:

- distributed transactions,
- excessive operational complexity,
- duplicated infrastructure,
- difficult debugging,
- network latency,
- eventual consistency problems,
- increased deployment overhead.

Neither extreme supports the long-term goals of Anverra Global.

---

# Architectural Decision

Anverra Global adopts a **Modular Monolith** architecture.

The application shall be deployed as a single executable application.

Internally, it shall consist of independent business modules.

Each module behaves as though it could become an independent service in the future.

Modules communicate through explicit contracts rather than implementation details.

---

# Why Not Microservices?

Microservices are not rejected.

They are postponed.

The architecture shall first optimize for:

- business correctness,
- maintainability,
- developer productivity,
- operational simplicity.

Extraction into independent services shall occur only when business or operational requirements justify it.

---

# Rationale

The Modular Monolith provides:

- Simple deployment
- Simple debugging
- Strong consistency
- Shared transactions where appropriate
- Easier testing
- Lower infrastructure cost
- Faster development
- Clear module ownership
- Easier refactoring

while preserving clear architectural boundaries.

---

# Core Principles

The platform consists of business modules.

Modules own business capabilities.

Modules expose contracts.

Modules hide implementation.

Modules evolve independently.

Modules communicate intentionally.

---

# Module Characteristics

Every module shall:

- own one primary business capability,
- contain its own domain model,
- contain its own application layer,
- contain its own adapters,
- own its own persistence,
- own its own business rules.

Modules shall never become collections of unrelated functionality.

---

# Business Capability Ownership

Each business capability has exactly one owner.

Examples include:

- Identity
- Organization
- Customer
- Product
- Policy
- Commission
- Notification
- Reporting

Ownership shall never overlap.

---

# Internal Structure

Every module follows the same internal architecture.

Example

```
Module

Domain

Application

Adapters

Configuration

Tests
```

Consistency is mandatory.

---

# Module Communication

Modules communicate only through:

- Application Services
- Published Events
- Explicit Interfaces

Modules shall never access another module's internal implementation.

---

# Shared Code

Only technical infrastructure may be shared.

Examples include:

- Security
- Logging
- Framework configuration
- Common utilities
- Infrastructure abstractions

Business logic shall never move into shared libraries merely because multiple modules require similar functionality.

If business logic appears duplicated, the business model shall be reviewed before extracting shared code.

---

# Dependencies

Dependencies shall always point toward stable abstractions.

Business modules shall not depend directly upon one another's implementation.

Platform components shall not own business behavior.

Dependency direction shall remain explicit.

---

# Data Ownership

Every module owns its own business data.

Modules shall not directly update another module's persistence.

Cross-module collaboration occurs through contracts or events.

Data ownership is explained in detail within the Data Ownership principle.

---

# Evolution Strategy

Modules shall be designed so that future extraction into independent services is possible.

However:

Future extraction shall never influence today's implementation unnecessarily.

The architecture optimizes for current business needs while preserving future flexibility.

---

# Mandatory Rules

Every module shall own exactly one business capability.

Every module shall expose explicit contracts.

Every module shall hide internal implementation.

No cyclic dependencies.

No shared business logic.

No direct database access across module boundaries.

Business rules remain inside their owning module.

Modules shall remain independently understandable.

---

# Recommended Practices

Keep modules cohesive.

Keep public interfaces small.

Prefer event-driven collaboration where practical.

Keep module dependencies minimal.

Review module boundaries regularly.

Use meaningful business names.

Avoid generic "common business" modules.

---

# Prohibited Practices

Do not create utility modules containing unrelated business logic.

Do not expose internal repositories.

Do not expose internal entities.

Do not bypass module APIs.

Do not access another module's database tables.

Do not introduce cyclic dependencies.

Do not organize modules around technical layers.

Do not create "shared domain" modules.

---

# Allowed Exceptions

Very small supporting modules with minimal business behavior may expose lightweight interfaces.

Migration utilities may temporarily access multiple modules under controlled conditions.

Such exceptions shall be documented and removed when no longer required.

---

# AI Guidance

Before generating code, AI shall identify:

- owning business capability,
- owning module,
- existing public contracts,
- required integrations,
- dependency direction.

AI shall generate new functionality inside the appropriate module.

AI shall not create new modules unless justified by a distinct business capability.

AI shall never place business logic inside the Platform module.

AI shall never access another module's persistence directly.

---

# Implementation Guidance

Implementation sequence:

1. Identify business capability.
2. Locate owning module.
3. Extend domain model.
4. Extend application layer.
5. Extend adapters.
6. Publish events if necessary.
7. Update tests.

Never begin by modifying infrastructure.

---

# Review Checklist

Reviewers shall verify:

- Does the feature belong to the correct module?
- Is ownership clear?
- Are module boundaries respected?
- Are dependencies explicit?
- Are contracts used correctly?
- Is shared code technical rather than business?
- Does implementation introduce coupling?
- Could this module evolve independently?

---

# Examples

Good

Customer module validates customer registration.

Notification module sends notifications.

Customer module publishes CustomerRegistered event.

Notification module reacts independently.

Responsibilities remain separated.

---

Bad

Customer module inserts rows into Notification tables.

Policy module modifies Customer persistence.

Commission module queries Product repositories directly.

These violate module ownership.

---

# Anti-patterns

Big Ball of Mud

God Module

Shared Business Module

Distributed Monolith

Circular Dependencies

Framework-Centric Modules

Database-Driven Modules

Feature Leakage

---

# Engineering Decision

The Modular Monolith is the default architectural style for Anverra Global.

Alternative deployment models require explicit architectural approval.

Business capability boundaries take precedence over technical convenience.

---

# References

- Simon Brown — Modular Monoliths
- Martin Fowler — Monolith First
- Vaughn Vernon — Implementing Domain-Driven Design

---

# Related Documents

- Architecture First
- Domain Driven Design
- Hexagonal Architecture
- Module Boundaries
- Business Capability Ownership
- Dependency Direction
- Data Ownership
- Architecture Review