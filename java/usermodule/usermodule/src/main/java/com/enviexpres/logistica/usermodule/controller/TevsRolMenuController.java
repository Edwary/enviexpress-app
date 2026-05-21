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

import com.enviexpres.logistica.usermodule.model.TevsRolMenu;
import com.enviexpres.logistica.usermodule.service.itf.TevsRolMenuService;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevsRolMenuController {

	@Autowired
	private TevsRolMenuService tevsRolMenuService;
	
	@PostMapping("/rolMenu/create")
	public Mono<ResponseEntity<Map<String, Object>>> createUpdaterolMenu(@Valid @RequestBody Map<String, String> entity) {
		return tevsRolMenuService.create(entity)
				.flatMap(tevsRolMenu -> {
					if(Objects.isNull(tevsRolMenu)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevsRolMenu), HttpStatus.CREATED));
					}
				});
	}
	
	@GetMapping("/rolMenu/findById/{idRolMenu}")
	public Mono<ResponseEntity<Map<String, Object>>> findById(@PathVariable(value="idRolMenu") String idrolMenu) {
		return tevsRolMenuService.findById(idrolMenu)
				.flatMap(tevsRolMenu -> {
					if(Objects.isNull(tevsRolMenu)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevsRolMenu), HttpStatus.OK));
					}
				});
	}
	
	@GetMapping("/rolMenu/findAll")
	public Flux<TevsRolMenu> findAllrolMenu() {
		return tevsRolMenuService.findAll();
	}
	
	@PutMapping("/rolMenu/toggle")
	public Mono<ResponseEntity<Map<String, Object>>> togglerolMenu(@Valid @RequestBody Map<String, String> entity) {
		return tevsRolMenuService.toggle(entity)
				.flatMap(tevsRolMenu -> {
					if(Objects.isNull(tevsRolMenu)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevsRolMenu), HttpStatus.OK));
					}
				});
	}
}
