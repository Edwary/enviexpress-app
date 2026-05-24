package com.enviexpres.logistica.usermodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tves_rol_menu")
public class TevsRolMenu {

	@Id
	private String uuid;
	private String idRolMenu;
	private String idRol;
	private String idMenu;
	private String idEstado;
	
	public TevsRolMenu() {}

	public TevsRolMenu(String idRolMenu) {
		this.idRolMenu = idRolMenu;
	}
	
	public TevsRolMenu(String uuid, String idRolMenu, String idRol, String idMenu, String idEstado) {
		super();
		this.uuid = uuid;
		this.idRolMenu = idRolMenu;
		this.idRol = idRol;
		this.idMenu = idMenu;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdRolMenu() {
		return idRolMenu;
	}

	public void setIdRolMenu(String idRolMenu) {
		this.idRolMenu = idRolMenu;
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(String idMenu) {
		this.idMenu = idMenu;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	
	
}
