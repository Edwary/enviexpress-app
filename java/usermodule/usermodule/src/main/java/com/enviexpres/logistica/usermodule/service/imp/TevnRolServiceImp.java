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
		TevnRol tevnRol = new TevnRol();
		
		if (StringUtils.isEmpty(entity.get("idRol"))) {
			String lastId = tevnRolRepository.findTopByOrderByIdRolDesc().block().getIdRol();
			tevnRol.setIdRol(UtilsGeneral.devolverConsecutivo4Digitos(lastId));
		} else {
			tevnRol = tevnRolRepository.findByIdRol(entity.get("idRol")).block();
		}
		tevnRol.setNombre(entity.get("nombre"));
		tevnRol.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
		return tevnRolRepository.save(tevnRol);
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
		TevnRol tevnRol = tevnRolRepository.findByIdRol(entity.get("idRol")).block();
		if (tevnRol == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado");
		}
		tevnRol.setIdEstado(entity.get("idEstado"));
		return tevnRolRepository.save(tevnRol);
	}

	@Override
	public Mono<Void> remove(String idRol) {
		TevnRol tevnRol = tevnRolRepository.findByIdRol(idRol).block();
		if (tevnRol == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado");
		}
		return tevnRolRepository.delete(tevnRol);
	}

	

}
