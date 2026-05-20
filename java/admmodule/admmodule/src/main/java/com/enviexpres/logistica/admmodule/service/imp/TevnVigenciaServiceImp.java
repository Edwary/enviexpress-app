package com.enviexpres.logistica.admmodule.service.imp;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.vimodules.admmodule.model.TvvnVigencia;
import co.com.vimodules.admmodule.repository.itf.TvvnVigenciaRepository;
import co.com.vimodules.admmodule.service.itf.TvvnVigenciaService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.ViGeneral;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnVigenciaServiceImp implements TevnVigenciaService {

	@Autowired
	private TvvnVigenciaRepository tvvnVigenciaRepository;
	
	@Override
	public Mono<TvvnVigencia> create(Map<String, Object> entity) {
		TvvnVigencia tvvnVigencia = new TvvnVigencia();
		String idVigencia = String.valueOf(entity.get("idVigencia"));
		if(!idVigencia.isEmpty()) {
			tvvnVigencia = tvvnVigenciaRepository.findByIdVigencia(idVigencia).block();
			tvvnVigencia.setNumAnio(String.valueOf(entity.get("numTipoFecha")));
			tvvnVigencia.setFechaInicio(UtilConverter.toDate(String.valueOf(entity.get("fechaInicio"))));
			tvvnVigencia.setFechaFin(UtilConverter.toDate(String.valueOf(entity.get("fechaFin"))));
			tvvnVigencia.setIdEstado(Objects.isNull(entity.get("idEstado")) ? ViConstant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
			return tvvnVigenciaRepository.save(tvvnVigencia);
		}else {
			Flux<TvvnVigencia> tvvnVigenciaFlux = tvvnVigenciaRepository.findAll();
			
			if(tvvnVigenciaFlux.count().block() > 0L) {
				tvvnVigencia.setIdVigencia(ViGeneral.devolverConsecutivo4Digitos(tvvnVigenciaFlux.last().block().getIdVigencia()));
			}else {
				tvvnVigencia.setIdVigencia(ViGeneral.devolverConsecutivo4Digitos(""));
			}
			tvvnVigencia.setNumAnio(String.valueOf(entity.get("numTipoFecha")));
			tvvnVigencia.setFechaInicio(UtilConverter.toDate(String.valueOf(entity.get("fechaInicio"))));
			tvvnVigencia.setFechaFin(UtilConverter.toDate(String.valueOf(entity.get("fechaFin"))));
			tvvnVigencia.setIdEstado(Objects.isNull(entity.get("idEstado")) ? ViConstant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
			return tvvnVigenciaRepository.save(tvvnVigencia);
		}
	}

	@Override
	public Mono<TvvnVigencia> findById(String id) {
		return tvvnVigenciaRepository.findById(id);
	}

	@Override
	public Flux<TvvnVigencia> findAll() {
		return tvvnVigenciaRepository.findAll().filter(vigencia -> ViConstant.IND_ESTADO_ACTIVO.equals(vigencia.getIdEstado()));
	}

	@Override
	public Mono<Void> remove(String id) {
		TvvnVigencia tvvnVigencia = tvvnVigenciaRepository.findByIdVigencia(id).block();
		tvvnVigencia.setIdEstado(ViConstant.IND_ESTADO_ELIMINADO);
		tvvnVigenciaRepository.save(tvvnVigencia);
		return Mono.empty();
	}

	@Override
	public Flux<Map<String, Object>> findAllObject(Map<String, String> where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<TvvnVigencia> toggle(Map<String, Object> entity) {
		TvvnVigencia tvvnVigencia = tvvnVigenciaRepository.findByIdVigencia(String.valueOf(entity.get("idVigencia"))).block();
		
		if (!Objects.isNull(tvvnVigencia)) {
			tvvnVigencia.setIdEstado(String.valueOf(entity.get("idEstado")));
		}
		
		return tvvnVigenciaRepository.save(tvvnVigencia);
	}

}
