package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Order;
import com.inventoryManagementSystem.backend.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    public Optional<Order> findByOrderId(String orderId);
    List<Order> findByStatusAndDeliveryDateNotNull(String status);

    List<Order> findByUserId(String userId);

    @Query("{ 'status': 'Pending', 'deliveryDate': { $lte: ?0 } }")
    List<Order> findOrdersWithPastDeliveryDate(LocalDateTime currentTime);
}
