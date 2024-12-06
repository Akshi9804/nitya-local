import { Component } from '@angular/core';
import {  MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'app-add-new-location',
  standalone: true,
  imports: [CommonModule,MatDialogModule,ReactiveFormsModule],
  templateUrl: './add-new-location.component.html',
  styleUrl: './add-new-location.component.scss'
})
export class AddNewLocationComponent {
  location!: Location;
  disableAddLocation: boolean = false;
  addLocationForm!: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<AddNewLocationComponent>,
    private locationService:LocationService,
    private formBuilder:FormBuilder, 
  ) {
    this.constructForm();
  }
  

  constructForm() {
    this.addLocationForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      address: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  addLocation(){
    if (this.addLocationForm.valid) {
      const newLocation = this.addLocationForm.getRawValue();
      this.locationService.addLocation(newLocation).subscribe({
        next:(response) => {
          console.log('Item added successfully:', response);
          this.dialogRef.close(response.data); 
        },
        error: (error) => {
          console.error('Error adding item:', error);
        }
    });
    }
  }
}
