package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("barcode")
@Getter
@Setter
public class Barcode {
    @Id
    private String itemId;

    private String barcode;

    private LocalDateTime generatedDate;

    private LocalDateTime lastScanned;
}
