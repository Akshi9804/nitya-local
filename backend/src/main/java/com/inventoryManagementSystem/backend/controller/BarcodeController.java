package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.Barcode;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.BarcodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/barcode")
public class BarcodeController {
    private final BarcodeService barcodeService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<Barcode>> getItem(@PathVariable String id) {
        return new ResponseEntity<>(barcodeService.fetchBarcodeById(id), HttpStatus.OK);
    }
}
