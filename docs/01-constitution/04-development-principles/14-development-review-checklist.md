---
document: Development Review Checklist
id: AEC-DEV-014
version: 1.0.0
status: Draft
---

# Purpose

Provide a standardized review checklist for every implementation.

---

# Intent

Every code review shall evaluate engineering quality consistently.

---

# Review Areas

## Business

- Correct business behavior
- Correct ownership
- Correct terminology

## Architecture

- DDD followed
- Hexagonal Architecture respected
- Module boundaries preserved

## Code Quality

- Clean Code
- SOLID
- Readability
- Naming
- Cohesion

## Reliability

- Defensive Programming
- Error Handling
- Validation

## APIs

- Contract consistency
- Backward compatibility
- Security

## Performance

- Measured optimization
- Efficient database access
- Caching reviewed

## Security

- Authentication
- Authorization
- Input validation
- Secret handling

## Testing

- Unit tests
- Integration tests
- Edge cases
- Regression tests

## Documentation

- ADR required?
- API documentation updated?
- Architecture updated?

---

# AI Guidance

Before presenting generated code, AI shall perform an internal review using every Development Principle.

Generated implementations should satisfy this checklist before submission.

---

# Mandatory Approval Gates

No implementation is approved if:

- Business rules are incorrect.
- Architecture is violated.
- Security is compromised.
- Tests fail.
- Public contracts break.
- Technical debt increases without approval.

---

# Constitutional Compliance Matrix

| Constitution | Status |
|-------------|--------|
| Vision | ✓ |
| Engineering Principles | ✓ |
| Architecture Principles | ✓ |
| Development Principles | ✓ |
| Quality Principles | Pending |

---

# Engineering Decision

Every implementation shall pass this checklist before merge approval.

---

# Related Documents

- All Development Principles
- Architecture Principles
- Engineering Principles