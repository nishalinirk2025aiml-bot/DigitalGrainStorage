package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.Payment;
import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    private final GrainStorageService service;

    public PaymentController(GrainStorageService service) {
        this.service = service;
    }

    // Create payment
    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return service.createPayment(payment);
    }

    // Get all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return service.getAllPayments();
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable Long id) {
        return service.getPayment(id);
    }
}
