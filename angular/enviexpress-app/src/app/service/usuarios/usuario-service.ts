import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8081';

  getRoles(): Observable<any> {
    return this.http.get(`${this.baseUrl}/rol/findAll`);
  }

  getUsuarios(filtros: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/usuario/findIfContains`, filtros);
  }

  saveUsuario(usuario: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/usuario/create`, usuario);
  }

  toggleUsuario(usuario: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/usuario/toggle`, usuario);
  }

  deleteUsuario(idUsuario: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/usuario/${idUsuario}`);
  }
}