package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.repository.AuditRepository;
import com.dpgb.microservice.repository.ProductRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    AuditRepository auditRepository;

    ObjectMapper mapper = new ObjectMapper();

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Paper");
        product.setDescription("Whiter paper for school projects");
        product.setUnitValue(1.25);
        product.setQuantity(1);

    }

    @Test
    public void createProductWithSuccess() throws Exception {
        when(productRepository.save(this.product)).thenReturn(this.product);
         mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeProducts(this.product)))
                .andExpect(status().isCreated());

    }

    @Test
    public void createProductWithError() throws Exception {
        Product emptyProduct = new Product();

        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeProducts(emptyProduct)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateProduct() throws Exception {

        when(productRepository.findById(1)).thenReturn(Optional.of(this.product));

        mvc.perform(
                put("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializeProducts(this.product)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getProduct() throws Exception {
        when(productRepository.findById(1)).thenReturn(Optional.of(this.product));

        mvc.perform(get("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.product.getId()))
                .andExpect(jsonPath("$.name").value("Paper"))
                .andExpect(jsonPath("$.description").value("Whiter paper for school projects"));
    }

    public byte[] serializeProducts(Product product) throws JsonProcessingException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(product);
    }

    @Test
    public void getAllProducts() throws Exception {
        mvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProduct() throws Exception {
        when(productRepository.findById(1)).thenReturn(Optional.of(this.product));
        doNothing().when(productRepository).deleteById(this.product.getId());

        mvc.perform(
                delete("/products/{id}", this.product.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}