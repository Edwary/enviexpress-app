package com.enviexpres.logistica.clientmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.clientmodule.model.TevuClienteAuditoria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevuClienteAuditoriaService {

	Mono<TevuClienteAuditoria> create(Map<String, Object> entity);
	
	Mono<TevuClienteAuditoria> findById(String id);
	
	Flux<TevuClienteAuditoria> findAll();
	
	Mono<Void> remove(String id);
}
