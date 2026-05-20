package com.enviexpres.logistica.admmodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevsDepartamento;

import reactor.core.publisher.Flux;

@Repository
public interface TevsDepartamentoRepository extends ReactiveMongoRepository<TevsDepartamento, String> {

	@Query("{ $and: ["
			+ "	{ $or: [ "
			+ "		{ 'idDepartamento' : { $regex: ?#{[0].get('idDepartamento') == null ? '' : [0].get('idDepartamento') }, $options: 'i' } }, "
			+ "		{ 'idDepartamento' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'nmDepartamento' : { $regex: ?#{[0].get('nmDepartamento') == null ? '' : [0].get('nmDepartamento') }, $options: 'i' } }, "
			+ "		{ 'nmDepartamento' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'sbDepartamento' : { $regex: ?#{[0].get('sbDepartamento') == null ? '' : [0].get('sbDepartamento') }, $options: 'i' } }, "
			+ "		{ 'sbDepartamento' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'codigoDane' : { $regex: ?#{[0].get('codigoDane') == null ? '' : [0].get('codigoDane') }, $options: 'i' } }, "
			+ "		{ 'codigoDane' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'codigoPostal' : { $regex: ?#{[0].get('codigoPostal') == null ? '' : [0].get('codigoPostal') }, $options: 'i' } }, "
			+ "		{ 'codigoPostal' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'idPais' : { $regex: ?#{[0].get('idPais') == null ? '' : [0].get('idPais') }, $options: 'i' } }, "
			+ "		{ 'idPais' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
			+ "		{ 'idEstado' : { $exists: false } }"
			+ "		] }, "
			+ " ] }")
	Flux<TevsDepartamento> findIfContains(Map<String, String> parameters);
	
	
	@Aggregation({
		"{ $match : { $and: ["
							+ "	{ $or: [ "
							+ "		{ 'idDepartamento' : { $regex: ?#{[0].get('idDepartamento') == null ? '' : [0].get('idDepartamento') }, $options: 'i' } }, "
							+ "		{ 'idDepartamento' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'nmDepartamento' : { $regex: ?#{[0].get('nmDepartamento') == null ? '' : [0].get('nmDepartamento') }, $options: 'i' } }, "
							+ "		{ 'nmDepartamento' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'sbDepartamento' : { $regex: ?#{[0].get('sbDepartamento') == null ? '' : [0].get('sbDepartamento') }, $options: 'i' } }, "
							+ "		{ 'sbDepartamento' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'codigoDane' : { $regex: ?#{[0].get('codigoDane') == null ? '' : [0].get('codigoDane') }, $options: 'i' } }, "
							+ "		{ 'codigoDane' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'codigoPostal' : { $regex: ?#{[0].get('codigoPostal') == null ? '' : [0].get('codigoPostal') }, $options: 'i' } }, "
							+ "		{ 'codigoPostal' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'idPais' : { $regex: ?#{[0].get('idPais') == null ? '' : [0].get('idPais') }, $options: 'i' } }, "
							+ "		{ 'idPais' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
							+ "		{ 'idEstado' : { $exists: false } }"
							+ "		] }, "
							+ " ] } "
		+ "}",
		"{ $lookup: { from: 'tvvs_departamento', localField: 'codigoDane', foreignField: 'codigoDane', as: 'tvvs_departamento' } }",
		"{ $unwind: { path: '$tvvs_departamento' } }",
		"{ $lookup: { from: 'tvvp_pais', localField: 'idPais', foreignField: '_id', as: 'tvvp_pais' } }",
		"{ $unwind: { path: '$tvvp_pais' } }",
		"{ $lookup: { from: 'tvvn_estado', localField: 'idEstado', foreignField: '_id', as: 'tvvn_estado' } }",
		"{ $unwind: { path: '$tvvn_estado' } }",
		"{ $project: { 'tvvs_departamento': 1, 'tvvp_pais': 2, 'tvvn_estado': 3 } }"
	})
	Flux<Document> findObjectIfContains(Map<String, String> parameters);
}
