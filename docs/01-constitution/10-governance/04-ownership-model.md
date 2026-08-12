---
document: Ownership Model
id: AEC-GOV-004
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-GOV-001
  - AEC-GOV-002
  - AEC-GOV-003
---

# Purpose

Define the ownership model used to establish clear accountability for engineering systems, components, decisions, risks, standards, and technical outcomes.

---

# Intent

The ownership model should answer:

- Who owns this system?
- Who can make decisions about it?
- Who is responsible for its health?
- Who owns its risks?
- Who owns its technical debt?
- Who maintains its documentation?
- Who can approve significant changes?
- What happens when ownership is unclear?

---

# Constitutional Decision

Every important engineering asset and responsibility shall have an identifiable owner.

Ownership shall represent accountability for outcomes, not merely possession of code or infrastructure.

---

# What Is Ownership?

Ownership means accepting responsibility for the health, evolution, and outcomes of a defined engineering scope.

Ownership includes:

```text
Understand
   ↓
Maintain
   ↓
Operate
   ↓
Improve
   ↓
Protect
   ↓
Evolve
```

---

# Ownership Is Not Possession

A team may own a system without personally performing every activity associated with it.

For example:

```text
System Owner
    ↓
Coordinates
    ├── Development
    ├── Security
    ├── Operations
    ├── Testing
    └── Documentation
```

Ownership means accountability for the overall outcome.

---

# Ownership Scope

Ownership may exist at multiple levels:

```text
Organization
    ↓
Domain
    ↓
Product
    ↓
System
    ↓
Service
    ↓
Repository
    ↓
Component
```

Ownership should be assigned at the level where accountability is meaningful.

---

# Primary Ownership

Every critical system should have a primary owner.

The primary owner is accountable for:

- System health.
- Architecture.
- Reliability.
- Security.
- Documentation.
- Technical debt.
- Operational readiness.
- Lifecycle decisions.

---

# Secondary Ownership

Critical systems should have secondary or backup ownership where practical.

Secondary ownership reduces:

- Single-person dependency.
- Knowledge concentration.
- Operational risk.

---

# Ownership Metadata

A system ownership record should ideally identify:

```text
System
Primary Owner
Backup Owner
Team
Business Capability
Technical Domain
Repository
Production Environment
Criticality
Dependencies
Documentation
Runbook
```

The exact implementation may vary.

---

# Ownership Responsibilities

System ownership includes responsibility for:

## Technical Health

- Correctness.
- Maintainability.
- Architecture.
- Performance.

## Operational Health

- Availability.
- Monitoring.
- Recovery.
- Incident response.

## Security

- Secure implementation.
- Vulnerability remediation.
- Access control.
- Sensitive data handling.

## Documentation

- Architecture.
- Operational procedures.
- APIs.
- Important decisions.

## Evolution

- Technical debt.
- Upgrades.
- Deprecation.
- Migration.

---

# Ownership and Decision Authority

Ownership does not mean unlimited authority.

A system owner may make decisions within their scope while higher-risk decisions may require additional governance.

Example:

```text
System Owner
     ↓
Normal implementation decisions

System Owner
     ↓
Major architecture change
     ↓
Architecture Review
```

---

# Ownership and Risk

Every significant accepted risk should have an owner.

Risk ownership includes:

- Understanding the risk.
- Tracking mitigation.
- Monitoring changes.
- Accepting residual risk where authorized.

---

# Ownership and Technical Debt

Technical debt should not exist without ownership.

Each significant debt item should have:

- Owner.
- Impact.
- Priority.
- Context.
- Desired resolution.

---

# Ownership and Security

Security responsibility should remain shared.

```text
System Owner
     +
Security Specialist
     +
Engineering Team
```

The system owner remains accountable for the system while specialists provide expertise and governance.

---

# Ownership and Operations

Production ownership should be explicit.

The responsible team should understand:

- How the system is deployed.
- How it is monitored.
- How it fails.
- How it recovers.
- Who responds to incidents.

---

# Ownership and Documentation

Owners are accountable for important documentation remaining accurate.

This includes:

- README.
- Architecture.
- Runbooks.
- API documentation.
- Operational procedures.
- Important decisions.

---

# Ownership and Dependencies

System owners should understand important dependencies.

For each critical dependency, consider:

- Owner.
- Availability.
- Failure behavior.
- Contract.
- Escalation path.

---

# Dependency Ownership

A dependency should not be considered reliable merely because another team owns it.

Consumers should understand:

```text
Dependency
    ↓
Contract
    ↓
Failure Behavior
    ↓
Recovery
```

---

# Shared Systems

Shared systems require especially clear ownership.

Examples:

- Authentication.
- Shared databases.
- CI infrastructure.
- Messaging infrastructure.
- Shared libraries.

---

# Shared Ownership

"Shared ownership" should be used carefully.

A shared system should still have:

```text
One Primary Accountable Owner
+
Multiple Contributors
```

Otherwise accountability becomes ambiguous.

---

# Product Ownership vs Technical Ownership

Business ownership and technical ownership are different.

```text
Product Owner
→ Business outcome

System Owner
→ Technical outcome
```

The two roles should collaborate.

---

# Repository Ownership

Repositories should have identifiable ownership.

Ownership should cover:

- Code.
- Build.
- Dependencies.
- Tests.
- Documentation.
- Security.

---

# Component Ownership

Large repositories may assign ownership at component or module level.

Component ownership should not create conflicting authority.

Repository-level ownership should remain clear.

---

# Ownership Transfer

Ownership transfers should be explicit.

A transfer should consider:

- Documentation.
- Access.
- Runbooks.
- Architecture.
- Technical debt.
- Known risks.
- Operational history.

---

# Ownership Acceptance

A new owner should understand:

- Scope.
- Responsibilities.
- Risks.
- Dependencies.
- Operational expectations.

Ownership should not be transferred silently.

---

# Ownership During Reorganization

When teams are reorganized:

```text
Old Owner
    ↓
Transition
    ↓
New Owner
```

There should not be a period where critical systems have no owner.

---

# Ownership During Leave

Critical systems should have backup ownership.

Temporary absence should not create operational ambiguity.

---

# Ownership During Incidents

The system owner provides system expertise.

Incident leadership may temporarily coordinate the response.

Incident coordination does not permanently transfer system ownership.

---

# Ownership and Lifecycle

Owners are responsible for lifecycle decisions:

```text
Create
  ↓
Operate
  ↓
Maintain
  ↓
Evolve
  ↓
Deprecate
  ↓
Retire
```

---

# Deprecation

When a system is deprecated, ownership remains necessary until retirement is complete.

Deprecation does not mean ownership disappears.

---

# Retirement

Before retirement, owners should consider:

- Consumers.
- Data.
- Dependencies.
- Security.
- Documentation.
- Operational cleanup.

---

# Ownership Gaps

An ownership gap exists when:

```text
Important Asset
      ↓
No Accountable Owner
```

Ownership gaps are governance risks and should be resolved.

---

# Ownership Conflicts

Conflicts may occur when:

```text
Team A → Claims Ownership
Team B → Claims Ownership
```

Resolve using:

1. Scope.
2. Architecture.
3. Business responsibility.
4. Operational responsibility.
5. Governance escalation.

---

# Ownership Anti-Patterns

## Orphaned System

No accountable owner.

## Accidental Owner

A team becomes responsible merely because it happens to maintain the code.

## Shared Accountability Without Authority

Multiple teams are accountable but none can make decisions.

## Hero Ownership

One individual becomes indispensable.

## Ownership by Repository Location

The location of code determines responsibility without considering system boundaries.

---

# Ownership Review

Ownership should be reviewed when:

- Systems change.
- Teams change.
- Architecture changes.
- Criticality changes.
- Dependencies change.
- Organizational structure changes.

---

# Ownership Metrics

Useful indicators include:

- Percentage of critical systems with owners.
- Percentage with backup owners.
- Unowned technical debt.
- Unowned risks.
- Stale ownership records.

Metrics should identify gaps rather than punish teams.

---

# Mandatory Rules

Critical engineering assets shall:

- Have a primary owner.
- Have appropriate backup ownership.
- Have ownership records.
- Have risk ownership where applicable.
- Have lifecycle accountability.

---

# Recommended Practices

Keep ownership close to the system.

Automate ownership metadata where practical.

Review ownership periodically.

Distribute knowledge.

Document ownership transfers.

---

# Prohibited Practices

Do not:

- Leave critical systems unowned.
- Treat shared ownership as a substitute for accountability.
- Transfer ownership without knowledge transfer.
- Depend on one individual for critical system knowledge.
- Remove ownership before system retirement is complete.

---

# Definition of Done

Ownership is adequately established when:

- Scope is clear.
- Primary owner exists.
- Backup ownership exists where required.
- Responsibilities are documented.
- Decision authority is understood.
- Risks have owners.
- Technical debt has owners.
- Lifecycle responsibility is clear.

---

# Engineering Decision

Ownership shall remain explicit throughout the complete lifecycle of an engineering system.

The objective is simple:

> Every important engineering outcome must have someone accountable for ensuring that it remains healthy, understood, secure, and capable of evolving.