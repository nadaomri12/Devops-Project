
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
  selector: 'app-dialog',
 
  template: `
  <h1 mat-dialog-title>{{ data.title }}</h1>
  <div mat-dialog-content>{{ data.message }}</div>
  <div mat-dialog-actions>
    <button mat-button (click)="onClose()">OK</button>
  </div>
`,
styleUrls: ['./dialog.component.scss'],
 
})
export class DialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onClose(): void {
    this.dialogRef.close();
  }
}

