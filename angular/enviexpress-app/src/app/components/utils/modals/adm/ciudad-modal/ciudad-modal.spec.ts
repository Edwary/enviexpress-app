import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CiudadModal } from './ciudad-modal';

describe('CiudadModal', () => {
  let component: CiudadModal;
  let fixture: ComponentFixture<CiudadModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CiudadModal],
    }).compileComponents();

    fixture = TestBed.createComponent(CiudadModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
