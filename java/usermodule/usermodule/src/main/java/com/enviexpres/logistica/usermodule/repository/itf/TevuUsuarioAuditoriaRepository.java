package com.enviexpres.logistica.usermodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevuUsuarioAuditoria;

@Repository
public interface TevuUsuarioAuditoriaRepository extends ReactiveMongoRepository<TevuUsuarioAuditoria, String>{

}
