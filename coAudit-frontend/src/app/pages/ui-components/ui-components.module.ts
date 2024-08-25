import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';

import { UiComponentsRoutes } from './ui-components.routing';

// ui components
import {  QualityPolicyComponent } from './QualityPolicy/QualityPolicy.component';
import { ProcessusComponent } from './processus/processus.component';
import { SocietèComponent } from './Societè/Societè.component';
import { OperationComponent } from './operation/operation.component';
import { TaskComponent } from './Task/task.component';
import { MatNativeDateModule } from '@angular/material/core';
import { ImageUploadComponent } from "../../image-upload/image-upload.component";
import { UsersComponent } from './users/users.component';
import { ObjectiveComponent } from './objective/objective.component';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PosteComponent } from './poste/poste.component';


@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(UiComponentsRoutes),
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    TablerIconsModule.pick(TablerIcons),
    MatNativeDateModule,
    ImageUploadComponent,
    ToastModule,
    ConfirmPopupModule,
    ButtonModule,
 
],
  declarations: [
    QualityPolicyComponent,
    ProcessusComponent,
    SocietèComponent,
    OperationComponent,
    TaskComponent,
    UsersComponent,
    
  
  ],
exports:[
  ImageUploadComponent,
  SocietèComponent,

],
providers:[
  ConfirmationService, MessageService,
 ],

})
export class UicomponentsModule {}