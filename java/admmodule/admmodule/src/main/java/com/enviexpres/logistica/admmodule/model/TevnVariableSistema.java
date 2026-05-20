package com.enviexpres.logistica.admmodule.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tevn_variable_sistema")
public class TevnVariableSistema {

	@Id
	private String uuid;
	private String idVariable;
	private String nmVariable;
	private String valor;
	private String dsVariable;
	private String tipo;
	private String idEstado;
	
	public TevnVariableSistema() {}

	public TevnVariableSistema(String uuid, String idVariable, String nmVariable, String valor, 
			String dsVariable, String tipo, String idEstado) {
		super();
		this.uuid = uuid;
		this.idVariable = idVariable;
		this.nmVariable = nmVariable;
		this.valor = valor;
		this.dsVariable = dsVariable;
		this.tipo = tipo;
		this.idEstado = idEstado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdVariable() {
		return idVariable;
	}

	public void setIdVariable(String idVariable) {
		this.idVariable = idVariable;
	}

	public String getNmVariable() {
		return nmVariable;
	}

	public void setNmVariable(String nmVariable) {
		this.nmVariable = nmVariable;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDsVariable() {
		return dsVariable;
	}

	public void setDsVariable(String dsVariable) {
		this.dsVariable = dsVariable;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
}
