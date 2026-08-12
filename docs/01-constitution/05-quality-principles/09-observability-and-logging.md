---
document: Observability and Logging
id: AEC-QLT-009
version: 1.0.0
status: Draft
---

# Purpose

Define standards for observability across all software components.

---

# Intent

Every production issue should be diagnosable without modifying running software.

---

# Quality Decision

Observability is a mandatory engineering capability.

Every service shall produce logs, metrics, and traces.

---

# Standard Tooling

Metrics

- Micrometer
- Prometheus

Tracing

- OpenTelemetry

Logging

- SLF4J
- Logback
- Structured JSON

Dashboards

- Grafana

---

# Logging Principles

Every log shall include:

- Timestamp
- Level
- Correlation ID
- Service Name
- Module
- Business Context

---

# Mandatory Rules

- Structured logging only.
- No sensitive data.
- Correlation IDs propagated.
- Health endpoints implemented.
- Business events logged appropriately.

---

# Recommended Practices

- Business-centric log messages.
- Metrics for every critical workflow.
- Distributed tracing.
- Meaningful dashboards.

---

# Prohibited Practices

- Logging passwords.
- Logging secrets.
- Duplicate logging.
- Console debugging in production.
- Missing correlation identifiers.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Structured Logs | 100% |
| Correlation ID Coverage | 100% |
| Trace Coverage | ≥95% |
| Sensitive Data Leakage | 0 |

---

# AI Guidance

AI shall generate production-ready logging automatically.

AI shall never log confidential information.

---

# Engineering Decision

Software that cannot be observed cannot be reliably operated.

---

# Related Documents

- Error Handling
- Monitoring and Alerting