package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.Warehouse;
import com.example.DigitalGrainStorage.service.GrainStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin
public class WarehouseController {

    private final GrainStorageService service;

    public WarehouseController(GrainStorageService service) {
        this.service = service;
    }

    // Create warehouse
    @PostMapping
    public Warehouse createWarehouse(@RequestBody Warehouse warehouse) {
        return service.createWarehouse(warehouse);
    }

    // Get all warehouses
    @GetMapping
    public List<Warehouse> getAllWarehouses() {
        return service.getAllWarehouses();
    }

    // Get warehouse by ID
    @GetMapping("/{id}")
    public Warehouse getWarehouse(@PathVariable Long id) {
        return service.getWarehouse(id);
    }

    // Check warehouse capacity
    @GetMapping("/{id}/capacity")
    public Object getCapacity(@PathVariable Long id) {
        return service.getWarehouseCapacity(id);
    }
}