package com.inventoryManagementSystem.backend.utility;

import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.StatusEntry;

public class Utility {
    public static <T> CommonResponse<T> getResponse(StatusEntry statusEntry, Object obj){
        CommonResponse<T> response = new CommonResponse<>();
        response.setData((T)obj);
        response.setStatusEntry(statusEntry);
        return response;
    }

    public static <T> CommonResponse<T> getResponse(StatusEntry statusEntry){
        CommonResponse<T> response = new CommonResponse<>();
        response.setStatusEntry(statusEntry);
        return response;
    }
}
