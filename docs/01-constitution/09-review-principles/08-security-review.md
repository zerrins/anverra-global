---
document: Security Review
id: AEC-REV-008
version: 1.0.0
status: Draft
stability: Level 3
owner: Security Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-REV-001
  - AEC-REV-002
  - AEC-REV-003
  - AEC-REV-004
  - AEC-REV-005
  - AEC-ARC-000
  - AEC-SEC-000
---

# Purpose

Define the principles, scope, workflow, responsibilities, and completion criteria for security review within the Anverra Engineering Operating System (AEOS).

Security review identifies and evaluates security risks before engineering changes become difficult or dangerous to correct.

---

# Intent

Security review should answer:

- What assets are affected?
- What trust boundaries exist?
- Who can perform the operation?
- What could an attacker do?
- What data is exposed?
- What privileges are introduced?
- What happens when security controls fail?
- How is misuse detected?
- Are security assumptions supported by evidence?

---

# Constitutional Decision

Engineering changes with meaningful security impact shall receive security review proportional to their risk.

Security shall be considered throughout the engineering lifecycle rather than only immediately before production deployment.

---

# Security Review Philosophy

Security review is not limited to finding vulnerabilities.

It should evaluate:

```text
Assets
   ↓
Threats
   ↓
Trust Boundaries
   ↓
Controls
   ↓
Failure Modes
   ↓
Detection
   ↓
Recovery
```

---

# Security Review Triggers

Security review should be considered for changes involving:

- Authentication.
- Authorization.
- Identity.
- Secrets.
- Cryptography.
- Personal or sensitive data.
- External exposure.
- Network boundaries.
- Administrative capabilities.
- Privilege changes.
- File access.
- Data export.
- Third-party integrations.
- Infrastructure security.
- Security-sensitive configuration.

---

# Security Classification

Security review depth should be proportional to:

- Data sensitivity.
- Exposure.
- Privilege.
- Attack surface.
- Business impact.
- Exploitability.
- Reversibility.

---

# Assets

Review should identify assets such as:

- Credentials.
- Tokens.
- Personal data.
- Financial information.
- Business data.
- Source code.
- Infrastructure.
- Databases.
- APIs.
- Administrative capabilities.

---

# Trust Boundaries

Identify where trust changes.

Examples:

```text
Internet
   ↓
API Gateway
   ↓
Application
   ↓
Database
```

or:

```text
User
   ↓
Identity Provider
   ↓
Application
   ↓
Privileged Service
```

Each boundary should have appropriate controls.

---

# Threat Modeling

Security-sensitive designs should consider:

- Who can attack the system?
- What capabilities do they have?
- What assets are attractive?
- What attack paths exist?
- What controls prevent or limit attacks?

The level of threat modeling should match risk.

---

# Authentication

Review:

- Identity verification.
- Credential handling.
- Token validation.
- Session management.
- Expiration.
- Rotation.
- Revocation.
- Failure behavior.

Authentication establishes identity; it does not establish permission.

---

# Authorization

Review:

- Resource-level access.
- Role permissions.
- Object-level access.
- Tenant boundaries.
- Administrative privileges.
- Privilege escalation.

Authorization should be evaluated for both expected and malicious usage.

---

# Least Privilege

Systems should receive only the permissions necessary for their function.

Review should identify:

- Excessive service permissions.
- Excessive user privileges.
- Broad database access.
- Unnecessary administrative capabilities.

---

# Secrets

Review whether:

- Secrets are stored securely.
- Secrets are excluded from source control.
- Secrets are not logged.
- Rotation is possible.
- Access is appropriately restricted.

---

# Sensitive Data

Review:

- Collection.
- Storage.
- Transmission.
- Logging.
- Caching.
- Export.
- Deletion.

Only necessary data should be exposed.

---

# Input Handling

Review inputs for:

- Validation.
- Injection.
- Encoding.
- Size limits.
- Unexpected values.
- Malformed input.

Never rely on client-side validation alone for security.

---

# Injection Risks

Consider:

- SQL injection.
- Command injection.
- Template injection.
- Script injection.
- Query injection.
- Header injection.

The applicable injection classes depend on the technology.

---

# Dependency Security

Review significant dependencies for:

- Known vulnerabilities.
- Trustworthiness.
- Maintenance.
- Version.
- Supply-chain risk.

Automated dependency scanning should be used where practical.

---

# Cryptography

Where cryptography is involved, review:

- Algorithm choice.
- Key management.
- Key rotation.
- Randomness.
- Storage.
- Failure behavior.

Do not design custom cryptographic algorithms unless there is a compelling and expert-reviewed reason.

---

# Logging and Security

Security-sensitive logging should support detection without exposing sensitive information.

Review:

- Authentication failures.
- Authorization failures.
- Suspicious activity.
- Administrative operations.

Avoid logging:

- Passwords.
- Tokens.
- Private keys.
- Sensitive personal data.

---

# Rate Limiting

Review whether abuse controls are required.

Examples:

- Login.
- OTP.
- Password reset.
- Public APIs.
- Expensive operations.

Rate limiting should consider:

- Scope.
- Identity.
- IP.
- Resource.
- Distributed behavior.

---

# Abuse Cases

Security review should consider intentional misuse.

Examples:

```text
Normal Request

vs

Repeated Request

vs

Malformed Request

vs

Unauthorized Request

vs

Automated Abuse
```

---

# Failure Behavior

Security controls should fail safely.

Review:

- Authentication service unavailable.
- Authorization dependency unavailable.
- Key service unavailable.
- Configuration missing.
- Token validation failure.

Do not automatically choose fail-open behavior.

---

# Security Monitoring

Consider whether security-relevant behavior is observable.

Examples:

- Login failures.
- Privilege changes.
- Administrative operations.
- Suspicious access.
- Unusual data export.

---

# Incident Response

For critical security controls, consider:

- Detection.
- Alerting.
- Containment.
- Credential revocation.
- Recovery.
- Forensics.

---

# Security Review Workflow

```text
Change
  ↓
Asset Identification
  ↓
Threat Analysis
  ↓
Control Analysis
  ↓
Security Review
  ↓
Findings
  ↓
Remediation
  ↓
Validation
  ↓
Approval
```

---

# Security Findings

Security findings should include:

- Description.
- Impact.
- Likelihood where known.
- Affected component.
- Recommended mitigation.
- Validation status.

---

# Severity

Security severity should reflect actual risk.

A useful conceptual model is:

```text
Risk = Likelihood × Impact
```

Additional factors may include:

- Exposure.
- Exploitability.
- Detectability.
- Existing controls.

---

# Security Evidence

Evidence may include:

- Threat models.
- Security tests.
- Static analysis.
- Dependency scans.
- Penetration testing.
- Configuration validation.
- Access-control tests.

---

# Security Review and Code Review

Security review complements code review.

Code review may identify:

```text
"This code has an authorization bug."
```

Security review may identify:

```text
"This workflow's authorization model is insufficient even if the code is internally consistent."
```

---

# Security Review and Architecture

Architecture review should establish security boundaries.

Security review should validate whether those boundaries provide appropriate protection.

---

# Security Review and API Review

API review should identify:

- Authentication.
- Authorization.
- Data exposure.
- Abuse controls.

Security review provides deeper analysis where risk requires it.

---

# AI-Assisted Security Review

AI may assist with:

- Vulnerability detection.
- Threat identification.
- Security checklist generation.
- Suspicious pattern detection.
- Permission analysis.

AI findings must be validated.

AI shall not be treated as a complete security control.

---

# AI-Generated Security Changes

AI-generated security code requires the same or greater scrutiny as human-written security code.

The origin of the code does not reduce security responsibility.

---

# Security Exceptions

Security exceptions shall be:

- Explicit.
- Risk-assessed.
- Owned.
- Time-bounded where appropriate.
- Approved by the appropriate authority.

---

# Mandatory Rules

Security review shall:

- Identify security impact.
- Evaluate trust boundaries.
- Evaluate authentication and authorization where applicable.
- Protect sensitive data.
- Consider abuse.
- Consider failure behavior.
- Resolve critical security findings before acceptance.

---

# Recommended Practices

Threat-model high-risk changes.

Automate vulnerability scanning.

Use security specialists for high-risk changes.

Test authorization explicitly.

Review failure behavior.

---

# Prohibited Practices

Do not:

- Treat security as a final deployment-only activity.
- Store secrets in source code.
- Ignore authorization because authentication exists.
- Treat passing static analysis as proof of security.
- Allow AI to independently approve security changes.

---

# Definition of Done

Security review is complete when:

- Security impact is understood.
- Assets are identified.
- Relevant threats are considered.
- Controls are evaluated.
- Required security tests are complete.
- Critical findings are resolved.
- Required approvals are obtained.

---

# Review Checklist

### Assets

- [ ] Assets identified
- [ ] Data sensitivity considered
- [ ] Privileges identified

### Boundaries

- [ ] Trust boundaries
- [ ] Authentication
- [ ] Authorization

### Threats

- [ ] Abuse cases
- [ ] Injection
- [ ] Privilege escalation
- [ ] Data exposure

### Controls

- [ ] Secrets
- [ ] Cryptography
- [ ] Rate limiting
- [ ] Monitoring

### Verification

- [ ] Security tests
- [ ] Automated scans
- [ ] Specialist review where required

### Completion

- [ ] Critical findings resolved
- [ ] Exceptions documented
- [ ] Approval obtained

---

# Engineering Decision

Security review shall identify and reduce security risk throughout the engineering lifecycle.

Security shall be treated as a property of the system's architecture, design, implementation, operations, and behavior rather than as a separate final-stage inspection.