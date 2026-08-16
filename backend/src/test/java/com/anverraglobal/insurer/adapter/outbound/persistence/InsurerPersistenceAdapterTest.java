package com.anverraglobal.insurer.adapter.outbound.persistence;

import com.anverraglobal.insurer.domain.Insurer;
import com.anverraglobal.insurer.domain.InsurerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(InsurerPersistenceAdapter.class)
@Testcontainers
class InsurerPersistenceAdapterTest {

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
    private InsurerPersistenceAdapter adapter;

    @Test
    void shouldSaveAndLoad() {
        Insurer insurer = Insurer.create("Global Health");
        Insurer saved = adapter.save(insurer);
        
        Optional<Insurer> loaded = adapter.findById(saved.getId());
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getName()).isEqualTo("Global Health");
        assertThat(loaded.get().getStatus()).isEqualTo(InsurerStatus.ACTIVE);
    }

    @Test
    void shouldEnforceUniqueName() {
        Insurer insurer1 = Insurer.create("Unique Name");
        adapter.save(insurer1);

        Insurer insurer2 = Insurer.create("Unique Name");
        assertThatThrownBy(() -> adapter.save(insurer2))
                .isInstanceOf(org.springframework.data.relational.core.conversion.DbActionExecutionException.class);
    }

    @Test
    void shouldSupportOptimisticLocking() {
        Insurer insurer = Insurer.create("Lock Test");
        Insurer saved = adapter.save(insurer);

        Insurer loaded1 = adapter.findById(saved.getId()).get();
        Insurer loaded2 = adapter.findById(saved.getId()).get();

        loaded1.update("Updated 1");
        adapter.save(loaded1);

        loaded2.update("Updated 2");
        assertThatThrownBy(() -> adapter.save(loaded2))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void shouldSearchAndPaginate() {
        adapter.save(Insurer.create("Searchable 1"));
        adapter.save(Insurer.create("Searchable 2"));
        
        Insurer inactive = Insurer.create("Searchable 3");
        inactive.deactivate();
        adapter.save(inactive);

        Page<Insurer> activePage = adapter.search("Searchable", "ACTIVE", PageRequest.of(0, 10));
        assertThat(activePage.getTotalElements()).isEqualTo(2);

        Page<Insurer> allPage = adapter.search("Searchable", null, PageRequest.of(0, 10));
        assertThat(allPage.getTotalElements()).isEqualTo(3);
    }
}
