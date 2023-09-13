package com.example.springboot.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.example.springboot.models.ProductModel;

//@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    
}
