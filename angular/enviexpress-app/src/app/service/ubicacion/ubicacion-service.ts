import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class UbicacionService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/ciudad';
  
  getCiudades(filtros: any): Observable<any> { 
    return this.http.post(`${this.baseUrl}/contains`, filtros); 
  }
}
