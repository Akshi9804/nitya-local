import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Supplier } from '../../interfaces/supplierInterface.interface';
import { SupplierService } from '../../services/supplier.service';
import { Router, RouterLink } from '@angular/router';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { ConfirmationDialogComponent } from '../common-elements/confirmation-dialog/confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-view-suppliers',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule,CommonModule,RouterLink,MatIconModule,MatButtonModule],
  templateUrl: './view-suppliers.component.html',
  styleUrl: './view-suppliers.component.scss'
})
export class ViewSuppliersComponent implements OnInit,AfterViewInit{
  suppliers: Supplier[] = [];
  dataSource = new MatTableDataSource<Supplier>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private supplierService: SupplierService, private router:Router,private dialog:MatDialog){}

  ngOnInit(): void {
    this.fetchSuppliers();
  }
  displayedColumns: string[] = ['supplierId', 'name', "email" , "mobile" , 'address','deliveryInDays','viewSupplier','deleteSupplier'];

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  navigateToAddSuppliers(){
    this.router.navigate(['/task/suppliers/add']);
  }

  deleteSupplier(id:string){
    this.supplierService.deleteSupplier(id).subscribe({
      next: (response) => {console.log('Delete successful:', response);
        this.fetchSuppliers();
      },
      error: (error) => console.error('Delete error:', error),
    });
  }

  fetchSuppliers() {
    this.supplierService.getSuppliers().subscribe((response) => {
      this.suppliers = response.data; // Extract supplier data
      this.dataSource.data = this.suppliers;
    });
  }

  openConfirmationDialogForDelete(id:string){
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Are you sure you want to delete this item?' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result == "true") {
        this.deleteSupplier(id);
      } else {
        console.log('Deletion canceled by user.');
      }
    });
  }

}
