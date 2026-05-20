package com.enviexpres.logistica.admmodule.service.imp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.admmodule.model.TevnEstado;
import com.enviexpres.logistica.admmodule.repository.itf.TevnEstadoRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevnEstadoService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilsGeneral;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnEstadoServiceImp implements TevnEstadoService {

	@Autowired
	private TevnEstadoRepository TevnEstadoRepository;
	
	@Override
	public Mono<TevnEstado> create(TevnEstado TevnEstado) {
		if(Objects.isNull(TevnEstado.getIdEstado())){
			Flux<TevnEstado> TevnEstadoFlux = TevnEstadoRepository.findAll();
			List<TevnEstado> TevnEstadoIfContains = TevnEstadoFlux.toStream()
					.filter(estado -> estado.getNmEstado().equals(TevnEstado.getNmEstado()) || estado.getSbEstado().equals(TevnEstado.getSbEstado()) || estado.getColor().equals(TevnEstado.getColor()))
					.collect(Collectors.toList());
			if(TevnEstado.getNmEstado().isEmpty() || TevnEstado.getSbEstado().isEmpty() || TevnEstado.getColor().isEmpty() || TevnEstado.getModulo().isEmpty()) return null;
			if(TevnEstadoIfContains.size() > 0) return null;
			if(TevnEstadoFlux.count().block() > 0L) {
				String valueId = UtilsGeneral.devolverConsecutivo4Digitos(TevnEstadoFlux.last().block().getIdEstado());
				TevnEstado.setIdEstado(valueId);
			}else {
				TevnEstado.setIdEstado("0001");
			}
			return TevnEstadoRepository.save(TevnEstado);
		}else {
			return TevnEstadoRepository.save(TevnEstado);
		}
	}

	@Override
	public Mono<TevnEstado> findById(String id) {
		return TevnEstadoRepository.findById(id);
	}

	@Override
	public Flux<TevnEstado> findAll() {
		return TevnEstadoRepository.findAll();
	}

	@Override
	public Mono<Void> remove(String id) {
		TevnEstado TevnEstado = TevnEstadoRepository.findById(id).block();
		if(TevnEstado.getModulo().equals(Constant.MODULO_TODOS)) {
			return null;
		} else {
			return TevnEstadoRepository.delete(TevnEstado);
		}
	}

	@Override
	public Flux<TevnEstado> findIfContains(Map<String, String> filter) {
		return TevnEstadoRepository.findIfContains(filter);
	}

}
