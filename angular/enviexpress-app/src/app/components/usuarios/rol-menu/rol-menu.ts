import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { forkJoin } from 'rxjs';
import { RolMenuService } from '../../../service/rolMenu/rol-menu-service';
import { MenuService } from '../../../service/menu/menu-service';
import { RolService } from '../../../service/rol/rol-service';

@Component({
  selector: 'app-rol-menu',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './rol-menu.html'
})
export class RolMenuComponent implements OnInit {
  private rolMenuService = inject(RolMenuService);
  private menuService = inject(MenuService);
  private rolService = inject(RolService);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  rolMenus: any[] = [];
  roles: any[] = [];
  menus: any[] = [];
  cargando: boolean = false;
  isAdmin: boolean = false;

  filtros = {
    idMenu: '',
    idRol: '',
    idEstado: ''
  };

  isModalOpen: boolean = false;
  isToggleOpen: boolean = false;
  rolMenuForm: any = {};

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const idRolLocal = localStorage.getItem('idRol');
      this.isAdmin = idRolLocal === '0001';
    }
    this.cargarDatosMaestros();
  }

  cargarDatosMaestros() {
    this.cargando = true;
    forkJoin({
      rolesData: this.rolService.findAll(),
      menusData: this.menuService.getMenus(this.filtros)
    }).subscribe({
      next: (res: any) => {
        this.roles = res.rolesData ? res.rolesData : res.rolesData;
        this.menus = res.menusData.body ? res.menusData.body : res.menusData;
        this.buscarRolMenus();
      },
      error: (err) => {
        console.error('Error cargando catálogos de configuración', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  buscarRolMenus() {
    this.cargando = true;
    this.rolMenuService.getRolMenus(this.filtros).subscribe({
      next: (res: any) => {
        if (Array.isArray(res)) this.rolMenus = res;
        else if (res && Array.isArray(res.data)) this.rolMenus = res.data;
        else if (res && Array.isArray(res.body)) this.rolMenus = res.body;
        else this.rolMenus = [];

        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error listando accesos', err);
        this.rolMenus = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(relacion?: any) {
    if (relacion) {
      this.rolMenuForm = { ...relacion };
    } else {
      this.rolMenuForm = {
        uuid: '',
        idRolMenu: '',
        idRol: '',
        idMenu: '',
        idEstado: '0001'
      };
    }
    this.isModalOpen = true;
  }

  cerrarModal() {
    this.isModalOpen = false;
    this.rolMenuForm = {};
  }

  guardarRolMenu() {
    this.rolMenuService.saveRolMenu(this.rolMenuForm).subscribe({
      next: () => {
        this.cerrarModal();
        this.buscarRolMenus();
        this.isModalOpen = false;
      },
      error: (err) => console.error('Error procesando la asignación', err)
    });
  }

  eliminarRolMenu(idRolMenu: string) {
    if (confirm('¿Desea remover este menú del rol asignado?')) {
      this.rolMenuService.deleteRolMenu(idRolMenu).subscribe({
        next: () => this.buscarRolMenus(),
        error: (err) => console.error('Error eliminando asignación', err)
      });
    }
  }

  abrirModalToggle(relacion: any) {
    this.rolMenuForm = { idRolMenu: relacion.idRolMenu, idEstado: relacion.idEstado };
    this.isToggleOpen = true;
  }

  cerrarModalToggle() {
    this.isToggleOpen = false;
    this.rolMenuForm = {};
  }

  confirmarCambioEstado() {
    this.rolMenuService.toggleRolMenu(this.rolMenuForm).subscribe({
      next: () => {
        this.cerrarModalToggle();
        this.buscarRolMenus();
        this.isToggleOpen = false;
      },
      error: (err) => console.error('Error modificando estado de asignación', err)
    });
  }

  getNombreRol(idRol: string): string {
    const target = this.roles.find(r => r.idRol === idRol);
    return target ? target.nombre || target.nmRol : idRol;
  }

  getNombreMenu(idMenu: string): string {
    const target = this.menus.find(m => m.idMenu === idMenu);
    return target ? target.name || target.nombre : idMenu;
  }
}