package com.enviexpres.logistica.usermodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "tevn_rol")
public class TevnRol {

	@Id
	private String uuid;
	private String idRol;
	private String nombre;
	private String idEstado;
	
	public TevnRol() {}
	
	public TevnRol(String uuid, String idRol, String nombre, String idEstado) {
		super();
		this.uuid = uuid;
		this.idRol = idRol;
		this.nombre = nombre;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	
}
