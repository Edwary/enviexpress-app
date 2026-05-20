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
		TevnVigencia tevnVigencia = new TevnVigencia();
		String idVigencia = String.valueOf(entity.get("idVigencia"));
		if(!idVigencia.isEmpty()) {
			tevnVigencia = tevnVigenciaRepository.findByIdVigencia(idVigencia).block();
			tevnVigencia.setNumAnio(String.valueOf(entity.get("numTipoFecha")));
			tevnVigencia.setFechaInicio(UtilConverter.toDate(String.valueOf(entity.get("fechaInicio"))));
			tevnVigencia.setFechaFin(UtilConverter.toDate(String.valueOf(entity.get("fechaFin"))));
			tevnVigencia.setIdEstado(Objects.isNull(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
			return tevnVigenciaRepository.save(tevnVigencia);
		}else {
			Flux<TevnVigencia> tevnVigenciaFlux = tevnVigenciaRepository.findAll();
			
			if(tevnVigenciaFlux.count().block() > 0L) {
				tevnVigencia.setIdVigencia(UtilsGeneral.devolverConsecutivo4Digitos(tevnVigenciaFlux.last().block().getIdVigencia()));
			}else {
				tevnVigencia.setIdVigencia(UtilsGeneral.devolverConsecutivo4Digitos(""));
			}
			tevnVigencia.setNumAnio(String.valueOf(entity.get("numTipoFecha")));
			tevnVigencia.setFechaInicio(UtilConverter.toDate(String.valueOf(entity.get("fechaInicio"))));
			tevnVigencia.setFechaFin(UtilConverter.toDate(String.valueOf(entity.get("fechaFin"))));
			tevnVigencia.setIdEstado(Objects.isNull(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
			return tevnVigenciaRepository.save(tevnVigencia);
		}
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
		TevnVigencia tevnVigencia = tevnVigenciaRepository.findByIdVigencia(id).block();
		tevnVigencia.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
		tevnVigenciaRepository.save(tevnVigencia);
		return Mono.empty();
	}

	@Override
	public Flux<Map<String, Object>> findAllObject(Map<String, String> where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<TevnVigencia> toggle(Map<String, Object> entity) {
		TevnVigencia tevnVigencia = tevnVigenciaRepository.findByIdVigencia(String.valueOf(entity.get("idVigencia"))).block();
		
		if (!Objects.isNull(tevnVigencia)) {
			tevnVigencia.setIdEstado(String.valueOf(entity.get("idEstado")));
		}
		
		return tevnVigenciaRepository.save(tevnVigencia);
	}

}
