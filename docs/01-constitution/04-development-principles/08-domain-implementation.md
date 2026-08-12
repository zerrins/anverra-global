---
document: Domain Implementation
id: AEC-DEV-008
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-002
  - AEC-ARC-004
  - AEC-DEV-003
---

# Purpose

Define the implementation standards for Domain-Driven Design (DDD) within the Anverra Global platform.

This document translates the architectural principles defined in the Domain-Driven Design Constitution into concrete implementation practices.

Every engineer and AI agent shall implement business logic using these standards.

---

# Intent

Business logic is the most valuable part of the software system.

It should remain:

- independent,
- expressive,
- maintainable,
- testable,
- technology-agnostic.

The Domain Layer represents the business—not the database, framework, or infrastructure.

---

# Problem Statement

Many systems claim to use DDD while implementing business logic incorrectly.

Typical problems include:

- Business logic inside controllers.
- Validation inside repositories.
- Database entities treated as domain models.
- Transaction scripts instead of rich domain models.
- Infrastructure annotations inside domain classes.
- Anemic domain models.
- Business rules duplicated across services.

These practices weaken the domain model and reduce maintainability.

---

# Development Decision

The Anverra Global platform shall implement Domain-Driven Design using rich domain models within the Hexagonal Architecture.

Business behavior shall be encapsulated inside Aggregates and Domain Services.

The Domain Layer shall remain independent of all frameworks and infrastructure.

---

# Rationale

A rich domain model:

- protects business invariants,
- centralizes business rules,
- reduces duplication,
- improves readability,
- simplifies testing,
- supports long-term evolution.

Business logic should remain where business experts expect to find it.

---

# Why This Matters to AI

Without explicit guidance, AI often generates:

- Anemic entities.
- Fat services.
- Controllers containing business logic.
- Persistence-driven domain models.
- Generic CRUD implementations.

These patterns violate the architectural principles of AEOS.

This document ensures AI consistently produces rich, business-oriented domain models.

---

# Domain Implementation Principles

The Domain Layer shall:

- model business behavior,
- protect invariants,
- express business terminology,
- avoid infrastructure dependencies,
- remain framework-independent.

The Domain Layer shall never become a persistence layer.

---

# Aggregate Implementation

Aggregates are the primary consistency boundary.

Each Aggregate shall:

- own its internal state,
- validate business rules,
- expose business behavior,
- prevent invalid state transitions.

Example

```
Policy

issue()

cancel()

renew()

expire()
```

Business operations belong inside the Aggregate.

---

# Aggregate Root

Every Aggregate has exactly one Aggregate Root.

Responsibilities include:

- controlling access,
- enforcing invariants,
- coordinating internal entities,
- publishing domain events.

External components shall communicate only with the Aggregate Root.

---

# Entity Implementation

Entities represent business concepts with identity.

Examples:

- Customer
- Policy
- Organization
- Claim

Entities shall:

- expose behavior,
- avoid public mutable state,
- protect invariants,
- use business terminology.

Entities shall not become DTOs.

---

# Value Object Implementation

Value Objects represent descriptive business concepts.

Characteristics:

- immutable,
- equality by value,
- self-validating,
- side-effect free.

Examples:

```
Money

Address

EmailAddress

Percentage

PolicyNumber
```

Validation belongs inside the Value Object.

---

# Domain Services

Domain Services represent business behavior that naturally spans multiple Aggregates.

Examples:

```
CommissionCalculator

PremiumCalculator

PolicyEligibilityService
```

Domain Services shall not become procedural transaction scripts.

---

# Repository Interfaces

Repositories belong to the Domain.

Repositories expose business-oriented operations.

Examples:

```
findByPolicyNumber()

findActivePolicies()

existsCustomer()

save()
```

Repositories shall not expose persistence-specific behavior.

---

# Repository Implementations

Implementations belong to Infrastructure.

Examples:

```
JpaPolicyRepository

CosmosPolicyRepository

RedisCustomerCache
```

Business logic shall never appear inside repository implementations.

---

# Factory Implementation

Factories create complex Aggregates.

Factories shall:

- validate construction,
- preserve invariants,
- simplify creation,
- hide construction complexity.

Factories are optional.

Simple Aggregates may use constructors directly.

---

# Domain Events

Domain Events represent completed business facts.

Examples:

```
CustomerRegistered

PolicyIssued

ClaimApproved

CommissionCalculated
```

Events shall:

- be immutable,
- use business terminology,
- represent completed actions,
- avoid implementation details.

---

# Specifications

Specifications encapsulate reusable business rules.

Examples:

```
EligibleForRenewalSpecification

PolicyIssuableSpecification
```

Specifications should be used when rules become:

- reusable,
- complex,
- independently testable.

---

# Validation Placement

Validation belongs in different places depending on responsibility.

Boundary validation

↓

Application

Business validation

↓

Domain

Persistence validation

↓

Infrastructure

Validation shall not be duplicated.

---

# Transaction Boundaries

Transactions belong to the Application Layer.

Aggregates should not manage transactions.

Business consistency remains the responsibility of the Aggregate.

---

# Domain Model Purity

The Domain Layer shall not depend upon:

- Spring
- JPA
- Hibernate
- Kafka
- Redis
- SQL
- HTTP
- REST
- JSON
- Cloud SDKs

Technology belongs outside the Domain.

---

# Mandatory Rules

Business behavior belongs inside Aggregates.

Entities expose behavior.

Value Objects remain immutable.

Repositories remain interfaces.

Infrastructure implements repositories.

Business terminology shall remain consistent.

Framework annotations are prohibited inside the Domain Layer.

---

# Recommended Practices

Model business concepts explicitly.

Keep Aggregates cohesive.

Use expressive Value Objects.

Prefer behavior over getters/setters.

Raise Domain Events.

Protect invariants.

Continuously refine the domain model.

---

# Prohibited Practices

Do not create Anemic Domain Models.

Do not expose mutable business state.

Do not inject repositories into entities.

Do not place business rules inside controllers.

Do not treat database tables as Aggregates.

Do not use entities as API DTOs.

Do not expose persistence annotations throughout the Domain Layer.

---

# Allowed Exceptions

Simple reference data with no business behavior may use lightweight domain models.

Read-only projections may bypass Aggregates when business consistency is not affected.

Migration utilities may temporarily simplify domain implementation when isolated.

Exceptions shall be documented.

---

# AI Guidance

Before generating domain code, AI shall identify:

- Bounded Context
- Aggregate
- Aggregate Root
- Entities
- Value Objects
- Domain Services
- Domain Events
- Repositories

AI shall implement business behavior inside the Domain.

AI shall never generate framework dependencies inside domain classes.

AI shall preserve business invariants.

---

# Implementation Guidance

Recommended implementation order:

1. Model business capability.
2. Define Aggregate.
3. Define Value Objects.
4. Define Entities.
5. Define Domain Events.
6. Define Repository Interfaces.
7. Implement Application Services.
8. Implement Infrastructure Adapters.
9. Add automated tests.

Implementation shall always begin from the business model—not from the database schema.

---

# Review Checklist

Reviewers shall verify:

- Are Aggregates cohesive?
- Are invariants protected?
- Is business behavior inside the Domain?
- Are Value Objects immutable?
- Are repositories interfaces?
- Are infrastructure dependencies absent?
- Are Domain Events meaningful?
- Is business terminology consistent?
- Is the Domain Layer framework-independent?

---

# Examples

## Good

```
Policy.issue()

↓

Validate business rules

↓

Raise PolicyIssued event

↓

Persist Aggregate
```

Business behavior remains encapsulated.

---

```
Money.add()

Money.subtract()
```

Behavior belongs inside the Value Object.

---

## Bad

```
PolicyController

↓

Issue policy

↓

Calculate premium

↓

Persist entity

↓

Publish Kafka event
```

Business logic has escaped the Domain Layer.

---

```
CustomerEntity

getters

setters

no behavior
```

Anemic Domain Model.

---

# Anti-patterns

Anemic Domain Model

Transaction Script

God Service

CRUD Domain

Database-Driven Design

Entity as DTO

Framework-Centric Domain

Leaky Repository

Shared Business Logic

---

# Engineering Decision

Business logic shall remain inside the Domain Layer.

The Domain Model is the authoritative representation of business behavior.

Frameworks, persistence technologies, and infrastructure shall adapt to the Domain—not the reverse.

---

# References

- Eric Evans — Domain-Driven Design
- Vaughn Vernon — Implementing Domain-Driven Design
- Alistair Cockburn — Hexagonal Architecture
- Robert C. Martin — Clean Architecture

---

# Related Documents

- Domain-Driven Design
- Hexagonal Architecture
- Modular Monolith
- SOLID Principles
- Defensive Programming
- Error Handling
- Refactoring
- Development Review Checklist