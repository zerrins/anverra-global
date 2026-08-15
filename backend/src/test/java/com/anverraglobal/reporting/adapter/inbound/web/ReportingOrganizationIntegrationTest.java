package com.anverraglobal.reporting.adapter.inbound.web;

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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ReportingOrganizationIntegrationTest {

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
        registry.add("spring.modulith.events.jdbc.schema-initialization.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final UUID agentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private final UUID dataEntryUserId = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reporting_policy_read_models");
        jdbcTemplate.update("DELETE FROM organization_memberships");

        // Insert agent membership
        jdbcTemplate.update(
                "INSERT INTO organization_memberships (id, identity_id, role, version) VALUES (?, ?, 'AGENT', 0)",
                UUID.randomUUID(), agentId
        );

        // Insert data entry membership under agent
        jdbcTemplate.update(
                "INSERT INTO organization_memberships (id, identity_id, role, parent_identity_id, version) VALUES (?, ?, 'DATA_ENTRY', ?, 0)",
                UUID.randomUUID(), dataEntryUserId, agentId
        );

        // Insert some reporting data for the agent
        jdbcTemplate.update(
                "INSERT INTO reporting_policy_read_models (policy_id, policy_number, customer_id, agent_a_id, branch_id, premium, status) " +
                "VALUES (?, 'POL-1', ?, ?, ?, ?, 'ACTIVE')",
                UUID.randomUUID(), UUID.randomUUID(), agentId, UUID.randomUUID(), new BigDecimal("1000.00")
        );
    }

    @Test
    @WithMockUser(username = "dddddddd-dddd-dddd-dddd-dddddddddddd", roles = "USER")
    void testDataEntryPolicyStatistics_Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dddddddd-dddd-dddd-dddd-dddddddddddd", roles = "USER")
    void testDataEntryCommissionStatistics_Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/reporting/commissions/statistics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", roles = "AGENT")
    void testAgentPolicyStatistics_Ok() throws Exception {
        mockMvc.perform(get("/api/v1/reporting/policies/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPolicies").value(1));
    }
}
