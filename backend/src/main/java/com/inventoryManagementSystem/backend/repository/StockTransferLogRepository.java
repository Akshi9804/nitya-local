package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Order;
import com.inventoryManagementSystem.backend.entity.StockTransferLog;
import com.inventoryManagementSystem.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockTransferLogRepository extends MongoRepository<StockTransferLog, String> {
    @Query("{ 'status': 'Pending', 'deliveryDate': { $lte: ?0 } }")
    List<StockTransferLog> findStockTransferLogsWithPastDeliveryDate(LocalDateTime currentTime);
}
