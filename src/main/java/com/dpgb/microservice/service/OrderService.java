package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.exception.OrderNotFoundException;
import com.dpgb.microservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Order findById(Integer id) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new OrderNotFoundException("Order id: " + id + " not found.");
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void delete(Integer id) {
        if (orderRepository.findById(id).isPresent()) {
            orderRepository.deleteById(id);
        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.Could not be deleted.");
    }

    public Order update(Integer id, Order updateOrder) {
        if (orderRepository.findById(id).isPresent()) {
            return orderRepository.save(updateOrder);
        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.Could not be updated.");
    }

    public Order save(Order createOrder) {
        List<Product> productList = createOrder.getProductList();
        Double total = 0.0;
        if (productList != null) {
            for (Product product : productList) {
                if (product != null) {
                    total = (product.getUnitValue() * product.getQuantity())+ total;
                }
            }
        }
        createOrder.setTotalPrice(total);
        return orderRepository.save(createOrder);

    }
}
