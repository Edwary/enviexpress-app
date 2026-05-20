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

import co.com.vimodules.admmodule.configuration.ApiConsumer;
import co.com.vimodules.admmodule.model.TvvnError;
import co.com.vimodules.admmodule.model.TvvnEstado;
import co.com.vimodules.admmodule.model.TvvpPais;
import co.com.vimodules.admmodule.model.TvvsDepartamento;
import co.com.vimodules.admmodule.model.TvvtCiudad;
import co.com.vimodules.admmodule.repository.itf.TvvnErrorRepository;
import co.com.vimodules.admmodule.repository.itf.TvvsDepartamentoRepository;
import co.com.vimodules.admmodule.repository.itf.TvvtCiudadRepository;
import co.com.vimodules.admmodule.service.itf.TvvtCiudadService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.ViGeneral;
import co.com.vimodules.admmodule.utils.exception.ViValidationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevtCiudadServiceImp implements TevtCiudadService {

	@Autowired
	private TvvsDepartamentoRepository tvvsDepartamentoRepository;
	
	@Autowired
	private TvvtCiudadRepository tvvtCiudadRepository;

	@Autowired
	private TvvnErrorRepository tvvnErrorRepository;
	
	private static String MODULO = ViConstant.MODULO_ADM;
	
	ApiConsumer apiConsumer = new ApiConsumer();
	
	@Override
	public Mono<TvvtCiudad> create(Map<String, Object> entity) {
		TvvtCiudad tvvtCiudad = new TvvtCiudad();
		tvvtCiudad.setIdCiudad(String.valueOf(entity.get("idCiudad")));
		tvvtCiudad.setNmCiudad(String.valueOf(entity.get("nmCiudad")));
		tvvtCiudad.setSbCiudad(String.valueOf(entity.get("sbCiudad")));
		tvvtCiudad.setCodigoPostal(String.valueOf(entity.get("codigoPostal")));
		tvvtCiudad.setCodigoDane(String.valueOf(entity.get("codigoDane")));
		tvvtCiudad.setSubRegion(String.valueOf(entity.get("subRegion")));
		tvvtCiudad.setTipo(String.valueOf(entity.get("tipo")));
		tvvtCiudad.setIdDepartamento(String.valueOf(entity.get("idDepartamento")));
		tvvtCiudad.setIdEstado(String.valueOf(entity.get("idEstado")));
		return tvvtCiudadRepository.save(tvvtCiudad);
	}

	@Override
	public Mono<TvvtCiudad> findById(String id) {
		return tvvtCiudadRepository.findById(id);
	}

	@Override
	public Flux<TvvtCiudad> findAll() {
		List<TvvtCiudad> tvvtCiudadList = tvvtCiudadRepository.findAll().toStream().filter(c -> !c.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList());
		return Flux.fromIterable(tvvtCiudadList);
	}

	@Override
	public Mono<Void> remove(String id) {
		TvvtCiudad tvvtCiudad = tvvtCiudadRepository.findById(id).block();
		return tvvtCiudadRepository.delete(tvvtCiudad);
	}

	@Override
	public Flux<TvvtCiudad> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		Iterable<TvvtCiudad> tvvtCiudadIterable = entityList.stream()
				.map(tvvtCiudadMap -> {
					TvvtCiudad tvvtCiudad = new TvvtCiudad();
					tvvtCiudad.setIdCiudad(String.valueOf(tvvtCiudadMap.get("idCiudad")));
					tvvtCiudad.setNmCiudad(String.valueOf(tvvtCiudadMap.get("nmCiudad")));
					tvvtCiudad.setSbCiudad(String.valueOf(tvvtCiudadMap.get("sbCiudad")));
					tvvtCiudad.setCodigoPostal(String.valueOf(tvvtCiudadMap.get("codigoPostal")));
					tvvtCiudad.setCodigoDane(String.valueOf(tvvtCiudadMap.get("codigoDane")));
					tvvtCiudad.setSubRegion(String.valueOf(tvvtCiudadMap.get("subRegion")));
					tvvtCiudad.setTipo(String.valueOf(tvvtCiudadMap.get("tipo")));
					tvvtCiudad.setIdDepartamento(String.valueOf(tvvtCiudadMap.get("idDepartamento")));
					tvvtCiudad.setIdEstado(String.valueOf(tvvtCiudadMap.get("idEstado")));
					return tvvtCiudad;
				})
				.collect(Collectors.toList());
		
		return tvvtCiudadRepository.saveAll(tvvtCiudadIterable);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tvvtCiudadesMapList = new ArrayList<Map<String, Object>>();
		Flux<Document> tvvtCiudadObjectFlux = tvvtCiudadRepository.findObjectIfContains(filter);
		tvvtCiudadObjectFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream()
		.forEach(tvvtCiudadObject -> {
			try {
				Map<String, Object> tvvtCiudadMap = new HashMap<>();
				TvvtCiudad tvvtCiudad = UtilConverter.documentToClass(TvvtCiudad.class, (Document) tvvtCiudadObject.get("tvvt_ciudad"));
				if(!tvvtCiudad.getIdEstado().equals(ViConstant.IND_ESTADO_ELIMINADO)) {
					TvvsDepartamento tvvsDepartamento = UtilConverter.documentToClass(TvvsDepartamento.class, (Document) tvvtCiudadObject.get("tvvs_departamento"));
					TvvpPais tvvpPais = UtilConverter.documentToClass(TvvpPais.class, (Document) tvvtCiudadObject.get("tvvp_pais"));
					TvvnEstado tvvnEstado = UtilConverter.documentToClass(TvvnEstado.class, (Document) tvvtCiudadObject.get("tvvn_estado"));
					tvvtCiudadMap = UtilConverter.classToMap(tvvtCiudad);
					tvvtCiudadMap.put("nmDepartamento", tvvsDepartamento.getNmDepartamento());
					tvvtCiudadMap.put("sbDepartamento", tvvsDepartamento.getSbDepartamento());
					tvvtCiudadMap.put("nmPais", tvvpPais.getNmPais());
					tvvtCiudadMap.put("sbPais", tvvpPais.getSbPais());
					tvvtCiudadMap.put("nmEstado", tvvnEstado.getNmEstado());
					tvvtCiudadMap.put("sbEstado", tvvnEstado.getSbEstado());
					tvvtCiudadesMapList.add(tvvtCiudadMap);
				}
			} catch (IllegalAccessException | InstantiationException e) {
				TvvnError tvvnError = ViGeneral.createError(e, MODULO);
				tvvnErrorRepository.save(tvvnError);
				throw new ViValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
			}
		});
		Flux<Map<String, Object>> tvvtCiudadesMapFlux = Flux.fromIterable(tvvtCiudadesMapList);
		return tvvtCiudadesMapFlux;
	}

	@Override
	public Mono<TvvtCiudad> toggleCiudad(String id) {
		TvvtCiudad tvvtCiudad = tvvtCiudadRepository.findById(id).block();
		if(tvvtCiudad.getIdEstado().equals(ViConstant.IND_ESTADO_ACTIVO)) {
			tvvtCiudad.setIdEstado(ViConstant.IND_ESTADO_INACTIVO);
		} else {
			tvvtCiudad.setIdEstado(ViConstant.IND_ESTADO_ACTIVO);
		}
		return tvvtCiudadRepository.save(tvvtCiudad);
	}

	@Override
	public Mono<TvvtCiudad> logicRemove(String id) {
		TvvtCiudad tvvtCiudad = tvvtCiudadRepository.findById(id).block();
		tvvtCiudad.setIdEstado(ViConstant.IND_ESTADO_ELIMINADO);
		return tvvtCiudadRepository.save(tvvtCiudad);
	}
	
	@Override
	public Flux<TvvtCiudad> actualizarCiudades() {
		
		//Flux<TvvsDepartamento> tvvsDepartamentoFlux = tvvsDepartamentoRepository.findAll();
		Flux<TvvtCiudad> tvvtCiudadFlux = tvvtCiudadRepository.findAll();
		List<TvvtCiudad> tvvtCiudadFluxList = tvvtCiudadFlux.collectList().block();
		tvvtCiudadFlux.toStream().forEach(ciudad -> ciudad.setIdEstado(ViConstant.IND_ESTADO_INACTIVO));
		tvvtCiudadRepository.saveAll(tvvtCiudadFlux.toIterable());
		
		List<String> tvvtCiudadDane = tvvtCiudadFlux.toStream().map(ciudad -> ciudad.getCodigoDane()).collect(Collectors.toList());
		List<TvvtCiudad> tvvtCiudadList = new ArrayList<>();
		Flux<Document> apiFlux = apiConsumer.fetchDataFromApi(ViConstant.API_MUNICIPIOS_GOV_CO);
		apiFlux.toStream().forEach(apiCiudad -> {
			String codigoDaneMunicipioApi = String.valueOf(apiCiudad.get("c_digo_dane_del_municipio")).replace(".", "");
			String nmCiudadApi = String.valueOf(apiCiudad.get("municipio"));
			String idDeptoApi = String.valueOf(apiCiudad.get("c_digo_dane_del_departamento"));
			String regionApi = String.valueOf(apiCiudad.get("region"));
			if(tvvtCiudadDane.contains(codigoDaneMunicipioApi)) {
				TvvtCiudad tvvtCiudad = tvvtCiudadFlux.toStream().filter(ciudad -> ciudad.getCodigoDane().equals(codigoDaneMunicipioApi)).findFirst().orElse(null);
				if(!Objects.isNull(tvvtCiudad)) {
					tvvtCiudad.setCodigoDane(String.format("%05d", Long.valueOf(codigoDaneMunicipioApi)));
					tvvtCiudad.setNmCiudad(nmCiudadApi);
					tvvtCiudad.setSubRegion(regionApi);
					tvvtCiudad.setIdDepartamento(String.format("%02d", Long.valueOf(idDeptoApi)));
					tvvtCiudad.setCodigoPostal("");
					tvvtCiudad.setSbCiudad(nmCiudadApi.toUpperCase().substring(0, 3));
					tvvtCiudad.setTipo(ViConstant.TIPO_MUNICIPIO);
					tvvtCiudad.setIdEstado(ViConstant.IND_ESTADO_ACTIVO);
					tvvtCiudadList.add(tvvtCiudad);
				}
			} else {
				TvvtCiudad tvvtCiudad = new TvvtCiudad();
				tvvtCiudad.setIdCiudad(ViGeneral.devolverConsecutivo7Digitos(String.valueOf(tvvtCiudadFluxList.size())));
				tvvtCiudad.setCodigoDane(String.format("%05d", Long.valueOf(codigoDaneMunicipioApi)));
				tvvtCiudad.setNmCiudad(nmCiudadApi);
				tvvtCiudad.setSubRegion(regionApi);
				tvvtCiudad.setTipo(ViConstant.TIPO_MUNICIPIO);
				tvvtCiudad.setIdDepartamento(String.format("%02d", Long.valueOf(idDeptoApi)));
				tvvtCiudad.setCodigoPostal("");
				tvvtCiudad.setSbCiudad(nmCiudadApi.toUpperCase().substring(0, 3));
				tvvtCiudad.setIdEstado(ViConstant.IND_ESTADO_ACTIVO);
				tvvtCiudadList.add(tvvtCiudad);
				tvvtCiudadFluxList.add(tvvtCiudad);
			}
		});
		
		return tvvtCiudadRepository.saveAll(tvvtCiudadList);
	}
}
