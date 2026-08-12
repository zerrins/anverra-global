---
document: Hexagonal Architecture
id: AEC-ARC-004
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-001
  - AEC-ARC-002
  - AEC-ARC-003
---

# Purpose

Establish Hexagonal Architecture (Ports and Adapters) as the standard architectural pattern for implementing all backend business modules within Anverra Global.

Hexagonal Architecture isolates business logic from technology concerns, allowing the domain model to evolve independently of frameworks, databases, messaging systems, and external integrations.

---

# Intent

Business logic is the most valuable and stable part of the software system.

Technologies evolve continuously:

- Frameworks change.
- Databases change.
- Messaging platforms change.
- Cloud providers change.
- User interfaces change.

Business rules generally evolve much more slowly.

The architecture shall therefore ensure that business logic remains independent of implementation technologies.

---

# Problem Statement

Many enterprise applications become tightly coupled to frameworks and infrastructure.

Typical symptoms include:

- Domain classes annotated with framework-specific annotations.
- Business rules implemented inside controllers.
- Services directly calling database repositories.
- Business logic depending on REST APIs.
- Domain objects aware of messaging systems.
- Testing requiring full framework startup.

These dependencies make software difficult to evolve and expensive to maintain.

---

# Architectural Decision

Anverra Global adopts **Hexagonal Architecture (Ports and Adapters)** for all backend business modules.

The Domain Layer is the center of the architecture.

All technology-specific concerns exist outside the Domain.

Dependencies always point inward toward the Domain.

No dependency shall point outward from the Domain to infrastructure.

---

# Rationale

Hexagonal Architecture provides:

- Clear separation of concerns.
- Independent business logic.
- High testability.
- Technology independence.
- Easier refactoring.
- Better maintainability.
- Better AI reasoning.
- Reduced framework coupling.

---

# Core Principles

The domain owns business behavior.

Infrastructure supports the domain.

Frameworks are implementation details.

Business logic shall remain independent of technology.

Dependencies always point toward the center of the architecture.

---

# Architectural Layers

Every business module follows the same logical structure.

```
Domain

↓

Application

↓

Adapters

↓

Infrastructure
```

Each layer has distinct responsibilities.

---

# Domain Layer

The Domain Layer contains business knowledge.

Typical contents include:

- Aggregates
- Entities
- Value Objects
- Domain Services
- Domain Events
- Repository Interfaces
- Business Policies
- Specifications
- Factory Interfaces

The Domain Layer shall not depend upon:

- Spring
- JPA
- REST
- Kafka
- Redis
- SQL
- HTTP
- Cloud SDKs

The Domain Layer is pure business code.

---

# Application Layer

The Application Layer orchestrates business use cases.

Responsibilities include:

- Commands
- Queries
- Use Cases
- Transaction orchestration
- Authorization checks
- DTO mapping
- Coordination between Aggregates

The Application Layer shall not contain business rules already owned by the Domain.

---

# Adapter Layer

Adapters connect the Application Layer with external systems.

Examples include:

Inbound:

- REST Controllers
- Message Consumers
- Scheduled Jobs
- GraphQL
- CLI

Outbound:

- Database
- Kafka
- Redis
- External APIs
- Email
- File Storage

Adapters translate external protocols into application requests.

---

# Infrastructure Layer

Infrastructure provides technical implementations.

Examples include:

- Spring Configuration
- Repository Implementations
- Database Configuration
- Kafka Configuration
- Redis Configuration
- Security Configuration
- Monitoring
- Logging
- Dependency Injection

Infrastructure supports the application but never owns business behavior.

---

# Ports

Ports define stable interfaces.

Inbound Ports

Represent application capabilities exposed to external actors.

Outbound Ports

Represent external capabilities required by the application.

Examples:

- CustomerRepository
- NotificationSender
- PaymentGateway
- IdentityProvider

Ports belong to the Application or Domain depending on ownership.

---

# Adapters

Adapters implement Ports.

Examples:

```
CustomerRepository

↓

JpaCustomerRepository
```

```
NotificationSender

↓

EmailNotificationAdapter
```

```
PaymentGateway

↓

StripeAdapter
```

Adapters are replaceable.

Business logic should remain unchanged when adapters are replaced.

---

# Dependency Direction

Dependencies shall always flow inward.

```
REST

↓

Application

↓

Domain
```

Never:

```
Domain

↓

REST
```

---

# Package Structure

Every module shall follow the same package organization.

```
domain/

application/

adapter/
    inbound/
    outbound/

configuration/
```

Additional packages may exist only when justified.

---

# Mandatory Rules

The Domain Layer shall not depend upon Spring.

The Domain Layer shall not contain infrastructure code.

Controllers shall invoke Application Services.

Repositories shall be interfaces inside the Domain.

Repository implementations belong to outbound adapters.

Business rules belong inside Aggregates or Domain Services.

Technology shall never dictate business structure.

---

# Recommended Practices

Prefer constructor injection.

Prefer immutable Value Objects.

Keep Application Services thin.

Keep Adapters simple.

Keep Domain rich.

Use interfaces to isolate external dependencies.

Treat frameworks as replaceable.

---

# Prohibited Practices

Do not annotate Domain classes with REST annotations.

Do not inject repositories into controllers.

Do not implement business rules inside controllers.

Do not expose JPA entities directly through APIs.

Do not allow adapters to call one another directly.

Do not allow infrastructure to own business decisions.

Do not place SQL inside business logic.

---

# Allowed Exceptions

Simple technical utilities may exist outside the layered structure.

Temporary migration adapters may bypass normal layering when approved and documented.

Testing utilities may simplify certain interactions without affecting production architecture.

---

# AI Guidance

Before generating code, AI shall determine:

- Which layer owns the responsibility.
- Whether a Port already exists.
- Whether a new Adapter is required.
- Whether business logic belongs in the Domain.
- Whether orchestration belongs in the Application Layer.

AI shall never:

- Generate Spring dependencies inside the Domain.
- Place business rules inside Adapters.
- Access infrastructure directly from the Domain.
- Create repositories inside controllers.

Generated package structures shall always follow the approved architecture.

---

# Implementation Guidance

For every new business capability:

1. Extend the Domain model.
2. Define or reuse Ports.
3. Implement Application Services.
4. Create required Adapters.
5. Configure Infrastructure.
6. Add automated tests.
7. Verify dependency direction.

Framework configuration should be the final implementation step.

---

# Review Checklist

Reviewers shall verify:

- Does the Domain depend only on business concepts?
- Are dependencies pointing inward?
- Are Ports properly defined?
- Are Adapters thin?
- Does the Application Layer orchestrate rather than own business rules?
- Are controllers free of business logic?
- Are repository implementations outside the Domain?
- Is the package structure consistent?
- Are external integrations isolated?

---

# Examples

Good

```
CustomerAggregate

↓

CustomerApplicationService

↓

CustomerRepository

↓

JpaCustomerRepository
```

Business logic remains independent.

---

Bad

```
CustomerController

↓

JpaRepository

↓

Business Logic

↓

Database
```

Business rules become tightly coupled to infrastructure.

---

# Anti-patterns

Layered Architecture with leaking responsibilities

Transaction Script

Fat Controllers

Anemic Domain with procedural services

Framework-Centric Domain

Database-Centric Architecture

Direct SQL from Controllers

Repository Leakage

Infrastructure-Driven Design

---

# Engineering Decision

Hexagonal Architecture is mandatory for backend business modules.

Alternative implementation styles require explicit architectural approval.

No implementation convenience shall justify violating dependency direction.

---

# References

- Alistair Cockburn — Hexagonal Architecture
- Vaughn Vernon — Implementing Domain-Driven Design
- Robert C. Martin — Clean Architecture

---

# Related Documents

- Architecture First
- Domain-Driven Design
- Modular Monolith
- Separation of Concerns
- Dependency Direction
- Module Boundaries
- Explicit Contracts
- Architecture Review