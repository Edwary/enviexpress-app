package com.enviexpres.logistica.admmodule.service.imp;

import java.util.Map;

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
        if (TevnEstado.getIdEstado() != null) {
            return TevnEstadoRepository.save(TevnEstado);
        }

        // Validación básica
        if (TevnEstado.getNmEstado().isEmpty() || TevnEstado.getSbEstado().isEmpty() 
            || TevnEstado.getColor().isEmpty() || TevnEstado.getModulo().isEmpty()) {
            return Mono.empty();
        }

        return TevnEstadoRepository.findAll()
            .collectList()
            .flatMap(listaEstados -> {
                // Verificar si ya existe por nombre, abreviatura o color
                boolean existe = listaEstados.stream().anyMatch(e -> 
                    e.getNmEstado().equals(TevnEstado.getNmEstado()) || 
                    e.getSbEstado().equals(TevnEstado.getSbEstado()) || 
                    e.getColor().equals(TevnEstado.getColor())
                );

                if (existe) return Mono.empty();

                // Generar ID consecutivo
                if (!listaEstados.isEmpty()) {
                    TevnEstado ultimo = listaEstados.get(listaEstados.size() - 1);
                    TevnEstado.setIdEstado(UtilsGeneral.devolverConsecutivo4Digitos(ultimo.getIdEstado()));
                } else {
                    TevnEstado.setIdEstado("0001");
                }
                return TevnEstadoRepository.save(TevnEstado);
            });
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
        return TevnEstadoRepository.findById(id)
            .flatMap(estado -> {
                if (estado.getModulo().equals(Constant.MODULO_TODOS)) {
                    return Mono.empty(); 
                }
                return TevnEstadoRepository.delete(estado);
            });
    }

    @Override
    public Flux<TevnEstado> findIfContains(Map<String, String> filter) {
        return TevnEstadoRepository.findIfContains(filter);
    }
}