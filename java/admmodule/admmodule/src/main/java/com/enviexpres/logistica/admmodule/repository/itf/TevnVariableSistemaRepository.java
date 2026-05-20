package com.enviexpres.logistica.admmodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevnVariableSistema;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevnVariableSistemaRepository extends ReactiveMongoRepository<TevnVariableSistema, String>{

	@Query("{ 'idVariable' : ?0 }")
	Mono<TevnVariableSistema> findByIdVariable(String idVariable);
	
	@Aggregation({
		"{ $match : { $and: [ "
							+ "	{ $or: [ "
							+ "		{ 'nmVariable' : { $regex: ?#{[0].get('nmVariable') == null ? '' : [0].get('nmVariable') }, $options: 'i' } }, "
							+ "		{ 'nmVariable' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'tipo' : { $regex: ?#{[0].get('tipo') == null ? '' : [0].get('tipo') }, $options: 'i' } }, "
							+ "		{ 'tipo' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
							+ "		{ 'idEstado' : { $exists: false } }"
							+ "		] }, "
						+ "	] }"
		+ "}",
		"{ $lookup: { from: 'tvvn_variable_sistema', localField: 'idVariable', foreignField: 'idVariable', as: 'tvvn_variable_sistema' } }",
		"{ $unwind: { path: '$tvvn_variable_sistema', preserveNullAndEmptyArrays: true } }",
		"{ $lookup: { from: 'tvvn_estado', localField: 'idEstado', foreignField: '_id', as: 'tvvn_estado' } }",
		"{ $unwind: { path: '$tvvn_estado', preserveNullAndEmptyArrays: true } }",
		"{ $project: { 'tvvn_variable_sistema': 1, 'tvvn_estado': 2 } }"
	})
	Flux<Document> findIfContains(Map<String, String> where);
}
