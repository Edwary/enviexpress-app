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

import com.enviexpres.logistica.admmodule.model.TevpPais;
import com.enviexpres.logistica.admmodule.service.itf.TevpPaisService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class TevpPaisController {

    @Autowired
    private TevpPaisService tevpPaisService;

    @PostMapping("/pais/create")
    public Mono<ResponseEntity<Map<String, Object>>> crearPais(@Valid @RequestBody Map<String, Object> entity){
        if(entity.isEmpty()) return Mono.just(ResponseEntity.noContent().build());

        String id = String.valueOf(entity.get("idPais"));
        return tevpPaisService.findById(id)
            .flatMap(aux -> tevpPaisService.create(entity)) // Ya existe, actualiza
            .switchIfEmpty(tevpPaisService.create(entity))  // No existe, crea
            .map(pais -> ResponseEntity.status(HttpStatus.CREATED).body(UtilConverter.apiResponse(HttpStatus.CREATED, pais)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.CONFLICT).body(UtilConverter.apiResponse(HttpStatus.CONFLICT, null)));
    }

    @PostMapping("/pais/createVarious")
    public Mono<ResponseEntity<List<TevpPais>>> crearPaises(@Valid @RequestBody List<Map<String, Object>> entityList){
        return tevpPaisService.createVarious(entityList).collectList()
            .map(list -> list.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(list));
    }

    @PostMapping("/pais/contains")
    public Mono<ResponseEntity<List<Map<String, Object>>>> findAll(@Valid @RequestBody Map<String, String> filter){
        return tevpPaisService.findIfContains(filter).collectList()
            .map(list -> ResponseEntity.ok(list))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/pais/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deletePais(@PathVariable(value = "id") String id){
        return tevpPaisService.logicRemove(id)
            .map(res -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, res)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }

    @PostMapping("/pais/toggle/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> togglePais(@PathVariable(value = "id") String id){
        return tevpPaisService.togglePais(id)
            .map(pais -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, pais)))
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }
}