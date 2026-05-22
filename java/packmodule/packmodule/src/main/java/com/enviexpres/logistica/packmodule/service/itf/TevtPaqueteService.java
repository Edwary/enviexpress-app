package com.enviexpres.logistica.packmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.packmodule.model.TevjPaqueteAccion;
import com.enviexpres.logistica.packmodule.model.TevtPaquete;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevtPaqueteService {

	Mono<TevtPaquete> create(Map<String, String> entity);
	
	Mono<Map<String, Object>> findById(String idPaquete);	
	
	Flux<TevtPaquete> findAll();

	Mono<Map<String, Object>> toggle(Map<String, String> entity);
	
	Mono<Void> remove(String idPaquete);
	
	Flux<Map<String, Object>> findByIdContains(Map<String, String> where);
	
	Mono<TevjPaqueteAccion> updatePaqueteAccion(Map<String, String> entity);
}
