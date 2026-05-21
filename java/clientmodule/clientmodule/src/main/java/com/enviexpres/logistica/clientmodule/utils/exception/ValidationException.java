package com.enviexpres.logistica.clientmodule.utils.exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
	
	private static final long serialVersionUID = 1L;
	private static final Properties mensajes = new Properties();
	
	static {
		try {
			InputStream inputStream = ValidationException.class.getClassLoader().getResourceAsStream("idioma_es.properties");
            if (inputStream != null) {
                mensajes.load(inputStream);
            } else {
                throw new IOException("No se pudo cargar el archivo de recursos.");
            }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ValidationException(HttpStatus status, String messageKey) {
		super(status, mensajes.getProperty(messageKey, messageKey));
	}
	
	public ValidationException(String message) {
		super(HttpStatus.BAD_REQUEST, mensajes.getProperty(message, message));
	}
	
    @Override
    public String getMessage() {
        // Devuelve el mensaje personalizado que deseas incluir en la respuesta
        return "Mensaje personalizado: " + super.getMessage();
    }
}
