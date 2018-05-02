package com.dpgb.microservice.controller;

import com.dpgb.microservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ProductRepository productRepository;


    @Test
    public void getProduct()  throws Exception{

    }

    @Test
    public void getAllProducts() {
    }

    @Test
    public void deleteProduct() {
    }

    @Test
    public void updateProduct() {
    }

    @Test
    public void createProduct() {
    }
}