---
document: Event Driven Collaboration
id: AEC-ARC-010
version: 1.0.0
status: Draft
stability: Level 4
owner: Architecture
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-ARC-003
  - AEC-ARC-007
  - AEC-ARC-009
---

# Purpose

Define how business modules collaborate through business events while preserving module autonomy.

---

# Intent

Modules should communicate through business facts rather than implementation details.

Publishing events allows modules to evolve independently while remaining synchronized around significant business occurrences.

---

# Problem Statement

Direct synchronous communication between modules often leads to:

- Tight coupling
- Hidden dependencies
- Cascading failures
- Reduced flexibility
- Difficult evolution

---

# Architectural Decision

Business modules shall prefer event-driven collaboration when another module only needs to react to a completed business action.

Events describe facts that have already occurred.

---

# Event Characteristics

Business events shall be:

- Immutable
- Past tense
- Business oriented
- Explicitly versioned when required

Examples:

- CustomerRegistered
- PolicyIssued
- CommissionCalculated
- PaymentReceived

---

# Event Ownership

The module that owns the business capability owns the event.

Only the owning module publishes that event.

---

# Event Consumers

Consumers react independently.

Consumers shall not modify the publisher's business state.

---

# Mandatory Rules

- Events represent completed business facts.
- Events are immutable.
- Events use business terminology.
- Publishers never know consumers.
- Consumers remain optional.

---

# Recommended Practices

- Publish meaningful events.
- Keep payloads concise.
- Include identifiers rather than entire object graphs.
- Prefer asynchronous collaboration where appropriate.

---

# Prohibited Practices

- Publishing technical events.
- Using events as remote procedure calls.
- Mutating published events.
- Publishing implementation details.

---

# Allowed Exceptions

Simple synchronous interactions are acceptable when no long-term coupling is introduced.

---

# AI Guidance

AI shall identify opportunities for event-driven collaboration when multiple modules react independently to the same business action.

AI shall not replace direct business workflows with events unnecessarily.

---

# Implementation Guidance

1. Complete business transaction.
2. Raise domain event.
3. Publish integration event if required.
4. Consumers process independently.

---

# Review Checklist

- Is this a business fact?
- Is ownership correct?
- Is the event immutable?
- Is coupling reduced?
- Are consumers independent?

---

# Anti-patterns

- Event as Command
- Chatty Events
- Technical Events
- Shared Mutable Events

---

# Engineering Decision

Events communicate facts, not requests.

---

# Related Documents

- Explicit Contracts
- Business Capability Ownership
- Modular Monolith