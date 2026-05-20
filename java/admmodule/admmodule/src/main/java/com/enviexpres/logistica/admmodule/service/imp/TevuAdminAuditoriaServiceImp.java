package com.enviexpres.logistica.admmodule.service.imp;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.admmodule.model.TevnError;
import com.enviexpres.logistica.admmodule.model.TevuAdminAuditoria;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevuAdminAuditoriaRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevuAdminAuditoriaService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevuAdminAuditoriaServiceImp implements TevuAdminAuditoriaService {

	@Autowired
	private TevuAdminAuditoriaRepository tevuAdminAuditoriaRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	private static String MODULO = Constant.MODULO_ADM;
	
	@Override
	public Mono<TevuAdminAuditoria> create(Map<String, Object> entity) {
		TevuAdminAuditoria tevuAdminAuditoria = new TevuAdminAuditoria();
		
		Flux<TevuAdminAuditoria> tevuAdminAuditoriaFlux = tevuAdminAuditoriaRepository.findAll();
		Date date = UtilConverter.currentDateComplete();
		try {
			if(tevuAdminAuditoriaFlux.count().block() > 0L) {
				tevuAdminAuditoria.setConsecutivo(UtilsGeneral.devolverConsecutivo12Digitos(tevuAdminAuditoriaFlux.last().block().getConsecutivo()));
			} else {
				tevuAdminAuditoria.setConsecutivo(UtilsGeneral.devolverConsecutivo12Digitos(""));
			}
			tevuAdminAuditoria.setVista(String.valueOf(entity.get("vista")));
			tevuAdminAuditoria.setUsuario(String.valueOf(entity.get("usuario")));
			tevuAdminAuditoria.setNup(String.valueOf(entity.get("nup")));
			tevuAdminAuditoria.setNus(String.valueOf(entity.get("nus")));
			tevuAdminAuditoria.setFecha(date);
			tevuAdminAuditoria.setHora(UtilConverter.getHora(date));
			tevuAdminAuditoria.setAccion(String.valueOf(entity.get("accion")));
			tevuAdminAuditoria.setContenido(UtilConverter.classToDocument(entity.get("contenido")));
		} catch (RuntimeException e) {
			TevnError tevnError = UtilConverter.createError(e, MODULO);
			tevnErrorRepository.save(tevnError);
			throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
		}
		
		return tevuAdminAuditoriaRepository.save(tevuAdminAuditoria);
	}

	@Override
	public Mono<TevuAdminAuditoria> findById(String id) {
		return tevuAdminAuditoriaRepository.findById(id);
	}

	@Override
	public Flux<TevuAdminAuditoria> findAll() {
		return tevuAdminAuditoriaRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		return tevuAdminAuditoriaRepository.deleteById(id);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
