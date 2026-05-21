package com.enviexpres.logistica.clientmodule.utils;

import org.springframework.http.HttpStatus;

public class UtilsException extends Exception {

	private static final long serialVersionUID = 1L;
	private int codigo;
	private HttpStatus status;
	private String message;
	private String statusStr;
	
	public UtilsException(int codigo, String message) {
		super(message);
		this.codigo = codigo;
	}
	
	public UtilsException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
	
	public UtilsException(String statusStr, String message) {
		super(message);
		this.statusStr = statusStr;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	
}
