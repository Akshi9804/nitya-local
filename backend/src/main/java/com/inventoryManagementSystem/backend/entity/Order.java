package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("order")
@Getter
@Setter
@RequiredArgsConstructor
public class Order {
    //OrderId, SupplierId, OrderType
            //(Incoming/Outgoing), ItemId, Quantity, OrderDate, DeliveryDate, and Status.
    @Id
    private String id;
    private String orderId;
    private String supplierId;
    private String orderType;
    private String itemId;
    private int quantity;
    private String locId;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String status;
    private String userId;
}
