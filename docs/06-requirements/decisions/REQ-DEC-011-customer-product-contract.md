# REQ-DEC-011: Customer & Product Implementation Contract

- **Capability:** Customer, Product, Policy Integration
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Architecture / Product Governance

## Executive Summary
This decision record formally freezes the implementation contracts for the Customer and Product domains, resolving the ambiguities left by `REQ-DEC-002` and `REQ-DEC-003`. **Customer and Product implementation is now authorized to proceed.**

## 1. Customer Ownership
- **Decision:** Customer is **ORGANIZATION-SCOPED**.
- **Rationale:** Customer visibility and access must be governed by the existing `OrganizationScope` authorization boundary. The Customer module must not introduce an independent or parallel authorization hierarchy. The concrete persistence representation must reuse the existing Organization hierarchy and select the smallest representation that preserves it. Global Admin remains unrestricted.

## 2. Customer Visibility
- **Decision:** Visibility is based strictly on the authenticated user's `OrganizationScope`.
- **Behavior:**
  - `ROLE_ADMIN` → Unrestricted Customer visibility.
  - `ROLE_DEALER` → Customers within permitted Dealer scope.
  - `ROLE_BRANCH_ADMIN` → Customers within permitted Branch scope.
  - `ROLE_AGENT` → Customers within permitted OrganizationScope.
  - `ROLE_USER` / `DATA_ENTRY` → Customers within inherited permitted scope.
  - `ROLE_CUSTOMER` → Own Customer record only.
- **Constraints:** No frontend filtering. Backend `OrganizationScope` remains authoritative. Agent visibility is NOT artificially restricted to only Customers attached to assigned Policies.

## 3. Customer Authorization
| Role | List | Read | Create | Update | Lifecycle |
|------|------|------|--------|--------|-----------|
| `ROLE_ADMIN` | All | All | Yes | Yes | Yes |
| `ROLE_DEALER` | Scope | Scope | Yes | Yes | Yes |
| `ROLE_BRANCH_ADMIN` | Scope | Scope | Yes | Yes | Yes |
| `ROLE_AGENT` | Scope | Scope | Yes | Yes | No |
| `ROLE_USER` / `DATA_ENTRY`| Scope | Scope | Yes | Yes | No |
| `ROLE_CUSTOMER` | Self | Self | No | No | No |

## 4. Customer Schema & API
- **Fields:** `id` (UUID), `customer_type` (INDIVIDUAL, ORGANIZATION), `name`, `contact_info`, `address_info`, `status` (ACTIVE, INACTIVE), `created_at`, `updated_at`, `version`, `individual_info` (JSONB), `business_info` (JSONB), and organization ownership representation.
- **KYC Fields:** PAN, Aadhaar, GSTIN, DOB, etc., remain deferred unless explicitly required by existing authoritative requirements. They should use JSONB if needed for extensibility without strict DB columns.
- **Events:** NONE FOR MVP.
- **API Endpoints:**
  - `POST /api/v1/customers`
  - `GET /api/v1/customers` (Server-side pagination, filters: name, customer_type, status)
  - `GET /api/v1/customers/{id}`
  - `PUT /api/v1/customers/{id}`
  - `POST /api/v1/customers/{id}/lifecycle/activate`
  - `POST /api/v1/customers/{id}/lifecycle/deactivate`

## 5. Product Ownership & Schema
- **Decision:** Product is **GLOBAL REFERENCE/MASTER DATA**. It is NOT organization-scoped.
- **Fields:** `id` (UUID), `name` (unique per category), `category` (1 of 10 defined in REQ-DEC-003), `status` (ACTIVE, INACTIVE), `created_at`, `updated_at`, `version`.
- **Authorization:** `ROLE_ADMIN` manages Product. Authenticated business users may read ACTIVE Product reference data. Inactive Products are retained for historical integrity. Global Admin may manage and inspect inactive Products.
- **Events:** NONE FOR MVP.
- **API Endpoints:**
  - `POST /api/v1/products`
  - `GET /api/v1/products`
  - `GET /api/v1/products/{id}`
  - `PUT /api/v1/products/{id}`
  - `POST /api/v1/products/{id}/lifecycle/activate`
  - `POST /api/v1/products/{id}/lifecycle/deactivate`

## 6. Policy / Product Integration
- **Decision:** Policy gains `productId: UUID`.
- **Migration Strategy:** `product_id` is NULLABLE for historical existing policies. Do NOT create a fake/default Product to backfill existing policies. Existing policies with no Product remain valid historical records with `product_id = NULL`.
- **New Policy Creation:** `productId` is REQUIRED. Product existence must be validated at the Policy application boundary. Do NOT introduce a direct Policy → Product database foreign key.

## 7. Web UX
- **Customer UX:** Customer List (with Search, Type/Status filters), Customer Details, Customer Create/Edit, Customer Activate/Deactivate. UX remains backend-authoritative (no frontend OrganizationScope calculations).
- **Product UX:** Product List (with Search, Category filter), Product Details, Product Create/Edit, Product Activate/Deactivate. Mutation is restricted to `ROLE_ADMIN`.
- **Product Selection UX (Policy Creation):** Selectable only when ACTIVE. Backend/API driven selection. Requires loading state, empty state, validation errors. Inactive or invalid Product assignment is rejected by the backend.

## 8. Testing Contract
- **Customer Backend:** Aggregate/unit, Persistence, API/controller, OrganizationScope authorization isolation.
- **Product Backend:** Aggregate/unit, Persistence (Unique constraints), API/controller, `ROLE_ADMIN` mutation.
- **Integration:** Valid/Invalid/Inactive Product assignment to Policy. Existing Policy read integrity.
- **Frontend:** List, filters, forms, Policy creation selection, RFC 7807 error handling, MSW authorization states.

## 9. Security Invariants
- `OrganizationScope` remains the absolute backend authorization boundary.
- No frontend role-based filtering or client-supplied scopes may be trusted.
- Data Entry continues to inherit parent scope.

## 10. Rejected Alternatives
- *Globally Shared Customers:* Rejected due to data leakage risks across Dealers.
- *Policy-Associated Visibility:* Rejected as overly restrictive and complex for Agents.
- *Fake Product Backfill:* Rejected as it corrupts historical integrity.
- *DB Foreign Key between Policy & Product:* Rejected to preserve strict bounded-context isolation in the modulith architecture.
