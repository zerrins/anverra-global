# Success Criteria

**Stage:** 1 — Vision  
**Document:** 07 — Success Criteria  
**Version:** 1.0  
**Status:** Expanded Draft

---

## 1. Purpose

This document defines how Anverra should determine whether the intent established in Stage 1 is being realized.

Success should not be evaluated through a single number.

Anverra needs evidence across:

- business
- product
- engineering
- AI engineering
- operations
- quality

The existing AEOS Success Metrics & KPIs document establishes this multi-dimensional approach.

---

# 2. Measurement Principles

The existing AEOS direction establishes these principles:

- metrics support decision-making
- metrics drive improvement rather than blame
- every KPI should have a clear owner
- trends are more important than isolated values
- business and engineering metrics carry equal importance

These principles should govern how Stage 1 success is evaluated.

---

# 3. Business Success

Business success should demonstrate that Anverra Global is creating meaningful value in insurance distribution.

Existing business KPI categories include:

## Customer

- customer growth
- customer retention
- active customers
- customer onboarding completion rate

## Distribution Network

- active agents
- active dealers
- active partners
- productivity per intermediary

## Insurance Operations

- proposals created
- policies issued
- policy renewal rate
- policy servicing turnaround time

## Financial

- commission accuracy
- commission processing time
- revenue growth
- business conversion rate

These metrics should eventually have formal definitions and targets.

---

# 4. Product Success

Product success should show that users are adopting and benefiting from the platform.

Existing product KPIs include:

- feature adoption
- user engagement
- time to complete common workflows
- customer satisfaction
- support ticket volume

The important idea is that product success is not equivalent to feature count.

A feature that exists but is not useful does not represent strong product success.

---

# 5. Engineering Success

Engineering success should demonstrate that Anverra can continuously evolve the platform safely.

Existing engineering KPIs include:

- deployment frequency
- lead time for changes
- build success rate
- mean time to restore
- change failure rate
- automated test coverage
- static analysis compliance

These measures provide a view of delivery capability and engineering health.

---

# 6. Quality Success

Quality should be evaluated through:

- security findings
- technical debt trend
- architecture compliance
- documentation coverage
- code review completion
- regression defects

Quality trends should improve or remain within acceptable boundaries as product scope grows.

---

# 7. Operational Success

Operational KPIs include:

- API availability
- system uptime
- average response time
- error rate
- background job success rate
- infrastructure health

These demonstrate whether the product can be reliably operated.

---

# 8. AI Engineering Success

AI engineering requires dedicated measures because normal engineering productivity metrics do not show whether AI assistance is safe or useful.

Existing AI Engineering KPIs include:

- AI-assisted implementation rate
- specification compliance
- review acceptance rate
- AI-generated defect rate
- documentation synchronization rate

These should be interpreted together.

For example:

A higher AI-assisted implementation rate is not automatically positive if specification compliance decreases and AI-generated defects increase.

---

# 9. Vision-Level Success

Stage 1 should ultimately be considered successful when the following conditions are observable.

## 9.1 Business

Anverra Global is producing measurable value in insurance distribution.

## 9.2 Product

Users adopt the platform and can complete important workflows effectively.

## 9.3 Engineering

The system can evolve without uncontrolled growth in complexity or failure rate.

## 9.4 AI Engineering

AI meaningfully increases engineering capability while remaining governed and validated.

## 9.5 Knowledge

Important business and engineering knowledge remains discoverable and synchronized with implementation.

## 9.6 Governance

Important decisions and changes remain traceable and human-accountable.

---

# 10. KPI Definition Standard

The existing AEOS KPI governance requires every KPI to define:

- Owner
- Definition
- Calculation method
- Measurement frequency
- Target
- Threshold
- Review cadence

Therefore, this document intentionally does not invent numerical targets.

Targets should be established in later KPI artifacts when sufficient business and operational evidence exists.

---

# 11. Leading and Lagging Indicators

Anverra should eventually distinguish between:

### Leading indicators

Signals that suggest future performance.

Examples:

- onboarding completion
- feature adoption
- test coverage
- documentation synchronization
- specification compliance

### Lagging indicators

Signals that show realized outcomes.

Examples:

- retention
- revenue growth
- change failure rate
- production defects
- customer satisfaction

Both categories are required.

---

# 12. Metric Relationships

Metrics should not be interpreted independently.

For example:

**AI-assisted implementation rate ↑**

is useful only when considered with:

- specification compliance
- review acceptance
- AI-generated defect rate
- delivery lead time
- quality trends

Similarly:

**Deployment frequency ↑**

is not automatically good if:

- change failure rate ↑
- regression defects ↑
- availability ↓

The purpose of measurement is therefore to understand system behavior, not to maximize individual numbers.

---

# 13. Success Anti-Patterns

Avoid:

- optimizing a single KPI
- setting targets without ownership
- measuring activity instead of outcomes
- using metrics primarily for blame
- ignoring trends
- hiding negative indicators
- treating AI adoption as success by itself
- increasing delivery speed while allowing quality to collapse
- allowing documentation quality to decline while code complexity increases

---

# 14. Review Cadence

KPIs should be reviewed periodically.

The review should ask:

1. What changed?
2. Why did it change?
3. Is the change meaningful?
4. Is it improving the intended outcome?
5. Are there negative side effects?
6. Does the KPI definition remain useful?
7. Does a threshold or target need revision?
8. Does the engineering system need improvement?

The exact cadence belongs to the KPI governance layer.

---

# 15. Continuous Improvement

Success criteria are not static.

As Anverra learns more, it should refine:

- measurements
- targets
- thresholds
- definitions
- ownership
- review processes

This is consistent with the AEOS continuous-improvement model, where improvement sources include:

- ADRs
- retrospectives
- production learnings
- AI feedback
- engineering metrics

---

# 16. Completion Criteria for Stage 1

Stage 1 can be considered sufficiently defined when:

- company vision is explicit
- mission is explicit
- product vision is explicit
- engineering vision is explicit
- AI engineering vision is explicit
- core values are explicit
- success dimensions are explicit
- KPI categories are identified
- KPI governance expectations are known
- downstream stages can derive goals and requirements from the vision

---

# 17. Summary

Stage 1 success is ultimately:

> **Anverra creates meaningful insurance-distribution value through a product that users benefit from, an engineering system that can evolve safely, and an AI-assisted operating model that increases capability without sacrificing governance, quality, or human accountability.**