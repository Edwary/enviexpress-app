import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RolService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8081/rol';

  getRoles(filtros: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/findIfContains`, filtros);
  }
  
  findAll(): Observable<any> {
    return this.http.get(`${this.baseUrl}/findAll`);
  }

  saveRol(rol: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, rol);
  }

  toggleRol(rol: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/toggle`, rol);
  }

  deleteRol(idRol: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idRol}`);
  }
}