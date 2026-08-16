package com.anverraglobal.customer.application;

import com.anverraglobal.customer.application.port.outbound.CustomerRepositoryPort;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerStatus;
import com.anverraglobal.customer.domain.CustomerType;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerVerificationServiceImplTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    private CustomerVerificationServiceImpl verificationService;

    @BeforeEach
    void setUp() {
        verificationService = new CustomerVerificationServiceImpl(customerRepository);
    }

    @Test
    void verifyCustomerActiveAndInScope_whenActiveAndInScope_succeeds() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID identityId = UUID.randomUUID();
        OrganizationScope scope = OrganizationScope.empty(identityId);

        Customer activeCustomer = Customer.create(
                CustomerType.INDIVIDUAL,
                "John Doe",
                "{}", "{}", "{}", "{}",
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
        );
        activeCustomer.activate();

        when(customerRepository.findByIdAndScope(customerId, scope)).thenReturn(Optional.of(activeCustomer));

        // Act
        verificationService.verifyCustomerActiveAndInScope(customerId, scope);

        // Assert
        verify(customerRepository).findByIdAndScope(customerId, scope);
    }

    @Test
    void verifyCustomerActiveAndInScope_whenCustomerDoesNotExistOrOutOfScope_throwsNoSuchElementException() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID identityId = UUID.randomUUID();
        OrganizationScope scope = OrganizationScope.empty(identityId);

        when(customerRepository.findByIdAndScope(customerId, scope)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> verificationService.verifyCustomerActiveAndInScope(customerId, scope))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Customer not found or out of scope");
    }

    @Test
    void verifyCustomerActiveAndInScope_whenCustomerIsInactive_throwsIllegalStateException() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID identityId = UUID.randomUUID();
        OrganizationScope scope = OrganizationScope.empty(identityId);

        Customer inactiveCustomer = Customer.create(
                CustomerType.INDIVIDUAL,
                "John Doe",
                "{}", "{}", "{}", "{}",
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
        );
        // By default it is DRAFT, which is not ACTIVE. Let's make it INACTIVE properly or just use it as DRAFT (also fails).
        // Let's activate then deactivate it to make it explicitly INACTIVE.
        inactiveCustomer.activate();
        inactiveCustomer.deactivate();
        
        // Assert status is INACTIVE just to be sure
        org.assertj.core.api.Assertions.assertThat(inactiveCustomer.getStatus()).isEqualTo(CustomerStatus.INACTIVE);

        when(customerRepository.findByIdAndScope(customerId, scope)).thenReturn(Optional.of(inactiveCustomer));

        // Act & Assert
        assertThatThrownBy(() -> verificationService.verifyCustomerActiveAndInScope(customerId, scope))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Customer is not ACTIVE");
    }
}
