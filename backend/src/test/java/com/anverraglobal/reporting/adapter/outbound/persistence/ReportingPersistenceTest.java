package com.anverraglobal.reporting.adapter.outbound.persistence;

import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.policy.event.PolicyActivatedEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import com.anverraglobal.policy.event.PolicyDeactivatedEvent;
import com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent;
import com.anverraglobal.policy.event.PolicyReactivatedEvent;
import com.anverraglobal.reporting.application.dto.CommissionStatisticsResponse;
import com.anverraglobal.reporting.application.dto.PolicyStatisticsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(ReportingPersistenceAdapter.class)
class ReportingPersistenceTest {

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
    private ReportingPersistenceAdapter persistenceAdapter;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.update("DELETE FROM reporting_policy_read_models");
    }

    @Test
    void testPolicyCreatedEventPersistsRow() {
        UUID policyId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID agentAId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        PolicyCreatedEvent event = PolicyCreatedEvent.create(
                policyId, 1L, "POL-001", customerId, agentAId, null, branchId, "DRAFT", new BigDecimal("1000.00")
        );

        persistenceAdapter.savePolicyCreatedEvent(event);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(rows).hasSize(1);
        Map<String, Object> row = rows.get(0);
        assertThat(row.get("policy_number")).isEqualTo("POL-001");
        assertThat(row.get("customer_id")).isEqualTo(customerId);
        assertThat(row.get("agent_a_id")).isEqualTo(agentAId);
        assertThat(row.get("branch_id")).isEqualTo(branchId);
        assertThat(((Number) row.get("premium")).doubleValue()).isEqualTo(1000.00);
        assertThat(row.get("status")).isEqualTo("DRAFT");
        assertThat(((Number) row.get("policy_aggregate_version")).longValue()).isEqualTo(1L);
        assertThat(((Number) row.get("commission_aggregate_version")).longValue()).isEqualTo(0L);
    }

    @Test
    void testPolicyLifecycleEventsUpdateStatus() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "DRAFT", new BigDecimal("1000.00"), 1L);

        persistenceAdapter.savePolicyActivatedEvent(PolicyActivatedEvent.create(
                policyId, 2L, "POL-001", UUID.randomUUID(), null, null, null, "ACTIVE", new BigDecimal("1000.00")));
        assertStatus(policyId, "ACTIVE", 2L);

        persistenceAdapter.savePolicyDeactivatedEvent(PolicyDeactivatedEvent.create(
                policyId, 3L, "POL-001", UUID.randomUUID(), null, null, null, "INACTIVE", new BigDecimal("1000.00")));
        assertStatus(policyId, "INACTIVE", 3L);

        persistenceAdapter.savePolicyReactivatedEvent(PolicyReactivatedEvent.create(
                policyId, 4L, "POL-001", UUID.randomUUID(), null, null, null, "ACTIVE", new BigDecimal("1000.00")));
        assertStatus(policyId, "ACTIVE", 4L);
    }

    @Test
    void testPolicyPremiumUpdatedEventUpdatesPremium() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "ACTIVE", new BigDecimal("1000.00"), 1L);

        persistenceAdapter.savePolicyPremiumUpdatedEvent(PolicyPremiumUpdatedEvent.create(
                policyId, 2L, "POL-001", UUID.randomUUID(), null, null, null, "ACTIVE", new BigDecimal("1200.00")
        ));

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(((Number) row.get("premium")).doubleValue()).isEqualTo(1200.00);
        assertThat(((Number) row.get("policy_aggregate_version")).longValue()).isEqualTo(2L);
    }

    @Test
    void testCommissionConfiguredEventUpdatesCommissionFields() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "ACTIVE", new BigDecimal("1000.00"), 1L);

        CommissionConfiguredEvent event = CommissionConfiguredEvent.create(
                policyId, 1L, "CONFIGURED", "PERCENTAGE", new BigDecimal("150.00"), new BigDecimal("100.00"), new BigDecimal("50.00")
        );

        persistenceAdapter.saveCommissionConfiguredEvent(event);

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(row.get("commission_status")).isEqualTo("CONFIGURED");
        assertThat(row.get("commission_type")).isEqualTo("PERCENTAGE");
        assertThat(((Number) row.get("total_commission_value")).doubleValue()).isEqualTo(150.00);
        assertThat(((Number) row.get("agent_a_share")).doubleValue()).isEqualTo(100.00);
        assertThat(((Number) row.get("agent_b_share")).doubleValue()).isEqualTo(50.00);
        assertThat(((Number) row.get("commission_aggregate_version")).longValue()).isEqualTo(1L);
        assertThat(((Number) row.get("policy_aggregate_version")).longValue()).isEqualTo(1L); // remains unchanged
    }

    @Test
    void testOutdatedPolicyEventIsIgnored() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "DRAFT", new BigDecimal("1000.00"), 5L);

        persistenceAdapter.savePolicyActivatedEvent(PolicyActivatedEvent.create(
                policyId, 4L, "POL-001", UUID.randomUUID(), null, null, null, "ACTIVE", new BigDecimal("1000.00")));
        
        // Status should remain DRAFT because incoming version 4 < existing version 5
        assertStatus(policyId, "DRAFT", 5L);
    }

    @Test
    void testOutdatedCommissionEventIsIgnored() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "ACTIVE", new BigDecimal("1000.00"), 1L);

        CommissionConfiguredEvent event1 = CommissionConfiguredEvent.create(
                policyId, 2L, "CONFIGURED", "PERCENTAGE", new BigDecimal("150.00"), new BigDecimal("100.00"), new BigDecimal("50.00")
        );
        persistenceAdapter.saveCommissionConfiguredEvent(event1);

        CommissionConfiguredEvent event2 = CommissionConfiguredEvent.create(
                policyId, 1L, "UNSET", null, null, null, null
        );
        persistenceAdapter.saveCommissionConfiguredEvent(event2); // Should be ignored

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(row.get("commission_status")).isEqualTo("CONFIGURED");
        assertThat(((Number) row.get("commission_aggregate_version")).longValue()).isEqualTo(2L);
    }

    @Test
    void testPolicyStatisticsAggregation() {
        UUID customerId = UUID.randomUUID();
        createPolicy(UUID.randomUUID(), "DRAFT", new BigDecimal("100.00"), 1L, customerId, null, null);
        createPolicy(UUID.randomUUID(), "DRAFT", new BigDecimal("100.00"), 1L, customerId, null, null);
        createPolicy(UUID.randomUUID(), "ACTIVE", new BigDecimal("100.00"), 1L, customerId, null, null);
        createPolicy(UUID.randomUUID(), "INACTIVE", new BigDecimal("100.00"), 1L, customerId, null, null);

        OrganizationScope scope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false); // Global Admin
        PolicyStatisticsResponse stats = persistenceAdapter.getPolicyStatistics(scope);

        assertThat(stats.totalPolicies()).isEqualTo(4L);
        assertThat(stats.draftCount()).isEqualTo(2L);
        assertThat(stats.activeCount()).isEqualTo(1L);
        assertThat(stats.inactiveCount()).isEqualTo(1L);
    }

    @Test
    void testCommissionStatisticsAggregation() {
        UUID policy1 = UUID.randomUUID();
        createPolicy(policy1, "ACTIVE", new BigDecimal("100.00"), 1L);
        persistenceAdapter.saveCommissionConfiguredEvent(CommissionConfiguredEvent.create(
                policy1, 1L, "CONFIGURED", "FIXED", new BigDecimal("50.00"), new BigDecimal("30.00"), new BigDecimal("20.00")));

        UUID policy2 = UUID.randomUUID();
        createPolicy(policy2, "ACTIVE", new BigDecimal("100.00"), 1L);
        persistenceAdapter.saveCommissionConfiguredEvent(CommissionConfiguredEvent.create(
                policy2, 1L, "CONFIGURED", "FIXED", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00")));

        UUID policy3 = UUID.randomUUID();
        createPolicy(policy3, "ACTIVE", new BigDecimal("100.00"), 1L);
        persistenceAdapter.saveCommissionConfiguredEvent(CommissionConfiguredEvent.create(
                policy3, 1L, "UNSET", null, null, null, null));

        OrganizationScope scope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        CommissionStatisticsResponse stats = persistenceAdapter.getCommissionStatistics(scope);

        assertThat(stats.configuredCommissionCount()).isEqualTo(2L); // UNSET is excluded
        assertThat(stats.totalCommissionAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(stats.agentACommissionAmount()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(stats.agentBCommissionAmount()).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void testEmptyResultReturnsZeros() {
        OrganizationScope scope = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        PolicyStatisticsResponse policyStats = persistenceAdapter.getPolicyStatistics(scope);
        assertThat(policyStats.totalPolicies()).isEqualTo(0L);
        assertThat(policyStats.draftCount()).isEqualTo(0L);
        assertThat(policyStats.activeCount()).isEqualTo(0L);
        assertThat(policyStats.inactiveCount()).isEqualTo(0L);

        CommissionStatisticsResponse commissionStats = persistenceAdapter.getCommissionStatistics(scope);
        assertThat(commissionStats.configuredCommissionCount()).isEqualTo(0L);
        assertThat(commissionStats.totalCommissionAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(commissionStats.agentACommissionAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(commissionStats.agentBCommissionAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testOrganizationScopeFiltering() {
        UUID customer1 = UUID.randomUUID();
        UUID customer2 = UUID.randomUUID();
        UUID agentA = UUID.randomUUID();
        UUID branch1 = UUID.randomUUID();

        createPolicy(UUID.randomUUID(), "ACTIVE", new BigDecimal("100.00"), 1L, customer1, agentA, branch1);
        createPolicy(UUID.randomUUID(), "DRAFT", new BigDecimal("100.00"), 1L, customer2, agentA, branch1);
        createPolicy(UUID.randomUUID(), "ACTIVE", new BigDecimal("100.00"), 1L, customer2, null, null);

        // Global Admin sees all
        OrganizationScope globalAdmin = new OrganizationScope(UUID.randomUUID(), null, null, null, true, false);
        assertThat(persistenceAdapter.getPolicyStatistics(globalAdmin).totalPolicies()).isEqualTo(3L);

        // Customer 1 sees only their 1 policy
        OrganizationScope customerScope = OrganizationScope.forCustomer(UUID.randomUUID(), customer1);
        assertThat(persistenceAdapter.getPolicyStatistics(customerScope).totalPolicies()).isEqualTo(1L);

        // Agent A sees their 2 policies
        OrganizationScope agentScope = OrganizationScope.forAgent(UUID.randomUUID(), agentA);
        assertThat(persistenceAdapter.getPolicyStatistics(agentScope).totalPolicies()).isEqualTo(2L);

        // Branch 1 sees its 2 policies
        OrganizationScope branchScope = OrganizationScope.forBranchAdmin(UUID.randomUUID(), branch1);
        assertThat(persistenceAdapter.getPolicyStatistics(branchScope).totalPolicies()).isEqualTo(2L);
        
        // Unauthorized Empty scope sees 0
        OrganizationScope emptyScope = OrganizationScope.empty(UUID.randomUUID());
        assertThat(persistenceAdapter.getPolicyStatistics(emptyScope).totalPolicies()).isEqualTo(0L);
    }

    private void createPolicy(UUID policyId, String status, BigDecimal premium, Long version) {
        createPolicy(policyId, status, premium, version, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    void testBothEventOrdersA() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "ACTIVE", new BigDecimal("1000.00"), 5L);
        
        // Initial commission
        CommissionConfiguredEvent initialCommission = CommissionConfiguredEvent.create(
                policyId, 3L, "CONFIGURED", "PERCENTAGE", new BigDecimal("150.00"), new BigDecimal("100.00"), new BigDecimal("50.00")
        );
        persistenceAdapter.saveCommissionConfiguredEvent(initialCommission);

        PolicyPremiumUpdatedEvent premiumEvent = PolicyPremiumUpdatedEvent.create(
                policyId, 6L, "POL-001", UUID.randomUUID(), null, null, null, "ACTIVE", new BigDecimal("1200.00")
        );
        
        CommissionConfiguredEvent unsetCommission = CommissionConfiguredEvent.create(
                policyId, 4L, "UNSET", null, null, null, null
        );

        // Order A: Premium -> Commission
        persistenceAdapter.savePolicyPremiumUpdatedEvent(premiumEvent);
        persistenceAdapter.saveCommissionConfiguredEvent(unsetCommission);

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(((Number) row.get("premium")).doubleValue()).isEqualTo(1200.00);
        assertThat(row.get("commission_status")).isEqualTo("UNSET");
        assertThat(((Number) row.get("policy_aggregate_version")).longValue()).isEqualTo(6L);
        assertThat(((Number) row.get("commission_aggregate_version")).longValue()).isEqualTo(4L);
    }

    @Test
    void testBothEventOrdersB() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "ACTIVE", new BigDecimal("1000.00"), 5L);
        
        // Initial commission
        CommissionConfiguredEvent initialCommission = CommissionConfiguredEvent.create(
                policyId, 3L, "CONFIGURED", "PERCENTAGE", new BigDecimal("150.00"), new BigDecimal("100.00"), new BigDecimal("50.00")
        );
        persistenceAdapter.saveCommissionConfiguredEvent(initialCommission);

        PolicyPremiumUpdatedEvent premiumEvent = PolicyPremiumUpdatedEvent.create(
                policyId, 6L, "POL-001", UUID.randomUUID(), null, null, null, "ACTIVE", new BigDecimal("1200.00")
        );
        
        CommissionConfiguredEvent unsetCommission = CommissionConfiguredEvent.create(
                policyId, 4L, "UNSET", null, null, null, null
        );

        // Order B: Commission -> Premium
        persistenceAdapter.saveCommissionConfiguredEvent(unsetCommission);
        persistenceAdapter.savePolicyPremiumUpdatedEvent(premiumEvent);

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(((Number) row.get("premium")).doubleValue()).isEqualTo(1200.00);
        assertThat(row.get("commission_status")).isEqualTo("UNSET");
        assertThat(((Number) row.get("policy_aggregate_version")).longValue()).isEqualTo(6L);
        assertThat(((Number) row.get("commission_aggregate_version")).longValue()).isEqualTo(4L);
    }

    @Test
    void testMissingPolicyRowBehavior() {
        UUID policyId = UUID.randomUUID();
        CommissionConfiguredEvent event = CommissionConfiguredEvent.create(
                policyId, 1L, "CONFIGURED", "PERCENTAGE", new BigDecimal("150.00"), new BigDecimal("100.00"), new BigDecimal("50.00")
        );

        // This should run silently without throwing an exception, and 0 rows should be updated.
        persistenceAdapter.saveCommissionConfiguredEvent(event);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(rows).isEmpty(); // Verifies no placeholder row was created
    }

    @Test
    void testDuplicatePolicyEventIsIdempotent() {
        UUID policyId = UUID.randomUUID();
        
        PolicyCreatedEvent event = PolicyCreatedEvent.create(
                policyId, 1L, "POL-001", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );

        // First execution
        persistenceAdapter.savePolicyCreatedEvent(event);
        Map<String, Object> row1 = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        
        // Second identical execution
        persistenceAdapter.savePolicyCreatedEvent(event);
        Map<String, Object> row2 = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        
        assertThat(row1).isEqualTo(row2); // State remains completely unchanged
    }

    @Test
    void testDuplicateCommissionEventIsIdempotent() {
        UUID policyId = UUID.randomUUID();
        createPolicy(policyId, "ACTIVE", new BigDecimal("1000.00"), 1L);

        CommissionConfiguredEvent event = CommissionConfiguredEvent.create(
                policyId, 1L, "CONFIGURED", "PERCENTAGE", new BigDecimal("150.00"), new BigDecimal("100.00"), new BigDecimal("50.00")
        );

        // First execution
        persistenceAdapter.saveCommissionConfiguredEvent(event);
        Map<String, Object> row1 = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        
        // Second identical execution
        persistenceAdapter.saveCommissionConfiguredEvent(event);
        Map<String, Object> row2 = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        
        assertThat(row1).isEqualTo(row2); // State remains completely unchanged
    }
    private void createPolicy(UUID policyId, String status, BigDecimal premium, Long version, UUID customerId, UUID agentAId, UUID branchId) {
        PolicyCreatedEvent event = PolicyCreatedEvent.create(
                policyId, version, "POL-" + policyId.toString().substring(0, 5), customerId, agentAId, null, branchId, status, premium
        );
        persistenceAdapter.savePolicyCreatedEvent(event);
    }

    private void assertStatus(UUID policyId, String expectedStatus, Long expectedVersion) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(row.get("status")).isEqualTo(expectedStatus);
        assertThat(((Number) row.get("policy_aggregate_version")).longValue()).isEqualTo(expectedVersion);
    }
}
