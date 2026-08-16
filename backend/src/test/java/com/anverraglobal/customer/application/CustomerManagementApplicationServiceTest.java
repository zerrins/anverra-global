package com.anverraglobal.customer.application;

import com.anverraglobal.customer.application.port.inbound.CreateCustomerCommand;
import com.anverraglobal.customer.application.port.inbound.UpdateCustomerCommand;
import com.anverraglobal.customer.application.port.outbound.CustomerRepositoryPort;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerType;
import com.anverraglobal.organization.contracts.OrganizationHierarchyContract;
import com.anverraglobal.organization.contracts.OrganizationHierarchyContract.HierarchyInfo;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerManagementApplicationServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private OrganizationScopeResolutionService scopeResolutionService;

    @Mock
    private OrganizationHierarchyContract hierarchyContract;

    private CustomerManagementApplicationService service;

    private final UUID identityId = UUID.randomUUID();
    private final UUID targetDealerId = UUID.randomUUID();
    private final UUID targetBranchId = UUID.randomUUID();
    private final UUID targetAgentId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        service = new CustomerManagementApplicationService(customerRepository, scopeResolutionService, hierarchyContract);
    }

    @Test
    void test1_normalDealerCreationDerivesOwnership() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, null, null, null);
        when(hierarchyContract.getHierarchyForIdentity(identityId))
                .thenReturn(Optional.of(new HierarchyInfo(identityId, "ROLE_DEALER", null, targetDealerId, null)));
        
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer customer = service.createCustomer(identityId, "ROLE_DEALER", cmd);

        assertEquals(targetDealerId, customer.getDealerId());
        assertEquals(identityId, customer.getBranchId()); // Fallback to identityId since branchId is null
        assertEquals(identityId, customer.getAgentId());
    }

    @Test
    void test2_normalBranchAdminCreationDerivesOwnership() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, null, null, null);
        when(hierarchyContract.getHierarchyForIdentity(identityId))
                .thenReturn(Optional.of(new HierarchyInfo(identityId, "ROLE_BRANCH_ADMIN", targetBranchId, targetDealerId, null)));
        
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer customer = service.createCustomer(identityId, "ROLE_BRANCH_ADMIN", cmd);

        assertEquals(targetDealerId, customer.getDealerId());
        assertEquals(targetBranchId, customer.getBranchId());
        assertEquals(identityId, customer.getAgentId());
    }

    @Test
    void test3_agentCreationDerivesOwnership() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, null, null, null);
        when(hierarchyContract.getHierarchyForIdentity(identityId))
                .thenReturn(Optional.of(new HierarchyInfo(identityId, "ROLE_AGENT", targetBranchId, targetDealerId, null)));
        
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer customer = service.createCustomer(identityId, "ROLE_AGENT", cmd);

        assertEquals(targetDealerId, customer.getDealerId());
        assertEquals(targetBranchId, customer.getBranchId());
        assertEquals(identityId, customer.getAgentId());
    }

    @Test
    void test4_dataEntryCreationDerivesInheritedOwnership() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, null, null, null);
        UUID parentAgentId = UUID.randomUUID();
        when(hierarchyContract.getHierarchyForIdentity(identityId))
                .thenReturn(Optional.of(new HierarchyInfo(identityId, "DATA_ENTRY", targetBranchId, targetDealerId, parentAgentId)));
        
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer customer = service.createCustomer(identityId, "DATA_ENTRY", cmd);

        assertEquals(targetDealerId, customer.getDealerId());
        assertEquals(targetBranchId, customer.getBranchId());
        assertEquals(parentAgentId, customer.getAgentId());
    }

    @Test
    void test5_normalUserCannotInjectTargetDealerId() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, targetDealerId, null, null);
        assertThrows(IllegalArgumentException.class, () -> service.createCustomer(identityId, "ROLE_AGENT", cmd));
    }

    @Test
    void test8_globalAdminCanCreateCustomer() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, targetDealerId, targetBranchId, targetAgentId);
        
        when(hierarchyContract.getHierarchyForIdentity(targetAgentId))
                .thenReturn(Optional.of(new HierarchyInfo(targetAgentId, "ROLE_AGENT", targetBranchId, targetDealerId, null)));
        
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer customer = service.createCustomer(identityId, "ROLE_ADMIN", cmd);

        assertEquals(targetDealerId, customer.getDealerId());
        assertEquals(targetBranchId, customer.getBranchId());
        assertEquals(targetAgentId, customer.getAgentId());
    }

    @Test
    void test13_inconsistentHierarchyIsRejected() {
        CreateCustomerCommand cmd = new CreateCustomerCommand(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, targetDealerId, targetBranchId, targetAgentId);
        
        when(hierarchyContract.getHierarchyForIdentity(targetAgentId))
                .thenReturn(Optional.of(new HierarchyInfo(targetAgentId, "ROLE_AGENT", UUID.randomUUID(), targetDealerId, null))); // different branch
        
        assertThrows(IllegalArgumentException.class, () -> service.createCustomer(identityId, "ROLE_ADMIN", cmd));
    }

    @Test
    void test15_inScopeCustomerCanBeRead() {
        Customer c = mock(Customer.class);
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.of(c));

        Customer result = service.getCustomer(identityId, "ROLE_AGENT", UUID.randomUUID());
        assertNotNull(result);
    }

    @Test
    void test16_outOfScopeCustomerReturnsNotFoundSemantics() {
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getCustomer(identityId, "ROLE_AGENT", UUID.randomUUID()));
    }

    @Test
    void test20_inScopeUpdateSucceeds() {
        Customer c = Customer.create(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, targetDealerId, targetBranchId, targetAgentId);
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.of(c));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer result = service.updateCustomer(identityId, "ROLE_AGENT", c.getId(), new UpdateCustomerCommand(null, "New Name", "c2", "a2", "{\"pan\":\"1\"}", null));
        assertEquals("New Name", result.getName());
    }

    @Test
    void test21_outOfScopeUpdateIsRejected() {
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.updateCustomer(identityId, "ROLE_AGENT", UUID.randomUUID(), new UpdateCustomerCommand(null, "New Name", "c2", "a2", null, null)));
    }

    @Test
    void test23_customerCannotUpdateAnotherCustomer() {
        assertThrows(AccessDeniedException.class, () -> service.updateCustomer(identityId, "ROLE_CUSTOMER", UUID.randomUUID(), new UpdateCustomerCommand(null, "New Name", "c2", "a2", null, null)));
    }

    @Test
    void test25_permittedUserCanActivate() {
        Customer c = Customer.create(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, targetDealerId, targetBranchId, targetAgentId);
        c.deactivate();
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.of(c));
        
        service.activateCustomer(identityId, "ROLE_AGENT", c.getId());
        verify(customerRepository).save(c);
    }

    @Test
    void test29_dataEntryCannotLifecycle() {
        assertThrows(AccessDeniedException.class, () -> service.activateCustomer(identityId, "DATA_ENTRY", UUID.randomUUID()));
    }

    @Test
    void test30_customerCannotLifecycleAnotherCustomer() {
        assertThrows(AccessDeniedException.class, () -> service.activateCustomer(identityId, "ROLE_CUSTOMER", UUID.randomUUID()));
    }

    @Test
    void test32_servicePassesOrganizationScopeToRepository() {
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.listByScope(eq(scope), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of()));
        
        Page<Customer> page = service.listCustomers(identityId, "ROLE_AGENT", null, null, null, PageRequest.of(0, 10));
        assertNotNull(page);
    }

    @Test
    void test33_updatePassesIndividualInfoToDomain() {
        Customer c = Customer.create(CustomerType.INDIVIDUAL, "Alpha", "c1", "a1", "{\"pan\":\"1\"}", null, targetDealerId, targetBranchId, targetAgentId);
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.of(c));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer result = service.updateCustomer(identityId, "ROLE_AGENT", c.getId(), new UpdateCustomerCommand(null, "New Name", "c2", "a2", "{\"pan\":\"NEW\"}", null));
        assertEquals("{\"pan\":\"NEW\"}", result.getIndividualInfo());
    }

    @Test
    void test34_updatePassesBusinessInfoToDomain() {
        Customer c = Customer.create(CustomerType.ORGANIZATION, "Alpha", "c1", "a1", null, "{\"gstin\":\"1\"}", targetDealerId, targetBranchId, targetAgentId);
        OrganizationScope scope = OrganizationScope.empty(identityId);
        when(scopeResolutionService.resolveScope(identityId, "ROLE_AGENT")).thenReturn(scope);
        when(customerRepository.findByIdAndScope(any(), eq(scope))).thenReturn(Optional.of(c));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Customer result = service.updateCustomer(identityId, "ROLE_AGENT", c.getId(), new UpdateCustomerCommand(null, "New Name", "c2", "a2", null, "{\"gstin\":\"NEW\"}"));
        assertEquals("{\"gstin\":\"NEW\"}", result.getBusinessInfo());
    }
}
