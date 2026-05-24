package com.enviexpres.logistica.admmodule.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enviexpres.logistica.admmodule.model.TevnMenu;
import com.enviexpres.logistica.admmodule.service.itf.TevnMenuService;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TevnMenuController {

	@Autowired
    private TevnMenuService tevnMenuService;
    
    @PostMapping("/menu/create")
    public Mono<ResponseEntity<Map<String, Object>>> createMenu(@Valid @RequestBody Map<String, Object> entity){
        return tevnMenuService.create(entity)
                .map(menu -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, menu)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }
    
    @GetMapping("/menu/{id}")
    public Mono<ResponseEntity<TevnMenu>> getMenuById(@PathVariable(value = "id") String id){
        return tevnMenuService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/menus")
    public Flux<TevnMenu> getMenus(){
        return tevnMenuService.findAll();
    }
    
    @DeleteMapping("/menu/{id}")
    public Mono<ResponseEntity<Void>> deleteMenu(@PathVariable(value = "id") String id){
        return tevnMenuService.remove(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping("/createMenus")
    public Mono<ResponseEntity<List<TevnMenu>>> createVariousMenu(@Valid @RequestBody List<Map<String, Object>> entityList){
        return tevnMenuService.createVarious(entityList)
                .collectList()
                .map(list -> list.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(list));
    }
    
    @GetMapping("/menu/tree")
    public Flux<TreeSet<Map<String,Object>>> treeSet(){
        return tevnMenuService.menuTree();
    }
    
    @PostMapping("/menu/contains")
    public Mono<ResponseEntity<TreeSet<Map<String,Object>>>> getMenuContains(@Valid @RequestBody Map<String, String> filter) {
        return tevnMenuService.findIfContains(filter)
                .next()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/menu/toggle")
    public Mono<ResponseEntity<Map<String, Object>>> toggle(@Valid @RequestBody Map<String, Object> entity){
        return tevnMenuService.toggle(entity)
                .map(menu -> ResponseEntity.ok(UtilConverter.apiResponse(HttpStatus.OK, menu)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(UtilConverter.apiResponse(HttpStatus.I_AM_A_TEAPOT, null)));
    }
    
    @PostMapping("/menu/findIfContains")
    public Mono<ResponseEntity<Map<String, Object>>> findMenuIfContains(@RequestBody Map<String, String> filter) {
        
        return tevnMenuService.findMenuIfContains(filter)
                .collectList()
                .map(listaMenus -> {
                    if (listaMenus.isEmpty()) {
                        return new ResponseEntity<>(
                            UtilConverter.apiResponse(HttpStatus.NOT_FOUND, null), 
                            HttpStatus.NOT_FOUND
                        );
                    }
                    return new ResponseEntity<>(
                        UtilConverter.apiResponse(HttpStatus.OK, listaMenus), 
                        HttpStatus.OK
                    );
                });
    }
}
