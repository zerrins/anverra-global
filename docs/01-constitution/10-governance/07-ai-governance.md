---
document: AI Governance
id: AEC-GOV-007
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering AI Governance
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-003
  - AEC-REV-011
  - AEC-AI-000
  - AEC-AI-003
  - AEC-AI-006
---

# Purpose

Define the governance framework for the use of artificial intelligence within engineering.

AI governance establishes boundaries for safe, responsible, effective, and accountable AI-assisted engineering.

---

# Intent

AI governance should answer:

- Which AI uses are permitted?
- Which AI tools are approved?
- What information may be provided to AI systems?
- Who remains accountable for AI-generated output?
- Which AI uses require additional review?
- How should AI-generated code be validated?
- How should AI risks be managed?
- How should AI governance evolve?

---

# Constitutional Decision

AI is an engineering capability, not an autonomous source of engineering authority.

Humans remain accountable for engineering decisions and outcomes produced with AI assistance.

---

# AI Governance Principles

## 1. Human Accountability

AI does not own:

- Architecture decisions.
- Security decisions.
- Production risk.
- Business decisions.
- Engineering outcomes.

Humans remain accountable.

---

# 2. Appropriate Use

AI should be used where it provides meaningful value.

Examples:

- Code generation.
- Code explanation.
- Refactoring assistance.
- Test generation.
- Documentation.
- Review assistance.
- Debugging.
- Research.

---

# 3. Validation

AI output must be validated according to its risk.

```text
AI Output
   ↓
Validation
   ↓
Engineering Acceptance
```

---

# 4. Data Protection

AI systems must not receive information that the user is not authorized to disclose to that AI system.

---

# 5. Least Context

Provide AI with the minimum information necessary to perform the task effectively.

---

# 6. Transparency

Where required by organizational policy, meaningful AI use should be identifiable.

---

# 7. Risk Proportionality

AI governance requirements should depend on:

- Use case.
- Data sensitivity.
- Impact.
- Autonomy.
- Reversibility.

---

# AI Use Categories

AI usage may be classified as:

```text
Category A — Productivity Assistance
Category B — Engineering Generation
Category C — Engineering Decision Support
Category D — Operational Assistance
Category E — High-Impact / Autonomous Use
```

---

# Category A — Productivity Assistance

Examples:

- Summarization.
- Writing assistance.
- Brainstorming.
- Documentation formatting.

Generally low risk.

---

# Category B — Engineering Generation

Examples:

- Code generation.
- Test generation.
- Configuration generation.
- Documentation generation.

Requires normal engineering validation.

---

# Category C — Decision Support

Examples:

- Architecture analysis.
- Security analysis.
- Performance analysis.
- Technical recommendations.

AI output should be treated as analysis rather than authority.

---

# Category D — Operational Assistance

Examples:

- Incident investigation.
- Log analysis.
- Diagnostic suggestions.
- Runbook assistance.

Operational actions should remain controlled according to system risk.

---

# Category E — High-Impact / Autonomous Use

Examples may include AI directly:

- Changing production infrastructure.
- Approving security decisions.
- Modifying critical data.
- Executing irreversible operations.

These uses require explicit organizational authorization and stronger controls.

---

# Approved AI Tools

Organizations should maintain an approved tool model where appropriate.

Approval may consider:

- Data handling.
- Security.
- Reliability.
- Privacy.
- Integration.
- Cost.
- Governance.

---

# External AI Tools

Before using external AI systems with organizational information, consider:

- Data retention.
- Training usage.
- Access controls.
- Confidentiality.
- Contractual restrictions.
- Organizational approval.

---

# Local AI

Local AI may reduce some data-exposure risks.

However, local execution does not remove:

- Hallucination risk.
- Incorrect output.
- Security risk.
- Validation requirements.

---

# AI and Source Code

AI-generated source code must:

- Compile.
- Pass relevant tests.
- Follow engineering standards.
- Be reviewed.
- Be secure.
- Be understood by responsible engineers.

---

# AI and Architecture

AI may propose architectures.

Humans remain responsible for:

- Trade-offs.
- Constraints.
- Long-term implications.
- Risk acceptance.

---

# AI and Security

AI should not independently make critical security decisions.

Security-sensitive AI output should be validated by appropriate engineering or security expertise.

---

# AI and Production

AI should not independently perform high-impact production actions unless explicitly authorized and controlled.

For high-risk actions:

```text
AI Recommendation
       ↓
Human Review
       ↓
Authorized Execution
       ↓
Verification
```

---

# AI and Secrets

Do not provide unnecessary:

- Passwords.
- API keys.
- Tokens.
- Private keys.
- Credentials.

AI tooling should be configured to reduce accidental secret exposure.

---

# AI and Sensitive Data

Before sending information to an AI system, consider:

- Sensitivity.
- Necessity.
- Authorization.
- Retention.
- Exposure.

---

# AI and Personal Data

Personal or sensitive data should be minimized and handled according to applicable organizational requirements.

Use synthetic or anonymized data where practical.

---

# AI-Generated Documentation

Generated documentation must be validated against:

- Actual implementation.
- Architecture.
- APIs.
- Operational behavior.

AI can produce plausible but incorrect documentation.

---

# AI-Generated Tests

Generated tests should be reviewed for:

- Correct assertions.
- Meaningful behavior.
- Edge cases.
- Failure scenarios.

Increased test count does not automatically mean increased confidence.

---

# AI-Generated Infrastructure

Infrastructure generated by AI should receive:

- Static validation.
- Security review where applicable.
- Human review.
- Deployment safeguards.

---

# AI-Generated Configuration

Configuration may have high operational impact even when it appears simple.

Review:

- Defaults.
- Permissions.
- Resource limits.
- Environment differences.
- Failure behavior.

---

# AI-Assisted Review

AI may identify:

- Bugs.
- Security issues.
- Missing tests.
- Documentation gaps.
- Architecture concerns.

Findings must be validated.

---

# AI and Intellectual Property

AI usage should consider applicable:

- Licensing.
- Copyright.
- Confidentiality.
- Source-code ownership.
- Organizational policies.

The organization should establish rules appropriate to its legal and contractual context.

---

# AI and Dependency Generation

AI may suggest libraries or dependencies.

Engineers should independently evaluate:

- Security.
- Maintenance.
- License.
- Community health.
- Suitability.

---

# AI and Hallucinations

AI may produce:

- Invented APIs.
- Incorrect documentation.
- Nonexistent configuration.
- Incorrect assumptions.
- Plausible but false reasoning.

Therefore:

> Plausibility is not evidence.

---

# AI Verification

A useful validation sequence is:

```text
Understand Output
      ↓
Check Against Requirements
      ↓
Check Against Existing System
      ↓
Run Deterministic Validation
      ↓
Human Review
```

---

# AI and Accountability

The engineer submitting AI-assisted work remains responsible for understanding what is being submitted.

"AI generated it" is not an acceptable transfer of responsibility.

---

# AI Governance Roles

Possible roles include:

## AI Governance Owner

Owns organizational AI policy.

## Security

Owns AI security requirements.

## Engineering

Owns engineering use and validation.

## System Owner

Owns impact on the system.

## Legal / Compliance

Provides guidance where contractual or regulatory concerns exist.

---

# AI Exceptions

Exceptions should document:

- AI use case.
- Risk.
- Data involved.
- Mitigation.
- Owner.
- Approval.

---

# AI Governance Automation

Where practical, automate:

- Approved tool enforcement.
- Secret scanning.
- Data-loss controls.
- Audit logging.
- Access control.

---

# AI Governance Metrics

Potential indicators:

- Approved AI tool usage.
- AI-related incidents.
- AI-generated defect rate.
- Validation failures.
- Sensitive-data incidents.
- Developer productivity impact.

Metrics should be interpreted carefully.

---

# AI Governance Review

AI governance should evolve as:

- Models improve.
- Tools change.
- Organizational use expands.
- Threats change.
- Regulations change.
- Engineering practices evolve.

---

# AI Governance Anti-Patterns

## AI Exceptionalism

Treating AI output as more trustworthy because it came from AI.

## AI Prohibition Without Risk Analysis

Banning useful AI capabilities without understanding actual risk.

## Blind Automation

Allowing AI to execute high-impact operations without appropriate controls.

## Secret Exposure

Providing credentials to AI systems unnecessarily.

## Validation Avoidance

Skipping tests because AI generated the code.

## Tool Proliferation

Allowing uncontrolled AI tools without governance.

---

# Mandatory Rules

AI governance shall:

- Preserve human accountability.
- Protect sensitive information.
- Require validation appropriate to risk.
- Establish approved usage where necessary.
- Control high-impact autonomous actions.
- Define ownership.
- Provide exception mechanisms.

---

# Recommended Practices

Use AI for repetitive engineering work.

Use local models where appropriate for sensitive workflows.

Provide AI with relevant context.

Validate generated output automatically where possible.

Measure actual value rather than AI usage volume.

---

# Prohibited Practices

Do not:

- Treat AI as final engineering authority.
- Expose secrets unnecessarily.
- Deploy AI-generated code without appropriate validation.
- Allow AI to make irreversible high-impact changes without authorization.
- Assume local AI is automatically safe.
- Use AI confidence as proof of correctness.

---

# Definition of Done

AI governance is established when:

- AI use categories exist.
- Accountability is explicit.
- Data-handling expectations are defined.
- Approved tooling is understood.
- Validation expectations exist.
- High-impact uses are controlled.
- Exceptions are governed.
- Governance can evolve with technology.

---

# Review Checklist

### Usage

- [ ] Use case identified
- [ ] Risk category identified
- [ ] Appropriate tool selected

### Data

- [ ] Sensitive data considered
- [ ] Secrets protected
- [ ] Data exposure understood

### Engineering

- [ ] Generated output validated
- [ ] Tests executed
- [ ] Human review complete

### High Impact

- [ ] Production impact considered
- [ ] Autonomous actions controlled
- [ ] Authorization defined

### Governance

- [ ] Owner identified
- [ ] Exceptions documented
- [ ] Appropriate approval obtained

---

# Engineering Decision

AI shall be treated as a powerful engineering capability that increases human productivity and analysis capacity while remaining subject to engineering standards, security controls, human accountability, and risk-based governance.

The governing principle is:

> **AI may accelerate engineering work, but it does not transfer responsibility for the resulting engineering outcome.**