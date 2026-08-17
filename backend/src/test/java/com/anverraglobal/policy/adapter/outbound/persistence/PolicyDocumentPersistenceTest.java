package com.anverraglobal.policy.adapter.outbound.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class PolicyDocumentPersistenceTest {

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
    private PolicyDocumentRepository policyDocumentRepository;

    @Test
    void testSaveAndLoadDocument() {
        PolicyDocumentEntity entity = new PolicyDocumentEntity();
        UUID policyId = UUID.randomUUID();
        entity.setPolicyId(policyId);
        entity.setStorageKey("policies/" + policyId + "/test.pdf");
        entity.setOriginalFilename("test.pdf");
        entity.setContentType("application/pdf");
        entity.setSizeBytes(1024L);
        entity.setVersion(null);

        PolicyDocumentEntity saved = policyDocumentRepository.save(entity);
        assertThat(saved.getVersion()).isNotNull();

        PolicyDocumentEntity loaded = policyDocumentRepository.findById(policyId).orElseThrow();
        assertThat(loaded.getPolicyId()).isEqualTo(policyId);
        assertThat(loaded.getStorageKey()).isEqualTo("policies/" + policyId + "/test.pdf");
        assertThat(loaded.getOriginalFilename()).isEqualTo("test.pdf");
        assertThat(loaded.getContentType()).isEqualTo("application/pdf");
        assertThat(loaded.getSizeBytes()).isEqualTo(1024L);
    }

    @Test
    void testReplaceExistingDocument() {
        UUID policyId = UUID.randomUUID();

        PolicyDocumentEntity doc1 = new PolicyDocumentEntity();
        doc1.setPolicyId(policyId);
        doc1.setStorageKey("key1");
        doc1.setOriginalFilename("file1.pdf");
        doc1.setContentType("application/pdf");
        doc1.setSizeBytes(100L);
        doc1.setVersion(null);

        policyDocumentRepository.save(doc1);

        PolicyDocumentEntity loaded = policyDocumentRepository.findById(policyId).orElseThrow();

        // Update
        loaded.setStorageKey("key2");
        loaded.setOriginalFilename("file2.pdf");
        policyDocumentRepository.save(loaded);

        PolicyDocumentEntity updated = policyDocumentRepository.findById(policyId).orElseThrow();
        assertThat(updated.getStorageKey()).isEqualTo("key2");
        assertThat(updated.getOriginalFilename()).isEqualTo("file2.pdf");
    }

    @Test
    void testDeleteDocument() {
        UUID policyId = UUID.randomUUID();

        PolicyDocumentEntity doc = new PolicyDocumentEntity();
        doc.setPolicyId(policyId);
        doc.setStorageKey("key");
        doc.setOriginalFilename("file.pdf");
        doc.setContentType("application/pdf");
        doc.setSizeBytes(100L);
        doc.setVersion(null);

        policyDocumentRepository.save(doc);

        assertThat(policyDocumentRepository.findById(policyId)).isPresent();

        policyDocumentRepository.deleteById(policyId);

        assertThat(policyDocumentRepository.findById(policyId)).isEmpty();
    }
}
