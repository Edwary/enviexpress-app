package com.enviexpres.logistica.admmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevnEstado;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnEstadoService {
	
	Mono<TevnEstado> create(TevnEstado entity);
	
	Mono<TevnEstado> findById(String id);
	
	Flux<TevnEstado> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TevnEstado> findIfContains(Map<String, String> filter);
	
}
