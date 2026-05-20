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

import co.com.vimodules.admmodule.model.TvvnError;
import co.com.vimodules.admmodule.model.TvvnEstado;
import co.com.vimodules.admmodule.model.TvvpPais;
import co.com.vimodules.admmodule.repository.itf.TvvnErrorRepository;
import co.com.vimodules.admmodule.repository.itf.TvvpPaisRepository;
import co.com.vimodules.admmodule.service.itf.TvvpPaisService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.ViGeneral;
import co.com.vimodules.admmodule.utils.exception.ViValidationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevpPaisServiceImp implements TevpPaisService {

	@Autowired
	private TvvpPaisRepository tvvpPaisRepository;
	
	@Autowired
	private TvvnErrorRepository tvvnErrorRepository;
	
	private static String MODULO = ViConstant.MODULO_ADM;
	
	@Override
	public Mono<TvvpPais> create(Map<String, Object> entity) {
		TvvpPais tvvpPais = new TvvpPais();
		tvvpPais.setIdPais(String.valueOf(entity.get("idPais")));
		tvvpPais.setNmPais(String.valueOf(entity.get("nmPais")));
		tvvpPais.setSbPais(String.valueOf(entity.get("sbPais")));
		tvvpPais.setContinente(String.valueOf(entity.get("continente")));
		tvvpPais.setIdEstado(String.valueOf(entity.get("idEstado")).isEmpty() ? ViConstant.IND_ESTADO_ACTIVO : String.valueOf(entity.get("idEstado")));
		return tvvpPaisRepository.save(tvvpPais);
	}

	@Override
	public Mono<TvvpPais> findById(String id) {
		return tvvpPaisRepository.findById(id);
	}

	@Override
	public Flux<TvvpPais> findAll() {
		List<TvvpPais> tvvpPaisList = tvvpPaisRepository.findAll().toStream().filter(p -> !p.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList());
		return Flux.fromIterable(tvvpPaisList);
	}

	@Override
	public Mono<Void> remove(String id) {
		TvvpPais tvvpPais = tvvpPaisRepository.findById(id).block();
		return tvvpPaisRepository.delete(tvvpPais);
	}

	@Override
	public Flux<TvvpPais> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		Iterable<TvvpPais> tvvpPaisIterable = entityList.stream()
				.map(tvvpPaisMap -> {
					TvvpPais tvvpPais = new TvvpPais();
					tvvpPais.setIdPais(String.valueOf(tvvpPaisMap.get("idPais")));
					tvvpPais.setNmPais(String.valueOf(tvvpPaisMap.get("nmPais")));
					tvvpPais.setSbPais(String.valueOf(tvvpPaisMap.get("sbPais")));
					tvvpPais.setContinente(String.valueOf(tvvpPaisMap.get("continente")));
					tvvpPais.setIdEstado(String.valueOf(tvvpPaisMap.get("idEstado")));
					return tvvpPais;
				})
				.collect(Collectors.toList());
		
		return tvvpPaisRepository.saveAll(tvvpPaisIterable);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tvvpPaisList = new ArrayList<>();
		if(filter.get("nmPais").isEmpty() && filter.get("sbPais").isEmpty() && filter.get("continente").isEmpty() && filter.get("idEstado").isEmpty()) {
			Flux<TvvpPais> tvvpPaisObjectFlux = Flux.fromIterable(tvvpPaisRepository.findAll().toStream().filter(p -> !p.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList()));
			tvvpPaisObjectFlux.toStream().forEach(tvvpPais -> {
				Map<String, Object> tvvpPaisMap = UtilConverter.classToMap(tvvpPais);
				tvvpPaisList.add(tvvpPaisMap);
			});
			return Flux.fromIterable(tvvpPaisList);
		}else {
			Flux<Document> tvvpPaisesFlux = tvvpPaisRepository.findObjectIfContains(filter);
			tvvpPaisesFlux.map(document -> {
				Map<String, Object> resultMap = new HashMap<>();
				for(String key : document.keySet()) {
					resultMap.put(key, document.get(key));
				}
				return resultMap;
			}).collectList().block().stream().forEach(tvvpPaisObject -> {
				try {
					Map<String, Object> tvvpPaisMap = new HashMap<>();
					TvvpPais tvvpPais = UtilConverter.documentToClass(TvvpPais.class, (Document) tvvpPaisObject.get("tvvp_pais"));
					if(!tvvpPais.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)) {
						TvvnEstado tvvnEstado = UtilConverter.documentToClass(TvvnEstado.class, (Document) tvvpPaisObject.get("tvvn_estado"));
						tvvpPaisMap = UtilConverter.classToMap(tvvpPais);
						tvvpPaisMap.put("nmEstado", tvvnEstado.getNmEstado());
						tvvpPaisMap.put("sbEstado", tvvnEstado.getSbEstado());
						tvvpPaisList.add(tvvpPaisMap);
					}
				} catch (IllegalAccessException | InstantiationException e) {
					TvvnError tvvnError = ViGeneral.createError(e, MODULO);
					tvvnErrorRepository.save(tvvnError);
					throw new ViValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
				}
			});
			Flux<Map<String, Object>> mapResultFlux = Flux.fromIterable(tvvpPaisList);
			return mapResultFlux;
		}
	}
	
	@Override
	public Mono<TvvpPais> togglePais(String id) {
		TvvpPais tvvpPais = tvvpPaisRepository.findById(id).block();
		if(tvvpPais.getIdEstado().equals(ViConstant.IND_ESTADO_ACTIVO)) {
			tvvpPais.setIdEstado(ViConstant.IND_ESTADO_INACTIVO);
		} else {
			tvvpPais.setIdEstado(ViConstant.IND_ESTADO_ACTIVO);
		}
		return tvvpPaisRepository.save(tvvpPais);
	}
	
	@Override
	public Mono<TvvpPais> logicRemove(String id) {
		TvvpPais tvvpPais = tvvpPaisRepository.findById(id).block();
		tvvpPais.setIdEstado(ViConstant.IND_ESTADO_ELIMINADO);
		return tvvpPaisRepository.save(tvvpPais);
	}

}
