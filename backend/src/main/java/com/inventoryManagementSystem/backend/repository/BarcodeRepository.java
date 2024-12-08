package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Barcode;
import com.inventoryManagementSystem.backend.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarcodeRepository extends MongoRepository<Barcode, String> {
}
