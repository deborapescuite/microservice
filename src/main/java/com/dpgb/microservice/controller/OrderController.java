package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Audit;
import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.exception.OrderNotFoundException;
import com.dpgb.microservice.repository.AuditRepository;
import com.dpgb.microservice.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuditRepository auditRepository;

    @RequestMapping(method = GET, value = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Order getOrder(@PathVariable Integer id) {
        logger.info("GET a order with id: " + id);
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new OrderNotFoundException("Order id: " + id + " not found.");
    }

    @RequestMapping(method = GET, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        logger.info("GET all orders created.");
        return orderRepository.findAll();
    }

    @RequestMapping(method = DELETE, value = "/order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Integer id) {
        logger.info("DELETE  a order with id: " + id);
        if (orderRepository.findById(id).isPresent()) {
            orderRepository.deleteById(id);
            //TO DO : add user id
            auditRepository.save(new Audit("Delete order", id, LocalTime.now(), null));
        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.Could not be deleted.");
    }

    @RequestMapping(method = PUT, value = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Order updateOrder(@PathVariable Integer id, @Valid @RequestBody Order updateOrder) {
        logger.info("UPDATE/PUT  a order with id: " + id);
        if (orderRepository.findById(id).isPresent()) {
            //TO DO : add user id
            auditRepository.save(new Audit("Update order", id, LocalTime.now(), null));
            return orderRepository.save(updateOrder);

        } else
            throw new OrderNotFoundException("Order id: " + id + " not found.Could not be updated.");
    }

    @RequestMapping(method = POST, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order createOrder, UriComponentsBuilder ucb) {
        logger.info("CREATE/POST  a new order.");
        List<Product> productList = createOrder.getProductList();
        Double total = 0.0;
        if (productList != null) {
            for (Product product : productList) {
                if (product != null) {
                    total += product.getUnitValue() * product.getQuantity();
                }
            }
        }
        createOrder.setTotalPrice(total);
        Order order = orderRepository.save(createOrder);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri =
                ucb.path("/order/")
                        .path(String.valueOf(order.getId()))
                        .build()
                        .toUri();
        headers.setLocation(locationUri);
        ResponseEntity<Order> responseEntity =
                new ResponseEntity<Order>(
                        order, headers, HttpStatus.CREATED);
        return responseEntity;
    }

}
