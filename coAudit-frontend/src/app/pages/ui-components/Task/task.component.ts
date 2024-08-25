import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { ApplicationService } from 'src/app/services/application.service'; 
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Location } from '@angular/common';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TaskService } from 'src/app/services/task.service';

export interface Task {
  designation: string;
  createdAt: Date;
  updatedAt: Date;
  code: string;
  id: String;
}

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./app-task.css'],
  providers: [ConfirmationService, MessageService] 
})
export class TaskComponent implements AfterViewInit {
  displayedColumns: string[] = ['code', 'designation', 'data', 'action'];
  dataSource: MatTableDataSource<Task>;
  Task: Task[] = [];
  operationId: any;
  TaskForm!: FormGroup;
  showPopup: boolean = false;
  selectedTask: Task | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private router: Router,
    private appService: ApplicationService,
    private taskService:TaskService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private location: Location,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
    this.dataSource = new MatTableDataSource<Task>([]);
  }

  ngOnInit(): void {
    this.operationId = this.route.snapshot.paramMap.get('id');
    this.TaskForm = this.fb.group({
      designation: ['', Validators.required],
    });
    this.getAllTasks();
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





  openPopup(task?: Task): void {
    this.selectedTask = task || null;
    if (this.selectedTask) {
      this.TaskForm.patchValue({
        designation: this.selectedTask.designation,
      });
    } else {
      this.TaskForm.reset();
    }
    this.getAllTasks();
    this.showPopup = true;
  }

  closePopup(): void {
    this.showPopup = false;
  }

  saveTask() {
    if (this.TaskForm.valid) {
      const taskToSave = {
        designation: this.TaskForm.value.designation,
        operation: this.operationId
      };

      if (this.selectedTask) {
        this.taskService.updateTask(this.selectedTask.id, taskToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Tâche mise à jour avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllTasks();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour de la tâche',
              life: 3000,
            });
            console.error('Error updating task:', error);
          }
        );
      } else {
        this.taskService.addTask(taskToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Tâche ajoutée avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllTasks();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de l\'ajout de la tâche',
              life: 3000,
            });
            console.error('Error adding task:', error);
          }
        );
      }
    }
  }
  confirmDelete(event: Event, id: number): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous vraiment supprimer cette tâche ?',
      header: 'Confirmation',
      icon: 'pi pi-info-circle',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
        this.taskService.deleteTask(id).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Tâche supprimée avec succès',
              life: 3000,
            });
            this.getAllTasks();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la suppression de la tâche',
              life: 3000,
            });
            console.error('Error deleting task:', error);
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
      }
    });
  }
  navigateToData(row: any): void {
    this.router.navigate(['ui-components/task', row.id, 'data']);
  }
  getAllTasks() {
    this.appService.getAllOperationTask(this.operationId).subscribe(
      (data: Task[]) => {
        // Sort tasks by the most recent of either createdAt or modifiedAt date in descending order
        this.Task = data.sort((a, b) => {
          const aDate = a.updatedAt ? new Date(a.updatedAt).getTime() : new Date(a.createdAt).getTime();
          const bDate = b.updatedAt ? new Date(b.updatedAt).getTime() : new Date(b.createdAt).getTime();
          return bDate - aDate; // Sort in descending order
        });
        this.dataSource.data = this.Task;
      },
      (error) => {
        console.error('Error fetching tasks:', error);
      }
    );
  }
  navigateBack(): void {
    this.location.back();
  }

  downloadData() {
    // Logic for downloading data
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

