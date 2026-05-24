package com.enviexpres.logistica.admmodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevnMenu;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevnMenuRepository extends ReactiveMongoRepository<TevnMenu, String> {
	
	@Query("{ 'idMenu': ?0 }")
	Mono<TevnMenu> findByIdMenu(String idMenu);
	
	@Query("{ 'indVisible' : ?0 }")
	Flux<TevnMenu> findAllVisible(String indVisible);
	
	@Aggregation({
	    "{ $match : { $and: [ "
	        + " { 'tipoMenu' : { $ne : 'PANTALLA' } }, "
	        + " { $or: [ "
	        + "     { 'nmMenu' : { $regex: ?#{[0].get('nmMenu') == null ? '' : [0].get('nmMenu') }, $options: 'i' } }, "
	        + "     { 'nmMenu' : { $exists: false } }"
	        + "     ] }, "
	        + "] }}",
	    "{ $addFields: { 'orden_num': { $toInt: '$orden' } } }",
	    "{ $sort: { 'orden_num': 1 } }",
	    "{ $project: { 'orden_num': 0 } }" 
	})
	Flux<TevnMenu> findObjectIfContains(Map<String, String> filter);
	
	@Aggregation({
	    "{ $match: { $and: [ "
						    + "    { $or: [ "
						    + "        { 'nmMEnu' : { $regex: ?#{[0].get('nmMEnu') == null ? '' : [0].get('nmMEnu') }, $options: 'i' } }, "
						    + "        { 'nmMEnu' : { $exists: false } }"
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
				    + "		'tevn_menu'		: '$$ROOT', "
				    + "   	'tevn_estado'	: 1 "
	    + "} }"
	})
	Flux<Document> findIfContains(Map<String, String> where);
}
