package com.anverraglobal.organization.adapter.outbound.persistence;

import com.anverraglobal.organization.application.OrganizationScopeResolutionServiceImpl;
import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({OrganizationPersistenceAdapter.class, OrganizationScopeResolutionServiceImpl.class})
public class OrganizationPersistenceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrganizationScopeResolutionServiceImpl scopeResolutionService;

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM organization_memberships");
        jdbcTemplate.execute("DELETE FROM branches");
        jdbcTemplate.execute("DELETE FROM dealers");
    }

    @Test
    void testGlobalAdminBypassesDatabase() {
        UUID identityId = UUID.randomUUID();
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, "ROLE_ADMIN");

        assertThat(scope.isGlobalAdmin()).isTrue();
        assertThat(scope.isDataEntry()).isFalse();
    }

    @Test
    void testCustomerScope() {
        UUID identityId = insertMembership(UUID.randomUUID(), "CUSTOMER", null, null, null);
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, "ROLE_CUSTOMER");

        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.allowedCustomerIds()).containsExactly(identityId);
    }

    @Test
    void testAgentScope() {
        UUID identityId = insertMembership(UUID.randomUUID(), "AGENT", null, null, null);
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, "ROLE_AGENT");

        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.allowedAgentIds()).containsExactly(identityId);
    }

    @Test
    void testBranchAdminScope() {
        UUID branchId = UUID.randomUUID();
        UUID identityId = insertMembership(UUID.randomUUID(), "BRANCH_ADMIN", branchId, null, null);
        
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, "ROLE_BRANCH_ADMIN");

        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.allowedBranchIds()).containsExactly(branchId);
    }

    @Test
    void testDealerScope() {
        UUID dealerId = UUID.randomUUID();
        insertDealer(dealerId, "Test Dealer");
        
        UUID branch1 = insertBranch(UUID.randomUUID(), dealerId, "Branch 1");
        UUID branch2 = insertBranch(UUID.randomUUID(), dealerId, "Branch 2");
        
        UUID identityId = insertMembership(UUID.randomUUID(), "DEALER", null, dealerId, null);
        
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, "ROLE_DEALER");

        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.allowedBranchIds()).containsExactlyInAnyOrder(branch1, branch2);
    }

    @Test
    void testDataEntryUnderAgent() {
        UUID agentId = insertMembership(UUID.randomUUID(), "AGENT", null, null, null);
        UUID dataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, agentId);

        OrganizationScope scope = scopeResolutionService.resolveScope(dataEntryId, "ROLE_USER");

        assertThat(scope.isDataEntry()).isTrue();
        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.identityId()).isEqualTo(dataEntryId);
        assertThat(scope.allowedAgentIds()).containsExactly(agentId);
    }

    @Test
    void testDataEntryUnderBranchAdmin() {
        UUID branchId = UUID.randomUUID();
        UUID branchAdminId = insertMembership(UUID.randomUUID(), "BRANCH_ADMIN", branchId, null, null);
        UUID dataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, branchAdminId);

        OrganizationScope scope = scopeResolutionService.resolveScope(dataEntryId, "ROLE_USER");

        assertThat(scope.isDataEntry()).isTrue();
        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.identityId()).isEqualTo(dataEntryId);
        assertThat(scope.allowedBranchIds()).containsExactly(branchId);
    }

    @Test
    void testDataEntryNullParent_fails() {
        UUID dataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, null);

        assertThatThrownBy(() -> scopeResolutionService.resolveScope(dataEntryId, "ROLE_USER"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("missing parent_identity_id");
    }

    @Test
    void testDataEntryMissingParent_fails() {
        UUID dataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, UUID.randomUUID());

        assertThatThrownBy(() -> scopeResolutionService.resolveScope(dataEntryId, "ROLE_USER"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void testDataEntryParentIsDataEntry_fails() {
        UUID parentDataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, UUID.randomUUID());
        UUID dataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, parentDataEntryId);

        assertThatThrownBy(() -> scopeResolutionService.resolveScope(dataEntryId, "ROLE_USER"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("must be AGENT or BRANCH_ADMIN");
    }

    @Test
    void testDataEntryParentIsCustomer_fails() {
        UUID customerId = insertMembership(UUID.randomUUID(), "CUSTOMER", null, null, null);
        UUID dataEntryId = insertMembership(UUID.randomUUID(), "DATA_ENTRY", null, null, customerId);

        assertThatThrownBy(() -> scopeResolutionService.resolveScope(dataEntryId, "ROLE_USER"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("must be AGENT or BRANCH_ADMIN");
    }

    @Test
    void testMultipleMemberships_fails() {
        UUID identityId = UUID.randomUUID();
        insertMembership(identityId, "AGENT", null, null, null);
        insertMembership(identityId, "CUSTOMER", null, null, null);

        assertThatThrownBy(() -> scopeResolutionService.resolveScope(identityId, "ROLE_USER"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Multiple organization memberships found");
    }

    @Autowired
    private OrganizationPersistenceAdapter persistenceAdapter;

    @Test
    void testUnknownIdentity_returnsEmptyScope() {
        OrganizationScope scope = scopeResolutionService.resolveScope(UUID.randomUUID(), "ROLE_USER");
        
        assertThat(scope.isGlobalAdmin()).isFalse();
        assertThat(scope.isDataEntry()).isFalse();
        assertThat(scope.allowedCustomerIds()).isEmpty();
        assertThat(scope.allowedAgentIds()).isEmpty();
        assertThat(scope.allowedBranchIds()).isEmpty();
    }

    @Test
    void testFindAllDealers() {
        UUID d1 = insertDealer(UUID.randomUUID(), "D1");
        UUID d2 = insertDealer(UUID.randomUUID(), "D2");

        var dealers = persistenceAdapter.findAllDealers();
        assertThat(dealers).hasSize(2);
        assertThat(dealers).extracting(com.anverraglobal.organization.domain.Dealer::getName).containsExactlyInAnyOrder("D1", "D2");
    }

    @Test
    void testFindBranchesByDealer() {
        UUID d1 = insertDealer(UUID.randomUUID(), "D1");
        UUID b1 = insertBranch(UUID.randomUUID(), d1, "B1");
        UUID b2 = insertBranch(UUID.randomUUID(), d1, "B2");

        var branches = persistenceAdapter.findBranchesByDealer(d1);
        assertThat(branches).hasSize(2);
        assertThat(branches).extracting(com.anverraglobal.organization.domain.Branch::getName).containsExactlyInAnyOrder("B1", "B2");
    }

    @Test
    void testFindAgentIdsByBranch() {
        UUID branchId = UUID.randomUUID();
        UUID agent1Id = insertMembership(UUID.randomUUID(), "AGENT", branchId, null, null);
        UUID agent2Id = insertMembership(UUID.randomUUID(), "AGENT", branchId, null, null);
        UUID customerId = insertMembership(UUID.randomUUID(), "CUSTOMER", branchId, null, null);

        var agentIds = persistenceAdapter.findAgentIdsByBranch(branchId);
        assertThat(agentIds).containsExactlyInAnyOrder(agent1Id, agent2Id);
        assertThat(agentIds).doesNotContain(customerId);
    }

    private UUID insertMembership(UUID identityId, String role, UUID branchId, UUID dealerId, UUID parentIdentityId) {
        jdbcTemplate.update(
                "INSERT INTO organization_memberships (id, identity_id, role, branch_id, dealer_id, parent_identity_id, version) VALUES (?, ?, ?, ?, ?, ?, 0)",
                UUID.randomUUID(), identityId, role, branchId, dealerId, parentIdentityId
        );
        return identityId;
    }

    private UUID insertDealer(UUID id, String name) {
        jdbcTemplate.update(
                "INSERT INTO dealers (id, name, version) VALUES (?, ?, 0)",
                id, name
        );
        return id;
    }

    private UUID insertBranch(UUID id, UUID dealerId, String name) {
        jdbcTemplate.update(
                "INSERT INTO branches (id, dealer_id, name, version) VALUES (?, ?, ?, 0)",
                id, dealerId, name
        );
        return id;
    }
}
