# REQ-DEC-010: Identity / JWT Contract

- **Capability:** Authentication, Identity Mapping & JWT Authorization Contract
- **Status:** APPROVED / FROZEN
- **Decision owner:** Architecture / Security
- **Identity Provider:** Auth0
- **OIDC Flow:** Authorization Code + PKCE
- **Architectural basis:** Phase 5 Stage 10 (Web UX Frontend Implementation)

## 1. Identity Provider & Flow

Auth0 is the approved external OIDC/OAuth2 Identity Provider for AnverraGlobal.
The React SPA must use the **Authorization Code + PKCE** flow.
- Implicit flow, password grant, and custom backend credential storage are rejected.
- Keycloak and AWS Cognito are rejected alternatives.

## 2. Identity Mapping (Model C1)

AnverraGlobal internal UUID is carried as a custom JWT claim:
`https://anverraglobal.com/identity_id`

- **Purpose:** Answers "Which internal AnverraGlobal identity does this token represent?"
- **Rule:** This is an identity locator ONLY. It is NOT an authorization grant.
- **Fail Closed:** The backend must fail closed (401/403) if this claim is missing, malformed, or not a valid UUID.

## 3. JWT Claims Contract

### Standard Claims (Backend Validated)
- `iss`: Issuer
- `aud`: Audience
- `exp`: Expiration
- `sub`: Auth0 Subject (Ignored by Anverra business logic)

### Application Claims
- `https://anverraglobal.com/identity_id`: String representation of the canonical AnverraGlobal UUID.
- `https://anverraglobal.com/roles`: Array of strings containing RBAC roles (e.g., `["ROLE_AGENT"]`).

## 4. RBAC Cardinality & Conflict Handling

AnverraGlobal supports exactly **ONE** active RBAC role per request context.
- **0 roles**: Fail closed (Authentication insufficient).
- **1 role**: Valid. Pass to `OrganizationScopeResolutionService`.
- **>1 roles**: Fail closed (Ambiguous context → HTTP 403).

Material mismatches between JWT role and Organization DB membership MUST fail closed (e.g., `ROLE_AGENT` + `CUSTOMER` DB row → `OrganizationScope.empty()`).

## 5. Organization Role Compatibility

Canonical mappings:
- `ROLE_CUSTOMER` ↔ `CUSTOMER`
- `ROLE_AGENT` ↔ `AGENT`
- `ROLE_BRANCH_ADMIN` ↔ `BRANCH_ADMIN`
- `ROLE_DEALER` ↔ `DEALER`

### Data Entry Contract
- **Mapping:** `ROLE_USER` ↔ `DATA_ENTRY`
- `ROLE_USER` is the generic authenticated RBAC context. `DATA_ENTRY` is the physical DB membership.
- **Rules:** Requires exactly one parent (`AGENT` or `BRANCH_ADMIN`). Cannot inherit from Data Entry, Customer, Dealer, or Global Admin. Inherits parent's exact scope. Results in `isDataEntry = true`.
- **Rejected Alternative:** `ROLE_DATA_ENTRY` does not exist.

### Global Admin Contract
- **Mapping:** `ROLE_ADMIN`
- **Rules:** Bypasses `organization_memberships` completely. Yields unrestricted `OrganizationScope` with `isGlobalAdmin = true` and `isDataEntry = false`.

### Unknown Identity
- Authenticated identity with no `organization_memberships` row yields `OrganizationScope.empty()`. It is NOT a 403.

### Multiple Memberships
- Identities with more than one `organization_memberships` row yield `AccessDeniedException` (HTTP 403). Fail closed.

## 6. Token Lifecycle & Storage

- **Access Token Lifetime:** 15 minutes.
- **Refresh Strategy:** Rotating Refresh Tokens maintained by the SPA via Auth0.
- **Browser Storage:** In-memory storage only.
- **Rejected Storage:** localStorage, sessionStorage, IndexedDB are rejected.
- **Rejected Architecture:** Backend For Frontend (BFF) is rejected.

## 7. Logout & Session Behavior

- Clear in-memory application authentication state.
- Clear cached data.
- Perform Auth0 logout / IdP session termination via Auth0 browser flow.
- Redirect to application login entry point.

## 8. Development Authentication

- **Local Frontend Dev:** Dedicated Auth0 Development Tenant.
- **Backend Tests:** `@WithMockUser` remains approved.
- **Frontend Tests:** MSW (Mock Service Worker) for API/Auth boundaries.
- **Rejected Alternatives:** Local Keycloak Docker, hardcoded JWTs, embedded backend passwords.

## 9. 401 / 403 Behavior

- **HTTP 401 (Unauthenticated):** Clear local state, redirect to Auth0 login.
- **HTTP 403 (Unauthorized):** Do not silently retry login. Render a standard Access Denied state. Data Entry Reporting attempts remain 403.

## 10. Architectural Ownership & Security Invariants

- **Identity/Security:** Owns JWT validation, identity/RBAC extraction, authentication context.
- **Organization:** Owns hierarchy, `organization_memberships`, Data Entry inheritance, and `OrganizationScope`.
- **Frontend:** Owns authentication UX, token acquisition, and rendering authorization outcomes. Frontend NEVER calculates `OrganizationScope` or decides visibility.
- **Invariants:** Client-supplied authorization flags are never trusted. JWT roles cannot override DB organization hierarchy. No `ROLE_DATA_ENTRY` is allowed. Global Admin requires explicit `ROLE_ADMIN` and bypasses DB hierarchy.
