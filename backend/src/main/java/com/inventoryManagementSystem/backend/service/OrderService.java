package com.inventoryManagementSystem.backend.service;


import com.inventoryManagementSystem.backend.entity.*;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.*;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CounterService counterService;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;
    private final StockAdjustmentLogRepository stockAdjustmentLogRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final LocationRepository locationRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final UserActivityLogService userActivityLogService;

    public CommonResponse<String> addOutgoingOrder(Order order,String userId) {

        // Generate ID for the new order
        Optional<Item> item = inventoryRepository.findByItemId(order.getItemId());
        if(item.isPresent() && item.get().getQuantity()>=order.getQuantity())
        {
            Optional<Location> locationDetails = locationRepository.findByLocId(order.getLocId());
            if(!locationDetails.isPresent() || !locationDetails.get().getStockDetails().containsKey(order.getItemId()))
            {
                return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), "Location not found for the item");
            }
        long orderSequence = counterService.generateSequence("orderId");
        order.setOrderId("ODR-" + orderSequence);

        // Add order date
        order.setOrderDate(LocalDateTime.now());

        // set status
        order.setStatus("Pending");
        order.setUserId(userId);

        userActivityLogService.addUserActivityLog(userId,"Order placed", "Outgoing order of "+item.get().getName()+" of quantity "+order.getQuantity()+" is palced");

        //set delivery date
        order.setDeliveryDate(LocalDateTime.now().plusMinutes(2));
        //adjust item quantity and log the stock change
        item.get().setQuantity(item.get().getQuantity()-order.getQuantity());
        locationDetails.get().getStockDetails().put(order.getItemId(),locationDetails.get().getStockDetails().get(order.getItemId())-order.getQuantity());
        item.get().setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(item.get());
        locationRepository.save(locationDetails.get());
        //create a log
            StockAdjustmentLog log = new StockAdjustmentLog();
            long logSequence = counterService.generateSequence("stockLogId");
            log.setLogId("STOCLG-" + logSequence);
            log.setItemId(order.getItemId());
            log.setChangeType("Quantity decreased");
            log.setQuantity(order.getQuantity());
            log.setLoggedBy(userId);
            log.setReason("Outgoing order");
            log.setTimeStamp(LocalDateTime.now());
        stockAdjustmentLogRepository.save(log);

        } else{
            order.setStatus("Cancelled");
        }

        orderRepository.save(order);
        String data = "Order generated successfully";

        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);
    }

    public CommonResponse<List<Order>> getOrdersByUserId(String userId) {
        List<Order> data = orderRepository.findByUserId(userId); // Fetch orders by userId
        if (data.isEmpty()) {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "No orders found for this user.");
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), data);
    }

    public CommonResponse<String> addIncomingOrder(Order order,String user,String role) {
            long orderSequence = counterService.generateSequence("orderId");
            order.setOrderId("ODR-" + orderSequence);
            order.setStatus("Pending");
            order.setUserId(user);
            Optional<Item> item = inventoryRepository.findByItemId(order.getItemId());
            Optional<Supplier> supplier = supplierRepository.findBySupplierId(order.getSupplierId());

            PurchaseOrder po = new PurchaseOrder();
            long poSequence = counterService.generateSequence("purchaseOrderId");
            po.setPoId("PO-" + poSequence);
            po.setOrderDate(LocalDateTime.now());
            po.setOrderId(order.getOrderId());
            po.setQuantity(order.getQuantity());
            po.setItemName(item.get().getName());
            po.setSupplierName(supplier.get().getName());
            po.setSupplierId(order.getSupplierId());
            po.setLoggedBy(user);
            po.setItemId(order.getItemId());
            po.setLocId(order.getLocId());

            if(role.equals("admin"))
            {
                order.setOrderDate(LocalDateTime.now());

                po.setApprovalStatus("Approved");
                po.setExpectedDelivery(LocalDateTime.now().plusMinutes(supplier.get().getDeliveryInDays()));
                order.setDeliveryDate(po.getExpectedDelivery());
                userActivityLogService.addUserActivityLog(user,"Purchase order placed", "Incoming order of "+item.get().getName()+" of quantity "+order.getQuantity()+" from supplier "+supplier.get().getName()+" is palced");
            } else{
                po.setApprovalStatus("Pending");
                userActivityLogService.addUserActivityLog(user,"Order request placed", "Incoming order of "+item.get().getName()+" of quantity "+order.getQuantity()+" from supplier "+supplier.get().getName()+" is palced");
            }
            purchaseOrderRepository.save(po);
            orderRepository.save(order);
            String data = "Order generated successfully";

            return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);


    }

    public CommonResponse<List<Order>> getAllOrders() {
        List<Order> data = orderRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
    }

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void updateOrdersWithPastDeliveryDate() {
        // Fetch all orders with status "Pending" and past delivery date
        List<Order> pendingOrders = orderRepository.findOrdersWithPastDeliveryDate(LocalDateTime.now());

        if (!pendingOrders.isEmpty()) {
            // Update the status of these orders to "Done"
            pendingOrders.forEach(order -> {
                Optional<Item> item = inventoryRepository.findByItemId(order.getItemId());
                Optional<Location> location = locationRepository.findByLocId(order.getLocId());
                if(item.isPresent() && location.isPresent())
                {
                    item.get().setQuantity(item.get().getQuantity()+order.getQuantity());
                    item.get().setLastUpdated(LocalDateTime.now());
                    order.setStatus("Done");
                    inventoryRepository.save(item.get());
                    if(location.isPresent()){
                        location.get().getStockDetails().put(order.getItemId(),location.get().getStockDetails().get(order.getItemId())+order.getQuantity());
                        locationRepository.save(location.get());
                        //add the change to the log
                        StockAdjustmentLog log = new StockAdjustmentLog();
                        long logSequence = counterService.generateSequence("stockLogId");
                        log.setLogId("STOCLG-" + logSequence);
                        log.setItemId(order.getItemId());
                        log.setChangeType("Quantity increased");
                        log.setQuantity(order.getQuantity());
                        log.setReason("Incoming order");
                        log.setTimeStamp(LocalDateTime.now());
                        stockAdjustmentLogRepository.save(log);

                        //creating the notifications
                        List<User> users = userRepository.findAll();
                        String message = "Item " + item.get().getName() + " has arrived to Location" + location.get().getName();

                        // Create and save notifications for each admin
                        for (User user : users) {
                            notificationService.addNotification(user.getUserId(),message,"Item Arrival");
                        }

                        System.out.println("Notification sent to users for item: " + item.get().getName());
                    }


                }
            });
            orderRepository.saveAll(pendingOrders);

            System.out.println("Updated " + pendingOrders.size() + " orders to 'Done'.");
        }
    }
}
