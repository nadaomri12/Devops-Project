import { Component, AfterViewInit, ViewChild, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Observable } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ApplicationService } from 'src/app/services/application.service';
import { PDFService } from 'src/app/services/pdf.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PostService } from 'src/app/services/post.service';
export interface Poste{
  id: string;
  createdAt: Date;
  updatedAt: Date;
  code: string;
  designation: string;
}
@Component({
  selector: 'app-poste',
  standalone: true,
  imports:  [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatAutocompleteModule,
    ToastModule,
    ConfirmPopupModule,],
  templateUrl: './poste.component.html',
  styleUrl: './poste.component.scss'
})
export class PosteComponent implements AfterViewInit{
  displayedColumns: string[] = ['code', 'designation', 'action'];
  dataSource: MatTableDataSource<Poste> = new MatTableDataSource<Poste>([]);
  PosteID:any;
  dataForm!: FormGroup;
  poste: Poste[] = [];


  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  constructor(
    private router: Router,
    private postservice:PostService,
    private pdfservice:PDFService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private location: Location,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}
  ngOnInit(): void {
    this.dataForm = this.fb.group({
      designation: ['', Validators.required],
    });
  this.getAllPoste();
  }
  ngAfterViewInit() {
  
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  selectedJob: Poste | null = null;
  showPopup: boolean = false;

  openPopup(Poste?: Poste): void {
    this.selectedJob = Poste || null;
    if (this.selectedJob) {
      this.dataForm.patchValue({
        code: this.selectedJob.code,
        designation: this.selectedJob.designation,
       
      });
    } else {
      this.dataForm.reset();
    }
    this.showPopup = true;
    this.getAllPoste();
  }

  closePopup(): void {
    this.showPopup = false;
  }

  savePoste() {
    if (this.dataForm.valid) {
      const PosteToSave = {
        designation: this.dataForm.value.designation,
    
      };

      if (this.selectedJob) {
        this.postservice.updatePoste(this.selectedJob.id, PosteToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Opération mise à jour avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllPoste();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour de l\'opération',
              life: 3000,
            });
            console.error('Error updating Poste:', error);
          }
        );
      } else {
        this.postservice.addJob(PosteToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Poste ajoutée avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllPoste();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de l\'ajout de la poste',
              life: 3000,
            });
            console.error('Error adding Poste:', error);
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
        this.postservice.deletePoste(id).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Poste supprimée avec succès',
              life: 3000,
            });
            this.getAllPoste();
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

  getAllPoste(): void {
  
      this.postservice.getPoste().subscribe(
        (poste: any) => {
          this.dataSource.data = poste;
        },
        error => {
          console.error('Error fetching task data:', error);
        }
      );
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
    downloadData(id:any) {
      this.pdfservice.exportPosteToPDF(id).subscribe(response => {
        const blob = new Blob([response], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `FichedePoste_${new Date().toISOString()}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      });
    }
  }

