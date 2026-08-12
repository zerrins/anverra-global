---
document: Separation of Concerns
id: AEC-ARC-005
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-001
  - AEC-ARC-002
  - AEC-ARC-004
---

# Purpose

Define the principle of Separation of Concerns as a mandatory architectural rule for the Anverra Global platform.

Separation of Concerns ensures that each architectural component owns a single, well-defined responsibility.

A clear separation of responsibilities improves maintainability, testability, readability, scalability, and long-term evolution.

---

# Intent

Every software component should have one primary reason to change.

When responsibilities are mixed, software becomes difficult to understand, test, modify, and extend.

Separation of Concerns reduces accidental complexity by assigning every responsibility to its appropriate architectural location.

---

# Problem Statement

Large enterprise systems frequently accumulate technical debt because unrelated responsibilities become coupled.

Typical symptoms include:

- Controllers containing business logic.
- Services performing persistence.
- Repositories enforcing business rules.
- UI components implementing business calculations.
- Infrastructure making business decisions.
- AI-generated code placing logic wherever it is easiest.

These violations reduce modularity and increase maintenance cost.

---

# Architectural Decision

Every responsibility shall have a clearly defined owner.

Responsibilities shall never overlap unless explicitly documented.

The architecture separates concerns into logical layers, modules, and business capabilities.

---

# Separation Dimensions

The architecture separates concerns across multiple dimensions.

## Business

Business capabilities own business behavior.

Examples:

- Customer
- Policy
- Product
- Commission

---

## Architectural

Responsibilities are separated into:

- Domain
- Application
- Adapter
- Infrastructure

---

## Technical

Technology concerns remain isolated.

Examples:

- Persistence
- Messaging
- Security
- Logging
- Monitoring
- Configuration

---

## Operational

Deployment, monitoring, scaling, and infrastructure remain separate from business behavior.

---

# Responsibility Matrix

## Domain

Responsible for:

- Business rules
- Aggregates
- Entities
- Value Objects
- Domain Events

Not responsible for:

- REST
- Database
- Messaging
- Security configuration
- HTTP

---

## Application

Responsible for:

- Use Cases
- Commands
- Queries
- Transactions
- Coordination

Not responsible for:

- Business policies
- Database implementation
- Framework configuration

---

## Adapter

Responsible for:

- Protocol translation
- Request mapping
- Response mapping
- External integrations

Not responsible for:

- Business decisions

---

## Infrastructure

Responsible for:

- Spring
- Database
- Kafka
- Redis
- Configuration
- Security implementation
- Observability

Not responsible for:

- Business rules

---

# Mandatory Rules

Every responsibility has one owner.

Business rules belong in the Domain.

Controllers coordinate requests.

Repositories persist data.

Adapters translate protocols.

Infrastructure supports implementation.

Shared utilities shall remain technical.

---

# Recommended Practices

Keep responsibilities explicit.

Review responsibilities during design.

Move duplicated responsibilities into their proper owner.

Prefer composition over mixed responsibilities.

Document ownership.

---

# Prohibited Practices

Do not mix business rules with infrastructure.

Do not calculate business decisions inside controllers.

Do not place persistence logic inside Aggregates.

Do not duplicate business validation.

Do not hide responsibilities inside utility classes.

---

# Allowed Exceptions

Very small technical utilities may perform multiple closely related technical tasks.

Migration utilities may temporarily combine concerns when isolated.

Exceptions shall be documented.

---

# AI Guidance

Before generating code, AI shall determine:

- What responsibility is being implemented?
- Which architectural layer owns it?
- Which module owns it?

AI shall refuse to mix concerns merely to reduce implementation effort.

---

# Implementation Guidance

For every feature:

1. Identify responsibilities.
2. Assign ownership.
3. Validate ownership.
4. Implement.
5. Review responsibility boundaries.

---

# Review Checklist

Reviewers shall verify:

- Does every responsibility have a single owner?
- Is business logic isolated?
- Are infrastructure concerns separated?
- Are responsibilities duplicated?
- Does each class have one primary reason to change?

---

# Examples

Good

Customer Aggregate validates registration.

Application Service coordinates workflow.

Repository persists Customer.

Controller exposes REST.

---

Bad

CustomerController:

- validates business rules
- calculates commissions
- saves entities
- sends emails

---

# Anti-patterns

God Class

Fat Controller

Smart Repository

Utility Everything

Mixed Responsibilities

---

# Engineering Decision

Responsibility ownership shall always be explicit.

Mixed responsibilities require architectural review.

---

# Related Documents

- Hexagonal Architecture
- Dependency Direction
- Module Boundaries