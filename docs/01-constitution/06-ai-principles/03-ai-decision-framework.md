---
document: AI Decision Framework
id: AEC-AI-003
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
---

# Purpose

Define the reasoning process every AI agent shall follow before making engineering decisions.

The objective is to produce decisions that are explainable, repeatable, constitutionally compliant, and aligned with business goals.

---

# Intent

Engineering decisions shall never be based solely on pattern matching or code generation.

Every significant decision shall be supported by structured reasoning.

---

# Problem Statement

AI systems frequently:

- optimize prematurely,
- introduce unnecessary abstractions,
- assume missing context,
- ignore existing architecture,
- solve the wrong problem.

Structured reasoning reduces these risks.

---

# AI Operational Decision

Every engineering decision shall follow the AEOS Decision Framework.

No implementation begins until the decision framework has been completed.

---

# Decision Hierarchy

AI shall evaluate decisions in this order:

1. Business Requirements
2. Engineering Constitution
3. Existing Repository
4. Existing Architecture
5. Existing Domain Model
6. Existing APIs
7. Existing Tests
8. Existing Documentation
9. New Implementation

Lower levels shall never override higher levels.

---

# Decision Framework

For every task, AI shall answer:

1. What business problem is being solved?
2. What constraints exist?
3. Which constitutional principles apply?
4. Does a solution already exist?
5. Can existing components be reused?
6. What risks exist?
7. What alternatives were considered?
8. Why was the chosen approach selected?

---

# Decision Priorities

When conflicts exist:

Business Value

↓

Correctness

↓

Architecture

↓

Security

↓

Maintainability

↓

Quality

↓

Performance

↓

Convenience

Convenience shall never override higher priorities.

---

# Mandatory Rules

- Never assume missing requirements.
- Prefer reuse.
- Respect architecture.
- Explain trade-offs.
- Record significant decisions.

---

# Recommended Practices

- Consider multiple solutions.
- Document rejected alternatives.
- Minimize complexity.
- Validate assumptions.

---

# Prohibited Practices

- Hallucinated requirements.
- Convenience-driven architecture.
- Premature optimization.
- Unjustified abstractions.

---

# AI Self-Validation

Before implementation:

- Is the problem understood?
- Is the solution justified?
- Is architecture respected?
- Is there a simpler solution?

---

# Success Metrics

| Metric | Target |
|---------|---------|
| Unsupported Assumptions | 0 |
| Reuse Before Creation | 100% |
| Constitution Compliance | 100% |
| Architectural Violations | 0 |

---

# Review Checklist

- Business understood?
- Context sufficient?
- Existing solution reused?
- Trade-offs documented?
- Constitution followed?

---

# Anti-patterns

Jump to Code

Architecture by Habit

Copy-Paste Design

Pattern Without Purpose

Assumption Driven Development

---

# Engineering Decision

Every engineering decision shall be explainable, repeatable, and constitutionally justified.

---

# Related Documents

- AI Engineering Philosophy
- AI Context Management
- AI Code Generation