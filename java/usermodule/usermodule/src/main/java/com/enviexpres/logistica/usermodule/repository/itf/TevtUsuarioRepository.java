package com.enviexpres.logistica.usermodule.repository.itf;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevtUsuario;

import reactor.core.publisher.Mono;

@Repository
public interface TevtUsuarioRepository extends ReactiveMongoRepository<TevtUsuario, String>{

	Mono<TevtUsuario> findTopByOrderByNusDesc();

	@Query("{ 'nus' : ?0 }")
	Mono<TevtUsuario> findByNus(String nus);
	
	@Query("{ 'nmUsuario' : ?0 }")
	Mono<TevtUsuario> findByNmUsuario(String nmUsuario);
	
	@Query("{ 'email' : ?0 }")
	Mono<TevtUsuario> findByEmailUsuario(String emailUsuario);
	
	Mono<TevtUsuario> findByNmUsuarioOrEmail(String nmUsuario, String email);
}
