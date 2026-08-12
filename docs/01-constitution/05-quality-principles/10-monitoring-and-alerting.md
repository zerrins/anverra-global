---
document: Monitoring and Alerting
id: AEC-QLT-010
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-QLT-009
---

# Purpose

Define the standards for monitoring application health, business operations, infrastructure, and production incidents.

Monitoring provides continuous visibility into system behavior, while alerting ensures timely response to abnormal conditions.

---

# Intent

Monitoring shall answer:

- Is the system healthy?
- Is the business functioning?
- Are users affected?
- Should engineering respond?

Monitoring exists to detect problems before customers report them.

---

# Problem Statement

Reactive monitoring results in:

- Late incident detection
- Increased downtime
- Customer impact
- Difficult troubleshooting
- Missing business visibility

Without measurable monitoring, engineering operates blindly.

---

# Quality Decision

Every production component shall expose operational telemetry and meaningful alerts.

Monitoring is mandatory for all production services.

---

# Rationale

Fast detection reduces Mean Time To Detect (MTTD).

Meaningful monitoring improves:

- Reliability
- Availability
- Incident response
- Capacity planning
- Engineering confidence

---

# Why This Matters to AI

AI often generates metrics but does not identify which ones matter.

AI shall prioritize business and operational metrics over infrastructure metrics alone.

---

# Monitoring Principles

Every service shall expose:

- Health
- Metrics
- Logs
- Traces
- Business KPIs

Monitoring shall be proactive rather than reactive.

---

# Standard Tooling

Metrics

- Micrometer
- Prometheus

Visualization

- Grafana

Tracing

- OpenTelemetry

Logging

- Loki
- Logback

Alerting

- Alertmanager

---

# Monitoring Categories

## Infrastructure

- CPU
- Memory
- Disk
- Network

---

## Application

- Request rate
- Latency
- Error rate
- Thread pools
- Connection pools

---

## Business

- Policies issued
- Claims processed
- Premium collected
- Commission generated

---

## Dependencies

- PostgreSQL
- Kafka
- Redis
- External APIs

---

# Alerting Principles

Alerts shall be:

- Actionable
- Relevant
- Prioritized
- Deduplicated

Avoid alert fatigue.

---

# Severity Levels

P1 – Critical outage

P2 – Major degradation

P3 – Partial impact

P4 – Informational

---

# Mandatory Rules

- Every service has health checks.
- Every service exports metrics.
- Critical failures trigger alerts.
- Dashboards exist for every production service.
- Correlation IDs are searchable.

---

# Recommended Practices

- SLI/SLO based alerts.
- Business dashboards.
- Error budgets.
- Capacity monitoring.
- Trend analysis.

---

# Prohibited Practices

- Alerting on every exception.
- Missing health endpoints.
- Console-only monitoring.
- Duplicate alerts.
- Ignoring business metrics.

---

# AI Guidance

AI shall generate:

- Metrics
- Dashboards
- Health indicators
- Alert recommendations

AI shall never generate production services without observability.

---

# Implementation Guidance

1. Define SLIs.
2. Configure metrics.
3. Configure tracing.
4. Build dashboards.
5. Configure alerts.
6. Validate alerts.
7. Review periodically.

---

# Quality Metrics

| Metric | Target |
|---------|---------|
| Service Health Coverage | 100% |
| Dashboard Coverage | 100% |
| Critical Alerts Tested | 100% |
| Mean Time To Detect | <5 minutes |

---

# Review Checklist

- Metrics exposed?
- Dashboards available?
- Alerts actionable?
- Health endpoints implemented?
- Business KPIs monitored?

---

# Anti-patterns

Dashboard Without Alerts

Alert Storm

No Business Metrics

No Health Checks

Unknown Production State

---

# Constitutional Compliance Matrix

| Requirement | Status |
|------------|--------|
| Monitoring | Mandatory |
| Alerting | Mandatory |
| Dashboards | Mandatory |
| Health Checks | Mandatory |

---

# Engineering Decision

Software that cannot be monitored shall not be deployed to production.

---

# Related Documents

- Observability and Logging
- Reliability and Resilience
- Release Quality Gates