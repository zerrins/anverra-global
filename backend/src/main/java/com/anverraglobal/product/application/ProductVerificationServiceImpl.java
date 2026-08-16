package com.anverraglobal.product.application;

import com.anverraglobal.product.contracts.ProductVerificationContract;
import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductVerificationServiceImpl implements ProductVerificationContract {

    private final ProductManagementApplicationService service;

    public ProductVerificationServiceImpl(ProductManagementApplicationService service) {
        this.service = service;
    }

    @Override
    @Transactional(readOnly = true)
    public void verifyProductActive(UUID productId) {
        try {
            Product product = service.getProduct(productId);
            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new IllegalStateException("Product is not ACTIVE: " + productId);
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Product not found: " + productId);
        }
    }
}
