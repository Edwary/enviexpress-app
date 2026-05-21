package com.enviexpres.logistica.usermodule.service.imp;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.usermodule.model.TevuUsuarioAuditoria;
import com.enviexpres.logistica.usermodule.model.dto.TevnError;
import com.enviexpres.logistica.usermodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevuUsuarioAuditoriaRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevuUsuarioAuditoriaService;
import com.enviexpres.logistica.usermodule.utils.Constant;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;
import com.enviexpres.logistica.usermodule.utils.UtilsGeneral;
import com.enviexpres.logistica.usermodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevuUsuarioAuditoriaServiceImp implements TevuUsuarioAuditoriaService {

	@Autowired
	private TevuUsuarioAuditoriaRepository tevuUsuarioAuditoriaRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	@Override
	public Mono<TevuUsuarioAuditoria> create(Map<String, Object> entity) {
		TevuUsuarioAuditoria tevuUsuarioAuditoria = new TevuUsuarioAuditoria();
		
		Flux<TevuUsuarioAuditoria> tevuUsuarioAuditoriaFlux = tevuUsuarioAuditoriaRepository.findAll();
		Date date = UtilConverter.currentDateComplete();
		try {
			if(tevuUsuarioAuditoriaFlux.count().block() > 0L) {
				tevuUsuarioAuditoria.setConsecutivo(UtilsGeneral.devolverConsecutivo12Digitos(tevuUsuarioAuditoriaFlux.last().block().getConsecutivo()));
			} else {
				tevuUsuarioAuditoria.setConsecutivo(UtilsGeneral.devolverConsecutivo12Digitos(""));
			}
			tevuUsuarioAuditoria.setVista(String.valueOf(entity.get("vista")));
			tevuUsuarioAuditoria.setUsuario(String.valueOf(entity.get("usuario")));
			tevuUsuarioAuditoria.setNup(String.valueOf(entity.get("nup")));
			tevuUsuarioAuditoria.setNus(String.valueOf(entity.get("nus")));
			tevuUsuarioAuditoria.setFecha(date);
			tevuUsuarioAuditoria.setHora(UtilConverter.getHora(date));
			tevuUsuarioAuditoria.setAccion(String.valueOf(entity.get("accion")));
			tevuUsuarioAuditoria.setContenido(UtilConverter.classToDocument(entity.get("contenido")));
		} catch (RuntimeException e) {
			TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_USUARIOS);
			tevnErrorRepository.save(tevnError);
			throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
		}
		
		return tevuUsuarioAuditoriaRepository.save(tevuUsuarioAuditoria);
	}

	@Override
	public Mono<TevuUsuarioAuditoria> findById(String id) {
		return tevuUsuarioAuditoriaRepository.findById(id);
	}

	@Override
	public Flux<TevuUsuarioAuditoria> findAll() {
		return tevuUsuarioAuditoriaRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		return tevuUsuarioAuditoriaRepository.deleteById(id);
	}

}
