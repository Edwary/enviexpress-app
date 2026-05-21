package com.enviexpres.logistica.clientmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.clientmodule.model.TevsCliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevsClienteService {

	Mono<TevsCliente> create(Map<String, String> entity);
	
	Mono<TevsCliente> findById(String idRol);	
	
	Flux<TevsCliente> findAll();

	Mono<TevsCliente> toggle(Map<String, String> entity);
	
	Mono<Void> remove(String idRol);
}
