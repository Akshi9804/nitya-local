import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { PurchaseOrder } from '../../interfaces/purchase-order.interface';
import { PurchaseOrderService } from '../../services/purchase-order.service';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-purchase-order',
  standalone: true,
  imports: [MatTabsModule,CommonModule,ReactiveFormsModule,MatTableModule,MatCardModule,
    MatButtonModule, MatIconButton,MatIcon],
  templateUrl: './purchase-order.component.html',
  styleUrl: './purchase-order.component.scss'
})
export class PurchaseOrderComponent implements OnInit {
  pendingOrders: PurchaseOrder[] =[];
  approvedOrders: PurchaseOrder[] = [];
  dataSource = new MatTableDataSource<PurchaseOrder>([]);
  displayedColumns: string[]=['poId','orderId', 'supplierName', "itemName" , 'quantity','orderDate','expectedDelivery','approvalStatus'];

  constructor(private purchaseOrderService:PurchaseOrderService,private cdr: ChangeDetectorRef){}

  ngOnInit(): void {
    this.fetchApprovedOrders();
    this.fetchPendingOrders();
  }

  fetchPendingOrders(){
    this.purchaseOrderService.getPendingOrders().subscribe({
      next: (response) => {
        this.pendingOrders=response.data;
        console.log(this.pendingOrders);
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  fetchApprovedOrders(){
    this.purchaseOrderService.getApprovedOrders().subscribe({
      next: (response) => {
        this.approvedOrders=response.data;
        this.dataSource.data=this.approvedOrders;
        console.log(this.approvedOrders)
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  editOrder(order: PurchaseOrder) {
    console.log('Editing order:', order);
  }

  approveOrder(poId: string) {
    this.purchaseOrderService.approveRequest(poId).subscribe({
      next: (response) => {
        console.log(response.data);
        this.fetchApprovedOrders();
        this.fetchPendingOrders();
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    });
  }

  declineRequest(poId:string){
    this.purchaseOrderService.declineRequest(poId).subscribe({
      next: (response) => {
        console.log(response.data);
        this.fetchApprovedOrders();
        this.fetchPendingOrders();
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    });
  }
}
