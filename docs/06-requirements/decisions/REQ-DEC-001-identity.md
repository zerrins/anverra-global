# REQ-DEC-001: Identity & Access Architecture

- **Capability:** Identity & Access
- **Status:** AUTHORITATIVE / RESOLVED
- **Decision owner:** Human/Product/Security decision
- **Authentication architecture:** External OIDC/OAuth2 Identity Provider + JWT access tokens
- **Concrete IdP:** DEFERRED / implementation-infrastructure decision
- **Password/credential ownership:** External IdP
- **Backend responsibility:** JWT validation + authoritative authorization
- **Web/Mobile responsibility:** IdP-based authentication UX/token acquisition
- **Architectural basis:** Phase 4 D06
- **Scope:** Authentication architecture only
- **Authorization model:** NOT YET RESOLVED
- **JWT claims:** NOT YET RESOLVED
- **Roles/permissions:** NOT YET RESOLVED
- **Login/registration UX:** Universal Login via IdP
- **Password Policy:** 8 characters minimum, 1 capital letter, 1 symbol, 1 number (Configured in IdP)
