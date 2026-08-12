---
document: Documentation Definition of Done
id: AEC-DOC-014
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-DOC-003
  - AEC-DOC-009
  - AEC-DOC-010
  - AEC-DOC-011
  - AEC-DOC-013
  - AEC-DEV-014
  - AEC-AI-014
---

# Purpose

Define the constitutional Definition of Done (DoD) for engineering documentation within the Anverra Engineering Operating System (AEOS).

Documentation shall not be considered complete merely because a file exists.

Documentation is complete when it is accurate, useful, discoverable, appropriately reviewed, secure, maintainable, and aligned with the engineering reality it represents.

---

# Intent

The Definition of Done establishes a consistent completion standard for:

- New documentation.
- Documentation updates.
- Architecture documentation.
- API documentation.
- Operational documentation.
- Decision records.
- AI documentation.
- Engineering standards.
- Knowledge-management artifacts.

---

# Problem Statement

Documentation work is frequently considered complete when:

- A file was created.
- A paragraph was added.
- A diagram was generated.
- An API specification was updated.

This creates incomplete engineering knowledge.

A document may exist while:

- Links are broken.
- Examples are incorrect.
- Architecture is stale.
- Ownership is missing.
- Review is incomplete.
- Security requirements are violated.

Existence is not completion.

---

# Constitutional Decision

Documentation work shall be considered Done only when all applicable Definition of Done criteria have been satisfied.

Completion shall be determined by documentation quality and engineering usefulness—not file creation.

---

# Rationale

Documentation is an engineering artifact.

Incomplete documentation can cause:

- Incorrect implementations.
- Operational failures.
- Security problems.
- Architectural misunderstandings.
- Consumer integration failures.

The Definition of Done protects against incomplete knowledge.

---

# Definition of Done Philosophy

Documentation is Done when a qualified reader can use it for its intended purpose with reasonable confidence.

The document shall be:

```text
Correct

+

Complete Enough

+

Discoverable

+

Reviewed

+

Secure

+

Maintainable

+

Authoritative

+

Lifecycle Managed
```

---

# Universal Definition of Done

Every applicable documentation change shall satisfy the following.

## Purpose

- Purpose is clear.
- Scope is understood.
- Intended audience is appropriate.

---

## Accuracy

- Information reflects current engineering reality.
- Examples are valid.
- References are correct.
- Terminology is accurate.

---

## Completeness

- Required concepts are covered.
- Important dependencies are included.
- Known limitations are documented where relevant.

---

## Structure

- Document follows applicable standards.
- Headings are logical.
- Navigation works.
- Metadata is present where required.

---

## Discoverability

- Document is stored in the correct location.
- Indexes are updated where required.
- Related documents are linked.

---

## Authority

- Source of truth is clear.
- Conflicting sources have been resolved.
- Status is correct.

---

## Review

- Required automated checks pass.
- Required human review is complete.
- Findings are resolved or explicitly accepted.

---

## Security

- No secrets are exposed.
- Sensitive information is appropriately handled.
- Security implications are reviewed where applicable.

---

## Lifecycle

- Owner is identified.
- Status is correct.
- Version is updated when required.
- Supersession is documented where applicable.

---

# Documentation Type Specific Criteria

## README

A README is Done when:

- Purpose is clear.
- Setup is documented where required.
- Navigation is clear.
- Important links work.
- Instructions are validated.

---

# Architecture Documentation

Architecture documentation is Done when:

- Current architecture is represented accurately.
- Boundaries are clear.
- Important dependencies are represented.
- Relevant diagrams are updated.
- Related ADRs are linked.
- Current vs target state is explicit.

---

# API Documentation

API documentation is Done when:

- Contract is authoritative.
- Requests are documented.
- Responses are documented.
- Errors are documented.
- Authentication is documented.
- Examples are valid.
- Versioning is clear.
- Contract validation passes.

---

# Operational Documentation

Operational documentation is Done when:

- Procedure is actionable.
- Prerequisites are documented.
- Commands are verified where practical.
- Expected results are described.
- Recovery or rollback is documented where required.
- Ownership is clear.

---

# Decision Documentation

A decision record is Done when:

- Context is documented.
- Decision is explicit.
- Alternatives are captured where meaningful.
- Consequences are documented.
- Status is clear.
- Related decisions are linked.

---

# Diagram

A diagram is Done when:

- Purpose is clear.
- Scope is appropriate.
- Notation is understandable.
- Relationships are accurate.
- Current/target state is clear.
- Source is maintained where practical.

---

# AI Documentation

AI documentation is Done when:

- Intended AI audience is clear.
- Context is accurate.
- Instructions are consistent with engineering standards.
- Conflicting guidance is resolved.
- Sensitive information is excluded.
- Human review is completed where required.

---

# Documentation Review Gate

Before completion:

```text
Automated Validation

↓

Human Review

↓

Issue Resolution

↓

Approval
```

Critical documentation shall not bypass required review.

---

# Documentation Quality Gate

The following shall be evaluated:

```text
Accuracy
Completeness
Clarity
Discoverability
Consistency
Security
Maintainability
Authority
```

---

# Documentation Automation Gate

Where automation exists:

- Validation shall pass.
- Links shall pass.
- Metadata shall pass.
- API contracts shall validate.
- Diagrams shall render where applicable.
- Generated output shall be reproducible where practical.

---

# Documentation Security Gate

Verify:

- No credentials.
- No secrets.
- No unintended sensitive data.
- Access controls appropriate.
- Security guidance accurate.

---

# Documentation Lifecycle Gate

Verify:

- Status correct.
- Owner identified.
- Version appropriate.
- Supersession documented.
- Retirement state correct where applicable.

---

# Documentation Traceability Gate

Where applicable, documentation should trace to:

- Requirement.
- Code change.
- Architecture decision.
- Incident.
- Release.
- Security finding.
- Governance decision.

Traceability requirements shall be proportional to impact.

---

# Human Approval

Human approval is required for documentation that materially affects:

- Architecture.
- Security.
- Production operations.
- Public APIs.
- Governance.
- Compliance.

---

# AI Responsibilities

AI shall:

- Identify affected documentation.
- Apply documentation standards.
- Validate generated content.
- Identify missing information.
- Identify likely drift.
- Report incomplete work.
- Avoid claiming completion without evidence.

AI shall prefer:

```text
Incomplete but Honest
```

over:

```text
Complete but Incorrect
```

---

# Human Responsibilities

Humans remain responsible for:

- Meaning.
- Accuracy.
- Architectural intent.
- Business semantics.
- Security.
- Operational correctness.
- Final approval.

---

# Evidence of Completion

Evidence may include:

- Pull Request approval.
- Automated validation results.
- API contract validation.
- Diagram rendering.
- Review record.
- Updated ADR.
- Updated runbook.
- Successful procedure test.

Evidence should be proportional to documentation criticality.

---

# Exceptions

Exceptions shall:

- Be documented.
- Include reason.
- Identify owner.
- Assess risk.
- Include remediation where necessary.
- Have an appropriate review date.

---

# Mandatory Rules

Documentation shall not be considered Done when:

- It contains known critical inaccuracies.
- Required review is incomplete.
- Required links are broken.
- Required ownership is missing.
- Required security controls are violated.
- It conflicts with authoritative engineering knowledge.

---

# Recommended Practices

Review documentation in the same Pull Request as related code.

Automate deterministic checks.

Use templates.

Keep documentation close to the system it describes.

Treat documentation defects as engineering work.

---

# Prohibited Practices

Do not:

- Mark documentation Done merely because a file exists.
- Skip required review.
- Publish known false information.
- Leave broken critical links.
- Claim procedures are tested when they are not.
- Claim AI validation that did not occur.

---

# Allowed Exceptions

Emergency changes may temporarily use abbreviated documentation processes when required to restore service or protect customers.

Outstanding documentation shall be completed after stabilization.

---

# Success Metrics

| Metric | Target |
|---|---:|
| Documentation DoD Compliance | 100% |
| Critical Documentation Review | 100% |
| Critical Documentation Accuracy | 100% |
| Broken Critical Links | 0 |
| Critical Documentation Security Violations | 0 |
| Unowned Critical Documentation | 0 |

---

# Review Checklist

Before marking documentation Done:

- [ ] Purpose clear
- [ ] Audience appropriate
- [ ] Scope correct
- [ ] Content accurate
- [ ] Required information complete
- [ ] Structure compliant
- [ ] Links valid
- [ ] Source of truth clear
- [ ] Owner identified
- [ ] Status correct
- [ ] Version updated if required
- [ ] Security checked
- [ ] Automated validation passed
- [ ] Human review completed
- [ ] Related documentation updated
- [ ] Traceability established where required

---

# Examples

## Good

```text
API Change

↓

OpenAPI Updated

↓

Examples Updated

↓

Contract Tests Pass

↓

Consumer Documentation Updated

↓

Review

↓

Merge

↓

Documentation Done
```

---

## Poor

```text
API Changed

↓

Developer Updates One Paragraph

↓

Merge

↓

Done
```

The documentation is incomplete.

---

# Anti-patterns

File-Exists Definition of Done

Documentation Later

Review Later

Generated-and-Published

Unverified Procedures

Stale Architecture

Broken Links

Unowned Documentation

---

# Constitutional Compliance Matrix

| Constitution | Status |
|---|---|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |
| Documentation Principles | Mandatory |
| Review Principles | Mandatory |
| Governance Principles | Mandatory |

---

# Engineering Decision

Documentation is complete only when it communicates accurate and useful engineering knowledge, satisfies applicable quality and security requirements, has passed required review, and remains aligned with the system it describes.

The Definition of Done ensures that documentation is treated as engineering work rather than administrative file creation.

---

# References

- Definition of Done
- Docs-as-Code
- Diátaxis
- Engineering Constitution
- Documentation Principles

---

# Related Documents

- Documentation Philosophy
- Documentation Architecture
- Documentation Standards
- Knowledge Management
- Documentation Lifecycle
- Documentation Review
- Documentation Automation
- Documentation Versioning
- Documentation Governance
- AI Definition of Done