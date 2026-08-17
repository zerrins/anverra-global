package com.anverraglobal.organization.adapter.inbound.web;

import com.anverraglobal.organization.application.OrganizationManagementApplicationService;
import com.anverraglobal.organization.domain.Branch;
import com.anverraglobal.organization.domain.Dealer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizationManagementController.class)
@AutoConfigureMockMvc
class OrganizationManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationManagementApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000000", authorities = {"ROLE_ADMIN"})
    void createDealer() throws Exception {
        Dealer dealer = Dealer.create("Test Dealer");
        when(service.createDealer(eq("Test Dealer"), any(), eq("ROLE_ADMIN"))).thenReturn(dealer);

        mockMvc.perform(post("/api/v1/dealers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrganizationManagementController.DealerRequest("Test Dealer"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Dealer"));
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000000", authorities = {"ROLE_DEALER"})
    void createBranch() throws Exception {
        UUID dealerId = UUID.randomUUID();
        Branch branch = Branch.create(dealerId, "Test Branch");
        when(service.createBranch(eq(dealerId), eq("Test Branch"), any(), eq("ROLE_DEALER"))).thenReturn(branch);

        mockMvc.perform(post("/api/v1/branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrganizationManagementController.BranchRequest(dealerId, "Test Branch"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Branch"));
    }
}
