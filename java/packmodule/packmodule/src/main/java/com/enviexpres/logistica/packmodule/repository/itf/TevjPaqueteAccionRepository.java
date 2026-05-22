package com.enviexpres.logistica.packmodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.packmodule.model.TevjPaqueteAccion;

import reactor.core.publisher.Mono;

@Repository
public interface TevjPaqueteAccionRepository extends ReactiveMongoRepository<TevjPaqueteAccion, String>{

	Mono<TevjPaqueteAccion> findTopByOrderByIdPaqueteAccionDesc();
	
}
