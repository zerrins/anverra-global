# Core Values

**Stage:** 1 — Vision  
**Document:** 06 — Core Values  
**Version:** 1.0  
**Status:** Expanded Draft  
**Authority:** Organizational / Engineering

---

# 1. Purpose

Core values define the principles that should remain stable while Anverra's:

- products
- technologies
- processes
- engineering practices
- teams

evolve.

They provide a decision lens for situations where multiple reasonable options exist.

Core values are therefore not intended to be slogans.

They should influence:

- product decisions
- engineering decisions
- architecture decisions
- technology decisions
- AI decisions
- standards
- workflows
- validation
- metrics

The existing AEOS Product Principles provide the primary source direction for these values.

---

# 2. Core Values

Anverra's current core values are:

1. Business First
2. AI-First Engineering
3. Modular by Design
4. Enterprise Ready
5. Security by Default
6. Documentation as Code
7. Human Accountability
8. Simplicity Before Complexity
9. Consistency
10. Continuous Improvement

---

# 3. Business First

> **Business value drives engineering priorities.**

Engineering exists to create useful outcomes.

Technical work should therefore be connected to:

- customer value
- operational value
- product value
- risk reduction
- engineering sustainability

This does not mean choosing the fastest implementation.

It means understanding what outcome matters before optimizing how it is delivered.

---

## 3.1 Practical Interpretation

Before significant engineering work begins, the team should be able to explain:

- what problem is being solved
- who benefits
- what outcome is expected
- why the work matters
- how success will be recognized

Technical elegance alone is not sufficient justification for substantial work.

---

# 4. AI-First Engineering

> **AI should be a first-class engineering capability, governed by AEOS.**

AI should be integrated into engineering where it provides meaningful value.

AI adoption should not mean blindly using AI everywhere.

The goal is to build an engineering system in which AI can reliably assist with appropriate work.

---

## 4.1 Practical Interpretation

AI should be considered when it can improve:

- reasoning
- context discovery
- planning
- implementation
- validation
- documentation
- repetitive engineering work

AI should remain governed by:

- explicit responsibilities
- constraints
- validation
- traceability
- human accountability

---

# 5. Modular by Design

> **Business capabilities should have clear boundaries.**

Modularity should make change safer and systems easier to understand.

The objective is not maximum decomposition.

The objective is:

> **Controlled responsibility and dependency.**

---

## 5.1 Practical Interpretation

A module should have:

- clear responsibility
- meaningful ownership
- controlled dependencies
- understandable interfaces
- appropriate validation boundaries

Modularity should reduce unnecessary change propagation.

It should not become an excuse for creating excessive technical fragmentation.

---

# 6. Enterprise Ready

> **Engineering should account for scalability, maintainability, observability, security, and reliability.**

Enterprise readiness is not a single feature.

It is a system property created through many engineering decisions.

---

## 6.1 Enterprise Characteristics

Enterprise readiness should consider:

- scalability
- maintainability
- reliability
- security
- observability
- auditability
- operational recoverability
- controlled change
- documentation

The system should be capable of evolving without uncontrolled increases in complexity and operational risk.

---

# 7. Security by Default

> **Security is built into the system rather than added at the end.**

Security should influence:

- design
- implementation
- validation
- deployment
- operations

Security should not be treated as a final-stage checklist.

---

## 7.1 Practical Interpretation

Engineering should consider security from the beginning of work.

Relevant areas may include:

- identity
- authorization
- data protection
- secrets
- auditability
- dependency security
- secure configuration
- operational access

Detailed security requirements belong to later AEOS security specifications and standards.

---

# 8. Documentation as Code

> **Documentation is a first-class engineering artifact.**

Documentation should evolve alongside implementation.

Important knowledge should be:

- explicit
- version controlled
- discoverable
- reviewable
- traceable

---

## 8.1 Practical Interpretation

Documentation should exist for important:

- architecture
- requirements
- decisions
- standards
- workflows
- testing expectations
- operational knowledge
- AI engineering knowledge

When implementation changes, relevant documentation should be evaluated for synchronization.

---

# 9. Human Accountability

> **AI may assist engineering, but humans remain accountable.**

Humans retain ownership of:

- architecture
- business decisions
- risk
- governance
- approvals
- production outcomes

AI can assist with execution.

It does not become the ultimate owner of the consequences of that execution.

---

## 9.1 Practical Interpretation

AI may:

- propose
- analyze
- implement
- test
- document
- review

Humans remain responsible for deciding:

- whether the approach is appropriate
- whether the risk is acceptable
- whether the result should be approved
- whether it should reach production

---

# 10. Simplicity Before Complexity

> **Choose the simplest solution that satisfies current requirements while preserving appropriate future extensibility.**

Complexity has a cost.

It increases:

- cognitive load
- maintenance effort
- failure modes
- operational burden
- change risk

Complexity should therefore have a reason.

---

## 10.1 Practical Interpretation

When comparing solutions, ask:

1. What problem does the additional complexity solve?
2. Is the problem real?
3. Is the simpler solution insufficient?
4. What maintenance cost does the complex solution introduce?
5. What operational cost does it introduce?
6. Is the additional complexity justified?

The objective is not to avoid complexity at all costs.

The objective is to avoid **unnecessary** complexity.

---

# 11. Consistency

> **Business terminology, APIs, documentation, and code should use a shared ubiquitous language.**

Consistency reduces translation between:

- business
- product
- engineering
- documentation
- AI agents

The same concept should not casually acquire multiple competing names.

---

## 11.1 Practical Interpretation

If a business concept has an established name, that name should be reused consistently across:

- requirements
- APIs
- code
- database concepts
- documentation
- tests
- AI context

For example, if the organization establishes a canonical term for a business concept, engineering should not casually introduce multiple synonyms.

---

# 12. Continuous Improvement

> **Improve the engineering system through measured learning rather than ad hoc change.**

Improvement sources include:

- ADRs
- retrospectives
- production learnings
- AI feedback
- engineering metrics

The objective is to improve without sacrificing stability.

---

## 12.1 Practical Interpretation

When something repeatedly causes problems, the organization should ask:

```text
What happened?
      ↓
Why did it happen?
      ↓
What did we learn?
      ↓
Should the learning become:
    • Knowledge?
    • Standard?
    • Skill?
    • Workflow?
    • Validation?
      ↓
How do we prevent recurrence?
```

The goal is to improve the system rather than repeatedly solve the same problem.

---

# 13. How Values Should Be Used

Values should influence decisions.

They should not be decorative statements.

For example, suppose a feature could be implemented using:

### Option A

A simple modular design.

### Option B

A complex distributed architecture.

Relevant values may include:

- Simplicity Before Complexity
- Modular by Design
- Enterprise Ready

The decision should consider whether the additional complexity produces enough value to justify it.

The result should be an explicit engineering decision when the trade-off is significant.

---

# 14. Values in Everyday Engineering

Values should influence decisions such as:

### Architecture

Should we introduce another service?

Relevant values:

- Modular by Design
- Simplicity Before Complexity
- Enterprise Ready

### AI

Should an agent be allowed to make this change autonomously?

Relevant values:

- AI-First Engineering
- Human Accountability
- Security by Default

### Documentation

Should this architectural decision be documented?

Relevant values:

- Documentation as Code
- Consistency
- Continuous Improvement

### Security

Can security validation be postponed?

Relevant values:

- Security by Default
- Enterprise Ready
- Human Accountability

### Product Prioritization

Should this technical improvement be prioritized?

Relevant value:

- Business First

---

# 15. Value Conflicts

Values may sometimes appear to conflict.

Examples include:

- speed vs maintainability
- flexibility vs simplicity
- AI autonomy vs human accountability
- scalability vs operational simplicity

The values do not provide a mechanical answer to every conflict.

Instead, they require the trade-off to be explicit.

Important trade-offs should become documented decisions.

---

# 16. Value Conflict Example

Consider a requirement that could be delivered:

### Option A

Quick implementation with substantial technical debt.

### Option B

Longer implementation with stronger maintainability.

Relevant values:

- Business First
- Simplicity Before Complexity
- Enterprise Ready
- Continuous Improvement

The correct decision depends on the actual business context.

The values require the team to explicitly consider the consequences rather than automatically choosing either "speed" or "quality."

---

# 17. Value Hierarchy

When a serious conflict occurs, the following general hierarchy should apply:

1. business and user safety
2. security and governance
3. correctness and reliability
4. maintainability and long-term sustainability
5. delivery efficiency
6. optimization

This is a decision guideline.

It is not a replacement for:

- formal risk governance
- architecture governance
- security governance
- business approval

---

# 18. Why a Hierarchy Exists

Without an explicit hierarchy, engineering decisions can accidentally optimize low-level objectives at the expense of higher-level outcomes.

For example:

```text
Optimization
     ↓
Delivery Speed
     ↓
Reliability
     ↓
Security
     ↓
User Safety
```

would be an undesirable priority order.

The intended direction is the reverse:

```text
User / Business Safety
        ↓
Security / Governance
        ↓
Correctness / Reliability
        ↓
Maintainability
        ↓
Delivery Efficiency
        ↓
Optimization
```

---

# 19. Values and AI Engineering

AI makes the values particularly important.

AI can increase engineering speed.

Therefore:

> **Business First** prevents optimizing AI activity instead of business outcomes.

AI can create significant implementation output.

Therefore:

> **Simplicity Before Complexity** prevents unnecessary generated architecture.

AI can operate quickly.

Therefore:

> **Human Accountability** prevents uncontrolled autonomous decision-making.

AI can consume large amounts of context.

Therefore:

> **Consistency** and **Documentation as Code** help establish reliable organizational knowledge.

AI can introduce defects.

Therefore:

> **Security by Default**, **Enterprise Ready**, and **Continuous Improvement** require appropriate validation and learning.

---

# 20. Values and Engineering Governance

These values should influence:

- goals
- requirements
- architecture decisions
- technology decisions
- standards
- skills
- workflows
- validation
- metrics

They are therefore upstream of implementation details.

The relationship can be represented as:

```text
Core Values
     ↓
Goals
     ↓
Requirements
     ↓
Decisions
     ↓
Constraints
     ↓
Specifications
     ↓
Standards
     ↓
Implementation
     ↓
Validation
```

---

# 21. Values and Metrics

Values should influence what the organization measures.

For example:

### Business First

Measure outcomes rather than activity alone.

### Enterprise Ready

Measure reliability, maintainability, security, and operational health.

### Documentation as Code

Measure documentation coverage and synchronization.

### Continuous Improvement

Measure trends and improvement over time.

### AI-First Engineering

Measure AI contribution together with quality and compliance.

Metrics should therefore reinforce the values rather than accidentally undermine them.

---

# 22. Anti-Patterns

Values are violated when:

- AI output is accepted without validation
- security is postponed indefinitely
- complexity is introduced without justification
- documentation is intentionally allowed to become stale
- terminology differs arbitrarily between teams
- engineering optimizes metrics without business outcomes
- short-term delivery repeatedly creates long-term instability
- AI usage becomes an objective by itself
- technical activity is mistaken for business value

---

# 23. Values as a Decision Test

Before making an important decision, ask:

### Business

Does this create meaningful value?

### AI

Could AI help responsibly, and is AI actually appropriate?

### Modularity

Does this preserve meaningful boundaries?

### Enterprise

Can the system remain scalable, maintainable, observable, secure, and reliable?

### Security

Is security built into the decision?

### Documentation

Will important knowledge remain discoverable?

### Accountability

Is human ownership clear?

### Simplicity

Is the complexity justified?

### Consistency

Does this preserve shared terminology and patterns?

### Improvement

Will this make the engineering system better over time?

---

# 24. Stability

Core values should change very rarely.

A change should indicate a meaningful change in how Anverra wants to operate.

A value should not change merely because:

- a technology changed
- a team changed
- an implementation was inconvenient
- an individual disagreed with a decision

Core values operate above those details.

---

# 25. Change Governance

If a core value needs to change, the change should identify:

- the value being changed
- why it is changing
- what strategic assumption changed
- which downstream artifacts depend on it
- which goals may be affected
- which requirements may need review
- which standards may need revision
- which metrics may need revision

A core-value change should therefore be treated as a strategic change.

---

# 26. Relationship to Stage 1

The Stage 1 hierarchy is:

```text
Company Vision
      ↓
Mission
      ↓
Product Vision
      ↓
Engineering Vision
      ↓
AI Engineering Vision
      ↓
Core Values
      ↓
Success Criteria
```

Core Values provide the principles through which the other Stage 1 directions should be interpreted.

They are not a replacement for the Vision or Mission.

They define how Anverra should behave while pursuing them.

---

# 27. Definition of Done

The Core Values document is adequately defined when:

- stable principles are explicit
- each value has a clear meaning
- values can influence real decisions
- value conflicts are acknowledged
- a general value hierarchy exists
- AI-specific implications are understood
- governance implications are understood
- values connect to later AEOS artifacts
- the stability of the values is understood

---

# 28. Summary

The values can be summarized as:

> **Create business value, use AI responsibly, preserve modularity, build for enterprise use, secure by default, document knowledge, keep humans accountable, prefer simplicity, remain consistent, and improve continuously.**