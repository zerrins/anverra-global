package com.anverraglobal.customer.application.port.outbound;

import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    Optional<Customer> findByIdAndScope(UUID id, OrganizationScope scope);
    Page<Customer> listByScope(OrganizationScope scope, String name, String customerType, String status, Pageable pageable);
}
