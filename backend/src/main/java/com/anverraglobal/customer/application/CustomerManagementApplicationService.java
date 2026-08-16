package com.anverraglobal.customer.application;

import com.anverraglobal.customer.application.port.inbound.CreateCustomerCommand;
import com.anverraglobal.customer.application.port.inbound.UpdateCustomerCommand;
import com.anverraglobal.customer.application.port.outbound.CustomerRepositoryPort;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.organization.contracts.OrganizationHierarchyContract;
import com.anverraglobal.organization.contracts.OrganizationHierarchyContract.HierarchyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CustomerManagementApplicationService {

    private final CustomerRepositoryPort customerRepository;
    private final OrganizationScopeResolutionService scopeResolutionService;
    private final OrganizationHierarchyContract hierarchyContract;

    public CustomerManagementApplicationService(
            CustomerRepositoryPort customerRepository,
            OrganizationScopeResolutionService scopeResolutionService,
            OrganizationHierarchyContract hierarchyContract) {
        this.customerRepository = customerRepository;
        this.scopeResolutionService = scopeResolutionService;
        this.hierarchyContract = hierarchyContract;
    }

    public Customer createCustomer(UUID identityId, String role, CreateCustomerCommand command) {
        if ("ROLE_CUSTOMER".equals(role)) {
            throw new AccessDeniedException("Customer cannot create another Customer");
        }

        UUID dealerId = null;
        UUID branchId = null;
        UUID agentId = null;

        if ("ROLE_ADMIN".equals(role)) {
            // Global Admin targeting
            dealerId = command.targetDealerId();
            branchId = command.targetBranchId();
            agentId = command.targetAgentId();

            if (dealerId == null || branchId == null || agentId == null) {
                throw new IllegalArgumentException("Global Admin must provide targetDealerId, targetBranchId, and targetAgentId");
            }

            // Validate hierarchy
            Optional<HierarchyInfo> optionalAgent = hierarchyContract.getHierarchyForIdentity(agentId);
            if (optionalAgent.isEmpty()) {
                throw new IllegalArgumentException("Target agent does not exist");
            }
            HierarchyInfo agentMem = optionalAgent.get();
            if (!branchId.equals(agentMem.branchId()) || !dealerId.equals(agentMem.dealerId())) {
                throw new IllegalArgumentException("Inconsistent hierarchy provided for Global Admin creation");
            }
        } else {
            // Normal user deriving ownership
            if (command.targetDealerId() != null || command.targetBranchId() != null || command.targetAgentId() != null) {
                throw new IllegalArgumentException("Non-admins cannot specify target ownership fields");
            }

            Optional<HierarchyInfo> optionalMembership = hierarchyContract.getHierarchyForIdentity(identityId);
            if (optionalMembership.isEmpty()) {
                throw new AccessDeniedException("Authenticated user lacks organization membership");
            }
            HierarchyInfo membership = optionalMembership.get();

            if ("DATA_ENTRY".equals(role)) {
                agentId = membership.parentIdentityId();
            } else {
                agentId = identityId;
            }
            dealerId = membership.dealerId() != null ? membership.dealerId() : identityId;
            branchId = membership.branchId() != null ? membership.branchId() : identityId;
        }

        Customer customer = Customer.create(
                command.customerType(),
                command.name(),
                command.contactInfo(),
                command.addressInfo(),
                command.individualInfo(),
                command.businessInfo(),
                dealerId,
                branchId,
                agentId
        );

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(UUID identityId, String role, UUID customerId) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        return customerRepository.findByIdAndScope(customerId, scope)
                .orElseThrow(() -> new NoSuchElementException("Customer not found or out of scope"));
    }

    @Transactional(readOnly = true)
    public Page<Customer> listCustomers(UUID identityId, String role, String name, String customerType, String status, Pageable pageable) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        return customerRepository.listByScope(scope, name, customerType, status, pageable);
    }

    public Customer updateCustomer(UUID identityId, String role, UUID customerId, UpdateCustomerCommand command) {
        if ("ROLE_CUSTOMER".equals(role)) {
            throw new AccessDeniedException("Customer cannot update another Customer");
        }
        
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Customer customer = customerRepository.findByIdAndScope(customerId, scope)
                .orElseThrow(() -> new NoSuchElementException("Customer not found or out of scope"));

        customer.update(command.name(), command.contactInfo(), command.addressInfo(), command.individualInfo(), command.businessInfo());

        return customerRepository.save(customer);
    }

    public void activateCustomer(UUID identityId, String role, UUID customerId) {
        verifyLifecyclePermission(role);
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Customer customer = customerRepository.findByIdAndScope(customerId, scope)
                .orElseThrow(() -> new NoSuchElementException("Customer not found or out of scope"));

        customer.activate();
        customerRepository.save(customer);
    }

    public void deactivateCustomer(UUID identityId, String role, UUID customerId) {
        verifyLifecyclePermission(role);
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Customer customer = customerRepository.findByIdAndScope(customerId, scope)
                .orElseThrow(() -> new NoSuchElementException("Customer not found or out of scope"));

        customer.deactivate();
        customerRepository.save(customer);
    }

    private void verifyLifecyclePermission(String role) {
        if ("DATA_ENTRY".equals(role)) {
            throw new AccessDeniedException("Data Entry cannot lifecycle customers");
        }
        if ("ROLE_CUSTOMER".equals(role)) {
            throw new AccessDeniedException("Customer cannot lifecycle customers");
        }
    }
}
