package com.inventoryManagementSystem.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document("user")
@Getter
@Setter
public class User {
    /**
     * UserId, Username, PasswordHash, Role, Email,
     * and IsActive.
     */
    @Id
    private String id;
    private String userId;
    private String name;
    private String password;
    private String role;
    private String email;
    private boolean isActive;
}
