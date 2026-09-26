package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.QualityTest;
import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality-tests")
@CrossOrigin
public class QualityController {

    private final GrainStorageService service;

    public QualityController(GrainStorageService service) {
        this.service = service;
    }

    // Perform quality test
    @PostMapping
    public QualityTest createQualityTest(
            @RequestBody QualityTest qualityTest) {

        return service.createQualityTest(qualityTest);
    }

    // Get all quality tests
    @GetMapping
    public List<QualityTest> getAllQualityTests() {
        return service.getAllQualityTests();
    }

    // Get quality test by ID
    @GetMapping("/{id}")
    public QualityTest getQualityTest(@PathVariable Long id) {
        return service.getQualityTest(id);
    }
}