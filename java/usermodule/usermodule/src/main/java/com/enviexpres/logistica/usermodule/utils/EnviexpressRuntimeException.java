package com.enviexpres.logistica.usermodule.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.enviexpres.logistica.usermodule.utils.exception.EnviexpresExceptionHandlingController;

public class EnviexpressRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final Properties mensajes = new Properties();
	
	static {
		try {
			InputStream inputStream = EnviexpresExceptionHandlingController.class.getClassLoader().getResourceAsStream("idioma_es.properties");
            if (inputStream != null) {
                mensajes.load(inputStream);
            } else {
                throw new IOException("No se pudo cargar el archivo de recursos.");
            }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public EnviexpressRuntimeException(String message) {
        super(mensajes.getProperty(message));
    }

    public EnviexpressRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
