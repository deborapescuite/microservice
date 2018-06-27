package com.dpgb.microservice.utils;

public enum UserType {

    ADMIN, CUSTOMER;

    public String getUserType() {
        return name();
    }

}
