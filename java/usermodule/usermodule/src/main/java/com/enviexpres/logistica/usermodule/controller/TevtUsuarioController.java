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

import com.enviexpres.logistica.usermodule.model.TevtUsuario;
import com.enviexpres.logistica.usermodule.service.itf.TevtUsuarioService;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevtUsuarioController {

	@Autowired
	private TevtUsuarioService tevtUsuarioService;
	
	@PostMapping("/usuario/create")
	public Mono<ResponseEntity<Map<String, Object>>> createUpdateusuario(@Valid @RequestBody Map<String, String> entity) {
		return tevtUsuarioService.create(entity)
				.flatMap(tevtUsuario -> {
					if(Objects.isNull(tevtUsuario)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevtUsuario), HttpStatus.CREATED));
					}
				});
	}
	
	@GetMapping("/usuario/findById/{idUsuario}")
	public Mono<ResponseEntity<Map<String, Object>>> findById(@PathVariable(value="idUsuario") String idUsuario) {
		return tevtUsuarioService.findById(idUsuario)
				.flatMap(tevtUsuario -> {
					if(Objects.isNull(tevtUsuario)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevtUsuario), HttpStatus.OK));
					}
				});
	}
	
	@GetMapping("/usuario/findAll")
	public Flux<TevtUsuario> findAllusuario() {
		return tevtUsuarioService.findAll();
	}
	
	@PutMapping("/usuario/toggle")
	public Mono<ResponseEntity<Map<String, Object>>> toggleusuario(@Valid @RequestBody Map<String, String> entity) {
		return tevtUsuarioService.toggle(entity)
				.flatMap(tevtUsuario -> {
					if(Objects.isNull(tevtUsuario)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevtUsuario), HttpStatus.OK));
					}
				});
	}
	
	@PostMapping("/usuario/login")
	public Mono<ResponseEntity<Map<String, Object>>> login(@Valid @RequestBody Map<String, String> entity) {
		return tevtUsuarioService.login(entity)
				.flatMap(tevtUsuario -> {
					if(Objects.isNull(tevtUsuario)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevtUsuario), HttpStatus.OK));
					}
				});
	}
}
