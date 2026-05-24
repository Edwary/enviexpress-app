import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit, Renderer2, ElementRef, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpStatusCode } from "@angular/common/http";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";

// Angular Material Imports (Necesarios en Standalone)
import { MatTableModule, MatTableDataSource } from "@angular/material/table";
import { MatPaginatorModule, MatPaginator } from "@angular/material/paginator";
import { MatSortModule, MatSort } from "@angular/material/sort";
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from "@angular/material/dialog";

// Terceros y Servicios (Ajusta las rutas según tu proyecto)
import { TranslateService, TranslateModule } from "@ngx-translate/core";
import { Icons } from "../../../../assets/Icons";
import { ApiResponse } from "../../../interface/ApiResponse";
import { Sidenav } from "../../../service/utils/sidenav/sidenav";
import { MaestraUbicacionesService } from '../../../service/utils/adm/maestraUbicacionesService/maestra-ubicaciones-service';
import { UtilsService } from "../../../service/utils/utils-service"

// Modales (Ajusta las rutas según tu proyecto)
import { PaisModalComponent } from "../../utils/modals/adm/pais-modal/pais-modal";
import { DepartamentoModalComponent } from '../../utils/modals/adm/departamento-modal/departamento-modal';
import { CiudadModalComponent } from '../../utils/modals/adm/ciudad-modal/ciudad-modal';
import { DireccionModalComponent } from '../../utils/modals/adm/direccion-modal/direccion-modal';

@Component({
  selector: 'app-maestra-ubicaciones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonToggleModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    TranslateModule
  ],
  templateUrl: './maestra-ubicaciones.html',
  styleUrls: ['./maestra-ubicaciones.css'],
  providers: [DatePipe]
})
export class MaestraUbicacionesComponent implements OnInit, OnDestroy, AfterViewInit {

  url: string = '';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  gestionarPaisesSeccion: boolean = false;
  gestionarDepartamentosSeccion: boolean = false;
  gestionarCiudadesSeccion: boolean = false;
  gestionarBarriosSeccion: boolean = false;
  
  currentPage: number = 1;
  
  // Reemplazados los modelos por arrays de any
  paisesDataSource: any[] = [];
  departamentosDataSource: any[] = [];
  ciudadesDataSource: any[] = [];
  barriosDataSource: any[] = [];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  dataSource = new MatTableDataSource<any>();
  estadoOptions: any[] = [];
  columnas: any[] = [];
  
  idPais: string = "";
  nmPais: string = "";
  sbPais: string = "";
  continente: string = "";
  idDepartamento: string = "";
  nmDepartamento: string = "";
  sbDepartamento: string = "";
  nmCiudad: string = "";
  idCiudad: string = "";
  sbCiudad: string = "";
  subRegion: string = "";
  idBarrio: string = "";
  nmBarrio: string = "";
  tipo: string = "";
  codigoDane: string = "";
  codigoPostal: string = "";
  idEstado: string = "";
  direccionTexto: string = "";
  
  filter: any = {
    "idPais": "", "nmPais": "", "sbPais": "", "continente": "", "idDepartamento": "", "nmDepartamento": "",
    "sbDepartamento": "", "nmCiudad": "", "idCiudad": "", "sbCiudad": "", "subRegion": "", "idBarrio": "", "nmBarrio": "",
    "tipo": "", "codigoDane": "", "codigoPostal": "", "idEstado": "", "direccionBarrio": "",
  };
  
  message: string = "";
  private subscription!: Subscription;
  isMenuExpanded: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private translate: TranslateService,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private renderer: Renderer2,
    private el: ElementRef,
    private sidenavService: Sidenav,
    private utilsService: UtilsService,
    private maestraUbicacionesService: MaestraUbicacionesService
  ) {
    translate.setDefaultLang('./messages/messages.es');
    this.url = this.translate.instant('url.adm');
    
    this.subscription = this.sidenavService.toggleMeny$.subscribe((isExpanded) => {
      this.isMenuExpanded = isExpanded;
    });

    this.estadoOptions = [
      { label: "Activo", value: "0001" },
      { label: "Inactivo", value: "0002" },
    ];
  }

  ngOnInit(): void {
    this.maestraUbicacionesService.gestionarPaisesObservable.subscribe(() => { this.gestionarPaises(); });
    this.maestraUbicacionesService.gestionarDepartamentosObservable.subscribe(() => { this.gestionarDepartamentos(); });
    this.maestraUbicacionesService.gestionarCiudadesObservable.subscribe(() => { this.gestionarCiudades(); });
    
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  ngOnDestroy(): void {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  ngAfterViewInit(): void {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  getIconPath(iconName: string): string {
    return Icons.ICONS[iconName];
  }

  formatDate(date: any): string | null {
    if (date) {
      return this.datePipe.transform(date, 'dd-MM-yyyy');
    }
    return '';
  }

  gestionarPaises() {
    this.gestionarPaisesSeccion = true;
    this.gestionarDepartamentosSeccion = false;
    this.gestionarCiudadesSeccion = false;
    this.gestionarBarriosSeccion = false;
    this.columnas = ['Identificador', 'Nombre', 'Abreviatura', 'Continente', 'Estado', 'Acciones'];

    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'pais/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          this.paisesDataSource = responseData.body || [];
          this.dataSource = new MatTableDataSource<any>(this.paisesDataSource);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        } else if (responseData.statusCodeValue === HttpStatusCode.NotFound) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  gestionarDepartamentos() {
    this.gestionarPaisesSeccion = false;
    this.gestionarDepartamentosSeccion = true;
    this.gestionarCiudadesSeccion = false;
    this.gestionarBarriosSeccion = false;
    this.columnas = ['Identificador', 'Nombre Depto', 'Abreviatura', 'Region', 'Código Dane', 'Código Postal', 'Nombre País', 'Estado', 'Acciones'];

    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'departamento/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          this.departamentosDataSource = responseData.body || [];
          this.dataSource = new MatTableDataSource<any>(this.departamentosDataSource);
          this.dataSource.paginator = this.paginator;
        } else if (responseData.statusCodeValue === HttpStatusCode.NotFound) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  gestionarCiudades() {
    this.gestionarPaisesSeccion = false;
    this.gestionarDepartamentosSeccion = false;
    this.gestionarCiudadesSeccion = true;
    this.gestionarBarriosSeccion = false;
    this.columnas = ['Identificador', 'Nombre Ciudad', 'Abreviatura', 'Sub-Region', 'Código Dane', 'Código Postal', 'Tipo', 'Nombre Departamento', 'Nombre País', 'Estado', 'Acciones'];

    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'ciudad/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          this.ciudadesDataSource = responseData.body || [];
          this.dataSource = new MatTableDataSource<any>(this.ciudadesDataSource);
          this.dataSource.paginator = this.paginator;
        } else if (responseData.statusCodeValue === HttpStatusCode.NotFound) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  gestionarBarrios() {
    this.gestionarPaisesSeccion = false;
    this.gestionarDepartamentosSeccion = false;
    this.gestionarCiudadesSeccion = false;
    this.gestionarBarriosSeccion = true;
    this.columnas = ['Identificador', 'Nombre Barrio', 'Dirección', 'Código Postal', 'Nombre Ciudad', 'Nombre Departamento', 'Nombre País', 'Estado', 'Acciones'];

    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'barrio/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          this.barriosDataSource = responseData.body || [];
          this.dataSource = new MatTableDataSource<any>(this.barriosDataSource);
          this.dataSource.paginator = this.paginator;
        } else if (responseData.statusCodeValue === HttpStatusCode.NotFound) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  filtrarDatos() {
    this.filter.idEstado = this.idEstado;
    this.filter.codigoDane = this.codigoDane;
    this.filter.codigoPostal = this.codigoPostal;
    
    if (this.gestionarPaisesSeccion) {
      this.filter.nmPais = this.nmPais;
      this.filter.continente = this.continente;
      this.gestionarPaises(); // Reutilizamos el método para no duplicar código
    }
    if (this.gestionarDepartamentosSeccion) {
      this.filter.idPais = this.idPais;
      this.filter.nmDepartamento = this.nmDepartamento;
      this.gestionarDepartamentos();
    }
    if (this.gestionarCiudadesSeccion) {
      this.filter.idPais = this.idPais;
      this.filter.idDepartamento = this.idDepartamento;
      this.filter.nmCiudad = this.nmCiudad;
      this.filter.subRegion = this.subRegion;
      this.filter.tipo = this.tipo;
      this.gestionarCiudades();
    }
    if (this.gestionarBarriosSeccion) {
      this.filter.idPais = this.idPais;
      this.filter.idDepartamento = this.idDepartamento;
      this.filter.idCiudad = this.idCiudad;
      this.filter.nmBarrio = this.nmBarrio;
      this.gestionarBarrios();
    }
  }

  createUpdateUbicacionModal(rowIndex: number): void {
    let ubicacionEdit: any = null;
    rowIndex = rowIndex === -1 ? -1 : rowIndex + this.paginator.pageIndex * this.paginator.pageSize;
    
    if (this.gestionarPaisesSeccion) {
      ubicacionEdit = (rowIndex > -1 ? this.paisesDataSource[rowIndex] : {});
      this.dialog.open(PaisModalComponent, { width: '50%', data: { paisData: ubicacionEdit } });
    } else if (this.gestionarDepartamentosSeccion) {
      ubicacionEdit = (rowIndex > -1 ? this.departamentosDataSource[rowIndex] : {});
      this.dialog.open(DepartamentoModalComponent, { width: '50%', data: { deptoData: ubicacionEdit } });
    } else if (this.gestionarCiudadesSeccion) {
      ubicacionEdit = (rowIndex > -1 ? this.ciudadesDataSource[rowIndex] : {});
      this.dialog.open(CiudadModalComponent, { width: '50%', data: { ciudadData: ubicacionEdit } });
    } else {
      this.utilsService.openSnackBar(this.translate.instant('info_messages.adm.ubicaciones.creacion_no_encontrada'), 'yellow-snackbar');
    }
  }

  inactivarEliminarUbcacion(rowIndex: number, tipo: string): void {
    let ubicacionEdit: any = null;
    rowIndex = rowIndex + this.paginator.pageIndex * this.paginator.pageSize;

    if (tipo === "I") {
      const message = this.translate.instant('info_messages.adm.ubicaciones.inactivar_ubicacion');
      if (this.gestionarPaisesSeccion) {
        ubicacionEdit = (rowIndex > -1 ? this.paisesDataSource[rowIndex] : {});
        this.dialog.open(PaisModalComponent, { width: '25%', data: { toggle: { idPais: ubicacionEdit.idPais, message: message } } });
      } else if (this.gestionarDepartamentosSeccion) {
        ubicacionEdit = (rowIndex > -1 ? this.departamentosDataSource[rowIndex] : {});
        this.dialog.open(DepartamentoModalComponent, { width: '25%', data: { toggle: { idDepto: ubicacionEdit.idDepartamento, message: message } } });
      } else if (this.gestionarCiudadesSeccion) {
        ubicacionEdit = (rowIndex > -1 ? this.ciudadesDataSource[rowIndex] : {});
        this.dialog.open(CiudadModalComponent, { width: '25%', data: { toggle: { idCiudad: ubicacionEdit.idCiudad, message: message } } });
      } 
    }
    if (tipo === "E") {
      const message = this.translate.instant('info_messages.adm.ubicaciones.eliminar_ubicacion');
      if (this.gestionarPaisesSeccion) {
        ubicacionEdit = (rowIndex > -1 ? this.paisesDataSource[rowIndex] : {});
        this.dialog.open(PaisModalComponent, { width: '25%', data: { delete: { idPais: ubicacionEdit.idPais, message: message } } });
      } else if (this.gestionarDepartamentosSeccion) {
        ubicacionEdit = (rowIndex > -1 ? this.departamentosDataSource[rowIndex] : {});
        this.dialog.open(DepartamentoModalComponent, { width: '25%', data: { delete: { idDepto: ubicacionEdit.idDepartamento, message: message } } });
      } else if (this.gestionarCiudadesSeccion) {
        ubicacionEdit = (rowIndex > -1 ? this.ciudadesDataSource[rowIndex] : {});
        this.dialog.open(CiudadModalComponent, { width: '25%', data: { delete: { idCiudad: ubicacionEdit.idCiudad, message: message } } });
      }
    }
  }

  buscarPais() { this.dialog.open(PaisModalComponent, { width: '60%', data: { buscarPais: true } }); }
  buscarDepto() { this.dialog.open(DepartamentoModalComponent, { width: '60%', data: { buscarDepto: true } }); }
  buscarCiudad() { this.dialog.open(CiudadModalComponent, { width: '60%', data: { buscarCiudad: true } }); }
  direccionModal(): void { this.dialog.open(DireccionModalComponent, { width: '70%', data: { direccionData: {} } }); }
}