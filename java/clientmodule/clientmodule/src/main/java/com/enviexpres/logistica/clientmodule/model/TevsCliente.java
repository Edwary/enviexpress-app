package com.enviexpres.logistica.clientmodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tevs_client")
public class TevsCliente {

	 @Id
	 private String uuid;
	 private String idCliente;
	 private String nmCliente;
	 private String documento;
	 private String tipoDocumento;
	 private String email;
	 private String telefono;
	 private String idEstado;
	 
	 public TevsCliente() {}

	public TevsCliente(String uuid, String idCliente, String nmCliente, String documento, String tipoDocumento, String email,
			String telefono, String idEstado) {
		super();
		this.uuid = uuid;
		this.idCliente = idCliente;
		this.nmCliente = nmCliente;
		this.documento = documento;
		this.tipoDocumento = tipoDocumento;
		this.email = email;
		this.telefono = telefono;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getNmCliente() {
		return nmCliente;
	}

	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	 
}
