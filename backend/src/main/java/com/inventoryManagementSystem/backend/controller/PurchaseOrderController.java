package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.PurchaseOrder;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/purchase-order")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @GetMapping("/requests")
    public ResponseEntity<CommonResponse<List<PurchaseOrder>>> getPendingPurchaseOrders() {
        return new ResponseEntity<>(purchaseOrderService.getPendingPurchaseOrders(), HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<CommonResponse<List<PurchaseOrder>>> getApprovedPurchaseOrders() {
        return new ResponseEntity<>(purchaseOrderService.getApprovedPurchaseOrders(), HttpStatus.OK);
    }

    @PutMapping("/changeStatus")
    public ResponseEntity<CommonResponse<String>> getApprovedPurchaseOrders(@RequestBody Map<String,String> inputMap) {
        return new ResponseEntity<>(purchaseOrderService.changeApprovalStatus(inputMap.get("poId"),inputMap.get("userId")), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<String>> deletePurchaseOrder(@PathVariable String id) {
        return new ResponseEntity<>(purchaseOrderService.declineRequest(id), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<CommonResponse<String>> editPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        return new ResponseEntity<>(purchaseOrderService.editRequest(purchaseOrder), HttpStatus.OK);
    }


}
