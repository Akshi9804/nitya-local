import { Injectable } from '@angular/core';
import { User } from '../interfaces/user.interface';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../interfaces/api-response.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private user:User;
  private baseUrl = 'http://localhost:8090/user';
  constructor(private http:HttpClient) { }
  role : string = "admin";
  userId: string = "USR-1";

  role1 : string = "staff";
  userId1: string = "USR-2";

  signupUser(user: User): Observable<ApiResponse<String>> {
    user.role="admin";
    user.isActive=true;
    console.log(user);
    return this.http.post<ApiResponse<String>>(`${this.baseUrl}/signup`,user);
  }

  signinUser(cred:any): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.baseUrl}/signin`,cred);
  }

  setUser(user: any): void {
    this.user = user;
  }

  // Get user data
  getUser(): any {
    return this.user;
  }
}
