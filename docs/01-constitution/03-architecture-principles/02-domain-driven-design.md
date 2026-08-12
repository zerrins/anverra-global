---
document: Domain Driven Design
id: AEC-ARC-002
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-001
  - AEC-ENG-001
---

# Purpose

Establish Domain-Driven Design (DDD) as the primary architectural approach for modeling and implementing the Anverra Global platform.

DDD ensures that software reflects the business domain rather than technical infrastructure.

Business concepts, terminology, rules, and responsibilities shall drive software structure.

---

# Intent

Software exists to solve business problems.

Business concepts are significantly more stable than frameworks, databases, programming languages, and infrastructure technologies.

By modeling software around business capabilities instead of technical concerns, the system remains understandable, maintainable, and capable of evolving over long periods.

DDD is therefore adopted as the architectural language of the platform.

---

# Problem Statement

Large enterprise systems frequently become difficult to maintain because they evolve around technical constructs instead of business concepts.

Common symptoms include:

- Database tables becoming business models.
- Controllers containing business logic.
- Services owning unrelated responsibilities.
- Business rules duplicated across multiple modules.
- Framework annotations leaking into domain logic.
- AI-generated code following framework conventions rather than business architecture.

These issues reduce maintainability and make future evolution increasingly expensive.

---

# Architectural Decision

Anverra Global adopts Domain-Driven Design as the primary modeling approach.

The software architecture shall be organized around business capabilities.

Every significant business concept shall have an explicit representation within the domain model.

Business terminology shall be consistent across:

- Documentation
- Source Code
- APIs
- Database Design
- AI Skills
- User Interfaces

---

# Rationale

Domain Driven Design provides:

- Better alignment between business and engineering.
- Clear ownership of responsibilities.
- Reduced accidental complexity.
- Improved maintainability.
- Easier communication between business and engineering teams.
- Stable architecture despite technology changes.
- Better AI reasoning because concepts map directly to the business domain.

---

# Core Principles

The architecture shall model the business domain rather than the database.

Business language shall define software terminology.

Business capabilities define module boundaries.

Business rules belong in the Domain Layer.

Technology shall support the domain rather than dictate it.

The domain model is the authoritative representation of business knowledge.

---

# Domain Building Blocks

## Bounded Context

A bounded context defines the boundary within which a particular business model is valid.

Each bounded context:

- owns its terminology,
- owns its business rules,
- owns its persistence,
- owns its APIs,
- owns its evolution.

No bounded context shall directly manipulate another bounded context's internal implementation.

---

## Aggregate

An Aggregate represents a consistency boundary.

Every Aggregate:

- protects business invariants,
- owns its internal entities,
- controls state transitions,
- validates business rules.

External modules communicate only with the Aggregate Root.

---

## Aggregate Root

The Aggregate Root:

- protects aggregate consistency,
- exposes business behavior,
- prevents invalid state,
- controls access to child entities.

No external object shall directly modify internal entities.

---

## Entity

Entities possess identity.

Their identity remains constant while their attributes may change.

Entities represent long-lived business concepts.

Examples:

- Customer
- Policy
- Organization
- Insurance Product

---

## Value Object

Value Objects represent descriptive information.

Characteristics:

- immutable,
- equality by value,
- no identity,
- replaceable.

Examples:

- Address
- Money
- Premium
- Percentage
- Date Range

---

## Domain Service

Domain Services contain business behavior that does not naturally belong to a single Aggregate.

They coordinate domain concepts without becoming procedural transaction scripts.

---

## Repository

Repositories provide access to Aggregates.

Repositories are part of the Domain abstraction.

Persistence technology belongs outside the Domain.

---

## Factory

Factories create complex Aggregates while ensuring invariants are satisfied.

Object construction shall not bypass business validation.

---

## Domain Event

A Domain Event represents a business fact that has already occurred.

Examples:

- CustomerRegistered
- PolicyIssued
- CommissionCalculated

Events describe completed business actions.

---

# Ubiquitous Language

The platform shall maintain a single business vocabulary shared by:

- Product Owners
- Business Analysts
- Architects
- Engineers
- QA Engineers
- AI Agents

Names appearing in documentation should appear consistently in source code.

Avoid translating business terminology into technical terminology.

Business language has precedence.

---

# Mandatory Rules

Business logic belongs inside the Domain Layer.

Every business capability shall own its Aggregates.

Aggregates protect business invariants.

Entities shall expose business behavior instead of public mutable state.

Value Objects shall be immutable.

Repositories shall return Aggregates.

Infrastructure shall never contain business rules.

Controllers shall never contain business logic.

Persistence models shall not become domain models.

Business terminology shall remain consistent.

---

# Recommended Practices

Prefer rich domain models over procedural services.

Model business behavior explicitly.

Represent business concepts using meaningful names.

Keep Aggregates small.

Prefer composition over inheritance.

Use Domain Events for cross-module communication.

Refactor continuously as understanding improves.

Collaborate with domain experts frequently.

---

# Prohibited Practices

Do not model the database before the business domain.

Do not expose mutable entities publicly.

Do not place business rules inside controllers.

Do not duplicate business logic.

Do not bypass Aggregate Roots.

Do not create "God Services."

Do not allow infrastructure concerns inside the Domain Layer.

Do not leak ORM annotations into business concepts unless explicitly approved by architecture standards.

---

# Allowed Exceptions

Simple CRUD reference data with no meaningful business behavior may use simplified domain models.

Read-only reporting models may bypass rich domain behavior when no business invariants exist.

Temporary migration code may use procedural approaches provided it is isolated and removed after migration.

Every exception shall be documented.

---

# AI Guidance

Before generating any implementation, AI shall identify:

- Business capability
- Bounded Context
- Aggregate
- Aggregate Root
- Entities
- Value Objects
- Domain Services
- Domain Events
- Repositories

AI shall generate code using business terminology.

AI shall never invent domain concepts.

AI shall never generate technical names where business terminology exists.

If business concepts are unclear, AI shall request clarification before implementation.

Generated code shall preserve Aggregate consistency.

---

# Implementation Guidance

Implementation order should follow:

1. Business Capability
2. Bounded Context
3. Aggregate
4. Aggregate Root
5. Value Objects
6. Entities
7. Domain Services
8. Domain Events
9. Repository
10. Application Layer
11. Adapters

Implementation shall never begin from the database schema.

---

# Review Checklist

Reviewers shall verify:

- Is the business capability clearly identified?
- Does the Aggregate protect invariants?
- Is business terminology consistent?
- Is business logic inside the Domain Layer?
- Are Value Objects immutable?
- Are repositories returning Aggregates?
- Are module boundaries respected?
- Are Domain Events meaningful business facts?
- Does implementation follow ubiquitous language?
- Is technical infrastructure isolated?

---

# Examples

Good

Customer Aggregate

- register()
- updateAddress()
- deactivate()

Business behavior is encapsulated.

---

Bad

CustomerService

```
saveCustomer()

updateCustomer()

deleteCustomer()

approveCustomer()

calculatePremium()

sendNotification()
```

Multiple unrelated responsibilities indicate poor domain modeling.

---

# Anti-patterns

- Transaction Script
- Anemic Domain Model
- God Service
- Database Driven Design
- CRUD First Development
- Framework Driven Domain Model
- Shared Domain Objects
- Leaky Repository
- Mutable Value Objects

---

# Engineering Decision

When Domain-Driven Design conflicts with framework conventions, Domain-Driven Design takes precedence.

Business architecture is always more important than framework convenience.

---

# References

- Eric Evans — Domain-Driven Design
- Vaughn Vernon — Implementing Domain-Driven Design

---

# Related Documents

- Architecture First
- Modular Monolith
- Hexagonal Architecture
- Module Boundaries
- Business Capability Ownership
- Data Ownership
- Architecture Review