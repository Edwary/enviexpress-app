import { TestBed } from '@angular/core/testing';

import { AuditoriaClientes } from './auditoria-clientes';

describe('AuditoriaClientes', () => {
  let service: AuditoriaClientes;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuditoriaClientes);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
