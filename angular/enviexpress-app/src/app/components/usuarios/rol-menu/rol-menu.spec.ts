import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolMenu } from './rol-menu';

describe('RolMenu', () => {
  let component: RolMenu;
  let fixture: ComponentFixture<RolMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RolMenu],
    }).compileComponents();

    fixture = TestBed.createComponent(RolMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
