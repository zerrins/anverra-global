package com.anverraglobal.organization.application;

import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.domain.Branch;
import com.anverraglobal.organization.domain.Dealer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrganizationManagementApplicationService {

    private final OrganizationPersistencePort persistencePort;
    private final OrganizationScopeResolutionService scopeResolutionService;

    public OrganizationManagementApplicationService(OrganizationPersistencePort persistencePort,
                                                    OrganizationScopeResolutionService scopeResolutionService) {
        this.persistencePort = persistencePort;
        this.scopeResolutionService = scopeResolutionService;
    }

    public Dealer createDealer(String name, UUID requesterId, String requesterRole) {
        verifyAdminAccess(requesterRole);
        Dealer dealer = Dealer.create(name);
        persistencePort.saveDealer(dealer);
        return dealer;
    }

    public Dealer updateDealer(UUID id, String name, UUID requesterId, String requesterRole) {
        verifyAdminAccess(requesterRole);
        Dealer dealer = persistencePort.findDealerById(id)
                .orElseThrow(() -> new NoSuchElementException("Dealer not found"));
        dealer.updateName(name);
        persistencePort.saveDealer(dealer);
        return dealer;
    }

    public Dealer activateDealer(UUID id, UUID requesterId, String requesterRole) {
        verifyAdminAccess(requesterRole);
        Dealer dealer = persistencePort.findDealerById(id)
                .orElseThrow(() -> new NoSuchElementException("Dealer not found"));
        dealer.activate();
        persistencePort.saveDealer(dealer);
        return dealer;
    }

    public Dealer deactivateDealer(UUID id, UUID requesterId, String requesterRole) {
        verifyAdminAccess(requesterRole);
        Dealer dealer = persistencePort.findDealerById(id)
                .orElseThrow(() -> new NoSuchElementException("Dealer not found"));
        dealer.deactivate();
        persistencePort.saveDealer(dealer);
        return dealer;
    }

    public List<Dealer> listDealers(UUID requesterId, String requesterRole) {
        verifyAdminAccess(requesterRole);
        return persistencePort.findAllDealers();
    }

    public Dealer getDealer(UUID id, UUID requesterId, String requesterRole) {
        verifyAdminAccess(requesterRole);
        return persistencePort.findDealerById(id)
                .orElseThrow(() -> new NoSuchElementException("Dealer not found"));
    }

    public Branch createBranch(UUID dealerId, String name, UUID requesterId, String requesterRole) {
        verifyBranchManagementAccess(dealerId, requesterId, requesterRole);
        Dealer dealer = persistencePort.findDealerById(dealerId)
                .orElseThrow(() -> new NoSuchElementException("Dealer not found"));
        if (dealer.getStatus() == com.anverraglobal.organization.domain.OrganizationStatus.INACTIVE) {
             throw new IllegalStateException("Cannot create branch for INACTIVE dealer");
        }
        Branch branch = Branch.create(dealerId, name);
        persistencePort.saveBranch(branch);
        return branch;
    }

    public Branch updateBranch(UUID branchId, String name, UUID requesterId, String requesterRole) {
        Branch branch = persistencePort.findBranchById(branchId)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        verifyBranchManagementAccess(branch.getDealerId(), requesterId, requesterRole);
        branch.updateName(name);
        persistencePort.saveBranch(branch);
        return branch;
    }

    public Branch activateBranch(UUID branchId, UUID requesterId, String requesterRole) {
        Branch branch = persistencePort.findBranchById(branchId)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        verifyBranchManagementAccess(branch.getDealerId(), requesterId, requesterRole);
        branch.activate();
        persistencePort.saveBranch(branch);
        return branch;
    }

    public Branch deactivateBranch(UUID branchId, UUID requesterId, String requesterRole) {
        Branch branch = persistencePort.findBranchById(branchId)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        verifyBranchManagementAccess(branch.getDealerId(), requesterId, requesterRole);
        branch.deactivate();
        persistencePort.saveBranch(branch);
        return branch;
    }

    public List<Branch> listBranches(UUID dealerId, UUID requesterId, String requesterRole) {
        verifyBranchManagementAccess(dealerId, requesterId, requesterRole);
        return persistencePort.findBranchesByDealer(dealerId);
    }

    public Branch getBranch(UUID branchId, UUID requesterId, String requesterRole) {
        Branch branch = persistencePort.findBranchById(branchId)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        verifyBranchManagementAccess(branch.getDealerId(), requesterId, requesterRole);
        return branch;
    }

    private void verifyAdminAccess(String requesterRole) {
        if (!"ROLE_ADMIN".equals(requesterRole)) {
            throw new AccessDeniedException("Only ADMIN can perform this action");
        }
    }

    private void verifyBranchManagementAccess(UUID dealerId, UUID requesterId, String requesterRole) {
        if ("ROLE_ADMIN".equals(requesterRole)) {
            return;
        }
        var scope = scopeResolutionService.resolveScope(requesterId, requesterRole);
        if (!"ROLE_DEALER".equals(requesterRole)) {
             throw new AccessDeniedException("Only ADMIN or DEALER can manage branches");
        }
        
        List<com.anverraglobal.organization.application.port.out.OrganizationPersistencePort.OrganizationMembershipDto> memberships = 
              persistencePort.findMembershipsByIdentity(requesterId);
        
        boolean hasAccess = memberships.stream()
            .anyMatch(m -> "DEALER".equals(m.getRole()) && dealerId.equals(m.getDealerId()));
            
        if (!hasAccess) {
             throw new AccessDeniedException("Dealer can only manage their own branches");
        }
    }
}
