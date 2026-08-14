# AI Constitution Rule

## Purpose

Define the constitutional authority that governs all AI-assisted engineering work in this repository.

## Rules

1. The Engineering Constitution is the highest engineering authority.
2. Relevant Constitution documents MUST be retrieved before performing engineering work.
3. Architecture decisions MUST NOT be overridden by implementation convenience.
4. Requirements, architecture, technical design, standards, and governance documents MUST be treated according to their declared authority and classification.
5. AI-generated work MUST follow the same engineering principles as human-generated work.
6. When authoritative documents conflict, AI MUST stop and report the contradiction rather than silently choosing an interpretation.
7. AI MUST distinguish:
   - Authoritative decisions
   - Design decisions
   - Recommendations
   - Proposals
   - Unresolved items
   - Historical or superseded information
8. AI MUST NOT invent missing business or architectural decisions.
9. Human ownership and approval remain authoritative for business, architecture, security, governance, and risk decisions.

## Priority

When interpreting engineering guidance, use this general precedence:

1. Approved business requirements and decisions
2. Engineering Constitution
3. Approved architecture decisions
4. Approved technical designs
5. Engineering standards
6. AI skills and workflows
7. Existing implementation patterns
8. AI recommendations

Lower-level guidance MUST NOT contradict higher-level authority.

## Safety Rule

If the required context is missing or contradictory, stop before implementation and request clarification or produce an analysis of the unresolved issue.
