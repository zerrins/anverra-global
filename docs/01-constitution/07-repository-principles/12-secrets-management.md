---
document: Secrets Management
id: AEC-REP-012
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-REP-010
  - AEC-REP-011
  - AEC-QLT-012
---

# Purpose

Define the constitutional standards governing secrets management within the Anverra Engineering Operating System (AEOS).

Secrets are highly sensitive engineering assets that protect software, infrastructure, data, and identities.

Every secret shall be securely created, stored, distributed, rotated, monitored, revoked, and retired throughout its lifecycle.

---

# Intent

Secrets shall never be treated as configuration.

Secrets shall be managed independently from:

- Source Code
- Configuration Files
- Build Artifacts
- Documentation
- Deployment Packages

Engineering teams shall minimize secret exposure throughout the software lifecycle.

---

# Problem Statement

Poor secrets management commonly results in:

- Credential leakage
- Repository exposure
- Production compromise
- Privilege escalation
- Supply chain attacks
- Long-lived credentials
- Manual credential sharing
- Compliance violations

Once exposed, secrets should be assumed compromised.

---

# Repository Decision

Repositories shall never store production secrets.

Secrets shall be managed using approved secret management systems.

Every secret shall have:

- Owner
- Purpose
- Scope
- Rotation Policy
- Expiration Policy
- Audit History

---

# Rationale

Source code is designed to be shared.

Secrets are designed to be protected.

Combining them creates unnecessary engineering and security risk.

Secrets require independent governance.

---

# Secrets Philosophy

A secret represents an identity or authorization capability.

Every secret grants access.

Every granted access shall be:

- Necessary
- Minimal
- Time Limited
- Auditable
- Revocable

Least privilege applies to every secret.

---

# Secret Categories

AEOS recognizes the following categories.

---

## Authentication Secrets

Examples

- Passwords
- API Keys
- Access Tokens
- Refresh Tokens
- OAuth Credentials

---

## Database Credentials

Examples

- Database Users
- Connection Passwords
- Service Accounts

---

## Cloud Credentials

Examples

- AWS IAM
- Azure Managed Identity
- GCP Service Accounts

Long-lived cloud keys should be avoided.

---

## Encryption Keys

Examples

- AES Keys
- RSA Keys
- JWT Signing Keys
- KMS Keys

---

## Certificates

Examples

- TLS Certificates
- Mutual TLS Certificates
- Code Signing Certificates

---

## Infrastructure Secrets

Examples

- Kubernetes Secrets
- Docker Registry Credentials
- Terraform Credentials

---

## AI Credentials

Examples

- OpenAI API Keys
- Anthropic API Keys
- Azure OpenAI Credentials
- Vector Database Credentials

AI credentials follow identical governance requirements.

---

# Secret Lifecycle

Every secret follows the same lifecycle.

```
Generate

↓

Approve

↓

Store Securely

↓

Distribute Securely

↓

Use

↓

Monitor

↓

Rotate

↓

Revoke

↓

Retire
```

Secret management is continuous.

---

# Secret Generation

Secrets shall:

- Be cryptographically strong
- Meet organizational entropy requirements
- Avoid predictable values
- Be generated using approved tooling

Manual secret creation should be avoided.

---

# Secret Storage

Approved storage includes:

- Azure Key Vault
- AWS Secrets Manager
- Google Secret Manager
- HashiCorp Vault
- Kubernetes External Secrets

Repositories shall store references to secrets—not the secrets themselves.

---

# Secret Distribution

Secrets shall be distributed through:

- Secret Management Services
- Environment Injection
- Managed Identity
- Short-Lived Credentials

Email, chat, and source code repositories are prohibited distribution mechanisms.

---

# Secret Usage

Applications shall retrieve secrets at runtime whenever practical.

Secrets shall not be embedded into:

- Source Code
- Docker Images
- Build Artifacts
- Mobile Applications
- Client-side JavaScript

Secrets should remain external.

---

# Secret Rotation

Every secret shall define a rotation policy.

Recommended frequencies:

| Secret Type | Recommended Rotation |
|--------------|----------------------|
| API Keys | 90 Days |
| Database Passwords | 90 Days |
| Certificates | Before Expiry |
| Access Tokens | Minutes / Hours |
| Service Credentials | Organization Policy |

Emergency rotation shall be supported.

---

# Secret Revocation

Compromised secrets shall be revoked immediately.

Revocation process:

```
Identify

↓

Disable

↓

Replace

↓

Redeploy

↓

Audit

↓

Document
```

---

# Secret Expiration

Secrets should have expiration dates whenever supported.

Long-lived credentials increase organizational risk.

---

# Secret Access

Access shall follow least privilege.

Every secret shall define:

- Owner
- Consumers
- Purpose
- Environment
- Expiration

Secret access shall be logged.

---

# Secret Auditing

Every access to production secrets shall be auditable.

Audit records should include:

- Identity
- Timestamp
- Secret Name
- Action
- Environment

---

# Secret Scanning

Repositories shall implement automated scanning for:

- Passwords
- Tokens
- API Keys
- Certificates
- Private Keys

Builds shall fail when unauthorized secrets are detected.

---

# Secrets in CI/CD

Pipelines shall retrieve secrets securely at runtime.

Secrets shall never be:

- Logged
- Printed
- Cached
- Published
- Embedded in artifacts

Pipeline credentials shall use least privilege.

---

# Secrets in AI Systems

AI agents shall never:

- Generate fake production secrets
- Expose existing credentials
- Log secrets
- Store secrets in prompts
- Recommend hardcoded credentials

AI shall recommend managed identity whenever available.

---

# AI Guidance

AI shall:

- Recommend external secret management.
- Detect hardcoded credentials.
- Recommend credential rotation.
- Use placeholders in documentation.
- Explain secret purpose without exposing values.

---

# Mandatory Rules

Repositories shall:

- Never commit secrets.
- Use approved secret managers.
- Rotate secrets.
- Audit secret access.
- Scan repositories.
- Separate secrets from configuration.
- Document ownership.

---

# Recommended Practices

Prefer managed identities.

Use short-lived credentials.

Rotate automatically.

Review secret usage regularly.

Minimize secret scope.

Encrypt secrets at rest and in transit.

---

# Prohibited Practices

Do not:

- Commit secrets to Git.
- Store secrets in source code.
- Share secrets through email or chat.
- Hardcode credentials.
- Reuse production credentials.
- Disable secret scanning.
- Store secrets in documentation.

---

# Allowed Exceptions

Temporary development credentials may be used only within isolated local development environments.

Such credentials shall:

- Never reach shared repositories.
- Never be reused in higher environments.
- Be clearly identified as non-production.

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Secrets Committed to Repository | 0 |
| Secret Rotation Compliance | 100% |
| Secret Scan Coverage | 100% |
| Unauthorized Secret Access | 0 |
| Managed Identity Adoption | Maximum Practical |

---

# Review Checklist

Reviewers shall verify:

- Are secrets externalized?
- Is secret ownership defined?
- Are rotation policies documented?
- Are repositories free of credentials?
- Are secret scans enabled?
- Is runtime retrieval implemented?
- Are production secrets isolated?

---

# Examples

## Good

```
Application

↓

Managed Identity

↓

Key Vault

↓

Runtime Secret Retrieval
```

---

## Poor

```
Source Code

↓

Database Password

↓

Git Repository

↓

Production
```

---

# Anti-patterns

Hardcoded Credentials

Shared Administrator Accounts

Password by Email

Static API Keys

Secrets in Docker Images

Secrets in Git History

Long-Lived Tokens

Copy-Paste Credentials

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Quality Principles | Mandatory |
| Security Principles | Mandatory |
| Repository Principles | Mandatory |
| AI Engineering Principles | Mandatory |

---

# Engineering Decision

Secrets are governed security assets.

Every secret shall be generated securely, stored externally, accessed using least privilege, rotated regularly, audited continuously, and retired safely.

Repositories shall contain references to secrets—not secrets themselves.

---

# References

- OWASP Secrets Management Cheat Sheet
- NIST SP 800-57
- Azure Key Vault
- HashiCorp Vault
- Engineering Constitution

---

# Related Documents

- Configuration Management
- Environment Management
- Build Standards
- Security Principles
- AI Engineering Principles
- Engineering Governance