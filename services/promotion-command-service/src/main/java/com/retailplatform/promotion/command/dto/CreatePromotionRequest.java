package com.retailplatform.promotion.command.dto;

import com.retailplatform.promotion.command.entity.PromotionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreatePromotionRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private PromotionType type;

    @NotNull
    private BigDecimal discountValue;

    private String zoneCode;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;
}
