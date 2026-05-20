package com.enviexpres.logistica.admmodule.service.itf;

import com.enviexpres.logistica.admmodule.model.TevnError;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnErrorService {

	Mono<TevnError> create(TevnError tvvnError);
	
	Flux<TevnError> findAll();
	
	Mono<TevnError> finById(String id);
}
