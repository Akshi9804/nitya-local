package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("purchase-order")
@Getter
@Setter
@RequiredArgsConstructor
public class PurchaseOrder {
    //OrderId, SupplierId, OrderType
    //(Incoming/Outgoing), ItemId, Quantity, OrderDate, DeliveryDate, and Status.
    @Id
    private String id;
    private String poId;
    private String orderId;
    private String supplierId;
    private String supplierName;
    private String itemId;
    private String itemName;
    private String locId;
    private int quantity;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDelivery;
    private String approvalStatus;
    private String loggedBy;
}
