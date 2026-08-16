package com.anverraglobal.policy.adapter.inbound.web;

import com.anverraglobal.policy.adapter.outbound.persistence.PolicyEntity;
import com.anverraglobal.policy.adapter.outbound.persistence.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class PolicyApiTest {

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

    @org.springframework.boot.test.mock.mockito.SpyBean
    private PolicyRepository policyRepository;

    @org.springframework.boot.test.mock.mockito.SpyBean
    private com.anverraglobal.commission.adapter.outbound.persistence.CommissionRepository commissionRepository;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.anverraglobal.organization.contracts.OrganizationScopeResolutionService scopeResolutionService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.anverraglobal.customer.contracts.CustomerVerificationContract customerVerificationContract;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.anverraglobal.insurer.contracts.InsurerVerificationContract insurerVerificationContract;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.anverraglobal.product.contracts.ProductVerificationContract productVerificationContract;


    private final UUID testIdentityId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final UUID testCustomerId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final UUID testBranchId = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @BeforeEach
    void setUp() {
        policyRepository.deleteAll();
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new com.anverraglobal.organization.contracts.dto.OrganizationScope(testIdentityId, null, null, null, true, false));
        org.mockito.Mockito.doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
        org.mockito.Mockito.doNothing().when(insurerVerificationContract).verifyInsurerActive(org.mockito.ArgumentMatchers.any());
        org.mockito.Mockito.doNothing().when(productVerificationContract).verifyProductActive(org.mockito.ArgumentMatchers.any());
    }

    private PolicyEntity insertTestPolicy(String number, String status) {
        PolicyEntity entity = new PolicyEntity();
        entity.setId(UUID.randomUUID());
        entity.setPolicyNumber(number);
        entity.setCreatedBy(testIdentityId);
        entity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setCustomerId(testCustomerId);
        entity.setBranchId(testBranchId);
        entity.setPremium(new BigDecimal("1000.0000"));
        entity.setStatus(status);
        entity.setVersion(null);
        return policyRepository.save(entity);
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldCreatePolicy() throws Exception {
        String requestBody = """
                {
                    "policyNumber": "POL-CREATE",
                    "customerId": "22222222-2222-2222-2222-222222222222",
                    "insurerId": "55555555-5555-5555-5555-555555555555",
                    "productId": "77777777-7777-7777-7777-777777777777",
                    "agentAId": null,
                    "agentBId": null,
                    "branchId": "44444444-4444-4444-4444-444444444444"
                }
                """;

        mockMvc.perform(post("/api/v1/policies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.policyNumber").value("POL-CREATE"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldGetPolicy() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-GET", "DRAFT");

        mockMvc.perform(get("/api/v1/policies/" + policy.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNumber").value("POL-GET"));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldListPolicies() throws Exception {
        insertTestPolicy("POL-LIST-1", "DRAFT");
        insertTestPolicy("POL-LIST-2", "DRAFT");

        mockMvc.perform(get("/api/v1/policies?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldUpdatePolicy() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-UPDATE", "DRAFT");

        String requestBody = """
                {
                    "customerId": "33333333-3333-3333-3333-333333333333",
                    "insurerId": "66666666-6666-6666-6666-666666666666"
                }
                """;

        mockMvc.perform(patch("/api/v1/policies/" + policy.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("33333333-3333-3333-3333-333333333333"))
                .andExpect(jsonPath("$.insurerId").value("66666666-6666-6666-6666-666666666666"));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldUpdatePremium() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-PREM", "DRAFT");
        
        com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity commission = new com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity();
        commission.setPolicyId(policy.getId());
        commission.setStatus("CONFIGURED");
        commissionRepository.save(commission);

        String requestBody = """
                {
                    "premium": 1500.0000
                }
                """;

        mockMvc.perform(patch("/api/v1/policies/" + policy.getId() + "/premium")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldResolvePolicy() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-RESOLVE", "DRAFT");

        mockMvc.perform(post("/api/v1/policies/resolve")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"policyNumber\": \"POL-RESOLVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyId").value(policy.getId().toString()));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldActivatePolicy() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-ACT", "DRAFT");
        policy.setInsurerId(UUID.randomUUID());
        policy.setProductId(UUID.randomUUID());
        policy.setEffectiveDate(java.time.LocalDate.of(2025, 1, 1));
        policy.setExpiryDate(java.time.LocalDate.of(2026, 1, 1));
        policy.setSumAssured(new BigDecimal("50000.00"));
        policyRepository.save(policy);

        mockMvc.perform(post("/api/v1/policies/" + policy.getId() + "/lifecycle/activate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commissionConfigured\": true}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldDeactivatePolicy() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-DEACT", "ACTIVE");

        mockMvc.perform(post("/api/v1/policies/" + policy.getId() + "/lifecycle/deactivate")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReactivatePolicy() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-REACT", "INACTIVE");
        policy.setInsurerId(UUID.randomUUID());
        policy.setProductId(UUID.randomUUID());
        policy.setEffectiveDate(java.time.LocalDate.of(2025, 1, 1));
        policy.setExpiryDate(java.time.LocalDate.of(2026, 1, 1));
        policy.setSumAssured(new BigDecimal("50000.00"));
        policyRepository.save(policy);

        mockMvc.perform(post("/api/v1/policies/" + policy.getId() + "/lifecycle/reactivate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commissionConfigured\": true}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn404ForUnknownPolicy() throws Exception {
        mockMvc.perform(get("/api/v1/policies/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn422ForInvalidStatusTransition() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-ERR", "DRAFT");

        mockMvc.perform(post("/api/v1/policies/" + policy.getId() + "/lifecycle/deactivate")
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    void shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/policies"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "USER")
    void shouldReturn403WhenOutOfScope() throws Exception {
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(com.anverraglobal.organization.contracts.dto.OrganizationScope.forCustomer(UUID.randomUUID(), UUID.randomUUID()));

        PolicyEntity policy = insertTestPolicy("POL-403", "DRAFT");

        mockMvc.perform(get("/api/v1/policies/" + policy.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn405ForDelete() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-DEL", "DRAFT");

        mockMvc.perform(delete("/api/v1/policies/" + policy.getId())
                        .with(csrf()))
                .andExpect(status().isMethodNotAllowed()); // Or 404 depending on mappings, usually 405 if endpoint not mapped but resource matches /api/v1/policies/{id}
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn400ForBadRequest() throws Exception {
        String requestBody = """
                {
                    "policyNumber": "POL-BAD",
                    "customerId": "invalid-uuid"
                }
                """;

        mockMvc.perform(post("/api/v1/policies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn409ForOptimisticLockingConflict() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-409", "DRAFT");

        // Simulate an optimistic locking conflict by throwing the exception when save is called
        org.mockito.Mockito.doThrow(new org.springframework.dao.OptimisticLockingFailureException("Mocked conflict"))
                .when(policyRepository).save(org.mockito.ArgumentMatchers.any(PolicyEntity.class));

        String requestBody = """
                {
                    "premium": 2000.0000
                }
                """;

        mockMvc.perform(patch("/api/v1/policies/" + policy.getId() + "/premium")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldConfigureCommissionSuccessfully() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-COMM-1", "DRAFT");
        
        // Initial UNSET commission is created automatically or we assume it's created.
        // Actually, we need to create it manually for the test because Policy create doesn't do it directly in this test setup unless we go through the full app service flow.
        // Wait, did insertTestPolicy create it? No. Let's create an UNSET commission.
        com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity commission = new com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity();
        commission.setPolicyId(policy.getId());
        commission.setStatus("UNSET");
        commissionRepository.save(commission);

        String requestBody = """
                {
                    "commissionType": "FIXED",
                    "totalCommissionValue": 200.00,
                    "agentAShare": 150.00,
                    "agentBShare": 50.00
                }
                """;

        mockMvc.perform(put("/api/v1/policies/" + policy.getId() + "/commission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        var updatedComm = commissionRepository.findById(policy.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("CONFIGURED", updatedComm.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals("FIXED", updatedComm.getType());
        org.junit.jupiter.api.Assertions.assertEquals(0, new BigDecimal("200.00").compareTo(updatedComm.getTotalCommissionValue()));

        // Verify Event
        Integer eventCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE event_type LIKE '%CommissionConfiguredEvent%'", Integer.class);
        org.junit.jupiter.api.Assertions.assertTrue(eventCount > 0, "Event should be published");
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldConfigureCommissionToZeroSuccessfully() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-COMM-ZERO", "DRAFT");
        
        com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity commission = new com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity();
        commission.setPolicyId(policy.getId());
        commission.setStatus("UNSET");
        commissionRepository.save(commission);

        String requestBody = """
                {
                    "commissionType": "FIXED",
                    "totalCommissionValue": 0.00,
                    "agentAShare": 0.00,
                    "agentBShare": 0.00
                }
                """;

        mockMvc.perform(put("/api/v1/policies/" + policy.getId() + "/commission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        var updatedComm = commissionRepository.findById(policy.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("CONFIGURED", updatedComm.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals(0, new BigDecimal("0.00").compareTo(updatedComm.getTotalCommissionValue()));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn422WhenCommissionExceedsLimit() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-COMM-ERR1", "DRAFT");
        
        com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity commission = new com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity();
        commission.setPolicyId(policy.getId());
        commission.setStatus("UNSET");
        commissionRepository.save(commission);

        // Premium is 1000.0000, 50% is 500
        String requestBody = """
                {
                    "commissionType": "FIXED",
                    "totalCommissionValue": 600.00,
                    "agentAShare": 300.00,
                    "agentBShare": 300.00
                }
                """;

        mockMvc.perform(put("/api/v1/policies/" + policy.getId() + "/commission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn422WhenAgentSharesDoNotMatch() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-COMM-ERR2", "DRAFT");
        
        com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity commission = new com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity();
        commission.setPolicyId(policy.getId());
        commission.setStatus("UNSET");
        commissionRepository.save(commission);

        String requestBody = """
                {
                    "commissionType": "FIXED",
                    "totalCommissionValue": 200.00,
                    "agentAShare": 150.00,
                    "agentBShare": 100.00
                }
                """;

        mockMvc.perform(put("/api/v1/policies/" + policy.getId() + "/commission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "USER")
    void shouldReturn403WhenConfiguringOutOfScope() throws Exception {
        org.mockito.Mockito.when(scopeResolutionService.resolveScope(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(com.anverraglobal.organization.contracts.dto.OrganizationScope.forCustomer(UUID.randomUUID(), UUID.randomUUID()));

        PolicyEntity policy = insertTestPolicy("POL-COMM-403", "DRAFT");

        String requestBody = """
                {
                    "commissionType": "FIXED",
                    "totalCommissionValue": 200.00,
                    "agentAShare": 100.00,
                    "agentBShare": 100.00
                }
                """;

        mockMvc.perform(put("/api/v1/policies/" + policy.getId() + "/commission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", roles = "ADMIN")
    void shouldReturn409ForOptimisticLockingConflictOnCommission() throws Exception {
        PolicyEntity policy = insertTestPolicy("POL-COMM-409", "DRAFT");
        
        com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity commission = new com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity();
        commission.setPolicyId(policy.getId());
        commission.setStatus("UNSET");
        commissionRepository.save(commission);

        // Simulate optimistic locking by throwing from the repository when saving CommissionEntity
        org.mockito.Mockito.doThrow(new org.springframework.dao.OptimisticLockingFailureException("Mocked conflict"))
                .when(commissionRepository).save(org.mockito.ArgumentMatchers.any());

        String requestBody = """
                {
                    "commissionType": "FIXED",
                    "totalCommissionValue": 200.00,
                    "agentAShare": 150.00,
                    "agentBShare": 50.00
                }
                """;

        mockMvc.perform(put("/api/v1/policies/" + policy.getId() + "/commission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
