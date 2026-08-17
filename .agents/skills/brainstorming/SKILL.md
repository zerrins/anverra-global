---
name: brainstorming
description: "Use before any creative or non-trivial engineering work — new features, new components, added functionality, or behavior changes. Explores intent, requirements, and design through collaborative dialogue before implementation. This skill sits upstream of the .ai/skills/requirement-analysis skill."
source: https://github.com/obra/superpowers (adapted for AnverraGlobal)
---

# Brainstorming — Requirements Discovery

Turn ideas into fully formed designs through collaborative dialogue, then hand
off to the AnverraGlobal `.ai/` engineering system for formal analysis.

This skill is **upstream** of `.ai/skills/requirement-analysis`. It bridges the
gap between an unformed user request and a formally structured requirement
ready for analysis.

<HARD-GATE>
Do NOT write code, scaffold files, or invoke any `.ai/` implementation skill
until you have stated your intent, design, and classification — and the user
has explicitly approved. This gate applies on every path. What scales with
task simplicity is the artifact; the approval gate never scales away.
</HARD-GATE>

---

## Three Paths

Before your first question, classify the request and announce the
classification so the user can override it:

> "This looks like a Bounded change — I'll ask one clarifying question and
> present a short design here rather than write a spec."

### Spike
A feasibility question ("can we…", "is it possible…", "quick exploration is
fine"). Output is an **answer**, not code you keep.

- Present the question and what you'll probe in 2–3 sentences
- Get approval
- Investigate as cheaply as correctness allows
- Report findings as a recommendation; anything built stays labeled throwaway
- A spike's output is an answer — keeping the code is a new request; classify it fresh

### Bounded
A well-scoped change to code that already exists in this repo — a new flag,
a small endpoint, a one-file fix.

Bounded means the flow you are changing is already present and readable here.
If no existing flow exists to change, the task is not bounded.

- Ask the clarifying questions that matter (one at a time)
- Present a short design **in chat** (a few sentences to a few short paragraphs)
- **STOP and wait** for explicit approval before implementing
- Invoke `.ai/skills/implementation-planning` for the plan (no spec file)

### Architectural
New projects, new subsystems, changes that restructure how components fit
together, or changes that alter interfaces others depend on.

Full process (all steps in order):

1. **Explore project context** — check files, docs, existing `.ai/` rules and skills
2. **Ask clarifying questions** — one at a time: understand purpose, constraints, success criteria
3. **Propose 2–3 approaches** — with tradeoffs and a recommendation
4. **Present design** — in sections; get user approval after each section
5. **Write requirement or design spec** — save to the appropriate location (see below)
6. **Self-review the spec** — check for placeholders, contradictions, scope creep
7. **User reviews spec** — get explicit sign-off before proceeding
8. **Hand off to `.ai/skills/requirement-analysis`** — formal decomposition begins

When in doubt between two paths, take the heavier one. If hidden complexity
is discovered mid-task, upgrade the path — stop, say so, re-classify.

---

## Documentation Output

Specs and design documents produced by this skill MUST follow AnverraGlobal's
existing documentation taxonomy. Do NOT create `docs/superpowers/` or any
external tooling-specific directory.

### Correct output locations

| Type of output | Location |
|---|---|
| Business / functional requirement spec | `docs/06-requirements/decisions/REQ-DEC-NNN-<topic>.md` |
| UX / product design spec | `docs/06-requirements/decisions/REQ-DEC-NNN-<topic>.md` |
| Architecture / technical design spec | `docs/04-system-design/` or `docs/05-implementation/` as appropriate |
| Implementation plan (after architectural approval) | delegated to `.ai/skills/implementation-planning` |

### Classification

Every requirement or design spec produced by brainstorming MUST carry the
`docs/06-requirements/README.md` classification:

- **A. AUTHORITATIVE** — explicitly established by an approved business source
- **B. EXPLICITLY DEFERRED** — addressed in a future phase
- **C. PROPOSED / REQUIRES CONFIRMATION** — candidate pending human approval
- **D. IMPLEMENTATION-LEVEL** — technical choices within architecture
- **E. UNKNOWN / UNSUPPORTED** — absent from documentation

Brainstorming output is always at minimum **C. PROPOSED / REQUIRES CONFIRMATION**
until explicitly approved by the appropriate human authority. Do not promote a
brainstorming output to AUTHORITATIVE without human sign-off.

---

## Handoff to Formal Engineering System

After brainstorming produces an approved spec or design, the formal
AnverraGlobal engineering process begins:

```
Brainstorming (this skill) → [approved spec or design]
    ↓
.ai/skills/repository-discovery → Establish repo baseline
    ↓
.ai/skills/requirement-analysis → Formal decomposition and traceability
    ↓
.ai/skills/architecture-analysis → Architecture compatibility check
    ↓  (if needed)
.ai/skills/technical-design → Technical decisions
    ↓
.ai/skills/implementation-planning → File-level plan
    ↓  [human-approval gate]
.ai/skills/implementation-execution → Execute plan tasks
```

Do NOT skip straight from brainstorming to implementation-execution.

---

## Anti-Patterns

| Thought | Reality |
|---|---|
| "This is too simple to need a design" | Simple means a short design, not no design. Two sentences in chat, then wait for approval. |
| "I understand this kind of app, so it's bounded" | Bounded measures the repo, not familiarity. A new feature with no existing flow is architectural. |
| "The spike works, so I'll keep the code" | A spike's output is an answer. Keeping code is a new request — classify it fresh. |
| "It grew, but I'm almost done" | Hidden complexity upgrades the path mid-task. Stop and re-classify. |
| "They approved the spike, so the follow-up is approved too" | Each task gets its own classification and its own approval. |
| "I'll write the spec and start implementing while they read it" | The gate is the approval, not the spec's existence. Present, then stop. |

---

## Checklist

### Spike Path
1. Frame the question and probe plan (2–3 sentences)
2. Get approval
3. Investigate cheaply
4. Report findings — label anything built as throwaway

### Bounded Path
1. Explore project context (files, docs, recent commits)
2. Ask one clarifying question at a time
3. Present short design in chat (approach, files touched, testing intent)
4. **Wait for explicit approval**
5. Invoke `.ai/skills/implementation-planning` → proceed with `.ai` workflow

### Architectural Path
1. Explore project context + consult Graphify if graph exists
2. Ask clarifying questions one at a time
3. Propose 2–3 approaches with tradeoffs
4. Present design in sections; get approval after each section
5. Write spec to `docs/06-requirements/decisions/` or `docs/04-system-design/`
6. Self-review spec for placeholders, contradictions, scope
7. Get explicit user sign-off
8. Hand off to `.ai/skills/requirement-analysis`
