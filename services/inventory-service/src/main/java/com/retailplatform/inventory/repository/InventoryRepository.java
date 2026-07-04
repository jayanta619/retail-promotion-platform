package com.retailplatform.inventory.repository;

import com.retailplatform.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
}
