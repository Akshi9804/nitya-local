package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.Item;
import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.InventoryRepository;
import com.inventoryManagementSystem.backend.repository.SupplierRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final CounterService counterService;
    private final InventoryRepository inventoryRepository;

    public CommonResponse<List<Supplier>> getAllSuppliers() {
        List<Supplier> data = supplierRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
    }



    public CommonResponse<String> addSupplier(Supplier supplier) {
        String name = supplier.getName();
        boolean exists = supplierRepository.existsByName(name); // Custom repository method

        if (exists) {
            String errorMessage = "Supplier with name '" + name + "' already exists";
            return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), errorMessage);
        }

        // Generate sequence for the new supplier
        long sequence = counterService.generateSequence("supplierId");
        supplier.setSupplierId("SUP-" + sequence);

        // Save supplier to the database
        supplierRepository.insert(supplier);
        String data = supplier.getName() + " added successfully";

        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);
    }

    public CommonResponse<Supplier> findSupplierById(String id){
        Optional<Supplier> data = supplierRepository.findBySupplierId(id);
        if(data.isPresent())
            return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
        else
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA));
    }

    public CommonResponse<String> updateSupplierDetails(Supplier supplier){
        Optional<Supplier> dbSupplier = supplierRepository.findBySupplierId(supplier.getSupplierId());
        if(dbSupplier.isPresent()){
            // Add the itemId to the productsProvided array of the supplier
            dbSupplier.get().setMobile(supplier.getMobile());
            dbSupplier.get().setEmail(supplier.getEmail());
            dbSupplier.get().setAddress(supplier.getAddress());
            dbSupplier.get().setDeliveryInDays(supplier.getDeliveryInDays());
            supplierRepository.save(dbSupplier.get());  // Save the updated supplier

            // Prepare response
            String data = supplier.getName() + " updated successfully";
            return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), data);
        }
        else{
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "Supplier not found");
        }

    }

    public CommonResponse<String> deleteSupplier(String supplierId){
        Optional<Supplier> supplier = supplierRepository.deleteBySupplierId(supplierId);
        if(supplier.isPresent())
        {
            String data = supplier.get().getName() + " deleted successfully";
            return Utility.getResponse(new StatusEntry(ResponseEnum.DELETED_SUCCESSFULLY), data);
        }
        else {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "Supplier not found");
        }

    }

    public CommonResponse<String> addExistingItem(String supplierId, String itemId){
        Optional<Item> dbItem = inventoryRepository.findByItemId(itemId);

        String data;
        if(dbItem.isPresent())
        {
            Optional<Supplier> dbSupplier = supplierRepository.findBySupplierId(supplierId);
            if(dbSupplier.isPresent())
            {
                Supplier supplier = dbSupplier.get();
                boolean exists=false;
                for(String item:supplier.getProductsProvided()){
                    if(item.equals(dbItem.get().getItemId())){
                        exists=true;
                        break;
                    }
                }
                if(exists){
                    data=supplier.getName()+" already have "+dbItem.get().getItemId()+" in the items he is providing";
                    return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), data);
                }else{
                    supplier.getProductsProvided().add(dbItem.get().getItemId());
                    supplierRepository.save(supplier);
                    data="Added item to "+supplier.getName()+" successfully";
                    return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), data);
                }
            }
            data = "Supplier not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
        }
        else {
            data="Item not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
        }

    }

    public CommonResponse<String> deleteItemForSupplier(String supplierId, String itemId) {
        // Fetch the supplier by supplierId
        Optional<Supplier> dbSupplier = supplierRepository.findBySupplierId(supplierId);

        if (dbSupplier.isPresent()) {
            Supplier supplier = dbSupplier.get();

            // Check if the item is in the supplier's productsProvided list
            boolean exists = supplier.getProductsProvided().remove(itemId);

            if (exists) {
                // Save the updated supplier
                supplierRepository.save(supplier);

                // Return success response
                String data = "Item " + itemId + " removed from " + supplier.getName() + " successfully";
                return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), data);
            } else {
                // If the item is not found in the supplier's list
                String data = "Item " + itemId + " not found in the list of products provided by " + supplier.getName();
                return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
            }
        } else {
            // If the supplier is not found
            String data = "Supplier with ID " + supplierId + " not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), data);
        }
    }




}
