import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ApplicationService } from 'src/app/services/application.service'; 
import { Router } from '@angular/router';
import { FormBuilder, FormGroup , Validators} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PDFService } from 'src/app/services/pdf.service';
import { QualitypolicyService } from 'src/app/services/qualitypolicy.service';

export interface PolitiqueDeQualite {
  title: string;
  updatedAt:Date;
  createdAt: Date;
  code: string;
}

@Component({
  selector: 'app-QualityPolicy',
  templateUrl: './QualityPolicy.component.html',
  styleUrls: ['./QualityPolicy.component.css'],
  providers: [ConfirmationService, MessageService],
})
export class QualityPolicyComponent implements AfterViewInit {
  displayedColumns: string[] = ['code','title' ,'action'];
  dataSource: MatTableDataSource<PolitiqueDeQualite>;
  QualityPolicy: PolitiqueDeQualite[] = [];
 
  dataForm!: FormGroup;
  showPopup: boolean = false;
  showEditDialog: boolean = false;
  selectedPolicy:any
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private appService:ApplicationService ,
    private router: Router,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private pdfservice:PDFService,
    private qualitypolicyService:QualitypolicyService,
    private confirmationService: ConfirmationService, private messageService: MessageService) {
   
    this.dataSource = new MatTableDataSource<PolitiqueDeQualite>([]);
  }
 

  ngOnInit(): void {
    this.dataForm = this.fb.group({
      title: ['', Validators.required],})
    this.getAllQualityPolicy();
  }

  openDialog(title: string, message: string): void {
    this.dialog.open(DialogComponent, {
      data: {
        title: title,
        message: message
      }
    });
  }









  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.sort = this.sort;
    // Set default sort direction and column
    this.sort.active = 'createDate'; // Use 'createDate' or 'Modification' based on preference
    this.sort.direction = 'desc'; // 'desc' for descending (most recent first)
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }


 

  getAllQualityPolicy() {
    this.qualitypolicyService.getAllQualityPolicy().subscribe(
      (data: PolitiqueDeQualite[]) => {
        // Sort the data by the most recent of either createdAt or updatedAt date in descending order
        this.QualityPolicy = data.sort((a, b) => {
          const aDate = a.updatedAt ? new Date(a.updatedAt).getTime() : new Date(a.createdAt).getTime();
          const bDate = b.updatedAt ? new Date(b.updatedAt).getTime() : new Date(b.createdAt).getTime();
          return bDate - aDate; // Sort in descending order
        });
        this.dataSource.data = this.QualityPolicy;
      
      },
      (error) => {
        console.error('Error fetching quality policies:', error);
      }
    );
  }
  

  addQuality() {
    if (this.dataForm.valid) {
      const datatosave = {
        title: this.dataForm.value.title,
      };
      this.qualitypolicyService.addQuality(datatosave).subscribe(
        (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: 'Politique de qualité ajoutée avec succès',
            life: 3000,
          });
          this.dataForm.reset();
          this.closePopup();
          this.getAllQualityPolicy();
        },
        (error) => {
          console.error('Error adding quality policy:', error);
        }
      );
    }
  }

  openPopup(): void {
    this.showPopup = true;
    this.getAllQualityPolicy(); // Refresh data when opening the popup

  }

  closePopup(): void {
    this.showPopup = false;
    this.dataForm.reset(); // Clear form data on close
  }

 

  confirm2(event: Event, id: number): void {
    console.log(`Delete button clicked for ID: ${id}`);
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Voulez-vous supprimer cet enregistrement ?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      acceptLabel: 'Oui',
      rejectLabel: 'Non',
      accept: () => {
        this.deleteQualityPolicy(id);
      },
      reject: () => {
        this.messageService.add({
          severity: 'info',
          summary: 'Rejeté',
          detail: 'Vous avez rejeté',
          life: 3000,
        });
      },
    });
  }

  deleteQualityPolicy(id: number): void {
    this.qualitypolicyService.deleteQualityPolicy(id).subscribe(
      () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'Politique de qualité supprimée avec succès',
          life: 3000,
        });
        this.getAllQualityPolicy();
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur',
          detail: 'Échec de la suppression de la politique de qualité',
          life: 3000,
        });
        console.error('Une erreur s\'est produite lors de la suppression de la Politique de qualité :', error);
      }
    );
  }


    //primeng
    openEditDialog(PQ: PolitiqueDeQualite): void {
      this.selectedPolicy = { ...PQ };
      this.showEditDialog = true;
    }
    
   
    handleBackButton(): void {
      // Afficher le toast d'information
      this.messageService.add({
        severity: 'info',
        summary: 'Annulation',
        detail: 'Modification annulée',
        life: 3000 // Durée d'affichage du toast
      });
    
      // Fermer le dialogue et réinitialiser la politique sélectionnée
      this.closeEditDialog();
    }
 
    
    updateQualityPolicy(): void {
      if (this.selectedPolicy) {
        this.qualitypolicyService.updateQualityPolicy(this.selectedPolicy.id, this.selectedPolicy).subscribe({
          next: () => {
            // Show success toast notification
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Politique mise à jour avec succès',
              life: 3000 // Duration for which the toast will be visible
            });
    
            // Close the edit dialog and refresh the policy list
            this.closeEditDialog();
            this.getAllQualityPolicy();
          },
          error: (error) => {
            // Show error toast notification
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Échec de la mise à jour de la politique',
              life: 3000 // Duration for which the toast will be visible
            });
    
            console.error('Update policy error:', error);
          }
        });
      }
    }
    
    closeEditDialog(): void {
      this.showEditDialog = false;
      this.selectedPolicy = undefined;
      // No toast notification for cancellation here
    }
  
    downloadData() {
      {
         this.pdfservice.exportQPToPDF().subscribe(response => {
           const blob = new Blob([response], { type: 'application/pdf' });
           const url = window.URL.createObjectURL(blob);
           const a = document.createElement('a');
           a.href = url;
           a.download = `QP_${new Date().toISOString()}.pdf`;
           document.body.appendChild(a);
           a.click();
           document.body.removeChild(a);
         });
       }
     }
  
  }
  