package com.enviexpres.logistica.packmodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.packmodule.model.TevuPaqueteAuditoria;

@Repository
public interface TevuPaqueteAuditoriaRepository extends ReactiveMongoRepository<TevuPaqueteAuditoria, String>{

}
