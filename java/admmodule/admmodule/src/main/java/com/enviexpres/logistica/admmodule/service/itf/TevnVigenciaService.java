package com.enviexpres.logistica.admmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevnVigencia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnVigenciaService {

	Mono<TevnVigencia> create(Map<String, Object> entity);
	
	Mono<TevnVigencia> findById(String id);
	
	Flux<TevnVigencia> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<Map<String, Object>> findAllObject(Map<String, String> where);
	
	Mono<TevnVigencia> toggle(Map<String, Object> entity);
	
}
