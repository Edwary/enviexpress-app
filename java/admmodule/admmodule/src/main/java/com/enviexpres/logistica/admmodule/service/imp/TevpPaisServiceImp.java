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
import com.enviexpres.logistica.admmodule.model.TevpPais;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevpPaisRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevpPaisService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevpPaisServiceImp implements TevpPaisService {

	@Autowired
	private TevpPaisRepository tevpPaisRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	private static String MODULO = Constant.MODULO_ADM;
	
	@Override
	public Mono<TevpPais> create(Map<String, Object> entity) {
		TevpPais tevpPais = new TevpPais();
		tevpPais.setIdPais(String.valueOf(entity.get("idPais")));
		tevpPais.setNmPais(String.valueOf(entity.get("nmPais")));
		tevpPais.setSbPais(String.valueOf(entity.get("sbPais")));
		tevpPais.setContinente(String.valueOf(entity.get("continente")));
		tevpPais.setIdEstado(String.valueOf(entity.get("idEstado")).isEmpty() ? Constant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
		return tevpPaisRepository.save(tevpPais);
	}

	@Override
	public Mono<TevpPais> findById(String id) {
		return tevpPaisRepository.findById(id);
	}

	@Override
	public Flux<TevpPais> findAll() {
		List<TevpPais> tevpPaisList = tevpPaisRepository.findAll().toStream().filter(p -> !p.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList());
		return Flux.fromIterable(tevpPaisList);
	}

	@Override
	public Mono<Void> remove(String id) {
		TevpPais tevpPais = tevpPaisRepository.findById(id).block();
		return tevpPaisRepository.delete(tevpPais);
	}

	@Override
	public Flux<TevpPais> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		Iterable<TevpPais> tevpPaisIterable = entityList.stream()
				.map(tevpPaisMap -> {
					TevpPais tevpPais = new TevpPais();
					tevpPais.setIdPais(String.valueOf(tevpPaisMap.get("idPais")));
					tevpPais.setNmPais(String.valueOf(tevpPaisMap.get("nmPais")));
					tevpPais.setSbPais(String.valueOf(tevpPaisMap.get("sbPais")));
					tevpPais.setContinente(String.valueOf(tevpPaisMap.get("continente")));
					tevpPais.setIdEstado(String.valueOf(tevpPaisMap.get("idEstado")));
					return tevpPais;
				})
				.collect(Collectors.toList());
		
		return tevpPaisRepository.saveAll(tevpPaisIterable);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tevpPaisList = new ArrayList<>();
		if(filter.get("nmPais").isEmpty() && filter.get("sbPais").isEmpty() && filter.get("continente").isEmpty() && filter.get("idEstado").isEmpty()) {
			Flux<TevpPais> tevpPaisObjectFlux = Flux.fromIterable(tevpPaisRepository.findAll().toStream().filter(p -> !p.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList()));
			tevpPaisObjectFlux.toStream().forEach(tevpPais -> {
				Map<String, Object> tevpPaisMap = UtilConverter.classToMap(tevpPais);
				tevpPaisList.add(tevpPaisMap);
			});
			return Flux.fromIterable(tevpPaisList);
		}else {
			Flux<Document> tevpPaisesFlux = tevpPaisRepository.findObjectIfContains(filter);
			tevpPaisesFlux.map(document -> {
				Map<String, Object> resultMap = new HashMap<>();
				for(String key : document.keySet()) {
					resultMap.put(key, document.get(key));
				}
				return resultMap;
			}).collectList().block().stream().forEach(tevpPaisObject -> {
				try {
					Map<String, Object> tevpPaisMap = new HashMap<>();
					TevpPais tevpPais = UtilConverter.documentToClass(TevpPais.class, (Document) tevpPaisObject.get("tevp_pais"));
					if(!tevpPais.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)) {
						TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) tevpPaisObject.get("tevn_estado"));
						tevpPaisMap = UtilConverter.classToMap(tevpPais);
						tevpPaisMap.put("nmEstado", tevnEstado.getNmEstado());
						tevpPaisMap.put("sbEstado", tevnEstado.getSbEstado());
						tevpPaisList.add(tevpPaisMap);
					}
				} catch (IllegalAccessException | InstantiationException e) {
					TevnError tevnError = UtilConverter.createError(e, MODULO);
					tevnErrorRepository.save(tevnError);
					throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
				}
			});
			Flux<Map<String, Object>> mapResultFlux = Flux.fromIterable(tevpPaisList);
			return mapResultFlux;
		}
	}
	
	@Override
	public Mono<TevpPais> togglePais(String id) {
		TevpPais tevpPais = tevpPaisRepository.findById(id).block();
		if(tevpPais.getIdEstado().equals(Constant.IND_ESTADO_ACTIVO)) {
			tevpPais.setIdEstado(Constant.IND_ESTADO_INACTIVO);
		} else {
			tevpPais.setIdEstado(Constant.IND_ESTADO_ACTIVO);
		}
		return tevpPaisRepository.save(tevpPais);
	}
	
	@Override
	public Mono<TevpPais> logicRemove(String id) {
		TevpPais tevpPais = tevpPaisRepository.findById(id).block();
		tevpPais.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
		return tevpPaisRepository.save(tevpPais);
	}

}
