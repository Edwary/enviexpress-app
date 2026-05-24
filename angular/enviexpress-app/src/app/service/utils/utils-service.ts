import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpStatusCode } from "@angular/common/http";
import { MatSnackBar } from '@angular/material/snack-bar';
import { DatePipe } from '@angular/common';
import { map, catchError } from 'rxjs/operators';
import { TranslateService } from "@ngx-translate/core";

@Injectable({
  providedIn: 'root',
})
export class UtilsService {

  private url_adm = 'http://localhost:8080/';
  private url_usuarios = 'http://localhost:8081/';
  private url_clientes = 'http://localhost:8082/';
  private url_paquetes = 'http://localhost:8083/';

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  datosUsuario: any = "";

  constructor(
    private http: HttpClient,
    private _snackBar: MatSnackBar,
    private datePipe: DatePipe,
    private translate: TranslateService,
  ) {}

  openSnackBar(message: string, type: string) {
    this._snackBar.open(message, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [type, 'custom-snackbar'],
    });
  }

  openSnackBarSeconds(message: string, type: string, durationInSecons: number) {
    this._snackBar.open(message, 'Cerrar', {
      duration: durationInSecons * 1000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [type, 'custom-snackbar'],
    });
  }

  getColorClassCircle(idEstado: string) {
    return {
      'display': 'inline-block',
      'width': '20px',
      'height': '20px',
      'border-radius': '50%',
      'background-color': this.translate.instant('titulos.adm.estados.' + idEstado + ".colorEstado"),
      'text-align': 'center',
      'vertical-align': 'center'
    }
  }

  getCircleStyle(colorEstado: string) {
    return {
      'display': 'inline-block',
      'width': '20px',
      'height': '20px',
      'border-radius': '50%',
      'background-color': colorEstado,
      'text-align': 'center',
      'vertical-align': 'center'
    }
  }

  formatDate(date: any): string | null {
    if (date) {
      return this.datePipe.transform(date, 'dd-MM-yyyy');
    }
    return '';
  }

  parseDate(dateString: string) {
    if (dateString) {
      const parts = dateString.split('-');
      if (parts.length === 3) {
        const year = parseInt(parts[2], 10);
        const month = parseInt(parts[1], 10) - 1;
        const day = parseInt(parts[0], 10);

        if (!isNaN(year) && !isNaN(month) && !isNaN(day)) {
          return new Date(year, month, day);
        }
      }
    }
    return null;
  }
}
