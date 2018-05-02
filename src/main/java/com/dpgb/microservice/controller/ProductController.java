package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.exception.ProductNotFoundException;
import com.dpgb.microservice.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProductController {
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    ProductRepository productRepository;

    @RequestMapping(method = GET, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable Integer id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new ProductNotFoundException("Product id: " + id + " not found.");
    }

    @RequestMapping(method = GET, value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @RequestMapping(method = DELETE, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
    }

    @RequestMapping(method = PUT, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product updateProduct(@PathVariable Integer id, @Valid @RequestBody Product updateProduct) {
        if (productRepository.findById(id).isPresent())
            return productRepository.save(updateProduct);
        else
            throw new ProductNotFoundException("Product id: " + id + " not found.");
    }

    @RequestMapping(method = POST, value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createProduct(@Valid @RequestBody Product createProduct) {
        productRepository.save(createProduct);
    }

}
