package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    boolean existsByName(String name);
    public Optional<Supplier> findBySupplierId(String supplierId);
    public Optional<Supplier> deleteBySupplierId(String supplierId);
    List<Supplier> findByProductsProvidedContaining(String itemId);
}
