package com.enviexpres.logistica.packmodule.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.packmodule.model.TevjPaqueteAccion;
import com.enviexpres.logistica.packmodule.model.TevjPaqueteEstado;
import com.enviexpres.logistica.packmodule.model.TevtPaquete;
import com.enviexpres.logistica.packmodule.model.dto.TevnError;
import com.enviexpres.logistica.packmodule.model.dto.TevnEstado;
import com.enviexpres.logistica.packmodule.model.dto.TevsCliente;
import com.enviexpres.logistica.packmodule.model.dto.TevsDepartamento;
import com.enviexpres.logistica.packmodule.model.dto.TevtCiudad;
import com.enviexpres.logistica.packmodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.packmodule.repository.itf.TevjPaqueteAccionRepository;
import com.enviexpres.logistica.packmodule.repository.itf.TevjPaqueteEstadoRepository;
import com.enviexpres.logistica.packmodule.repository.itf.TevtPaqueteRepository;
import com.enviexpres.logistica.packmodule.service.itf.TevtPaqueteService;
import com.enviexpres.logistica.packmodule.utils.Constant;
import com.enviexpres.logistica.packmodule.utils.UtilConverter;
import com.enviexpres.logistica.packmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.packmodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TvetPaqueteServiceImp implements TevtPaqueteService {

	@Autowired
	private TevtPaqueteRepository tevtPaqueteRepository;
	
	@Autowired
	private TevjPaqueteEstadoRepository tevjPaqueteEstadoRepository;
	
	@Autowired
	private TevjPaqueteAccionRepository tevjPaqueteAccionRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;

	@Override
	public Mono<TevtPaquete> create(Map<String, String> entity) {
		TevtPaquete tevtPaquete = new TevtPaquete();
		
		if (StringUtils.isEmpty(entity.get("idPaquete"))) { 
			ObjectId uuid = new ObjectId();
			tevtPaquete.setUuid(uuid.toHexString());
			tevtPaquete.setIdPaquete(UtilConverter.convertObjectIdToNumber(uuid.toHexString()));
			tevtPaquete.setFechaCreacion(UtilConverter.currentDate());
		} else {
			tevtPaquete = tevtPaqueteRepository.findByIdPaquete(entity.get("idPaquete")).block();
		}
		tevtPaquete.setIdCliente(entity.get("idCliente"));
		tevtPaquete.setDestinatario(entity.get("destinatario"));
		tevtPaquete.setDireccion(entity.get("direccion"));
		tevtPaquete.setIdCiudad(entity.get("idCiudad"));
		tevtPaquete.setIdDepartamento(entity.get("idDepartamento"));
		tevtPaquete.setTelefono(entity.get("idTelefono"));
		tevtPaquete.setPeso(entity.get("peso"));
		tevtPaquete.setValorDeclarado(entity.get("valorDeclarado"));
		tevtPaquete.setIdEstado(Constant.IND_ESTADO_REGISTRADO);
		Mono<TevtPaquete> tevtPaqueteMono = tevtPaqueteRepository.save(tevtPaquete);
		TevjPaqueteEstado tevjPaqueteEstado = new TevjPaqueteEstado();
		tevjPaqueteEstado.setIdPaquete(tevtPaquete.getIdPaquete());
		tevjPaqueteEstado.setIdEstado(Constant.IND_ESTADO_REGISTRADO);
		tevjPaqueteEstado.setIdEstadoAnterior(Constant.IND_ESTADO_NUEVO);
		tevjPaqueteEstado.setNus(entity.get("nus"));
		tevjPaqueteEstado.setFechaEstado(UtilsGeneral.currentDate());
		tevjPaqueteEstadoRepository.save(tevjPaqueteEstado);
		return tevtPaqueteMono;
	}

	@Override
	public Mono<Map<String, Object>> findById(String idPaquete) {
		try {
			Map<String, Object> tevtPaqueteMap = new HashMap<String, Object>();
			List<Map<String, Object>> tevjPaqueteEstadoMapList = new ArrayList<>();
			
			Document tevtPaqueteDocument = tevtPaqueteRepository.findIdPaqueteDocument(idPaquete).block();
			TevtPaquete tevtPaquete = UtilConverter.documentToClass(TevtPaquete.class, (Document) tevtPaqueteDocument.get("tvet_paquete"));
			if (tevtPaquete == null) {
				throw new ValidationException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
			}
			
			TevtCiudad tevtCiudad = UtilConverter.documentToClass(TevtCiudad.class, (Document) tevtPaqueteDocument.get("tvet_ciudad"));
			TevsDepartamento tevsDepartamento = UtilConverter.documentToClass(TevsDepartamento.class, (Document) tevtPaqueteDocument.get("tves_departamento"));
			Flux<Document> tevjPaqueteEstadoFlux = tevjPaqueteEstadoRepository.findByIdPaquete(idPaquete);
			
			tevjPaqueteEstadoFlux.map(document -> {
				Map<String, Object> resultMap = new HashMap<>();
				for(String key : document.keySet()) {
					resultMap.put(key, document.get(key));
				}
				return resultMap;
			}).collectList().block().stream().forEach(tevjPaqueteEstadoObject -> {
				Map<String, Object> tevjPaqueteEstadoMap = new HashMap<>();
				try {
					TevjPaqueteEstado tevjPaqueteEstado = UtilConverter.documentToClass(TevjPaqueteEstado.class, (Document) tevjPaqueteEstadoObject.get("tevj_paquete_estado"));
					TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) tevjPaqueteEstadoObject.get("tevn_estado"));
					tevjPaqueteEstadoMap.put("uuid", tevjPaqueteEstado.getUuid());
					tevjPaqueteEstadoMap.put("fecha", tevjPaqueteEstado.getFechaEstado());
					tevjPaqueteEstadoMap.put("observaciones", tevjPaqueteEstado.getObservaciones());
					tevjPaqueteEstadoMap.put("idEstado", tevnEstado.getIdEstado());
					tevjPaqueteEstadoMap.put("nmEstado", tevnEstado.getNmEstado());
					tevjPaqueteEstadoMap.put("color", tevnEstado.getColor());
					tevjPaqueteEstadoMapList.add(tevjPaqueteEstadoMap);
				} catch (IllegalAccessException | InstantiationException e) {
					TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_PAQUETES);
					tevnErrorRepository.save(tevnError);
					throw new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información");
				}
			});
			
			tevtPaqueteMap = UtilConverter.classToMap(tevtPaquete);
			tevtPaqueteMap.put("nmCiudad", tevtCiudad.getNmCiudad());
			tevtPaqueteMap.put("codigoPostal", tevtCiudad.getCodigoPostal());
			tevtPaqueteMap.put("nmDepartamento", tevsDepartamento.getNmDepartamento());
			tevtPaqueteMap.put("codigoDane", tevsDepartamento.getCodigoPostal());
			tevtPaqueteMap.put("estados", tevjPaqueteEstadoMapList);
			return Mono.just(tevtPaqueteMap);
		} catch (IllegalAccessException | InstantiationException e) {
			TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_PAQUETES);
			tevnErrorRepository.save(tevnError);
			throw new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información");
		}
	}

	@Override
	public Flux<TevtPaquete> findAll() {
		return tevtPaqueteRepository.findAll();
	}

	@Override
	public Mono<Map<String, Object>> toggle(Map<String, String> entity) {
		Map<String, Object> tevtPaqueteMap = new HashMap<>();
		
		TevtPaquete tevtPaquete = tevtPaqueteRepository.findByIdPaquete(entity.get("idPaquete")).block();
		if (tevtPaquete == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Paquete no Encontrado");
		}
		TevjPaqueteEstado tevjPaqueteEstado = tevjPaqueteEstadoRepository.findTopByIdPaqueteOrderByFechaEstadoDesc(entity.get("idPaquete")).block();
		if (Constant.IND_ESTADO_EN_REPARTO.equals(entity.get("idEstado")) && Constant.IND_ESTADO_ENTREGADO.equals(tevjPaqueteEstado.getIdEstado())) {
			throw new ValidationException(HttpStatus.CONFLICT, "Paquete ya se encuentra entregado");
		}
		if (Constant.IND_ESTADO_CANCELADO.equals(tevjPaqueteEstado.getIdEstado())) {
			throw new ValidationException(HttpStatus.EXPECTATION_FAILED, "Paquete está cancelado");
		}
		if (Constant.IND_ESTADO_DEVUELTO.equals(tevjPaqueteEstado.getIdEstado()) && Constant.IND_ESTADO_ENTREGADO.equals(entity.get("idEstado"))) {
			throw new ValidationException(HttpStatus.I_AM_A_TEAPOT, "Un paquete devuelto no puede marcarse como entregado");
		}
		if (Constant.IND_ESTADO_ENTREGADO.equals(entity.get("idEstado")) && StringUtils.isEmpty(tevtPaquete.getDestinatario())) {
			throw new ValidationException(HttpStatus.NOT_ACCEPTABLE, "No se puede marcar como entregado porque el nombre del destinatario está vació");
		}
		if (Constant.IND_ESTADO_NOVEDAD.equals(entity.get("idEstado")) && StringUtils.isEmpty(tevjPaqueteEstado.getObservaciones())) {
			throw new ValidationException(HttpStatus.NOT_ACCEPTABLE, "No se puede marcar como novedad porque no hya observación");
		}
		tevtPaqueteMap = UtilConverter.classToMap(tevtPaquete);
		
		TevjPaqueteEstado tevjPaqueteEstadoNuevo = new TevjPaqueteEstado();
		tevjPaqueteEstadoNuevo.setIdPaquete(tevtPaquete.getIdPaquete());
		tevjPaqueteEstadoNuevo.setIdEstado(entity.get("idEstado"));
		tevjPaqueteEstadoNuevo.setIdEstadoAnterior(tevjPaqueteEstado.getIdEstado());
		tevjPaqueteEstadoNuevo.setFechaEstado(UtilsGeneral.currentDate());
		tevjPaqueteEstadoNuevo.setNus(entity.get("nus"));
		tevjPaqueteEstadoRepository.save(tevjPaqueteEstadoNuevo);
		tevtPaqueteMap.put("avanceEstado", "Se ha actualizado correctamente el estado del paquete");
		
		tevtPaquete.setIdEstado(tevjPaqueteEstadoNuevo.getIdEstado());
		tevtPaquete = tevtPaqueteRepository.save(tevtPaquete).block();
		
		if (Constant.IND_ESTADO_ENTREGADO.equals(entity.get("idEstado")) || Constant.IND_ESTADO_NOVEDAD.equals(entity.get("idEstado")) || Constant.IND_ESTADO_DEVUELTO.equals(entity.get("idEstado"))) {
			TevjPaqueteAccion tevjPaqueteAccion = new TevjPaqueteAccion();
			String lastIdAccion = tevjPaqueteAccionRepository.findTopByOrderByIdPaqueteAccionDesc().block().getIdEstado();
			tevjPaqueteAccion.setIdEstado(UtilsGeneral.devolverConsecutivo12Digitos(lastIdAccion));
			tevjPaqueteAccion.setIdPaqueteAccion(tevtPaquete.getIdPaquete());
			tevjPaqueteAccion.setNus(entity.get("nus"));
			tevjPaqueteAccion.setIdEstado(Constant.IND_ESTADO_PENDIENTE);
			tevjPaqueteAccion.setFechaCreacion(UtilConverter.currentDate());
			if (Constant.IND_ESTADO_ENTREGADO.equals(entity.get("idEstado"))) { 
				tevjPaqueteAccion.setTipoAccion(Constant.ACCION_LIQUIDACION);
				tevtPaqueteMap.put("accion", "Se generó una acción de liquidación");
			}
			if (Constant.IND_ESTADO_NOVEDAD.equals(entity.get("idEstado"))) { 
				tevjPaqueteAccion.setTipoAccion(Constant.ACCION_ALERTA_OPERATIVA);
				tevtPaqueteMap.put("accion", "Se generó una acción de alerta operativa");
			}
			if (Constant.IND_ESTADO_DEVUELTO.equals(entity.get("idEstado"))) { 
				tevjPaqueteAccion.setTipoAccion(Constant.ACCION_PENDIENTE_REVISION);
				tevtPaqueteMap.put("accion", "Se generó una acción de pendiente revisión");
			}
			tevjPaqueteAccionRepository.save(tevjPaqueteAccion);
		}
		
		return Mono.just(tevtPaqueteMap);
	}

	@Override
	public Mono<Void> remove(String idPaquete) {
		TevtPaquete tevtPaquete = tevtPaqueteRepository.findByIdPaquete(idPaquete).block();
		if (tevtPaquete == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Paquete no Encontrado");
		}
		return tevtPaqueteRepository.delete(tevtPaquete);
	}

	@Override
	public Flux<Map<String, Object>> findByIdContains(Map<String, String> where) {
		List<Map<String, Object>> tevtPaqueteMapList = new ArrayList<>();
		
		Date fechaInicio = UtilConverter.toDate(where.get("fechaInicio"));
		Date fechaFin = UtilConverter.toDate(where.get("fechaFin"));
		
		Flux<Document> tevtPaqueteFlux = tevtPaqueteRepository.findIfContains(where, fechaInicio, fechaFin);
		
		tevtPaqueteFlux.map(document -> {
			Map<String, Object> resultMap = new HashMap<>();
			for(String key : document.keySet()) {
				resultMap.put(key, document.get(key));
			}
			return resultMap;
		}).collectList().block().stream().forEach(tevtPaqueteObject -> {
			try {
				Map<String, Object> tevtPaqueteMap = new HashMap<>();
				TevtPaquete tevtPaquete = UtilConverter.documentToClass(TevtPaquete.class, (Document) tevtPaqueteObject.get("tevt_paquete"));
				TevsCliente tevtCliente = UtilConverter.documentToClass(TevsCliente.class, (Document) tevtPaqueteObject.get("tevt_cliente"));
				TevtCiudad tevtCiudad = UtilConverter.documentToClass(TevtCiudad.class, (Document) tevtPaqueteObject.get("tevt_ciudad"));
				TevsDepartamento tevsDepartamento = UtilConverter.documentToClass(TevsDepartamento.class, (Document) tevtPaqueteObject.get("tevs_departamento"));
				TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) tevtPaqueteObject.get("tevn_estado"));
				tevtPaqueteMap = UtilConverter.classToMap(tevtPaquete);
				tevtPaqueteMap.put("cliente", tevtCliente.getNmCliente());
				tevtPaqueteMap.put("nmCiudad", tevtCiudad.getNmCiudad());
				tevtPaqueteMap.put("nmDepartamento", tevsDepartamento.getNmDepartamento());
				tevtPaqueteMap.put("nmEstado", tevnEstado.getNmEstado());
				tevtPaqueteMap.put("color", tevnEstado.getColor());
				tevtPaqueteMapList.add(tevtPaqueteMap);
			} catch (IllegalAccessException | InstantiationException e) {
				TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_PAQUETES);
				tevnErrorRepository.save(tevnError);
				throw new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información");
			}
		});
		
		Flux<Map<String, Object>> mapResultFlux = Flux.fromIterable(tevtPaqueteMapList);
		return mapResultFlux;
	}

	@Override
	public Mono<TevjPaqueteAccion> updatePaqueteAccion(Map<String, String> entity) {
		TevjPaqueteAccion tevjPaqueteAccion = tevjPaqueteAccionRepository.findById(entity.get("idPaqueteAccion")).block();
		if (tevjPaqueteAccion == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "Paquete no Encontrado");
		}
		tevjPaqueteAccion.setIdEstado(entity.get("idEstado"));
		return tevjPaqueteAccionRepository.save(tevjPaqueteAccion);
	}
	
}
