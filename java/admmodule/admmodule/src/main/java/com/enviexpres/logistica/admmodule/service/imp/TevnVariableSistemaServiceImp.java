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

import com.enviexpres.logistica.admmodule.model.TevnError;
import com.enviexpres.logistica.admmodule.model.TevnEstado;
import com.enviexpres.logistica.admmodule.model.TevnVariableSistema;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevnVariableSistemaRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevnVariableSistemaService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnVariableSistemaServiceImp implements TevnVariableSistemaService {

	@Autowired
	TevnVariableSistemaRepository tevnVariableSistemaRepository;
	
	@Autowired
	TevnErrorRepository tevnErrorRepository;
	
	@Override
	public Mono<TevnVariableSistema> create(Map<String, String> entity) {
		TevnVariableSistema tevnVariableSistema = new TevnVariableSistema();
		if(StringUtils.isEmpty(entity.get("idVariable"))) {
			Flux<TevnVariableSistema> tevnVariableSistemaFlux = tevnVariableSistemaRepository.findAll();
			TevnVariableSistema tevnVariableSistemaLast = tevnVariableSistemaFlux.blockLast();
			String lastId = Objects.isNull(tevnVariableSistemaLast) ? "0" : tevnVariableSistemaLast.getIdVariable();
			tevnVariableSistema.setIdVariable(UtilsGeneral.devolverConsecutivo4Digitos(lastId));
		} else {
			tevnVariableSistema = tevnVariableSistemaRepository.findByIdVariable(entity.get("idVariable")).block();
		}
		tevnVariableSistema.setNmVariable(entity.get("nmVariable"));
		tevnVariableSistema.setValor(entity.get("valor"));
		tevnVariableSistema.setDsVariable(entity.get("dsVariable"));
		tevnVariableSistema.setTipo(StringUtils.isEmpty(entity.get("tipo")) ? Constant.VARIABLE_TIPO_CONFIGURACION : entity.get("tipo"));
		tevnVariableSistema.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
		return tevnVariableSistemaRepository.save(tevnVariableSistema);
	}

	@Override
	public Mono<TevnVariableSistema> findById(String id) {
		return tevnVariableSistemaRepository.findById(id);
	}

	@Override
	public Flux<TevnVariableSistema> findAll() {
		return tevnVariableSistemaRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		return tevnVariableSistemaRepository.deleteById(id);
	}

	@Override
	public Flux<TevnVariableSistema> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		String lastId = tevnVariableSistemaRepository.findAll().blockLast().getIdVariable();
		TevnVariableSistema tevnVariableSistema = new TevnVariableSistema();
		tevnVariableSistema.setIdVariable(lastId);
		
		Iterable<TevnVariableSistema> tevnVariableSistemaIterable = entityList.stream()
				.map(tevnVariableSistemaMap -> {
					tevnVariableSistema.setIdVariable(UtilsGeneral.devolverConsecutivo4Digitos(tevnVariableSistema.getIdVariable()));
					tevnVariableSistema.setNmVariable(String.valueOf(tevnVariableSistemaMap.get("nmVariable")));
					tevnVariableSistema.setValor(String.valueOf(tevnVariableSistemaMap.get("valor")));
					tevnVariableSistema.setDsVariable(String.valueOf(tevnVariableSistemaMap.get("dsVariable")));
					tevnVariableSistema.setIdEstado(Objects.isNull(tevnVariableSistemaMap.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : String.valueOf(tevnVariableSistemaMap.get("idEstado")));
					return tevnVariableSistema;
				})
				.collect(Collectors.toList());
		return tevnVariableSistemaRepository.saveAll(tevnVariableSistemaIterable);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tevnVariableSistemaMapList = new ArrayList<Map<String, Object>>();
		Flux<Document> tevnVariableSistemaFlux = tevnVariableSistemaRepository.findIfContains(filter);
		tevnVariableSistemaFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream().forEach(tevnVariableSistemaObject -> {
			try {
				Map<String, Object> tevnVariableSistemaMap = new HashMap<>();
				TevnVariableSistema tevnVariableSistema = UtilConverter.documentToClass(TevnVariableSistema.class, (Document) tevnVariableSistemaObject.get("tevn_variable_sistema"));
				TevnEstado tevnEstado = Objects.isNull(tevnVariableSistemaObject.get("tevn_estado")) ? null : UtilConverter.documentToClass(TevnEstado.class, (Document) tevnVariableSistemaObject.get("tevn_estado"));
				tevnVariableSistemaMap = UtilConverter.classToMap(tevnVariableSistema);
				tevnVariableSistemaMap.put("nmEstado", Objects.isNull(tevnEstado) ? "" : tevnEstado.getNmEstado());
				tevnVariableSistemaMap.put("sbEstado", Objects.isNull(tevnEstado) ? "" : tevnEstado.getSbEstado());
				tevnVariableSistemaMap.put("colorEstado", Objects.isNull(tevnEstado) ? "" : tevnEstado.getColor());
				tevnVariableSistemaMapList.add(tevnVariableSistemaMap);
			} catch(IllegalAccessException | InstantiationException e) {
				TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_ADM);
				tevnErrorRepository.save(tevnError);
				throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.SinInformacion");
			}
		});
		
		Flux<Map<String, Object>> tevnVariableSistemaMapFlux = Flux.fromIterable(tevnVariableSistemaMapList);
		return tevnVariableSistemaMapFlux;
	}

	@Override
	public Mono<TevnVariableSistema> toggle(Map<String, String> entity) {
		TevnVariableSistema tevnVariableSistema = tevnVariableSistemaRepository.findById(entity.get("idVariable")).block();
		if(!Objects.isNull(tevnVariableSistema)) {
			tevnVariableSistema.setIdEstado(entity.get("idEstado"));
			return tevnVariableSistemaRepository.save(tevnVariableSistema);
		} else {
			return null;
		}
	}

	@Override
	public Mono<TevnVariableSistema> logicRemove(String id) {
		TevnVariableSistema tevnVariableSistema = tevnVariableSistemaRepository.findById(id).block();
		if(!Objects.isNull(tevnVariableSistema)) {
			tevnVariableSistema.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
			return tevnVariableSistemaRepository.save(tevnVariableSistema);
		} else {
			return null;
		}
	}

}
