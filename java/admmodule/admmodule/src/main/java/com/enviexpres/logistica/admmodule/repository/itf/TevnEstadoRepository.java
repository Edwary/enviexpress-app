package com.enviexpres.logistica.admmodule.repository.itf;

import java.util.Map;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevnEstado;

import reactor.core.publisher.Flux;

@Repository
public interface TevnEstadoRepository extends ReactiveMongoRepository<TevnEstado, String> {

	@Query("{ $and: ["
			+ "	{ $or: [ "
			+ "		{ 'nmEstado' : { $regex: ?#{[0].get('nmEstado') == null ? '' : [0].get('nmEstado') }, $options: 'i' } }, "
			+ "		{ 'nmEstado' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'sbEstado' : { $regex: ?#{[0].get('sbEstado') == null ? '' : [0].get('sbEstado') }, $options: 'i' } }, "
			+ "		{ 'sbEstado' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'color' : { $regex: ?#{[0].get('color') == null ? '' : [0].get('color') }, $options: 'i' } }, "
			+ "		{ 'color' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'modulo' : { $regex: ?#{[0].get('modulo') == null ? '' : [0].get('modulo') }, $options: 'i' } }, "
			+ "		{ 'modulo' : { $exists: false } }"
			+ "		] }, "
			+ "] }")
	Flux<TevnEstado> findIfContains(Map<String, String> filter);
	
}
