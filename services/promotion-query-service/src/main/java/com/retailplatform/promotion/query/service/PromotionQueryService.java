package com.retailplatform.promotion.query.service;

import com.retailplatform.common.exception.ResourceNotFoundException;
import com.retailplatform.promotion.query.document.PromotionDocument;
import com.retailplatform.promotion.query.repository.PromotionSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionQueryService {

    private final PromotionSearchRepository searchRepository;

    @Cacheable(value = "promotion", key = "#id")
    public PromotionDocument getPromotionById(String id) {
        return searchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found: " + id));
    }

    public List<PromotionDocument> searchByZoneAndStatus(String zoneCode, String status) {
        return searchRepository.findByZoneCodeAndStatus(zoneCode, status);
    }

    public List<PromotionDocument> searchByName(String name) {
        return searchRepository.findByNameContainingIgnoreCase(name);
    }
}
