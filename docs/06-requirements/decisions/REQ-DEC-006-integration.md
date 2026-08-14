# REQ-DEC-006: Cross-Module & Organizational Integration

- **Capability:** Cross-Module & Organizational Integration
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human / Product / Business decision

### 1. Authority
This decision establishes BUSINESS relationships and ACCESS SCOPE.
It does NOT establish:
- database foreign keys
- API contracts
- HTTP endpoints
- event topics
- event payloads
- technical authorization implementation
- database schemas
- persistence strategy
- service/module communication mechanism
Those remain unresolved.

### 2. Core Policy-Customer Relationship
Every Policy belongs to exactly one Customer.
One Customer may have multiple Policies.
Customer association is immutable after Policy creation.
A Policy cannot be reassigned from one Customer to another.
Do NOT invent a customer transfer/reassignment workflow.

### 3. Policy Creation Paths
There are three authoritative Policy creation paths.

#### 3.1 Customer-created Policy
When a Customer creates a Policy:
- The Policy initially has no Agent.
- The Policy initially has no Commission.
- Commission information is NOT required.
- The Customer may edit the Policy while:
  - the Customer originally created it, AND
  - no Commission has been added.
The exact editable fields remain unresolved.
Do NOT invent a field-level editability matrix.

#### 3.2 Agent-created Policy
When a regular Agent creates/manages a Policy:
- The Agent acts as Agent A / Primary Agent.
- Commission rules from REQ-DEC-005 apply.
- Optional Agent B may be involved.
- The Policy is NOT assigned to any Branch.
- Regular Agent-created Policies do NOT automatically become branch-bound.
- Branch Admins do not gain access merely because an Agent-created Policy belongs to a Customer.
Agent access is based on Policy involvement.

#### 3.3 Branch Admin-created Policy
When a Branch Admin creates a Policy:
- The Branch Admin acts as Agent A / Primary Agent.
- Commission rules from REQ-DEC-005 apply.
- The Policy is assigned to the Branch Admin's Branch.
- The Policy is therefore branch-bound.
- The Branch Admin can access/manage Policies belonging to their Branch.
- Dealer can access/manage the Policy through the Dealer's branch hierarchy.
- Data Entry users under that Branch Admin inherit the Branch Admin's branch Policy scope.

IMPORTANT:
Branch Admin is not being defined as a separate Policy business model.
A Branch Admin performs the same Policy/Commission business actions as an Agent when acting as Agent A.
The difference is primarily ACCESS SCOPE.
Do not invent a "Branch Admin Policy" entity/type.

### 4. Dealer-created Policy
A Dealer may create a Policy.
A Dealer cannot act as Agent A merely by creating the Policy.
When a Dealer creates a Policy:
- Dealer MUST select a Branch Admin.
- The selected Branch Admin becomes Agent A / Primary Agent.
- Commission rules from REQ-DEC-005 apply.
- The Policy belongs to the selected Branch Admin's Branch.
- The Policy is therefore branch-bound.
- The Dealer can manage the Policy.
- The selected Branch Admin can manage the Policy.
- Data Entry users under the selected Branch Admin inherit the Branch scope.

The selected Branch Admin determines the Policy's Branch.
Do NOT create a Dealer-owned unassigned Policy concept.

### 5. Branch Boundary
Branch assignment is NOT universal.
Authoritative rules:
- Regular Agent-created Policy → NOT branch-bound.
- Branch Admin-created Policy → branch-bound.
- Dealer-created Policy → branch-bound through the selected Branch Admin.

A Policy created by a regular Agent must NOT automatically receive a Branch.
A Policy does not become branch-bound merely because the Customer is known to a Branch.
Do NOT invent automatic branch assignment.

### 6. Dealer Organizational Hierarchy
The organizational hierarchy is:
```text
Dealer
  |
  +-- Branch
  |     |
  |     +-- Branch Admin
  |           |
  |           +-- Data Entry
  |
  +-- Branch
        |
        +-- Branch Admin
              |
              +-- Data Entry
```
A Dealer may have multiple Branches.
Each Branch has its Branch Admin(s) as defined by the business organization.
Do NOT invent additional organizational levels.

### 7. Dealer Policy Access
A Dealer can access/manage all Policies belonging to all Branches under that Dealer.
Dealer visibility is organizational.
A Dealer does NOT need to be personally involved as an Agent in a Policy to access/manage it.

Example:
```text
Dealer A
  |
  +-- Branch 1
  |     +-- Policy P1
  |     +-- Policy P2
  |
  +-- Branch 2
        +-- Policy P3
```
Dealer A can access/manage P1, P2, P3.
A Dealer cannot access Policies belonging to branches outside that Dealer's organizational hierarchy.

### 8. Branch Admin Policy Access
A Branch Admin can access/manage:
> ALL Policies belonging to that Branch.

IMPORTANT:
Access is NOT limited to Policies created by that Branch Admin.
For example:
```text
Branch A
  |
  +-- Branch Admin A
  |
  +-- Policy P1 — added by Branch Admin A
  +-- Policy P2 — added by another permitted actor
  +-- Policy P3 — added by Dealer and assigned to Branch A
```
Branch Admin A can access/manage P1, P2, P3.
Do NOT narrow Branch Admin access to "Policies added by them."

### 9. Regular Agent Policy Access
A regular Agent can access only Policies in which that Agent is involved.
An Agent may access a Policy when the Agent is:
- Agent A, OR
- Agent B

An Agent cannot automatically access:
- all Policies belonging to a Customer
- all Policies belonging to a Branch
- all Policies belonging to a Dealer

An Agent-created Policy remains non-branch-bound.

### 10. Data Entry under Agent
A Data Entry user registered under an Agent inherits the Policy access scope of that parent Agent.
Therefore:
```text
Agent
  |
  +-- Data Entry
       |
       +-- access to Policies accessible to parent Agent
```
Data Entry under an Agent does NOT gain independent global Policy visibility.
Do NOT invent additional Data Entry permissions.

### 11. Data Entry under Branch Admin
A Data Entry user registered under a Branch Admin inherits the Policy access scope of that parent Branch Admin.
Therefore:
```text
Branch Admin
  |
  +-- Branch
       |
       +-- Policies
       |
       +-- Data Entry
             |
             +-- access to Policies belonging to parent Branch
```
Data Entry under a Branch Admin does NOT get access to other branches.
Do NOT invent additional Data Entry permissions.

### 12. Customer Policy Visibility
A Customer can view Policies belonging to that Customer.
This includes Policies originally added by:
- the Customer
- an Agent
- a Branch Admin
- a Dealer through a selected Branch Admin

The Customer's Policy visibility is based on Customer association.

### 13. Customer Editability
Customer editing is conditional.

#### Customer-created, non-commission Policy
If:
- Customer originally created the Policy, AND
- no Commission has been added,

then:
- Customer can view it.
- Customer can edit it.
The exact editable fields are intentionally unresolved.

#### Commission-bearing / agent-managed Policy
If Commission has been added / the Policy becomes agent-managed:
- Customer can view the Policy.
- Customer cannot edit the Policy.

This applies even if the Policy was originally created by the Customer.
Customer cannot modify:
- Commission
- Agent assignment
- Agent allocation

Do NOT invent additional field-level restrictions beyond the above.

### 14. Customer Visibility of Agents
Customers may see the names/identity of Agents/Brokers involved in their Policies.
This does NOT give Customers access to Commission information.
Customers cannot modify Agent assignment or Commission information.
Do NOT invent additional Agent profile information visible to Customers.

### 15. Customer → Agent Transition
A Customer-created Policy can later become agent-managed / commission-bearing.
When this happens:
- Agent involvement may be established.
- Commission may be added.
- REQ-DEC-005 commission rules apply.
- Customer editing becomes blocked.
- Customer retains view access.

Do NOT invent the technical workflow for this transition.

### 16. Policy → Product Integration
Every Policy belongs to exactly one Product.
One Product may be associated with multiple Policies.
Product remains insurer-independent.
Product lifecycle is independent from Policy lifecycle.

If Product becomes INACTIVE:
- Existing Policies retain their Product association.
- Existing Policies are NOT automatically changed.
- Existing Policies do NOT become INACTIVE merely because Product is inactive.
- Inactive Product remains available for historical/reference/statistics purposes.
- Inactive Product cannot be selected for new Policies.

Do NOT invent Product replacement/migration workflows.

### 17. Policy → Insurer Integration
Every Policy identifies exactly one Insurer/Provider.
One Insurer may be associated with multiple Policies.
Product does NOT become tied to Insurer.

If Insurer becomes unavailable/inactive:
- Existing Policies retain their Insurer association.
- Existing Policies are NOT automatically changed.
- Existing Policies do NOT become INACTIVE merely because Insurer is inactive.
- Insurer remains available for historical/reference/statistics purposes.
- Inactive/unavailable Insurer cannot be selected for new Policies.

Do NOT invent detailed Insurer profiles or workflows.

### 18. Policy → Commission Integration
Commission applies to agent-managed Policies.
Customer-created Policies can initially exist without Commission.

When an Agent/Broker/Branch Admin becomes responsible for a Policy and Commission is added:
- REQ-DEC-005 applies.
- Agent A is established.
- Optional Agent B may participate.
- Commission rules and allocation rules apply.

Commission is not an intrinsic mandatory property of every Policy.
Do NOT contradict REQ-DEC-005.

### 19. Branch Admin as Agent
When a Branch Admin creates/manages a branch-bound Policy:
- Branch Admin acts as Agent A.
- Branch Admin follows the same Policy business actions as an Agent.
- Branch Admin follows the same Commission business rules as Agent A.
- Branch Admin is additionally subject to Branch-scoped access.

Do NOT define Branch Admin as a separate Commission model.
Do NOT create a separate commission formula for Branch Admin.

### 20. Dealer vs Agent Responsibility
Dealer is an organizational manager across branches.
Dealer can:
- view/manage Policies across their branches
- create a Policy
- select a Branch Admin as responsible Agent A

Dealer cannot:
- become Agent A simply by creating a Policy
- create an unassigned Dealer-owned Policy

The selected Branch Admin is the responsible Agent A.

### 21. Access Scope Summary
| Actor | Policy scope |
|---|---|
| Customer | Policies belonging to that Customer |
| Customer-created, no Commission | Customer can view + edit |
| Customer-created then commission-bearing | Customer can view only |
| Regular Agent | Policies where Agent is involved |
| Branch Admin | ALL Policies belonging to their Branch |
| Dealer | ALL Policies belonging to all Branches under Dealer |
| Data Entry under Agent | Parent Agent's Policy scope |
| Data Entry under Branch Admin | Parent Branch Admin's Branch Policy scope |
| Unrelated Agent | No access |
| Unrelated Branch Admin | No access |
| Unrelated Dealer | No access |

### 22. Explicitly Unresolved
Do NOT invent:
- exact roles/permission matrix beyond the business access scopes above
- authorization implementation
- JWT claims
- scopes
- API endpoints
- API contracts
- DTOs
- HTTP methods
- database foreign keys
- database schemas
- tables
- columns
- persistence model
- events
- event topics
- event payloads
- synchronous vs asynchronous technical communication
- technical identity relationships
- UI screens
- navigation
- field-level editability beyond explicitly approved rules
- Data Entry actions beyond inherited Policy scope
- Dealer/Branch Admin user-management workflows
- branch creation/management workflows
- Agent registration workflows
- Customer onboarding/KYC workflows

### 23. Explicitly Out of Scope
Do NOT introduce:
- automatic branch assignment for regular Agent-created Policies
- multi-level Agent → Agent hierarchy
- Agent B → Agent C
- Dealer acting as Agent A
- unassigned Dealer-owned Policies
- Customer reassignment between Policies
- Product replacement workflows
- Insurer replacement workflows
- detailed insurer management
- accounting
- payout workflows
- complex commission hierarchy
- claims/underwriting/renewal workflows
