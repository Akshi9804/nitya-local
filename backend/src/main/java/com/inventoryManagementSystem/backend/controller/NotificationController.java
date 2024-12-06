package com.inventoryManagementSystem.backend.controller;


import com.inventoryManagementSystem.backend.entity.Notification;
import com.inventoryManagementSystem.backend.entity.Order;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<CommonResponse<List<Notification>>> getNotificationsByUserId(@RequestBody String userId) {
        System.out.println(userId);
        return new ResponseEntity<>(notificationService.getNotificationsByUserId(userId), HttpStatus.OK);
    }
}
