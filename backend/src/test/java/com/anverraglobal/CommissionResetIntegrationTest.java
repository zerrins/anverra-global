package com.anverraglobal;

import com.anverraglobal.commission.application.port.outbound.CommissionRepositoryPort;
import com.anverraglobal.commission.contracts.CommissionManagementService;
import com.anverraglobal.commission.domain.Commission;
import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RecordApplicationEvents
class CommissionResetIntegrationTest {

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
    private CommissionManagementService commissionManagementService;

    @Autowired
    private CommissionRepositoryPort commissionRepositoryPort;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    void testResetMissingCommission_IsNoOp() {
        UUID policyId = UUID.randomUUID();
        applicationEvents.clear();

        commissionManagementService.resetToUnset(policyId);

        // Ensure no commission was physically created
        assertThat(commissionRepositoryPort.findById(policyId)).isEmpty();

        // Ensure no event was published
        long eventsPublished = applicationEvents.stream(CommissionConfiguredEvent.class)
                .filter(e -> e.aggregateId().equals(policyId))
                .count();
        assertThat(eventsPublished).isEqualTo(0L);
    }

    @Test
    void testResetConfiguredCommission_PublishesEventAndResets() {
        UUID policyId = UUID.randomUUID();
        
        // Setup initial configuration
        commissionManagementService.configureCommission(policyId, "FIXED", new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("50"), new BigDecimal("1000"));
        
        applicationEvents.clear();

        // Perform reset
        commissionManagementService.resetToUnset(policyId);

        // Verify commission state
        Commission commission = commissionRepositoryPort.findById(policyId).orElseThrow();
        assertThat(commission.getStatus().name()).isEqualTo("UNSET");
        assertThat(commission.getTotalCommissionValue()).isNull();

        // Verify event published
        long eventsPublished = applicationEvents.stream(CommissionConfiguredEvent.class)
                .filter(e -> e.aggregateId().equals(policyId) && e.commissionStatus().equals("UNSET"))
                .count();
        assertThat(eventsPublished).isEqualTo(1L);
    }
}
