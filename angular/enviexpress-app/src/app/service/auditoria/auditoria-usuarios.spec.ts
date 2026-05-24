import { TestBed } from '@angular/core/testing';

import { AuditoriaUsuarios } from './auditoria-usuarios';

describe('AuditoriaUsuarios', () => {
  let service: AuditoriaUsuarios;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuditoriaUsuarios);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
