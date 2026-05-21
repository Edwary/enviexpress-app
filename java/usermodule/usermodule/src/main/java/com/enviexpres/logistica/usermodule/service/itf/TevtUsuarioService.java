package com.enviexpres.logistica.usermodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.usermodule.model.TevtUsuario;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevtUsuarioService {
	Mono<TevtUsuario> create(Map<String, String> entity);
	
	Mono<TevtUsuario> findById(String nus);
	
	Flux<TevtUsuario> findAll();
	
	Mono<TevtUsuario> toggle(Map<String, String> entity);
	
	Mono<Void> remove(String nus);
}
