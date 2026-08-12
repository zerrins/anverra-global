---
document: Code Review
id: AEC-REV-004
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-DEV-000
  - AEC-QLT-000
---

# Purpose

Define the principles, standards, workflow, responsibilities, and completion criteria for source-code review within the Anverra Engineering Operating System (AEOS).

Code review exists to evaluate whether an implementation correctly, safely, and maintainably realizes its intended behavior.

---

# Intent

Code review should answer:

- Does the implementation solve the intended problem?
- Is the behavior correct?
- Are important edge cases handled?
- Does the implementation fit the architecture?
- Is the code maintainable?
- Is the error handling appropriate?
- Are security risks addressed?
- Is the implementation sufficiently tested?
- Does the change introduce unnecessary complexity?
- Does the implementation affect documentation or operations?

---

# Constitutional Decision

Meaningful source-code changes shall receive appropriate code review before acceptance.

Review depth shall be proportional to:

- Risk.
- Complexity.
- Blast radius.
- Security impact.
- Reversibility.
- Architectural significance.

---

# Code Review Is Not Code Inspection Alone

A reviewer should not evaluate only syntax or individual lines.

Code review should evaluate:

```text
Intent
   ↓
Design
   ↓
Implementation
   ↓
Behavior
   ↓
System Impact
```

---

# Author Responsibilities

Before requesting review, the author shall:

- Understand the change.
- Perform self-review.
- Remove unnecessary changes.
- Add or update tests where appropriate.
- Update documentation where required.
- Identify known limitations.
- Identify relevant risks.
- Provide sufficient context.

The author should not expect the reviewer to reconstruct the entire intent of the change from code alone.

---

# Pull Request Context

A meaningful code review should provide:

- Problem statement.
- Expected behavior.
- Scope.
- Relevant design information.
- Testing performed.
- Known risks.
- Deployment considerations where applicable.

For larger changes, link to:

- Requirements.
- Design documents.
- ADRs.
- Issues.
- API specifications.

---

# Self Review

Before requesting peer review, the author should inspect:

- Complete diff.
- Unintended changes.
- Debug code.
- Temporary code.
- Logging.
- Exception handling.
- Tests.
- Configuration.
- Documentation.

Self-review is the first quality gate.

---

# Review the Diff

Reviewers should understand:

- Added behavior.
- Removed behavior.
- Modified behavior.
- New dependencies.
- Configuration changes.
- Data changes.

A large diff should be evaluated carefully for scope creep.

---

# Scope Discipline

A change should generally contain only work necessary for its stated objective.

Avoid unrelated:

- Refactoring.
- Formatting changes.
- Dependency upgrades.
- Renaming.
- File movement.

unless they are required or clearly justified.

Unrelated changes make review harder and increase risk.

---

# Correctness Review

Reviewers should verify:

- Business logic.
- Control flow.
- State transitions.
- Data transformations.
- Boundary conditions.
- Null handling.
- Error paths.
- Concurrency behavior where relevant.

---

# Input Validation

Review whether inputs are:

- Validated.
- Normalized where necessary.
- Properly bounded.
- Safe for downstream systems.

Do not assume upstream validation exists unless the contract guarantees it.

---

# Error Handling

Review:

- Expected failures.
- Unexpected failures.
- Error propagation.
- Error messages.
- Retry behavior.
- Timeout behavior.
- Partial failure.

Avoid:

- Swallowing exceptions.
- Generic catch-all handling without justification.
- Logging and continuing when correctness requires failure.

---

# Logging

Review whether logging is:

- Useful.
- Appropriate in severity.
- Free of sensitive data.
- Sufficient for diagnosis.

Avoid:

- Logging secrets.
- Logging unnecessary personal data.
- Excessive noisy logs.
- Missing context for important failures.

---

# Concurrency

Where applicable, review:

- Shared state.
- Race conditions.
- Synchronization.
- Thread safety.
- Async behavior.
- Ordering guarantees.
- Duplicate execution.

Concurrency bugs can remain invisible during normal tests and therefore require deliberate review.

---

# Transactions

Where transactions exist, review:

- Transaction boundaries.
- Isolation.
- Rollback behavior.
- External side effects.
- Retry behavior.
- Idempotency.

Do not assume a transaction automatically makes a workflow atomic across external systems.

---

# Database Access

Review:

- Query correctness.
- Index usage where relevant.
- N+1 patterns.
- Transaction behavior.
- Connection handling.
- Migration compatibility.
- Data integrity.

Production-scale implications should be considered for significant changes.

---

# External Dependencies

Review:

- Timeout behavior.
- Retry policy.
- Failure handling.
- Version compatibility.
- Authentication.
- Rate limits.
- Dependency availability.

---

# API Calls

Review:

- Request construction.
- Response handling.
- Error handling.
- Timeout behavior.
- Retry behavior.
- Compatibility.

---

# Resource Management

Review whether resources are properly managed:

- Connections.
- Threads.
- Files.
- Streams.
- Memory.
- Executors.
- Network resources.

Resource leaks may only become visible under sustained production load.

---

# Performance

Performance review should consider:

- Algorithmic complexity.
- Database calls.
- Network calls.
- Serialization.
- Memory allocation.
- Caching.
- Concurrency.

Performance concerns should be supported by evidence where possible.

Avoid speculative optimization without meaningful impact.

---

# Security

Review security-sensitive code for:

- Authentication.
- Authorization.
- Input handling.
- Injection.
- Secrets.
- Sensitive data.
- Trust boundaries.
- Privilege escalation.

Security review requirements may trigger a separate specialist review.

---

# Maintainability

Evaluate:

- Readability.
- Naming.
- Cohesion.
- Coupling.
- Duplication.
- Abstraction.
- Complexity.

The question is:

> Can another engineer safely understand and modify this code later?

---

# Abstractions

Review whether abstractions are:

- Necessary.
- Understandable.
- Appropriately scoped.
- Consistent with the architecture.

Avoid abstraction introduced only because code duplication appears once or twice.

---

# Duplication

Duplication should be evaluated in context.

Do not automatically eliminate all duplication.

Sometimes duplication is preferable to introducing:

- Premature abstraction.
- Tight coupling.
- Unclear dependencies.

---

# Dependencies

New dependencies should be reviewed for:

- Necessity.
- Security.
- Maintenance.
- Compatibility.
- Licensing where applicable.
- Operational impact.
- Transitive dependencies.

---

# Configuration

Review configuration changes for:

- Defaults.
- Environment-specific behavior.
- Secrets.
- Compatibility.
- Operational impact.
- Failure behavior.

Configuration that materially changes application behavior should receive equivalent engineering scrutiny to source code.

---

# Tests

Code review should evaluate whether tests provide sufficient evidence.

Consider:

- Happy path.
- Edge cases.
- Failure cases.
- Boundary conditions.
- Regression coverage.
- Integration behavior.

Passing tests do not replace code review.

---

# Test Quality

A test should provide meaningful confidence.

Avoid tests that merely:

- Execute lines.
- Assert implementation details unnecessarily.
- Duplicate framework behavior.
- Cannot fail when behavior is broken.

---

# Test-to-Change Relationship

Reviewers should ask:

```text
What changed?

↓

What behavior changed?

↓

Which tests demonstrate the behavior?
```

---

# Backward Compatibility

Review whether the change affects existing:

- APIs.
- Database schemas.
- Events.
- Configuration.
- Clients.
- Consumers.

Compatibility should be explicit when relevant.

---

# Feature Flags

Where feature flags are used, review:

- Default behavior.
- Rollout behavior.
- Flag ownership.
- Cleanup strategy.
- Failure behavior.

Feature flags should not become permanent hidden architecture.

---

# Observability

For production-impacting code, review:

- Logs.
- Metrics.
- Traces.
- Error reporting.
- Operational diagnostics.

A system that cannot explain its failures is harder to operate safely.

---

# Documentation Impact

Code review should identify changes requiring:

- README updates.
- API documentation.
- Architecture documentation.
- Runbooks.
- ADRs.
- Configuration documentation.

---

# Review Comments

Code review comments should distinguish:

```text
Bug
Security Issue
Reliability Risk
Maintainability Issue
Question
Suggestion
Nit
```

Do not present optional preferences as defects.

---

# Blocking Findings

Code review should block acceptance when the implementation contains:

- Known critical correctness defects.
- Security vulnerabilities.
- Data corruption risks.
- Broken required contracts.
- Unacceptable production risks.
- Missing mandatory behavior.

---

# Code Review Workflow

```text
Implementation
      ↓
Self Review
      ↓
Automated Checks
      ↓
Pull Request
      ↓
Peer Review
      ↓
Findings
      ↓
Corrections
      ↓
Re-review
      ↓
Approval
      ↓
Merge
```

---

# Re-review

A reviewer should re-check changes after significant corrections.

Do not assume that fixing one issue cannot introduce another.

---

# Review Size

Smaller changes are generally easier to review effectively.

Where practical:

- Keep Pull Requests focused.
- Separate unrelated changes.
- Split large changes into logical units.

Large changes should provide additional context.

---

# Large Change Review

For large changes:

- Provide architecture/design context.
- Identify migration strategy.
- Explain risk.
- Identify testing strategy.
- Consider incremental review.

Do not force reviewers to understand thousands of changed lines without guidance.

---

# Generated Code

Generated code shall be reviewed according to its resulting behavior and risk.

Generated code is not exempt from:

- Security review.
- Testing.
- Maintainability expectations.
- Architecture constraints.

---

# AI-Generated Code

AI-generated code shall be treated as normal production code for review purposes.

The review process should verify:

- Correctness.
- Security.
- Maintainability.
- Dependencies.
- Tests.
- Architecture.

The author remains accountable for code they submit.

---

# Emergency Changes

Emergency changes may use an abbreviated review path.

Where normal review cannot be completed before deployment:

```text
Emergency Review
      ↓
Safe Deployment
      ↓
Post-Change Review
```

Emergency status does not permanently exempt the change from review.

---

# Code Review Metrics

Useful metrics include:

- Escaped defects.
- Review cycle time.
- Rework.
- Repeated findings.
- Review coverage.

Avoid using:

- Number of comments.
- Lines reviewed.
- Number of approvals.

as direct measures of reviewer performance.

---

# Mandatory Rules

Code review shall:

- Be performed before normal acceptance.
- Consider correctness.
- Consider security where applicable.
- Consider tests.
- Consider maintainability.
- Consider operational impact.
- Identify documentation impact.
- Resolve blocking findings.

---

# Recommended Practices

Keep Pull Requests focused.

Review early.

Use automated checks.

Prefer evidence.

Explain non-obvious decisions.

Use specialists for specialist risks.

---

# Prohibited Practices

Do not:

- Approve known critical defects.
- Block changes over personal style preferences.
- Treat test success as proof of correctness.
- Ignore generated-code risks.
- Skip review merely because code was AI-generated.

---

# Definition of Done

Code review is complete when:

- Self-review is complete.
- Required automated checks pass.
- Applicable reviewers have reviewed the change.
- Blocking findings are resolved.
- Required specialist reviews are complete.
- Tests provide appropriate evidence.
- Documentation impact is addressed.
- Required approvals exist.

---

# Review Checklist

### Context

- [ ] Problem understood
- [ ] Scope understood
- [ ] Requirements understood

### Code

- [ ] Correctness
- [ ] Error handling
- [ ] Concurrency
- [ ] Resource management
- [ ] Maintainability
- [ ] Dependencies

### Security

- [ ] Input handling
- [ ] Authorization
- [ ] Secrets
- [ ] Sensitive data

### Verification

- [ ] Tests reviewed
- [ ] Automated checks pass
- [ ] Edge cases considered

### Operations

- [ ] Logging
- [ ] Metrics
- [ ] Failure behavior
- [ ] Deployment impact

### Documentation

- [ ] Affected documentation updated

### Completion

- [ ] Blocking findings resolved
- [ ] Required approvals obtained

---

# Engineering Decision

Code review is a quality control mechanism that evaluates implementation correctness, system impact, maintainability, security, and operational safety.

The objective is not to inspect every character.

The objective is to determine whether the implementation is safe and appropriate to become part of the engineering system.