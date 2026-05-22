package com.enviexpres.logistica.packmodule.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevj_paquete_accion")
public class TevjPaqueteAccion {

	@Id
	private String uuid;
	private String idPaqueteAccion;
	private String tipoAccion;
	private String idPaquete;
	private String nus;
	private String idEstado;
	private Date fechaCreacion;
	private Date fechaProcesada;
	private Date fechaFallida;
	
	public TevjPaqueteAccion() {}

	public TevjPaqueteAccion(String uuid, String idPaqueteAccion, String tipoAccion, String idPaquete, String nus,
			String idEstado, Date fechaCreacion, Date fechaProcesada, Date fechaFallida) {
		super();
		this.uuid = uuid;
		this.idPaqueteAccion = idPaqueteAccion;
		this.tipoAccion = tipoAccion;
		this.idPaquete = idPaquete;
		this.nus = nus;
		this.idEstado = idEstado;
		this.fechaCreacion = fechaCreacion;
		this.fechaProcesada = fechaProcesada;
		this.fechaFallida = fechaFallida;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdPaqueteAccion() {
		return idPaqueteAccion;
	}

	public void setIdPaqueteAccion(String idPaqueteAccion) {
		this.idPaqueteAccion = idPaqueteAccion;
	}

	public String getTipoAccion() {
		return tipoAccion;
	}

	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}

	public String getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}

	public String getNus() {
		return nus;
	}

	public void setNus(String nus) {
		this.nus = nus;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaProcesada() {
		return fechaProcesada;
	}

	public void setFechaProcesada(Date fechaProcesada) {
		this.fechaProcesada = fechaProcesada;
	}

	public Date getFechaFallida() {
		return fechaFallida;
	}

	public void setFechaFallida(Date fechaFallida) {
		this.fechaFallida = fechaFallida;
	}

	
}
