import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Item } from '../../interfaces/item.interface';
import { ItemService } from '../../services/item.service';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule,MatIconModule,RouterLink],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss'
})
export class InventoryComponent implements OnInit, AfterViewInit {
  items: Item[] = [];
  dataSource = new MatTableDataSource<Item>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private itemService: ItemService){}

  ngOnInit(): void {
    this.itemService.getItems().subscribe((response) => {
      this.items = response.data; // Extract supplier data
      this.dataSource.data = this.items;
    });
  }
  displayedColumns: string[] = ['itemId', 'name', "category" , "quantity" , 'price','reorderLevel','lastUpdated','viewItem'];

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  addItem(){
    
  }

}
