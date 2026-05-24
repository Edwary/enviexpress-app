package com.enviexpres.logistica.clientmodule.service.imp;

import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.clientmodule.model.TevsCliente;
import com.enviexpres.logistica.clientmodule.model.dto.TevnError;
import com.enviexpres.logistica.clientmodule.model.dto.TevnEstado;
import com.enviexpres.logistica.clientmodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.clientmodule.repository.itf.TevsClienteRepository;
import com.enviexpres.logistica.clientmodule.service.itf.TevsClienteService;
import com.enviexpres.logistica.clientmodule.utils.Constant;
import com.enviexpres.logistica.clientmodule.utils.UtilConverter;
import com.enviexpres.logistica.clientmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.clientmodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevsClienteServiceImp implements TevsClienteService {

	@Autowired
	private TevsClienteRepository tevsClienteRepository;

	@Autowired
	private TevnErrorRepository tevnErrorRepository;
	
	@Override
	public Mono<TevsCliente> create(Map<String, String> entity) {
		if (StringUtils.isEmpty(entity.get("nmCliente")) || 
			StringUtils.isEmpty(entity.get("documento")) || 
			StringUtils.isEmpty(entity.get("email")) || 
			StringUtils.isEmpty(entity.get("telefono"))) {
			return Mono.error(new ValidationException(HttpStatus.NOT_ACCEPTABLE, "Datos Incompletos"));
		}

		String idCliente = entity.get("idCliente");

		Mono<TevsCliente> sourceMono = StringUtils.isEmpty(idCliente)
			? tevsClienteRepository.findTopByOrderByIdClienteDesc()
				.map(last -> {
					TevsCliente nuevo = new TevsCliente();
					nuevo.setIdCliente(UtilsGeneral.devolverConsecutivo12Digitos(last == null ? "0" : last.getIdCliente()));
					return nuevo;
				})
				.defaultIfEmpty(crearClienteInicial())
			: tevsClienteRepository.findByIdClient(idCliente)
				.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Cliente no encontrado")));

		return sourceMono.map(tevsCliente -> {
			tevsCliente.setNmCliente(entity.get("nmCliente"));
			tevsCliente.setDocumento(entity.get("documento"));
			tevsCliente.setTipoDocumento(entity.get("tipoDocumento"));
			tevsCliente.setEmail(entity.get("email"));
			tevsCliente.setTelefono(entity.get("telefono"));
			tevsCliente.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
			return tevsCliente;
		}).flatMap(tevsClienteRepository::save);
	}

	@Override
	public Mono<TevsCliente> findById(String idCliente) {
		return tevsClienteRepository.findByIdClient(idCliente)
			.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "No se encontró el cliente")));
	}

	@Override
	public Flux<TevsCliente> findAll() {
		return tevsClienteRepository.findAll();
	}

	@Override
	public Mono<TevsCliente> toggle(Map<String, String> entity) {
		return tevsClienteRepository.findByIdClient(entity.get("idCliente"))
			.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "No se encontró el cliente")))
			.flatMap(tevsCliente -> {
				tevsCliente.setIdEstado(entity.get("idEstado"));
				return tevsClienteRepository.save(tevsCliente);
			});
	}

	@Override
	public Mono<Void> remove(String idCliente) {
		return tevsClienteRepository.findByIdClient(idCliente)
			.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "No se encontró el cliente")))
			.flatMap(tevsClienteRepository::delete);
	}
	
	private TevsCliente crearClienteInicial() {
		TevsCliente nuevo = new TevsCliente();
		nuevo.setIdCliente(UtilsGeneral.devolverConsecutivo12Digitos("0"));
		return nuevo;
	}

	@Override
	public Flux<Map<String, Object>> findIfContains(Map<String, String> where) {
		return tevsClienteRepository.findIfContains(where)
		        .flatMap(document -> {
		            try {
		                TevsCliente tevsCliente = UtilConverter.documentToClass(TevsCliente.class, (Document) document.get("tevs_cliente"));
		                TevnEstado tevnEstado = UtilConverter.documentToClass(TevnEstado.class, (Document) document.get("tevn_estado"));
		                
		                Map<String, Object> tevsClienteMap = UtilConverter.classToMap(tevsCliente);
		                tevsClienteMap.put("nmEstado", tevnEstado.getNmEstado());
		                tevsClienteMap.put("color", tevnEstado.getColor());
		                
		                return Mono.just(tevsClienteMap);
		            } catch (Exception e) {
		                TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_PAQUETES);
		                return tevnErrorRepository.save(tevnError)
		                    .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Sin Información")));
		            }
		        });
	}
	
}