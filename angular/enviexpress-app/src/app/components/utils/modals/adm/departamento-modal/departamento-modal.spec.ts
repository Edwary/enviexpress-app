import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

import { DepartamentoModalComponent } from './departamento-modal';

describe('DepartamentoModalComponent', () => {
  let component: DepartamentoModalComponent;
  let fixture: ComponentFixture<DepartamentoModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ 
        DepartamentoModalComponent, // Importar al ser Standalone
        HttpClientTestingModule,
        BrowserAnimationsModule,
        TranslateModule.forRoot()
      ],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} } // Mock para los datos
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DepartamentoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});