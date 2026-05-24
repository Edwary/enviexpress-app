import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpStatusCode} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";
import { Router } from '@angular/router';

/** Interfaces */
import { ApiResponse } from '../../interface/ApiResponse';

/** Services */
import { UtilsService } from '../utils/utils-service'; 

@Injectable({
  providedIn: 'root',
})
export class AuditoriaAdm {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  message : string = "";
  datosUsuario : any = "";
  constructor(private http: HttpClient,
    private utilsService : UtilsService,
    private translate : TranslateService,
    private router : Router) { }

    registroAuditoria(vista: string, accion : string, contenido : any ){
      this.datosUsuario = localStorage.getItem("datosUsuario");
      let infoPersona =  JSON.parse(this.datosUsuario);
      let auditoria = {
        vista : vista,
        usuario : infoPersona.tvanUsuario.nmUsuario,
        nup : infoPersona.tvanPersona.nup,
        nus : infoPersona.tvanUsuario.nus,
        accion : accion,
        contenido : contenido
      }
      this.http.post<ApiResponse<Map<String, Object>>>(this.translate.instant('url.adm')+'auditoria/create', auditoria, this.httpOptions).subscribe(
        (responseData) => {
          
        }, (error) => {
          this.message = this.translate.instant('error_messages.adm.auditoria.error_registrando_movimiento');
        this.utilsService.openSnackBar(this.message, "imperialRed-snackbar");
        }
      );
    }
}
