package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.User;
import com.inventoryManagementSystem.backend.entity.UserActivityLog;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.UserActivityLogRepository;
import com.inventoryManagementSystem.backend.repository.UserRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserActivityLogService {
    private final UserActivityLogRepository userActivityLogRepository;
    private final UserRepository userRepository;
    private final CounterService counterService;

    public void addUserActivityLog(String userId, String action, String description) {
        UserActivityLog userActivityLog = new UserActivityLog();
        long orderSequence = counterService.generateSequence("userActivityLogId");
        userActivityLog.setLogId("ACT-" + orderSequence);
        userActivityLog.setUserId(userId);
        userActivityLog.setAction(action);
        userActivityLog.setDescription(description);
        userActivityLog.setTimeStamp(LocalDateTime.now()); // Automatically set the timestamp
        userActivityLogRepository.save(userActivityLog);
    }

    public CommonResponse<List<UserActivityLog>> fetchLogsByUserId(String userId) {
        List<UserActivityLog> logs = userActivityLogRepository.findByUserId(userId);
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), logs);
    }

    public CommonResponse<List<UserActivityLog>> fetchStaffLogs() {
        // Fetch all users with the "staff" role
        List<User> staffUsers = userRepository.findByRole("staff");

        // Extract staff user IDs
        List<String> staffUserIds = staffUsers.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        // Fetch logs for staff user IDs
        List<UserActivityLog> staffLogs = userActivityLogRepository.findAll().stream()
                .filter(log -> staffUserIds.contains(log.getUserId()))
                .collect(Collectors.toList());

        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), staffLogs);
    }

}
