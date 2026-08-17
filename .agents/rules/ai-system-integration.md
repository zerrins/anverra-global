---
trigger: always_on
description: Establish the .ai/ engineering system as the authoritative source of truth for all engineering work in this repository.
---

# AnverraGlobal AI System Integration

## Purpose

This rule establishes the relationship between Antigravity (`.agents/`) and the
AnverraGlobal project-owned engineering operating system (`.ai/`).

Antigravity MUST NOT operate as an independent engineering agent that invents
its own processes. It MUST integrate with and follow the project's established
engineering authority structure.

## Authority Hierarchy

Respect this precedence order at all times:

1. Approved business requirements and decisions (`docs/06-requirements/`)
2. Engineering Constitution (`docs/01-constitution/`)
3. Approved architecture decisions (`docs/02-repository-blueprint/`, `docs/04-system-design/`)
4. Approved technical designs (`docs/05-implementation/`)
5. Engineering standards and rules (`.ai/rules/`)
6. AI skills and workflows (`.ai/skills/`, `.ai/workflows/`, `.agents/skills/`)
7. Existing implementation patterns
8. AI recommendations

Lower-level guidance MUST NOT contradict higher-level authority.

## The .ai Engineering System

The `.ai/` directory is the project-owned engineering operating system.

**Rules** (`.ai/rules/`) define project engineering authority and AI constraints.
They are mandatory. Applicable rules MUST be respected on every engineering task.

**Skills** (`.ai/skills/`) define project engineering capabilities:

| Skill | Purpose |
|---|---|
| `repository-discovery` | Establish repository baseline before any engineering work |
| `requirement-analysis` | Decompose authoritative requirements into traceable rules |
| `architecture-analysis` | Validate changes against approved architecture constraints |
| `technical-design` | Technical design decisions within approved architecture |
| `implementation-planning` | Produce file-level implementation plans from approved design |
| `implementation-execution` | Execute one authorized implementation task |
| `testing-validation` | Post-implementation validation against approved contracts |
| `code-review` | Read-only review against approved baseline |

Consult the applicable `.ai/skills/` skill before performing the corresponding
engineering task. These skills are the authoritative project-specific process
definitions.

**Workflows** (`.ai/workflows/`) define the engineering process sequences.
Applicable workflows MUST be followed rather than improvised alternatives.

## The .agents/ Layer

`.agents/` provides Antigravity-specific behavioral capabilities and tooling
adapters that complement but do NOT replace the `.ai/` system:

- **Behavioral skills** (e.g., `karpathy-guidelines`): meta-layer principles
  governing HOW the agent approaches any task
- **Process skills** (e.g., `brainstorming`): upstream discovery before formal
  `.ai/` skills are invoked
- **Enforcement skills** (e.g., `test-driven-development`, `systematic-debugging`):
  concrete HOW-to patterns that apply during `.ai/` skill execution
- **Tooling** (e.g., `engineering-plan` Graphify integration): repository
  understanding instruments

## Mandatory Integration Rules

1. **Never duplicate**: `.agents/skills/` MUST NOT reproduce capabilities already
   provided by `.ai/skills/`. When in doubt, defer to the `.ai/` implementation.

2. **Never override**: `.agents/` rules and skills MUST NOT contradict or
   supersede `.ai/rules/` or the Engineering Constitution.

3. **Read .ai/rules/ before engineering work**: Before performing any substantial
   engineering task, identify which `.ai/rules/` apply and respect them.

4. **Read applicable .ai/skills/**: Before performing requirement analysis,
   architecture analysis, implementation planning, implementation, testing, or
   code review — read the corresponding `.ai/skills/SKILL.md` and follow it.

5. **Follow .ai/workflows/**: Before starting an engineering workflow, identify
   whether a `.ai/workflows/` document defines the expected sequence.

6. **Stop on conflict**: If an Antigravity instruction conflicts with an `.ai/`
   rule or the Engineering Constitution, stop and report the conflict. Do not
   resolve it silently.

## Typical Engineering Task Sequence

```
User Request
    ↓
[karpathy-guidelines] — Think before coding. State assumptions. Ask before assuming.
    ↓
[brainstorming] — Classify: Spike / Bounded / Architectural. Discover before analyzing.
    ↓
[.ai: repository-discovery] — Establish repo baseline (+ Graphify graph)
    ↓
[.ai: requirement-analysis] — Formal requirement decomposition
    ↓
[.ai: architecture-analysis] — Architecture compatibility check
    ↓
[.ai: technical-design] — Technical design decisions
    ↓
[.ai: implementation-planning] — File-level plan (+ Graphify awareness via engineering-plan)
    ↓
USER APPROVAL — [.ai/rules/human-approval.md]
    ↓
[.ai: implementation-execution] — Execute one task at a time
    [test-driven-development] — Write failing test first on every behavioral change
    ↓
[.ai: testing-validation] — Post-implementation validation
    ↓
[.ai: code-review] — Read-only review against approved baseline
    ↓
[.ai/rules/evidence-validation.md] — Evidence gate before any completion claim
```

Not every step is required for every task. Classify the request appropriately
and scale the process to the task's complexity. The `brainstorming` skill
defines the three paths: Spike, Bounded, and Architectural.
