package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.Location;
import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.InventoryRepository;
import com.inventoryManagementSystem.backend.repository.LocationRepository;
import com.inventoryManagementSystem.backend.repository.SupplierRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final InventoryRepository inventoryRepository;
    private final CounterService counterService;
    private final SupplierRepository supplierRepository;
    private final LocationRepository locationRepository;
    private final BarcodeService barcodeService;

    public CommonResponse<List<Item>> getAllItems() {
        List<Item> data = inventoryRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
    }
    public CommonResponse<String> addItemFromSupplier(Item item, String supplierId) {
        String name = item.getName();
        boolean exists = inventoryRepository.existsByName(name); // Custom repository method

        if (exists) {
            String errorMessage = "Item with name '" + name + "' already exists";
            return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), errorMessage);
        }

        Optional<Supplier> supplier = supplierRepository.findBySupplierId(supplierId);
        if(supplier.isPresent())
        {
            // Generate sequence for the new item
            long sequence = counterService.generateSequence("itemId");
            item.setItemId("ITM-" + sequence);

            //create and add a barcode to the item
            barcodeService.addBarcode(item.getItemId());

            // Set the current date-time for the lastUpdated field
            item.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(item);

            // Add the itemId to the productsProvided array of the supplier
            supplier.get().getProductsProvided().add(item.getItemId());
            supplierRepository.save(supplier.get());  // Save the updated supplier

            // Prepare response
            String data = item.getName() + " added successfully";
            return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);
        }else{
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "Supplier not found");
        }

    }

    public CommonResponse<String> addItem(Item item) {
        String name = item.getName();
        boolean exists = inventoryRepository.existsByName(name); // Custom repository method

        if (exists) {
            String errorMessage = "Item with name '" + name + "' already exists";
            return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), errorMessage);
        }

        // Generate sequence for the new item
        long sequence = counterService.generateSequence("itemId");
        item.setItemId("ITM-" + sequence);

        // Create and add a barcode to the item
        barcodeService.addBarcode(item.getItemId());

        // Set the current date-time for the lastUpdated field
        item.setLastUpdated(LocalDateTime.now());

        // Save the item to the inventory
        inventoryRepository.save(item);

        // Add the item with quantity 0 to the stock details of each available location
        for (String loc : item.getAvailableLocations()) {
            Optional<Location> locationOptional = locationRepository.findByLocId(loc); // Assuming loc is locationId
            if (locationOptional.isPresent()) {
                Location location = locationOptional.get();
                Map<String, Integer> stockDetails = location.getStockDetails();

                // Add stock entry for this item at the location
                if (!stockDetails.containsKey(item.getItemId())) {
                    stockDetails.put(item.getItemId(), 0); // itemId, quantity: 0
                    location.setStockDetails(stockDetails); // Update the stock details for the location
                    locationRepository.save(location); // Save the updated location
                }
            }
        }

        // Prepare response
        String data = item.getName() + " added successfully";
        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);
    }


    public CommonResponse<List<Item>> findItemsByIDs(List<String> ids){
        List<Item> data = inventoryRepository.findAllByItemIdIn(ids);
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
    }

    public CommonResponse<String> deleteItem(String itemId) {
        // Find the item in the inventory
        Optional<Item> itemOptional = inventoryRepository.findByItemId(itemId);

        if (itemOptional.isPresent()) {
            // Retrieve the item to delete
            Item item = itemOptional.get();

            // Remove the item from the supplier's productsProvided list
            List<Supplier> suppliers = supplierRepository.findByProductsProvidedContaining(itemId);
            for (Supplier supplier : suppliers) {
                supplier.getProductsProvided().remove(itemId);
                supplierRepository.save(supplier); // Save the updated supplier
            }
            List<Location> locations = locationRepository.findAll(); // You might want to filter the locations more specifically

            // Iterate through each location and remove the item from stock details if it exists
            for (Location location : locations) {
                Map<String, Integer> stockDetails = location.getStockDetails();
                if (stockDetails != null && stockDetails.containsKey(itemId)) {
                    stockDetails.remove(itemId); // Remove item from the stock details
                    location.setStockDetails(stockDetails); // Set the updated stock details
                    locationRepository.save(location); // Save the updated location
                }
            }

            // Delete the item from the inventory
            inventoryRepository.delete(item);

            // Prepare response
            return Utility.getResponse(new StatusEntry(ResponseEnum.DELETED_SUCCESSFULLY),
                    "Item with ID '" + itemId + "' deleted successfully");
        } else {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "Item not found");
        }
    }

    public CommonResponse<Item> getItem(String itemId) {
        Optional<Item> itemOptional = inventoryRepository.findByItemId(itemId);
        if(itemOptional.isPresent())
        {
            Item data =itemOptional.get();
            return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
        }
        else{
            String data = "Item with "+itemId+" not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA));
        }
    }

    public CommonResponse<String> editItem(Item updatedItem) {
        Optional<Item> existingItemOptional = inventoryRepository.findByItemId(updatedItem.getItemId());

        if (existingItemOptional.isPresent()) {
            Item existingItem = existingItemOptional.get();

            // Update the fields of the existing item with the values from updatedItem
            existingItem.setCategory(updatedItem.getCategory());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setReorderLevel(updatedItem.getReorderLevel());
            existingItem.setLastUpdated(LocalDateTime.now()); // Set last updated to current date
            for(String loc:updatedItem.getAvailableLocations())
            {
                if (!existingItem.getAvailableLocations().contains(loc)) {
                    existingItem.getAvailableLocations().add(loc);

                    // Now update stockDetails in the Location for the newly added location
                    Optional<Location> locationOptional = locationRepository.findByLocId(loc); // Assuming loc is locationId
                    if (locationOptional.isPresent()) {
                        Location location = locationOptional.get();
                        Map<String, Integer> stockDetails = location.getStockDetails();

                        // Add stock entry for this item at the location
                        if (!stockDetails.containsKey(updatedItem.getItemId())) {
                            stockDetails.put(updatedItem.getItemId(),  0); // itemId, quantity: 0
                            location.setStockDetails(stockDetails); // Update the stock details for the location
                            locationRepository.save(location); // Save the updated location
                        }
                    }
                }
            }


            // Save the updated item to the database
            inventoryRepository.save(existingItem);

            return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY),
                    "Item updated successfully.");
        } else {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA),
                    "Item not found.");
        }
    }

}
