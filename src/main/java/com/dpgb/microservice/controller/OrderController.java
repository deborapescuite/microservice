package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.service.OrderService;
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
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @RequestMapping(method = GET, value = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Order getOrder(@PathVariable Integer id) {
        logger.info("GET a order with id: " + id);
        return orderService.findById(id);
    }

    @RequestMapping(method = GET, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        logger.info("GET all orders created.");
        return orderService.findAll();
    }

    @RequestMapping(method = DELETE, value = "/order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Integer id) {
        logger.info("DELETE  a order with id: " + id);
        orderService.delete(id);
    }

    @RequestMapping(method = PUT, value = "/order/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Order updateOrder(@PathVariable Integer id, @Valid @RequestBody Order updateOrder) {
        logger.info("UPDATE/PUT  a order with id: " + id);
        return orderService.update(id, updateOrder);
    }

    @RequestMapping(method = POST, value = "/order",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order createOrder, UriComponentsBuilder ucb) {
        logger.info("CREATE/POST  a new order.");

        Order order = orderService.save(createOrder);

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
