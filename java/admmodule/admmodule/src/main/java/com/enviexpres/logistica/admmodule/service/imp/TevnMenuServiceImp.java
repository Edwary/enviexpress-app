package com.enviexpres.logistica.admmodule.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.com.vimodules.admmodule.model.TvvnMenu;
import co.com.vimodules.admmodule.repository.itf.TvvnErrorRepository;
import co.com.vimodules.admmodule.repository.itf.TvvnMenuRepository;
import co.com.vimodules.admmodule.service.itf.TvvnMenuService;
import co.com.vimodules.admmodule.utils.IntComparator;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.exception.ViValidationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnMenuServiceImp implements TevnMenuService {

	@Autowired
	private TevnMenuRepository tvvnMenuRepository;
	
	@Autowired
	private TvvnErrorRepository tvvnErrorRepository;
	
	@Override
	public Mono<TvvnMenu> create(Map<String, Object> entity) {
		TvvnMenu tvvnMenu = null;
		tvvnMenu = tvvnMenuRepository.findByIdMenu(String.valueOf(entity.get("idMenu"))).block();
		if(Objects.isNull(tvvnMenu)) {
			tvvnMenu = new TvvnMenu();
		}
		tvvnMenu.setIdMenu(String.valueOf(entity.get("idMenu")));
		tvvnMenu.setNmMenu(String.valueOf(entity.get("nmMenu")));
		tvvnMenu.setIdMenuSup(String.valueOf(entity.get("idMenuSup")));
		tvvnMenu.setTipoMenu(String.valueOf(entity.get("tipoMenu")));
		tvvnMenu.setHref(String.valueOf(entity.get("href")));
		tvvnMenu.setNivel(String.valueOf(entity.get("nivel")));
		tvvnMenu.setOrden(String.valueOf(entity.get("orden")));
		tvvnMenu.setIndVisible(String.valueOf(entity.get("indVisible")));
		tvvnMenu.setIdEstado(String.valueOf(entity.get("idEstado")));
		return tvvnMenuRepository.save(tvvnMenu);
	}

	@Override
	public Mono<TvvnMenu> findById(String id) {
		return tvvnMenuRepository.findById(id);
	}

	@Override
	public Flux<TvvnMenu> findAll() {
		return tvvnMenuRepository.findAll().sort((tvvnMenu1, tvvnMenu2) -> {
			return tvvnMenu1.getOrden().compareTo(tvvnMenu2.getOrden());
		});
	}

	@Override
	public Mono<Void> remove(String id) {
		TvvnMenu tvvnMenuMono = tvvnMenuRepository.findById(id).block();
		return tvvnMenuRepository.delete(tvvnMenuMono);
	}

	@Override
	public Flux<TvvnMenu> createVarious(List<Map<String, Object>> tvvnMenuList) {
		
		if(Objects.isNull(tvvnMenuList)) {
			return null;
		}
		
		Iterable<TvvnMenu> tvvnMenuIterable = tvvnMenuList.stream()
				.map(tvvnMenuMap -> {
					TvvnMenu tvvnMenu = new TvvnMenu();
					tvvnMenu.setIdMenu(String.valueOf(tvvnMenuMap.get("idMenu")));
					tvvnMenu.setNmMenu(String.valueOf(tvvnMenuMap.get("nmMenu")));
					tvvnMenu.setIdMenuSup(String.valueOf(tvvnMenuMap.get("idMenuSup")));
					tvvnMenu.setTipoMenu(String.valueOf(tvvnMenuMap.get("tipoMenu")));
					tvvnMenu.setHref(String.valueOf(tvvnMenuMap.get("href")));
					tvvnMenu.setNivel(String.valueOf(tvvnMenuMap.get("nivel")));
					tvvnMenu.setOrden(String.valueOf(tvvnMenuMap.get("orden")));
					tvvnMenu.setIndVisible(String.valueOf(tvvnMenuMap.get("indVisible")));
					tvvnMenu.setIdEstado(String.valueOf(tvvnMenuMap.get("idEstado")));
					return tvvnMenu;
				})
				.collect(Collectors.toList());
		
		return tvvnMenuRepository.saveAll(tvvnMenuIterable);
	}

	@Override
	public Flux<TreeSet<Map<String, Object>>> menuTree() {
		Flux<TvvnMenu> tvvnMenuFlux =  tvvnMenuRepository.findAllVisible(ViConstant.IND_VISIBLE_S).sort((tvvnMenu1, tvvnMenu2) -> {
			return tvvnMenu1.getOrden().compareTo(tvvnMenu2.getOrden());
		});
		List<TvvnMenu> tvvnMenuList = tvvnMenuFlux.collectList().block();
		TreeSet<Map<String, Object>> menu = new TreeSet<>(new IntComparator("orden"));
		for(TvvnMenu tvvnMenu : tvvnMenuList) {
			Map<String, Object> menuPadre = new HashMap<>();
			
			//if(tvvnMenu.getIndVisible().equals(ViConstant.IND_VISIBLE_N)) continue;
			
			if(tvvnMenu.getIdMenu().equals(tvvnMenu.getIdMenuSup())) {
				menuPadre.put("id", tvvnMenu.getIdMenu());
				menuPadre.put("name", tvvnMenu.getNmMenu());
				menuPadre.put("sup", tvvnMenu.getIdMenuSup());
				menuPadre.put("tipo", tvvnMenu.getTipoMenu());
				menuPadre.put("href", tvvnMenu.getHref());
				menuPadre.put("nivel",  Integer.valueOf(tvvnMenu.getNivel()));
				menuPadre.put("orden", tvvnMenu.getOrden());
				menuPadre.put("visible", tvvnMenu.getIndVisible());
				menuPadre.put("estado", tvvnMenu.getIdEstado());
				
				List<TvvnMenu> tvvnMenuSubList = tvvnMenuList
						.stream()
						.filter(tm -> tm.getIdMenuSup().equals(tvvnMenu.getIdMenu()))
						.collect(Collectors.toList());
				if(tvvnMenuSubList.size() > 0) {
					menuPadre.put("children", buscarHijosMenu(tvvnMenu, tvvnMenuSubList, tvvnMenuList));
				}else {
					menuPadre.put("children", "");
				}
				
				menu.add(menuPadre);
			}
		}
		return Flux.just(menu);
	}
	
	public TreeSet<Map<String, Object>> buscarHijosMenu(TvvnMenu tvvnMenu, List<TvvnMenu> tvvnMenuHijos, List<TvvnMenu> tvvnMenuList){
		TreeSet<Map<String, Object>> hijoTree = new TreeSet<>(new IntComparator("orden"));
		
		for(TvvnMenu tvvnMenuHijo : tvvnMenuHijos) {
			
			if(tvvnMenuHijo.getIdMenu().equals(tvvnMenu.getIdMenuSup())) continue;
			
			Map<String, Object> menuHijo = new HashMap<>();
			menuHijo.put("id", tvvnMenuHijo.getIdMenu());
			menuHijo.put("name", tvvnMenuHijo.getNmMenu());
			menuHijo.put("sup", tvvnMenuHijo.getIdMenuSup());
			menuHijo.put("tipo", tvvnMenuHijo.getTipoMenu());
			menuHijo.put("href", tvvnMenuHijo.getHref());
			menuHijo.put("nivel", Integer.valueOf(tvvnMenuHijo.getNivel()));
			menuHijo.put("orden", tvvnMenuHijo.getOrden());
			menuHijo.put("visible", tvvnMenuHijo.getIndVisible());
			menuHijo.put("estado", tvvnMenuHijo.getIdEstado());
			
			List<TvvnMenu> tvvnMenuSubList = tvvnMenuList
					.stream()
					.filter(tm -> tm.getIdMenuSup().equals(tvvnMenuHijo.getIdMenu()))
					.collect(Collectors.toList());
			if(tvvnMenuSubList.size() > 0) {
				menuHijo.put("children", buscarHijosMenu(tvvnMenu, tvvnMenuSubList, tvvnMenuList));
			}else {
				menuHijo.put("children", "");
			}
			
			hijoTree.add(menuHijo);
		}
		
		return hijoTree;
	}

	@Override
	public Flux<TreeSet<Map<String, Object>>> findIfContains(Map<String, String> filter) {
		TreeSet<Map<String, Object>> menuTree = new TreeSet<>(new IntComparator("orden"));
		
		List<TvvnMenu> tvvnMenuList = tvvnMenuRepository.findObjectIfContains(filter).collectList().block();
		menuTree = UtilConverter.getTree(TvvnMenu.class, tvvnMenuList, "orden", "idMenu", "idMenuSup");
		
		return Flux.just(menuTree);
	}

	@Override
	public Mono<TvvnMenu> toggle(Map<String, Object> entity) {
		TvvnMenu tvvnMenu = null;
		tvvnMenu = tvvnMenuRepository.findByIdMenu(String.valueOf(entity.get("idMenu"))).block();
		if(Objects.isNull(tvvnMenu)) {
			throw new ViValidationException(HttpStatus.NOT_FOUND, "No fue posible encontrar el menú");
		}
		tvvnMenu.setIdEstado(String.valueOf(entity.get("idEstado")));
		return tvvnMenuRepository.save(tvvnMenu);
	}
	
}
