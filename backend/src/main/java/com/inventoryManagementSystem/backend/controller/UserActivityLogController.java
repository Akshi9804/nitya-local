package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.Notification;
import com.inventoryManagementSystem.backend.entity.UserActivityLog;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/user-activity-log")
public class UserActivityLogController {
    private final UserActivityLogService userActivityLogService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<UserActivityLog>>> getAllStaffLogs() {
        return new ResponseEntity<>(userActivityLogService.fetchStaffLogs(), HttpStatus.OK);
    }
}
