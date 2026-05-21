package com.enviexpres.logistica.clientmodule.repository.dto.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.clientmodule.model.dto.TevnError;

@Repository
public interface TevnErrorRepository extends ReactiveMongoRepository<TevnError, String>{

}
