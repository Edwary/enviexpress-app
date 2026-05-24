import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { LayoutComponent } from './components/adm/layout/layout/layout';
import { DashboardComponent } from './components/adm/dashboard/dashboard/dashboard';

import { MaestraUbicacionesComponent } from './components/adm/maestra-ubicaciones/maestra-ubicaciones';
import { ClienteComponent } from './components/clientes/clientes/clientes';
import { UsuarioComponent } from './components/usuarios/usuarios/usuarios';
import { PaqueteComponent } from './components/paquetes/paquete/paquete';
import { RolComponent } from './components/usuarios/rol/rol/rol';
import { MenuComponent } from './components/usuarios/menu/menu';
import { RolMenuComponent } from './components/usuarios/rol-menu/rol-menu';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'maestra-ubicaciones', component: MaestraUbicacionesComponent },
      { path: 'clientes', component: ClienteComponent },
      { path: 'usuarios', component: UsuarioComponent },
      { path: 'paquetes', component: PaqueteComponent },
      { path: 'rol', component: RolComponent },
      { path: 'menu', component: MenuComponent },
      { path: 'rolMenu', component: RolMenuComponent },
    ]
  },
  { path: '**', redirectTo: 'login' }
];
