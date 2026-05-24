package com.enviexpres.logistica.usermodule.service.imp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.usermodule.model.TevuUsuarioAuditoria;
import com.enviexpres.logistica.usermodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevuUsuarioAuditoriaRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevuUsuarioAuditoriaService;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;
import com.enviexpres.logistica.usermodule.utils.UtilsGeneral;

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
		return tevuUsuarioAuditoriaRepository.findAll()
		        .reduce((first, last) -> last)
		        .map(last -> UtilsGeneral.devolverConsecutivo12Digitos(last.getConsecutivo()))
		        .defaultIfEmpty(UtilsGeneral.devolverConsecutivo12Digitos(""))
		        .map(consecutivo -> {
		            TevuUsuarioAuditoria audit = new TevuUsuarioAuditoria();
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
		        }).flatMap(tevuUsuarioAuditoriaRepository::save);
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
