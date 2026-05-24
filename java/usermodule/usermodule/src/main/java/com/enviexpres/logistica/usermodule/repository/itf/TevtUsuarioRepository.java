package com.enviexpres.logistica.usermodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevtUsuario;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevtUsuarioRepository extends ReactiveMongoRepository<TevtUsuario, String>{

	Mono<TevtUsuario> findTopByOrderByNusDesc();

	@Query("{ 'nus' : ?0 }")
	Mono<TevtUsuario> findByNus(String nus);
	
	@Query("{ 'nmUsuario' : ?0 }")
	Mono<TevtUsuario> findByNmUsuario(String nmUsuario);
	
	@Query("{ 'email' : ?0 }")
	Mono<TevtUsuario> findByEmailUsuario(String emailUsuario);
	
	Mono<TevtUsuario> findByNmUsuarioOrEmail(String nmUsuario, String email);
	
	@Aggregation({
	    "{ $match: { $and: [ "
						    + "    { $or: [ "
						    + "        { 'nus' : { $regex: ?#{[0].get('nus') == null ? '' : [0].get('nus') }, $options: 'i' } }, "
						    + "        { 'nus' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'nmUsuario' : { $regex: ?#{[0].get('nmUsuario') == null ? '' : [0].get('nmUsuario') }, $options: 'i' } }, "
						    + "        { 'nmUsuario' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'nombre' : { $regex: ?#{[0].get('nombre') == null ? '' : [0].get('nombre') }, $options: 'i' } }, "
						    + "        { 'nombre' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'email' : { $regex: ?#{[0].get('email') == null ? '' : [0].get('email') }, $options: 'i' } }, "
						    + "        { 'email' : { $exists: false } }"
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
	    "{ $lookup: { from: 'tevn_rol', localField: 'idRol', foreignField: 'idRol', as: 'tevn_rol' } }",
	    "{ $unwind: { path: '$tevn_rol', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
	    "{ $project: { "
				    + "   	'tevt_usuario'	: '$$ROOT', "
				    + "		'tevn_rol'		: 1, "
				    + "   	'tevn_estado'	: 1 "
	    + "} }"
	})
	Flux<Document> findIfContains(Map<String, String> where);
}
