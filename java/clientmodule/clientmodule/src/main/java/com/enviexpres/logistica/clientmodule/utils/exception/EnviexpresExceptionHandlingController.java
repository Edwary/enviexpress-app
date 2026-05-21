package com.enviexpres.logistica.clientmodule.utils.exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.enviexpres.logistica.clientmodule.utils.EnviexpressRuntimeException;
import com.enviexpres.logistica.clientmodule.utils.UtilsException;

@ControllerAdvice
public class EnviexpresExceptionHandlingController {

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
	
	@ExceptionHandler(EnviexpressRuntimeException.class)
	public ResponseEntity<UtilsException> handleViValidationException(EnviexpressRuntimeException ex) {
		UtilsException errorResponse = new UtilsException(ex.hashCode(),mensajes.getProperty(ex.getMessage(), ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
	
}
