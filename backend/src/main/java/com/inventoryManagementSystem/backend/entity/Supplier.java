package com.inventoryManagementSystem.backend.entity;

import com.inventoryManagementSystem.backend.service.CounterService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Document("supplier")
@Getter
@Setter
@RequiredArgsConstructor
public class Supplier {
//SupplierId, Name, ContactInfo, Address, and
//ProductsProvided, expectedDeliveryInDays
    @Id
    private String id;
    private String supplierId;
    private String name;
    private String email;
    private String mobile;
    private String address;
    private int deliveryInDays;
    private List<String> productsProvided= new ArrayList<>();
}
