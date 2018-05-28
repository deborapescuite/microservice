package com.dpgb.microservice.repository;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createAndFindOrderTest() {
        Product product1 = new Product();
        product1.setName("Scissor");
        product1.setDescription("Basic Scissor");
        product1.setUnitValue(5.0d);
        product1.setQuantity(1);

        Product product2 = new Product();
        product2.setName("Eraser");
        product2.setDescription("White eraser");
        product2.setUnitValue(2.60d);
        product2.setQuantity(1);
        List<Product> productList = new ArrayList<Product>();
        productList.add(product1);
        productList.add(product2);

        Order order = new Order();
        order.setId(1);
        order.setCreationDate(new Date());
        order.setProductList(productList);
        order.setUserID(1);

        Integer id = orderRepository.save(order).getId();

        Order result = orderRepository.findById(id).get();

        assertThat(result.getUserID())
                .isEqualTo(order.getUserID());
    }

    @Test
    public void updateOrderTest() {
        Product product = new Product();
        product.setName("Pen");
        product.setDescription("Red pen");
        product.setUnitValue(1.25d);
        product.setQuantity(1);

        List<Product> productList = new ArrayList<Product>();
        productList.add(product);

        Order order = new Order();
        order.setId(1);
        order.setCreationDate(new Date());
        order.setProductList(productList);
        order.setUserID(1);

        Integer orderId = orderRepository.save(order).getId();

        order.setUserID(2);

        orderRepository.save(order);

        assertThat(orderRepository.getOne(orderId).getUserID().equals(2));
    }

    @Test
    public void deleteOrderTest() {
        Order order = new Order();
        order.setId(1);
        order.setCreationDate(new Date());
        order.setProductList(new ArrayList<Product>());
        order.setUserID(1);
        order.setTotalPrice(0d);

        Order savedOrder = orderRepository.save(order);

        orderRepository.delete(savedOrder);

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList.isEmpty());
    }
}