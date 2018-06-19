package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.exception.ProductNotFoundException;
import com.dpgb.microservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product findById(Integer id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new ProductNotFoundException("Product id: " + id + " not found.");
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void delete(Integer id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        } else
            throw new ProductNotFoundException("Product id: " + id + " not found.Could not be deleted.");
    }

    public Product update(Integer id, Product updateProduct) {
        if (productRepository.findById(id).isPresent()) {
            return productRepository.save(updateProduct);

        } else
            throw new ProductNotFoundException("Product id: " + id + " not found.Could not be updated.");
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
}