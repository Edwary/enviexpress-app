import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, of } from 'rxjs'; // Importamos 'of'
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private rolUsuario: string = '';
  private platformId = inject(PLATFORM_ID);

  login(credenciales: any): Observable<any> {
    return this.http.post('http://localhost:8081/usuario/login', credenciales, { observe: 'response' }).pipe(
      tap((response: any) => {
        if (response.body && response.body.body.idRol) {
          this.rolUsuario = response.body.body.idRol;
          
          if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem('idRol', this.rolUsuario);
          }
        }
      })
    );
  }

  getMenu(): Observable<any[]> {
    let rol = this.rolUsuario;
    
    if (isPlatformBrowser(this.platformId)) {
      rol = localStorage.getItem('idRol') || this.rolUsuario;
    }
    
    if (!rol) {
      console.warn('No hay rol definido. Se omite la carga del menú.');
      return of([]); 
    }
    
    return this.http.get<any[]>(`http://localhost:8081/rolMenu/ByRol/${rol}`);
  }
}