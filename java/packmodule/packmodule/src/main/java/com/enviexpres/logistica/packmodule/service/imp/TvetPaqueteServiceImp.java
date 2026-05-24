package com.enviexpres.logistica.packmodule.service.imp;

import java.util.Date;
import java.util.HashMap;
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
	    Mono<TevtPaquete> paqueteMono;
	    
	    if (StringUtils.isEmpty(entity.get("idPaquete"))) { 
	        TevtPaquete nuevo = new TevtPaquete();
	        ObjectId uuid = new ObjectId();
	        nuevo.setUuid(uuid.toHexString());
	        nuevo.setIdPaquete(UtilConverter.convertObjectIdToNumber(uuid.toHexString()));
	        nuevo.setFechaCreacion(UtilConverter.currentDate());
	        paqueteMono = Mono.just(nuevo);
	    } else {
	        paqueteMono = tevtPaqueteRepository.findByIdPaquete(entity.get("idPaquete"))
	            .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Paquete base no encontrado")));
	    }

	    return paqueteMono.flatMap(tevtPaquete -> {
	        tevtPaquete.setIdCliente(entity.get("idCliente"));
	        tevtPaquete.setDestinatario(entity.get("destinatario"));
	        tevtPaquete.setDireccion(entity.get("direccion"));
	        tevtPaquete.setIdCiudad(entity.get("idCiudad"));
	        tevtPaquete.setIdDepartamento(entity.get("idDepartamento"));
	        tevtPaquete.setTelefono(entity.get("idTelefono"));
	        tevtPaquete.setPeso(entity.get("peso"));
	        tevtPaquete.setValorDeclarado(entity.get("valorDeclarado"));
	        tevtPaquete.setIdEstado(Constant.IND_ESTADO_REGISTRADO);
	        
	        // Guardamos paquete y luego el estado inicial de manera encadenada
	        return tevtPaqueteRepository.save(tevtPaquete).flatMap(paqueteGuardado -> {
	            TevjPaqueteEstado tevjPaqueteEstado = new TevjPaqueteEstado();
	            tevjPaqueteEstado.setIdPaquete(paqueteGuardado.getIdPaquete());
	            tevjPaqueteEstado.setIdEstado(Constant.IND_ESTADO_REGISTRADO);
	            tevjPaqueteEstado.setIdEstadoAnterior(Constant.IND_ESTADO_NUEVO);
	            tevjPaqueteEstado.setNus(entity.get("nus"));
	            tevjPaqueteEstado.setFechaEstado(UtilsGeneral.currentDate());
	            
	            return tevjPaqueteEstadoRepository.save(tevjPaqueteEstado).thenReturn(paqueteGuardado);
	        });
	    });
	}

	@Override
	public Mono<Map<String, Object>> findById(String idPaquete) {
	    return tevtPaqueteRepository.findIdPaqueteDocument(idPaquete)
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Paquete no encontrado")))
	        .flatMap(tevtPaqueteDocument -> {
	            try {
	                TevtPaquete tevtPaquete = UtilConverter.documentToClass(TevtPaquete.class, (Document) tevtPaqueteDocument.get("tvet_paquete"));
	                TevtCiudad tevtCiudad = UtilConverter.documentToClass(TevtCiudad.class, (Document) tevtPaqueteDocument.get("tvet_ciudad"));
	                TevsDepartamento tevsDepartamento = UtilConverter.documentToClass(TevsDepartamento.class, (Document) tevtPaqueteDocument.get("tves_departamento"));
	                
	                Map<String, Object> tevtPaqueteMap = UtilConverter.classToMap(tevtPaquete);
	                tevtPaqueteMap.put("nmCiudad", tevtCiudad.getNmCiudad());
	                tevtPaqueteMap.put("codigoPostal", tevtCiudad.getCodigoPostal());
	                tevtPaqueteMap.put("nmDepartamento", tevsDepartamento.getNmDepartamento());
	                tevtPaqueteMap.put("codigoDane", tevsDepartamento.getCodigoPostal());

	                // Obtener estados asíncronamente
	                return tevjPaqueteEstadoRepository.findByIdPaquete(idPaquete)
	                    .flatMap(document -> {
	                        try {
	                            Map<String, Object> tevjPaqueteEstadoMap = new HashMap<>();
	                            TevjPaqueteEstado estado = UtilConverter.documentToClass(TevjPaqueteEstado.class, (Document) document.get("tevj_paquete_estado"));
	                            TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) document.get("tevn_estado"));
	                            
	                            tevjPaqueteEstadoMap.put("uuid", estado.getUuid());
	                            tevjPaqueteEstadoMap.put("fecha", estado.getFechaEstado());
	                            tevjPaqueteEstadoMap.put("observaciones", estado.getObservaciones());
	                            tevjPaqueteEstadoMap.put("idEstado", tevnEstado.getIdEstado());
	                            tevjPaqueteEstadoMap.put("nmEstado", tevnEstado.getNmEstado());
	                            tevjPaqueteEstadoMap.put("color", tevnEstado.getColor());
	                            return Mono.just(tevjPaqueteEstadoMap);
	                        } catch (Exception e) {
	                            return Mono.error(e);
	                        }
	                    })
	                    .collectList()
	                    .map(estadosList -> {
	                        tevtPaqueteMap.put("estados", estadosList);
	                        return tevtPaqueteMap;
	                    });

	            } catch (Exception e) {
	                TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_PAQUETES);
	                return tevnErrorRepository.save(tevnError)
	                    .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información")));
	            }
	        });
	}

	@Override
	public Flux<TevtPaquete> findAll() {
	    return tevtPaqueteRepository.findAll();
	}

	@Override
	public Mono<Map<String, Object>> toggle(Map<String, String> entity) {
	    Mono<TevtPaquete> paqueteMono = tevtPaqueteRepository.findByIdPaquete(entity.get("idPaquete"))
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Paquete no Encontrado")));
	        
	    Mono<TevjPaqueteEstado> estadoAnteriorMono = tevjPaqueteEstadoRepository.findTopByIdPaqueteOrderByFechaEstadoDesc(entity.get("idPaquete"))
	        .defaultIfEmpty(new TevjPaqueteEstado());

	    // Buscamos paquete y su último estado en paralelo
	    return Mono.zip(paqueteMono, estadoAnteriorMono).flatMap(tuple -> {
	        TevtPaquete tevtPaquete = tuple.getT1();
	        TevjPaqueteEstado tevjPaqueteEstado = tuple.getT2();
	        String nuevoIdEstado = entity.get("idEstado");

	        // Validaciones de negocio
	        if (Constant.IND_ESTADO_EN_REPARTO.equals(nuevoIdEstado) && Constant.IND_ESTADO_ENTREGADO.equals(tevjPaqueteEstado.getIdEstado())) {
	            return Mono.error(new ValidationException(HttpStatus.CONFLICT, "Paquete ya se encuentra entregado"));
	        }
	        if (Constant.IND_ESTADO_CANCELADO.equals(tevjPaqueteEstado.getIdEstado())) {
	            return Mono.error(new ValidationException(HttpStatus.EXPECTATION_FAILED, "Paquete está cancelado"));
	        }
	        if (Constant.IND_ESTADO_DEVUELTO.equals(tevjPaqueteEstado.getIdEstado()) && Constant.IND_ESTADO_ENTREGADO.equals(nuevoIdEstado)) {
	            return Mono.error(new ValidationException(HttpStatus.I_AM_A_TEAPOT, "Un paquete devuelto no puede marcarse como entregado"));
	        }
	        if (Constant.IND_ESTADO_ENTREGADO.equals(nuevoIdEstado) && StringUtils.isEmpty(tevtPaquete.getDestinatario())) {
	            return Mono.error(new ValidationException(HttpStatus.NOT_ACCEPTABLE, "No se puede marcar como entregado porque el nombre del destinatario está vació"));
	        }
	        if (Constant.IND_ESTADO_NOVEDAD.equals(nuevoIdEstado) && StringUtils.isEmpty(tevjPaqueteEstado.getObservaciones())) {
	            return Mono.error(new ValidationException(HttpStatus.NOT_ACCEPTABLE, "No se puede marcar como novedad porque no hay observación"));
	        }

	        TevjPaqueteEstado tevjPaqueteEstadoNuevo = new TevjPaqueteEstado();
	        tevjPaqueteEstadoNuevo.setIdPaquete(tevtPaquete.getIdPaquete());
	        tevjPaqueteEstadoNuevo.setIdEstado(nuevoIdEstado);
	        tevjPaqueteEstadoNuevo.setIdEstadoAnterior(tevjPaqueteEstado.getIdEstado());
	        tevjPaqueteEstadoNuevo.setFechaEstado(UtilsGeneral.currentDate());
	        tevjPaqueteEstadoNuevo.setNus(entity.get("nus"));

	        // Guardar nuevo estado
	        return tevjPaqueteEstadoRepository.save(tevjPaqueteEstadoNuevo).flatMap(savedEstado -> {
	            tevtPaquete.setIdEstado(savedEstado.getIdEstado());
	            
	            // Guardar paquete actualizado
	            return tevtPaqueteRepository.save(tevtPaquete).flatMap(savedPaquete -> {
	                try {
	                    Map<String, Object> mapResult = UtilConverter.classToMap(savedPaquete);
	                    mapResult.put("avanceEstado", "Se ha actualizado correctamente el estado del paquete");

	                    // Evaluar creación de Acción
	                    if (Constant.IND_ESTADO_ENTREGADO.equals(nuevoIdEstado) || Constant.IND_ESTADO_NOVEDAD.equals(nuevoIdEstado) || Constant.IND_ESTADO_DEVUELTO.equals(nuevoIdEstado)) {
	                        
	                        return tevjPaqueteAccionRepository.findTopByOrderByIdPaqueteAccionDesc()
	                            .map(TevjPaqueteAccion::getIdEstado)
	                            .defaultIfEmpty("0")
	                            .flatMap(lastIdAccion -> {
	                                TevjPaqueteAccion accion = new TevjPaqueteAccion();
	                                accion.setIdEstado(UtilsGeneral.devolverConsecutivo12Digitos(lastIdAccion));
	                                accion.setIdPaqueteAccion(savedPaquete.getIdPaquete());
	                                accion.setNus(entity.get("nus"));
	                                accion.setIdEstado(Constant.IND_ESTADO_PENDIENTE);
	                                accion.setFechaCreacion(UtilConverter.currentDate());
	                                
	                                if (Constant.IND_ESTADO_ENTREGADO.equals(nuevoIdEstado)) { 
	                                    accion.setTipoAccion(Constant.ACCION_LIQUIDACION);
	                                    mapResult.put("accion", "Se generó una acción de liquidación");
	                                } else if (Constant.IND_ESTADO_NOVEDAD.equals(nuevoIdEstado)) { 
	                                    accion.setTipoAccion(Constant.ACCION_ALERTA_OPERATIVA);
	                                    mapResult.put("accion", "Se generó una acción de alerta operativa");
	                                } else if (Constant.IND_ESTADO_DEVUELTO.equals(nuevoIdEstado)) { 
	                                    accion.setTipoAccion(Constant.ACCION_PENDIENTE_REVISION);
	                                    mapResult.put("accion", "Se generó una acción de pendiente revisión");
	                                }
	                                
	                                return tevjPaqueteAccionRepository.save(accion).thenReturn(mapResult);
	                            });
	                    }
	                    return Mono.just(mapResult);
	                } catch (Exception e) {
	                    return Mono.error(new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al mapear la respuesta"));
	                }
	            });
	        });
	    });
	}

	@Override
	public Mono<Void> remove(String idPaquete) {
	    return tevtPaqueteRepository.findByIdPaquete(idPaquete)
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Paquete no Encontrado")))
	        .flatMap(tevtPaqueteRepository::delete);
	}

	@Override
	public Flux<Map<String, Object>> findByIdContains(Map<String, String> where) {
	    Date fechaInicio = UtilConverter.toDate(where.get("fechaInicio"));
	    Date fechaFin = UtilConverter.toDate(where.get("fechaFin"));
	    
	    return tevtPaqueteRepository.findIfContains(where, fechaInicio, fechaFin)
	        .flatMap(document -> {
	            try {
	                TevtPaquete tevtPaquete = UtilConverter.documentToClass(TevtPaquete.class, (Document) document.get("tevt_paquete"));
	                TevsCliente tevtCliente = UtilConverter.documentToClass(TevsCliente.class, (Document) document.get("tevt_cliente"));
	                TevtCiudad tevtCiudad = UtilConverter.documentToClass(TevtCiudad.class, (Document) document.get("tevt_ciudad"));
	                TevsDepartamento tevsDepartamento = UtilConverter.documentToClass(TevsDepartamento.class, (Document) document.get("tevs_departamento"));
	                TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) document.get("tevn_estado"));
	                
	                Map<String, Object> tevtPaqueteMap = UtilConverter.classToMap(tevtPaquete);
	                tevtPaqueteMap.put("cliente", tevtCliente.getNmCliente());
	                tevtPaqueteMap.put("nmCiudad", tevtCiudad.getNmCiudad());
	                tevtPaqueteMap.put("nmDepartamento", tevsDepartamento.getNmDepartamento());
	                tevtPaqueteMap.put("nmEstado", tevnEstado.getNmEstado());
	                tevtPaqueteMap.put("color", tevnEstado.getColor());
	                
	                return Mono.just(tevtPaqueteMap);
	            } catch (Exception e) {
	                TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_PAQUETES);
	                return tevnErrorRepository.save(tevnError)
	                    .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información")));
	            }
	        });
	}

	@Override
	public Mono<TevjPaqueteAccion> updatePaqueteAccion(Map<String, String> entity) {
	    return tevjPaqueteAccionRepository.findById(entity.get("idPaqueteAccion"))
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Paquete no Encontrado")))
	        .flatMap(tevjPaqueteAccion -> {
	            tevjPaqueteAccion.setIdEstado(entity.get("idEstado"));
	            return tevjPaqueteAccionRepository.save(tevjPaqueteAccion);
	        });
	}

	@Override
	public Mono<Long> findByIdEstado(String idEstado) {
	    return tevtPaqueteRepository.countByIdEstado(idEstado)
	    		.defaultIfEmpty(0L);
	}
	
}
