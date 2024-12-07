import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {MatTabsModule} from '@angular/material/tabs';
import { Router } from '@angular/router';
import { SupplierService } from '../../services/supplier.service';
import { ItemService } from '../../services/item.service';
import { Supplier } from '../../interfaces/supplierInterface.interface';
import { Item } from '../../interfaces/item.interface';
import { OrderService } from '../../services/order.service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { Order } from '../../interfaces/order.interface';
import { UserService } from '../../services/user.service';
import { LocationService } from '../../services/location.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../interfaces/user.interface';



@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [MatTabsModule,CommonModule,ReactiveFormsModule,MatTableModule, MatPaginatorModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.scss'
})
export class OrdersComponent implements OnInit, AfterViewInit{
  addOrderForm!: FormGroup;
  disableAddOrder: boolean = true;
  suppliers: Supplier[] = [];
  items: Item[] = [];
  orderTypes = ['Incoming', 'Outgoing'];
  orders:Order[]=[];
  dataSource = new MatTableDataSource<Order>([]);
  displayedColumns: string[]=['orderId', 'supplierId', "itemId" , "orderType" , 'quantity','orderDate','deliveryDate','status'];
  availableLocations: any[] = []; 
  item:Item;
  user:User;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private formBuilder: FormBuilder,
    private supplierService: SupplierService,
    private itemService: ItemService,
    private orderService:OrderService,
    private locationService:LocationService,
    private authService:AuthService
  ) {
    this.constructForm();
  }

  ngOnInit() {
    this.fetchSuppliers();
    this.addOrderForm.statusChanges.subscribe((status) => {
      this.disableAddOrder = this.addOrderForm.invalid;
    });
    // Watch for changes in supplier selection
    this.addOrderForm.get('supplierId')?.valueChanges.subscribe((supplierId) => {
      if (supplierId) {
        const selectedSupplier = this.suppliers.find(s => s.supplierId === supplierId);
        if (selectedSupplier) {
          this.fetchItems(selectedSupplier);
        }
      } else {
        this.items = [];
      }
    });
    this.addOrderForm.get('itemId')?.valueChanges.subscribe((itemId) => {
      if (itemId) {
        this.itemService.getItem(itemId).subscribe((response) => {
          this.item = Array.isArray(response.data) ? response.data[0] : response.data;
          console.log(this.item);
          this.fetchLocationNames();
  
        });
      }
    });
    this.user=this.authService.getUser();
    this.getOrders();
  }

 


  constructForm() {
    this.addOrderForm = this.formBuilder.group({
      supplierId: ['', Validators.required], // Supplier dropdown
      itemId: ['', Validators.required],    // Item dropdown
      orderType: ['', Validators.required],
      locId:['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]], // Quantity field
    });
  }

  /**
   * Custom validator to ensure at least one order type is selected.
   */
  validateOrderType(group: FormGroup) {
    const incoming = group.get('incoming')?.value;
    const outgoing = group.get('outgoing')?.value;
    return incoming || outgoing ? null : { required: true };
  }

  /**
   * Fetches the list of suppliers.
   */
  fetchSuppliers() {
    this.supplierService.getSuppliers().subscribe({
      next: (response) => {
        this.suppliers = response.data; // Assuming `data` contains the list of suppliers
      },
      error: (error) => {
        console.error('Error fetching suppliers:', error);
      }
    });
  }

  /**
   * Fetches the list of items provided by the selected supplier.
   * - The selected supplier
   */
  fetchItems(supplier: Supplier) {
    this.itemService.getAllItemsByIds(supplier.productsProvided).subscribe({
      next: (response) => {
        this.items = response.data; // Assuming `data` contains the list of items
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    });
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  /**
   * Handles the form submission.
   */
  addOrder() {
    if (this.addOrderForm.invalid) {
      console.error('Form is invalid');
      return;
    }

    const orderData = this.addOrderForm.value;
    console.log('Order Data:', orderData);
    this.orderService.addOrder(orderData,this.user.userId,this.user.role).subscribe({
      next: (response) => {
        this.addOrderForm.reset();
      this.disableAddOrder = true;
      this.getOrders();
      },
      error: (error) => {
        console.error('Error adding supplier:', error);
      },
    });
  }

  getOrders(){
    this.orderService.getOrders().subscribe({
      next: (response) => {
        this.orders=response.data;
        this.dataSource.data=this.orders;
      },
      error: (error) => {
        console.error('Error getting orders:', error);
      },
    });
  }

  fetchLocationNames() {
    if (this.item?.availableLocations?.length) {
      const locationIds = this.item.availableLocations;
  
      // Call getAllLocationByIds from LocationService to fetch location details
      this.locationService.getAllLocationsByIds(locationIds).subscribe({
        next: (response) => {
          if (response?.data) {
            // Map the response to the desired format: [{ locId: locName }]
            this.availableLocations = response.data.map((location: any) => ({
              locId: location.locId, // Ensure this matches the ID field in the location object
              locName: location.name, // Ensure this matches the name field in the location object
            }));
            console.log('Available locations:', this.availableLocations);
          } else {
            console.error('Failed to fetch locations');
          }
        },
        error: (error) => {
          console.error('Error fetching locations:', error);
        },
      });
    } else {
      // Clear availableLocations if no locations are associated
      this.availableLocations = [];
    }
  }
  
}
