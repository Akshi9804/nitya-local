import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '../interfaces/api-response.interface';
import { Observable } from 'rxjs';
import { Item } from '../interfaces/item.interface';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  private baseUrl = 'http://localhost:8090/item';

  constructor(private http: HttpClient) {}

  getAllItemsByIds(ids : string[]): Observable<ApiResponse<Item>> {
    return this.http.post<ApiResponse<Item>>(`${this.baseUrl}/findItems`,ids);
  }

  // Add Item
  addItemFromSupplier(item :Item, supplierId:string): Observable<ApiResponse<Item>> {
    return this.http.post<ApiResponse<Item>>(this.baseUrl, {item,supplierId});
  }

  getItems():Observable<ApiResponse<Item>>{
    return this.http.get<ApiResponse<Item>>(this.baseUrl)
  }

    getItem(itemId:string):Observable<ApiResponse<Item>>{
    return this.http.get<ApiResponse<Item>>(`${this.baseUrl}/${itemId}`)
  }

  editItem(item:Item):Observable<ApiResponse<String>>{
    return this.http.put<ApiResponse<String>>(`${this.baseUrl}/update-item`,item)
  }

  deleteItem(itemId:string):Observable<ApiResponse<String>>{
    return this.http.delete<ApiResponse<String>>(`${this.baseUrl}/delete-item/${itemId}`)
  }

  addItem(item: Item, supplierId: string): Observable<ApiResponse<Item>> {
    return this.http.post<ApiResponse<Item>>(`${this.baseUrl}/add-item`,item);
  }
}
