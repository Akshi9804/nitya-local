package com.inventoryManagementSystem.backend.service;

import com.inventoryManagementSystem.backend.entity.Order;
import com.inventoryManagementSystem.backend.entity.PurchaseOrder;
import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.OrderRepository;
import com.inventoryManagementSystem.backend.repository.PurchaseOrderRepository;
import com.inventoryManagementSystem.backend.repository.SupplierRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final UserActivityLogService userActivityLogService;

    public CommonResponse<List<PurchaseOrder>> getPendingPurchaseOrders() {
        List<PurchaseOrder> pendingOrders = purchaseOrderRepository.findByApprovalStatus("Pending");
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), pendingOrders);
    }

    public CommonResponse<List<PurchaseOrder>> getApprovedPurchaseOrders() {
        List<PurchaseOrder> approvedOrders = purchaseOrderRepository.findByApprovalStatus("Approved");
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), approvedOrders);
    }

    public CommonResponse<String> changeApprovalStatus(String poId,String userId) {
        Optional<PurchaseOrder> po = purchaseOrderRepository.findByPoId(poId);
        String data;
        if(po.isPresent()){
            po.get().setApprovalStatus("Approved");
            Order order = orderRepository.findByOrderId(po.get().getOrderId()).get();
            order.setOrderDate(LocalDateTime.now());
            Supplier supplier = supplierRepository.findBySupplierId(po.get().getSupplierId()).get();
            po.get().setExpectedDelivery(order.getOrderDate().plusMinutes(supplier.getDeliveryInDays()));
            order.setDeliveryDate(po.get().getExpectedDelivery());
            purchaseOrderRepository.save(po.get());
            orderRepository.save(order);
            userActivityLogService.addUserActivityLog(userId,"Purchase order request approved",  order.getOrderId()+" is approved");
            data=po.get().getPoId()+"("+po.get().getOrderId()+") approved";
            return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY),data);
        }else{
            data="Purchase order not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA),data);
        }

    }

    public CommonResponse<String> declineRequest(String poId) {
        Optional<PurchaseOrder> po = purchaseOrderRepository.findByPoId(poId);
        String data;
        if(po.isPresent()){
            Order order = orderRepository.findByOrderId(po.get().getOrderId()).get();
            order.setStatus("Cancelled");
            purchaseOrderRepository.deleteByPoId(poId);
            orderRepository.save(order);
            data=po.get().getPoId()+"("+po.get().getOrderId()+") deleted";
            return Utility.getResponse(new StatusEntry(ResponseEnum.DELETED_SUCCESSFULLY),data);
        }else{
            data="Purchase order not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA),data);
        }

    }

    public CommonResponse<String> editRequest(PurchaseOrder purchaseOrder) {
        Optional<PurchaseOrder> dbPo = purchaseOrderRepository.findByPoId(purchaseOrder.getPoId());
        String data;
        if(dbPo.isPresent()){
            dbPo.get().setQuantity(purchaseOrder.getQuantity());
            purchaseOrderRepository.save(dbPo.get());
            data=dbPo.get().getPoId()+"("+dbPo.get().getOrderId()+") updated";
            return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY),data);
        }else{
            data="Purchase order not found";
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA),data);
        }

    }


}
