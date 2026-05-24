import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private rolUsuario: string = 'ADM';

  login(credenciales: any): Observable<any> {
    return this.http.post('http://localhost:8081/usuario/login', credenciales, { observe: 'response' }).pipe(
      tap((response: any) => {
        if (response.body && response.body.idRol) {
          this.rolUsuario = response.body.idRol;
          localStorage.setItem('idRol', this.rolUsuario);
        }
      })
    );
  }

  getMenu(): Observable<any[]> {
    const rol = localStorage.getItem('idRol') || this.rolUsuario;
    console.log('Obteniendo menú para rol:', rol);
    return this.http.get<any[]>(`http://localhost:8081/rolMenu/${rol}`);
  }
}