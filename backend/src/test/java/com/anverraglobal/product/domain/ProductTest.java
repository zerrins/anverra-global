package com.anverraglobal.product.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testCreateValidProduct() {
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        assertNotNull(p.getId());
        assertEquals("Test", p.getName());
        assertEquals(ProductCategory.LIFE_INSURANCE, p.getCategory());
        assertEquals(ProductStatus.ACTIVE, p.getStatus());
        assertNotNull(p.getCreatedAt());
        assertNotNull(p.getUpdatedAt());
    }
    
    @Test
    void testCreateInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> Product.create("", ProductCategory.LIFE_INSURANCE));
        assertThrows(IllegalArgumentException.class, () -> Product.create(null, ProductCategory.LIFE_INSURANCE));
    }
    
    @Test
    void testCreateInvalidCategory() {
        assertThrows(IllegalArgumentException.class, () -> Product.create("Test", null));
    }
    
    @Test
    void testLifecycle() {
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        p.deactivate();
        assertEquals(ProductStatus.INACTIVE, p.getStatus());
        p.activate();
        assertEquals(ProductStatus.ACTIVE, p.getStatus());
    }
}
