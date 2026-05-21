package com.enviexpres.logistica.clientmodule.service.imp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.clientmodule.model.TevsCliente;
import com.enviexpres.logistica.clientmodule.repository.itf.TevsClienteRepository;
import com.enviexpres.logistica.clientmodule.service.itf.TevsClienteService;
import com.enviexpres.logistica.clientmodule.utils.Constant;
import com.enviexpres.logistica.clientmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.clientmodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevsClienteServiceImp implements TevsClienteService {

	@Autowired
	private TevsClienteRepository tevsClienteRepository;

	@Override
	public Mono<TevsCliente> create(Map<String, String> entity) {
		TevsCliente tevsCliente = new TevsCliente();
		
		if (StringUtils.isEmpty(entity.get("idCliente"))) {
			String lastId = tevsClienteRepository.findTopByOrderByIdClienteDesc().block().getIdCliente();
			tevsCliente.setIdCliente(UtilsGeneral.devolverConsecutivo12Digitos(lastId));
		} else {
			tevsCliente = tevsClienteRepository.findByIdClient(entity.get("idCliente")).block();
		}
		if (StringUtils.isEmpty(entity.get("nmCliente")) || StringUtils.isEmpty(entity.get("documento")) || StringUtils.isEmpty(entity.get("email")) || StringUtils.isEmpty("telefono")) {
			throw new ValidationException(HttpStatus.NOT_ACCEPTABLE, "Datos Incompletos");
		}
		tevsCliente.setNmCliente(entity.get("nmCliente"));
		tevsCliente.setDocumento(entity.get("documento"));
		tevsCliente.setTipoDocumento(entity.get("tipoDocumento"));
		tevsCliente.setEmail(entity.get("email"));
		tevsCliente.setTelefono(entity.get("telefono"));
		tevsCliente.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
		return tevsClienteRepository.save(tevsCliente);
	}

	@Override
	public Mono<TevsCliente> findById(String idCliente) {
		Mono<TevsCliente> tevsClienteMono = tevsClienteRepository.findByIdClient(idCliente);
		if (tevsClienteMono.block() == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "No se encontró el cliente");
		}
		return tevsClienteMono;
	}

	@Override
	public Flux<TevsCliente> findAll() {
		return tevsClienteRepository.findAll();
	}

	@Override
	public Mono<TevsCliente> toggle(Map<String, String> entity) {
		TevsCliente tevsCliente = tevsClienteRepository.findByIdClient(entity.get("idCliente")).block();
		if (tevsCliente == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "No se encontró el cliente");
		}
		tevsCliente.setIdEstado(entity.get("idEstado"));
		return tevsClienteRepository.save(tevsCliente);
	}

	@Override
	public Mono<Void> remove(String idCliente) {
		TevsCliente tevsCliente = tevsClienteRepository.findByIdClient(idCliente).block();
		if (tevsCliente == null) {
			throw new ValidationException(HttpStatus.NOT_FOUND, "No se encontró el cliente");
		}
		return tevsClienteRepository.delete(tevsCliente);
	}
	
	
}
