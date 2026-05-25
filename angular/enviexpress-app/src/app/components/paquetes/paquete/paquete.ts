import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { PaqueteService } from '../../../service/paquete/paquete-service';
import { ClienteService } from '../../../service/clientes/cliente-service';
import { UbicacionService } from '../../../service/ubicacion/ubicacion-service';
import { EstadoService } from '../../../service/estado/estado-service';
import { UtilsService } from '../../../service/utils/utils-service';

import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-paquete',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './paquete.html',
  providers: [DatePipe]
})
export class PaqueteComponent implements OnInit {
  private paqueteService = inject(PaqueteService);
  private clienteService = inject(ClienteService);
  private ubicacionService = inject(UbicacionService);
  private estadoService = inject(EstadoService);
  private utilsService = inject(UtilsService);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  // Estados Generales
  paquetes: any[] = [];
  estadosPaquete: any[] = [];
  cargando: boolean = false;
  isAdmin: boolean = false;
  isOperador: boolean = false;
  usuarioNus: string = '';

  // Filtros principales
  filtros = { idCliente: '', idEstado: '', idCiudad: '', fechaInicio: '', fechaFin: '' };
  filtroClienteDisplay: string = '';
  filtroCiudadDisplay: string = '';

  // Control de Modales
  modales = {
    principal: false, toggle: false, roadmap: false, 
    selectorCliente: false, selectorCiudad: false, avanzarEstado: false
  };

  // Formularios y Objetos temporales
  paqueteForm: any = {};
  paqueteDetalle: any = null; // Para el Roadmap
  toggleForm: any = {};
  
  // Listas para selectores
  clientesSelector: any[] = [];
  ciudadesSelector: any[] = [];
  filtrosModalCliente = { documento: '', nombre: '', idEstado: '0001' };
  filtrosModalCiudad = { nmCiudad: '' };
  origenSelector: 'filtro' | 'formulario' = 'filtro';

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const idRol = localStorage.getItem('idRol');
      this.isAdmin = idRol === '0001';
      this.isOperador = idRol === '0002';
      this.usuarioNus = localStorage.getItem('nus') || ''; 
    }
    this.cargarEstados();
    this.buscarPaquetes();
    this.buscarClientesModal();
    this.buscarCiudadesModal();
  }

  cargarEstados() {
    this.estadoService.getEstadosPorModulo('PAQUETES').subscribe({
      next: (res: any) => {
        if (Array.isArray(res)) {
          this.estadosPaquete = res;
        } else if (res && Array.isArray(res.data)) {
          this.estadosPaquete = res.data;
        } else if (res && Array.isArray(res.body)) {
          this.estadosPaquete = res.body;
        } else {
          this.estadosPaquete = [];
        }
      },
      error: (err) => {
        console.error('Error cargando estados', err);
        this.estadosPaquete = [];
      }
    });
  }

  buscarPaquetes() {
    this.cargando = true;
    this.paqueteService.getPaquetes(this.filtros).subscribe({
      next: (res: any) => {
        // Blindaje: Buscamos el arreglo sin importar dónde lo esconda el backend
        if (Array.isArray(res)) {
          this.paquetes = res;
        } else if (res && Array.isArray(res.data)) {
          this.paquetes = res.data;
        } else if (res && Array.isArray(res.body)) {
          this.paquetes = res.body;
        } else {
          // Si no hay datos (ej. un 404 controlado), forzamos el arreglo vacío
          this.paquetes = []; 
        }
        
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => { 
        this.paquetes = []; 
        this.cargando = false; 
        this.cdr.detectChanges(); 
      }
    });
  }

  abrirSelectorCliente(origen: 'filtro' | 'formulario') {
    this.origenSelector = origen;
    this.modales.selectorCliente = true;
  }

  buscarClientesModal() {
    this.clienteService.getClientes(this.filtrosModalCliente).subscribe(res => {
      this.clientesSelector = res.body ? res.body : res;
    });
  }

  seleccionarCliente(cliente: any) {
    if (this.origenSelector === 'filtro') {
      this.filtros.idCliente = cliente.idCliente;
      this.filtroClienteDisplay = `${cliente.documento} - ${cliente.nmCliente || cliente.nombre}`;
    } else {
      this.paqueteForm.idCliente = cliente.idCliente;
      this.paqueteForm.clienteDisplay = `${cliente.documento} - ${cliente.nmCliente || cliente.nombre}`;
    }
    this.modales.selectorCliente = false;
  }

  abrirSelectorCiudad(origen: 'filtro' | 'formulario') {
    this.origenSelector = origen;
    this.modales.selectorCiudad = true;
  }

  buscarCiudadesModal() {
    this.ubicacionService.getCiudades(this.filtrosModalCiudad).subscribe(res => {
      this.ciudadesSelector = res.body ? res.body : res;
    });
  }

  seleccionarCiudad(ciudad: any) {
    if (this.origenSelector === 'filtro') {
      this.filtros.idCiudad = ciudad.idCiudad;
      this.filtroCiudadDisplay = `${ciudad.codigoPostal} - ${ciudad.nmCiudad}`;
    } else {
      this.paqueteForm.idCiudad = ciudad.idCiudad;
      this.paqueteForm.ciudadDisplay = `${ciudad.codigoPostal} - ${ciudad.nmCiudad}`;
      this.paqueteForm.idDepartamento = ciudad.idDepartamento;
      this.paqueteForm.nmDepartamento = ciudad.nmDepartamento;
    }
    this.modales.selectorCiudad = false;
  }

  abrirModal(paquete?: any) {
    if (paquete) {
      this.paqueteForm = { ...paquete };
      this.paqueteForm.clienteDisplay = 'Cliente Cargado'; // Ideal buscar el nombre real con el ID
      this.paqueteForm.ciudadDisplay = 'Ciudad Cargada';
    } else {
      this.paqueteForm = {
        idPaquete: '', idCliente: '', documentoDestinatario: '', nombreDestinatario: '', telefonoDestinatario: '', 
        direccion: '', idCiudad: '', idDepartamento: '', telefono: '', peso: '', valorDeclarado: '', idEstado: '0008' 
      };
    }
    this.modales.principal = true;
  }

  guardarPaquete() {
    if (!this.paqueteForm.documentoDestinatario) {
      this.utilsService.openSnackBar('El documento del destinatario no puede estar en blanco', 'error');
      return;
    }
    this.paqueteService.savePaquete(this.paqueteForm).subscribe({
      next: () => {
        this.modales.principal = false;
        this.buscarPaquetes();
        this.utilsService.openSnackBar('Paquete guardado correctamente', 'success');
      },
      error: (err) => this.utilsService.openSnackBar('Error al guardar paquete', 'error')
    });
  }

  eliminarPaquete(idPaquete: string) {
    if (confirm('¿Eliminar este paquete?')) {
      this.paqueteService.deletePaquete(idPaquete).subscribe(() => this.buscarPaquetes());
    }
  }

  abrirModalToggle(paquete: any) {
    this.toggleForm = { idPaquete: paquete.idPaquete, idEstado: paquete.idEstado, nus: this.usuarioNus };
    this.modales.toggle = true;
  }

  ejecutarToggle() {
    this.paqueteService.togglePaquete(this.toggleForm).subscribe({
      next: (res: any) => {
        const msg = res.data?.avanceEstado || 'Estado actualizado';
        this.utilsService.openSnackBar(msg, 'success');
        this.modales.toggle = false;
        this.modales.avanzarEstado = false;
        this.buscarPaquetes();
        if (this.modales.roadmap) this.cargarDetalleRoadmap(this.toggleForm.idPaquete); // Recargar roadmap si está abierto
      },
      error: (err: any) => {
        const msg = err.error?.message || 'Error en validación de estado';
        this.utilsService.openSnackBar(msg, 'error');
      }
    });
  }

  abrirRoadmap(idPaquete: string) {
    this.modales.roadmap = true;
    this.paqueteDetalle = [];
    this.cargarDetalleRoadmap(idPaquete);
  }

  cargarDetalleRoadmap(idPaquete: string) {
    this.paqueteService.getPaqueteById(idPaquete).subscribe({
      next: (res: any) => {
        
        if (res && res.body && res.body.estados) {
          this.paqueteDetalle = res.body;
        } else if (res && res.data && res.data.body) {
          this.paqueteDetalle = res.data.body;
        } else if (res && res.data) {
          this.paqueteDetalle = res.data;
        } else {
          this.paqueteDetalle = res;
        }

        this.procesarColoresRoadmap();
        
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error('Error cargando el detalle del paquete', err);
        this.paqueteDetalle = null;
        this.cdr.detectChanges();
      }
    });
  }

  procesarColoresRoadmap() {
    if (!this.paqueteDetalle || !this.paqueteDetalle.estados) return;
    const estados = this.paqueteDetalle.estados;
    const total = estados.length;

    estados.forEach((est: any, index: number) => {
      const isLast = index === total - 1;
      const isAnomalia = ['0013', '0014', '0015'].includes(est.idEstado);
      
      if (isLast) {
        est.renderColor = isAnomalia ? est.color : '#4392F1'; // Azul si es normal actual
      } else {
        const nextState = estados[index + 1];
        const nextIsAnomalia = ['0013', '0014', '0015'].includes(nextState.idEstado);
        
        if (nextIsAnomalia) {
          est.renderColor = '#F52F57'; // Rojo si el siguiente fue anomalía
        } else {
          est.renderColor = est.color; // Su color nativo de la BD
        }
      }
    });
  }

  operadorAccion(tipo: string) {
    this.toggleForm = { idPaquete: this.paqueteDetalle.idPaquete, nus: this.usuarioNus };
    
    if (tipo === 'avanzar') {
      this.toggleForm.idEstado = ''; 
      this.modales.avanzarEstado = true;
    } else {
      if (tipo === 'novedad') this.toggleForm.idEstado = '0013';
      if (tipo === 'cancelar') this.toggleForm.idEstado = '0015';
      if (tipo === 'devolver') this.toggleForm.idEstado = '0014';
      if (tipo === 'liquidacion') this.toggleForm.idEstado = '0012';
      this.ejecutarToggle();
    }
  }

  borrarFiltros() {
    this.filtros = { idCliente: '', idEstado: '', idCiudad: '', fechaInicio: '', fechaFin: '' };
    this.filtroClienteDisplay = '';
    this.filtroCiudadDisplay = '';
    this.buscarPaquetes();
  }
}