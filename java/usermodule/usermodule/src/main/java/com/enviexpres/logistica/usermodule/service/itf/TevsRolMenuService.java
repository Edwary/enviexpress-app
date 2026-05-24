package com.enviexpres.logistica.usermodule.service.itf;

import java.util.Map;
import java.util.TreeSet;

import com.enviexpres.logistica.usermodule.model.TevsRolMenu;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevsRolMenuService {

	Mono<TevsRolMenu> create(Map<String, String> entity);
	
	Mono<TevsRolMenu> findById(String idRolMenu);
	
	Flux<TevsRolMenu> findAll();
	
	Flux<Map<String, Object>> findIfContainsRol(String idRol);
	
	Mono<TevsRolMenu> toggle(Map<String, String> entity);
	
	Mono<Void> remove(String idRolMenu);
	
	Mono<TreeSet<Map<String, Object>>> getMenuTreeByRol(String idRol);
	
	Flux<Map<String, Object>> findIfContains(Map<String, String> where);
	
}
