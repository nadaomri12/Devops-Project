import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { PDFService } from 'src/app/services/pdf.service';
import { Location } from '@angular/common';
import { ConfirmationService, MessageService } from 'primeng/api';
export interface Utilisateur {
  fullName: string;
  createdAt: Date;
  updatedAt: Date;
  canlogin:boolean;
  code: string;
  pathAvatar: string;
  language:String;
  email: string; 
}

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent implements AfterViewInit {
  Users: Utilisateur[] = [];
 totalUsers :any //used for the pagination
  displayedColumns: string[] = [ 'code','fullName', 'email', 'action'];
  dataSource: MatTableDataSource<Utilisateur>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  showPopup: boolean = false;
  selectedUser: any;
  userForm!: FormGroup;
  currentPage = 0; 
  pageSize = 5; 
  isFormSubmitted: boolean = false;
  constructor(
    private router: Router,
    private authService: AuthService,
    private fb: FormBuilder,
    private pdfservice:PDFService,
    private dialog: MatDialog,
    private location: Location,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    
    private snackBar: MatSnackBar,
  ) {
    this.dataSource = new MatTableDataSource<Utilisateur>([]);
  }

  ngOnInit() {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]+$/)]],
      password: ['', Validators.required],
      fullName: ['', Validators.required],
      role: ['', Validators.required],
      canlogin: [false, Validators.required],
      pathAvatar: [''],
      language: ['', Validators.required],
    });
  console.log(this.currentPage);
   
    this.getAllUsers();
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
      duration: 3000, // Durée de l'affichage en millisecondes
      horizontalPosition: 'center',
      verticalPosition: 'top',
    })
  }
  ngAfterViewInit() {
  
    this.dataSource.sort = this.sort;
  
    // Set default sort direction and column
    this.sort.active = 'createDate'; // Use 'createDate' or 'Modification' based on preference
    this.sort.direction = 'desc'; // 'desc' for descending (most recent first)
    
  }
  
  getAllUsers() {
    this.authService.getAllUsers(this.currentPage, this.pageSize).subscribe(
      (data) => {
        // Sort users by the most recent of either createdAt or updatedAt date in descending order
        this.Users = data.content.sort((a: Utilisateur, b: Utilisateur) => {
          const aDate = a.updatedAt ? new Date(a.updatedAt).getTime() : new Date(a.createdAt).getTime();
          const bDate = b.updatedAt ? new Date(b.updatedAt).getTime() : new Date(b.createdAt).getTime();
          return bDate - aDate; // Sort in descending order
        });
  
        this.dataSource.data = this.Users;
        this.totalUsers = data.totalElements;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );
  }
  
  

  onPageChange(event: PageEvent) : PageEvent{
    

    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
 
     // Journaux pour le débogage
  console.log('Page Size:', this.pageSize);
  console.log('Current Page:', this.currentPage);
  console.log('totalUsers',this.totalUsers)
    this.getAllUsers();
    return event;
  }






  confirmDelete(event: Event, id: number): void{
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous vraiment supprimer ce utilisateur ?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
    this.authService.deleteUser(id).subscribe(
      () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'Utilisateur supprimée avec succès',
          life: 3000,
        });
        this.getAllUsers();
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec de la suppression de l\'utilisateur',
          life: 3000,
        });
        console.error('Error deleting user:', error);
      }
    );
  },
  reject: () => {
    this.messageService.add({
      severity: 'info',
      summary: 'Annulé',
      detail: 'Suppression annulée',
      life: 3000,
    });
  },
});
}



  

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  

  downloadData() {
    {
       this.pdfservice.exportUsersToPDF().subscribe(response => {
         const blob = new Blob([response], { type: 'application/pdf' });
         const url = window.URL.createObjectURL(blob);
         const a = document.createElement('a');
         a.href = url;
         a.download = `users_${new Date().toISOString()}.pdf`;
         document.body.appendChild(a);
         a.click();
         document.body.removeChild(a);
       });
     }
   }



  showUserDialog: boolean = false;
  showUserDetail:boolean = false;


openUserDialog(user: Utilisateur): void {
  this.selectedUser = { ...user };
  this.showUserDialog = true;
  this.getAllUsers(); // Refresh data when opening the popup

}

closeUserDialog(): void {
  this.showUserDialog = false;
  this.selectedUser = undefined;
}
openUserdetail(user: Utilisateur): void {
  this.selectedUser = { ...user };
  this.showUserDetail = true;
}

closeUserDetail(): void {
  this.showUserDetail = false;
  this.selectedUser = undefined;
}

addUser() {
  console.log('Form Values:', this.userForm.value);
  console.log('Form Valid:', this.userForm.valid);
  if (this.userForm.valid) {
    const userObject = {
      email: this.userForm.value.email,
      password: this.userForm.value.password,
      canlogin: this.userForm.value.canlogin,
      role: this.userForm.value.role,
      language: this.userForm.value.language,
      fullName: this.userForm.value.fullName,
    };

    this.authService.signUp(userObject).subscribe({
      next: (res) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'utilisateur est ajoutée avec succès',
          life: 3000,
        });
        this.userForm.reset(),
        this.closePopup();
        this.getAllUsers();
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec de l\'ajout de l\'utilisateur',
          life: 3000,
        });
        console.error('Error adding user:', error);
      }
    });
    

  } else {
    console.log('Form is invalid');
  }
}

  openPopup(): void {
    this.showPopup = true;

  }

  closePopup(): void {
    this.showPopup = false;
    
  }
  handleBackButton(): void {
    // Display the informational toast
    this.messageService.add({
      severity: 'info',
      summary: 'Annulation',
      detail: 'Modification annulée',
      life: 3000 // Duration of the toast
    });
  }
  updateUser(): void {
    if (this.selectedUser) {
      this.authService.updateUser(this.selectedUser.id, this.selectedUser).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Utilisateur mis à jour avec succès',
            life: 3000,
          });
          this.userForm.reset();
          this.closeUserDialog();
          this.getAllUsers();
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Échec de la mise à jour de l\'utilisateur',
            life: 3000,
          });
          console.error('Error updating user:', error);
        }
      });
    }
  }
  
  }
  

  
  

 

