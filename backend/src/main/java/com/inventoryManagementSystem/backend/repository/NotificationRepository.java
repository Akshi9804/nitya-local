package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Notification;
import com.inventoryManagementSystem.backend.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    Optional<Notification> findByNotificationId(String notId);
}
