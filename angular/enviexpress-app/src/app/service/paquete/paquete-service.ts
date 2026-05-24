import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PaqueteService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8083/paquete';

  getPaquetes(filtros: any): Observable<any> { 
    return this.http.post(`${this.baseUrl}/findIfContains`, filtros); 
  }
  
  getPaqueteById(id: string): Observable<any> { 
    return this.http.get(`${this.baseUrl}/findById/${id}`); 
  }
  
  savePaquete(paquete: any): Observable<any> { 
    return this.http.post(`${this.baseUrl}/create`, paquete); 
  }
  
  togglePaquete(paquete: any): Observable<any> { 
    return this.http.put(`${this.baseUrl}/toggle`, paquete); 
  } 
  
  deletePaquete(id: string): Observable<any> { 
    return this.http.delete(`${this.baseUrl}/${id}`); 
  }
}