import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MaestraUbicacionesService {
  private gestionarPaisesSource = new Subject<void>();
  private gestionarDepartamentosSource = new Subject<void>();
  private gestionarCiudadesSource = new Subject<void>();
  
  gestionarPaisesObservable = this.gestionarPaisesSource.asObservable();
  gestionarDepartamentosObservable = this.gestionarDepartamentosSource.asObservable();
  gestionarCiudadesObservable = this.gestionarCiudadesSource.asObservable();

  constructor() { }

  gestionarPaises() {
    this.gestionarPaisesSource.next();
  }

  gestionarDepartamentos() {
    this.gestionarDepartamentosSource.next();
  }

  gestionarCiudades() {
    this.gestionarCiudadesSource.next();
  }

}
