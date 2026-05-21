package com.enviexpres.logistica.clientmodule.model.dto;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevn_error")
public class TevnError {

	@Id
	private String idError;
	private String consecutivo;
	private Date fechaError;
	private String horaError;
	private String nmModulo;
	private String dsError;

	public TevnError() {
	}

	public TevnError(String idError) {
		this.idError = idError;
	}

	public TevnError(String idError, String dsError, Date fechaError, String horaError, String nmModulo, String consecutivo ) {
		this.idError = idError;
		this.consecutivo = consecutivo;
		this.fechaError = fechaError;
		this.horaError = horaError;
		this.nmModulo = nmModulo;
		this.dsError = dsError;
	}

	public String getIdError() {
		return this.idError;
	}

	public void setIdError(String idError) {
		this.idError = idError;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getHoraError() {
		return horaError;
	}

	public void setHoraError(String horaError) {
		this.horaError = horaError;
	}

	public Date getFechaError() {
		return this.fechaError;
	}

	public void setFechaError(Date fechaError) {
		this.fechaError = fechaError;
	}
	
	public String getNmModulo() {
		return nmModulo;
	}

	public void setNmModulo(String nmModulo) {
		this.nmModulo = nmModulo;
	}

	public String getDsError() {
		return this.dsError;
	}

	public void setDsError(String dsError) {
		this.dsError = dsError;
	}
	
}
