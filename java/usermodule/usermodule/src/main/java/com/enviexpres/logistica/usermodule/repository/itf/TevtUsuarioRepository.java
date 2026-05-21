package com.enviexpres.logistica.usermodule.repository.itf;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevtUsuario;

import reactor.core.publisher.Mono;

@Repository
public interface TevtUsuarioRepository extends ReactiveMongoRepository<TevtUsuario, String>{

	Mono<TevtUsuario> findTopByOrderByIdUsuarioDesc();

	@Query("{ 'nus' : ?0 }")
	Mono<TevtUsuario> findByNus(String nus);
	
}
