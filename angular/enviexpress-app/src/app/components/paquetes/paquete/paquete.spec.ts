import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Paquete } from './paquete';

describe('Paquete', () => {
  let component: Paquete;
  let fixture: ComponentFixture<Paquete>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Paquete],
    }).compileComponents();

    fixture = TestBed.createComponent(Paquete);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
