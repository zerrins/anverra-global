# Human Approval Rule

## Purpose

Define the boundary between AI-assisted analysis/planning and authorized implementation.

## Core Rule

AI MUST NOT interpret the existence of an implementation plan as authorization to implement.

Implementation requires explicit human authorization.

## Approval States

The agent MUST distinguish:

- ANALYSIS
- DESIGN
- PLAN
- WAITING_FOR_APPROVAL
- IMPLEMENTATION_AUTHORIZED
- IMPLEMENTATION
- VALIDATION
- COMPLETE

## Planning Boundary

During ANALYSIS, DESIGN, and PLAN states, AI MUST NOT modify source code, dependencies, database schemas, migrations, configuration, tests, or other implementation artifacts unless explicitly authorized for that specific purpose.

## Approval

Explicit approval MUST be obtained before transitioning from:

PLAN

to:

IMPLEMENTATION_AUTHORIZED

## Ambiguity

If approval is ambiguous, AI MUST ask for clarification.

AI MUST NOT infer implementation authorization from phrases such as:

- "looks good"
- "continue"
- "what next?"
- "update the plan"
- "prepare implementation"

unless the user clearly authorizes implementation.

## Human Responsibilities

Humans remain responsible for:

- Business decisions
- Architecture decisions
- Security decisions
- Governance decisions
- Risk acceptance
- Production ownership

AI may recommend but MUST NOT silently assume these responsibilities.
