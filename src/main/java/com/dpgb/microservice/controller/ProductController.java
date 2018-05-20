package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Audit;
import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.exception.ProductNotFoundException;
import com.dpgb.microservice.repository.AuditRepository;
import com.dpgb.microservice.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ProductController {
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AuditRepository auditRepository;

    @RequestMapping(method = GET, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable Integer id) {
        logger.info("GET a product with id: " + id);
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new ProductNotFoundException("Product id: " + id + " not found.");
    }

    @RequestMapping(method = GET, value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        logger.info("GET all products created.");
        return productRepository.findAll();
    }

    @RequestMapping(method = DELETE, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) {
        logger.info("DELETE  a product with id: " + id);
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            //TO DO : add user id
            auditRepository.save(new Audit("Delete product", id, LocalTime.now(), null));
        } else
            throw new ProductNotFoundException("Product id: " + id + " not found.Could not be deleted.");
    }

    @RequestMapping(method = PUT, value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Product updateProduct(@PathVariable Integer id, @Valid @RequestBody Product updateProduct) {
        logger.info("UPDATE/PUT  a product with id: " + id);
        if (productRepository.findById(id).isPresent()) {
            //TO DO : add user id
            auditRepository.save(new Audit("Update product", id, LocalTime.now(), null));
            return productRepository.save(updateProduct);

        } else
            throw new ProductNotFoundException("Product id: " + id + " not found.Could not be updated.");
    }

    @RequestMapping(method = POST, value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@Valid @RequestBody Product createProduct) {
        logger.info("CREATE/POST  a new product.");
        productRepository.save(createProduct);
    }

}
