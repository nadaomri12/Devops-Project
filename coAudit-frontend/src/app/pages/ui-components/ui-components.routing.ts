import { Routes } from '@angular/router';
import { AuthGuard } from 'src/app/auth.guard';

// ui
import { QualityPolicyComponent } from './QualityPolicy/QualityPolicy.component';
import { ProcessusComponent } from './processus/processus.component';
import { SocietèComponent } from './Societè/Societè.component';
import { OperationComponent } from './operation/operation.component';
import { TaskComponent } from './Task/task.component';
import { UsersComponent } from './users/users.component';
import { ObjectiveComponent } from './objective/objective.component';
import { DataComponent } from './data/data.component';
import { PosteComponent } from './poste/poste.component';
export const UiComponentsRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'users',
        component: UsersComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'qualitypolicy',
        component: QualityPolicyComponent,
        canActivate: [AuthGuard], 
      },
      {
        path: 'processus',
        component: ProcessusComponent,
        canActivate: [AuthGuard], 
      },
      {
        path: 'Company',
        component: SocietèComponent,
        canActivate: [AuthGuard], 
      },
      {
        path: 'processus/:id/operation',
        component: OperationComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'processus/:id/objectives',
        component: ObjectiveComponent,
        canActivate: [AuthGuard], 
      },
      {
        path: 'operation/:id/task',
        component: TaskComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'task/:id/data',
        component: DataComponent,
        canActivate: [AuthGuard], 
      },
      {
        path: 'poste',
        component: PosteComponent,
        canActivate: [AuthGuard],
      },
    ],
  },
];
