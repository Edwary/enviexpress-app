package com.enviexpres.logistica.admmodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "tevs_departamento")
public class TevsDepartamento {

	@Id
	private String idDepartamento;
	private String idPais;
	private String nmDepartamento;
	private String sbDepartamento;
	private String region;
	private String codigoDane;
	private String codigoPostal;
	private String idEstado;

	public TevsDepartamento() {
	}

	public TevsDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public TevsDepartamento(String idDepartamento, String idPais, String nmDepartamento, String sbDepartamento, String region, 
			String codigoDane, String codigoPostal, String idEstado) {
		this.idDepartamento = idDepartamento;
		this.idPais = idPais;
		this.nmDepartamento = nmDepartamento;
		this.sbDepartamento = sbDepartamento;
		this.region = region;
		this.codigoDane = codigoDane;
		this.codigoPostal = codigoPostal;
		this.idEstado = idEstado;
	}

	
    
	
	public String getIdDepartamento() {
		return this.idDepartamento;
	}

	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	
	public String getIdPais() {
		return this.idPais;
	}

	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	
	public String getNmDepartamento() {
		return this.nmDepartamento;
	}

	public void setNmDepartamento(String nmDepartamento) {
		this.nmDepartamento = nmDepartamento;
	}

	
	public String getSbDepartamento() {
		return this.sbDepartamento;
	}

	public void setSbDepartamento(String sbDepartamento) {
		this.sbDepartamento = sbDepartamento;
	}

	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCodigoDane() {
		return codigoDane;
	}

	public void setCodigoDane(String codigoDane) {
		this.codigoDane = codigoDane;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
}
