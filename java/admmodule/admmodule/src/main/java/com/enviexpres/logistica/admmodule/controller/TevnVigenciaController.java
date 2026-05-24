package com.enviexpres.logistica.admmodule.controller;

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

import com.enviexpres.logistica.admmodule.model.TevnVigencia;
import com.enviexpres.logistica.admmodule.service.itf.TevnVigenciaService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevnVigenciaController {

    @Autowired
    private TevnVigenciaService tevnVigenciaService;

    @PostMapping("/vigencia/create")
    public Mono<ResponseEntity<Map<String, Object>>> createUpdate(@Valid @RequestBody Map<String, Object> entity) {
        return tevnVigenciaService.create(entity)
            .map(vig -> ResponseEntity.status(HttpStatus.CREATED).body(UtilConverter.apiResponse(HttpStatus.CREATED, vig)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.CONFLICT).body(UtilConverter.apiResponse(HttpStatus.CONFLICT, null)));
    }

    @GetMapping("/vigencia/{idVigencia}")
    public Mono<ResponseEntity<Map<String, Object>>> findById(@PathVariable("idVigencia") String idVigencia) {
        return tevnVigenciaService.findById(idVigencia)
            .map(vig -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, vig)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null)));
    }

    @GetMapping("/vigencia/all")
    public Flux<TevnVigencia> findAllVigencia() {
        return tevnVigenciaService.findAll();
    }

    @PutMapping("/vigencia/toggle")
    public Mono<ResponseEntity<Map<String, Object>>> toggle(@Valid @RequestBody Map<String, Object> entity) {
        return tevnVigenciaService.toggle(entity)
            .map(vig -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, vig)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.CONFLICT).body(UtilConverter.apiResponse(HttpStatus.CONFLICT, null)));
    }
}