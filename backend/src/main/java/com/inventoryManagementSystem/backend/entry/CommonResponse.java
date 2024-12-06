package com.inventoryManagementSystem.backend.entry;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private StatusEntry statusEntry;

    private T data;
}
