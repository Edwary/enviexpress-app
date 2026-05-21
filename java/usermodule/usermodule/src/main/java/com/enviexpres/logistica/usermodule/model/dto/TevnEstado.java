package com.enviexpres.logistica.usermodule.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "tevn_estado")
public class TevnEstado {

	@Id
	private String idEstado;
	@NotNull
	private String nmEstado;
	private String dsEstado;
	private String sbEstado;
	@NotNull
	private String color;
	private Boolean visibilidad;
	private String modulo;

	public TevnEstado() {
	}

	public TevnEstado(String idEstado, String nmEstado, String color) {
		this.idEstado = idEstado;
	}

	public TevnEstado(String idEstado, String nmEstado, String dsEstado, String sbEstado, String color,
			Boolean visibilidad, String modulo) {
		this.idEstado = idEstado;
		this.nmEstado = nmEstado;
		this.dsEstado = dsEstado;
		this.sbEstado = sbEstado;
		this.color = color;
		this.visibilidad = visibilidad;
		this.modulo = modulo;
	}

	public String getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public String getNmEstado() {
		return this.nmEstado;
	}

	public void setNmEstado(String nmEstado) {
		this.nmEstado = nmEstado;
	}

	public String getDsEstado() {
		return this.dsEstado;
	}

	public void setDsEstado(String dsEstado) {
		this.dsEstado = dsEstado;
	}

	public String getSbEstado() {
		return this.sbEstado;
	}

	public void setSbEstado(String sbEstado) {
		this.sbEstado = sbEstado;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(Boolean visibilidad) {
		this.visibilidad = visibilidad;
	}

	public String getModulo() {
		return this.modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
}
