package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.OrderItem;
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

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;


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
        userService.findById(createOrder.getUserId());
        createOrder.setTotalPrice(generateTotalPrice(createOrder.getOrderItems()));
        verifyProduct(createOrder.getOrderItems());
        return orderRepository.save(createOrder);
    }

    private Double generateTotalPrice( List<OrderItem> orderItems) {
        Double total = 0.0;
        for (OrderItem orderItem : orderItems) {
             if (orderItem != null) {
                 total = (orderItem.getUnitValue() * orderItem.getQuantity())+ total;
             }
         }
        return total;
    }

    public void verifyProduct(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            productService.findById(orderItem.getProduct().getId());
        }
    }
}
