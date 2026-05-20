package com.enviexpres.logistica.admmodule.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.admmodule.model.TevuAdminAuditoria;
import com.enviexpres.logistica.admmodule.service.itf.TevuAdminAuditoriaService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevuAdminAuditoriaController {

	@Autowired
	private TevuAdminAuditoriaService tevuAdminAuditoriaService;
	
	@GetMapping("/auditoria/registros")
	public Flux<TevuAdminAuditoria> getAllAuditoria(){
		return tevuAdminAuditoriaService.findAll();
	}
	
	@PostMapping("/auditoria/create")
	public Mono<TevuAdminAuditoria> createAuditoria(@Valid @RequestBody Map<String, Object> entity){
		return tevuAdminAuditoriaService.create(entity);
	}
}
