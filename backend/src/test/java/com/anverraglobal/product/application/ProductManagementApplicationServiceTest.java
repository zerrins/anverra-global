package com.anverraglobal.product.application;

import com.anverraglobal.product.application.port.outbound.ProductRepositoryPort;
import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductCategory;
import com.anverraglobal.product.domain.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductManagementApplicationServiceTest {

    @Mock
    private ProductRepositoryPort repository;

    private ProductManagementApplicationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ProductManagementApplicationService(repository);
    }

    @Test
    void testCreateProductSuccess() {
        when(repository.existsByNameAndCategory("Test", ProductCategory.LIFE_INSURANCE.name())).thenReturn(false);
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product product = service.createProduct("Test", "LIFE_INSURANCE");

        assertThat(product.getName()).isEqualTo("Test");
        assertThat(product.getCategory()).isEqualTo(ProductCategory.LIFE_INSURANCE);
        verify(repository).save(any(Product.class));
    }

    @Test
    void testCreateProductDuplicateThrowsException() {
        when(repository.existsByNameAndCategory("Test", ProductCategory.LIFE_INSURANCE.name())).thenReturn(true);

        assertThatThrownBy(() -> service.createProduct("Test", "LIFE_INSURANCE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
                
        verify(repository, never()).save(any());
    }

    @Test
    void testGetProductSuccess() {
        UUID id = UUID.randomUUID();
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        when(repository.findById(id)).thenReturn(Optional.of(p));

        Product result = service.getProduct(id);
        
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test");
    }

    @Test
    void testGetProductNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProduct(id))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testSearchProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        Page<Product> page = new PageImpl<>(List.of(p));
        when(repository.search("Test", "LIFE_INSURANCE", "ACTIVE", pageable)).thenReturn(page);

        Page<Product> result = service.searchProducts("Test", "LIFE_INSURANCE", "ACTIVE", pageable);
        
        assertThat(result.getContent()).hasSize(1);
        verify(repository).search("Test", "LIFE_INSURANCE", "ACTIVE", pageable);
    }

    @Test
    void testUpdateProductSuccess() {
        UUID id = UUID.randomUUID();
        Product p = Product.create("Old", ProductCategory.LIFE_INSURANCE);
        when(repository.findById(id)).thenReturn(Optional.of(p));
        when(repository.existsByNameAndCategoryAndIdNot("New", ProductCategory.HEALTH_INSURANCE.name(), id)).thenReturn(false);
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = service.updateProduct(id, "New", "HEALTH_INSURANCE");

        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getCategory()).isEqualTo(ProductCategory.HEALTH_INSURANCE);
        verify(repository).save(p);
    }

    @Test
    void testUpdateProductDuplicateThrowsException() {
        UUID id = UUID.randomUUID();
        Product p = Product.create("Old", ProductCategory.LIFE_INSURANCE);
        when(repository.findById(id)).thenReturn(Optional.of(p));
        when(repository.existsByNameAndCategoryAndIdNot("New", ProductCategory.HEALTH_INSURANCE.name(), id)).thenReturn(true);

        assertThatThrownBy(() -> service.updateProduct(id, "New", "HEALTH_INSURANCE"))
                .isInstanceOf(IllegalArgumentException.class);
                
        verify(repository, never()).save(any());
    }

    @Test
    void testActivateProduct() {
        UUID id = UUID.randomUUID();
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        p.deactivate();
        
        when(repository.findById(id)).thenReturn(Optional.of(p));
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        service.activateProduct(id);

        assertThat(p.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        verify(repository).save(p);
    }

    @Test
    void testDeactivateProduct() {
        UUID id = UUID.randomUUID();
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        
        when(repository.findById(id)).thenReturn(Optional.of(p));
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        service.deactivateProduct(id);

        assertThat(p.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        verify(repository).save(p);
    }
}
