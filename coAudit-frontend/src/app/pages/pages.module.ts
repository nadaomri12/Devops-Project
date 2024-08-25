import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { MaterialModule } from '../material.module';
import { FormsModule } from '@angular/forms';
import { NgApexchartsModule } from 'ng-apexcharts';
import { MatPaginatorModule } from '@angular/material/paginator';
// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';

import { ImageUploadComponent } from "../image-upload/image-upload.component";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    NgApexchartsModule,
    MatPaginatorModule,
   
    TablerIconsModule.pick(TablerIcons),
    ImageUploadComponent
],
  exports: [TablerIconsModule ,ImageUploadComponent],
})
export class PagesModule {}
