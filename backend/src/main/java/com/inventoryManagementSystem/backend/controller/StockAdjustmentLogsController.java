package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.PurchaseOrder;
import com.inventoryManagementSystem.backend.entity.StockAdjustmentLog;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.repository.StockAdjustmentLogRepository;
import com.inventoryManagementSystem.backend.service.StockAdjustmentLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/stock-adjustment-logs")
public class StockAdjustmentLogsController {

    private final StockAdjustmentLogsService stockAdjustmentLogsService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<StockAdjustmentLog>>> getAllLogs() {
        return new ResponseEntity<>(stockAdjustmentLogsService.getAllLogs(), HttpStatus.OK);
    }
}
