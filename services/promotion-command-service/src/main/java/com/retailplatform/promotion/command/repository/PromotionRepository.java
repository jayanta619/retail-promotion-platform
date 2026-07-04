package com.retailplatform.promotion.command.repository;

import com.retailplatform.promotion.command.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
}
