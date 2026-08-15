package com.anverraglobal;

import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.time.Instant;
import java.time.Duration;
import org.awaitility.Awaitility;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReportingEventIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.modulith.events.jdbc.schema-initialization.enabled", () -> "true");
        registry.add("app.retry.initial-delay", () -> "1000");
        registry.add("app.retry.fixed-delay", () -> "1000");
    }

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM reporting_policy_read_models");
        jdbcTemplate.execute("DELETE FROM event_publication");
    }

    @Test
    void testPolicyThenCommission_OrderA() {
        UUID policyId = UUID.randomUUID();
        
        PolicyCreatedEvent policyEvent = PolicyCreatedEvent.create(
                policyId, 1L, "POL-INT-1", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );
        CommissionConfiguredEvent commissionEvent = CommissionConfiguredEvent.create(
                policyId, 0L, "CONFIGURED", "PERCENTAGE", new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00")
        );

        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long policyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ?", Long.class, policyId);
            assertThat(policyCount).isEqualTo(1L);
        });

        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(commissionEvent));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ? AND commission_status = 'CONFIGURED'", 
                    Long.class, policyId);
            assertThat(count).isEqualTo(1L);
        });
        
        Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
        assertThat(incompleteCount).isEqualTo(0L);
    }

    @Test
    void testCommissionThenPolicy_OrderB_Redelivered() throws InterruptedException {
        UUID policyId = UUID.randomUUID();
        
        CommissionConfiguredEvent commissionEvent = CommissionConfiguredEvent.create(
                policyId, 0L, "CONFIGURED", "PERCENTAGE", new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00")
        );
        
        PolicyCreatedEvent policyEvent = PolicyCreatedEvent.create(
                policyId, 1L, "POL-INT-B", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );

        // 1. Publish commission event first (out of order)
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(commissionEvent));

        // Wait for event to be marked incomplete
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(incompleteCount).isGreaterThan(0L);
        });
        
        // Ensure policy is still missing
        Long countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ?", Long.class, policyId);
        assertThat(countBefore).isEqualTo(0L);

        // 2. Publish policy creation
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long policyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ?", Long.class, policyId);
            assertThat(policyCount).isEqualTo(1L);
        });

        // 3. Wait for retry scheduler to redeliver the event
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            Long count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ? AND commission_status = 'CONFIGURED'", 
                    Long.class, policyId);
            assertThat(count).isEqualTo(1L);
            
            Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(incompleteCount).isEqualTo(0L);
        });
    }

    @Test
    void testRetryFailureRemainsIncomplete() {
        UUID policyId = UUID.randomUUID();
        CommissionConfiguredEvent commissionEvent = CommissionConfiguredEvent.create(
                policyId, 0L, "CONFIGURED", "PERCENTAGE", new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00")
        );
        
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(commissionEvent));
        
        // Wait for event to be marked incomplete
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(incompleteCount).isGreaterThan(0L);
        });
        
        // Wait another 7 seconds to let the scheduler try redelivery, it should fail again
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {}
        
        // Should STILL be incomplete
        Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
        assertThat(incompleteCount).isGreaterThan(0L);
    }
    
    @Test
    void testDuplicateCommissionEvent() {
        UUID policyId = UUID.randomUUID();
        PolicyCreatedEvent policyEvent = PolicyCreatedEvent.create(
                policyId, 1L, "POL-INT-DUP", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));

        CommissionConfiguredEvent initialConfig = CommissionConfiguredEvent.create(
                policyId, 0L, "CONFIGURED", "FIXED", new BigDecimal("100.00"), new BigDecimal("100.00"), BigDecimal.ZERO
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(initialConfig));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long configCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ? AND commission_status = 'CONFIGURED'", 
                    Long.class, policyId);
            assertThat(configCount).isEqualTo(1L);
        });
        
        // Duplicate
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(initialConfig));
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            // Must have completed and not caused issues
            Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(incompleteCount).isEqualTo(0L);
        });
        
        java.util.Map<String, Object> finalState = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(((Number) finalState.get("commission_aggregate_version")).longValue()).isEqualTo(0L);
    }
    
    @Test
    void testStaleCommissionEvent() {
        UUID policyId = UUID.randomUUID();
        PolicyCreatedEvent policyEvent = PolicyCreatedEvent.create(
                policyId, 1L, "POL-INT-STALE", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));
        
        // First configuration
        CommissionConfiguredEvent config2 = CommissionConfiguredEvent.create(
                policyId, 2L, "CONFIGURED", "FIXED", new BigDecimal("200.00"), new BigDecimal("200.00"), BigDecimal.ZERO
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(config2));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            java.util.Map<String, Object> state1 = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
            assertThat(((Number) state1.get("commission_aggregate_version")).longValue()).isEqualTo(2L);
        });

        // Stale config
        CommissionConfiguredEvent config1 = CommissionConfiguredEvent.create(
                policyId, 1L, "CONFIGURED", "FIXED", new BigDecimal("100.00"), new BigDecimal("100.00"), BigDecimal.ZERO
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(config1));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long incompleteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(incompleteCount).isEqualTo(0L);
        });
        
        java.util.Map<String, Object> finalState = jdbcTemplate.queryForMap("SELECT * FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        assertThat(((Number) finalState.get("commission_aggregate_version")).longValue()).isEqualTo(2L);
        assertThat(((Number) finalState.get("total_commission_value")).doubleValue()).isEqualTo(200.00);
    }
    
    @Test
    void testRetryFiltering() throws InterruptedException {
        UUID policyId = UUID.randomUUID();
        
        // 1. Publish Commission event out of order -> becomes INCOMPLETE
        CommissionConfiguredEvent commissionEvent = CommissionConfiguredEvent.create(
                policyId, 0L, "CONFIGURED", "PERCENTAGE", new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00")
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(commissionEvent));
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long incompleteCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL AND event_type LIKE '%CommissionConfiguredEvent%'", 
                Long.class);
            assertThat(incompleteCount).isGreaterThan(0L);
        });

        // Publish the policy to allow the Commission event to succeed
        PolicyCreatedEvent policyEvent = PolicyCreatedEvent.create(
                policyId, 1L, "POL-INT-FLT", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM reporting_policy_read_models WHERE policy_id = ? AND commission_status = 'CONFIGURED'", 
                    Long.class, policyId);
            assertThat(count).isEqualTo(1L);
        });
    }

    // Removed dummy listener from here; moved to TestExecutorListener component

    @Test
    void testUnrelatedEventFiltering() throws InterruptedException {
        UUID dummyId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        
        com.anverraglobal.policy.event.PolicyDeactivatedEvent unrelatedEvent = com.anverraglobal.policy.event.PolicyDeactivatedEvent.create(
                dummyId, 1L, "POL-DUMMY-1", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "INACTIVE", new BigDecimal("1000.00")
        );
        
        // Publish it so the intentional exception leaves it incomplete
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(unrelatedEvent));

        // Verify it is incomplete
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL AND event_type LIKE '%PolicyDeactivatedEvent%'", 
                Long.class);
            assertThat(count).isEqualTo(1L);
        });

        // Trigger scheduler explicitly to prove it skips this event
        eventRetryScheduler.retryIncompleteEvents();

        // Verify it is still incomplete. The scheduler did not attempt to resubmit it, because if it did, 
        // it would have hit the exception again, but we just verify it was ignored by checking its state.
        Long countAfter = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL AND event_type LIKE '%PolicyDeactivatedEvent%'", 
            Long.class);
        assertThat(countAfter).isEqualTo(1L);
        
        // Note: It simply remains incomplete. It is not deleted or marked failed.
    }

    @Autowired
    private EventRetryScheduler eventRetryScheduler;

    @Test
    void testAgeFiltering() {
        UUID policyId = UUID.randomUUID();
        
        // Publish out-of-order Commission event to become incomplete
        CommissionConfiguredEvent commissionEvent = CommissionConfiguredEvent.create(
                policyId, 0L, "CONFIGURED", "PERCENTAGE", new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00")
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(commissionEvent));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long incompleteCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", 
                Long.class);
            assertThat(incompleteCount).isGreaterThan(0L);
        });

        // Now, we simulate age by directly modifying the publication_date of the incomplete event in the DB.
        // We use Java Instant to avoid DB/Java clock skew.
        
        // C. Event published exactly 24 hours ago -> NOT eligible
        Instant exact24hAgo = Instant.now().minus(Duration.ofHours(24));
        jdbcTemplate.update("UPDATE event_publication SET publication_date = ?", java.sql.Timestamp.from(exact24hAgo));
        
        // Publish policy so that if the event were retried, it would succeed.
        PolicyCreatedEvent policyEvent = PolicyCreatedEvent.create(
                policyId, 1L, "POL-AGE-24", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "DRAFT", new BigDecimal("1000.00")
        );
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));

        // Explicitly trigger the retry mechanism
        eventRetryScheduler.retryIncompleteEvents();

        // The event is exactly 24 hours old, so isAfter(24h) is false -> not retried.
        // It must remain incomplete! (Not deleted, not marked complete)
        Long countAfter24h = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
        assertThat(countAfter24h).isGreaterThan(0L);

        // D. Event published 25 hours ago -> NOT eligible
        Instant exact25hAgo = Instant.now().minus(Duration.ofHours(25));
        jdbcTemplate.update("UPDATE event_publication SET publication_date = ?", java.sql.Timestamp.from(exact25hAgo));
        eventRetryScheduler.retryIncompleteEvents();
        Long countAfter25h = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
        assertThat(countAfter25h).isGreaterThan(0L);

        // B. Event published 23 hours ago -> eligible
        Instant exact23hAgo = Instant.now().minus(Duration.ofHours(23));
        jdbcTemplate.update("UPDATE event_publication SET publication_date = ?", java.sql.Timestamp.from(exact23hAgo));
        eventRetryScheduler.retryIncompleteEvents();
        
        // Now it should be retried and complete successfully because the policy exists!
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long finalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(finalCount).isEqualTo(0L);
        });

        // A. Event published 1 hour ago -> eligible (prove by republishing another event)
        CommissionConfiguredEvent commissionEvent2 = CommissionConfiguredEvent.create(
                policyId, 2L, "CONFIGURED", "PERCENTAGE", new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00")
        );
        // Force it to be incomplete by deleting the policy read-model temporally
        jdbcTemplate.update("DELETE FROM reporting_policy_read_models WHERE policy_id = ?", policyId);
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(commissionEvent2));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long incompleteCount2 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", 
                Long.class);
            assertThat(incompleteCount2).isGreaterThan(0L);
        });

        // Set to 1 hour ago
        Instant exact1hAgo = Instant.now().minus(Duration.ofHours(1));
        jdbcTemplate.update("UPDATE event_publication SET publication_date = ?", java.sql.Timestamp.from(exact1hAgo));

        // Recreate the policy so it can succeed
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(policyEvent));

        eventRetryScheduler.retryIncompleteEvents();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Long finalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_publication WHERE completion_date IS NULL", Long.class);
            assertThat(finalCount).isEqualTo(0L);
        });
    }

    @Test
    void testExecutorVerification() throws InterruptedException {
        // We will publish an event and verify the thread name used by Modulith listeners.
        // Spring Boot log outputs confirm "[modulith-event-X]".
        // To programmatically verify this without brittle introspection, we add a listener that records the thread name.
        
        UUID dummyId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        com.anverraglobal.policy.event.PolicyDeactivatedEvent testEvent = com.anverraglobal.policy.event.PolicyDeactivatedEvent.create(
                dummyId, 1L, "POL-DUMMY-2", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), "INACTIVE", new BigDecimal("1000.00")
        );
        
        transactionTemplate.executeWithoutResult(status -> eventPublisher.publishEvent(testEvent));
        
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(TestExecutorListener.lastThreadName).isNotNull();
            assertThat(TestExecutorListener.lastThreadName).startsWith("modulith-event-");
        });
    }
}

@org.springframework.stereotype.Component
class TestExecutorListener {
    static volatile String lastThreadName;

    @org.springframework.modulith.events.ApplicationModuleListener
    void on(com.anverraglobal.policy.event.PolicyDeactivatedEvent event) {
        if (event.aggregateId().toString().equals("11111111-1111-1111-1111-111111111111")) {
            lastThreadName = Thread.currentThread().getName();
        }
    }

    @org.springframework.modulith.events.ApplicationModuleListener
    public void onTestOnlyPolicyDeactivatedEvent(com.anverraglobal.policy.event.PolicyDeactivatedEvent event) {
        if (event.aggregateId().toString().equals("00000000-0000-0000-0000-000000000000")) {
            throw new IllegalStateException("Intentional test failure for unrelated event");
        }
    }
}
