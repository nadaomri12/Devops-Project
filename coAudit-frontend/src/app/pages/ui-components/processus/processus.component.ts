import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth';
import { ApplicationService } from 'src/app/services/application.service'; 
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { Utilisateur } from '../users/users.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PDFService } from 'src/app/services/pdf.service';
import { ProcessService } from 'src/app/services/process.service';

export interface Process {
  title: string;
  id: string;
  version: string;
  pilot: Utilisateur;
  createdAt: Date;
  updatedAt: Date;
  code: string;
}

@Component({
  selector: 'app-processus',
  templateUrl: './processus.component.html',
  styleUrls: ['./processus.component.css'],
  providers: [ConfirmationService, MessageService],
})
export class ProcessusComponent implements AfterViewInit {
  displayedColumns: string[] = ['code', 'title', 'pilote', 'version',  'Objectives', 'Operations', 'action'];
  dataSource: MatTableDataSource<Process>;
  process: Process[] = [];
  Users: any[] = [];
  currentPage = 0;
  pageSize = 10;
  totalProcess = 0;
  userRole: any;
  dataForm!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private router: Router,
  
    private pdfservice:PDFService,
    private processService:ProcessService,
    private auth: AuthService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
    this.dataSource = new MatTableDataSource<Process>([]);
  }

  ngOnInit(): void {
    this.userRole = localStorage.getItem('userRole');
    this.dataForm = this.fb.group({
      version: ['', Validators.required],
      title: ['', Validators.required],
      pilot: ['', Validators.required]
    });
    this.getAllProcess();
  }

  ngAfterViewInit() {
  
    this.dataSource.sort = this.sort;
        // Set default sort direction and column
        this.sort.active = 'createDate'; // Use 'createDate' or 'Modification' based on preference
        this.sort.direction = 'desc'; // 'desc' for descending (most recent first)
  }

  navigateToobjective(row: any): void {
    this.router.navigate(['ui-components/processus', row.id, 'objectives']);
  }

  navigateTooperation(row: any): void {
    this.router.navigate(['ui-components/processus', row.id, 'operation']);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  isSimpleUser(): boolean {
    return this.userRole === 'SIMPLE_USER';
  }
  getAllProcess() {
    this.processService.getAllProcess(this.currentPage, this.pageSize).subscribe(
      (data) => {
        // Sort processes by the most recent of either createdAt or updatedAt date in descending order
        this.process = data.content.sort((a: Process, b: Process) => {
          const aDate = a.updatedAt ? new Date(a.updatedAt).getTime() : new Date(a.createdAt).getTime();
          const bDate = b.updatedAt ? new Date(b.updatedAt).getTime() : new Date(b.createdAt).getTime();
          return bDate - aDate; // Sort in descending order
        });
        this.dataSource.data = this.process;
        this.totalProcess = data.totalElements;
      },
      (error) => {
        console.error('Error fetching processes:', error);
      }
    );
  }
  

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;

    this.getAllProcess();
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

 

 
  selectedProcess: Process | null = null;
  showPopup: boolean = false;

  openPopup(process?: Process): void {
    this.selectedProcess = process || null;
    if (this.selectedProcess) {
      this.dataForm.patchValue({
        title: this.selectedProcess.title,
        version: this.selectedProcess.version,
        pilot: this.selectedProcess.pilot.email,
      });
    } else {
      this.dataForm.reset();
    }
    this.getAllUsers();
    this.showPopup = true;
  }

  closePopup(): void {
    this.showPopup = false;
  }

  saveProcess(): void {
    if (this.dataForm.valid) {
      const processToSave = {
        title: this.dataForm.value.title,
        version: this.dataForm.value.version,
        pilot: this.dataForm.value.pilot,
      };

      if (this.selectedProcess) {
        this.processService.updateProcess(this.selectedProcess.id, processToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Processus mis à jour avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllProcess();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour du processus',
              life: 3000,
            });
            console.error('Error updating process:', error);
          }
        );
      } else {
        this.processService.addProcess(processToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Processus ajouté avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllProcess();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de l\'ajout du processus',
              life: 3000,
            });
            console.error('Error adding process:', error);
          }
        );
      }
    }
  }

  downloadData(id:any) {
    this.pdfservice.exportProcessToPDF(id).subscribe(response => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `Objective_${new Date().toISOString()}.pdf`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    });
  }
   


  confirmDelete(event: Event, id: number): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous vraiment supprimer ce processus ?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
        this.processService.deleteProcess(id).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Processus supprimé avec succès',
              life: 3000,
            });
            this.getAllProcess();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la suppression du processus',
              life: 3000,
            });
            console.error('Une erreur s\'est produite lors de la suppression du processus:', error);
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
    // Display the informational toast
    this.messageService.add({
      severity: 'info',
      summary: 'Annulation',
      detail: 'Modification annulée',
      life: 3000 // Duration of the toast
    });
  
    // Close the dialog and reset the selected process
    this.closePopup();
  }
}
