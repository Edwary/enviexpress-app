package com.enviexpres.logistica.usermodule.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.usermodule.model.TevuUsuarioAuditoria;
import com.enviexpres.logistica.usermodule.service.itf.TevuUsuarioAuditoriaService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevuUsuarioAuditoriaController {

	@Autowired
	private TevuUsuarioAuditoriaService tevuUsuarioAuditoriaService;
	
	@GetMapping("/auditoria/registros")
	public Flux<TevuUsuarioAuditoria> getAllAuditoria(){
		return tevuUsuarioAuditoriaService.findAll();
	}
	
	@PostMapping("/auditoria/create")
	public Mono<TevuUsuarioAuditoria> createAuditoria(@Valid @RequestBody Map<String, Object> entity){
		return tevuUsuarioAuditoriaService.create(entity);
	}

}
