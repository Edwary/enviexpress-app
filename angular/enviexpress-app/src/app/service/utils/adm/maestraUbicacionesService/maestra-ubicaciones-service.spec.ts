import { TestBed } from '@angular/core/testing';

import { MaestraUbicacionesService } from './maestra-ubicaciones-service';

describe('MaestraUbicacionesService', () => {
  let service: MaestraUbicacionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaestraUbicacionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
