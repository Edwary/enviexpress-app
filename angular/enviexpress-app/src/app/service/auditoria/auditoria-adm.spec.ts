import { TestBed } from '@angular/core/testing';

import { AuditoriaAdm } from './auditoria-adm';

describe('AuditoriaAdm', () => {
  let service: AuditoriaAdm;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuditoriaAdm);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
