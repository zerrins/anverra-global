package com.anverraglobal.insurer.adapter.inbound.web;

import com.anverraglobal.insurer.application.InsurerManagementApplicationService;
import com.anverraglobal.insurer.domain.Insurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsurerController.class)
class InsurerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsurerManagementApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testCreateInsurerAdmin() throws Exception {
        Insurer p = Insurer.create("Test");
        Mockito.when(service.createInsurer("Test")).thenReturn(p);

        InsurerController.CreateInsurerRequest req = new InsurerController.CreateInsurerRequest("Test");
        mockMvc.perform(post("/api/v1/insurers")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_AGENT")
    void testCreateInsurerAgentForbidden() throws Exception {
        Mockito.when(service.createInsurer(Mockito.anyString()))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("Access Denied"));
                
        InsurerController.CreateInsurerRequest req = new InsurerController.CreateInsurerRequest("Test");
        mockMvc.perform(post("/api/v1/insurers")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_AGENT")
    void testListInsurersAllowed() throws Exception {
        Insurer p = Insurer.create("Test");
        Mockito.when(service.searchInsurers(null, null, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of(p)));

        mockMvc.perform(get("/api/v1/insurers?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test"));
    }
    
    @Test
    void testUnauthenticatedAccessRejected() throws Exception {
        mockMvc.perform(get("/api/v1/insurers"))
                .andExpect(status().isUnauthorized());
    }
}
