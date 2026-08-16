package com.anverraglobal.product.contracts;

import java.util.UUID;

public interface ProductVerificationContract {

    /**
     * Verifies that the given Product exists and is ACTIVE.
     *
     * @param productId The UUID of the Product to verify.
     * @throws java.util.NoSuchElementException if the Product does not exist.
     * @throws java.lang.IllegalStateException  if the Product exists but is not ACTIVE.
     */
    void verifyProductActive(UUID productId);
}
