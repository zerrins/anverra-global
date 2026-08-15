package com.anverraglobal.organization.application;

import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class OrganizationScopeResolutionServiceImpl implements OrganizationScopeResolutionService {

    private final OrganizationPersistencePort persistencePort;

    public OrganizationScopeResolutionServiceImpl(OrganizationPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public OrganizationScope resolveScope(UUID identityId, String role) {
        if ("ROLE_ADMIN".equals(role)) {
            return new OrganizationScope(identityId, null, null, null, true, false);
        }

        List<OrganizationPersistencePort.OrganizationMembershipDto> memberships = persistencePort.findMembershipsByIdentity(identityId);

        if (memberships.isEmpty()) {
            return OrganizationScope.empty(identityId);
        }

        if (memberships.size() > 1) {
            throw new AccessDeniedException("Multiple organization memberships found for identity: " + identityId);
        }

        OrganizationPersistencePort.OrganizationMembershipDto membership = memberships.get(0);
        return resolveMembership(membership, identityId, role);
    }

    private OrganizationScope resolveMembership(OrganizationPersistencePort.OrganizationMembershipDto membership, UUID originalIdentityId, String jwtRole) {
        String dbRole = membership.getRole();
        
        // Strip ROLE_ prefix from jwtRole to compare with dbRole
        String normalizedJwtRole = jwtRole.startsWith("ROLE_") ? jwtRole.substring(5) : jwtRole;
        
        // Ensure the JWT role matches the DB role, or if JWT role is USER, DB role must be DATA_ENTRY
        boolean isMatch = normalizedJwtRole.equals(dbRole) || 
                          ("USER".equals(normalizedJwtRole) && "DATA_ENTRY".equals(dbRole));
                          
        if (!isMatch) {
            // Role mismatch resolves to empty scope
            return OrganizationScope.empty(originalIdentityId);
        }

        switch (dbRole) {
            case "CUSTOMER":
                return OrganizationScope.forCustomer(originalIdentityId, membership.getIdentityId());
            case "AGENT":
                return OrganizationScope.forAgent(originalIdentityId, membership.getIdentityId());
            case "BRANCH_ADMIN":
                if (membership.getBranchId() == null) {
                    throw new AccessDeniedException("Branch Admin membership missing branch_id");
                }
                return OrganizationScope.forBranchAdmin(originalIdentityId, membership.getBranchId());
            case "DEALER":
                if (membership.getDealerId() == null) {
                    throw new AccessDeniedException("Dealer membership missing dealer_id");
                }
                List<UUID> branchIds = persistencePort.findBranchIdsByDealer(membership.getDealerId());
                return OrganizationScope.forDealer(originalIdentityId, new HashSet<>(branchIds));
            case "DATA_ENTRY":
                return resolveDataEntry(membership, originalIdentityId, jwtRole);
            default:
                throw new AccessDeniedException("Unsupported organization membership role: " + dbRole);
        }
    }

    private OrganizationScope resolveDataEntry(OrganizationPersistencePort.OrganizationMembershipDto dataEntryMembership, UUID originalIdentityId, String jwtRole) {
        if (dataEntryMembership.getParentIdentityId() == null) {
            throw new AccessDeniedException("Data Entry membership is missing parent_identity_id");
        }

        List<OrganizationPersistencePort.OrganizationMembershipDto> parentMemberships = persistencePort.findMembershipsByIdentity(dataEntryMembership.getParentIdentityId());

        if (parentMemberships.isEmpty()) {
            throw new AccessDeniedException("Data Entry parent identity does not exist in organization");
        }

        if (parentMemberships.size() > 1) {
            throw new AccessDeniedException("Data Entry parent has multiple organization memberships");
        }

        OrganizationPersistencePort.OrganizationMembershipDto parentMembership = parentMemberships.get(0);

        String parentDbRole = parentMembership.getRole();
        if (!"AGENT".equals(parentDbRole) && !"BRANCH_ADMIN".equals(parentDbRole)) {
            throw new AccessDeniedException("Data Entry parent must be AGENT or BRANCH_ADMIN. Found: " + parentDbRole);
        }

        // We must pass a valid JWT role to the parent's resolution to pass the security check. 
        // We synthesize the corresponding role prefix for the parent.
        String synthesizedParentJwtRole = "ROLE_" + parentDbRole;

        // Resolve the parent's scope using the parent's own identity to construct the base sets
        OrganizationScope parentScope = resolveMembership(parentMembership, parentMembership.getIdentityId(), synthesizedParentJwtRole);

        // Construct Data Entry scope inheriting exactly the parent's boundaries, but for the Data Entry's identity
        return new OrganizationScope(
                originalIdentityId,
                parentScope.allowedCustomerIds(),
                parentScope.allowedAgentIds(),
                parentScope.allowedBranchIds(),
                false, // isGlobalAdmin
                true   // isDataEntry
        );
    }
}
