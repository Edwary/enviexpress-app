import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../../../service/utils/dashboard/dashboard';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './dashboard.html'
})
export class DashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);
  private cdr = inject(ChangeDetectorRef);
  
  estadisticas: any = null;
  cargando: boolean = true;

  ngOnInit() {
    
    this.dashboardService.obtenerEstadisticasPaquetes().subscribe({
      next: (datos) => {
        this.estadisticas = datos;
        this.cargando = false;
        
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error('Error cargando estadísticas', err);
        this.cargando = false;
        
        // También lo aseguramos en caso de error
        this.cdr.detectChanges(); 
      }
    });
  }
}