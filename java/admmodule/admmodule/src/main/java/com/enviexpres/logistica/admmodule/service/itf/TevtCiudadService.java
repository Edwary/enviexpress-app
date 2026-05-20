 package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import co.com.vimodules.admmodule.model.TvvtCiudad;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevtCiudadService {

	Mono<TvvtCiudad> create(Map<String, Object> entity);
	
	Mono<TvvtCiudad> findById(String id);
	
	Flux<TvvtCiudad> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TvvtCiudad> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TvvtCiudad> toggleCiudad(String id);
	
	Mono<TvvtCiudad> logicRemove(String id);

	Flux<TvvtCiudad> actualizarCiudades();
	
}
