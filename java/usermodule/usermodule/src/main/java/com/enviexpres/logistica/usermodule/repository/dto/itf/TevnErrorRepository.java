package com.enviexpres.logistica.usermodule.repository.dto.itf;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.dto.TevnError;

@Repository
public interface TevnErrorRepository extends ReactiveMongoRepository<TevnError, String>{

}
