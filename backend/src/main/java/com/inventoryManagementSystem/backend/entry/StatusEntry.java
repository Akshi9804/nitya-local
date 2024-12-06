package com.inventoryManagementSystem.backend.entry;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusEntry {
    private int statusCode;
    private String statusMessage;
    private String statusType;

    public StatusEntry(ResponseEnum responseEnum) {
        this.statusCode = responseEnum.getStatusCode();
        this.statusMessage = responseEnum.getStatusMessage();
        this.statusType = responseEnum.getStatusType();
    }
}
