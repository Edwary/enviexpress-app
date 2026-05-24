package com.enviexpres.logistica.admmodule.service.imp;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.admmodule.model.TevnVigencia;
import com.enviexpres.logistica.admmodule.repository.itf.TevnVigenciaRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevnVigenciaService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.UtilsGeneral;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnVigenciaServiceImp implements TevnVigenciaService {

	@Autowired
	private TevnVigenciaRepository tevnVigenciaRepository;
	
	@Override
	public Mono<TevnVigencia> create(Map<String, Object> entity) {
	    String idVigencia = String.valueOf(entity.get("idVigencia"));
	    
	    Mono<TevnVigencia> vigenciaMono = idVigencia.isEmpty() 
	        ? tevnVigenciaRepository.findAll().reduce((first, last) -> last).map(last -> {
	            TevnVigencia tv = new TevnVigencia();
	            tv.setIdVigencia(UtilsGeneral.devolverConsecutivo4Digitos(last.getIdVigencia()));
	            return tv;
	        }).defaultIfEmpty(new TevnVigencia())
	        : tevnVigenciaRepository.findByIdVigencia(idVigencia).defaultIfEmpty(new TevnVigencia());

	    return vigenciaMono.map(tv -> {
	        tv.setNumAnio(String.valueOf(entity.get("numTipoFecha")));
	        tv.setFechaInicio(UtilConverter.toDate(String.valueOf(entity.get("fechaInicio"))));
	        tv.setFechaFin(UtilConverter.toDate(String.valueOf(entity.get("fechaFin"))));
	        tv.setIdEstado(Objects.isNull(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
	        return tv;
	    }).flatMap(tevnVigenciaRepository::save);
	}

	@Override
	public Mono<TevnVigencia> findById(String id) {
		return tevnVigenciaRepository.findById(id);
	}

	@Override
	public Flux<TevnVigencia> findAll() {
		return tevnVigenciaRepository.findAll().filter(vigencia -> Constant.IND_ESTADO_ACTIVO.equals(vigencia.getIdEstado()));
	}

	@Override
	public Mono<Void> remove(String id) {
	    return tevnVigenciaRepository.findByIdVigencia(id)
	        .flatMap(tv -> {
	            tv.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
	            return tevnVigenciaRepository.save(tv);
	        }).then();
	}

	@Override
	public Flux<Map<String, Object>> findAllObject(Map<String, String> where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<TevnVigencia> toggle(Map<String, Object> entity) {
	    return tevnVigenciaRepository.findByIdVigencia(String.valueOf(entity.get("idVigencia")))
	        .map(tv -> {
	            tv.setIdEstado(String.valueOf(entity.get("idEstado")));
	            return tv;
	        }).flatMap(tevnVigenciaRepository::save);
	}

}
