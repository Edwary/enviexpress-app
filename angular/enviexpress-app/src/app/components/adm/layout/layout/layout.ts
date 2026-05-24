import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // Necesario para <router-outlet>
import { AuthService } from '../../../../service/utils/auth/auth';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './layout.html'
})
export class LayoutComponent implements OnInit {
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  menuItems: any[] = [];
  cargandoMenu: boolean = true;

  ngOnInit() {
    this.authService.getMenu().subscribe({
      next: (res) => {
        let menuData = res;
        this.menuItems = menuData.map((item: any) => ({
          ...item,
          expandido: false
        }));
        this.cargandoMenu = false;

        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error('Error cargando el menú', err);
        this.cargandoMenu = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleMenu(item: any) {
    if (item.children && item.children.length > 0) {
      item.expandido = !item.expandido;
    } else {
      if (item.href != '') {
        this.router.navigate([item.href]);
      }
    }
  }
}