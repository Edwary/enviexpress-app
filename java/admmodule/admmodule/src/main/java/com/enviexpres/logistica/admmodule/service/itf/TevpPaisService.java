package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import co.com.vimodules.admmodule.model.TvvpPais;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevpPaisService {

	Mono<TvvpPais> create(Map<String, Object> entity);
	
//	Mono<TvvpPais> update(Map<String, Object> entity);
	
	Mono<TvvpPais> findById(String id);
	
	Flux<TvvpPais> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TvvpPais> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TvvpPais> togglePais(String id);
	
	Mono<TvvpPais>  logicRemove(String id);
	
}
