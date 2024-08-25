import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';

// icons
import { TablerIconsModule } from 'angular-tabler-icons';
import * as TablerIcons from 'angular-tabler-icons/icons';

// Import all material modules
import { MaterialModule } from './material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


// Import Layouts
import { FullComponent } from './layouts/full/full.component';
import { BlankComponent } from './layouts/blank/blank.component';

// Vertical Layout
import { SidebarComponent } from './layouts/full/sidebar/sidebar.component';
import { HeaderComponent } from './layouts/full/header/header.component';
import { BrandingComponent } from './layouts/full/sidebar/branding.component';
import { AppNavItemComponent } from './layouts/full/sidebar/nav-item/nav-item.component';
import { ImageUploadComponent } from './image-upload/image-upload.component';
import { UicomponentsModule } from './pages/ui-components/ui-components.module';
import { DialogComponent } from './dialog/dialog.component';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MatDialog } from '@angular/material/dialog';
import { AuthInterceptorService } from './interceptors/auth-interceptor.service';


@NgModule({
  declarations: [
    AppComponent,
    FullComponent,
    BlankComponent,
    SidebarComponent,
    HeaderComponent,
    BrandingComponent,
    AppNavItemComponent,
    DialogComponent
   

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ConfirmPopupModule,
    ToastModule,
    ReactiveFormsModule,
    MaterialModule,
    ImageUploadComponent,
    TablerIconsModule.pick(TablerIcons),
    
    FormsModule,
    MatDialogModule,
    MatSnackBarModule,
   
  ],
  exports: [TablerIconsModule ,ImageUploadComponent],
  providers: [ConfirmationService, MessageService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
    
  ],
 
  bootstrap: [AppComponent],
  
})
export class AppModule {}
