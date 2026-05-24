package com.enviexpres.logistica.usermodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.usermodule.model.TevnRol;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnRolService {
	
	Mono<TevnRol> create(Map<String, String> entity);
	
	Mono<TevnRol> findById(String idRol);	
	
	Flux<TevnRol> findAll();

	Mono<TevnRol> toggle(Map<String, String> entity);
	
	Mono<Void> remove(String idRol);
	
	Flux<Map<String, Object>> findIfContains(Map<String, String> where);
}
