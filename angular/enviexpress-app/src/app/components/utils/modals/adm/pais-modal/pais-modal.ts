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

// Terceros y Servicios
import { TranslateService, TranslateModule } from "@ngx-translate/core";
import { ApiResponse } from '../../../../../interface/ApiResponse';
import { AuditoriaAdm } from '../../../../../service/auditoria/auditoria-adm';
import { MaestraEstados } from '../../../../../service/utils/adm/maestraEstadosService/maestra-estados';
import { MaestraUbicacionesService } from '../../../../../service/utils/adm/maestraUbicacionesService/maestra-ubicaciones-service';
import { UtilsService } from '../../../../../service/utils/utils-service';

@Component({
  selector: 'app-pais-modal',
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
  templateUrl: './pais-modal.html',
  styleUrls: ['./pais-modal.css']
})
export class PaisModalComponent implements OnInit, AfterViewInit {
  httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
  
  createUpdatePais: boolean = false;
  togglePais: boolean = false;
  deletePais: boolean = false;
  buscarPais: boolean = false;
  
  createUpdatePaisForm: FormGroup;
  buscarPaisForm: FormGroup;
  togglePaisForm: FormGroup;
  deletePaisForm: FormGroup;
  
  seleccione: any = "";
  idPais: any = "";
  nmPais: any = "";
  sbPais: any = "";
  continente: any = "";
  idEstado: any = "";
  message: string = "";
  selectedRow: any = "";
  
  searchTermNmPais: string = '';
  searchTermSbPais: string = '';
  searchTermContienente: string = '';
  searchTermIdEstado: string = '';
  
  estadoOptions: any[] = [];
  paisesDataSource: any[] = [];
  columnas: string[] = ['Seleccione', 'Identificador', 'Nombre', 'Abreviatura', 'Continente', 'Estado'];
  
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  dataSource = new MatTableDataSource<any>();
  filter: any = { "idPais": "", "nmPais": "", "sbPais": "", "continente": "", "idEstado": "0001" };

  constructor(
    private http: HttpClient,
    public dialogRef: MatDialogRef<PaisModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
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
    this.createUpdatePaisForm = this.formBuilder.group({
      idPais: new FormControl(''),
      nmPais: new FormControl(''),
      sbPais: new FormControl(''),
      continente: new FormControl(''),
      idEstado: new FormControl('')
    });

    this.buscarPaisForm = this.formBuilder.group({ row: new FormControl('') });
    this.togglePaisForm = this.formBuilder.group({ message: new FormControl(''), idPais: new FormControl('') });
    this.deletePaisForm = this.formBuilder.group({ message: new FormControl(''), idPais: new FormControl('') });

    this.estadoOptions = [
      { value: '0001', label: 'Activo' }, 
      { value: '0002', label: 'Inactivo' }
    ];
  }

  ngOnInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    
    if (this.data.paisData != null) {
      this.createUpdatePais = true;
      const paisData = this.data.paisData;
      this.idEstado = paisData.idEstado ? (paisData.idEstado == "0001" ? "0001" : "0002") : "";

      this.createUpdatePaisForm.setValue({
        idPais: paisData.idPais || '',
        nmPais: paisData.nmPais || '',
        sbPais: paisData.sbPais || '',
        continente: paisData.continente || '',
        idEstado: this.idEstado
      });
    } else if (this.data.buscarPais != null) {
      this.gestionarPaises();
      this.buscarPais = true;
      this.maestraUbicacionesService.gestionarPaisesObservable.subscribe(() => { this.gestionarPaises(); });
    } else if (this.data.toggle != null) {
      this.togglePais = true;
      this.message = this.data.toggle.message ?? '¿Está seguro que desea inactivar/activar el país?';
      this.idPais = this.data.toggle.idPais;
      this.togglePaisForm.setValue({ message: this.message, idPais: this.idPais });
    } else if (this.data.delete != null) {
      this.deletePais = true;
      this.message = this.data.delete.message ?? '¿Está seguro que desea eliminar el país?';
      this.idPais = this.data.delete.idPais;
      this.deletePaisForm.setValue({ message: this.message, idPais: this.idPais });
    }
  }

  ngAfterViewInit(): void {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  submitCreateUpdatePais(): void {
    if (this.createUpdatePaisForm.valid) {
      const formValues = this.createUpdatePaisForm.value;
      
      const nuevaUbicacion: any = {
        idPais: formValues.idPais || '',
        nmPais: formValues.nmPais || '',
        sbPais: formValues.sbPais || '',
        continente: formValues.continente || '',
        idEstado: this.idEstado === '' ? (formValues.idEstado === 'Activo' ? '0001' : (formValues.idEstado === 'Inactivo' ? '0002' : "")) : this.idEstado
      };

      this.http.post<ApiResponse<Map<String, Object>>>(this.translate.instant('url.adm') + 'pais/create', nuevaUbicacion, this.httpOptions).subscribe(
        (responseData) => {
          this.auditoria.registroAuditoria(this.router.url, "Crear/Modificar País", nuevaUbicacion);
          if (responseData.statusCodeValue === HttpStatusCode.Created || responseData.statusCodeValue === HttpStatusCode.Ok) {
            this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.ubicaciones.ubicacion_creada'), "emerald-snackbar");
            this.dialogRef.close();
            this.maestraUbicacionesService.gestionarPaises();
          } else if (responseData.statusCodeValue === HttpStatusCode.NotFound || responseData.statusCodeValue === HttpStatusCode.ImATeapot) {
            this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.sin_datos'), "yellow-snackbar");
          } else if (responseData.statusCodeValue === HttpStatusCode.Found) {
            this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.existente'), "yellow-snackbar");
          } else if (responseData.statusCodeValue === HttpStatusCode.Conflict) {
            this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_crear_ubicacion'), "imperialRed-snackbar");
          }
        }, (error) => {
          // Manejo de errores simplificado para brevedad, sigue la misma lógica
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
        }
      );
    }
  }

  submitTogglePais(): void {
    this.http.post<ApiResponse<Response>>(this.translate.instant('url.adm') + 'pais/toggle/' + this.data.toggle.idPais, this.httpOptions).subscribe(
      (responseData) => {
        this.auditoria.registroAuditoria(this.router.url, "Activar/Inactivar País", { "idPais": this.data.toggle.idPais });
        if (responseData.statusCodeValue == HttpStatusCode.ImATeapot) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.estados.no_borrable'), "dodgerBlue-snackbar");
          this.dialogRef.close();
        } else if (responseData.statusCodeValue == HttpStatusCode.Ok) {
          this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.estados.estado_inactivo'), "emerald-snackbar");
          this.dialogRef.close();
          this.maestraUbicacionesService.gestionarPaises();
        }
      },
      (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.estados.error_eliminar_estado'), "imperialRed-snackbar");
      }
    );
  }

  submitDeletePais(): void {
    this.http.delete<ApiResponse<Response>>(this.translate.instant('url.adm') + 'pais/' + this.data.delete.idPais, this.httpOptions).subscribe(
      (responseData) => {
        this.auditoria.registroAuditoria(this.router.url, "Eliminar País", { "idPais": this.data.delete.idPais });
        if (responseData.statusCodeValue == HttpStatusCode.ImATeapot) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicacion.no_borrable'), "dodgerBlue-snackbar");
          this.dialogRef.close();
        } else if (responseData.statusCodeValue == HttpStatusCode.Ok) {
          this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.ubicaciones.ubicacion_eliminada'), "emerald-snackbar");
          this.dialogRef.close();
          this.maestraUbicacionesService.gestionarPaises();
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

  gestionarPaises() {
    this.columnas = ['Seleccione', 'Nombre', 'Estado'];
    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'pais/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          responseData.body.forEach((pais: any) => { pais.nmPais = pais.idPais + " - (" + pais.sbPais + ") " + pais.nmPais; });
          this.paisesDataSource = responseData.body;
          this.dataSource = new MatTableDataSource<any>(this.paisesDataSource);
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
    this.filter.nmPais = this.nmPais;
    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'pais/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          responseData.body.forEach((pais: any) => { pais.nmPais = pais.idPais + " - (" + pais.sbPais + ") " + pais.nmPais; });
          this.paisesDataSource = responseData.body;
          this.dataSource = new MatTableDataSource<any>(this.paisesDataSource);
          this.dataSource.paginator = this.paginator;
        }
      }, (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicaciones.error_en_filtro'), "imperialRed-snackbar");
      }
    );
  }

  submitBuscarPais() {
    if (this.buscarPaisForm.valid) {
      this.dialogRef.close(this.selectedRow);
    }
  }
}