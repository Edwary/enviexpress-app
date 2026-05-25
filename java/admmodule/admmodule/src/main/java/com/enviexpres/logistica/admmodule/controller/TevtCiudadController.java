package com.enviexpres.logistica.admmodule.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.admmodule.model.TevtCiudad;
import com.enviexpres.logistica.admmodule.service.itf.TevtCiudadService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class TevtCiudadController {

    @Autowired
    private TevtCiudadService tevtCiudadService;

    @PostMapping("/ciudad/create")
    public Mono<ResponseEntity<Map<String, Object>>> crearCiudad(@Valid @RequestBody Map<String, Object> entity) {
        if (entity.isEmpty()) {
            return Mono.just(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.NO_CONTENT, null), HttpStatus.NO_CONTENT));
        } else if ((!Objects.isNull(entity.get("idCiudad")) || String.valueOf(entity.get("idCiudad")).isEmpty()) 
                && (!Objects.isNull(entity.get("nmCiudad")) || String.valueOf(entity.get("idCiudad")).isEmpty()) 
                && (!Objects.isNull(entity.get("sbCiudad")) || String.valueOf(entity.get("idCiudad")).isEmpty())) {
            
            return tevtCiudadService.findById(String.valueOf(entity.get("idCiudad")))
                    .flatMap(aux -> tevtCiudadService.create(entity))
                    .switchIfEmpty(tevtCiudadService.create(entity))
                    .map(ciudad -> new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.CREATED, ciudad), HttpStatus.CREATED))
                    .defaultIfEmpty(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.CONFLICT, null), HttpStatus.CONFLICT));
        } else {
            return Mono.just(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
        }
    }

    @PostMapping("/ciudad/createVarious")
    public Mono<ResponseEntity<List<TevtCiudad>>> crearCiudades(@Valid @RequestBody List<Map<String, Object>> entityList) {
        return tevtCiudadService.createVarious(entityList)
                .collectList()
                .map(list -> {
                    if (list.isEmpty()) {
                        return new ResponseEntity<List<TevtCiudad>>(HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<>(list, HttpStatus.OK);
                });
    }

    @PostMapping("/ciudad/contains")
    public Mono<ResponseEntity<Map<String, Object>>> getCiudadContains(@Valid @RequestBody Map<String, String> filter) {
        return tevtCiudadService.findIfContains(filter)
                .collectList()
                .map(list -> {
                    return new ResponseEntity<>(
                        UtilConverter.apiResponse(HttpStatus.OK, list), 
                        HttpStatus.OK
                    );
                }).defaultIfEmpty(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null),HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/ciudad/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteCiudad(@PathVariable(value = "id") String id) {
        return tevtCiudadService.logicRemove(id)
                .map(response -> new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.OK, response), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
    }

    @PostMapping("/ciudad/toggle/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> toggleCiudad(@PathVariable(value = "id") String id) {
        return tevtCiudadService.toggleCiudad(id)
                .map(ciudad -> new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.OK, ciudad), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null), HttpStatus.I_AM_A_TEAPOT));
    }

    @GetMapping("/ciudad/actualizarApi")
    public Mono<ResponseEntity<List<TevtCiudad>>> actualizarCiudades() {
        return tevtCiudadService.actualizarCiudades()
                .collectList()
                .map(list -> {
                    if (list.isEmpty()) {
                        return new ResponseEntity<List<TevtCiudad>>(HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<>(list, HttpStatus.OK);
                });
    }
}