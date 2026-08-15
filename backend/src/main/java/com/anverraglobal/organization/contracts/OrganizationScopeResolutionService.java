package com.anverraglobal.organization.contracts;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;

import java.util.UUID;

/**
 * Public contract for resolving organizational boundaries for an authenticated identity.
 */
public interface OrganizationScopeResolutionService {
    
    /**
     * Resolves the authoritative organizational scope for a given identity and their asserted role.
     * 
     * @param identityId the Identity Provider's subject identifier (e.g. user_id)
     * @param role the systemic function of the user (e.g. ROLE_AGENT, ROLE_DEALER, ROLE_CUSTOMER)
     * @return the computed OrganizationScope governing the user's allowed data access
     */
    OrganizationScope resolveScope(UUID identityId, String role);
}
