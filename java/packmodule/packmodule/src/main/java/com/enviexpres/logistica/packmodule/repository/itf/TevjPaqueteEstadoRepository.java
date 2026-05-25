package com.enviexpres.logistica.packmodule.repository.itf;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.packmodule.model.TevjPaqueteEstado;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevjPaqueteEstadoRepository extends ReactiveMongoRepository<TevjPaqueteEstado, String>{

	@Aggregation({
	    "{ $match: { "
				    + "    $or: [ "
				    + "        { 'idPaquete' : { $regex: ?0, $options: 'i' } }, "
				    + "        { 'idPaquete' : { $exists: false } }"
				    + "    ] "
				    + "} }",
		
		"{ $sort: { 'fechaEstado': -1 } }",
	    
		"{ $lookup: { from: 'tevt_paquete', localField: 'idPaquete', foreignField: 'idPaquete', as: 'tevt_paquete' } }",
	    "{ $unwind: { path: '$tevt_paquete', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevt_usuario', localField: 'nus', foreignField: 'nus', as: 'tevt_usuario' } }",
	    "{ $unwind: { path: '$tevt_usuario', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstadoAnterior', foreignField: '_id', as: 'tevn_estado_ant' } }",
	    "{ $unwind: { path: '$tevn_estado_ant', preserveNullAndEmptyArrays: true } }",
	    "{ $project: { "
			    + "    'tevj_paquete_estado' : '$$ROOT', "
			    + "    'tevt_paquete' : 1, "
			    + "    'tevn_estado' : 1, "
			    + "    'tevn_estado_ant' : 1 "
	    + "} }"
	})
	Flux<Document> findByIdPaquete(String idPaquete);
	
	Mono<TevjPaqueteEstado> findTopByIdPaqueteOrderByFechaEstadoDesc(String idPaquete);
	
}
