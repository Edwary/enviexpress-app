import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RolMenuService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8081/rolMenu';

  getRolMenus(filtros: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/findIfContains`, filtros);
  }

  saveRolMenu(rolMenu: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, rolMenu);
  }

  toggleRolMenu(rolMenu: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/toggle`, rolMenu);
  }

  deleteRolMenu(idRolMenu: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idRolMenu}`);
  }
}