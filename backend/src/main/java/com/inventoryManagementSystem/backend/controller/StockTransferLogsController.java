package com.inventoryManagementSystem.backend.controller;


import com.inventoryManagementSystem.backend.entity.StockAdjustmentLog;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.StockTransferLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/stock-transfer-logs")
public class StockTransferLogsController {
    private final StockTransferLogsService stockTransferLogsService;
    @GetMapping
    public ResponseEntity<CommonResponse<List<StockAdjustmentLog>>> getAllLogs() {
        return new ResponseEntity<>(stockTransferLogsService.getStockTransferLogs(), HttpStatus.OK);
    }
}
