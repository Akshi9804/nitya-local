import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-menubar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './menubar.component.html',
  styleUrl: './menubar.component.scss'
})
export class MenubarComponent implements OnInit {
  constructor(private userService: UserService, private router: Router) { }
  role: string;
  ngOnInit(): void {
    this.role = this.userService.role;
  }

  isActive(route: string): boolean {
    return this.router.url === route; // Compare the current URL with the given route
  }

  checkRole(): boolean {
    if (this.role === 'admin') {
      return true;
    } else {
      return false;
    }
  }
}
