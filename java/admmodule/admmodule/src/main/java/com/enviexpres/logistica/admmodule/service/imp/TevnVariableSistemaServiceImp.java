package com.enviexpres.logistica.admmodule.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.com.vimodules.admmodule.model.TvvnVariableSistema;
import co.com.vimodules.admmodule.repository.itf.TvvnErrorRepository;
import co.com.vimodules.admmodule.repository.itf.TvvnVariableSistemaRepository;
import co.com.vimodules.admmodule.service.itf.TvvnVariableSistemaService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.ViGeneral;
import co.com.vimodules.admmodule.model.TvvnError;
import co.com.vimodules.admmodule.model.TvvnEstado;
import co.com.vimodules.admmodule.utils.exception.ViValidationException;
import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnVariableSistemaServiceImp implements TevnVariableSistemaService {

	@Autowired
	TvvnVariableSistemaRepository tvvnVariableSistemaRepository;
	
	@Autowired
	TvvnErrorRepository tvvnErrorRepository;
	
	@Override
	public Mono<TvvnVariableSistema> create(Map<String, String> entity) {
		TvvnVariableSistema tvvnVariableSistema = new TvvnVariableSistema();
		if(StringUtils.isEmpty(entity.get("idVariable"))) {
			Flux<TvvnVariableSistema> tvvnVariableSistemaFlux = tvvnVariableSistemaRepository.findAll();
			TvvnVariableSistema tvvnVariableSistemaLast = tvvnVariableSistemaFlux.blockLast();
			String lastId = Objects.isNull(tvvnVariableSistemaLast) ? "0" : tvvnVariableSistemaLast.getIdVariable();
			tvvnVariableSistema.setIdVariable(ViGeneral.devolverConsecutivo4Digitos(lastId));
		} else {
			tvvnVariableSistema = tvvnVariableSistemaRepository.findByIdVariable(entity.get("idVariable")).block();
		}
		tvvnVariableSistema.setNmVariable(entity.get("nmVariable"));
		tvvnVariableSistema.setValor(entity.get("valor"));
		tvvnVariableSistema.setDsVariable(entity.get("dsVariable"));
		tvvnVariableSistema.setTipo(StringUtils.isEmpty(entity.get("tipo")) ? ViConstant.VARIABLE_TIPO_CONFIGURACION : entity.get("tipo"));
		tvvnVariableSistema.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? ViConstant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
		return tvvnVariableSistemaRepository.save(tvvnVariableSistema);
	}

	@Override
	public Mono<TvvnVariableSistema> findById(String id) {
		return tvvnVariableSistemaRepository.findById(id);
	}

	@Override
	public Flux<TvvnVariableSistema> findAll() {
		return tvvnVariableSistemaRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		return tvvnVariableSistemaRepository.deleteById(id);
	}

	@Override
	public Flux<TvvnVariableSistema> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		String lastId = tvvnVariableSistemaRepository.findAll().blockLast().getIdVariable();
		TvvnVariableSistema tvvnVariableSistema = new TvvnVariableSistema();
		tvvnVariableSistema.setIdVariable(lastId);
		
		Iterable<TvvnVariableSistema> tvvnVariableSistemaIterable = entityList.stream()
				.map(tvvnVariableSistemaMap -> {
					tvvnVariableSistema.setIdVariable(ViGeneral.devolverConsecutivo4Digitos(tvvnVariableSistema.getIdVariable()));
					tvvnVariableSistema.setNmVariable(String.valueOf(tvvnVariableSistemaMap.get("nmVariable")));
					tvvnVariableSistema.setValor(String.valueOf(tvvnVariableSistemaMap.get("valor")));
					tvvnVariableSistema.setDsVariable(String.valueOf(tvvnVariableSistemaMap.get("dsVariable")));
					tvvnVariableSistema.setIdEstado(Objects.isNull(tvvnVariableSistemaMap.get("idEstado")) ? ViConstant.IND_ESTADO_ACTIVO : String.valueOf(tvvnVariableSistemaMap.get("idEstado")));
					return tvvnVariableSistema;
				})
				.collect(Collectors.toList());
		return tvvnVariableSistemaRepository.saveAll(tvvnVariableSistemaIterable);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tvvnVariableSistemaMapList = new ArrayList<Map<String, Object>>();
		Flux<Document> tvvnVariableSistemaFlux = tvvnVariableSistemaRepository.findIfContains(filter);
		tvvnVariableSistemaFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream().forEach(tvvnVariableSistemaObject -> {
			try {
				Map<String, Object> tvvnVariableSistemaMap = new HashMap<>();
				TvvnVariableSistema tvvnVariableSistema = UtilConverter.documentToClass(TvvnVariableSistema.class, (Document) tvvnVariableSistemaObject.get("tvvn_variable_sistema"));
				TvvnEstado tvvnEstado = Objects.isNull(tvvnVariableSistemaObject.get("tvvn_estado")) ? null : UtilConverter.documentToClass(TvvnEstado.class, (Document) tvvnVariableSistemaObject.get("tvvn_estado"));
				tvvnVariableSistemaMap = UtilConverter.classToMap(tvvnVariableSistema);
				tvvnVariableSistemaMap.put("nmEstado", Objects.isNull(tvvnEstado) ? "" : tvvnEstado.getNmEstado());
				tvvnVariableSistemaMap.put("sbEstado", Objects.isNull(tvvnEstado) ? "" : tvvnEstado.getSbEstado());
				tvvnVariableSistemaMap.put("colorEstado", Objects.isNull(tvvnEstado) ? "" : tvvnEstado.getColor());
				tvvnVariableSistemaMapList.add(tvvnVariableSistemaMap);
			} catch(IllegalAccessException | InstantiationException e) {
				TvvnError tvvnError = ViGeneral.createError(e, ViConstant.MODULO_ATOM);
				tvvnErrorRepository.save(tvvnError);
				throw new ViValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.SinInformacion");
			}
		});
		
		Flux<Map<String, Object>> tvvnVariableSistemaMapFlux = Flux.fromIterable(tvvnVariableSistemaMapList);
		return tvvnVariableSistemaMapFlux;
	}

	@Override
	public Mono<TvvnVariableSistema> toggle(Map<String, String> entity) {
		TvvnVariableSistema tvvnVariableSistema = tvvnVariableSistemaRepository.findById(entity.get("idVariable")).block();
		if(!Objects.isNull(tvvnVariableSistema)) {
			tvvnVariableSistema.setIdEstado(entity.get("idEstado"));
			return tvvnVariableSistemaRepository.save(tvvnVariableSistema);
		} else {
			return null;
		}
	}

	@Override
	public Mono<TvvnVariableSistema> logicRemove(String id) {
		TvvnVariableSistema tvvnVariableSistema = tvvnVariableSistemaRepository.findById(id).block();
		if(!Objects.isNull(tvvnVariableSistema)) {
			tvvnVariableSistema.setIdEstado(ViConstant.IND_ESTADO_ELIMINADO);
			return tvvnVariableSistemaRepository.save(tvvnVariableSistema);
		} else {
			return null;
		}
	}

}
