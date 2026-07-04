package com.retailplatform.promotion.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
