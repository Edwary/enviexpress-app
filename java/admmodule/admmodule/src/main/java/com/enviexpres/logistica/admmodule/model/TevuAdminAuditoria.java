package com.enviexpres.logistica.admmodule.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevu_admin_auditoria")
public class TevuAdminAuditoria {

	@Id
	private String consecutivo;
	private String vista;
	private String usuario;
	private String nup;
	private String nus;
	private Date fecha;
	private String hora;
	private String accion;
	private org.bson.Document contenido;
	
	public TevuAdminAuditoria() {}
	
	public TevuAdminAuditoria(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public TevuAdminAuditoria(String consecutivo, String vista, String usuario, String nup, String nus,Date fecha, String hora, String accion,
			org.bson.Document contenido) {
		super();
		this.consecutivo = consecutivo;
		this.vista = vista;
		this.usuario = usuario;
		this.nup = nup;
		this.nus = nus;
		this.fecha = fecha;
		this.hora = hora;
		this.accion = accion;
		this.contenido = contenido;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNup() {
		return nup;
	}

	public void setNup(String nup) {
		this.nup = nup;
	}

	public String getNus() {
		return nus;
	}

	public void setNus(String nus) {
		this.nus = nus;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public org.bson.Document getContenido() {
		return contenido;
	}

	public void setContenido(org.bson.Document contenido) {
		this.contenido = contenido;
	}
	
}
