package com.anverraglobal.customer.application;

import com.anverraglobal.customer.application.port.outbound.CustomerRepositoryPort;
import com.anverraglobal.customer.contracts.CustomerVerificationContract;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerStatus;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CustomerVerificationServiceImpl implements CustomerVerificationContract {

    private final CustomerRepositoryPort customerRepository;

    public CustomerVerificationServiceImpl(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void verifyCustomerActiveAndInScope(UUID customerId, OrganizationScope scope) {
        Customer customer = customerRepository.findByIdAndScope(customerId, scope)
                .orElseThrow(() -> new NoSuchElementException("Customer not found or out of scope: " + customerId));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new IllegalStateException("Customer is not ACTIVE: " + customerId);
        }
    }
}
