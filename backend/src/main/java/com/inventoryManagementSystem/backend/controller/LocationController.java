package com.inventoryManagementSystem.backend.controller;

import com.inventoryManagementSystem.backend.entity.Location;
import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/location")
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<Location>>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    // Add a supplier
    @PostMapping
    public ResponseEntity<CommonResponse<String>> addLocation(@RequestBody Location location ) {
        return new ResponseEntity<>(locationService.addLocation(location),HttpStatus.OK);
    }

    @GetMapping("/find-location/{id}")
    public ResponseEntity<CommonResponse<Location>> findLocation(@PathVariable String id) {
        CommonResponse<Location> location = locationService.getLocation(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @PostMapping("/get-locations")
    public ResponseEntity<CommonResponse<List<Location>>> findAllLocationByIds(@RequestBody String[]ids) {
        CommonResponse<List<Location>> location = locationService.getAllLocationByIds(ids);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<String>> deleteSupplier(@PathVariable String id) {
        return new ResponseEntity<>(locationService.deleteLocation(id), HttpStatus.OK);
    }

    @PutMapping("/delete-existing-item/{id}")
    public ResponseEntity<CommonResponse<String>> deleteExistingItem(@RequestBody String itemName,@PathVariable String id) {
        return new ResponseEntity<>(locationService.deleteItemFromLocation(id,itemName), HttpStatus.OK);
    }

    @PutMapping("/add-existing-item")
    public ResponseEntity<CommonResponse<String>> addExistingItem(@RequestBody Map<String,String> inputMap) {
        return new ResponseEntity<>(locationService.addExistingItem(inputMap.get("locId"),inputMap.get("itemId")),HttpStatus.OK);
    }

    @PostMapping("/transfer-stock")
    public ResponseEntity<CommonResponse<String>> transferStock(@RequestBody Map<String,String> inputMap) {
        return new ResponseEntity<>(locationService.transferStock(inputMap.get("itemId"),Integer.parseInt(inputMap.get("quantity")),inputMap.get("from"),inputMap.get("to"),inputMap.get("loggedBy")),HttpStatus.OK);
    }
}
