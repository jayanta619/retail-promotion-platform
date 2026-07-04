package com.retailplatform.promotion.query.repository;

import com.retailplatform.promotion.query.document.PromotionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface PromotionSearchRepository extends ElasticsearchRepository<PromotionDocument, String> {
    List<PromotionDocument> findByZoneCodeAndStatus(String zoneCode, String status);
    List<PromotionDocument> findByNameContainingIgnoreCase(String name);
}
