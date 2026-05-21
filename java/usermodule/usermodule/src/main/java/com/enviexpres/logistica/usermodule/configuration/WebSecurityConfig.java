package com.enviexpres.logistica.usermodule.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        		.allowedOrigins("http://localhost:4200")
		        .allowedMethods("*") // Permite todos los métodos (GET, POST, PUT, DELETE, etc.)
		        .allowedHeaders("*"); // Permite todas las cabeceras
    }
	
}
