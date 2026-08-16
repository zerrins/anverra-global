package com.anverraglobal.customer.adapter.inbound.web;

import com.anverraglobal.customer.application.CustomerManagementApplicationService;
import com.anverraglobal.customer.application.port.inbound.CreateCustomerCommand;
import com.anverraglobal.customer.application.port.inbound.UpdateCustomerCommand;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerStatus;
import com.anverraglobal.customer.domain.CustomerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerManagementApplicationService customerService;

    private final UUID testIdentityId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private Customer mockCustomer() {
        return new Customer(
                UUID.randomUUID(),
                CustomerType.INDIVIDUAL,
                "Test User",
                "test@example.com",
                "123 Test St",
                CustomerStatus.ACTIVE,
                "individual_info",
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.now(),
                Instant.now(),
                0L
        );
    }

    @Test
    void unauthenticatedGet_rejected() throws Exception {
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedPost_rejected() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000000", roles = "ADMIN")
    void validGlobalAdminCreation_returns201() throws Exception {
        when(customerService.createCustomer(any(), any(), any())).thenReturn(mockCustomer());

        String json = """
                {
                    "customerType": "INDIVIDUAL",
                    "name": "Test",
                    "contactInfo": "test@test.com",
                    "addressInfo": "123 Test St",
                    "individualInfo": "123",
                    "targetDealerId": "00000000-0000-0000-0000-000000000001",
                    "targetBranchId": "00000000-0000-0000-0000-000000000002",
                    "targetAgentId": "00000000-0000-0000-0000-000000000003"
                }
                """;
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "00000000-0000-0000-0000-000000000000", roles = "AGENT")
    void validNormalUserCreation_returns201() throws Exception {
        when(customerService.createCustomer(any(), any(), any())).thenReturn(mockCustomer());

        String json = """
                {
                    "customerType": "INDIVIDUAL",
                    "name": "Test",
                    "contactInfo": "test@test.com",
                    "addressInfo": "123 Test St",
                    "individualInfo": "123"
                }
                """;
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void missingName_returns400() throws Exception {
        String json = """
                {
                    "customerType": "INDIVIDUAL",
                    "contactInfo": "test@test.com",
                    "addressInfo": "123 Test St",
                    "individualInfo": "123"
                }
                """;
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void invalidConditionalJson_returns400() throws Exception {
        // Individual missing individualInfo
        String json = """
                {
                    "customerType": "INDIVIDUAL",
                    "name": "Test",
                    "contactInfo": "test@test.com",
                    "addressInfo": "123 Test St"
                }
                """;
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void normalUserSupplyingTargetOwnership_rejectedByService() throws Exception {
        when(customerService.createCustomer(any(), any(), any())).thenThrow(new IllegalArgumentException("Non-admins cannot specify target ownership fields"));
        
        String json = """
                {
                    "customerType": "INDIVIDUAL",
                    "name": "Test",
                    "contactInfo": "test@test.com",
                    "addressInfo": "123 Test St",
                    "individualInfo": "123",
                    "targetDealerId": "00000000-0000-0000-0000-000000000001"
                }
                """;
        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Non-admins cannot specify target ownership fields"));
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void getById_returns200() throws Exception {
        Customer c = mockCustomer();
        when(customerService.getCustomer(any(), any(), eq(c.getId()))).thenReturn(c);

        mockMvc.perform(get("/api/v1/customers/" + c.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void getMissingOrOutOfScope_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(customerService.getCustomer(any(), any(), eq(id)))
                .thenThrow(new NoSuchElementException("Customer not found or out of scope"));

        mockMvc.perform(get("/api/v1/customers/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Customer not found or out of scope"));
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void listReturns200() throws Exception {
        when(customerService.listCustomers(any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(mockCustomer())));

        mockMvc.perform(get("/api/v1/customers?page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void validUpdateReturns200() throws Exception {
        Customer c = mockCustomer();
        when(customerService.updateCustomer(any(), any(), eq(c.getId()), any())).thenReturn(c);

        String json = """
                {
                    "name": "New Name",
                    "contactInfo": "new@test.com",
                    "addressInfo": "456 Test St",
                    "individualInfo": "{\\"pan\\":\\"NEWPAN\\"}",
                    "businessInfo": "{\\"gstin\\":\\"NEWGSTIN\\"}"
                }
                """;
        mockMvc.perform(put("/api/v1/customers/" + c.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void activateReturns200() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(customerService).activateCustomer(any(), any(), eq(id));

        mockMvc.perform(post("/api/v1/customers/" + id + "/lifecycle/activate")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "DATA_ENTRY")
    void forbiddenLifecycle_returns403() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new AccessDeniedException("Data Entry cannot lifecycle customers"))
                .when(customerService).activateCustomer(any(), any(), eq(id));

        mockMvc.perform(post("/api/v1/customers/" + id + "/lifecycle/activate")
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.detail").value("Data Entry cannot lifecycle customers"));
    }
}
