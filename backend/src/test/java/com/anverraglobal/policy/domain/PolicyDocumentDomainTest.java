package com.anverraglobal.policy.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PolicyDocumentDomainTest {

    @Test
    void shouldCreateValidPolicyDocument() {
        UUID policyId = UUID.randomUUID();
        PolicyDocument document = PolicyDocument.createNew(
                policyId,
                "storage/key/file.pdf",
                "file.pdf",
                "application/pdf",
                1024L
        );

        assertThat(document.getPolicyId()).isEqualTo(policyId);
        assertThat(document.getStorageKey()).isEqualTo("storage/key/file.pdf");
        assertThat(document.getOriginalFilename()).isEqualTo("file.pdf");
        assertThat(document.getContentType()).isEqualTo("application/pdf");
        assertThat(document.getSizeBytes()).isEqualTo(1024L);
        assertThat(document.getVersion()).isNull();
    }

    @Test
    void shouldAcceptZeroSizeBytes() {
        PolicyDocument document = PolicyDocument.createNew(
                UUID.randomUUID(),
                "storage/key/empty.pdf",
                "empty.pdf",
                "application/pdf",
                0L
        );
        assertThat(document.getSizeBytes()).isEqualTo(0L);
    }

    @Test
    void shouldRejectNullPolicyId() {
        assertThatThrownBy(() -> PolicyDocument.createNew(null, "key", "file", "type", 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Policy ID must not be null");
    }

    @Test
    void shouldRejectBlankStorageKey() {
        assertThatThrownBy(() -> PolicyDocument.createNew(UUID.randomUUID(), "  ", "file", "type", 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Storage key must not be blank");
    }

    @Test
    void shouldRejectBlankOriginalFilename() {
        assertThatThrownBy(() -> PolicyDocument.createNew(UUID.randomUUID(), "key", "  ", "type", 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Original filename must not be blank");
    }

    @Test
    void shouldRejectBlankContentType() {
        assertThatThrownBy(() -> PolicyDocument.createNew(UUID.randomUUID(), "key", "file", "  ", 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content type must not be blank for new documents");
    }

    @Test
    void shouldRejectNegativeSizeBytes() {
        assertThatThrownBy(() -> PolicyDocument.createNew(UUID.randomUUID(), "key", "file", "type", -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Size bytes must be zero or positive for new documents");
    }

    @Test
    void shouldAcceptNullMetadataForLegacyReconstruction() {
        PolicyDocument legacyDoc = new PolicyDocument(
                UUID.randomUUID(),
                "legacy-key",
                "legacy-file.pdf",
                null,
                null,
                0L
        );
        assertThat(legacyDoc.getContentType()).isNull();
        assertThat(legacyDoc.getSizeBytes()).isNull();
    }
}
