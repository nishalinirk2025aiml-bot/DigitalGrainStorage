package com.example.DigitalGrainStorage.repository;

import com.example.DigitalGrainStorage.model.InventoryLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryLedgerRepository
        extends JpaRepository<InventoryLedger, Long> {

    List<InventoryLedger> findByGrainLotId(Long grainLotId);

    List<InventoryLedger> findByWarehouseId(Long warehouseId);

    List<InventoryLedger> findByTransactionType(String transactionType);
}