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

import com.enviexpres.logistica.admmodule.configuration.ApiConsumer;
import com.enviexpres.logistica.admmodule.model.TevnError;
import com.enviexpres.logistica.admmodule.model.TevnEstado;
import com.enviexpres.logistica.admmodule.model.TevpPais;
import com.enviexpres.logistica.admmodule.model.TevsDepartamento;
import com.enviexpres.logistica.admmodule.model.TevtCiudad;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevsDepartamentoRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevtCiudadRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevtCiudadService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevtCiudadServiceImp implements TevtCiudadService {

	@Autowired
	private TevsDepartamentoRepository tevsDepartamentoRepository;
	
	@Autowired
	private TevtCiudadRepository tevtCiudadRepository;

	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	private static String MODULO = Constant.MODULO_ADM;
	
	ApiConsumer apiConsumer = new ApiConsumer();
	
	@Override
	public Mono<TevtCiudad> create(Map<String, Object> entity) {
		TevtCiudad tevtCiudad = new TevtCiudad();
		tevtCiudad.setIdCiudad(String.valueOf(entity.get("idCiudad")));
		tevtCiudad.setNmCiudad(String.valueOf(entity.get("nmCiudad")));
		tevtCiudad.setSbCiudad(String.valueOf(entity.get("sbCiudad")));
		tevtCiudad.setCodigoPostal(String.valueOf(entity.get("codigoPostal")));
		tevtCiudad.setCodigoDane(String.valueOf(entity.get("codigoDane")));
		tevtCiudad.setSubRegion(String.valueOf(entity.get("subRegion")));
		tevtCiudad.setTipo(String.valueOf(entity.get("tipo")));
		tevtCiudad.setIdDepartamento(String.valueOf(entity.get("idDepartamento")));
		tevtCiudad.setIdEstado(String.valueOf(entity.get("idEstado")));
		return tevtCiudadRepository.save(tevtCiudad);
	}

	@Override
	public Mono<TevtCiudad> findById(String id) {
		return tevtCiudadRepository.findById(id);
	}

	@Override
	public Flux<TevtCiudad> findAll() {
		List<TevtCiudad> tevtCiudadList = tevtCiudadRepository.findAll().toStream().filter(c -> !c.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)).collect(Collectors.toList());
		return Flux.fromIterable(tevtCiudadList);
	}

	@Override
	public Mono<Void> remove(String id) {
		TevtCiudad tevtCiudad = tevtCiudadRepository.findById(id).block();
		return tevtCiudadRepository.delete(tevtCiudad);
	}

	@Override
	public Flux<TevtCiudad> createVarious(List<Map<String, Object>> entityList) {
		
		if(Objects.isNull(entityList)) {
			return null;
		}
		
		Iterable<TevtCiudad> tevtCiudadIterable = entityList.stream()
				.map(tevtCiudadMap -> {
					TevtCiudad tevtCiudad = new TevtCiudad();
					tevtCiudad.setIdCiudad(String.valueOf(tevtCiudadMap.get("idCiudad")));
					tevtCiudad.setNmCiudad(String.valueOf(tevtCiudadMap.get("nmCiudad")));
					tevtCiudad.setSbCiudad(String.valueOf(tevtCiudadMap.get("sbCiudad")));
					tevtCiudad.setCodigoPostal(String.valueOf(tevtCiudadMap.get("codigoPostal")));
					tevtCiudad.setCodigoDane(String.valueOf(tevtCiudadMap.get("codigoDane")));
					tevtCiudad.setSubRegion(String.valueOf(tevtCiudadMap.get("subRegion")));
					tevtCiudad.setTipo(String.valueOf(tevtCiudadMap.get("tipo")));
					tevtCiudad.setIdDepartamento(String.valueOf(tevtCiudadMap.get("idDepartamento")));
					tevtCiudad.setIdEstado(String.valueOf(tevtCiudadMap.get("idEstado")));
					return tevtCiudad;
				})
				.collect(Collectors.toList());
		
		return tevtCiudadRepository.saveAll(tevtCiudadIterable);
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
		List<Map<String, Object>> tevtCiudadesMapList = new ArrayList<Map<String, Object>>();
		Flux<Document> tevtCiudadObjectFlux = tevtCiudadRepository.findObjectIfContains(filter);
		tevtCiudadObjectFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream()
		.forEach(tevtCiudadObject -> {
			try {
				Map<String, Object> tevtCiudadMap = new HashMap<>();
				TevtCiudad tevtCiudad = UtilConverter.documentToClass(TevtCiudad.class, (Document) tevtCiudadObject.get("tevt_ciudad"));
				if(!tevtCiudad.getIdEstado().equals(Constant.IND_ESTADO_ELIMINADO)) {
					TevsDepartamento tevsDepartamento = UtilConverter.documentToClass(TevsDepartamento.class, (Document) tevtCiudadObject.get("tevs_departamento"));
					TevpPais tevpPais = UtilConverter.documentToClass(TevpPais.class, (Document) tevtCiudadObject.get("tevp_pais"));
					TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) tevtCiudadObject.get("tevn_estado"));
					tevtCiudadMap = UtilConverter.classToMap(tevtCiudad);
					tevtCiudadMap.put("nmDepartamento", tevsDepartamento.getNmDepartamento());
					tevtCiudadMap.put("sbDepartamento", tevsDepartamento.getSbDepartamento());
					tevtCiudadMap.put("nmPais", tevpPais.getNmPais());
					tevtCiudadMap.put("sbPais", tevpPais.getSbPais());
					tevtCiudadMap.put("nmEstado", tevnEstado.getNmEstado());
					tevtCiudadMap.put("sbEstado", tevnEstado.getSbEstado());
					tevtCiudadesMapList.add(tevtCiudadMap);
				}
			} catch (IllegalAccessException | InstantiationException e) {
				TevnError tevnError = UtilConverter.createError(e, MODULO);
				tevnErrorRepository.save(tevnError);
				throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
			}
		});
		Flux<Map<String, Object>> tevtCiudadesMapFlux = Flux.fromIterable(tevtCiudadesMapList);
		return tevtCiudadesMapFlux;
	}

	@Override
	public Mono<TevtCiudad> toggleCiudad(String id) {
		TevtCiudad tevtCiudad = tevtCiudadRepository.findById(id).block();
		if(tevtCiudad.getIdEstado().equals(Constant.IND_ESTADO_ACTIVO)) {
			tevtCiudad.setIdEstado(Constant.IND_ESTADO_INACTIVO);
		} else {
			tevtCiudad.setIdEstado(Constant.IND_ESTADO_ACTIVO);
		}
		return tevtCiudadRepository.save(tevtCiudad);
	}

	@Override
	public Mono<TevtCiudad> logicRemove(String id) {
		TevtCiudad tevtCiudad = tevtCiudadRepository.findById(id).block();
		tevtCiudad.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
		return tevtCiudadRepository.save(tevtCiudad);
	}
	
	@Override
	public Flux<TevtCiudad> actualizarCiudades() {
		
		//Flux<TevsDepartamento> tevsDepartamentoFlux = tevsDepartamentoRepository.findAll();
		Flux<TevtCiudad> tevtCiudadFlux = tevtCiudadRepository.findAll();
		List<TevtCiudad> tevtCiudadFluxList = tevtCiudadFlux.collectList().block();
		tevtCiudadFlux.toStream().forEach(ciudad -> ciudad.setIdEstado(Constant.IND_ESTADO_INACTIVO));
		tevtCiudadRepository.saveAll(tevtCiudadFlux.toIterable());
		
		List<String> tevtCiudadDane = tevtCiudadFlux.toStream().map(ciudad -> ciudad.getCodigoDane()).collect(Collectors.toList());
		List<TevtCiudad> tevtCiudadList = new ArrayList<>();
		Flux<Document> apiFlux = apiConsumer.fetchDataFromApi(Constant.API_MUNICIPIOS_GOV_CO);
		apiFlux.toStream().forEach(apiCiudad -> {
			String codigoDaneMunicipioApi = String.valueOf(apiCiudad.get("c_digo_dane_del_municipio")).replace(".", "");
			String nmCiudadApi = String.valueOf(apiCiudad.get("municipio"));
			String idDeptoApi = String.valueOf(apiCiudad.get("c_digo_dane_del_departamento"));
			String regionApi = String.valueOf(apiCiudad.get("region"));
			if(tevtCiudadDane.contains(codigoDaneMunicipioApi)) {
				TevtCiudad tevtCiudad = tevtCiudadFlux.toStream().filter(ciudad -> ciudad.getCodigoDane().equals(codigoDaneMunicipioApi)).findFirst().orElse(null);
				if(!Objects.isNull(tevtCiudad)) {
					tevtCiudad.setCodigoDane(String.format("%05d", Long.valueOf(codigoDaneMunicipioApi)));
					tevtCiudad.setNmCiudad(nmCiudadApi);
					tevtCiudad.setSubRegion(regionApi);
					tevtCiudad.setIdDepartamento(String.format("%02d", Long.valueOf(idDeptoApi)));
					tevtCiudad.setCodigoPostal("");
					tevtCiudad.setSbCiudad(nmCiudadApi.toUpperCase().substring(0, 3));
					tevtCiudad.setTipo(Constant.TIPO_MUNICIPIO);
					tevtCiudad.setIdEstado(Constant.IND_ESTADO_ACTIVO);
					tevtCiudadList.add(tevtCiudad);
				}
			} else {
				TevtCiudad tevtCiudad = new TevtCiudad();
				tevtCiudad.setIdCiudad(UtilsGeneral.devolverConsecutivo7Digitos(String.valueOf(tevtCiudadFluxList.size())));
				tevtCiudad.setCodigoDane(String.format("%05d", Long.valueOf(codigoDaneMunicipioApi)));
				tevtCiudad.setNmCiudad(nmCiudadApi);
				tevtCiudad.setSubRegion(regionApi);
				tevtCiudad.setTipo(Constant.TIPO_MUNICIPIO);
				tevtCiudad.setIdDepartamento(String.format("%02d", Long.valueOf(idDeptoApi)));
				tevtCiudad.setCodigoPostal("");
				tevtCiudad.setSbCiudad(nmCiudadApi.toUpperCase().substring(0, 3));
				tevtCiudad.setIdEstado(Constant.IND_ESTADO_ACTIVO);
				tevtCiudadList.add(tevtCiudad);
				tevtCiudadFluxList.add(tevtCiudad);
			}
		});
		
		return tevtCiudadRepository.saveAll(tevtCiudadList);
	}
}
