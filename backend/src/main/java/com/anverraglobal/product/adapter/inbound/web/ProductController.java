package com.anverraglobal.product.adapter.inbound.web;

import com.anverraglobal.product.application.ProductManagementApplicationService;
import com.anverraglobal.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductManagementApplicationService service;

    public ProductController(ProductManagementApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        Product product = service.createProduct(request.name(), request.category());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(product));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> listProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Page<Product> products = service.searchProducts(name, category, status, pageable);
        return ResponseEntity.ok(products.map(this::mapToResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        Product product = service.getProduct(id);
        return ResponseEntity.ok(mapToResponse(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody UpdateProductRequest request) {
        Product product = service.updateProduct(id, request.name(), request.category());
        return ResponseEntity.ok(mapToResponse(product));
    }

    @PostMapping("/{id}/lifecycle/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable UUID id) {
        service.activateProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/lifecycle/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable UUID id) {
        service.deactivateProduct(id);
        return ResponseEntity.ok().build();
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory().name(),
                product.getStatus().name(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getVersion()
        );
    }

    public record CreateProductRequest(String name, String category) {}
    public record UpdateProductRequest(String name, String category) {}
    public record ProductResponse(UUID id, String name, String category, String status, Instant createdAt, Instant updatedAt, Long version) {}
}
