package com.enviexpres.logistica.packmodule.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.packmodule.model.TevtPaquete;
import com.enviexpres.logistica.packmodule.service.itf.TevtPaqueteService;
import com.enviexpres.logistica.packmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevtPaqueteController {

	@Autowired
	private TevtPaqueteService tevtPaqueteService;
	
	@PostMapping("/paquete/create")
	public Mono<ResponseEntity<Map<String, Object>>> createUpdateRol(@Valid @RequestBody Map<String, String> entity) {
		return tevtPaqueteService.create(entity)
				.flatMap(tevtPaquete -> {
					if(Objects.isNull(tevtPaquete)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevtPaquete), HttpStatus.CREATED));
					}
				});
	}
	
	@GetMapping("/paquete/findById/{idPaquete}")
	public Mono<ResponseEntity<Map<String, Object>>> findById(@PathVariable(value="idPaquete") String idPaquete) {
		return tevtPaqueteService.findById(idPaquete)
				.flatMap(tevtPaquete -> {
					if(Objects.isNull(tevtPaquete)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevtPaquete), HttpStatus.OK));
					}
				});
	}
	
	@GetMapping("/paquete/findAll")
	public Flux<TevtPaquete> findAllRol() {
		return tevtPaqueteService.findAll();
	}
	
	@PutMapping("/paquete/toggle")
	public Mono<ResponseEntity<Map<String, Object>>> toggleRol(@Valid @RequestBody Map<String, String> entity) {
		return tevtPaqueteService.toggle(entity)
				.flatMap(tevtPaquete -> {
					if(Objects.isNull(tevtPaquete)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevtPaquete), HttpStatus.OK));
					}
				});
	}
	
	@GetMapping("/paquete/count/{idEstado}")
	public Mono<Long> contarPaquetesPorEstado(@PathVariable("idEstado") String idEstado) {
	    return tevtPaqueteService.findByIdEstado(idEstado);
	}
	
	@PostMapping("/paquete/findIfContains")
    public Mono<ResponseEntity<Map<String, Object>>> findMenuIfContains(@RequestBody Map<String, String> filter) {
        
        return tevtPaqueteService.findByIdContains(filter)
                .collectList()
                .map(listaPaquetes -> {
                    return new ResponseEntity<>(
                        UtilConverter.apiResponse(HttpStatus.OK, listaPaquetes), 
                        HttpStatus.OK
                    );
                }).defaultIfEmpty(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null),HttpStatus.NOT_FOUND));
    }
}
