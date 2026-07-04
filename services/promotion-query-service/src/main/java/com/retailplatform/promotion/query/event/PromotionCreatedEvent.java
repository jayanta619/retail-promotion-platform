package com.retailplatform.promotion.query.event;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class PromotionCreatedEvent {
    private UUID promotionId;
    private String name;
    private String type;
    private BigDecimal discountValue;
    private String zoneCode;
    private Instant startDate;
    private Instant endDate;
    private Instant occurredAt;
}
