package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.PurchaseOrder;
import com.inventoryManagementSystem.backend.entity.StockAdjustmentLog;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.StockAdjustmentLogRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockAdjustmentLogsService {
    private final StockAdjustmentLogRepository stockAdjustmentLogRepository;

    public CommonResponse<List<StockAdjustmentLog>> getAllLogs() {
        List<StockAdjustmentLog> logs = stockAdjustmentLogRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), logs);
    }
}
