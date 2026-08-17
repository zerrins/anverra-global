package com.anverraglobal.policy.adapter.inbound.web;

import com.anverraglobal.policy.application.PolicyDocumentManagementApplicationService;
import com.anverraglobal.policy.application.PolicyDocumentManagementApplicationService.DocumentDownloadInfo;
import com.anverraglobal.policy.application.PolicyDocumentManagementApplicationService.PresignedUploadInfo;
import com.anverraglobal.policy.domain.PolicyDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyDocumentController.class)
class PolicyDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyDocumentManagementApplicationService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", authorities = "ROLE_AGENT")
    void shouldGenerateUploadUrl() throws Exception {
        UUID policyId = UUID.randomUUID();
        PresignedUploadInfo info = new PresignedUploadInfo("key", "url");

        when(documentService.generateUploadUrl(any(), anyString(), eq(policyId), eq("test.pdf"), eq("application/pdf")))
                .thenReturn(info);

        PolicyDocumentController.PresignedUploadRequest request = new PolicyDocumentController.PresignedUploadRequest("test.pdf", "application/pdf");

        mockMvc.perform(post("/api/v1/policies/{policyId}/document/presigned-upload", policyId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storageKey").value("key"))
                .andExpect(jsonPath("$.uploadUrl").value("url"));
    }

    @Test
    @WithMockUser(username = "11111111-1111-1111-1111-111111111111", authorities = "ROLE_AGENT")
    void shouldRegisterDocument() throws Exception {
        UUID policyId = UUID.randomUUID();
        PolicyDocument doc = new PolicyDocument(policyId, "key", "test.pdf", "application/pdf", 100L, 1L);

        when(documentService.registerDocument(any(), anyString(), eq(policyId), eq("key"), eq("test.pdf"), eq("application/pdf"), eq(100L)))
                .thenReturn(doc);

        PolicyDocumentController.RegisterPolicyDocumentRequest request = new PolicyDocumentController.RegisterPolicyDocumentRequest("key", "test.pdf", "application/pdf", 100L);

        mockMvc.perform(put("/api/v1/policies/{policyId}/document", policyId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalFilename").value("test.pdf"));
    }
}
