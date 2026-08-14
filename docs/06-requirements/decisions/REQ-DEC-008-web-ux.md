# REQ-DEC-008: Web UX Requirements

- **Capability:** Policy, Commission, Organizational Integration, Policy Statistics, Policy Document
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human / Product / Business decision

### 1. Purpose and Scope
This document consolidates the Web UX requirements resulting from the approved Phase 5 decisions. It complements the API requirements governed in REQ-DEC-007.

### 2. Existing Policy Resolution UX
- If a Customer attempts to add a Policy that already exists, do NOT present a uniqueness error as the primary UX.
- Show the existing Policy to the Customer when authorized.
- If an Agent-created Policy already exists and the Customer attempts to add it, show the existing Policy rather than exposing a raw uniqueness violation.

### 3. Customer Editability UX
- Customer-created Policy with no Commission → Customer may edit according to approved rules.
- Once Agent/Commission involvement occurs → Customer becomes View Only.
- UX conditionally disables form editing based on this role/context rule.

### 4. Agent / Commission UX
- Agent A can opt into Commission sharing via UX.
- When sharing is selected, UX provides an eligible dropdown for Agent B selection.
- Customers can see involved Agent/Broker names on the detail view.

### 5. Commission Dashboard UX
- Separate top-level Commission capability in UX.
- Role-aware visibility on landing.
- Scope-aware filters. User-selected date range. Drill-down to authorized Policies.
- **CRITICAL:** Commission dashboards MUST NEVER display percentages. Only absolute Commission amounts are shown.

### 6. Policy Statistics UX
- Separate Policy Statistics capability under Policies.
- Scope-aware filters. User-selected date range. Drill-down to authorized Policies.
- UX must label agent-wise counts as "Policies involving Agent", explicitly indicating they can exceed Total Policies.

### 7. Policy Document UX
- Optional attachment during creation or later.
- Authorized users see a direct download option.
- Replacing or removing the document requires explicit confirmation dialogs.
- Saving a policy without a document produces a non-blocking warning.

### 8. Policy Lifecycle UX
- **Creation/Edit:** "Activate when saved" is enabled by default. If unchecked, saved as DRAFT. If activation requirements are not satisfied, save as DRAFT with clear readiness explanation.
- **ACTIVE:** Editing does not automatically change lifecycle. Deactivate is an explicit action requiring confirmation.
- **INACTIVE:** Explicit Reactivate action available.
- **Premium Changes:** UX must warn the user before committing a Premium change, explicitly noting that it will reset the Commission to UNSET.

### 9. Policy Form & List UX
- **Creation/Edit Form:** Single-page form. Logical sections (Policy Information, Financial Information, Commission, Policy Document, Activation). Required fields clearly indicated. Inline validation plus full validation on Save.
- **Form State:** Unsaved-change warning. Disable Save while processing. Preserve form state after failed save. Do not expose raw technical errors.
- **List View:** Search, Scope-aware filters, Sorting, Server-side pagination. Contextual empty states, Loading/skeleton state. Preserve list context when returning from a Policy detail. Fresh visit resets list state.
- **Detail View:** Single-page logical sections. Role-aware fields/actions.
- **Creation Context:** Original creation context visible to authorized operational users, but not Customers.
