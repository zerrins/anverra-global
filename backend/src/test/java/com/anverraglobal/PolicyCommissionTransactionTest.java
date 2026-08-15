package com.anverraglobal;

import com.anverraglobal.commission.adapter.outbound.persistence.CommissionEntity;
import com.anverraglobal.commission.adapter.outbound.persistence.CommissionRepository;
import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.policy.adapter.outbound.persistence.PolicyEntity;
import com.anverraglobal.policy.adapter.outbound.persistence.PolicyRepository;
import com.anverraglobal.policy.application.PolicyManagementApplicationService;
import com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RecordApplicationEvents
class PolicyCommissionTransactionTest {

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
    private PolicyManagementApplicationService policyManagementApplicationService;

    @Autowired
    private PolicyRepository policyRepository;

    @SpyBean
    private CommissionRepository commissionRepository;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.anverraglobal.organization.contracts.OrganizationScopeResolutionService scopeResolutionService;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testSuccessfulTransaction_PremiumUpdateAndCommissionReset() {
        jdbcTemplate.execute("DELETE FROM event_publication");
        // Setup initial state
        UUID policyId = UUID.randomUUID();
        
        PolicyEntity policyEntity = new PolicyEntity();
        policyEntity.setId(policyId);
        policyEntity.setPolicyNumber("TX-123");
        policyEntity.setCreatedBy(UUID.randomUUID());
        policyEntity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        policyEntity.setCustomerId(UUID.randomUUID());
        policyEntity.setPremium(new BigDecimal("1000.0000"));
        policyEntity.setStatus("DRAFT");
        policyEntity.setVersion(null);
        policyRepository.save(policyEntity);

        CommissionEntity commissionEntity = new CommissionEntity();
        commissionEntity.setPolicyId(policyId);
        commissionEntity.setStatus("CONFIGURED");
        commissionEntity.setType("FIXED");
        commissionEntity.setTotalCommissionValue(new BigDecimal("500.0000"));
        commissionEntity.setAgentAShare(new BigDecimal("250.0000"));
        commissionEntity.setAgentBShare(new BigDecimal("250.0000"));
        commissionEntity.setVersion(null);
        commissionRepository.save(commissionEntity);

        applicationEvents.clear();

        // Execute Transaction
        policyManagementApplicationService.updatePremium(policyId, new BigDecimal("1500.0000"));

        // Verify Policy was updated
        PolicyEntity updatedPolicy = policyRepository.findById(policyId).orElseThrow();
        assertThat(updatedPolicy.getPremium()).isEqualTo(new BigDecimal("1500.0000"));

        // Verify Commission was reset
        CommissionEntity updatedCommission = commissionRepository.findById(policyId).orElseThrow();
        assertThat(updatedCommission.getStatus()).isEqualTo("UNSET");
        assertThat(updatedCommission.getType()).isNull();
        assertThat(updatedCommission.getTotalCommissionValue()).isNull();

        // Verify ApplicationEvents captured them in memory
        long policyEventsCount = applicationEvents.stream(PolicyPremiumUpdatedEvent.class)
                .filter(e -> e.aggregateId().equals(policyId)).count();
        long commissionEventsCount = applicationEvents.stream(CommissionConfiguredEvent.class)
                .filter(e -> e.aggregateId().equals(policyId) && "UNSET".equals(e.commissionStatus())).count();

        assertThat(policyEventsCount).isEqualTo(1);
        assertThat(commissionEventsCount).isEqualTo(1);

        // Verify EVENT_PUBLICATION table for physical event persistence
        long policyDbEvents = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM event_publication WHERE event_type = ?", Long.class, 
                PolicyPremiumUpdatedEvent.class.getName());
        long commissionDbEvents = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM event_publication WHERE event_type = ?", Long.class, 
                CommissionConfiguredEvent.class.getName());

        assertThat(policyDbEvents).isEqualTo(1);
        assertThat(commissionDbEvents).isEqualTo(1);
    }

    @Test
    void testFailedTransaction_Rollback() {
        jdbcTemplate.execute("DELETE FROM event_publication");
        // Setup initial state
        UUID policyId = UUID.randomUUID();
        
        PolicyEntity policyEntity = new PolicyEntity();
        policyEntity.setId(policyId);
        policyEntity.setPolicyNumber("TX-FAIL");
        policyEntity.setCreatedBy(UUID.randomUUID());
        policyEntity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        policyEntity.setCustomerId(UUID.randomUUID());
        policyEntity.setPremium(new BigDecimal("1000.0000"));
        policyEntity.setStatus("DRAFT");
        policyEntity.setVersion(null);
        policyRepository.save(policyEntity);

        CommissionEntity commissionEntity = new CommissionEntity();
        commissionEntity.setPolicyId(policyId);
        commissionEntity.setStatus("CONFIGURED");
        commissionEntity.setType("FIXED");
        commissionEntity.setTotalCommissionValue(new BigDecimal("500.0000"));
        commissionEntity.setAgentAShare(new BigDecimal("250.0000"));
        commissionEntity.setAgentBShare(new BigDecimal("250.0000"));
        commissionEntity.setVersion(null);
        commissionRepository.save(commissionEntity);

        applicationEvents.clear();

        // Simulate a failure in Commission module persistence to trigger rollback
        doThrow(new RuntimeException("Simulated Database Failure")).when(commissionRepository).save(any(CommissionEntity.class));

        // Execute Transaction
        assertThatThrownBy(() -> policyManagementApplicationService.updatePremium(policyId, new BigDecimal("1500.0000")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Simulated Database Failure");

        // Verify Policy was NOT updated
        PolicyEntity unchangedPolicy = policyRepository.findById(policyId).orElseThrow();
        assertThat(unchangedPolicy.getPremium()).isEqualTo(new BigDecimal("1000.0000"));

        // Verify Commission was NOT reset
        CommissionEntity unchangedCommission = commissionRepository.findById(policyId).orElseThrow();
        assertThat(unchangedCommission.getStatus()).isEqualTo("CONFIGURED");
        assertThat(unchangedCommission.getTotalCommissionValue()).isEqualTo(new BigDecimal("500.0000"));

        // Verify EVENT_PUBLICATION table has NO records due to transaction rollback
        long policyDbEvents = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM event_publication WHERE event_type = ?", Long.class, 
                PolicyPremiumUpdatedEvent.class.getName());
        long commissionDbEvents = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM event_publication WHERE event_type = ?", Long.class, 
                CommissionConfiguredEvent.class.getName());

        assertThat(policyDbEvents).isEqualTo(0);
        assertThat(commissionDbEvents).isEqualTo(0);
    }
}
