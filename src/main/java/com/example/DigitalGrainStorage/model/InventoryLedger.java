package com.example.DigitalGrainStorage.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_ledger")
public class InventoryLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long grainLotId;

    private Long warehouseId;

    private String transactionType;

    private double quantity;

    private double balanceQuantity;

    private String referenceNumber;

    private LocalDateTime transactionDate;

    public InventoryLedger() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGrainLotId() {
        return grainLotId;
    }

    public void setGrainLotId(Long grainLotId) {
        this.grainLotId = grainLotId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getBalanceQuantity() {
        return balanceQuantity;
    }

    public void setBalanceQuantity(double balanceQuantity) {
        this.balanceQuantity = balanceQuantity;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}