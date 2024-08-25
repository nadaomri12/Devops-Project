import { Component, AfterViewInit, ViewChild, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Router, ActivatedRoute } from '@angular/router';
import { ApplicationService } from 'src/app/services/application.service'; 
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
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
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { DataService } from 'src/app/services/data.service';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { Location } from '@angular/common';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { PDFService } from 'src/app/services/pdf.service';
import { TaskService } from 'src/app/services/task.service';

export interface TaskData {
  id: string;
  data: {
    id: string;
    createdAt: Date;
    updatedAt: Date;
    code: string;
    designation: string;
  };
  type: string;
}

@Component({
  selector: 'app-data',
  standalone: true,
  imports: [
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
    ConfirmPopupModule,
  ],
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss'],
  providers: [ConfirmationService, MessageService] // Add these providers
})
export class DataComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['data.code', 'data.designation', 'type', 'action'];
  dataSource: MatTableDataSource<TaskData> = new MatTableDataSource<TaskData>([]);
  TaskData: TaskData[] = [];
  dataForm!: FormGroup;
  selectedData: TaskData | null = null;
  showPopup: boolean = false;
  dataa: string[] = [];
  filteredOptions!: Observable<string[]>;
  TaskId: any;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private router: Router,
    private appService: ApplicationService,
    private taskService:TaskService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private dataService: DataService,
    private location: Location,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
   
  ) {}

  ngOnInit(): void {
    this.dataForm = this.fb.group({
      designation: ['', Validators.required],
      type: ['', Validators.required],
    });
    this.TaskId = this.route.snapshot.paramMap.get('id');
    this.getAllData();
    this.getAllDataTask();
    this.filteredOptions = this.dataForm.get('designation')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || ''))
    );
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

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.dataa.filter(option => option.toLowerCase().includes(filterValue));
  }

  getAllData(): void {
    this.dataService.getAllData().subscribe(
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

  getAllDataTask(): void {
    if (this.TaskId) {
      this.taskService.getAllDataTask(this.TaskId).subscribe(
        (taskData: any) => {
          this.dataSource.data = taskData;
        },
        error => {
          console.error('Error fetching task data:', error);
        }
      );
    }
  }

  openPopup(data?: TaskData): void {
    this.selectedData = data || null;

    if (this.selectedData) {
      this.dataForm.patchValue({
        designation: this.selectedData.data.designation,
        type: this.selectedData.type,
      });
    } else {
      this.dataForm.reset();
    }

    this.showPopup = true;
  }

  closePopup(): void {
    this.showPopup = false;
  }

  saveData() {
    if (this.dataForm.valid) {
      const DataToSave = {
        designation: this.dataForm.value.designation,
        type: this.dataForm.value.type,
        task: this.TaskId,
      };

      if (this.selectedData) {
        this.dataService.updateData(this.selectedData.id, this.selectedData.data.id, DataToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Données mises à jour avec succès',
              life: 3000
            });
            this.closePopup();
            this.getAllDataTask();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour des données',
              life: 3000
            });
            console.error('Error updating Data:', error);
          }
        );
      } else {
        this.dataService.addData(DataToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Données ajoutées avec succès',
              life: 3000
            });
            this.closePopup();
            this.getAllDataTask();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de l\'ajout des données',
              life: 3000
            });
            console.error('Error adding Data:', error);
          }
        );
      }
    }
  }

  confirmDelete(event: Event, id: number): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous vraiment supprimer cette donnée ?',
      header: 'Confirmation',
      icon: 'pi pi-info-circle',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
        this.dataService.deleteData(id).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Donnée supprimée avec succès',
              life: 3000
            });
            this.getAllDataTask();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la suppression de la donnée',
              life: 3000
            });
            console.error('Error deleting data:', error);
          }
        );
      },
      reject: () => {
        this.messageService.add({
          severity: 'info',
          summary: 'Annulé',
          detail: 'Suppression annulée',
          life: 3000
        });
      }
    });
  }

  navigateBack(): void {
    this.location.back();
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

