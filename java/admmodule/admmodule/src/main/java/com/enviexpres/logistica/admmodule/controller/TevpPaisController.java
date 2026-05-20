package com.enviexpres.logistica.admmodule.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.enviexpres.logistica.admmodule.model.TevpPais;
import com.enviexpres.logistica.admmodule.service.itf.TevpPaisService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class TevpPaisController {

	@Autowired
	private TevpPaisService tevpPaisService;
	
	@PostMapping("/pais/create")
	public Mono<ResponseEntity<Map<String, Object>>> crearPais(@Valid @RequestBody Map<String, Object> entity){
		if(entity.isEmpty()) {
			
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NO_CONTENT, null), HttpStatus.NO_CONTENT));
		}else if((!Objects.isNull(entity.get("idPais")) || String.valueOf(entity.get("idPais")).isEmpty()) && (!Objects.isNull(entity.get("nmPais")) || String.valueOf(entity.get("idPais")).isEmpty()) && (!Objects.isNull(entity.get("sbPais"))  || String.valueOf(entity.get("idPais")).isEmpty())) {
			TevpPais tevpPaisAux = tevpPaisService.findById(String.valueOf(entity.get("idPais"))).block();
			if(!Objects.isNull(tevpPaisAux)) {
				TevpPais tevpPais = tevpPaisService.create(entity).block();
//				return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.FOUND, tevpPaisAux), HttpStatus.FOUND));
				if(Objects.isNull(tevpPais)) {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
				} else {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevpPais), HttpStatus.CREATED));
				}
			} else  {
				TevpPais tevpPais = tevpPaisService.create(entity).block();
				if(Objects.isNull(tevpPais)) {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
				} else {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevpPais), HttpStatus.CREATED));
				}
			}
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null),HttpStatus.I_AM_A_TEAPOT));
		}
	}
	
	@PostMapping("/pais/createVarious")
	public Flux<ResponseEntity<List<TevpPais>>> crearPaises(@Valid @RequestBody List<Map<String, Object>> entityList){
		List<TevpPais> tevpPaisList = tevpPaisService.createVarious(entityList).collectList().block();
		if(Objects.isNull(tevpPaisList)) {
			return Flux.just(new ResponseEntity<List<TevpPais>>(HttpStatus.NOT_FOUND));
		} else {
			return Flux.just(new ResponseEntity<List<TevpPais>>(tevpPaisList ,HttpStatus.OK));
		}
	}
	
	@PostMapping("/pais/contains")
	public Flux<ResponseEntity<List<Map<String, Object>>>> findAll(@Valid @RequestBody Map<String, String> filter){
		List<Map<String, Object>> tevpPaisList = tevpPaisService.findIfContains(filter).collectList().block();
		if(Objects.isNull(tevpPaisList)) {
			return Flux.just(new ResponseEntity<List<Map<String, Object>>>(HttpStatus.NOT_FOUND));
		} else {
			return Flux.just(new ResponseEntity<List<Map<String, Object>>>(tevpPaisList ,HttpStatus.OK));
		}
	}
	
	@DeleteMapping("/pais/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> deletePais(@PathVariable(value = "id") String id){
		Object response = tevpPaisService.logicRemove(id).block();
		if(Objects.isNull(response)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, response), HttpStatus.OK));
		}
	}
	
	@PostMapping("/pais/toggle/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> togglePais(@PathVariable(value = "id") String id){
		TevpPais tevpPais = tevpPaisService.togglePais(id).block();
		if(Objects.isNull(tevpPais)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevpPais), HttpStatus.OK));
		}
	}
}
