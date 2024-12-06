package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("notification")
@Getter
@Setter
@RequiredArgsConstructor
public class Notification {
    /**
     * NotificationId, UserId, Message, AlertType,
     * and Timestamp.
     */
    @Id
    private String id;
    private String notificationId;
    private String userId;
    private String message;
    private String alertType;
    private LocalDateTime timeStamp;
    private boolean read;
}
