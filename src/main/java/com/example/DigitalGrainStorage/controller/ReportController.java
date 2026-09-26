package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class ReportController {

    private final GrainStorageService service;

    public ReportController(GrainStorageService service) {
        this.service = service;
    }

    // Balance Sheet
    @GetMapping("/balance-sheet")
    public Map<String, Object> getBalanceSheet() {
        return service.getBalanceSheet();
    }

    // Profit and Loss
    @GetMapping("/profit-loss")
    public Map<String, Object> getProfitLoss() {
        return service.getProfitLoss();
    }

    // Budget Report
    @GetMapping("/budget")
    public Map<String, Object> getBudgetReport() {
        return service.getBudgetReport();
    }
}