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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER') ")
    @RequestMapping(method = GET, value = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Order getOrder(@PathVariable Integer id) {
        return orderService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER') ")
    @RequestMapping(method = GET, value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = DELETE, value = "/order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = PUT, value = "/order/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Order updateOrder(@PathVariable Integer id, @Valid @RequestBody Order updateOrder) {
        return orderService.update(id, updateOrder);
    }

    @PreAuthorize("hasAuthority('CUSTOMER') ")
    @RequestMapping(method = POST, value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order createOrder, UriComponentsBuilder ucb) {
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
