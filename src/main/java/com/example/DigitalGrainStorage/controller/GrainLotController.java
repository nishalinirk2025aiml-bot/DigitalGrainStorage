package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.GrainLot;
import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grain-lots")
@CrossOrigin
public class GrainLotController {

    private final GrainStorageService service;

    public GrainLotController(GrainStorageService service) {
        this.service = service;
    }

    // Add a new grain lot
    @PostMapping
    public GrainLot createGrainLot(@RequestBody GrainLot grainLot) {
        return service.createGrainLot(grainLot);
    }

    // Get all grain lots
    @GetMapping
    public List<GrainLot> getAllGrainLots() {
        return service.getAllGrainLots();
    }

    // Get grain lot by ID
    @GetMapping("/{id}")
    public GrainLot getGrainLot(@PathVariable Long id) {
        return service.getGrainLot(id);
    }
}