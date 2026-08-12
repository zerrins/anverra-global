---
document: Security Governance
id: AEC-GOV-008
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering Security
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-003
  - AEC-GOV-004
  - AEC-SEC-000
  - AEC-SEC-001
  - AEC-SEC-002
  - AEC-REV-008
---

# Purpose

Define the governance model through which security is owned, controlled, measured, reviewed, and continuously improved across engineering.

---

# Intent

Security governance should establish:

- Security ownership.
- Minimum security expectations.
- Security decision authority.
- Risk management.
- Security review requirements.
- Exception handling.
- Security escalation.
- Security measurement.
- Continuous improvement.

---

# Constitutional Decision

Security is a shared engineering responsibility governed by explicit security standards, specialist expertise, risk-based controls, and accountable decision-making.

Security governance shall protect the organization without unnecessarily preventing legitimate engineering work.

---

# Security Governance Model

```text
Security Principles
        ↓
Security Policies
        ↓
Security Standards
        ↓
Engineering Controls
        ↓
Security Validation
        ↓
Monitoring
        ↓
Incident Response
        ↓
Continuous Improvement
```

---

# Security Ownership

Security responsibility exists at multiple levels.

```text
Engineering Leadership
        ↓
Security Governance
        ↓
System Owners
        ↓
Engineering Teams
        ↓
Individual Changes
```

---

# Security Specialist Responsibility

Security specialists provide:

- Security expertise.
- Threat analysis.
- Security architecture guidance.
- Security standards.
- Security assessments.
- Vulnerability guidance.
- Security governance.

They do not become the sole owners of security outcomes.

---

# System Owner Security Responsibility

System owners are accountable for the security of systems they own.

Responsibilities include:

- Understanding security risks.
- Applying security standards.
- Addressing vulnerabilities.
- Maintaining secure configuration.
- Ensuring appropriate access controls.
- Participating in security incidents.

---

# Engineer Security Responsibility

Engineers are responsible for:

- Secure implementation.
- Appropriate authentication.
- Authorization.
- Input validation.
- Secret handling.
- Dependency security.
- Security testing.

---

# Security Principles

Security governance should follow:

1. Least privilege.
2. Defense in depth.
3. Secure defaults.
4. Explicit trust boundaries.
5. Fail safely.
6. Minimize sensitive data.
7. Verify rather than assume.
8. Monitor security-relevant behavior.

---

# Risk-Based Security

Security controls should reflect:

```text
Asset Value
+
Threat
+
Exposure
+
Impact
+
Likelihood
```

High-risk systems require stronger controls.

---

# Security Classification

Systems and data may be classified according to organizational requirements.

Examples:

```text
Public
Internal
Confidential
Restricted
```

The exact classification scheme may vary.

---

# Identity and Access Governance

Security governance should establish:

- Authentication requirements.
- Authorization requirements.
- Least privilege.
- Privileged access controls.
- Access reviews.
- Credential lifecycle.

---

# Secrets Governance

Secrets should be:

- Stored securely.
- Access-controlled.
- Rotated appropriately.
- Scanned for accidental exposure.
- Removed from source code where possible.

Examples:

- Passwords.
- API keys.
- Tokens.
- Private keys.
- Connection credentials.

---

# Dependency Security

Engineering teams should maintain awareness of dependency risks.

Governance should establish expectations for:

- Vulnerability scanning.
- Critical vulnerability remediation.
- Dependency updates.
- Unsupported dependencies.
- Security exceptions.

---

# Vulnerability Management

A vulnerability lifecycle may be:

```text
Detection
   ↓
Classification
   ↓
Ownership
   ↓
Risk Assessment
   ↓
Remediation
   ↓
Verification
   ↓
Closure
```

---

# Vulnerability Ownership

A vulnerability should have an accountable owner.

Finding a vulnerability without assigning responsibility does not reduce risk.

---

# Critical Vulnerabilities

Critical vulnerabilities should receive expedited handling according to organizational risk policy.

Possible actions include:

- Immediate mitigation.
- Temporary containment.
- Emergency patching.
- Service isolation.
- Compensating controls.

---

# Security Exceptions

Exceptions may be required when a control cannot reasonably be implemented immediately.

An exception should document:

- Requirement.
- Reason.
- Risk.
- Compensating control.
- Owner.
- Approval.
- Review date.

---

# Security Risk Acceptance

Residual security risk should be accepted by an appropriately authorized risk owner.

Engineers should not silently accept organizational security risk outside their authority.

---

# Security Review Triggers

Security review may be required for:

- Authentication changes.
- Authorization changes.
- Sensitive data processing.
- External exposure.
- Cryptographic changes.
- Identity systems.
- Major infrastructure changes.
- New third-party integrations.

---

# Security by Design

Security should be considered during:

```text
Requirements
    ↓
Architecture
    ↓
Design
    ↓
Implementation
    ↓
Testing
    ↓
Deployment
```

Security should not be introduced only immediately before production.

---

# Threat Modeling

Threat modeling should be used where system risk justifies it.

It should identify:

- Assets.
- Actors.
- Trust boundaries.
- Threats.
- Mitigations.
- Residual risk.

---

# Security Testing

Appropriate security testing may include:

- Static analysis.
- Dependency scanning.
- Secret scanning.
- Dynamic testing.
- Penetration testing.
- Authorization testing.
- Configuration validation.

---

# Security Monitoring

Security-relevant systems should provide appropriate observability.

Examples:

- Authentication events.
- Authorization failures.
- Privileged actions.
- Suspicious activity.
- Configuration changes.

---

# Security Incidents

Security incidents should follow an explicit response process.

```text
Detection
   ↓
Containment
   ↓
Investigation
   ↓
Eradication
   ↓
Recovery
   ↓
Lessons Learned
```

---

# Security and Production

Production security responsibilities should be understood before deployment.

Consider:

- Access.
- Secrets.
- Network exposure.
- Monitoring.
- Logging.
- Recovery.

---

# Security and AI

AI systems should follow the same security principles as other engineering systems.

Additional considerations may include:

- Prompt/data exposure.
- Model access.
- Tool permissions.
- Generated output.
- Autonomous actions.

---

# Security and Third Parties

Third-party integrations should be evaluated according to:

- Data access.
- Trust.
- Authentication.
- Authorization.
- Dependency risk.
- Operational impact.

---

# Security Governance Metrics

Useful indicators include:

- Critical vulnerabilities.
- Mean time to remediation.
- Unresolved security exceptions.
- Security incident frequency.
- Secret exposure events.
- High-risk systems without security review.

Metrics should support risk reduction rather than create reporting theater.

---

# Security Governance Review

Security governance should periodically assess:

- Threat changes.
- Vulnerability trends.
- Incident lessons.
- Control effectiveness.
- Architecture changes.
- New technologies.

---

# Security Anti-Patterns

## Security as Final Gate

Security is considered only before production.

## Security Ownership Transfer

Engineering assumes security owns all security outcomes.

## Vulnerability Without Owner

Findings exist without accountable remediation.

## Permanent Exception

A temporary security exception becomes permanent.

## Control Without Risk

Security controls are imposed without understanding the actual threat.

---

# Mandatory Rules

Security governance shall:

- Define ownership.
- Establish minimum security controls.
- Provide risk assessment.
- Define vulnerability ownership.
- Control security exceptions.
- Support security incident response.
- Review security governance periodically.

---

# Recommended Practices

Automate security checks.

Use threat modeling for high-risk changes.

Integrate security into development.

Keep security standards understandable.

Review security exceptions regularly.

---

# Prohibited Practices

Do not:

- Leave critical vulnerabilities unowned.
- Store secrets unnecessarily in source code.
- Treat security review as a replacement for secure engineering.
- Allow unauthorized risk acceptance.
- Make AI the sole decision-maker for security-critical actions.

---

# Definition of Done

Security governance is effective when:

- Security ownership is clear.
- Applicable controls are defined.
- High-risk changes trigger appropriate review.
- Vulnerabilities have owners.
- Exceptions are controlled.
- Security incidents generate learning.
- Security standards evolve with threats.

---

# Engineering Decision

Security governance shall establish clear accountability and proportional controls while ensuring that security remains integrated into normal engineering practice.

Security is not a separate phase of engineering.

It is a property of the entire engineering lifecycle.