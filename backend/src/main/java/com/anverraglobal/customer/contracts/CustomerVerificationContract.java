package com.anverraglobal.customer.contracts;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;

import java.util.UUID;

public interface CustomerVerificationContract {
    
    /**
     * Verifies that the given Customer exists, is ACTIVE, and is accessible within the provided OrganizationScope.
     * 
     * @param customerId The UUID of the Customer to verify.
     * @param scope The OrganizationScope of the caller.
     * @throws java.util.NoSuchElementException if the Customer does not exist or is outside the caller's scope.
     * @throws java.lang.IllegalStateException if the Customer exists but is not ACTIVE.
     */
    void verifyCustomerActiveAndInScope(UUID customerId, OrganizationScope scope);
}
