package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.StockAdjustmentLog;
import com.inventoryManagementSystem.backend.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAdjustmentLogRepository  extends MongoRepository<StockAdjustmentLog, String> {
}
