import { Component, Inject, ViewChild, AfterViewInit, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { HttpClient, HttpHeaders, HttpStatusCode } from "@angular/common/http";
import { Router } from '@angular/router';

// Angular Material Standalone Imports
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBarModule, MatSnackBar } from "@angular/material/snack-bar";
import { MatPaginatorModule, MatPaginator } from "@angular/material/paginator";
import { MatSortModule, MatSort } from "@angular/material/sort";
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';


import { TranslateService, TranslateModule } from "@ngx-translate/core";
import { ApiResponse } from '../../../../../interface/ApiResponse';
import { PaisModalComponent } from '../pais-modal/pais-modal';
import { UtilsService } from '../../../../../service/utils/utils-service';
import { MaestraEstados } from '../../../../../service/utils/adm/maestraEstadosService/maestra-estados';
import { MaestraUbicacionesService } from '../../../../../service/utils/adm/maestraUbicacionesService/maestra-ubicaciones-service';
import { AuditoriaAdm } from '../../../../../service/auditoria/auditoria-adm';

@Component({
  selector: 'app-departamento-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatDialogModule,
    MatSnackBarModule,
    MatPaginatorModule,
    MatSortModule,
    MatRadioModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    TranslateModule
  ],
  templateUrl: './departamento-modal.html',
  styleUrls: ['./departamento-modal.css']
})
export class DepartamentoModalComponent implements OnInit, AfterViewInit {

  httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
  
  createUpdateDepto: boolean = false;
  toggleDepto: boolean = false;
  deleteDepto: boolean = false;
  buscarDepto: boolean = false;
  
  createUpdateDeptoForm: FormGroup;
  buscarDeptoForm: FormGroup;
  toggleDeptoForm: FormGroup;
  deleteDeptoForm: FormGroup;
  
  seleccione: any = "";
  idDepto: any = "";
  nmDepto: any = "";
  sbDepto: any = "";
  region: any = "";
  codigoPostal: any = "";
  codigoDane: any = "";
  idPais: any = "";
  idEstado: any = "";
  message: string = "";
  selectedRow: any = "";
  
  estadoOptions: any[] = [];
  deptosDataSource: any[] = []; // Reemplazado TvvsDepartamento por any[]
  columnas: string[] = ['Seleccione', 'Identificador', 'Ubicación', 'Estado'];
  
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  dataSource = new MatTableDataSource<any>();
  filter: any = {
    "idDepartamento": "", "nmDepartamento": "", "sbDepartamento": "", 
    "region": "", "codigoPostal": "", "codigoDane": "", "idPais": "", "idEstado": "0001"
  };

  constructor(
    private http: HttpClient,
    public dialogRef: MatDialogRef<DepartamentoModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, // Reemplazado TvvsDepartamento por any
    private formBuilder: FormBuilder,
    private utilsService: UtilsService,
    private dialog: MatDialog,
    private maestraEstadosService: MaestraEstados,
    private translate: TranslateService,
    private _snackBAr: MatSnackBar,
    private maestraUbicacionesService: MaestraUbicacionesService,
    private router: Router,
    private auditoria: AuditoriaAdm
  ) {
    this.createUpdateDeptoForm = this.formBuilder.group({
      idDepto: new FormControl(''),
      nmDepto: new FormControl(''),
      sbDepto: new FormControl(''),
      region: new FormControl(''),
      codigoPostal: new FormControl(''),
      codigoDane: new FormControl(''),
      idPais: new FormControl(''),
      idEstado: new FormControl('')
    });

    this.buscarDeptoForm = this.formBuilder.group({ row: new FormControl('') });
    this.toggleDeptoForm = this.formBuilder.group({ message: new FormControl(''), idDepto: new FormControl('') });
    this.deleteDeptoForm = this.formBuilder.group({ message: new FormControl(''), idDepto: new FormControl('') });

    this.estadoOptions = [
      { value: '0001', label: 'Activo' }, 
      { value: '0002', label: 'Inactivo' }
    ];
  }

  ngOnInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    
    if (this.data.deptoData != null) {
      this.createUpdateDepto = true;
      const deptoData = this.data.deptoData;
      this.idEstado = deptoData.idEstado ? (deptoData.idEstado == "0001" ? "0001" : "0002") : "";
      this.idPais = deptoData.idPais ? deptoData.idPais : "";

      this.createUpdateDeptoForm.setValue({
          idDepto: deptoData.idDepartamento || '',
          nmDepto: deptoData.nmDepartamento || '',
          sbDepto: deptoData.sbDepartamento || '',
          region: deptoData.region || '',
          codigoPostal: deptoData.codigoPostal || '',
          codigoDane: deptoData.codigoDane || '',
          idPais: deptoData.nmPais || '',
          idEstado: this.idEstado
      });

    } else if (this.data.buscarDepto != null) {
      this.gestionarDepartamentos();
      this.buscarDepto = true;
      this.maestraUbicacionesService.gestionarDepartamentosObservable.subscribe(() => { this.gestionarDepartamentos(); });
      
    } else if (this.data.toggle != null) {
      this.toggleDepto = true;
      this.message = this.data.toggle.message ?? '¿Está seguro que desea inactivar/activar el departamento?';
      this.idDepto = this.data.toggle.idDepto;
      this.toggleDeptoForm.setValue({ message: this.message, idDepto: this.idDepto });
      
    } else if (this.data.delete != null) {
      this.deleteDepto = true;
      this.message = this.data.delete.message ?? '¿Está seguro que desea eliminar el departamento?';
      this.idDepto = this.data.delete.idDepto;
      this.deleteDeptoForm.setValue({ message: this.message, idDepto: this.idDepto });
    }
  }

  ngAfterViewInit(): void {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  submitCreateUpdateDepto(): void {
    if (this.createUpdateDeptoForm.valid) {
      const formValues = this.createUpdateDeptoForm.value;
      
      // Creación del objeto genérico
      const nuevaUbicacion: any = {
        idDepartamento: formValues.idDepto || '',
        nmDepartamento: formValues.nmDepto || '',
        sbDepartamento: formValues.sbDepto || '',
        region: formValues.region || '',
        codigoDane: formValues.codigoDane || '',
        codigoPostal: formValues.codigoPostal || '',
        idPais: this.idPais || '',
        idEstado: this.idEstado === '' ? (formValues.idEstado === 'Activo' ? '0001' : (formValues.idEstado === 'Inactivo' ? '0002' : "")) : this.idEstado
      };

      this.http.post<ApiResponse<Map<String, Object>>>(this.translate.instant('url.adm') + 'departamento/create', nuevaUbicacion, this.httpOptions).subscribe(
        (responseData) => {
          this.auditoria.registroAuditoria(this.router.url, "Crear/Modificar Departamento", nuevaUbicacion);
          if (responseData.statusCodeValue === HttpStatusCode.Created || responseData.statusCodeValue === HttpStatusCode.Ok) {
            this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.ubicaciones.ubicacion_creada'), "emerald-snackbar");
            this.dialogRef.close();
            this.maestraUbicacionesService.gestionarDepartamentos();
          } else if (responseData.statusCodeValue === HttpStatusCode.NotFound || responseData.statusCodeValue === HttpStatusCode.ImATeapot) {
            this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
          } else if (responseData.statusCodeValue === HttpStatusCode.Found) {
            this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.existente'), "yellow-snackbar");
          } else if (responseData.statusCodeValue === HttpStatusCode.Conflict) {
            this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_crear_ubicacion'), "imperialRed-snackbar");
          }
        }, (error) => {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
        }
      );
    }
  }

  submitToggleDepto(): void {
    this.http.post<ApiResponse<Response>>(this.translate.instant('url.adm') + 'departamento/toggle/' + this.data.toggle.idDepto, this.httpOptions).subscribe(
      (responseData) => {
        this.auditoria.registroAuditoria(this.router.url, "Activar/Inactivar Departamento", { "idDepartamento": this.data.toggle.idDepto });
        if (responseData.statusCodeValue == HttpStatusCode.ImATeapot) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.estados.no_borrable'), "dodgerBlue-snackbar");
          this.dialogRef.close();
        } else if (responseData.statusCodeValue == HttpStatusCode.Ok) {
          this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.estados.estado_inactivo'), "emerald-snackbar");
          this.dialogRef.close();
          this.maestraUbicacionesService.gestionarDepartamentos();
        }
      },
      (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.estados.error_eliminar_estado'), "imperialRed-snackbar");
      }
    );
  }

  submitDeleteDepto(): void {
    this.http.delete<ApiResponse<Response>>(this.translate.instant('url.adm') + 'departamento/' + this.data.delete.idDepto, this.httpOptions).subscribe(
      (responseData) => {
        this.auditoria.registroAuditoria(this.router.url, "Eliminar Departamento", { "idDepartamento": this.data.delete.idDepto });
        if (responseData.statusCodeValue == HttpStatusCode.ImATeapot) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicacion.no_borrable'), "dodgerBlue-snackbar");
          this.dialogRef.close();
        } else if (responseData.statusCodeValue == HttpStatusCode.Ok) {
          this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.ubicaciones.ubicacion_eliminada'), "emerald-snackbar");
          this.dialogRef.close();
          this.maestraUbicacionesService.gestionarDepartamentos();
        }
      },
      (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_eliminar_ubicacion'), "imperialRed-snackbar");
      }
    );
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  gestionarDepartamentos() {
    this.columnas = ['Seleccione', 'Nombre', 'Estado'];
    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'departamento/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          responseData.body.forEach((depto: any) => {
            depto.nmDepartamento = depto.codigoDane + " - (" + depto.sbDepartamento + ") " + depto.nmDepartamento + " - " + depto.region;
          });
          this.deptosDataSource = responseData.body;
          this.dataSource = new MatTableDataSource<any>(this.deptosDataSource);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        } else {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  filtrarDatos() {
    this.filter.idEstado = this.idEstado;
    this.filter.nmDepartamento = this.nmDepto;
    this.filter.idDepartamento = this.idDepto;
    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'departamento/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          if (this.buscarDepto) {
            responseData.body.forEach((depto: any) => {
              depto.nmDepartamento = depto.codigoDane + " - (" + depto.sbDepartamento + ") " + depto.nmDepartamento + " - " + depto.region;
            });
          }
          this.deptosDataSource = responseData.body;
          this.dataSource = new MatTableDataSource<any>(this.deptosDataSource);
          this.dataSource.paginator = this.paginator;
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  submitBuscarDepto() {
    if (this.buscarDeptoForm.valid) {
      this.dialogRef.close(this.selectedRow);
    }
  }

  paisModal(): void {
    const dialogRef = this.dialog.open(PaisModalComponent, {
      width: '60%',
      data: { buscarPais: true }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.idPais = result.idPais;
        this.createUpdateDeptoForm.get('idPais')?.setValue(result.nmPais);
      }
    });
  }
}