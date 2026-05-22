package com.enviexpres.logistica.packmodule.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tevj_paquete_estado")
public class TevjPaqueteEstado {

	@Id
	private String uuid;
	private String idPaquete;
	private String idEstado;
	private String idEstadoAnterior;
	private Date fechaEstado;
	private String nus;
	private String observaciones;
	
	public TevjPaqueteEstado() {}

	public TevjPaqueteEstado(String uuid, String idPaquete, String idEstado, String idEstadoAnterior, Date fechaEstado,
			String nus, String observaciones) {
		super();
		this.uuid = uuid;
		this.idPaquete = idPaquete;
		this.idEstado = idEstado;
		this.idEstadoAnterior = idEstadoAnterior;
		this.fechaEstado = fechaEstado;
		this.nus = nus;
		this.observaciones = observaciones;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public String getIdEstadoAnterior() {
		return idEstadoAnterior;
	}

	public void setIdEstadoAnterior(String idEstadoAnterior) {
		this.idEstadoAnterior = idEstadoAnterior;
	}

	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getNus() {
		return nus;
	}

	public void setNus(String nus) {
		this.nus = nus;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
}
