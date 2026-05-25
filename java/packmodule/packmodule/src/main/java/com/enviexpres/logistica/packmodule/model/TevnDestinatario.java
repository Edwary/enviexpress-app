package com.enviexpres.logistica.packmodule.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevn_destinatario")
public class TevnDestinatario {

	@Id
	private String uuid;
	private String idDestinatario;
	private String nombre;
	@Indexed(unique = true)
	private String documento;
	private String telefono;
	private Date fechaCreacion;
	private String idEstado;
	
	public TevnDestinatario() {}

	public TevnDestinatario(String uuid, String idDestinatario, String nombre, String documento, String telefono,
			Date fechaCreacion, String idEstado) {
		super();
		this.uuid = uuid;
		this.idDestinatario = idDestinatario;
		this.nombre = nombre;
		this.documento = documento;
		this.telefono = telefono;
		this.fechaCreacion = fechaCreacion;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(String idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	
	
}
