import { Component, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { StockTransferLog } from '../../interfaces/stock-transfer-log.interface';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { LocationService } from '../../services/location.service';
import { ReactiveFormsModule } from '@angular/forms';
@Component({
  selector: 'app-stock-transfer-logs',
  standalone: true,
  imports: [MatTableModule,ReactiveFormsModule,CommonModule,MatPaginatorModule],
  templateUrl: './stock-transfer-logs.component.html',
  styleUrl: './stock-transfer-logs.component.scss'
})
export class StockTransferLogsComponent {
  transferLogs: StockTransferLog[]=[];
  dataSource = new MatTableDataSource<StockTransferLog>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private locationService:LocationService){}

  ngOnInit(): void {
    this.fetchLogs();
  }
  displayedColumns: string[] = ['itemId', "from", "to" , "quantity" , 'transferDate', 'deliveryDate' ,'loggedBy','status'];

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  fetchLogs() {
    this.locationService.getStockTransferLogs().subscribe({
      next:(res)=>{
        this.transferLogs = res.data; 
        this.dataSource.data = this.transferLogs;
      },
      error: (error)=>{
        console.log("Error fetching stock transfer logs: ",error)
      }
    });
  }
}
