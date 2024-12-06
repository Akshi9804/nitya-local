package com.inventoryManagementSystem.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("item")
@Getter
@Setter
public class Item {
    /**
     * ItemId, Name, Category, Quantity, UnitPrice,
     *     ReorderLevel, LastUpdated, and HistoryLogs
     */
    @Id
    private String id;
    private String itemId;
    private String name;
    private String category;
    private int quantity=0;
    private int price=0;
    private int reorderLevel=0;
    private LocalDateTime lastUpdated;
    private List<String> availableLocations=new ArrayList<>();
    private boolean notificationSent=false;

}
