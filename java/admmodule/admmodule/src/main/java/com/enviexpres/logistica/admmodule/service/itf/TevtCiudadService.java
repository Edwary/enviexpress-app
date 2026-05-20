 package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevtCiudad;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevtCiudadService {

	Mono<TevtCiudad> create(Map<String, Object> entity);
	
	Mono<TevtCiudad> findById(String id);
	
	Flux<TevtCiudad> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TevtCiudad> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TevtCiudad> toggleCiudad(String id);
	
	Mono<TevtCiudad> logicRemove(String id);

	Flux<TevtCiudad> actualizarCiudades();
	
}
