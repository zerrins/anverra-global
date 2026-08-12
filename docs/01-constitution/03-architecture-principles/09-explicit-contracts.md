---
document: Explicit Contracts
id: AEC-ARC-009
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-003
  - AEC-ARC-006
  - AEC-ARC-007
---

# Purpose

Define how architectural components communicate through explicit contracts.

Contracts protect module boundaries and reduce coupling.

---

# Intent

Every interaction between modules shall be intentional, discoverable, and stable.

Communication shall never rely on implementation details.

---

# Problem Statement

Implicit communication leads to:

- Hidden dependencies
- Tight coupling
- Breaking changes
- Difficult maintenance
- Unpredictable behavior

---

# Architectural Decision

Modules communicate only through explicit contracts.

Contracts include:

- Application service interfaces
- Published events
- API specifications
- Integration interfaces

Internal implementation is never a contract.

---

# Contract Characteristics

A contract shall be:

- Stable
- Documented
- Versioned when necessary
- Technology independent where practical
- Business focused

---

# Mandatory Rules

Every public interaction shall use a defined contract.

Contracts shall not expose internal entities.

Contracts shall remain backward compatible where practical.

Consumers shall depend on contracts rather than implementations.

---

# Recommended Practices

Keep contracts minimal.

Use business terminology.

Version contracts carefully.

Document behavioral expectations.

---

# Prohibited Practices

Do not expose repositories.

Do not expose persistence models.

Do not expose internal entities.

Do not depend upon implementation classes.

Do not bypass contracts.

---

# Allowed Exceptions

Internal module interactions may use implementation details when remaining within the same module boundary.

---

# AI Guidance

AI shall:

- Search for an existing contract before creating a new one.
- Reuse contracts where appropriate.
- Avoid contract duplication.
- Generate business-oriented interfaces.

---

# Implementation Guidance

Before creating a new interface:

1. Verify ownership.
2. Check for an existing contract.
3. Evaluate reuse.
4. Document the contract.
5. Implement consumers.

---

# Review Checklist

- Is the interaction explicit?
- Is a contract documented?
- Are implementation details hidden?
- Is business terminology used?
- Can contracts evolve independently?

---

# Examples

Good

Customer Module exposes:

CustomerQueryService

Notification Module depends upon that service.

---

Bad

Notification Module imports CustomerRepositoryImpl.

---

# Anti-patterns

Hidden Dependencies

Repository Sharing

Database Sharing

Implementation Coupling

Contract Explosion

---

# Engineering Decision

All cross-module communication shall occur through explicit contracts.

Implementation details are never public APIs.

---

# Related Documents

- Module Boundaries
- Dependency Direction
- Event Driven Collaboration
- Business Capability Ownership