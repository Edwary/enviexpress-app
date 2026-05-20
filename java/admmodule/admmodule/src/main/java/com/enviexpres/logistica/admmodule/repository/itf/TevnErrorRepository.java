package com.enviexpres.logistica.admmodule.repository.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevnError;

@Repository
public interface TevnErrorRepository extends ReactiveMongoRepository<TevnError, String> {

}