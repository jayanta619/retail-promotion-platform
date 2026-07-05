package com.retailplatform.promotion.query.controller;

import com.retailplatform.common.model.ApiResponse;
import com.retailplatform.promotion.query.document.PromotionDocument;
import com.retailplatform.promotion.query.service.PromotionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionQueryController {

    private final PromotionQueryService promotionQueryService;

    @GetMapping("/{id}")
    public ApiResponse<PromotionDocument> getById(@PathVariable("id") String id) {
        return ApiResponse.ok(promotionQueryService.getPromotionById(id), null);
    }

    @GetMapping("/search")
    public ApiResponse<List<PromotionDocument>> search(
            @RequestParam(value = "zoneCode", required = false) String zoneCode,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "name", required = false) String name) {

        if (name != null) {
            return ApiResponse.ok(promotionQueryService.searchByName(name), null);
        }
        return ApiResponse.ok(promotionQueryService.searchByZoneAndStatus(zoneCode, status), null);
    }
}
