import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Sidenav {
  private toggleMenuSubject = new Subject<boolean>();

  toggleMeny$ = this.toggleMenuSubject.asObservable();
  constructor() {
  }

  emitToggltMenu(isExpanded: boolean) : void {
    this.toggleMenuSubject.next(isExpanded);
  }
}
