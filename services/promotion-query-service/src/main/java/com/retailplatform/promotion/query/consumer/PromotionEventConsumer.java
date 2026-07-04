package com.retailplatform.promotion.query.consumer;

import com.retailplatform.promotion.query.document.PromotionDocument;
import com.retailplatform.promotion.query.event.PromotionCreatedEvent;
import com.retailplatform.promotion.query.repository.PromotionSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromotionEventConsumer {

    private final PromotionSearchRepository searchRepository;

    @KafkaListener(topics = "promotion.created.v1", groupId = "promotion-query-service")
    public void onPromotionCreated(PromotionCreatedEvent event) {
        log.info("Received PromotionCreatedEvent for promotionId={}", event.getPromotionId());

        PromotionDocument document = new PromotionDocument();
        document.setId(event.getPromotionId().toString());
        document.setName(event.getName());
        document.setType(event.getType());
        document.setStatus("DRAFT");
        document.setDiscountValue(event.getDiscountValue());
        document.setZoneCode(event.getZoneCode());
        document.setStartDate(event.getStartDate());
        document.setEndDate(event.getEndDate());

        searchRepository.save(document);
    }
}
