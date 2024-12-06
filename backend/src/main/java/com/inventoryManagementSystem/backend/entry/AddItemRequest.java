package com.inventoryManagementSystem.backend.entry;

import com.inventoryManagementSystem.backend.entity.Item;
import lombok.Data;

@Data
public class AddItemRequest {
    private Item item;
    private String supplierId;
}
