package com.anverraglobal.organization.application;

import com.anverraglobal.identity.contracts.IdentityProfileContract;
import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationHierarchyServiceImplTest {

    private final OrganizationPersistencePort persistencePort = Mockito.mock(OrganizationPersistencePort.class);
    private final OrganizationScopeResolutionService scopeResolutionService = Mockito.mock(OrganizationScopeResolutionService.class);
    private final IdentityProfileContract identityProfileContract = Mockito.mock(IdentityProfileContract.class);

    private final OrganizationHierarchyServiceImpl service = new OrganizationHierarchyServiceImpl(
            persistencePort, scopeResolutionService, identityProfileContract
    );

    @Test
    void getDealers_admin_success() {
        UUID adminId = UUID.randomUUID();
        UUID dealerId = UUID.randomUUID();
        Mockito.when(persistencePort.findAllDealers()).thenReturn(List.of(new OrganizationPersistencePort.DealerDto(dealerId, "Dealer 1")));

        List<HierarchyNodeResponse> dealers = service.getDealers(adminId, "ROLE_ADMIN");
        assertEquals(1, dealers.size());
        assertEquals("Dealer 1", dealers.get(0).name());
    }

    @Test
    void getDealers_agent_denied() {
        assertThrows(AccessDeniedException.class, () -> service.getDealers(UUID.randomUUID(), "ROLE_AGENT"));
    }

    @Test
    void getBranches_dealer_successForOwnDealer() {
        UUID dealerId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Mockito.when(scopeResolutionService.resolveScope(userId, "ROLE_DEALER"))
                .thenReturn(OrganizationScope.forDealer(userId, Set.of(branchId)));
        Mockito.when(persistencePort.findDealerById(dealerId))
                .thenReturn(Optional.of(new OrganizationPersistencePort.DealerDto(dealerId, "D1")));
        Mockito.when(persistencePort.findBranchesByDealer(dealerId))
                .thenReturn(List.of(new OrganizationPersistencePort.BranchDto(branchId, "Branch 1", dealerId)));

        List<HierarchyNodeResponse> branches = service.getBranches(userId, "ROLE_DEALER", dealerId);
        assertEquals(1, branches.size());
        assertEquals("Branch 1", branches.get(0).name());
    }

    @Test
    void getBranches_dealer_deniedForOtherDealer() {
        UUID dealerId = UUID.randomUUID();
        UUID otherDealerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Mockito.when(scopeResolutionService.resolveScope(userId, "ROLE_DEALER"))
                .thenReturn(OrganizationScope.forDealer(userId, Set.of(UUID.randomUUID())));
        Mockito.when(persistencePort.findDealerById(otherDealerId))
                .thenReturn(Optional.of(new OrganizationPersistencePort.DealerDto(otherDealerId, "D2")));
        Mockito.when(persistencePort.findBranchesByDealer(otherDealerId))
                .thenReturn(List.of(new OrganizationPersistencePort.BranchDto(UUID.randomUUID(), "B2", otherDealerId)));

        assertThrows(AccessDeniedException.class, () -> service.getBranches(userId, "ROLE_DEALER", otherDealerId));
    }

    @Test
    void getAgents_branchAdmin_successWithIdentityEnrichment() {
        UUID branchId = UUID.randomUUID();
        UUID dealerId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID agent1Id = UUID.randomUUID();
        UUID agent2Id = UUID.randomUUID();

        Mockito.when(scopeResolutionService.resolveScope(adminId, "ROLE_BRANCH_ADMIN"))
                .thenReturn(OrganizationScope.forBranchAdmin(adminId, branchId));
        Mockito.when(persistencePort.findBranchById(branchId))
                .thenReturn(Optional.of(new OrganizationPersistencePort.BranchDto(branchId, "B1", dealerId)));
        Mockito.when(persistencePort.findAgentIdsByBranch(branchId))
                .thenReturn(List.of(agent1Id, agent2Id));
        
        // Agent 1 has profile, Agent 2 is missing
        Mockito.when(identityProfileContract.resolveDisplayNames(Set.of(agent1Id, agent2Id)))
                .thenReturn(Map.of(agent1Id, "Agent John"));

        List<HierarchyNodeResponse> agents = service.getAgents(adminId, "ROLE_BRANCH_ADMIN", branchId);
        
        assertEquals(2, agents.size());
        assertTrue(agents.stream().anyMatch(a -> a.id().equals(agent1Id) && a.name().equals("Agent John")));
        
        // Fallback name check for Agent 2
        String fallbackName = "Agent " + agent2Id.toString().substring(0, 8);
        assertTrue(agents.stream().anyMatch(a -> a.id().equals(agent2Id) && a.name().equals(fallbackName)));

        // Verify batch call
        Mockito.verify(identityProfileContract, Mockito.times(1)).resolveDisplayNames(Set.of(agent1Id, agent2Id));
    }
}
