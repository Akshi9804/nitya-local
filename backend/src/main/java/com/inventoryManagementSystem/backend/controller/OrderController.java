package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.Order;
import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/order")
public class OrderController {
    private final OrderService orderService;

    // Fetch all orders
    @GetMapping
    public ResponseEntity<CommonResponse<List<Order>>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    // Add a supplier
    @PostMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> addOrderByStaff(@RequestBody Order order,@PathVariable String id,@RequestParam String role) {
        if(order.getOrderType().equals("Incoming"))
            return new ResponseEntity<>(orderService.addIncomingOrder(order,id,role),HttpStatus.OK);
        else
            return new ResponseEntity<>(orderService.addOutgoingOrder(order,id),HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public CommonResponse<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        return orderService.getOrdersByUserId(userId);
    }
}
