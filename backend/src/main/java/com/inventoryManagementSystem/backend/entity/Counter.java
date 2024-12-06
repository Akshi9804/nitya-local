package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("counters")
@Getter
@Setter
public class Counter {
    @Id
    private String id;
    private long sequenceValue;
}
