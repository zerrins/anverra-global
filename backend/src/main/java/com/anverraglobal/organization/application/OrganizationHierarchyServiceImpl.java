package com.anverraglobal.organization.application;

import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import com.anverraglobal.organization.contracts.OrganizationHierarchyContract;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationHierarchyServiceImpl implements OrganizationHierarchyContract {

    private final OrganizationPersistencePort persistencePort;
    private final OrganizationScopeResolutionService scopeResolutionService;
    private final com.anverraglobal.identity.contracts.IdentityProfileContract identityProfileContract;

    public OrganizationHierarchyServiceImpl(OrganizationPersistencePort persistencePort,
                                            OrganizationScopeResolutionService scopeResolutionService,
                                            com.anverraglobal.identity.contracts.IdentityProfileContract identityProfileContract) {
        this.persistencePort = persistencePort;
        this.scopeResolutionService = scopeResolutionService;
        this.identityProfileContract = identityProfileContract;
    }

    @Override
    public Optional<HierarchyInfo> getHierarchyForIdentity(UUID identityId) {
        List<OrganizationPersistencePort.OrganizationMembershipDto> memberships = persistencePort.findMembershipsByIdentity(identityId);
        if (memberships.isEmpty()) {
            return Optional.empty();
        }
        var m = memberships.get(0);
        return Optional.of(new HierarchyInfo(m.getIdentityId(), m.getRole(), m.getBranchId(), m.getDealerId(), m.getParentIdentityId()));
    }

    public List<com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse> getDealers(UUID requesterId, String requesterRole) {
        if (!"ROLE_ADMIN".equals(requesterRole)) {
            throw new org.springframework.security.access.AccessDeniedException("Only ADMIN can list all dealers");
        }
        return persistencePort.findAllDealers().stream()
                .map(d -> new com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse(d.getId(), d.getName()))
                .toList();
    }

    public List<com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse> getBranches(UUID requesterId, String requesterRole, UUID dealerId) {
        var scope = scopeResolutionService.resolveScope(requesterId, requesterRole);

        // Verify dealer actually exists
        if (persistencePort.findDealerById(dealerId).isEmpty()) {
            throw new java.util.NoSuchElementException("Dealer not found");
        }

        var branches = persistencePort.findBranchesByDealer(dealerId);
        
        if (!scope.isGlobalAdmin()) {
            branches = branches.stream()
                    .filter(b -> scope.allowsBranch(b.getId()))
                    .toList();
                    
            if (branches.isEmpty() && !"ROLE_ADMIN".equals(requesterRole)) {
                 throw new org.springframework.security.access.AccessDeniedException("Access Denied to Dealer Branches");
            }
        }

        return branches.stream()
                .map(b -> new com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse(b.getId(), b.getName()))
                .toList();
    }

    public List<com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse> getAgents(UUID requesterId, String requesterRole, UUID branchId) {
        var scope = scopeResolutionService.resolveScope(requesterId, requesterRole);

        // First verify branch exists
        var branchOpt = persistencePort.findBranchById(branchId);
        if (branchOpt.isEmpty()) {
            throw new java.util.NoSuchElementException("Branch not found");
        }

        if (!scope.allowsBranch(branchId)) {
            throw new org.springframework.security.access.AccessDeniedException("Access Denied to Branch Agents");
        }

        List<UUID> agentIds = persistencePort.findAgentIdsByBranch(branchId);
        if (agentIds.isEmpty()) {
            return List.of();
        }

        java.util.Map<UUID, String> names = identityProfileContract.resolveDisplayNames(new java.util.HashSet<>(agentIds));

        return agentIds.stream().map(id -> {
            String name = names.get(id);
            if (name == null) {
                String strId = id.toString();
                name = "Agent " + strId.substring(0, Math.min(8, strId.length()));
            }
            return new com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse(id, name);
        }).toList();
    }
}
