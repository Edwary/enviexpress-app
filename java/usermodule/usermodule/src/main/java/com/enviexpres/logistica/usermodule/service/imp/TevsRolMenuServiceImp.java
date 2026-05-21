package com.enviexpres.logistica.usermodule.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.enviexpres.logistica.usermodule.repository.itf.TevsRolMenuRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevsRolMenuService;
import com.enviexpres.logistica.usermodule.utils.Constant;
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
	private TevnErrorRepository tevnErrorRepository;
	
	@Override
	public Mono<TevsRolMenu> create(Map<String, String> entity) {
		TevsRolMenu tevsRolMenu = new TevsRolMenu();
		if (StringUtils.isEmpty(entity.get("idRolMenu"))) {
			String lastId = tevsRolMenuRepository.findTopByOrderByIdRolMenuDesc().block().getIdRolMenu();
			tevsRolMenu.setIdRolMenu(UtilsGeneral.devolverConsecutivo12Digitos(lastId));
		} else {
			tevsRolMenu = tevsRolMenuRepository.findByIdRolMenu(entity.get("idRolMenu")).block();
		}
		tevsRolMenu.setIdRol(entity.get("idRol"));
		tevsRolMenu.setIdMenu(entity.get("idMenu"));
		tevsRolMenu.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
		return tevsRolMenuRepository.save(tevsRolMenu);
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
		List<Map<String, Object>> tevsRolMenuMapList = new ArrayList<Map<String, Object>>();
		
		Flux<Document> tevsRolMenuFlux = tevsRolMenuRepository.findByRolMenuObject(idRol);
		
		tevsRolMenuFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for (String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream().forEach(tevsRolMenuObject -> {
			try {
				Map<String, Object> tevsRolMenuMap = new HashMap<>();
				TevsRolMenu tevsRolMenu = UtilConverter.documentToClass(TevsRolMenu.class, (Document) tevsRolMenuObject.get("tevs_rol_menu"));
				TevnRol tevnRol = UtilConverter.documentToClass(TevnRol.class, (Document) tevsRolMenuObject.get("tevn_rol"));
				TevnMenu tevnMenu = UtilConverter.documentToClass(TevnMenu.class, (Document) tevsRolMenuObject.get("tevn_menu"));
				TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) tevsRolMenuObject.get("tevn_estado"));
				tevsRolMenuMap = UtilConverter.classToMap(tevsRolMenu);
				tevsRolMenuMap.put("nmRol", tevnRol == null ? "" : tevnRol.getNombre());
				tevsRolMenuMap.put("nmMenu", tevnMenu == null ? "" : tevnMenu.getNmMenu());
				tevsRolMenuMap.put("nmEstado", tevnEstado == null ? "" :  tevnEstado.getNmEstado());
				tevsRolMenuMap.put("colorEstado", tevnEstado == null ? "" : tevnEstado.getColor());
				tevsRolMenuMapList.add(tevsRolMenuMap);
			} catch (IllegalAccessException | InstantiationException e) {
				TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_USUARIOS);
				tevnErrorRepository.save(tevnError);
				throw new ValidationException(HttpStatus.BAD_REQUEST, "Sin información para mostrar");
			}
		});
		
		Flux<Map<String, Object>> mapResultFlux = Flux.fromIterable(tevsRolMenuMapList);
		return mapResultFlux;
	}

	@Override
	public Mono<TevsRolMenu> toggle(Map<String, String> entity) {
		TevsRolMenu tevsRolMenu = tevsRolMenuRepository.findByIdRolMenu(entity.get("idRolMenu")).block();
		if (tevsRolMenu == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Rol Menu No Encontrado");
		}
		tevsRolMenu.setIdEstado(entity.get("idEstado"));
		return tevsRolMenuRepository.save(tevsRolMenu);
	}

	@Override
	public Mono<Void> remove(String idRolMenu) {
		TevsRolMenu tevsRolMenu = tevsRolMenuRepository.findByIdRolMenu(idRolMenu).block();
		if (tevsRolMenu == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Rol Menu No Encontrado");
		}
		return tevsRolMenuRepository.delete(tevsRolMenu);
	}
	
	
}
