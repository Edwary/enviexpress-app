package com.enviexpres.logistica.admmodule.service.imp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.vimodules.admmodule.model.TvvnEstado;
import co.com.vimodules.admmodule.repository.itf.TvvnEstadoRepository;
import co.com.vimodules.admmodule.service.itf.TvvnEstadoService;
import co.com.vimodules.admmodule.utils.UtilConverter;
import co.com.vimodules.admmodule.utils.ViConstant;
import co.com.vimodules.admmodule.utils.ViGeneral;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnEstadoServiceImp implements TvvnEstadoService {

	@Autowired
	private TvvnEstadoRepository tvvnEstadoRepository;
	
	@Override
	public Mono<TvvnEstado> create(TvvnEstado tvvnEstado) {
		if(Objects.isNull(tvvnEstado.getIdEstado())){
			Flux<TvvnEstado> tvvnEstadoFlux = tvvnEstadoRepository.findAll();
			List<TvvnEstado> tvvnEstadoIfContains = tvvnEstadoFlux.toStream()
					.filter(estado -> estado.getNmEstado().equals(tvvnEstado.getNmEstado()) || estado.getSbEstado().equals(tvvnEstado.getSbEstado()) || estado.getColor().equals(tvvnEstado.getColor()))
					.collect(Collectors.toList());
			if(tvvnEstado.getNmEstado().isEmpty() || tvvnEstado.getSbEstado().isEmpty() || tvvnEstado.getColor().isEmpty() || tvvnEstado.getModulo().isEmpty()) return null;
			if(tvvnEstadoIfContains.size() > 0) return null;
			if(tvvnEstadoFlux.count().block() > 0L) {
				String valueId = ViGeneral.devolverConsecutivo4Digitos(tvvnEstadoFlux.last().block().getIdEstado());
				tvvnEstado.setIdEstado(valueId);
			}else {
				tvvnEstado.setIdEstado("0001");
			}
			return tvvnEstadoRepository.save(tvvnEstado);
		}else {
			return tvvnEstadoRepository.save(tvvnEstado);
		}
	}

	@Override
	public Mono<TvvnEstado> findById(String id) {
		return tvvnEstadoRepository.findById(id);
	}

	@Override
	public Flux<TvvnEstado> findAll() {
		return tvvnEstadoRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		TvvnEstado tvvnEstado = tvvnEstadoRepository.findById(id).block();
		if(tvvnEstado.getModulo().equals(ViConstant.MODULO_TODOS)) {
			return null;
		} else {
			return tvvnEstadoRepository.delete(tvvnEstado);
		}
	}

	@Override
	public Flux<TvvnEstado> findIfContains(Map<String, String> filter) {
		return tvvnEstadoRepository.findIfContains(filter);
	}

}
