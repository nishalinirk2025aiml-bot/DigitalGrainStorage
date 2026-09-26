package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.SalesOrder;
import com.example.DigitalGrainStorage.model.CustomerInvoice;
import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin
public class SalesController {

    private final GrainStorageService service;

    public SalesController(GrainStorageService service) {
        this.service = service;
    }

    // Create Sales Order
    @PostMapping("/orders")
    public SalesOrder createSalesOrder(
            @RequestBody SalesOrder salesOrder) {

        return service.createSalesOrder(salesOrder);
    }

    // Get all Sales Orders
    @GetMapping("/orders")
    public List<SalesOrder> getAllSalesOrders() {
        return service.getAllSalesOrders();
    }

    // Create Customer Invoice
    @PostMapping("/invoices")
    public CustomerInvoice createCustomerInvoice(
            @RequestBody CustomerInvoice invoice) {

        return service.createCustomerInvoice(invoice);
    }

    // Get all Customer Invoices
    @GetMapping("/invoices")
    public List<CustomerInvoice> getAllCustomerInvoices() {
        return service.getAllCustomerInvoices();
    }
}