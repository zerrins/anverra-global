package com.anverraglobal.customer.adapter.outbound.persistence;

import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerType;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CustomerPersistenceAdapter.class)
@Testcontainers
class CustomerPersistenceAdapterTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private CustomerPersistenceAdapter adapter;

    @Autowired
    private CustomerRepository repository;

    private UUID dealerId1 = UUID.randomUUID();
    private UUID dealerId2 = UUID.randomUUID();
    
    private UUID branchId1 = UUID.randomUUID(); // Belongs to dealer 1
    private UUID branchId2 = UUID.randomUUID(); // Belongs to dealer 1
    private UUID branchId3 = UUID.randomUUID(); // Belongs to dealer 2
    
    private UUID agentA = UUID.randomUUID(); // Branch 1
    private UUID agentB = UUID.randomUUID(); // Branch 1
    private UUID agentC = UUID.randomUUID(); // Branch 2
    private UUID agentD = UUID.randomUUID(); // Branch 3

    private Customer c1, c2, c3, c4;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        
        c1 = Customer.create(CustomerType.INDIVIDUAL, "Alpha Customer", "contact1", "addr1", "{\"pan\":\"111\"}", null, dealerId1, branchId1, agentA);
        c2 = Customer.create(CustomerType.ORGANIZATION, "Bravo Corp", "contact2", "addr2", null, "{\"gstin\":\"222\"}", dealerId1, branchId1, agentB);
        c3 = Customer.create(CustomerType.INDIVIDUAL, "Charlie Customer", "contact3", "addr3", "{\"pan\":\"333\"}", null, dealerId1, branchId2, agentC);
        c4 = Customer.create(CustomerType.INDIVIDUAL, "Delta Customer", "contact4", "addr4", "{\"pan\":\"444\"}", null, dealerId2, branchId3, agentD);
        
        c4.deactivate();
        
        c1 = adapter.save(c1);
        c2 = adapter.save(c2);
        c3 = adapter.save(c3);
        c4 = adapter.save(c4);
    }

    @Test
    void test1_globalAdminCanSeeAllCustomers() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, null, null, null, PageRequest.of(0, 10));
        assertEquals(4, page.getTotalElements());
    }

    @Test
    void test2_dealerScopeCannotSeeCustomersOutsideItsScope() {
        OrganizationScope dealer1Scope = OrganizationScope.forDealer(UUID.randomUUID(), Set.of(branchId1, branchId2));
        Page<Customer> page = adapter.listByScope(dealer1Scope, null, null, null, PageRequest.of(0, 10));
        assertEquals(3, page.getTotalElements()); // c1, c2, c3
        
        OrganizationScope dealer2Scope = OrganizationScope.forDealer(UUID.randomUUID(), Set.of(branchId3));
        Page<Customer> page2 = adapter.listByScope(dealer2Scope, null, null, null, PageRequest.of(0, 10));
        assertEquals(1, page2.getTotalElements()); // c4
    }

    @Test
    void test3_branchAdminCannotSeeCustomersOutsideBranch() {
        OrganizationScope branch1Scope = OrganizationScope.forBranchAdmin(UUID.randomUUID(), branchId1);
        Page<Customer> page = adapter.listByScope(branch1Scope, null, null, null, PageRequest.of(0, 10));
        assertEquals(2, page.getTotalElements()); // c1, c2
    }

    @Test
    void test4_agentACannotSeeAgentBsCustomer() {
        OrganizationScope agentAScope = OrganizationScope.forAgent(UUID.randomUUID(), agentA);
        Page<Customer> page = adapter.listByScope(agentAScope, null, null, null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(c1.getId(), page.getContent().get(0).getId());
    }

    @Test
    void test5_customerACannotSeeCustomerB() {
        OrganizationScope customerScope = OrganizationScope.forCustomer(UUID.randomUUID(), c1.getId());
        Page<Customer> page = adapter.listByScope(customerScope, null, null, null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(c1.getId(), page.getContent().get(0).getId());
    }

    @Test
    void test6_dataEntryInheritsParentScope() {
        // Data Entry has isDataEntry=true, but has parent's allowedAgentIds
        OrganizationScope dataEntryAgentScope = new OrganizationScope(UUID.randomUUID(), null, Set.of(agentA), null, false, true);
        Page<Customer> page = adapter.listByScope(dataEntryAgentScope, null, null, null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(c1.getId(), page.getContent().get(0).getId());
    }

    @Test
    void test7_inScopeCustomerCanBeRetrievedById() {
        OrganizationScope agentAScope = OrganizationScope.forAgent(UUID.randomUUID(), agentA);
        Optional<Customer> found = adapter.findByIdAndScope(c1.getId(), agentAScope);
        assertTrue(found.isPresent());
    }

    @Test
    void test8_outOfScopeCustomerCannotBeRetrievedById() {
        OrganizationScope agentAScope = OrganizationScope.forAgent(UUID.randomUUID(), agentA);
        Optional<Customer> found = adapter.findByIdAndScope(c2.getId(), agentAScope); // c2 is agentB
        assertFalse(found.isPresent());
    }

    @Test
    void test9_nameFilteringWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, "Alpha", null, null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals("Alpha Customer", page.getContent().get(0).getName());
    }

    @Test
    void test10_customerTypeFilteringWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, null, "ORGANIZATION", null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals(CustomerType.ORGANIZATION, page.getContent().get(0).getCustomerType());
    }

    @Test
    void test11_statusFilteringWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, null, null, "INACTIVE", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals("Delta Customer", page.getContent().get(0).getName());
    }

    @Test
    void test12_combinedFiltersWork() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, "Customer", "INDIVIDUAL", "ACTIVE", PageRequest.of(0, 10));
        assertEquals(2, page.getTotalElements()); // Alpha and Charlie
    }

    @Test
    void test13_scopeAndFiltersWorkTogether() {
        OrganizationScope dealer1Scope = OrganizationScope.forDealer(UUID.randomUUID(), Set.of(branchId1, branchId2));
        Page<Customer> page = adapter.listByScope(dealer1Scope, "Alpha", "INDIVIDUAL", null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void test14_paginationWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, null, null, null, PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name")));
        assertEquals(2, page.getContent().size());
        assertEquals("Alpha Customer", page.getContent().get(0).getName());
        assertEquals("Bravo Corp", page.getContent().get(1).getName());
    }

    @Test
    void test15_totalCountIsCorrect() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, null, null, null, PageRequest.of(0, 2));
        assertEquals(4, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void test16_stableOrderingWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Page<Customer> page = adapter.listByScope(globalScope, null, null, null, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")));
        assertEquals("Delta Customer", page.getContent().get(0).getName());
        assertEquals("Charlie Customer", page.getContent().get(1).getName());
    }

    @Test
    void test17_jsonbPersistenceWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Customer loaded = adapter.findByIdAndScope(c1.getId(), globalScope).get();
        assertEquals("{\"pan\": \"111\"}", loaded.getIndividualInfo());
        
        Customer loadedOrg = adapter.findByIdAndScope(c2.getId(), globalScope).get();
        assertEquals("{\"gstin\": \"222\"}", loadedOrg.getBusinessInfo());
    }

    @Test
    void test18_optimisticLockingWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Customer instance1 = adapter.findByIdAndScope(c1.getId(), globalScope).get();
        Customer instance2 = adapter.findByIdAndScope(c1.getId(), globalScope).get();

        instance1.deactivate();
        adapter.save(instance1);

        instance2.deactivate();
        assertThrows(OptimisticLockingFailureException.class, () -> adapter.save(instance2));
    }
    
    @Test
    void test19_jsonbUpdateWorks() {
        OrganizationScope globalScope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        Customer loaded = adapter.findByIdAndScope(c1.getId(), globalScope).get();
        
        loaded.update("Alpha Customer", "contact1", "addr1", "{\"pan\": \"UPDATED\"}", null);
        adapter.save(loaded);
        
        Customer reloaded = adapter.findByIdAndScope(c1.getId(), globalScope).get();
        assertEquals("{\"pan\": \"UPDATED\"}", reloaded.getIndividualInfo());
    }
}
