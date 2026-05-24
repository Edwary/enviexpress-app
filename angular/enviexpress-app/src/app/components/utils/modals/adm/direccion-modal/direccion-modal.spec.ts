import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { DireccionModalComponent } from './direccion-modal';

describe('DireccionModalComponent', () => {
  let component: DireccionModalComponent;
  let fixture: ComponentFixture<DireccionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ 
        DireccionModalComponent, // Importar al ser componente Standalone
        HttpClientTestingModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DireccionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});