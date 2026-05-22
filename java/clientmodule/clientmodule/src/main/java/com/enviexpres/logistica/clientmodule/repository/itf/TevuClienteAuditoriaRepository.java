package com.enviexpres.logistica.clientmodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.clientmodule.model.TevuClienteAuditoria;

@Repository
public interface TevuClienteAuditoriaRepository extends ReactiveMongoRepository<TevuClienteAuditoria, String>{

}
