package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevnVariableSistema;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnVariableSistemaService {

	Mono<TevnVariableSistema> create(Map<String, String> entity);
		
	Mono<TevnVariableSistema> findById(String id);
	
	Flux<TevnVariableSistema> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TevnVariableSistema> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TevnVariableSistema> toggle(Map<String, String> entity);
	
	Mono<TevnVariableSistema> logicRemove(String id);
}
