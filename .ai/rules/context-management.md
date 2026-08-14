# AI Context Management Rule

## Purpose

Ensure AI agents operate from governed repository knowledge rather than model memory or assumptions.

## Mandatory Discovery

Before implementation, an AI agent MUST inspect relevant:

- Business requirements
- Engineering Constitution
- Architecture documentation
- Technical designs
- API contracts
- Persistence designs
- Event designs
- Security designs
- Development standards
- Quality standards
- Existing implementation
- Existing tests
- Build configuration
- Repository configuration

## Context Selection

AI SHOULD retrieve only the context relevant to the current task after establishing the repository baseline.

AI MUST NOT assume that a previous conversation, prompt, or model memory represents the current repository state.

## Repository Reality

The repository is authoritative for its current implementation state.

Documentation is authoritative according to its declared governance status.

If documentation and implementation disagree, AI MUST identify the discrepancy before making changes.

## Context Changes

When a task crosses module or architectural boundaries, AI MUST retrieve the relevant documentation for each affected boundary.

## Context Completion

Before implementation, the agent MUST be able to explain:

- What is being changed
- Why it is being changed
- Which requirements justify it
- Which architecture governs it
- Which modules are affected
- Which constraints apply
- How the change will be validated
