import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationService } from '../../services/location.service';
import { Location } from '../../interfaces/location.interface';
import { ItemService } from '../../services/item.service';
import { Item } from '../../interfaces/item.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-location-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './location-details.component.html',
  styleUrls: ['./location-details.component.scss'],
})
export class LocationDetailsComponent implements OnInit {
  location: Location;
  items: Item[];
  itemsAtLocation:any[];

  constructor(
    private route: ActivatedRoute,
    private locationService: LocationService,
    private itemService: ItemService,
    private router:Router
  ) {}

  ngOnInit() {
    this.fetchLocationDetails();
  }

  fetchLocationDetails() {
    const locId = this.route.snapshot.paramMap.get('locId');
    if (locId) {
      this.locationService.getLocationById(locId).subscribe({
        next: (res) => {

          const locationData = res.data;
          
          // Mapping the location data including stockDetails
          this.location = {
            locId: locationData.locId,
            name: locationData.name,
            address: locationData.address,
            stockDetails: new Map(
              Object.entries(locationData.stockDetails || {}).map(
                ([key, value]) => [key, value] // Convert stockDetails to a Map
              )
            ),
          };
          const itemIds=Array.from(this.location.stockDetails.keys()) as string[];
          this.itemService.getAllItemsByIds(itemIds).subscribe((itemRes) => {
            this.items = itemRes.data;
            console.log(this.items);
            this.itemsAtLocation = Array.from(this.location.stockDetails.entries()).map(
              ([itemId, quantity]) => {
                const item = this.items.find((item) => item.itemId === itemId);
                return {
                  itemName: item ? item.name : 'Unknown Item', // Fallback if no item is found
                  quantity: quantity,
                };
              }
            );
          });
        },
        error: (error) => {
          console.error('Error fetching location data: ', error);
        },
      });
    }
  }

  openAddExistingItem(){
    this.router.navigate(['/task/location',this.location.locId,'add-existing-item-location']);
  }
}
