import { TestBed } from '@angular/core/testing';

import { AuditoriaPaquetes } from './auditoria-paquetes';

describe('AuditoriaPaquetes', () => {
  let service: AuditoriaPaquetes;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuditoriaPaquetes);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
