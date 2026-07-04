package com.retailplatform.product.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.product.entity.Product;
import com.retailplatform.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repository;

    @GetMapping
    public ApiResponse<Page<Product>> list(Pageable pageable) {
        return ApiResponse.ok(repository.findAll(pageable), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getById(@PathVariable UUID id) {
        return ApiResponse.ok(repository.findById(id).orElseThrow(), null);
    }

    @PostMapping
    public ApiResponse<Product> create(@RequestBody Product entity) {
        return ApiResponse.ok(repository.save(entity), null);
    }
}
