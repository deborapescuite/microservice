package com.dpgb.microservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="tbl_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message="Name can't be null")
    @NotEmpty(message="Name can't be empty")
    private String name;
    @NotNull
    private String shortDescription;
    private String longDescription;
    @NotNull
    @Size(min=0, message = "UnitValue should be greater than 0")
    private Double unitValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Double getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Double unitValue) {
        this.unitValue = unitValue;
    }
}
