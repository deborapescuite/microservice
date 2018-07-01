package com.dpgb.microservice.repository;

import com.dpgb.microservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order,Integer> {

    List<Order> findByUserId(Integer userId);
}
