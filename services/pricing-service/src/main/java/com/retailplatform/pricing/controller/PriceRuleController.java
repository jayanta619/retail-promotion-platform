package com.retailplatform.pricing.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.pricing.entity.PriceRule;
import com.retailplatform.pricing.repository.PriceRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/price_rules")
@RequiredArgsConstructor
public class PriceRuleController {

    private final PriceRuleRepository repository;

    @GetMapping
    public ApiResponse<Page<PriceRule>> list(Pageable pageable) {
        return ApiResponse.ok(repository.findAll(pageable), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<PriceRule> getById(@PathVariable UUID id) {
        return ApiResponse.ok(repository.findById(id).orElseThrow(), null);
    }

    @PostMapping
    public ApiResponse<PriceRule> create(@RequestBody PriceRule entity) {
        return ApiResponse.ok(repository.save(entity), null);
    }
}
