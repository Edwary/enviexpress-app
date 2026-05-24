import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ClienteService } from '../../../service/clientes/cliente-service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './clientes.html'
})
export class ClienteComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  clientes: any[] = [];
  cargando: boolean = false;
  isAdmin: boolean = false; // Controla si se muestran los botones y acciones

  // Objeto para los filtros de búsqueda
  filtros = {
    nmCliente: '',
    documento: '',
    email: '',
    telefono: '',
    idEstado: ''
  };

  // Variables para la Modal
  isModalOpen: boolean = false;
  clienteForm: any = {};

  ngOnInit() {
    // Validamos el rol sin romper el SSR de Angular
    if (isPlatformBrowser(this.platformId)) {
      const idRol = localStorage.getItem('idRol');
      this.isAdmin = idRol === '0001';
    }
    this.buscarClientes();
  }

  buscarClientes() {
    this.cargando = true;
    this.clienteService.getClientes(this.filtros).subscribe({
      next: (res) => {
        console.log('Clientes obtenidos:', res.body);
        this.clientes = res.body; 
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error buscando clientes', err);
        this.clientes = [];
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(cliente?: any) {
    if (cliente) {
      // Editar: Clonamos el objeto para no afectar la fila hasta que se guarde
      this.clienteForm = { ...cliente };
    } else {
      // Crear: Formulario vacío
      this.clienteForm = {
        idCliente: '', 
        nmCliente: '',
        documento: '',
        email: '',
        telefono: '',
        idEstado: '0001'
      };
    }
    this.isModalOpen = true;
  }

  cerrarModal() {
    this.isModalOpen = false;
    this.clienteForm = {};
  }

  guardarCliente() {
    this.clienteService.saveCliente(this.clienteForm).subscribe({
      next: () => {
        this.cerrarModal();
        this.buscarClientes(); // Recargamos la tabla
      },
      error: (err) => console.error('Error guardando cliente', err)
    });
  }

  eliminarCliente(idCliente: string) {
    if (confirm('¿Estás seguro de que deseas eliminar este cliente?')) {
      this.clienteService.deleteCliente(idCliente).subscribe({
        next: () => this.buscarClientes(),
        error: (err) => console.error('Error eliminando cliente', err)
      });
    }
  }

  cambiarEstado(cliente: any) {
    this.clienteService.toggleCliente(cliente).subscribe({
      next: () => this.buscarClientes(),
      error: (err) => console.error('Error al cambiar el estado', err)
    });
  }
}