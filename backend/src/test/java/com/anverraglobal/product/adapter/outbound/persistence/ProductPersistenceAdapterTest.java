package com.anverraglobal.product.adapter.outbound.persistence;

import com.anverraglobal.product.application.port.outbound.ProductRepositoryPort;
import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductCategory;
import com.anverraglobal.product.domain.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ProductPersistenceAdapter.class)
@Testcontainers
class ProductPersistenceAdapterTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private ProductRepositoryPort persistenceAdapter;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndLoadProduct() {
        Product product = Product.create("Persistence Test Product", ProductCategory.LIFE_INSURANCE);
        
        Product saved = persistenceAdapter.save(product);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        
        Product loaded = persistenceAdapter.findById(saved.getId()).orElseThrow();
        assertThat(loaded.getName()).isEqualTo("Persistence Test Product");
        assertThat(loaded.getCategory()).isEqualTo(ProductCategory.LIFE_INSURANCE);
        assertThat(loaded.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(loaded.getVersion()).isEqualTo(saved.getVersion());
    }

    @Test
    void testDynamicSearch() {
        Product p1 = persistenceAdapter.save(Product.create("Apple Health", ProductCategory.HEALTH_INSURANCE));
        Product p2 = persistenceAdapter.save(Product.create("Apple Life", ProductCategory.LIFE_INSURANCE));
        Product p3 = persistenceAdapter.save(Product.create("Banana Health", ProductCategory.HEALTH_INSURANCE));
        
        p3.deactivate();
        persistenceAdapter.save(p3);

        // Search by name
        Page<Product> searchName = persistenceAdapter.search("Apple", null, null, PageRequest.of(0, 10));
        assertThat(searchName.getContent()).hasSize(2);
        
        // Search by category
        Page<Product> searchCategory = persistenceAdapter.search(null, "HEALTH_INSURANCE", null, PageRequest.of(0, 10));
        assertThat(searchCategory.getContent()).hasSize(2);
        
        // Search by status
        Page<Product> searchStatus = persistenceAdapter.search(null, null, "INACTIVE", PageRequest.of(0, 10));
        assertThat(searchStatus.getContent()).hasSize(1);
        assertThat(searchStatus.getContent().get(0).getName()).isEqualTo("Banana Health");
        
        // Combined filters
        Page<Product> combined = persistenceAdapter.search("Apple", "HEALTH_INSURANCE", "ACTIVE", PageRequest.of(0, 10));
        assertThat(combined.getContent()).hasSize(1);
        assertThat(combined.getContent().get(0).getName()).isEqualTo("Apple Health");
        
        // Sorting and Pagination
        Page<Product> paged = persistenceAdapter.search(null, "HEALTH_INSURANCE", null, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name")));
        assertThat(paged.getContent()).hasSize(1);
        assertThat(paged.getTotalElements()).isEqualTo(2);
        assertThat(paged.getContent().get(0).getName()).isEqualTo("Apple Health");
    }

    @Test
    void testDuplicateConstraint() {
        persistenceAdapter.save(Product.create("Unique Test", ProductCategory.MOTOR_INSURANCE));
        Product duplicate = Product.create("Unique Test", ProductCategory.MOTOR_INSURANCE);
        assertThatThrownBy(() -> persistenceAdapter.save(duplicate))
                .isInstanceOf(org.springframework.data.relational.core.conversion.DbActionExecutionException.class);
    }

    @Test
    void testSameNameDifferentCategorySucceeds() {
        persistenceAdapter.save(Product.create("Unique Test 2", ProductCategory.MOTOR_INSURANCE));
        Product differentCategory = Product.create("Unique Test 2", ProductCategory.TRAVEL_INSURANCE);
        Product savedDiff = persistenceAdapter.save(differentCategory);
        assertThat(savedDiff.getId()).isNotNull();
    }

    @Test
    void testOptimisticLocking() {
        Product product = Product.create("Lock Test", ProductCategory.FIRE_INSURANCE);
        Product saved = persistenceAdapter.save(product);
        
        Product instance1 = persistenceAdapter.findById(saved.getId()).orElseThrow();
        Product instance2 = persistenceAdapter.findById(saved.getId()).orElseThrow();
        
        instance1.update("Lock Test 1", ProductCategory.FIRE_INSURANCE);
        persistenceAdapter.save(instance1);
        
        instance2.update("Lock Test 2", ProductCategory.FIRE_INSURANCE);
        
        assertThatThrownBy(() -> persistenceAdapter.save(instance2))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testLifecyclePersistence() {
        Product product = Product.create("Lifecycle Test", ProductCategory.MARINE_INSURANCE);
        Product saved = persistenceAdapter.save(product);
        assertThat(saved.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        
        saved.deactivate();
        Product savedInactive = persistenceAdapter.save(saved);
        assertThat(savedInactive.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        
        savedInactive.activate();
        Product savedActive = persistenceAdapter.save(savedInactive);
        assertThat(savedActive.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }
}
