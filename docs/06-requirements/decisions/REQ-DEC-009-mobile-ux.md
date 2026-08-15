# REQ-DEC-009: Mobile UX Requirements

## Status

RESOLVED / AUTHORITATIVE

## Decision Owner

Product / Architecture

## 1. Decision

Define the Mobile MVP as an independent React Native + Expo application targeting iOS and Android phones, focused initially on authenticated Agent and Customer policy read workflows.

## 2. Context

Reference:

- D07 client architecture
- REQ-DEC-001 identity
- REQ-DEC-008 Web UX
- REQ-DEC-010 JWT contract

Explicitly state that Mobile is NOT required to achieve Web feature parity.

## 3. Goals

- Provide a dedicated, phone-first mobile experience for key personas.
- Enable Agents to view their assigned policies and customers.
- Enable Customers to view their own policies.
- Establish the foundational React Native + Expo architecture for future mobile expansion.

## 4. Non-Goals

- Web feature parity.
- Complex administrative workflows.
- Policy creation, editing, or lifecycle management.
- Commission administration.
- Reporting dashboards.

## 5. Platforms

iOS + Android, phone-first. Tablet-specific UX is deferred.

## 6. Personas

AGENT and CUSTOMER.

Explicitly Excluded from MVP: BRANCH_ADMIN, DATA_ENTRY, DEALER, ROLE_ADMIN.

## 7. Authentication

Auth0 + Authorization Code + PKCE + secure OS credential storage.

## 8. Authorization

Backend remains authoritative. The Mobile client must not calculate OrganizationScope, organization hierarchy, branch visibility, customer visibility, Data Entry scope, or Global Admin scope.

## 9. Navigation

Bottom-tab navigation (e.g., Policies, Profile/Account).

## 10. Policy

Included:
- Policy list
- Policy details
- Policy search/filter (where supported by existing APIs)
- Loading, empty, and error states
- Pull-to-refresh (where appropriate)

## 11. Commission

Deferred. The MVP may display commission-related information only if it is naturally embedded in an existing Policy Details response and requires no additional business logic.

## 12. Reporting

Deferred. No dedicated reporting dashboards in Mobile MVP.

## 13. Documents

Deferred. No camera capture, document scanning, document upload, offline storage, or preview infrastructure unless existing authoritative requirements explicitly require them.

## 14. Notifications

Deferred. No push notification infrastructure in MVP.

## 15. Offline

Online-only MVP. No offline reads/mutations or SQLite infrastructure.

## 16. Biometrics

Deferred. The authentication implementation must remain compatible with future biometric support.

## 17. Deep Linking

Deferred except for authentication redirect requirements.

## 18. API Integration

OpenAPI-generated client + TanStack Query. No direct database access, no BFF, and no mobile-specific backend or authorization logic.

## 19. Security

- OS secure credential storage required.
- No localStorage or sessionStorage for sensitive tokens.
- No client secret.
- 15-minute access tokens and rotating refresh tokens.
- No frontend authorization calculation.

## 20. Error Handling

- 401: Clear invalid authentication state, attempt Auth0 re-authentication, and return user to the app after successful login.
- 403: Render a standard "Access Denied" view without inferring backend reasoning or retrying with another role.
- Support RFC 7807 error presentation.

## 21. Testing

Unit + component + API mocking + authentication mocking. E2E testing framework selection is deferred.

## 22. MVP Boundary

**INCLUDED**
- React Native + Expo
- TypeScript
- iOS & Android
- Phone-first UX
- Auth0 authentication (PKCE, secure OS storage, token refresh)
- 401 & 403 handling
- Agent & Customer experiences
- Policy list & Policy details
- API-generated types & TanStack Query
- Component/unit testing

**EXCLUDED**
- Branch Admin, Data Entry, Dealer, and Global Admin workflows
- Commission administration
- Reporting dashboards
- Policy creation, editing, and lifecycle mutations
- Push notifications
- Offline mode
- Biometric authentication
- Camera/document scanning & upload
- Business deep links
- Mobile-specific BFF or backend

**DEFERRED**
Future expansion may include deferred items.

## 23. Future Expansion

Future Mobile decisions can extend functionality (e.g., policy creation, documents, offline mode) by adding workflows and integrating with updated backend APIs, without changing the foundational React Native, Expo, and TanStack Query architecture.

## 24. Alternatives Rejected

- **Web feature parity:** Not selected for this decision to keep MVP scope focused.
- **Flutter / Native iOS/Android:** Not selected; React Native + Expo is authoritative per D07.
- **Offline-first architecture:** Not selected for MVP to reduce complexity.
- **Mobile BFF / Mobile-specific backend:** Not selected; direct consumption of existing APIs is required.
- **Client-side authorization:** Explicitly rejected; authorization remains backend-owned.

## 25. Consequences

- **Positive:** Rapid MVP delivery, focused scope, strict alignment with authoritative backend security, leveraging existing APIs without new backend complexity.
- **Negative:** Limited functionality for administrative personas on mobile, requiring them to use the Web app. No offline support limits usage in poor connectivity scenarios.
