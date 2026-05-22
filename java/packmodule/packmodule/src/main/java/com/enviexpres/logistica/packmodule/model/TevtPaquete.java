package com.enviexpres.logistica.packmodule.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tvet_paquete")
public class TevtPaquete {

	@Id
	private String uuid;
	private String idPaquete;
	private String idCliente;
	private String destinatario;
	private String direccion;
	private String idCiudad;
	private String idDepartamento;
	private String telefono;
	private String peso;
	private String valorDeclarado;
	private Date fechaCreacion;
	private String idEstado;
	
	public TevtPaquete() {}

	public TevtPaquete(String uuid, String idPaquete, String idCliente, String destinatario, String direccion,
			String idCiudad, String idDepartamento, String telefono, String peso, String valorDeclarado,
			Date fechaCreacion, String idEstado) {
		super();
		this.uuid = uuid;
		this.idPaquete = idPaquete;
		this.idCliente = idCliente;
		this.destinatario = destinatario;
		this.direccion = direccion;
		this.idCiudad = idCiudad;
		this.idDepartamento = idDepartamento;
		this.telefono = telefono;
		this.peso = peso;
		this.valorDeclarado = valorDeclarado;
		this.fechaCreacion = fechaCreacion;
		this.idEstado = idEstado;
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

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(String idCiudad) {
		this.idCiudad = idCiudad;
	}

	public String getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getValorDeclarado() {
		return valorDeclarado;
	}

	public void setValorDeclarado(String valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
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
