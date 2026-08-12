---
document: API Review
id: AEC-REV-007
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
  - AEC-REV-005
  - AEC-DOC-005
---

# Purpose

Define the principles, standards, review workflow, and completion criteria for reviewing application programming interfaces (APIs) and other machine-consumed contracts.

API review protects consumers from unclear, unsafe, incompatible, or unnecessarily complex interfaces.

---

# Intent

API review should answer:

- Is the API solving the correct problem?
- Is the contract clear?
- Is the interface intuitive?
- Is the API consistent with organizational conventions?
- Is compatibility understood?
- Are errors well defined?
- Is security appropriate?
- Is the API observable and operable?
- Is the versioning strategy appropriate?
- Is documentation complete?

---

# Constitutional Decision

New public, partner-facing, cross-service, or otherwise significant APIs shall receive appropriate API review before becoming authoritative contracts.

---

# API Review Scope

API review may apply to:

- REST APIs.
- GraphQL APIs.
- gRPC services.
- Event contracts.
- Messaging interfaces.
- Webhooks.
- Internal service contracts.
- SDK interfaces.
- Schema-based integrations.

The applicable review criteria depend on the interface type.

---

# API as Contract

An API is a contract between producer and consumer.

The contract includes more than endpoint names.

It may include:

```text
Request

Response

Errors

Authentication

Authorization

Timing

Idempotency

Ordering

Versioning

Availability

Rate Limits
```

---

# API Design Principles

APIs should be:

- Clear.
- Consistent.
- Predictable.
- Explicit.
- Stable.
- Secure.
- Observable.
- Evolvable.

---

# Problem Definition

Before reviewing the interface, understand:

- What capability is being exposed?
- Who consumes it?
- What problem does it solve?
- What guarantees are required?

---

# Consumer Analysis

Review:

- Number of consumers.
- Consumer ownership.
- Internal vs external consumers.
- Synchronous vs asynchronous usage.
- Compatibility expectations.

Consumer impact strongly influences review depth.

---

# API Boundary

The API should expose a meaningful capability rather than internal implementation details.

Avoid contracts tightly coupled to internal database structure unless deliberately required.

---

# Naming

Names should be:

- Consistent.
- Predictable.
- Domain-appropriate.
- Unambiguous.

Follow established API conventions.

---

# Request Design

Review:

- Required fields.
- Optional fields.
- Defaults.
- Validation.
- Data types.
- Constraints.
- Nullability.

---

# Response Design

Review:

- Required fields.
- Optional fields.
- Data types.
- Semantics.
- Pagination.
- Metadata.
- Nullability.

---

# Error Design

Errors should communicate:

- What failed.
- Whether the client can correct it.
- Whether retry is appropriate.
- Stable error semantics.

Avoid exposing internal implementation details unnecessarily.

---

# HTTP Semantics

For HTTP APIs, review appropriate use of:

- Methods.
- Status codes.
- Headers.
- Resource semantics.

Do not use status codes merely because they are convenient.

---

# Idempotency

Review whether repeated requests can cause unintended side effects.

Important operations should define:

- Idempotency behavior.
- Idempotency keys where appropriate.
- Retry semantics.

---

# Retry Semantics

Consumers should know when retrying is safe.

Review:

- Timeouts.
- Retryable failures.
- Backoff.
- Duplicate effects.

---

# Pagination

For collection APIs, review:

- Pagination strategy.
- Limits.
- Ordering.
- Stable continuation.
- Large result behavior.

---

# Filtering and Sorting

Review:

- Supported filters.
- Validation.
- Performance implications.
- Consistency.

Avoid exposing unrestricted query capabilities without considering security and performance.

---

# Versioning

API versioning should communicate meaningful compatibility boundaries.

Review:

- Breaking changes.
- Non-breaking changes.
- Deprecation.
- Migration path.
- Consumer communication.

---

# Backward Compatibility

Review whether changes affect existing consumers.

Potentially breaking changes include:

- Removing fields.
- Changing field meaning.
- Changing requiredness.
- Changing error semantics.
- Changing authentication.
- Changing response types.

---

# Additive Changes

Additive changes are often safer but still require review.

Examples:

- New optional response field.
- New endpoint.
- New optional request field.

Consumers may still be affected by unexpected behavior, payload size, or semantics.

---

# Authentication

Review:

- Authentication mechanism.
- Credential handling.
- Token lifetime.
- Rotation.
- Failure behavior.

---

# Authorization

Review:

- Resource access.
- Tenant boundaries.
- Role permissions.
- Object-level authorization.
- Privilege escalation risks.

Authentication alone does not establish authorization.

---

# Sensitive Data

Review:

- Personal data.
- Financial data.
- Credentials.
- Internal metadata.

Determine whether data should be exposed through the API at all.

---

# Rate Limiting

Where applicable, define:

- Limits.
- Scope.
- Behavior when exceeded.
- Consumer expectations.

---

# Availability

API contracts should consider availability expectations where meaningful.

For critical APIs, review:

- Dependency behavior.
- Timeouts.
- Failover.
- Capacity.

---

# Performance

Consider:

- Latency.
- Throughput.
- Payload size.
- Query complexity.
- Serialization.
- Downstream dependencies.

Performance expectations should be measurable where necessary.

---

# Async APIs and Events

For event contracts, review:

- Schema.
- Event semantics.
- Producer ownership.
- Consumer expectations.
- Ordering.
- Delivery guarantees.
- Duplicate delivery.
- Replay.
- Retention.

---

# Event Compatibility

Event schema changes should consider:

- Existing consumers.
- Old producers.
- Schema evolution.
- Forward compatibility.
- Backward compatibility.

---

# Webhooks

Review:

- Authentication.
- Delivery retries.
- Duplicate events.
- Ordering.
- Verification.
- Failure handling.

---

# API Observability

Review whether consumers and operators can diagnose:

- Request failures.
- Latency.
- Rate limiting.
- Dependency failures.
- Authentication failures.

---

# API Documentation

API documentation should include where applicable:

- Endpoint/interface.
- Request.
- Response.
- Errors.
- Authentication.
- Examples.
- Version.
- Compatibility.
- Limits.
- Operational behavior.

---

# Contract Validation

Where practical, use:

- OpenAPI validation.
- Schema validation.
- Contract tests.
- Consumer-driven tests.

Automation should validate deterministic contract properties.

---

# API Review Workflow

```text
Capability
    ↓
Consumer Analysis
    ↓
API Design
    ↓
Contract Definition
    ↓
API Review
    ↓
Security Review
    ↓
Contract Validation
    ↓
Documentation
    ↓
Implementation
    ↓
Consumer Validation
```

---

# API Review Outcomes

Possible outcomes:

```text
Approved

Approved with Conditions

Changes Required

Compatibility Risk Identified

Security Review Required

Consumer Review Required

Rejected
```

---

# API Review and Architecture

API boundaries should align with appropriate service and domain boundaries.

Avoid APIs that merely expose internal implementation structure.

---

# API Review and Security

Security-sensitive APIs may require specialist security review.

Examples:

- Authentication APIs.
- Authorization APIs.
- Payment-related interfaces.
- Sensitive data interfaces.
- Administrative interfaces.

---

# API Review and Operations

Review:

- Deployment compatibility.
- Rollback.
- Monitoring.
- Rate limiting.
- Capacity.
- Failure behavior.

---

# API Review and Documentation

The authoritative API contract should be clearly identified.

Generated documentation should derive from the authoritative contract where practical.

---

# AI-Assisted API Review

AI may assist with:

- Detecting inconsistent naming.
- Finding missing fields.
- Comparing API versions.
- Identifying possible breaking changes.
- Generating consumer-impact questions.

AI findings require validation.

---

# Breaking Change Detection

Automated tooling should be used where practical to detect:

- Removed fields.
- Changed types.
- Required-field changes.
- Changed endpoints.
- Changed schemas.

Automated detection does not replace semantic review.

---

# API Deprecation

Deprecation should communicate:

- What is deprecated.
- Why.
- Replacement.
- Timeline where applicable.
- Migration guidance.

---

# API Review Anti-Patterns

## Internal Database Exposure

Exposing database structure as an API contract without justification.

## Inconsistent Semantics

Different APIs representing the same concept differently without reason.

## Hidden Breaking Changes

Changing behavior without communicating compatibility impact.

## Error Ambiguity

Returning errors that consumers cannot act upon.

## Retry Ambiguity

Leaving consumers uncertain whether retry is safe.

## Consumer Blindness

Designing an API without understanding consumers.

---

# Mandatory Rules

API review shall:

- Identify consumers.
- Define contract semantics.
- Consider compatibility.
- Consider security.
- Consider errors.
- Consider operational behavior.
- Maintain authoritative documentation.
- Validate contracts where practical.

---

# Recommended Practices

Design contracts before implementation.

Use machine-readable specifications.

Automate compatibility checks.

Document examples.

Review consumer impact.

---

# Prohibited Practices

Do not:

- Hide breaking changes.
- Expose sensitive data without justification.
- Make retry semantics ambiguous.
- Treat internal implementation as the API contract by default.
- Allow AI to approve API compatibility independently.

---

# Definition of Done

API review is complete when:

- Problem and consumers are understood.
- Contract is defined.
- Request and response semantics are clear.
- Error behavior is defined.
- Security is addressed.
- Compatibility is assessed.
- Versioning is defined.
- Contract validation passes where applicable.
- Documentation is complete.
- Required approvals are obtained.

---

# Review Checklist

### Contract

- [ ] Capability clear
- [ ] Consumer identified
- [ ] Request defined
- [ ] Response defined
- [ ] Errors defined

### Compatibility

- [ ] Breaking changes identified
- [ ] Versioning defined
- [ ] Deprecation considered
- [ ] Migration path available where required

### Security

- [ ] Authentication
- [ ] Authorization
- [ ] Sensitive data
- [ ] Abuse / rate limiting

### Reliability

- [ ] Timeouts
- [ ] Retries
- [ ] Idempotency
- [ ] Failure behavior

### Operations

- [ ] Observability
- [ ] Capacity
- [ ] Deployment
- [ ] Rollback

### Documentation

- [ ] Authoritative contract
- [ ] Examples
- [ ] Consumer guidance

### Completion

- [ ] Contract validation
- [ ] Required specialist review
- [ ] Required approvals

---

# Engineering Decision

API review protects the stability and usability of machine-consumed contracts.

An API shall be reviewed not merely as a collection of endpoints or schemas, but as a long-lived agreement between producers and consumers.