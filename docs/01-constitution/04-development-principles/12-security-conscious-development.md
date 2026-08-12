---
document: Security Conscious Development
id: AEC-DEV-012
version: 1.0.0
status: Draft
---

# Purpose

Ensure every implementation protects business assets, customer data, and platform integrity.

---

# Intent

Security is everyone's responsibility.

Every feature shall be designed with security in mind.

---

# Development Decision

Security shall be built into software rather than added afterwards.

---

# Why This Matters to AI

AI often produces functional code that lacks production-grade security.

This document defines mandatory security expectations.

---

# Security Principles

Protect:

- Identity
- Authentication
- Authorization
- Confidentiality
- Integrity
- Availability

---

# Mandatory Rules

- Validate all input.
- Use parameterized queries.
- Never store plaintext passwords.
- Encrypt sensitive data.
- Protect secrets.
- Apply least privilege.
- Audit sensitive operations.

---

# Recommended Practices

- Principle of least privilege.
- Secure defaults.
- Defense in depth.
- Secret rotation.
- Secure logging.

---

# Prohibited Practices

- Hardcoded credentials.
- SQL Injection.
- XSS.
- CSRF vulnerabilities.
- Logging secrets.
- Exposed stack traces.

---

# AI Guidance

AI shall generate secure implementations by default.

AI shall reject insecure patterns.

---

# Review Checklist

- Input validated?
- Authorization enforced?
- Secrets protected?
- Sensitive data encrypted?
- Logs sanitized?

---

# Engineering Decision

Security defects are production defects.

---

# Constitutional Compliance Matrix

| Requirement | Status |
|------------|--------|
| Authentication | Mandatory |
| Authorization | Mandatory |
| Input Validation | Mandatory |
| Encryption | Mandatory |
| Secret Management | Mandatory |

---

# Related Documents

- Defensive Programming
- Error Handling