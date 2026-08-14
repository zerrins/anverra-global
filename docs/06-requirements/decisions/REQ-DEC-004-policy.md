# REQ-DEC-004: Policy

- **Capability:** Policy
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human / Product / Business decision

### 1. Policy Business Definition
A Policy represents an insurance policy/business relationship record managed by AnverraGlobal.

The Policy capability is intentionally designed as an MVP business record supporting:
- policy identification
- customer association
- product association
- insurer identification
- policy lifecycle tracking
- policy-period tracking
- basic financial/reference information
- commission reference and historical/reference information
- filtering
- statistics
- reporting

AnverraGlobal is NOT being defined as a full insurance administration/underwriting/claims platform.

### 2. Policy Relationships
**CUSTOMER:**
- Every Policy belongs to exactly one Customer.
- One Customer may have multiple Policies.

**PRODUCT:**
- Every Policy belongs to exactly one Product.
- One Product may be associated with multiple Policies.
- Product remains insurer-independent.

**INSURER:**
- Every Policy identifies exactly one Insurer/Provider.
- One Insurer may be associated with multiple Policies.
- The Insurer relationship exists primarily for:
  - commission calculation
  - commission reporting/statistics
  - filtering/search
- No detailed Insurer profile is required.

**PRODUCT -> INSURER:**
- No Product -> Insurer relationship exists.
- Product remains a generic/reference insurance offering independent of insurer.

### 3. Insurer Reference Data
Insurer is lightweight controlled reference/master data.

The system maintains a controlled initial list of insurers.

The Insurer concept is intentionally limited to the information necessary to:
- identify the insurer/provider
- select an insurer when creating/editing a Policy
- support commission calculation
- support reporting/statistics
- support filtering/search

DO NOT introduce:
- detailed insurer profiles
- branches
- contacts
- regulatory information
- insurer onboarding
- insurer workflows
- insurer-specific product configuration

Exact Insurer technical fields, identifiers, database representation, API representation, and maintenance mechanism remain unresolved.

### 4. Policy Identity
Every Policy has a unique Policy Number/Reference.

The Policy Number/Reference:
- is a required business identifier
- is unique across Policies
- supports searching
- supports filtering
- supports reporting
- supports human reference

The exact Policy Number format is intentionally unresolved.

Do NOT invent:
- a prefix
- a numbering algorithm
- a UUID format
- an insurer-provided numbering convention
- database datatype/representation

### 5. Policy Lifecycle
Authoritative lifecycle states:
- DRAFT
- ACTIVE
- INACTIVE

Authoritative transitions:
- DRAFT -> ACTIVE
- ACTIVE -> INACTIVE
- INACTIVE -> ACTIVE

Physical deletion of a Policy is prohibited.
Historical/reference integrity must be preserved by retaining inactive Policies.

### 6. Progressive Policy Completion
A Policy can be created progressively.
A Policy starts as DRAFT and may have additional authorized information added or updated while it remains DRAFT.

The complete set of future Policy information is intentionally NOT defined now.

The currently authoritative mandatory activation requirements are:
- Customer
- Product
- Insurer
- Policy Number
- Effective Date
- Expiry Date
- Premium
- Sum Assured

A Policy may transition from DRAFT to ACTIVE only when the currently authoritative mandatory activation requirements are present and valid.

Important:
- ACTIVE means the Policy satisfies the currently authoritative activation requirements.
- ACTIVE does NOT mean the Policy model is permanently complete.
- Future approved requirements may add additional activation requirements through governed requirements changes.
- Do not invent those future requirements.

### 7. Policy Dates
Every Policy has:
- Effective Date
- Expiry Date

Authoritative invariant:
Effective Date < Expiry Date

A DRAFT Policy may have dates prepared before activation.
A Policy cannot become ACTIVE unless the currently authoritative date requirements are valid.
Renewal dates and renewal workflows are NOT currently part of REQ-DEC-004.

### 8. Policy Financial Information
Every Policy captures:
- Premium
- Sum Assured

These are Policy-level financial/reference values.

The Policy module does NOT calculate:
- Premium
- Sum Assured
- pricing
- underwriting calculations
- coverage calculations

Exact:
- datatype
- currency representation
- precision/scale
- database representation
- API representation
remain unresolved implementation details.

### 9. Commission Information on Policy
Policy retains a CURRENT commission snapshot/reference. It remains editable while ACTIVE.

Authoritative Policy-level commission information:
- Commission Type
- Commission Value
- Total Commission Amount
- Agent A
- Agent A Allocation
- Agent B, optional
- Agent B Allocation
- Commission Calculation/Result Reference

These are snapshot/reference values associated with the Policy.

The Commission module owns:
- commission calculation rules
- commission formulas
- slabs
- agent hierarchy
- payout logic
- other Commission-specific business rules

Policy does NOT own Commission calculation logic.
Customer-created Policies (0 Agents) may be activated without Commission. If one or more Agents are involved, Commission must not remain UNSET when activating the Policy. Explicitly configured zero Commission is valid and allows activation.
If Premium changes while the Policy is DRAFT and the Policy has an applicable commission configuration, the commission configuration is reset and must be re-entered and validated before activation.
- Premium change while ACTIVE resets commission configuration (if applicable).
- Agent B participation change while DRAFT resets commission distribution.
- Commission changes while ACTIVE do not return Policy to DRAFT.

### 10. Current Policy Business Information
The currently authoritative Policy information categories are:
- Policy Number
- Customer
- Product
- Insurer
- Effective Date
- Expiry Date
- Premium
- Sum Assured
- Lifecycle Status
- Commission Type
- Commission Value
- Total Commission Amount
- Agent A
- Agent A Allocation
- Agent B, optional
- Agent B Allocation
- Commission Calculation/Result Reference
- Created/Updated timestamps

Created/Updated timestamps are authoritative business/audit information categories.
Their exact technical representation remains unresolved.

### 11. Future Policy Information
The Policy may acquire additional business information in the future through separately approved requirements.

The following are NOT currently authorized and MUST NOT be silently promoted into requirements:
- Policy type/subtype
- Coverage
- Riders
- Nominee
- Beneficiary
- Renewal details
- Endorsements
- Claims information
- Payment schedules
- Policy documents
- Underwriting information
- Any other detailed insurance-policy concepts

These may only be introduced through future authoritative requirements decisions.

### 12. Explicitly Out of Scope
REQ-DEC-004 does NOT authorize:
- underwriting workflows
- claims workflows
- renewal workflows
- endorsement workflows
- premium calculation
- pricing engine
- coverage calculation
- rider management
- detailed policy administration
- insurer detailed profiles
- insurer onboarding
- insurer-specific product configuration
- commission calculation logic
- commission slabs
- commission hierarchy
- commission payout workflows

### 13. Intentionally Unresolved
The following MUST remain unresolved:
- exact technical Policy fields beyond approved business categories
- database schema
- table names
- column names
- datatypes
- constraints
- indexes
- Policy Number technical format
- Insurer technical representation
- API/OpenAPI endpoints
- HTTP methods
- DTOs
- API schemas
- error contracts
- events/event payloads
- authorization rules
- roles
- permissions
- UI/screens/forms
- exact Policy creation/edit UX
- exact Insurer maintenance UX
- cross-module technical implementation
- persistence implementation
- commission calculation implementation
- event-driven integration details
