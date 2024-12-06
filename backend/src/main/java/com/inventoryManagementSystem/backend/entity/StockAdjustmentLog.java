package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("stock-adjustment-log")
@Getter
@Setter
@RequiredArgsConstructor
public class StockAdjustmentLog {
    /**
     * LogId, ItemId, ChangeType
     * (Add/Remove/Adjust), Quantity, Reason, LoggedBy, and Timestamp.
     */
    @Id
    private String id;
    private String logId;
    private String itemId;
    private String changeType;
    private int quantity;
    private String reason;
    private String loggedBy;
    private LocalDateTime timeStamp;
}
