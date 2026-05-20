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

import com.enviexpres.logistica.admmodule.model.TevnMenu;
import com.enviexpres.logistica.admmodule.repository.itf.TevnMenuRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevnMenuService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.IntComparator;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnMenuServiceImp implements TevnMenuService {

	@Autowired
	private TevnMenuRepository TevnMenuRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	@Override
	public Mono<TevnMenu> create(Map<String, Object> entity) {
		TevnMenu TevnMenu = null;
		TevnMenu = TevnMenuRepository.findByIdMenu(String.valueOf(entity.get("idMenu"))).block();
		if(Objects.isNull(TevnMenu)) {
			TevnMenu = new TevnMenu();
		}
		TevnMenu.setIdMenu(String.valueOf(entity.get("idMenu")));
		TevnMenu.setNmMenu(String.valueOf(entity.get("nmMenu")));
		TevnMenu.setIdMenuSup(String.valueOf(entity.get("idMenuSup")));
		TevnMenu.setTipoMenu(String.valueOf(entity.get("tipoMenu")));
		TevnMenu.setHref(String.valueOf(entity.get("href")));
		TevnMenu.setNivel(String.valueOf(entity.get("nivel")));
		TevnMenu.setOrden(String.valueOf(entity.get("orden")));
		TevnMenu.setIndVisible(String.valueOf(entity.get("indVisible")));
		TevnMenu.setIdEstado(String.valueOf(entity.get("idEstado")));
		return TevnMenuRepository.save(TevnMenu);
	}

	@Override
	public Mono<TevnMenu> findById(String id) {
		return TevnMenuRepository.findById(id);
	}

	@Override
	public Flux<TevnMenu> findAll() {
		return TevnMenuRepository.findAll().sort((TevnMenu1, TevnMenu2) -> {
			return TevnMenu1.getOrden().compareTo(TevnMenu2.getOrden());
		});
	}

	@Override
	public Mono<Void> remove(String id) {
		TevnMenu TevnMenuMono = TevnMenuRepository.findById(id).block();
		return TevnMenuRepository.delete(TevnMenuMono);
	}

	@Override
	public Flux<TevnMenu> createVarious(List<Map<String, Object>> TevnMenuList) {
		
		if(Objects.isNull(TevnMenuList)) {
			return null;
		}
		
		Iterable<TevnMenu> TevnMenuIterable = TevnMenuList.stream()
				.map(TevnMenuMap -> {
					TevnMenu TevnMenu = new TevnMenu();
					TevnMenu.setIdMenu(String.valueOf(TevnMenuMap.get("idMenu")));
					TevnMenu.setNmMenu(String.valueOf(TevnMenuMap.get("nmMenu")));
					TevnMenu.setIdMenuSup(String.valueOf(TevnMenuMap.get("idMenuSup")));
					TevnMenu.setTipoMenu(String.valueOf(TevnMenuMap.get("tipoMenu")));
					TevnMenu.setHref(String.valueOf(TevnMenuMap.get("href")));
					TevnMenu.setNivel(String.valueOf(TevnMenuMap.get("nivel")));
					TevnMenu.setOrden(String.valueOf(TevnMenuMap.get("orden")));
					TevnMenu.setIndVisible(String.valueOf(TevnMenuMap.get("indVisible")));
					TevnMenu.setIdEstado(String.valueOf(TevnMenuMap.get("idEstado")));
					return TevnMenu;
				})
				.collect(Collectors.toList());
		
		return TevnMenuRepository.saveAll(TevnMenuIterable);
	}

	@Override
	public Flux<TreeSet<Map<String, Object>>> menuTree() {
		Flux<TevnMenu> TevnMenuFlux =  TevnMenuRepository.findAllVisible(Constant.IND_VISIBLE_S).sort((TevnMenu1, TevnMenu2) -> {
			return TevnMenu1.getOrden().compareTo(TevnMenu2.getOrden());
		});
		List<TevnMenu> TevnMenuList = TevnMenuFlux.collectList().block();
		TreeSet<Map<String, Object>> menu = new TreeSet<>(new IntComparator("orden"));
		for(TevnMenu TevnMenu : TevnMenuList) {
			Map<String, Object> menuPadre = new HashMap<>();
			
			//if(TevnMenu.getIndVisible().equals(ViConstant.IND_VISIBLE_N)) continue;
			
			if(TevnMenu.getIdMenu().equals(TevnMenu.getIdMenuSup())) {
				menuPadre.put("id", TevnMenu.getIdMenu());
				menuPadre.put("name", TevnMenu.getNmMenu());
				menuPadre.put("sup", TevnMenu.getIdMenuSup());
				menuPadre.put("tipo", TevnMenu.getTipoMenu());
				menuPadre.put("href", TevnMenu.getHref());
				menuPadre.put("nivel",  Integer.valueOf(TevnMenu.getNivel()));
				menuPadre.put("orden", TevnMenu.getOrden());
				menuPadre.put("visible", TevnMenu.getIndVisible());
				menuPadre.put("estado", TevnMenu.getIdEstado());
				
				List<TevnMenu> TevnMenuSubList = TevnMenuList
						.stream()
						.filter(tm -> tm.getIdMenuSup().equals(TevnMenu.getIdMenu()))
						.collect(Collectors.toList());
				if(TevnMenuSubList.size() > 0) {
					menuPadre.put("children", buscarHijosMenu(TevnMenu, TevnMenuSubList, TevnMenuList));
				}else {
					menuPadre.put("children", "");
				}
				
				menu.add(menuPadre);
			}
		}
		return Flux.just(menu);
	}
	
	public TreeSet<Map<String, Object>> buscarHijosMenu(TevnMenu TevnMenu, List<TevnMenu> TevnMenuHijos, List<TevnMenu> TevnMenuList){
		TreeSet<Map<String, Object>> hijoTree = new TreeSet<>(new IntComparator("orden"));
		
		for(TevnMenu TevnMenuHijo : TevnMenuHijos) {
			
			if(TevnMenuHijo.getIdMenu().equals(TevnMenu.getIdMenuSup())) continue;
			
			Map<String, Object> menuHijo = new HashMap<>();
			menuHijo.put("id", TevnMenuHijo.getIdMenu());
			menuHijo.put("name", TevnMenuHijo.getNmMenu());
			menuHijo.put("sup", TevnMenuHijo.getIdMenuSup());
			menuHijo.put("tipo", TevnMenuHijo.getTipoMenu());
			menuHijo.put("href", TevnMenuHijo.getHref());
			menuHijo.put("nivel", Integer.valueOf(TevnMenuHijo.getNivel()));
			menuHijo.put("orden", TevnMenuHijo.getOrden());
			menuHijo.put("visible", TevnMenuHijo.getIndVisible());
			menuHijo.put("estado", TevnMenuHijo.getIdEstado());
			
			List<TevnMenu> TevnMenuSubList = TevnMenuList
					.stream()
					.filter(tm -> tm.getIdMenuSup().equals(TevnMenuHijo.getIdMenu()))
					.collect(Collectors.toList());
			if(TevnMenuSubList.size() > 0) {
				menuHijo.put("children", buscarHijosMenu(TevnMenu, TevnMenuSubList, TevnMenuList));
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
		
		List<TevnMenu> TevnMenuList = TevnMenuRepository.findObjectIfContains(filter).collectList().block();
		menuTree = UtilConverter.getTree(TevnMenu.class, TevnMenuList, "orden", "idMenu", "idMenuSup");
		
		return Flux.just(menuTree);
	}

	@Override
	public Mono<TevnMenu> toggle(Map<String, Object> entity) {
		TevnMenu TevnMenu = null;
		TevnMenu = TevnMenuRepository.findByIdMenu(String.valueOf(entity.get("idMenu"))).block();
		if(Objects.isNull(TevnMenu)) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "No fue posible encontrar el menú");
		}
		TevnMenu.setIdEstado(String.valueOf(entity.get("idEstado")));
		return TevnMenuRepository.save(TevnMenu);
	}
	
}
