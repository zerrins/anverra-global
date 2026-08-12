---
document: Dependency Management
id: AEC-REP-009
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-000
  - AEC-REP-006
  - AEC-REP-007
  - AEC-REP-008
---

# Purpose

Define the constitutional standards governing dependency management within repositories managed under the Anverra Engineering Operating System (AEOS).

Dependencies extend software capabilities while introducing engineering, operational, security, licensing, and maintenance responsibilities.

Every dependency shall be intentionally selected, continuously evaluated, and actively governed throughout its lifecycle.

---

# Intent

Dependencies shall be treated as engineering assets rather than implementation shortcuts.

Every dependency should:

- Solve a clearly identified problem.
- Reduce engineering complexity.
- Be actively maintained.
- Be secure.
- Be legally acceptable.
- Be replaceable.
- Be versioned.
- Be documented.

The engineering organization—not the dependency—owns the resulting software.

---

# Problem Statement

Uncontrolled dependency growth frequently results in:

- Security vulnerabilities
- Dependency conflicts
- License violations
- Build instability
- Excessive transitive dependencies
- Vendor lock-in
- Upgrade failures
- Operational risk
- AI-generated unnecessary libraries

These issues accumulate into significant engineering debt.

---

# Repository Decision

Every repository shall implement a controlled dependency management strategy.

Dependencies shall be introduced only after technical and business evaluation.

Every dependency requires ongoing governance.

---

# Rationale

Modern software is primarily assembled rather than written from scratch.

Engineering quality depends upon responsible dependency selection and lifecycle management.

Dependencies influence:

- Security
- Performance
- Maintainability
- Compatibility
- Compliance
- Long-term sustainability

---

# Dependency Philosophy

Every dependency represents an engineering decision.

Before adding a dependency, engineers shall ask:

- Why is it needed?
- What problem does it solve?
- Can existing capabilities solve this problem?
- What are the long-term maintenance costs?
- What risks does it introduce?

Dependencies are strategic engineering investments.

---

# Dependency Principles

Every dependency shall be:

## Necessary

Only introduce dependencies that provide measurable engineering value.

---

## Maintained

Dependencies shall demonstrate active maintenance.

Indicators include:

- Regular releases
- Security patches
- Active community
- Responsive maintainers

---

## Secure

Dependencies shall undergo vulnerability assessment before adoption.

---

## Compatible

Dependencies shall align with repository technology standards.

---

## Replaceable

Repositories should minimize vendor lock-in.

Replacement strategies should remain feasible.

---

## Observable

Dependency usage shall remain visible through documentation and SBOM generation.

---

# Dependency Lifecycle

Every dependency follows the same lifecycle.

```
Engineering Need

↓

Evaluation

↓

Approval

↓

Implementation

↓

Security Scan

↓

License Validation

↓

Continuous Monitoring

↓

Version Updates

↓

Deprecation

↓

Removal
```

Dependency governance is continuous.

---

# Dependency Categories

## Runtime Dependencies

Required during application execution.

Examples:

- Spring Boot
- React
- PostgreSQL Drivers

---

## Build Dependencies

Required only during builds.

Examples:

- Maven Plugins
- Gradle Plugins
- Babel
- TypeScript

---

## Development Dependencies

Required only during development.

Examples:

- Linters
- Testing Frameworks
- Mock Libraries

---

## Infrastructure Dependencies

Support deployment and operations.

Examples:

- Terraform Providers
- Kubernetes Operators
- Docker Images

---

## AI Dependencies

Support AI-assisted engineering.

Examples:

- Prompt Libraries
- AI SDKs
- Embedding Frameworks
- Model Clients

AI dependencies follow identical governance rules.

---

# Dependency Selection Criteria

Before adoption, evaluate:

- Business value
- Community adoption
- Maintenance activity
- Release frequency
- Security history
- Documentation quality
- License compatibility
- Performance
- Compatibility
- Replacement difficulty

No dependency shall be introduced solely because it is popular.

---

# Version Management

Repositories shall:

- Pin dependency versions.
- Avoid floating versions.
- Document upgrade strategy.
- Review version changes.

Examples

Good

```
3.5.2
```

Poor

```
latest

*

+
```

Deterministic builds require deterministic dependency versions.

---

# Dependency Locking

Repositories shall maintain dependency lock files whenever supported.

Examples:

- package-lock.json
- pnpm-lock.yaml
- poetry.lock
- Cargo.lock
- go.sum

Lock files ensure reproducible builds.

---

# Transitive Dependencies

Transitive dependencies shall be monitored continuously.

Engineering teams shall understand:

- Why they exist.
- Security implications.
- Version conflicts.
- Upgrade impact.

Hidden dependencies remain engineering responsibility.

---

# Security Management

Every dependency shall undergo:

- Vulnerability Scanning
- CVE Monitoring
- Supply Chain Validation
- Integrity Verification

Critical vulnerabilities require immediate assessment.

Repositories shall never knowingly ship critical vulnerabilities without documented risk acceptance.

---

# License Compliance

Every dependency shall use an approved software license.

Examples of commonly accepted licenses:

- MIT
- Apache 2.0
- BSD

Restricted or incompatible licenses require legal review.

License compliance is mandatory.

---

# Dependency Updates

Dependencies shall be reviewed periodically.

Categories:

Security Updates

↓

Bug Fixes

↓

Minor Versions

↓

Major Versions

Major upgrades require engineering assessment.

---

# Deprecation Strategy

Deprecated dependencies shall follow:

```
Identify

↓

Evaluate Replacement

↓

Migration Plan

↓

Implementation

↓

Removal
```

Unsupported dependencies shall not remain indefinitely.

---

# Internal Dependencies

Internal libraries shall:

- Follow Semantic Versioning.
- Publish release notes.
- Document compatibility.
- Maintain API stability.

Internal dependencies require the same governance as external dependencies.

---

# Artifact Repositories

Dependencies shall originate only from approved artifact repositories.

Examples:

- Maven Central
- Internal Artifact Repository
- npm Registry
- GitHub Packages

Untrusted repositories shall not be used.

---

# SBOM Integration

Every production build shall include dependency information in the Software Bill of Materials.

The SBOM shall include:

- Direct dependencies
- Transitive dependencies
- Versions
- Licenses
- Suppliers

---

# AI Guidance

AI shall:

- Prefer existing repository dependencies.
- Avoid introducing duplicate libraries.
- Recommend well-maintained packages.
- Warn about abandoned projects.
- Consider license compatibility.
- Evaluate security implications.
- Avoid unnecessary dependencies.

AI shall never introduce a dependency without explaining its purpose.

---

# Mandatory Rules

Repositories shall:

- Review all new dependencies.
- Pin dependency versions.
- Maintain lock files.
- Scan for vulnerabilities.
- Validate licenses.
- Generate SBOMs.
- Monitor dependency health.

---

# Recommended Practices

Prefer mature libraries.

Minimize dependency count.

Review dependency usage regularly.

Remove unused dependencies.

Automate vulnerability scanning.

Monitor release notes.

Document major dependency decisions.

---

# Prohibited Practices

Do not:

- Use "latest" versions.
- Introduce duplicate libraries.
- Ignore vulnerability reports.
- Ignore license compatibility.
- Depend upon abandoned projects.
- Commit unapproved dependencies.
- Circumvent dependency governance.

---

# Allowed Exceptions

Experimental repositories may temporarily evaluate candidate dependencies.

Experimental dependencies shall not enter production without completing the full governance process.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Approved Dependency Usage | 100% |
| Critical Vulnerabilities Released | 0 |
| Dependency Lock Compliance | 100% |
| License Compliance | 100% |
| SBOM Coverage | 100% |
| Unused Dependencies | 0 |

---

# Review Checklist

Reviewers shall verify:

- Is the dependency necessary?
- Is it actively maintained?
- Is the version pinned?
- Has security been assessed?
- Is the license approved?
- Is the dependency documented?
- Is the SBOM updated?
- Does an existing dependency already solve this problem?

---

# Examples

## Good

```
Need JSON Serialization

↓

Existing Jackson Library

↓

Reuse Existing Dependency
```

---

```
Need Object Mapping

↓

Evaluate Existing Mapper

↓

Reuse MapStruct

↓

No Additional Library
```

---

## Poor

```
Need Logging

↓

Add Three Logging Libraries

↓

Duplicate Functionality

↓

Version Conflicts
```

---

# Anti-patterns

Dependency Explosion

Framework Chasing

Latest Version Syndrome

Copy-Paste Dependencies

Unmaintained Libraries

Transitive Dependency Blindness

Ignoring License Risk

Dependency by Convenience

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Engineering Principles | Mandatory |
| Repository Principles | Mandatory |

---

# Engineering Decision

Dependencies are governed engineering assets.

Every dependency shall be intentionally selected, continuously monitored, securely maintained, and periodically reviewed to preserve the stability, security, and maintainability of the engineering ecosystem.

Dependency governance is a continuous engineering responsibility.

---

# References

- Semantic Versioning 2.0.0
- SPDX
- CycloneDX
- OWASP Dependency-Check
- Supply-chain Levels for Software Artifacts (SLSA)
- Engineering Constitution

---

# Related Documents

- Branching Strategy
- Git Standards
- Versioning
- Build Standards
- Security Principles
- AI Engineering Principles
- Engineering Governance