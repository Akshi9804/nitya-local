package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.*;
import com.inventoryManagementSystem.backend.repository.InventoryRepository;
import com.inventoryManagementSystem.backend.repository.NotificationRepository;
import com.inventoryManagementSystem.backend.repository.UserRepository;
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

    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId); // No sorting applied
    }

    public void addNotification(String userId, String message, String alertType) {
        Notification notification = new Notification();
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

        // Filter items with quantity less than reorder level
        List<Item> itemsWithLowStock = allItems.stream()
                .filter(item -> item.getQuantity() < item.getReorderLevel())
                .toList();

        // Check if there are items with low stock
        if (!itemsWithLowStock.isEmpty()) {
            // Fetch all admin users
            List<User> admins = userRepository.findByRole("admin");

            // Create and save notifications for all low-stock items
            for (Item item : itemsWithLowStock) {
                String message = "Item " + item.getName() + " has low stock. Current quantity: " + item.getQuantity();

                // Create notifications for each admin
                for (User admin : admins) {
                    Notification notification = new Notification();
                    notification.setUserId(admin.getUserId());
                    notification.setMessage(message);
                    notification.setAlertType("Low Stock Alert");
                    notification.setTimeStamp(LocalDateTime.now());

                    // Save the notification
                    notificationRepository.save(notification);
                }

                System.out.println("Notification sent to admins for item: " + item.getName());
            }
        } else {
            System.out.println("No items with low stock found.");
        }
    }

}


