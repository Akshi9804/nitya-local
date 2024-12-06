package com.inventoryManagementSystem.backend.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    RETRIEVED_SUCCESSFULLY(1001,"SUCCESS","Retrieved Successfully"),
    INSERTED_SUCCESSFULLY(1003,"SUCCESS","Inserted Successfully"),
    DELETED_SUCCESSFULLY(1004,"SUCCESS","Deleted Successfully"),
    UPDATED_SUCCESSFULLY(1005,"SUCCESS","Updated Successfully"),

    NO_DATA(1050,"SUCCESS","No such data exists"),
    ALREADY_EXISTS(1100,"SUCCESS","Already exists"),

    FAILED(2000,"FAILED","RequestFailure");

    private final int statusCode;
    private final String statusMessage;
    private final String statusType;
}

