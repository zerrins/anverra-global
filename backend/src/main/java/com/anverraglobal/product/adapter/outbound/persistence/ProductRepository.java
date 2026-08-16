package com.anverraglobal.product.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductEntity, UUID>, PagingAndSortingRepository<ProductEntity, UUID> {
    boolean existsByNameAndCategory(String name, String category);
    boolean existsByNameAndCategoryAndIdNot(String name, String category, UUID id);
}
