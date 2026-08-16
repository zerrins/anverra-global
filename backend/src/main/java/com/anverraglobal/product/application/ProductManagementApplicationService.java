package com.anverraglobal.product.application;

import com.anverraglobal.product.application.port.outbound.ProductRepositoryPort;
import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductManagementApplicationService {

    private final ProductRepositoryPort repository;

    public ProductManagementApplicationService(ProductRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product createProduct(String name, String categoryName) {
        ProductCategory category = ProductCategory.valueOf(categoryName);
        if (repository.existsByNameAndCategory(name, category.name())) {
            throw new IllegalArgumentException("Product with this name and category already exists");
        }
        Product product = Product.create(name, category);
        return repository.save(product);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product updateProduct(UUID id, String name, String categoryName) {
        ProductCategory category = ProductCategory.valueOf(categoryName);
        Product product = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        
        if (repository.existsByNameAndCategoryAndIdNot(name, category.name(), id)) {
            throw new IllegalArgumentException("Product with this name and category already exists");
        }
        
        product.update(name, category);
        return repository.save(product);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void activateProduct(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        product.activate();
        repository.save(product);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deactivateProduct(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        product.deactivate();
        repository.save(product);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Product getProduct(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<Product> searchProducts(String name, String category, String status, Pageable pageable) {
        return repository.search(name, category, status, pageable);
    }
}
