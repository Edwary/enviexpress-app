package com.enviexpres.logistica.admmodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevpPais;

import reactor.core.publisher.Flux;

@Repository
public interface TevpPaisRepository extends ReactiveMongoRepository<TevpPais, String> {

	@Query("{ $and: [ "
			+ " { $or: [ { 'nmPais': { $regex: ?0, $options: 'i' } }, { 'nmPais': { $exists: false } } ] }, "
			+ " { $or: [ { 'sbPais': { $regex: ?1, $options: 'i' } }, { 'sbPais': { $exists: false } } ] }, "
			+ " { $or: [ { 'continente': { $regex: ?2, $options: 'i' } }, { 'continente': { $exists: false } } ] } "
			+ "] }")
 	Flux<TevpPais> findIfContains(String nmPais, String sbPais, String continente);
	
	@Aggregation({
		"{ $match : { $and: [ "
							+ "	{ $or: [ "
							+ "		{ 'nmPais' : { $regex: ?#{[0].get('nmPais') == null ? '' : [0].get('nmPais') }, $options: 'i' } }, "
							+ "		{ 'nmPais' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'sbPais' : { $regex: ?#{[0].get('sbPais') == null ? '' : [0].get('sbPais') }, $options: 'i' } }, "
							+ "		{ 'sbPais' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'continente' : { $regex: ?#{[0].get('continente') == null ? '' : [0].get('continente') }, $options: 'i' } }, "
							+ "		{ 'continente' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
							+ "		{ 'idEstado' : { $exists: false } }"
							+ "		] }, "
							+ "] }"
		+ "}",
		"{ $lookup: { from: 'tvvp_pais', localField: '_id', foreignField: '_id', as: 'tvvp_pais' } }",
		"{ $unwind: { path: '$tvvp_pais' } }",
		"{ $lookup: { from: 'tvvn_estado', localField: 'idEstado', foreignField: '_id', as: 'tvvn_estado' } }",
		"{ $unwind: { path: '$tvvn_estado' } }",
		"{ $project: { 'tvvp_pais': 1, 'tvvn_estado': 2 } }"
	})
 	Flux<Document> findObjectIfContains(Map<String, String> filter);
}
