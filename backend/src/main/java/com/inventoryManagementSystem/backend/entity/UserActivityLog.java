package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("user-activity-log")
@Getter
@Setter
public class UserActivityLog {
    @Id
    private String id;
    private String logId;
    private String userId;
    private String action;
    private String description;
    private LocalDateTime timeStamp;
}
