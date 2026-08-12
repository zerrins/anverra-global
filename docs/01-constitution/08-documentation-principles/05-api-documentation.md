---
document: API Documentation
id: AEC-DOC-005
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-DOC-002
  - AEC-DOC-003
  - AEC-ARC-000
  - AEC-DEV-007
---

# Purpose

Define the constitutional standards for documenting application programming interfaces and other machine-to-machine contracts within AEOS.

API documentation is a contract between producers and consumers.

It shall communicate how an interface behaves, what it accepts, what it returns, what failures mean, and how the contract evolves.

---

# Intent

An engineer or system consuming an API should be able to understand the interface without inspecting the implementation.

API documentation shall answer:

- What does the API do?
- What endpoints or operations exist?
- What inputs are required?
- What outputs are returned?
- What errors can occur?
- How is authentication performed?
- What are the compatibility guarantees?
- Which version should consumers use?

---

# Problem Statement

Poor API documentation causes:

- Integration failures
- Incorrect client implementations
- Repeated questions
- Hidden breaking changes
- Inconsistent error handling
- Security misunderstandings
- Slow development
- Consumer dependency on implementation details

An undocumented API is effectively an undocumented dependency.

---

# Constitutional Decision

Every externally consumed API shall have authoritative, version-aware documentation.

Where a machine-readable contract is appropriate, the contract shall be maintained in a structured specification.

Examples include:

- OpenAPI
- AsyncAPI
- GraphQL schemas
- gRPC/protobuf
- JSON Schema
- Event schemas

---

# Rationale

APIs are contracts.

Consumers should not need access to source code to understand an API.

Machine-readable specifications additionally enable:

- Validation
- Client generation
- Testing
- Contract checking
- Documentation generation
- AI-assisted understanding

---

# API Documentation Philosophy

## Contract First

The API contract is the authoritative representation of externally visible behavior.

---

## Consumer Focused

Documentation shall explain how consumers use the interface.

It should not require consumers to understand internal implementation.

---

## Explicit

Important behavior shall not depend on undocumented assumptions.

---

## Version Aware

Changes to contracts shall communicate compatibility implications.

---

## Machine Readable

Where practical, API definitions should be machine-readable.

---

# API Documentation Scope

API documentation should cover:

- Endpoints
- Operations
- Parameters
- Request bodies
- Response bodies
- Headers
- Authentication
- Authorization
- Errors
- Status codes
- Pagination
- Filtering
- Sorting
- Rate limits
- Idempotency
- Versioning
- Examples

---

# REST API Documentation

REST APIs should document:

- HTTP method
- Path
- Purpose
- Parameters
- Request body
- Response
- Status codes
- Authentication
- Authorization
- Errors

Example:

```text
POST /policies

Purpose:
Create a policy.

Request:
PolicyCreateRequest

Response:
201 Created

Errors:
400 Invalid Request
401 Unauthorized
403 Forbidden
409 Policy Conflict
```

---

# Request Documentation

Requests shall clearly identify:

- Required fields
- Optional fields
- Types
- Constraints
- Allowed values
- Default values
- Validation rules

---

# Response Documentation

Responses shall document:

- Fields
- Types
- Meaning
- Nullability
- Optionality
- Relationships

---

# Error Documentation

API errors shall be documented consistently.

An error should communicate:

- Error category
- HTTP/status code
- Machine-readable identifier where applicable
- Human-readable message
- Remediation guidance where appropriate

Example:

```json
{
  "code": "POLICY_NOT_FOUND",
  "message": "The requested policy does not exist."
}
```

---

# Authentication Documentation

Documentation shall explain:

- Authentication mechanism
- Required credentials
- Token requirements
- Expiration behavior
- Refresh behavior

Secrets shall never appear in documentation.

---

# Authorization Documentation

Documentation should explain required permissions where consumers need to understand authorization behavior.

Examples:

```text
policy:read
policy:create
policy:update
```

---

# Pagination

Paginated APIs shall document:

- Page size
- Maximum page size
- Page numbering or cursor behavior
- Next-page semantics
- Ordering guarantees

---

# Filtering and Sorting

Document:

- Supported filters
- Supported sort fields
- Sort direction
- Default ordering
- Unsupported combinations

---

# Idempotency

Where operations are idempotent or support idempotency keys, documentation shall explain the behavior.

---

# Rate Limiting

Where applicable, document:

- Limits
- Time windows
- Headers
- Retry behavior
- Error behavior

---

# Event Documentation

Event-driven systems shall document:

- Event name
- Producer
- Consumers
- Schema
- Delivery semantics
- Ordering
- Retry behavior
- Duplicate handling
- Versioning

---

# AsyncAPI and Event Contracts

Where practical, event contracts should use machine-readable specifications.

The contract should be authoritative for:

- Message structure
- Channels
- Producers
- Consumers
- Payload schema

---

# GraphQL Documentation

GraphQL APIs shall document:

- Schema
- Queries
- Mutations
- Subscriptions
- Types
- Arguments
- Authentication
- Authorization
- Deprecations

---

# gRPC Documentation

gRPC services shall maintain authoritative protobuf definitions.

Documentation should communicate:

- Service methods
- Request messages
- Response messages
- Error semantics
- Compatibility requirements

---

# API Examples

Important APIs should provide realistic examples.

Examples should demonstrate:

- Request
- Response
- Error
- Authentication where relevant

Examples shall not expose credentials or sensitive data.

---

# Contract and Implementation

The implementation shall conform to the published contract.

If implementation and contract disagree:

1. Determine the authoritative intended behavior.
2. Correct the implementation or contract.
3. Assess consumer impact.
4. Document breaking changes where necessary.

---

# API Versioning

API versioning shall follow the organization's compatibility strategy.

Versioning shall be used to manage meaningful contract evolution—not as a substitute for disciplined compatibility management.

---

# Breaking Changes

Breaking changes include:

- Removing fields
- Changing field meaning
- Changing required behavior
- Removing operations
- Changing authentication requirements
- Changing error semantics incompatibly

Breaking changes shall follow the applicable review and release process.

---

# Deprecation

Deprecated APIs shall clearly communicate:

- Deprecation status
- Recommended replacement
- Timeline where known
- Compatibility expectations

---

# API Documentation Lifecycle

```text
API Design

↓

Contract Definition

↓

Implementation

↓

Contract Validation

↓

Publication

↓

Consumer Usage

↓

Evolution

↓

Deprecation

↓

Retirement
```

---

# API Documentation Validation

Automation should validate:

- Specification syntax
- Schema correctness
- Examples
- Links
- Contract consistency

Where practical, contract tests should verify implementation against the documented contract.

---

# AI Guidance

AI shall:

- Read existing API contracts before modifying APIs.
- Update specifications with interface changes.
- Detect undocumented endpoints.
- Detect implementation/contract mismatches.
- Generate examples.
- Identify breaking changes.
- Preserve established API conventions.

AI shall never invent API behavior that is not supported by requirements or implementation evidence.

---

# Human Responsibilities

Humans remain responsible for:

- API design
- Business semantics
- Compatibility decisions
- Security requirements
- Versioning strategy
- Consumer impact

AI assists documentation and validation.

---

# Mandatory Rules

External APIs shall:

- Have authoritative documentation.
- Define request and response contracts.
- Document errors.
- Document authentication.
- Document compatibility behavior.
- Keep implementation and contract synchronized.

---

# Recommended Practices

Prefer contract-first development.

Use machine-readable specifications.

Automate contract validation.

Provide examples.

Document common integration scenarios.

Maintain changelogs for significant API changes.

---

# Prohibited Practices

Do not:

- Publish undocumented breaking changes.
- Expose internal implementation as the API contract.
- Include credentials in examples.
- Maintain multiple conflicting API specifications.
- Leave deprecated APIs without clear status.

---

# Allowed Exceptions

Internal prototypes may use lightweight API documentation during exploration.

Production-facing interfaces shall satisfy full API documentation requirements.

---

# Success Metrics

| Metric | Target |
|---|---:|
| External API Documentation Coverage | 100% |
| Contract Validation | 100% |
| Undocumented Breaking Changes | 0 |
| API Documentation Drift | 0 |
| Credential Exposure | 0 |
| Critical API Examples Valid | 100% |

---

# Review Checklist

Verify:

- Contract exists.
- Requests documented.
- Responses documented.
- Errors documented.
- Authentication documented.
- Authorization documented.
- Versioning defined.
- Breaking changes identified.
- Examples valid.
- Contract matches implementation.

---

# Examples

## Good

```text
OpenAPI Specification
        ↓
Contract Tests
        ↓
Implementation
        ↓
Generated Documentation
        ↓
Consumer Integration
```

---

## Poor

```text
Developer Implements Endpoint

↓

Consumer Calls Endpoint

↓

Consumer Discovers Behavior Through Errors
```

---

# Anti-patterns

Documentation After Implementation

Contract Drift

Undocumented Breaking Changes

Implementation as Contract

Example Drift

API Archaeology

Undocumented Errors

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

---

# Engineering Decision

API documentation is part of the API itself.

Externally consumed interfaces shall maintain accurate, discoverable, version-aware, machine-readable where practical, and consumer-oriented contracts.

API implementation and API documentation shall evolve together.

---

# References

- OpenAPI
- AsyncAPI
- GraphQL
- gRPC
- JSON Schema
- REST
- API Design Principles

---

# Related Documents

- Documentation Philosophy
- Documentation Standards
- Operational Documentation
- Decision Documentation
- API Design
- Backward Compatibility
- AI Documentation