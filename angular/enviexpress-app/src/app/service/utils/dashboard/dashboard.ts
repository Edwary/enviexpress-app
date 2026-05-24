import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8083/paquete/';

  obtenerEstadisticasPaquetes(): Observable<any> {
    return forkJoin({
      registrados: this.http.get(`${this.baseUrl}count/0008`),
      recogidos: this.http.get(`${this.baseUrl}count/0009`),
      enCentroDistribucion: this.http.get(`${this.baseUrl}count/0010`),
      enReparto: this.http.get(`${this.baseUrl}count/0011`),
      entregados: this.http.get(`${this.baseUrl}count/0012`),
      conNovedad: this.http.get(`${this.baseUrl}count/0013`),
      devueltos: this.http.get(`${this.baseUrl}count/0014`),
      cancelados: this.http.get(`${this.baseUrl}count/0015`),
      pendientes: this.http.get(`${this.baseUrl}count/0017`),
      procesados: this.http.get(`${this.baseUrl}count/0018`),
      fallidos: this.http.get(`${this.baseUrl}count/0019`)
    });
  }
}