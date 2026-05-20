package com.enviexpres.logistica.admmodule.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.admmodule.model.TevnVariableSistema;
import com.enviexpres.logistica.admmodule.service.itf.TevnVariableSistemaService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevnVariableSistemaController {

	@Autowired
	private TevnVariableSistemaService tevnVariableSistemaService;
	
	@PostMapping("/variable/create")
	public Mono<ResponseEntity<Map<String, Object>>> createVariable(@Valid @RequestBody Map<String, String> entity){
		TevnVariableSistema tevnVariableSistema = tevnVariableSistemaService.create(entity).block();
		if(Objects.isNull(tevnVariableSistema)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevnVariableSistema), HttpStatus.OK));
		}
	}
	
	@PostMapping("/variable/contains")
	public Flux<ResponseEntity<List<Map<String, Object>>>> findIfContains(@Valid @RequestBody Map<String, String> entity){
		List<Map<String, Object>> tevnVariableSistemaList = tevnVariableSistemaService.findIfContains(entity).collectList().block();
		if(Objects.isNull(tevnVariableSistemaList)) {
			return Flux.just(new ResponseEntity<List<Map<String, Object>>>(HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Flux.just(new ResponseEntity<List<Map<String, Object>>>(tevnVariableSistemaList, HttpStatus.OK));
		}
	}
	
	@PostMapping("/variable/createVarious")
	public Flux<ResponseEntity<List<TevnVariableSistema>>> createVarious(@Valid @RequestBody List<Map<String, Object>> entityList){
		List<TevnVariableSistema> tevnVariableSistemaList = tevnVariableSistemaService.createVarious(entityList).collectList().block();
		if(Objects.isNull(tevnVariableSistemaList)) {
			return Flux.just(new ResponseEntity<List<TevnVariableSistema>>(HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Flux.just(new ResponseEntity<List<TevnVariableSistema>>(tevnVariableSistemaList, HttpStatus.OK));
		}
	}
	
	@PostMapping("/variable/toggle")
	public Mono<ResponseEntity<Map<String, Object>>> toggleVariable(@Valid @RequestBody Map<String, String> entity){
		TevnVariableSistema tevnVariableSistema = tevnVariableSistemaService.toggle(entity).block();
		if(Objects.isNull(tevnVariableSistema)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevnVariableSistema), HttpStatus.OK));
		}
	}
	
	@PostMapping("/variable/delete/{idVariable}")
	public Mono<ResponseEntity<Map<String, Object>>> deleteVariable(@PathVariable( value = "idVariable" ) String idVariable){
		TevnVariableSistema tevnVariableSistema = tevnVariableSistemaService.logicRemove(idVariable).block();
		if(Objects.isNull(tevnVariableSistema)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevnVariableSistema), HttpStatus.OK));
		}
	}
}
