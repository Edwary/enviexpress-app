package com.enviexpres.logistica.admmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.admmodule.model.TevuAdminAuditoria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevuAdminAuditoriaService {

	Mono<TevuAdminAuditoria> create(Map<String, Object> entity);
	
	Mono<TevuAdminAuditoria> findById(String id);
	
	Flux<TevuAdminAuditoria> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<Map<String, Object>> findIfContains(Map<String, String> filter);
	
}
