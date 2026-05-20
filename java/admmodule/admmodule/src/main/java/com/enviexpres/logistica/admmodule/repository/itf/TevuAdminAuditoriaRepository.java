package com.enviexpres.logistica.admmodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevuAdminAuditoria;

@Repository
public interface TevuAdminAuditoriaRepository extends ReactiveMongoRepository<TevuAdminAuditoria, String> {

}
