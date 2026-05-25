import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EstadoService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/estado';

  getEstadosPorModulo(modulo: string): Observable<any> { 
    return this.http.post(`${this.baseUrl}/contains`, { 'modulo' : 'PAQUETES' }); 
  }
}
  