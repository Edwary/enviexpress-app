package com.enviexpres.logistica.usermodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevnRol;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevnRolRepository extends ReactiveMongoRepository<TevnRol, String> {

	Mono<TevnRol> findTopByOrderByIdRolDesc();
	
	@Query("{ 'idRol' : ?0 }")
	Mono<TevnRol> findByIdRol(String idRol);
	
	@Aggregation({
	    "{ $match: { $and: [ "
						    + "    { $or: [ "
						    + "        { 'nombre' : { $regex: ?#{[0].get('nombre') == null ? '' : [0].get('nombre') }, $options: 'i' } }, "
						    + "        { 'nombre' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'sbRol' : { $regex: ?#{[0].get('sbRol') == null ? '' : [0].get('sbRol') }, $options: 'i' } }, "
						    + "        { 'sbRol' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idRol' : { $regex: ?#{[0].get('idRol') == null ? '' : [0].get('idRol') }, $options: 'i' } }, "
						    + "        { 'idRol' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
						    + "        { 'idEstado' : { $exists: false } }"
						    + "    ] }, "
						    + " ] } "
	    + "}",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
	    "{ $project: { "
				    + "		'tevn_rol'		: '$$ROOT', "
				    + "   	'tevn_estado'	: 1 "
	    + "} }"
	})
	Flux<Document> findIfContains(Map<String, String> where);
}
