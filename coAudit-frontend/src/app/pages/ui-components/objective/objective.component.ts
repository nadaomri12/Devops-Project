
import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/Auth';
import { ApplicationService } from 'src/app/services/application.service'; 
import { FormBuilder, FormGroup , Validators} from '@angular/forms';
import { PolitiqueDeQualite } from '../QualityPolicy/QualityPolicy.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog'; 
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { ReactiveFormsModule } from '@angular/forms'; 
import { PDFService } from 'src/app/services/pdf.service';
import { Location } from '@angular/common';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ProcessService } from 'src/app/services/process.service';
import { QualitypolicyService } from 'src/app/services/qualitypolicy.service';
export interface Objective{
 updatedAT:Date;
 createdAt:Date;
 designation: String;
 axe:PolitiqueDeQualite;
 id:String
}
@Component({
  selector: 'app-objective',
  templateUrl: './objective.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    ToastModule,
    ConfirmPopupModule
   
  ],
  styleUrls: ['./objective.component.scss'],
 
})
export class ObjectiveComponent implements AfterViewInit {
  
  displayedColumns: string[] = ['code','designation', 'axe','action' ];
  dataSource: MatTableDataSource<Objective>;
  objective: Objective[] = [];
  obj:any;
  totalobjective:any;
  processId: any;
  currentPage = 0; 
  pageSize = 5; 
  objForm!: FormGroup;
  showPopup: boolean = false;
  QualityPolicy:any
  totalProcess=0;
  selectedObjective: Objective | null = null;
  processTitle: string = ''; 

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(private router: Router , private route: ActivatedRoute,
    private fb: FormBuilder,private appService:ApplicationService ,
    private auth:AuthService ,
    private pdfservice:PDFService,
    private processService:ProcessService,
    private qualitypolicyService:QualitypolicyService,
    private dialog: MatDialog,
    private location: Location,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private snackBar: MatSnackBar,) {
     
    this.dataSource = new MatTableDataSource<Objective>([]);

  }
  
  ngOnInit(): void {
    this.processId = this.route.snapshot.paramMap.get('id');
    this.objForm = this.fb.group({
      designation: ['', Validators.required],
      axe:['', Validators.required]
    })
    this.getAllObjective();
    
    this. getProcessDetails() ;

  
  }
  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.sort.active = 'updatedAT'; // Use 'createDate' or 'Modification' based on preference
    this.sort.direction = 'desc'; // 'desc' for descending (most recent first)
  }
  openDialog(title: string, message: string): void {
    this.dialog.open(DialogComponent, {
      data: {
        title: title,
        message: message
      }
    });
  }
  onPageChange(event: PageEvent) : PageEvent{
    

    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
 
     // Journaux pour le débogage
  console.log('Page Size:', this.pageSize);
  console.log('Current Page:', this.currentPage);
  console.log('totalUsers',this. totalobjective)
    this.getAllObjective();
    return event;
  }
  showAlert(message: string): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000, // Durée de l'affichage en millisecondes
      horizontalPosition: 'center',
      verticalPosition: 'top',
    })
  }

  getProcessDetails() {
    this.processService.  getProcess(this.processId).subscribe(
      (process) => {
        this.processTitle = process.title; 
      },
      (error) => {
        console.error('Error fetching process details:', error);
      }
    );
  }

  openPopup(objective?: Objective): void {
    this.selectedObjective = objective || null;
    if (this.selectedObjective) {
      this.objForm.patchValue({
        designation: this.selectedObjective.designation,
        axe: this.selectedObjective.axe.title,
      
      });
      
    } else {
      this.objForm.reset();
    }
    this.getAllQualityPolicy();
    this.showPopup = true;
  }

  closePopup(): void {
    this.showPopup = false;
  }
  saveObj() {
    if (this.objForm.valid) {
      const objToSave = {
        designation: this.objForm.value.designation,
        axe: this.objForm.value.axe,
        process: this.processId
      };

      if (this.selectedObjective) {
        // Update objective logic
        this.appService.updateObjective(this.selectedObjective.id, objToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Objective mis à jour avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllObjective();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour de l\'objective',
              life: 3000,
            });
            console.error('Error updating objective:', error);
          }
          
        
        );
      } else {
        // Add objective logic
        this.appService.addObjective(objToSave).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Objective ajoutée avec succès',
              life: 3000,
            });
            this.closePopup();
            this.getAllObjective();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de l\'ajout de l\'objective',
              life: 3000,
            });
            console.error('Error adding objective:', error);
          }
        );
      }
    }
    }
  
  getAllQualityPolicy() {
    this.qualitypolicyService.getAllQualityPolicy().subscribe(
      (data) => {
        this.QualityPolicy = data;
      
      },
      (error) => {
        console.error('Error fetching quality policies:', error);
      }
    );
  }



  getAllObjective() {
    this.appService.getAllProcessObjective(this.processId).subscribe(
      (data: Objective[]) => {
        // Sort the objectives by the most recent of either createdAt or updatedAt date in descending order
        this.obj = data.sort((a, b) => {
          const aDate = a.updatedAT ? new Date(a.updatedAT).getTime() : new Date(a.createdAt).getTime();
          const bDate = b.updatedAT ? new Date(b.updatedAT).getTime() : new Date(b.createdAt).getTime();
          return bDate - aDate; // Sort in descending order
        });
        this.dataSource.data = this.obj;
        
      },
      (error) => {
        console.error('Error fetching objectives:', error);
      }
    );
  }
  

  confirmDelete(event: Event, id: number): void{
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous vraiment supprimer cette objective ?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
    this.appService.deleteObj(id).subscribe(
      () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'Objective supprimée avec succès',
          life: 3000,
        });
        this.getAllObjective();
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec de la suppression de l\'objective',
          life: 3000,
        });
        console.error('Error deleting objective:', error);
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


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }


  downloadData() {
    this.pdfservice.exportObjectiveProcessToPDF(this.processId).subscribe(response => {
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
}
