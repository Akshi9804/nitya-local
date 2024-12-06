package com.inventoryManagementSystem.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document("location")
@Getter
@Setter
public class Location {
    @Id
    private String id;
    private String locId;
    private String name;
    private String address;
    private Map<String,Integer> stockDetails = new HashMap<>();;
}
