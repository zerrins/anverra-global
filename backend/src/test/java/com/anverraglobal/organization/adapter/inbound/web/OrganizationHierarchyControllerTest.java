package com.anverraglobal.organization.adapter.inbound.web;

import com.anverraglobal.organization.application.OrganizationHierarchyServiceImpl;
import com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class OrganizationHierarchyControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.modulith.events.jdbc.schema-initialization.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationHierarchyServiceImpl hierarchyService;

    @Test
    void getDealers_admin_returnsOk() throws Exception {
        UUID adminId = UUID.randomUUID();
        Mockito.when(hierarchyService.getDealers(adminId, "ROLE_ADMIN"))
                .thenReturn(List.of(new HierarchyNodeResponse(UUID.randomUUID(), "Test Dealer")));

        mockMvc.perform(get("/api/v1/hierarchy/dealers")
                        .with(jwt().jwt(builder -> builder.claim("https://anverraglobal.com/identity_id", adminId.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Dealer"));
    }

    @Test
    void getDealers_agent_returnsForbidden() throws Exception {
        UUID agentId = UUID.randomUUID();
        Mockito.when(hierarchyService.getDealers(agentId, "ROLE_AGENT"))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("Denied"));

        mockMvc.perform(get("/api/v1/hierarchy/dealers")
                        .with(jwt().jwt(builder -> builder.claim("https://anverraglobal.com/identity_id", agentId.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_AGENT"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBranches_notfound_returns404() throws Exception {
        UUID adminId = UUID.randomUUID();
        UUID dealerId = UUID.randomUUID();
        Mockito.when(hierarchyService.getBranches(adminId, "ROLE_ADMIN", dealerId))
                .thenThrow(new java.util.NoSuchElementException("Dealer not found"));

        mockMvc.perform(get("/api/v1/hierarchy/dealers/" + dealerId + "/branches")
                        .with(jwt().jwt(builder -> builder.claim("https://anverraglobal.com/identity_id", adminId.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAgents_authorized_returnsOk() throws Exception {
        UUID dealerId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        Mockito.when(hierarchyService.getAgents(dealerId, "ROLE_DEALER", branchId))
                .thenReturn(List.of(new HierarchyNodeResponse(UUID.randomUUID(), "Agent A")));

        mockMvc.perform(get("/api/v1/hierarchy/branches/" + branchId + "/agents")
                        .with(jwt().jwt(builder -> builder.claim("https://anverraglobal.com/identity_id", dealerId.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_DEALER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Agent A"));
    }
}
