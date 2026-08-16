package com.anverraglobal.product.adapter.inbound.web;

import com.anverraglobal.product.application.ProductManagementApplicationService;
import com.anverraglobal.product.domain.Product;
import com.anverraglobal.product.domain.ProductCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductManagementApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testCreateProductAdmin() throws Exception {
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        Mockito.when(service.createProduct("Test", "LIFE_INSURANCE")).thenReturn(p);

        ProductController.CreateProductRequest req = new ProductController.CreateProductRequest("Test", "LIFE_INSURANCE");
        mockMvc.perform(post("/api/v1/products")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_AGENT")
    void testCreateProductAgentForbidden() throws Exception {
        Mockito.when(service.createProduct(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("Access Denied"));
                
        ProductController.CreateProductRequest req = new ProductController.CreateProductRequest("Test", "LIFE_INSURANCE");
        mockMvc.perform(post("/api/v1/products")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_AGENT")
    void testListProductsAllowed() throws Exception {
        Product p = Product.create("Test", ProductCategory.LIFE_INSURANCE);
        Mockito.when(service.searchProducts(null, null, null, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of(p)));

        mockMvc.perform(get("/api/v1/products?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test"));
    }
    
    @Test
    void testUnauthenticatedAccessRejected() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }
}
