package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import co.com.vimodules.admmodule.model.TvvsDepartamento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevsDepartamentoService {

	Mono<TvvsDepartamento> create(Map<String, Object> entity);
	
	Mono<TvvsDepartamento> findById(String id);
	
	Flux<TvvsDepartamento> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TvvsDepartamento> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TvvsDepartamento> toggleDepartamento(String id);
	
	Mono<TvvsDepartamento> logicRemove(String id);
	
}
