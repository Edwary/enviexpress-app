import { TestBed } from '@angular/core/testing';

import { MaestraEstados } from './maestra-estados';

describe('MaestraEstados', () => {
  let service: MaestraEstados;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaestraEstados);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
