package com.enviexpres.logistica.admmodule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.admmodule.model.TevsDepartamento;
import com.enviexpres.logistica.admmodule.service.itf.TevsDepartamentoService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class TevsDepartamentoController {

    @Autowired
    private TevsDepartamentoService tevsDepartamentoService;

    @PostMapping("/departamento/create")
    public Mono<ResponseEntity<Map<String, Object>>> crearDepartamento(@Valid @RequestBody Map<String, Object> entity){
        if(entity.isEmpty()) return Mono.just(ResponseEntity.noContent().build());

        return tevsDepartamentoService.create(entity)
            .map(dep -> ResponseEntity.status(HttpStatus.CREATED).body(UtilConverter.apiResponse(HttpStatus.CREATED, dep)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.CONFLICT).body(UtilConverter.apiResponse(HttpStatus.CONFLICT, null)));
    }

    @PostMapping("/departamento/createVarious")
    public Mono<ResponseEntity<List<TevsDepartamento>>> createVarios(@Valid @RequestBody List<Map<String, Object>> entityList){
        return tevsDepartamentoService.createVarious(entityList).collectList()
            .map(list -> ResponseEntity.ok(list))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/departamento/contains")
    public Mono<ResponseEntity<List<Map<String, Object>>>> findIfContains(@Valid @RequestBody Map<String, String> filter){
        return tevsDepartamentoService.findIfContains(filter).collectList()
            .map(list -> ResponseEntity.ok(list))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/departamento/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteDepartamento(@PathVariable(value = "id") String id){
        return tevsDepartamentoService.logicRemove(id)
            .map(res -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, res)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }

    @PostMapping("/departamento/toggle/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> toggleDepartamento(@PathVariable(value = "id") String id){
        return tevsDepartamentoService.toggleDepartamento(id)
            .map(dep -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, dep)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }
}