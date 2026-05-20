package com.enviexpres.logistica.admmodule.service.itf;

import java.util.Map;

import co.com.vimodules.admmodule.model.TvvnVigencia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnVigenciaService {

	Mono<TvvnVigencia> create(Map<String, Object> entity);
	
	Mono<TvvnVigencia> findById(String id);
	
	Flux<TvvnVigencia> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<Map<String, Object>> findAllObject(Map<String, String> where);
	
	Mono<TvvnVigencia> toggle(Map<String, Object> entity);
	
}
