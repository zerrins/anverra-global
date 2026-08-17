package com.anverraglobal.policy.application;

import com.anverraglobal.policy.application.port.outbound.PolicyDocumentRepositoryPort;
import com.anverraglobal.policy.domain.Policy;
import com.anverraglobal.policy.domain.PolicyDocument;
import com.anverraglobal.policy.port.outbound.DocumentStoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyDocumentManagementApplicationServiceTest {

    @Mock
    private PolicyManagementApplicationService policyService;

    @Mock
    private PolicyDocumentRepositoryPort documentRepositoryPort;

    @Mock
    private DocumentStoragePort documentStoragePort;

    private PolicyDocumentManagementApplicationService service;

    private final UUID identityId = UUID.randomUUID();
    private final String role = "ROLE_AGENT";
    private final UUID policyId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new PolicyDocumentManagementApplicationService(policyService, documentRepositoryPort, documentStoragePort);
    }

    @Test
    void shouldGenerateUploadUrlForAuthorizedUser() {
        when(policyService.getPolicy(identityId, role, policyId)).thenReturn(mock(Policy.class));
        when(documentStoragePort.generateUploadUrl(anyString(), eq("application/pdf"))).thenReturn("https://upload.url");

        var result = service.generateUploadUrl(identityId, role, policyId, "test.pdf", "application/pdf");

        assertThat(result.storageKey()).startsWith("policies/" + policyId + "/").endsWith("-test.pdf");
        assertThat(result.uploadUrl()).isEqualTo("https://upload.url");
    }

    @Test
    void shouldThrowWhenGeneratingUploadUrlForUnauthorizedUser() {
        when(policyService.getPolicy(identityId, role, policyId)).thenThrow(new AccessDeniedException("Denied"));

        assertThatThrownBy(() -> service.generateUploadUrl(identityId, role, policyId, "test.pdf", "application/pdf"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void shouldRegisterDocumentAndRemoveOldOne() {
        when(policyService.getPolicy(identityId, role, policyId)).thenReturn(mock(Policy.class));

        String newStorageKey = "policies/" + policyId + "/new.pdf";
        PolicyDocument oldDoc = new PolicyDocument(policyId, "policies/" + policyId + "/old.pdf", "old.pdf", "application/pdf", 100L, 1L);
        PolicyDocument newDoc = new PolicyDocument(policyId, newStorageKey, "new.pdf", "application/pdf", 200L, null);

        when(documentRepositoryPort.findByPolicyId(policyId)).thenReturn(Optional.of(oldDoc));
        when(documentRepositoryPort.save(any(PolicyDocument.class))).thenReturn(newDoc);

        var result = service.registerDocument(identityId, role, policyId, newStorageKey, "new.pdf", "application/pdf", 200L);

        assertThat(result).isEqualTo(newDoc);
        verify(documentStoragePort).removeDocument("policies/" + policyId + "/old.pdf");
    }

    @Test
    void shouldThrowWhenRegisteringDocumentWithInvalidStorageKey() {
        when(policyService.getPolicy(identityId, role, policyId)).thenReturn(mock(Policy.class));

        assertThatThrownBy(() -> service.registerDocument(identityId, role, policyId, "invalid/key.pdf", "new.pdf", "application/pdf", 200L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldGetDocument() {
        when(policyService.getPolicy(identityId, role, policyId)).thenReturn(mock(Policy.class));
        PolicyDocument doc = new PolicyDocument(policyId, "policies/" + policyId + "/test.pdf", "test.pdf", "application/pdf", 100L, 1L);
        when(documentRepositoryPort.findByPolicyId(policyId)).thenReturn(Optional.of(doc));
        when(documentStoragePort.generateDownloadUrl(doc.getStorageKey())).thenReturn("https://download.url");

        var result = service.getDocument(identityId, role, policyId);

        assertThat(result.document()).isEqualTo(doc);
        assertThat(result.downloadUrl()).isEqualTo("https://download.url");
    }

    @Test
    void shouldRemoveDocument() {
        when(policyService.getPolicy(identityId, role, policyId)).thenReturn(mock(Policy.class));
        PolicyDocument doc = new PolicyDocument(policyId, "policies/" + policyId + "/test.pdf", "test.pdf", "application/pdf", 100L, 1L);
        when(documentRepositoryPort.findByPolicyId(policyId)).thenReturn(Optional.of(doc));

        service.removeDocument(identityId, role, policyId);

        verify(documentStoragePort).removeDocument(doc.getStorageKey());
        verify(documentRepositoryPort).deleteByPolicyId(policyId);
    }
}
