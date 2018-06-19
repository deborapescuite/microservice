package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.repository.ProductRepository;
import com.dpgb.microservice.service.OrderService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    OrderService orderService;

    @MockBean
    ProductRepository productRepository;

    ObjectMapper mapper = new ObjectMapper();

    private Order order;

    @Before
    public void setUp() {

        Product product = new Product();
        product.setName("Plastic folder");
        product.setDescription("Red plastic folder");
        product.setUnitValue(10.50d);
        product.setQuantity(1);

        List<Product> productList = new ArrayList<Product>();
        productList.add(product);

        order = new Order();
        order.setId(1);
        order.setUserID(1);
        order.setProductList(productList);
    }

    public byte[] serializeOrder(Order order) throws JsonProcessingException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        byte[] test = mapper.writeValueAsBytes(order);
        return mapper.writeValueAsBytes(order);
    }
    @Test
    public void createOrderWithSuccess() throws Exception {
        when(orderService.save(this.order)).thenReturn(this.order);
        mvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeOrder(this.order)))
                .andExpect(status().isCreated());

    }

    @Test
    public void createOrderWithError() throws Exception {
        Order emptyOrder = new Order();

        mvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeOrder(emptyOrder)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateOrder() throws Exception {

        when(orderService.findById(1)).thenReturn(this.order);

        mvc.perform(
                put("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializeOrder(this.order)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getOrder() throws Exception {
        when(orderService.findById(1)).thenReturn(this.order);

        mvc.perform(get("/order/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.order.getId()))
                .andExpect(jsonPath("$.userID").value(1));
    }

    @Test
    public void getAllOrders() throws Exception {
        mvc.perform(get("/order")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteOrder() throws Exception {
        when(orderService.findById(1)).thenReturn(this.order);
        doNothing().when(orderService).delete(this.order.getId());

        mvc.perform(
                delete("/order/{id}", this.order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}