import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { UsuarioService } from '../../../service/usuarios/usuario-service';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './usuarios.html'
})
export class UsuarioComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  usuarios: any[] = [];
  roles: any[] = [];
  cargando: boolean = false;
  isAdmin: boolean = false;

  filtros = {
    nmUsuario: '',
    nombre: '',
    email: '',
    idRol: '',
    idEstado: ''
  };

  isModalOpen: boolean = false;
  isToggleOpen: boolean = false;
  usuarioForm: any = {};
  usuarioActivo: any = '';

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const idRolLocal = localStorage.getItem('idRol');
      this.usuarioActivo = localStorage.getItem('idUsuario');
      console.log('Usuario activo:', this.usuarioActivo);
      this.isAdmin = idRolLocal === '0001';
    }
    this.cargarRoles();
    this.buscarUsuarios();
  }

  cargarRoles() {
    this.usuarioService.getRoles().subscribe({
      next: (res: any) => {
        if (Array.isArray(res)) this.roles = res;
        else if (res && Array.isArray(res.data)) this.roles = res.data;
        else if (res && Array.isArray(res)) this.roles = res;
      },
      error: (err) => console.error('Error cargando roles', err)
    });
  }

  buscarUsuarios() {
    this.usuarios = [];
    this.cargando = true;
    this.usuarioService.getUsuarios(this.filtros).subscribe({
      next: (res: any) => {
        if (Array.isArray(res.body)) this.usuarios = res.body;
        else if (res && Array.isArray(res.data)) this.usuarios = res.data;
        else if (res && Array.isArray(res.body)) this.usuarios = res.body;
        else this.usuarios = [];

        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error buscando usuarios', err);
        this.usuarios = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(usuario?: any) {
    if (usuario) {
      this.usuarioForm = { ...usuario, password: '' };
    } else {
      this.usuarioForm = {
        idUsuario: '',
        nmUsuario: '',
        nombre: '',
        email: '',
        password: '',
        idRol: '',
        idEstado: '0001'
      };
    }
    this.isModalOpen = true;
  }

  cerrarModal() {
    this.isModalOpen = false;
    this.usuarioForm = {};
  }

  generarPassword() {
    const randomPass = Math.floor(10000 + Math.random() * 90000);
    this.usuarioForm.password = randomPass.toString();
  }

  guardarUsuario() {
    this.usuarioService.saveUsuario(this.usuarioForm).subscribe({
      next: () => {
        this.cerrarModal();
        this.buscarUsuarios();
        this.isModalOpen = false;
      },
      error: (err) => {
        console.error('Error guardando usuario', err);
        this.isModalOpen = false;
      }
    });
  }

  eliminarUsuario(idUsuario: string) {
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      this.usuarioService.deleteUsuario(idUsuario).subscribe({
        next: () => this.buscarUsuarios(),
        error: (err) => console.error('Error eliminando usuario', err)
      });
    }
  }

  abrirModalToggle(usuario: any) {
    this.usuarioForm = { ...usuario, password: '' };
    this.isToggleOpen = true;
  }

  cerrarModalToggle() {
    this.isToggleOpen = false;
    this.usuarioForm = {};
  }

  confirmarCambioEstado() {
    this.usuarioService.toggleUsuario(this.usuarioForm).subscribe({
      next: () => {
        this.cerrarModalToggle();
        this.buscarUsuarios();
        this.isToggleOpen = false;
      },
      error: (err) => {
        console.error('Error al cambiar el estado', err);
        this.isToggleOpen = false;
      }
    });
  }

  getNombreRol(idRol: string): string {
    const rol = this.roles.find(r => r.idRol === idRol);
    return rol ? rol.nombre : 'Sin asignar';
  }
}
