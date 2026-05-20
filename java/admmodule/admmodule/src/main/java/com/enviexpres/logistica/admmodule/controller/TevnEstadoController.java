package com.enviexpres.logistica.admmodule.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.admmodule.model.TevnEstado;
import com.enviexpres.logistica.admmodule.service.itf.TevnEstadoService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevnEstadoController {
	
	@Autowired
	private TevnEstadoService tevnEstadoService;
	
	@GetMapping("/estados")
	public Flux<TevnEstado> getAllEstados(){
		return tevnEstadoService.findAll();
	}

	@PostMapping("/estado/create")
	public Mono<TevnEstado> createEstado(@Valid @RequestBody TevnEstado tevnEstado){
		return tevnEstadoService.create(tevnEstado);
	}
	
	@GetMapping("/estado/{id}")
	public Mono<ResponseEntity<TevnEstado>> getEstadoById(@PathVariable(value = "id") String id){
		return tevnEstadoService.findById(id)
				.map(estado -> ResponseEntity.ok(estado))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/estado/{id}")
	public Mono<ResponseEntity<Void>> deleteEstado(@PathVariable(value = "id") String id){
		Mono<Void> monoResponse = tevnEstadoService.remove(id);
		if(!Objects.isNull(monoResponse)) {
			return Mono.just(new ResponseEntity<Void>(HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
		}
	}
	
	@PostMapping("/estado/contains")
	public Flux<ResponseEntity<List<TevnEstado>>> getIfContains(@Valid @RequestBody Map<String, String> filter){
		List<TevnEstado> tevnEstadoList = tevnEstadoService.findIfContains(filter).collectList().block();
		HttpStatus httpStatus = tevnEstadoList != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return Flux.just(new ResponseEntity<>(tevnEstadoList, httpStatus));
	}
	
	
}
