import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { HttpClient, provideHttpClient } from '@angular/common/http';

// Importaciones de ngx-translate
// Importaciones actualizadas de ngx-translate
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader, provideTranslateHttpLoader } from '@ngx-translate/http-loader';

// Función obligatoria que le dice a Translate dónde buscar los archivos JSON de idiomas
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(), // Necesario para que funcione el TranslateHttpLoader
    
    provideTranslateHttpLoader({
      prefix: './assets/i18n/',
      suffix: '.json'
    }),

    // Configuración global de NGX-Translate
    importProvidersFrom(
      TranslateModule.forRoot({
        defaultLanguage: 'es', // Es buena práctica definir un idioma por defecto aquí
        loader: {
          provide: TranslateLoader,
          useClass: TranslateHttpLoader // La clase se instancia sola y lee la configuración de arriba
        }
      })
    )
  ]
};