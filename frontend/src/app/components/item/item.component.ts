import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../services/item.service';
import { Item } from '../../interfaces/item.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { EditItemDialogComponent } from '../edit-item-dialog/edit-item-dialog.component';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { ConfirmationDialogComponent } from '../common-elements/confirmation-dialog/confirmation-dialog.component';
import { LocationService } from '../../services/location.service';
import { Location } from '../../interfaces/location.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [MatIconModule,MatButtonModule,CommonModule],
  templateUrl: './item.component.html',
  styleUrl: './item.component.scss'
})
export class ItemComponent implements OnInit{
  item:Item;
  message:String;
  locations:string[];

  constructor(private dialog: MatDialog,private itemService:ItemService,
    private route:ActivatedRoute,private router : Router,
  private locationService:LocationService){}

  ngOnInit() {
   this.refreshItemData();
  }

  openEditItem(){
    const dialogRef = this.dialog.open(EditItemDialogComponent, {
      width: '400px',
      data: { item: this.item },
    });

    dialogRef.afterClosed().subscribe(() => {
      this.refreshItemData();
    });
  }

  openConfirmationDialogForDelete(){
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Are you sure you want to delete this item?' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result == "true") { 
        this.deleteItem();
      } else {
        console.log('Deletion canceled by user.');
      }
    });
  }

  deleteItem(){
    this.itemService.deleteItem(this.item.itemId).subscribe((res)=>{
      this.message=Array.isArray(res.data)? res.data[0]: res.data;
      this.router.navigate(['/task/inventory']);

    })
  }

  refreshItemData() {
    const itemId = this.route.snapshot.params['itemId'];
    if (itemId) {
      this.itemService.getItem(itemId).subscribe((response) => {
        this.item = Array.isArray(response.data) ? response.data[0] : response.data;
        console.log(this.item);
        this.fetchLocationNames();

      });
    }
  }
  fetchLocationNames() {
    if (this.item?.availableLocations?.length) {
      const locationIds = this.item.availableLocations;
      
      // Call getAllLocationByIds from LocationService to fetch location details
      this.locationService.getAllLocationsByIds(locationIds).subscribe((response) => {
        if (response?.data) {
          // Map the locations to their names
          this.locations = response.data.map((location: any) => location.name);
          console.log('Location names:', this.locations);
        } else {
          console.error('Failed to fetch locations');
        }
      });
    }
  }

}

