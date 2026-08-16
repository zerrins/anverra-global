package com.anverraglobal.identity.adapter.outbound.persistence;

import com.anverraglobal.identity.domain.IdentityProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(IdentityPersistenceAdapter.class)
class IdentityProfilePersistenceAdapterTest {

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
    private IdentityPersistenceAdapter adapter;

    @Autowired
    private IdentityProfileRepository repository;

    @Test
    void testUpsertAndResolve() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        IdentityProfile p1 = new IdentityProfile(id1, "Alice", "alice@example.com", Instant.now(), Instant.now());
        IdentityProfile p2 = new IdentityProfile(id2, "Bob", null, Instant.now(), Instant.now());

        adapter.upsert(p1);
        adapter.upsert(p2);

        Map<UUID, String> names = adapter.resolveDisplayNames(Set.of(id1, id2, UUID.randomUUID()));

        assertThat(names).hasSize(2);
        assertThat(names.get(id1)).isEqualTo("Alice");
        assertThat(names.get(id2)).isEqualTo("Bob");

        // Test update
        IdentityProfile p1Updated = new IdentityProfile(id1, "Alice Smith", "alice.smith@example.com", Instant.now(), Instant.now());
        adapter.upsert(p1Updated);

        Map<UUID, String> updatedNames = adapter.resolveDisplayNames(Set.of(id1));
        assertThat(updatedNames.get(id1)).isEqualTo("Alice Smith");
    }

    @Test
    void resolveDisplayNames_Empty() {
        Map<UUID, String> names = adapter.resolveDisplayNames(Set.of());
        assertThat(names).isEmpty();
    }
}
