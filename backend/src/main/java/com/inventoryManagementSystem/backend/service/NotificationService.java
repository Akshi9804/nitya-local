package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.*;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.InventoryRepository;
import com.inventoryManagementSystem.backend.repository.NotificationRepository;
import com.inventoryManagementSystem.backend.repository.UserRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final CounterService counterService;

    public CommonResponse<List<Notification>> getNotificationsByUserId(String userId) {
        List<Notification> notifications= notificationRepository.findByUserId(userId);
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), notifications);// No sorting applied
    }

    public CommonResponse<String> markNotificationAsRead(String notificationId) {
        Optional<Notification> optionalNotification = notificationRepository.findByNotificationId(notificationId);

        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            if (!notification.isRead()) {
                notification.setRead(true);
                notificationRepository.save(notification);
                return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), "Notification marked as read.");
            } else {
                return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), "Notification is already marked as read.");
            }
        } else {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "Notification not found.");
        }
    }

    public void addNotification(String userId, String message, String alertType) {
        Notification notification = new Notification();
        long notificationSequence = counterService.generateSequence("notificationId");
        notification.setNotificationId("NOT-" + notificationSequence);
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setAlertType(alertType);
        notification.setTimeStamp(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    @Scheduled(fixedRate = 60000 * 3) // Run every 3 minutes
    public void checkLowStockAndNotifyAdmins() {
        // Fetch all items from the repository
        List<Item> allItems = inventoryRepository.findAll();

        // Filter items with quantity less than reorder level and notification not sent
        List<Item> itemsWithLowStock = allItems.stream()
                .filter(item -> item.getQuantity() < item.getReorderLevel() && !item.isNotificationSent())
                .toList();

        if (!itemsWithLowStock.isEmpty()) {
            List<User> admins = userRepository.findByRole("admin");

            // Create and save notifications for all low-stock items
            for (Item item : itemsWithLowStock) {
                String message = "Item " + item.getName() + " has low stock";

                // Create notifications for each admin
                for (User admin : admins) {
                    addNotification(admin.getUserId(),message,"Low Stock Alert");
                }

                // Mark the item as notified
                item.setNotificationSent(true);
                inventoryRepository.save(item); // Update the item in the database

                System.out.println("Notification sent to admins for item: " + item.getName());
            }
        } else {
            System.out.println("No new items with low stock found.");
        }
    }


}


