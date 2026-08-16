package com.anverraglobal.identity.adapter.inbound.web;

import com.anverraglobal.identity.application.IdentityProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdentityProfileController.class)
class IdentityProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdentityProfileService identityProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void syncProfile_Success() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "John Doe", "john@example.com");

        mockMvc.perform(post("/api/v1/identity/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(identityProfileService).upsertProfile(eq(id), eq("John Doe"), eq("john@example.com"));
    }

    @Test
    @WithMockUser
    void syncProfile_MissingFields() throws Exception {
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(null, "", null);

        mockMvc.perform(post("/api/v1/identity/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void syncProfile_ServiceThrowsException_ReturnsOkToNotFailAuthentication() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "John Doe", "john@example.com");

        doThrow(new RuntimeException("DB Error")).when(identityProfileService).upsertProfile(any(), any(), any());

        mockMvc.perform(post("/api/v1/identity/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void syncProfile_Unauthenticated_ReturnsUnauthorized() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "John Doe", "john@example.com");

        mockMvc.perform(post("/api/v1/identity/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
