import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';

import { MaestraUbicacionesComponent } from './maestra-ubicaciones.component';

describe('MaestraUbicacionesComponent', () => {
  let component: MaestraUbicacionesComponent;
  let fixture: ComponentFixture<MaestraUbicacionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MaestraUbicacionesComponent, // Al ser standalone, se importa, no se declara
        HttpClientTestingModule,
        BrowserAnimationsModule,
        TranslateModule.forRoot()
      ]
      // Aquí también deberás proveer los Mock Services para SidenavService, etc., si los usas
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MaestraUbicacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});