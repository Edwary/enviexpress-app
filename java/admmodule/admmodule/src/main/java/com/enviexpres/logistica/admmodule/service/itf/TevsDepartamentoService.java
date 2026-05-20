package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevsDepartamento;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevsDepartamentoService {

	Mono<TevsDepartamento> create(Map<String, Object> entity);
	
	Mono<TevsDepartamento> findById(String id);
	
	Flux<TevsDepartamento> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TevsDepartamento> createVarious(List<Map<String, Object>> entityList);

	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
	Mono<TevsDepartamento> toggleDepartamento(String id);
	
	Mono<TevsDepartamento> logicRemove(String id);
	
}
