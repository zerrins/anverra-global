package com.anverraglobal.organization.contracts.dto;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * Represents the resolved organizational boundaries for a specific identity.
 * This is used to enforce authorization at the database level by appending to WHERE clauses.
 */
public record OrganizationScope(
        UUID identityId,
        Set<UUID> allowedCustomerIds,
        Set<UUID> allowedAgentIds,
        Set<UUID> allowedBranchIds,
        boolean isGlobalAdmin,
        boolean isDataEntry
) {
    
    public OrganizationScope {
        allowedCustomerIds = allowedCustomerIds == null ? Collections.emptySet() : Collections.unmodifiableSet(allowedCustomerIds);
        allowedAgentIds = allowedAgentIds == null ? Collections.emptySet() : Collections.unmodifiableSet(allowedAgentIds);
        allowedBranchIds = allowedBranchIds == null ? Collections.emptySet() : Collections.unmodifiableSet(allowedBranchIds);
    }
    
    public static OrganizationScope forCustomer(UUID identityId, UUID customerId) {
        return new OrganizationScope(identityId, Set.of(customerId), null, null, false, false);
    }
    
    public static OrganizationScope forAgent(UUID identityId, UUID agentId) {
        return new OrganizationScope(identityId, null, Set.of(agentId), null, false, false);
    }
    
    public static OrganizationScope forBranchAdmin(UUID identityId, UUID branchId) {
        return new OrganizationScope(identityId, null, null, Set.of(branchId), false, false);
    }
    
    public static OrganizationScope forDealer(UUID identityId, Set<UUID> ownedBranchIds) {
        return new OrganizationScope(identityId, null, null, ownedBranchIds, false, false);
    }
    
    public static OrganizationScope empty(UUID identityId) {
        return new OrganizationScope(identityId, null, null, null, false, false);
    }
    
    public boolean allowsBranch(UUID branchId) {
        if (isGlobalAdmin) return true;
        return allowedBranchIds.contains(branchId);
    }
    
    public boolean allowsAgent(UUID agentId) {
        if (isGlobalAdmin) return true;
        return allowedAgentIds.contains(agentId);
    }
    
    public boolean allowsCustomer(UUID customerId) {
        if (isGlobalAdmin) return true;
        return allowedCustomerIds.contains(customerId);
    }
}
