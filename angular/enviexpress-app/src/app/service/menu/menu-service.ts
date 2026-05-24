import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MenuService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/menu';

  findAll(): Observable<any> {
    return this.http.get(`${this.baseUrl}/findAll`);
  }

  create(menu: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, menu);
  }

  toggle(menu: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/toggle`, menu);
  }

  delete(idMenu: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idMenu}`);
  }

  getMenus(filtros: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/findIfContains`, filtros);
  }
}