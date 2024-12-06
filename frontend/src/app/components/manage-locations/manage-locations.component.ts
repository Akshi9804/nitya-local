import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-manage-locations',
  standalone: true,
  imports: [RouterOutlet,RouterLink],
  templateUrl: './manage-locations.component.html',
  styleUrl: './manage-locations.component.scss'
})
export class ManageLocationsComponent {

  constructor(private router:Router){}
  
  isActive(route: string): boolean {
    return this.router.url === route; // Compare the current URL with the given route
  }
}
