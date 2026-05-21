package com.enviexpres.logistica.usermodule.repository.itf;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevnRol;

import reactor.core.publisher.Mono;

@Repository
public interface TevnRolRepository extends ReactiveMongoRepository<TevnRol, String> {

	Mono<TevnRol> findTopByOrderByIdRolDesc();
	
	@Query("{ 'idRol' : ?0 }")
	Mono<TevnRol> findByIdRol(String idRol);
	
}
