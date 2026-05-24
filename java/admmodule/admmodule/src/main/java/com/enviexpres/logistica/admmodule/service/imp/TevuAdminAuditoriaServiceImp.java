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
	    return tevuAdminAuditoriaRepository.findAll()
	        .reduce((first, last) -> last)
	        .map(last -> UtilsGeneral.devolverConsecutivo12Digitos(last.getConsecutivo()))
	        .defaultIfEmpty(UtilsGeneral.devolverConsecutivo12Digitos(""))
	        .map(consecutivo -> {
	            TevuAdminAuditoria audit = new TevuAdminAuditoria();
	            audit.setConsecutivo(consecutivo);
	            audit.setVista(String.valueOf(entity.get("vista")));
	            audit.setUsuario(String.valueOf(entity.get("usuario")));
	            audit.setNup(String.valueOf(entity.get("nup")));
	            audit.setNus(String.valueOf(entity.get("nus")));
	            audit.setFecha(UtilConverter.currentDate());
	            audit.setHora(UtilConverter.getHora(UtilConverter.currentDate()));
	            audit.setAccion(String.valueOf(entity.get("accion")));
	            audit.setContenido(UtilConverter.classToDocument(entity.get("contenido")));
	            return audit;
	        }).flatMap(tevuAdminAuditoriaRepository::save);
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
