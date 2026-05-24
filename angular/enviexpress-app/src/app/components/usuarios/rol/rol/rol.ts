import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { RolService } from '../../../../service/rol/rol-service';

@Component({
  selector: 'app-roles',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './rol.html'
})
export class RolComponent implements OnInit {
  private rolService = inject(RolService);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  roles: any[] = [];
  cargando: boolean = false;
  isAdmin: boolean = false;

  filtros = {
    nombre: '',
    sbRol: '',
    idEstado: ''
  };

  // Estados de Modales
  isModalOpen: boolean = false;
  isToggleOpen: boolean = false;
  rolForm: any = {};

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const idRolLocal = localStorage.getItem('idRol');
      this.isAdmin = idRolLocal === '0001';
    }
    this.buscarRoles();
  }

  buscarRoles() {
    this.cargando = true;
    this.rolService.getRoles(this.filtros).subscribe({
      next: (res: any) => {
        if (Array.isArray(res)) this.roles = res;
        else if (res && Array.isArray(res.data)) this.roles = res.data;
        else if (res && Array.isArray(res.body)) this.roles = res.body;
        else this.roles = [];
        
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error buscando roles', err);
        this.roles = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(rol?: any) {
    if (rol) {
      this.rolForm = { ...rol };
    } else {
      this.rolForm = {
        uuid: '',
        idRol: '', 
        nombre: '',
        sbRol: '',
        idEstado: '0001'
      };
    }
    this.isModalOpen = true;
  }

  cerrarModal() {
    this.isModalOpen = false;
    this.rolForm = {};
  }

  guardarRol() {
    this.rolService.saveRol(this.rolForm).subscribe({
      next: () => {
        this.cerrarModal();
        this.buscarRoles();
        this.isModalOpen = false;
      },
      error: (err) => {
        console.error('Error guardando rol', err);
        this.isModalOpen = false;
      }
    });
  }

  eliminarRol(idRol: string) {
    if (confirm('¿Estás seguro de que deseas eliminar este rol?')) {
      this.rolService.deleteRol(idRol).subscribe({
        next: () => this.buscarRoles(),
        error: (err) => console.error('Error eliminando rol', err)
      });
    }
  }

  abrirModalToggle(rol: any) {
    this.rolForm = { idRol: rol.idRol, idEstado: rol.idEstado };
    this.isToggleOpen = true;
  }

  cerrarModalToggle() {
    this.isToggleOpen = false;
    this.rolForm = {};
  }

  confirmarCambioEstado() {
    this.rolService.toggleRol(this.rolForm).subscribe({
      next: () => {
        this.cerrarModalToggle();
        this.buscarRoles();
        this.isToggleOpen = false;
      },
      error: (err) => {
        console.error('Error al cambiar el estado', err);
        this.isToggleOpen = false;
      }
    });
  }
}