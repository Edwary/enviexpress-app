import { TestBed } from '@angular/core/testing';

import { RolMenuService } from './rol-menu-service';

describe('RolMenuService', () => {
  let service: RolMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RolMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
