# Repository Architecture

**Document ID:** AEOS-P02-S01-D02  
**Version:** 1.0  
**Status:** Baseline  
**Phase:** 2 — Application & Repository Blueprint  
**Stage:** 1 — System & Repository Blueprint  
**System:** AnverraGlobal

---

# 1. Purpose

This document defines the target repository architecture for the AnverraGlobal monorepo.

The System Blueprint (`01-anverra-system-blueprint.md`) established what AnverraGlobal is, what it contains, and how its major components relate to one another. It explicitly deferred the detailed repository layout, per-application internal structure, folder responsibilities, and naming conventions to this document.

This document answers:

> **How are the system components, application surfaces, supporting concerns, engineering assets, and documentation organized inside the AnverraGlobal repository?**

It adapts the AEOS canonical repository structure — which assumes a single-application repository — to the specific requirements of the AnverraGlobal multi-application monorepo.

This document does not define:

- Application boundary contracts (see `03-application-boundaries.md`)
- Detailed module internals or hexagonal layer rules (see `04-architectural-boundaries.md`)
- Technology choices, deployment architecture, or CI/CD implementation (later phases)

This document does not duplicate the Engineering Constitution. It applies constitutional repository principles to the specific AnverraGlobal repository.

---

# 2. Governing Principles

## 2.1 Constitutional Foundation

The following constitutional documents govern repository organization:

| Document | Governing Concern |
|----------|------------------|
| Repository Philosophy (AEC-REP-001) | Repository is an authoritative engineering workspace; organization is a constitutional requirement |
| Folder Structure (AEC-REP-002) | Canonical folder layout, naming standards, folder definitions, AI discovery order |
| Module Organization (AEC-REP-003) | Business-capability-first source organization, canonical module structure, module taxonomy |
| Modular Monolith (AEC-ARC-003) | Backend is a single deployable application with independent business modules |
| Architecture First (AEC-ARC-001) | Architecture defines structure before implementation; implementation realizes architecture |

These documents define the principles. This document applies them to the AnverraGlobal repository.

## 2.2 Monorepo Adaptation

The Constitution's canonical folder structure assumes a single-application repository with a single `src/` directory at the root.

AnverraGlobal is a monorepo containing three distinct application surfaces (Backend, Web, Mobile) and multiple supporting concerns (Infrastructure, AI Engineering, Documentation, Architecture).

The monorepo adaptation follows these rules:

1. **Application directories replace `src/`** — The canonical root-level `src/` is replaced by `backend/`, `frontend/`, and `mobile/`, each representing a distinct application surface established by the System Blueprint.

2. **Each application directory follows the canonical structure internally** — Within each application directory, the canonical layout concepts (source, tests, docs, config) are applied at the application level.

3. **System-level directories remain at the repository root** — Directories that serve the entire system (documentation, architecture, infrastructure, scripts, tools, AI engineering, configuration) remain at the repository root, consistent with the canonical layout.

4. **Business modules exist inside the Backend Application only** — The Modular Monolith's business-capability modules reside within `backend/src/modules/`. Other application directories do not contain business modules.

5. **Constitutional principles are not weakened** — The monorepo adaptation changes the structural location of application code but does not weaken any constitutional rule regarding module boundaries, dependency direction, naming standards, or organizational principles.

This adaptation is anticipated by the System Blueprint (§ 11.2) and acknowledged by the Constitution's Folder Structure document, which states that additional folders may be introduced when justified.

---

# 3. Repository Classification

## 3.1 Repository Model

| Attribute | Value |
|-----------|-------|
| Repository Name | anverra-global |
| Repository Model | Monorepo |
| Application Surfaces | 3 — Backend, Web (frontend), Mobile |
| Supporting Concerns | Infrastructure, AI Engineering, Documentation, Architecture, Scripts, Tools, Configuration, Repository Automation |
| Source of Truth | This document for repository organization; System Blueprint for system identity and component roles |

## 3.2 Repository Hierarchy

The AnverraGlobal repository organizes content across four distinct hierarchy levels:

### Level 1 — Repository Level

The repository root. Contains system-wide directories and files that serve the entire AnverraGlobal system.

Directories at this level include application surface directories, system documentation, architecture artifacts, infrastructure definitions, AI engineering assets, automation scripts, engineering tools, repository automation, and system-wide configuration.

No business logic or application source code exists directly at the repository root.

### Level 2 — Application Level

Application surface directories at the repository root: `backend/`, `frontend/`, `mobile/`.

Each application directory is the top-level container for one complete application surface. Application directories are not business modules — they are repository and organizational boundaries for distinct application surfaces.

### Level 3 — Application Internal Level

Directories inside each application directory that organize the application's source code, tests, documentation, configuration, and resources.

At this level, each application follows a consistent internal structure adapted from the canonical layout.

### Level 4 — Backend Business Module Level

Directories inside `backend/src/modules/` that represent individual business capability modules.

Each module follows the canonical module structure defined by the Constitution's Module Organization document. Module-level organization is the deepest structural level defined by this document.

Detailed module internals (domain, application, infrastructure, interfaces, contracts, events, configuration, documentation, tests) are governed by the Module Organization and Modular Monolith constitutional documents and elaborated in `04-architectural-boundaries.md`. This document establishes where modules reside, not how they are internally structured.

---

# 4. Target Repository Layout

## 4.1 Complete Target Tree

The following represents the target repository structure for AnverraGlobal:

```text
anverra-global/                          (repository root)
│
├── backend/                             (Backend Application — Modular Monolith)
│   ├── src/
│   │   ├── modules/                     (business-capability modules)
│   │   │   ├── identity/                (Identity & Access — platform module)
│   │   │   ├── customer/                (Customer Management — core business module)
│   │   │   ├── product/                 (Insurance Product Catalogue — core business module)
│   │   │   ├── policy/                  (Policy Lifecycle Management — core business module)
│   │   │   ├── commission/              (Commission Management — core business module)
│   │   │   ├── notification/            (Notification Management — supporting module)
│   │   │   └── reporting/               (Reporting & Analytics — supporting module)
│   │   └── platform/                    (shared technical infrastructure)
│   ├── tests/                           (backend application-level tests)
│   ├── docs/                            (backend-specific documentation)
│   ├── config/                          (backend configuration templates)
│   └── resources/                       (non-source backend artifacts)
│
├── frontend/                            (Web Application)
│   ├── src/                             (web application source code)
│   ├── tests/                           (web application tests)
│   ├── docs/                            (web application documentation)
│   ├── config/                          (web application configuration)
│   └── resources/                       (static assets, images, templates)
│
├── mobile/                              (Mobile Application)
│   ├── src/                             (mobile application source code)
│   ├── tests/                           (mobile application tests)
│   ├── docs/                            (mobile application documentation)
│   └── config/                          (mobile application configuration)
│
├── docs/                                (system-level documentation)
│   ├── constitution/                    (Engineering Constitution — Phase 1)
│   ├── repository-blueprint/            (Application & Repository Blueprint — Phase 2)
│   ├── adr/                             (Architecture Decision Records)
│   ├── api/                             (API documentation)
│   ├── business/                        (business documentation)
│   ├── deployment/                      (deployment guides)
│   ├── operations/                      (operational runbooks)
│   ├── onboarding/                      (onboarding guides)
│   └── decisions/                       (engineering decisions)
│
├── architecture/                        (system-level architecture artifacts)
│
├── infrastructure/                      (infrastructure definitions)
│
├── .ai/                                 (AI engineering workspace)
│   ├── prompts/                         (prompt templates)
│   ├── workflows/                       (AI workflows)
│   ├── memory/                          (engineering memory)
│   ├── context/                         (context documents)
│   ├── templates/                       (AI templates)
│   ├── rules/                           (coding rules)
│   └── skills/                          (AI skills)
│
├── scripts/                             (repository-wide automation scripts)
│
├── tools/                               (engineering tooling)
│
├── .github/                             (repository automation)
│   ├── workflows/                       (CI/CD workflows)
│   ├── ISSUE_TEMPLATE/                  (issue templates)
│   ├── PULL_REQUEST_TEMPLATE.md         (PR template)
│   └── CODEOWNERS                       (code ownership)
│
├── config/                              (system-wide configuration templates)
│
├── README.md                            (repository entry point)
├── CHANGELOG.md                         (change history)
├── LICENSE                              (license)
└── CONTRIBUTING.md                      (contribution guidelines)
```

Additional module directories will be introduced after their capability-to-module ownership decisions are formally resolved.

## 4.2 Root-Level Directories

Each root-level directory serves a specific purpose within the monorepo:

| Directory | Category | Purpose |
|-----------|----------|---------|
| `backend/` | Application surface | Backend Application source, tests, config, resources. Contains the Modular Monolith. |
| `frontend/` | Application surface | Web Application source, tests, config, resources. Client application consuming backend APIs. |
| `mobile/` | Application surface | Mobile Application source, tests, config. Client application consuming backend APIs. |
| `docs/` | System-level | System-wide engineering documentation including the Constitution, blueprints, ADRs, and operational guides. |
| `architecture/` | System-level | System-level architecture artifacts: context diagrams, container diagrams, data flow models. |
| `infrastructure/` | Supporting concern | Infrastructure definitions for runtime environments. Not a business application. |
| `.ai/` | Supporting concern | AI engineering assets: prompts, workflows, memory, context, rules, skills. Not a business application. |
| `scripts/` | Engineering support | Repository-wide automation, build, release, and maintenance scripts. |
| `tools/` | Engineering support | Engineering tooling: static analysis, code generation, internal utilities. |
| `.github/` | Engineering support | Repository automation: CI/CD workflows, PR templates, issue templates, code ownership. |
| `config/` | Engineering support | System-wide configuration templates. Environment-specific values are not committed. |

**Directories not present at the repository root:**

| Canonical Directory | Reason for Absence at Root |
|--------------------|--------------------------|
| `src/` | Replaced by `backend/`, `frontend/`, `mobile/` in the monorepo adaptation. Each application contains its own `src/` internally. |
| `tests/` | Application-level tests reside inside each application directory. A root-level `tests/` directory may be introduced later for cross-application or system-level end-to-end testing if required. It is not mandated at this stage. |
| `examples/` | Not immediately required. May be introduced inside specific application directories or at root level when justified by engineering need. |
| `resources/` | Application-specific resources reside inside each application directory. A root-level `resources/` is not required at this stage. |
| `build/` | Generated build artifacts are not version-controlled. Build output directories exist per-application as needed by the chosen build systems. |

**AI Engineering Directory — Normalization Decision:**

The canonical folder structure defined by the Constitution specifies `.ai/` (dot-prefixed) as the location for AI engineering assets. The System Blueprint (§ 11.2) referenced `ai/` (non-dot-prefixed) in its high-level monorepo illustration.

This document normalizes to `.ai/` for the following reasons:

1. The Constitution's Folder Structure (AEC-REP-002) is the authoritative source for repository organization and explicitly specifies `.ai/`.
2. The dot-prefix convention is consistent with `.github/` — both are engineering-infrastructure directories rather than business-application directories.
3. The existing `ai/` in the repository is an empty, untracked placeholder directory — no tracked content or established convention depends on the `ai/` name.
4. The System Blueprint's `ai/` reference was a high-level illustration, not a binding repository-organization decision. The System Blueprint explicitly defers detailed repository layout to this document.

The target repository uses `.ai/` exclusively. The `ai/` placeholder directory should be removed when the repository structure is implemented.

## 4.3 Root-Level Files

| File | Current State | Target State |
|------|--------------|-------------|
| `README.md` | Present — minimal (61 bytes) | **Required.** The repository entry point. Content will be expanded as the system matures. |
| `CHANGELOG.md` | Not present | **Required.** Introduced when the first tracked release or significant milestone occurs. |
| `LICENSE` | Not present | **Required.** Introduced when the licensing decision is made. |
| `CONTRIBUTING.md` | Not present | **Required.** Introduced when engineering contribution guidelines are formalized. |
| `.gitignore` | Not present as a root file | **Required.** Introduced when the repository structure is implemented. |

These files are declared as part of the target layout. They are not created by this document.

---

# 5. Application Architecture

## 5.1 Common Application Structure

Each application directory follows a consistent high-level internal structure adapted from the canonical layout:

```text
<application>/
├── src/                  (application source code)
├── tests/                (application tests)
├── docs/                 (application-specific documentation)
├── config/               (application configuration templates)
└── resources/            (non-source application artifacts — where applicable)
```

This common structure ensures:

- Predictable navigation across all application directories
- Consistent placement of source, tests, documentation, and configuration
- Application-level independence while maintaining system-wide consistency

Technology-specific internal structures (framework-imposed directories, build system layouts) exist within this common shell and are determined by the chosen technology stack for each application.

## 5.2 Backend Application

**Directory:** `backend/`  
**System Role:** Backend Application — the Modular Monolith  
**Established by:** System Blueprint § 7.1, § 10.1

The Backend Application is the core of the AnverraGlobal system. It is the single deployable application composed of business modules organized around capabilities.

```text
backend/
├── src/
│   ├── modules/              (business-capability modules — see § 6)
│   └── platform/             (shared technical infrastructure — see § 6.4)
├── tests/                    (integration tests, cross-module tests)
├── docs/                     (backend-specific documentation)
├── config/                   (backend configuration templates)
└── resources/                (non-source backend artifacts)
```

**Key structural decisions:**

- `src/modules/` is the container for all business-capability modules. It is a source-code organizational container, not an architectural layer or a business module itself. See § 6 for details.
- `src/platform/` contains shared technical infrastructure (security, logging, framework configuration, common utilities). Business logic does not reside in `platform/`.
- `tests/` contains tests that span multiple modules or verify cross-module integration. Module-specific unit and integration tests reside within each module's internal `tests/` directory.
- `docs/` contains backend-specific documentation that is not system-level. System-level documentation resides in the root `docs/` directory.
- `config/` contains backend configuration templates. Environment-specific values are not committed.
- `resources/` contains non-source backend artifacts such as templates, sample data, or static resources.

The backend's technology stack (language, framework, build system) has not been decided. Technology-specific internal structures (e.g., `src/main/java`, Gradle/Maven configuration, Docker files) are determined by the chosen technology and do not alter the organizational structure defined here.

## 5.3 Web Application

**Directory:** `frontend/`  
**System Role:** Web Application  
**Established by:** System Blueprint § 7.1

The Web Application provides a browser-based user interface. It is a client application that consumes Backend Application APIs. The Backend owns the business capabilities and authoritative business rules. The Web Application, as a client surface, may contain presentation logic, interaction logic, client-appropriate validation, local state/models, and technology-specific client concerns. Detailed application boundaries are deferred to Document 03.

```text
frontend/
├── src/                      (web application source code)
├── tests/                    (web application tests)
├── docs/                     (web application documentation)
├── config/                   (web application configuration)
└── resources/                (static assets, images, templates)
```

**Directory name mapping:** The directory name `frontend/` maps to the system concept "Web Application" as established by the System Blueprint (§ 7.1, § 11.2). The name `frontend/` is retained because it is established in the frozen System Blueprint and is descriptive of the application's role as a client-side application surface.

The Web Application's technology stack (framework, bundler, CSS approach) has not been decided. Framework-specific internal structures (e.g., `components/`, `pages/`, `hooks/`, `features/`) are determined by the chosen technology. This document defines the outer organizational shell only.

## 5.4 Mobile Application

**Directory:** `mobile/`  
**System Role:** Mobile Application  
**Established by:** System Blueprint § 7.1

The Mobile Application provides a mobile interface for field users. It is a client application that consumes Backend Application APIs. As with the Web Application, the Backend owns the authoritative business rules, while the Mobile Application may contain presentation logic, interaction logic, client-appropriate validation, local state/models, and technology-specific client concerns. Detailed application boundaries are deferred to Document 03.

```text
mobile/
├── src/                      (mobile application source code)
├── tests/                    (mobile application tests)
├── docs/                     (mobile application documentation)
└── config/                   (mobile application configuration)
```

The Mobile Application's target user groups, feature scope, and implementation technology are not yet decided (System Blueprint open decision D5). The technology choice (native, hybrid, cross-platform, or other approach) will determine the internal source organization. This document defines the outer organizational shell only.

---

# 6. Backend Module Container

## 6.1 Module Container

Business-capability modules reside in:

```text
backend/src/modules/
```

`modules/` is a source-code organizational container. It is not an architectural layer, not a business module, and not a deployment unit.

Each directory immediately below `modules/` represents one business-capability module. Each module:

- Owns exactly one primary business capability
- Follows the canonical module organization defined by the Constitution (AEC-REP-003)
- Communicates with other modules through explicit contracts

The detailed internal structure of each module (domain, application, infrastructure, interfaces, contracts, events, configuration, documentation, tests) is governed by the Module Organization and Modular Monolith constitutional documents and elaborated in `04-architectural-boundaries.md`. This document establishes where modules reside and which modules are established.

## 6.2 Established Modules

The following modules have established capability-to-module ownership as determined by the System Blueprint (§ 8.3):

| Module Directory | Business Capability | Module Category |
|-----------------|--------------------|-----------------| 
| `identity/` | Identity & Access | Platform |
| `customer/` | Customer Management | Core Business |
| `product/` | Insurance Product Catalogue | Core Business |
| `policy/` | Policy Lifecycle Management | Core Business |
| `commission/` | Commission Management | Core Business |
| `notification/` | Notification Management | Supporting Business |
| `reporting/` | Reporting & Analytics | Supporting Business |

Capability ownership is established by the appropriate upstream business and system architecture decisions (e.g., the System Blueprint). This document establishes only the repository location of these already-established modules. It does not independently decide or authorize business capability ownership. Their directories are part of the target repository structure based on those prior decisions.

## 6.3 Future Modules

The following business capabilities have unresolved module-ownership decisions as documented in the System Blueprint:

| Capabilities | Open Decision | System Blueprint Reference |
|-------------|--------------|---------------------------|
| Agent Management, Dealer Management, Partner Management | Whether these map to separate modules, a unified module, or a hybrid structure | See unresolved capability-to-module decisions documented in the System Blueprint |
| Proposal Management | Whether Proposal is a separate module or part of the Policy module | See unresolved capability-to-module decisions documented in the System Blueprint |
| Document & KYC Management | Whether these form a single module or separate modules | See unresolved capability-to-module decisions documented in the System Blueprint |
| Administration | Whether this is a dedicated platform module or distributed across modules | See unresolved capability-to-module decisions documented in the System Blueprint |

Additional module directories for these capabilities will be added to `backend/src/modules/` when their ownership decisions are formally resolved. This document does not pre-resolve those decisions.

## 6.4 Platform Technical Code

Shared technical infrastructure resides in:

```text
backend/src/platform/
```

`platform/` contains cross-cutting technical capabilities that are shared across business modules. Following the Constitution's Modular Monolith principle (AEC-ARC-003), shared code is limited to technical infrastructure. Examples of technical concerns that may reside in `platform/` include:

- Security utilities
- Logging infrastructure
- Framework configuration
- Common technical utilities
- Infrastructure abstractions

Detailed platform and shared-code boundaries are deferred to Document 04.

**Business logic does not reside in `platform/`.**

If business logic appears to be shared across modules, the business model and module boundaries should be reviewed before extracting code into `platform/`. The Constitution explicitly prohibits shared business logic modules and "common" modules containing unrelated functionality.

`platform/` is not a business module. It does not own a business capability. It provides technical services that business modules depend upon.

---

# 7. Supporting Repository Areas

## 7.1 Documentation

**Directory:** `docs/`  
**Level:** Repository (root)

`docs/` is the system-level documentation root for AnverraGlobal.

**Currently established:**

| Subdirectory | Content | Phase |
|-------------|---------|-------|
| `docs/constitution/` | Engineering Constitution | Phase 1 |
| `docs/repository-blueprint/` | Application & Repository Blueprint | Phase 2 |

**Target documentation areas:**

The following documentation areas are defined by the canonical folder structure and will be introduced as the project progresses:

| Subdirectory | Purpose |
|-------------|---------|
| `docs/adr/` | Architecture Decision Records |
| `docs/api/` | API documentation and specifications |
| `docs/business/` | Business domain documentation |
| `docs/deployment/` | Deployment guides |
| `docs/operations/` | Operational runbooks |
| `docs/onboarding/` | Engineering onboarding guides |
| `docs/decisions/` | Engineering decisions beyond ADRs |

These subdirectories are introduced when their content is authored. Empty placeholder directories are not required.

**Scope:** Root-level `docs/` contains system-wide documentation. Application-specific documentation resides inside each application's `docs/` directory (e.g., `backend/docs/`, `frontend/docs/`).

## 7.2 Architecture

**Directory:** `architecture/`  
**Level:** Repository (root)

`architecture/` contains system-level architecture artifacts that describe the AnverraGlobal system as a whole.

Examples of artifacts that belong here:

- System context diagrams (C4 Level 1)
- Container diagrams (C4 Level 2)
- System-level data flow diagrams
- High-level architecture models
- Cross-application integration diagrams

This directory contains architecture documentation and models. It does not contain application source code, business logic, or module implementation.

Application-level and module-level architecture documentation resides within the relevant application directory (e.g., `backend/docs/`). System-level architecture that spans applications and describes the overall system belongs in the root `architecture/` directory.

## 7.3 Infrastructure

**Directory:** `infrastructure/`  
**Level:** Repository (root)

`infrastructure/` contains infrastructure definitions for the runtime environments that support all application surfaces.

This may include infrastructure-as-code definitions, cloud resource configurations, deployment configurations, and networking definitions.

Infrastructure is a supporting concern. It is not a business application. It does not contain business logic.

Specific infrastructure technologies (cloud providers, orchestration tools, IaC frameworks) are not defined by this document. Those decisions belong to later technology and deployment phases.

## 7.4 AI Engineering

**Directory:** `.ai/`  
**Level:** Repository (root)

`.ai/` is the canonical AI engineering workspace as defined by the Constitution's Folder Structure (AEC-REP-002).

```text
.ai/
├── prompts/          (prompt templates for AI-assisted engineering)
├── workflows/        (AI workflow definitions)
├── memory/           (engineering memory and context persistence)
├── context/          (context documents for AI agents)
├── templates/        (AI engineering templates)
├── rules/            (coding rules and constraints for AI agents)
└── skills/           (AI skill definitions)
```

| Subdirectory | Purpose |
|-------------|---------|
| `prompts/` | Reusable prompt templates that guide AI-assisted engineering tasks |
| `workflows/` | Defined AI workflows for common engineering activities |
| `memory/` | Persistent engineering memory that AI agents use for continuity |
| `context/` | Context documents that provide domain, architectural, or project knowledge to AI agents |
| `templates/` | Templates for AI-generated artifacts (code, documentation, configurations) |
| `rules/` | Explicit coding rules and constraints that AI agents must follow |
| `skills/` | Skill definitions that extend AI agent capabilities for specialized tasks |

`.ai/` is an engineering-support directory. It does not contain runtime business logic. AI engineering assets support the development process under AEOS governance — they do not execute runtime business behavior within the deployed application.

The specific prompts, workflows, skills, and rules are not defined by this document. They are authored as AI engineering practices are established.

## 7.5 Scripts

**Directory:** `scripts/`  
**Level:** Repository (root)

`scripts/` contains repository-wide automation scripts.

Examples:

- Build automation
- Release scripts
- Database migration runners
- Maintenance utilities
- Development environment setup

Scripts in this directory serve the repository as a whole. Application-specific build scripts may reside within the relevant application directory.

`scripts/` does not contain business logic, production source code, or application runtime code.

## 7.6 Tools

**Directory:** `tools/`  
**Level:** Repository (root)

`tools/` contains engineering tooling — software utilities that support the engineering process.

Examples:

- Static analysis configurations or custom rules
- Code generation utilities
- Internal engineering utilities
- Repository validation tools

`tools/` is distinct from `scripts/`. Scripts are automation sequences that perform tasks. Tools are software utilities that provide capabilities.

`tools/` does not contain business logic, production source code, or application runtime code.

## 7.7 GitHub Automation

**Directory:** `.github/`  
**Level:** Repository (root)

`.github/` contains repository automation for GitHub-based engineering workflows.

```text
.github/
├── workflows/                   (CI/CD workflow definitions)
├── ISSUE_TEMPLATE/              (issue templates)
├── PULL_REQUEST_TEMPLATE.md     (pull request template)
└── CODEOWNERS                   (code ownership definitions)
```

CI/CD pipeline design, workflow implementation, and automation details are not defined by this document. Those belong to later engineering phases.

## 7.8 Configuration

**Directory:** `config/`  
**Level:** Repository (root)

`config/` contains system-wide configuration templates.

Configuration templates define the structure and expected values for configuration without containing environment-specific secrets or credentials.

**Rules:**

- Environment-specific values are not committed to the repository.
- Secrets and credentials are never stored in `config/` or anywhere in the repository.
- Application-specific configuration resides inside each application's `config/` directory (e.g., `backend/config/`, `frontend/config/`).
- Root-level `config/` is for system-wide or cross-application configuration templates.

The specific configuration management approach and tooling are not defined by this document.

---

# 8. Testing Organization

Testing artifacts are organized at multiple levels within the repository:

## Module-Level Tests

Each backend business module contains its own `tests/` directory as part of the canonical module structure. Module-level tests include unit tests and module-scoped integration tests.

Module tests reside inside the module and are governed by the Module Organization constitutional document.

## Application-Level Tests

Each application directory contains a `tests/` directory:

| Directory | Scope |
|-----------|-------|
| `backend/tests/` | Backend integration tests, cross-module tests, backend-level contract tests |
| `frontend/tests/` | Web application tests (unit, integration, component) |
| `mobile/tests/` | Mobile application tests (unit, integration) |

Application-level tests verify behavior that spans multiple modules within a single application or that tests the application as a cohesive unit.

## System-Level Tests

Cross-application and end-to-end tests that verify behavior across application boundaries (e.g., Web → Backend → Database) are not yet required.

When system-level end-to-end testing is introduced, a root-level `tests/` directory may be added to contain those test suites. This decision is deferred until the application boundaries and testing strategy are established.

## Test Technology

Specific testing frameworks, test runners, assertion libraries, and test infrastructure are not defined by this document. Those are determined by the chosen technology stack for each application.

---

# 9. Folder Naming Standards

The following naming standards apply throughout the AnverraGlobal repository, consistent with the Constitution's Folder Structure (AEC-REP-002) and Naming Conventions (AEC-REP-004):

## Required

- Use **lowercase** directory names
- Use **descriptive** names that communicate purpose
- Use **business-oriented** names for business-capability modules
- Use **consistent** names across the repository
- Place artifacts in **predictable** locations

## Prohibited

The following directory names are prohibited unless explicitly justified by an architectural decision:

| Prohibited Name | Reason |
|----------------|--------|
| `misc/` | No clear responsibility |
| `temp/` | Temporary content should not be version-controlled |
| `stuff/` | No clear responsibility |
| `helpers/` | Vague; responsibilities should belong to a specific module |
| `common/` | Encourages unrelated functionality in a single location |
| `new/` | Temporal naming; does not describe purpose |
| `old/` | Temporal naming; obsolete content should be archived or removed |
| `backup/` | Backups should not be version-controlled |

## Application Directory Names

| Directory | Maps To | Rationale |
|-----------|---------|-----------|
| `backend/` | Backend Application (Modular Monolith) | Descriptive of the application's role. Established by the System Blueprint. |
| `frontend/` | Web Application | Descriptive of the application's role as a client-side surface. Established by the System Blueprint. |
| `mobile/` | Mobile Application | Technology-neutral. Established by the System Blueprint. |

## Module Directory Names

Backend module directories use business-capability names:

```text
identity/
customer/
product/
policy/
commission/
notification/
reporting/
```

Module names must use approved business terminology and remain consistent with upstream business and domain decisions. They do not use technology-specific, abbreviated, or ambiguous names.

---

# 10. AI Repository Discovery

AI agents interacting with the AnverraGlobal repository should follow this discovery sequence, adapted from the Constitution's AI Repository Discovery order for the monorepo context:

```text
1.  README.md
    ↓  (repository entry point — understand what AnverraGlobal is)

2.  docs/constitution/
    ↓  (Engineering Constitution — understand governing principles)

3.  docs/repository-blueprint/
    ↓  (System Blueprint + Repository Architecture — understand system design)

4.  architecture/
    ↓  (system-level architecture artifacts — understand structural design)

5.  docs/
    ↓  (system documentation — ADRs, business docs, operational guides)

6.  .ai/
    ↓  (AI engineering context — prompts, rules, memory, skills)

7.  backend/docs/  →  frontend/docs/  →  mobile/docs/
    ↓  (application-specific documentation)

8.  backend/src/  →  frontend/src/  →  mobile/src/
    ↓  (application source code — understand implementation)

9.  backend/tests/  →  frontend/tests/  →  mobile/tests/
    ↓  (tests — understand quality expectations)

10. config/  →  backend/config/  →  frontend/config/  →  mobile/config/
    ↓  (configuration — understand runtime settings)

11. infrastructure/
    (infrastructure — understand deployment environment)
```

**Rationale:**

- Steps 1–3 establish context: what the system is, what principles govern it, and how it is structured.
- Steps 4–6 deepen understanding: architecture models, documentation, and AI-specific engineering context.
- Steps 7–9 examine implementation: application documentation first, then source code, then tests.
- Steps 10–11 examine operational context: configuration and infrastructure.

This order maximizes contextual understanding before implementation details, consistent with the Architecture First principle.

AI agents should not begin generating or modifying code before completing at least steps 1–6 of this discovery sequence.

---

# 11. Repository Evolution Rules

The repository structure defined in this document represents the target layout. The repository evolves toward this target through disciplined, constitutional changes.

## Adding Directories

New directories are introduced when their content is ready to be authored. Empty placeholder directories should not be created merely to match the target tree.

Exception: Application surface directories (`backend/`, `frontend/`, `mobile/`) may exist before source code is implemented, as they represent established system boundaries.

## Adding Modules

New business modules are added to `backend/src/modules/` only when:

1. The business capability has been identified and approved
2. The capability-to-module ownership has been resolved (not an open decision)
3. The module follows the canonical module structure

Modules are not added speculatively.

## Modifying Structure

Repository structure changes require:

- Alignment with constitutional principles
- Documentation of the rationale
- Consistency with this document's target layout or an approved update to this document

Ad-hoc structural changes that violate constitutional naming standards, folder organization principles, or hierarchy levels are prohibited.

## Removing Directories

The existing `ai/` placeholder directory should be removed when the repository structure is implemented, as this document establishes `.ai/` as the canonical location for AI engineering assets.

Obsolete directories should be archived or removed rather than left in place. The Constitution's Folder Structure recommends archiving obsolete folders and reviewing repository organization periodically.

---

# 12. Deferred Decisions

The following decisions are explicitly not made by this document:

## Deferred to Document 03 — Application Boundaries

| Topic | Reason |
|-------|--------|
| API contract structure between Web/Mobile and Backend | Application boundary concern |
| Shared type or contract packages across applications | Application boundary concern |
| Authentication flow across application boundaries | Application boundary concern |

## Deferred to Document 04 — Architectural Boundaries

| Topic | Reason |
|-------|--------|
| Detailed hexagonal layer rules within backend modules | Module-internal architecture |
| Module-internal dependency direction enforcement | Module-internal architecture |
| Module communication contract specifications | Module boundary architecture |
| `platform/` scope and boundary rules | Shared-code boundary architecture |
| Canonical module internal structure details | Module-internal architecture |

## Deferred to Later Phases or Technology Decisions

| Topic | Reason |
|-------|--------|
| Frontend framework-specific internal structure | Web technology not yet decided |
| Mobile application internal structure | Mobile technology not yet decided (see unresolved decisions documented in the System Blueprint) |
| Backend build system structure (Gradle, Maven, etc.) | Backend technology not yet decided |
| Database migration folder structure | Data architecture not yet defined |
| CI/CD pipeline design and workflow structure | CI/CD belongs to later engineering phases |
| Docker/containerization file placement | Deployment architecture not yet defined |
| Cloud provider and infrastructure technology | Infrastructure technology not yet decided |
| Specific module directories for unresolved capabilities | See unresolved capability-to-module decisions documented in the System Blueprint |

## Current State vs. Target State

| Aspect | Current State | Target State |
|--------|--------------|-------------|
| Tracked files | `README.md` only | All directories and files defined in this document |
| `docs/` | Untracked; contains `constitution/` and `repository-blueprint/` | Tracked; expanded with `adr/`, `api/`, `business/`, `deployment/`, `operations/`, `onboarding/`, `decisions/` |
| Application directories | Empty placeholders (`backend/`, `frontend/`, `mobile/`) | Populated with internal structure (`src/`, `tests/`, `docs/`, `config/`) |
| AI engineering | Empty `ai/` placeholder | `.ai/` with canonical subdirectories |
| Architecture | Does not exist | `architecture/` present with system-level artifacts |
| GitHub automation | Does not exist | `.github/` with workflows, templates, CODEOWNERS |
| Configuration | Does not exist | `config/` with system-wide templates |
| Root files | `README.md` only | `README.md`, `CHANGELOG.md`, `LICENSE`, `CONTRIBUTING.md`, `.gitignore` |
| Backend modules | No source code | `backend/src/modules/` with established module directories |

The repository evolves from its current skeleton state toward the target state through the phases defined by the Engineering Constitution. This document defines the target — it does not implement it.

---

# 13. Definition of Done

This Repository Architecture document is considered complete and baseline when:

- [ ] The target repository layout is fully defined as a monorepo adaptation of the canonical AEOS structure
- [ ] The monorepo adaptation rationale is explicitly documented
- [ ] All root-level directories are defined with their purpose, category, and constitutional basis
- [ ] All root-level files are identified with their current and target state
- [ ] The four hierarchy levels (repository, application, application-internal, module) are clearly defined
- [ ] Each application surface (backend, frontend, mobile) has a defined high-level internal structure
- [ ] The backend module container (`backend/src/modules/`) is established as an organizational container, not an architectural layer
- [ ] Only established modules (7) are listed by name; unresolved capabilities are explicitly deferred
- [ ] `platform/` is defined as shared technical infrastructure with business logic explicitly prohibited
- [ ] `.ai/` is established as the canonical AI engineering directory with the normalization rationale documented
- [ ] The `docs/` organization includes both current and target subdirectories
- [ ] `architecture/` is defined as the system-level architecture artifact location
- [ ] All supporting directories (infrastructure, scripts, tools, .github, config) are defined
- [ ] Testing organization across module, application, and system levels is defined
- [ ] Folder naming standards are established with prohibited names listed
- [ ] AI agent discovery sequence is adapted for the monorepo
- [ ] Repository evolution rules are defined
- [ ] Deferred decisions are explicitly listed with their target document or phase
- [ ] Current state is clearly distinguished from target state
- [ ] No technology choices have been invented (frameworks, languages, databases, cloud providers)
- [ ] No deployment architecture has been defined
- [ ] No API contracts have been specified
- [ ] No unresolved business capabilities have been silently assigned to modules
- [ ] Infrastructure and AI tooling are described as supporting concerns, not business applications
- [ ] The document is internally consistent
- [ ] The document does not contradict the Engineering Constitution or the System Blueprint

---

*This document is the authoritative repository architecture definition for AnverraGlobal. Application boundaries, architectural boundaries, and blueprint traceability are addressed in companion Stage 1 documents.*
