import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StockAdjustmentLog } from '../interfaces/stockAdjustmentLog.interface';
import { ApiResponse } from '../interfaces/api-response.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogsService {

  private baseUrl = 'http://localhost:8090/stock-adjustment-logs';

  constructor(private http:HttpClient) { }

  getStockAdjustmentLogs(): Observable<ApiResponse<StockAdjustmentLog>> {
    return this.http.get<ApiResponse<StockAdjustmentLog>>(this.baseUrl);
  }
}
