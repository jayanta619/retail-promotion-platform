package com.retailplatform.pricing.repository;

import com.retailplatform.pricing.entity.PriceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PriceRuleRepository extends JpaRepository<PriceRule, UUID> {
}
