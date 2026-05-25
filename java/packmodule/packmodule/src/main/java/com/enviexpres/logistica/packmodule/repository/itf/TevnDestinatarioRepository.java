package com.enviexpres.logistica.packmodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.packmodule.model.TevnDestinatario;

import reactor.core.publisher.Mono;

@Repository
public interface TevnDestinatarioRepository extends ReactiveMongoRepository<TevnDestinatario, String>{

	Mono<TevnDestinatario> findByDocumento(String documento);
	
	Mono<TevnDestinatario> findTopByOrderByIdDestinatarioDesc();
	
}
