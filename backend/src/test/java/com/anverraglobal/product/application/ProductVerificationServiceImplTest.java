package com.anverraglobal.product.application;

import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductVerificationServiceImplTest {

    @Mock
    private ProductManagementApplicationService service;

    private ProductVerificationServiceImpl verificationService;

    @BeforeEach
    void setUp() {
        verificationService = new ProductVerificationServiceImpl(service);
    }

    @Test
    void verifyProductActive_whenActive_succeeds() {
        Product product = Product.create("Motor Plan A", com.anverraglobal.product.domain.ProductCategory.MOTOR_INSURANCE);
        when(service.getProduct(product.getId())).thenReturn(product);

        assertThatCode(() -> verificationService.verifyProductActive(product.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void verifyProductActive_whenInactive_throwsIllegalStateException() {
        Product product = Product.create("Motor Plan A", com.anverraglobal.product.domain.ProductCategory.MOTOR_INSURANCE);
        product.deactivate();
        when(service.getProduct(product.getId())).thenReturn(product);

        assertThatThrownBy(() -> verificationService.verifyProductActive(product.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product is not ACTIVE");
    }

    @Test
    void verifyProductActive_whenNotFound_throwsNoSuchElementException() {
        UUID unknownId = UUID.randomUUID();
        when(service.getProduct(unknownId)).thenThrow(new NoSuchElementException("Product not found: " + unknownId));

        assertThatThrownBy(() -> verificationService.verifyProductActive(unknownId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Product not found");
    }
}
