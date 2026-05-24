package com.enviexpres.logistica.admmodule.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.admmodule.model.TevnError;
import com.enviexpres.logistica.admmodule.model.TevnEstado;
import com.enviexpres.logistica.admmodule.model.TevnMenu;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevnMenuRepository;
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
    private TevnMenuRepository tevnMenuRepository;

	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
    @Override
    public Mono<TevnMenu> create(Map<String, Object> entity) {
        String idMenu = String.valueOf(entity.get("idMenu"));
        
        return tevnMenuRepository.findByIdMenu(idMenu)
            .switchIfEmpty(Mono.just(new TevnMenu()))
            .map(menu -> {
                menu.setIdMenu(idMenu);
                menu.setNmMenu(String.valueOf(entity.get("nmMenu")));
                menu.setIdMenuSup(String.valueOf(entity.get("idMenuSup")));
                menu.setTipoMenu(String.valueOf(entity.get("tipoMenu")));
                menu.setHref(String.valueOf(entity.get("href")));
                menu.setNivel(String.valueOf(entity.get("nivel")));
                menu.setOrden(String.valueOf(entity.get("orden")));
                menu.setIndVisible(String.valueOf(entity.get("indVisible")));
                menu.setIdEstado(String.valueOf(entity.get("idEstado")));
                return menu;
            })
            .flatMap(tevnMenuRepository::save);
    }

    @Override
    public Mono<Void> remove(String id) {
        return tevnMenuRepository.findByIdMenu(id)
                .flatMap(tevnMenuRepository::delete);
    }

    @Override
    public Flux<TevnMenu> createVarious(List<Map<String, Object>> TevnMenuList) {
        if (Objects.isNull(TevnMenuList)) return Flux.empty();

        List<TevnMenu> menus = TevnMenuList.stream().map(map -> {
            TevnMenu menu = new TevnMenu();
            menu.setIdMenu(String.valueOf(map.get("idMenu")));
            menu.setNmMenu(String.valueOf(map.get("nmMenu")));
            menu.setIdMenuSup(String.valueOf(map.get("idMenuSup")));
            menu.setTipoMenu(String.valueOf(map.get("tipoMenu")));
            menu.setHref(String.valueOf(map.get("href")));
            menu.setNivel(String.valueOf(map.get("nivel")));
            menu.setOrden(String.valueOf(map.get("orden")));
            menu.setIndVisible(String.valueOf(map.get("indVisible")));
            menu.setIdEstado(String.valueOf(map.get("idEstado")));
            return menu;
        }).collect(Collectors.toList());

        return tevnMenuRepository.saveAll(menus);
    }

    @Override
    public Flux<TreeSet<Map<String, Object>>> menuTree() {
        return tevnMenuRepository.findAllVisible(Constant.IND_VISIBLE_S)
                .sort((m1, m2) -> m1.getOrden().compareTo(m2.getOrden()))
                .collectList()
                .map(tevnMenuList -> {
                    TreeSet<Map<String, Object>> menu = new TreeSet<>(new IntComparator("orden"));
                    for (TevnMenu tevnMenu : tevnMenuList) {
                        if (tevnMenu.getIdMenu().equals(tevnMenu.getIdMenuSup())) {
                            Map<String, Object> menuPadre = convertirAMapa(tevnMenu);
                            List<TevnMenu> hijos = tevnMenuList.stream()
                                    .filter(tm -> tm.getIdMenuSup().equals(tevnMenu.getIdMenu()))
                                    .collect(Collectors.toList());
                            
                            menuPadre.put("children", hijos.isEmpty() ? "" : buscarHijosMenu(tevnMenu, hijos, tevnMenuList));
                            menu.add(menuPadre);
                        }
                    }
                    return menu;
                })
                .flux();
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

	private Map<String, Object> convertirAMapa(TevnMenu m) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", m.getIdMenu());
        map.put("name", m.getNmMenu());
        map.put("sup", m.getIdMenuSup());
        map.put("tipo", m.getTipoMenu());
        map.put("href", m.getHref());
        map.put("nivel", Integer.valueOf(m.getNivel()));
        map.put("orden", m.getOrden());
        map.put("visible", m.getIndVisible());
        map.put("estado", m.getIdEstado());
        return map;
    }

    @Override
    public Flux<TreeSet<Map<String, Object>>> findIfContains(Map<String, String> filter) {
        return tevnMenuRepository.findObjectIfContains(filter)
                .collectList()
                .map(list -> UtilConverter.getTree(TevnMenu.class, list, "orden", "idMenu", "idMenuSup"))
                .flux();
    }

    @Override
    public Mono<TevnMenu> toggle(Map<String, Object> entity) {
        return tevnMenuRepository.findByIdMenu(String.valueOf(entity.get("idMenu")))
                .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "No fue posible encontrar el menú")))
                .map(menu -> {
                    menu.setIdEstado(String.valueOf(entity.get("idEstado")));
                    return menu;
                })
                .flatMap(tevnMenuRepository::save);
    }

	@Override
	public Mono<TevnMenu> findById(String id) {
		return tevnMenuRepository.findById(id);
	}

	@Override
	public Flux<TevnMenu> findAll() {
		return tevnMenuRepository.findAll().sort((tvvnMenu1, tvvnMenu2) -> {
			return tvvnMenu1.getOrden().compareTo(tvvnMenu2.getOrden());
		});
	}

	@Override
	public Flux<Map<String, Object>> findMenuIfContains(Map<String, String> where) {
		return tevnMenuRepository.findIfContains(where)
		        .flatMap(document -> {
		            try {
		                TevnMenu tevnMenu = UtilConverter.documentToClass(TevnMenu.class, (Document) document.get("tevn_menu"));
		                TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) document.get("tevn_estado"));
		                
		                Map<String, Object> tevnMenuMap = UtilConverter.classToMap(tevnMenu);
		                tevnMenuMap.put("nmEstado", tevnEstado.getNmEstado());
		                tevnMenuMap.put("color", tevnEstado.getColor());
		                
		                return Mono.just(tevnMenuMap);
		            } catch (Exception e) {
		                TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_USUARIOS);
		                return tevnErrorRepository.save(tevnError)
		                    .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información")));
		            }
		        });
	}
	
}
