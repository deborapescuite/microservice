package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Order;
import com.dpgb.microservice.entity.OrderItem;
import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.exception.OrderNotFoundException;
import com.dpgb.microservice.repository.ProductRepository;
import com.dpgb.microservice.service.OrderService;
import com.dpgb.microservice.utils.UserType;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    OrderService orderService;

    @MockBean
    ProductRepository productRepository;

    ObjectMapper mapper = new ObjectMapper();

    private Order order;

    private User user;

    private OrderItem orderItem;

    @Before
    public void setUp() {

        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        Product product = new Product();
        product.setName("Plastic folder");
        product.setUnitValue(10.50d);

        List<Product> productList = new ArrayList<Product>();
        productList.add(product);

        user = new User();
        user.setId(5);
        user.setName("TestUser");
        user.setUserType(UserType.ADMIN);

        orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setUnitValue(5.50);
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        orderItemList.add(orderItem);

        order = new Order();
        order.setId(1);
        order.setUserId(user.getId());
        order.setOrderItems(orderItemList);

    }

    public byte[] serializeOrder(Order order) throws JsonProcessingException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println(mapper.writeValueAsString(order));
        return mapper.writeValueAsBytes(order);
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void createOrderWithSuccess() throws Exception {
        when(orderService.save(this.order)).thenReturn(this.order);
        mvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(serializeOrder(this.order)))
                .andExpect(status().isCreated());

    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    public void updateOrder() throws Exception {

        when(orderService.findById(1)).thenReturn(this.order);

        mvc.perform(
                put("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(serializeOrder(this.order)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "CUSTOMER"})
    public void getOrder() throws Exception {
        when(orderService.findById(1)).thenReturn(this.order);

        mvc.perform(get("/order/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.order.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "CUSTOMER"})
    public void getAllOrders() throws Exception {
        mvc.perform(get("/order")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteOrder() throws Exception {
        when(orderService.findById(1)).thenReturn(this.order);
        doNothing().when(orderService).delete(this.order.getId());

        mvc.perform(
                delete("/order/{id}", this.order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "CUSTOMER"})
    public void getNotFoundOrder() throws Exception {
        when(orderService.findById(999)).thenThrow(OrderNotFoundException.class);
        mvc.perform(get("/order/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void createOrderWithError() throws Exception {
        Order emptyOrder = new Order();

        mvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeOrder(emptyOrder)))
                .andExpect(status().is4xxClientError());
    }

}
