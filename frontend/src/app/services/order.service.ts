import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../interfaces/order.interface';
import { ApiResponse } from '../interfaces/api-response.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl = 'http://localhost:8090/order';
  constructor(private http:HttpClient) { }

  getOrders(): Observable<ApiResponse<Order>>{
    return this.http.get<ApiResponse<Order>>(this.baseUrl);
  }

  addOrder(order:Order,userId:string,role:string): Observable<ApiResponse<String>>{
    return this.http.post<ApiResponse<String>>(`${this.baseUrl}/${userId}?role=${role}`,order);
  }

  
}
