---
document: Backward Compatibility
id: AEC-DEV-010
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-DEV-007
  - AEC-ARC-009
---

# Purpose

Define the engineering principles governing backward compatibility across APIs, events, modules, databases, and integrations.

Backward compatibility protects existing consumers from unnecessary disruption and enables the platform to evolve safely.

---

# Intent

Software should evolve without forcing existing consumers to change unnecessarily.

Breaking changes are expensive and shall occur only with explicit architectural approval.

---

# Problem Statement

Breaking compatibility causes:

- Integration failures
- Client upgrades
- Deployment coordination
- Downtime
- Increased support effort
- Reduced engineering confidence

---

# Development Decision

Backward compatibility is the default engineering strategy.

Breaking compatibility requires:

- architectural review,
- versioning,
- migration strategy,
- consumer communication.

---

# Rationale

Stable contracts reduce operational risk.

Consumers should upgrade because they choose to—not because they are forced to.

---

# Why This Matters to AI

AI frequently replaces existing implementations without considering compatibility.

AI shall always preserve public contracts unless explicitly instructed otherwise.

---

# Compatibility Scope

Applies to:

- REST APIs
- Event Contracts
- Public Interfaces
- Database Migrations
- Configuration
- SDKs

---

# Mandatory Rules

- Public contracts remain compatible.
- Existing fields shall not be removed.
- Existing behavior shall not change unexpectedly.
- Breaking changes require versioning.
- Database migrations shall be backward compatible whenever practical.

---

# Recommended Practices

- Prefer additive changes.
- Deprecate before removal.
- Support migration periods.
- Document breaking changes.

---

# Prohibited Practices

- Silent breaking changes.
- Renaming public fields.
- Changing semantics.
- Reusing deprecated fields.

---

# AI Guidance

Before modifying existing code, AI shall determine:

- Is this a public contract?
- Will existing consumers break?
- Is versioning required?

---

# Review Checklist

- Is compatibility preserved?
- Is versioning appropriate?
- Is migration documented?
- Are consumers protected?

---

# Engineering Decision

Compatibility is the default.

Breaking compatibility is the exception.

---

# Constitutional Compliance Matrix

| Requirement | Status |
|------------|--------|
| API Stability | Mandatory |
| Contract Preservation | Mandatory |
| Versioning | Mandatory |
| Migration Strategy | Mandatory |

---

# Related Documents

- API Design
- Explicit Contracts
- Architecture Review