package com.retailplatform.promotion.command.entity;

import com.retailplatform.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "promotion_rules")
@Getter
@Setter
public class PromotionRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(nullable = false)
    private String ruleKey;

    @Column(nullable = false)
    private String ruleValue;
}
