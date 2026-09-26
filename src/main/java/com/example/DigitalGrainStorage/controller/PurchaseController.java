package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.PurchaseOrder;
import com.example.DigitalGrainStorage.model.VendorBill;
import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin
public class PurchaseController {

    private final GrainStorageService service;

    public PurchaseController(GrainStorageService service) {
        this.service = service;
    }

    // Create Purchase Order
    @PostMapping("/orders")
    public PurchaseOrder createPurchaseOrder(
            @RequestBody PurchaseOrder purchaseOrder) {

        return service.createPurchaseOrder(purchaseOrder);
    }

    // Get all Purchase Orders
    @GetMapping("/orders")
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return service.getAllPurchaseOrders();
    }

    // Create Vendor Bill
    @PostMapping("/bills")
    public VendorBill createVendorBill(
            @RequestBody VendorBill vendorBill) {

        return service.createVendorBill(vendorBill);
    }

    // Get all Vendor Bills
    @GetMapping("/bills")
    public List<VendorBill> getAllVendorBills() {
        return service.getAllVendorBills();
    }
}