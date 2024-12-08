package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.Barcode;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.BarcodeRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarcodeService {
    private final BarcodeRepository barcodeRepository;

    public void addBarcode(String itemId) {
        Barcode barcode = new Barcode();
        barcode.setItemId(itemId);
        barcode.setBarcode("https://barcodeapi.org/api/Code39/"+itemId);
        barcode.setGeneratedDate(LocalDateTime.now());
        barcodeRepository.insert(barcode);
    }

    public boolean deleteBarcode(String itemId) {
        Optional<Barcode> barcodeOptional = barcodeRepository.findById(itemId);
        if (barcodeOptional.isPresent()) {
            barcodeRepository.delete(barcodeOptional.get());
            return true;
        }
        return false;
    }

    public CommonResponse<Barcode> fetchBarcodeById(String itemId) {
        Optional<Barcode> barcodeOptional = barcodeRepository.findById(itemId);
        if (barcodeOptional.isPresent()) {
            Barcode barcode = barcodeOptional.get();
            return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),barcode);
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA),"Barcode not found");
    }
}

