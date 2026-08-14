---
description: Perform repository discovery before any planning or implementation. Read-only.
---

# Repository Discovery Workflow

## Purpose

Establish the factual state of the repository before making implementation decisions.

This workflow is READ-ONLY.

It MUST NOT modify:

- source code
- tests
- dependencies
- configuration
- database migrations
- documentation
- AI configuration

unless a separate explicitly authorized workflow is invoked afterward.

---

## Required Skill

Use:

    .ai/skills/repository-discovery/SKILL.md

---

## Execution Order

### Step 1 — Repository Structure

Inspect:

- repository root
- backend
- frontend
- mobile
- infrastructure
- tests
- configuration
- documentation
- AI configuration

### Step 2 — Existing Implementation

Identify what actually exists:

- modules
- packages
- services
- controllers
- repositories
- domain objects
- configurations
- dependencies
- migrations
- tests

### Step 3 — Architecture Evidence

Inspect authoritative architecture documents and identify:

- approved modules
- prohibited modules
- module boundaries
- dependency rules
- persistence rules
- API rules
- event rules
- security rules

### Step 4 — Requirements Evidence

Identify relevant requirements and distinguish:

- authoritative requirements
- inferred behavior
- technical proposals
- unresolved decisions
- superseded decisions

### Step 5 — Reality vs Documentation

Produce two explicit sections:

## Exists Today

Only facts verified from the repository.

## Documented / Planned

Only facts stated by project documentation.

Never merge these two categories.

---

## Required Output

Produce:

1. Repository structure summary
2. Existing implementation inventory
3. Existing dependency inventory
4. Existing persistence inventory
5. Existing API inventory
6. Existing test inventory
7. Architecture baseline
8. Requirements baseline
9. Exists Today
10. Documented / Planned
11. Gaps
12. Risks
13. Questions requiring human clarification

---

## Completion Gate

Discovery is complete only when the agent can explain:

- what exists today
- what does not exist
- what the documentation says should exist
- where those two differ

Completion of discovery MUST NOT trigger implementation.

---

## Safety Gate

Final status MUST be one of:

    DISCOVERY COMPLETE — READ-ONLY

or

    DISCOVERY BLOCKED — INFORMATION MISSING
