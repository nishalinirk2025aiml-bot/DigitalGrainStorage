package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.VendorBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorBillRepository extends JpaRepository<VendorBill, Long> {

    List<VendorBill> findByVendorId(Long vendorId);

    List<VendorBill> findByPurchaseOrderId(Long purchaseOrderId);
}