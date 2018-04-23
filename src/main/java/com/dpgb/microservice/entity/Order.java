package com.dpgb.microservice.entity;

import java.time.LocalTime;

public class Order
{
    private Integer id;
    private User creatorUser;
    private Product product;
    private Integer quantity;
    private Double price;
    private LocalTime creationDate;
}
