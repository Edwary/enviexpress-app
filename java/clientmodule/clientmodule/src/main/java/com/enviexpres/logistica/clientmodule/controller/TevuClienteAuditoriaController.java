package com.enviexpres.logistica.clientmodule.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.clientmodule.model.TevuClienteAuditoria;
import com.enviexpres.logistica.clientmodule.service.itf.TevuClienteAuditoriaService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevuClienteAuditoriaController {

	@Autowired
	private TevuClienteAuditoriaService TevuClienteAuditoriaService;
	
	@GetMapping("/auditoria/registros")
	public Flux<TevuClienteAuditoria> getAllAuditoria(){
		return TevuClienteAuditoriaService.findAll();
	}
	
	@PostMapping("/auditoria/create")
	public Mono<TevuClienteAuditoria> createAuditoria(@Valid @RequestBody Map<String, Object> entity){
		return TevuClienteAuditoriaService.create(entity);
	}
}
