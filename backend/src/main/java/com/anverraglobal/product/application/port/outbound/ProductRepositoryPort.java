package com.anverraglobal.product.application.port.outbound;

import com.anverraglobal.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Page<Product> search(String name, String category, String status, Pageable pageable);
    boolean existsByNameAndCategory(String name, String category);
    boolean existsByNameAndCategoryAndIdNot(String name, String category, UUID id);
}
