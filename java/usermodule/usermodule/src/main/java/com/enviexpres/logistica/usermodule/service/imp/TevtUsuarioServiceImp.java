package com.enviexpres.logistica.usermodule.service.imp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.usermodule.model.TevnRol;
import com.enviexpres.logistica.usermodule.model.TevtUsuario;
import com.enviexpres.logistica.usermodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevnRolRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevtUsuarioRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevtUsuarioService;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;
import com.enviexpres.logistica.usermodule.utils.UtilsGeneral;
import com.enviexpres.logistica.usermodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevtUsuarioServiceImp implements TevtUsuarioService {

	@Autowired
	private TevtUsuarioRepository tevtUsuarioRepository;
	
	@Autowired
	private TevnRolRepository tevnRolRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;

	@Override
	public Mono<TevtUsuario> create(Map<String, String> entity) {
		TevtUsuario tevtUsuario = new TevtUsuario();
		
		if (StringUtils.isEmpty(entity.get("nus"))) {
			String lastId = tevtUsuarioRepository.findTopByOrderByNusDesc().block().getNus();
			tevtUsuario.setNus(UtilsGeneral.devolverConsecutivo12Digitos(lastId));
		} else {
			tevtUsuario = tevtUsuarioRepository.findByNus(entity.get("idUsuario")).block();
		}
		tevtUsuario.setNmUsuario(entity.get("nmUsuario"));
		tevtUsuario.setNombre(entity.get("nombre"));
		tevtUsuario.setPassword(entity.get("password"));
		tevtUsuario.setEmailUsuario(entity.get("emailUsuario"));
		tevtUsuario.setFechaCreacion(UtilConverter.toDate(entity.get("fechaCrecion")));
		TevnRol tevnRol = tevnRolRepository.findByIdRol(entity.get("idRol")).block();
		if (tevnRol == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado");
		}
		tevtUsuario.setIdRol(tevnRol.getIdRol());
		tevtUsuario.setIdEstado(entity.get("idEstado"));
		return null;
	}

	@Override
	public Mono<TevtUsuario> findById(String nus) {
		TevtUsuario tevtUsuario = tevtUsuarioRepository.findByNus(nus).block();
		if (tevtUsuario == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
		return Mono.just(tevtUsuario);
	}

	@Override
	public Flux<TevtUsuario> findAll() {
		return tevtUsuarioRepository.findAll();
	}

	@Override
	public Mono<TevtUsuario> toggle(Map<String, String> entity) {
		TevtUsuario tevtUsuario = tevtUsuarioRepository.findByNus(entity.get("nus")).block();
		if (tevtUsuario == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
		tevtUsuario.setIdEstado(entity.get("idEstado"));
		return null;
	}

	@Override
	public Mono<Void> remove(String nus) {
		TevtUsuario tevtUsuario = tevtUsuarioRepository.findByNus(nus).block();
		if (tevtUsuario == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
		return tevtUsuarioRepository.delete(tevtUsuario);
	}

	
	
}
