package com.enviexpres.logistica.packmodule.repository.itf;

import java.util.Date;
import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.packmodule.model.TevtPaquete;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevtPaqueteRepository extends ReactiveMongoRepository<TevtPaquete, String>{

	Mono<TevtPaquete> findTopByOrderByIdPaqueteDesc();
	
	@Query("{ 'idPaquete' : ?0 }")
	Mono<TevtPaquete> findByIdPaquete(String idPaquete);

	@Aggregation({
	    "{ $match: { 'idPaquete' : { $regex: ?0, $options: 'i' } } }",
	    "{ $lookup: { from: 'tevs_client', localField: 'idCliente', foreignField: 'idCliente', as: 'tevt_cliente' } }",
	    "{ $unwind: { path: '$tevt_cliente', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_destinatario', localField: 'destinatario', foreignField: 'idDestinatario', as: 'tevn_destinatario' } }",
	    "{ $unwind: { path: '$tevn_destinatario', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevt_ciudad', localField: 'idCiudad', foreignField: '_id', as: 'tevt_ciudad' } }",
	    "{ $unwind: { path: '$tevt_ciudad', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevs_departamento', localField: 'idDepartamento', foreignField: 'codigoDane', as: 'tevs_departamento' } }",
	    "{ $unwind: { path: '$tevs_departamento', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
	    "{ $project: { "
	    + "    	'tevt_paquete' : '$$ROOT', "
	    + "		'tevn_destinatario' : 1, "		
	    + "		'tevt_cliente' : 1, "
	    + "    	'tevt_ciudad' : 1, "
	    + "    	'tevs_departamento' : 1, "
	    + "    	'tevn_estado' : 1 "
	    + "} }"
	})
	Mono<Document> findIdPaqueteDocument(String idPaquete);
	
	@Aggregation({
	    "{ $match: { $and: [ "
						    + "    { $or: [ "
						    + "        { 'idPaquete' : { $regex: ?#{[0].get('idPaquete') == null ? '' : [0].get('idPaquete') }, $options: 'i' } }, "
						    + "        { 'idPaquete' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idCliente' : { $regex: ?#{[0].get('idCliente') == null ? '' : [0].get('idCliente') }, $options: 'i' } }, "
						    + "        { 'idCliente' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idCiudad' : { $regex: ?#{[0].get('idCiudad') == null ? '' : [0].get('idCiudad') }, $options: 'i' } }, "
						    + "        { 'idCiudad' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
						    + "        { 'idEstado' : { $exists: false } }"
						    + "    ] }, "
						    + "    { 'fechaCreacion' : { $gte: ?1, $lte: ?2 } } "
						    + " ] } "
	    + "}",
	    "{ $lookup: { from: 'tevs_client', localField: 'idCliente', foreignField: 'idCliente', as: 'tevt_cliente' } }",
	    "{ $unwind: { path: '$tevt_cliente', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_destinatario', localField: 'destinatario', foreignField: 'idDestinatario', as: 'tevn_destinatario' } }",
	    "{ $unwind: { path: '$tevn_destinatario', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevt_ciudad', localField: 'idCiudad', foreignField: '_id', as: 'tevt_ciudad' } }",
	    "{ $unwind: { path: '$tevt_ciudad', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevs_departamento', localField: 'idDepartamento', foreignField: 'codigoDane', as: 'tevs_departamento' } }",
	    "{ $unwind: { path: '$tevs_departamento', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
	    "{ $project: { "
				    + "    	'tevt_paquete' 		: '$$ROOT', "
				    + "		'tevn_destinatario'	: 1, "
				    + "    	'tevt_cliente' 		: 1, " 
				    + "    	'tevt_ciudad' 		: 1, "
				    + "    	'tevs_departamento'	: 1, "
				    + "    	'tevn_estado' 		: 1 "
	    + "} }"
	})
	Flux<Document> findIfContains(Map<String, String> where, Date fechaInicio, Date fechaFin);
	
	Mono<Long> countByIdEstado(String idEstado);
}
