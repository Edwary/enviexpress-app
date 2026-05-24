package com.enviexpres.logistica.admmodule.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.admmodule.model.TevnEstado;
import com.enviexpres.logistica.admmodule.model.TevnVariableSistema;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.repository.itf.TevnVariableSistemaRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevnVariableSistemaService;
import com.enviexpres.logistica.admmodule.utils.Constant;
import com.enviexpres.logistica.admmodule.utils.UtilConverter;
import com.enviexpres.logistica.admmodule.utils.UtilsGeneral;
import com.enviexpres.logistica.admmodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnVariableSistemaServiceImp implements TevnVariableSistemaService {
    @Autowired TevnVariableSistemaRepository tevnVariableSistemaRepository;
    @Autowired TevnErrorRepository tevnErrorRepository;

    @Override
    public Mono<TevnVariableSistema> create(Map<String, String> entity) {
        String idVariable = entity.get("idVariable");
        
        // Si no hay ID, buscamos el último para generar el consecutivo
        Mono<TevnVariableSistema> sourceMono = StringUtils.isEmpty(idVariable) 
            ? tevnVariableSistemaRepository.findAll().reduce((first, second) -> second)
            : tevnVariableSistemaRepository.findByIdVariable(idVariable);

        return sourceMono.defaultIfEmpty(new TevnVariableSistema())
            .map(tvs -> {
                if (StringUtils.isEmpty(idVariable)) {
                    String lastId = (tvs.getIdVariable() == null) ? "0" : tvs.getIdVariable();
                    tvs.setIdVariable(UtilsGeneral.devolverConsecutivo4Digitos(lastId));
                }
                tvs.setNmVariable(entity.get("nmVariable"));
                tvs.setValor(entity.get("valor"));
                tvs.setDsVariable(entity.get("dsVariable"));
                tvs.setTipo(StringUtils.isEmpty(entity.get("tipo")) ? Constant.VARIABLE_TIPO_CONFIGURACION : entity.get("tipo"));
                tvs.setIdEstado(StringUtils.isEmpty(entity.get("idEstado")) ? Constant.IND_ESTADO_ACTIVO : entity.get("idEstado"));
                return tvs;
            }).flatMap(tevnVariableSistemaRepository::save);
    }

    @Override
    public Flux<Map<String, Object>> findIfContains(Map<String, String> filter) {
        return tevnVariableSistemaRepository.findIfContains(filter)
            .flatMap(doc -> {
                try {
                    Map<String, Object> map = new HashMap<>();
                    TevnVariableSistema tvs = UtilConverter.documentToClass(TevnVariableSistema.class, (Document) doc.get("tevn_variable_sistema"));
                    TevnEstado te = Objects.isNull(doc.get("tevn_estado")) ? null : UtilConverter.documentToClass(TevnEstado.class, (Document) doc.get("tevn_estado"));
                    
                    map = UtilConverter.classToMap(tvs);
                    map.put("nmEstado", Objects.isNull(te) ? "" : te.getNmEstado());
                    map.put("sbEstado", Objects.isNull(te) ? "" : te.getSbEstado());
                    map.put("colorEstado", Objects.isNull(te) ? "" : te.getColor());
                    return Mono.just(map);
                } catch (Exception e) {
                    return tevnErrorRepository.save(UtilConverter.createError(e, Constant.MODULO_ADM))
                        .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.SinInformacion")));
                }
            });
    }

    @Override
    public Mono<TevnVariableSistema> toggle(Map<String, String> entity) {
        return tevnVariableSistemaRepository.findById(entity.get("idVariable"))
            .map(tvs -> {
                tvs.setIdEstado(entity.get("idEstado"));
                return tvs;
            }).flatMap(tevnVariableSistemaRepository::save);
    }

    @Override
    public Mono<TevnVariableSistema> logicRemove(String id) {
        return tevnVariableSistemaRepository.findById(id)
            .map(tvs -> {
                tvs.setIdEstado(Constant.IND_ESTADO_ELIMINADO);
                return tvs;
            }).flatMap(tevnVariableSistemaRepository::save);
    }

    @Override 
    public Mono<Void> remove(String id) { 
    	return tevnVariableSistemaRepository.deleteById(id); 
    }
    
    @Override 
    public Mono<TevnVariableSistema> findById(String id) { 
    	return tevnVariableSistemaRepository.findById(id); 
    }
    
    @Override 
    public Flux<TevnVariableSistema> findAll() { 
    	return tevnVariableSistemaRepository.findAll(); 
    }

	@Override
	public Flux<TevnVariableSistema> createVarious(List<Map<String, Object>> entityList) {
		// TODO Auto-generated method stub
		return null;
	}
}