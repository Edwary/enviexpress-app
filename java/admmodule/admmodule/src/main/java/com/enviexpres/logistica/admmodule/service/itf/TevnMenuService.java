package com.enviexpres.logistica.admmodule.service.itf;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.enviexpres.logistica.admmodule.model.TevnMenu;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TevnMenuService {

	Mono<TevnMenu> create(Map<String, Object> entity);
	
	Mono<TevnMenu> findById(String id);
	
	Flux<TevnMenu> findAll();
	
	Mono<Void> remove(String id);
	
	Flux<TevnMenu> createVarious(List<Map<String, Object>> TevnMenuList);
	
	Flux<TreeSet<Map<String, Object>>> menuTree(); 
	
	Flux<TreeSet<Map<String, Object>>> findIfContains(Map<String, String> filter);
	
	Mono<TevnMenu> toggle(Map<String, Object> entity);
}
