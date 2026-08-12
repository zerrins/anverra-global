---
document: Dependency Direction
id: AEC-ARC-006
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-004
  - AEC-ARC-005
---

# Purpose

Define the dependency rules governing all software components within the Anverra Global platform.

Dependencies determine how software components interact and evolve.

Incorrect dependency direction introduces coupling and architectural erosion.

---

# Intent

Dependencies shall always point toward stable business abstractions.

Technology depends upon business.

Business never depends upon technology.

Stable concepts shall never depend upon unstable concepts.

---

# Problem Statement

Poor dependency management causes:

- Circular references
- Framework coupling
- Business logic leakage
- Difficult testing
- Hidden dependencies
- Tight coupling

These problems significantly reduce maintainability.

---

# Architectural Decision

The platform adopts the Dependency Rule.

Source code dependencies always point inward toward the Domain.

Business dependencies always point toward stable abstractions.

No architectural layer may depend upon a less stable layer.

---

# Dependency Hierarchy

```
Infrastructure

↓

Adapters

↓

Application

↓

Domain
```

The Domain has no outward dependencies.

---

# Module Dependencies

Modules communicate through:

- Public interfaces
- Domain Events
- Application Services

Never through implementation details.

---

# Stable vs Unstable Components

Stable

- Domain
- Business concepts
- Contracts
- Value Objects

Unstable

- Frameworks
- Databases
- Messaging systems
- Cloud SDKs
- External APIs

Stable components shall never depend on unstable components.

---

# Mandatory Rules

Domain depends on nothing.

Application depends upon Domain.

Adapters depend upon Application.

Infrastructure depends upon all required abstractions.

No cyclic dependencies.

No reverse dependencies.

Dependencies shall be explicit.

---

# Recommended Practices

Depend upon interfaces.

Keep dependency graphs simple.

Review dependencies regularly.

Prefer events over direct calls for cross-module collaboration.

---

# Prohibited Practices

Do not inject repositories into Domain objects.

Do not expose framework types inside the Domain.

Do not allow modules to bypass contracts.

Do not create circular module references.

Do not depend upon implementation classes.

---

# Allowed Exceptions

Development tooling may reference implementation details when isolated from production code.

Testing utilities may simplify dependency wiring.

---

# AI Guidance

AI shall verify dependency direction before generating code.

AI shall reject implementations that:

- create circular dependencies,
- expose infrastructure to the Domain,
- violate module ownership.

Whenever uncertain, AI shall depend upon an abstraction rather than an implementation.

---

# Implementation Guidance

Before introducing a dependency:

1. Determine ownership.
2. Determine stability.
3. Verify architectural layer.
4. Check for an existing abstraction.
5. Validate dependency direction.

---

# Review Checklist

Reviewers shall verify:

- Are dependencies pointing inward?
- Does the Domain remain independent?
- Are interfaces used correctly?
- Are framework dependencies isolated?
- Are module dependencies explicit?
- Are cycles present?

---

# Examples

Good

REST Controller

↓

Application Service

↓

Domain

↓

Repository Interface

↓

JPA Repository

---

Bad

Domain

↓

Spring Service

↓

Repository Implementation

↓

Database

---

# Anti-patterns

Circular Dependencies

Framework-Centric Domain

Shared Mutable Dependencies

Implementation Coupling

Database-Driven Architecture

---

# Engineering Decision

Dependency direction is mandatory.

Violations require architectural approval.

---

# Related Documents

- Hexagonal Architecture
- Separation of Concerns
- Module Boundaries
- Explicit Contracts