package com.enviexpres.logistica.usermodule.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevt_usuario")
public class TevtUsuario {

	@Id
	private String uuid;
	private String nus;
	private String nmUsuario;
	private String nombre;
	private String password;
	private String passwordAnt;
	private String email;
	private Date fechaCreacion;
	private String idRol;
	private String idEstado;
	private Date fechaUltimoIngreso;
	
	public TevtUsuario() {}

	public TevtUsuario (String nus) {
		this.nus = nus;
	}
	
	public TevtUsuario(String uuid, String nus, String nmUsuario, String nombre, String password, String passwordAnt,
			String emailUsuario, Date fechaCreacion, String idRol, String idEstado, Date fechaUltimoIngreso) {
		super();
		this.uuid = uuid;
		this.nus = nus;
		this.nmUsuario = nmUsuario;
		this.nombre = nombre;
		this.password = password;
		this.passwordAnt = passwordAnt;
		this.email = emailUsuario;
		this.fechaCreacion = fechaCreacion;
		this.idRol = idRol;
		this.idEstado = idEstado;
		this.fechaUltimoIngreso = fechaUltimoIngreso;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNus() {
		return nus;
	}

	public void setNus(String nus) {
		this.nus = nus;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordAnt() {
		return passwordAnt;
	}

	public void setPasswordAnt(String passwordAnt) {
		this.passwordAnt = passwordAnt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public Date getFechaUltimoIngreso() {
		return fechaUltimoIngreso;
	}

	public void setFechaUltimoIngreso(Date fechaUltimoIngreso) {
		this.fechaUltimoIngreso = fechaUltimoIngreso;
	}
	
}
