package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(path="/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    // Fetch all suppliers
    @GetMapping
    public ResponseEntity<CommonResponse<List<Supplier>>> getAllSuppliers() {
        return new ResponseEntity<>(supplierService.getAllSuppliers(),HttpStatus.OK);
    }

    // Add a supplier
    @PostMapping
    public ResponseEntity<CommonResponse<String>> addSupplier(@RequestBody Supplier supplier) {
        return new ResponseEntity<>(supplierService.addSupplier(supplier),HttpStatus.OK);
    }

    @GetMapping("/find-supplier/{id}")
    public ResponseEntity<CommonResponse<Supplier>> findSupplier(@PathVariable String id) {
        CommonResponse<Supplier> supplier = supplierService.findSupplierById(id);
        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<String>> updateSupplier(@RequestBody Supplier supplier) {
        return new ResponseEntity<>(supplierService.updateSupplierDetails(supplier), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<String>> deleteSupplier(@PathVariable String id) {
        return new ResponseEntity<>(supplierService.deleteSupplier(id), HttpStatus.OK);
    }
    @PutMapping("/add-existing-item/{id}")
    public ResponseEntity<CommonResponse<String>> addExistingItem(@RequestBody Map<String,String> inputMap) {
        return new ResponseEntity<>(supplierService.addExistingItem(inputMap.get("supplierId"),inputMap.get("itemId")),HttpStatus.OK);
    }
    @PutMapping("/delete-item/{id}")
    public ResponseEntity<CommonResponse<String>> deleteItemFromSupplier(@RequestBody Map<String,String> inputMap) {
        return new ResponseEntity<>(supplierService.deleteItemForSupplier(inputMap.get("supplierId"),inputMap.get("itemId")), HttpStatus.OK);
    }
}
