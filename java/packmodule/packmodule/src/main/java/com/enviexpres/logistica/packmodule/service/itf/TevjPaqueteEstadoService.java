package com.enviexpres.logistica.packmodule.service.itf;

import java.util.Map;

import com.enviexpres.logistica.packmodule.model.TevjPaqueteEstado;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevjPaqueteEstadoService {

	Mono<TevjPaqueteEstado> create(Map<String, String> entity);
	
	Flux<Map<String, Object>> findByIdPaquete(String idPaquete);
}
