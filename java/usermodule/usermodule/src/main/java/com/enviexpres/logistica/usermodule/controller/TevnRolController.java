package com.enviexpres.logistica.usermodule.controller;

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

import com.enviexpres.logistica.usermodule.model.TevnRol;
import com.enviexpres.logistica.usermodule.service.itf.TevnRolService;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevnRolController {

	@Autowired
	private TevnRolService tevnRolService;
	
	@PostMapping("/rol/create")
	public Mono<ResponseEntity<Map<String, Object>>> createUpdateRol(@RequestBody Map<String, String> entity) {
		return tevnRolService.create(entity)
				.flatMap(tevnRol -> {
					if(Objects.isNull(tevnRol)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevnRol), HttpStatus.CREATED));
					}
				});
	}
	
	@GetMapping("/rol/findById/{idRol}")
	public Mono<ResponseEntity<Map<String, Object>>> findById(@PathVariable(value="idRol") String idRol) {
		return tevnRolService.findById(idRol)
				.flatMap(tevnRol -> {
					if(Objects.isNull(tevnRol)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevnRol), HttpStatus.OK));
					}
				});
	}
	
	@GetMapping("/rol/findAll")
	public Flux<TevnRol> findAllRol() {
		return tevnRolService.findAll();
	}
	
	@PutMapping("/rol/toggle")
	public Mono<ResponseEntity<Map<String, Object>>> toggleRol(@Valid @RequestBody Map<String, String> entity) {
		return tevnRolService.toggle(entity)
				.flatMap(tevnRol -> {
					if(Objects.isNull(tevnRol)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevnRol), HttpStatus.OK));
					}
				});
	}
}
