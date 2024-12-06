import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { StockAdjustmentLog } from '../../interfaces/stockAdjustmentLog.interface';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { LogsService } from '../../services/logs.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [MatTabsModule,CommonModule,ReactiveFormsModule,MatTableModule,MatPaginatorModule],
  templateUrl: './logs.component.html',
  styleUrl: './logs.component.scss'
})
export class LogsComponent {
  stockLogs: StockAdjustmentLog[]=[];
  dataSource = new MatTableDataSource<StockAdjustmentLog>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private logsService:LogsService, private router:Router,private dialog:MatDialog){}
  ngOnInit(): void {
    this.fetchLogs();
  }
  displayedColumns: string[] = ['logId', 'itemId', "changeType" , "quantity" , 'reason','loggedBy','timeStamp'];

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  navigateToAddSuppliers(){
    this.router.navigate(['/task/suppliers/add']);
  }

  fetchLogs() {
    this.logsService.getStockAdjustmentLogs().subscribe((response) => {
      this.stockLogs = response.data; 
      this.dataSource.data = this.stockLogs;
    });
  }
}
