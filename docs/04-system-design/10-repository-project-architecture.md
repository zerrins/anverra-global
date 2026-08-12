# Repository & Project Structure Architecture

**Document ID:** AEOS-P04-D10  
**Version:** 1.0  
**Status:** Proposed  
**Phase:** 4 — System Design & Implementation Planning  
**System:** AnverraGlobal  
**Depends on:** Phase 1 Engineering Constitution · Phase 2 System Architecture · Phase 3 Technology Architecture · AEOS-P04-D00 through D09

---

# 1. Document Identity

| Field | Value |
|---|---|
| **Document ID** | AEOS-P04-D10 |
| **Title** | Repository & Project Structure Architecture |
| **Version** | 1.0 |
| **Status** | Proposed |
| **Phase** | 4 — System Design & Implementation Planning |
| **System** | AnverraGlobal |
| **Immediate Governing Document** | AEOS-P04-D00 — Phase 4 System Design Overview |

---

# 2. Purpose

This document is a synthesis document that translates the already-established Phase 1–3 and D00-D09 logical architecture into an authoritative physical repository and project structure. It adds implementation depth without changing the established repository outer shell or upstream architectural decisions.

**Core Principle:**
The repository and project structure shall make the established architecture visible, enforceable, navigable, and difficult to violate.

---

# 3. Boundary and Scope

D10 focuses exclusively on the structural arrangement of artifacts.

D10 **governs**:
- Backend physical source and package structure.
- Business module structural templates.
- Shared technical code placement.
- Client (Web/Mobile) project structures.
- Persistence, API, security, and configuration resource placement.
- Generated vs. human-maintained code isolation.

D10 **does not govern**:
- Repository outer shell topology (Monorepo vs. Multi-repo), which is explicitly established by Phase 2 / AEOS-P02-S01-D02.
- Business module capabilities or new domain models.
- Database schemas, API endpoints, or event payloads.
- Deployment infrastructure, CI/CD vendors, or container orchestration.

**Structural Governance Rule:**
Any directory, package, filename, or repository tree shown in D10 is authoritative only where explicitly identified as a selected structural decision or as an implementation of an upstream authoritative decision. Illustrative examples MUST NOT be interpreted as additional architecture.

---

# 4. Repository Outer Shell (Phase 2 Authority)

The repository outer shell is established by Phase 2 / AEOS-P02-S01-D02 and remains completely authoritative and immutable within D10. 

The Phase 2 root repository shell is immutable within D10. D10 only elaborates the contents of these established directories but does not rename, remove, replace, or introduce alternative root-level topology. The structural representation inside the repository respects these predefined physical boundaries:

```text
anverra-global/
├── backend/
├── frontend/
├── mobile/
├── docs/
├── architecture/
├── infrastructure/
├── .ai/
├── scripts/
├── tools/
└── .github/
```

---

# 5. Backend Source Structure

D01 (Backend Implementation Architecture) establishes Spring Boot and Hexagonal Architecture. D02 establishes the Modular Monolith strategy. 

Phase 2 establishes the application-level repository shell. D01 establishes the Java source roots and Java package root `com.anverraglobal`. D10 combines these upstream decisions into the final physical implementation tree without redefining either decision.

The authoritative backend source tree must make dependency direction (inward towards the domain) structurally visible:

```text
backend/
├── src/main/java/com/anverraglobal/
│   ├── identity/          # Business Module
│   ├── customer/          # Business Module
│   ├── product/           # Business Module
│   ├── policy/            # Business Module
│   ├── commission/        # Business Module
│   ├── notification/      # Business Module
│   ├── reporting/         # Business Module
│   └── platform/          # Cross-cutting technical infrastructure (D01)
```
*Note: Only the business modules explicitly established by Phase 2 are represented. Placeholder directories for unresolved/deferred business capabilities are strictly prohibited.*

---

# 6. Business Module Structural Template

Each business module established by D02 must follow a strict, repeatable internal package template to physically enforce Hexagonal Architecture.

```text
<business-module>/
├── domain/                # Pure business logic, value objects, entities. NO framework dependencies.
├── application/           # Use cases, application services.
├── port/                  
│   ├── inbound/           # Primary ports (interfaces) for driving actors.
│   └── outbound/          # Secondary ports (interfaces) for driven actors.
├── adapter/               # Infrastructure, frameworks, database, HTTP.
│   ├── inbound/           # REST Controllers, async event listeners.
│   └── outbound/          # Persistence adapters (e.g., persistence), external API clients, event publishers.
├── contracts/             # Publicly governed API representations and cross-module sync interfaces.
└── events/                # Publicly governed asynchronous event models.
```
Note: Persistence implementations must remain under the D03-authoritative `<module>.adapter.outbound.persistence` boundary.

---

# 7. Contracts and Events Placement

D04 (API) and D05 (Async) establish the communication contracts. 
To ensure module encapsulation (D02), all inter-module communication must rely solely on the explicitly exported public surfaces.

- **`contracts/`**: Contains only the governed public synchronous module contract artifacts established by D02 and the applicable downstream contract architecture. Internal domain, application, port, adapter, persistence, and infrastructure types MUST NOT be exposed through this package.
- **`events/`**: Contains only governed asynchronous event contract artifacts established by D05.

Internal module components (`domain/`, `application/`, `adapter/`) are strictly protected and cannot be imported by other modules.

---

# 8. Shared Technical Structure

D10 enforces a strict boundary for shared code to prevent it from becoming a bypass for business architecture.

```text
backend/src/main/java/com/anverraglobal/platform/
```
(cross-cutting technical infrastructure, with its contents governed by D01 and the relevant D04/D06 responsibilities).

**Architectural Constraints:**
- No shared business logic or domain models.
- No shared persistence entities.
- No cross-module business workflows.

---

# 9. Persistence Artifact Placement

D03 establishes the persistence isolation strategy. Database migration files and persistence adapters are physically segregated to prevent schema leakage.

```text
backend/
├── src/main/resources/
│   └── db/migration/
│       ├── identity/      # Schema/migrations isolated by module
│       ├── customer/      
│       └── ...
```
D10 adopts the exact Flyway migration directory/resource structure established by D03.

---

# 10. API and Security Artifact Placement

- **API:** REST adapters (`@RestController`) are placed strictly in `<module>/adapter/inbound/`. OpenAPI contract YAML files are placed physically consistent with the selected D04 OpenAPI architecture.
- **Security:** Global security configurations (D06) are placed in the `platform/` module according to D01/D06. Module-specific authorization rules remain within the respective module's `adapter/inbound/` layer.

---

# 11. Configuration Resource Placement

D09 establishes configuration architecture. 

- Non-secret environment templates are placed in `backend/src/main/resources/`.
- Local developer configuration is physically separated from version-controlled configuration and follows D09's secret/non-secret boundary, with the concrete mechanism left to implementation tooling.

---

# 12. Test Structure

D08 is authoritative for testing. Test source trees must precisely mirror the application structure.

```text
backend/
├── src/test/java/com/anverraglobal/
│   ├── architecture/      # ArchUnit/Modulith architectural verification tests
│   └── <business-module>/
│       ├── domain/        # Fast, pure unit tests
│       ├── application/   # Mocked use-case tests
│       └── adapter/       # Integration tests (Testcontainers, MockMvc)
```

---

# 13. Client Project Structure (Web & Mobile)

D07 establishes the SPA and Mobile architectures. The structures physically isolate the Web and Mobile boundaries. Web/Mobile generated contract/client sharing follows the authoritative D07 decision. D10 only specifies the physical repository placement of those approved generated artifacts.

**Web Project Structure (`frontend/`):**
```text
frontend/
├── src/
│   ├── api/               # Generated OpenAPI client artifacts (Strict isolation)
│   ├── components/        # Reusable UI components
│   ├── features/          # Feature-sliced application logic
│   └── pages/             # Route-level application entry points
```

**Mobile Project Structure (`mobile/`):**
```text
mobile/
├── src/
│   ├── api/               # Generated OpenAPI client artifacts (Strict isolation)
│   ├── components/        # Reusable Mobile UI components
│   └── screens/           # Application screens and navigation
```

---

# 14. Source vs. Generated Code Governance

A strict physical boundary must distinguish human-maintained source code from generated code (e.g., OpenAPI clients, generated mappers).

- Generated artifacts (e.g., TypeScript API clients) must be placed in explicitly designated `api/` or `generated/` directories.
- These directories must not be manually edited. Generated artifacts MUST be reproducible from their authoritative source and MUST NOT be treated as manually maintained source code. The mechanism for regeneration is governed by the applicable build/tooling architecture.

---

# 15. Documentation & AI Tooling Structure

Documentation and AI agent governance must remain centralized, discoverable, and clearly authoritative.

```text
/ (Root)
├── docs/                  # System documentation
│   ├── 01-constitution/   # Phase 1
│   ├── 02-module-arch/    # Phase 2
│   ├── 03-tech-arch/      # Phase 3
│   └── 04-system-design/  # Phase 4 (D00-D11)
├── .ai/                   # AI Development Structure
│   ├── rules/             # Global agent constraints
│   └── skills/            # Targeted execution instructions
```

---

# 16. Technology Decision Register

| Decision ID | Decision | Selected Option | Rationale & Architectural Consequences |
|---|---|---|---|
| **O46** | Backend Source Structure | **Package-by-Module with Hexagonal inner structure.** | Physically enforces Modulith boundaries (D02) and Hexagonal purity (D01). |
| **O47** | Module Export Strategy | **Explicit `contracts/` and `events/` packages.** | Prevents implicit leakage of internal domain and adapter classes between modules. |
| **O48** | Shared Technical Boundary | **Use established `platform/` package.** | Operationalizes D01's cross-cutting technical boundary. |
| **O49** | Persistence Artifact Placement | **Module-isolated migration directories.** | Translates D03 schema-per-module isolation into physical file constraints (Operationalizes D03). |
| **O50** | Test Source Placement | **Tiered package matching (Architecture, Domain, Adapter).** | Physically represents the D08 test pyramid (Operationalizes D08). |
| **O51** | Generated Artifact Governance | **Strict isolation in `api/` or `generated/` directories.** | Ensures generated code traces directly to authoritative OpenAPI contracts (Operationalizes D04/D07). |
| **O52** | Web/Mobile Physical Structure | **Physical Web and Mobile project structures aligned with D07's independently bounded client architecture, with feature/screen organization applied within each client according to its established client architecture.** | Makes D07's boundaries physically visible without introducing a new client architecture (Operationalizes D07). |
| **O53** | Local Configuration Resource Placement | **Local developer configuration is physically separated from version-controlled configuration and follows D09's secret/non-secret boundary.** | Prevents accidental credential commits while leaving the concrete local configuration mechanism to implementation tooling (Operationalizes D09). |

---

# 17. Traceability

- **Phase 1 (Constitution):** Domain purity enforced by the `domain/` directory separation from `adapter/`.
- **Phase 2 (AEOS-P02-S01-D02):** The root repository outer shell is preserved without modification.
- **AEOS-P04-D01/D02:** Hexagonal and Modulith patterns translate directly into package boundaries.
- **AEOS-P04-D03:** DB migration files physically mapped to logical module boundaries.
- **AEOS-P04-D04/D05/D06:** Adapters, events, and security filters placed in specific infrastructure packages.
- **AEOS-P04-D07:** Web and Mobile directories structured for generated client artifact consumption.
- **AEOS-P04-D08:** Test placement mirrors the test pyramid.
- **AEOS-P04-D09:** Safe configuration template placement vs local ignored secrets.

---

# 18. Definition of Done

This document is complete when:
- [x] Repository topology is deferred purely to Phase 2 authority.
- [x] Backend package structure maps 1:1 with Hexagonal and Modulith constraints.
- [x] Client structure enforces generated vs manual code separation.
- [x] No unresolved business module placeholders are created.
- [x] O46-O53 D10 structural decisions are explicitly defined and resolved within this Proposed document.

---

# 19. Final Decision Status

**Final Decision Status:** Proposed

*(Awaiting formal architectural review)*
