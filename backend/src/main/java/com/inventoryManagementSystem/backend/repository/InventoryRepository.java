package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Item, String> {
    boolean existsByName(String name);
    List<Item> findAllByItemIdIn(List<String> itemIds);
    Optional<Item> findByItemId(String itemId);
    List<Item> findByQuantityLessThan(int reorderLevel);
    Optional<Item> findByName(String itemName);
}
