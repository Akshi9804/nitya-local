import { Component, ElementRef, ViewChild, OnInit, Renderer2, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SigninComponent } from '../signin/signin.component';
import { AuthService } from '../../services/auth.service';



@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ CommonModule,SigninComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{
  isLoggedIn:boolean=false;

  @ViewChild(SigninComponent) signin!: SigninComponent;

  constructor(private authService:AuthService){}
  ngOnInit(){
    this.isLoggedIn=this.authService.isLoggedIn();
  }
  showSignIn() {
    this.signin.openOverlay();
    
  }

}
