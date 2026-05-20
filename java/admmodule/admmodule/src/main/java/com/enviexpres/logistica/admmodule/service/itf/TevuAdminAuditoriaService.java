package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;

import co.com.vimodules.admmodule.model.TvvuAdminAuditoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevuAdminAuditoriaService {

	Mono<TvvuAdminAuditoria> create(Map<String, Object> entity);
	
	Mono<TvvuAdminAuditoria> findById(String id);
	
	Flux<TvvuAdminAuditoria> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
}
