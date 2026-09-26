package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInvoiceRepository
        extends JpaRepository<CustomerInvoice, Long> {

    List<CustomerInvoice> findByCustomerId(Long customerId);

    List<CustomerInvoice> findBySalesOrderId(Long salesOrderId);
}