package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("stock-transfer-log")
@Getter
@Setter
@RequiredArgsConstructor
public class StockTransferLog {
    /**
     * LogId, ItemId, ChangeType
     * (Add/Remove/Adjust), Quantity, Reason, LoggedBy, and Timestamp.
     */
    @Id
    private String id;
    private String logId;
    private String itemId;
    private String fromLocation;
    private String toLocation;
    private int quantity;
    private LocalDateTime transferDate;
    private LocalDateTime deliveryDate;
    private String loggedBy;
    private String status;
}
