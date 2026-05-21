package com.enviexpres.logistica.usermodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.usermodule.model.TevuUsuarioAuditoria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevuUsuarioAuditoriaService {

	Mono<TevuUsuarioAuditoria> create(Map<String, Object> entity);
	
	Mono<TevuUsuarioAuditoria> findById(String id);
	
	Flux<TevuUsuarioAuditoria> findAll();
	
	Mono<Void> remove(String id);
}
