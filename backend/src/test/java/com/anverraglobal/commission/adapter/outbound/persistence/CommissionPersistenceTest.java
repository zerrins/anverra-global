package com.anverraglobal.commission.adapter.outbound.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CommissionPersistenceTest {

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
    private CommissionRepository commissionRepository;

    @Test
    void testSaveAndLoadCommission_UNSET() {
        CommissionEntity entity = new CommissionEntity();
        UUID policyId = UUID.randomUUID();
        entity.setPolicyId(policyId);
        entity.setStatus("UNSET");
        entity.setType(null);
        entity.setTotalCommissionValue(null);
        entity.setAgentAShare(null);
        entity.setAgentBShare(null);
        entity.setVersion(null);

        CommissionEntity saved = commissionRepository.save(entity);
        assertThat(saved.getVersion()).isNotNull();

        CommissionEntity loaded = commissionRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getStatus()).isEqualTo("UNSET");
        assertThat(loaded.getType()).isNull();
        assertThat(loaded.getTotalCommissionValue()).isNull();
    }

    @Test
    void testSaveAndLoadCommission_ZERO() {
        CommissionEntity entity = new CommissionEntity();
        UUID policyId = UUID.randomUUID();
        entity.setPolicyId(policyId);
        entity.setStatus("CONFIGURED");
        entity.setType("FIXED");
        entity.setTotalCommissionValue(BigDecimal.ZERO);
        entity.setAgentAShare(BigDecimal.ZERO);
        entity.setAgentBShare(BigDecimal.ZERO);
        entity.setVersion(null);

        CommissionEntity saved = commissionRepository.save(entity);

        CommissionEntity loaded = commissionRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getStatus()).isEqualTo("CONFIGURED");
        assertThat(loaded.getType()).isEqualTo("FIXED");
        // Verify ZERO persists and reloads as ZERO, not null
        assertThat(loaded.getTotalCommissionValue()).isEqualByComparingTo(BigDecimal.ZERO);
    }
    @Test
    void testOptimisticLocking() {
        CommissionEntity entity = new CommissionEntity();
        UUID policyId = UUID.randomUUID();
        entity.setPolicyId(policyId);
        entity.setStatus("UNSET");
        entity.setType(null);
        entity.setTotalCommissionValue(null);
        entity.setAgentAShare(null);
        entity.setAgentBShare(null);
        entity.setVersion(null);

        commissionRepository.save(entity);

        // Load two independent instances representing the same state
        CommissionEntity instance1 = commissionRepository.findById(policyId).orElseThrow();
        CommissionEntity instance2 = commissionRepository.findById(policyId).orElseThrow();

        // Update and save first instance successfully
        instance1.setStatus("CONFIGURED");
        instance1.setType("FIXED");
        instance1.setTotalCommissionValue(new BigDecimal("100.00"));
        commissionRepository.save(instance1);

        // Attempt to update and save the stale second instance
        instance2.setStatus("CONFIGURED");
        instance2.setType("PERCENTAGE");
        instance2.setTotalCommissionValue(new BigDecimal("50.00"));

        Exception captured = null;
        try {
            commissionRepository.save(instance2);
        } catch (Exception e) {
            captured = e;
        }

        System.out.println("--- OPTIMISTIC LOCKING EXCEPTION ---");
        System.out.println(captured.getClass().getName());
        if (captured.getCause() != null) {
            System.out.println("Cause: " + captured.getCause().getClass().getName());
        }

        assertThat(captured).isNotNull();
        boolean isOptimisticLocking = captured instanceof org.springframework.dao.OptimisticLockingFailureException ||
                (captured.getCause() != null && captured.getCause() instanceof org.springframework.dao.OptimisticLockingFailureException);
        assertThat(isOptimisticLocking).isTrue();

        // Verify that the database retains the first successful update
        CommissionEntity loaded = commissionRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getStatus()).isEqualTo("CONFIGURED");
        assertThat(loaded.getType()).isEqualTo("FIXED");
        assertThat(loaded.getTotalCommissionValue()).isEqualByComparingTo(new BigDecimal("100.00"));
    }
}
