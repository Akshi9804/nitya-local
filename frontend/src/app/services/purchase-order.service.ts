import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../interfaces/api-response.interface';
import { PurchaseOrder } from '../interfaces/purchase-order.interface';

@Injectable({
  providedIn: 'root'
})
export class PurchaseOrderService {

  private baseUrl = 'http://localhost:8090/purchase-order';
  constructor(private http:HttpClient) { }

  getPendingOrders():Observable<ApiResponse<PurchaseOrder>>{
    return this.http.get<ApiResponse<PurchaseOrder>>(`${this.baseUrl}/requests`);
  }

  getApprovedOrders():Observable<ApiResponse<PurchaseOrder>>{
    return this.http.get<ApiResponse<PurchaseOrder>>(`${this.baseUrl}/approved`);
  }

  approveRequest(poId : string):Observable<ApiResponse<String>>{
    return this.http.put<ApiResponse<String>>(`${this.baseUrl}/changeStatus`,{poId:poId});
  }

  declineRequest(poId:string):Observable<ApiResponse<String>>{
    return this.http.delete<ApiResponse<String>>(`${this.baseUrl}/delete/${poId}`);
  }
}
