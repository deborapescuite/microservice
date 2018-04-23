package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ProductController {
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @RequestMapping(value="/product",produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(){
        Product o = new Product();
        o.setId(1);
        o.setName("teste");
        return o;
    }
}
