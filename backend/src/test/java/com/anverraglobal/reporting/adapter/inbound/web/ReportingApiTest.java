package com.anverraglobal.reporting.adapter.inbound.web;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ReportingApiTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.modulith.events.jdbc.schema-initialization.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.anverraglobal.organization.contracts.OrganizationScopeResolutionService scopeResolutionService;

    private final UUID globalAdminId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final UUID customerId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final UUID agentId = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private final UUID branchId = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reporting_policy_read_models");
        
        // Insert sample data
        insertPolicy(UUID.randomUUID(), "DRAFT", customerId, agentId, null, branchId);
        insertPolicy(UUID.randomUUID(), "ACTIVE", customerId, agentId, null, branchId);
        insertPolicy(UUID.randomUUID(), "INACTIVE", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID());
        insertPolicy(UUID.randomUUID(), "ACTIVE", UUID.randomUUID(), agentId, null, UUID.randomUUID());
        insertPolicy(UUID.randomUUID(), "DRAFT", UUID.randomUUID(), UUID.randomUUID(), null, branchId);
        // Agent B specific record
        insertPolicy(UUID.randomUUID(), "ACTIVE", UUID.randomUUID(), UUID.randomUUID(), agentId, UUID.randomUUID());
    }

    private void insertPolicy(UUID policyId, String status, UUID customerId, UUID agentAId, UUID agentBId, UUID branchId) {
        jdbcTemplate.update(
                "INSERT INTO reporting_policy_read_models (policy_id, policy_number, customer_id, agent_a_id, agent_b_id, branch_id, status, premium, policy_aggregate_version, commission_aggregate_version) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                policyId, "POL-" + policyId.toString().substring(0, 5), customerId, agentAId, agentBId, branchId, status, new BigDecimal("1000.00"), 1L, 0L
        );
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void testGlobalAdminScope() throws Exception {
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(globalAdminId, "ROLE_ADMIN"))
                .thenReturn(new OrganizationScope(globalAdminId, null, null, null, true, false));

        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPolicies").value(6))
                .andExpect(jsonPath("$.draftCount").value(2))
                .andExpect(jsonPath("$.activeCount").value(3))
                .andExpect(jsonPath("$.inactiveCount").value(1));
    }

    @Test
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222", roles = "CUSTOMER")
    void testCustomerScope() throws Exception {
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(customerId, "ROLE_CUSTOMER"))
                .thenReturn(OrganizationScope.forCustomer(customerId, customerId));

        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPolicies").value(2))
                .andExpect(jsonPath("$.draftCount").value(1))
                .andExpect(jsonPath("$.activeCount").value(1))
                .andExpect(jsonPath("$.inactiveCount").value(0));
    }

    @Test
    @WithMockUser(username = "33333333-3333-3333-3333-333333333333", roles = "AGENT")
    void testAgentScope() throws Exception {
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(agentId, "ROLE_AGENT"))
                .thenReturn(OrganizationScope.forAgent(agentId, agentId));

        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPolicies").value(4))
                .andExpect(jsonPath("$.draftCount").value(1))
                .andExpect(jsonPath("$.activeCount").value(3))
                .andExpect(jsonPath("$.inactiveCount").value(0));
    }

    @Test
    @WithMockUser(username = "44444444-4444-4444-4444-444444444444", roles = "BRANCH_ADMIN")
    void testBranchScope() throws Exception {
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(branchId, "ROLE_BRANCH_ADMIN"))
                .thenReturn(OrganizationScope.forBranchAdmin(branchId, branchId));

        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPolicies").value(3))
                .andExpect(jsonPath("$.draftCount").value(2))
                .andExpect(jsonPath("$.activeCount").value(1))
                .andExpect(jsonPath("$.inactiveCount").value(0));
    }

    @Test
    @WithMockUser(username = "55555555-5555-5555-5555-555555555555", roles = "USER")
    void testEmptyAuthorizedScope() throws Exception {
        UUID emptyUserId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(emptyUserId, "ROLE_USER"))
                .thenReturn(OrganizationScope.empty(emptyUserId));

        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPolicies").value(0))
                .andExpect(jsonPath("$.draftCount").value(0))
                .andExpect(jsonPath("$.activeCount").value(0))
                .andExpect(jsonPath("$.inactiveCount").value(0));
    }

    @Test
    void testUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "66666666-6666-6666-6666-666666666666", roles = "USER")
    void testDataEntryForbiddenOnPolicyStatistics() throws Exception {
        UUID dataEntryUserId = UUID.fromString("66666666-6666-6666-6666-666666666666");
        // Data Entry inherits parent Agent scope — the inherited IDs are present but isDataEntry=true
        // The Organization module sets this flag authoritatively; no role string is inspected.
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(dataEntryUserId, "ROLE_USER"))
                .thenReturn(new OrganizationScope(dataEntryUserId, null, Set.of(agentId), null, false, true));

        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "66666666-6666-6666-6666-666666666666", roles = "USER")
    void testDataEntryForbiddenOnCommissionStatistics() throws Exception {
        UUID dataEntryUserId = UUID.fromString("66666666-6666-6666-6666-666666666666");
        // Same Data Entry scope — isDataEntry=true blocks ALL Reporting Statistics regardless of inherited scope.
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(dataEntryUserId, "ROLE_USER"))
                .thenReturn(new OrganizationScope(dataEntryUserId, null, Set.of(agentId), null, false, true));

        mockMvc.perform(get("/api/v1/reporting/commissions/statistics"))
                .andExpect(status().isForbidden());
    }
}
