package com.inventoryManagementSystem.backend.controller;


import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entry.AddItemRequest;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/item")
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    public ResponseEntity<CommonResponse<String>> addItemFromSupplier(@RequestBody AddItemRequest request) {
        Item item = request.getItem();
        String supplierId = request.getSupplierId();
        CommonResponse<String> response = itemService.addItemFromSupplier(item, supplierId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-item")
    public ResponseEntity<CommonResponse<String>> addItem(@RequestBody Item item) {
        CommonResponse<String> response = itemService.addItem(item);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/findItems")
    public ResponseEntity<CommonResponse<List<Item>>> findItemsByIDs(@RequestBody List<String> ids) {
        return new ResponseEntity<>(itemService.findItemsByIDs(ids), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<CommonResponse<List<Item>>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(),HttpStatus.OK);
    }

    @DeleteMapping("/delete-item/{itemId}")
    public ResponseEntity<CommonResponse<String>> deleteItem(@PathVariable String itemId) {
        return new ResponseEntity<>(itemService.deleteItem(itemId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<Item>> getItem(@PathVariable String id) {
        return new ResponseEntity<>(itemService.getItem(id),HttpStatus.OK);
    }

    @PutMapping("/update-item")
    public ResponseEntity<CommonResponse<String>> editItem(@RequestBody Item updatedItem) {
        CommonResponse<String> response = itemService.editItem(updatedItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
