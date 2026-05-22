package com.enviexpres.logistica.packmodule.model.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "tevp_pais")
public class TevpPais {

	@Id
	private String idPais;
	private String nmPais;
	private String sbPais;
	private String continente;
	private String idEstado;

	public TevpPais() {
	}

	public TevpPais(String idPais) {
		this.idPais = idPais;
	}

	public TevpPais(String idPais, String nmPais, String sbPais, String continente, String idEstado) {
		this.idPais = idPais;
		this.nmPais = nmPais;
		this.sbPais = sbPais;
		this.continente = continente;
		this.idEstado = idEstado;
	}

	
    
	
	public String getIdPais() {
		return this.idPais;
	}

	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	
	public String getNmPais() {
		return this.nmPais;
	}

	public void setNmPais(String nmPais) {
		this.nmPais = nmPais;
	}

	
	public String getSbPais() {
		return this.sbPais;
	}

	public void setSbPais(String sbPais) {
		this.sbPais = sbPais;
	}

	
	public String getContinente() {
		return this.continente;
	}

	public void setContinente(String continente) {
		this.continente = continente;
	}

	
	public String getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
}
