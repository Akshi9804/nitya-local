package com.inventoryManagementSystem.backend.repository;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.Location;
import com.inventoryManagementSystem.backend.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
    boolean existsByName(String name);
    Optional<Location> findByLocId(String locId);
    Optional<Location> deleteByLocId(String locId);
    List<Location> findByLocIdIn(List<String> locIds);


}
