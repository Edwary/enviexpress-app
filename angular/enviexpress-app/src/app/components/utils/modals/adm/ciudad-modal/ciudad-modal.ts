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

// Terceros, Servicios y Modales
import { TranslateService, TranslateModule } from "@ngx-translate/core";
import { ApiResponse } from '../../../../../interface/ApiResponse';
import { PaisModalComponent } from '../pais-modal/pais-modal';
import { DepartamentoModalComponent } from '../departamento-modal/departamento-modal';
import { UtilsService } from '../../../../../service/utils/utils-service';
import { MaestraEstados } from '../../../../adm/maestra-estados/maestra-estados';
import { MaestraUbicacionesService } from '../../../../../service/utils/adm/maestraUbicacionesService/maestra-ubicaciones-service';
import { AuditoriaAdm } from '../../../../../service/auditoria/auditoria-adm';

@Component({
  selector: 'app-ciudad-modal',
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
  templateUrl: './ciudad-modal.html',
  styleUrls: ['./ciudad-modal.css']
})
export class CiudadModalComponent implements OnInit, AfterViewInit {

  httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
  
  createUpdateCiudad: boolean = false;
  toggleCiudad: boolean = false;
  deleteCiudad: boolean = false;
  buscarCiudad: boolean = false;
  
  createUpdateCiudadForm: FormGroup;
  buscarCiudadForm: FormGroup;
  toggleCiudadForm: FormGroup;
  deleteCiudadForm: FormGroup;
  
  seleccione: any = "";
  idCiudad: any = "";
  nmCiudad: any = "";
  sbCiudad: any = "";
  codigoPostal: any = "";
  codigoDane: any = "";
  subRegion: any = "";
  tipo: any = "";
  idDepartamento: any = "";
  idPais: any = "";
  idEstado: any = "";
  message: string = "";
  selectedRow: any = "";
  
  estadoOptions: any[] = [];
  ciudadesDataSource: any[] = []; // Reemplazado TvvtCiudad
  columnas: any[] = [];
  displayedColumnas: string[] = [];
  
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  dataSource = new MatTableDataSource<any>();
  filter: any = {
    "idCiudad": "", "nmCiudad": "", "sbCiudad": "", "subRegion": "",
    "tipo": "", "codigoPostal": "", "codigoDane": "", "idDepartamento": "", "idEstado": "0001"
  };

  constructor(
    private http: HttpClient,
    public dialogRef: MatDialogRef<CiudadModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, // Reemplazado TvvtCiudad
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
    this.createUpdateCiudadForm = this.formBuilder.group({
      idCiudad: new FormControl(''),
      nmCiudad: new FormControl(''),
      sbCiudad: new FormControl(''),
      tipo: new FormControl(''),
      subRegion: new FormControl(''),
      codigoPostal: new FormControl(''),
      codigoDane: new FormControl(''),
      idDepartamento: new FormControl(''),
      idEstado: new FormControl('')
    });

    this.buscarCiudadForm = this.formBuilder.group({ row: new FormControl('') });
    this.toggleCiudadForm = this.formBuilder.group({ message: new FormControl(''), idCiudad: new FormControl('') });
    this.deleteCiudadForm = this.formBuilder.group({ message: new FormControl(''), idCiudad: new FormControl('') });

    this.estadoOptions = [
      { value: '0001', label: 'Activo' }, 
      { value: '0002', label: 'Inactivo' }
    ];
  }

  ngOnInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    
    if (this.data.ciudadData != null) {
      this.createUpdateCiudad = true;
      const ciudadData = this.data.ciudadData;
      this.idEstado = ciudadData.idEstado ? (ciudadData.idEstado == "0001" ? "0001" : "0002") : "";
      this.idDepartamento = ciudadData.codigoDane ? ciudadData.codigoDane : "";

      this.createUpdateCiudadForm.setValue({
          idCiudad: ciudadData.idCiudad || '',
          nmCiudad: ciudadData.nmCiudad || '',
          sbCiudad: ciudadData.sbCiudad || '',
          tipo: ciudadData.tipo || '',
          subRegion: ciudadData.subRegion || '',
          codigoPostal: ciudadData.codigoPostal || '',
          codigoDane: ciudadData.codigoDane || '',
          idDepartamento: ciudadData.nmDepartamento || '',
          idEstado: this.idEstado
      });

    } else if (this.data.buscarCiudad != null) {
      this.gestionarCiudades();
      this.buscarCiudad = true;
      this.maestraUbicacionesService.gestionarCiudadesObservable.subscribe(() => { this.gestionarCiudades(); });
      
    } else if (this.data.toggle != null) {
      this.toggleCiudad = true;
      this.message = this.data.toggle.message ?? '¿Está seguro que desea inactivar/activar la ciudad?';
      this.idCiudad = this.data.toggle.idCiudad;
      this.toggleCiudadForm.setValue({ message: this.message, idCiudad: this.idCiudad });
      
    } else if (this.data.delete != null) {
      this.deleteCiudad = true;
      this.message = this.data.delete.message ?? '¿Está seguro que desea eliminar la ciudad?';
      this.idCiudad = this.data.delete.idCiudad;
      this.deleteCiudadForm.setValue({ message: this.message, idCiudad: this.idCiudad });
    }
  }

  ngAfterViewInit(): void {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  submitCreateUpdateCiudad(): void {
    if (this.createUpdateCiudadForm.valid) {
      const formValues = this.createUpdateCiudadForm.value;
      
      const nuevaUbicacion: any = {
        idCiudad: formValues.idCiudad || '',
        nmCiudad: formValues.nmCiudad || '',
        sbCiudad: formValues.sbCiudad || '',
        codigoPostal: formValues.codigoPostal || '',
        codigoDane: formValues.codigoDane || '',
        subRegion: formValues.subRegion || '',
        tipo: formValues.tipo || '',
        idDepartamento: this.idDepartamento || '',
        idEstado: this.idEstado === '' ? (formValues.idEstado === 'Activo' ? '0001' : (formValues.idEstado === 'Inactivo' ? '0002' : "")) : this.idEstado
      };

      this.http.post<ApiResponse<Map<String, Object>>>(this.translate.instant('url.adm') + 'ciudad/create', nuevaUbicacion, this.httpOptions).subscribe(
        (responseData) => {
          this.auditoria.registroAuditoria(this.router.url, "Crear/Modificar Ciudad", nuevaUbicacion);
          if (responseData.statusCodeValue === HttpStatusCode.Created || responseData.statusCodeValue === HttpStatusCode.Ok) {
            this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.ubicaciones.ubicacion_creada'), "emerald-snackbar");
            this.dialogRef.close();
            this.maestraUbicacionesService.gestionarCiudades();
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

  submitToggleCiudad(): void {
    this.http.post<ApiResponse<Response>>(this.translate.instant('url.adm') + 'ciudad/toggle/' + this.data.toggle.idCiudad, this.httpOptions).subscribe(
      (responseData) => {
        this.auditoria.registroAuditoria(this.router.url, "Activar/Inactivar Ciudad", { "idCiudad": this.data.toggle.idCiudad });
        if (responseData.statusCodeValue == HttpStatusCode.ImATeapot) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.estados.no_borrable'), "dodgerBlue-snackbar");
          this.dialogRef.close();
        } else if (responseData.statusCodeValue == HttpStatusCode.Ok) {
          this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.estados.estado_inactivo'), "emerald-snackbar");
          this.dialogRef.close();
          this.maestraUbicacionesService.gestionarCiudades();
        }
      },
      (error) => {
        this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.estados.error_eliminar_estado'), "imperialRed-snackbar");
      }
    );
  }

  submitDeleteCiudad(): void {
    this.http.delete<ApiResponse<Response>>(this.translate.instant('url.adm') + 'ciudad/' + this.data.delete.idCiudad, this.httpOptions).subscribe(
      (responseData) => {
        this.auditoria.registroAuditoria(this.router.url, "Eliminar Ciudad", { "idCiudad": this.data.delete.idCiudad });
        if (responseData.statusCodeValue == HttpStatusCode.ImATeapot) {
          this.utilsService.openSnackBar(this.translate.instant('error_messages.adm.ubicacion.no_borrable'), "dodgerBlue-snackbar");
          this.dialogRef.close();
        } else if (responseData.statusCodeValue == HttpStatusCode.Ok) {
          this.utilsService.openSnackBar(this.translate.instant('success_messages.adm.ubicaciones.ubicacion_eliminada'), "emerald-snackbar");
          this.dialogRef.close();
          this.maestraUbicacionesService.gestionarCiudades();
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

  gestionarCiudades() {
    this.columnas = [
      { columna: this.translate.instant('titulos.adm.seleccione'), campo: 'seleccione', tipo: 'seleccione' },
      { columna: this.translate.instant('titulos.adm.ubicaciones.ciudad'), campo: 'nmCiudad', tipo: 'input' }
    ];

    this.displayedColumnas = this.columnas.map((columna) => columna.columna);

    this.http.post<ApiResponse<any[]>[]>(this.translate.instant('url.adm') + 'ciudad/contains', this.filter, this.httpOptions).subscribe(
      (response) => {
        const responseData = response[0];
        if (responseData.statusCodeValue === HttpStatusCode.Ok) {
          responseData.body.forEach((ciudad: any) => {
            ciudad.nmCiudad = ciudad.codigoPostal + " - (" + ciudad.sbCiudad + ") " + ciudad.nmCiudad;
          });
          this.ciudadesDataSource = responseData.body;
          this.dataSource = new MatTableDataSource<any>(this.ciudadesDataSource);
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

  submitBuscarCiudad() {
    if (this.buscarCiudadForm.valid) {
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
        this.createUpdateCiudadForm.get('idPais')?.setValue(result.nmPais);
      }
    });
  }

  deptoModal(): void {
    const dialogRef = this.dialog.open(DepartamentoModalComponent, {
      width: '60%',
      data: { buscarDepto: true }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.idDepartamento = result.codigoDane;
        this.createUpdateCiudadForm.get('idDepartamento')?.setValue(result.nmDepartamento);
      }
    });
  }

  filtrarDatos(event: any, column: string) {
    const value = event.target.value.trim().toLowerCase();
    if (value) {
      this.dataSource.filterPredicate = (data: any, filter: string) => {
        return data[column].toLowerCase().includes(filter);
      }
      this.dataSource.filter = value;
    } else {
      this.dataSource.filter = '';
    }
  }
}