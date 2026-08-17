---
name: decision-council
description: "High-risk design review layer providing multi-agent peer review between technical-design and implementation-planning."
---

# Decision Council

## 1. Purpose

The Decision Council facilitates a structured, multi-agent critique of proposed technical designs. It provides independent, multi-perspective challenges to identify hidden risks, security flaws, and architectural drift before implementation planning begins.

## 2. Scope

The Council validates designs, not code. It acts as an optional safety gate specifically targeting Architectural and high-risk changes.

## 3. Activation Conditions

**Mandatory Triggers:**
- Introduction of a new top-level business module.
- Introduction of cross-module transactions.
- Changes to `OrganizationScope` or core authorization logic.
- Introduction of new external infrastructure or event brokers (e.g., Kafka, SQS).

**Optional Triggers:**
- Complex API contract modifications.
- Refactoring of existing cross-module event payloads.
- Non-trivial cross-domain Reporting projections.

## 4. Prohibited Usage

**Never Trigger For:**
- Bounded tasks (simple bug fixes, isolated feature additions).
- Spikes (exploratory code).
- Routine dependency bumps or documentation updates.
- Scenarios where `architecture-analysis` is incomplete or missing.

## 5. Council Roles

The Council is restricted to a specialized minimum set of perspectives:

### Enterprise Architect (Chair)
- **Review Focus:** Overall alignment with the Engineering Constitution and module architecture.
- **Questions Asked:** Does this violate core architectural principles? Is there a simpler adequate solution?
- **Failure Conditions:** Introduction of an unapproved top-level module (e.g., orchestration) or distributed saga without explicit approval.

### Domain Architect
- **Review Focus:** Aggregate boundaries, domain purity, and event architecture.
- **Questions Asked:** Are infrastructure concerns leaking into the domain? Are cross-module events durable and idempotent?
- **Failure Conditions:** Domain depending on infrastructure; unauthorized cross-module transactions.

### Security Architect
- **Review Focus:** `OrganizationScope`, RBAC, and authorization boundaries.
- **Questions Asked:** Is `OrganizationScope` securely resolved at the application boundary? Can resource constraints be bypassed?
- **Failure Conditions:** JWT treated as the authoritative hierarchy store; missing resource authorization checks.

### Database Architect
- **Review Focus:** D03 compliance, schema isolation, and transaction boundaries.
- **Questions Asked:** Are there cross-schema physical foreign keys? Are migrations sequential? Is optimistic locking correct?
- **Failure Conditions:** Use of `BIGSERIAL`, cross-schema physical FKs, or mapping annotations inside the domain.

### Compliance Reviewer
- **Review Focus:** Policy lifecycle invariants and critical business rules.
- **Questions Asked:** Can a Policy be physically deleted? Is the `UNSET` vs. `ZERO` commission invariant preserved?
- **Failure Conditions:** Allowing logical deletion bypasses; silent conversion of `UNSET` to `ZERO`.

## 6. Evidence Requirements

The Council requires the finalized technical design document, original requirement spec, and repository baseline as input.

Each finding reported by a Council member MUST include:
- **Finding:** Clear description of the issue or validation.
- **Evidence:** Exact reference to the technical design or architecture document.
- **Risk:** The severity of the finding (CRITICAL, HIGH, MEDIUM, LOW).
- **Confidence:** The confidence level (High, Medium, Low).

## 7. Debate Protocol

The Council operates strictly under a maximum 3-round protocol to force consensus and manage token execution context.

### Round 1: Independent Review
Each Council member reviews the technical design in isolation, producing a classified list of `PASS`, `FAIL`, or `CONCERN` findings based strictly on their specific domain.

*Early Stopping Condition: If Round 1 results in a unanimous `PASS`, or a unanimous CRITICAL `FAIL`, the debate stops immediately.*

### Round 2: Challenge Assumptions
Members review the findings of their peers. (e.g., the Database Architect might challenge the Domain Architect's proposed transaction boundary). The goal is to resolve `CONCERN` items into `PASS` or `FAIL`.

### Round 3: Chair Synthesis
The Enterprise Architect evaluates all finalized findings and synthesizes the ultimate decision.

## 8. Disagreement Handling

If a `FAIL` remains disputed after Round 2, the Chair logs it as a "Dissenting Opinion" and the Council state automatically becomes `COUNCIL BLOCKED`. Silently resolving conflicts is prohibited. Unresolved architectural contradictions or unmitigated security risks automatically halt the Council and escalate to the human owner.

## 9. Output Format

The Council MUST produce a standardized Decision Council Report:

# Decision Council Report

- **Decision:** [PASS | BLOCKED | DEFERRED]
- **Alternatives Considered:** [Summary of alternative approaches discussed]
- **Risks:** [List of unmitigated or accepted risks, classified by severity]
- **Dissenting Opinions:** [Unresolved disagreements between members]
- **Confidence:** [High | Medium | Low]
- **Human Decisions Required:** [Explicit questions the user must answer to unblock]

## 10. Governance Constraints

- `.ai` rules remain authoritative at all times.
- The Council does not authorize implementation.
- The Council does not replace human approval.
- The Council does not replace `architecture-analysis`.
- The Council does not replace `technical-design`.
- The Council validates designs, not code.
