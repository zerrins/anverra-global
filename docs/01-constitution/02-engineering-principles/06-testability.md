# Testability

**Stage:** 2 — Engineering Principles  
**Document:** 06 — Testability  
**Version:** 1.0  
**Status:** Expanded Draft  
**Principle ID:** AEOS-EP-006

---

# 1. Purpose

This document defines the **Testability** engineering principle.

Testability establishes that software should be designed so that its behavior can be validated efficiently, reliably, and repeatedly.

Testing is not merely a verification activity performed after implementation.

The system should be structured so that correct behavior can be demonstrated with appropriate evidence.

---

# 2. Principle Statement

> **Software should be designed so that important behavior can be validated reliably, repeatedly, and at an appropriate level of isolation.**

Testability is therefore both:

- a property of the system
- an engineering design concern

---

# 3. Existing AEOS Direction

The Engineering Vision identifies continuous validation as a core engineering principle.

Engineering should continuously validate:

- requirements
- architecture
- implementation
- tests
- security
- performance
- observability
- documentation
- AI-generated work

:contentReference[oaicite:17]{index=17}

The existing Testing Specification explicitly defines requirements for:

- unit testing
- integration testing
- architecture testing
- API testing
- end-to-end testing

:contentReference[oaicite:18]{index=18}

---

# 4. Why Testability Matters

Software that cannot be tested efficiently becomes difficult to change safely.

The relationship is:

```text
Change
  ↓
Uncertainty
  ↓
Validation
  ↓
Confidence
```

Without effective validation:

```text
Change
  ↓
Unknown Impact
  ↓
Production Risk
```

Testability therefore directly supports:

- maintainability
- modularity
- reliability
- predictable delivery
- continuous improvement

---

# 5. Testing Is Evidence

Testing should provide evidence rather than simply increase a coverage number.

A successful test should help answer:

> **What behavior does this prove?**

For example:

```text
Requirement
    ↓
Expected Behavior
    ↓
Test
    ↓
Evidence
```

The objective is meaningful confidence.

---

# 6. Testability Before Testing

A system can have many tests and still be difficult to test.

Poor testability may result from:

- excessive coupling
- hidden dependencies
- global state
- difficult configuration
- nondeterministic behavior
- external dependencies
- unclear responsibilities
- giant components

Testability should therefore influence design before tests are written.

---

# 7. Testable Design

A testable component should ideally have:

- clear responsibility
- explicit inputs
- observable outputs
- controlled dependencies
- predictable behavior
- limited hidden state

For example:

```text
Input
  ↓
Business Logic
  ↓
Output
```

is generally easier to test than:

```text
Input
  ↓
Hidden Global State
  ↓
Database
  ↓
External API
  ↓
Clock
  ↓
Randomness
  ↓
Business Logic
  ↓
Output
```

unless those dependencies are intentionally part of the behavior being tested.

---

# 8. Explicit Dependencies

Explicit dependencies improve testability.

If a component clearly depends on:

```text
PolicyRepository
NotificationService
Clock
```

tests can control those dependencies appropriately.

Hidden dependencies make tests more fragile and harder to understand.

This aligns with the architectural principle of explicit dependencies. :contentReference[oaicite:19]{index=19}

---

# 9. Testability and Modularity

Modularity and testability reinforce each other.

```text
Meaningful Module
      ↓
Controlled Responsibility
      ↓
Controlled Dependencies
      ↓
Focused Tests
```

The Engineering Vision explicitly expects modules to be independently testable where appropriate. 

Therefore a modularity decision should consider its effect on testability.

---

# 10. Testability and Maintainability

Tests are part of the maintainability system.

A maintainable system should allow engineers to make changes with confidence.

Tests provide executable evidence of expected behavior.

```text
Documentation
      +
Tests
      +
Architecture
      ↓
Shared Engineering Knowledge
```

Tests therefore complement written documentation.

---

# 11. Testability and Business Behavior

Tests should protect meaningful business behavior.

For Anverra, examples may include:

- customer lifecycle behavior
- policy lifecycle rules
- commission calculations
- product eligibility
- document workflows
- notification rules
- access control
- reporting behavior

Tests should not focus exclusively on implementation details.

---

# 12. Behavior Over Implementation

Prefer tests that express expected behavior.

For example:

```text
shouldRejectPolicyWhenProductIsInactive
```

communicates business behavior.

A test such as:

```text
shouldCallRepositoryMethodX
```

may be useful in some situations but can become overly coupled to implementation.

The correct balance depends on the type of test.

---

# 13. Test Levels

AEOS recognizes multiple levels of testing.

The existing Testing Specification identifies:

1. Unit tests
2. Integration tests
3. Architecture tests
4. API tests
5. End-to-end tests

:contentReference[oaicite:21]{index=21}

Each level provides different evidence.

---

# 14. Unit Tests

Unit tests should validate focused behavior.

They are particularly useful for:

- domain rules
- calculations
- validation
- transformations
- isolated business logic

A good unit test should generally be:

- fast
- deterministic
- focused
- easy to diagnose

---

# 15. Integration Tests

Integration tests validate interactions between components or infrastructure.

Examples include:

- database behavior
- module integration
- external service integration
- messaging
- persistence behavior

Integration tests are valuable where correctness depends on real component interaction.

---

# 16. Architecture Tests

Architecture tests provide automated evidence that architectural rules remain valid.

Examples may include checks for:

- dependency direction
- forbidden dependencies
- cyclic dependencies
- module boundaries
- layer violations

This is especially important because architecture can degrade gradually.

---

# 17. API Tests

API tests validate externally observable interface behavior.

They should provide confidence around:

- request validation
- response structure
- status behavior
- error behavior
- authorization
- business outcomes

API tests should focus on the contract rather than implementation details.

---

# 18. End-to-End Tests

End-to-end tests validate important workflows across multiple components.

They are particularly useful for critical user journeys.

However, they can be:

- slower
- more expensive
- more fragile
- harder to diagnose

Therefore they should be used where end-to-end evidence is valuable rather than as the only testing strategy.

---

# 19. Appropriate Testing Level

Not every behavior requires every testing level.

The preferred question is:

> **What is the cheapest reliable level at which this behavior can be validated?**

For example:

```text
Business Calculation
      ↓
Unit Test

Module Integration
      ↓
Integration Test

API Contract
      ↓
API Test

Critical User Workflow
      ↓
End-to-End Test
```

This avoids unnecessarily expensive validation.

---

# 20. Test Pyramid Thinking

A healthy testing strategy generally prefers more focused tests and fewer expensive broad tests.

Conceptually:

```text
        E2E
       /   \
     API / Integration
    /             \
       Unit Tests
```

The exact proportions should be determined by the system and risk.

The principle is not a fixed percentage.

---

# 21. Deterministic Tests

Tests should be repeatable.

A test that sometimes passes and sometimes fails without a meaningful code change reduces confidence.

Avoid unnecessary dependence on:

- current time
- randomness
- execution order
- external systems
- network availability
- shared mutable state
- machine-specific behavior

Where such dependencies are legitimate, they should be controlled appropriately.

---

# 22. Test Isolation

Tests should not unintentionally affect each other.

For example:

```text
Test A
  ↓
Changes Shared State
  ↓
Test B
  ↓
Unexpected Failure
```

is difficult to diagnose.

Prefer:

```text
Test A → Controlled State
Test B → Controlled State
Test C → Controlled State
```

where appropriate.

---

# 23. Test Data

Test data should be:

- understandable
- deterministic
- relevant
- appropriately isolated

Test data should help communicate the scenario rather than obscure it.

For example:

```text
activeProduct
eligibleCustomer
validPolicy
```

is generally easier to understand than:

```text
data1
data2
objectX
```

---

# 24. Test Naming

Test names should communicate expected behavior.

A useful structure is:

```text
<behavior>When<condition>
```

For example:

```text
shouldRejectPolicyWhenProductIsInactive
shouldCalculateCommissionForEligiblePolicy
shouldRenewPolicyWhenRenewalConditionsAreSatisfied
```

The exact naming standard belongs to later coding/testing standards.

---

# 25. Test Failures Should Be Diagnosable

A failing test should provide useful information.

A test suite that only says:

```text
FAILED
```

without identifying meaningful context increases maintenance cost.

Tests should make it reasonably clear:

- what behavior failed
- under what condition
- what was expected
- what actually happened

---

# 26. Testability and Observability

Observability helps diagnose integration and production behavior.

Tests and observability should therefore complement one another.

```text
Test
  ↓
Expected Behavior

Observability
  ↓
Runtime Behavior
```

Together they provide stronger evidence across development and operations.

---

# 27. Testability and Requirements

Testing should trace back to requirements.

The AEOS traceability model is:

```text
Intent
   ↓
Requirement
   ↓
Decision
   ↓
Task
   ↓
Artifact
   ↓
Validation
```

:contentReference[oaicite:22]{index=22}

Testing is one important form of validation.

Therefore an important requirement should have an identifiable validation strategy.

---

# 28. Testability and Acceptance Criteria

Acceptance criteria should be testable where practical.

A vague requirement such as:

> "The system should be fast."

does not provide enough information to construct meaningful validation.

A better requirement would eventually define observable behavior and acceptance conditions.

Detailed NFR and acceptance criteria belong to later AEOS specifications.

---

# 29. Testability and AI-Generated Code

AI-generated code must be tested using the same engineering expectations as human-generated code.

The AI Engineering Vision explicitly states that AI-generated work should not be trusted merely because the model is capable and that validation may include:

- tests
- static analysis
- architecture checks
- specification checks
- security checks
- documentation checks
- build verification
- runtime verification
- human review

:contentReference[oaicite:23]{index=23}

Therefore:

> **AI generation does not reduce validation requirements.**

---

# 30. AI and Test Generation

AI may assist with:

- test generation
- test-case discovery
- edge-case identification
- test maintenance
- test failure analysis

The AI Engineering Vision explicitly includes test generation and validation support among appropriate AI responsibilities. :contentReference[oaicite:24]{index=24}

However, generated tests must themselves be reviewed.

A bad test can create false confidence.

---

# 31. The Test-That-Proves-Nothing Anti-Pattern

A test is not valuable merely because it executes code.

For example:

```java
assertNotNull(service);
```

may technically be a test but provides little evidence about business behavior.

Similarly, a generated test that simply mirrors implementation details may fail to protect meaningful behavior.

The question should always be:

> **What failure would this test detect?**

---

# 32. Test Coverage

Coverage can be useful as an indicator.

The existing AEOS engineering KPIs include:

> **Automated test coverage** :contentReference[oaicite:25]{index=25}

However, coverage should not become the objective by itself.

High coverage does not guarantee:

- correct requirements
- useful assertions
- meaningful scenarios
- architectural correctness
- production safety

Therefore:

```text
Coverage
   +
Test Quality
   +
Requirement Coverage
   +
Risk Coverage
```

provides more meaningful evidence than coverage alone.

---

# 33. Testability and Regression Prevention

Tests should protect important existing behavior.

When a defect is discovered, an appropriate regression test should generally be considered.

The goal is:

```text
Defect
  ↓
Root Cause
  ↓
Fix
  ↓
Regression Test
  ↓
Future Protection
```

This turns individual failures into engineering learning.

---

# 34. Testability and Refactoring

Good tests provide safety during refactoring.

A strong test suite allows engineers to change internal implementation while verifying that externally meaningful behavior remains correct.

This supports the principle:

> **Implementation may change while behavior remains protected.**

---

# 35. Testability and Architecture Evolution

Architecture tests can help detect unintended structural degradation.

For example:

```text
Policy Module
      ↓
Customer Module
```

may be permitted.

But:

```text
Policy → Customer → Policy
```

may create a cycle.

Automated architecture validation can detect such problems before they become widespread.

---

# 36. Testability and Continuous Validation

Validation should happen throughout the engineering lifecycle.

Preferred direction:

```text
Plan
  ↓
Implement
  ↓
Validate
  ↓
Review
  ↓
Deliver
```

rather than:

```text
Implement
  ↓
Hope
  ↓
Fix
```

:contentReference[oaicite:26]{index=26}

Testing should therefore be integrated into development rather than treated as a final activity.

---

# 37. Testability and Developer Experience

A test suite should be easy enough to use that developers actually rely on it.

A good developer experience should make it easy to:

- run tests
- identify failures
- understand failures
- run focused tests
- run the full suite
- validate changes locally
- understand required test levels

The Engineering Vision explicitly identifies validation of changes as part of developer experience. :contentReference[oaicite:27]{index=27}

---

# 38. Testability and Speed

Tests should provide feedback at useful speeds.

A developer should be able to obtain:

```text
Fast Feedback
    ↓
Focused Development
```

and:

```text
Broader Validation
    ↓
Release Confidence
```

The solution is not to remove tests because they are slow.

Instead, the test strategy should provide appropriate validation at different levels.

---

# 39. Testability and External Dependencies

External dependencies can make tests slow or unreliable.

Examples include:

- third-party APIs
- payment systems
- email providers
- external insurance providers
- cloud services

Tests should distinguish between:

- behavior that can be validated locally
- behavior that genuinely requires integration validation

This avoids unnecessarily making every test dependent on external systems.

---

# 40. Testability Anti-Patterns

Avoid:

- tests that depend on execution order
- tests that depend on uncontrolled external systems
- hidden global state
- excessive mocking that removes meaningful behavior
- tests tightly coupled to implementation details
- giant end-to-end suites used for everything
- meaningless coverage-driven tests
- flaky tests
- duplicated test logic without reason
- tests that are difficult to diagnose
- AI-generated tests accepted without review
- treating tests as optional documentation

The Engineering Vision explicitly lists treating tests as optional documentation and skipping validation because AI produced the change as anti-patterns. :contentReference[oaicite:28]{index=28}

---

# 41. Testability Review Questions

Before approving significant implementation, ask:

1. What important behavior needs validation?
2. At what level should it be tested?
3. Can the component be tested without unnecessary infrastructure?
4. Are dependencies explicit?
5. Is the behavior deterministic?
6. Can failures be diagnosed?
7. Are business rules directly protected?
8. Are architectural constraints validated?
9. Are critical workflows covered?
10. If AI generated the implementation, has the generated test strategy been reviewed?

---

# 42. Testability Checklist

- [ ] Important behavior has a validation strategy.
- [ ] Components have clear responsibilities.
- [ ] Dependencies are explicit.
- [ ] Tests are deterministic where practical.
- [ ] Tests are appropriately isolated.
- [ ] Business behavior is directly validated.
- [ ] Appropriate unit tests exist.
- [ ] Appropriate integration tests exist.
- [ ] Architecture constraints are validated where required.
- [ ] API contracts are validated where required.
- [ ] Critical end-to-end workflows are validated where required.
- [ ] Test failures are diagnosable.
- [ ] Regression tests are added for important defects.
- [ ] Coverage is treated as evidence rather than the sole objective.
- [ ] AI-generated tests are reviewed.
- [ ] Validation is integrated into the engineering workflow.

---

# 43. Testability and AEOS Validation

Testability supports the broader AEOS execution model:

```text
Knowledge
    ↓
Reasoning
    ↓
Planning
    ↓
Execution
    ↓
Validation
    ↓
Feedback
    ↓
Improvement
```

The purpose is not simply to "run tests."

The purpose is to produce evidence that the engineering outcome satisfies its intended requirements.

---

# 44. Testability and Quality Governance

The existing Quality Specification defines engineering quality objectives, acceptance criteria, and quality gates. :contentReference[oaicite:29]{index=29}

Testability provides one mechanism through which those quality expectations can become executable evidence.

Detailed quality gates should therefore be defined by later AEOS quality and testing artifacts.

---

# 45. Final Rule

> **If important behavior cannot be validated reliably, the design should be questioned.**

Good testability means:

- behavior is observable
- dependencies are controllable
- tests are meaningful
- failures are diagnosable
- validation is repeatable
- evidence is proportional to risk

The objective is not:

> **"Write more tests."**

The objective is:

> **"Make correctness demonstrable."**