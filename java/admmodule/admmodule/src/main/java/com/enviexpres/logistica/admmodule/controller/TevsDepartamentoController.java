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

import com.enviexpres.logistica.admmodule.model.TevsDepartamento;
import com.enviexpres.logistica.admmodule.service.itf.TevsDepartamentoService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class TevsDepartamentoController {

	@Autowired
	private TevsDepartamentoService tevsDepartamentoService;
	
	@PostMapping("/departamento/create")
	public Mono<ResponseEntity<Map<String, Object>>> crearPais(@Valid @RequestBody Map<String, Object> entity){
		if(entity.isEmpty()) {
			
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NO_CONTENT, null), HttpStatus.NO_CONTENT));
		}else if((!Objects.isNull(entity.get("idDepartamento")) || String.valueOf(entity.get("idDepartamento")).isEmpty()) && (!Objects.isNull(entity.get("nmDepartamento")) || String.valueOf(entity.get("idDepartamento")).isEmpty()) && (!Objects.isNull(entity.get("sbDepartamento"))  || String.valueOf(entity.get("idDepartamento")).isEmpty())) {
			TevsDepartamento tevsDepartamentoAux = tevsDepartamentoService.findById(String.valueOf(entity.get("idPais"))).block();
			if(!Objects.isNull(tevsDepartamentoAux)) {
				TevsDepartamento tevsDepartamento = tevsDepartamentoService.create(entity).block();
				if(Objects.isNull(tevsDepartamento)) {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
				} else {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevsDepartamento), HttpStatus.CREATED));
				}
			} else  {
				TevsDepartamento tevsDepartamento = tevsDepartamentoService.create(entity).block();
				if(Objects.isNull(tevsDepartamento)) {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
				} else {
					return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevsDepartamento), HttpStatus.CREATED));
				}
			}
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null),HttpStatus.I_AM_A_TEAPOT));
		}
	}
	
	@PostMapping("/departamento/createVarious")
	public Flux<ResponseEntity<List<TevsDepartamento>>> createVarios(@Valid @RequestBody List<Map<String, Object>> entityList){
		List<TevsDepartamento> tevsDepartamentoList = tevsDepartamentoService.createVarious(entityList).collectList().block();
		if(Objects.isNull(tevsDepartamentoList)) {
			return Flux.just(new ResponseEntity<List<TevsDepartamento>>(HttpStatus.NOT_FOUND));
		} else {
			return Flux.just(new ResponseEntity<List<TevsDepartamento>>(tevsDepartamentoList ,HttpStatus.OK));
		}
	}
	
	@PostMapping("/departamento/contains")
	public Flux<ResponseEntity<List<Map<String, Object>>>> findIfContains(@Valid @RequestBody Map<String, String> filter){
		List<Map<String, Object>> tevsDepartamentoList = tevsDepartamentoService.findIfContains(filter).collectList().block();
		if(Objects.isNull(tevsDepartamentoList)) {
			return Flux.just(new ResponseEntity<List<Map<String, Object>>>(HttpStatus.NOT_FOUND));
		} else {
			return Flux.just(new ResponseEntity<List<Map<String, Object>>>(tevsDepartamentoList ,HttpStatus.OK));
		}
	}
	
	@DeleteMapping("/departamento/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> deletePais(@PathVariable(value = "id") String id){
		Object response = tevsDepartamentoService.logicRemove(id).block();
		if(Objects.isNull(response)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, response), HttpStatus.OK));
		}
	}
	
	@PostMapping("/departamento/toggle/{id}")
	public Mono<ResponseEntity<Map<String, Object>>> togglePais(@PathVariable(value = "id") String id){
		TevsDepartamento tevsDepartamento = tevsDepartamentoService.toggleDepartamento(id).block();
		if(Objects.isNull(tevsDepartamento)) {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
		} else {
			return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevsDepartamento), HttpStatus.OK));
		}
	}
}
