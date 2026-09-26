package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.GrainLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrainLotRepository extends JpaRepository<GrainLot, Long> {

    List<GrainLot> findByWarehouseId(Long warehouseId);

    List<GrainLot> findByVendorId(Long vendorId);

    List<GrainLot> findByProductId(Long productId);
}