import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // Necesario para <router-outlet>
import { AuthService } from '../../../../service/utils/auth/auth';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './layout.html'
})
export class LayoutComponent implements OnInit {
  private authService = inject(AuthService);
  menuItems: any[] = [];

  ngOnInit() {
    this.authService.getMenu().subscribe({
      next: (menu) => this.menuItems = menu,
      error: (err) => console.error('Error cargando el menú', err)
    });
  }
}