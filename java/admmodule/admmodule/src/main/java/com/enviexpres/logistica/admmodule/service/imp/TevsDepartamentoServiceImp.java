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
import com.enviexpres.logistica.admmodule.model.TevsDepartamento;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevsDepartamentoRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevsDepartamentoService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevsDepartamentoServiceImp implements TevsDepartamentoService {

	@Autowired
	private TevsDepartamentoRepository tevsDepartamentoRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	private static String MODULO = Constant.MODULO_ADM;
	
	@Override
	public Mono<TevsDepartamento> create(Map<String, Object> entity) {
		TevsDepartamento tevsDepartamento = new TevsDepartamento();
		tevsDepartamento.setIdDepartamento(String.valueOf(entity.get("idDepartamento")));
		tevsDepartamento.setIdPais(String.valueOf(entity.get("idPais")));
		tevsDepartamento.setNmDepartamento(String.valueOf(entity.get("nmDepartamento")));
		tevsDepartamento.setSbDepartamento(String.valueOf(entity.get("sbDepartamento")));
		tevsDepartamento.setRegion(String.valueOf(entity.get("region")));
		tevsDepartamento.setCodigoDane(String.valueOf(entity.get("codigoDane")));
		tevsDepartamento.setCodigoPostal(String.valueOf(entity.get("codigoPostal")));
		tevsDepartamento.setIdEstado(String.valueOf(entity.get("idEstado")));
		return tevsDepartamentoRepository.save(tevsDepartamento);
	}

	@Override
	public Mono<TevsDepartamento> findById(String id) {
		return tevsDepartamentoRepository.findById(id);
	}

	@Override
	public Flux<TevsDepartamento> findAll() {
		List<TevsDepartamento> tevsDepartamentoList = tevsDepartamentoRepository.findAll().toStream().filter(p -> !p.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList());
		return Flux.fromIterable(tevsDepartamentoList);
	}

	@Override
	public Mono<Void> remove(String id) {
		TevsDepartamento tevsDepartamento = tevsDepartamentoRepository.findById(id).block();
		return tevsDepartamentoRepository.delete(tevsDepartamento);
	}

	@Override
	public Flux<TevsDepartamento> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		Iterable<TevsDepartamento> iterableDepartamento = entityList.stream()
				.map(tevsDepartamentoMap -> {
					TevsDepartamento tevsDepartamento = new TevsDepartamento();
					tevsDepartamento.setIdDepartamento(String.valueOf(tevsDepartamentoMap.get("idDepartamento")));
					tevsDepartamento.setIdPais(String.valueOf(tevsDepartamentoMap.get("idPais")));
					tevsDepartamento.setNmDepartamento(String.valueOf(tevsDepartamentoMap.get("nmDepartamento")));
					tevsDepartamento.setSbDepartamento(String.valueOf(tevsDepartamentoMap.get("sbDepartamento")));
					tevsDepartamento.setRegion(String.valueOf(tevsDepartamentoMap.get("region")));
					tevsDepartamento.setCodigoDane(String.valueOf(tevsDepartamentoMap.get("codigoDane")));
					tevsDepartamento.setCodigoPostal(String.valueOf(tevsDepartamentoMap.get("codigoPostal")));
					tevsDepartamento.setIdEstado(String.valueOf(tevsDepartamentoMap.get("idEstado")));
					return tevsDepartamento;
				})
				.collect(Collectors.toList());
		
		return tevsDepartamentoRepository.saveAll(iterableDepartamento);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tevsDepartamentoMapList = new ArrayList<Map<String, Object>>();
		Flux<Document> tevsDepartamentoFlux = tevsDepartamentoRepository.findObjectIfContains(filter);
		tevsDepartamentoFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream().forEach(tevsDepartamentoObject -> {
			try { 
				Map<String, Object> tevsDepartamentoMap = new HashMap<>();
				TevsDepartamento tevsDepartamento = UtilConverter.documentToClass(TevsDepartamento.class, (Document) tevsDepartamentoObject.get("tevs_departamento"));
				if(!tevsDepartamento.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)) {
					TevpPais tevpPais = UtilConverter.documentToClass(TevpPais.class, (Document) tevsDepartamentoObject.get("tevp_pais"));
					TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) tevsDepartamentoObject.get("tevn_estado"));
					tevsDepartamentoMap = UtilConverter.classToMap(tevsDepartamento);
					tevsDepartamentoMap.put("nmPais", tevpPais.getNmPais());
					tevsDepartamentoMap.put("sbPais", tevpPais.getSbPais());
					tevsDepartamentoMap.put("nmEstado", tevnEstado.getNmEstado());
					tevsDepartamentoMap.put("sbEstado", tevnEstado.getSbEstado());
					tevsDepartamentoMapList.add(tevsDepartamentoMap);
				}
			} catch (IllegalAccessException | InstantiationException e) {
				TevnError tevnError = UtilConverter.createError(e, MODULO);
				tevnErrorRepository.save(tevnError);
				throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
			}
		});
		Flux<Map<String, Object>> tevsDepartamentoMapFlux = Flux.fromIterable(tevsDepartamentoMapList);
		return tevsDepartamentoMapFlux;
	}

	@Override
	public Mono<TevsDepartamento> toggleDepartamento(String id) {
		TevsDepartamento tevsDepartamento = tevsDepartamentoRepository.findById(id).block();
		if(tevsDepartamento.getIdEstado().equals(Constant.IND_ESTADO_ACTIVO)) {
			tevsDepartamento.setIdEstado(Constant.IND_ESTADO_INACTIVO);
		} else {
			tevsDepartamento.setIdEstado(Constant.IND_ESTADO_ACTIVO);
		}
		return tevsDepartamentoRepository.save(tevsDepartamento);
	}

	@Override
	public Mono<TevsDepartamento> logicRemove(String id) {
		TevsDepartamento tevsDepartamento = tevsDepartamentoRepository.findById(id).block();
		tevsDepartamento.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
		return tevsDepartamentoRepository.save(tevsDepartamento);
	}

}
