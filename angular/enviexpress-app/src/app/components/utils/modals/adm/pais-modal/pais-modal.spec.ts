import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

import { PaisModalComponent } from './pais-modal';

describe('PaisModal', () => {
  let component: PaisModalComponent;
  let fixture: ComponentFixture<PaisModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ 
        PaisModalComponent,
        HttpClientTestingModule,
        BrowserAnimationsModule,
        TranslateModule.forRoot()
      ],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} } 
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaisModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});