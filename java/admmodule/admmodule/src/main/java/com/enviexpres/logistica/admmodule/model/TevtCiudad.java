package com.enviexpres.logistica.admmodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection =  "tevt_ciudad")
public class TevtCiudad {

	@Id
	private String idCiudad;
	private String nmCiudad;
	private String sbCiudad;
	private String codigoPostal;
	private String codigoDane;
	private String subRegion;
	private String tipo;
	private String idDepartamento;
	private String idEstado;
	private String homologacion;

	public TevtCiudad() {
	}

	public TevtCiudad(String idCiudad) {
		this.idCiudad = idCiudad;
	}

	public TevtCiudad(String idCiudad, String nmCiudad, String sbCiudad, String codigoPostal, String codigoDane,
			String subRegion, String tipo, String idDepartamento, String idEstado, String homologacion) {
		this.idCiudad = idCiudad;
		this.nmCiudad = nmCiudad;
		this.sbCiudad = sbCiudad;
		this.codigoPostal = codigoPostal;
		this.codigoDane = codigoDane;
		this.subRegion = subRegion;
		this.tipo = tipo;
		this.idDepartamento = idDepartamento;
		this.idEstado = idEstado;
		this.homologacion = homologacion;
	}

	
    
	
	public String getIdCiudad() {
		return this.idCiudad;
	}

	public void setIdCiudad(String idCiudad) {
		this.idCiudad = idCiudad;
	}

	
	public String getNmCiudad() {
		return this.nmCiudad;
	}

	public void setNmCiudad(String nmCiudad) {
		this.nmCiudad = nmCiudad;
	}

	
	public String getSbCiudad() {
		return this.sbCiudad;
	}

	public void setSbCiudad(String sbCiudad) {
		this.sbCiudad = sbCiudad;
	}

	
	public String getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	
	public String getCodigoDane() {
		return this.codigoDane;
	}

	public void setCodigoDane(String codigoDane) {
		this.codigoDane = codigoDane;
	}

	
	public String getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getIdDepartamento() {
		return this.idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	
	public String getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public String getHomologacion() {
		return homologacion;
	}

	public void setHomologacion(String homologacion) {
		this.homologacion = homologacion;
	}
}
