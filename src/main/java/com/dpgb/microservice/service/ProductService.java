package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.exception.ProductNotFoundException;
import com.dpgb.microservice.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    public Product findById(Integer id) {
        logger.info("Find product by id:"+id);
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new ProductNotFoundException("Product id: " + id + " not found.");
    }

    public List<Product> findAll() {
        logger.info("Find all products.");
        return productRepository.findAll();
    }

    public void delete(Integer id) {
        logger.info("DELETE  a product with id: " + id + " by user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        } else
            throw new ProductNotFoundException("Product id: " + id + " not found.Could not be deleted.");
    }

    public Product update(Integer id, Product updateProduct) {
        logger.info("Update product id: " + id);
        if (productRepository.findById(id).isPresent()) {
            return productRepository.save(updateProduct);

        } else
            throw new ProductNotFoundException("Product id: " + id + " not found.Could not be updated.");
    }

    public Product save(Product product) {
        logger.info("Save product: "+ product.getName() );
        return productRepository.save(product);
    }
}