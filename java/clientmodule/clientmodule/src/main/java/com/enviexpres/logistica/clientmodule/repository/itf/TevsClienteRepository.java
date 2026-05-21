package com.enviexpres.logistica.clientmodule.repository.itf;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.clientmodule.model.TevsCliente;

import reactor.core.publisher.Mono;

@Repository
public interface TevsClienteRepository extends ReactiveMongoRepository<TevsCliente, String>{

	Mono<TevsCliente> findTopByOrderByIdClienteDesc();
	
	@Query("{ 'idCliente' : ?0 }")
	Mono<TevsCliente> findByIdClient(String idCliente);
}
