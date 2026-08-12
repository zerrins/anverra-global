---
document: Definition of Done
id: AEC-QLT-014
version: 1.0.0
status: Draft
---

# Purpose

Define the constitutional criteria for considering engineering work complete.

---

# Intent

Completion means production readiness—not merely implementation.

---

# Definition of Done

A feature is considered complete only when:

## Business

- Requirements implemented
- Acceptance criteria satisfied

## Architecture

- Architecture Principles respected
- Module ownership preserved

## Development

- Clean Code
- SOLID
- Readability
- Defensive Programming

## Quality

- Unit tests
- Integration tests
- Regression tests
- Quality gates

## Security

- Security review complete
- Secrets protected
- Authorization verified

## Operations

- Logging implemented
- Metrics exposed
- Alerts configured
- Dashboards updated

## Documentation

- ADR updated (if required)
- API documentation updated
- User documentation updated (if applicable)

## Review

- Peer review approved
- CI/CD successful
- No unresolved critical findings

---

# Mandatory Rules

No feature may bypass the Definition of Done.

Incomplete work shall not be represented as complete.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Mandatory Checklist | 100% |
| Critical Issues | 0 |
| CI Success | 100% |
| Documentation Updated | 100% |

---

# Constitutional Compliance Matrix

| Constitution Stage | Required |
|-------------------|----------|
| Vision | ✓ |
| Engineering Principles | ✓ |
| Architecture Principles | ✓ |
| Development Principles | ✓ |
| Quality Principles | ✓ |

---

# Engineering Decision

Done means production-ready.

Anything less is work in progress.

---

# Related Documents

- Release Quality Gates
- Development Review Checklist
- Engineering Principles