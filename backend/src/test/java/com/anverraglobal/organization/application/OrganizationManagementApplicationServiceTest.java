package com.anverraglobal.organization.application;

import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.organization.domain.Branch;
import com.anverraglobal.organization.domain.Dealer;
import com.anverraglobal.organization.domain.OrganizationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrganizationManagementApplicationServiceTest {

    private OrganizationPersistencePort persistencePort;
    private OrganizationScopeResolutionService scopeResolutionService;
    private OrganizationManagementApplicationService service;

    @BeforeEach
    void setUp() {
        persistencePort = mock(OrganizationPersistencePort.class);
        scopeResolutionService = mock(OrganizationScopeResolutionService.class);
        service = new OrganizationManagementApplicationService(persistencePort, scopeResolutionService);
    }

    @Test
    void adminCanCreateDealer() {
        Dealer dealer = service.createDealer("Test Dealer", UUID.randomUUID(), "ROLE_ADMIN");
        assertThat(dealer.getName()).isEqualTo("Test Dealer");
        verify(persistencePort).saveDealer(dealer);
    }

    @Test
    void nonAdminCannotCreateDealer() {
        assertThatThrownBy(() -> service.createDealer("Test", UUID.randomUUID(), "ROLE_DEALER"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void dealerCanCreateBranchForOwnDealer() {
        UUID dealerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        
        Dealer dealer = Dealer.create("Dealer");
        when(persistencePort.findDealerById(dealerId)).thenReturn(Optional.of(dealer));
        
        when(scopeResolutionService.resolveScope(requesterId, "ROLE_DEALER")).thenReturn(OrganizationScope.empty(requesterId));
        
        OrganizationPersistencePort.OrganizationMembershipDto membership = new OrganizationPersistencePort.OrganizationMembershipDto(requesterId, "DEALER", null, dealerId, null);
        when(persistencePort.findMembershipsByIdentity(requesterId)).thenReturn(List.of(membership));

        Branch branch = service.createBranch(dealerId, "Branch", requesterId, "ROLE_DEALER");
        assertThat(branch.getName()).isEqualTo("Branch");
        verify(persistencePort).saveBranch(branch);
    }

    @Test
    void dealerCannotCreateBranchForOtherDealer() {
        UUID dealerId = UUID.randomUUID();
        UUID otherDealerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        
        when(scopeResolutionService.resolveScope(requesterId, "ROLE_DEALER")).thenReturn(OrganizationScope.empty(requesterId));
        
        OrganizationPersistencePort.OrganizationMembershipDto membership = new OrganizationPersistencePort.OrganizationMembershipDto(requesterId, "DEALER", null, dealerId, null);
        when(persistencePort.findMembershipsByIdentity(requesterId)).thenReturn(List.of(membership));

        assertThatThrownBy(() -> service.createBranch(otherDealerId, "Branch", requesterId, "ROLE_DEALER"))
                .isInstanceOf(AccessDeniedException.class);
    }
}
