---
document: API Design
id: AEC-DEV-007
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-009
  - AEC-DEV-002
  - AEC-DEV-006
---

# Purpose

Define the constitutional standards for designing APIs within the Anverra Global platform.

APIs represent long-lived contracts between software components, external systems, mobile applications, web applications, partners, and future services.

A well-designed API should remain understandable, stable, discoverable, secure, and backward compatible throughout its lifecycle.

---

# Intent

APIs expose business capabilities—not implementation details.

Every API shall be designed around the business domain using clear contracts that remain stable as internal implementations evolve.

Consumers should depend on API contracts without needing knowledge of internal architecture.

---

# Problem Statement

Poor API design leads to:

- Breaking integrations
- Tight coupling
- Inconsistent naming
- Duplicate endpoints
- Leaky abstractions
- Difficult versioning
- Security vulnerabilities
- Poor developer experience
- Reduced API discoverability

Once an API is published, changing it becomes significantly more expensive than changing internal implementation.

---

# Development Decision

Anverra Global adopts **Contract-First API Design**.

Every public API shall be designed as a business contract before implementation begins.

Internal implementation shall conform to the contract rather than defining it.

---

# Rationale

APIs are among the most stable artifacts within a software platform.

Business logic, databases, frameworks, and deployment models may change.

API contracts should remain stable whenever possible.

Well-designed APIs:

- simplify integrations,
- improve client development,
- reduce maintenance cost,
- support future evolution,
- improve AI-generated clients and servers.

---

# Why This Matters to AI

AI frequently generates APIs that:

- expose database models,
- leak persistence details,
- use inconsistent naming,
- ignore versioning,
- mix transport and business concerns.

This document ensures AI generates APIs that reflect business capabilities and remain maintainable.

---

# API Design Principles

Every API shall be:

- Business-oriented
- Consistent
- Predictable
- Discoverable
- Stable
- Secure
- Versionable
- Backward compatible where practical

---

# API Ownership

Every API belongs to exactly one business capability.

Examples:

- Customer API
- Policy API
- Commission API
- Identity API

No API shall span unrelated business capabilities.

Ownership follows the Business Capability Ownership principle.

---

# Contract-First Design

Before implementation:

1. Identify the business capability.
2. Define the API contract.
3. Define request and response models.
4. Define error responses.
5. Define authorization requirements.
6. Review the contract.
7. Implement.

Implementation shall not drive API design.

---

# Resource Design

Resources shall represent business concepts.

Examples:

```
/customers
/policies
/claims
/products
/commissions
```

Avoid technical resources.

Examples to avoid:

```
/database
/entity
/object
/record
/data
```

---

# Naming Conventions

Use plural nouns for collections.

Use business terminology.

Avoid verbs in resource names.

Good

```
GET /customers

POST /policies

GET /claims/{claimId}
```

Poor

```
/createCustomer

/getPolicy

/processCommission
```

Actions that do not naturally map to CRUD may be represented using sub-resources or commands when justified.

---

# Request Models

Requests shall:

- contain only required information,
- validate inputs,
- avoid internal identifiers where possible,
- use meaningful property names.

Transport models shall remain independent from domain entities.

---

# Response Models

Responses shall:

- expose business information,
- hide internal implementation,
- remain stable,
- avoid persistence-specific fields.

Do not expose JPA entities, database identifiers that have no business meaning, or internal state.

---

# Error Responses

Every API shall return a consistent error model.

Minimum fields:

- errorCode
- message
- correlationId
- timestamp

Optional fields:

- fieldErrors
- documentationLink
- retryable

Stack traces shall never be returned to clients.

---

# Idempotency

Operations shall be idempotent whenever business semantics allow.

Examples:

- PUT should be idempotent.
- DELETE should be idempotent where practical.

For non-idempotent operations (such as payment processing), explicit idempotency mechanisms shall be provided when duplicate requests are possible.

---

# Pagination

Endpoints returning collections shall support pagination.

Recommended parameters:

```
page
size
sort
```

Avoid returning unbounded result sets.

---

# Filtering

Filtering shall be explicit.

Example:

```
GET /policies?status=ACTIVE
```

Avoid opaque filtering mechanisms.

---

# Sorting

Sorting parameters shall be documented and validated.

Only approved fields may be sortable.

---

# Versioning

Breaking API changes require versioning.

Preferred strategy:

```
/api/v1
/api/v2
```

Alternative strategies require architectural approval.

Versioning shall preserve consumer stability.

---

# Security

Every API shall define:

- authentication
- authorization
- input validation
- rate limiting (where applicable)
- audit requirements
- sensitive data handling

Security is part of API design—not an implementation detail.

---

# Performance

APIs should:

- minimize payload size,
- avoid unnecessary round trips,
- support pagination,
- support caching where appropriate,
- avoid N+1 retrieval patterns.

Performance optimization shall never compromise readability or correctness.

---

# Observability

Every API shall support:

- correlation identifiers,
- structured logging,
- metrics,
- tracing,
- audit events where required.

---

# Mandatory Rules

APIs expose business capabilities.

Contracts precede implementation.

Internal implementation shall remain hidden.

API contracts shall be documented.

Breaking changes require versioning.

Consistent error models are mandatory.

Pagination is required for collections.

Security requirements shall be explicit.

---

# Recommended Practices

Keep APIs simple.

Prefer predictable behavior.

Design for discoverability.

Use business terminology.

Keep responses minimal.

Document every public endpoint.

Review APIs before implementation.

---

# Prohibited Practices

Do not expose database entities.

Do not expose internal implementation.

Do not return inconsistent error models.

Do not break contracts without versioning.

Do not use transport-specific terminology in business APIs.

Do not leak stack traces.

Do not expose sensitive information.

---

# Allowed Exceptions

Internal development APIs may simplify certain conventions when isolated from production consumers.

Administrative APIs may expose additional operational information when properly secured.

Exceptions shall be documented and reviewed.

---

# AI Guidance

Before generating an API, AI shall determine:

- Which business capability owns the API?
- Does an existing contract already exist?
- Is versioning required?
- Are request and response models business-oriented?
- Are security requirements defined?
- Are error responses consistent?

AI shall never generate APIs directly from database schemas.

AI shall optimize for long-term contract stability.

---

# Implementation Guidance

Implementation sequence:

1. Define business capability.
2. Design contract.
3. Review contract.
4. Define validation.
5. Define authorization.
6. Implement Application Service.
7. Implement Adapter.
8. Test contract.
9. Publish documentation.

---

# Review Checklist

Reviewers shall verify:

- Is the API business-oriented?
- Is ownership clear?
- Is the contract documented?
- Are request and response models appropriate?
- Are errors consistent?
- Is versioning handled correctly?
- Are security requirements satisfied?
- Is backward compatibility preserved?
- Does the API hide internal implementation?

---

# Examples

## Good

```
GET /customers/{customerId}

POST /policies

GET /claims?status=OPEN
```

Business-oriented, predictable, resource-based.

---

## Bad

```
/createCustomer

/processPolicy

/getAllData

/saveEntity
```

Implementation-oriented, inconsistent, and not aligned with REST principles.

---

# Anti-patterns

Database-Driven APIs

Entity Exposure

RPC over REST

Leaky Contracts

Breaking Changes Without Versioning

Chatty APIs

God Endpoints

Inconsistent Error Models

Technology-Oriented Resources

---

# Engineering Decision

API contracts are constitutional assets.

They shall evolve deliberately, remain stable, and reflect business capabilities rather than implementation details.

---

# References

- Roy Fielding — Architectural Styles and the Design of Network-based Software Architectures
- Martin Fowler — Richardson Maturity Model
- Microsoft REST API Guidelines
- Google API Design Guide

---

# Related Documents

- Architecture First
- Explicit Contracts
- Error Handling
- Business Capability Ownership
- Domain Implementation
- Backward Compatibility
- Development Review Checklist