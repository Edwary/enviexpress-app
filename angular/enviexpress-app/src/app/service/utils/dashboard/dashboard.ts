import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8083/paquete/';

  private safeGet(url: string) {
    return this.http.get(url).pipe(
      catchError(err => {
        console.error(`Error silencioso en ${url}:`, err.message);
        return of({ data: { cantidad: 0 } }); 
      })
    );
  }

  obtenerEstadisticasPaquetes(): Observable<any> {
    return forkJoin({
      registrados: this.safeGet(`${this.baseUrl}count/0008`),
      recogidos: this.safeGet(`${this.baseUrl}count/0009`),
      enCentroDistribucion: this.safeGet(`${this.baseUrl}count/0010`),
      enReparto: this.safeGet(`${this.baseUrl}count/0011`),
      entregados: this.safeGet(`${this.baseUrl}count/0012`),
      conNovedad: this.safeGet(`${this.baseUrl}count/0013`),
      devueltos: this.safeGet(`${this.baseUrl}count/0014`),
      cancelados: this.safeGet(`${this.baseUrl}count/0015`),
      pendientes: this.safeGet(`${this.baseUrl}count/0017`),
      procesados: this.safeGet(`${this.baseUrl}count/0018`),
      fallidos: this.safeGet(`${this.baseUrl}count/0019`)
    });
  }
}