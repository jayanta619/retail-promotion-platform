package com.retailplatform.inventory.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.inventory.entity.Inventory;
import com.retailplatform.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository repository;

    @GetMapping
    public ApiResponse<Page<Inventory>> list(Pageable pageable) {
        return ApiResponse.ok(repository.findAll(pageable), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Inventory> getById(@PathVariable UUID id) {
        return ApiResponse.ok(repository.findById(id).orElseThrow(), null);
    }

    @PostMapping
    public ApiResponse<Inventory> create(@RequestBody Inventory entity) {
        return ApiResponse.ok(repository.save(entity), null);
    }
}
