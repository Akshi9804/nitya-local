package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.PurchaseOrder;
import com.inventoryManagementSystem.backend.entity.StockAdjustmentLog;
import com.inventoryManagementSystem.backend.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {
    List<PurchaseOrder> findByApprovalStatus(String status);
    Optional<PurchaseOrder> findByPoId(String itemId);
    Optional<PurchaseOrder> deleteByPoId(String poId);
}
