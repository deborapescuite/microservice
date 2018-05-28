package com.dpgb.microservice.repository;

import com.dpgb.microservice.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createAndFindProductTest() {
        Product product = new Product();
        product.setName("Pencil");
        product.setDescription("Faber Castel Pencil");
        product.setUnitValue(3.5d);
        product.setQuantity(1);

        Integer id = productRepository.save(product).getId();

        Product result = productRepository.findById(id).get();

        assertThat(result.getName())
                .isEqualTo(product.getName());
    }

    @Test
    public void updateProductTest() {
        Product product = new Product();
        product.setName("Pen");
        product.setDescription("Red pen");
        product.setUnitValue(1.25d);
        product.setQuantity(1);

        Integer productId = productRepository.save(product).getId();

        product.setUnitValue(2.50d);

        productRepository.save(product);

        assertThat(productRepository.getOne(productId).getUnitValue()).isEqualTo(2.50d);
    }

    @Test
    public void deleteProductTest() {
        Product product = new Product();
        product.setName("Scissor");
        product.setDescription("Basic Scissor");
        product.setUnitValue(10.59d);
        product.setQuantity(1);

        productRepository.save(product);

        productRepository.delete(product);

        List<Product> productList = productRepository.findAll();

        assertThat(product).isNotIn(productList);
    }
}