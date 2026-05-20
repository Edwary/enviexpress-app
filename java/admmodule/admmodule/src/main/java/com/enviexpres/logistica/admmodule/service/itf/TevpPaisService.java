package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevpPais;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevpPaisService {

	Mono<TevpPais> create(Map<String, Object> entity);
	
//	Mono<TevpPais> update(Map<String, Object> entity);
	
	Mono<TevpPais> findById(String id);
	
	Flux<TevpPais> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TevpPais> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TevpPais> togglePais(String id);
	
	Mono<TevpPais>  logicRemove(String id);
	
}
