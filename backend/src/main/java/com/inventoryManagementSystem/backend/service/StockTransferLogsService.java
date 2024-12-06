package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.StockAdjustmentLog;
import com.inventoryManagementSystem.backend.entity.StockTransferLog;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.StockTransferLogRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockTransferLogsService {
    private final StockTransferLogRepository stockTransferLogRepository;
    public CommonResponse<List<StockAdjustmentLog>> getStockTransferLogs() {
        List<StockTransferLog> logs = stockTransferLogRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), logs);
    }
}
