# REQ-DEC-003: Product Definition & MVP Lifecycle

- **Capability:** Product
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human / Product / Business decision

### Authoritative Business Definition
A Product represents an insurance offering/reference item maintained primarily for:
- policy association
- classification
- filtering
- statistics
- reporting

Product is intentionally a lightweight reference/master-data capability.
AnverraGlobal is NOT intended to become a detailed insurance-product configuration or product-engineering system.

### Product Category
Product Category is an authoritative business classification of a Product.

Category characteristics:
- Flat classification; no hierarchy/subcategories.
- Reference data only.
- Not a business workflow.
- Seeded/system-managed.
- Used primarily for filtering, statistics/reporting, and providing prefilled/selectable options when adding a Policy.
- Product Category does not have its own ACTIVE/INACTIVE lifecycle.
- Normal application users do not manage Product Categories through the application.

### Initial Authoritative Product Category Set
1. Life Insurance
2. Health Insurance
3. Motor Insurance
4. Travel Insurance
5. Property Insurance
6. Fire Insurance
7. Marine Insurance
8. Liability Insurance
9. Engineering Insurance
10. Commercial Insurance

### Product-to-Category Rule
- Every Product belongs to exactly one Product Category.
- A Product cannot belong to multiple categories.
- Product Categories are flat.

### Insurer Relationship
- Product is NOT tied to an Insurer/Provider.
- No Product -> Insurer business relationship is established by REQ-DEC-003.
- Do not invent an insurer/product association.

### Product Identity
- Each Product has a unique business identity/name within its Product Category.
- Product names must be unique within their category.
- The technical identifier, database key, code format, and datatype remain unresolved implementation details.

### Product Lifecycle
Initial lifecycle states:
- ACTIVE
- INACTIVE

Allowed transitions:
- ACTIVE -> INACTIVE
- INACTIVE -> ACTIVE

Products must NOT be physically deleted.
Historical/reference integrity must be preserved by retaining inactive Products.

### Product Maintenance
Products may be:
- initially seeded/system-managed
- subsequently maintained by authorized administrators through the application

Administrators may:
- create a Product
- update the Product name
- change the Product Category
- activate a Product
- deactivate a Product

No other Product-management capabilities are authorized by REQ-DEC-003.

### Explicitly Out of Scope
Do NOT promote any of the following into requirements:
- Product pricing
- Premium calculation
- Coverage modeling
- Riders
- Underwriting rules
- Eligibility rules
- Claims rules
- Product configuration
- Product versioning
- Product-specific workflows
- Insurer/Product relationship
- Detailed insurance-product structure

### Intentionally Unresolved
The following remain unresolved and MUST NOT be invented:
- Exact technical fields
- Exact database attributes
- Database schema/table structure
- Technical identifier format
- Product code format
- API/OpenAPI endpoints
- HTTP methods
- DTOs
- API schemas
- Events/event payloads
- Authorization rules
- Roles and permissions
- UI/screens/forms
- Exact admin UX
- Cross-module dependencies
- Product -> Policy technical relationship
- Persistence implementation details
