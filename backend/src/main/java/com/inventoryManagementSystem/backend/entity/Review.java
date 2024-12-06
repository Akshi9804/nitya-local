package com.inventoryManagementSystem.backend.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("review")
@Getter
@Setter
@RequiredArgsConstructor
public class Review {
    private String locName;
    private String review;
}
