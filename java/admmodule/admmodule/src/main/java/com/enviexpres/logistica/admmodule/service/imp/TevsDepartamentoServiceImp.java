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
import co.com.vimodules.admmodule.model.TvvsDepartamento;
import co.com.vimodules.admmodule.repository.itf.TvvnErrorRepository;
import co.com.vimodules.admmodule.repository.itf.TvvsDepartamentoRepository;
import co.com.vimodules.admmodule.service.itf.TvvsDepartamentoService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.ViGeneral;
import co.com.vimodules.admmodule.utils.exception.ViValidationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevsDepartamentoServiceImp implements TevsDepartamentoService {

	@Autowired
	private TvvsDepartamentoRepository tvvsDepartamentoRepository;
	
	@Autowired
	private TvvnErrorRepository tvvnErrorRepository;
	
	private static String MODULO = ViConstant.MODULO_ADM;
	
	@Override
	public Mono<TvvsDepartamento> create(Map<String, Object> entity) {
		TvvsDepartamento tvvsDepartamento = new TvvsDepartamento();
		tvvsDepartamento.setIdDepartamento(String.valueOf(entity.get("idDepartamento")));
		tvvsDepartamento.setIdPais(String.valueOf(entity.get("idPais")));
		tvvsDepartamento.setNmDepartamento(String.valueOf(entity.get("nmDepartamento")));
		tvvsDepartamento.setSbDepartamento(String.valueOf(entity.get("sbDepartamento")));
		tvvsDepartamento.setRegion(String.valueOf(entity.get("region")));
		tvvsDepartamento.setCodigoDane(String.valueOf(entity.get("codigoDane")));
		tvvsDepartamento.setCodigoPostal(String.valueOf(entity.get("codigoPostal")));
		tvvsDepartamento.setIdEstado(String.valueOf(entity.get("idEstado")));
		return tvvsDepartamentoRepository.save(tvvsDepartamento);
	}

	@Override
	public Mono<TvvsDepartamento> findById(String id) {
		return tvvsDepartamentoRepository.findById(id);
	}

	@Override
	public Flux<TvvsDepartamento> findAll() {
		List<TvvsDepartamento> tvvsDepartamentoList = tvvsDepartamentoRepository.findAll().toStream().filter(p -> !p.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList());
		return Flux.fromIterable(tvvsDepartamentoList);
	}

	@Override
	public Mono<Void> remove(String id) {
		TvvsDepartamento tvvsDepartamento = tvvsDepartamentoRepository.findById(id).block();
		return tvvsDepartamentoRepository.delete(tvvsDepartamento);
	}

	@Override
	public Flux<TvvsDepartamento> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		Iterable<TvvsDepartamento> iterableDepartamento = entityList.stream()
				.map(tvvsDepartamentoMap -> {
					TvvsDepartamento tvvsDepartamento = new TvvsDepartamento();
					tvvsDepartamento.setIdDepartamento(String.valueOf(tvvsDepartamentoMap.get("idDepartamento")));
					tvvsDepartamento.setIdPais(String.valueOf(tvvsDepartamentoMap.get("idPais")));
					tvvsDepartamento.setNmDepartamento(String.valueOf(tvvsDepartamentoMap.get("nmDepartamento")));
					tvvsDepartamento.setSbDepartamento(String.valueOf(tvvsDepartamentoMap.get("sbDepartamento")));
					tvvsDepartamento.setRegion(String.valueOf(tvvsDepartamentoMap.get("region")));
					tvvsDepartamento.setCodigoDane(String.valueOf(tvvsDepartamentoMap.get("codigoDane")));
					tvvsDepartamento.setCodigoPostal(String.valueOf(tvvsDepartamentoMap.get("codigoPostal")));
					tvvsDepartamento.setIdEstado(String.valueOf(tvvsDepartamentoMap.get("idEstado")));
					return tvvsDepartamento;
				})
				.collect(Collectors.toList());
		
		return tvvsDepartamentoRepository.saveAll(iterableDepartamento);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tvvsDepartamentoMapList = new ArrayList<Map<String, Object>>();
		Flux<Document> tvvsDepartamentoFlux = tvvsDepartamentoRepository.findObjectIfContains(filter);
		tvvsDepartamentoFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream().forEach(tvvsDepartamentoObject -> {
			try { 
				Map<String, Object> tvvsDepartamentoMap = new HashMap<>();
				TvvsDepartamento tvvsDepartamento = UtilConverter.documentToClass(TvvsDepartamento.class, (Document) tvvsDepartamentoObject.get("tvvs_departamento"));
				if(!tvvsDepartamento.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)) {
					TvvpPais tvvpPais = UtilConverter.documentToClass(TvvpPais.class, (Document) tvvsDepartamentoObject.get("tvvp_pais"));
					TvvnEstado tvvnEstado = UtilConverter.documentToClass(TvvnEstado.class, (Document) tvvsDepartamentoObject.get("tvvn_estado"));
					tvvsDepartamentoMap = UtilConverter.classToMap(tvvsDepartamento);
					tvvsDepartamentoMap.put("nmPais", tvvpPais.getNmPais());
					tvvsDepartamentoMap.put("sbPais", tvvpPais.getSbPais());
					tvvsDepartamentoMap.put("nmEstado", tvvnEstado.getNmEstado());
					tvvsDepartamentoMap.put("sbEstado", tvvnEstado.getSbEstado());
					tvvsDepartamentoMapList.add(tvvsDepartamentoMap);
				}
			} catch (IllegalAccessException | InstantiationException e) {
				TvvnError tvvnError = ViGeneral.createError(e, MODULO);
				tvvnErrorRepository.save(tvvnError);
				throw new ViValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
			}
		});
		Flux<Map<String, Object>> tvvsDepartamentoMapFlux = Flux.fromIterable(tvvsDepartamentoMapList);
		return tvvsDepartamentoMapFlux;
	}

	@Override
	public Mono<TvvsDepartamento> toggleDepartamento(String id) {
		TvvsDepartamento tvvsDepartamento = tvvsDepartamentoRepository.findById(id).block();
		if(tvvsDepartamento.getIdEstado().equals(ViConstant.IND_ESTADO_ACTIVO)) {
			tvvsDepartamento.setIdEstado(ViConstant.IND_ESTADO_INACTIVO);
		} else {
			tvvsDepartamento.setIdEstado(ViConstant.IND_ESTADO_ACTIVO);
		}
		return tvvsDepartamentoRepository.save(tvvsDepartamento);
	}

	@Override
	public Mono<TvvsDepartamento> logicRemove(String id) {
		TvvsDepartamento tvvsDepartamento = tvvsDepartamentoRepository.findById(id).block();
		tvvsDepartamento.setIdEstado(ViConstant.IND_ESTADO_ELIMINADO);
		return tvvsDepartamentoRepository.save(tvvsDepartamento);
	}

}
