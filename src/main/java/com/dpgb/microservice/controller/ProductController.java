package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProductController {
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER') ")
    @RequestMapping(method = GET, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable Integer id) {
        logger.info("GET a product with id: " + id);
        return productService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER') ")
    @RequestMapping(method = GET, value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        logger.info("GET all products created.");
        return productService.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = DELETE, value = "/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) {
        logger.info("DELETE  a product with id: " + id);
        productService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = PUT, value = "/products/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Product updateProduct(@PathVariable Integer id, @Valid @RequestBody Product updateProduct) {
        logger.info("UPDATE/PUT  a product with id: " + id);
        return productService.update(id, updateProduct);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = POST, value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product createProduct, UriComponentsBuilder ucb) {
        logger.info("CREATE/POST  a new product.");
        Product product = productService.save(createProduct);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri =
                ucb.path("/products/")
                        .path(String.valueOf(product.getId()))
                        .build()
                        .toUri();
        headers.setLocation(locationUri);
        ResponseEntity<Product> responseEntity =
                new ResponseEntity<Product>(
                        product, headers, HttpStatus.CREATED);
        return responseEntity;
    }

}
