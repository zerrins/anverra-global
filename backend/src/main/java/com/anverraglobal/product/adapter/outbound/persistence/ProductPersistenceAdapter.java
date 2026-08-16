package com.anverraglobal.product.adapter.outbound.persistence;

import com.anverraglobal.product.application.port.outbound.ProductRepositoryPort;
import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductCategory;
import com.anverraglobal.product.domain.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {
    
    private final ProductRepository repository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductPersistenceAdapter(ProductRepository repository, NamedParameterJdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Product> search(String name, String category, String status, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1 ");
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM products WHERE 1=1 ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name ILIKE :name ");
            countSql.append(" AND name ILIKE :name ");
            params.addValue("name", "%" + name + "%");
        }
        if (category != null) {
            sql.append(" AND category = :category ");
            countSql.append(" AND category = :category ");
            params.addValue("category", category);
        }
        if (status != null) {
            sql.append(" AND status = :status ");
            countSql.append(" AND status = :status ");
            params.addValue("status", status);
        }
        
        Long total = jdbcTemplate.queryForObject(countSql.toString(), params, Long.class);
        
        sql.append(" ORDER BY ");
        if (pageable.getSort().isSorted()) {
            List<String> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                orders.add(order.getProperty() + " " + order.getDirection().name());
            });
            sql.append(String.join(", ", orders));
        } else {
            sql.append(" created_at DESC ");
        }
        
        sql.append(" LIMIT :limit OFFSET :offset");
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        
        List<ProductEntity> entities = jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            return new ProductEntity(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant(),
                rs.getLong("version")
            );
        });
        
        return new org.springframework.data.domain.PageImpl<>(
            entities.stream().map(this::toDomain).toList(), 
            pageable, 
            total != null ? total : 0
        );
    }

    @Override
    public boolean existsByNameAndCategory(String name, String category) {
        return repository.existsByNameAndCategory(name, category);
    }

    @Override
    public boolean existsByNameAndCategoryAndIdNot(String name, String category, UUID id) {
        return repository.existsByNameAndCategoryAndIdNot(name, category, id);
    }

    private ProductEntity toEntity(Product domain) {
        return new ProductEntity(
            domain.getId(),
            domain.getName(),
            domain.getCategory().name(),
            domain.getStatus().name(),
            domain.getCreatedAt(),
            domain.getUpdatedAt(),
            domain.getVersion()
        );
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
            entity.getId(),
            entity.getName(),
            ProductCategory.valueOf(entity.getCategory()),
            ProductStatus.valueOf(entity.getStatus()),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getVersion()
        );
    }
}
