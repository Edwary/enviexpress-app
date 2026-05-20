package com.enviexpres.logistica.admmodule.repository.itf;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevnVigencia;

import reactor.core.publisher.Mono;

@Repository
public interface TevnVigenciaRepository extends ReactiveMongoRepository<TevnVigencia, String> {

	@Query(" 'idVigencia' : ?0 ")
	Mono<TevnVigencia> findByIdVigencia(String idVigencia);
	
	
}
