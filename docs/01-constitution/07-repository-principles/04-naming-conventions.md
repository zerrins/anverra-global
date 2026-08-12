---
document: Naming Conventions
id: AEC-REP-004
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
  - AEC-REP-003
---

# Purpose

Define the constitutional naming standards for repositories, modules, packages, source code, APIs, databases, infrastructure, documentation, and engineering artifacts within the Anverra Engineering Operating System (AEOS).

Naming is an architectural concern.

Consistent naming improves:

- Readability
- Discoverability
- Communication
- Maintainability
- AI comprehension
- Engineering consistency

Every identifier shall communicate intent rather than implementation.

---

# Intent

Names should describe business meaning.

A reader should understand **what something represents** without reading its implementation.

Business language always takes precedence over technical terminology.

Names shall remain:

- Consistent
- Explicit
- Predictable
- Descriptive
- Stable

---

# Problem Statement

Poor naming commonly results in:

- Ambiguous responsibilities
- Duplicate implementations
- Confusing APIs
- Difficult code reviews
- Hidden business meaning
- Increased onboarding effort
- AI misunderstanding repository intent

Naming inconsistency is a long-term source of engineering debt.

---

# Repository Decision

Every engineering artifact shall follow standardized naming conventions defined by AEOS.

Naming conventions are mandatory across all repositories.

---

# Rationale

Engineers spend far more time reading software than writing it.

Clear names reduce cognitive load and improve communication between:

- Engineers
- Architects
- QA Engineers
- DevOps Engineers
- Product Teams
- AI Agents

Naming should explain the domain without requiring implementation knowledge.

---

# Naming Philosophy

Names describe intent.

Names shall never describe implementation details unless required.

Prefer:

```
PolicyRenewalService
```

over

```
PolicyRenewalManagerImpl
```

The first communicates purpose.

The second communicates technology.

Purpose always takes precedence.

---

# General Naming Principles

Every name shall be:

## Business-Oriented

Use business terminology.

Example

```
Policy
Claim
Commission
Customer
Invoice
```

Avoid technical jargon.

---

## Explicit

Avoid abbreviations.

Good

```
NotificationService
```

Poor

```
NotifSvc
```

---

## Consistent

Use identical terminology throughout the repository.

If the business uses "Policy", do not introduce:

```
Contract
Agreement
InsurancePolicy
```

without explicit business justification.

---

## Predictable

Similar concepts shall follow identical naming patterns.

Examples

```
CreatePolicyCommand

UpdatePolicyCommand

DeletePolicyCommand
```

---

## Stable

Names should survive implementation changes.

Avoid technology-specific names.

---

# Repository Naming

Repository names shall:

- use lowercase
- use hyphens
- describe business capability

Examples

Good

```
policy-service

customer-service

commission-engine

identity-provider
```

Poor

```
service1

backend-new

project

spring-api

java-service
```

---

# Folder Naming

Folders shall:

- use lowercase
- avoid spaces
- avoid abbreviations
- describe responsibility

Examples

```
customer

policy

claims

billing

documentation
```

Avoid

```
misc

tmp

common2

stuff

new
```

---

# Package Naming

Packages shall:

- follow reverse-domain notation where applicable
- reflect business capabilities
- remain hierarchical

Example

```
com.anverra.policy.application

com.anverra.policy.domain

com.anverra.policy.infrastructure
```

Package names should never expose framework details.

---

# Module Naming

Modules shall represent business capabilities.

Examples

```
policy

customer

claims

billing

authentication
```

Technology shall not determine module names.

---

# Class Naming

Classes shall use PascalCase.

Names should represent responsibilities.

Examples

```
PolicyAggregate

CustomerRepository

ClaimValidator

CommissionCalculator

RenewPolicyCommand
```

Avoid

```
Manager

Helper

Utility

Processor

Handler2

DataObject
```

Generic names hide intent.

---

# Interface Naming

Interfaces should describe capability rather than implementation.

Examples

```
PolicyRepository

NotificationSender

PaymentGateway
```

Avoid unnecessary prefixes.

Poor

```
IPolicyRepository
```

---

# Method Naming

Methods shall describe behavior.

Use verbs.

Examples

```
createPolicy()

renewPolicy()

calculateCommission()

publishEvent()

validateCustomer()
```

Avoid

```
process()

execute()

run()

handle()

doTask()
```

Behavior should be obvious.

---

# Variable Naming

Variables should describe business meaning.

Good

```
policyNumber

renewalDate

customerId

commissionRate
```

Poor

```
x

obj

tmp

data

value
```

Single-letter names are acceptable only for short-lived loop variables or mathematical expressions.

---

# Constant Naming

Constants shall:

- use UPPER_SNAKE_CASE
- remain immutable

Examples

```
DEFAULT_TIMEOUT

MAX_RETRY_COUNT

DEFAULT_PAGE_SIZE
```

---

# Enumeration Naming

Enums shall represent business concepts.

Example

```
PolicyStatus

ClaimType

CommissionType
```

Enum values should be singular and descriptive.

```
ACTIVE

EXPIRED

SUSPENDED
```

---

# Event Naming

Events represent completed business actions.

Examples

```
PolicyIssued

PolicyRenewed

ClaimSubmitted

PaymentReceived
```

Avoid technical events.

```
PolicyUpdatedEvent2
```

---

# API Naming

REST resources should use plural nouns.

Examples

```
/customers

/policies

/claims
```

Operations should rely on HTTP semantics.

Avoid verbs in URLs.

Poor

```
/createPolicy

/updatePolicy

/deletePolicy
```

---

# Database Naming

Tables

```
customers

policies

claims
```

Primary Keys

```
customer_id

policy_id
```

Foreign Keys

```
customer_id

policy_id
```

Indexes

```
idx_policy_number

idx_customer_email
```

Constraints

```
fk_policy_customer

uk_policy_number
```

---

# Configuration Naming

Configuration keys shall follow hierarchical notation.

Examples

```
server.port

database.connection.timeout

messaging.kafka.bootstrap-servers
```

Avoid inconsistent separators.

---

# Test Naming

Tests should describe expected behavior.

Examples

```
shouldRenewPolicyWhenPolicyIsActive()

shouldRejectExpiredPolicy()

shouldCalculateCommissionCorrectly()
```

Test names should read like business rules.

---

# Documentation Naming

Documentation shall use descriptive names.

Examples

```
README.md

architecture-overview.md

policy-domain.md

deployment-guide.md
```

Avoid

```
notes.md

temp.md

document1.md
```

---

# AI Naming Guidance

AI shall:

- Preserve existing business terminology.
- Never introduce inconsistent synonyms.
- Prefer explicit names over short names.
- Recommend renaming when clarity improves.
- Respect repository naming conventions.

---

# Mandatory Rules

Names shall:

- Represent business meaning.
- Remain consistent.
- Avoid unnecessary abbreviations.
- Avoid implementation details.
- Follow constitutional standards.
- Use the project's ubiquitous language.

---

# Recommended Practices

Prefer nouns for domain concepts.

Prefer verbs for behaviors.

Prefer explicitness over brevity.

Review names during code reviews.

Refactor unclear names.

Maintain naming consistency across repositories.

---

# Prohibited Practices

Do not use:

```
Helper

Manager

Utils

Misc

Stuff

Temp

Data

Object

Processor

Handler2
```

Do not encode framework names into business concepts.

Do not use inconsistent terminology.

Do not create multiple names for the same business concept.

---

# Allowed Exceptions

Industry-standard abbreviations may be used when universally understood.

Examples

```
API

URL

JWT

UUID

HTTP

SQL
```

Such abbreviations should remain consistent.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Naming Consistency | 100% |
| Business Terminology Usage | 100% |
| Generic Class Names | 0 |
| Ambiguous Identifiers | 0 |
| Repository Naming Compliance | 100% |

---

# Review Checklist

Reviewers shall verify:

- Do names represent business meaning?
- Are naming conventions consistent?
- Are generic names avoided?
- Is ubiquitous language preserved?
- Are APIs clearly named?
- Are tests descriptive?
- Are database objects consistently named?
- Can AI infer responsibility from names?

---

# Examples

## Good

```
CustomerAggregate

IssuePolicyCommand

CommissionCalculator

PolicyRepository

ClaimSubmitted
```

---

## Poor

```
Manager

Utils

Handler

Processor

CommonService

Helper

Object

Data
```

---

# Anti-patterns

Abbreviation Overload

Technology-Driven Names

Generic Object Naming

Inconsistent Terminology

Implementation-Oriented Naming

Hungarian Notation

Meaningless Prefixes

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

Names shall communicate business intent rather than implementation details.

Every engineering artifact shall follow a consistent, business-oriented naming convention that improves readability, maintainability, collaboration, and AI-assisted development.

---

# References

- Engineering Constitution
- Domain-Driven Design
- Clean Code
- Clean Architecture
- Team Topologies

---

# Related Documents

- Repository Philosophy
- Folder Structure
- Module Organization
- Development Principles
- Architecture Principles
- AI Engineering Principles