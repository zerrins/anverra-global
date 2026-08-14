# REQ-DEC-005: Commission

- **Capability:** Commission
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human / Product / Business decision

### 1. Commission Business Purpose
Commission represents the commission information and distribution associated with a Policy.
The Commission capability is responsible for commission-specific calculation and validation rules.

The application requires Commission primarily for:
- calculating commission from Policy Premium
- recording commission information against a Policy
- determining the commission available for involved agents
- distributing commission between the involved agents
- supporting commission reference
- supporting commission statistics
- supporting commission reporting

Do NOT turn Commission into a general accounting, settlement, payment, tax, or payout platform.

### 2. Commission Basis
Commission is based on the Policy Premium.
The authoritative rule is:
Total Commission <= 50% of Policy Premium
This maximum applies regardless of commission input mode.

Examples:
Premium = ₹50,000
Percentage mode:
20% => ₹10,000 VALID
50% => ₹25,000 VALID
50.01% => INVALID

Fixed amount mode:
₹20,000 => VALID
₹25,000 => VALID
₹25,001 => INVALID

Do not invent currency rules, precision, rounding, datatype, or technical representation.

### 3. Commission Input
When adding a Policy, the agent chooses exactly one commission input mode:
- Percentage
- Fixed Amount

The agent enters the corresponding Commission Value.
Percentage mode: Commission Amount = Premium × entered percentage
Fixed Amount mode: Commission Amount = entered fixed amount

The resulting Total Commission must never exceed 50% of Premium.
Do not invent additional commission formulas.

### 4. Commission Ownership
The Commission capability owns:
- commission calculation rules
- commission validation
- commission-specific business rules

Policy stores/contains the approved commission business information for reference, statistics, reporting, and Policy-level use.
Do NOT interpret this as requiring a separate technical Commission service/module implementation now.

### 5. Agents per Policy
A Policy may involve a maximum of two Agents.
There are only two relevant positions:
- Agent A — Primary Agent
- Agent B — Optional Secondary Agent

Agent B -> Agent C is NOT relevant to AnverraGlobal and is explicitly out of scope.
Do not create a multi-level agent hierarchy engine.

### 6. Single-Agent Commission
If only Agent A is involved:
- Agent A receives 100% of the calculated Commission.
- No separate Agent A allocation input is required.

### 7. Two-Agent Commission
If Agent B is involved:
- Agent A remains the Primary Agent.
- Agent A defines Agent B's commission share.
- Agent B's share may be entered as: Percentage, OR Fixed Amount
- Agent A receives the remainder.
- Agent B may receive up to 100% of the calculated Commission.
- Agent A may consequently receive 0–100% as the remainder.
- 100% of the calculated Commission must be allocated.
- No amount may remain unallocated.
- Agent B's allocation may not exceed the Total Commission.

Do not invent minimum Agent A shares.

### 8. Agent B Allocation Rule
Agent A explicitly defines Agent B's share.
Agent A does NOT separately enter the remainder.
The system derives: Agent A Allocation = Total Commission - Agent B Allocation
The combined allocation must equal exactly 100% of Total Commission.
Do not invent alternative allocation formulas.

### 9. Commission During Draft
Commission is determined during Policy creation.
While the Policy is DRAFT, the agent may establish/update:
- Commission Type
- Commission Value
- Agent A
- optional Agent B
- Agent B share

Customer-created Policies with zero Agents may be activated without Commission. If one or more Agents are involved, Commission must not remain UNSET when activating the Policy. Explicitly configured zero Commission is valid and allows activation.

### 10. Premium Change While Draft
If Premium changes while a Policy is DRAFT and the Policy has an applicable commission configuration:
- Existing commission configuration is RESET.
- Commission Type becomes unset.
- Commission Value becomes unset.
- Agent allocation becomes unset.
- The agent must enter a new valid commission configuration.
- The new configuration must satisfy: Total Commission <= 50% of current Premium.

Do NOT automatically recalculate or preserve the previous commission.

### 11. Agent Participation Change While Draft
If Agent B is added or removed while the Policy is DRAFT:
- Commission distribution configuration is RESET.
- Agent B share becomes unset.
- Agent allocation becomes unset.
- Agent A must define the new Agent B share if Agent B is present.
- Agent A receives the derived remainder.
- The distribution must again allocate 100% of Total Commission.

Do not automatically preserve or redistribute the old allocation.

### 12. Commission After Policy Becomes Active
Commission remains editable after Policy becomes ACTIVE.
An ACTIVE Policy may have its:
- Commission Type
- Commission Value
- Agent A
- Agent B
- Agent B allocation
updated.

Commission changes do NOT:
- return the Policy to DRAFT
- require an approval workflow
- create a new Policy lifecycle state
All existing commission invariants continue to apply.

### 13. Premium Change After Policy Becomes Active
If Premium changes while the Policy is ACTIVE and the Policy has an applicable commission configuration:
- Existing commission configuration is RESET.
- Commission Type becomes unset.
- Commission Value becomes unset.
- Agent allocation becomes unset.
- The agent must enter a new valid commission configuration.
- The Policy remains ACTIVE.
- The new commission must satisfy: Total Commission <= 50% of current Premium.

Do NOT automatically recalculate or preserve the previous commission.

### 14. Commission Reset Semantics
RESET means:
- Commission Type = unset
- Commission Value = unset
- Agent allocation = unset

Reset does NOT mean:
- 0%
- ₹0
- INVALID commission state

UNSET and ZERO are semantically different. UNSET means Commission has not been configured. ZERO means Commission was explicitly configured as zero.
UNSET is excluded from Commission statistics. Explicit ZERO is a valid Commission and is included in Commission statistics as amount 0.

The system must distinguish "unset" from a legitimate zero value.
Do NOT invent a separate Commission lifecycle/status state.

### 15. Policy-Level Commission Information
The following Commission information is authoritative Policy-level business information:
- Commission Type
- Commission Value
- Total Commission Amount
- Agent A
- Agent A Allocation
- Agent B, if applicable
- Agent B Allocation
- Commission Calculation/Result Reference

These are available for Policy reference, filtering where applicable, statistics, and reporting.
The exact technical representation remains unresolved.

### 16. Commission Result Reference
Policy may retain a Commission Calculation/Result Reference.
This is a reference to the Commission result/business calculation.
Do NOT invent event names, IDs, UUID formats, API structures, database structures, or asynchronous workflows.

### 17. Explicitly Out of Scope
REQ-DEC-005 does NOT authorize:
- Agent B -> Agent C
- multi-level commission hierarchy
- complex hierarchy engines
- insurer commission agreements
- product-specific commission configuration
- detailed insurer commission contracts
- commission payout scheduling
- payment settlement
- accounting integration
- TDS/tax logic
- clawbacks
- cancellation adjustments
- reconciliation workflows
- commission approval workflows
- commission versioning/history
- commission audit-history implementation
- general ledger/accounting
- banking/payment workflows

These may only be introduced through future authoritative requirements.

### 18. Intentionally Unresolved
Do NOT invent:
- exact technical fields, database schemas, tables, columns, datatypes, precision/scale, currency implementation, rounding rules
- API/OpenAPI, HTTP methods, DTOs, API schemas
- events, event payloads
- authorization, roles, permissions
- UI implementation, exact Policy commission form layout, exact agent-selection UX
- persistence implementation
- technical Commission module structure
- event-driven integration
