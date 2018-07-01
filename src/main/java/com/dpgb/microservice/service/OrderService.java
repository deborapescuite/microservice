package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.OrderItem;

import com.dpgb.microservice.exception.OrderNotFoundException;
import com.dpgb.microservice.exception.OrderOfOtherUserException;
import com.dpgb.microservice.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    public Order findById(Integer id) {
        logger.info("Find order by id: " + id);

        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            com.dpgb.microservice.entity.User user = getAuthUser();
            if ((optional.get().getUserId() == user.getId()) || user.getUserType().name() == com.dpgb.microservice.utils.UserType.ADMIN.name())
                return optional.get();
            else
                throw new OrderOfOtherUserException("This order: " + id + " belongs to user: " + optional.get().getUserId() + ".You are not allowed to see it.");
        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.");
    }

    private com.dpgb.microservice.entity.User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) authentication.getPrincipal();
        com.dpgb.microservice.entity.User customUser = userService.findByName(loggedUser.getUsername());
        return customUser;
    }

    public List<Order> findAll() {
        com.dpgb.microservice.entity.User user = getAuthUser();

        if (user.getUserType().name() == com.dpgb.microservice.utils.UserType.ADMIN.name()) {
            logger.info("Find all orders.");
            return orderRepository.findAll();
        } else {

            logger.info("Find all orders by user id: " + user.getId());
            return orderRepository.findByUserId(user.getId());
        }
    }

    public void delete(Integer id) {
        if (orderRepository.findById(id).isPresent()) {
            logger.info("Delete order - id: " + id);
            orderRepository.deleteById(id);
        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.Could not be deleted.");
    }

    public Order update(Integer id, Order updateOrder) {
        if (orderRepository.findById(id).isPresent()) {
            logger.info("Update order - id: " + id);
            return orderRepository.save(updateOrder);
        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.Could not be updated.");
    }

    public Order save(Order createOrder) {
        logger.info("Save order.");
        userService.findById(createOrder.getUserId());
        createOrder.setTotalPrice(generateTotalPrice(createOrder.getOrderItems()));
        verifyProduct(createOrder.getOrderItems());
        return orderRepository.save(createOrder);
    }

    private Double generateTotalPrice(List<OrderItem> orderItems) {
        logger.info("Generate order total price.");
        Double total = 0.0;
        for (OrderItem orderItem : orderItems) {
            if (orderItem != null) {
                total = (orderItem.getUnitValue() * orderItem.getQuantity()) + total;
            }
        }
        return total;
    }

    public void verifyProduct(List<OrderItem> orderItems) {
        logger.info("Verify products existence");
        for (OrderItem orderItem : orderItems) {
            productService.findById(orderItem.getProduct().getId());
        }
    }
}
