package com.anverraglobal.customer.adapter.inbound.web;

import com.anverraglobal.customer.application.CustomerManagementApplicationService;
import com.anverraglobal.customer.application.port.inbound.CreateCustomerCommand;
import com.anverraglobal.customer.application.port.inbound.UpdateCustomerCommand;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.security.Principal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerManagementApplicationService customerService;

    public CustomerController(CustomerManagementApplicationService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            Principal principal,
            @Valid @RequestBody CreateCustomerRequest request) {
        
        validateConditionalFields(request);

        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        CreateCustomerCommand command = new CreateCustomerCommand(
                request.customerType(),
                request.name(),
                request.contactInfo(),
                request.addressInfo(),
                request.individualInfo(),
                request.businessInfo(),
                request.targetDealerId(),
                request.targetBranchId(),
                request.targetAgentId()
        );

        Customer customer = customerService.createCustomer(identityId, role, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(
            Principal principal,
            @PathVariable("id") UUID customerId) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        Customer customer = customerService.getCustomer(identityId, role, customerId);
        return ResponseEntity.ok(mapToResponse(customer));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> listCustomers(
            Principal principal,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String customerType,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        Page<Customer> customers = customerService.listCustomers(identityId, role, name, customerType, status, pageable);
        return ResponseEntity.ok(customers.map(this::mapToResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            Principal principal,
            @PathVariable("id") UUID customerId,
            @Valid @RequestBody UpdateCustomerRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                null, // customerType not mutable
                request.name(),
                request.contactInfo(),
                request.addressInfo(),
                request.individualInfo(),
                request.businessInfo()
        );

        Customer customer = customerService.updateCustomer(identityId, role, customerId, command);
        return ResponseEntity.ok(mapToResponse(customer));
    }

    @PostMapping("/{id}/lifecycle/activate")
    public ResponseEntity<Void> activateCustomer(
            Principal principal,
            @PathVariable("id") UUID customerId) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        customerService.activateCustomer(identityId, role, customerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/lifecycle/deactivate")
    public ResponseEntity<Void> deactivateCustomer(
            Principal principal,
            @PathVariable("id") UUID customerId) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        customerService.deactivateCustomer(identityId, role, customerId);
        return ResponseEntity.ok().build();
    }

    private void validateConditionalFields(CreateCustomerRequest request) {
        if (request.customerType() == CustomerType.INDIVIDUAL) {
            if (request.individualInfo() == null || request.individualInfo().isBlank()) {
                throw new IllegalArgumentException("individualInfo is required for INDIVIDUAL customers");
            }
        }
        if (request.customerType() == CustomerType.ORGANIZATION) {
            if (request.businessInfo() == null || request.businessInfo().isBlank()) {
                throw new IllegalArgumentException("businessInfo is required for ORGANIZATION customers");
            }
        }
    }

    private UUID extractIdentityId(Principal principal) {
        if (principal == null) return UUID.fromString("00000000-0000-0000-0000-000000000000");
        try {
            return UUID.fromString(principal.getName());
        } catch (Exception e) {
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
    }

    private String extractRole(Principal principal) {
        if (principal instanceof org.springframework.security.core.Authentication auth) {
            if (!auth.getAuthorities().isEmpty()) {
                return auth.getAuthorities().iterator().next().getAuthority();
            }
        }
        return "ROLE_USER"; // fallback
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getCustomerType().name(),
                customer.getName(),
                customer.getContactInfo(),
                customer.getAddressInfo(),
                customer.getStatus().name(),
                customer.getIndividualInfo(),
                customer.getBusinessInfo(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    public record CreateCustomerRequest(
            @NotNull(message = "customerType is required") CustomerType customerType,
            @NotBlank(message = "name is required") String name,
            @NotBlank(message = "contactInfo is required") String contactInfo,
            @NotBlank(message = "addressInfo is required") String addressInfo,
            String individualInfo,
            String businessInfo,
            UUID targetDealerId,
            UUID targetBranchId,
            UUID targetAgentId
    ) {}

    public record UpdateCustomerRequest(
            @NotBlank(message = "name is required") String name,
            @NotBlank(message = "contactInfo is required") String contactInfo,
            @NotBlank(message = "addressInfo is required") String addressInfo,
            String individualInfo,
            String businessInfo
    ) {}

    public record CustomerResponse(
            UUID id,
            String customerType,
            String name,
            String contactInfo,
            String addressInfo,
            String status,
            String individualInfo,
            String businessInfo,
            Instant createdAt,
            Instant updatedAt
    ) {}
}
