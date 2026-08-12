---
document: AI Definition of Done
id: AEC-AI-014
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-AI-000
  - AEC-AI-013
  - AEC-DEV-014
  - AEC-REP-014
---

# Purpose

Define the constitutional Definition of Done (DoD) for all AI-assisted engineering activities within the Anverra Engineering Operating System (AEOS).

The Definition of Done establishes the minimum engineering criteria that shall be satisfied before AI-assisted work is considered complete.

Completion means more than successful implementation.

Completed engineering work shall be correct, maintainable, secure, documented, tested, reviewed, and constitutionally compliant.

---

# Intent

Every AI-assisted engineering activity shall satisfy the same engineering quality standards regardless of:

- AI model
- Programming language
- Framework
- Repository
- Team
- Technology stack

The Definition of Done ensures consistent engineering quality throughout the organization.

---

# Problem Statement

AI-generated implementations frequently appear complete while lacking:

- Architectural validation
- Business correctness
- Test coverage
- Documentation
- Security validation
- Performance review
- Human approval
- Repository consistency

Implementation alone is not completion.

---

# Constitutional Decision

AI-generated work shall not be considered complete until every applicable Definition of Done criterion has been satisfied.

Completion is determined by engineering quality—not by AI generation.

---

# Rationale

Engineering quality depends upon the complete lifecycle rather than implementation alone.

Software becomes valuable only after:

- Validation
- Review
- Documentation
- Testing
- Integration
- Operational readiness

The Definition of Done protects engineering quality.

---

# Definition of Done Philosophy

Done means:

Correct.

Tested.

Documented.

Reviewed.

Integrated.

Maintainable.

Deployable.

Governed.

If any essential engineering activity remains incomplete, the work is not done.

---

# Definition of Done Principles

Every completed engineering activity shall be:

## Business Complete

Business objectives fully implemented.

Acceptance criteria satisfied.

---

## Architecturally Correct

Architecture remains consistent.

Module boundaries preserved.

Repository conventions respected.

---

## Constitutionally Compliant

Engineering work complies with:

- Engineering Principles
- Architecture Principles
- Development Principles
- Quality Principles
- AI Principles
- Repository Principles
- Documentation Principles
- Governance Principles

---

## Production Ready

The implementation is deployable without requiring undocumented work.

---

## Maintainable

Future engineers can understand, modify, and extend the implementation.

---

# AI Definition of Done Lifecycle

Every AI-assisted activity progresses through the following lifecycle.

```
Requirement

↓

Planning

↓

Architecture

↓

Implementation

↓

Testing

↓

Documentation

↓

AI Review

↓

Human Review

↓

Approval

↓

Ready for Merge

↓

Done
```

No stage shall be skipped without documented approval.

---

# Definition of Done Checklist

## Requirements

- Business objective understood.
- Acceptance criteria defined.
- Scope agreed.
- Constraints documented.

---

## Architecture

- Existing architecture preserved.
- Module boundaries respected.
- No architectural drift introduced.
- ADR updated where necessary.

---

## Implementation

- Business rules implemented.
- Existing patterns reused.
- Code readable.
- Error handling implemented.
- Defensive programming applied.
- No unnecessary complexity introduced.

---

## Testing

- Unit tests implemented.
- Integration tests updated.
- Regression tests reviewed.
- Edge cases covered.
- Negative scenarios covered.
- All tests passing.

---

## Security

- Authentication validated.
- Authorization validated.
- Secrets externalized.
- Dependencies reviewed.
- Security scan passed.

---

## Performance

- Performance impact assessed.
- Resource usage acceptable.
- No obvious regressions introduced.
- Expensive operations reviewed.

---

## Documentation

- README updated where applicable.
- API documentation synchronized.
- Architecture documentation updated.
- Operational documentation reviewed.
- Changelog updated.

---

## AI Validation

- AI self-review completed.
- Constitutional compliance verified.
- Repository standards followed.
- Duplicate implementation avoided.
- Existing abstractions reused.

---

## Human Review

- Business correctness validated.
- Engineering quality approved.
- Security concerns addressed.
- Architecture accepted.
- Documentation reviewed.

---

## Repository

- Branch strategy followed.
- Commit messages compliant.
- CI successful.
- Build successful.
- Quality gates passed.

---

# Completion Gates

Every AI-assisted implementation shall satisfy the following gates.

```
Requirement Gate

↓

Architecture Gate

↓

Implementation Gate

↓

Quality Gate

↓

Security Gate

↓

Documentation Gate

↓

AI Review Gate

↓

Human Review Gate

↓

Merge Gate
```

Failure at any gate prevents completion.

---

# AI Responsibilities

AI shall verify:

- Business requirements implemented.
- Repository conventions followed.
- Tests updated.
- Documentation synchronized.
- Security considerations addressed.
- Architecture preserved.
- Constitutional compliance maintained.

AI shall report incomplete work honestly.

---

# Human Responsibilities

Human engineers remain responsible for approving:

- Business correctness
- Architecture
- Security acceptance
- Operational readiness
- Customer impact
- Regulatory compliance
- Production deployment

The Definition of Done cannot be satisfied without human approval.

---

# Evidence of Completion

Every completed engineering activity should provide evidence where applicable.

Examples:

- Passing CI pipeline
- Test reports
- Coverage reports
- Security scan reports
- Architecture review
- Updated documentation
- Pull Request approval
- Linked work item
- ADR (if required)

Evidence supports auditability.

---

# Exceptions

Exceptions shall:

- Be documented.
- Be approved.
- Include risk assessment.
- Include remediation plan.
- Include review schedule.

Temporary exceptions shall not become permanent practice.

---

# AI Guidance

AI shall:

- Evaluate completion objectively.
- Never claim incomplete work is finished.
- Recommend remaining tasks.
- Explain missing Definition of Done criteria.
- Preserve repository quality.
- Encourage incremental completion.

AI shall prefer honesty over premature completion.

---

# Mandatory Rules

AI-assisted work shall:

- Satisfy business requirements.
- Pass all applicable tests.
- Preserve architecture.
- Update documentation.
- Pass quality gates.
- Complete human review.
- Meet constitutional standards.

---

# Recommended Practices

Review the Definition of Done before implementation begins.

Automate validation where practical.

Keep Pull Requests small.

Prefer incremental completion.

Use checklists consistently.

Continuously improve completion criteria.

---

# Prohibited Practices

Do not:

- Merge unreviewed AI-generated code.
- Skip testing.
- Ignore documentation updates.
- Bypass quality gates.
- Claim completion prematurely.
- Ignore architectural violations.
- Leave known defects undocumented.

---

# Allowed Exceptions

Emergency production fixes may temporarily bypass selected non-critical completion criteria.

Such exceptions shall:

- Be approved.
- Be documented.
- Be reviewed retrospectively.
- Complete outstanding activities after stabilization.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Definition of Done Compliance | 100% |
| AI Work Reviewed by Humans | 100% |
| Documentation Synchronization | 100% |
| Quality Gate Compliance | 100% |
| Production Defects from Incomplete Work | 0 |
| Constitutional Compliance | 100% |

---

# Review Checklist

Before marking work as complete, verify:

- Requirements satisfied
- Business rules correct
- Architecture preserved
- Tests passing
- Security validated
- Documentation updated
- AI review completed
- Human review completed
- Repository standards followed
- Definition of Done satisfied

---

# Examples

## Good

```
Requirement

↓

Plan

↓

Implement

↓

Test

↓

Document

↓

AI Review

↓

Human Review

↓

Merge

↓

Done
```

---

## Poor

```
Generate Code

↓

Compile

↓

Merge

↓

Production
```

Successful compilation does not satisfy the Definition of Done.

---

# Anti-patterns

Definition of Done by Compilation

Testing After Release

Documentation Later

Architecture by Accident

Unchecked AI Output

Merge First, Review Later

Quality Gate Bypass

Premature Completion

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
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

The Definition of Done is the constitutional completion standard for AI-assisted engineering.

Engineering work is complete only when it satisfies business objectives, architectural integrity, quality requirements, security expectations, documentation obligations, repository standards, constitutional principles, and human approval.

Completion is measured by engineering excellence—not by code generation.

---

# References

- Engineering Constitution
- Definition of Done (Scrum Guide)
- Domain-Driven Design
- Clean Architecture
- ISO/IEC 42001

---

# Related Documents

- AI Collaboration
- Human-AI Collaboration
- AI Governance
- Development Review Checklist
- Repository Checklist
- Engineering Governance