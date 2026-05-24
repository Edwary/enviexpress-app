package com.enviexpres.logistica.usermodule.service.imp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.usermodule.model.TevnRol;
import com.enviexpres.logistica.usermodule.repository.itf.TevnRolRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevnRolService;
import com.enviexpres.logistica.usermodule.utils.Constant;
import com.enviexpres.logistica.usermodule.utils.UtilsGeneral;
import com.enviexpres.logistica.usermodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnRolServiceImp implements TevnRolService {

	@Autowired
	private TevnRolRepository tevnRolRepository;
	
	@Override
	public Mono<TevnRol> create(Map<String, String> entity) {
	    String idRol = entity.get("idRol");

	    Mono<TevnRol> sourceMono = StringUtils.isEmpty(idRol) 
	        ? tevnRolRepository.findTopByOrderByIdRolDesc()
	            .map(last -> {
	                TevnRol nuevo = new TevnRol();
	                nuevo.setIdRol(UtilsGeneral.devolverConsecutivo4Digitos(last.getIdRol()));
	                return nuevo;
	            })
	            .defaultIfEmpty(new TevnRol() {{ setIdRol(UtilsGeneral.devolverConsecutivo4Digitos("0")); }})
	        : tevnRolRepository.findByIdRol(idRol)
	            .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado")));

	    return sourceMono.map(rol -> {
	        rol.setNombre(entity.get("nombre"));
	        rol.setSbRol(entity.get("sbRol"));
	        rol.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
	        return rol;
	    }).flatMap(tevnRolRepository::save);
	}

	@Override
	public Mono<TevnRol> findById(String idRol) {
		return tevnRolRepository.findByIdRol(idRol);
	}

	@Override
	public Flux<TevnRol> findAll() {
		return tevnRolRepository.findAll();
	}
	
	@Override
	public Mono<TevnRol> toggle(Map<String, String> entity) {
	    return tevnRolRepository.findByIdRol(entity.get("idRol"))
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado")))
	        .flatMap(tevnRol -> {
	            tevnRol.setIdEstado(entity.get("idEstado"));
	            return tevnRolRepository.save(tevnRol);
	        });
	}

	@Override
	public Mono<Void> remove(String idRol) {
	    return tevnRolRepository.findByIdRol(idRol)
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado")))
	        .flatMap(tevnRol -> tevnRolRepository.delete(tevnRol));
	}

	

}
