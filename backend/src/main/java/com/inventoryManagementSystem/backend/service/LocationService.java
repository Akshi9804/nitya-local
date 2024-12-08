package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.*;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.InventoryRepository;
import com.inventoryManagementSystem.backend.repository.LocationRepository;
import com.inventoryManagementSystem.backend.repository.StockTransferLogRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final CounterService counterService;
    private final InventoryRepository inventoryRepository;
    private final StockTransferLogRepository stockTransferLogRepository;

    public CommonResponse<List<Location>> getAllLocations() {
        List<Location> data = locationRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
    }

    public CommonResponse<String> addLocation(Location location) {
        String name = location.getName();
        boolean exists = locationRepository.existsByName(name); // Custom repository method

        if (exists) {
            String errorMessage = "Location with name '" + name + "' already exists";
            return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), errorMessage);
        }
        long sequence = counterService.generateSequence("locationId");
        location.setLocId("LOC-" + sequence);
        String data = location.getName() + " added successfully";
        locationRepository.save(location);
        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);

    }
    public CommonResponse<String> deleteLocation(String locId) {
        Optional<Location> location = locationRepository.deleteByLocId(locId);
        if(location.isPresent())
        {
            Map<String, Integer> stockDetails = location.get().getStockDetails();
            if (stockDetails != null) {
                for (String itemId : stockDetails.keySet()) {
                    Optional<Item> optionalItem = inventoryRepository.findByItemId(itemId); // Assuming itemRepository exists
                    if (optionalItem.isPresent()) {
                        Item item = optionalItem.get();

                        // Remove the locId from the item's location array
                        if (item.getAvailableLocations() != null && item.getAvailableLocations().contains(locId)) {
                            item.getAvailableLocations().remove(locId);
                            inventoryRepository.save(item); // Save updated item back to the database
                        }
                    }
                }
            }
            String data = location.get().getName() + " deleted successfully";
            return Utility.getResponse(new StatusEntry(ResponseEnum.DELETED_SUCCESSFULLY), data);
        }
        else {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "Location not found");
        }
    }

    public CommonResponse<String> addExistingItem(String locId, String itemId) {
        Optional<Item> dbItem = inventoryRepository.findByItemId(itemId);

        String data;
        if (dbItem.isPresent()) {
            Optional<Location> dbLoc = locationRepository.findByLocId(locId);
            if (dbLoc.isPresent()) {
                Location location = dbLoc.get();

                // Check if the item is already present in stockDetails
                if (location.getStockDetails().containsKey(itemId)) {
                    data = "Item " + itemId + " already exists in location " + location.getName();
                    return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), data);
                } else {
                    // Add the item to stockDetails with quantity 0
                    location.getStockDetails().put(itemId, 0);
                    locationRepository.save(location);
                    Item item = dbItem.get();
                    item.getAvailableLocations().add(locId);
                    inventoryRepository.save(item); // Save the updated item


                    data = "Added item " + itemId + " to location " + location.getName() + " successfully with quantity 0";
                    return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), data);
                }
            }

            data = "Location not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
        } else {
            data = "Item not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
        }
    }

    public CommonResponse<String> deleteItemFromLocation(String locId, String itemName) {
        // Find the location by locId
        Optional<Location> dbLoc = locationRepository.findByLocId(locId);

        String data;
        if (dbLoc.isPresent()) {
            Location location = dbLoc.get();

            // Check if the item exists in stockDetails
            if (location.getStockDetails().containsKey(itemName)) {
                // Remove the item from stockDetails
                location.getStockDetails().remove(itemName);
                locationRepository.save(location); // Save the updated location

                // Update the corresponding Item's location array
                Optional<Item> dbItem = inventoryRepository.findByName(itemName);
                if (dbItem.isPresent()) {
                    Item item = dbItem.get();

                    // Remove the location from the item's locations array
                    if (item.getAvailableLocations() != null && item.getAvailableLocations().contains(locId)) {
                        item.getAvailableLocations().remove(locId);
                        inventoryRepository.save(item); // Save the updated item
                    }
                }

                data = "Item " + itemName + " successfully removed from location " + location.getName();
                return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), data);
            } else {
                data = "Item " + itemName + " does not exist in location " + location.getName();
                return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
            }
        } else {
            data = "Location not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
        }
    }

    public CommonResponse<Location> getLocation(String locId) {
        Optional<Location> locationOptional = locationRepository.findByLocId(locId);
        if(locationOptional.isPresent())
        {
            Location data =locationOptional.get();
            return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
        }
        else{
            String data = "Location with "+locId+" not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA),data);
        }
    }

    public CommonResponse<List<Location>> getAllLocationByIds(String[] ids) {
        System.out.println(ids);
            List<Location> locations = locationRepository.findByLocIdIn(Arrays.asList(ids));

            if (locations.isEmpty()) {
                String errorMessage = "No locations found for the provided IDs";
                return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), errorMessage);
            }

            // Successfully retrieved locations
            return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), locations);
    }

    public CommonResponse<String> transferStock(String itemId, int quantity, String from, String to, String loggedBy) {
        Optional<Location> optionalFromLocation = locationRepository.findByLocId(from);
        if(!optionalFromLocation.isPresent())
        {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "No location found");
        }
        Location fromLocation = optionalFromLocation.get();
        if(!fromLocation.getStockDetails().containsKey(itemId)){
            return Utility.getResponse(new StatusEntry(ResponseEnum.FAILED), "Item not found at the provided location");
        }
        if(fromLocation.getStockDetails().get(itemId)<quantity)
        {
            return Utility.getResponse(new StatusEntry(ResponseEnum.FAILED), "Enough supplier of the item are not available at the provided location");
        }
        Optional<Location> optionalToLocation = locationRepository.findByLocId(to);
        if(!optionalToLocation.isPresent())
        {
            return Utility.getResponse(new StatusEntry(ResponseEnum.FAILED), "To location not found");
        }
        Location toLocation = optionalToLocation.get();
        StockTransferLog log = new StockTransferLog();
        log.setItemId(itemId);
        log.setQuantity(quantity);
        log.setFromLocation(fromLocation.getLocId());
        log.setToLocation(toLocation.getLocId());
        log.setLoggedBy(loggedBy);
        log.setTransferDate(LocalDateTime.now());
        log.setDeliveryDate(log.getTransferDate().plusMinutes(3));
        log.setStatus("Pending");

        stockTransferLogRepository.save(log);
        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), "Transfer done");
    }

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void updateStockTransferWithPastDeliveryDate() {
        List<StockTransferLog> pendingTransfers = stockTransferLogRepository.findStockTransferLogsWithPastDeliveryDate(LocalDateTime.now());

        if (!pendingTransfers.isEmpty()) {

            pendingTransfers.forEach(transfer -> {

                Optional<Location> optionalFromLocation = locationRepository.findByLocId(transfer.getFromLocation());
                if(optionalFromLocation.isPresent())
                {
                    transfer.setStatus("Cancelled");
                }
                Location fromLocation = optionalFromLocation.get();
                if(fromLocation.getStockDetails().containsKey(transfer.getItemId())){
                    transfer.setStatus("Cancelled");
                }
                if(fromLocation.getStockDetails().get(transfer.getItemId())<transfer.getQuantity())
                {
                    transfer.setStatus("Cancelled");
                }
                Optional<Location> optionalToLocation = locationRepository.findByLocId(transfer.getToLocation());
                if(!optionalToLocation.isPresent())
                {
                    transfer.setStatus("Cancelled");
                }
                String itemId= transfer.getItemId();
                int quantity=transfer.getQuantity();
                Location toLocation = optionalToLocation.get();
                fromLocation.getStockDetails().put(itemId,fromLocation.getStockDetails().get(itemId)-quantity);
                toLocation.getStockDetails().put(itemId,toLocation.getStockDetails().get(itemId)+quantity);
                transfer.setStatus("Done");
                locationRepository.save(fromLocation);
                locationRepository.save(toLocation);
            });
            stockTransferLogRepository.saveAll(pendingTransfers);
        }
    }



}
