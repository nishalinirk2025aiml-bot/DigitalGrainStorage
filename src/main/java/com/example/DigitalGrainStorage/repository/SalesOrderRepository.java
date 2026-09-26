package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    List<SalesOrder> findByCustomerId(Long customerId);
}