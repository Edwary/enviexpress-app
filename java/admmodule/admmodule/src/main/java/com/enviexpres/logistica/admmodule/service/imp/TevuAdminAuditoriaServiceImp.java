package com.enviexpres.logistica.admmodule.service.imp;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.com.vimodules.admmodule.model.TvvuAdminAuditoria;
import co.com.vimodules.admmodule.repository.itf.TvvuAdminAuditoriaRepository;
import co.com.vimodules.admmodule.service.itf.TvvuAdminAuditoriaService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViGeneral;
import co.com.vimodules.admmodule.model.TvvnError;
import co.com.vimodules.admmodule.utils.exception.ViValidationException;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.repository.itf.TvvnErrorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevuAdminAuditoriaServiceImp implements TevuAdminAuditoriaService {

	@Autowired
	private TvvuAdminAuditoriaRepository tvvuAdminAuditoriaRepository;
	
	@Autowired
	private TvvnErrorRepository tvvnErrorRepository;
	
	private static String MODULO = ViConstant.MODULO_ADM;
	
	@Override
	public Mono<TvvuAdminAuditoria> create(Map<String, Object> entity) {
		TvvuAdminAuditoria tvvuAdminAuditoria = new TvvuAdminAuditoria();
		
		Flux<TvvuAdminAuditoria> tvvuAdminAuditoriaFlux = tvvuAdminAuditoriaRepository.findAll();
		Date date = UtilConverter.currentDateComplete();
		try {
			if(tvvuAdminAuditoriaFlux.count().block() > 0L) {
				tvvuAdminAuditoria.setConsecutivo(ViGeneral.devolverConsecutivo12Digitos(tvvuAdminAuditoriaFlux.last().block().getConsecutivo()));
			} else {
				tvvuAdminAuditoria.setConsecutivo(ViGeneral.devolverConsecutivo12Digitos(""));
			}
			tvvuAdminAuditoria.setVista(String.valueOf(entity.get("vista")));
			tvvuAdminAuditoria.setUsuario(String.valueOf(entity.get("usuario")));
			tvvuAdminAuditoria.setNup(String.valueOf(entity.get("nup")));
			tvvuAdminAuditoria.setNus(String.valueOf(entity.get("nus")));
			tvvuAdminAuditoria.setFecha(date);
			tvvuAdminAuditoria.setHora(UtilConverter.getHora(date));
			tvvuAdminAuditoria.setAccion(String.valueOf(entity.get("accion")));
			tvvuAdminAuditoria.setContenido(UtilConverter.classToDocument(entity.get("contenido")));
		} catch (RuntimeException e) {
			TvvnError tvvnError = UtilConverter.createError(e, MODULO);
			tvvnErrorRepository.save(tvvnError);
			throw new ViValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
		}
		
		return tvvuAdminAuditoriaRepository.save(tvvuAdminAuditoria);
	}

	@Override
	public Mono<TvvuAdminAuditoria> findById(String id) {
		return tvvuAdminAuditoriaRepository.findById(id);
	}

	@Override
	public Flux<TvvuAdminAuditoria> findAll() {
		return tvvuAdminAuditoriaRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		return tvvuAdminAuditoriaRepository.deleteById(id);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
