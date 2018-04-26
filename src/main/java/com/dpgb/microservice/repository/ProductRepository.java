package com.dpgb.microservice.repository;

import com.dpgb.microservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{

}
