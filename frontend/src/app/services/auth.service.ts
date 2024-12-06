import { Injectable } from '@angular/core';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isAuthenticated = false;
  private isAdmin = false;

  constructor(private userService:UserService){}
  
  login(): void { 
      this.isAuthenticated = true;
    if(this.userService.getUser().role==='admin')
      this.isAdmin=true;
    console.log(this.isAuthenticated,this.isAdmin)
  }

  // Mock logout method
  logout(): void {
    this.isAuthenticated = false;
    this.isAdmin=false;
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated;
  }

  isUserAdmin(): boolean {
    return this.isAdmin;
  }
}
