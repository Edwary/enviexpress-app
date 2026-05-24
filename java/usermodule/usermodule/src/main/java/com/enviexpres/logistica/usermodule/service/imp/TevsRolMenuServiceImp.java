package com.enviexpres.logistica.usermodule.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.usermodule.model.TevnRol;
import com.enviexpres.logistica.usermodule.model.TevsRolMenu;
import com.enviexpres.logistica.usermodule.model.dto.TevnError;
import com.enviexpres.logistica.usermodule.model.dto.TevnEstado;
import com.enviexpres.logistica.usermodule.model.dto.TevnMenu;
import com.enviexpres.logistica.usermodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.usermodule.repository.dto.itf.TevnMenuRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevsRolMenuRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevsRolMenuService;
import com.enviexpres.logistica.usermodule.utils.Constant;
import com.enviexpres.logistica.usermodule.utils.IntComparator;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;
import com.enviexpres.logistica.usermodule.utils.UtilsGeneral;
import com.enviexpres.logistica.usermodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevsRolMenuServiceImp implements TevsRolMenuService {

	@Autowired
	private TevsRolMenuRepository tevsRolMenuRepository;

	@Autowired
	private TevnMenuRepository tevnMenuRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	@Override
	public Mono<TevsRolMenu> create(Map<String, String> entity) {
	    String idRolMenu = entity.get("idRolMenu");

	    Mono<TevsRolMenu> sourceMono = StringUtils.isEmpty(idRolMenu)
	        ? tevsRolMenuRepository.findTopByOrderByIdRolMenuDesc()
	            .map(last -> {
	                TevsRolMenu nuevo = new TevsRolMenu();
	                nuevo.setIdRolMenu(UtilsGeneral.devolverConsecutivo12Digitos(last == null ? "0" : last.getIdRolMenu()));
	                return nuevo;
	            })
	            .defaultIfEmpty(new TevsRolMenu(UtilsGeneral.devolverConsecutivo12Digitos("0")))
	        : tevsRolMenuRepository.findByIdRolMenu(idRolMenu)
	            .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Registro no encontrado")));

	    return sourceMono.map(trm -> {
	        trm.setIdRol(entity.get("idRol"));
	        trm.setIdMenu(entity.get("idMenu"));
	        trm.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
	        return trm;
	    }).flatMap(tevsRolMenuRepository::save);
	}

	@Override
	public Mono<TevsRolMenu> findById(String idRolMenu) {
		return tevsRolMenuRepository.findByIdRolMenu(idRolMenu);
	}

	@Override
	public Flux<TevsRolMenu> findAll() {
		return tevsRolMenuRepository.findAll();
	}

	@Override
	public Flux<Map<String, Object>> findIfContainsRol(String idRol) {
	    return tevsRolMenuRepository.findByRolMenuObject(idRol)
	        .flatMap(document -> {
	            try {
	                // Convertimos el documento a mapa
	                Map<String, Object> tevsRolMenuMap = new HashMap<>();
	                TevsRolMenu tevsRolMenu = UtilConverter.documentToClass(TevsRolMenu.class, (Document) document.get("tevs_rol_menu"));
	                TevnRol tevnRol = UtilConverter.documentToClass(TevnRol.class, (Document) document.get("tevn_rol"));
	                TevnMenu tevnMenu = UtilConverter.documentToClass(TevnMenu.class, (Document) document.get("tevn_menu"));
	                TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) document.get("tevn_estado"));

	                tevsRolMenuMap = UtilConverter.classToMap(tevsRolMenu);
	                tevsRolMenuMap.put("nmRol", tevnRol == null ? "" : tevnRol.getNombre());
	                tevsRolMenuMap.put("nmMenu", tevnMenu == null ? "" : tevnMenu.getNmMenu());
	                tevsRolMenuMap.put("nmEstado", tevnEstado == null ? "" : tevnEstado.getNmEstado());
	                tevsRolMenuMap.put("colorEstado", tevnEstado == null ? "" : tevnEstado.getColor());

	                return Mono.just(tevsRolMenuMap);
	            } catch (IllegalAccessException | InstantiationException e) {
	                // Si ocurre un error, guardamos el log y emitimos un error reactivo
	                TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_USUARIOS);
	                return tevnErrorRepository.save(tevnError)
	                    .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Sin información para mostrar")));
	            }
	        });
	}

	@Override
	public Mono<TevsRolMenu> toggle(Map<String, String> entity) {
		return tevsRolMenuRepository.findByIdRolMenu(entity.get("idRolMenu"))
				.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Rol Menu no encontrado")))
				.flatMap(tevsRolMenu -> {
					tevsRolMenu.setIdEstado(entity.get("idEstado"));
					return tevsRolMenuRepository.save(tevsRolMenu);
				});
	}

	@Override
	public Mono<Void> remove(String idRolMenu) {
		return tevsRolMenuRepository.findByIdRolMenu(idRolMenu)
				.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Rol menu no encontrado")))
				.flatMap(tevsRolMenu -> tevsRolMenuRepository.delete(tevsRolMenu));
	}
	
	@Override
	public Mono<TreeSet<Map<String, Object>>> getMenuTreeByRol(String idRol) {
	    
	    return tevsRolMenuRepository.findByIdRol(idRol)
	        .flatMap(rolMenu -> tevnMenuRepository.findByIdMenu(rolMenu.getIdMenu()))
	        .filter(menu -> Constant.IND_VISIBLE_S.equals(menu.getIndVisible()))
	        .sort((m1, m2) -> m1.getOrden().compareTo(m2.getOrden()))
	        .collectList()
	        .map(tevnMenuList -> {
	            TreeSet<Map<String, Object>> menuTree = new TreeSet<>(new IntComparator("orden"));
	            
	            for (TevnMenu tevnMenu : tevnMenuList) {
	                // Verificamos si es un menú padre (raíz)
	                if (tevnMenu.getIdMenu().equals(tevnMenu.getIdMenuSup()) || tevnMenu.getIdMenuSup() == null || tevnMenu.getIdMenuSup().isEmpty()) {
	                    
	                    Map<String, Object> menuPadre = convertirAMapa(tevnMenu);
	                    
	                    // Buscamos los hijos de este padre dentro de la lista que ya consultamos
	                    List<TevnMenu> hijos = tevnMenuList.stream()
	                            .filter(tm -> tm.getIdMenuSup() != null 
	                                       && tm.getIdMenuSup().equals(tevnMenu.getIdMenu()) 
	                                       && !tm.getIdMenu().equals(tm.getIdMenuSup()))
	                            .collect(Collectors.toList());
	                    
	                    // Si tiene hijos, llamamos a la recursividad
	                    menuPadre.put("children", hijos.isEmpty() ? "" : buscarHijosMenu(tevnMenu, hijos, tevnMenuList));
	                    menuTree.add(menuPadre);
	                }
	            }
	            return menuTree;
	        });
	}

	private TreeSet<Map<String, Object>> buscarHijosMenu(TevnMenu menuPadre, List<TevnMenu> hijos, List<TevnMenu> listaCompleta) {
	    TreeSet<Map<String, Object>> hijoTree = new TreeSet<>(new IntComparator("orden"));
	    
	    for (TevnMenu hijo : hijos) {
	        // Evitar bucles infinitos si un menú es padre de sí mismo por error de DB
	        if (hijo.getIdMenu().equals(menuPadre.getIdMenuSup())) continue;
	        
	        Map<String, Object> menuHijo = convertirAMapa(hijo);
	        
	        List<TevnMenu> subHijos = listaCompleta.stream()
	                .filter(tm -> tm.getIdMenuSup() != null && tm.getIdMenuSup().equals(hijo.getIdMenu()))
	                .collect(Collectors.toList());
	                
	        if (!subHijos.isEmpty()) {
	            menuHijo.put("children", buscarHijosMenu(hijo, subHijos, listaCompleta));
	        } else {
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
}
