package com.inventoryManagementSystem.backend.repository;


import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByName(String name);
    boolean existsByEmail(String name);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByRoleIsNull();
    Optional<User> findByUserId(String name);
}
