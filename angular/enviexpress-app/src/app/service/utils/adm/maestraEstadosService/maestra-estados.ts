import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MaestraEstados {
  private gestionarEstadoSource = new Subject<void>();
  
  gestionarEstadoObservable = this.gestionarEstadoSource.asObservable();

  constructor() { }

  gestionarEstado(){
    this.gestionarEstadoSource.next();
  }
}
