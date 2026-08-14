---
name: repository-discovery
description: Establish the current repository baseline before engineering work. Use before requirement analysis, architecture analysis, technical design, implementation planning, implementation, testing, review, or significant changes.
---

# Repository Discovery

## Purpose

Establish an evidence-based understanding of the current repository before making engineering decisions or changes.

This skill is read-only unless the calling workflow explicitly authorizes a separate change.

## Core Principle

Never assume the repository state.

Inspect the repository and use observed evidence to establish:

- structure
- technologies
- modules
- dependencies
- build system
- configuration
- tests
- documentation
- existing implementation
- architectural boundaries
- current working-tree state

## Required Discovery Sequence

### 1. Establish Repository Root

Determine:

- current working directory
- repository root
- Git branch
- Git status

The repository root MUST be used as the reference point for subsequent discovery.

### 2. Inspect Top-Level Structure

Identify:

- backend
- frontend
- mobile
- infrastructure
- documentation
- configuration
- AI/agent configuration
- build and deployment files

Do not assume that a directory exists merely because documentation refers to it.

### 3. Inspect Technology Stack

Inspect authoritative build/configuration files such as:

- `pom.xml`
- `build.gradle`
- `package.json`
- lock files
- application configuration
- Docker files
- CI/CD configuration

Record observed technologies and versions where relevant.

### 4. Inspect Existing Modules

Determine the actual implementation modules and packages.

For Spring Modulith projects, inspect:

- root package
- module packages
- module boundaries
- `package-info.java`
- application modules
- contracts
- inbound adapters
- outbound adapters

Compare implementation reality against authoritative architecture documentation.

### 5. Inspect Existing Implementation

Identify relevant:

- controllers
- application services
- domain models
- repositories
- persistence mappings
- configuration
- integrations
- event handlers
- tests

Do not inspect every file unnecessarily. Narrow the search after establishing the repository structure.

### 6. Inspect Documentation

Retrieve documentation relevant to the requested task.

Prioritize:

1. Engineering Constitution
2. approved business requirements
3. architecture decisions
4. technical architecture
5. technical design
6. API contracts
7. persistence/event/security documentation
8. implementation standards
9. existing AI rules and skills

Treat document authority according to its declared classification.

### 7. Inspect Existing Tests

Identify:

- unit tests
- integration tests
- architecture tests
- API tests
- persistence tests
- security tests
- end-to-end tests

Record existing testing conventions rather than inventing new ones.

### 8. Inspect Dependencies

Identify dependencies relevant to the requested change.

Do not recommend adding a dependency merely because an alternative library exists.

Check whether an existing dependency already provides the required capability.

### 9. Inspect Git State

Always determine:

- current branch
- uncommitted changes
- untracked files
- recent relevant commits

Never overwrite, discard, or silently incorporate unrelated working-tree changes.

## Evidence Classification

Every significant discovery MUST be classified as one of:

- OBSERVED — directly verified in repository
- DOCUMENTED — stated by authoritative documentation
- INFERRED — derived from observed/documented evidence
- PROPOSED — recommendation not yet approved
- UNRESOLVED — requires human decision
- SUPERSEDED — previously valid but replaced
- HISTORICAL — retained for context only

Never present an inference or proposal as an observed fact.

## Contradiction Detection

When documentation and implementation disagree:

1. Identify both sources.
2. Determine their authority.
3. Report the contradiction.
4. Do not silently modify either source.
5. Stop implementation if the contradiction affects correctness or architecture.

## Change Safety

This skill MUST NOT:

- modify source code
- modify dependencies
- create migrations
- modify database schemas
- modify configuration
- create tests
- modify architecture documents
- modify requirements
- create implementation artifacts

unless a separate explicitly authorized workflow performs those actions.

## Output

Produce a concise repository baseline containing:

### Repository

- root
- branch
- working-tree status

### Structure

- major directories
- implementation modules

### Technology

- backend
- frontend/mobile
- database
- infrastructure
- testing

### Architecture

- relevant modules
- important boundaries
- applicable architecture documents

### Existing Implementation

- relevant files/components
- existing patterns

### Dependencies

- relevant existing dependencies
- potentially missing dependencies

### Tests

- existing relevant test coverage/conventions

### Risks

- contradictions
- missing context
- unresolved decisions
- unexpected repository state

### Readiness

State one of:

- READY FOR NEXT STAGE
- BLOCKED — MISSING CONTEXT
- BLOCKED — ARCHITECTURAL CONTRADICTION
- BLOCKED — HUMAN DECISION REQUIRED

## Completion Rule

Repository discovery is complete only when the agent can explain what exists today and distinguish it from what the documentation says should exist.

The agent MUST NOT begin implementation merely because discovery completed.
