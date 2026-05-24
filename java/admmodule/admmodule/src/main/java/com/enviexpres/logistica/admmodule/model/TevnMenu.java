package com.enviexpres.logistica.admmodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevn_menu")
public class TevnMenu {

	@Id
	private String uuid;
	private String idMenu;
	private String nmMenu;
	private String idMenuSup;
	private String tipoMenu;
	private String href;
	private String nivel;
	private String orden;
	private String indVisible;
	private String indRol;
	private String idEstado;

	public TevnMenu() {
	}

	public TevnMenu(String idMenu, String idMenuSup) {
		this.idMenu = idMenu;
		this.idMenuSup = idMenuSup;
	}

	public TevnMenu(String uuid, String idMenu, String nmMenu, String idMenuSup, String tipoMenu, String href,
			String nivel, String orden, String indVisible, String indRol, String idEstado) {
		super();
		this.uuid = uuid;
		this.idMenu = idMenu;
		this.nmMenu = nmMenu;
		this.idMenuSup = idMenuSup;
		this.tipoMenu = tipoMenu;
		this.href = href;
		this.nivel = nivel;
		this.orden = orden;
		this.indVisible = indVisible;
		this.indRol = indRol;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(String idMenu) {
		this.idMenu = idMenu;
	}

	public String getNmMenu() {
		return nmMenu;
	}

	public void setNmMenu(String nmMenu) {
		this.nmMenu = nmMenu;
	}

	public String getIdMenuSup() {
		return idMenuSup;
	}

	public void setIdMenuSup(String idMenuSup) {
		this.idMenuSup = idMenuSup;
	}

	public String getTipoMenu() {
		return tipoMenu;
	}

	public void setTipoMenu(String tipoMenu) {
		this.tipoMenu = tipoMenu;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getIndVisible() {
		return indVisible;
	}

	public void setIndVisible(String indVisible) {
		this.indVisible = indVisible;
	}

	public String getIndRol() {
		return indRol;
	}

	public void setIndRol(String indRol) {
		this.indRol = indRol;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
}
