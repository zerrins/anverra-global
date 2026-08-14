# Phase 5 API Contract Technical Design

## 1. API Design Principles
- **Style:** REST over HTTP/JSON using Spring Web MVC.
- **Model Separation:** Strict 3-tier separation (HTTP DTO != Application Command != Domain Model).
- **Controller Logic:** Controllers contain ZERO business logic. They extract security context (Principal ID) and delegate to the application layer.
- **Errors:** RFC 7807 `ProblemDetail` standard for error payloads.
- **Classification:** **AUTHORITATIVE** (Inherited from D04).

## 2. API Versioning
- **Decision:** All Phase 5 API paths will use `/api/v1`.
- **Classification:** **DESIGN DECISION**

## 3. Module / Controller Ownership
- **Policy Module:** Owns `PolicyController` and `PolicyDocumentController`.
- **Reporting Module:** Owns `PolicyStatisticsController` and `CommissionStatisticsController`.
- **Organization Module:** Organization provides the organizational hierarchy and scope-resolution capability required by Phase 5. Its own HTTP API surface is outside the scope of this Phase 5 API contract unless explicitly required by an authoritative requirement.
- **Commission Module:** No standalone Commission CRUD endpoints should be introduced unless directly required by an authoritative requirement. Commission configuration is primarily coordinated through Policy application use cases where specified.
- **Classification:** **AUTHORITATIVE**

## 4. Policy Endpoints
The following endpoints belong to the Policy module:
- `POST /api/v1/policies` (Create)
- `GET /api/v1/policies/{id}` (Retrieve by ID)
- `GET /api/v1/policies` (List / Search)
- `PATCH /api/v1/policies/{id}` (Progressive Update)
- **Classification:** **DESIGN DECISION**

## 5. Existing Policy Resolution API
- **Business Requirement:** An authorized duplicate Policy attempt must safely resolve without throwing a generic 409 error or leaking unauthorized existence.
- **Endpoint:** `POST /api/v1/policies/resolve`
- **Request Structure:** Payload containing the unique business keys (e.g., `{ "referenceNumber": "...", "insurerId": "..." }`).
- **Response Structure:** 
  - `200 OK` with `{ "policyId": "<uuid>" }` (Policy exists and caller is authorized). Frontend can navigate to the existing Policy.
  - `404 Not Found` (Authorized lookup target genuinely does not exist).
- **Existence Concealment:** Any security-sensitive existence-concealment behavior MUST follow the mechanism established by D04/D06. 
- **Difference from 409:** The `resolve` endpoint is the *normal* workflow to check existing policies. A `409 Conflict` is strictly reserved for an unexpected concurrency/integrity conflict, including a uniqueness race despite the normal resolution workflow.
- **Classification:** **DESIGN DECISION**

## 6. Lifecycle APIs
- **Endpoints:**
  - `POST /api/v1/policies/{id}/lifecycle/activate`
  - `POST /api/v1/policies/{id}/lifecycle/deactivate`
  - `POST /api/v1/policies/{id}/lifecycle/reactivate`
- **Rules:**
  - Editing via `PATCH` does not implicitly change lifecycle.
  - No `DELETE /api/v1/policies/{id}` endpoint exists.
  - Activation validates the required fields and applies the 0/1/2-agent + UNSET/ZERO/POSITIVE matrix.
  - Invalid transitions return the D04-authorized business validation response (e.g., `400 Bad Request` or `422 Unprocessable Entity` as an API design proposal).
- **Classification:** **DESIGN DECISION**

## 7. Commission Representation
- **Representation:**
  - `UNSET`: Represented as unconfigured/omitted or explicit status (e.g., `{ "status": "UNSET" }`). It is excluded from statistics.
  - `ZERO`: Represented as explicitly configured zero (e.g., `{ "amount": 0, "status": "CONFIGURED" }`). It is included in statistics.
  - `POSITIVE`: Represented as `{ "amount": 100.50, "status": "CONFIGURED" }`.
- **Rules:** Commission is not mandatory for 0-agent Policy activation. ZERO is a valid commission and does not keep Policy in DRAFT.
- **Classification:** **DESIGN DECISION**

## 8. Reporting APIs
- **Endpoints:**
  - `GET /api/v1/reporting/policies/statistics`
  - `GET /api/v1/reporting/commissions/statistics`
- **Ownership:** Exclusively owned by the `Reporting` module. Operational modules (Policy, Commission) do not expose these.
- **Rules:** Commission statistics expose absolute amounts only. Data Entry users receive a `403 Forbidden` on Commission aggregate APIs.
- **Classification:** **REQUIRED CONSEQUENCE**

## 9. Document APIs
- **Ownership:** Policy module (e.g., `PolicyDocumentController`), using a provider-neutral `DocumentStoragePort`. No document module is introduced.
- **Endpoints:**
  - `POST /api/v1/policies/{id}/document` (Upload)
  - `GET /api/v1/policies/{id}/document` (Download)
  - `PUT /api/v1/policies/{id}/document` (Replace)
  - `DELETE /api/v1/policies/{id}/document` (Remove)
- **Download Behavior:** The `GET` endpoint returns a temporary signed object-storage URL through the provider-neutral `DocumentStoragePort`. Cloudflare R2 remains the selected provider.
- **Rules:** 0..1 document. No document versioning. Authorization is inherited from the Policy.
- **Classification:** **DESIGN DECISION**

## 10. Authorization Behavior
- **Rules:** The backend is authoritative. The application layer resolves the `OrganizationScope`. The API merely extracts the authenticated Principal and passes it to the application layer. Reporting does not independently resolve hierarchy; it consumes resolved context.
- **Classification:** **AUTHORITATIVE**

## 11. Pagination / Search / Filter / Sorting
- **Model:** Server-side pagination using `page`, `size`, `sort` query parameters.
- **Rules:** Filters *never* expand authorization scope; they only narrow the baseline authorization scope.
- **Classification:** **DESIGN DECISION**

## 12. Error Model
- **Format:** RFC 7807 `ProblemDetail`.
- **Status Codes:**
  - `401`: Unauthenticated.
  - `403`: Authenticated but unauthorized to perform the requested resource/action.
  - `404`: Authorized lookup target genuinely does not exist.
  - `400`: Malformed request.
  - `409`: Unexpected concurrency/integrity conflict.
  - `422`: Business validation failures (deferred to API contract design by D04).
- **Classification:** `401, 403, 404, 400, 409` are **AUTHORITATIVE**. `422` is a **DESIGN DECISION / PROPOSAL**.

## 13. Concurrency Behavior
- **Rules:** Optimistic locking remains the recommended implementation mechanism.
- **Representation:** The exact HTTP representation remains an API implementation/detail decision. It is NOT mandatory to use ETag / If-Match, nor is a request-body "version" mechanism an architectural mandate.
- **Classification:** **DESIGN DECISION**

## 14. OpenAPI Structure
- **Strategy:** Code-first OpenAPI using `springdoc-openapi`.
- **Classification:** **AUTHORITATIVE** (from D04)

## 15. DTO / Command / Domain Separation
- **Rules:** Request DTOs are mapped to immutable Application Commands via stateless API Mappers. Application Commands are passed to Inbound Ports. Domain Models are never returned directly to the API layer.
- **Classification:** **AUTHORITATIVE**

## 16. API-to-Requirement Traceability
- **REQ-DEC-004 (Identity/Organization):** Handled via Authorization behavior.
- **REQ-DEC-005 (Commission Matrix):** Enforced in Lifecycle Activation endpoint.
- **REQ-DEC-007 (Reporting Separation):** Enforced by Reporting API Ownership.
- **REQ-DEC-008 (Document Storage):** Enforced by Document APIs and signed URL download mechanism.
- **Classification:** **AUTHORITATIVE**

## 17. Decisions Made
1. API paths will use `/api/v1`.
2. Existing Policy resolution uses `POST /api/v1/policies/resolve`.
3. Document download returns a temporary signed URL.
4. UNSET commission is explicitly represented via status/omission, not as zero.
- **Classification:** **DESIGN DECISION**

## 18. Remaining Technical Ambiguities
- Exact JSON field names (e.g., `camelCase` conventions for specific domain fields) are deferred to implementation.
- Maximum page sizes for list endpoints.
- **Classification:** **UNRESOLVED**

## 19. Implementation Dependencies
- `springdoc-openapi-starter-webmvc-ui` MUST be added to `backend/pom.xml` during implementation to support the Code-First OpenAPI strategy.
- **Classification:** **REQUIRED CONSEQUENCE**

## 20. Explicitly Deferred Implementation Items
- Do NOT write Java code, controllers, DTOs, or Spring Web MVC logic yet.
- Do NOT generate OpenAPI clients yet.
- **Classification:** **REQUIRED CONSEQUENCE**
