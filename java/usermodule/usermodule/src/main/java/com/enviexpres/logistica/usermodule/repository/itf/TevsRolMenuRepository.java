package com.enviexpres.logistica.usermodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.usermodule.model.TevsRolMenu;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TevsRolMenuRepository extends ReactiveMongoRepository<TevsRolMenu, String> {

	Mono<TevsRolMenu> findTopByOrderByIdRolMenuDesc();
	
	@Query("{ 'idRolMenu' : ?0 }")
	Mono<TevsRolMenu> findByIdRolMenu(String idRolMenu);
	
	@Query("{ 'idRol' : ?0 }")
	Flux<TevsRolMenu> findByIdRol(String idRol);
	
	@Aggregation({
		"{ $match: { $and: [ "
							+ " { $or: [ "
							+ "		{ 'idRol' : ?0 }, "
							+ " ] } "
							+ " ] } "
		+ "}",
		"{ $lookup: { from: 'tevs_rol_menu', localField: 'idRolMenu', foreignField: 'idRolMenu', as: 'tevs_rol_menu'  } }",
		"{ $unwind: { path: '$tevs_rol_menu', preserveNullAndEmptyArrays: true } }",
		"{ $lookup: { from: 'tevn_rol', localField: 'idRol', foreignField: 'idRol', as: 'tevn_rol'  } }",
		"{ $unwind: { path: '$tevn_rol', preserveNullAndEmptyArrays: true } }",
		"{ $lookup: { from: 'tevn_menu', localField: 'idMenu', foreignField: 'idMenu', as: 'tevn_menu'  } }",
		"{ $unwind: { path: '$tevn_menu', preserveNullAndEmptyArrays: true } }",
		"{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: 'idEstado', as: 'tevn_estado'  } }",
		"{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
		"{ $project: {	'tevs_rol_menu' : 1, "
		+ "				'tevn_rol' : 1, "
		+ "				'tevn_menu' : 1, "
		+ "				'tevn_estado' : 1 "
		+ " } }"
	})
	Flux<Document> findByRolMenuObject(String idRol);
	
	@Aggregation({
		"{ $match: { $and: [ "
							+ "    { $or: [ "
						    + "        { 'idRol' : { $regex: ?#{[0].get('idRol') == null ? '' : [0].get('idRol') }, $options: 'i' } }, "
						    + "        { 'idRol' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idMenu' : { $regex: ?#{[0].get('idMenu') == null ? '' : [0].get('idMenu') }, $options: 'i' } }, "
						    + "        { 'idMenu' : { $exists: false } }"
						    + "    ] }, "
						    + "    { $or: [ "
						    + "        { 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
						    + "        { 'idEstado' : { $exists: false } }"
						    + "    ] }, "
							+ " ] } "
		+ "}",
		"{ $lookup: { from: 'tevn_rol', localField: 'idRol', foreignField: 'idRol', as: 'tevn_rol'  } }",
		"{ $unwind: { path: '$tevn_rol', preserveNullAndEmptyArrays: true } }",
		"{ $lookup: { from: 'tevn_menu', localField: 'idMenu', foreignField: 'idMenu', as: 'tevn_menu'  } }",
		"{ $unwind: { path: '$tevn_menu', preserveNullAndEmptyArrays: true } }",
		"{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado'  } }",
		"{ $unwind: { path: '$tevn_estado', preserveNullAndEmptyArrays: true } }",
		"{ $project: {	'tevs_rol_menu' : '$$ROOT', "
		+ "				'tevn_rol' : 1, "
		+ "				'tevn_menu' : 1, "
		+ "				'tevn_estado' : 1 "
		+ " } }"
	})
	Flux<Document> findIfContains(Map<String, String> where);
}
