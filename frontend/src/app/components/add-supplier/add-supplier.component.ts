import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SupplierService } from '../../services/supplier.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-add-supplier',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './add-supplier.component.html',
  styleUrl: './add-supplier.component.scss'
})
export class AddSupplierComponent {

  disableAddSupplier: boolean = false;

  addSupplierForm!: FormGroup;
  constructor(private formBuilder: FormBuilder,private supplierService: SupplierService, private router: Router) {
    this.constructForm();
  }
  constructForm() {
    this.addSupplierForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern(/^\d{10}/)]],
      address: ['', [Validators.required, Validators.minLength(5)]],
      deliveryInDays: ['', [Validators.required ]]
    });
  }

  addSupplier(){
    if (this.addSupplierForm.invalid) {
      console.error('Form is invalid');
      return;
    }

    const supplierData = this.addSupplierForm.value;


    this.supplierService.addSupplier(supplierData).subscribe({
      next: (response) => {
        console.log('Supplier added successfully:', response);
        this.router.navigate(['/task/suppliers']); 
      },
      error: (error) => {
        console.error('Error adding supplier:', error);
        this.disableAddSupplier = false; 
      },
    });
  }
}
