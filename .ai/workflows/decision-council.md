---
description: "High-risk design review layer providing multi-agent peer review between technical-design and implementation-planning."
---

# Decision Council Workflow

## 1. Purpose

The AI Decision Council provides an independent, multi-perspective review of proposed technical designs. It acts as a high-risk design review layer invoked after technical design and before implementation planning.

This workflow is READ-ONLY.

It MUST NOT:
- modify application source code
- modify frontend code
- modify backend code
- modify database migrations
- modify existing `.ai/rules/`
- modify existing `.ai/skills/`
- modify existing `.ai/workflows/`
- modify existing `.agents/skills/`

## 2. Authority Rules

The Decision Council operates strictly within the existing governance boundaries:
- It does not override `.ai/rules/`
- It does not override the Engineering Constitution
- It does not authorize implementation
- It does not replace human approval
- It does not replace `architecture-analysis`
- It does not replace `technical-design`
- It does not review code
- It does not make repository changes

The existing Engineering Constitution remains the highest authority.

## 3. Council Invocation Decision

Before proceeding, every eligible change must evaluate and record whether the Council was invoked or intentionally skipped.

**Classification:**
- SPIKE
- BOUNDED
- ARCHITECTURAL

**Trigger Evaluation:**
- New module introduced: YES/NO
- Cross-module transaction: YES/NO
- Security boundary change: YES/NO
- `OrganizationScope` impact: YES/NO
- Infrastructure change: YES/NO
- Compliance impact: YES/NO
- Data integrity risk: YES/NO

**Decision:**
One of:
- COUNCIL REQUIRED
- COUNCIL NOT REQUIRED
- COUNCIL DEFERRED

**Reason:**
Explain why the decision was made.

**Purpose:**
Ensure future reviewers can understand why the Council was or was not invoked.

## 4. Trigger Matrix

Determine whether the Council should be invoked based on the following criteria:

### Mandatory Council Invocation
The Council MUST be invoked for:
- New top-level business module
- Cross-module transaction introduction
- `OrganizationScope` changes
- Core authorization model changes
- New infrastructure/broker/database technology
- Compliance-sensitive architecture changes
- Data integrity risks

### Optional Council Invocation
The Council MAY be invoked for:
- Complex API contracts
- Major event contract changes
- Cross-domain reporting architecture
- Significant performance architecture changes

### Never Invoke
The Council MUST NOT be invoked for:
- Bounded changes
- Spikes
- Documentation-only changes
- Routine dependency upgrades
- Small UI fixes
- Simple bug fixes

## 5. Input Requirements

Before invoking the Council, verify the availability of the following inputs:

**Required:**
- Requirement analysis output
- Architecture analysis output
- Technical design document
- Repository discovery baseline

**When Applicable:**
- Architecture decision records
- Existing event contracts
- Persistence design
- Security design
- Graphify signals

If required inputs are missing, the workflow state becomes:
**COUNCIL DEFERRED**
Reason: Insufficient evidence for independent review.

## 6. Execution Flow

Follow this sequence to execute the Council review:

**Step 1:** Collect Inputs
Gather all approved technical design artifacts and required inputs.

**Step 2:** Activate Council Roles
Engage the following perspectives:
- Enterprise Architect
- Domain Architect
- Security Architect
- Database Architect
- Compliance Reviewer

**Step 3:** Execute Controlled Review
Execute the debate protocol (Maximum 3 rounds):
- Round 1: Independent review
- Round 2: Challenge assumptions
- Round 3: Chair synthesis

**Step 4:** Generate Report
Produce the final Decision Council Report.

**Step 5:** Apply State Transition
Determine the final workflow state based on the report.

## 7. Evidence Requirements

Every Council finding must include:
- **Finding:** What was identified.
- **Evidence:** Exact supporting source.
- **Risk:** CRITICAL / HIGH / MEDIUM / LOW
- **Confidence:** HIGH / MEDIUM / LOW

Do not allow:
- unsupported opinions
- invented constraints
- assumed compliance requirements

## 8. Output Artifact

Generate the Decision Council Report and save it to:
`docs/04-system-design/council-reports/`

Naming convention:
`CNCL-NNN-<decision-topic>.md`

**Explanation:**
Council reports are review artifacts. They may support Architecture Decision Records (ADRs) but do not automatically become ADRs.

The report must contain:

```markdown
# Decision Council Report

## Decision
[PASS / BLOCKED / DEFERRED]

## Technical Design Reviewed
[Reference to the design document]

## Council Members
[List of participating roles]

## Findings
[Classified list of findings with Evidence, Risk, and Confidence]

## Alternatives Considered
[Summary of alternative architectural approaches debated]

## Risks
[Identified risks categorized by severity]

## Dissenting Opinions
[Any unresolved disagreements between Council members]

## Confidence
[HIGH / MEDIUM / LOW]

## Human Decisions Required
[Targeted questions for the human owner]
```

## 9. Council Report Lifecycle

The Council report progresses through the following states:
```
DRAFT
 |
 v
IN REVIEW
 |
 +--> COUNCIL PASS
 |
 +--> COUNCIL BLOCKED
 |
 +--> COUNCIL DEFERRED
```

A report that is incomplete or under review MUST NOT be treated as a final architectural decision.

## 10. Workflow States

The workflow MUST end in one of the following states:

### COUNCIL NOT REQUIRED
The change does not match trigger conditions. Continue to implementation planning.

### COUNCIL IN REVIEW
Council members are evaluating the design.

### COUNCIL PASS
The technical design has passed multi-perspective review. Implementation planning may continue.

### COUNCIL BLOCKED
Human decision required. Implementation planning must stop.

### COUNCIL DEFERRED
Required information is missing. Return to the previous workflow stage.

## 11. Implementation Planning Gate

Implementation planning MUST NOT begin unless the Council result is:
- **COUNCIL PASS**
OR
- **COUNCIL NOT REQUIRED**

A `COUNCIL BLOCKED` or `COUNCIL DEFERRED` state strictly prevents implementation planning.

## 12. Council Approval Limitation

A Council `PASS` does NOT authorize implementation.

The following remain mandatory:
- Technical design completion
- Implementation planning
- Human approval gate
- `IMPLEMENTATION_AUTHORIZED` transition

Council confidence level, unanimous agreement, or low-risk classification MUST NOT bypass human approval.

## 13. Evidence Validation

The Council report becomes a permanent engineering artifact. Future testing and validation must verify:
- Council invocation decision exists when applicable
- Correct trigger evaluation was performed
- Required Council roles participated
- Findings contain evidence references
- Risks were classified
- Final Council state is valid
- Human approval exists before implementation
- Implementation matches the approved design reviewed by Council
