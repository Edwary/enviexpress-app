package com.enviexpres.logistica.clientmodule.service.imp;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.clientmodule.model.TevuClienteAuditoria;
import com.enviexpres.logistica.clientmodule.model.dto.TevnError;
import com.enviexpres.logistica.clientmodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.clientmodule.repository.itf.TevuClienteAuditoriaRepository;
import com.enviexpres.logistica.clientmodule.service.itf.TevuClienteAuditoriaService;
import com.enviexpres.logistica.clientmodule.utils.Constant;
import com.enviexpres.logistica.clientmodule.utils.UtilConverter;
import com.enviexpres.logistica.clientmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.clientmodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevuClienteAuditoriaServiceImp implements TevuClienteAuditoriaService {

	@Autowired
	private TevuClienteAuditoriaRepository tevuClienteAuditoriaRepository;

	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	private static String MODULO = Constant.MODULO_CLIENTES;
	
	@Override
	public Mono<TevuClienteAuditoria> create(Map<String, Object> entity) {
		TevuClienteAuditoria tevuClienteAuditoria = new TevuClienteAuditoria();
		
		Flux<TevuClienteAuditoria> tevuClienteAuditoriaFlux = tevuClienteAuditoriaRepository.findAll();
		Date date = UtilConverter.currentDateComplete();
		try {
			if(tevuClienteAuditoriaFlux.count().block() > 0L) {
				tevuClienteAuditoria.setConsecutivo(UtilsGeneral.devolverConsecutivo12Digitos(tevuClienteAuditoriaFlux.last().block().getConsecutivo()));
			} else {
				tevuClienteAuditoria.setConsecutivo(UtilsGeneral.devolverConsecutivo12Digitos(""));
			}
			tevuClienteAuditoria.setVista(String.valueOf(entity.get("vista")));
			tevuClienteAuditoria.setUsuario(String.valueOf(entity.get("usuario")));
			tevuClienteAuditoria.setNup(String.valueOf(entity.get("nup")));
			tevuClienteAuditoria.setNus(String.valueOf(entity.get("nus")));
			tevuClienteAuditoria.setFecha(date);
			tevuClienteAuditoria.setHora(UtilConverter.getHora(date));
			tevuClienteAuditoria.setAccion(String.valueOf(entity.get("accion")));
			tevuClienteAuditoria.setContenido(UtilConverter.classToDocument(entity.get("contenido")));
		} catch (RuntimeException e) {
			TevnError tevnError = UtilConverter.createError(e, MODULO);
			tevnErrorRepository.save(tevnError);
			throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
		}
		
		return tevuClienteAuditoriaRepository.save(tevuClienteAuditoria);
	}

	@Override
	public Mono<TevuClienteAuditoria> findById(String id) {
		return tevuClienteAuditoriaRepository.findById(id);
	}

	@Override
	public Flux<TevuClienteAuditoria> findAll() {
		return tevuClienteAuditoriaRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		return tevuClienteAuditoriaRepository.deleteById(id);
	}
	
	
}
