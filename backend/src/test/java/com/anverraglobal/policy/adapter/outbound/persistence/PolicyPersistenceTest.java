package com.anverraglobal.policy.adapter.outbound.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class PolicyPersistenceTest {

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
    private PolicyRepository policyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testSaveAndLoadPolicy() {
        PolicyEntity entity = new PolicyEntity();
        UUID policyId = UUID.randomUUID();
        entity.setId(policyId);
        entity.setPolicyNumber("POL-12345");
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setCustomerId(UUID.randomUUID());
        entity.setPremium(new BigDecimal("1234.5600"));
        entity.setStatus("DRAFT");
        // For Spring Data JDBC, if version is Long, new entities should have version null. 
        // We ensure version is null initially.
        entity.setVersion(null);

        PolicyEntity saved = policyRepository.save(entity);
        
        assertThat(saved.getVersion()).isNotNull();

        PolicyEntity loaded = policyRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getPolicyNumber()).isEqualTo("POL-12345");
        assertThat(loaded.getPremium()).isEqualTo(new BigDecimal("1234.5600"));
        assertThat(loaded.getStatus()).isEqualTo("DRAFT");
        assertThat(loaded.getVersion()).isEqualTo(saved.getVersion());
        assertThat(loaded.getInsurerId()).isNull();
    }

    @Test
    void testSaveAndLoadPolicyWithInsurer() {
        PolicyEntity entity = new PolicyEntity();
        UUID policyId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        entity.setId(policyId);
        entity.setPolicyNumber("POL-INSURER");
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setCustomerId(UUID.randomUUID());
        entity.setInsurerId(insurerId);
        entity.setPremium(new BigDecimal("2345.6700"));
        entity.setStatus("ACTIVE");
        entity.setVersion(null);

        PolicyEntity saved = policyRepository.save(entity);
        
        assertThat(saved.getVersion()).isNotNull();

        PolicyEntity loaded = policyRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getPolicyNumber()).isEqualTo("POL-INSURER");
        assertThat(loaded.getInsurerId()).isEqualTo(insurerId);
    }

    @Test
    void testSaveAndLoadLegacyPolicyWithNullCustomer() {
        PolicyEntity entity = new PolicyEntity();
        UUID policyId = UUID.randomUUID();
        entity.setId(policyId);
        entity.setPolicyNumber("POL-LEGACY-NULL");
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setCustomerId(null); // legacy policy
        entity.setPremium(new BigDecimal("500.0000"));
        entity.setStatus("DRAFT");
        entity.setVersion(null);

        PolicyEntity saved = policyRepository.save(entity);
        assertThat(saved.getVersion()).isNotNull();

        PolicyEntity loaded = policyRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getCustomerId()).isNull();
        assertThat(loaded.getInsurerId()).isNull();
        assertThat(loaded.getPolicyNumber()).isEqualTo("POL-LEGACY-NULL");
    }

    @Test
    void testOptimisticLocking() {
        PolicyEntity entity = new PolicyEntity();
        UUID policyId = UUID.randomUUID();
        entity.setId(policyId);
        entity.setPolicyNumber("POL-OPT-" + UUID.randomUUID().toString().substring(0, 8));
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setCustomerId(UUID.randomUUID());
        entity.setPremium(new BigDecimal("100.0000"));
        entity.setStatus("DRAFT");
        entity.setVersion(null);

        policyRepository.save(entity);

        // Load two independent instances representing the same state
        PolicyEntity instance1 = policyRepository.findById(policyId).orElseThrow();
        PolicyEntity instance2 = policyRepository.findById(policyId).orElseThrow();

        // Update and save first instance successfully
        instance1.setStatus("ACTIVE");
        policyRepository.save(instance1);

        // Attempt to update and save the stale second instance
        instance2.setStatus("CANCELED");
        
        Exception captured = null;
        try {
            policyRepository.save(instance2);
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
        PolicyEntity loaded = policyRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getStatus()).isEqualTo("ACTIVE");
    }
}
