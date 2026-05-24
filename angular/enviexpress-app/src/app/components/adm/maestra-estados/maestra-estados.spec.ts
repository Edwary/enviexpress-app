import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaestraEstados } from './maestra-estados';

describe('MaestraEstados', () => {
  let component: MaestraEstados;
  let fixture: ComponentFixture<MaestraEstados>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaestraEstados],
    }).compileComponents();

    fixture = TestBed.createComponent(MaestraEstados);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
