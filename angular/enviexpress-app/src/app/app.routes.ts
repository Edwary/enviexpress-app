import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { LayoutComponent } from './components/adm/layout/layout/layout';
import { DashboardComponent } from './components/adm/dashboard/dashboard/dashboard';

import { MaestraUbicacionesComponent } from './components/adm/maestra-ubicaciones/maestra-ubicaciones';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'maestra-ubicaciones', component: MaestraUbicacionesComponent }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
