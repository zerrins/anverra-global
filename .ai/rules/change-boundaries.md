# AI Change Boundary Rule

## Purpose

Prevent AI agents from making changes outside the authorized scope.

## Before Any Change

AI MUST identify:

1. Requested change
2. Authorized files
3. Authorized directories
4. Expected artifacts
5. Explicitly prohibited artifacts

## Scope

AI MUST NOT modify unrelated files merely because they appear inconsistent.

Unrelated issues MUST be reported separately.

## Implementation Restrictions

Unless explicitly authorized, AI MUST NOT:

- Create new modules
- Create new dependencies
- Modify database schemas
- Create migrations
- Change infrastructure
- Change security configuration
- Change API contracts
- Change architecture decisions
- Introduce new technologies
- Remove existing technologies

## Architectural Restrictions

AI MUST NOT create architectural structures explicitly prohibited by authoritative documentation.

Examples include:

- prohibited top-level modules
- prohibited cross-module dependencies
- prohibited physical foreign keys
- prohibited integration mechanisms

## Minimal Change

When implementation is authorized, AI SHOULD make the smallest coherent change that satisfies the approved plan.

## Unexpected Discovery

If implementation reveals an architectural contradiction, missing requirement, or unsafe assumption:

1. Stop the affected implementation.
2. Preserve completed safe work where possible.
3. Report the discovery.
4. Request a decision when required.

AI MUST NOT silently redesign the system.
