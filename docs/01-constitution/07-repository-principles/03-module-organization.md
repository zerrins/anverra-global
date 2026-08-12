---
document: Module Organization
id: AEC-REP-003
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
  - AEC-ARC-003
  - AEC-ARC-007
---

# Purpose

Define the constitutional standards governing module organization within repositories managed under the Anverra Engineering Operating System (AEOS).

Modules are the primary organizational unit of software.

Every module represents a cohesive business capability with clearly defined ownership, responsibilities, boundaries, and interfaces.

The objective is to ensure that repositories scale through modularity rather than complexity.

---

# Intent

Software shall be organized around business capabilities rather than technologies.

Modules should:

- encapsulate business behavior,
- minimize coupling,
- maximize cohesion,
- expose explicit interfaces,
- evolve independently,
- remain understandable.

Module organization shall communicate business intent before implementation details.

---

# Problem Statement

Technology-centric repositories often organize software using folders such as:

```
controllers/
services/
repositories/
models/
utils/
```

As applications grow this causes:

- duplicated business logic,
- unclear ownership,
- hidden dependencies,
- circular references,
- poor scalability,
- difficult onboarding,
- architecture erosion.

Business capability boundaries become impossible to identify.

---

# Repository Decision

Every production system shall organize source code by business capability.

Technical layers exist inside business modules rather than becoming the top-level organizational model.

---

# Rationale

Businesses evolve through capabilities.

Software should evolve the same way.

When organization mirrors business capabilities:

- ownership becomes clear,
- changes remain localized,
- architecture remains stable,
- AI agents reason more accurately,
- engineering complexity decreases.

---

# Module Philosophy

A module represents one cohesive business capability.

Examples:

- Customer
- Policy
- Claims
- Commission
- Billing
- Authentication
- Notifications
- Reporting

Modules shall own their business logic.

Modules are engineering products.

---

# Module Principles

Every module shall be:

## Business Focused

A module exists to solve one business problem.

---

## Highly Cohesive

Related responsibilities remain together.

---

## Loosely Coupled

Dependencies between modules are minimized.

---

## Independently Evolvable

A module should evolve without unnecessary changes to unrelated modules.

---

## Explicitly Integrated

Communication between modules occurs through well-defined interfaces.

---

## Self-Contained

Where practical, a module should contain everything required to implement its business capability.

---

# Canonical Module Structure

Every module follows the same internal organization.

```
customer/

├── domain/
│
├── application/
│
├── infrastructure/
│
├── interfaces/
│
├── contracts/
│
├── events/
│
├── configuration/
│
├── documentation/
│
└── tests/
```

This structure applies regardless of programming language.

---

# Layer Responsibilities

## Domain

Contains business rules.

Examples:

- Entities
- Value Objects
- Aggregates
- Domain Services
- Specifications
- Domain Events

The Domain layer has no knowledge of frameworks.

---

## Application

Coordinates use cases.

Responsibilities include:

- Commands
- Queries
- Orchestration
- Transactions
- Application Services

Application contains workflow—not business rules.

---

## Infrastructure

Contains technical implementations.

Examples:

- Database
- Kafka
- Redis
- HTTP Clients
- Persistence
- File Storage

Infrastructure depends inward.

---

## Interfaces

Contains interaction mechanisms.

Examples:

- REST Controllers
- GraphQL
- gRPC
- Messaging Adapters
- CLI
- Scheduled Jobs

Interfaces translate requests into application use cases.

---

## Contracts

Contains externally visible contracts.

Examples:

- DTOs
- API Models
- Events
- Public Interfaces

Contracts define integration boundaries.

---

## Events

Contains integration events.

Examples:

- Published Events
- Consumed Events
- Event Schemas

Events should represent completed business actions.

---

## Configuration

Contains module-specific configuration.

Configuration shall remain isolated.

---

## Documentation

Contains module documentation.

Examples:

- README
- ADRs
- Sequence Diagrams
- Business Rules

Documentation evolves with the module.

---

## Tests

Contains automated tests.

Examples:

- Unit Tests
- Integration Tests
- Contract Tests

Tests are part of the module.

---

# Module Taxonomy

AEOS recognizes the following module categories.

## Core Business Modules

Directly implement business capabilities.

Examples:

- Policy
- Claims
- Commission

---

## Supporting Business Modules

Provide supporting business functionality.

Examples:

- Notifications
- Reporting
- Billing

---

## Platform Modules

Provide technical capabilities.

Examples:

- Authentication
- Authorization
- Audit
- Logging

---

## Shared Modules

Contain reusable components.

Shared modules shall remain minimal.

Business logic shall never migrate into shared modules.

---

# Dependency Rules

Dependencies shall follow this hierarchy.

```
Interfaces

↓

Application

↓

Domain

↑

Infrastructure
```

Business modules communicate only through explicit contracts.

Circular dependencies are prohibited.

---

# Module Ownership

Each module shall have:

- Business Owner
- Technical Owner
- Engineering Team

Ownership shall be documented.

---

# AI Module Discovery

Before modifying a module, AI shall:

1. Read module README.
2. Read module ADRs.
3. Understand business capability.
4. Identify public contracts.
5. Review existing tests.
6. Review dependencies.
7. Identify integration points.
8. Validate architecture.

No implementation begins before module discovery.

---

# Module Communication

Modules communicate through:

- Public APIs
- Domain Events
- Commands
- Queries
- Published Contracts

Direct database access across modules is prohibited.

---

# Mandatory Rules

Modules shall:

- represent business capabilities,
- have explicit ownership,
- expose explicit interfaces,
- remain cohesive,
- minimize coupling,
- include documentation,
- include automated tests,
- preserve architectural boundaries.

---

# Recommended Practices

Prefer small cohesive modules.

Prefer explicit interfaces.

Document module responsibilities.

Review module boundaries regularly.

Measure module coupling.

Keep business language consistent.

---

# Prohibited Practices

Do not organize by framework.

Do not expose internal implementation.

Do not share databases across business modules without explicit justification.

Do not create "common" modules containing unrelated functionality.

Do not duplicate business capabilities.

Do not create circular dependencies.

---

# Allowed Exceptions

Very small applications may temporarily combine closely related capabilities when justified.

Such organization shall remain easy to evolve into separate modules.

---

# AI Guidance

AI shall:

- Identify the affected business capability.
- Preserve module boundaries.
- Reuse existing functionality.
- Avoid introducing cross-module coupling.
- Recommend module extraction when cohesion decreases.

---

# Implementation Guidance

When introducing a new module:

1. Identify the business capability.
2. Define ownership.
3. Create the canonical module structure.
4. Define public contracts.
5. Implement the domain model.
6. Add application services.
7. Implement infrastructure adapters.
8. Add interfaces.
9. Add tests.
10. Document the module.

---

# Success Metrics

| Metric | Target |
|---------|--------|
| Business Capability Alignment | 100% |
| Module Cohesion | High |
| Circular Dependencies | 0 |
| Module Documentation | 100% |
| Module Test Coverage | 100% |
| Public Contracts Documented | 100% |

---

# Review Checklist

Reviewers shall verify:

- Does the module represent one business capability?
- Is ownership defined?
- Are dependencies correct?
- Are boundaries preserved?
- Are contracts explicit?
- Are tests included?
- Is documentation complete?
- Is architecture respected?

---

# Examples

## Good

```
policy/
claims/
commission/
customer/
billing/
```

Each module owns its business logic.

---

## Poor

```
controllers/
services/
helpers/
models/
utils/
```

Business capabilities become scattered across multiple folders.

---

# Anti-patterns

Layer-First Organization

God Module

Shared Business Logic

Circular Dependencies

Technology-Centric Modules

Utility Dumping Ground

Cross-Module Database Access

Framework-Oriented Design

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

Modules are the primary architectural unit of software.

Every module shall represent a cohesive business capability with explicit ownership, clear boundaries, well-defined contracts, and independent evolution.

Business architecture—not technology—determines module organization.

---

# References

- Engineering Constitution
- Domain-Driven Design
- Clean Architecture
- Team Topologies
- Building Evolutionary Architectures

---

# Related Documents

- Repository Philosophy
- Folder Structure
- Naming Conventions
- Business Capability Ownership
- Hexagonal Architecture
- Separation of Concerns
- Domain-Driven Design