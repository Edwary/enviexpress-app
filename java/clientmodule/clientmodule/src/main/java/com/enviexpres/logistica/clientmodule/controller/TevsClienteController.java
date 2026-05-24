package com.enviexpres.logistica.clientmodule.controller;

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

import com.enviexpres.logistica.clientmodule.model.TevsCliente;
import com.enviexpres.logistica.clientmodule.service.itf.TevsClienteService;
import com.enviexpres.logistica.clientmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevsClienteController {

	@Autowired
	private TevsClienteService tevsClienteService;
	
	@PostMapping("/cliente/create")
	public Mono<ResponseEntity<Map<String, Object>>> createUpdateRol(@Valid @RequestBody Map<String, String> entity) {
		return tevsClienteService.create(entity)
				.flatMap(tevsCliente -> {
					if(Objects.isNull(tevsCliente)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.CREATED, tevsCliente), HttpStatus.CREATED));
					}
				});
	}
	
	@GetMapping("/cliente/findById/{idCliente}")
	public Mono<ResponseEntity<Map<String, Object>>> findById(@PathVariable(value="idCliente") String idCliente) {
		return tevsClienteService.findById(idCliente)
				.flatMap(tevsCliente -> {
					if(Objects.isNull(tevsCliente)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevsCliente), HttpStatus.OK));
					}
				});
	}
	
	@GetMapping("/cliente/findAll")
	public Flux<TevsCliente> findAllRol() {
		return tevsClienteService.findAll();
	}
	
	@PutMapping("/cliente/toggle")
	public Mono<ResponseEntity<Map<String, Object>>> toggleRol(@Valid @RequestBody Map<String, String> entity) {
		return tevsClienteService.toggle(entity)
				.flatMap(tevsCliente -> {
					if(Objects.isNull(tevsCliente)) {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(new ResponseEntity<Map<String, Object>>(UtilConverter.apiResponse(HttpStatus.OK, tevsCliente), HttpStatus.OK));
					}
				});
	}
	
	@PostMapping("/cliente/findIfContains")
    public Mono<ResponseEntity<Map<String, Object>>> findIfContains(@RequestBody Map<String, String> filter) {
        
        return tevsClienteService.findIfContains(filter)
                .collectList()
                .map(listaClientes -> {
                    if (listaClientes.isEmpty()) {
                        return new ResponseEntity<>(
                            UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), 
                            HttpStatus.NOT_FOUND
                        );
                    }
                    return new ResponseEntity<>(
                        UtilConverter.apiResponse(HttpStatus.OK, listaClientes), 
                        HttpStatus.OK
                    );
                });
    }
}
