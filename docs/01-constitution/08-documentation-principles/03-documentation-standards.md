---
document: Documentation Standards
id: AEC-DOC-003
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-08
last-reviewed:
depends-on:
  - AEC-DOC-001
  - AEC-DOC-002
  - AEC-REP-004
---

# Purpose

Define the minimum standards for creating, formatting, structuring, reviewing, and maintaining engineering documentation within AEOS-governed repositories.

Documentation standards provide consistency across teams, repositories, technologies, and documentation types.

---

# Intent

Documentation should be predictable.

An engineer should be able to open an unfamiliar document and understand:

- What it is.
- Why it exists.
- Who owns it.
- What it describes.
- Whether it is current.
- How it relates to other engineering knowledge.

---

# Problem Statement

Inconsistent documentation standards produce:

- Difficult navigation
- Poor readability
- Ambiguous terminology
- Inconsistent metadata
- Broken links
- Unclear ownership
- Difficult maintenance
- Poor AI context quality

Documentation requires common standards to remain useful at organizational scale.

---

# Constitutional Decision

All governed engineering documentation shall follow AEOS documentation standards unless a documented exception applies.

Documentation standards apply to:

- Markdown
- API specifications
- Diagrams
- ADRs
- Runbooks
- Engineering guides
- Architecture documents
- AI context documents
- Operational documentation

---

# Rationale

Standardization reduces cognitive overhead.

Engineers should spend their time understanding the information rather than learning how every team structures its documentation.

---

# Documentation Format

Markdown shall be the preferred format for repository-native human-readable documentation unless another format provides a clear technical advantage.

Other formats may include:

- YAML
- JSON
- OpenAPI
- AsyncAPI
- Mermaid
- PlantUML
- SVG
- PDF

Structured formats should be used where they provide machine-readable value.

---

# File Naming

Documentation filenames shall:

- Be descriptive.
- Use lowercase where repository standards require it.
- Use hyphens for word separation.
- Avoid spaces.
- Avoid ambiguous names.

Preferred:

```text
architecture-overview.md
deployment-guide.md
database-migration.md
```

Avoid:

```text
Doc1.md
final.md
notes.md
stuff.md
```

---

# Document Titles

Every document shall begin with a clear title.

Example:

```markdown
# Architecture Overview
```

The title shall communicate the primary subject of the document.

---

# Metadata

Governed documents should include metadata.

Example:

```yaml
---
document: Architecture Overview
id: AEC-ARC-001
version: 1.0.0
status: Active
owner: Architecture
created: 2026-08-08
last-reviewed: 2026-08-08
---
```

Metadata shall not contain sensitive information.

---

# Document Purpose

Every significant document shall explain why it exists.

A reader should understand the document's purpose without reading the entire document.

---

# Audience

Where useful, documents should identify intended audiences.

Examples:

```text
Audience:
- Backend Engineers
- Architects
- Operations
```

---

# Document Structure

Long-form engineering documents should use predictable sections.

Recommended structure:

```text
Purpose

Intent

Context

Principles

Standards

Examples

Exceptions

Review

References

Related Documents
```

The exact structure may vary by document type.

---

# Headings

Use hierarchical headings.

Preferred:

```markdown
# Main Topic

## Major Section

### Subsection
```

Avoid excessive heading depth.

---

# Paragraphs

Prefer short, focused paragraphs.

A paragraph should communicate one coherent idea.

Large blocks of text should be divided when doing so improves comprehension.

---

# Lists

Use lists for:

- Requirements
- Rules
- Options
- Steps
- Checklists

Avoid using lists where normal prose communicates the concept more clearly.

---

# Tables

Use tables for structured comparisons.

Examples:

- Responsibility matrices
- Configuration properties
- Compatibility matrices
- Decision comparisons

Do not use tables for large blocks of prose.

---

# Code Examples

Code examples shall:

- Be syntactically valid where practical.
- Use representative values.
- Avoid real credentials.
- Avoid production secrets.
- Clearly indicate language or format.

Example:

```text
DATABASE_URL=<environment-specific-value>
```

Never use actual secrets in examples.

---

# Commands

Commands shall be:

- Safe
- Complete
- Contextualized

Dangerous commands should include explicit warnings.

---

# Links

Links shall:

- Use meaningful anchor text.
- Point to valid targets.
- Prefer stable internal references.
- Avoid unnecessary external dependencies.

Broken links shall be corrected promptly.

---

# Cross-References

Related documents should be linked when the relationship is important.

Example:

```text
See Architecture Decision Records for the rationale behind this design.
```

Cross-references shall not create circular documentation unnecessarily.

---

# Terminology

Engineering terminology shall be consistent.

Organizations should maintain a glossary for important domain terminology.

Avoid using synonyms for the same concept when doing so could create ambiguity.

---

# Business Language

Documentation should use domain language consistently.

For example, if the business calls something a:

```text
Policy
```

documentation should not arbitrarily refer to it as:

```text
Contract
Agreement
Record
Document
```

unless these represent genuinely different concepts.

---

# Technical Language

Technical terms should be used accurately.

Acronyms should be expanded on first use when the audience may not know them.

Example:

```text
Software Bill of Materials (SBOM)
```

---

# Examples

Complex concepts should include examples where examples improve understanding.

Examples should represent realistic usage without exposing sensitive information.

---

# Diagrams

Diagrams shall:

- Have clear titles.
- Communicate a specific purpose.
- Use consistent notation.
- Include legends where necessary.
- Remain synchronized with architecture.

Diagrams shall not exist merely for visual decoration.

---

# Document Status

Documents should clearly communicate lifecycle status.

Recommended statuses:

```text
Draft
Active
Deprecated
Superseded
Archived
```

---

# Review Metadata

Important documents should record:

- Owner
- Last reviewed date
- Review frequency where required

---

# Documentation Versioning

Documents that represent contractual or version-sensitive information should be versioned.

Examples:

- APIs
- Architecture standards
- Operational procedures
- Engineering policies

Not every informational document requires an independent version number.

---

# Documentation Review

Documentation shall be reviewed when:

- Architecture changes.
- Public APIs change.
- Operational procedures change.
- Security requirements change.
- Major business behavior changes.
- Governance standards change.

---

# Documentation Validation

Repositories should automate:

- Markdown linting
- Link checking
- Spell checking
- Metadata validation
- Diagram validation
- API specification validation

Automation reduces documentation defects.

---

# Accessibility

Documentation should be accessible to its intended audience.

Consider:

- Clear headings
- Readable language
- Descriptive links
- Text alternatives for important diagrams
- Avoidance of unnecessary visual complexity

---

# Internationalization

Where repositories support multiple languages, terminology shall remain consistent across translations.

Translated documentation shall not silently change engineering meaning.

---

# Documentation Security

Documentation shall not contain:

- Passwords
- API keys
- Private keys
- Tokens
- Confidential credentials

Sensitive operational information shall follow security classification requirements.

---

# AI-Generated Documentation

AI-generated documentation shall follow the same standards as human-generated documentation.

AI shall not receive lower quality expectations.

AI-generated documentation shall be:

- Reviewed
- Verified
- Context-aware
- Traceable where significant
- Consistent with repository standards

---

# AI Guidance

AI shall:

- Follow document templates.
- Preserve terminology.
- Use repository conventions.
- Validate links.
- Avoid fabricated information.
- Identify assumptions.
- Update affected documentation when implementation changes.
- Prefer existing documentation over generating duplicates.

---

# Human Responsibilities

Humans shall remain responsible for:

- Accuracy
- Ownership
- Business terminology
- Architectural intent
- Approval of important documentation

AI may assist but does not own engineering truth.

---

# Mandatory Rules

Documentation shall:

- Use meaningful filenames.
- Have clear titles.
- Avoid secrets.
- Maintain valid references.
- Follow repository conventions.
- Remain accurate.
- Identify ownership where required.

---

# Recommended Practices

Prefer simple language.

Use examples.

Use diagrams when they add value.

Keep documents focused.

Automate validation.

Review documentation alongside implementation.

---

# Prohibited Practices

Do not:

- Publish knowingly inaccurate information.
- Use meaningless filenames.
- Store secrets in documentation.
- Duplicate authoritative information unnecessarily.
- Leave broken links unresolved.
- Generate documentation without verification.

---

# Allowed Exceptions

Temporary drafts may use incomplete metadata or structure while actively being developed.

Before being classified as Active, the document shall satisfy applicable standards.

---

# Success Metrics

| Metric | Target |
|---------|--------|
| Documentation Standards Compliance | 100% |
| Broken Links | 0 |
| Documentation Secret Exposure | 0 |
| Critical Documentation Accuracy | 100% |
| Metadata Compliance | 100% |
| AI-Generated Documentation Review | 100% |

---

# Review Checklist

Reviewers shall verify:

- Filename appropriate
- Title clear
- Purpose defined
- Audience appropriate
- Metadata present where required
- Terminology consistent
- Links valid
- Examples safe
- No secrets present
- Documentation accurate

---

# Examples

## Good

```text
deployment-guide.md

# Deployment Guide

Purpose

...

Prerequisites

...

Deployment

...

Rollback

...

Troubleshooting
```

The document is discoverable, structured, and actionable.

---

## Poor

```text
final-final-new.md

Some deployment notes.
Ask John if something doesn't work.
```

This represents tribal knowledge rather than maintainable engineering documentation.

---

# Anti-patterns

Documentation Dumping

Meaningless Filenames

Broken Links

Secret Leakage

Inconsistent Terminology

Unstructured Documents

Unverified AI Documentation

Copy-Paste Documentation

Documentation Without Ownership

---

# Constitutional Compliance Matrix

| Constitution | Status |
|--------------|--------|
| Engineering Principles | Mandatory |
| Architecture Principles | Mandatory |
| Development Principles | Mandatory |
| Quality Principles | Mandatory |
| AI Principles | Mandatory |
| Repository Principles | Mandatory |
| Documentation Principles | Mandatory |

---

# Engineering Decision

Documentation standards establish the minimum quality expectations for engineering knowledge.

Every governed document shall be understandable, discoverable, accurate, maintainable, secure, and consistent with the AEOS documentation architecture.

AI-generated documentation shall be held to the same standard as human-generated documentation.

---

# References

- Docs-as-Code
- Markdown
- Diátaxis
- arc42
- C4 Model
- Engineering Constitution

---

# Related Documents

- Documentation Philosophy
- Documentation Architecture
- Diagrams and Models
- API Documentation
- Operational Documentation
- Decision Documentation
- Documentation Lifecycle
- Documentation Review
- AI Documentation