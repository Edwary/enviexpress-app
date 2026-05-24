import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MenuService } from '../../../service/menu/menu-service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './menu.html'
})
export class MenuComponent implements OnInit {
  private menuService = inject(MenuService);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  menus: any[] = [];
  cargando: boolean = false;
  isAdmin: boolean = false;

  filtros = {
    nombre: '',
    href: '',
    idEstado: ''
  };

  // Estados de Modales
  isModalOpen: boolean = false;
  isToggleOpen: boolean = false;
  menuForm: any = {};

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const idRolLocal = localStorage.getItem('idRol');
      this.isAdmin = idRolLocal === '0001';
    }
    this.buscarMenus();
  }

  buscarMenus() {
    this.menus = [];
    this.cargando = true;
    this.menuService.getMenus(this.filtros).subscribe({
      next: (res: any) => {
        if (Array.isArray(res)) this.menus = res;
        else if (res && Array.isArray(res.data)) this.menus = res.data;
        else if (res && Array.isArray(res.body)) this.menus = res.body;
        else this.menus = [];
        
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error buscando menús', err);
        this.menus = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  
  abrirModal(menu?: any) {
    if (menu) {
      this.menuForm = { ...menu };
    } else {
      this.menuForm = {
        uuid: '',
        idMenu: '', 
        nmMenu: '',
        idMenuSup: '',
        href: '',
        idEstado: '0001'
      };
    }
    this.isModalOpen = true;
  }

  cerrarModal() {
    this.isModalOpen = false;
    this.menuForm = {};
  }

  guardarMenu() {
    this.menuService.create(this.menuForm).subscribe({
      next: () => {
        this.cerrarModal();
        this.buscarMenus();
        this.isModalOpen = false;
      },
      error: (err) => {
        console.error('Error guardando menú', err);
        this.isModalOpen = false;
      }
    });
  }

  eliminarMenu(idMenu: string) {
    if (confirm('¿Estás seguro de que deseas eliminar este menú del sistema?')) {
      this.menuService.delete(idMenu).subscribe({
        next: () => this.buscarMenus(),
        error: (err) => console.error('Error eliminando menú', err)
      });
    }
  }

  
  abrirModalToggle(menu: any) {
    this.menuForm = { idMenu: menu.idMenu, idEstado: menu.idEstado };
    this.isToggleOpen = true;
  }

  cerrarModalToggle() {
    this.isToggleOpen = false;
    this.menuForm = {};
  }

  confirmarCambioEstado() {
    this.menuService.toggle(this.menuForm).subscribe({
      next: () => {
        this.cerrarModalToggle();
        this.buscarMenus();
        this.isToggleOpen = false;
      },
      error: (err) => {
        console.error('Error al cambiar el estado del menú', err);
        this.isToggleOpen = false;
      }
    });
  }
}