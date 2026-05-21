package com.enviexpres.logistica.clientmodule.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "clientmodule")
public class ClientmoduleConfiguration {

	private String servers;
    private String credentials;
    // Otras propiedades

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // Configura aquí las propiedades del cliente HTTP, como timeouts, etc.
        return restTemplate;
    }

    // Getter y setter para las propiedades

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}
