package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Notification;
import com.inventoryManagementSystem.backend.entity.User;
import com.inventoryManagementSystem.backend.entity.UserActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityLogRepository extends MongoRepository<UserActivityLog, String> {
    List<UserActivityLog> findByUserId(String userId);
}
