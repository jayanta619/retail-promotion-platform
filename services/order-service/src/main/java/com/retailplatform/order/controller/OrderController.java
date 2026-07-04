package com.retailplatform.order.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.order.entity.Order;
import com.retailplatform.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;

    @GetMapping
    public ApiResponse<Page<Order>> list(Pageable pageable) {
        return ApiResponse.ok(repository.findAll(pageable), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getById(@PathVariable UUID id) {
        return ApiResponse.ok(repository.findById(id).orElseThrow(), null);
    }

    @PostMapping
    public ApiResponse<Order> create(@RequestBody Order entity) {
        return ApiResponse.ok(repository.save(entity), null);
    }
}
