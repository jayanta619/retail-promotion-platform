package com.retailplatform.promotion.command.producer;

import com.retailplatform.promotion.command.event.PromotionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionEventPublisher {

    private static final String TOPIC = "promotion.created.v1";

    private final KafkaTemplate<String, PromotionCreatedEvent> kafkaTemplate;

    public void publishPromotionCreated(PromotionCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getPromotionId().toString(), event);
    }
}
