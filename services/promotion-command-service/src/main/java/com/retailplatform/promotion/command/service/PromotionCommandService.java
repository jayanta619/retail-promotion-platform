package com.retailplatform.promotion.command.service;

import com.retailplatform.promotion.command.dto.CreatePromotionRequest;
import com.retailplatform.promotion.command.entity.Promotion;
import com.retailplatform.promotion.command.entity.PromotionStatus;
import com.retailplatform.promotion.command.event.PromotionCreatedEvent;
import com.retailplatform.promotion.command.producer.PromotionEventPublisher;
import com.retailplatform.promotion.command.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionCommandService {

    private final PromotionRepository promotionRepository;
    private final PromotionEventPublisher eventPublisher;

    @Transactional
    public UUID createPromotion(CreatePromotionRequest request) {
        Promotion promotion = new Promotion();
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setType(request.getType());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setZoneCode(request.getZoneCode());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setStatus(PromotionStatus.DRAFT);

        Promotion saved = promotionRepository.save(promotion);

        eventPublisher.publishPromotionCreated(new PromotionCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getType().name(),
                saved.getDiscountValue(),
                saved.getZoneCode(),
                saved.getStartDate(),
                saved.getEndDate(),
                Instant.now()
        ));

        return saved.getId();
    }
}
