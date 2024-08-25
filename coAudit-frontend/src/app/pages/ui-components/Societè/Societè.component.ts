import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationService } from 'src/app/services/application.service'; 
import { AuthService } from 'src/app/services/Auth';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { CompanyService } from 'src/app/services/company.service';

@Component({
  selector: 'app-Societè',
  templateUrl: './Societè.component.html',
  styleUrls: ['./Societè.component.css'],
})
export class SocietèComponent implements OnInit {
  isNewCompany: boolean = true;
  company: any = {};
  selectedFile: File | null = null;
  Users: any[] = [];
  companyForm!: FormGroup;
  isFormSubmitted: boolean = false;
  currentPage = 0;
  image: string | ArrayBuffer | null = null; // To store the image preview

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private companyService: CompanyService,
    private auth: AuthService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.companyForm = this.fb.group({
      id: [''],
      name: ['', Validators.required],
      code: [''],
      responsible: ['', Validators.required],
      address: ['', Validators.required],
      image: [''],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]*$/), Validators.maxLength(8), Validators.minLength(8)]],
      file: [null]
    });

    this.getAllUsers();
    this.loadExistingCompany();
  }

  openDialog(title: string, message: string): void {
    this.dialog.open(DialogComponent, {
      data: {
        title: title,
        message: message
      }
    });
  }

  showAlert(message: string): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000, // Duration in milliseconds
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

  loadExistingCompany() {
    this.companyService.getcompany().subscribe(
      (companies: any[]) => {
        if (companies && companies.length > 0) {
          this.isNewCompany = false;
          const company = companies[0];
          this.companyForm.patchValue({
            id: company.id,
            name: company.name,
            code: company.code,
            responsible: company.responsible.email,
            address: company.address,
            phone: company.phone,
            image:company.image
           
          });
        
          console.log('Form values:', this.companyForm.value);
        } else {
          console.log('No companies found');
          }
      },
      (error) => {
        console.error('Error fetching company:', error);
      }
    );
  }

  getAllUsers() {
    this.auth.getAllUsers(this.currentPage, 100).subscribe(
      (data) => {
        this.Users = data.content;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.companyForm.patchValue({ file: file });
  
      // Preview the image
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.image = e.target.result; // This is the base64-encoded image data
      };
      reader.readAsDataURL(file);
    }
  }
  

  addCompany() {
    if (this.companyForm.valid) {
      const formData = new FormData();
      
      // Convert companyForm value to JSON string
      const companyDtoJson = JSON.stringify(this.companyForm.value);
    
      // Append the JSON string to FormData
      formData.append('companyDto', companyDtoJson);

      // Append the file to FormData if it exists
      if (this.selectedFile) {
        formData.append('file', this.selectedFile);
      }

      // Call the service to add the company
      this.companyService.addcompany(formData).subscribe(
        (response) => {
          console.log('Company added successfully:', response);
          this.openDialog('Succès', 'Votre société a été ajoutée avec succès');
          this.companyForm.reset();
          this.selectedFile = null; // Clear the file after submission
        
        },
        (error) => {
          console.error('Error adding company:', error);
          this.showAlert('Une erreur est survenue lors de l\'ajout de la société');
        }
      );
    } else {
      console.log('Form is invalid');
    }
  }

  updateCompany() {
    const companyData = this.companyForm.value;

    if (companyData.id) {
      // Update existing company
      this.companyService.updatecompany(companyData.id, companyData).subscribe(
        (response) => {
          console.log('Company updated successfully:', response);
          this.openDialog('Succès', 'Votre société a été mise à jour avec succès');
          this.router.navigate(['/ui-components/societe']);
        },
        (error) => {
          console.error('Error updating company:', error);
        }
      );
    }
  }
}
