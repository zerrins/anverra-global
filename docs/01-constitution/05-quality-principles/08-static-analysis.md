---
document: Static Analysis
id: AEC-QLT-008
version: 1.0.0
status: Draft
---

# Purpose

Define automated static quality verification.

---

# Quality Decision

Static analysis is mandatory in every build pipeline.

---

# Standard Tooling

Backend

- SonarQube
- SpotBugs
- PMD
- Checkstyle

Frontend

- ESLint
- TypeScript Compiler
- Prettier

Infrastructure

- Hadolint
- Trivy
- Checkov

---

# Mandatory Rules

- Zero Critical issues.
- Zero Blocker issues.
- No security hotspots unresolved.
- Build fails on constitutional violations.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Critical Issues | 0 |
| Blocker Issues | 0 |
| High Security Findings | 0 |
| Code Smells | Continuous Reduction |

---

# AI Guidance

AI shall satisfy static analysis before considering implementation complete.

---

# Engineering Decision

Static analysis complements human review—it does not replace it.

---

# Related Documents

- Clean Code
- Development Review Checklist