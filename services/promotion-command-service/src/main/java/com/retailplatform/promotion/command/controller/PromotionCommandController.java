package com.retailplatform.promotion.command.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.promotion.command.dto.CreatePromotionRequest;
import com.retailplatform.promotion.command.service.PromotionCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotions/commands")
@RequiredArgsConstructor
public class PromotionCommandController {

    private final PromotionCommandService promotionCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<UUID>> createPromotion(@Valid @RequestBody CreatePromotionRequest request) {
        UUID id = promotionCommandService.createPromotion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(id, null));
    }
}
