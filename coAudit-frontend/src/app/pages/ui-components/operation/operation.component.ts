import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApplicationService } from 'src/app/services/application.service'; 
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { PageEvent } from '@angular/material/paginator';
import { Utilisateur } from '../users/users.component';
import { AuthService } from 'src/app/services/Auth';
import { Location } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

import { ConfirmationService, MessageService } from 'primeng/api';
import { PDFService } from 'src/app/services/pdf.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { PostService } from 'src/app/services/post.service';
 export interface Poste{
  designation: string;
 }
export interface Operation {
  code: string;
  designation: string;
  responsible: Utilisateur;
  responsibility: Poste;
  createdAt: Date;
  updatedAt: Date;
  id: string;
  tache: string; 
}

@Component({
  selector: 'app-menu',
  templateUrl: './operation.component.html',
  styleUrls: ['./operation.component.css'],
  providers: [ConfirmationService, MessageService],
})
export class OperationComponent implements AfterViewInit {
  displayedColumns: string[] = ['code', 'designation', 'responsible', 'responsibility', 'tache', 'action'];
  dataSource: MatTableDataSource<Operation>;
  operations: Operation[] = [];
  Users: any[] = []; 
  currentPage = 0;
  pageSize = 10;
  totalOperations = 0;
  processID: any;
  dataForm!: FormGroup;
  dataa: string[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  filteredOptions!: Observable<string[]>;
  constructor(
    private router: Router,
    private appService: ApplicationService,
    private pdfservice:PDFService,
    private fb: FormBuilder,
    private postService:PostService,
    private route: ActivatedRoute,
    private auth: AuthService,
    private location: Location,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
    this.dataSource = new MatTableDataSource<Operation>([]);
  }

  ngOnInit(): void {
    this.processID = this.route.snapshot.paramMap.get('id');
    this.dataForm = this.fb.group({
      designation: ['', Validators.required],
      responsible: ['', Validators.required],
      responsibility: ['', Validators.required],
    });
    
    this.filteredOptions = this.dataForm.get('responsibility')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || ''))
    );
    
    this.getAllOperations();
    this.getAllUsers();
    this.getAllPoste()
  }
  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.dataa.filter(option => option.toLowerCase().includes(filterValue));
  }
  getAllPoste(): void {
    this.postService.getPoste().subscribe(
      (data: any) => {
        if (data && Array.isArray(data)) {
          this.dataa = data.map((item: any) => item.designation);
          console.log('All data:', this.dataa);
        }
      },
      error => {
        console.error('Error fetching all data:', error);
      }
    );
  }
  ngAfterViewInit() {
    this.getAllOperations();
    this.getAllUsers();
    this.getAllPoste()
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
        // Set default sort direction and column
        this.sort.active = 'createDate'; // Use 'createDate' or 'Modification' based on preference
        this.sort.direction = 'desc'; // 'desc' for descending (most recent first)
  }

  navigateBack(): void {
    this.location.back();
  }

  navigateToTask(row: any): void {
    this.router.navigate(['ui-components/operation', row.id, 'task']);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  getAllOperations() {
    this.appService.getAllProcessOperation(this.processID).subscribe(
      (data: Operation[]) => {
        // Sort the operations by the most recent of either createdAt or updatedAt date in descending order
        this.operations = data.sort((a, b) => {
          const aDate = a.updatedAt ? new Date(a.updatedAt).getTime() : new Date(a.createdAt).getTime();
          const bDate = b.updatedAt ? new Date(b.updatedAt).getTime() : new Date(b.createdAt).getTime();
          return bDate - aDate; // Sort in descending order
        });
        this.dataSource.data = this.operations;
      },
      (error) => {
        console.error('Error fetching operations:', error);
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

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.getAllOperations();
  }

 

  showAlert(message: string): void {
    this.messageService.add({
      severity: 'info',
      summary: 'Information',
      detail: message,
      life: 3000
    });
  }

  downloadData() {
    this.pdfservice.exportOPERATIONProcessToPDF(this.processID).subscribe(response => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `Operationsprocess_${new Date().toISOString()}.pdf`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    });
  }

  selectedOperation: Operation | null = null;
  showPopup: boolean = false;

  openPopup(operation?: Operation): void {
    this.selectedOperation = operation || null;
    if (this.selectedOperation) {
      this.dataForm.patchValue({
        code: this.selectedOperation.code,
        designation: this.selectedOperation.designation,
        responsible: this.selectedOperation.responsible.email,
        responsibility: this.selectedOperation.responsibility.designation,
      });
    } else {
      this.dataForm.reset();
    }
    this.showPopup = true;
    this.getAllOperations();
  }

  closePopup(): void {
    this.showPopup = false;
  }

  saveOperation() {
    if (this.dataForm.valid) {
      const operationToSave = {
        designation: this.dataForm.value.designation,
        responsible: this.dataForm.value.responsible,
        responsibility: this.dataForm.value.responsibility,
        process: this.processID,
      };

      if (this.selectedOperation) {
        this.appService.updateOperation(this.selectedOperation.id, operationToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Opération mise à jour avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllOperations();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour de l\'opération',
              life: 3000,
            });
            console.error('Error updating operation:', error);
          }
        );
      } else {
        this.appService.addOperation(operationToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Opération ajoutée avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllOperations();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de l\'ajout de l\'opération',
              life: 3000,
            });
            console.error('Error adding operation:', error);
          }
        );
      }
    }
  }

  confirmDelete(event: Event, id: number): void{
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous vraiment supprimer cette opération ?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
        this.appService.deleteOperation(id).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Opération supprimée avec succès',
              life: 3000,
            });
            this.getAllOperations();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la suppression de l\'opération',
              life: 3000,
            });
            console.error('Error deleting operation:', error);
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

  handleBackButton(): void {
    this.messageService.add({
      severity: 'info',
      summary: 'Annulation',
      detail: 'Modification annulée',
      life: 3000 // Duration of the toast
    });
    this.closePopup();
  }
}
