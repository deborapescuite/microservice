package com.dpgb.microservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Entity
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private User creatorUser;
    @NotNull
    private Product product;
    @NotNull
    @Size(min=0, message = "Quantity should be greater than 0")
    private Integer quantity;
    @NotNull
    @Size(min=0, message = "Price should be greater than 0")
    private Double price;
    private LocalTime creationDate;
}
