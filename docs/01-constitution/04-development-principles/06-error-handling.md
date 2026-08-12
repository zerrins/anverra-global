---
document: Error Handling
id: AEC-DEV-006
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-DEV-001
  - AEC-DEV-005
  - AEC-ARC-004
---

# Purpose

Define a consistent, predictable, and observable error handling strategy for the Anverra Global platform.

Errors are inevitable in distributed software systems.

The objective is not to eliminate failures but to ensure that failures are detected early, communicated clearly, handled appropriately, and never compromise business integrity.

---

# Intent

Every failure should have a clearly defined behavior.

Errors shall:

- preserve business consistency,
- communicate meaningful information,
- support debugging,
- support monitoring,
- support automated recovery where appropriate.

Error handling is considered part of the business implementation rather than an afterthought.

---

# Problem Statement

Inconsistent error handling leads to:

- hidden failures,
- swallowed exceptions,
- misleading API responses,
- duplicated handling,
- corrupted business state,
- poor observability,
- unpredictable behavior,
- difficult debugging.

Without a common strategy, each engineer implements failures differently.

This inconsistency significantly increases maintenance cost.

---

# Development Decision

Anverra Global adopts a layered error handling strategy.

Errors shall be handled at the appropriate architectural boundary.

Business failures and technical failures shall be represented differently.

Every unexpected failure shall be observable.

---

# Rationale

Well-designed error handling:

- protects business integrity,
- improves user experience,
- simplifies debugging,
- improves AI implementation consistency,
- reduces production incidents.

Failures should be informative rather than surprising.

---

# Why This Matters to AI

AI-generated code frequently:

- catches generic exceptions,
- ignores exceptions,
- logs without handling,
- returns null,
- swallows stack traces,
- converts every error into RuntimeException.

These practices reduce reliability.

This document ensures AI generates predictable production-grade failure handling.

---

# Error Categories

Errors are classified into four categories.

## Business Errors

Business rules prevent the requested operation.

Examples:

- Policy already issued.
- Customer already exists.
- Premium exceeds limits.
- Renewal period expired.

These are expected failures.

They should be communicated clearly.

---

## Validation Errors

External input is invalid.

Examples:

- Missing mandatory field.
- Invalid email.
- Invalid date.
- Negative premium.
- Invalid document format.

Validation errors occur before business processing begins.

---

## Technical Errors

Infrastructure fails.

Examples:

- Database unavailable.
- Kafka unavailable.
- Redis unavailable.
- External API timeout.
- Network failure.

Business logic should remain isolated from these failures whenever possible.

---

## System Errors

Unexpected implementation failures.

Examples:

- NullPointerException
- IllegalStateException
- Serialization failure
- Configuration errors
- Programming defects

These indicate defects requiring investigation.

---

# Error Ownership

Each layer owns different failures.

## Domain

Owns business exceptions.

Examples:

- InvalidPolicyStateException
- CustomerAlreadyExistsException

---

## Application

Coordinates business failures.

Maps domain exceptions when necessary.

---

## Adapter

Translates exceptions into protocol-specific responses.

Examples:

- REST status codes
- Kafka dead-letter queues
- GraphQL errors

---

## Infrastructure

Handles technology failures.

Examples:

- SQL exceptions
- Redis failures
- Messaging failures

---

# Exception Design

Exceptions shall:

- describe the business problem,
- be meaningful,
- be specific,
- preserve root cause.

Avoid generic exception types.

Prefer:

```
PolicyAlreadyIssuedException
```

Avoid:

```
BusinessException
```

unless representing a common abstraction.

---

# Mandatory Rules

Never swallow exceptions.

Never return null to indicate failure.

Never expose internal implementation details.

Business exceptions shall remain business-oriented.

Infrastructure exceptions shall not leak into the Domain.

Unexpected failures shall always be logged.

Business invariants shall never be violated.

---

# Logging

Errors shall be logged exactly once.

Duplicate logging is prohibited.

Logs shall include:

- correlation identifier,
- module,
- business operation,
- exception type,
- relevant business identifiers.

Sensitive information shall never be logged.

---

# API Error Responses

REST APIs shall return consistent error structures.

Every response should contain:

- error code,
- message,
- correlation identifier,
- timestamp.

Internal implementation details shall never be exposed.

---

# Retry Strategy

Retry only when failures are transient.

Examples:

Retry:

- network timeout,
- temporary service unavailable,
- optimistic locking.

Do not retry:

- business validation,
- authentication failure,
- invalid requests.

Retries shall be bounded.

Infinite retries are prohibited.

---

# Transaction Handling

Business transactions shall remain atomic.

Partial business updates are prohibited.

Failed transactions shall rollback unless explicitly designed otherwise.

Compensating actions shall be documented.

---

# Mandatory Rules

Errors shall be classified.

Errors shall be observable.

Unexpected failures shall propagate appropriately.

Business failures shall preserve consistency.

Logging shall remain consistent.

Retry behavior shall be explicit.

---

# Recommended Practices

Prefer domain-specific exceptions.

Preserve root causes.

Use immutable error responses.

Centralize exception mapping.

Document public error codes.

Keep exception hierarchies shallow.

---

# Prohibited Practices

Do not catch Exception unless required.

Do not ignore exceptions.

Do not suppress stack traces.

Do not use exceptions for normal control flow.

Do not expose SQL errors to clients.

Do not expose stack traces through APIs.

Do not log sensitive information.

---

# Allowed Exceptions

Framework integration layers may temporarily wrap exceptions before translating them into application-specific errors.

Migration utilities may simplify exception handling while remaining isolated.

---

# AI Guidance

AI shall:

- classify every failure,
- generate domain-specific exceptions,
- preserve business consistency,
- log unexpected failures,
- avoid generic catch blocks,
- avoid returning null,
- avoid silent failures,
- avoid broad RuntimeException usage.

Before generating error handling, AI shall determine:

- Is this business?
- Is this validation?
- Is this infrastructure?
- Is this unexpected?

---

# Implementation Guidance

Implementation sequence:

1. Detect failure.
2. Classify failure.
3. Preserve business state.
4. Rollback if required.
5. Translate exception.
6. Log appropriately.
7. Return protocol-specific response.
8. Preserve observability.

---

# Review Checklist

Reviewers shall verify:

- Are exceptions meaningful?
- Is business state protected?
- Is error classification correct?
- Are failures observable?
- Is duplicate logging avoided?
- Are API responses consistent?
- Are sensitive details protected?
- Is retry appropriate?
- Is rollback behavior correct?

---

# Examples

## Good

```
throw PolicyAlreadyIssuedException(...)
```

Business meaning is explicit.

---

```
throw CustomerNotEligibleForRenewalException(...)
```

Business rule is preserved.

---

## Bad

```
catch(Exception e){

}
```

Silent failure.

---

```
return null;
```

Failure hidden.

---

```
throw new RuntimeException(...)
```

Business meaning lost.

---

# Anti-patterns

Exception Swallowing

Log And Throw

Return Null

Generic RuntimeException

Catch Everything

Silent Retry

Hidden Failure

Stack Trace Leakage

Boolean Error Codes

---

# Engineering Decision

Errors are first-class engineering artifacts.

Every failure shall communicate intent, preserve business integrity, and remain observable.

Error handling shall prioritize correctness over convenience.

---

# References

- Effective Java — Joshua Bloch
- Clean Code — Robert C. Martin
- Release It! — Michael T. Nygard

---

# Related Documents

- Defensive Programming
- API Design
- Domain Implementation
- Development Review Checklist
- Hexagonal Architecture
- Clean Code