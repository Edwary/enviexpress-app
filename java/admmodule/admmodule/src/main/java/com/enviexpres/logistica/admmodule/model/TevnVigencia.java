package com.enviexpres.logistica.admmodule.model;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "tevn_vigencia" )
public class TevnVigencia {
	@Id
	private String uuid;
	private String idVigencia;
	private String numAnio;
	private Date fechaInicio;
	private Date fechaFin;
	private String idEstado;
	
	public TevnVigencia() {}

	public TevnVigencia(String uuid, String idVigencia, String numAnio, Date fechaInicio, Date fechaFin, String idEstado) {
		super();
		this.uuid = uuid;
		this.idVigencia = idVigencia;
		this.numAnio = numAnio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdVigencia() {
		return idVigencia;
	}

	public void setIdVigencia(String idVigencia) {
		this.idVigencia = idVigencia;
	}

	public String getNumAnio() {
		return numAnio;
	}

	public void setNumAnio(String numAnio) {
		this.numAnio = numAnio;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
}
