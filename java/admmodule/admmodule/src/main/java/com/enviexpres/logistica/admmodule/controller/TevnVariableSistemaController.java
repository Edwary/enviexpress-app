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
        return tevnVariableSistemaService.create(entity)
            .map(var -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, var)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }

    @PostMapping("/variable/contains")
    public Mono<ResponseEntity<List<Map<String, Object>>>> findIfContains(@Valid @RequestBody Map<String, String> entity){
        return tevnVariableSistemaService.findIfContains(entity).collectList()
            .map(list -> ResponseEntity.ok(list))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build());
    }

    @PostMapping("/variable/createVarious")
    public Mono<ResponseEntity<List<TevnVariableSistema>>> createVarious(@Valid @RequestBody List<Map<String, Object>> entityList){
        return tevnVariableSistemaService.createVarious(entityList).collectList()
            .map(list -> ResponseEntity.ok(list))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build());
    }

    @PostMapping("/variable/toggle")
    public Mono<ResponseEntity<Map<String, Object>>> toggleVariable(@Valid @RequestBody Map<String, String> entity){
        return tevnVariableSistemaService.toggle(entity)
            .map(var -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, var)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }

    @PostMapping("/variable/delete/{idVariable}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteVariable(@PathVariable(value = "idVariable") String idVariable){
        return tevnVariableSistemaService.logicRemove(idVariable)
            .map(var -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, var)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }
}