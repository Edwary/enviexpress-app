package com.enviexpres.logistica.clientmodule.repository.itf;

import java.util.Date;
import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.clientmodule.model.TevsCliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevsClienteRepository extends ReactiveMongoRepository<TevsCliente, String>{

	Mono<TevsCliente> findTopByOrderByIdClienteDesc();
	
	@Query("{ 'idCliente' : ?0 }")
	Mono<TevsCliente> findByIdClient(String idCliente);
	
	@Aggregation({
	    "{ $match: { $and: [ "
						    + "    { $or: [ "
						    + "        { 'idCliente' : { $regex: ?#{[0].get('idCliente') == null ? '' : [0].get('idCliente') }, $options: 'i' } }, "
						    + "        { 'idCliente' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'nmCliente' : { $regex: ?#{[0].get('nmCliente') == null ? '' : [0].get('nmCliente') }, $options: 'i' } }, "
						    + "        { 'nmCliente' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'documento' : { $regex: ?#{[0].get('documento') == null ? '' : [0].get('documento') }, $options: 'i' } }, "
						    + "        { 'documento' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'email' : { $regex: ?#{[0].get('email') == null ? '' : [0].get('email') }, $options: 'i' } }, "
						    + "        { 'email' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'telefono' : { $regex: ?#{[0].get('telefono') == null ? '' : [0].get('telefono') }, $options: 'i' } }, "
						    + "        { 'telefono' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
						    + "        { 'idEstado' : { $exists: false } }"
						    + "    ] }, "
						    + " ] } "
	    + "}",
	    "{ $lookup: { from: 'tevs_client', localField: 'idCliente', foreignField: 'idCliente', as: 'tevs_cliente' } }",
	    "{ $unwind: { path: '$tevs_cliente', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
	    "{ $project: { "
				    + "    'tevs_cliente' : 1, " 
				    + "    'tevn_estado' : 1 "
	    + "} }"
	})
	Flux<Document> findIfContains(Map<String, String> where);
}
