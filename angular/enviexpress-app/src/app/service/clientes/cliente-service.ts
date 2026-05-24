import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8082/cliente';

  getClientes(filtros: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/findIfContains`, filtros);
  }

  saveCliente(cliente: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, cliente);
  }

  toggleCliente(cliente: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/toggle`, cliente);
  }

  deleteCliente(idCliente: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idCliente}`);
  }
}