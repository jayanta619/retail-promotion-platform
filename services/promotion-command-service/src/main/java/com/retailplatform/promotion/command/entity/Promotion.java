package com.retailplatform.promotion.command.entity;

import com.retailplatform.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "promotions")
@Getter
@Setter
public class Promotion extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    @Enumerated(EnumType.STRING)
    private PromotionStatus status = PromotionStatus.DRAFT;

    private BigDecimal discountValue;

    private String zoneCode;

    private Instant startDate;

    private Instant endDate;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionRule> rules;
}
