---
document: Business Capability Ownership
id: AEC-ARC-008
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-002
  - AEC-ARC-003
---

# Purpose

Define ownership rules for business capabilities.

Every business capability shall have one architectural owner.

Ownership establishes accountability, consistency, and architectural integrity.

---

# Intent

Business responsibilities should never be ambiguous.

If multiple modules own the same business concept, consistency cannot be guaranteed.

Ownership removes ambiguity.

---

# Problem Statement

Shared ownership often results in:

- Duplicate business rules
- Inconsistent validation
- Data synchronization issues
- Circular dependencies
- Conflicting implementations

---

# Architectural Decision

Each business capability shall be owned by exactly one module.

Examples include:

- Identity
- Organization
- Customer
- Product
- Policy
- Commission
- Notification
- Reporting

Ownership shall be explicit and documented.

---

# Ownership Responsibilities

The owning module is responsible for:

- Business rules
- Validation
- Persistence
- Aggregate lifecycle
- Business events
- Public contracts

No other module shall assume these responsibilities.

---

# Ownership Rules

Ownership includes:

- Creation
- Modification
- Validation
- Deletion
- Lifecycle
- Business policies

Ownership does not imply exclusive usage.

Other modules may consume the capability through approved contracts.

---

# Mandatory Rules

Exactly one owner.

No duplicate ownership.

No shared validation.

No external persistence updates.

Business behavior remains within the owning module.

---

# Recommended Practices

Document ownership.

Review ownership during design.

Avoid splitting cohesive capabilities.

---

# Prohibited Practices

Do not duplicate business rules.

Do not create "shared ownership."

Do not modify another module's aggregates.

Do not bypass ownership through database access.

---

# Allowed Exceptions

Read-only projections for reporting are permitted.

Integration adapters may transform data without assuming ownership.

---

# AI Guidance

AI shall identify the owning module before generating code.

If ownership is unclear, AI shall request clarification.

AI shall never generate duplicate business behavior.

---

# Implementation Guidance

Every feature shall begin by identifying the owning business capability.

Implementation shall extend that module.

Cross-capability workflows shall use contracts or events.

---

# Review Checklist

- Is ownership explicit?
- Is there exactly one owner?
- Are business rules centralized?
- Does another module duplicate ownership?
- Are ownership boundaries respected?

---

# Examples

Good

Policy Module owns policy issuance.

Commission Module calculates commissions using Policy contracts.

---

Bad

Commission Module modifies Policy status.

---

# Anti-patterns

Shared Ownership

Duplicate Validation

Business Rule Replication

Cross-Module Updates

---

# Engineering Decision

Business capability ownership is mandatory.

Ownership conflicts require architectural resolution before implementation.

---

# Related Documents

- Module Boundaries
- Domain-Driven Design
- Explicit Contracts