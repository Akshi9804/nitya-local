import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Item } from '../../interfaces/item.interface';
import { ItemService } from '../../services/item.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';


@Component({
  selector: 'app-add-item-dialog',
  standalone: true,
  imports: [CommonModule,MatDialogModule,ReactiveFormsModule],
  templateUrl: './add-item-dialog.component.html',
  styleUrl: './add-item-dialog.component.scss'
})
export class AddItemDialogComponent {
  item!: Item;
  disableAddItem: boolean = false;
  addItemForm!: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<AddItemDialogComponent>,
    private itemService:ItemService,
    private formBuilder:FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { supplierId: string } 
  ) {
    this.constructForm();
  }
  

  constructForm() {
    this.addItemForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      category: ['', [Validators.required, Validators.minLength(5)]],
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  addItem(){
    if (this.addItemForm.valid) {
      const newItem = this.addItemForm.getRawValue();
      this.itemService.addItemFromSupplier(newItem,this.data.supplierId).subscribe(
        (response) => {
          console.log('Item added successfully:', response);
          this.dialogRef.close(response.data); // Pass the new item back to the parent
        },
        (error) => {
          console.error('Error adding item:', error);
          alert('Failed to add item. Please try again.');
        }
      );
    }
  }
}
