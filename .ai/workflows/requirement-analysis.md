---
description: Analyze a business request against authoritative requirements without implementing or silently inventing business rules.
---

# Requirement Analysis Workflow

## Purpose

Convert a business request into a precise, traceable requirements understanding.

This workflow MUST NOT implement source code.

It MUST NOT silently resolve business ambiguity.

---

## Required Skills

Use:

    .ai/skills/repository-discovery/SKILL.md
    .ai/skills/requirement-analysis/SKILL.md

---

## Execution Order

### Step 1 — Discover Repository Reality

Run repository discovery first.

Establish:

- what exists today
- what is missing
- relevant modules
- relevant documentation
- existing implementation constraints

Do not assume documented components exist.

---

### Step 2 — Identify Authoritative Sources

Locate and inspect the relevant:

- business requirements
- requirement decisions
- architecture decisions
- existing technical constraints

Classify each source as:

- AUTHORITATIVE
- DESIGN DECISION
- DESIGN PROPOSAL
- INFERRED
- SUPERSEDED
- UNRESOLVED

---

### Step 3 — Decompose the Request

Break the requested change into:

1. Business objective
2. Actors
3. Inputs
4. Outputs
5. Business rules
6. State transitions
7. Authorization rules
8. Data ownership
9. Integration behavior
10. Error behavior
11. Non-functional constraints

---

### Step 4 — Trace Requirements

For every requirement identify:

    Requirement
        ↓
    Authoritative source
        ↓
    Business rule
        ↓
    Expected system behavior

Every important behavior MUST have a source.

---

### Step 5 — Detect Conflicts

Check for contradictions between:

- the new request
- existing requirements
- architecture
- technical design
- API contracts
- persistence design
- event design

Do NOT silently reconcile conflicts.

Report them explicitly.

---

### Step 6 — Detect Ambiguities

Identify questions where implementation would require guessing.

Examples:

- unclear business ownership
- unclear lifecycle behavior
- unclear authorization
- unclear cardinality
- unclear status semantics
- unclear error behavior
- unclear persistence ownership

Each ambiguity MUST contain:

- question
- affected requirement
- possible interpretations
- consequence of each interpretation

---

### Step 7 — Separate Business Decisions from Technical Decisions

Business decisions include:

- workflow behavior
- lifecycle rules
- permissions
- ownership
- validation
- required/optional behavior

Technical decisions include:

- framework
- database mechanism
- API implementation
- library
- persistence strategy
- event mechanism

Do not present technical preferences as business requirements.

---

## Required Output

Produce:

### 1. Request Understanding

What the user is asking for.

### 2. Authoritative Requirements

Requirements that are explicitly established.

### 3. Inferred Requirements

Reasonable consequences of authoritative requirements.

### 4. Technical Proposals

Potential implementation approaches that are NOT yet approved.

### 5. Conflicts

Existing rules that conflict with the request.

### 6. Ambiguities

Questions requiring human decisions.

### 7. Traceability Matrix

| Requirement | Source | Rule | Status |
|---|---|---|---|

### 8. Implementation Impact

Identify affected:

- modules
- APIs
- persistence
- events
- authorization
- frontend
- tests

Do NOT implement any of them.

---

## Human Decision Gate

If a business decision is unresolved:

    STOP

Do not invent an answer.

Ask the human to decide.

Once the human provides a decision, record it as an explicit decision before continuing.

---

## Completion Rule

Requirement analysis is complete only when:

- all business rules are explicit
- authoritative sources are identified
- contradictions are identified
- ambiguities are identified
- inferred behavior is separated from authoritative behavior
- implementation impact is understood

Requirement analysis completion MUST NOT authorize implementation.

---

## Safety Gate

Final status MUST be one of:

    REQUIREMENTS COMPLETE — READY FOR ARCHITECTURE

    REQUIREMENTS BLOCKED — HUMAN DECISION REQUIRED

    REQUIREMENTS BLOCKED — AUTHORITATIVE SOURCE CONFLICT
