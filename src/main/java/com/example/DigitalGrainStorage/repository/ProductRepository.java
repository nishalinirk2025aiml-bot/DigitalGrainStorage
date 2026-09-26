package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}